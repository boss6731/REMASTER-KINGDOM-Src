package l1j.server.server.model;

import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.utils.IntRange;

public class Resistance {
	// XXX 部分顯示和效果限制值
	private static final int LIMIT_MIN = -128;
	private static final int LIMIT_MAX = 999;
	private static final int LIMIT_MIN_MR = 0;
	private static final int LIMIT_MAX_MR = 999;

	private int baseMr = 0; // 基本魔法防禦
	private int addedMr = 0; // 包括由物品或魔法增加的魔法防禦
	private int levelMr = 0;

	private int fire = 0; // 火抗性
	private int water = 0; // 水抗性
	private int wind = 0; // 風抗性
	private int earth = 0; // 地抗性

	private int calcPcDefense = 0; // PVP 傷害減少
	private int PVPweaponTotalDamage = 0; // PVP 額外傷害

	private L1Character character = null;

	public void dispose() {
		character = null;
	}

	public Resistance() {
	}

	public Resistance(L1Character cha) {
		init();
		character = cha;
	}

	public void init() {
		baseMr = addedMr = 0;
		fire = water = wind = earth = 0;
		calcPcDefense = PVPweaponTotalDamage = 0;
	}

	private int checkMrRange(int i, final int MIN) {
		return IntRange.ensure(i, MIN, LIMIT_MAX_MR);
	}

	private byte checkRange(int i) {
		return (byte) IntRange.ensure(i, LIMIT_MIN, LIMIT_MAX);
	}

	public int getEffectedMrBySkill() {
		int effectedMr = 0;
		if (character != null) {
			effectedMr = getMr();
			
			if (character.hasSkillEffect(L1SkillId.ERASE_MAGIC))
				effectedMr /= 2;
		}
		return effectedMr;
	}

	public int getAddedMr() {
		return addedMr;
	}

	public int getMr() {
		int add_mr = baseMr + addedMr + getLevelMr();
		int Level = character.getLevel();
		int passive_mr = 0;
		if (character != null && character.isPassive(MJPassiveID.TACTICAL_ADVANCE.toInt())) {
			if (Level < 80) 
				Level = 80;
			
			passive_mr += 3 + ((Level - 80) / 3) * 2;
			
			if (passive_mr > 15)
				passive_mr = 15;
		}

		if (character == null)
			return 0;

		if (character.hasSkillEffect(L1SkillId.POTENTIAL)) {
			add_mr += (add_mr * 0.2);// 20%
		}
		if (character.isPassive(MJPassiveID.RESIST_ELEMENT.toInt())) {
			add_mr += 5;
		}
		if (character.hasSkillEffect(L1SkillId.BLACK_DRAGON_MAAN) || character.hasSkillEffect(L1SkillId.NAVER_BLACK_DRAGON_MAAN)) {
			add_mr += (add_mr * 0.1);// 10%
//			System.out.println("BLACK_DRAGON_MAAN: " +L1SkillId.BLACK_DRAGON_MAAN);
//			System.out.println("NAVER_BLACK_DRAGON_MAAN: " +L1SkillId.NAVER_BLACK_DRAGON_MAAN);
		}

		return checkMrRange(add_mr + passive_mr, LIMIT_MIN_MR);
	}

	public int getBaseMr() {
		return baseMr;
	}

	public void addMr(int i) {
		setAddedMr(addedMr + i);

		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.sendPackets(new S_SPMR(pc));

		}
	}

	public void setBaseMr(int i) {
		baseMr = checkMrRange(i, LIMIT_MIN_MR);
	}

	private void setAddedMr(int i) {
		addedMr = checkMrRange(i, -baseMr);
	}

	public int getLevelMr() {
		levelMr = character.getLevel() / 2;
		return levelMr;
	}

	public int getcalcPcDefense() {
	/*	int add_reduc = 0;
		int percent = (int)Math.round(character.getCurrentHp() / character.getMaxHp() * 100.0D);
		if(character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			if (pc != null && pc.isPassive(MJPassiveID.BERSERK.toInt())) {
				if (percent < 50) {
				add_reduc += 16;
				} else {
					add_reduc += 8;
				}
			}
		}
		return calcPcDefense + add_reduc;*/
		return calcPcDefense;
	}

	public int getPVPweaponTotalDamage() {
		/*int add_dmg = 0;
		int percent = (int)Math.round(character.getCurrentHp() / character.getMaxHp() * 100.0D);
		if(character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			if (pc != null && pc.isPassive(MJPassiveID.BERSERK.toInt())) {
				if (percent < 50) {
				add_dmg += 10;
				} else {
					add_dmg += 5;
				}
			}
		}
		return PVPweaponTotalDamage + add_dmg;*/
		return PVPweaponTotalDamage;
	}

	public int getFire() {
		return increase_elemental_resist(fire);
	}

	public int getWater() {
		return increase_elemental_resist(water);
	}

	public int getWind() {
		return increase_elemental_resist(wind);
	}

	public int getEarth() {
		return increase_elemental_resist(earth);
	}

	private int increase_elemental_resist(int source_resist) {
		return character == null || !character.isPassive(MJPassiveID.RESIST_ELEMENT.toInt()) ? source_resist : source_resist + 5;
	}

	public void addFire(int i) {
		fire = checkRange(fire + i);
	}

	public void addWater(int i) {
		water = checkRange(water + i);
	}

	public void addWind(int i) {
		wind = checkRange(wind + i);
	}

	public void addEarth(int i) {
		earth = checkRange(earth + i);
	}

	public void addcalcPcDefense(int i) {
		calcPcDefense = checkRange(calcPcDefense + i);
	}
	
	

	public void addPVPweaponTotalDamage(int i) {
		PVPweaponTotalDamage = checkRange(PVPweaponTotalDamage + i);
	}

	public void addAllNaturalResistance(int i) {
		addFire(i);
		addWater(i);
		addWind(i);
		addEarth(i);
	}

	public int getMrAfterEraseRemove() {
		int effectedMr = getMr();
		if (character.hasSkillEffect(L1SkillId.ERASE_MAGIC)) {
			character.removeSkillEffect(L1SkillId.ERASE_MAGIC);
			character.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 55, false));
			effectedMr /= 2; // 25%
		}
		return effectedMr;
	}
}
