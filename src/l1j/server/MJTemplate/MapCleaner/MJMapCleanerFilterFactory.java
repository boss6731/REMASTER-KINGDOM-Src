package l1j.server.MJTemplate.MapCleaner;

import l1j.server.MJTemplate.MJL1Type;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJMapCleanerFilterFactory {
	
	public static MJMapCleanerFilter createDefaultFilter(){
		int flg = MJL1Type.L1TYPE_SUMMON | MJL1Type.L1TYPE_DOLL;
		return new MJMapCleanerFilter(){
			@Override
			public boolean isFilter(L1Object obj){
				if(obj == null || obj.instanceOf(flg))
					return true;
				
				if(obj.instanceOf(MJL1Type.L1TYPE_PC)){
					L1PcInstance pc = (L1PcInstance)obj;
					if(!pc.isDead())
						pc.start_teleport(33445, 32794, 4, pc.getHeading(), 18339, true, true);
					return true;
				}
				return false;
			}
		};
	}
	
	public static MJMapCleanerFilter createBattleHunterFilter(){
		int flg = MJL1Type.L1TYPE_SUMMON | MJL1Type.L1TYPE_DOLL;
		return new MJMapCleanerFilter(){
			@Override
			public boolean isFilter(L1Object obj){
				if(obj == null || obj.instanceOf(flg))
					return true;
				
				return false;
			}
		};
	}
}
