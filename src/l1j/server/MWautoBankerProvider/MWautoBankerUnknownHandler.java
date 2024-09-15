package l1j.server.MWautoBankerProvider;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1PcInstance;

public class MWautoBankerUnknownHandler {
	
	public MWautoBankerUnknownHandler(){
	}
	
//	public synchronized void giveunknown(L1PcInstance pc, String param, String arg) {
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		MWautoBankerController MwB = MWautoBankerController.getInstance();
//		try {
//			StringTokenizer st = new StringTokenizer(arg);
//			String name = st.nextToken();
//			int count = 0;
//			String won = null;
//			if (st.hasMoreTokens()) {
//				won = st.nextToken();
//				int idx = won.indexOf("元");
//		        String won1 = won.substring(0, idx);
//				count = Integer.parseInt(won1);
//			}
//			String year = null;
//			if (st.hasMoreTokens()) {
//				year = st.nextToken();
//			}
//            String moon = year.replace('年', '-');
//            String day = moon.replace('月', '-');
//            String hour = day.replace('日', ' ');
//            String min = hour.replace('時', ':');
//            String sec = min.replace('分', ':');
//            String time = sec.replace('秒', ' ');
//			MWautoBankerDataTable banker = new MWautoBankerDataTable();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			java.util.Date date = sdf.parse(time);
//			java.sql.Timestamp t = new java.sql.Timestamp(date.getTime());
//			pc.set_lastunknown_count(pc.get_lastunknown_count() + 1);
//			if(pc.get_lastunknown_count() >= 3) {
//				System.out.println("pc.get_lastunknown_count()" + pc.get_lastunknown_count());
//				pc.set_lastunknownbankerTime(timestamp);
//			}
//			if (pc.get_lastunknownbankerTime().getTime() + 10 * 60 * 1000 > System.currentTimeMillis()) {
//				int remaintime = (int)((pc.get_lastunknownbankerTime().getTime() + 5 * 60 * 1000 - System.currentTimeMillis())/1000);
//				System.out.println(pc.get_lastunknownbankerTime().getTime() + 10 * 60 * 1000);
//				System.out.println(System.currentTimeMillis());
//				pc.sendPackets(remaintime + "分鐘後請再試一次。");
//				return;
//			}
//			if(!banker.UnknownDepositor(pc, name, count, t)) {
//				pc.sendPackets("存款信息有誤\n 請重新確認存款人姓名、金額和日期");
//				return;
//			}
//			int givecount = MwB.MWautoInfo.NcoinService ? count + (int)(count * (MwB.MWautoInfo.ServicePer / 100)): count;;
//			pc.sendPackets("該存款記錄已確認。");
//			switch(MwB.MWautoInfo.EctItemService){
//			case 1:
//				pc.getInventory().storeItem(MwB.MWautoInfo.ServiceItem1, count >= 10000 ? (int)(MwB.MWautoInfo.ServiceItem1count * count / 10000) : 1);
//				break;
//			case 2:
//				pc.getInventory().storeItem(MwB.MWautoInfo.ServiceItem1, count >= 10000 ? (int)(MwB.MWautoInfo.ServiceItem1count * count / 10000) : 1);
//				pc.getInventory().storeItem(MwB.MWautoInfo.ServiceItem2, count >= 10000 ? (int)(MwB.MWautoInfo.ServiceItem1count * count / 10000) : 1);
//				break;
//			case 3:
//				pc.getInventory().storeItem(MwB.MWautoInfo.ServiceItem1, count >= 10000 ? (int)(MwB.MWautoInfo.ServiceItem1count * count / 10000) : 1);
//				pc.getInventory().storeItem(MwB.MWautoInfo.ServiceItem2, count >= 10000 ? (int)(MwB.MWautoInfo.ServiceItem1count * count / 10000) : 1);
//				pc.getInventory().storeItem(MwB.MWautoInfo.ServiceItem3, count >= 10000 ? (int)(MwB.MWautoInfo.ServiceItem1count * count / 10000) : 1);
//				break;
//			default:
//				break;
//			}
//			pc.getNetConnection().getAccount().Ncoin_point += givecount;
//			pc.getNetConnection().getAccount().updateNcoin();
//			banker.PaidUnknownNcoin(pc, name);
//			banker.DepositorInfoLog(pc, name, count, t);
//			banker = null;
//			sdf = null;
//			st = null;
//			timestamp = null;
//		} catch (Exception e) {
//			pc.sendPackets(".存款確認 存款人姓名 存款金額 存款日期"); "
//					+"
//		例).存款確認 Tae Jin Ah 100000韓元 2020年1月8日0時38分28秒"
////                    + "\n如上例所示，存款日期需輸入到年、月、日、時、分、秒"
////                    + "\n此命令是為了存款時關鍵字不匹配的用戶"
////                    + "\n失敗超過3次後可在10分鐘後再試，目前計數器為"+" "+pc.get_lastunknown_count()+"次");
//		}	
//	}
}
