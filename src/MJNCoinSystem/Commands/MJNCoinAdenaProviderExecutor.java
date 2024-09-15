package MJNCoinSystem.Commands;

import MJNCoinSystem.MJNCoinAdenaInfo;
import MJNCoinSystem.MJNCoinCharacterReport;
import MJNCoinSystem.MJNCoinCreditLoader;
import MJNCoinSystem.MJNCoinCreditLoader.MJNCoinCreditInfo;
import MJNCoinSystem.MJNCoinCharacterReport.MJNCoinCharacterInfo;
import MJNCoinSystem.MJNCoinIdFactory;
import MJNCoinSystem.MJNCoinSettings;
import l1j.server.MJSurveySystem.MJInterfaceSurvey;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJNCoinAdenaProviderExecutor extends MJNCoinExecutor {

	@Override
	public void execute(MJCommandArgs args) {
		try {
			L1PcInstance pc = args.getOwner();
			if (pc == null)
				return;

			Account account = pc.getAccount();
			if (account == null)
				return;

			int adena_amount = args.nextInt();
			int ncoin_amount = args.nextInt();

			if (!pc.getInventory().checkItem(L1ItemId.ADENA)) {
				args.notify("金幣不足。");
				return;
			}

			if (adena_amount < MJNCoinSettings.ADENA_GENERATE_UNIT) {
				args.notify(String.format("銷售登記最低為 %,d 元起，並以 %,d 元為單位。", MJNCoinSettings.ADENA_GENERATE_UNIT, MJNCoinSettings.ADENA_GENERATE_UNIT));
				return;
			}
			if (adena_amount > MJNCoinSettings.ADENA_GENERATE_UNIT * MJNCoinSettings.ADENA_GENERATE_MAX) {
				args.notify(String.format("銷售登記一次最多為 %,d 元。", MJNCoinSettings.ADENA_GENERATE_UNIT * MJNCoinSettings.ADENA_GENERATE_MAX));
				return;
			}
			adena_amount -= (adena_amount % MJNCoinSettings.ADENA_GENERATE_UNIT);
			int amount = adena_amount / MJNCoinSettings.ADENA_GENERATE_UNIT;
			int ncoin_per_adena = ncoin_amount / amount;
			if (ncoin_per_adena < MJNCoinSettings.ADENA_MARKET_PRICE) {
				args.notify(String.format("請按照最少每 %,d 元的標準 %,d 元設定。", MJNCoinSettings.ADENA_GENERATE_UNIT, MJNCoinSettings.ADENA_MARKET_PRICE));
				return;
			}

			if (!pc.getInventory().checkItem(L1ItemId.ADENA, adena_amount)) {
				args.notify("金幣不足。");
				return;
			}
			do_survey(pc, ncoin_amount, adena_amount, ncoin_per_adena);
		} catch (Exception e) {
			args.notify(".銷售登記 [金幣] [銷售金額]");
			MJNCoinCharacterInfo cInfo = MJNCoinCharacterReport.getInstance().get_character_info(args.getOwner().getId());
			MJNCoinCreditInfo credit = MJNCoinCreditLoader.getInstance().select_selling_info(cInfo != null ? cInfo.get_selling_price() : 0L);
			args.notify(String.format("注意)銷售的金額將扣除 %d%% 的手續費。", (int) (credit.get_commission() * 100)));
		}

		@override
		public String get_command_name () {
			return "銷售登記";
		}

		private void do_survey ( final L1PcInstance pc, final int ncoin_amount, final int adena_amount,
		final int ncoin_per_adena){
			ServerBasePacket pck = MJSurveySystemLoader.getInstance().register
			Survey(String.format("%,d萬 金幣將被登記。是否繼續？", adena_amount / 10000), pc.getId(), new MJInterfaceSurvey() {
				@override
				public void survey(L1PcInstance pc, int num, boolean isYes) {
					if (!isYes) {
						pc.sendPackets("您已取消銷售登記。");
						return;
					}
					try {
						if (!pc.getInventory().consumeItem(L1ItemId.ADENA, adena_amount)) {
							pc.sendPackets("金幣不足。");
							return;
						}


						String current_date = MJString.get_current_datetime();
						MJNCoinCharacterInfo cInfo = MJNCoinCharacterReport.getInstance().get_character_info(pc.getId());
						MJNCoinCreditInfo credit = MJNCoinCreditLoader.getInstance().select_selling_info(cInfo != null ? cInfo.get_selling_price() : 0L);
						int commission_ncoin = (int) (ncoin_amount * credit.get_commission());
						int trade_id = MJNCoinIdFactory.ADENA.next_id();
						Object sync = MJNCoinIdFactory.ADENA.get_sync_object(trade_id);
						MJNCoinAdenaInfo aInfo = null;
						synchronized (sync) {
							aInfo = MJNCoinAdenaInfo.newInstance()
									.set_trade_id(trade_id)
									.set_character_object_id(pc.getId())
									.set_character_name(pc.getName())
									.set_account_name(pc.getAccountName())
									.set_adena_amount(adena_amount)
									.set_ncoin_value(ncoin_amount)
									.set_commission(credit.get_commission())
									.set_commission_ncoin(commission_ncoin)
									.set_drain_ncoin(ncoin_amount - commission_ncoin)
									.set_ncoin_per_adena(ncoin_per_adena)
									.set_ncoin_per_adena_unit(MJNCoinSettings.ADENA_GENERATE_UNIT)
									.set_generate_date(current_date);
							MJNCoinAdenaInfo.do_store(aInfo);
						}

						pc.sendPackets("銷售登記已完成。");
						do_write_letter(pc.getName(), current_date, "[銷售登記通知]", aInfo.to_provider());
					} catch (Exception e) {
						pc.sendPackets(".銷售登記 [金幣] [銷售金額]");
						MJNCoinCharacterInfo cInfo = MJNCoinCharacterReport.getInstance().get_character_info(pc.getId());
						MJNCoinCreditInfo credit = MJNCoinCreditLoader.getInstance().select_selling_info(cInfo != null ? cInfo.get_selling_price() : 0L);
						pc.sendPackets(String.format("注意)銷售金額的 %d%% 將作為手續費扣除。", (int) (credit.get_commission() * 100)));
					}
				}
			}, 10000L);
			if (pck == null) {
				pc.sendPackets("請於10秒後再使用。");
			} else {
				pc.sendPackets(pck);
			}

		}
	}
}