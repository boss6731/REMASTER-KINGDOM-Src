package l1j.server.MJTemplate.Regen;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJUnderWaterLooper implements Runnable {
	private static final long LOOP_DELAY_MILLIS = 3000L; // 循環延遲時間，單位為毫秒（3秒）
	// private static final int UNDER_WATER_DAMAGE = 20;
	private static final int UNDER_WATER_DAMAGE = 200; // 2022年伊娃王國更新時提升的水下傷害值

	private static MJUnderWaterLooper _instance;

	// 獲取單例實例的方法
	public static MJUnderWaterLooper getInstance() {
		if (_instance == null) {
			_instance = new MJUnderWaterLooper();
		}
		return _instance;
	}

	// 此處可添加 run 方法實現循環邏輯
	@override
	public void run() {
// 這裡實現水下循環邏輯
	}
}
	
	private MJUnderWaterLooper() {
		GeneralThreadPool.getInstance().schedule(this, LOOP_DELAY_MILLIS);
		
	}

	@Override
	public void run() {
		try {
			for(L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if(pc == null || pc.getNetConnection() == null || !pc.getNetConnection().isConnected())
					continue;
				
				if(pc.isDead())
					continue;
				
				if(!MJReGenerator.is_under_water(pc))
					continue;
				
				int new_hp = Math.max(pc.getCurrentHp() - UNDER_WATER_DAMAGE, pc.isGm() ? 1 : 0);
				if(new_hp <= 0)
					pc.death(null, true);
				else
					pc.setCurrentHp(new_hp);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		GeneralThreadPool.getInstance().schedule(this, LOOP_DELAY_MILLIS);
	}
}
