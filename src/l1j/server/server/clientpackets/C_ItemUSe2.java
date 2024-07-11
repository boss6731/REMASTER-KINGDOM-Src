 package l1j.server.server.clientpackets;

 import MJShiftObject.MJShiftObjectManager;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.util.ArrayList;
 import java.util.Random;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJRankSystem.Business.MJRankBusiness;
 import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.Stadium.StadiumManager;
 import l1j.server.server.Account;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.ExpTable;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.PetTypeTable;
 import l1j.server.server.model.Getback;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1GuardianInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.L1CastleLocation;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1HouseLocation;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1PcInventory;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_CharAmount;
 import l1j.server.server.serverpackets.S_CharVisualUpdate;
 import l1j.server.server.serverpackets.S_ChatPacket;
 import l1j.server.server.serverpackets.S_ItemName;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.S_OwnCharStatus2;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.S_ReturnedStat;
 import l1j.server.server.serverpackets.S_SPMR;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_Sound;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.S_UserCommands1;
 import l1j.server.server.serverpackets.S_UserCommands10;
 import l1j.server.server.serverpackets.S_UserCommands11;
 import l1j.server.server.serverpackets.S_UserCommands12;
 import l1j.server.server.serverpackets.S_UserCommands13;
 import l1j.server.server.serverpackets.S_UserCommands14;
 import l1j.server.server.serverpackets.S_UserCommands15;
 import l1j.server.server.serverpackets.S_UserCommands16;
 import l1j.server.server.serverpackets.S_UserCommands17;
 import l1j.server.server.serverpackets.S_UserCommands18;
 import l1j.server.server.serverpackets.S_UserCommands2;
 import l1j.server.server.serverpackets.S_UserCommands3;
 import l1j.server.server.serverpackets.S_UserCommands7;
 import l1j.server.server.serverpackets.S_UserCommands8;
 import l1j.server.server.serverpackets.S_UserCommands9;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1BookMark;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.templates.L1ItemBookMark;
 import l1j.server.server.templates.L1PetType;
 import l1j.server.server.types.Point;
 import l1j.server.server.utils.CommonUtil;
 import l1j.server.server.utils.L1SpawnUtil;
 import l1j.server.server.utils.MJCommons;
 import l1j.server.server.utils.SQLUtil;



 public class C_ItemUSe2
   extends ClientBasePacket
 {
   private static final String C_ITEM_USE2 = "[C] C_ItemUSe2";
   private static Random _random = new Random(System.nanoTime());

   public C_ItemUSe2(byte[] abyte0, GameClient client) throws Exception {
     super(abyte0);
     int itemObjid = readD();

     L1PcInstance pc = client.getActiveChar();
     if (pc != null) {

       if ((!MJCommons.isLock((L1Character)pc) && !MJCommons.isNonAction((L1Character)pc)) ||
         pc.hasSkillEffect(5037) || pc.hasSkillEffect(230)) {

         L1ItemInstance l1iteminstance = pc.getInventory().getItem(itemObjid);

         if (l1iteminstance != null) {

           if (!Config.Login.UseShiftServer || MJShiftObjectManager.getInstance().use_item_white_list(pc, l1iteminstance)) {
             if (!CommonUtil.teleport_check(pc, l1iteminstance)) {
               int spellsc_objid = 0;
               int l = 0;

               try { int itemId = l1iteminstance.getItem().getItemId();

                 if (!Config.ServerAdSetting.DelayTimer ||
                   l1iteminstance.getItem().get_delayEffect() > 0 ||
                   !pc.hasItemDelayTime(l1iteminstance))
                 { int j; L1Item template; int itemId2, earingId, earing2Id, potion1, potion2, rx; L1ItemInstance l1ItemInstance2; int dollId; L1ItemInstance l1ItemInstance1, Skill_item, enchant_item; long timeItem7; int ry; L1ItemInstance l1ItemInstance3; boolean isAppear; L1ItemInstance weapon_item; int ux; L1DollInstance doll; int useTimeItem7, uy; L1ItemInstance item; int n; boolean bool1; L1NpcInstance npc; int k; boolean found; int historybookId; L1MonsterInstance l1MonsterInstance1; int m; L1MonsterInstance mob; int diaryId, i1, i2, itemid[]; long timeItem; int random500, itemid2[], useTimeItem, random502, i3; long timeItem1; int useTimeItem1, i4, useTimeItem2, i5; L1GuardianInstance guardian; int random, arrayOfInt1[]; L1ItemInstance l1ItemInstance4; int count, i6; L1ItemInstance l1ItemInstance5; int[] allBuffSkill; long EXP_TIME;
                   int[] itemid1;
                   L1SkillUse l1skilluse;
                   int random501, i;
                   long EXP_TIME2;
                   int arrayOfInt2[], i7;
                   long curtimeN;
                   int itemid3[], random503;
                   long time;
                   int castle_id, house_id;
                   ArrayList<L1ItemBookMark> _books;
                   int i8;
                   switch (itemId) {

                   }

                   int loc_x = 0 + CommonUtil.random(-5, 5), loc_y = 0 + CommonUtil.random(-5, 5);
                   int[] loc = null;

                   if (itemId >= 40033 && itemId <= 40038) {
                     boolean _elixirContinue = true;
                     if (pc.getElixirStats() < 30);

                     int elix = 0;

                     if (pc.getLevel() < 100)
                     {
                       if (pc.getLevel() < 50 || pc.getLevel() >= 80)
                       {
                         if (pc.getLevel() < 80 || pc.getLevel() >= 90)
                         {
                           if (pc.getLevel() >= 90 && pc.getLevel() >= 100);
                         }
                       }
                     }
                     if (pc.getLevel() >= 50 && pc.getElixirStats() < elix);

                     if (!_elixirContinue) {

                       pc.sendPackets((ServerBasePacket)new S_ServerMessage(4472));

                       return;
                     }
                   }
                   L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(l);
                   switch (itemId) {
                     case 4100428:
                       if (l1iteminstance1.get_Carving() != 0) {
                         if (l1iteminstance1.getBless() < 128) {
                           int enchant_level = l1iteminstance1.getEnchantLevel();
                           if (enchant_level >= Config.ServerEnchant.CarvingEnchant || l1iteminstance1.getItem().get_safeenchant() == 0) {
                             if (l1iteminstance1.get_Carving() == 0)
                               break;  l1iteminstance1.set_Carving(0);
                             l1iteminstance1.setEndTime(null);

                             int st = 0;
                             if (!l1iteminstance1.isIdentified());

                             if (l1iteminstance1.getItem().isTradable());

                             if (!l1iteminstance1.getItem().isCantDelete());

                             if (l1iteminstance1.getItem().get_safeenchant() >= 0);

                             if (l1iteminstance1.getBless() < 64)
                               break;  st = 32;
                             if (!l1iteminstance1.isIdentified());
                             break;
                           }
                             pc.sendPackets("刻印武器強化 (" + Config.ServerEnchant.CarvingEnchant + ") 以下無法進行。");
                             return;
                         }
                           pc.sendPackets(new S_ServerMessage(79));
                           return;
                       }
                         pc.sendPackets(l1iteminstance1.getName() + " 不是刻印物品。");
                         return;
                     case 5990:
                       if (pc.getInventory().checkItem(itemId, 1) &&
                         !MJRankBusiness.getInstance().onExpendiant(pc));
                       break;
                     case 844:
                     case 845:
                     case 846:
                     case 847:
                     case 848:
                     case 854:
                         if (l1iteminstance1.getEndTime() == null) {
                             break; // 如果物品沒有到期時間，則跳出檢查循環
                         }
                         pc.sendPackets("期限物品無法使用。");
                         return; // 結束方法

                     case 3000519:
                       if (pc.getLevel() < Config.ServerAdSetting.NewCha1) {
                         if (!pc.getMap().isEscapable() && !pc.isGm())
                         {
                           pc.sendPackets((ServerBasePacket)new S_ServerMessage(647)); }  break;
                       }
                         pc.sendPackets(new S_SystemMessage("您不是新手等級。"));
                         return;
                     case 7021:
                       if (pc.getInventory().getSize() <= 120) {
                         if (pc.getInventory().getWeight100() <= 82) {
                           if (!pc.getInventory().checkItem(7021, 1))
                             break;  pc.getInventory().removeItem(l1iteminstance, 1);
                           if (!pc.is전사());
                           if (!pc.isKnight());
                           if (!pc.isDragonknight());
                           if (!pc.isCrown());
                           if (!pc.isWizard());
                           if (!pc.isBlackwizard());
                           if (!pc.isElf());
                           if (!pc.isDarkelf()) {
                             break; // 如果玩家不是黑暗精靈，則跳出檢查循環
                           }
                           pc.sendPackets(new S_SystemMessage("您的物品過重，無法使用。"));
                           return; // 結束方法
                         }
                         pc.sendPackets(new S_SystemMessage("您持有的物品太多了。"));
                         return; // 結束方法
                     case 447011:
                       if (pc.getInventory().getSize() <= 120) {
                         if (pc.getInventory().getWeight100() <= 82) {
                           if (!pc.getInventory().checkItem(447011, 1))
                             break;  pc.getInventory().removeItem(l1iteminstance, 1);
                           if (!pc.is전사());
                           if (!pc.isKnight());
                           if (!pc.isDragonknight());
                           if (!pc.isCrown());
                           if (!pc.isWizard());
                           if (!pc.isBlackwizard());
                           if (!pc.isElf());
                           if (!pc.isDarkelf());
                           break;
                         }
                         pc.sendPackets(new S_SystemMessage("您的物品過重，無法使用。"));
                         return; // 結束方法
                       }
                       pc.sendPackets(new S_SystemMessage("您持有的物品太多了。"));
                       return; // 結束方法

                     case 40097:
                     case 40119:
                     case 140119:
                     case 140329:
                       template = null;
                       for (L1ItemInstance eachItem : pc.getInventory().getItems()) {
                         if (eachItem.getItem().getBless() == 2)
                         {
                           if (eachItem.isEquipped() || (itemId != 40119 && itemId != 40097)) {
                             int id_normal = eachItem.getItemId() - 200000;
                             L1Item l1Item = ItemTable.getInstance().getTemplate(id_normal);
                             if (l1Item != null)
                             {
                               if (!pc.getInventory().checkItem(id_normal) || !l1Item.isStackable()) {

                                 eachItem.setItem(l1Item);
                                 pc.getInventory().updateItem(eachItem, 64);
                                 pc.getInventory().saveItem(eachItem, 64);
                                 eachItem.setBless(eachItem.getBless() - 1);
                                 pc.getInventory().updateItem(eachItem, 512);
                                 pc.getInventory().saveItem(eachItem, 512);
                               }  }
                           }
                         }
                       }
                       break;
                     case 210083:
                       if (client.getAccount().getCharSlot() >= 10);
                       break;

                     case 200000:
                       if (pc.getMap().isSafetyZone((Point)pc.getLocation())) {
                         if (pc.getLevel() <= 54);
                         break;
                       }
                       pc.sendPackets(new S_ChatPacket(pc, "只能在安全區域使用。"));
                       return; // 結束方法
                     case 60035:
                       if (!pc.getInventory().checkItem(60035, 1))
                         break;  if (l1iteminstance1 != null && l1iteminstance1.getItem() != null) {
                         int choiceItem = l1iteminstance1.getItem().getItemId();
                         if (choiceItem < 12800 || choiceItem > 12844)
                         {
                           if (choiceItem < 12845 || choiceItem > 12894)
                           {
                             if (choiceItem < 12895 || choiceItem > 12944)
                             {
                               if (choiceItem < 12945 || choiceItem > 12994)
                               {
                                 if (choiceItem < 12995 || choiceItem > 13044) {
                                   pc.sendPackets("可以在魔法藥水符文上使用。");
                                   return; // 結束方法
                                 }  }  }  }  }
                         break;
                       }
                       return;
                     case 60027:
                       if (!pc.getInventory().checkItem(60027, 1))
                         break;  if (l1iteminstance1 != null && l1iteminstance1.getItem() != null) {
                         int choiceItem = l1iteminstance1.getItem().getItemId();
                         if (choiceItem < 13045 || choiceItem > 13094)
                         {
                           if (choiceItem < 13095 || choiceItem > 13144)
                           {
                             if (choiceItem < 13145 || choiceItem > 13194)
                             {
                               if (choiceItem < 13195 || choiceItem > 13244)
                               {
                                 if (choiceItem < 13245 || choiceItem > 13294)
                                 {
                                   if (choiceItem < 13295 || choiceItem > 13344)
                                   {
                                     if (choiceItem < 13345 || choiceItem > 13394)
                                     {
                                       if (choiceItem < 13395 || choiceItem > 13444)
                                       {
                                         if (choiceItem < 13445 || choiceItem > 13494) {
                                           pc.sendPackets("可以在魔法藥水符文上使用。");
                                           return; // 結束方法
                                         }  }  }  }  }  }  }  }  }
                         break;
                       }
                       return;
                     case 410094:
                       if (l1iteminstance1 != null) {
                         if (!pc.getInventory().checkItem(410094, 1))
                           break;  int[] last = { 22232, 22233, 22234, 22235, 22236, 22237, 22238, 22239, 22240, 22241, 22242, 22243, 22244, 22245, 22246, 22247, 22248, 22249 };

                         int i9 = 0;
                         int choiceItem = l1iteminstance1.getItem().getItemId();
                         switch (choiceItem) {

                         }

                         if (i9 != 18)
                           break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       return;


                     case 210082:
                       itemId2 = l1iteminstance1.getItem().getItemId();
                       if (itemId2 != 210075) {
                         if (itemId2 != 210079) {
                           if (itemId2 != 500208) {
                             if (itemId2 != 500209) {

                               pc.sendPackets((ServerBasePacket)new S_ServerMessage(79)); break;
                             }  if (!pc.getInventory().checkItem(500209)); break;
                           }  if (!pc.getInventory().checkItem(500208)); break;
                         }  if (!pc.getInventory().checkItem(210079)); break;
                       }  if (!pc.getInventory().checkItem(210075));
                       break;
                     case 40925:
                       earingId = l1iteminstance1.getItem().getItemId();
                       if (earingId >= 40987 && 40989 >= earingId &&
                         _random.nextInt(100) >= Config.ServerRates.CreateChanceRecollection)
                       {

                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(158, l1iteminstance1.getName()));
                       }
                       break;

                     case 40926:
                     case 40927:
                     case 40928:
                     case 40929:
                       earing2Id = l1iteminstance1.getItem().getItemId();
                       potion1 = 0;
                       potion2 = 0;
                       if (earing2Id < 41173 || 41184 < earing2Id)
                         break;
                       if (itemId != 40926)
                       {

                         if (itemId != 40927)
                         {

                           if (itemId != 40928)
                           {

                             if (itemId != 40929);
                           }
                         }
                       }
                       if (earing2Id >= itemId + potion1 && itemId + potion2 >= earing2Id &&
                         _random.nextInt(99) + 1 >= Config.ServerRates.CreateChanceMysterious);
                       break;

                         case 3000106:
                           if (pc.getLevel() < 90) {

                             if (!pc.getMap().isEscapable() && !pc.isGm()) {

                               pc.sendPackets(new S_ServerMessage(647)); // 發送無法逃脫的提示消息
                               break;
                             }

                             int i9 = _random.nextInt(2);
                             int i10 = _random.nextInt(2);
                             int i11 = 32809 + i9;
                             int i12 = 32727 + i10;

                             if (itemId != 3000106) {
                               break; // 如果itemId不等於3000106，則跳出循環
                             }

                             // 這裡應該有其他代碼處理跳轉或其他邏輯
                           } else {
                             pc.sendPackets(new S_SystemMessage("該地下城僅限75級 以下玩家進入。"));
                             return; // 結束方法
                           }
                           break;
                     case 3000444:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands8(1));
                       if (pc.getInventory().checkItem(40395, 10)) {


                         if (pc.getInventory().checkItem(40394, 10)) {


                           if (pc.getInventory().checkItem(40396, 10)) {


                             if (pc.getInventory().checkItem(40393, 10)) {


                               if (pc.getInventory().checkItem(3000360, 100)) {


                                 if (pc.getInventory().checkItem(810003, 3)) {


                                   if (pc.getInventory().checkEnchantItem(1136, 8, 1)) {
                                     break;  // 如果玩家的背包中有+8的惡夢之弓，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("缺少+8的惡夢之弓。"));
                                   return;  // 結束方法

                                 }
                                 pc.sendPackets(new S_SystemMessage("缺少匠人的武器魔法卷軸。"));
                                 return;  // 結束方法

                               }
                               pc.sendPackets(new S_SystemMessage("缺少轉生寶石。"));
                               return;  // 結束方法

                             }
                             pc.sendPackets(new S_SystemMessage("缺少火龍之鱗。"));
                             return;  // 結束方法

                           }
                           pc.sendPackets(new S_SystemMessage("缺少地龍之鱗。"));
                           return;  // 結束方法

                         }
                         pc.sendPackets(new S_SystemMessage("缺少風龍之鱗。"));
                         return;  // 結束方法

                       }
                       pc.sendPackets(new S_SystemMessage("缺少水龍之鱗。"));
                       return;  // 結束方法
                     case 3000445:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands9(1));
                       if (pc.getInventory().checkItem(40395, 10)) {


                         if (pc.getInventory().checkItem(40394, 10)) {


                           if (pc.getInventory().checkItem(40396, 10)) {


                             if (pc.getInventory().checkItem(40393, 10)) {


                               if (pc.getInventory().checkItem(40513, 10)) {


                                 if (pc.getInventory().checkItem(810003, 3)) {


                                   if (pc.getInventory().checkEnchantItem(203006, 8, 1)) {
                                     break;  // 如果玩家的背包中有+8的颱風之斧，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("+8的颱風之斧不足。"));
                                   return;  // 結束方法

                                   pc.sendPackets(new S_SystemMessage("缺少匠人的武器魔法卷軸。"));
                                   return;  // 結束方法

                                   pc.sendPackets(new S_SystemMessage("缺少食人妖的眼淚。"));
                                   return;  // 結束方法

                                   pc.sendPackets(new S_SystemMessage("缺少火龍之鱗。"));
                                   return;  // 結束方法

                                   pc.sendPackets(new S_SystemMessage("缺少地龍之鱗。"));
                                   return;  // 結束方法

                                   pc.sendPackets(new S_SystemMessage("缺少風龍之鱗。"));
                                   return;  // 結束方法

                                   pc.sendPackets(new S_SystemMessage("缺少水龍之鱗。"));
                                   return;  // 結束方法
                     case 3000446:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands10(1));
                       if (pc.getInventory().checkItem(40395, 10)) {


                         if (pc.getInventory().checkItem(40394, 10)) {


                           if (pc.getInventory().checkItem(40396, 10)) {


                             if (pc.getInventory().checkItem(40393, 10)) {


                               if (pc.getInventory().checkItem(700020, 15)) {


                                 if (pc.getInventory().checkItem(810003, 3)) {


                                   if (pc.getInventory().checkEnchantItem(203017, 8, 1)) {
                                     break; // 如果玩家的背包中有+8的殲滅者鏈劍，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("+8的殲滅者鏈劍不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("匠人的武器魔法卷軸不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("冰之結晶不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("火龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("地龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("風龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("水龍之鱗不足。"));
                                   return; // 結束方法
                     case 3000447:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands11(1));
                       if (pc.getInventory().checkItem(40395, 10)) {


                         if (pc.getInventory().checkItem(40394, 10)) {


                           if (pc.getInventory().checkItem(40396, 10)) {


                             if (pc.getInventory().checkItem(40393, 10)) {


                               if (pc.getInventory().checkItem(410061, 270)) {


                                 if (pc.getInventory().checkItem(810003, 3)) {


                                   if (pc.getInventory().checkEnchantItem(505010, 8, 1)) {
                                     break; // 如果玩家的背包中有+8的真鬥士大劍，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("+8的真鬥士大劍不足。"));
                                   return; // 結束方法

                                    // 如果沒有相應物品，發送提示消息並結束方法
                                   pc.sendPackets(new S_SystemMessage("匠人的武器魔法卷軸不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("魔物的氣息不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("火龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("地龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("風龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("水龍之鱗不足。"));
                                   return; // 結束方法
                     case 3000448:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands12(1));
                       if (pc.getInventory().checkItem(40395, 10)) {


                         if (pc.getInventory().checkItem(40394, 10)) {


                           if (pc.getInventory().checkItem(40396, 10)) {


                             if (pc.getInventory().checkItem(40393, 10)) {


                               if (pc.getInventory().checkItem(40677, 15)) {


                                 if (pc.getInventory().checkItem(810003, 3)) {


                                   if (pc.getInventory().checkEnchantItem(54, 8, 1)) {
                                     break; // 如果玩家的背包中有+8的柯爾茲之劍，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("+8的柯爾茲之劍不足。"));
                                   return; // 結束方法

                                    // 如果沒有相應物品，發送提示消息並結束方法
                                   pc.sendPackets(new S_SystemMessage("匠人的武器魔法卷軸不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("暗影金屬不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("火龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("地龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("風龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("水龍之鱗不足。"));
                                   return; // 結束方法
                     case 3000449:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands13(1));
                       if (pc.getInventory().checkItem(40395, 10)) {


                         if (pc.getInventory().checkItem(40394, 10)) {


                           if (pc.getInventory().checkItem(40396, 10)) {


                             if (pc.getInventory().checkItem(40393, 10)) {


                               if (pc.getInventory().checkItem(40644, 50)) {


                                 if (pc.getInventory().checkItem(810003, 3)) {


                                   if (pc.getInventory().checkEnchantItem(58, 8, 1)) {
                                     break; // 如果玩家的背包中有+8的死亡騎士火劍，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("+8的死亡騎士火劍不足。"));
                                   return; // 結束方法

                                    // 如果沒有相應物品，發送提示消息並結束方法
                                   pc.sendPackets(new S_SystemMessage("匠人的武器魔法卷軸不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("迷宮結構圖不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("火龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("地龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("風龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("水龍之鱗不足。"));
                                   return; // 結束方法
                     case 3000450:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands17(1));
                       if (pc.getInventory().checkItem(40395, 10)) {


                         if (pc.getInventory().checkItem(40394, 10)) {


                           if (pc.getInventory().checkItem(40396, 10)) {


                             if (pc.getInventory().checkItem(40393, 10)) {


                               if (pc.getInventory().checkItem(40324, 100)) {


                                 if (pc.getInventory().checkItem(810003, 3)) {


                                   if (pc.getInventory().checkEnchantItem(76, 8, 1)) {
                                     break; // 如果玩家的背包中有+8的朗德雙刀，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("+8的朗德雙刀不足。"));
                                   return; // 結束方法

                                    // 如果沒有相應物品，發送提示消息並結束方法
                                   pc.sendPackets(new S_SystemMessage("匠人的武器魔法卷軸不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("暗黃石不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("火龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("地龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("風龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("水龍之鱗不足。"));
                                   return; // 結束方法
                     case 3000451:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands14(1));
                       if (pc.getInventory().checkItem(40395, 10)) {


                         if (pc.getInventory().checkItem(40394, 10)) {


                           if (pc.getInventory().checkItem(40396, 10)) {


                             if (pc.getInventory().checkItem(40393, 10)) {


                               if (pc.getInventory().checkItem(40487, 80)) {


                                 if (pc.getInventory().checkItem(810003, 3)) {


                                   if (pc.getInventory().checkEnchantItem(505009, 8, 1)) {
                                     break; // 如果玩家的背包中有+8的真刺劍，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("+8的真刺劍不足。"));
                                   return; // 結束方法

                                    // 如果沒有相應物品，發送提示消息並結束方法
                                   pc.sendPackets(new S_SystemMessage("匠人的武器魔法卷軸不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("黃金鍛件不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("火龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("地龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("風龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("水龍之鱗不足。"));
                                   return; // 結束方法
                     case 3000452:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands18(1));
                       if (pc.getInventory().checkItem(40395, 10)) {


                         if (pc.getInventory().checkItem(40394, 10)) {


                           if (pc.getInventory().checkItem(40396, 10)) {


                             if (pc.getInventory().checkItem(40393, 10)) {


                               if (pc.getInventory().checkItem(40324, 100)) {


                                 if (pc.getInventory().checkItem(810003, 3)) {


                                   if (pc.getInventory().checkEnchantItem(203018, 8, 1)) {
                                     break; // 如果玩家的背包中有+8的咆哮雙刀，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("+8的咆哮雙刀不足。"));
                                   return; // 結束方法

                                    // 如果沒有相應物品，發送提示消息並結束方法
                                   pc.sendPackets(new S_SystemMessage("匠人的武器魔法卷軸不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("暗黃石不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("火龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("地龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("風龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("水龍之鱗不足。"));
                                   return; // 結束方法
                     case 3000453:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands15(1));
                       if (pc.getInventory().checkItem(40395, 10)) {


                         if (pc.getInventory().checkItem(40394, 10)) {


                           if (pc.getInventory().checkItem(40396, 10)) {


                             if (pc.getInventory().checkItem(40393, 10)) {


                               if (pc.getInventory().checkItem(40969, 700)) {


                                 if (pc.getInventory().checkItem(810003, 3)) {


                                   if (pc.getInventory().checkEnchantItem(202003, 8, 1)) {
                                     break; // 如果玩家的背包中有+8的傑洛斯法杖，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("+8的傑洛斯法杖不足。"));
                                   return; // 結束方法

                                    // 如果沒有相應物品，發送提示消息並結束方法
                                   pc.sendPackets(new S_SystemMessage("匠人的武器魔法卷軸不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("靈魂結晶不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("火龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("地龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("風龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("水龍之鱗不足。"));
                                   return; // 結束方法
                     case 3000454:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands16(1));
                       if (pc.getInventory().checkItem(40395, 10)) {


                         if (pc.getInventory().checkItem(40394, 10)) {


                           if (pc.getInventory().checkItem(40396, 10)) {


                             if (pc.getInventory().checkItem(40393, 10)) {


                               if (pc.getInventory().checkItem(40646, 60)) {


                                 if (pc.getInventory().checkItem(810003, 3)) {


                                   if (pc.getInventory().checkEnchantItem(59, 8, 1)) {
                                     break; // 如果玩家的背包中有+8的奈特巴爾德雙手劍，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("+8的奈特巴爾德雙手劍不足。"));
                                   return; // 結束方法

                                    // 如果沒有相應物品，發送提示消息並結束方法
                                   pc.sendPackets(new S_SystemMessage("匠人的武器魔法卷軸不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("巴西利斯克的角不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("火龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("地龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("風龍之鱗不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_SystemMessage("水龍之鱗不足。"));
                                   return; // 結束方法
                     case 3000029:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands1(1));
                       if (pc.getInventory().checkItem(40508, 500)) {


                         if (pc.getInventory().checkItem(40460, 30)) {



                           if (pc.getInventory().checkItem(40052, 5)) {



                             if (pc.getInventory().checkItem(40053, 5)) {



                               if (pc.getInventory().checkItem(40054, 5)) {



                                 if (pc.getInventory().checkItem(40055, 5)) {



                                   if (pc.getInventory().checkItem(40460, 30)) {
                                     break; // 如果玩家的背包中有 30 個阿西塔焦之灰，則跳出檢查
                                   }
                                   pc.sendPackets(new S_ChatPacket(pc, "阿西塔焦之灰不足。"));
                                   return; // 結束方法

                                    // 如果沒有相應物品，發送提示消息並結束方法
                                   pc.sendPackets(new S_ChatPacket(pc, "高級綠寶石不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_ChatPacket(pc, "高級藍寶石不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_ChatPacket(pc, "高級紅寶石不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_ChatPacket(pc, "高級鑽石不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_ChatPacket(pc, "阿西塔焦之灰不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_ChatPacket(pc, "奧里哈魯根不足。"));
                                   return; // 結束方法
                     case 3000030:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands2(1));
                       if (pc.getInventory().checkItem(181, 1)) {


                         if (pc.getInventory().checkItem(40394, 15)) {



                           if (pc.getInventory().checkItem(40491, 30)) {



                             if (pc.getInventory().checkItem(40498, 50)) {
                               break; // 如果玩家的背包中有50個風之眼淚，則跳出檢查
                             }
                             pc.sendPackets(new S_ChatPacket(pc, "風之眼淚不足。"));
                             return; // 結束方法

                              // 如果沒有相應物品，發送提示消息並結束方法
                             pc.sendPackets(new S_ChatPacket(pc, "獅鷲獸的羽毛不足。"));
                             return; // 結束方法

                             pc.sendPackets(new S_ChatPacket(pc, "風龍的鱗片不足。"));
                             return; // 結束方法

                             pc.sendPackets(new S_ChatPacket(pc, "長弓（1個）不足。"));
                             return; // 結束方法

                     case 3000031:
                       pc.sendPackets((ServerBasePacket)new S_UserCommands3(1));
                       if (pc.getInventory().checkItem(81, 1)) {


                         if (pc.getInventory().checkItem(40466, 1)) {


                           if (pc.getInventory().checkItem(40525, 3)) {


                             if (pc.getInventory().checkItem(40413, 9)) {


                               if (pc.getInventory().checkItem(40402, 10)) {



                                 if (pc.getInventory().checkItem(40053, 3)) {


                                   if (pc.getInventory().checkItem(40308, 100000)) {
                                     break; // 如果玩家的背包中有100,000個阿德納，則跳出檢查
                                   }
                                   pc.sendPackets(new S_ChatPacket(pc, "金幣不足。"));
                                   return; // 結束方法

                                      // 如果沒有相應物品，發送提示消息並結束方法
                                   pc.sendPackets(new S_ChatPacket(pc, "高級紅寶石不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_ChatPacket(pc, "冰后之息不足。"));
                                   return; // 結束方法

                                   // 這條消息重複了上一條，提示玩家缺少冰后之息
                                   pc.sendPackets(new S_ChatPacket(pc, "冰后之息不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_ChatPacket(pc, "格蘭肯之淚不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_ChatPacket(pc, "龍的心臟不足。"));
                                   return; // 結束方法

                                   pc.sendPackets(new S_ChatPacket(pc, "黑光雙刀（1個）不足。"));
                                   return; // 結束方法
                     case 3000354:
                       if (!pc.getMap().isEscapable() && !pc.isGm())
                       {

                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(647));
                       }
                       break;

                     case 3000394:
                       if (!pc.getMap().isEscapable() && !pc.isGm())
                       {

                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(647));
                       }
                       break;

                     case 3000396:
                       if (!pc.getMap().isEscapable() && !pc.isGm())
                       {

                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(647));
                       }
                       break;

                     case 3000395:
                       if (!pc.getMap().isEscapable() && !pc.isGm())
                       {

                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(647));
                       }
                       break;

                     case 40824:
                       if (!pc.getMap().isEscapable() && !pc.isGm()) {

                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(647)); break;
                       }  rx = _random.nextInt(2); ry = _random.nextInt(2); ux = 32807 + rx; uy = 32747 + ry;
                       if (itemId != 40824);
                       break;
                     case 40825:
                       if (!pc.getMap().isEscapable() && !pc.isGm()) {

                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(647)); break;
                       }  rx = _random.nextInt(2); ry = _random.nextInt(2); ux = 32810 + rx; uy = 32765 + ry;
                       if (itemId != 40825);
                       break;
                     case 40417:
                       if (pc.getX() >= 32667 && pc.getX() <= 32673 && pc
                         .getY() >= 32978 && pc.getY() <= 32984 && pc.getMapId() != 440);
                       break;

                     case 43201:
                       if (pc.getLevel() < 56) {
                         break; // 如果玩家等級小於56，則跳出檢查
                       }
                       pc.sendPackets(new S_SystemMessage("只有等級55及以下的玩家才能使用。"));
                       return; // 結束方法

                     case 40003:
                       for (L1ItemInstance lightItem : pc.getInventory().getItems()) {
                         if (lightItem.getItem().getItemId() != 40002 && lightItem.getItem().getItemId() != 7005) {
                           continue;
                         }
                         break;
                       }
                       break;



                     case 3000255:
                       l1ItemInstance2 = pc.getInventory().getItem(l1iteminstance.getId());
                       l1ItemInstance3 = pc.getInventory().getItem(l);
                       if (l1ItemInstance2 != null && l1ItemInstance3 != null) {


                         int id = l1ItemInstance3.getItemId();
                         if (id == 203006 || id == 59 || id == 202003 || id == 1136 || id == 203018 || id == 203017 || id == 1120)
                           break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       return;

                     case 100001:
                       dollId = l1iteminstance1.getItem().getItemId();
                       isAppear = true;
                       doll = pc.getMagicDoll();
                       item = null;

                       if (doll == null ||
                         doll.getItemObjId() != itemId)

                       {



                         if (!isAppear ||
                           doll == null)

                         {


                           if (dollId != 41248 && dollId != 41250 && dollId != 210086 && dollId != 210072 && dollId != 210070 && dollId != 210096 && dollId != 500213 && dollId != 41249 && dollId != 210071 && dollId != 210105 && dollId != 447012 && dollId != 447013 && dollId != 447014 && dollId != 500215 && dollId != 447016 && dollId != 447015 && dollId != 500214 && dollId != 447017 && dollId != 510216 && dollId != 510217 && dollId != 510218 && dollId != 510219 && dollId != 510220 && dollId != 510221 && dollId != 510222)

                           {


                             pc.sendPackets(new S_SystemMessage("這是無法更改的物品。"));return; pc.getInventory().removeItem(l1iteminstance1, 1); pc.getInventory().removeItem(l1iteminstance, 1); int i9 = _random.nextInt(1060) + 1; if (i9 > 150) if (i9 > 260) if (i9 > 370) if (i9 > 480) if (i9 > 590) if (i9 > 680) if (i9 > 770) if (i9 > 810) if (i9 > 850) if (i9 > 880) if (i9 > 900) if (i9 > 920) if (i9 > 940) if (i9 > 960) if (i9 > 970) if (i9 > 980) if (i9 > 990) if (i9 > 1000) if (i9 > 1010) if (i9 > 1020)
                                                                   if (i9 > 1030)
                                                                     if (i9 > 1040)
                                                                       if (i9 > 1050)
                                                                         if (i9 > 1055)
                                                                           if (i9 > 1060);                         break; }  pc.sendPackets((ServerBasePacket)new S_SystemMessage("無法在召喚魔法娃娃的狀態下進行變更.")); return; }  break;case 3000148: l1ItemInstance1 = pc.getInventory().getItem(l1iteminstance.getId());
                       weapon_item = pc.getInventory().getItem(l);
                       if (l1ItemInstance1 != null && weapon_item != null) {
                         if (l1iteminstance1.getBless() < 128) {
                           int id = weapon_item.getItemId();
                           if (id == 12 || id == 61 || id == 134 || id == 86 || id == 202011 || id == 202012 || id == 202013 || id == 202014)
                             break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                           return;
                         }
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       return;

                     case 719:
                       Skill_item = pc.getInventory().getItem(l);
                       if (Skill_item != null) {
                         if (l1iteminstance1.getBless() < 128) {
                           int id = Skill_item.getItemId();
                           if (id == 40222 || id == 41148 || id == 5559 || id == 210125)
                             break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                           return;
                         }
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       return;



                     case 707:
                       enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
                       weapon_item = pc.getInventory().getItem(l);
                       if (enchant_item != null && weapon_item != null) {
                         if (l1iteminstance1.getBless() < 128) {
                           int id = weapon_item.getItemId();
                           if (id == 22208 || id == 22209 || id == 22210 || id == 22211 || id == 22200 || id == 22201 || id == 22202 || id == 22203 || id == 22204 || id == 22205 || id == 22206 || id == 22207 || id == 22196 || id == 22197 || id == 22198 || id == 22199) {
                             break;
                           }

                           pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                           return;
                         }
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       return;


                     case 706:
                       enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
                       weapon_item = pc.getInventory().getItem(l);
                       if (enchant_item != null && weapon_item != null) {
                         if (l1iteminstance1.getBless() < 128) {
                           int id = weapon_item.getItemId();
                           if (id == 900081 || id == 900082 || id == 900083 || id == 900084)
                             break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                           return;
                         }
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       return;



                     case 705:
                       enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
                       weapon_item = pc.getInventory().getItem(l);
                       if (enchant_item != null && weapon_item != null) {


                         if (l1iteminstance1.getBless() < 128) {



                           int id = weapon_item.getItemId();
                           if (id == 900093 || id == 900094 || id == 900095 || id == 900096 || id == 900097 || id == 900098 || id == 900099)
                             break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                           return;
                         }
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       return;



                     case 704:
                       enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
                       weapon_item = pc.getInventory().getItem(l);
                       if (enchant_item != null && weapon_item != null) {


                         if (l1iteminstance1.getBless() < 128) {



                           int id = weapon_item.getItemId();
                           if (id == 222337 || id == 222339 || id == 222341)
                             break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                           return;
                         }
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       return;



                     case 703:
                       enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
                       weapon_item = pc.getInventory().getItem(l);
                       if (enchant_item != null && weapon_item != null) {


                         if (l1iteminstance1.getBless() < 128) {



                           int id = weapon_item.getItemId();
                           if (id == 222330 || id == 222331 || id == 222332 || id == 222333 || id == 222334 || id == 222335 || id == 222336)
                             break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                           return;
                         }
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       return;



                         case 4100076:
                           timeItem7 = System.currentTimeMillis(); // 記錄當前時間
                           useTimeItem7 = 600; // 設定使用間隔時間（單位：秒）
                           if (!pc.hasSkillEffect(3281)) {
                             break; // 如果玩家沒有3281號技能效果，跳出檢查
                           }
                           n = pc.getSkillEffectTimeSec(3281); // 獲取玩家3281號技能效果的剩餘時間（秒）
                           pc.sendPackets(new S_SystemMessage(String.format("%d秒後可以使用。", n)));
                           return; // 結束方法



                     case 40566:
                       if (!pc.isElf() || pc
                         .getX() < 33971 || pc.getX() > 33975 || pc
                         .getY() < 32324 || pc.getY() > 32328 || pc.getMapId() != 4 || pc
                         .getInventory().checkItem(40548))
                         break;  bool1 = false;
                       l1MonsterInstance1 = null;
                       for (L1Object obj : L1World.getInstance().getObject()) {
                         if (!(obj instanceof L1MonsterInstance))
                           continue;  L1MonsterInstance l1MonsterInstance = (L1MonsterInstance)obj;
                         if (l1MonsterInstance == null ||
                           l1MonsterInstance.getNpcTemplate().get_npcId() != 45300) {
                           continue;
                         }

                         break;
                       }

                       if (!bool1)
                       {

                         L1SpawnUtil.spawn(pc, 45300, 0, 0);
                       }
                       break;


                     case 40557:
                       if (pc.getX() != 32620 || pc.getY() != 32641 || pc.getMapId() != 4)
                         break;  for (L1Object object : L1World.getInstance().getObject()) {
                         if (!(object instanceof L1NpcInstance))
                           continue;  L1NpcInstance l1NpcInstance = (L1NpcInstance)object;
                         if (l1NpcInstance.getNpcTemplate().get_npcId() != 45883)
                           continue;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       break;



                     case 40558:
                       if (pc.getX() != 33513 || pc.getY() != 32890 || pc.getMapId() != 4)
                         break;  npc = null;
                       for (L1Object object : L1World.getInstance().getObject()) {
                         if (!(object instanceof L1NpcInstance))
                           continue;  L1NpcInstance l1NpcInstance = (L1NpcInstance)object;
                         if (l1NpcInstance.getNpcTemplate().get_npcId() != 45889)
                           continue;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       break;


                     case 40559:
                       if (pc.getX() != 34215 || pc.getY() != 33195 || pc.getMapId() != 4)
                         break;  npc = null;
                       for (L1Object object : L1World.getInstance().getObject()) {
                         if (!(object instanceof L1NpcInstance))
                           continue;  L1NpcInstance l1NpcInstance = (L1NpcInstance)object;
                         if (l1NpcInstance.getNpcTemplate().get_npcId() != 45888)
                           continue;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       break;



                     case 40560:
                       if (pc.getX() != 32580 || pc.getY() != 33260 || pc.getMapId() != 4)
                         break;  for (L1Object object : L1World.getInstance().getObject()) {
                         if (!(object instanceof L1NpcInstance))
                           continue;  L1NpcInstance l1NpcInstance = (L1NpcInstance)object;
                         if (l1NpcInstance.getNpcTemplate().get_npcId() != 45886)
                           continue;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       break;


                     case 40561:
                       if (pc.getX() != 33046 || pc.getY() != 32806 || pc.getMapId() != 4)
                         break;  for (L1Object object : L1World.getInstance().getObject()) {
                         if (!(object instanceof L1NpcInstance))
                           continue;  L1NpcInstance l1NpcInstance = (L1NpcInstance)object;
                         if (l1NpcInstance.getNpcTemplate().get_npcId() != 45885)
                           continue;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       break;


                     case 40562:
                       if (pc.getX() != 33447 || pc.getY() != 33476 || pc.getMapId() != 4)
                         break;  for (L1Object object : L1World.getInstance().getObject()) {
                         if (!(object instanceof L1NpcInstance))
                           continue;  L1NpcInstance l1NpcInstance = (L1NpcInstance)object;
                         if (l1NpcInstance.getNpcTemplate().get_npcId() != 45887)
                           continue;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       break;

                     case 40563:
                       if (pc.getX() != 32730 || pc.getY() != 32426 || pc.getMapId() != 4)
                         break;  for (L1Object object : L1World.getInstance().getObject()) {
                         if (!(object instanceof L1NpcInstance))
                           continue;  L1NpcInstance l1NpcInstance = (L1NpcInstance)object;
                         if (l1NpcInstance.getNpcTemplate().get_npcId() != 45884)
                           continue;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       break;

                     case 40826:
                       if (!pc.getMap().isEscapable() && !pc.isGm()) {
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(647)); break;
                       }  k = _random.nextInt(2); m = _random.nextInt(2); i1 = 32799 + k; i2 = 32798 + m;
                       if (itemId != 40826);
                       break;
                     case 3000393:
                       if (l1iteminstance1.getItem().getType2() != 0) {



                         if (l1iteminstance1.get_durability() <= 0) {



                           if (!l1iteminstance1.isEquipped()) {



                             if (l1iteminstance1.isIdentified()) {
                               break; // 如果物品已鑑定，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("只能用於已鑑定的物品。"));
                             return; // 結束方法

                              // 如果物品正在穿戴中，發送提示消息並結束方法
                             pc.sendPackets(new S_SystemMessage("無法使用於穿戴中的物品。"));
                             return; // 結束方法

                              // 如果物品已損壞，發送提示消息並結束方法
                             pc.sendPackets(new S_SystemMessage("無法更改損壞的物品。"));
                             return; // 結束方法

                              // 如果物品不是裝備，發送提示消息並結束方法
                             pc.sendPackets(new S_SystemMessage("只能用於裝備。"));
                             return; // 結束方法
                     case 40017:
                     case 40507:
                     case 4100661:
                       if (pc.hasSkillEffect(71) != true);
                       break;

                     case 40616:
                     case 40782:
                     case 40783:
                       if (pc.getX() < 32698 || pc.getX() > 32702 || pc.getY() < 32894 || pc.getY() > 32898 || pc
                         .getMapId() != 523)
                         break;  pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(), l1iteminstance
                           .getItem().get_mapid(), 5, 18339, true, false);




                     case 40700:
                       pc.sendPackets((ServerBasePacket)new S_Sound(10));
                       pc.broadcastPacket((ServerBasePacket)new S_Sound(10));
                       if (pc.getX() < 32619 || pc.getX() > 32623 || pc.getY() < 33120 || pc.getY() > 33124 || pc
                         .getMapId() != 440)
                         break;
                       found = false;
                       mob = null;
                       for (L1Object obj : L1World.getInstance().getObject()) {
                         if (!(obj instanceof L1MonsterInstance))
                           continue;  L1MonsterInstance l1MonsterInstance = (L1MonsterInstance)obj;
                         if (l1MonsterInstance == null ||
                           l1MonsterInstance.getNpcTemplate().get_npcId() != 45875) {
                           continue;
                         }

                         break;
                       }

                       if (!found)
                       {
                         L1SpawnUtil.spawn(pc, 45875, 0, 0);
                       }
                       break;

                     case 41121:
                       if (pc.getQuest().get_step(37) != 255 &&
                         !pc.getInventory().checkItem(41122, 1));
                       break;




                     case 41130:
                       if (pc.getQuest().get_step(36) != 255 &&
                         !pc.getInventory().checkItem(41131, 1));
                       break;




                     case 40692:
                       if (!pc.getInventory().checkItem(40621)) {


                         if (pc.getX() < 32856 || pc.getX() > 32858 || pc.getY() < 32857 || pc.getY() > 32858 || pc
                           .getMapId() != 443)
                           break;  pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(), l1iteminstance
                             .getItem().get_mapid(), 5, 18339, true, false);
                       }
                       break;


                     case 41208:
                       if (pc.getX() < 32844 || pc.getX() > 32845 || pc.getY() < 32693 || pc.getY() > 32694 || pc
                         .getMapId() != 550)
                         break;  pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(), l1iteminstance
                           .getItem().get_mapid(), 5, 18339, true, false);




                     case 4101027:
                       if (L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId()))
                       {


                         if (l1iteminstance.getItemId() == 4101027) {
                           break; // 如果物品ID是4101027，則跳出檢查
                         }
                         pc.sendPackets(new S_SystemMessage("只能在據點內使用。"));
                         return; // 結束方法
                     case 40964:
                       historybookId = l1iteminstance1.getItem().getItemId();
                       if (historybookId >= 41011 && 41018 >= historybookId &&
                         _random.nextInt(99) + 1 > Config.ServerRates.CreateChanceHistoryBook)
                       {

                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(158, l1iteminstance1.getName()));
                       }
                       break;





                     case 41036:
                       diaryId = l1iteminstance1.getItem().getItemId();
                       if (diaryId >= 41038 && 41047 >= diaryId &&
                         _random.nextInt(99) + 1 > Config.ServerRates.CreateChanceDiary)
                       {

                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(158, l1iteminstance1.getName()));
                       }
                       break;



                     case 30084:
                       if (pc.hasSkillEffect(71) != true);
                       break;

                     case 3000193:
                       if (!pc.getMap().isEscapable() && !pc.isGm());
                       break;


                     case 3000194:
                       itemid = new int[] { 40308 };
                       random500 = CommonUtil.random(100);

                       itemid2 = new int[] { 3000176 };
                       random502 = CommonUtil.random(100);

                       if (random500 > 10)
                       {

                         createNewItem(pc, itemid[0], CommonUtil.random(15000, 20000));
                       }
                       if (random502 > 0.3D);
                       break;




                     case 41299:
                       itemid = new int[] { 41302 };
                       random500 = CommonUtil.random(100);

                       if (random500 > 7)
                       {

                         createNewItem(pc, itemid[0], CommonUtil.random(300000, 400000));
                       }
                       break;


                     case 41300:
                       itemid = new int[] { 41302 };
                       random500 = CommonUtil.random(100);

                       if (random500 > 7)
                       {

                         createNewItem(pc, itemid[0], CommonUtil.random(3000000, 4000000));
                       }
                       break;


                     case 41303:
                       itemid = new int[] { 41302 };
                       random500 = CommonUtil.random(100);

                       if (random500 > 7)
                       {

                         createNewItem(pc, itemid[0], CommonUtil.random(400000, 500000));
                       }
                       break;


                     case 41304:
                       itemid = new int[] { 41302 };
                       random500 = CommonUtil.random(100);

                       if (random500 > 7)
                       {

                         createNewItem(pc, itemid[0], CommonUtil.random(4000000, 5000000));
                       }
                       break;


                     case 3000145:
                       itemid = new int[] { 41159 };
                       random500 = CommonUtil.random(100);

                       if (random500 > 90)
                       {

                         createNewItem(pc, itemid[0], CommonUtil.random(100, 200));
                       }
                       break;


                     case 3000146:
                       itemid = new int[] { 3000246 };
                       random500 = CommonUtil.random(100);

                       if (random500 > 90)
                       {

                         createNewItem(pc, itemid[0], CommonUtil.random(100, 200));
                       }
                       break;



                     case 3000195:
                       for (L1Object obj : L1World.getInstance().getObject()) {
                         if (!(obj instanceof L1NpcInstance))
                           continue;  L1NpcInstance l1NpcInstance = (L1NpcInstance)obj;
                         if (l1NpcInstance.getNpcId() != 4200015) {
                           continue;
                         }
                         break;
                       }
                       break;

                         case 721:
                           if (pc.getLevel() >= 89) {
                             break; // 如果玩家等級大於或等於89，則跳出檢查
                           }
                           pc.sendPackets(new S_SystemMessage("需要達到等級89才能使用。"));
                           return; // 結束方法

                         case 722:
                           if (pc.getLevel() >= 90) {
                             break; // 如果玩家等級大於或等於90，則跳出檢查
                           }
                           pc.sendPackets(new S_SystemMessage("需要達到等級90才能使用。"));
                           return; // 結束方法

                         case 500220:
                           timeItem = System.currentTimeMillis(); // 記錄當前時間
                           useTimeItem = 1800; // 設定使用間隔時間（1800秒）
                           if (!pc.hasSkillEffect(70702)) {
                             break; // 如果玩家沒有70702號技能效果，則跳出檢查
                           }
                           i3 = pc.getSkillEffectTimeSec(70702); // 獲取玩家70702號技能效果的剩餘時間（秒）
                           pc.sendPackets(new S_SystemMessage(String.format("%d秒後可以使用。", i3)));
                           return; // 結束方法

                         case 4100301:
                           timeItem1 = System.currentTimeMillis(); // 記錄當前時間
                           useTimeItem1 = 1800; // 設定使用間隔時間（1800秒）
                           if (!pc.hasSkillEffect(70703)) {
                             break; // 如果玩家沒有70703號技能效果，則跳出檢查
                           }
                           i4 = pc.getSkillEffectTimeSec(70703); // 獲取玩家70703號技能效果的剩餘時間（秒）
                           pc.sendPackets(new S_SystemMessage(String.format("%d秒後可以使用。", i4)));
                           return; // 結束方法

                         case 4100623:
                           useTimeItem2 = 600; // 設定使用間隔時間（600秒）
                           if (!pc.hasSkillEffect(70702)) {
                             break; // 如果玩家沒有70702號技能效果，則跳出檢查
                           }
                           i5 = pc.getSkillEffectTimeSec(70702); // 獲取玩家70702號技能效果的剩餘時間（秒）
                           pc.sendPackets(new S_SystemMessage(String.format("%d秒後可以使用。", i5)));
                           return; // 結束方法



                     case 40493:
                       pc.sendPackets((ServerBasePacket)new S_Sound(165));
                       pc.broadcastPacket((ServerBasePacket)new S_Sound(165));
                       guardian = null;
                       for (L1Object visible : pc.getKnownObjects()) {
                         if (!(visible instanceof L1GuardianInstance))
                           continue;  guardian = (L1GuardianInstance)visible;
                         if (guardian.getNpcTemplate().get_npcId() == 70850 &&
                           !createNewItem(pc, 88, 1));
                       }
                       break;

                     case 40325:
                       if (!pc.getInventory().checkItem(40318, 1));
                       break;

                     case 40326:
                       if (!pc.getInventory().checkItem(40318, 1));
                       break;

                     case 40327:
                       if (!pc.getInventory().checkItem(40318, 1));
                       break;

                     case 40328:
                       if (!pc.getInventory().checkItem(40318, 1));
                       break;


                         case 41027:
                           pc.sendPackets(new S_UserCommands7(1)); // 發送用戶命令7到客戶端
                           if (pc.getInventory().checkItem(41027, 1)) {
                             break; // 如果玩家的背包中有ID為41027的物品，則跳出檢查
                           }
                           pc.sendPackets(new S_SystemMessage("缺少完成的拉斯塔巴德歷史書。"));
                           return; // 結束方法

                     case 4100622:
                       if (pc.getAccount().getBlessOfAinBonusPoint() > 90000000);
                       break;

                     case 4200294:
                       if (pc.getAccount().getBlessOfAinBonusPoint() > 90000000);
                       break;

                     case 30001359:
                       if (pc.getAccount().getBlessOfAinBonusPoint() > 90000000);
                       break;

                         case 490028:
                           if (pc.getLawful() < 32767) {
                             if (pc.getLawful() >= -32768 && pc.getLawful() > 32767) {
                               break; // 如果玩家的正義值在 -32768 至 32767 之間，且大於 32767，則跳出檢查（但這個條件永遠為假）
                             }
                           }
                           pc.sendPackets(new S_SystemMessage("當前條件不符合使用要求。"));
                           return; // 結束方法

                         case 490029:
                           if (pc.getLawful() > -32768) {
                             if (pc.getLawful() >= -32768 && pc.getLawful() > 32767) {
                               break; // 如果玩家的正義值在 -32768 至 32767 之間，且大於 32767，則跳出檢查（但這個條件永遠為假）
                             }
                           }
                           pc.sendPackets(new S_SystemMessage("當前條件不符合使用要求。"));
                           return; // 結束方法

                         case 4100056:
                           if (pc.getInventory().getSize() <= 160) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於 160 且重量百分比小於等於 82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法

                         case 4100057:
                           if (pc.getInventory().getSize() <= 160) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於 160 且重量百分比小於等於 82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法

                         case 4100058:
                           if (pc.getInventory().getSize() <= 160) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於160且重量百分比小於等於82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法

                         case 3000177:
                           if (pc.getInventory().getSize() <= 160) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於160且重量百分比小於等於82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法

                         case 3000178:
                           if (pc.getInventory().getSize() <= 160) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於160且重量百分比小於等於82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法

                         case 3000179:
                           if (pc.getInventory().getSize() <= 160) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於160且重量百分比小於等於82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法

                         case 3000187:
                           if (pc.getInventory().getSize() <= 160) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於160且重量百分比小於等於82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法

                         case 3000188:
                           if (pc.getInventory().getSize() <= 160) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於160且重量百分比小於等於82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法

                         case 4100299:
                           if (!pc.getInventory().checkItem(4100299, 99)) {
                             pc.sendPackets(new S_SystemMessage("現金(100)個不足。"));
                             return; // 檢查玩家是否持有至少99個ID為4100299的物品，如果不足則發送提示消息並結束方法
                           }
                           pc.getInventory().consumeItem(4100299, 99); // 消耗99個ID為4100299的物品
                           if (pc.getInventory().getSize() <= 120) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於120且重量百分比小於等於82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法
                         case 3000249:
                           if (pc.getInventory().getSize() <= 120) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於120且重量百分比小於等於82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法

                         case 4100440:
                           if (pc.getInventory().getSize() <= 120) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於120且重量百分比小於等於82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法

                         case 4100441:
                           if (pc.getInventory().getSize() <= 120) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於120且重量百分比小於等於82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法

                         case 4100442:
                           if (pc.getInventory().getSize() <= 120) {
                             if (pc.getInventory().getWeight100() <= 82) {
                               break; // 如果玩家的背包大小小於等於120且重量百分比小於等於82%，則跳出檢查
                             }
                             pc.sendPackets(new S_SystemMessage("物品太重，無法使用。"));
                             return; // 結束方法
                           }
                           pc.sendPackets(new S_SystemMessage("持有的物品過多，無法使用。"));
                           return; // 結束方法



                     case 4100591:
                       random = CommonUtil.random(100);
                       count = 0;

                       if (random > 30) {



                         count = CommonUtil.random(15, 30);
                         (pc.getNetConnection().getAccount()).Ncoin_point += count;
                       }
                       if (random > 5);



                       if (random > 3);



                       if (random > 1);
                       break;

                     case 3000173:
                       if (!pc.getInventory().checkItem(3000173, 1));
                       break;

                     case 3000389:
                       if (!pc.getInventory().checkItem(3000389, 1));
                       break;

                     case 3000378:
                       if (!pc.getInventory().checkItem(3000378, 1));
                       break;

                     case 3000379:
                       if (!pc.getInventory().checkItem(3000379, 1));
                       break;

                     case 3000381:
                       if (!pc.getInventory().checkItem(3000381, 1));
                       break;

                     case 3000382:
                       if (!pc.getInventory().checkItem(3000382, 1));
                       break;

                     case 3000383:
                       if (!pc.getInventory().checkItem(3000383, 1));
                       break;

                     case 3000384:
                       if (!pc.getInventory().checkItem(3000384, 1));
                       break;

                     case 3000385:
                       if (!pc.getInventory().checkItem(3000385, 1));
                       break;

                     case 3000386:
                       if (!pc.getInventory().checkItem(3000386, 1));
                       break;

                     case 3000387:
                       if (!pc.getInventory().checkItem(3000387, 1));
                       break;

                     case 3000388:
                       if (!pc.getInventory().checkItem(3000388, 1));
                       break;

                     case 3000368:
                       if (!pc.getInventory().checkItem(3000368, 1));
                       break;

                     case 3000369:
                       if (!pc.getInventory().checkItem(3000369, 1));
                       break;

                     case 3000370:
                       if (!pc.getInventory().checkItem(3000370, 1));
                       break;

                     case 3000371:
                       if (!pc.getInventory().checkItem(3000371, 1));
                       break;

                     case 3000372:
                       if (!pc.getInventory().checkItem(3000372, 1));
                       break;


                     case 3000373:
                       if (!pc.getInventory().checkItem(3000373, 1));
                       break;


                     case 3000374:
                       if (!pc.getInventory().checkItem(3000374, 1));
                       break;


                     case 3000375:
                       if (!pc.getInventory().checkItem(3000375, 1));
                       break;


                     case 3000376:
                       if (!pc.getInventory().checkItem(3000376, 1));
                       break;



                     case 3000377:
                       if (!pc.getInventory().checkItem(3000377, 1));
                       break;


                     case 3000038:
                       if (!pc.getInventory().checkItem(3000038, 1));
                       break;

                     case 4100120:
                       arrayOfInt1 = new int[] { 40308 };
                       i6 = CommonUtil.random(100);

                       if (i6 > 30)
                       {

                         createNewItem(pc, arrayOfInt1[0], CommonUtil.random(200000, 500000));
                       }
                       break;

                     case 3000039:
                       if (!pc.getInventory().checkItem(3000039, 1));
                       break;


                     case 3000196:
                       arrayOfInt1 = new int[] { 40308 };
                       i6 = CommonUtil.random(100);

                       itemid1 = new int[] { 3000197 };
                       random501 = CommonUtil.random(100);

                       arrayOfInt2 = new int[] { 3000198 };
                       i7 = CommonUtil.random(100);

                       itemid3 = new int[] { 3000199 };
                       random503 = CommonUtil.random(100);

                       if (i6 > 30)
                       {

                         createNewItem(pc, arrayOfInt1[0], CommonUtil.random(50000, 300000));
                       }
                       if (random501 > 0.3D);


                       if (i7 > 0.5D);


                       if (random503 > 0.8D);
                       break;




                     case 3000174:
                       if (!pc.getInventory().checkItem(3000174, 1));
                       break;


                     case 3000169:
                       if (!pc.getInventory().checkItem(3000169, 1));
                       break;

                     case 718:
                       if (!pc.getInventory().checkItem(718, 1));
                       break;


                     case 698:
                       l1ItemInstance4 = pc.getInventory().getItem(l1iteminstance.getId());
                       l1ItemInstance5 = pc.getInventory().getItem(l);
                       if (l1ItemInstance4 != null && l1ItemInstance5 != null) {


                         if (l1iteminstance1.getBless() < 128) {



                           int id = l1ItemInstance5.getItemId();
                           if (id == 40346 || id == 40370 || id == 40362 || id == 40354)
                             break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                           return;
                         }
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       return;

                     case 776:
                       l1ItemInstance4 = pc.getInventory().getItem(l1iteminstance.getId());
                       l1ItemInstance5 = pc.getInventory().getItem(l);
                       if (l1ItemInstance4 != null && l1ItemInstance5 != null) {

                         if (l1iteminstance1.getBless() < 128) {

                           int id = l1ItemInstance5.getItemId();
                           if (id == 4100359 || id == 4100360 || id == 4100361 || id == 4100362 || id == 4100363 || id == 4100364)
                             break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                           return;
                         }
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                         return;
                       }
                       return;

                     case 777:
                       l1ItemInstance4 = pc.getInventory().getItem(l1iteminstance.getId());
                       l1ItemInstance5 = pc.getInventory().getItem(l);
                       if (l1ItemInstance4 != null && l1ItemInstance5 != null) {


                         if (l1iteminstance1.getBless() < 128) {



                           int id = l1ItemInstance5.getItemId();
                           if (id == 4100365 || id == 4100366 || id == 4100367 || id == 4100368 || id == 4100369 || id == 4100370 || id == 4100371) {



                             int enchant = l1ItemInstance5.getEnchantLevel();
                             int[] random_item = { 4100367, 4100367, 4100367, 4100367, 4100367, 4100369, 4100369, 4100369, 4100369, 4100371, 4100371, 4100371, 4100370, 4100370, 4100368, 4100368, 4100365, 4100366 };

                             int i9 = CommonUtil.random(random_item.length);
                             L1ItemInstance new_item = pc.getInventory().storeItem(random_item[i9], 1, enchant);
                             if (random_item[i9] != 4100365 && random_item[i9] != 4100366)
                             {

                               // 假設這段代碼在某個 `switch-case` 結構中，處理獲取新物品的情況
                               switch (itemId) {
                                 // 其他 case
                                 // ...

                                 default:
                                   // 假設檢查成功獲得了新物品 new_item
                                   if (new_item != null) {
                                     pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 獲得了。")); // 發送玩家獲得物品的消息
                                     break; // 跳出 switch-case 結束當前 case 的執行
                                   } else {
                                     pc.sendPackets(new S_ServerMessage(79)); // 發送錯誤消息，代碼79可能是“無法執行”或類似的提示
                                     return; // 結束方法
                                   }
                                   break; // 為防止漏掉的 break

                                 // 其他 case
                                 // ...
                               }
                               pc.sendPackets(new S_ServerMessage(79)); // 在 switch-case 外的默認錯誤處理，發送錯誤消息79
                               return; // 結束方法
                               case 3000458:
                                 if (pc.getLevel() < 1 || pc.getLevel() < 56) {
                                   // 如果玩家等級小於1或小於56

                                   // 增加經驗值
                                   pc.add_exp(
                                           ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1L - pc.get_exp() +
                                                   (ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1L) / 30000000L
                                   );

                                   // 發送“使用了클라우디아 패스 이용권”的系統消息
                                   pc.sendPackets(new S_SystemMessage("已使用克勞迪亞通行證."));

                                   // 從玩家背包中移除1個該物品
                                   pc.getInventory().removeItem(l1iteminstance, 1);
                                 } else {
                                   // 如果玩家等級大於等於56，發送“不符合條件”的系統消息
                                   pc.sendPackets(new S_SystemMessage("僅限於等級55以下使用。"));
                                 }
                                 return; // 結束方法
                     case 41316:
                       if (!pc.hasSkillEffect(1015)) {

                         if (!pc.hasSkillEffect(1013));
                         break;
                       }
                       pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                       return;

                     case 41354:
                       if (!pc.hasSkillEffect(1013) && !pc.hasSkillEffect(1014))
                         break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                       return;

                     case 3000147:
                       j = loc_x + 32769;
                       loc_y += 32832;
                       if (itemId != 3000147);
                       break;

                               case 3000229:
                                 if (!pc.hasSkillEffect(230) && !pc.hasSkillEffect(243) && !pc.hasSkillEffect(5027) && !pc.hasSkillEffect(5002)) {
                                    // 檢查玩家是否沒有受到技能效果230, 243, 5027, 5002的影響

                                   if (pc.getMapId() >= 1708 && pc.getMapId() <= 1709) {
                                     break; // 如果玩家處於地圖ID在1708到1709之間，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage("只能在被遺忘的島/等待區域使用。"));
                                      // 發送系統消息，通知玩家只能在指定地圖使用該物品
                                   return; // 結束方法
                                 }
                                 return; // 結束方法

                               case 3000413:
                                 if (pc.getMapId() >= 1710) {
                                   break; // 如果玩家處於地圖ID大於等於1710的區域，則跳出檢查
                                 }
                                 pc.sendPackets(new S_SystemMessage("只能在被遺忘的島等待區域使用。"));
                                    // 發送系統消息，通知玩家只能在指定地圖使用該物品
                                 return; // 結束方法


                     case 41260:
                       for (L1Object object : L1World.getInstance().getVisibleObjects((L1Object)pc, 3)) {
                         if (!(object instanceof l1j.server.server.model.Instance.L1EffectInstance) || (
                           (L1NpcInstance)object).getNpcTemplate().get_npcId() != 81170)
                           continue;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(1162));
                         return;
                       }
                       break;

                     case 3000397:
                       if (!pc.hasSkillEffect(1018))
                         break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                       return;

                     case 3000398:
                       if (!pc.hasSkillEffect(1017))
                         break;  pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                       return;

                     case 3000128:
                       allBuffSkill = new int[] { 22000 };
                       l1skilluse = new L1SkillUse();
                       if (!pc.hasSkillEffect(3000128));

                       for (i = 0; i < allBuffSkill.length;) {
                         l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, 4);
                       }
                       break;


                               case 700021:
                                 if (!pc.getInventory().checkItem(50021)) {
                                      // 檢查玩家的背包中是否沒有物品ID為50021的物品

                                   if (!pc.getInventory().checkItem(700021, 1)) {
                                     break; // 檢查玩家的背包中是否沒有至少一個物品ID為700021的物品，如果沒有，則跳出檢查
                                   }
                                   pc.sendPackets(new S_ChatPacket(pc, "您已經擁有解封咒語書。"));
                                      // 發送聊天消息通知玩家已經擁有解封咒語書
                                   return; // 結束方法
                                 }
                                 pc.sendPackets(new S_ChatPacket(pc, "您已經擁有解封咒語書。"));
                                    // 發送聊天消息通知玩家已經擁有解封咒語書
                                 return; // 結束方法
                     case 700000:
                       if (pc.getLevel() < 1 || pc.getLevel() > 48)
                       {
                         if (pc.getLevel() < 49 || pc.getLevel() > 64)
                         {
                           if (pc.getLevel() < 65 || pc.getLevel() > 69)
                           {
                             if (pc.getLevel() < 70 || pc.getLevel() > 74)
                             {
                               if (pc.getLevel() < 75 || pc.getLevel() > 78)
                               {
                                 if (pc.getLevel() != 79)
                                 {
                                   if (pc.getLevel() < 80 || pc.getLevel() > 81)
                                   {
                                     if (pc.getLevel() < 82 || pc.getLevel() > 83)
                                     {
                                       if (pc.getLevel() < 84 || pc.getLevel() > 85)
                                       {
                                         if (pc.getLevel() != 86)
                                         {
                                           if (pc.getLevel() != 87)
                                           {
                                             if (pc.getLevel() != 88)
                                             {
                                               if (pc.getLevel() != 89)
                                               {
                                                 if (pc.getLevel() < 90); }  }  }  }  }  }  }  }  }  }
                           }
                         }
                       }
                       break;
                               case 4100074:
                                 EXP_TIME = System.currentTimeMillis() / 1000L; // 以秒為單位獲取當前時間
                                 if (pc.getQuizTime() + 2L <= EXP_TIME) { // 檢查玩家的測驗時間是否超過當前時間兩秒
                                   if (pc.getLevel() <= Config.ServerAdSetting.ExpPosis) {
                                     break; // 如果玩家等級小於或等於配置設定的等級，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage(Config.ServerAdSetting.ExpPosis + " 等級以下只能使用。"));
                                   // 發送系統消息，通知玩家該物品僅限於特定等級以下使用
                                   return; // 結束方法
                                 }
                                 return; // 結束方法

                               case 4100075:
                                 EXP_TIME2 = System.currentTimeMillis() / 1000L; // 以秒為單位獲取當前時間
                                 if (pc.getQuizTime() + 2L <= EXP_TIME2) { // 檢查玩家的測驗時間是否超過當前時間兩秒
                                   if (pc.getLevel() >= Config.ServerAdSetting.ExpPosis1) {
                                     break; // 如果玩家等級大於或等於配置設定的等級，則跳出檢查
                                   }
                                   pc.sendPackets(new S_SystemMessage(Config.ServerAdSetting.ExpPosis1 + " 等級以上才能使用。"));
                                   // 發送系統消息，通知玩家該物品僅限於特定等級以上使用
                                   return; // 結束方法
                                 }
                                 return; // 結束方法
                               case 4100585:
                                 long curtimeN = System.currentTimeMillis() / 1000L; // 以秒為單位獲取當前時間
                                 if (pc.getQuizTime() + 1L <= curtimeN) { // 檢查玩家的測驗時間是否超過當前時間一秒
                                   if (pc.getLevel() >= 80) { // 檢查玩家等級是否大於或等於80
                                     if (pc.getLevel() <= 82 || (pc.getLevel() >= 83 && pc.getLevel() <= 84) || (pc.getLevel() >= 85 && pc.getLevel() <= 86) || (pc.getLevel() >= 87 && pc.getLevel() <= 88) || pc.getLevel() >= 89) {
                                       break; // 如果玩家等級符合這些範圍，則跳出檢查
                                     }
                                     pc.sendPackets(new S_SystemMessage("80級以上才能使用。")); // 發送系統消息，通知玩家該物品僅限於80級以上使用
                                     return; // 結束方法
                                   }
                                 } else {
                                   long time = pc.getQuizTime() + 1L - curtimeN; // 計算剩餘時間
                                   pc.sendPackets(new S_ChatPacket(pc, time + " 秒後才可使用。")); // 發送聊天消息通知玩家剩餘時間
                                   return; // 結束方法
                                 }
                                 break;
                     case 40033:
                       if (pc.getLevel() > 49) {
                         if (pc.getElixirStats() >= 30)
                           break;  if (pc.getLevel() < 100) {

                           if (pc.getLevel() < 90 || pc.getLevel() > 99) {

                             if (pc.getAbility().getStr() >= 50);
                             break;
                           }
                           if (pc.getAbility().getStr() >= 55);
                           break;
                         }
                         if (pc.getAbility().getStr() >= 60);
                         break;
                       }
                       pc.sendPackets(4500);
                       return;

                     case 40034:
                       if (pc.getLevel() > 49) {

                         if (pc.getElixirStats() >= 30)
                           break;  if (pc.getLevel() < 100) {


                           if (pc.getLevel() < 90 || pc.getLevel() > 99) {


                             if (pc.getAbility().getCon() >= 50);
                             break;
                           }
                           if (pc.getAbility().getCon() >= 55);
                           break;
                         }
                         if (pc.getAbility().getCon() >= 60);
                         break;
                       }
                       pc.sendPackets(4500);
                       return;

                     case 40035:
                       if (pc.getLevel() > 49) {

                         if (pc.getElixirStats() >= 30)
                           break;  if (pc.getLevel() < 100) {


                           if (pc.getLevel() < 90 || pc.getLevel() > 99) {



                             if (pc.getAbility().getDex() >= 50);
                             break;
                           }
                           if (pc.getAbility().getDex() >= 55);
                           break;
                         }
                         if (pc.getAbility().getDex() >= 60);
                         break;
                       }
                       pc.sendPackets(4500);
                       return;

                     case 40036:
                       if (pc.getLevel() > 49) {

                         if (pc.getElixirStats() >= 30)
                           break;  if (pc.getLevel() < 100) {

                           if (pc.getLevel() < 90 || pc.getLevel() > 99) {


                             if (pc.getAbility().getInt() >= 50);
                             break;
                           }
                           if (pc.getAbility().getInt() >= 55);
                           break;
                         }
                         if (pc.getAbility().getInt() >= 60);
                         break;
                       }
                       pc.sendPackets(4500);
                       return;

                     case 40037:
                       if (pc.getLevel() > 49) {



                         if (pc.getElixirStats() >= 30)
                           break;  if (pc.getLevel() < 100) {


                           if (pc.getLevel() < 90 || pc.getLevel() > 99) {

                             if (pc.getAbility().getWis() >= 50);
                             break;
                           }
                           if (pc.getAbility().getWis() >= 55);
                           break;
                         }
                         if (pc.getAbility().getWis() >= 60);
                         break;
                       }
                       pc.sendPackets(4500);
                       return;

                     case 40038:
                       if (pc.getLevel() > 49) {



                         if (pc.getElixirStats() >= 30)
                           break;  if (pc.getLevel() < 100) {

                           if (pc.getLevel() < 90 || pc.getLevel() > 99) {



                             if (pc.getAbility().getCha() >= 50);
                             break;
                           }
                           if (pc.getAbility().getCha() >= 55);
                           break;
                         }
                         if (pc.getAbility().getCha() >= 60);
                         break;
                       }
                       pc.sendPackets(4500);
                       return;

                               case 3000126:
                                 if (pc.getInventory().getSize() <= 120) { // 檢查玩家的背包大小是否小於等於120
                                   if (pc.getInventory().getWeight100() <= 82) { // 檢查玩家的背包重量是否小於等於82%
                                     if (!pc.getInventory().checkItem(3000126, 1)) { // 檢查玩家是否擁有至少一個物品ID為3000126的物品
                                       break;
                                     }
                                   }
                                   pc.sendPackets(new S_SystemMessage("物品太重，無法使用。")); // 發送系統消息，通知玩家物品太重
                                   return;
                                 }
                                 pc.sendPackets(new S_SystemMessage("攜帶物品過多。")); // 發送系統消息，通知玩家攜帶物品過多
                                 return;

                               case 3000127:
                                 if (pc.getInventory().getSize() <= 120) { // 檢查玩家的背包大小是否小於等於120
                                   if (pc.getInventory().getWeight100() <= 82) { // 檢查玩家的背包重量是否小於等於82%
                                     if (!pc.getInventory().checkItem(3000127, 1)) { // 檢查玩家是否擁有至少一個物品ID為3000127的物品
                                       break;
                                     }
                                   }
                                   pc.sendPackets(new S_SystemMessage("物品太重，無法使用。")); // 發送系統消息，通知玩家物品太重
                                   return;
                                 }
                                 pc.sendPackets(new S_SystemMessage("攜帶物品過多。")); // 發送系統消息，通知玩家攜帶物品過多
                                 return;
                               case 3000123:
                                 if (!pc.is_combat_field() && !StadiumManager.getInstance().is_on_stadium(pc.getMapId())) {
                                   // 檢查玩家是否不在戰鬥區域且不在體育場內

                                   int i9 = L1CastleLocation.getCastleIdByArea((L1Character) pc);
                                   // 根據玩家所在區域獲取城堡ID

                                   if (i9 == 0) {
                                     // 如果不在任何城堡區域內

                                     if (itemId != 3000123) {
                                       // 如果物品ID不是3000123

                                       pc.sendPackets(new S_SystemMessage("只能在普通區域使用。"));
                                       // 發送系統消息，通知玩家只能在普通區域使用
                                       break;
                                     }

                                     if (pc.getZoneType() == 0) {
                                       // 如果玩家所在區域類型為0（普通區域）

                                       break; // 跳出檢查
                                     }

                                     pc.sendPackets(new S_SystemMessage("只能在普通區域（野外）使用。"));
                                     // 發送系統消息，通知玩家只能在普通區域（野外）使用
                                     return; // 結束方法
                                   }

                                   pc.sendPackets(new S_SystemMessage("在攻城區域內無法召喚。"));
                                   // 發送系統消息，通知玩家在攻城區域內無法召喚
                                   return; // 結束方法
                                 }

                                 pc.sendPackets(new S_SystemMessage("在該地圖無法使用此物品。"));
                                    // 發送系統消息，通知玩家在該地圖無法使用此物品
                                 return; // 結束方法

                     case 4100666:
                       castle_id = 0;
                       house_id = 0;
                       if (pc.getMapId() == 15482 || pc.getMapId() == 15492) {
                         if ((pc.getMapId() < 1708 || pc.getMapId() > 1712) && pc.getMapId() != 34) {
                           if (pc.getClanid() != 0) {
                             L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
                             if (clan == null);
                           }
                           if (castle_id == 0)
                           {
                             if (house_id == 0)
                             {
                               if (pc.getHomeTownId() <= 0) {
                                 int newX = 33635;
                                 int newY = 32794;
                                 short mapId = 15482;

                                 pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false));
                                 pc.getInventory().removeItem(l1iteminstance, 1);
                                 pc.send_effect(12261, true);
                                 SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, newX, newY, mapId, 8);
                               }  }  }  break;
                         }  pc.sendPackets((ServerBasePacket)new S_ServerMessage(647)); return;
                       }  pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false));
                       pc.sendPackets(3274);
                       return;
                     case 30086:
                     case 40124:
                       castle_id = 0;
                       house_id = 0;

                       if ((pc.getMapId() < 1708 || pc.getMapId() > 1712) && pc.getMapId() != 34) {
                         if (pc.getClanid() != 0) {
                           L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
                           if (clan == null);
                         }
                         if (castle_id == 0 || castle_id == 4)
                         {
                           if (castle_id != 4)
                           {
                             if (house_id == 0)
                             {
                               if (pc.getHomeTownId() <= 0) {

                                 loc = Getback.GetBack_Location(pc, true);
                                 pc.start_teleport(loc[0], loc[1], loc[2], 5, 18339, true, false);
                                 pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false));
                                 pc.getInventory().removeItem(l1iteminstance, 1);
                               }  }  }  }  break;
                       }
                       pc.sendPackets((ServerBasePacket)new S_ServerMessage(647));
                       return;
                     case 40312:
                       if (!pc.isParalyzed() && !pc.isSleeped() && !pc.isDead()) {

                         if (pc.getMapId() < 1708 || pc.getMapId() > 1709) {

                           if (!pc.hasSkillEffect(87) && !pc.hasSkillEffect(123) &&
                             !pc.hasSkillEffect(70705) && !pc.hasSkillEffect(208) &&
                             !pc.hasSkillEffect(157) && !pc.hasSkillEffect(230) &&
                             !pc.hasSkillEffect(5027) && !pc.hasSkillEffect(5002) &&
                             !pc.hasSkillEffect(66) && !pc.hasSkillEffect(243) &&
                             !pc.hasSkillEffect(242) && !pc.hasSkillEffect(77)) {
                             break;
                           }
                           return;
                         }
                         pc.sendPackets(new S_SystemMessage("在該地圖無法使用此物品。"));
                         pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false)); // 發送使玩家癱瘓的效果
                         return;

                         case 60256:
                           if (!pc.getInventory().checkItem(60255)) {
                             break; // 如果玩家沒有物品ID為60255的物品，跳出檢查
                           }
                           pc.sendPackets(new S_ServerMessage(939)); // 發送系統消息
                           pc.sendPackets(new S_SystemMessage("你擁有龍之紫水晶。")); // 發送系統消息，通知玩家持有龍之紫水晶
                           return;

                     case 89136:
                       if (pc.get_SpecialSize() != 40) {

                         if (pc.get_SpecialSize() != 0)
                         {

                           if (pc.get_SpecialSize() != 20);
                         }
                         break;
                       }
                       pc.sendPackets((ServerBasePacket)new S_ServerMessage(1622));
                       return;
                     case 51093:
                     case 51094:
                     case 51095:
                     case 51096:
                     case 51097:
                     case 51098:
                     case 51099:
                     case 51100:
                     case 51102:
                     case 51103:
                       if (pc.getMap().isSafetyZone((Point)pc.getLocation())) {



                         if (pc.getLevel() == pc.getHighLevel()) {




                           if (pc.getClanid() == 0) {


                             if (itemId != 51093 || pc.getType() != 0) {


                               if (itemId != 51094 || pc.getType() != 1) {


                                 if (itemId != 51095 || pc.getType() != 2) {


                                   if (itemId != 51096 || pc.getType() != 3) {


                                     if (itemId != 51097 || pc.getType() != 4) {


                                       if (itemId != 51098 || pc.getType() != 5) {


                                         if (itemId != 51099 || pc.getType() != 6) {


                                           if (itemId != 51100 || pc.getType() != 7) {


                                             if (itemId != 51102 || pc.getType() != 8) {


                                               if (itemId != 51103 || pc.getType() != 9) {


                                                 pc.重置屬性點();

                                                 int[] Mclass = {0, 20553, 138, 20278, 2786, 6658, 6671, 20567, 18520, 19296};
                                                 int[] Wclass = {1, 48, 37, 20279, 2796, 6661, 6650, 20577, 18499, 19299};

                                                 if (itemId != 51093 || pc.getType() == 0 || pc.get_sex() != 0) {


                                                   if (itemId != 51093 || pc.getType() == 0 || pc.get_sex() != 1) {


                                                     if (itemId != 51094 || pc.getType() == 1 || pc.get_sex() != 0) {


                                                       if (itemId != 51094 || pc.getType() == 1 || pc.get_sex() != 1) {


                                                         if (itemId != 51095 || pc.getType() == 2 || pc.get_sex() != 0) {


                                                           if (itemId != 51095 || pc.getType() == 2 || pc.get_sex() != 1) {


                                                             if (itemId != 51096 || pc.getType() == 3 || pc.get_sex() != 0) {


                                                               if (itemId != 51096 || pc.getType() == 3 || pc.get_sex() != 1) {


                                                                 if (itemId != 51097 || pc.getType() == 4 || pc.get_sex() != 0) {


                                                                   if (itemId != 51097 || pc.getType() == 4 || pc.get_sex() != 1) {


                                                                     if (itemId != 51098 || pc.getType() == 5 || pc.get_sex() != 0) {


                                                                       if (itemId != 51098 || pc.getType() == 5 || pc.get_sex() != 1) {


                                                                         if (itemId != 51099 || pc.getType() == 6 || pc.get_sex() != 0) {


                                                                           if (itemId != 51099 || pc.getType() == 6 || pc.get_sex() != 1) {


                                                                             if (itemId != 51100 || pc.getType() == 7 || pc.get_sex() != 0) {


                                                                               if (itemId != 51100 || pc.getType() == 7 || pc.get_sex() != 1) {


                                                                                 if (itemId != 51102 || pc.getType() == 8 || pc.get_sex() != 0) {

                                                                                   if (itemId != 51102 || pc.getType() == 8 || pc.get_sex() != 1) {

                                                                                     if (itemId != 51103 || pc.getType() == 9 || pc.get_sex() != 0) {

                                                                                       if (itemId == 51103 && pc.getType() != 9 && pc.get_sex() != 1)
                                                                                         ;
                                                                                     }
                                                                                   }
                                                                                 }
                                                                               }
                                                                             }
                                                                           }
                                                                         }
                                                                       }
                                                                     }
                                                                   }
                                                                 }
                                                               }
                                                             }
                                                           }
                                                         }
                                                       }
                                                     }
                                                   }
                                                 }
                                                 int basestr = 0;
                                                 int basedex = 0;
                                                 int basecon = 0;
                                                 int baseint = 0;
                                                 int basewis = 0;
                                                 int basecha = 0;
                                                 switch (pc.getType()) {

                                                 }


                                                 pc.getAbility().init();
                                                 pc.getAbility().setBaseStr(basestr);
                                                 pc.getAbility().setBaseInt(baseint);
                                                 pc.getAbility().setBaseWis(basewis);
                                                 pc.getAbility().setBaseDex(basedex);
                                                 pc.getAbility().setBaseCon(basecon);
                                                 pc.getAbility().setBaseCha(basecha);

                                                 SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().get(pc.getId());
                                                 if (noti == null) ;


                                                 if (pc.getWeapon() == null) ;

                                                 pc.getInventory().takeoffEquip(945); // 脫下玩家裝備ID為945的裝備
                                                 pc.sendPackets(new S_CharVisualUpdate(pc)); // 發送更新角色外觀的數據包

                                                 for (L1ItemInstance armor : pc.getInventory().getItems()) {
                                                   for (int type = 0; type <= 12; type++) { // 循環檢查所有裝備類型
                                                     if (armor == null) {
                                                       continue; // 如果裝備為null，跳過此次循環
                                                     }
                                                     // 您的邏輯可能需要在這裡添加
                                                   }
                                                 }
                                                 break;
                                               }

                                               // 檢查不同職業並發送相應的消息
                                               pc.sendPackets(new S_ChatPacket(pc, "你已經是黃金槍騎職業。"));
                                               return;
                                             }
                                               pc.sendPackets(new S_ChatPacket(pc, "你已經是騎士職業。"));
                                               return;
                                             }
                                               pc.sendPackets(new S_ChatPacket(pc, "你已經是戰士職業。"));
                                               return;
                                             }
                                               pc.sendPackets(new S_ChatPacket(pc, "你已經是幻術師職業。"));
                                               return;
                                             }
                                               // 發送玩家已經是特定職業的消息
                                               pc.sendPackets(new S_ChatPacket(pc, "你已經是龍騎士職業。"));
                                               return;
                                             }

                                             pc.sendPackets(new S_ChatPacket(pc, "你已經是黑暗妖精職業。"));
                                             return;
                                           }

                                           pc.sendPackets(new S_ChatPacket(pc, "你已經是法師職業。"));
                                           return;
                                         }

                                         pc.sendPackets(new S_ChatPacket(pc, "你已經是妖精職業。"));
                                         return;
                                       }

                                       pc.sendPackets(new S_ChatPacket(pc, "你已經是騎士職業。"));
                                       return;
                                     }

                                     pc.sendPackets(new S_ChatPacket(pc, "你已經是王族職業。"));
                                     return;
                                   }

                                   pc.sendPackets(new S_ChatPacket(pc, "請先退出血盟。"));
                                   return;
                                 }

                                 pc.sendPackets(new S_SystemMessage("你的角色等級已降低。請升級後再使用。"));
                                 return;
                               }

                               pc.sendPackets(new S_ChatPacket(pc, "只能在安全區域使用。"));
                               return;


                     case 700023:
                       _books = l1iteminstance.getBookMark();
                       for (i8 = 0; i8 < pc._bookmarks.size();) {
                         L1BookMark.deleteBookmark(pc, ((L1BookMark)pc._bookmarks.get(i8)).getName());
                       }
                       pc._bookmarks.clear();
                       pc._speedbookmarks.clear();
                       L1BookMark.deleteBookmarkItem(pc);

                       for (i8 = 0; i8 < _books.size();) {
                         L1BookMark.addBookmarkItem(pc, _books.get(i8));
                       }
                       break;
                   }



                   if (!Config.ServerAdSetting.DelayTimer); return; }  if (l1iteminstance.getItem().getType() != 17); return; } catch (Exception exception) { return; }
             }  pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false)); return;
           }  return;
         }  return;
       }  return;
        }  // 假設這裡是上一個代碼塊的結束大括號

        private boolean 制作秘笈物品發放(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
          // 創建指定的物品
          L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
          if (item != null) { // 如果物品創建成功
            item.setCount(count); // 設置物品數量
            item.setEnchantLevel(EnchantLevel); // 設置物品強化等級
            item.setIdentified(true); // 設置物品為已鑑定狀態

            // 檢查玩家背包是否可以添加物品
            if (pc.getInventory().checkAddItem(item, count) == 0) {
              pc.getInventory().storeItem(item); // 將物品存入玩家背包
            } else {
              pc.sendPackets(new S_ServerMessage(82)); // 發送系統消息，通知玩家背包空間不足
              return false; // 返回失敗
            }

            // 發送成功製作物品的消息
            pc.sendPackets(new S_SystemMessage("物品製作成功。"));
            pc.sendPackets(new S_ServerMessage(143, item.getLogName())); // 發送系統消息，通知玩家製作的物品名稱
            pc.send_effect(7976); // 發送特效
            return true; // 返回成功
          }
          return false; // 如果物品創建失敗，返回失敗
        }
     return false; }


   private void deleteSpell(L1PcInstance pc) {
     int player = pc.getId();
     Connection con = null;
     PreparedStatement pstm = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM character_skills WHERE char_obj_id=?");
       pstm.setInt(1, player);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   private void deletePassiveSpell(L1PcInstance pc) {
     int player = pc.getId();
     Connection con = null;
     PreparedStatement pstm = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM passive_user_info WHERE character_id=?");
       pstm.setInt(1, player);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   private void useMobEventSpownWand(L1PcInstance pc, L1ItemInstance item) {
     try {
       int[][] mobArray = { { 45042, 45043, 45005, 45010, 45011, 45573, 81201, 45609, 7320217, 7310015, 7310021, 7310028, 7310034, 7310041, 7310046, 7310051, 7310056, 7310061, 7310066, 45546, 45600, 45601, 5136, 5135, 5146, 45529, 7000093, 7310148, 7310154, 7310160 } };

       int category = 0;
       int rnd = _random.nextInt((mobArray[category]).length);
       L1SpawnUtil.spawn(pc, mobArray[category][rnd], 0, 0);
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   private void petbuy(GameClient client, int npcid, int paytype, int paycount) {
     L1PcInstance pc = client.getActiveChar();
     L1PcInventory inv = pc.getInventory();
     int charisma = pc.getAbility().getTotalCha();
     int petcost = 0;
     Object[] petlist = pc.getPetList().values().toArray();
     for (Object pet : petlist) {
       petcost += ((L1NpcInstance)pet).getPetcost();
     }
     if (pc.isCrown()) {
       charisma += 6;
     } else if (pc.isElf()) {
       charisma += 12;
     } else if (pc.isWizard()) {
       charisma += 6;
     } else if (pc.isDarkelf()) {
       charisma += 6;
     } else if (pc.isDragonknight()) {
       charisma += 6;
     } else if (pc.isBlackwizard()) {
       charisma += 6;
     }
     charisma -= petcost;
     int petCount = charisma / 6;
     if (petCount <= 0) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(489));
       return;
     }
     if (pc.getInventory().checkItem(paytype, paycount)) {
       pc.getInventory().consumeItem(paytype, paycount);
       L1SpawnUtil.spawn(pc, npcid, 0, 0);
       L1MonsterInstance targetpet = null;
       L1ItemInstance petamu = null;
       L1PetType petType = null;
       for (L1Object object : L1World.getInstance().getVisibleObjects((L1Object)pc, 3)) {
         if (object instanceof L1MonsterInstance) {
           targetpet = (L1MonsterInstance)object;
           petType = PetTypeTable.getInstance().get(targetpet.getNpcTemplate().get_npcId());
           if (petType == null || targetpet.isDead()) {
             return;
           }

           if (charisma >= 6 && inv.getSize() < 200) {
             petamu = inv.storeItem(40314, 1);
             if (petamu != null) {
               new L1PetInstance((L1NpcInstance)targetpet, pc, petamu.getId());
               pc.sendPackets((ServerBasePacket)new S_ItemName(petamu));
             }
           }
         }
       }
     }
   }
   private boolean createNewItem(L1PcInstance pc, int item_id, int count) {
     L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
     if (item != null) {
       item.setCount(count);
       if (pc.getInventory().checkAddItem(item, count) == 0) {
         pc.getInventory().storeItem(item);
       } else {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));

         return false;
       }
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getLogName()));
       return true;
     }
     return false;
   }


   private boolean createNewItem2(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
     L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
     if (item != null) {
       item.setCount(count);
       item.setEnchantLevel(EnchantLevel);
       item.setIdentified(true);
       if (pc.getInventory().checkAddItem(item, count) == 0) {
         pc.getInventory().storeItem(item);
       } else {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));

         return false;
       }
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getLogName()));
       return true;
     }
     return false;
   }

                     private static boolean 封印物品(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr, boolean identi) {
                        // 創建指定的物品
                       L1ItemInstance item = ItemTable.getInstance().createItem(item_id);

                        // 如果物品創建成功
                       if (item != null) {
                          // 設置物品數量
                         item.setCount(count);
                          // 設置物品是否已鑑定
                         item.setIdentified(identi);
                          // 設置物品強化等級
                         item.setEnchantLevel(EnchantLevel);
                          // 設置物品屬性強化等級
                         item.setAttrEnchantLevel(attr);
                          // 設置物品為已鑑定狀態
                         item.setIdentified(true);

                         // 檢查玩家背包是否可以添加物品
                         if (pc.getInventory().checkAddItem(item, count) == 0) {
                            // 將物品存入玩家背包
                           pc.getInventory().storeItem(item);
                            // 設置物品祝福狀態
                           item.setBless(Bless);
                            // 更新並保存物品
                           pc.getInventory().updateItem(item, 512);
                           pc.getInventory().saveItem(item, 512);
                         } else {
                            // 發送系統消息，通知玩家背包空間不足
                           pc.sendPackets(new S_ServerMessage(82)); // "背包空間不足，無法添加新物品。"
                           return false; // 返回失敗
                         }

                          // 發送系統消息，通知玩家獲得了物品
                         pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
                         return true; // 返回成功
                       }
                       return false; // 如果物品創建失敗，返回失敗
                     }

                     private void 重置屬性點(L1PcInstance pc) {
                       try {
                          // 使用技能重置屬性點
                         L1SkillUse l1skilluse = new L1SkillUse();
                         l1skilluse.handleCommands(pc, 44, pc.getId(), pc.getX(), pc.getY(), null, 0, 1);

                          // 如果玩家裝備了武器，取消裝備
                         if (pc.getWeapon() != null) {
                           pc.getInventory().setEquipped(pc.getWeapon(), false, false, false, false);
                         }

                            // 循環檢查並取消裝備所有已裝備的盔甲
                         for (L1ItemInstance armor : pc.getInventory().getItems()) {
                           if (armor != null && armor.isEquipped()) {
                             pc.getInventory().setEquipped(armor, false, false, false, false);
                           }
                         }

                            // 發送更新角色外觀的數據包
                         pc.sendPackets(new S_CharVisualUpdate(pc));
                            // 發送角色狀態數據包
                         pc.sendPackets(new S_OwnCharStatus2(pc));

                            // 解除癱瘓狀態
                         pc.sendPackets(new S_Paralysis(7, false));

                              // 設置返回屬性點的經驗值
                         pc.setReturnStat(pc.get_exp());

                            // 發送SPMR（技能點數和魔法抗性）的數據包
                         pc.sendPackets(new S_SPMR(pc));
                          // 發送角色屬性防禦數據包
                         pc.sendPackets(new S_OwnCharAttrDef(pc));
                          // 發送角色狀態數據包
                         pc.sendPackets(new S_OwnCharStatus2(pc));
                          // 發送返回屬性點的數據包
                         pc.sendPackets(new S_ReturnedStat(pc, 1));

                          // 保存角色數據
                         pc.save();

                       } catch (Exception e) {
                          // 捕捉並打印錯誤信息
                         System.out.println("重置屬性點命令出錯");
                         e.printStackTrace();
                       }
                     }
   private void cancelAbsoluteBarrier(L1PcInstance pc) {
     if (pc.hasSkillEffect(78)) {
       pc.removeSkillEffect(78);
     }
   }

   public void PureExp(L1PcInstance pc, int type) {
     long needExp = ExpTable.getNeedExpNextLevel(52);
     double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
     long exp = 0L;
     if (type == 1) {
       exp = (long)(needExp * 0.05D * exppenalty);
     } else if (type == 2) {
       exp = (long)(needExp * 0.06D * exppenalty);
     } else if (type == 3) {
       exp = (long)(needExp * 0.05D * exppenalty);
     } else if (type == 4) {
       exp = (long)(needExp * 0.02D * exppenalty);
     } else if (type == 5) {
       exp = (long)(needExp * 0.01D * exppenalty);
     } else {
       pc.sendPackets(3564);
     }
     pc.add_exp(exp);
     pc.send_effect(3944, true);
   }


   public String getType() {
     return "[C] C_ItemUSe2";
   }
 }


