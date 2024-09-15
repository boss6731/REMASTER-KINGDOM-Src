package l1j.server.server.model.item.collection.time.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
//import l1j.server.server.model.L1Ability;
//import l1j.server.server.model.L1Resistance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_HPUpdate;
//import l1j.server.server.serverpackets.S_FourthSpeed;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.utils.StringUtil;

/**
 * 실렉티스 전시회 옵션 오브젝트
 * @author LinOffice
 */
public class L1TimeCollectionAblity {
	private int flag;
	private int sum;
	private L1TimeCollectionBuffType buffType;
	private int ac;
	private int str, con, dex, inti, wis, cha;
	private int shortDamage, shortHit, shortCritical;
	private int longDamage, longHit, longCritical;
	private int spellpower, magicHit, magicCritical;
	private int hp, mp, hpr, mpr;
	private int attrFire, attrWater, attrWind, attrEarth, attrAll;
	private int mr;
	private int weight;
	private int dg, er, me;
	private int reduction, reductionEgnor, reductionMagic;
	private int PVPDamage, PVPReduction, PVPReductionEgnor, PVPReductionMagic, PVPReductionMagicEgnor;
	private int toleranceSkill, toleranceSpirit, toleranceDragon, toleranceFear, toleranceAll;
	private int hitupSkill, hitupSpirit, hitupDragon, hitupFear, hitupAll;
	private int exp;
	private int imunEgnor;
	private int strangeTime;
	private boolean firstSpeed, secondSpeed, thirdSpeed, forthSpeed;
	
	public L1TimeCollectionAblity(ResultSet rs) throws SQLException {
		this(
				rs.getInt("flag"), rs.getInt("sum"),
				L1TimeCollectionBuffType.getType(rs.getString("buffType")),
				rs.getInt("ac"),
				rs.getInt("str"), rs.getInt("con"), rs.getInt("dex"), rs.getInt("int"), rs.getInt("wis"), rs.getInt("cha"),
				rs.getInt("shortDamage"), rs.getInt("shortHit"), rs.getInt("shortCritical"),
				rs.getInt("longDamage"), rs.getInt("longHit"), rs.getInt("longCritical"),
				rs.getInt("spellpower"), rs.getInt("magicHit"), rs.getInt("magicCritical"),
				rs.getInt("hp"), rs.getInt("mp"), rs.getInt("hpr"), rs.getInt("mpr"),
				rs.getInt("attrFire"), rs.getInt("attrWater"), rs.getInt("attrWind"), rs.getInt("attrEarth"), rs.getInt("attrAll"),
				rs.getInt("mr"), rs.getInt("weight"), rs.getInt("dg"), rs.getInt("er"), rs.getInt("me"),
				rs.getInt("reduction"), rs.getInt("reductionEgnor"), rs.getInt("reductionMagic"),
				rs.getInt("PVPDamage"), rs.getInt("PVPReduction"), rs.getInt("PVPReductionEgnor"), rs.getInt("PVPReductionMagic"), rs.getInt("PVPReductionMagicEgnor"),
				rs.getInt("toleranceSkill"), rs.getInt("toleranceSpirit"), rs.getInt("toleranceDragon"), rs.getInt("toleranceFear"), rs.getInt("toleranceAll"),
				rs.getInt("hitupSkill"), rs.getInt("hitupSpirit"), rs.getInt("hitupDragon"), rs.getInt("hitupFear"), rs.getInt("hitupAll"),
				rs.getInt("exp"), rs.getInt("imunEgnor"), rs.getInt("strangeTime"),
				Boolean.valueOf(rs.getString("firstSpeed")), Boolean.valueOf(rs.getString("secondSpeed")), Boolean.valueOf(rs.getString("thirdSpeed")), Boolean.valueOf(rs.getString("forthSpeed"))
				);
	}
	
	public L1TimeCollectionAblity(int flag, int sum,
			L1TimeCollectionBuffType buffType, int ac, 
			int str, int con, int dex, int inti, int wis, int cha, 
			int shortDamage, int shortHit, int shortCritical, 
			int longDamage, int longHit, int longCritical,
			int spellpower, int magicHit, int magicCritical, 
			int hp, int mp, int hpr, int mpr, 
			int attrFire, int attrWater, int attrWind, int attrEarth, int attrAll, 
			int mr, int weight, int dg, int er, int me, 
			int reduction, int reductionEgnor, int reductionMagic,
			int pVPDamage, int pVPReduction, int pVPReductionEgnor, int pVPReductionMagic, int pVPReductionMagicEgnor,
			int toleranceSkill, int toleranceSpirit, int toleranceDragon, int toleranceFear, int toleranceAll, 
			int hitupSkill, int hitupSpirit, int hitupDragon, int hitupFear, int hitupAll,
			int exp, int imunEgnor, int strangeTime, 
			boolean firstSpeed, boolean secondSpeed, boolean thirdSpeed, boolean forthSpeed) {
		this.flag					= flag;
		this.sum					= sum;
		this.buffType				= buffType;
		this.ac						= ac;
		this.str					= str;
		this.con					= con;
		this.dex					= dex;
		this.inti					= inti;
		this.wis					= wis;
		this.cha					= cha;
		this.shortDamage			= shortDamage;
		this.shortHit				= shortHit;
		this.shortCritical			= shortCritical;
		this.longDamage				= longDamage;
		this.longHit				= longHit;
		this.longCritical			= longCritical;
		this.spellpower				= spellpower;
		this.magicHit				= magicHit;
		this.magicCritical			= magicCritical;
		this.hp						= hp;
		this.mp						= mp;
		this.hpr					= hpr;
		this.mpr					= mpr;
		this.attrFire				= attrFire;
		this.attrWater				= attrWater;
		this.attrWind				= attrWind;
		this.attrEarth				= attrEarth;
		this.attrAll				= attrAll;
		this.mr						= mr;
		this.weight					= weight;
		this.dg						= dg;
		this.er						= er;
		this.me						= me;
		this.reduction				= reduction;
		this.reductionEgnor			= reductionEgnor;
		this.reductionMagic			= reductionMagic;
		this.PVPDamage				= pVPDamage;
		this.PVPReduction			= pVPReduction;
		this.PVPReductionEgnor		= pVPReductionEgnor;
		this.PVPReductionMagic		= pVPReductionMagic;
		this.PVPReductionMagicEgnor	= pVPReductionMagicEgnor;
		this.toleranceSkill			= toleranceSkill;
		this.toleranceSpirit		= toleranceSpirit;
		this.toleranceDragon		= toleranceDragon;
		this.toleranceFear			= toleranceFear;
		this.toleranceAll			= toleranceAll;
		this.hitupSkill				= hitupSkill;
		this.hitupSpirit			= hitupSpirit;
		this.hitupDragon			= hitupDragon;
		this.hitupFear				= hitupFear;
		this.hitupAll				= hitupAll;
		this.exp					= exp;
		this.imunEgnor				= imunEgnor;
		this.strangeTime			= strangeTime;
		this.firstSpeed				= firstSpeed;
		this.secondSpeed			= secondSpeed;
		this.thirdSpeed				= thirdSpeed;
		this.forthSpeed				= forthSpeed;
	}
	
	public int getFlag() {
		return flag;
	}
	public int getSum() {
		return sum;
	}
	public L1TimeCollectionBuffType getBuffType() {
		return buffType;
	}
	
	/**
	 * 능력치 설정
	 * @param owner
	 * @param active
	 */
	public void ablity(L1PcInstance owner, boolean active){
		int value				= active ? 1 : -1;
//		L1Ability ablity		= owner.getAbility();
//		L1Resistance resistance	= owner.getResistance();
		
		if(ac != 0)						owner.getAC().addAc(-ac * value);
		
//		if(str > 0)						ablity.addAddedStr((byte) str * value);
		if(str > 0)						owner.getAbility().addAddedStr((byte) str * value);
//		if(con > 0)						ablity.addAddedCon((byte) con * value);
		if(con > 0)						owner.getAbility().addAddedCon((byte) con * value);
//		if(dex > 0)						ablity.addAddedDex((byte) dex * value);
		if(dex > 0)						owner.getAbility().addAddedDex((byte) dex * value);
//		if(inti > 0)					ablity.addAddedInt((byte) inti * value);
		if(inti > 0)					owner.getAbility().addAddedInt((byte) inti * value);
//		if(wis > 0)						ablity.addAddedWis((byte) wis * value);
		if(wis > 0)						owner.getAbility().addAddedWis((byte) wis * value);
//		if(cha > 0)						ablity.addAddedCha((byte) cha * value);
		if(cha > 0)						owner.getAbility().addAddedCha((byte) cha * value);
//		if(shortDamage > 0)				ablity.addShortDmgup(shortDamage * value);
		if(shortDamage > 0)				owner.addDmgup(shortDamage * value);
//		if(shortHit > 0)				ablity.addShortHitup(shortHit * value);
		if(shortHit > 0)				owner.addHitup(shortHit * value);
//		if(shortCritical > 0)			ablity.addShortCritical(shortCritical * value);
		if(shortCritical > 0)			owner.add_melee_critical_rate(shortCritical * value);
//		if(longDamage > 0)				ablity.addLongDmgup(longDamage * value);
		if(longDamage > 0)				owner.addBowDmgup(longDamage * value);
//		if(longHit > 0)					ablity.addLongHitup(longHit * value);
		if(longHit > 0)					owner.addBowHitup(longHit * value);
//		if(longCritical > 0)			ablity.addLongCritical(longCritical * value);
		if(longCritical > 0)			owner.add_missile_critical_rate(longCritical * value);
//		if(spellpower > 0)				ablity.addSp(spellpower * value);
		if(spellpower > 0)				owner.getAbility().addSp(spellpower * value);
//		if(magicHit > 0)				ablity.addMagicHitup(magicHit * value);
		if(magicHit > 0)				owner.addBaseMagicHitUp(magicHit * value);
//		if(magicCritical > 0)			ablity.addMagicHitup(magicCritical * value);
		if(magicCritical > 0)			owner.addBaseMagicCritical(magicCritical * value);

		if(hp > 0)						owner.addMaxHp(hp * value);
		if(mp > 0)						owner.addMaxMp(mp * value);
		if(hpr > 0)						owner.addHpr(hpr * value);
		if(mpr > 0)						owner.addMpr(mpr * value);
	
//		if(attrFire > 0)				resistance.addFire(attrFire * value);
		if(attrFire > 0)				owner.getResistance().addFire(attrFire * value);
//		if(attrWater > 0)				resistance.addWater(attrWater * value);
		if(attrWater > 0)				owner.getResistance().addWater(attrWater * value);
//		if(attrWind > 0)				resistance.addWind(attrWind * value);
		if(attrWind > 0)				owner.getResistance().addWind(attrWind * value);
//		if(attrEarth > 0)				resistance.addEarth(attrEarth * value);
		if(attrEarth > 0)				owner.getResistance().addEarth(attrEarth * value);
//		if(attrAll > 0)					resistance.addAllNaturalResistance(attrAll * value);
		if(attrAll > 0)					owner.getResistance().addAllNaturalResistance(attrAll * value);
				
//		if(mr > 0)						resistance.addMr(mr * value);
		if(mr > 0)						owner.addMr(mr * value);
		if(weight > 0)					owner.addWeightReduction(weight * value);
//		if(dg > 0)						ablity.addDg(dg * value);
		if(dg > 0)						owner.addDg(dg * value);
//		if(er > 0)						ablity.addEr(er * value);
		if(er > 0)						owner.addEffectedER(er * value);
//		if(me > 0)						ablity.addMe(me * value);
		if(me > 0)						owner.addMagicDodgeProbability(me * value);
//		if(reduction > 0)				ablity.addDamageReduction(reduction * value);
		if(reduction > 0)				owner.addDamageReduction(reduction * value);
//		if(reductionEgnor > 0)			ablity.addDamageReductionEgnor(reductionEgnor * value);
		if(reductionEgnor > 0)			owner.addDamageReductionIgnore(reductionEgnor * value);
//		if(reductionMagic > 0)			ablity.addMagicDamageReduction(reductionMagic * value);
//		if(reductionMagic > 0)			owner.addMagicDamageReduction(reductionMagic * value);
//		if(PVPDamage > 0)				ablity.addPVPDamage(PVPDamage * value);
		if(PVPDamage > 0)				owner.getResistance().addPVPweaponTotalDamage(PVPDamage * value);
//		if(PVPReduction > 0)			ablity.addPVPDamageReduction(PVPReduction * value);
		if(PVPReduction > 0)			owner.getResistance().addcalcPcDefense(PVPReduction * value);
//		if(PVPReductionEgnor > 0)		ablity.addPVPDamageReductionEgnor(PVPReductionEgnor * value);
		if(PVPReductionEgnor > 0)		owner.add_pvp_dmg_ignore(PVPReductionEgnor * value);
//		if(PVPReductionMagic > 0)		ablity.addPVPMagicDamageReduction(PVPReductionMagic * value);
		if(PVPReductionMagicEgnor > 0)	owner.add_pvp_mdmg_ignore(PVPReductionMagicEgnor * value);
//		if(toleranceSkill > 0)			resistance.addToleranceSkill(toleranceSkill * value);
		if(toleranceSkill > 0)			owner.addSpecialResistance(eKind.ABILITY, (toleranceSkill * value));
//		if(toleranceSpirit > 0)			resistance.addToleranceSpirit(toleranceSpirit * value);
		if(toleranceSpirit > 0)			owner.addSpecialResistance(eKind.SPIRIT, (toleranceSpirit * value));
//		if(toleranceDragon > 0)			resistance.addToleranceDragon(toleranceDragon * value);
		if(toleranceDragon > 0)			owner.addSpecialResistance(eKind.DRAGON_SPELL, (toleranceDragon * value));
//		if(toleranceFear > 0)			resistance.addToleranceFear(toleranceFear * value);
		if(toleranceFear > 0)			owner.addSpecialResistance(eKind.FEAR,(toleranceFear * value));
//		if(toleranceAll > 0)			resistance.addAllTolerance(toleranceAll * value);
		if(toleranceAll > 0)			owner.addSpecialResistance(eKind.ALL,(toleranceAll * value));
//		if(hitupSkill > 0)				resistance.addHitupSkill(hitupSkill * value);
		if(hitupSkill > 0)				owner.addSpecialPierce(eKind.ABILITY, (hitupSkill * value));
//		if(hitupSpirit > 0)				resistance.addHitupSpirit(hitupSpirit * value);
		if(hitupSpirit > 0)				owner.addSpecialPierce(eKind.SPIRIT, (hitupSpirit * value));
//		if(hitupDragon > 0)				resistance.addHitupDragon(hitupDragon * value);
		if(hitupDragon > 0)				owner.addSpecialPierce(eKind.DRAGON_SPELL, (hitupDragon * value));
//		if(hitupFear > 0)				resistance.addHitupFear(hitupFear * value);
		if(hitupFear > 0)				owner.addSpecialPierce(eKind.FEAR, (hitupFear * value));
//		if(hitupAll > 0)				resistance.addAllHitup(hitupAll * value);
		if(hitupAll > 0)				owner.addSpecialPierce(eKind.ALL, (hitupAll * value));
//		if(exp > 0)						owner.addBonusExp(exp * value);
		if(exp > 0)						owner.add_item_exp_bonus(exp * value);
//		if(imunEgnor > 0)				ablity.addEmunEgnor(imunEgnor * value);
		if(imunEgnor > 0)				owner.add_immune_ignore(imunEgnor * value);
//		if(strangeTime > 0)				ablity.addStrangeTime(strangeTime * value);
//		if(strangeTime > 0)				ablity.addStrangeTime(strangeTime * value);
/*		if(firstSpeed){ //1단가속 촐기
			if(active){
				owner.removeSpeedSkill();
				if(owner.getMoveState().getMoveSpeed() != 1){
					owner.getMoveState().setMoveSpeed(1);
					owner.sendPackets(new S_SkillHaste(owner.getId(), 1, -1), true);
					owner.broadcastPacket(new S_SkillHaste(owner.getId(), 1, 0), true);
				}
			}else{
				owner.getMoveState().setMoveSpeed(0);
				owner.broadcastPacketWithMe(new S_SkillHaste(owner.getId(), 0, 0), true);
			}
		}
		if(secondSpeed){//2단가속 용기
			if(active){
				if(owner.getSkill().hasSkillEffect(L1SkillId.STATUS_BRAVE))owner.getSkill().removeSkillEffect(L1SkillId.STATUS_BRAVE);
				if(owner.getMoveState().getBraveSpeed() == 0){
					owner.getMoveState().setBraveSpeed(1);
					owner.sendPackets(new S_SkillBrave(owner.getId(), 1, -1), true);
					owner.broadcastPacket(new S_SkillBrave(owner.getId(), 1, 0), true);
				}
			}else{
				owner.getMoveState().setBraveSpeed(0);
				owner.broadcastPacketWithMe(new S_SkillBrave(owner.getId(), 0, 0), true);
			}
		}
		if(thirdSpeed){//3단가속 드진주
			if(active){
				if(owner.getSkill().hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL)){
					owner.getSkill().killSkillEffectTimer(L1SkillId.STATUS_DRAGON_PEARL);
					owner.sendPackets(new S_Liquor(owner.getId(), 0), true);
					owner.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 8, 0), true);
					owner.getMoveState().setThirdSpeed(0);
				}
				owner.broadcastPacketWithMe(new S_Liquor(owner.getId(), 8), true);
				owner.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 73, true), true);
				owner.getMoveState().setThirdSpeed(1);
			}else{
				owner.broadcastPacketWithMe(new S_Liquor(owner.getId(), 0), true);
				owner.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 73, false), true);
				owner.getMoveState().setThirdSpeed(0);
			}
		}
		if(forthSpeed){//4단가속 할파인형
			owner.getMoveState().setFourthSpeed(active);
			owner.broadcastPacketWithMe(new S_FourthSpeed(owner.getId(), active), true);
		}
	*/	

		owner.sendPackets(new S_OwnCharAttrDef(owner), true);
		owner.sendPackets(new S_OwnCharStatus(owner), true);
		owner.sendPackets(new S_SPMR(owner), true);
		owner.sendPackets(new S_HPUpdate(owner));
		owner.sendPackets(new S_OwnCharAttrDef(owner));
		SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(owner);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("flag: ").append(flag).append(StringUtil.LineString);
		sb.append("sum: ").append(sum).append(StringUtil.LineString);
		sb.append("buffType: ").append(buffType.getName()).append(StringUtil.LineString);
		return sb.toString();
	}
}
