package l1j.server.CPMWBQSystem.RenwController;

import java.util.Date;
import l1j.server.CPMWBQSystem.CPMWBQSystemProvider;
import l1j.server.CPMWBQSystem.Database.CPMWBQUserTable;
import l1j.server.CPMWBQSystem.info.CPMWBQReward;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class CPMWRenewController implements Runnable {
	private static CPMWRenewController _instance;

	public static CPMWRenewController getInstance() {
		if (_instance == null) {
			_instance = new CPMWRenewController();
		}
		return _instance;
	}

	private Date day = new Date(System.currentTimeMillis());

	public CPMWRenewController() {
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	public void run() {
		try {
			day.setTime(System.currentTimeMillis());
			initBQUser();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		GeneralThreadPool.getInstance().schedule(this, 60 * 1000);
	}

	@SuppressWarnings("deprecation")
	public void initBQUser() {
		try {
			if (day.getHours() == CPMWBQReward.service().gethour()
					&& day.getMinutes() == CPMWBQReward.service().getminiute() && CPMWBQReward.service().use()) {
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					CPMWBQUserTable userinfo = CPMWBQUserTable.getInstance();
					userinfo.InnituserInfo();
					if (pc.Get_BQ_Info() != null) {
						pc.Get_BQ_Info().clear();
					}
					CPMWBQSystemProvider.provider().BQload(pc);
				}
				L1World.getInstance().broadcastServerMessage(String.format("\\aH[怪物圖鑑重置通知](每天%s時%s分重置怪物圖鑑)",
						CPMWBQReward.service().gethour(), CPMWBQReward.service().getminiute()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}