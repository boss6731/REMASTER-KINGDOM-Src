package l1j.server.Payment;

import java.text.DecimalFormat;

import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJSurveySystem.MJInterfaceSurvey;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJPaymentUserHandler implements MJCommand{
	public static void do_execute(MJCommandArgs args){
		new MJPaymentUserHandler().execute(args);
	}
	
	private MJPaymentInfo m_pInfo;
	private MJPaymentUserHandler(){}
	@Override
	public void execute(MJCommandArgs args) {
		try{
			String code = args.nextString();
			String name = args.nextString();
			if (l1j.server.MJTemplate.MJString.isNullOrEmpty(code) || code.length() < 4) {
				args.notify("充值代碼不能為空，且必須至少輸入4位數。");
				throw new Exception();
			}
			if (l1j.server.MJTemplate.MJString.isNullOrEmpty(name)) {
				args.notify("存款人名不能為空。");
				throw new Exception();
			}

			m_pInfo = MJPaymentInfo.newInstance(code.toUpperCase());
			if (m_pInfo == null) {
				args.notify(String.format("代碼充值：%s，存款人名：%s 的信息不存在。", code, name));
				return;
			}
			if (m_pInfo.get_is_use()) {
				args.notify(String.format("該代碼已被 %s 角色領取。", m_pInfo.get_character_name()));
				return;
			}
			args.getOwner().sendPackets(MJSurveySystemLoader.getInstance().registerSurvey("是否要使用該代碼進行充值？（請在15秒內選擇）", args.getOwner().getId(), new MJInterfaceSurvey(){
				@Override
				public void survey(L1PcInstance pc, int num, boolean isYes) {
					if (!isYes) {
						pc.sendPackets("您已取消代碼充值。");
						return;
					}
					m_pInfo
					.set_account_name(pc.getAccountName())
					.set_character_name(pc.getName())
					.set_expire_date(MJNSHandler.getLocalTime())
					.set_is_use(true)
					.do_update();
					pc.getInventory().storeItem(m_pInfo.get_itemid(), m_pInfo.get_count());
					pc.sendPackets(String.format("已發放 (%s) 個優惠券物品。", new DecimalFormat("#,##0").format(m_pInfo.get_count())));
				}
			}, 15000L));
		} catch (Exception e) {
			args.notify(".代碼充值 [代碼] [存款人名]");
		}
	}
}
