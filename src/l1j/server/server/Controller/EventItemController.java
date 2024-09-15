package l1j.server.server.server.Controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class EventItemController implements Runnable {
	private static EventItemController _instance;
	private static final SimpleDateFormat _sdf = new SimpleDateFormat("HHmm");
	public static final int SleepTime = 1 * 60 * 1000; // 每1分鐘檢查一次

	public static EventItemController getInstance() {
		if (_instance == null) {
			_instance = new EventItemController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			checkEventItem();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Calendar getRealTime() {
		TimeZone _tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
		Calendar cal = Calendar.getInstance(_tz);
		return cal;
	}

	private void checkEventItem() {
		Calendar realTime = getRealTime();
		int nowTime = Integer.valueOf(_sdf.format(realTime.getTime()));
		int EventTime = Config.ServerRates.EventTime;
		int EventNumber = Config.ServerRates.EventNumber;
		int EventItem = Config.ServerRates.EventItem;
		if (EventTime == 0) return;

		if (nowTime % EventTime == 0) {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (!pc.isDead() && !pc.isPrivateShop() && !pc.noPlayerCK && pc != null) {
					L1ItemInstance item = pc.getInventory().storeItem(EventItem, EventNumber);
					if (item == null)continue;
					if (item != null)
						pc.sendPackets(new S_SystemMessage(item.getName() + " (" + EventNumber + ") 獲得"));
				}
			}
		} else {
			return;
		}
	}

}
