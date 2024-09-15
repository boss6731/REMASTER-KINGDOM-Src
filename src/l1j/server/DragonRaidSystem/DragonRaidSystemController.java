package l1j.server.server.DragonRaidSystem;

import l1j.server.Config;
import l1j.server.DragonRaidSystem.DragonController.RaidOfAntaras;
import l1j.server.DragonRaidSystem.DragonController.RaidOfFafurion;
import l1j.server.DragonRaidSystem.DragonController.RaidOfLindvior;
import l1j.server.DragonRaidSystem.DragonController.RaidOfValakas;
import l1j.server.DragonRaidSystem.DragonRaidSystemLoader;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.GeneralThreadPool;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class DragonRaidSystemController implements Runnable {
	private static DragonRaidSystemController instance;

	public static DragonRaidSystemController getInstance() {
		if (instance == null) {
			instance = new DragonRaidSystemController();
			GeneralThreadPool.getInstance().execute(instance);
		}
		return instance;
	}

	private void isSpawnTime() {
		try {
			List<DragonRaidSystemInfo> list = DragonRaidSystemLoader.getList();
			if (list.size() > 0) {
				Calendar oCalendar = Calendar.getInstance();
				int hour = oCalendar.get(Calendar.HOUR_OF_DAY);
				int minute = oCalendar.get(Calendar.MINUTE);
				int second = oCalendar.get(Calendar.SECOND);
				int rnd = MJRnd.next(4) + 1;
				DragonRaidSystemInfo info = DragonRaidSystemLoader.find(rnd);
				if (Config.ServerAdSetting.DragonRaidRandom) {
					if (info != null) {
						if (dayCk(info.getSpawnDay())) {
							if (info.isSpawnTime(hour, minute, oCalendar) && second == 0) {
								if (info.getBossNum() == 1) {
									RaidOfAntaras anta = new RaidOfAntaras();
									anta.Start();
								} else if (info.getBossNum() == 2) {
									RaidOfFafurion fafu = new RaidOfFafurion();
									fafu.Start();
								} else if (info.getBossNum() == 3) {
									RaidOfLindvior lind = new RaidOfLindvior();
									lind.Start();
								} else if (info.getBossNum() == 4) {
									RaidOfValakas vala = new RaidOfValakas();
									vala.Start();
								}
							}
						}
					}
				} else {

					for (DragonRaidSystemInfo b : list) {
						if (dayCk(b.getSpawnDay())) {
							if (b.isSpawnTime(hour, minute, oCalendar) && second == 0) {
								// 指定時間開啟時
								if (b.getBossNum() == 1) {
									RaidOfAntaras anta = new RaidOfAntaras();
									anta.Start();
								} else if (b.getBossNum() == 2) {
									RaidOfFafurion fafu = new RaidOfFafurion();
									fafu.Start();
								} else if (b.getBossNum() == 3) {
									RaidOfLindvior lind = new RaidOfLindvior();
									lind.Start();
								} else if (b.getBossNum() == 4) {
									RaidOfValakas vala = new RaidOfValakas();
									vala.Start();
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean dayCk(String days) {
		StringTokenizer st = new StringTokenizer(days, ",");
		Calendar calendar = Calendar.getInstance();
		// 1星期日 2星期一 3星期二 4星期三 5星期四 6星期五 7星期六
		int nowDay = calendar.get(Calendar.DAY_OF_WEEK);
		while (st.hasMoreTokens()) {
			int ckDay = Integer.parseInt(st.nextToken().trim());
			if (nowDay == ckDay) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void run() {
		try {
			while (true) {
				isSpawnTime();
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return new l1j.server.server.model.Instance.L1PcInstance[0];
    }
}