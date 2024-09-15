package l1j.server.MJTemplate.Regen;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJUnderWaterLooper implements Runnable{
	private static final long LOOP_DELAY_MILLIS = 3000L;
//	private static final int UNDER_WATER_DAMAGE = 20;
	private static final int UNDER_WATER_DAMAGE = 200;//22�� ���ٿձ� ������� ����
	
	private static MJUnderWaterLooper _instance;
	public static MJUnderWaterLooper getInstance() {
		if(_instance == null) {
			_instance = new MJUnderWaterLooper();
		}
		return _instance;
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
        return new L1PcInstance[0];
    }
}
