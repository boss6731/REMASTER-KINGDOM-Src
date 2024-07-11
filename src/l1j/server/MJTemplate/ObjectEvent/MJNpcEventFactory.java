package l1j.server.MJTemplate.ObjectEvent;

import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJNpcEventFactory {
	MJNpcEventFactory(){	
	}
	
	public MJAttrKey<MJObjectEventComposite<MJNpcTalkEventArgs>> npcTalkKey(){
		return npcTalkKey;
	}
	
	public void fireNpcTalked(L1PcInstance pc, L1NpcInstance npc, String action){
		pc.eventHandler().fire(npcTalkKey(), new MJNpcTalkEventArgs(pc, npc, action));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJNpcTalkEventArgs>> npcTalkKey = MJAttrKey.newInstance("mj-npc-talk");
	public static class MJNpcTalkEventArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		public final L1NpcInstance npc;
		public final String action;
		private MJNpcTalkEventArgs(L1PcInstance pc, L1NpcInstance npc, String action){
			this.pc = pc;
			this.npc = npc;
			this.action = action;
		}
	}
}
