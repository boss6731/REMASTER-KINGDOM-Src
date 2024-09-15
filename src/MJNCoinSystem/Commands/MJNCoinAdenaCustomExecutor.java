package MJNCoinSystem.Commands;

import java.sql.PreparedStatement;

import MJNCoinSystem.MJNCoinAdenaInfo;
import MJNCoinSystem.MJNCoinAdenaManager;
import MJNCoinSystem.MJNCoinCharacterReport;
import MJNCoinSystem.MJNCoinIdFactory;
import MJNCoinSystem.MJNCoinSettings;
import l1j.server.MJSurveySystem.MJInterfaceSurvey;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJNCoinAdenaCustomExecutor extends MJNCoinExecutor{
	@Override
	public void execute(MJCommandArgs args) {
		try{
			L1PcInstance pc = args.getOwner();
			if(pc == null)
				return;
			
			Account account = pc.getAccount();
			if(account == null)
				return;

			int trade_id = args.nextInt();

			MJNCoinAdenaInfo aInfo = MJNCoinAdenaInfo.from_trade_id(trade_id);
			if (aInfo == null) {
				args.notify(String.format("找不到交易編號為 %d 的信息。", trade_id));
				return;
			}
			if (!MJString.isNullOrEmpty(aInfo.get_complete_date())) {
				args.notify("該物品的交易已完成。");
				return;
			}
			do_survey(args, pc, account, trade_id, aInfo.get_adena_amount(), aInfo.get_ncoin_value());
		} catch(Exception e) {
			args.notify(".購買申請 [帖子編號]");
			args.notify("例如) .購買申請 0017");
		}

		@override
		public String get_command_name() {
			return "購買申請";
		}

		private void do_survey(final MJCommandArgs args, final L1PcInstance pc, final Account account, final int trade_id, final int adena_amount, final int ncoin_value) {
			ServerBasePacket pck = MJSurveySystemLoader.getInstance().registerSurvey(
					String.format("(%,d萬金幣)總共消耗 %,dN幣。是否繼續？", adena_amount / 10000, ncoin_value),
					pc.getId(),
					new MJInterfaceSurvey() {
						@override
						public void survey(L1PcInstance pc, int num, boolean isYes) {
							if (!isYes) {
								pc.sendPackets("你已取消購買申請。");
								return;
							}
						}
					}
			);
		}
		try {
			MJNCoinAdenaInfo aInfo = null;
			String current_date = MJString.EmptyString;

			Object sync = MJNCoinIdFactory.ADENA.get_sync_object(trade_id);
			synchronized(sync) {
				aInfo = MJNCoinAdenaInfo.from_trade_id(trade_id);
				if (aInfo == null) {
					args.notify(String.format("找不到交易編號為 %d 的信息。", trade_id));
					return;
				}

				if (!MJString.isNullOrEmpty(aInfo.get_complete_date())) {
					args.notify("該物品的交易已完成。");
					return;
				}

				if (account.Ncoin_point < aInfo.get_ncoin_value()) {
					pc.sendPackets(String.format("N幣 %d 元不足。", aInfo.get_ncoin_value()));
					return;
				}
						current_date = MJString.get_current_datetime();
						if(pc.isGm()) {
							int ncoin_amount = aInfo.get_ncoin_value();
							int commission_ncoin = (int)(ncoin_amount * MJNCoinSettings.GM_COMMISSION);
							aInfo
							.set_complete_date(current_date)
							.set_customer_id(pc.getId())
							.set_customer_name(pc.getName())
							.set_commission(MJNCoinSettings.GM_COMMISSION)
							.set_commission_ncoin(commission_ncoin)
							.set_drain_ncoin(ncoin_amount - commission_ncoin);
							MJNCoinAdenaInfo.update_customer_for_gm(aInfo);
						}else {
							aInfo
							.set_complete_date(current_date)
							.set_customer_id(pc.getId())
							.set_customer_name(pc.getName());
							MJNCoinAdenaInfo.update_customer(aInfo);	
						}
					}
					MJNCoinCharacterReport
					.getInstance()
					.get_character_info_notfound_created(pc.getId(), pc.getName())
					.add_buy_price(aInfo.get_ncoin_value())
					.do_update();
					account.Ncoin_point -= aInfo.get_ncoin_value();
					account.updateNcoin();
					
					L1ItemInstance adena = ItemTable.getInstance().createItem(L1ItemId.ADENA);
					adena.setCount(aInfo.get_adena_amount());
					SupplementaryService service = WarehouseManager.getInstance().getSupplementaryService(account.getName());
					service.storeTradeItem(adena);
					
					L1PcInstance owner = L1World.getInstance().getPlayer(aInfo.get_character_name());
					if(owner == null){
						final MJNCoinAdenaInfo a = aInfo;
						Updator.exec("update accounts set Ncoin_Point=Ncoin_Point+? where login=?", new Handler(){
							@Override
							public void handle(PreparedStatement pstm) throws Exception {
								pstm.setInt(1, a.get_drain_ncoin());
								pstm.setString(2, a.get_account_name());
							}
						});
					}else{
						owner.getAccount().Ncoin_point += aInfo.get_drain_ncoin();
						owner.getAccount().updateNcoin();
						owner.send_effect(2048);
					}
					MJNCoinCharacterReport
					.getInstance()
					.get_character_info_notfound_created(aInfo.get_character_object_id(), aInfo.get_character_name())
					.add_selling_price(aInfo.get_ncoin_value())
					.do_update();


			MJNCoinAdenaManager.DEFAULT.complete_adena_trade(aInfo);
			do_write_letter(pc.getName(), current_date, "[購買通知] 購買已完成。", aInfo.toString());
			do_write_letter(aInfo.get_character_name(), current_date, "[銷售通知] 銷售已完成。", aInfo.to_provider());
		} catch(Exception e) {
			args.notify(".購買申請 [帖子編號]");
			args.notify("例如) .購買申請 0017");
		}

	}, 10000L);
	if(pck == null) {
		pc.sendPackets("請於10秒後再使用。");

	}else {

		pc.sendPackets(pck);

	}

}




