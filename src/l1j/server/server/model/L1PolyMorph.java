 package l1j.server.server.model;

 import java.util.HashMap;
 import java.util.Map;
 import l1j.server.Config;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_POLYMORPH_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.server.datatables.PolyTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_ChangeShape;
 import l1j.server.server.serverpackets.S_CharVisualUpdate;
 import l1j.server.server.serverpackets.S_CloseList;
 import l1j.server.server.serverpackets.S_OwnCharStatus;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillIconGFX;
 import l1j.server.server.serverpackets.ServerBasePacket;
































 public class L1PolyMorph
 {
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
     private static final Map<Integer, Integer> weaponFlgMap = new HashMap<>();

     static {
         weaponFlgMap.put(Integer.valueOf(1), Integer.valueOf(2));
         weaponFlgMap.put(Integer.valueOf(2), Integer.valueOf(1));
         weaponFlgMap.put(Integer.valueOf(3), Integer.valueOf(4));
         weaponFlgMap.put(Integer.valueOf(4), Integer.valueOf(256));
         weaponFlgMap.put(Integer.valueOf(5), Integer.valueOf(16));
         weaponFlgMap.put(Integer.valueOf(6), Integer.valueOf(8));
         weaponFlgMap.put(Integer.valueOf(7), Integer.valueOf(32));
         weaponFlgMap.put(Integer.valueOf(8), Integer.valueOf(256));
         weaponFlgMap.put(Integer.valueOf(9), Integer.valueOf(256));
         weaponFlgMap.put(Integer.valueOf(10), Integer.valueOf(256));
         weaponFlgMap.put(Integer.valueOf(11), Integer.valueOf(128));
         weaponFlgMap.put(Integer.valueOf(12), Integer.valueOf(64));
         weaponFlgMap.put(Integer.valueOf(13), Integer.valueOf(256));
         weaponFlgMap.put(Integer.valueOf(14), Integer.valueOf(16));
         weaponFlgMap.put(Integer.valueOf(15), Integer.valueOf(8));
         weaponFlgMap.put(Integer.valueOf(16), Integer.valueOf(32));
         weaponFlgMap.put(Integer.valueOf(17), Integer.valueOf(512));
         weaponFlgMap.put(Integer.valueOf(18), Integer.valueOf(1024));
     }

     private static final Map<Integer, Integer> armorFlgMap = new HashMap<>();

     static {
         armorFlgMap.put(Integer.valueOf(1), Integer.valueOf(1));
         armorFlgMap.put(Integer.valueOf(2), Integer.valueOf(16));
         armorFlgMap.put(Integer.valueOf(3), Integer.valueOf(8));
         armorFlgMap.put(Integer.valueOf(4), Integer.valueOf(32));
         armorFlgMap.put(Integer.valueOf(5), Integer.valueOf(256));
         armorFlgMap.put(Integer.valueOf(6), Integer.valueOf(1024));
         armorFlgMap.put(Integer.valueOf(7), Integer.valueOf(128));
         armorFlgMap.put(Integer.valueOf(7), Integer.valueOf(128));
         armorFlgMap.put(Integer.valueOf(8), Integer.valueOf(2));
         armorFlgMap.put(Integer.valueOf(9), Integer.valueOf(512));
         armorFlgMap.put(Integer.valueOf(10), Integer.valueOf(64));
         armorFlgMap.put(Integer.valueOf(12), Integer.valueOf(4));
     }


     private int _id;

     private String _name;

     private int _polyId;
     private int _minLevel;
     private int _weaponEquipFlg;
     private int _armorEquipFlg;
     private boolean _canUseSkill;
     private int _causeFlg;
     private int _option;
     private boolean _spearGfx;

     public L1PolyMorph(int id, String name, int polyId, int minLevel, int weaponEquipFlg, int armorEquipFlg, boolean canUseSkill, int causeFlg, int option, boolean spearGfx) {
         this._id = id;
         this._name = name;
         this._polyId = polyId;
         this._minLevel = minLevel;
         this._weaponEquipFlg = weaponEquipFlg;
         this._armorEquipFlg = armorEquipFlg;
         this._canUseSkill = canUseSkill;
         this._causeFlg = causeFlg;
         this._option = option;
         this._spearGfx = spearGfx;
     }

     public int getId() {
         return this._id;
     }

     public String getName() {
         return this._name;
     }

     public int getPolyId() {
         return this._polyId;
     }

     public int getMinLevel() {
         return this._minLevel;
     }

     public int getWeaponEquipFlg() {
         return this._weaponEquipFlg;
     }

     public int getArmorEquipFlg() {
         return this._armorEquipFlg;
     }

     public boolean canUseSkill() {
         return this._canUseSkill;
     }

     public int getCauseFlg() {
         return this._causeFlg;
     }
     public boolean isSpearGfx() {
         return this._spearGfx;
     }


     public int getOption() {
         return this._option;
     }

     public static void handleCommands(L1PcInstance pc, String s) {
         if (pc == null || pc.isDead()) {
             return;
         }
         L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);

         if (poly != null || s.equals("none")) {
             if (s.equals("none")) {
                 pc.removeSkillEffect(67);
                 pc.sendPackets((ServerBasePacket)new S_CloseList(pc.getId()));
             } else if (pc.getLevel() >= poly.getMinLevel() || pc.isGm() || Config.ServerAdSetting.PolyEvent) {
                 doPoly((L1Character)pc, poly.getPolyId(), 7200, 1, false, false);
                 pc.sendPackets((ServerBasePacket)new S_CloseList(pc.getId()));
             } else {
                 pc.sendPackets((ServerBasePacket)new S_ServerMessage(181));
             }
         }
     }

     public static void doPoly(L1Character cha, int polyId, int timeSecs, int cause, boolean ring, boolean ring2) {
         int forcepolyid = 0;
         forcepolyid = polyId;
         if (cha instanceof L1PcInstance) {
             L1PcInstance pc = (L1PcInstance)cha;
             if (pc.get_ForcePolyId() != 0) {
                 forcepolyid = pc.get_ForcePolyId();
             }
         }
         doPoly(cha, forcepolyid, timeSecs, cause, ring, ring2, false);
     }




     public static void doPoly(L1Character cha, int polyId, int timeSecs, int cause, boolean ring, boolean ring2, boolean login) {
         try {
             if (cha == null || cha.isDead()) {
                 return;
             }
             if (cha instanceof L1PcInstance) {
                 L1PcInstance pc = (L1PcInstance)cha;
                 if (pc.getMapId() == 5302 || pc.getMapId() == 5490 || pc.getMapId() == 5153) {
                     pc.sendPackets((ServerBasePacket)new S_ServerMessage(1170));

                     return;
                 }
                 if (pc.getCurrentSpriteId() == 6034 || pc.getCurrentSpriteId() == 6035) {
                     pc.sendPackets((ServerBasePacket)new S_ServerMessage(181));
                     return;
                 }
                 if (!isMatchCause(polyId, cause)) {
                     pc.sendPackets((ServerBasePacket)new S_ServerMessage(181));

                     return;
                 }
                 if (pc.isAutoSetting()) {
                     pc.setAutoPolyID(polyId);

                     return;
                 }

                 L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId); // 從變形表中獲取變形模板
                 if (!pc.isGm() && pc.noPlayerCK && pc.noPlayerck2 && pc.noPlayerRobot && poly == null) {
// 如果玩家不是管理員，且開啟了 noPlayerCK、noPlayerck2、noPlayerRobot 設置，並且找不到變形模板
                     System.out.println(String.format("在變形表中找不到變形信息。變形編號: %d, 用戶名: %s", new Object[] { Integer.valueOf(polyId), pc.getName() }));
// 打印錯誤信息到控制台
                     pc.sendPackets("嘗試以異常方式進行變形。"); // 向玩家發送提示信息
                     return; // 結束方法
                 }

                 if (!pc.isGm() && pc.noPlayerCK && pc.noPlayerck2 && pc.noPlayerRobot && poly.getOption() != 0) {
// 如果玩家不是管理員，且開啟了 noPlayerCK、noPlayerck2、noPlayerRobot 設置，並且變形選項不為 0
                     if (poly.getOption() == 0) {
                         return; // 如果選項為 0，直接返回
                     }
                     polyId = poly.PolyOption(cha, polyId); // 根據選項更新變形編號
                 }

                 if (polyId == 0) {
                     pc.sendPackets("嘗試以異常方式進行變形。"); // 如果變形編號為 0，向玩家發送提示信息
                     return; // 結束方法
                 }

                 if (pc.hasSkillEffect(67)) {
                     pc.killSkillEffectTimer(67);
                 }


                 if (pc.hasSkillEffect(80012)) {
                     pc.getAbility().addAddedStr(-1);
                     pc.getAbility().addAddedDex(-1);
                     pc.getAbility().addAddedCon(-1);
                     pc.getAbility().addAddedInt(-1);
                     pc.getAbility().addAddedWis(-1);
                     pc.getAbility().addAddedCha(-1);
                     pc.addMaxHp(-200);
                     pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                     L1SkillUse.off_icons(pc, 80012);
                     pc.killSkillEffectTimer(80012);
                 } else if (pc.hasSkillEffect(80013)) {
                     pc.getAbility().addAddedStr(-5);
                     pc.getAbility().addAddedDex(-5);
                     pc.getAbility().addAddedCon(-5);
                     pc.getAbility().addAddedInt(-5);
                     pc.getAbility().addAddedWis(-5);
                     pc.getAbility().addAddedCha(-5);
                     pc.addMaxHp(-500);
                     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -5);
                     pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                     L1SkillUse.off_icons(pc, 80013);
                     pc.killSkillEffectTimer(80013);
                     SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                 }

                 if (ring) {
                     pc.getAbility().addAddedStr(1);
                     pc.getAbility().addAddedDex(1);
                     pc.getAbility().addAddedCon(1);
                     pc.getAbility().addAddedInt(1);
                     pc.getAbility().addAddedWis(1);
                     pc.getAbility().addAddedCha(1);
                     pc.addMaxHp(200);
                     pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                     pc.setSkillEffect(80012, (timeSecs * 1000));
                 } else if (ring2) {
                     pc.getAbility().addAddedStr(5);
                     pc.getAbility().addAddedDex(5);
                     pc.getAbility().addAddedCon(5);
                     pc.getAbility().addAddedInt(5);
                     pc.getAbility().addAddedWis(5);
                     pc.getAbility().addAddedCha(5);
                     pc.addMaxHp(500);
                     pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, 5);
                     SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
                     pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
                     pc.setSkillEffect(80013, (timeSecs * 1000));
                 } else {
                     pc.setSkillEffect(67, (timeSecs * 1000));
                 }

                 L1ItemInstance weapon = pc.getWeapon();
                 boolean weaponTakeoff = (weapon != null && !isEquipableWeapon(polyId, weapon.getItem().getType()));

                 if (pc.getCurrentSpriteId() != polyId) {
                     pc.sendPackets((ServerBasePacket)new S_ChangeShape(pc.getId(), polyId, weaponTakeoff));
                     if (!pc.isGmInvis()) {
                         if (pc.isInvisble()) {
                             pc.broadcastPacketForFindInvis((ServerBasePacket)new S_ChangeShape(pc.getId(), polyId), true);
                         } else {
                             pc.broadcastPacket((ServerBasePacket)new S_ChangeShape(pc.getId(), polyId));
                         }
                     }

                     pc.setCurrentSprite(polyId);

                     weapon = pc.getWeapon();
                     if (weapon != null) {
                         S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc);
                         pc.sendPackets((ServerBasePacket)charVisual);
                         pc.broadcastPacket((ServerBasePacket)charVisual);
                     }

                     if (ring) {
                         SC_POLYMORPH_NOTI.send(pc, polyId);
                         L1SkillUse.on_icons(pc, 80012, timeSecs);
                     } else if (ring2) {
                         SC_POLYMORPH_NOTI.send(pc, polyId);
                         L1SkillUse.on_icons(pc, 80013, timeSecs);
                     }
                 }
                 if (ring && ring2) {
                     ring = false;
                     ring2 = true;
                 }

                 if (timeSecs > 0) {
                     pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(35, timeSecs, ring, ring2));
                 }


                 pc.getInventory().takeoffEquip(polyId);
                 pc.send_tarobj_party_effect(pc.getId(), 6130);
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(160, pc, weapon), true);
             } else if (cha instanceof L1MonsterInstance) {
                 L1MonsterInstance mob = (L1MonsterInstance)cha;
                 mob.killSkillEffectTimer(67);
                 mob.setSkillEffect(67, (timeSecs * 1000));
                 if (mob.getCurrentSpriteId() != polyId) {
                     mob.setCurrentSprite(polyId);
                     mob.broadcastPacket((ServerBasePacket)new S_ChangeShape(mob.getId(), polyId));
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }


     public static void doPolyPraivateShop(L1Character cha, int polyIndex) {
         if (cha == null || cha.isDead()) {
             return;
         }
         if (cha instanceof L1PcInstance) {
             L1PcInstance pc = (L1PcInstance)cha;

             int[] PolyList = { 11479, 11427, 10047, 9688, 11322, 10069, 10034, 10032 };
             if (pc.getCurrentSpriteId() != PolyList[polyIndex - 1]) {
                 pc.setCurrentSprite(PolyList[polyIndex - 1]);
                 L1ItemInstance weapon = pc.getWeapon();
                 boolean weaponTakeoff = (weapon != null && !isEquipableWeapon(PolyList[polyIndex - 1], weapon.getItem().getType()));
                 if (weaponTakeoff) {
                     pc.getInventory().setEquipped(weapon, false);
                 }
                 pc.sendPackets((ServerBasePacket)new S_ChangeShape(pc.getId(), PolyList[polyIndex - 1], 70));
                 if (!pc.isGmInvis() && !pc.isInvisble()) {
                     Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_ChangeShape(pc.getId(), PolyList[polyIndex - 1], 70));
                 }
             }
             pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate((L1Character)pc, 70));
             Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_CharVisualUpdate((L1Character)pc, 70));
         }
     }

     public static void undoPolyPrivateShop(L1Character cha) {
         if (cha instanceof L1PcInstance) {
             L1PcInstance pc = (L1PcInstance)cha;
             int classId = pc.getClassId();
             pc.setCurrentSprite(classId);
             if (!pc.isDead()) {
                 pc.sendPackets((ServerBasePacket)new S_ChangeShape(pc.getId(), classId, pc.getCurrentWeapon()));
                 Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_ChangeShape(pc.getId(), classId, pc.getCurrentWeapon()));
                 pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate((L1Character)pc, pc.getCurrentWeapon()));
                 Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_CharVisualUpdate((L1Character)pc, pc.getCurrentWeapon()));
             }
         }
     }

     public static void undoPoly(L1Character cha) {
         if (cha instanceof L1PcInstance) {
             L1PcInstance pc = (L1PcInstance)cha;

             if (pc.getMapId() == 5143) {
                 doPoly((L1Character)pc, 5065, 1000, 4, false, false);
                 return;
             }
             int classId = pc.getClassId();
             pc.setCurrentSprite(classId);
             pc.sendShape(classId);
             L1ItemInstance weapon = pc.getWeapon();

             if (pc.isDragonknight()) {
                 for (L1ItemInstance items : pc.getInventory().getItems()) {
                     if (items.getItem().getType() == 18 &&
                             items.getItem().getType1() == 50) {
                         items.getItem().setType1(24);
                         if (weapon != null) {
                             pc.getInventory().setEquipped(weapon, false);
                             pc.getInventory().setEquipped(weapon, true);
                         }
                     }
                 }
             }

             if (weapon != null) {
                 S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc);
                 pc.sendPackets((ServerBasePacket)charVisual);
                 pc.broadcastPacket((ServerBasePacket)charVisual);
             }
             pc.sendPackets((ServerBasePacket)new S_PacketBox(160, pc, weapon), true);
             if (pc.isPolyRingMaster2() || pc.isPolyRingMaster()) {
                 boolean ring = false;
                 boolean ring2 = false;
                 if (pc.isPolyRingMaster2()) {
                     ring = false;
                     ring2 = true;
                 } else if (pc.isPolyRingMaster()) {
                     ring = true;
                     ring2 = false;
                 }
                 if (ring && ring2) {
                     ring = false;
                     ring2 = true;
                 }
                 pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(35, 0, ring, ring2));
                 L1SkillUse.off_icons(pc, 80012);
                 L1SkillUse.off_icons(pc, 80013);


             }


         }
         else if (cha instanceof L1MonsterInstance) {
             L1MonsterInstance mob = (L1MonsterInstance)cha;
             mob.setCurrentSprite(mob.getNpcTemplate().get_gfxid());
             mob.broadcastPacket((ServerBasePacket)new S_ChangeShape(mob.getId(), mob.getCurrentSpriteId()));
         }
     }

     public static void MagicBookPoly(L1PcInstance pc, String s, int time) {
         if (pc == null || pc.isDead()) {
             return;
         }
         L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
         if (poly != null) {
             doPoly((L1Character)pc, poly.getPolyId(), time, 1, false, false);
             pc.sendPackets((ServerBasePacket)new S_CloseList(pc.getId()));
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

         Integer flg = weaponFlgMap.get(Integer.valueOf(weaponType));
         if (flg != null) {
             return (0 != (poly.getWeaponEquipFlg() & flg.intValue()));
         }
         return true;
     }

     public static boolean isEquipableArmor(int polyId, int armorType) {
         L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);
         if (poly == null) {
             return true;
         }

         Integer flg = armorFlgMap.get(Integer.valueOf(armorType));
         if (flg != null) {
             return (0 != (poly.getArmorEquipFlg() & flg.intValue()));
         }
         return true;
     }

     public static boolean isMatchCause(int polyId, int cause) {
         L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);

         if (poly == null) {
             return true;
         }
         if (cause == 0) {
             return true;
         }
         return (0 != (poly.getCauseFlg() & cause));
     }


     private int PolyOption(L1Character cha, int polyId) {
         try {
             L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId); // 從變形表中獲取變形模板
             if (cha instanceof L1PcInstance) {
                 L1PcInstance pc = (L1PcInstance) cha; // 將角色轉型為玩家實例

                 // 處理變形選項為 1 的情況
                 if (poly.getOption() == 1) {
                     // 無需額外檢查，因為上面已經確定了 poly.getOption() == 1
                 }
                 // 處理變形選項為 2 的情況
                 else if (poly.getOption() == 2) {
                     boolean hasRequiredItem = pc.getInventory().checkItem(4100023) || pc.getInventory().checkItem(4100024) ||
                             pc.getInventory().checkItem(4100025) || pc.getInventory().checkItem(4100026) ||
                             pc.getInventory().checkItem(4100027) || pc.getInventory().checkItem(4100028) ||
                             pc.getInventory().checkItem(4100029) || pc.getInventory().checkItem(4100030) ||
                             pc.getInventory().checkItem(4100031) || pc.getInventory().checkItem(4100032);

                     boolean isPolyRingMaster = pc.isPolyRingMaster() || pc.isPolyRingMaster2();

                     if (!hasRequiredItem && (poly.getOption() != 2 || !isPolyRingMaster)) {
                         System.out.println(pc.getName() + " 嘗試使用變形 bug。");
                         polyId = 0; // 設置變形 ID 為 0，防止變形
                     }
                 }
                 // 處理變形選項為 3 的情況
                 else if (poly.getOption() == 3) {
                     if (!pc.getInventory().checkItem(4100133)) {
                         System.out.println(pc.getName() + " 嘗試使用變形 bug。");
                         polyId = 0; // 設置變形 ID 為 0，防止變形
                     }
                 }
                 // 處理變形選項為 4 的情況
                 else if (poly.getOption() == 4) {
                     System.out.println(pc.getName() + " 嘗試使用變形 bug。");
                     polyId = 0; // 設置變形 ID 為 0，防止變形
                 }
                 // 處理變形選項為 5 的情況
            else if (poly.getOption() == 5 && !pc.getInventory().checkItem(900077)) {
                     System.out.println(pc.getName() + " 嘗試使用變形 bug。");
                     polyId = 0; // 設置變形 ID 為 0，防止變形
                 }
             }
         } catch (Exception e) {
             e.printStackTrace(); // 處理異常，打印堆棧跟蹤
         }
         return polyId; // 返回變形 ID
     }

             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         return polyId;
     }

     private static String[] _replacePolyName = new String[] { "prince", "knight", "elf", "wizard", "darkelf", "dragonknight", "illusionist", "warrior", "fencer", "lancer" };
     public static String getReplacePolyName(int type) {
         return _replacePolyName[type];
     }
 }


