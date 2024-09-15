package l1j.server.MJTemplate.ObjectEvent;

import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJPcEventFactory {
	MJPcEventFactory(){
	}
	
	
	
	
	/**
	 * 퀘스트 완료 시
	 **/
	public MJAttrKey<MJObjectEventComposite<MJPcQuestFinishedArgs>> pcQuestFinishedKey(){
		return pcQuestFinishedKey;
	}
	
	public void fireQuestFinished(L1PcInstance pc, int questId){
		pc.eventHandler().fire(pcQuestFinishedKey(), new MJPcQuestFinishedArgs(pc, questId));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJPcQuestFinishedArgs>> pcQuestFinishedKey = MJAttrKey.newInstance("mj-pc-quest-finished");
	public static class MJPcQuestFinishedArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		public final int questId;
		private MJPcQuestFinishedArgs(L1PcInstance pc, int questId){
			this.pc = pc;
			this.questId = questId;
		}
	}
	
	
	
	
	/**
	 * 레벨 변동 시
	 **/
	public MJAttrKey<MJObjectEventComposite<MJPcLevelChangedArgs>> pcLevelChangedKey(){
		return pcLevelChangedKey;
	}
	
	public void fireLevelChanged(L1PcInstance pc){
		pc.eventHandler().fire(pcLevelChangedKey(), new MJPcLevelChangedArgs(pc));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJPcLevelChangedArgs>> pcLevelChangedKey = MJAttrKey.newInstance("mj-pc-level-changed");
	public static class MJPcLevelChangedArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		private MJPcLevelChangedArgs(L1PcInstance pc){
			super();
			this.pc = pc;
		}
	}
	
	
	
	
	/**
	 * 혈맹 변동 시
	 **/
	public MJAttrKey<MJObjectEventComposite<MJPcPledgeChangedArgs>> pcPledgeChangedKey(){
		return pcPledgeChangedKey;
	}
	
	public void firePledgeChanged(L1PcInstance pc, String currentPledge, String previousPledge){
		pc.eventHandler().fire(pcPledgeChangedKey(), new MJPcPledgeChangedArgs(pc, currentPledge, previousPledge));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJPcPledgeChangedArgs>> pcPledgeChangedKey = MJAttrKey.newInstance("mj-pc-pledge-changed");
	public static class MJPcPledgeChangedArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		public final String currentPledge;
		public final String previousPledge;
		private MJPcPledgeChangedArgs(L1PcInstance pc, String currentPledge, String previousPledge){
			super();
			this.pc = pc;
			this.currentPledge = currentPledge;
			this.previousPledge = previousPledge;
		}
	}
	
	
	
	
	/**
	 * pss 시작 시
	 **/
	public MJAttrKey<MJObjectEventComposite<MJPcPssStartedArgs>> pcPssStartedKey(){
		return pcPssStartedKey;
	}
	
	public void firePssStarted(L1PcInstance pc){
		pc.eventHandler().fire(pcPssStartedKey(), new MJPcPssStartedArgs(pc));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJPcPssStartedArgs>> pcPssStartedKey = MJAttrKey.newInstance("mj-pc-pss-started");

	public static class MJPcPssStartedArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		private MJPcPssStartedArgs(L1PcInstance pc){
			this.pc = pc;
		}
	}
	
	
	/**
	 * 도감 추가시
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcCPMWBQAddedKeyArgs>> pcCPMWBQAddedKey(){
//		System.out.println("오는지 확인 2");
		return pcCPMWBQAddedKey;
	}
	
	public void fireCPMWBQAdded(L1PcInstance pc){
//		System.out.println("오는지 확인 4");
		pc.eventHandler().fire(pcCPMWBQAddedKey(), new MJpcCPMWBQAddedKeyArgs(pc));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJpcCPMWBQAddedKeyArgs>> pcCPMWBQAddedKey = MJAttrKey.newInstance("mj-pc-CPMWBQ-added");

	public static class MJpcCPMWBQAddedKeyArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		private MJpcCPMWBQAddedKeyArgs(L1PcInstance pc){
//			System.out.println("오는지 확인 3");
			this.pc = pc;
		}
	}
	
	/**
	 * 통합제작
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcCraftOpenKeyArgs>> pcCraftOpenKey(){
//		System.out.println("오는지 확인 2");
		return pcCraftOpenKey;
	}
	
	public void fireCraftOpen(L1PcInstance pc){
//		System.out.println("오는지 확인 4");
		pc.eventHandler().fire(pcCraftOpenKey(), new MJpcCraftOpenKeyArgs(pc));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJpcCraftOpenKeyArgs>> pcCraftOpenKey = MJAttrKey.newInstance("mj-pc-Craft-Open");

	public static class MJpcCraftOpenKeyArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		private MJpcCraftOpenKeyArgs(L1PcInstance pc){
//			System.out.println("오는지 확인 3");
			this.pc = pc;
		}
	}
	
	/**
	 * 잠재력
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcMagicDollKeyArgs>> pcMagicDollKey(){
//		System.out.println("오는지 확인 2");
		return pcMagicDollKey;
	}
	
	public void fireMagicDoll(L1PcInstance pc){
//		System.out.println("오는지 확인 4");
		pc.eventHandler().fire(pcMagicDollKey(), new MJpcMagicDollKeyArgs(pc));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJpcMagicDollKeyArgs>> pcMagicDollKey = MJAttrKey.newInstance("mj-pc-Magic-Doll");

	public static class MJpcMagicDollKeyArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		private MJpcMagicDollKeyArgs(L1PcInstance pc){
//			System.out.println("오는지 확인 3");
			this.pc = pc;
		}
	}
	/**
	 * 인던
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcIndunKeyArgs>> pcIndunKey(){
//		System.out.println("오는지 확인 2");
		return pcIndunKey;
	}
	
	public void fireIndun(L1PcInstance pc){
//		System.out.println("오는지 확인 4");
		pc.eventHandler().fire(pcIndunKey(), new MJpcIndunKeyArgs(pc));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJpcIndunKeyArgs>> pcIndunKey = MJAttrKey.newInstance("mj-pc-Indun");

	public static class MJpcIndunKeyArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		private MJpcIndunKeyArgs(L1PcInstance pc){
//			System.out.println("오는지 확인 3");
			this.pc = pc;
		}
	}
	
	
	/**
	 * 아인포인트
	 **/
/*	public MJAttrKey<MJObjectEventComposite<MJpcEinhasadKeyArgs>> pcEinhasadKey(){
//		System.out.println("오는지 확인 2");
		return pcEinhasadKey;
	}
	
	public void fireEinhasad(L1PcInstance pc){
//		System.out.println("오는지 확인 4");
		if (pcEinhasadKey() == null){
		} else if (pcEinhasadKey() != null){
			pc.eventHandler().fire(pcEinhasadKey(), new MJpcEinhasadKeyArgs(pc));
		} 
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJpcEinhasadKeyArgs>> pcEinhasadKey = MJAttrKey.newInstance("mj-pc-Einhasad");

	public static class MJpcEinhasadKeyArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		private MJpcEinhasadKeyArgs(L1PcInstance pc){
//			System.out.println("오는지 확인 3");
			this.pc = pc;
		}
	}*/
	
	/**
	 * 숨사
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcHiddenDungeonKeyArgs>> pcHiddenDungeonKey(){
//		System.out.println("오는지 확인 2");
		return pcHiddenDungeonKey;
	}
	
	public void fireHiddenDungeon(L1PcInstance pc){
//		System.out.println("오는지 확인 4");
		pc.eventHandler().fire(pcHiddenDungeonKey(), new MJpcHiddenDungeonKeyArgs(pc));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJpcHiddenDungeonKeyArgs>> pcHiddenDungeonKey = MJAttrKey.newInstance("mj-pc-HiddenDungeon");

	public static class MJpcHiddenDungeonKeyArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		private MJpcHiddenDungeonKeyArgs(L1PcInstance pc){
//			System.out.println("오는지 확인 3");
			this.pc = pc;
		}
	}
	/**
	 * 모리아 말걸기
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcMoriaKeyArgs>> pcMoriaKey(){
//		System.out.println("오는지 확인 2");
		return pcMoriaKey;
	}
	
	public void fireMoria(L1PcInstance pc){
//		System.out.println("오는지 확인 4");
		pc.eventHandler().fire(pcMoriaKey(), new MJpcMoriaKeyArgs(pc));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJpcMoriaKeyArgs>> pcMoriaKey = MJAttrKey.newInstance("mj-pc-Moria");

	public static class MJpcMoriaKeyArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		private MJpcMoriaKeyArgs(L1PcInstance pc){
//			System.out.println("오는지 확인 3");
			this.pc = pc;
		}
	}
	
	/**
	 * 바르가 말걸기
	 **/
	public MJAttrKey<MJObjectEventComposite<MJpcBargaKeyArgs>> pcBargaKey(){
//		System.out.println("오는지 확인 2");
		return pcBargaKey;
	}
	
	public void fireBarga(L1PcInstance pc){
//		System.out.println("오는지 확인 4");
		pc.eventHandler().fire(pcBargaKey(), new MJpcBargaKeyArgs(pc));
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJpcBargaKeyArgs>> pcBargaKey = MJAttrKey.newInstance("mj-pc-Barga");

	public static class MJpcBargaKeyArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		private MJpcBargaKeyArgs(L1PcInstance pc){
//			System.out.println("오는지 확인 3");
			this.pc = pc;
		}
	}
	
	
}
		

