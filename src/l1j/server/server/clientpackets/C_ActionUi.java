 package l1j.server.server.clientpackets;

 import MJShiftObject.Battle.MJShiftBattlePlayManager;
 import MJShiftObject.MJEShiftObjectType;
 import MJShiftObject.MJShiftObjectHelper;
 import MJShiftObject.MJShiftObjectManager;
 import MJShiftObject.Template.CommonServerInfo;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.Calendar;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Random;
 import java.util.TimeZone;
 import l1j.server.Config;
 import l1j.server.IndunSystem.MiniGame.L1Gambling;
 import l1j.server.IndunSystem.MiniGame.L1Gambling3;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJCaptchaSystem.Loader.MJCaptchaLoadManager;
 import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
 import l1j.server.MJDShopSystem.MJDShopItem;
 import l1j.server.MJDShopSystem.MJDShopStorage;
 import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
 import l1j.server.MJSurveySystem.MJSurveyFactory;
 import l1j.server.MJSurveySystem.MJSurveySystemLoader;
 import l1j.server.MJTemplate.Chain.Chat.MJNormalChatFilterChain;
 import l1j.server.MJTemplate.Chain.Chat.MJWhisperChatFilterChain;
 import l1j.server.MJTemplate.Chain.Chat.MJWorldChatFilterChain;
 import l1j.server.MJTemplate.Lineage2D.MJPoint;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_MSG_ANNOUNCE;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatService;
 import l1j.server.server.Account;
 import l1j.server.server.Controller.FishingTimeController;
 import l1j.server.server.Controller.LoginController;
 import l1j.server.server.GMCommands;
 import l1j.server.server.GameClient;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.UserCommands;
 import l1j.server.server.datatables.BuddyTable;
 import l1j.server.server.datatables.EventTimeTable;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.MapsTable;
 import l1j.server.server.datatables.NpcActionTable;
 import l1j.server.server.datatables.SpamTable;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.L1Buddy;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1ChatParty;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1ExcludingList;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1Party;
 import l1j.server.server.model.L1PolyMorph;
 import l1j.server.server.model.L1TownLocation;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.npc.action.L1NpcAction;
 import l1j.server.server.monitor.Logger;
 import l1j.server.server.monitor.LoggerInstance;
 import l1j.server.server.serverpackets.S_ACTION_UI;
 import l1j.server.server.serverpackets.S_ACTION_UI2;
 import l1j.server.server.serverpackets.S_ChangeCharName;
 import l1j.server.server.serverpackets.S_ChangeShape;
 import l1j.server.server.serverpackets.S_CharVisualUpdate;
 import l1j.server.server.serverpackets.S_ChatPacket;
 import l1j.server.server.serverpackets.S_Disconnect;
 import l1j.server.server.serverpackets.S_DisplayEffect;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_DoActionShop;
 import l1j.server.server.serverpackets.S_NewChat;
 import l1j.server.server.serverpackets.S_NpcChatPacket;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.S_Ping;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SocialAction;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.S_TamWindow;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.utils.CommonUtil;
 import l1j.server.server.utils.MJCommons;
 import l1j.server.server.utils.SQLUtil;









 public class C_ActionUi
   extends ClientBasePacket
 {
   Random _Random = new Random(System.nanoTime());

   private static final String C_ACTION_UI = "[C] C_ActionUi";

   private static final int CS_PING_ACK = 1001;
   private static final int ACCOUNT_TAM = 460;
   private static final int ACCOUNT_TAM_CANCEL = 480;
   private static final int ACCOUNT_TAM_UPDATE = 317;
   private static final int 액션 = 319;
   private static final int 수상한하늘정원 = 132;
   private static final int 가입대기 = 68;
   private static final int 공성관련 = 69;
   private static final int 표식설정 = 338;
   private static final int EVENT_TELEPORT = 143;
   private static final int NEWSHOP = 817;
   private static final int BLESSED_TELEPORT = 829;
   private static final int ENVIRONMENT_SETTING = 1002;

   public C_ActionUi(byte[] abyte0, GameClient client) {
     super(abyte0); try {
       int stat, chatlen, ran; Iterator<L1NpcInstance> iter; byte[] name_byte; int size; L1NpcInstance npc; String code; byte[] flag; int i; L1Party party; int len; StringBuffer sb; int j, day, charobjid, itemid, atype, k; S_SocialAction s_SocialAction;
       L1PcInstance pc = client.getActiveChar();
       int type = readH();
       if (pc == null)
         return;
       if (pc.isGhost()) {
         return;
       }


       switch (type) {


         case 1001:
           if (pc.isGm())
             S_Ping.reqForGM(pc);
           break;
         case 817:
           try {
             if (pc == null || pc.isGhost()) {
               return;
             }
             if (pc.isInvisble()) {
               pc.sendPackets((ServerBasePacket)new S_ServerMessage(755));
               return;
             }
               if (pc.getMapId() != 800) { // 註解: 如果玩家不在地圖ID為800的地圖上
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("個人商店只能在市場中開啟。")); // 註解: 發送系統消息，通知玩家個人商店只能在市場中開啟

                   return; // 註解: 返回，停止後續執行
               }
             if (pc.getMapId() != 800) {
               if (pc.isFishing()) {
                 try {
                   pc.setFishing(false);
                   pc.setFishingTime(0L);
                   pc.setFishingReady(false);
                   pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));
                   Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_CharVisualUpdate(pc));
                   FishingTimeController.getInstance().removeMember(pc);
                   pc.sendPackets((ServerBasePacket)new S_ServerMessage(2120));
                   return;
                 } catch (Exception exception) {}
               } else {

                 pc.sendPackets((ServerBasePacket)new S_ServerMessage(3405));

                 return;
               }
             }
               if (pc.getInventory().checkEquipped(22232) || pc.getInventory().checkEquipped(22234) || pc.getInventory().checkEquipped(22233) ||
                       pc.getInventory().checkEquipped(22235) || pc.getInventory().checkEquipped(22236) || pc.getInventory().checkEquipped(22237) ||
                       pc.getInventory().checkEquipped(22238) || pc.getInventory().checkEquipped(22239) || pc.getInventory().checkEquipped(22240) ||
                       pc.getInventory().checkEquipped(22241) || pc.getInventory().checkEquipped(22242) || pc.getInventory().checkEquipped(22243) ||
                       pc.getInventory().checkEquipped(22244) || pc.getInventory().checkEquipped(22245) || pc.getInventory().checkEquipped(22246) ||
                       pc.getInventory().checkEquipped(22247) || pc.getInventory().checkEquipped(22248) || pc.getInventory().checkEquipped(22249)) {
                   // 註解: 如果玩家裝備了任意一個ID為22232, 22234, 22233, 22235, 22236, 22237, 22238, 22239, 22240, 22241, 22242, 22243,
                   // 22244, 22245, 22246, 22247, 22248 或 22249 的物品
                   pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "如果裝備了符文，請將其解除。")); // 註解: 發送聊天消息，通知玩家如果裝備了符文請將其解除

                   return; // 註解: 返回，停止後續執行
               }

               boolean tradable = true; // 註解: 初始化可交易標誌為true

               int length = readH(); // 註解: 讀取數據長度
               readC(); // 註解: 讀取一個字節的數據
               int shoptype = readC(); // 註解: 讀取商店類型
               if (shoptype == 0) { // 註解: 如果商店類型為0
                   if (pc.getCurrentSpriteId() != pc.getClassId()) { // 註解: 如果當前角色的外觀ID與職業ID不一致
                       pc.sendPackets((ServerBasePacket)new S_SystemMessage("變身將被解除。")); // 註解: 發送系統消息，通知玩家變身將被解除
                       L1PolyMorph.undoPoly((L1Character)pc); // 註解: 解除變身狀態
                   }
                   if (pc.hasSkillEffect(67)) { // 註解: 如果玩家有技能效果67
                       pc.removeSkillEffect(67); // 註解: 移除技能效果67
                   } else if (pc.hasSkillEffect(80012)) { // 註解: 如果玩家有技能效果80012
                       pc.removeSkillEffect(80012); // 註解: 移除技能效果80012
                   } else if (pc.hasSkillEffect(80013)) { // 註解: 如果玩家有技能效果80013
                       pc.removeSkillEffect(80013); // 註解: 移除技能效果80013
                   }



                   for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) { // 註解: 遍歷所有玩家
                       if (target.getId() != pc.getId() && target.getAccountName().toLowerCase().equals(pc.getAccountName().toLowerCase()) && target.isPrivateShop()) {
                            // 註解: 如果目標玩家的ID與當前玩家的ID不同，且帳號名稱（不區分大小寫）相同，且處於私商狀態
                           pc.sendPackets("\f3你的另一角色已經處於無人商店狀態。"); // 註解: 發送消息通知玩家
                           pc.sendPackets("\f3請關閉商店。指令：/商店"); // 註解: 發送消息通知玩家
                           pc.setPrivateShop(false); // 註解: 設置當前玩家私商狀態為false
                           pc.상점변신 = 0; // 註解: 重置商店變身狀態
                           pc.sendPackets((ServerBasePacket)new S_DoActionGFX(pc.getId(), 3)); // 註解: 發送動作圖形數據包給客戶端
                           pc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(pc.getId(), 3)); // 註解: 廣播動作圖形數據包給所有玩家
                           pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc)); // 註解: 廣播玩家更新數據包

                           return; // 註解: 返回，停止後續執行
                       }
                   }

               int ObjectId = 0;
               int descid = 0;
               int Price = 0;
               int Count = 0;
               int enc = 0;
               int attrtype = 0;
               int attrlevel = 0;
               int bless = 0;
               int searching = 0;
               Object[] petlist = null;
               for (int m = 0; m < length; m++) {
                 int n = readC();
                 if (n == 18)
                 { readP(1);
                   for (int i2 = 0; i2 < 3; i2++) {
                     int code2 = readC();
                     if (code2 == 8) {
                       ObjectId = readBit();
                     } else if (code2 == 16) {
                       Price = readBit();
                     } else if (code2 == 24) {
                       Count = readBit();
                     }
                   }
                   if (ObjectId != -1)

                   {
                     L1ItemInstance checkItem = pc.getInventory().getItem(ObjectId);
                     if (checkItem == null || ObjectId != checkItem.getId()) {
                       pc.sendPackets((ServerBasePacket)new S_Disconnect());
                       return;
                     }
                     if (!checkItem.isStackable() && Count != 1) {
                       pc.sendPackets((ServerBasePacket)new S_Disconnect());

                       return;
                     }
                     if (checkItem.getCount() <= 0 || Count <= 0) {

                       pc.disposeShopInfo();

                       return;
                     }
                     if (checkItem.getBless() >= 128) {
                       pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, checkItem.getItem().getName()));

                       pc.disposeShopInfo();

                       return;
                     }
                       if (!checkItem.getItem().isTradable()) { // 註解: 如果物品不可交易
                           tradable = false; // 註解: 將可交易標誌設置為false
                           pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")); // 註解: 發送服務消息，通知玩家物品不可交易

                           break; // 註解: 跳出循環
                       }
                       if (checkItem.get_Carving() != 0) { // 註解: 如果物品有刻印
                           tradable = false; // 註解: 將可交易標誌設置為false
                           pc.sendPackets((ServerBasePacket)new S_SystemMessage("刻印的物品無法交易。"), true); // 註解: 發送系統消息，通知玩家刻印物品不可交易
                       }

                       if (!MJCompanionInstanceCache.is_companion_oblivion(checkItem.getId())) { // 註解: 如果物品不是同伴的遺物
                           tradable = false; // 註解: 將可交易標誌設置為false
                           pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")); // 註解: 發送服務消息，通知玩家物品不可交易
                       }

                       petlist = pc.getPetList().values().toArray(); // 註解: 獲取玩家的寵物列表
                       for (Object petObject : petlist) { // 註解: 遍歷寵物列表
                           if (petObject instanceof L1PetInstance) { // 註解: 如果寵物物件是L1PetInstance類型
                               L1PetInstance pet = (L1PetInstance)petObject; // 註解: 將物件轉換為L1PetInstance
                               if (checkItem.getId() == pet.getItemObjId()) { // 註解: 如果物品ID與寵物的物品ID匹配
                                   tradable = false; // 註解: 將可交易標誌設置為false
                                   pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")); // 註解: 發送服務消息，通知玩家物品不可交易

                                   break; // 註解: 跳出循環
                               }
                           }
                       }
                       L1DollInstance doll = pc.getMagicDoll(); // 註解: 獲取玩家的魔法娃娃實例
                       if (doll != null && // 註解: 如果魔法娃娃不為空
                               checkItem.getId() == doll.getItemObjId()) { // 註解: 且物品ID與魔法娃娃的物品ID匹配
                           tradable = false; // 註解: 將可交易標誌設置為false
                           pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")); // 註解: 發送服務消息，通知玩家物品不可交易

                           break; // 註解: 跳出循環
                       }
                     pc.addSellings(MJDShopItem.create(checkItem, Count, checkItem.getEnchantLevel(), checkItem.getAttrEnchantLevel(), checkItem.getBless(), Price, false)); }  }
                 else if (n == 26)
                 { readC();
                   for (int i2 = 0; i2 < 4; i2++) {
                     int code2 = readC();
                     if (code2 == 8) {
                       descid = readBit();
                     } else if (code2 == 16) {
                       Price = readBit();
                     } else if (code2 == 24) {
                       Count = readBit();
                     } else if (code2 == 34) {
                       readC();
                       for (int i3 = 0; i3 < 5; i3++) {
                         int code3 = readC();
                         if (code3 == 8) {
                           enc = readBit();
                         } else if (code3 == 16) {
                           attrtype = readBit();
                         } else if (code3 == 24) {
                           attrlevel = readBit();
                         } else if (code3 == 32) {
                           bless = readBit();
                         } else if (code3 == 40) {
                           searching = readBit();
                         }
                       }
                     }
                   }
                   if (descid != -1) {


                     L1Item buyitem = ItemTable.getInstance().findStoreCachedItem(descid, bless);
                     L1ItemInstance buyitem2 = ItemTable.getInstance().createItem(buyitem.getItemId());
                     if (buyitem2 != null) {


                       if (buyitem2.getCount() <= 0 || Count <= 0) {
                         pc.disposeShopInfo();
                         return;
                       }
                       if (buyitem2.getBless() >= 128) {
                         pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, buyitem2.getItem().getName()));
                         pc.disposeShopInfo();
                         return;
                       }
                         if (!buyitem2.getItem().isTradable()) { // 註解: 如果物品不可交易
                             tradable = false; // 註解: 將可交易標誌設置為false
                             pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, buyitem2.getItem().getName(), "無法交易。")); // 註解: 發送服務消息，通知玩家物品不可交易

                             break; // 註解: 跳出循環
                         }
                         if (buyitem2.get_Carving() != 0) { // 註解: 如果物品有刻印
                             tradable = false; // 註解: 將可交易標誌設置為false
                             pc.sendPackets((ServerBasePacket)new S_SystemMessage("刻印的物品無法交易。"), true); // 註解: 發送系統消息，通知玩家刻印物品不可交易
                         }

                         if (!MJCompanionInstanceCache.is_companion_oblivion(buyitem2.getId())) { // 註解: 如果物品不是同伴的遺物
                             tradable = false; // 註解: 將可交易標誌設置為false
                             pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, buyitem2.getItem().getName(), "無法交易。")); // 註解: 發送服務消息，通知玩家物品不可交易
                         }

                         petlist = pc.getPetList().values().toArray(); // 註解: 獲取玩家的寵物列表
                         for (Object petObject : petlist) { // 註解: 遍歷寵物列表
                             if (petObject instanceof L1PetInstance) { // 註解: 如果寵物物件是L1PetInstance類型
                                 L1PetInstance pet = (L1PetInstance)petObject; // 註解: 將物件轉換為L1PetInstance
                                 if (buyitem2.getId() == pet.getItemObjId()) { // 註解: 如果物品ID與寵物的物品ID匹配
                                     tradable = false; // 註解: 將可交易標誌設置為false
                                     pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, buyitem2.getItem().getName(), "無法交易。")); // 註解: 發送服務消息，通知玩家物品不可交易

                                     break; // 註解: 跳出循環
                                 }
                             }
                         }
                         L1DollInstance doll = pc.getMagicDoll(); // 註解: 獲取玩家的魔法娃娃實例
                         if (doll != null && // 註解: 如果魔法娃娃不為空
                                 buyitem2.getId() == doll.getItemObjId()) { // 註解: 且物品ID與魔法娃娃的物品ID匹配
                             tradable = false; // 註解: 將可交易標誌設置為false
                             pc.sendPackets((ServerBasePacket)new S_ServerMessage(166, buyitem2.getItem().getName(), "無法交易。")); // 註解: 發送服務消息，通知玩家物品不可交易

                             break; // 註解: 跳出循環
                         }
                       pc.addPurchasings(MJDShopItem.create(buyitem2, Count, enc, getAttrName(attrtype, attrlevel), bless, Price, true));
                     }
                   }  }
                 else { break; }

               }  if (!tradable) {
                 pc.setPrivateShop(false);
                 pc.sendPackets((ServerBasePacket)new S_DoActionGFX(pc.getId(), 3));
                 pc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(pc.getId(), 3));

                 pc.disposeShopInfo();

                 return;
               }
               int l1 = readC();
               byte[] chat = readByteL(l1);

               readC();
               int l2 = readC();
               String polynum = readS(l2);


               int poly = 0;

               pc.getNetConnection().getAccount().updateShopOpenCount();
               pc.sendPackets((ServerBasePacket)new S_PacketBox(198, (pc.getNetConnection().getAccount()).Shop_open_count), true);

               pc.setShopChat(chat);
               pc.setPrivateShop(true);
               pc.sendPackets((ServerBasePacket)new S_DoActionShop(pc.getId(), 70, chat));
               pc.broadcastPacket((ServerBasePacket)new S_DoActionShop(pc.getId(), 70, chat));


               if (polynum.equalsIgnoreCase("tradezone1")) {
                 poly = 11479;
               } else if (polynum.equalsIgnoreCase("tradezone2")) {
                 poly = 11483;
               } else if (polynum.equalsIgnoreCase("tradezone3")) {
                 poly = 11480;
               } else if (polynum.equalsIgnoreCase("tradezone4")) {
                 poly = 11485;
               } else if (polynum.equalsIgnoreCase("tradezone5")) {
                 poly = 11482;
               } else if (polynum.equalsIgnoreCase("tradezone6")) {
                 poly = 11486;
               } else if (polynum.equalsIgnoreCase("tradezone7")) {
                 poly = 11481;
               } else if (polynum.equalsIgnoreCase("tradezone8")) {
                 poly = 11484;
               }
               pc.상점변신 = poly;
               pc.setCurrentSprite(poly);
               pc.sendPackets((ServerBasePacket)new S_ChangeShape(pc.getId(), poly, 70));
               Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_ChangeShape(pc.getId(), poly, 70));
               pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));
               Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_CharVisualUpdate(pc));
               pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));



               pc.curePoison();

               GeneralThreadPool.getInstance().execute((Runnable)new MJDShopStorage((L1Character)pc, false)); break;
             }
             if (shoptype != 1 || (
               pc.isPrivateReady() && pc.isPrivateShop())) {
               break;
             }
             pc.setPrivateShop(false);
             pc.상점변신 = 0;
             pc.sendPackets((ServerBasePacket)new S_DoActionGFX(pc.getId(), 3));
             pc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(pc.getId(), 3));
             L1PolyMorph.undoPolyPrivateShop((L1Character)pc);

             GeneralThreadPool.getInstance().execute((Runnable)new MJDShopStorage((L1Character)pc, true));

           }
           catch (Exception e) {
             e.printStackTrace();
           }
           break;

         case 143:
           readH();
           readC();
           stat = readC();

           iter = EventTimeTable.getInstance().get_npc_iter();
           npc = null;
           while (iter.hasNext()) {
             npc = iter.next();
             if (npc == null) {
               continue;
             }
             if (pc.getMapId() == 5166 || pc.getMapId() == 5167 || pc.getMapId() == 666) {
               continue;
             }
             if (!npc.is_boss_alarm()) {
               continue;
             }
             if (npc.get_boss_type() != stat) {
               continue;
             }
             if (!npc.is_boss_tel()) {
               continue;
             }


             if (pc.getLevel() < Config.ServerAdSetting.YNpclevel) {
               pc.sendPackets((ServerBasePacket)new S_ServerMessage(1287));
               pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false));
               return;
             }
             if (pc.isFishing()) {
               pc.sendPackets((ServerBasePacket)new S_ServerMessage(4725));

               pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false));
               return;
             }
             if (!pc.getMap().isTeleportable()) {
               pc.sendPackets(1413);

               pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false));
               return;
             }
             if (pc.getMapId() == 2237 || pc.getMapId() == 666 || (pc.getMapId() >= 1708 && pc.getMapId() <= 1712) || (pc.getMapId() >= 12852 && pc.getMapId() <= 12862) || (pc
               .getMapId() >= 15871 && pc.getMapId() <= 15899)) {
               pc.sendPackets(1413);

               pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false));

               return;
             }
             if (MJShiftBattlePlayManager.is_shift_battle(pc)) {
               return;
             }
             int x = npc.get_boss_tel_x(), y = npc.get_boss_tel_y(), map = npc.get_boss_tel_m(), rnd = npc.get_boss_tel_rnd();





               if (x == 0 && y == 0 && map == 0) { // 註解: 如果x, y和map都為0
                   pc.sendPackets("服務已被管理員暫停。"); // 註解: 發送消息通知玩家服務已被管理員暫停

                   return; // 註解: 返回，停止後續執行
               }

               if (pc.getInventory().checkItem(40308, npc.get_boss_tel_count())) { // 註解: 如果玩家的背包中有足夠數量的40308物品（阿登納）
                   pc.getInventory().consumeItem(40308, npc.get_boss_tel_count()); // 註解: 消耗指定數量的40308物品
               } else { // 註解: 如果玩家的背包中沒有足夠數量的40308物品
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("您缺少 " + npc.get_boss_tel_count() + " 金幣")); // 註解: 發送系統消息通知玩家阿登納數量不足
                   return; // 註解: 返回，停止後續執行
               }
             MJPoint pt = MJPoint.newInstance(x, y, rnd, (short)map, 50);
             pc.set_MassTel(true);
             pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, true);
           }
           break;

         case 68:
           pc.sendPackets((ServerBasePacket)new S_ACTION_UI2(69, true));
           break;

         case 829:
           readH();
           readC();
           chatlen = readBit();
           name_byte = readByte(chatlen);
           code = new String(name_byte, "MS949");



             for (L1NpcInstance tel_map_npc : L1World.getInstance().getAllNpc()) { // 註解: 遍歷所有NPC實例
                 if (tel_map_npc.getNpcId() == 9000) { // 註解: 如果NPC的ID為9000
                     L1Object l1Object = L1World.getInstance().findObject(tel_map_npc.getId()); // 註解: 獲取對應ID的L1Object實例
                     L1NpcAction action = NpcActionTable.getInstance().get(code, pc, l1Object); // 註解: 獲取NPC動作實例
                     if (action == null) { // 註解: 如果動作實例為空
                         System.out.println(String.format("(C_ActionUI::BLESSED_TELEPORT)收到未知的動作代碼: %s", new Object[] { code })); // 註解: 打印未知動作代碼
                         pc.sendPackets(4729); // 註解: 發送錯誤消息代碼4729給玩家
                         return; // 註解: 返回，停止後續執行
                     }
                     if (pc.getInventory().checkItem(140100)) { // 註解: 如果玩家的背包中有ID為140100的物品
                         pc.getInventory().consumeItem(140100, 1); // 註解: 消耗1個ID為140100的物品
                         action.execute(code, pc, l1Object, null); // 註解: 執行NPC動作
                     }
                 }
             }
             break; // 註解: 跳出循環

           case 132: // 註解: 處理代碼132的情況
               if (MJShiftBattlePlayManager.is_shift_battle(pc)) { // 註解: 如果玩家在參與換班戰鬥
                   return; // 註解: 返回，停止後續執行
               }

               if (!pc.isPcBuff()) { // 註解: 如果玩家沒有使用PC房增益
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("只有在使用PC增益時才能使用此功能。")); // 註解: 發送系統消息通知玩家需要使用PC房增益

                   return; // 註解: 返回，停止後續執行
               }
           if (!MapsTable.getInstance().isPCTEL(pc.getMapId())) {
             pc.sendPackets(4729);
             return;
           }
           if (!pc.getMap().isTeleportable()) {
             if (!pc.getMap().isRuler()) {
               return;
             }
             if (pc.getInventory().checkItem(900111));
           }


           ran = this._Random.nextInt(4);

           if (ran == 0) {
             pc.start_teleport(32779, 32825, 622, pc.getHeading(), 18339, true, false); break;
           }  if (ran == 1) {
             pc.start_teleport(32761, 32819, 622, pc.getHeading(), 18339, true, false); break;
           }  if (ran == 2) {
             pc.start_teleport(32756, 32837, 622, pc.getHeading(), 18339, true, false); break;
           }
           pc.start_teleport(32770, 32839, 622, pc.getHeading(), 18339, true, false);
           break;



         case 338:
           size = readH();
           flag = new byte[size];
           for (i = 0; i < size; i++) {
             flag[i] = (byte)readC();
           }
           party = pc.getParty();
           if (party == null) {
             return;
           }
           for (L1PcInstance member : party.getMembers()) {
             member.sendPackets((ServerBasePacket)new S_ACTION_UI(flag));
           }
           break;


         case 460:
           pc.sendPackets((ServerBasePacket)new S_TamWindow(pc.getAccountName()));
           break;
         case 480:
           len = readC() - 1;
           readH();
           sb = new StringBuffer(len * 2);
           for (j = 0; j < len; j++) {
             sb.append(String.valueOf((byte)readC()));
           }

             day = Nexttam(sb.toString()); // 註解: 使用sb字符串計算下一個時間戳
             charobjid = TamCharid(sb.toString()); // 註解: 使用sb字符串獲取角色對象ID
             if (charobjid != pc.getId()) { // 註解: 如果獲取的角色ID與當前玩家的ID不一致
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("只有該角色可以取消此操作。")); // 註解: 發送系統消息通知玩家只有特定角色可以取消操作
                 return; // 註解: 返回，停止後續執行
             }
           itemid = 0;
           if (day != 0) {
             if (day == 3) {
               itemid = 600226;
             } else if (day == 7) {
               itemid = 3000235;
             } else if (day == 30) {
               itemid = 600227;
             }
             L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
             if (item != null) {
               pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getName() + " (1)"));
               tamcancle(sb.toString());
               pc.sendPackets((ServerBasePacket)new S_TamWindow(pc.getAccountName()));
             }
           }
           break;
         case 319:
           readH();
           readC();
           atype = readBit();
           readC();
           k = readBit();
           s_SocialAction = S_SocialAction.get(pc.getId(), atype, k);
           pc.sendPackets((ServerBasePacket)s_SocialAction, false);
           Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)s_SocialAction, false);
           s_SocialAction.clear();
           break;
         case 69:

           try { readH();
             readC();
             int castleId = readC();
             MJCastleWarBusiness.getInstance().proclaim(pc, castleId); }
           catch (Exception exception) {  }
           finally
           { clear(); }

           break;
       }





     } catch (Exception e) {
       e.printStackTrace();
     }
   }

     private void ChatWhisper(L1PcInstance whisperFrom, int chatType, int chatcount, String text, String targetName, int unknown) {
         if (MJWhisperChatFilterChain.getInstance().handle(whisperFrom, targetName, text)) { // 註解: 如果私聊消息被過濾鏈處理
             return; // 註解: 返回，停止後續執行
         }
         if (targetName.length() > 50) { // 註解: 如果目標名稱長度超過50個字符
             return; // 註解: 返回，停止後續執行
         }
         if (text.length() > 45) { // 註解: 如果文本長度超過45個字符
             whisperFrom.sendPackets((ServerBasePacket)new S_SystemMessage("私人訊息超過了允許的字數。")); // 註解: 發送系統消息通知玩家私人訊息超過字數限制

             return; // 註解: 返回，停止後續執行
         }
     if (whisperFrom.hasSkillEffect(1005)) {
       whisperFrom.sendPackets((ServerBasePacket)new S_ServerMessage(242));
       return;
     }
     if (whisperFrom.getLevel() < Config.ServerAdSetting.WHISPERCHATLEVEL) {
       whisperFrom.sendPackets((ServerBasePacket)new S_ServerMessage(404, String.valueOf(Config.ServerAdSetting.WHISPERCHATLEVEL)));

       return;
     }
         L1PcInstance whisperTo = L1World.getInstance().getPlayer(targetName); // 註解: 獲取目標玩家實例
         if (whisperTo == null) { // 註解: 如果目標玩家不存在
             whisperFrom.sendPackets((ServerBasePacket)new S_ServerMessage(73, targetName)); // 註解: 發送服務消息通知發送者目標玩家不存在

             return; // 註解: 返回，停止後續執行
         }
         if (whisperTo.hasSkillEffect(1005)) { // 註解: 如果目標玩家處於聊天禁言狀態
             whisperFrom.sendPackets((ServerBasePacket)new S_SystemMessage("目標玩家目前處於禁言狀態。")); // 註解: 發送系統消息通知發送者目標玩家被禁言
         }
     if (whisperTo.equals(whisperFrom)) {
       return;
     }

     if (whisperTo != null) {
       L1ExcludingList spamList2 = SpamTable.getInstance().getExcludeTable(whisperTo.getId());
       if (spamList2.contains(0, whisperFrom.getName())) {
         whisperFrom.sendPackets((ServerBasePacket)new S_ServerMessage(117, whisperTo.getName()));

         return;
       }
     }
     if (!whisperTo.isCanWhisper()) {
       whisperFrom.sendPackets((ServerBasePacket)new S_ServerMessage(205, whisperTo.getName()));

       return;
     }
         // 傳送私人訊息給目標玩家和自己
         whisperFrom.sendPackets((ServerBasePacket)new S_NewChat(whisperFrom, 3, chatType, text, whisperTo.getName())); // 註解: 傳送私人訊息給發送者自己
         whisperTo.sendPackets((ServerBasePacket)new S_NewChat(whisperFrom, 4, chatType, text, whisperFrom.getName())); // 註解: 傳送私人訊息給目標玩家
         LoggerInstance.getInstance().addWhisper(whisperFrom, whisperTo, text); // 註解: 記錄私人訊息
         MJMyChatService.service().whisperWriter().write(whisperFrom, text, whisperTo.getName()); // 註解: 使用聊天服務記錄私人訊息
     }

     // 公開聊天函數
     private void Chat(final L1PcInstance pc, int chatType, int chatcount, String chatText, int unknown) {
         int temporaryItemObjectId;
         L1Gambling gam;
         S_NewChat s_chatpacket;
         L1ExcludingList spamList;
         L1MonsterInstance mob;

            // 執行驗證碼認證
         MJCaptchaLoadManager.getInstance().do_auth_captcha(pc, chatText);

            // 如果使用了Shift服務器並且玩家正在轉移
         if (Config.Login.UseShiftServer && pc.is_shift_transfer()) {
             if (chatType != 0) { // 註解: 如果聊天類型不是0（公開聊天）
                 pc.sendPackets("目前無法進行聊天。"); // 註解: 發送消息通知玩家目前無法聊天

                 return; // 註解: 返回，停止後續執行
             }

             if (!MJCommons.isLetterOrDigitString(chatText, 5, 12)) { // 註解: 如果聊天文本不符合字母或數字且長度在5到12之間
                 pc.sendPackets(String.format("%s 不能作為有效的帳號使用。", new Object[] { chatText })); // 註解: 發送消息通知玩家該文本無法作為有效帳號使用

                 return; // 註解: 返回，停止後續執行
             }
         }
         Account account = Account.load(chatText); // 註解: 根據聊天文本加載帳號
         if (account != null) { // 註解: 如果帳號存在
             pc.sendPackets(String.format("%s 已經存在的帳號。", new Object[] { chatText })); // 註解: 發送消息通知玩家該帳號已存在
             return; // 註解: 返回，停止後續執行
         }
       account = pc.getAccount();
       GameClient clnt = pc.getNetConnection();
       LoginController.getInstance().logout(clnt);
       clnt.setStatus(MJClientStatus.CLNT_STS_ENTERWORLD);
       MJShiftObjectHelper.update_account_name(pc.getAccount(), chatText);
       MJShiftObjectHelper.update_account_name(pc, chatText);
       pc.setAccountName(chatText);
       account.setName(chatText);
       pc.getNetConnection().setAccount(account);
         try {
             LoginController.getInstance().login(pc.getNetConnection(), pc.getAccount()); // 註解: 嘗試使用新的帳號進行登錄
         } catch (Exception e) {
             e.printStackTrace(); // 註解: 如果登錄過程中發生異常，打印堆棧跟蹤信息
         }

         pc.sendPackets(String.format("[伺服器轉移] 帳號已更改為 %s。", new Object[] { chatText })); // 註解: 發送消息通知玩家帳號已更改

            // 開始為GM進行傳送
         pc.start_teleportForGM(
                 33443 + (MJRnd.isBoolean() ? -1 : 1) * MJRnd.next(4), // 隨機X坐標
                 32797 + (MJRnd.isBoolean() ? -1 : 1) * MJRnd.next(4), // 隨機Y坐標
                 4, // 地圖ID
                 pc.getHeading(), // 玩家朝向
                 18339, // 傳送效果
                 true, // 是否顯示效果
                 true // 是否立即傳送
         );

         int locx = 32723 + CommonUtil.random(10); // 註解: 計算隨機X坐標
         int locy = 32851 + CommonUtil.random(10); // 註解: 計算隨機Y坐標
         pc.start_teleport(locx, locy, 5166, 5, 18339, false, false); // 註解: 傳送玩家到新的隨機位置

         pc.sendPackets((ServerBasePacket)new S_DisplayEffect(10)); // 註解: 顯示傳送效果
         pc.sendPackets((ServerBasePacket)new S_Paralysis(4, true)); // 註解: 使玩家陷入癱瘓狀態
         pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "請稍候，正在移動到角色名稱更改窗口。")); // 註解: 發送消息通知玩家等待

            // 排程任務，在2.5秒後執行
         GeneralThreadPool.getInstance().schedule(new Runnable() {
             public void run() {
                 pc.getNetConnection().setStatus(MJClientStatus.CLNT_STS_CHANGENAME); // 註解: 設置客戶端狀態為更改名稱
                 pc.sendPackets((ServerBasePacket)S_ChangeCharName.getChangedStart()); // 註解: 發送開始更改名稱的封包

                 int[] loc = L1TownLocation.getGetBackLoc(7); // 註解: 獲取城鎮返回位置
                 pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true, true); // 註解: 傳送玩家到返回位置
             }
         }, 2500L); // 註解: 延遲2.5秒後執行

         return; // 註解: 返回，停止後續執行
     }



     if (pc.is_ready_server_shift()) {
       do_shift_server(pc, chatText);
       pc.set_ready_server_shift(false);

       return;
     }
     if (pc.hasSkillEffect(64) || pc.hasSkillEffect(161) || pc.hasSkillEffect(202) || pc.hasSkillEffect(1007)) {
       return;
     }
     if (pc.hasSkillEffect(1005)) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(242));
       return;
     }
     if (pc.getMapId() == 631 && !pc.isGm()) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(912));

       return;
     }
     if (pc.isDeathMatch() && !pc.isGm() && !pc.isGhost()) { // 註解: 如果玩家處於死亡競技場且不是GM且不是幽靈狀態
     pc.sendPackets((ServerBasePacket)new S_SystemMessage("在死亡競技場比賽期間禁止聊天。")); // 註解: 發送系統消息通知玩家在死亡競技場比賽期間禁止聊天

        return; // 註解: 返回，停止後續執行
        }

     if (!pc.isGm() && pc.getMapId() == 5153 && chatType != 0) { // 註解: 如果玩家不是GM且在地圖ID為5153的地圖且聊天類型不是普通聊天
     pc.sendPackets((ServerBasePacket)new S_SystemMessage("在高級戰鬥區進行期間只能使用普通聊天。")); // 註解: 發送系統消息通知玩家在高級戰鬥區進行期間只能使用普通聊天
     return; // 註解: 返回，停止後續執行
     }

     if (chatText.startsWith(".") && (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE || pc.isMonitor())) {
       final String cmd = chatText.substring(1);
       GeneralThreadPool.getInstance().execute(new Runnable()
           {
             public void run() {
               GMCommands.getInstance().handleCommands(pc, cmd);
             }
           });

       return;
     }
     switch (chatType) {
       case 0:
         if (pc.isGhost() && !pc.isGm() && !pc.isMonitor()) {
           return;
         }

         if (chatText.startsWith(".")) {
           final String cmd = chatText.substring(1);
           GeneralThreadPool.getInstance().execute(new Runnable()
               {
                 public void run() {
                   UserCommands.getInstance().handleCommands(pc, cmd);
                 }
               });

           return;
         }
         if (MJNormalChatFilterChain.getInstance().handle(pc, chatText)) {
           return;
         }

           temporaryItemObjectId = pc.getTemporaryItemObjectId(); // 註解: 獲取玩家臨時物品的對象ID
           if (temporaryItemObjectId > 0) { // 註解: 如果臨時物品對象ID大於0
               L1Object obj = L1World.getInstance().findObject(temporaryItemObjectId); // 註解: 根據ID查找物品
               if (obj.instanceOf(512)) { // 註解: 如果找到的物品是L1ItemInstance的實例（ID為512）
                   L1ItemInstance item = (L1ItemInstance)obj; // 註解: 將物品轉為L1ItemInstance實例
                   int itemId = item.getItemId(); // 註解: 獲取物品ID
                   if (itemId == 700085 || itemId == 700086) { // 註解: 如果物品ID是700085或700086（代表是某種特定物品）
                       if (MJSurveyFactory.isMegaphoneSpeaking) { // 註解: 如果目前已有其他玩家使用擴音器發言
                           pc.sendPackets("已經有一個擴音器消息在播放中。請稍候再試。"); // 註解: 通知玩家有擴音器消息在播放
                           pc.clearTemporaryItemObjectId(); // 註解: 清除玩家的臨時物品對象ID

                           return; // 註解: 返回，停止後續執行
                       }

                            // 注册并提示用户确认他们输入的消息
                       pc.sendPackets(MJSurveySystemLoader.getInstance().registerSurvey(
                               String.format("您輸入的文字為："%s"。您確定要發送嗎？", new Object[] { chatText }), // 註解: 格式化字符串，讓玩家確認消息內容
                               temporaryItemObjectId, // 註解: 設置臨時物品對象ID
                               MJSurveyFactory.createMegaphoneSurvey(temporaryItemObjectId, chatText, (itemId == 700085) ? 20 : 40), // 註解: 創建擴音器調查，根據物品ID決定發言時長
                               10000L)); // 註解: 設置超時時間為10000毫秒

                       pc.clearTemporaryItemObjectId(); // 註解: 清除玩家的臨時物品對象ID

                       return; // 註解: 返回，停止後續執行
                   }
               }
           }
           gam = new L1Gambling(); // 註解: 創建一個新的L1Gambling實例
           if (pc.isGambling()) { // 註解: 如果玩家處於賭博狀態
               if (chatText.startsWith("홀")) { // 註解: 如果聊天文本以"홀"開頭
                   gam.Gambling2(pc, chatText, 1); // 註解: 進行賭博操作，參數1
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("짝")) { // 註解: 如果聊天文本以"짝"開頭
                   gam.Gambling2(pc, chatText, 2); // 註解: 進行賭博操作，參數2
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("1")) { // 註解: 如果聊天文本以"1"開頭
                   gam.Gambling2(pc, chatText, 3); // 註解: 進行賭博操作，參數3
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("2")) { // 註解: 如果聊天文本以"2"開頭
                   gam.Gambling2(pc, chatText, 4); // 註解: 進行賭博操作，參數4
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("3")) { // 註解: 如果聊天文本以"3"開頭
                   gam.Gambling2(pc, chatText, 5); // 註解: 進行賭博操作，參數5
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("4")) { // 註解: 如果聊天文本以"4"開頭
                   gam.Gambling2(pc, chatText, 6); // 註解: 進行賭博操作，參數6
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("5")) { // 註解: 如果聊天文本以"5"開頭
                   gam.Gambling2(pc, chatText, 7); // 註解: 進行賭博操作，參數7
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("6")) { // 註解: 如果聊天文本以"6"開頭
                   gam.Gambling2(pc, chatText, 8); // 註解: 進行賭博操作，參數8
                   return; // 註解: 返回，停止後續執行
               }
           }
           if (pc.isGambling3()) { // 註解: 如果玩家處於第三種賭博狀態
               L1Gambling3 gam1 = new L1Gambling3(); // 註解: 創建一個新的L1Gambling3實例
               if (chatText.startsWith("獸人戰士")) { // 註解: 如果聊天文本以"獸人戰士"開頭
                   gam1.Gambling3(pc, chatText, 1); // 註解: 進行第三種賭博操作，參數1
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("斯巴托伊")) { // 註解: 如果聊天文本以"斯巴托伊"開頭
                   gam1.Gambling3(pc, chatText, 2); // 註解: 進行第三種賭博操作，參數2
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("野豬")) { // 註解: 如果聊天文本以"野豬"開頭
                   gam1.Gambling3(pc, chatText, 3); // 註解: 進行第三種賭博操作，參數3
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("史萊姆")) { // 註解: 如果聊天文本以"史萊姆"開頭
                   gam1.Gambling3(pc, chatText, 4); // 註解: 進行第三種賭博操作，參數4
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("骷髏")) { // 註解: 如果聊天文本以"骷髏"開頭
                   gam1.Gambling3(pc, chatText, 5); // 註解: 進行第三種賭博操作，參數5
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("狼人")) { // 註解: 如果聊天文本以"狼人"開頭
                   gam1.Gambling3(pc, chatText, 6); // 註解: 進行第三種賭博操作，參數6
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("熊怪")) { // 註解: 如果聊天文本以"熊怪"開頭
                   gam1.Gambling3(pc, chatText, 7); // 註解: 進行第三種賭博操作，參數7
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("長老")) { // 註解: 如果聊天文本以"長老"開頭
                   gam1.Gambling3(pc, chatText, 8); // 註解: 進行第三種賭博操作，參數8
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("怪物眼")) { // 註解: 如果聊天文本以"怪物眼"開頭
                   gam1.Gambling3(pc, chatText, 9); // 註解: 進行第三種賭博操作，參數9

                   return; // 註解: 返回，停止後續執行
               }
           }
           if (pc._ClassChange && pc.getInventory().checkItem(849, 1)) { // 註解: 如果玩家處於職業變更狀態且擁有ID為849的物品（數量為1）
               if (chatText.startsWith("王族")) { // 註解: 如果聊天文本以"君主"開頭
                   pc._ClassChange = false; // 註解: 將職業變更狀態設置為false
                   createNewItem(pc, 51093, 1, 1); // 註解: 為玩家創建新物品，ID為51093，數量為1
                   createNewItem(pc, 844, 12, 1); // 註解: 為玩家創建新物品，ID為844，數量為12
                   createNewItem(pc, 845, 8, 1); // 註解: 為玩家創建新物品，ID為845，數量為8
                   createNewItem(pc, 846, 1, 1); // 註解: 為玩家創建新物品，ID為846，數量為1
                   pc.getInventory().consumeItem(849, 1); // 註解: 消耗玩家背包中的ID為849的物品1個
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("騎士")) { // 註解: 如果聊天文本以"騎士"開頭
                   pc._ClassChange = false; // 註解: 將職業變更狀態設置為false
                   createNewItem(pc, 51094, 1, 1); // 註解: 為玩家創建新物品，ID為51094，數量為1
                   createNewItem(pc, 844, 12, 1); // 註解: 為玩家創建新物品，ID為844，數量為12
                   createNewItem(pc, 845, 8, 1); // 註解: 為玩家創建新物品，ID為845，數量為8
                   createNewItem(pc, 846, 1, 1); // 註解: 為玩家創建新物品，ID為846，數量為1
                   pc.getInventory().consumeItem(849, 1); // 註解: 消耗玩家背包中的ID為849的物品1個
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("戰士")) { // 註解: 如果聊天文本以"戰士"開頭
                   pc._ClassChange = false; // 註解: 將職業變更狀態設置為false
                   createNewItem(pc, 51100, 1, 1); // 註解: 為玩家創建新物品，ID為51100，數量為1
                   createNewItem(pc, 844, 11, 1); // 註解: 為玩家創建新物品，ID為844，數量為11
                   createNewItem(pc, 845, 8, 1); // 註解: 為玩家創建新物品，ID為845，數量為8
                   createNewItem(pc, 846, 2, 1); // 註解: 為玩家創建新物品，ID為846，數量為2
                   pc.getInventory().consumeItem(849, 1); // 註解: 消耗玩家背包中的ID為849的物品1個
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("龍騎士")) { // 註解: 如果聊天文本以"龍騎士"開頭
                   pc._ClassChange = false; // 註解: 將職業變更狀態設置為false
                   createNewItem(pc, 51098, 1, 1); // 註解: 為玩家創建新物品，ID為51098，數量為1
                   createNewItem(pc, 844, 12, 1); // 註解: 為玩家創建新物品，ID為844，數量為12
                   createNewItem(pc, 845, 8, 1); // 註解: 為玩家創建新物品，ID為845，數量為8
                   createNewItem(pc, 846, 1, 1); // 註解: 為玩家創建新物品，ID為846，數量為1
                   pc.getInventory().consumeItem(849, 1); // 註解: 消耗玩家背包中的ID為849的物品1個
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("黑暗妖精")) { // 註解: 如果聊天文本以"黑暗妖精"開頭
                   pc._ClassChange = false; // 註解: 將職業變更狀態設置為false

                   createNewItem(pc, 51097, 1, 1); // 註解: 為玩家創建新物品，ID為51097，數量為1
                   createNewItem(pc, 844, 12, 1); // 註解: 為玩家創建新物品，ID為844，數量為12
                   createNewItem(pc, 845, 8, 1); // 註解: 為玩家創建新物品，ID為845，數量為8
                   createNewItem(pc, 846, 1, 1); // 註解: 為玩家創建新物品，ID為846，數量為1
                   pc.getInventory().consumeItem(849, 1); // 註解: 消耗玩家背包中的ID為849的物品1個
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("妖精")) { // 註解: 如果聊天文本以"妖精"開頭
                   pc._ClassChange = false; // 註解: 將職業變更狀態設置為false
                   createNewItem(pc, 51095, 1, 1); // 註解: 為玩家創建新物品，ID為51095，數量為1
                   createNewItem(pc, 844, 12, 1); // 註解: 為玩家創建新物品，ID為844，數量為12
                   createNewItem(pc, 845, 8, 1); // 註解: 為玩家創建新物品，ID為845，數量為8
                   createNewItem(pc, 846, 1, 1); // 註解: 為玩家創建新物品，ID為846，數量為1
                   pc.getInventory().consumeItem(849, 1); // 註解: 消耗玩家背包中的ID為849的物品1個
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("法師")) { // 註解: 如果聊天文本以"魔法師"開頭
                   pc._ClassChange = false; // 註解: 將職業變更狀態設置為false
                   createNewItem(pc, 51096, 1, 1); // 註解: 為玩家創建新物品，ID為51096，數量為1
                   createNewItem(pc, 844, 12, 1); // 註解: 為玩家創建新物品，ID為844，數量為12
                   createNewItem(pc, 845, 8, 1); // 註解: 為玩家創建新物品，ID為845，數量為8
                   createNewItem(pc, 846, 1, 1); // 註解: 為玩家創建新物品，ID為846，數量為1
                   pc.getInventory().consumeItem(849, 1); // 註解: 消耗玩家背包中的ID為849的物品1個
                   return; // 註解: 返回，停止後續執行
               }
               if (chatText.startsWith("幻術師")) { // 註解: 如果聊天文本以"幻術師"開頭
                   pc._ClassChange = false; // 註解: 將職業變更狀態設置為false
                   createNewItem(pc, 51099, 1, 1); // 註解: 為玩家創建新物品，ID為51099，數量為1
                   createNewItem(pc, 844, 12, 1); // 註解: 為玩家創建新物品，ID為844，數量為12
                   createNewItem(pc, 845, 8, 1); // 註解: 為玩家創建新物品，ID為845，數量為8
                   createNewItem(pc, 846, 1, 1); // 註解: 為玩家創建新物品，ID為846，數量為1
                   pc.getInventory().consumeItem(849, 1); // 註解: 消耗玩家背包中的ID為849的物品1個
                   return; // 註解: 返回，停止後續執行
               }
                    // 如果玩家輸入的職業不匹配上述任何條件
               pc._ClassChange = false; // 註解: 將職業變更狀態設置為false
               pc.sendPackets("您選擇了錯誤的職業。"); // 註解: 發送消息通知玩家選擇的職業不正確

               return; // 註解: 返回，停止後續執行
           }

         if (pc.is_combat_field()) {
           return;
         }


         pc.sendPackets((ServerBasePacket)new S_NewChat(chatType, chatcount, chatText, "", unknown));
         s_chatpacket = new S_NewChat(chatType, chatText, chatcount, pc);
         spamList = SpamTable.getInstance().getExcludeTable(pc.getId());

         if (!spamList.contains(0, pc.getName())) {
           pc.sendPackets((ServerBasePacket)s_chatpacket);
         }

         for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer((L1Object)pc)) {
           if (listner.getMapId() == 621 && !listner.isGm() && !pc.isGm()) {
             continue;
           }
           if (!listner.isOutsideChat() && !pc.isGm()) {
             L1Buddy buddy = BuddyTable.getInstance().getBuddy(pc.getId(), listner.getName());
             L1Party party = listner.getParty();
             L1ChatParty cparty = listner.getChatParty();

             if (buddy != null || (listner.getClanid() > 0 && listner.getClanid() == pc.getClanid()) || (party != null && party
               .isMember(pc)) || (cparty != null && cparty.isMember(pc))) {
               continue;
             }
           }
           L1ExcludingList spamList3 = SpamTable.getInstance().getExcludeTable(listner.getId());
           if (!spamList3.contains(0, pc.getName())) {
             listner.sendPackets((ServerBasePacket)s_chatpacket);
           }
         }

         mob = null;
         for (L1Object obj : pc.getKnownObjects()) {
           if (obj instanceof L1MonsterInstance) {
             mob = (L1MonsterInstance)obj;
             if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
               mob.broadcastPacket((ServerBasePacket)new S_NpcChatPacket((L1NpcInstance)mob, chatText, 0));
             }
           }
         }


           if (pc.getLevel() >= Config.CharSettings.LimitLevel && !pc.isGm()) { // 註解: 如果玩家等級達到或超過限制等級且不是GM
               Account.ban(pc.getAccountName(), 95); // 註解: 將玩家帳號封禁，封禁理由代碼為95
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(pc.getName() + " 的帳號已被封禁。")); // 註解: 發送系統消息通知玩家帳號已被封禁
               pc.sendPackets((ServerBasePacket)new S_Disconnect()); // 註解: 發送斷線封包，斷線玩家

               if (pc.getOnlineStatus() == 1) { // 註解: 如果玩家處於在線狀態
                   pc.sendPackets((ServerBasePacket)new S_Disconnect()); // 註解: 再次發送斷線封包，確保玩家斷線
               }
               System.out.println("▶ 配置等級漏洞通過普通聊天[封禁]：" + pc.getName()); // 註解: 在系統控制台打印封禁信息
           }
           break; // 註解: 中斷循環或結束條件

       case 3:
         if (MJWorldChatFilterChain.getInstance().handle(pc, chatText)) {
           return;
         }
         chatWorld(pc, chatType, chatcount, chatText);
         break;

       case 4:
         if (pc.getClanid() != 0) {
           L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
           if (clan != null) {
             S_NewChat s_chatpacket1 = new S_NewChat(pc, 4, chatType, chatText, "");
             LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Clan, pc, chatText);
             for (L1PcInstance listner : clan.getOnlineClanMember()) {
               L1ExcludingList spamList4 = SpamTable.getInstance().getExcludeTable(listner.getId());
               if (!spamList4.contains(0, pc.getName())) {
                 listner.sendPackets((ServerBasePacket)s_chatpacket1);
               }
             }
             MJMyChatService.service().pledgeWriter().write(pc, chatText, "");
           }
         }
         break;

       case 11:
         if (pc.isInParty()) {
           S_NewChat s_chatpacket2 = new S_NewChat(pc, 4, chatType, chatText, "");
           LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Party, pc, chatText);
           for (L1PcInstance listner : pc.getParty().getMembers()) {
             L1ExcludingList spamList11 = SpamTable.getInstance().getExcludeTable(listner.getId());
             if (!spamList11.contains(0, pc.getName())) {
               listner.sendPackets((ServerBasePacket)s_chatpacket2);
             }
           }
         }
         break;

       case 12:
         if (pc.isGm()) {
           chatWorld(pc, chatType, chatcount, chatText); break;
         }
         if (MJWorldChatFilterChain.getInstance().handle(pc, chatText)) {
           return;
         }
         chatWorld(pc, 12, chatcount, chatText);
         break;

       case 13:
         if (pc.getClanid() != 0) {
           L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
           int rank = pc.getClanRank();
           if (clan != null && (rank == 10 || rank == 9)) {
             S_NewChat s_chatpacket3 = new S_NewChat(pc, 4, chatType, chatText, "");
             LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Alliance, pc, chatText);

             for (L1PcInstance listner : clan.getOnlineClanMember()) {
               int listnerRank = listner.getClanRank();
               L1ExcludingList spamList13 = SpamTable.getInstance().getExcludeTable(listner.getId());
               if (!spamList13.contains(0, pc.getName()) && (listnerRank == 10 || listnerRank == 9)) {
                 listner.sendPackets((ServerBasePacket)s_chatpacket3);
               }
             }
           }
         }
         break;

       case 14:
         if (pc.isInChatParty()) {
           S_NewChat s_chatpacket4 = new S_NewChat(pc, 4, chatType, chatText, "");
           LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Party, pc, chatText);



           for (L1PcInstance listner : pc.getChatParty().getMembers()) {
             L1ExcludingList spamList14 = SpamTable.getInstance().getExcludeTable(listner.getId());
             if (!spamList14.contains(0, pc.getName())) {
               listner.sendPackets((ServerBasePacket)s_chatpacket4);
             }
           }
         }
         break;

       case 15:
         if (pc.getClanid() != 0) {
           L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
           int rank = pc.getClanRank();
           if (clan != null && (rank == 10 || rank == 9)) {
             S_NewChat s_chatpacket3 = new S_NewChat(pc, 4, chatType, chatText, "");
             LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Alliance, pc, chatText);

             for (L1PcInstance listner : clan.getOnlineClanMember()) {
               int listnerRank = listner.getClanRank();
               L1ExcludingList spamList13 = SpamTable.getInstance().getExcludeTable(listner.getId());
               if (!spamList13.contains(0, pc.getName()) && (listnerRank == 10 || listnerRank == 9)) {
                 listner.sendPackets((ServerBasePacket)s_chatpacket3);
               }
             }
           }
         }
         break;

       case 17:
         if (pc.getClanid() != 0) {
           L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
           if (clan != null && pc.isCrown() && pc.getId() == clan.getLeaderId()) {
             S_NewChat s_chatpacket5 = new S_NewChat(pc, 4, chatType, chatText, "");
             LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Guardian, pc, chatText);



             for (L1PcInstance listner : clan.getOnlineClanMember()) {
               L1ExcludingList spamList17 = SpamTable.getInstance().getExcludeTable(listner.getId());
               if (!spamList17.contains(0, pc.getName())) {
                 listner.sendPackets((ServerBasePacket)s_chatpacket5);
               }
             }
           }
         }
         break;
     }  }


     private void chatWorld(L1PcInstance pc, int chatType, int chatcount, String text) {
         try {
             if (pc.getLevel() >= Config.CharSettings.LimitLevel && !pc.isGm()) { // 註解: 如果玩家等級達到或超過限制等級且不是GM
                 Account.ban(pc.getAccountName(), 95); // 註解: 將玩家帳號封禁，封禁理由代碼為95
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(pc.getName() + " 的帳號已被封禁。")); // 註解: 發送系統消息通知玩家帳號已被封禁
                 pc.sendPackets((ServerBasePacket)new S_Disconnect()); // 註解: 發送斷線封包，斷線玩家

                 if (pc.getOnlineStatus() == 1) { // 註解: 如果玩家處於在線狀態
                     pc.sendPackets((ServerBasePacket)new S_Disconnect()); // 註解: 再次發送斷線封包，確保玩家斷線
                 }
                 System.out.println("▶ 配置等級漏洞通過世界聊天[封禁]：" + pc.getName()); // 註解: 在系統控制台打印封禁信息
             }

             if (pc.isGm() || pc.getAccessLevel() == 1) { // 註解: 如果玩家是GM或具有訪問等級1
                 if (chatType == 3) { // 註解: 如果聊天類型為3
                     if (pc.Notice) { // 註解: 如果玩家的公告狀態為true
                         L1World.getInstance().broadcastPacketToAll(SC_MSG_ANNOUNCE.AnnounceMessage(1, "[公告] : " + text + ""), true); // 註解: 向全體廣播公告消息
                     } else {
                         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_NewChat(pc, 4, 3, text, "[******] ")); // 註解: 向全體廣播新聊天消息
                     }
                     LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Global, pc, text); // 註解: 將聊天記錄添加到日誌
                 } else if (chatType == 12) { // 註解: 如果聊天類型為12
                     if (pc.Notice) { // 註解: 如果玩家的公告狀態為true
                         L1World.getInstance().broadcastPacketToAll(SC_MSG_ANNOUNCE.AnnounceMessage(1, "[公告] : " + text + ""), true); // 註解: 向全體廣播公告消息
                     } else {
                         L1World.getInstance().broadcastPacketToAll(SC_MSG_ANNOUNCE.AnnounceMessage(1, "[公告] : " + text + ""), true); // 註解: 向全體廣播公告消息
                     }
                 }
             }
         } catch (Exception e) {
             e.printStackTrace(); // 註解: 捕捉異常並打印堆棧跟蹤
         }
     }

         MJMyChatService.service().worldWriter().write(pc, text, "");
       } else if (pc.getLevel() >= Config.ServerAdSetting.GLOBALCHATLEVEL) {
         if (L1World.getInstance().isWorldChatElabled()) {
           if (pc.get_food() >= 12) {
             pc.sendPackets((ServerBasePacket)new S_PacketBox(11, pc.get_food()));
             if (chatType == 12) {
               pc.sendPackets((ServerBasePacket)new S_PacketBox(11, pc.get_food()));
             } else if (chatType == 3) {
               pc.sendPackets((ServerBasePacket)new S_PacketBox(11, pc.get_food()));
               LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Global, pc, text);
             }
             pc.sendPackets((ServerBasePacket)new S_PacketBox(11, pc.get_food()));
             MJMyChatService.service().worldWriter().write(pc, text, "");
             for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
               L1ExcludingList spamList15 = SpamTable.getInstance().getExcludeTable(listner.getId());
               if (!spamList15.contains(0, pc.getName())) {
                 if (listner.isShowTradeChat() && chatType == 12) {
                   listner.sendPackets((ServerBasePacket)new S_NewChat(pc, 4, chatType, text, ""));
                   listner.sendPackets((ServerBasePacket)new S_NewChat(pc, 4, 3, text, "")); continue;
                 }  if (listner.isShowWorldChat() && chatType == 3) {
                   listner.sendPackets((ServerBasePacket)new S_NewChat(pc, 4, chatType, text, ""));
                 }
               }
             }
           } else {
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(462));
           }
         } else {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(510));
         }
       } else {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(195, String.valueOf(Config.ServerAdSetting.GLOBALCHATLEVEL)));
       }
     } catch (Exception exception) {}
   }


     private void do_shift_server(L1PcInstance pc, String chatText) {
             if (!pc.getInventory().checkItem(MJShiftObjectManager.getInstance().get_character_transfer_itemid())) { // 註解: 檢查玩家背包中是否有轉服券
             pc.sendPackets("無法在背包中找到轉服券，因此取消轉服操作。"); // 註解: 發送消息通知玩家無法找到轉服券
             return; // 註解: 返回，停止後續執行
             }
             try {
             List<CommonServerInfo> servers = MJShiftObjectManager.getInstance().get_commons_servers(true); // 註解: 獲取可用服務器列表
             if (servers == null || servers.size() <= 0) { // 註解: 如果沒有可用服務器
             pc.sendPackets("目前沒有可移動的服務器。"); // 註解: 發送消息通知玩家沒有可用服務器
             return; // 註解: 返回，停止後續執行
             }
             CommonServerInfo select_server_info = null; // 註解: 初始化選擇的服務器信息
             for (CommonServerInfo csInfo : servers) { // 註解: 遍歷服務器列表
             if (csInfo.server_description.equals(chatText)) { // 註解: 如果服務器描述與聊天文本匹配
             select_server_info = csInfo; // 註解: 設置選擇的服務器信息
             break; // 註解: 跳出循環
             }
             }
             if (select_server_info == null) { // 註解: 如果沒有匹配的服務器
             pc.sendPackets(String.format("找不到 %s。", new Object[] { chatText })); // 註解: 發送消息通知玩家找不到服務器
             return; // 註解: 返回，停止後續執行
             }
             if (!select_server_info.server_is_on) { // 註解: 如果選擇的服務器未開啟
             pc.sendPackets(String.format("%s 無法轉移（服務器關閉）。", new Object[] { chatText })); // 註解: 發送消息通知玩家服務器關閉
             return; // 註解: 返回，停止後續執行
             }
             if (!select_server_info.server_is_transfer) { // 註解: 如果選擇的服務器不允許轉移
             pc.sendPackets(String.format("%s 無法轉移（功能關閉）。", new Object[] { chatText })); // 註解: 發送消息通知玩家功能關閉
             return; // 註解: 返回，停止後續執行
             }
             if (!pc.getInventory().consumeItem(MJShiftObjectManager.getInstance().get_character_transfer_itemid(), 1)) // 註解: 消耗玩家背包中的轉服券
             return; // 註解: 返回，停止後續執行
             MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.TRANSFER, select_server_info.server_identity, ""); // 註解: 執行轉服操作
             System.out.println(String.format("%s 使用了轉服券（%s）。", new Object[] { pc.getName(), select_server_info.server_description })); // 註解: 在系統控制台打印轉服信息
             return; // 註解: 返回，停止後續執行
             } catch (Exception e) { // 註解: 捕捉異常
             e.printStackTrace(); // 註解: 打印異常堆棧跟蹤
             return; // 註解: 返回，停止後續執行
             }
             }
   }
   public int Nexttam(String encobj) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     int day = 0;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT day FROM `tam` WHERE encobjid = ? order by id asc limit 1");
       pstm.setString(1, encobj);
       rs = pstm.executeQuery();
       while (rs.next()) {
         day = rs.getInt("Day");
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return day;
   }

   public int TamCharid(String encobj) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     int objid = 0;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT objid FROM `tam` WHERE encobjid = ? order by id asc limit 1");
       pstm.setString(1, encobj);
       rs = pstm.executeQuery();
       while (rs.next()) {
         objid = rs.getInt("objid");
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return objid;
   }

   public void tamcancle(String objectId) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("delete from tam where encobjid = ? order by id asc limit 1");
       pstm.setString(1, objectId);
       pstm.executeUpdate();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public Calendar getRealTime() {
     TimeZone _tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
     Calendar cal = Calendar.getInstance(_tz);
     return cal;
   }


   public String getType() {
     return "[C] C_ActionUi";
   }

   class PrivateShopReadier implements Runnable {
     private L1PcInstance _pc;

     PrivateShopReadier(L1PcInstance pc) {
       this._pc = pc;
     }


     public void run() {
       if (this._pc != null)
         this._pc.setPrivateReady(false);
     }
   }

   private boolean createNewItem(L1PcInstance pc, int item_id, int count, int bless) {
     L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
     if (item != null) {
       item.setCount(count);
       item.setIdentified(true);
       if (pc.getInventory().checkAddItem(item, count) == 0) {
         pc.getInventory().storeItem(item);
         item.setBless(bless);
         pc.getInventory().updateItem(item, 512);
         pc.getInventory().saveItem(item, 512);
       } else {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));
         return false;
       }
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getLogName()), true);
       return true;
     }
     return false;
   }


   private int getAttrName(int attrkind, int attrlevel) {
     int attrinstance = 0;
     if (attrkind == 1) {
       if (attrlevel == 1) {
         attrinstance = 1;
       } else if (attrlevel == 2) {
         attrinstance = 2;
       } else if (attrlevel == 3) {
         attrinstance = 3;
       } else if (attrlevel == 4) {
         attrinstance = 4;
       } else if (attrlevel == 5) {
         attrinstance = 5;
       }
     } else if (attrkind == 2) {
       if (attrlevel == 1) {
         attrinstance = 6;
       } else if (attrlevel == 2) {
         attrinstance = 7;
       } else if (attrlevel == 3) {
         attrinstance = 8;
       } else if (attrlevel == 4) {
         attrinstance = 9;
       } else if (attrlevel == 5) {
         attrinstance = 10;
       }
     } else if (attrkind == 3) {
       if (attrlevel == 1) {
         attrinstance = 11;
       } else if (attrlevel == 2) {
         attrinstance = 12;
       } else if (attrlevel == 3) {
         attrinstance = 13;
       } else if (attrlevel == 4) {
         attrinstance = 14;
       } else if (attrlevel == 5) {
         attrinstance = 15;
       }
     } else if (attrkind == 4) {
       if (attrlevel == 1) {
         attrinstance = 16;
       } else if (attrlevel == 2) {
         attrinstance = 17;
       } else if (attrlevel == 3) {
         attrinstance = 18;
       } else if (attrlevel == 4) {
         attrinstance = 19;
       } else if (attrlevel == 5) {
         attrinstance = 20;
       }
     }
     return attrinstance;
   }
 }


