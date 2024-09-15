package l1j.server.server.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.MobSkillTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1MobSkill;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Skills;

public class L1MobSkillUse {
	private static Random _rnd = new Random(System.nanoTime());

	private L1MobSkill _mobSkillTemplate = null;

	private L1NpcInstance _attacker = null;

	private L1Character _target = null;


	private long _sleepTime = 0;

	private int _skillUseCount[];

	public void dispose(){
		_mobSkillTemplate 	= null;
		_attacker 			= null;
		_target				= null;
	}
	
	public L1MobSkillUse(L1NpcInstance npc) {
		try {
			if (npc == null)
				return;
			_sleepTime = 0;
			_mobSkillTemplate = MobSkillTable.getInstance().getTemplate(npc.getNpcTemplate().get_npcId());
			if (_mobSkillTemplate == null) {
				return;
			}
			_attacker = npc;
			_skillUseCount = new int[getMobSkillTemplate().getSkillSize()];
		} catch (Exception e) {
		}
	}

	private int getSkillUseCount(int idx) {
		return _skillUseCount[idx];
	}

	private void skillUseCountUp(int idx) {
		_skillUseCount[idx]++;
	}

	public void resetAllSkillUseCount() {
		if (getMobSkillTemplate() == null) {
			return;
		}

		for (int i = 0; i < getMobSkillTemplate().getSkillSize(); i++) {
			_skillUseCount[i] = 0;
		}
	}

	public long getSleepTime() {
		return _sleepTime;
	}

	public void setSleepTime(long i) {
		_sleepTime = i;
	}

	public L1MobSkill getMobSkillTemplate() {
		return _mobSkillTemplate;
	}

	public boolean skillUse(L1Character tg) {
		try {
			if (tg == null || _mobSkillTemplate == null) {
				return false;
			}
			_target = tg;
			int type;
			type = getMobSkillTemplate().getType(0);

			if (type == L1MobSkill.TYPE_NONE) {
				return false;
			}

			int i = 0;
			for (i = 0; i < getMobSkillTemplate().getSkillSize()
					&& getMobSkillTemplate().getType(i) != L1MobSkill.TYPE_NONE; i++) {

				int changeType = getMobSkillTemplate().getChangeTarget(i);
				if (changeType > 0) {
					_target = changeTarget(changeType, i);
				} else {
					_target = tg;
				}

				if (isSkillUseble(i) == false) {
					continue;
				}

				boolean is_use = false;
				type = getMobSkillTemplate().getType(i);				
				if (type == L1MobSkill.TYPE_PHYSICAL_ATTACK) {
					if (physicalAttack(i) == true) {
						skillUseCountUp(i);
						is_use = true;
					}
				} else if (type == L1MobSkill.TYPE_MAGIC_ATTACK) {
					if (magicAttack(i) == true) {
						skillUseCountUp(i);
						is_use = true;
					}
				} else if (type == L1MobSkill.TYPE_SUMMON) {
					if (summon(i) == true) {
						skillUseCountUp(i);
						is_use = true;
					}
				} else if (type == L1MobSkill.TYPE_POLY) {
					if (poly(i) == true) {
						skillUseCountUp(i);
						is_use = true;
					}
				}
				if(is_use) {
					String ment = getMobSkillTemplate().getSpellMent(i);
					if(!MJString.isNullOrEmpty(ment)) {
						Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker, ment, 2));
					}
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean summon(int idx) {
		int summonId = getMobSkillTemplate().getSummon(idx);
		int min = getMobSkillTemplate().getSummonMin(idx);
		int max = getMobSkillTemplate().getSummonMax(idx);
		int count = 0;

		if (summonId == 0) {
			return false;
		}

		count = _rnd.nextInt(max) + min;
		mobspawn(summonId, count);

		_attacker.broadcastPacket(new S_SkillSound(_attacker.getId(), 761));

		S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_SkillBuff);
		_attacker.broadcastPacket(gfx);

		_sleepTime = _attacker.getCurrentSpriteInterval(EActionCodes.spell_nodir);
		return true;
	}

	private boolean poly(int idx) {
		int polyId = getMobSkillTemplate().getPolyId(idx);
		boolean usePoly = false;

		if (polyId == 0) {
			return false;
		}

		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker)) {
			if (pc == null || pc.isDead()) {
				continue;
			}
			if (pc.isGhost()) {
				continue;
			}
			if (pc.isGmInvis()) {
				continue;
			}
			if (_attacker.glanceCheck(pc.getX(), pc.getY()) == false) {
				continue;
			}

			int npcId = _attacker.getNpcTemplate().get_npcId();
			switch (npcId) {
			case 81082:
				pc.getInventory().takeoffEquip(945);
				break;
			default:
				break;
			}
			L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_NPC, false, false);

			usePoly = true;
		}
		if (usePoly) {
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker)) {
				if (pc == null)
					continue;
				pc.sendPackets(new S_SkillSound(pc.getId(), 230));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), 230));
				break;
			}
			S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_SkillBuff);
			_attacker.broadcastPacket(gfx);

			_sleepTime = _attacker.getCurrentSpriteInterval(EActionCodes.spell_nodir);
		}

		return usePoly;
	}

	private boolean magicAttack(int idx) {
		L1SkillUse skillUse = new L1SkillUse();
		int skillid = getMobSkillTemplate().getSkillId(idx);
		boolean canUseSkill = false;
		if (_attacker.hasSkillEffect(L1SkillId.SILENCE)) {
			return false;
		}

		if ((skillid >= 22020 && skillid <= 22029 || skillid >= 22041 && skillid <= 22052)
				|| (skillid >= 7001 && skillid <= 7050)) {
			if (_attacker.hasSkillEffect(L1SkillId.PREDICATEDELAY)) {// 용언 딜레이
				return false;
			} else {
				_attacker.setSkillEffect(L1SkillId.PREDICATEDELAY, 10 * 1000);
			}
		}

		int npcId = _attacker.getNpcTemplate().get_npcId();
		switch (npcId) {//몬스터 스킬채팅
		case 7320219:
		case 45617: 
		case 45529:
			if(skillid == 707049 || skillid == 707050 || skillid == 707051 || skillid == 707052){
				boolean isuse = false;
				for(L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_attacker)){
					if(pc == null || pc.isDead())
						continue;
					
					if(skillUse.checkUseSkill(null, skillid, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_NORMAL, _attacker)){
						isuse = true;
						skillUse.handleCommands(null, skillid, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_NORMAL, _attacker);
					}
					
					if(isuse){
						if (getMobSkillTemplate().getLeverage(idx) > 0) {
							skillUse.setLeverage(getMobSkillTemplate().getLeverage(idx));
						}
						_sleepTime = _attacker.getCurrentSpriteInterval(EActionCodes.spell_dir);
					}
				}
				return true;
			}
			break;
		default:
			break;
		}

		if (skillid > 0) {
			canUseSkill = skillUse.checkUseSkill(null, skillid, _target.getId(), _target.getX(), _target.getY(), null,0, L1SkillUse.TYPE_NORMAL, _attacker);
		}
		if (canUseSkill == true) {
			if (getMobSkillTemplate().getLeverage(idx) > 0) {
				skillUse.setLeverage(getMobSkillTemplate().getLeverage(idx));
			}
			
			if (_target instanceof L1CastleGuardInstance)
				return false;
			
			skillUse.handleCommands(null, skillid, _target.getId(), _target.getX(), _target.getY(), null, 0,L1SkillUse.TYPE_NORMAL, _attacker);
			L1Skills skill = SkillsTable.getInstance().getTemplate(skillid);
			if (skill.getTarget().equals("attack") && skillid != 18) {
				_sleepTime = _attacker.getCurrentSpriteInterval(EActionCodes.spell_dir);
			} else {
				_sleepTime = _attacker.getCurrentSpriteInterval(EActionCodes.spell_nodir);
			}

			return true;
		}
		return false;
	}

	private boolean physicalAttack(int idx) {
		Map<Integer, Integer> targetList = new ConcurrentHashMap<Integer, Integer>();
		int areaWidth = getMobSkillTemplate().getAreaWidth(idx);
		int areaHeight = getMobSkillTemplate().getAreaHeight(idx);
		int range = getMobSkillTemplate().getRange(idx);
		int actId = getMobSkillTemplate().getActid(idx);
		int gfxId = getMobSkillTemplate().getGfxid(idx);

		if (_attacker.getLocation().getTileLineDistance(_target.getLocation()) > range) {
			return false;
		}

		if (!_attacker.glanceCheck(_target.getX(), _target.getY())) {
			return false;
		}

		_attacker.setHeading(_attacker.targetDirection(_target.getX(), _target.getY()));

		if (areaHeight > 0) {
			L1Character cha = null;
			ArrayList<L1Object> olist = L1World.getInstance().getVisibleBoxObjects(_attacker, _attacker.getHeading(), areaWidth,areaHeight);
			if(olist != null){
				for (L1Object obj : olist) {
					if (obj == null || !(obj instanceof L1Character))
						continue;

					cha = (L1Character) obj;
					if (cha.isDead())
						continue;

					if (!_attacker.glanceCheck(cha.getX(), cha.getY()))
						continue;
					
					if (_target instanceof L1PcInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance)cha;
							if (pc.isGhost() || pc.isGmInvis())
								continue;
							
							if(_attacker instanceof L1SummonInstance || _attacker instanceof L1PetInstance || _target instanceof MJCompanionInstance){
								if (cha.getId() == _attacker.getMaster().getId() || cha.getZoneType() == 1)
									continue;
							}
						}else if(!(obj instanceof L1SummonInstance) && !(obj instanceof L1PetInstance) && !(obj instanceof MJCompanionInstance))
							continue;
						
						targetList.put(obj.getId(), 0);
					}else{
						if (obj instanceof L1MonsterInstance) {
							targetList.put(obj.getId(), 0);
						}
					}
				}
			}
		} else {
			targetList.put(_target.getId(), 0);
		}

		if (targetList.size() == 0) {
			return false;
		}

		Iterator<Integer> ite = targetList.keySet().iterator();
		L1Attack attack = null;

		EActionCodes actionCode = EActionCodes.attack;
		while (ite.hasNext()) {
			int targetId = ite.next();
			actionCode = EActionCodes.attack;
			
			/**
			 * 종료시 타겟을 못찾는 null 에러로 인한 추가
			 */
			if((L1Character) L1World.getInstance().findObject(targetId) == null)
				continue;
			
			attack = new L1Attack(_attacker, (L1Character) L1World.getInstance().findObject(targetId));
			if (attack.calcHit()) {
				if (getMobSkillTemplate().getLeverage(idx) > 0) {
					attack.setLeverage(getMobSkillTemplate().getLeverage(idx));
				}
				attack.calcDamage();
			}
			if (actId > 0) {
				attack.setActId(actId);
			}
			
			if (targetId == _target.getId()) {
				if (gfxId > 0) {
					switch (_attacker.getNpcId()) {
					case 7320176: 
					case 45263: 
					case 7320180: 
					case 7320182:
						_attacker.broadcastPacket(new S_DoActionGFX(_attacker.getId(), 18));
						S_SkillSound pck = new S_SkillSound(_target.getId(), gfxId);
						_target.broadcastPacket(pck, false);
						_target.sendPackets(pck, true);
						
						/*ServerBasePacket[] pcks = new ServerBasePacket[] { new S_SkillSound(_target.getId(), gfxId),
								new S_DoActionGFX(_target.getId(), EActionCodes.damage.toInt()) };
						_target.broadcastPacket(pcks, false);
						_target.sendPackets(pcks[0], true);
						if (!_target.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
							_target.sendPackets(pcks[1], true);*/
						actionCode = EActionCodes.spell_dir;
						break;
					default:
						_attacker.broadcastPacket(new S_SkillSound(_attacker.getId(), gfxId));
						break;
					}
				}
				attack.action();
			}
			attack.commit();
		}

		_sleepTime = _attacker.getCurrentSpriteInterval(actionCode);
		return true;
	}

	private boolean isSkillUseble(int skillIdx) {
		boolean useble = false;

		if (getMobSkillTemplate().getTriggerRandom(skillIdx) > 0) {
			int chance = _rnd.nextInt(100) + 1;
			if (chance < getMobSkillTemplate().getTriggerRandom(skillIdx)) {
				useble = true;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerHp(skillIdx) > 0) {
			int hpRatio = (_attacker.getCurrentHp() * 100) / _attacker.getMaxHp();
			if (hpRatio <= getMobSkillTemplate().getTriggerHp(skillIdx)) {
				useble = true;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerCompanionHp(skillIdx) > 0) {
			L1NpcInstance companionNpc = searchMinCompanionHp();
			if (companionNpc == null) {
				return false;
			}

			int hpRatio = (companionNpc.getCurrentHp() * 100) / companionNpc.getMaxHp();
			if (hpRatio <= getMobSkillTemplate().getTriggerCompanionHp(skillIdx)) {
				useble = true;
				_target = companionNpc;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerRange(skillIdx) != 0) {
			int distance = _attacker.getLocation().getTileLineDistance(_target.getLocation());

			if (getMobSkillTemplate().isTriggerDistance(skillIdx, distance)) {
				useble = true;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerCount(skillIdx) > 0) {
			if (getSkillUseCount(skillIdx) < getMobSkillTemplate().getTriggerCount(skillIdx)) {
				useble = true;
			} else {
				return false;
			}
		}
		return useble;
	}

	private L1NpcInstance searchMinCompanionHp() {
		L1NpcInstance npc;
		L1NpcInstance minHpNpc = null;
		int hpRatio = 100;
		int companionHpRatio;
		int family = _attacker.getNpcTemplate().get_family();

		for (L1Object object : L1World.getInstance().getVisibleObjects(_attacker)) {
			if (object == null)
				continue;
			if (object instanceof L1NpcInstance) {
				npc = (L1NpcInstance) object;
				if (npc.getNpcTemplate().get_family() == family) {
					companionHpRatio = (npc.getCurrentHp() * 100) / npc.getMaxHp();
					if (companionHpRatio < hpRatio) {
						hpRatio = companionHpRatio;
						minHpNpc = npc;
					}
				}
			}
		}
		return minHpNpc;
	}

	private void mobspawn(int summonId, int count) {
		int i;

		for (i = 0; i < count; i++) {
			mobspawn(summonId);
		}
	}

	private void mobspawn(int summonId) {
		try {
			L1Npc spawnmonster = NpcTable.getInstance().getTemplate(summonId);
			if (spawnmonster != null) {
				L1NpcInstance mob = null;
				try {
					String implementationName = spawnmonster.getImpl();
					Constructor<?> _constructor = Class
							.forName((new StringBuilder()).append("l1j.server.server.model.Instance.")
									.append(implementationName).append("Instance").toString())
							.getConstructors()[0];
					mob = (L1NpcInstance) _constructor.newInstance(new Object[] { spawnmonster });
					mob.setId(IdFactory.getInstance().nextId());
					L1Location loc = _attacker.getLocation().randomLocation(8, false);
					int heading = _rnd.nextInt(8);
					mob.setX(loc.getX());
					mob.setY(loc.getY());
					mob.setHomeX(loc.getX());
					mob.setHomeY(loc.getY());
					short mapid = _attacker.getMapId();
					mob.setMap(mapid);
					mob.setHeading(heading);
					L1World.getInstance().storeObject(mob);
					L1World.getInstance().addVisibleObject(mob);
					L1Object object = L1World.getInstance().findObject(mob.getId());
					L1MonsterInstance newnpc = (L1MonsterInstance) object;
					if (summonId == 45061 || summonId == 45161 || summonId == 45181 || summonId == 45455) {
						newnpc.broadcastPacket(new S_DoActionGFX(newnpc.getId(), ActionCodes.ACTION_Hide));
						newnpc.setStatus(13);
						newnpc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(newnpc));
						//newnpc.broadcastPacket(S_WorldPutObject.get(newnpc));
						newnpc.broadcastPacket(new S_DoActionGFX(newnpc.getId(), ActionCodes.ACTION_Appear));
						newnpc.setStatus(0);
						newnpc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(newnpc));
						//newnpc.broadcastPacket(S_WorldPutObject.get(newnpc));
					}
					newnpc.onNpcAI();
					newnpc.getLight().turnOnOffLight();
					newnpc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
					newnpc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private L1Character changeTarget(int type, int idx) {
		L1Character target;

		switch (type) {
		case L1MobSkill.CHANGE_TARGET_ME:
			target = _attacker;
			break;
		case L1MobSkill.CHANGE_TARGET_RANDOM:
			List<L1Character> targetList = new ArrayList<L1Character>();
			L1Character cha = null;
			for (L1Object obj : L1World.getInstance().getVisibleObjects(_attacker)) {
				if (obj == null)
					continue;
				if (obj instanceof L1PcInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance || obj instanceof MJCompanionInstance) {
					cha = (L1Character) obj;
					int distance = _attacker.getLocation().getTileLineDistance(cha.getLocation());

					if (!getMobSkillTemplate().isTriggerDistance(idx, distance)) {
						continue;
					}

					if (!_attacker.glanceCheck(cha.getX(), cha.getY())) {
						continue;
					}

					if (!_attacker.getHateList().containsKey(cha)) {
						continue;
					}

					if (cha.isDead()) {
						continue;
					}

					if (cha instanceof L1PcInstance) {
						if (((L1PcInstance) cha).isGhost()) {
							continue;
						}
					}
					targetList.add((L1Character) obj);
				}
			}

			if (targetList.size() == 0) {
				target = _target;
			} else {
				int randomSize = targetList.size() * 100;
				int targetIndex = _rnd.nextInt(randomSize) / 100;
				target = targetList.get(targetIndex);
			}
			break;

		default:
			target = _target;
			break;
		}
		return target;
	}
}
