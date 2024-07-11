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
//				int idx = won.indexOf("원");
//		        String won1 = won.substring(0, idx);
//				count = Integer.parseInt(won1);
//			}
//			String year = null;
//			if (st.hasMoreTokens()) {
//				year = st.nextToken();
//			}
//			String moon = year.replace('년', '-');
//			String day = moon.replace('월', '-');
//			String hour = day.replace('일', ' ');
//			String min = hour.replace('시', ':');
//			String sec = min.replace('분', ':');
//			String time = sec.replace('초', ' ');
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
//				pc.sendPackets(remaintime+"분뒤 다시 시도해주세요.");
//				return;
//			}
//			if(!banker.UnknownDepositor(pc, name, count, t)) {
//				pc.sendPackets("입금정보가 잘못 되었습니다 \n입금자명 금액 날짜를 다시 확인해주세요");
//				return;
//			}
//			int givecount = MwB.MWautoInfo.NcoinService ? count + (int)(count * (MwB.MWautoInfo.ServicePer / 100)): count;;
//			pc.sendPackets("해당 입금 내역이 확인 되었습니다.");
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
//			pc.sendPackets(".입금확인 입금자명 입금액 입금날짜 "
//					+ "\nex).입금확인 태진아 100000원 2020년1월8월0시38분28초"
//					+ "\n위 예제와 마찬가지로 입금날짜는 년,월,일,시,분,초까지 다 입력"
//					+ "\n해당 커멘드는 신청시 키워드와 입금시 키워드가 맞지 않게 입금한 유저를 위함"
//					+ "\n3회 이상 실패시 10분뒤 시도가 가능 현재 카운터"+" "+pc.get_lastunknown_count()+"번");
//		}	
//	}
}
