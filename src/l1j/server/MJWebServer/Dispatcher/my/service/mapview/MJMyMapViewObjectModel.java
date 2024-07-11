package l1j.server.MJWebServer.Dispatcher.my.service.mapview;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmModel;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

abstract class MJMyMapViewObjectModel extends MJMyGmModel{
	private static void writeModel(MJMyMapViewObjectInfoModel model, L1Character character, String instance){
		model.objectId = character.getId();
		model.name = character.getName();
		model.level = character.getLevel();
		model.instance = instance;
		model.x = character.getX();
		model.y = character.getY();
		model.moveSpeed = (int) character.getCurrentSpriteInterval(EActionCodes.walk);
	}
	static MJMyMapViewObjectModel newInfoModel(L1NpcInstance npc){
		MJMyMapViewObjectInfoNpcModel model = new MJMyMapViewObjectInfoNpcModel();
		writeModel(model, npc, "npc");
		model.npcId = npc.getNpcId();
		model.impl = MJString.EmptyString;
		model.impl = npc.getNpcTemplate().getImpl();
		return model;
	}
	
	static MJMyMapViewObjectModel newInfoModel(L1MonsterInstance monster){
		MJMyMapViewObjectInfoMonsterModel model = new MJMyMapViewObjectInfoMonsterModel();
		writeModel(model, monster, "monster");
		model.npcId = monster.getNpcId();
		model.exp = 0;
		model.exp = monster.getNpcTemplate().get_exp();
		return model;
	}
	
	static MJMyMapViewObjectModel newInfoModel(L1PcInstance pc){
		MJMyMapViewObjectInfoPcModel model = new MJMyMapViewObjectInfoPcModel();
		writeModel(model, pc, "pc");
		model.title = MJString.isNullOrEmpty(pc.getTitle()) ? MJString.EmptyString : pc.getTitle();
		model.pledge = MJString.isNullOrEmpty(pc.getClanname()) ? MJString.EmptyString : pc.getClanname();
		model.gender = pc.get_sex() == 0 ? "m" : "f";
		model.characterClass = MJEClassesType.fromGfx(pc.getClassId()).toInt();
		return model;
	}
	
	static MJMyMapViewObjectModel newRemove(int objectId){
		MJMyMapViewObjectRemoveModel model = new MJMyMapViewObjectRemoveModel();
		model.objectId = objectId;
		return model;
	}
	
	static MJMyMapViewObjectModel newMove(int objectId, int newX, int newY, int moveSpeed){
		MJMyMapViewObjectMoveModel model = new MJMyMapViewObjectMoveModel();
		model.objectId = objectId;
		model.x = newX;
		model.y = newY;
		model.moveSpeed = moveSpeed;
		return model;
	}
	
	int objectId;
	MJMyMapViewObjectModel(){
		super();
	}

	@SuppressWarnings("unused")
	private static class MJMyMapViewObjectInfoModel extends MJMyMapViewObjectModel{
		int x;
		int y;
		String name;
		int level;
		String instance;
		int moveSpeed;
		private MJMyMapViewObjectInfoModel(){
			super();
		}
	}

	@SuppressWarnings("unused")
	private static class MJMyMapViewObjectInfoNpcModel extends MJMyMapViewObjectInfoModel{
		private int npcId;
		private String impl;
		private MJMyMapViewObjectInfoNpcModel(){
			super();
		}
	}

	@SuppressWarnings("unused")
	private static class MJMyMapViewObjectInfoMonsterModel extends MJMyMapViewObjectInfoModel{
		private int npcId;
		private long exp;
		private MJMyMapViewObjectInfoMonsterModel(){
		}
	}

	@SuppressWarnings("unused")
	private static class MJMyMapViewObjectInfoPcModel extends MJMyMapViewObjectInfoModel{
		private String title;
		private String pledge;
		private String gender;
		private int characterClass;
		private MJMyMapViewObjectInfoPcModel(){
			super();
		}
	}

	private static class MJMyMapViewObjectRemoveModel extends MJMyMapViewObjectModel{
		private MJMyMapViewObjectRemoveModel(){
			super();
		}
	}

	@SuppressWarnings("unused")
	private static class MJMyMapViewObjectMoveModel extends MJMyMapViewObjectModel{
		int x;
		int y;
		int moveSpeed;
		private MJMyMapViewObjectMoveModel(){
			super();
		}
	}
}
