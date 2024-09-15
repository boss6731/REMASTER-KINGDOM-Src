package l1j.server.server.serverpackets;


import l1j.server.MJInstanceSystem.MJInstanceObject;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.StringUtil;

public class S_OtherCharPacks extends ServerBasePacket {

	private static final String S_OTHER_CHAR_PACKS = "[S] S_OtherCharPacks";
	private byte[] _byte = null;
	private static final byte[] MINUS_BYTES = { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x01 };
	public S_OtherCharPacks(L1PcInstance pc, L1PcInstance user){
		buildPacket(pc, user);
	}
	
	private void buildPacket(L1PcInstance pc, L1PcInstance user){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x0077);

		writeC(0x08);//座標
		writeBit(pc.getX(), pc.getY());

		writeC(0x10); // 物件編號
		writeBit(pc.getId());

		writeC(0x18);//圖像
		if (pc.isDead()){
			writeBit(pc.getTempCharGfxAtDead());
		} else {
			writeBit(pc.getTempCharGfx());
		}
		
		writeC(0x20);
		if(pc.isDead()){
			writeBit(pc.getStatus());
		} else if (pc.isPrivateShop()){
			writeBit(ActionCodes.ACTION_Shop);
		} else if (pc.isFishing()){
			writeBit(ActionCodes.ACTION_Fishing);
		} else {
			writeBit(pc.getCurrentWeapon());
		}

		writeC(0x28); //方向
		writeC(pc.getMoveState().getHeading());

		writeC(0x30);// 光照
		writeC(pc.getLight().getChaLightSize());

		writeC(0x38);// 物件數量
		writeC(1);

		writeC(0x40);// 正義值
		writeBit(pc.getLawful());

		writeC(0x4a);// 名字
		writeBytesWithLength(pc.getName().getBytes());

		writeC(0x52);// 稱號
		if(StringUtil.isNullOrEmpty(pc.getTitle())){
			writeC(0x00);
		}
		else{
			writeBytesWithLength(pc.getTitle().getBytes());
		}

		writeC(0x58);// 速度 1加速, 2減速
		writeB(pc.isHaste());

		writeC(0x60);// 第二速度 1勇氣, 2減速, 3迅捷, 4快速移動, 5亡者, 6血之狂怒
		int brave = 0;
		if(pc.isBrave()){
			brave = 1;
		} else if(pc.isElfBrave()){
			brave = 3;
		} else if(pc.isFastMovable()){
			brave = (pc.isPassive(MJPassiveID.MOVING_ACCELERATION_PASS.toInt()) || pc.isPassive(MJPassiveID.HOLY_WALK_EVOLUTION.toInt())) ? 3 : 4;
//					isPassive.isStatus(L1PassiveConstruct.MOVING_ACCELERATION_LAST) || pc.getPassiveSkill().isStatus(L1PassiveConstruct.HOLY_WALK_EVOLUTION)) ? 3 : 4;
		} else if(pc.isFruit()){
			brave = pc.isPassive(MJPassiveID.DARK_HORSE.toInt()) ? 3 : 4;
//					pc.getPassiveSkill().isStatus(L1PassiveConstruct.DARKHORSE) ? 3 : 4;
		} else if(pc.hasSkillEffect(L1SkillId.BLOOD_LUST)){
//				isBloodLust()){
			brave = 6;
		} else if(pc.hasSkillEffect(L1SkillId.HURRICANE)){
//				isHurricane()){
			brave = 9;
		} else if(pc.hasSkillEffect(L1SkillId.FOCUS_WAVE)){
//				isFocusWave()){
			brave = 13;
		}
		writeC(brave);
		
		writeC(0x68); //三段加速
		writeC(pc.getPearl() == 1 ? 8 : 0 );
//				.isThirdSpeed()? 8 : 0);

		writeC(0x70);// 幽靈
		writeB(pc.isGhost());

		writeH(0x180);// 用戶識別
		writeC(1);

		writeH(0x0188);// 隱形
		writeB(pc.isInvisble() || pc.isGmInvis());

		writeH(0x0198);// 血盟徽章
		writeBit(pc.getClanid() > 0 ? pc.getClan().getEmblemId() : 0);

		writeH(0x01a2);// 血盟名字
		if(pc.getClanid() == 0 || StringUtil.isNullOrEmpty(pc.getClanname())){
			writeC(0);
		} else{
			writeBytesWithLength(pc.getClanname().getBytes());
		}

		writeH(0x01aa);// 寵物主人名字
		writeC(0);

		writeH(0x01b0);// 高度
		writeC(0);

		writeH(0x01b8);// HP
		if((pc.isInParty() && pc.getParty().isMember(user))){
			writeC((100 * pc.getCurrentHp()) / pc.getMaxHp());
		} else {
			writeByte(MINUS_BYTES);
		}

		writeH(0x01c0);// NPC 等級
		writeC(0);

		writeH(0x01ca);// 商店聊天
		if(pc.getShopChat() == null || pc.getShopChat().length <= 0){
			writeC(0);
		} else{
			writeBytesWithLength(pc.getShopChat());
		}

		writeH(0x01d0);// 武器圖像
		writeByte(MINUS_BYTES);

		writeH(0x01d8);// 伴侶
		writeC(0);

		writeH(0x01e0);// 邊界等級
		int value = 0;
		if(pc.getLevel() >= 80){
			value = 11;
		} else if(pc.getLevel() >= 55){
			value = (pc.getLevel() - 25) / 5;
		} else if(pc.getLevel() >= 52){
			value = 5;
		} else if(pc.getLevel() >= 50){
			value = 4;
		} else if(pc.getLevel() >= 15){
			value = pc.getLevel() / 15;
		}
		writeC(value);
		
		writeH(0x01f0);// MP
		if((pc.isInParty() && pc.getParty().isMember(user))){
			writeC((100 * pc.getCurrentMp()) / pc.getMaxMp());
		} else {
        	writeByte(MINUS_BYTES);
		}
		
		
		writeH(0x0280);// 伺服器編號
		writeC(0);
//		writeBit(Config.SERVER_NUMBER);
		
		writeH(0x0288);
		/** 2016.11.26 MJ App Center LFC **/
		MJInstanceObject instobj = MJInstanceSpace.getInstance().getOpenObject(pc.getMapId());
		if(instobj != null)
			writeC(instobj.getMarkStatus(pc));
		else if(pc.getMapId() == 99/* && pc.getMapId() <= 1710*/) { //其他人顯示的標準
			if(pc.getClanid() == user.getClanid()) {
				writeC(3); //同血盟 天鵝
			} else {
				writeC(9); //不同血盟 蛇
			}
		} else {
			if(pc.getClanid() == 290040001) { //紅色騎士團
				writeC(7); // 血盟徽號編號
			} else {
				writeC(0); // 血盟徽號編號
			}
		}

		writeH(0x02f0); // 職業類型
		writeC(pc.getType());

		writeH(0x0390); // 四段加速
		writeB(pc.isFourgear());

		writeH(0x039a); // 速度 proto
		if(pc.isKnight() && pc.isPassive(MJPassiveID.RAISING_WEAPON.toInt()) && pc.getCurrentWeapon() == 50){
//				.getPassiveSkill().isStatus(L1PassiveConstruct.RAIGING_WEAPON) && pc.getCurrentWeapon() == L1ItemWeaponIdConstruct.TOHAND_SWORD.getId()){
			writeBytesWithLength(S_SpeedChange.RAIGING_WEAPONE_ATTACK_ON);
		} else if(pc.hasSkillEffect(L1SkillId.VANGUARD)){
			if (pc.getVanguardType()){
				writeBytesWithLength(S_SpeedChange.BANGUARD_LONG_FORM_ON);// 遠距離形態
			} else if (!pc.getVanguardType()){
				writeBytesWithLength(S_SpeedChange.BANGUARD_SHORT_FORM_ON);// 近距離形態
			} else {
				writeC(0x00);
			}
		} else if(pc.hasSkillEffect(L1SkillId.SHOCK_ATTACK)){
			writeBytesWithLength(S_SpeedChange.SHOCK_ATTACK_ON);
		} else if(pc.hasSkillEffect(L1SkillId.BLIND_HIDING) && pc.isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())){
			writeBytesWithLength(S_SpeedChange.BLIND_HIDING_ON);
		} else{
			writeC(0x00);
		}
		
		writeH(0);
	}



	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_OTHER_CHAR_PACKS;
	}

}
