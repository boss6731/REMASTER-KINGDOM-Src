package l1j.server.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FREEZE;

import java.util.Random;

import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.ActionCodes;
import l1j.server.server.server.datatables.SkillsTable;
import l1j.server.server.server.datatables.WeaponSkillTable;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.templates.L1Skills;

public class L1WeaponSkill {

	private static Random _random = new Random(System.nanoTime());

	private int _weaponId;

	private int _probability;

	private int _fixDamage;

	private int _randomDamage;

	private int _area;

	private int _skillId;

	private int _skillTime;

	private int _effectId;

	private int _effectTarget;

	private boolean _isArrowType;

	private int _attr;

	public L1WeaponSkill(int weaponId, int probability, int fixDamage, int randomDamage, int area, int skillId,
			int skillTime, int effectId, int effectTarget, boolean isArrowType, int attr) {
		_weaponId = weaponId;
		_probability = probability;
		_fixDamage = fixDamage;
		_randomDamage = randomDamage;
		_area = area;
		_skillId = skillId;
		_skillTime = skillTime;
		_effectId = effectId;
		_effectTarget = effectTarget;
		_isArrowType = isArrowType;
		_attr = attr;
	}

	public int getWeaponId() {
		return _weaponId;
	}

	public int getProbability() {
		return _probability;
	}

	public int getFixDamage() {
		return _fixDamage;
	}

	public int getRandomDamage() {
		return _randomDamage;
	}

	public int getArea() {
		return _area;
	}

	public int getSkillId() {
		return _skillId;
	}

	public int getSkillTime() {
		return _skillTime;
	}

	public int getEffectId() {
		return _effectId;
	}

	public int getEffectTarget() {
		return _effectTarget;
	}

	public boolean isArrowType() {
		return _isArrowType;
	}

	public int getAttr() {
		return _attr;
	}

	public static double getWeaponSkillDamage(L1PcInstance pc, L1Character cha, int weaponId) {
		L1WeaponSkill weaponSkill = WeaponSkillTable.getInstance().getTemplate(weaponId);
		if (pc == null || cha == null || weaponSkill == null) {
			return 0;
		}

		int chance = _random.nextInt(100) + 1;
		if (weaponSkill.getProbability() < chance) {
			return 0;
		}

		int skillId = weaponSkill.getSkillId();

		if (skillId == L1SkillId.SILENCE && cha instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;

			if (npc.getNpcId() == 45684 || npc.getNpcId() == 45683 || npc.getNpcId() == 45681 || npc.getNpcId() == 45682
					|| npc.getNpcId() == 900011 || npc.getNpcId() == 900012 || npc.getNpcId() == 900013
					|| npc.getNpcId() == 900038 || npc.getNpcId() == 900039 || npc.getNpcId() == 900040
					|| npc.getNpcId() == 5096 || npc.getNpcId() == 5097 || npc.getNpcId() == 5098
					|| npc.getNpcId() == 5099 || npc.getNpcId() == 5100) {
				return 0;
			}
		}

		if (skillId != 0) {
			L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
			if (skill != null && skill.getTarget().equals("buff")) {
				if (!isFreeze(cha)) {
					if (skillId == 56) {
						if (!cha.hasSkillEffect(skillId)) {
							cha.addDmgup(-6);
							cha.getAC().addAc(12);
							if (cha instanceof L1PcInstance) {
								L1PcInstance target = (L1PcInstance) cha;
								target.sendPackets(new S_OwnCharAttrDef(target));
							}
						}
					}
					cha.setSkillEffect(skillId, weaponSkill.getSkillTime() * 1000);
				}
			}
		}

		int effectId = weaponSkill.getEffectId();
		if (effectId != 0) {
			int chaId = 0;
			if (weaponSkill.getEffectTarget() == 0) {
				chaId = cha.getId();
			} else {
				chaId = pc.getId();
			}
			boolean isArrowType = weaponSkill.isArrowType();
			if (!isArrowType) {
				pc.sendPackets(new S_SkillSound(chaId, effectId));
				pc.broadcastPacket(new S_SkillSound(chaId, effectId));
			} else {
				S_UseAttackSkill packet = new S_UseAttackSkill(pc, cha.getId(), effectId, cha.getX(), cha.getY(),
						ActionCodes.ACTION_Attack, false);
				pc.sendPackets(packet);
				pc.broadcastPacket(packet, cha);
			}
		}

		double damage = 0;
		int randomDamage = weaponSkill.getRandomDamage();
		if (randomDamage != 0) {
			damage = _random.nextInt(randomDamage);
		}
		damage += weaponSkill.getFixDamage();

		if (effectId == 6985) {
			damage += pc.getAbility().getTotalInt() * 3;
		} else {
			damage += pc.getAbility().getTotalInt() * 2;
		}

		int area = weaponSkill.getArea();
		if (area > 0 || area == -1) {
			L1PcInstance targetPc = null;
			L1NpcInstance targetNpc = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(cha, area)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()) {
					continue;
				}
				if (object.getId() == cha.getId()) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					if (targetPc.getZoneType() == 1) {
						continue;
					}
				}

				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance || object instanceof L1SummonInstance
							|| object instanceof L1PetInstance || object instanceof L1MonsterInstance
							|| object instanceof MJCompanionInstance)) {
						continue;
					}
				}
				damage = calcDamageReduction((L1Character) object, damage, weaponSkill.getAttr());
				if (damage <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.broadcastPacket(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) damage);
				} else if (object instanceof L1SummonInstance || object instanceof L1PetInstance
						|| object instanceof L1MonsterInstance || object instanceof MJCompanionInstance) {
					targetNpc = (L1NpcInstance) object;
					targetNpc.broadcastPacket(new S_DoActionGFX(targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) damage);
				}
			}
		}

		return calcDamageReduction(cha, damage, weaponSkill.getAttr());
	}

	public static void chainSword(L1PcInstance pc, L1Character target) { // 鏈鋸劍傷害。
		int chance = 0;
		if (pc.getWeapon() != null) {
			chance = pc.getWeapon().getItem().get_weak_point_chance() + (pc.getWeapon().getItem().get_weak_point_enchant_value() * pc.getWeapon().getEnchantLevel());
		}

		if (_random.nextInt(100) >= chance)
			return;

		if (pc.hasSkillEffect(L1SkillId.CHAINSWORD1)) {
			pc.killSkillEffectTimer(L1SkillId.CHAINSWORD1);
		}

//		L1SkillUse.on_icons(pc, L1SkillId.CHAINSWORD1, 8000);
		pc.setSkillEffect(L1SkillId.CHAINSWORD1, 8000);
		target.send_effect(21932);
//		pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 1), true);
		if (!pc.hasSkillEffect(L1SkillId.CHAINSWORD_STUN_REUSE_TIME)) {
			if (pc.isPassive(MJPassiveID.FOU_SLAYER_FORCE.toInt())) {
				pc.setChainSwordExposed(true);
				pc.setChainSwordStep(3);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 3), true);
			} else if (pc.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt())) {
				pc.setChainSwordExposed(true);
				pc.setChainSwordStep(2);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 2), true);	
			} else {
				pc.setChainSwordExposed(true);
				pc.setChainSwordStep(1);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 1), true);
			}
		} else {
			pc.setChainSwordExposed(true);
			pc.setChainSwordStep(1);
			pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 1), true);
		}
		 
		
	}

	public static double calcDamageReduction(L1Character cha, double dmg, int attr) {
		if (isFreeze(cha)) {
			return 0;
		}

		int ran1 = 0; // 隨機數值應用
		int mrset = 0; // 從魔法抗性中減去隨機數值的值
		int mrs = cha.getResistance().getEffectedMrBySkill();
		ran1 = _random.nextInt(5) + 1;
		mrset = mrs - ran1;
		double calMr = 0.00D;
		calMr = (220 - mrset) / 250.00D;
		dmg *= calMr;

		if (dmg < 0) {
			dmg = 0;
		}

		int resist = 0;
		if (attr == L1Skills.ATTR_EARTH) {
			resist = cha.getResistance().getEarth();
		} else if (attr == L1Skills.ATTR_FIRE) {
			resist = cha.getResistance().getFire();
		} else if (attr == L1Skills.ATTR_WATER) {
			resist = cha.getResistance().getWater();
		} else if (attr == L1Skills.ATTR_WIND) {
			resist = cha.getResistance().getWind();
		}
		int resistFloor = (int) (0.32 * Math.abs(resist));
		if (resist >= 0) {
			resistFloor *= 1;
		} else {
			resistFloor *= -1;
		}
		double attrDeffence = resistFloor / 32.0;
		dmg = (1.0 - attrDeffence) * dmg;

		return dmg;
	}

	private static boolean isFreeze(L1Character cha) {
		if (cha.hasSkillEffect(STATUS_FREEZE)) {
			return true;
		}
		if (cha.hasSkillEffect(ABSOLUTE_BARRIER)) {
			return true;
		}
		if (cha.hasSkillEffect(ICE_LANCE)) {
			return true;
		}
		if (cha.hasSkillEffect(EARTH_BIND)) {
			return true;
		}

		if (cha.hasSkillEffect(COUNTER_MAGIC)) {
			cha.removeSkillEffect(COUNTER_MAGIC);
			int castgfx = SkillsTable.getInstance().getTemplate(COUNTER_MAGIC).getCastGfx();
			cha.broadcastPacket(new S_SkillSound(cha.getId(), castgfx));
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillSound(pc.getId(), castgfx));
			}
			return true;
		}
		return false;
	}
}
