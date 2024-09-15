package l1j.server.server.model;

import java.lang.reflect.Constructor;
import java.util.Random;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.MobGroupTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1MobGroup;
import l1j.server.server.templates.L1Npc;

public class L1MobGroupSpawn {

	private static L1MobGroupSpawn _instance;

	private static Random _random = new Random(System.nanoTime());

	private boolean _isRespawnScreen;

	private boolean _isInitSpawn;

	private L1MobGroupSpawn() {
	}

	public static L1MobGroupSpawn getInstance() {
		if (_instance == null) {
			_instance = new L1MobGroupSpawn();
		}
		return _instance;
	}

	public void doSpawn(L1NpcInstance leader, int groupId, boolean isRespawnScreen, boolean isInitSpawn) {

		L1MobGroup mobGroup = MobGroupTable.getInstance().getTemplate(groupId);
		if (mobGroup == null) {
			return;
		}

		L1NpcInstance mob;
		_isRespawnScreen = isRespawnScreen;
		_isInitSpawn = isInitSpawn;

		L1MobGroupInfo mobGroupInfo = new L1MobGroupInfo();
		mobGroupInfo.setRemoveGroup(mobGroup.isRemoveGroupIfLeaderDie());
		mobGroupInfo.addMember(leader);

		if (mobGroup.getMinion1Id() > 0 && mobGroup.getMinion1Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion1Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion1Id());
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion2Id() > 0 && mobGroup.getMinion2Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion2Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion2Id());
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion3Id() > 0 && mobGroup.getMinion3Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion3Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion3Id());
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion4Id() > 0 && mobGroup.getMinion4Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion4Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion4Id());
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion5Id() > 0 && mobGroup.getMinion5Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion5Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion5Id());
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion6Id() > 0 && mobGroup.getMinion6Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion6Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion6Id());
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion7Id() > 0 && mobGroup.getMinion7Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion7Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion7Id());
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
		if (mobGroup.getMinion8Id() > 0 && mobGroup.getMinion8Count() > 0) {
			for (int i = 0; i < mobGroup.getMinion8Count(); i++) {
				mob = spawn(leader, mobGroup.getMinion8Id());
				if (mob != null) {
					mobGroupInfo.addMember(mob);
				}
			}
		}
	}

	private L1NpcInstance spawn(L1NpcInstance leader, int npcId) {
		L1NpcInstance mob = null;
		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);
			if (l1npc == null) {
				return null;
			}

			String s = l1npc.getImpl();
			Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
			Object parameters[] = { l1npc };
			mob = (L1NpcInstance) constructor.newInstance(parameters);
			mob.setId(IdFactory.getInstance().nextId());

			mob.setHeading(leader.getHeading());
			mob.setMap(leader.getMapId());
			mob.setMovementDistance(leader.getMovementDistance());
			mob.setRest(leader.isRest());

			mob.setX(leader.getX() + _random.nextInt(5) - 2);
			mob.setY(leader.getY() + _random.nextInt(5) - 2);
			if (!isDoSpawn(mob)) {
				mob.setX(leader.getX());
				mob.setY(leader.getY());
			}
			mob.setHomeX(mob.getX());
			mob.setHomeY(mob.getY());

			if (mob instanceof L1MonsterInstance) {
				((L1MonsterInstance) mob).initHideForMinion(leader);
			}

			mob.setSpawn(leader.getSpawn());
			mob.setRespawn(leader.isReSpawn());
			mob.setSpawnNumber(leader.getSpawnNumber());

			L1World.getInstance().storeObject(mob);
			L1World.getInstance().addVisibleObject(mob);

			if (mob instanceof L1MonsterInstance) {
				if (!_isInitSpawn && mob.getHiddenStatus() == 0) {
					mob.onNpcAI();
				}
			}
			mob.getLight().turnOnOffLight();
			mob.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mob;
	}

	private boolean isDoSpawn(L1NpcInstance mob) {
		if (mob.getMap().isInMap(mob.getLocation()) && mob.getMap().isPassable(mob.getLocation())) {
			if (_isRespawnScreen) {
				return true;
			}
			if (L1World.getInstance().getVisiblePlayer(mob).size() == 0) {
				return true;
			}
		}
		return false;
	}

}
