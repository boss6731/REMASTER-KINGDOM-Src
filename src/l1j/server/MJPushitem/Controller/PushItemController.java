package l1j.server.MJPushitem.Controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import l1j.server.MJPushitem.MJPushProvider;
import l1j.server.MJPushitem.dataloader.MJPushItemData;
import l1j.server.MJPushitem.model.MJItemPushModel;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class PushItemController implements Runnable {

	private static PushItemController _instance;

	private static boolean PUSH_ON_OFF = true;

	public static PushItemController getInstance() {
		if (_instance == null) {
			_instance = new PushItemController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (PUSH_ON_OFF) {
					pushItemResult();
				}
				Thread.sleep(60*1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setPushSystem(boolean flag){
		PUSH_ON_OFF = flag;
	}
	
	public static boolean isPushSystem(){
		return PUSH_ON_OFF;
	}
	
	private void result(MJItemPushModel push_list) {
		try {
			if (push_list == null)
				return;

			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null)
					continue;
				if (!pc.isPacketSendOK())
					continue;
				
				if(push_list.getMinlevel() > pc.getLevel() || push_list.getMaxlevel() < pc.getLevel()) {
					continue;
				}

				MJPushProvider.provider().messagenew(pc, push_list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pushItemResult() {
		try {
			MJItemPushModel push_list = null;
			Set<Integer> keys = MJPushItemData.getlist().keySet();
			int pushid = 0;
			for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
				pushid = iterator.next();
				if (checkTime(MJPushItemData.getlist().get(pushid).getEnabledate())) {
					push_list = MJPushItemData.getlist().get(pushid);
					if (push_list != null) {
						result(push_list);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean checkTime(long date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String time = formatter.format(date * 1000);
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		String[] real_time = time.split(":");
		if (cal.get(Calendar.HOUR_OF_DAY) == Integer.valueOf(real_time[0]) && cal.get(Calendar.MINUTE) == Integer.valueOf(real_time[1])) {
			return true;
		}
		return false;
	}
}
