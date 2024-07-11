package l1j.server.MJTemplate.ObjectEvent;

import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJMonsterEventFactory {
	MJMonsterEventFactory(){
	}
	
	public MJAttrKey<MJObjectEventComposite<MJMonsterKillEventArgs>> monsterKillKey(){
		return monsterKillKey;
	}
	
	public void fireMonsterKill(L1Character latestAttacker, L1MonsterInstance monster){
		for(L1Character character : monster.getHateList().toTargetArrayList()){
			if(character == null || !(character instanceof L1PcInstance)){
				continue;
			}
			L1PcInstance pc = (L1PcInstance)character;
			MJMonsterKillEventArgs args = new MJMonsterKillEventArgs(latestAttacker, monster);
			pc.eventHandler().fire(monsterKillKey(), args);
		}
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJMonsterKillEventArgs>> monsterKillKey = MJAttrKey.newInstance("mj-monster-kill");
	public static class MJMonsterKillEventArgs extends MJObjectEventArgs{
		public final L1Character latestAttacker;
		public final L1MonsterInstance monster;
		private MJMonsterKillEventArgs(L1Character latestAttacker, L1MonsterInstance monster){
			super();
			this.latestAttacker = latestAttacker;
			this.monster = monster;
		}
	}
}
