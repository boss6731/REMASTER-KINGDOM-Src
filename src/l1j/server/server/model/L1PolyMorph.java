package l1j.server.server.model;

import java.util.HashMap;
import java.util.Map;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_POLYMORPH_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_CloseList;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconGFX;

public class L1PolyMorph {
	private static final int DAGGER_EQUIP = 1;

	private static final int SWORD_EQUIP = 2;

	private static final int TWOHANDSWORD_EQUIP = 4;

	private static final int AXE_EQUIP = 8;

	private static final int SPEAR_EQUIP = 16;

	private static final int STAFF_EQUIP = 32;

	private static final int EDORYU_EQUIP = 64;

	private static final int CLAW_EQUIP = 128;

	private static final int BOW_EQUIP = 256;

	private static final int KIRINGKU_EQUIP = 512;

	private static final int CHAINSWORD_EQUIP = 1024;

	// armor equip bit
	private static final int HELM_EQUIP = 1;

	private static final int AMULET_EQUIP = 2;

	private static final int EARRING_EQUIP = 4;

	private static final int TSHIRT_EQUIP = 8;

	private static final int ARMOR_EQUIP = 16;

	private static final int CLOAK_EQUIP = 32;

	private static final int BELT_EQUIP = 64;

	private static final int SHIELD_EQUIP = 128;

	private static final int GARDER_EQUIP = 128;

	private static final int GLOVE_EQUIP = 256;

	private static final int RING_EQUIP = 512;

	private static final int BOOTS_EQUIP = 1024;

	public static final int MORPH_BY_ITEMMAGIC = 1;

	public static final int MORPH_BY_GM = 2;

	public static final int MORPH_BY_NPC = 4;

	public static final int MORPH_BY_KEPLISHA = 8;

	public static final int MORPH_BY_LOGIN = 0;

	private static final Map<Integer, Integer> weaponFlgMap = new HashMap<Integer, Integer>();

	static {
		weaponFlgMap.put(1, SWORD_EQUIP);
		weaponFlgMap.put(2, DAGGER_EQUIP);
		weaponFlgMap.put(3, TWOHANDSWORD_EQUIP);
		weaponFlgMap.put(4, BOW_EQUIP);
		weaponFlgMap.put(5, SPEAR_EQUIP);
		weaponFlgMap.put(6, AXE_EQUIP);
		weaponFlgMap.put(7, STAFF_EQUIP);
		weaponFlgMap.put(8, BOW_EQUIP);
		weaponFlgMap.put(9, BOW_EQUIP);
		weaponFlgMap.put(10, BOW_EQUIP);
		weaponFlgMap.put(11, CLAW_EQUIP);
		weaponFlgMap.put(12, EDORYU_EQUIP);
		weaponFlgMap.put(13, BOW_EQUIP);
		weaponFlgMap.put(14, SPEAR_EQUIP);
		weaponFlgMap.put(15, AXE_EQUIP);
		weaponFlgMap.put(16, STAFF_EQUIP);
		weaponFlgMap.put(17, KIRINGKU_EQUIP);
		weaponFlgMap.put(18, CHAINSWORD_EQUIP);
	}

	private static final Map<Integer, Integer> armorFlgMap = new HashMap<Integer, Integer>();

	static {
		armorFlgMap.put(1, HELM_EQUIP);
		armorFlgMap.put(2, ARMOR_EQUIP);
		armorFlgMap.put(3, TSHIRT_EQUIP);
		armorFlgMap.put(4, CLOAK_EQUIP);
		armorFlgMap.put(5, GLOVE_EQUIP);
		armorFlgMap.put(6, BOOTS_EQUIP);
		armorFlgMap.put(7, SHIELD_EQUIP);
		armorFlgMap.put(7, GARDER_EQUIP);
		armorFlgMap.put(8, AMULET_EQUIP);
		armorFlgMap.put(9, RING_EQUIP);
		armorFlgMap.put(10, BELT_EQUIP);
		armorFlgMap.put(12, EARRING_EQUIP);
	}

	private int _id;
	private String _name;
	private int _polyId;
	private int _minLevel;
	private int _weaponEquipFlg;
	private int _armorEquipFlg;
	private boolean _canUseSkill;
	private int _causeFlg;
	// 변신 버그 방지
	private int _option;
	private boolean _spearGfx;

	// 변신 버그 방지
	public L1PolyMorph(int id, String name, int polyId, int minLevel, int weaponEquipFlg, int armorEquipFlg,
			boolean canUseSkill, int causeFlg, int option, boolean spearGfx) {
		_id = id;
		_name = name;
		_polyId = polyId;
		_minLevel = minLevel;
		_weaponEquipFlg = weaponEquipFlg;
		_armorEquipFlg = armorEquipFlg;
		_canUseSkill = canUseSkill;
		_causeFlg = causeFlg;
		_option = option;
		_spearGfx = spearGfx;
	}

	public int getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public int getPolyId() {
		return _polyId;
	}

	public int getMinLevel() {
		return _minLevel;
	}

	public int getWeaponEquipFlg() {
		return _weaponEquipFlg;
	}

	public int getArmorEquipFlg() {
		return _armorEquipFlg;
	}

	public boolean canUseSkill() {
		return _canUseSkill;
	}

	public int getCauseFlg() {
		return _causeFlg;
	}
	public boolean isSpearGfx() {
		return _spearGfx;
	}
	
	// 변신 버그 방지
	public int getOption() {
		return _option;
	}

	public static void handleCommands(L1PcInstance pc, String s) {
		if (pc == null || pc.isDead()) {
			return;
		}
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
		
		if (poly != null || s.equals("none")) {
			if (s.equals("none")) {
				pc.removeSkillEffect(L1SkillId.SHAPE_CHANGE);
				pc.sendPackets(new S_CloseList(pc.getId()));
			} else if (pc.getLevel() >= poly.getMinLevel() || pc.isGm() || Config.ServerAdSetting.PolyEvent) {
				doPoly(pc, poly.getPolyId(), 7200, MORPH_BY_ITEMMAGIC, false, false);
				pc.sendPackets(new S_CloseList(pc.getId()));
			} else {
				pc.sendPackets(new S_ServerMessage(181));
			}
		}
	}
	
	public static void doPoly(L1Character cha, int polyId, int timeSecs, int cause, boolean ring, boolean ring2) {
		int forcepolyid = 0;
		forcepolyid = polyId;
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.get_ForcePolyId() != 0) {
				forcepolyid = pc.get_ForcePolyId();
			}
		}
		doPoly(cha, forcepolyid, timeSecs, cause, ring, ring2, false);
	}

	public static void doPoly(L1Character cha, int polyId, int timeSecs, int cause, boolean ring, boolean ring2, boolean login) {
		try {
			/*if (ring && !login)
				timeSecs = 3600;*/
			
			if (cha == null || cha.isDead()) {
				return;
			}
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.getMapId() == 5302 || pc.getMapId() == 5490 || pc.getMapId() == 5153) { // 낚시터,배틀존
					pc.sendPackets(new S_ServerMessage(1170)); // 이곳에서 변신할수 없습니다.
					return;
				}

				if (pc.getCurrentSpriteId() == 6034 || pc.getCurrentSpriteId() == 6035) {
					pc.sendPackets(new S_ServerMessage(181));
					return;
				}
				if (!isMatchCause(polyId, cause)) {
					pc.sendPackets(new S_ServerMessage(181)); // \f1 그러한 monster에게는 변신할 수 없습니다.
					return;
				}

				if (pc.isAutoSetting()) {
					pc.setAutoPolyID(polyId);
					return;
				}

				// 防止變身Bug
				L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);
				if(!pc.isGm() && pc.noPlayerCK && pc.noPlayerck2 && pc.noPlayerRobot && poly == null){
					System.out.println(String.format("在Poly表中找不到變身信息。變身編號: %d, 使用者名稱: %s", polyId, pc.getName()));
					pc.sendPackets("嘗試以非正常方式變身。");
					return;
				}
				
				if (!pc.isGm() && pc.noPlayerCK && pc.noPlayerck2 && pc.noPlayerRobot && poly.getOption() != 0) {
					if (poly.getOption() == 0)
						return;
					polyId = poly.PolyOption(cha, polyId);
				}
				
				if (polyId == 0) {
					pc.sendPackets("嘗試以非正常方式變身。");
					return;
				}
				
				//if (pc.getCurrentSpriteId() != polyId) {
					if(pc.hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
						pc.killSkillEffectTimer(L1SkillId.SHAPE_CHANGE);
					}

					
					if(pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER)) {
						pc.getAbility().addAddedStr((byte) -1);
						pc.getAbility().addAddedDex((byte) -1);
						pc.getAbility().addAddedCon((byte) -1);
						pc.getAbility().addAddedInt((byte) -1);
						pc.getAbility().addAddedWis((byte) -1);
						pc.getAbility().addAddedCha((byte) -1);
						pc.addMaxHp(-200);
						pc.sendPackets(new S_OwnCharStatus(pc));
						L1SkillUse.off_icons(pc, L1SkillId.POLY_RING_MASTER);
						pc.killSkillEffectTimer(L1SkillId.POLY_RING_MASTER);
					} else if(pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2)) {
						pc.getAbility().addAddedStr((byte) -5);
						pc.getAbility().addAddedDex((byte) -5);
						pc.getAbility().addAddedCon((byte) -5);
						pc.getAbility().addAddedInt((byte) -5);
						pc.getAbility().addAddedWis((byte) -5);
						pc.getAbility().addAddedCha((byte) -5);
						pc.addMaxHp(-500);
						pc.addSpecialResistance(eKind.ALL, -5);
						pc.sendPackets(new S_OwnCharStatus(pc));
						L1SkillUse.off_icons(pc, L1SkillId.POLY_RING_MASTER2);
						pc.killSkillEffectTimer(L1SkillId.POLY_RING_MASTER2);
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					}
					
					if (ring) {
						pc.getAbility().addAddedStr((byte) 1);
						pc.getAbility().addAddedDex((byte) 1);
						pc.getAbility().addAddedCon((byte) 1);
						pc.getAbility().addAddedInt((byte) 1);
						pc.getAbility().addAddedWis((byte) 1);
						pc.getAbility().addAddedCha((byte) 1);
						pc.addMaxHp(200);
						pc.sendPackets(new S_OwnCharStatus(pc));
						pc.setSkillEffect(L1SkillId.POLY_RING_MASTER, timeSecs * 1000);
					} else if (ring2) {
						pc.getAbility().addAddedStr((byte) 5);
						pc.getAbility().addAddedDex((byte) 5);
						pc.getAbility().addAddedCon((byte) 5);
						pc.getAbility().addAddedInt((byte) 5);
						pc.getAbility().addAddedWis((byte) 5);
						pc.getAbility().addAddedCha((byte) 5);
						pc.addMaxHp(500);
						pc.addSpecialResistance(eKind.ALL, 5);
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						pc.sendPackets(new S_OwnCharStatus(pc));
						pc.setSkillEffect(L1SkillId.POLY_RING_MASTER2, timeSecs * 1000);
					} else {
						pc.setSkillEffect(L1SkillId.SHAPE_CHANGE, timeSecs * 1000);
					}
			
				L1ItemInstance weapon = pc.getWeapon();
				boolean weaponTakeoff = (weapon != null && !isEquipableWeapon(polyId, weapon.getItem().getType()));

				if (pc.getCurrentSpriteId() != polyId) {
					pc.sendPackets(new S_ChangeShape(pc.getId(), polyId, weaponTakeoff));
					if (!pc.isGmInvis()) {
						if (pc.isInvisble()) {
							pc.broadcastPacketForFindInvis(new S_ChangeShape(pc.getId(), polyId), true);
						} else {
							pc.broadcastPacket(new S_ChangeShape(pc.getId(), polyId));
						}
					}
//					System.out.println(polyId);
					pc.setCurrentSprite(polyId);

					weapon = pc.getWeapon();
					if (weapon != null) {
						S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc);
						pc.sendPackets(charVisual);
						pc.broadcastPacket(charVisual);
					}

					if (ring) {
						SC_POLYMORPH_NOTI.send(pc, polyId);
						L1SkillUse.on_icons(pc, L1SkillId.POLY_RING_MASTER, timeSecs);
					} else if (ring2) {
						SC_POLYMORPH_NOTI.send(pc, polyId);
						L1SkillUse.on_icons(pc, L1SkillId.POLY_RING_MASTER2, timeSecs);
					}
				}
				if (ring && ring2){
					ring = false;
					ring2 = true;
				}
				
				if (timeSecs > 0){
					pc.sendPackets(new S_SkillIconGFX(35, timeSecs, ring, ring2));
				}
				
				
				pc.getInventory().takeoffEquip(polyId);
				pc.send_tarobj_party_effect(pc.getId(), 6130);
				pc.sendPackets(new S_PacketBox(S_PacketBox.공격가능거리, pc, weapon), true);
			} else if (cha instanceof L1MonsterInstance) {
				L1MonsterInstance mob = (L1MonsterInstance) cha;
				mob.killSkillEffectTimer(L1SkillId.SHAPE_CHANGE);
				mob.setSkillEffect(L1SkillId.SHAPE_CHANGE, timeSecs * 1000);
				if (mob.getCurrentSpriteId() != polyId) {
					mob.setCurrentSprite(polyId);
					mob.broadcastPacket(new S_ChangeShape(mob.getId(), polyId));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 3.80 변신리스트 **/
	public static void doPolyPraivateShop(L1Character cha, int polyIndex) {
		if ((cha == null) || cha.isDead()) {
			return;
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;

			int PolyList[] = { 11479, 11427, 10047, 9688, 11322, 10069, 10034, 10032 };
			if (pc.getCurrentSpriteId() != PolyList[polyIndex - 1]) {
				pc.setCurrentSprite(PolyList[polyIndex - 1]);
				L1ItemInstance weapon = pc.getWeapon();
				boolean weaponTakeoff = (weapon != null && !isEquipableWeapon(PolyList[polyIndex - 1], weapon.getItem().getType()));
				if (weaponTakeoff) {
					pc.getInventory().setEquipped(weapon, false);
				}
				pc.sendPackets(new S_ChangeShape(pc.getId(), PolyList[polyIndex - 1], ActionCodes.ACTION_Shop));
				if (!pc.isGmInvis() && !pc.isInvisble()) {
					Broadcaster.broadcastPacket(pc, new S_ChangeShape(pc.getId(), PolyList[polyIndex - 1], ActionCodes.ACTION_Shop));
				}
			}
			pc.sendPackets(new S_CharVisualUpdate(pc, ActionCodes.ACTION_Shop));
			Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc, ActionCodes.ACTION_Shop));
		}
	}

	public static void undoPolyPrivateShop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int classId = pc.getClassId();
			pc.setCurrentSprite(classId);
			if (!pc.isDead()) {
				pc.sendPackets(new S_ChangeShape(pc.getId(), classId, pc.getCurrentWeapon()));
				Broadcaster.broadcastPacket(pc, new S_ChangeShape(pc.getId(), classId, pc.getCurrentWeapon()));
				pc.sendPackets(new S_CharVisualUpdate(pc, pc.getCurrentWeapon()));
				Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc, pc.getCurrentWeapon()));
			}
		}
	}

	public static void undoPoly(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			
			if (pc.getMapId() == 5143) { // 펫레이싱
				doPoly(pc, 5065, 1000, MORPH_BY_NPC, false, false);
				return;
			}
			int classId = pc.getClassId();
			pc.setCurrentSprite(classId);
			pc.sendShape(classId);
			L1ItemInstance weapon = pc.getWeapon();
			// 용기사 포우
			if (pc.isDragonknight()) {
				for (L1ItemInstance items : pc.getInventory().getItems()) {
					if (items.getItem().getType() == 18) {
						if (items.getItem().getType1() == 50) {
							items.getItem().setType1(24);
							if (weapon != null) {
								pc.getInventory().setEquipped(weapon, false);
								pc.getInventory().setEquipped(weapon, true);
							}
						}
					}
				}
			}
			if (weapon != null) {
				S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc);
				pc.sendPackets(charVisual);
				pc.broadcastPacket(charVisual);
			}
			pc.sendPackets(new S_PacketBox(S_PacketBox.ATTACKABLE_DISTANCE, pc, weapon), true);
			if(pc.isPolyRingMaster2() || pc.isPolyRingMaster()) {
				boolean ring = false;
				boolean ring2 = false;
				if (pc.isPolyRingMaster2()){
					ring = false;
					ring2 = true;
				} else if (pc.isPolyRingMaster()){
					ring = true;
					ring2 = false;
				}
				if (ring && ring2){
					ring = false;
					ring2 = true;
				}
				pc.sendPackets(new S_SkillIconGFX(35, 0, ring, ring2));
				L1SkillUse.off_icons(pc, L1SkillId.POLY_RING_MASTER);
				L1SkillUse.off_icons(pc, L1SkillId.POLY_RING_MASTER2);
			}
//			if(pc.isPolyRingMaster2()) {
//				L1SkillUse.off_icons(pc, L1SkillId.POLY_RING_MASTER2);
//			} else if(pc.isPolyRingMaster()) {
//				L1SkillUse.off_icons(pc, L1SkillId.POLY_RING_MASTER);
//			}
		} else if (cha instanceof L1MonsterInstance) {
			L1MonsterInstance mob = (L1MonsterInstance) cha;
			mob.setCurrentSprite(mob.getNpcTemplate().get_gfxid());
			mob.broadcastPacket(new S_ChangeShape(mob.getId(), mob.getCurrentSpriteId()));
		}
	}

	public static void MagicBookPoly(L1PcInstance pc, String s, int time) {
		if (pc == null || pc.isDead()) {
			return;
		}
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
		if (poly != null) {
			doPoly(pc, poly.getPolyId(), time, MORPH_BY_ITEMMAGIC, false, false);
			pc.sendPackets(new S_CloseList(pc.getId()));
		}
		if (pc.getMagicItemId() != 0) {
			pc.getInventory().consumeItem(pc.getMagicItemId(), 1);
			pc.setMagicItemId(0);
		}
	}

	public static boolean isEquipableWeapon(int polyId, int weaponType) {
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);

		if (poly == null) {
			return true;
		}

		Integer flg = weaponFlgMap.get(weaponType);
		if (flg != null) {
			return 0 != (poly.getWeaponEquipFlg() & flg);
		}
		return true;
	}

	public static boolean isEquipableArmor(int polyId, int armorType) {
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);
		if (poly == null) {
			return true;
		}

		Integer flg = armorFlgMap.get(armorType);
		if (flg != null) {
			return 0 != (poly.getArmorEquipFlg() & flg);
		}
		return true;
	}

	public static boolean isMatchCause(int polyId, int cause) {
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);

		if (poly == null) {
			return true;
		}
		if (cause == MORPH_BY_LOGIN) {
			return true;
		}
		return 0 != (poly.getCauseFlg() & cause);
	}

	// 防止變身Bug
	private int PolyOption(L1Character cha, int polyId) {
		try {
			L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (poly.getOption() == 1) { // 一般變身卷軸
					if (poly.getOption() != 1) {
						System.out.println(pc.getName() + " 嘗試進行變身Bug。");
						polyId = 0;
					}
				} else if (poly.getOption() == 2) { // 變身支配戒指
					if (!pc.getInventory().checkItem(4100023) && !pc.getInventory().checkItem(4100024)
							&& !pc.getInventory().checkItem(4100025) && !pc.getInventory().checkItem(4100026)
							&& !pc.getInventory().checkItem(4100027) && !pc.getInventory().checkItem(4100028)
							&& !pc.getInventory().checkItem(4100029) && !pc.getInventory().checkItem(4100030)
							&& !pc.getInventory().checkItem(4100031) && !pc.getInventory().checkItem(4100032)) {
						if (poly.getOption() != 2 || !pc.isPolyRingMaster() || !pc.isPolyRingMaster2()) {
							System.out.println(pc.getName() + " 嘗試進行變身Bug。");
							polyId = 0;
						}
					}
				} else if (poly.getOption() == 3) { // 活動變身戒指
					if (!pc.getInventory().checkItem(4100133)) {
						System.out.println(pc.getName() + " 嘗試進行變身Bug。");
						polyId = 0;
					}
				} else if (poly.getOption() == 4) { // 職業變身
					if (poly.getOption() != 4) {
						System.out.println(pc.getName() + " 嘗試進行變身Bug。");
						polyId = 0;
					}
					if (pc.isCrown()) {
						if (pc.get_sex() == 0)
							polyId = 11408;
						else
							polyId = 11409;
					} else if (pc.isKnight()) {
						if (pc.get_sex() == 0)
							polyId = 11410;
						else
							polyId = 11411;
					} else if (pc.isElf()) {
						if (pc.get_sex() == 0)
							polyId = 11412;
						else
							polyId = 11413;
					} else if (pc.isWizard()) {
						if (pc.get_sex() == 0)
							polyId = 11414;
						else
							polyId = 11415;
					} else if (pc.isDarkelf()) {
						if (pc.get_sex() == 0)
							polyId = 11416;
						else
							polyId = 11417;
					} else if (pc.isDragonknight()) {
						if (pc.get_sex() == 0)
							polyId = 11418;
						else
							polyId = 11419;
					} else if (pc.isBlackwizard()) {
						if (pc.get_sex() == 0)
							polyId = 11420;
						else
							polyId = 11421;
					} else if (pc.is전사()) {
						if (pc.get_sex() == 0)
							polyId = 12613;
						else
							polyId = 12614;
					} else if (pc.isFencer()) {
						if (pc.get_sex() == 0)
							polyId = 18520;
						else
							polyId = 18499;
					} else if (pc.isLancer()) {
						if (pc.get_sex() == 0)
							polyId = 19296;
						else
							polyId = 19299;
					}
				} else if (poly.getOption() == 5) { // 活動變身
					if (!pc.getInventory().checkItem(900077)) {
						System.out.println(pc.getName() + " 嘗試進行變身Bug。");
						polyId = 0;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return polyId;
	}
	
	private static String[] _replacePolyName = { "prince", "knight", "elf", "wizard", "darkelf", "dragonknight", "illusionist", "warrior", "fencer", "lancer" };
	public static String getReplacePolyName(int type) {
		return _replacePolyName[type];
	}
}
