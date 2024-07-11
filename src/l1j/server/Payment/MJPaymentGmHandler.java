package l1j.server.Payment;

import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;

public class MJPaymentGmHandler implements MJCommand{
	public static void do_execute(MJCommandArgs args){
		new MJPaymentGmHandler().execute(args);
	}
	
	private MJPaymentGmHandler(){}
	@Override
	public void execute(MJCommandArgs args) {
		try {
			int itemid = args.nextInt();
			if (itemid <= 0) {
				args.notify("請輸入有效的物品號碼。");
				throw new Exception();
			}
			int count = args.nextInt();
			if (count <= 0) {
				args.notify("物品數量應大於或等於1。");
				throw new Exception();
			}
			int num = args.nextInt();
			if (num <= 0) {
				args.notify("請輸入要生成的數量。");
				throw new Exception();
			}
			for (int i = 1; i <= num; i++) {
				MJPaymentInfo pInfo = MJPaymentInfo.newInstance(itemid, count);
				args.notify(String.format("\aH[發放]優惠券 : %s, 物品號碼 : %s, 數量 : %s, 要生成的數量 : %d", pInfo.get_code(), pInfo.get_itemid(), pInfo.get_count(), num));
			}
		} catch (Exception e) {
			args.notify(".優惠券 [物品號碼] [數量] [生成數量]");
		}
	}


