 package l1j.server.server.clientpackets;

 import MJShiftObject.Battle.MJShiftBattlePlayManager;
 import java.util.Collection;
 import java.util.Iterator;
 import java.util.List;
 import l1j.server.Config;
 import l1j.server.EventSystem.EventSystemInfo;
 import l1j.server.EventSystem.EventSystemLoader;
 import l1j.server.MJDeathPenalty.Exp.MJDeathPenaltyExpModel;
 import l1j.server.MJDeathPenalty.Exp.MJDeathPenaltyexpDatabaseLoader;
 import l1j.server.MJInstanceSystem.MJInstanceEnums;
 import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
 import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
 import l1j.server.MJSurveySystem.MJSurveySystemLoader;
 import l1j.server.MJTemplate.Lineage2D.MJPoint;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_ENTER_INDUN_ROOM_ACK;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.MJWarSystem.MJWar;
 import l1j.server.MJWarSystem.MJWarFactory;
 import l1j.server.server.Account;
 import l1j.server.server.GMCommands;
 import l1j.server.server.GameClient;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.BossMonsterSpawnList;
 import l1j.server.server.datatables.CharacterCustomQuestTable;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.datatables.EventTimeTable;
 import l1j.server.server.datatables.ExpTable;
 import l1j.server.server.datatables.HouseTable;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.datatables.PetTable;
 import l1j.server.server.datatables.PolyTable;
 import l1j.server.server.datatables.ServerCustomQuestTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.L1CastleLocation;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1ChatParty;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1ClanJoin;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1Party;
 import l1j.server.server.model.L1PolyMorph;
 import l1j.server.server.model.L1Question;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_CharAmount;
 import l1j.server.server.serverpackets.S_CharVisualUpdate;
 import l1j.server.server.serverpackets.S_ChatPacket;
 import l1j.server.server.serverpackets.S_ClanAttention;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.S_Resurrection;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_Sound;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.S_Trade;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.CustomQuest;
 import l1j.server.server.templates.L1Boss;
 import l1j.server.server.templates.L1House;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.templates.L1Pet;




 public class C_Attr
   extends ClientBasePacket
 {
   private static final String C_ATTR = "[C] C_Attr";
   public static final int MSGCODE_6008_BOSS = 1;
   public static final int MSGCODE_6008_LFC = 2;
   public static final int MSGCODE_6008_KDINIT = 3;
   public static final int MSGCODE_6008_Name = 6;
   public static final int MSGCODE_6008_EVENT_BOSS = 12;
   public static final int MSGCODE_6008_QUESTION = 24;
   public static final int MSGCODE_6008_INDUN_INVITE = 48;
   public static final int MSGCODE_6008_CONSOLE = 96;
   public static final int MSGCODE_6008_CLAN_ALLIANCE_KICK = 192;
   public static final int MSGCODE_6008_QUEST = 288;
   public static final int MSGCODE_6008_RESET = 70000;
   public static final int MSGCODE_6008_RESET_CONFIRM = 70001;
   public static final int MSGCODE_NO = 0;
   public static final int MSGCODE_YES = 1;
   public static final int CLAN_ALLIANCE_CLEAR = 5460;
   public static final int CLAN_ALLIANCE_JOIN = 223;
   public static final int CLAN_ALLIANCE_WITHDRAWAL = 1210;

   public C_Attr(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0); int attrcode, c; String name; L1PcInstance allianceLeader; int PcClanId, TargetClanId; String PcClanName, TargetClanName; L1Clan PcClan, TargetClan; boolean maple, ring, ring2; L1PcInstance joinPc, enemyLeader; String clanName, enemyClanName; L1PcInstance trading_partner; L1Npc npc, npc3; L1PcInstance resusepc1, resusepc2; int len; L1PetInstance pet; int houseId; L1PcInstance fightPc, partner, chatPc, target;
     int i = readH();


     int msgIdx = 0;

     if (i == 479) {
       attrcode = i;
     } else {
       msgIdx = readD();
       attrcode = readH();
     }

     final L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null) {
       return;
     }
     if (attrcode == 6008) {
       int j; switch (msgIdx) {
         case 192:
           j = readC();
           if (j == 1) {
             L1Clan kick_clan = L1World.getInstance().getClan(pc.getTempID());
             if (kick_clan.isAlliance_leader()) {
               return;
             }
             if (kick_clan != null && pc.getClan().AllianceSize() > 0) {
               Integer[] arrayOfInteger; int k; byte b; for (arrayOfInteger = pc.getClan().Alliance(), k = arrayOfInteger.length, b = 0; b < k; ) { int clanid = arrayOfInteger[b].intValue();
                 if (clanid != 0) {

                   L1Clan clan = L1World.getInstance().getClan(clanid);
                   if (clan != null)

                   { clan.removeAlliance(kick_clan.getClanId());
                     if (pc.getClan().AllianceSize() == 1) {
                       pc.getClan().AllianceDelete();
                       pc.getClan().setAlliance_leader(false);
                       for (L1PcInstance tempPc : pc.getClan().getOnlineClanMember()) {
                         tempPc.sendPackets((ServerBasePacket)new S_ServerMessage(1204, kick_clan.getClanName()));
                         SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, pc.getClanid(), false, true);
                       }
                     } else {
                       for (L1PcInstance tempPc : clan.getOnlineClanMember()) {
                         tempPc.sendPackets((ServerBasePacket)new S_ServerMessage(1204, kick_clan.getClanName()));
                         SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, clan.getClanId(), false, true);
                       }
                       for (L1PcInstance tempPc : kick_clan.getOnlineClanMember()) {
                         tempPc.sendPackets((ServerBasePacket)new S_ServerMessage(1204, kick_clan.getClanName()));
                         SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, pc.getClanid(), false, true);
                       }
                     }
                     kick_clan.AllianceDelete();
                     kick_clan.setAlliance_leader(false);
                     ClanTable.getInstance().updateClan(clan); }
                 }  b++; }

             }  pc.setTempID(0);
             ClanTable.getInstance().updateClan(pc.getClan()); break;
           }  if (j == 0) {
             pc.setTempID(0);
           }
           break;
             case 96: // 註解: 處理操作碼為96的情況
                 j = readC(); // 註解: 讀取一個字節
                 if (j == 0) { // 註解: 如果讀取的字節為0
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("您已拒絕參加移動。")); // 註解: 向玩家發送消息，通知已拒絕參加移動
                     pc.setConsole_type(0); // 註解: 將玩家的控制台類型設置為0
                     break; // 註解: 跳出 switch 語句
                 }
                 if (j == 1) { // 註解: 如果讀取的字節為1
                     if (pc.getLevel() < Config.ServerAdSetting.YNpclevel) { // 註解: 如果玩家等級小於要求的等級
                         pc.sendPackets((ServerBasePacket)new S_SystemMessage("等級 " + Config.ServerAdSetting.YNpclevel + " 以上才可以使用。")); // 註解: 向玩家發送消息，通知等級不足
                         return; // 註解: 返回，停止後續執行
                     }
             EventSystemInfo EventInfo = EventSystemLoader.getInstance().getEventSystemInfo2(pc.getConsole_type());
             if (EventInfo != null &&
               EventInfo.get_npc_id() == pc.getConsole_type()) {
               L1Map m = L1WorldMap.getInstance().getMap((short)EventInfo.get_event_map_id());
               int x = EventInfo.get_teleport_x();
               int y = EventInfo.get_teleport_y();
               int cx = 0;
               int cy = 0;
               int current_try = 0;
               int limit_try = 100;
               do {
                 cx = x + (MJRnd.isBoolean() ? MJRnd.next(5) : -MJRnd.next(5));
                 cy = y + (MJRnd.isBoolean() ? MJRnd.next(5) : -MJRnd.next(5));
               } while (++current_try < limit_try && !MJPoint.isValidPosition(m, cx, cy));
               if (current_try >= limit_try) {
                 cx = x;
                 cy = y;
               }
               pc.start_teleport(cx, cy, (short)EventInfo.get_event_map_id(), pc.getHeading(), 169, true, false);
             }
           }
           break;


             case 48: // 註解: 處理操作碼為48的情況
                 j = readC(); // 註解: 讀取一個字節
                 if (j == 1) { // 註解: 如果讀取的字節為1
                     SC_ARENACO_ENTER_INDUN_ROOM_ACK pck = SC_ARENACO_ENTER_INDUN_ROOM_ACK.newInstance(); // 註解: 創建新的 SC_ARENACO_ENTER_INDUN_ROOM_ACK 實例
                     pck.set_room_id(pc.get_indun_room_num()); // 註解: 設置房間ID為玩家的副本房間號
                     pck.set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult.SUCCESS); // 註解: 設置結果為成功
                     pc.sendPackets((MJIProtoMessage)pck, MJEProtoMessages.SC_ARENACO_ENTER_INDUN_ROOM_ACK); // 註解: 向玩家發送 SC_ARENACO_ENTER_INDUN_ROOM_ACK 消息
                     break; // 註解: 跳出 switch 語句
                 }
                 if (j == 0) { // 註解: 如果讀取的字節為0
                     pc.set_indun_room_num(-1); // 註解: 將玩家的副本房間號設置為-1
                     pc.sendPackets("已拒絕邀請。"); // 註解: 向玩家發送消息，通知已拒絕邀請
                 }
                 break; // 註解: 跳出 switch 語句

             case 24: // 註解: 處理操作碼為24的情況
                 j = readC(); // 註解: 讀取一個字節
                 if (j == 1) { // 註解: 如果讀取的字節為1
                     L1Question.good++; // 註解: 增加好評數量
                     pc.sendPackets("感謝您的參與。"); // 註解: 向玩家發送消息，感謝參與調查
                     break; // 註解: 跳出 switch 語句
                 }
                 if (j == 0) { // 註解: 如果讀取的字節為0
                     L1Question.bad++; // 註解: 增加差評數量
                     pc.sendPackets("感謝您的參與。"); // 註解: 向玩家發送消息，感謝參與調查
                 }
                 break; // 註解: 跳出 switch 語句

             case 12: // 註解: 處理操作碼為12的情況
                 j = readC(); // 註解: 讀取一個字節
                 if (j == 0) { // 註解: 如果讀取的字節為0
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("您未參加Boss突襲移動。")); // 註解: 向玩家發送消息，通知未參加Boss突襲移動
                     break; // 註解: 跳出 switch 語句
                 }
           if (j == 1) {
             Iterator<L1NpcInstance> iter = EventTimeTable.getInstance().get_npc_iter();
             L1NpcInstance l1NpcInstance = null;

             while (iter.hasNext()) {
               l1NpcInstance = iter.next();
               if (l1NpcInstance == null) {
                 continue;
               }

               if (!l1NpcInstance.is_boss_alarm()) {
                 continue;
               }
               if (l1NpcInstance.getNpcId() != pc.getBossNpc()) {
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
               if (pc.getMapId() == 2237 || (pc.getMapId() >= 1708 && pc.getMapId() <= 1712) || (pc.getMapId() >= 12852 && pc.getMapId() <= 12862) || (pc
                 .getMapId() >= 15871 && pc.getMapId() <= 15899)) {
                 pc.sendPackets(1413);
                 pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false));

                 return;
               }
                 if (MJShiftBattlePlayManager.is_shift_battle(pc)) { // 註解: 如果玩家正在參加Shift戰鬥
                     return; // 註解: 返回，停止後續執行
                 }
                 if (pc.getInventory().checkItem(40308, l1NpcInstance.get_boss_tel_count())) { // 註解: 如果玩家的背包中有足夠數量的ID為40308的物品
                     pc.getInventory().consumeItem(40308, l1NpcInstance.get_boss_tel_count()); // 註解: 消耗指定數量的物品
                 } else { // 註解: 如果玩家背包中沒有足夠數量的物品
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("不足的金幣 " + l1NpcInstance.get_boss_tel_count() + " 金幣。")); // 註解: 向玩家發送消息，通知缺少物品
                     return; // 註解: 返回，停止後續執行
                 }

                 int x = 0, y = 0, map = 0; // 註解: 初始化傳送座標和地圖ID
                 map = l1NpcInstance.getMapId(); // 註解: 獲取NPC所在地圖ID
                 x = l1NpcInstance.getHomeX(); // 註解: 獲取NPC的家園X座標
                 y = l1NpcInstance.getHomeY(); // 註解: 獲取NPC的家園Y座標

                 if (x == 0 && y == 0 && map == 0) { // 註解: 如果座標和地圖ID都為0
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("傳送時間已過。")); // 註解: 向玩家發送消息，通知傳送時間已過
                     pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false)); // 註解: 向玩家發送消息，取消癱瘓效果
                     return; // 註解: 返回，停止後續執行
                 }

                 MJPoint pt = MJPoint.newInstance(x, y, l1NpcInstance.getHomeRnd(), (short)map, 50); // 註解: 創建新的傳送目標點
                 pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true); // 註解: 開始傳送到目標點
             }
               return; // 註解: 返回，停止後續執行
           }
             break; // 註解: 跳出 switch 語句


         case 3:
           j = readC();
           if (j == 1) {
             L1ItemInstance item = pc.getKillDeathInitializeItem();
             if (item != null) {
               pc.getKDA().onInit(pc);
               pc.getInventory().removeItem(item, 1);
             }
           }
           pc.setKillDeathInitializeItem(null);
           return;
         case 6:
           j = readC();
           if (j == 1) {
             L1ItemInstance item = pc.getNameInstance();
             if (item != null) {
               int[] MALE_LIST = { 0, 20553, 138, 20278, 2786, 6658, 6671, 20567, 18520, 19296 };
               int[] FEMALE_LIST = { 1, 48, 37, 20279, 2796, 6661, 6650, 20577, 18499, 19299 };
               if (pc.get_sex() == 0) {
                 pc.set_sex(1);
                 pc.setClassId(FEMALE_LIST[pc.getType()]);
               } else {
                 pc.set_sex(0);
                 pc.setClassId(MALE_LIST[pc.getType()]);
               }
               pc.setCurrentSprite(pc.getClassId());
               pc.sendShape(pc.getClassId());
               pc.getInventory().removeItem(item, 1);
               pc.sendPackets((ServerBasePacket)new S_Paralysis(4, true));
             }

             GeneralThreadPool.getInstance().schedule(new Runnable()
                 {
                   public void run() {
                     GameClient clnt = pc.getNetConnection();
                     C_NewCharSelect.restartProcess(pc);
                     Account acc = clnt.getAccount();
                     clnt.sendPacket((ServerBasePacket)new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
                     if (acc.countCharacters() > 0)
                       C_CommonClick.sendCharPacks(clnt);
                   }
                 }500L);
           }
           pc.setNameInstance(null);
           return;

         case 2:
           j = readC();
           if (j == 0) {
             MJLFCCreator.setInstStatus(pc, MJInstanceEnums.InstStatus.INST_USERSTATUS_NONE);
           } else if (j == 1 &&
             pc.getInstStatus() == MJInstanceEnums.InstStatus.INST_USERSTATUS_LFCREADY) {
             MJLFCCreator.setInstStatus(pc, MJInstanceEnums.InstStatus.INST_USERSTATUS_LFCINREADY);
           }
           return;

             case 1: // 註解: 處理操作碼為1的情況
                 j = readC(); // 註解: 讀取一個字節

                 if (j == 0) { // 註解: 如果讀取的字節為0
                     if (pc.getLevel() < Config.ServerAdSetting.YNpclevel) { // 註解: 如果玩家等級小於要求的等級
                         pc.sendPackets((ServerBasePacket)new S_SystemMessage("等級 " + Config.ServerAdSetting.YNpclevel + " 以上才可以使用。")); // 註解: 向玩家發送消息，通知等級不足
                         return; // 註解: 返回，停止後續執行
                     }
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("您拒絕參加Boss移動。")); // 註解: 向玩家發送消息，通知拒絕參加Boss移動
                     pc.setBossYN(0); // 註解: 將玩家的BossYN設置為0
                     break; // 註解: 跳出 switch 語句
                 }

                 if (j == 1) { // 註解: 如果讀取的字節為1
                     if (pc.getLevel() < Config.ServerAdSetting.YNpclevel) { // 註解: 如果玩家等級小於要求的等級
                         pc.sendPackets((ServerBasePacket)new S_SystemMessage("等級 " + Config.ServerAdSetting.YNpclevel + " 以上才可以使用。")); // 註解: 向玩家發送消息，通知等級不足
                         return; // 註解: 返回，停止後續執行
                     }
                     L1Boss boss = BossMonsterSpawnList.find(pc.getBossYN()); // 註解: 查找玩家的BossYN對應的Boss
                     if (boss == null) { // 註解: 如果找不到Boss
                         pc.sendPackets("時間已過，傳送失敗。(10秒)"); // 註解: 向玩家發送消息，通知傳送失敗
                         break; // 註解: 跳出 switch 語句
                     }
                     L1Map m = L1WorldMap.getInstance().getMap((short)boss.getMap()); // 註解: 獲取Boss所在的地圖
                     int x = boss.getX(); // 註解: 獲取Boss的X座標
                     int y = boss.getY(); // 註解: 獲取Boss的Y座標
                     int cx = 0; // 註解: 初始化傳送的X座標
                     int cy = 0; // 註解: 初始化傳送的Y座標
                     int current_try = 0; // 註解: 初始化當前嘗試次數
                     int limit_try = 100; // 註解: 設置嘗試次數上限

                     do {
                         cx = x + (MJRnd.isBoolean() ? MJRnd.next(5) : -MJRnd.next(5)); // 註解: 隨機生成傳送的X座標
                         cy = y + (MJRnd.isBoolean() ? MJRnd.next(5) : -MJRnd.next(5)); // 註解: 隨機生成傳送的Y座標
                     } while (++current_try < limit_try && !MJPoint.isValidPosition(m, cx, cy)); // 註解: 如果位置無效且未超過嘗試次數，繼續嘗試

                     if (current_try >= limit_try) { // 註解: 如果超過嘗試次數上限
                         cx = x; // 註解: 將傳送的X座標設置為Boss的X座標
                         cy = y; // 註解: 將傳送的Y座標設置為Boss的Y座標
                     }

                     pc.start_teleport(cx, cy, (short)boss.getMap(), pc.getHeading(), 18339, true, false); // 註解: 開始傳送到Boss所在位置

                     pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "已傳送到該Boss區域。")); // 註解: 向玩家發送消息，通知已傳送到Boss區域
                 }
                 break; // 註解: 跳出 switch 語句


             case 70000: // 註解: 處理操作碼為70000的情況
                 if (readC() == 1) { // 註解: 如果讀取的字節為1
                     pc.sendPackets((ServerBasePacket)new S_Message_YN(70001, 6008, "是否確定要進行初始化？")); // 註解: 向玩家發送確認消息，詢問是否確定進行初始化
                 } else if (readC() == 0) { // 註解: 如果讀取的字節為0
                     pc.sendPackets("已取消DB初始化。"); // 註解: 向玩家發送消息，通知已取消DB初始化
                 }
                 return; // 註解: 返回，停止後續執行

             case 70001: // 註解: 處理操作碼為70001的情況
                 if (readC() == 1) { // 註解: 如果讀取的字節為1
                     GMCommands.clear_DB(pc); // 註解: 執行清除DB的GM命令
                     pc.sendPackets("正在進行DB初始化。"); // 註解: 向玩家發送消息，通知正在進行DB初始化
                 } else if (readC() == 0) { // 註解: 如果讀取的字節為0
                     pc.sendPackets("已取消DB初始化。"); // 註解: 向玩家發送消息，通知已取消DB初始化
                 }
                 return; // 註解: 返回，停止後續執行
         }

       if (MJSurveySystemLoader.getInstance().isSurvey(msgIdx)) {
         MJSurveySystemLoader.getInstance().submitSurvey(pc, msgIdx, (readC() == 1));


         return;
       }
     }

     switch (attrcode) {
       case 5460:
         c = readC();
         if (c == 1) {
           if (pc.getClan() != null && pc.getClan().AllianceSize() > 0) {
             Integer[] arrayOfInteger; int j; byte b; for (arrayOfInteger = pc.getClan().Alliance(), j = arrayOfInteger.length, b = 0; b < j; ) { int clanid = arrayOfInteger[b].intValue();
               if (clanid != 0) {

                 L1Clan clan = L1World.getInstance().getClan(clanid);
                 if (clan != null) {

                   clan.AllianceDelete();
                   clan.removeAlliance(pc.getClanid());
                   ClanTable.getInstance().updateClan(clan);
                   for (L1PcInstance tempPc : clan.getOnlineClanMember()) {
                     tempPc.sendPackets((ServerBasePacket)new S_ServerMessage(1204, pc.getClan().getClanName()));
                     SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, clan.getClanId(), false, true);
                   }
                   for (L1PcInstance tempPc : pc.getClan().getOnlineClanMember())
                     SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, pc.getClanid(), false, true);
                 }
               }  b++; }

           }  pc.getClan().AllianceDelete();
           pc.getClan().setAlliance_leader(false);
           ClanTable.getInstance().updateClan(pc.getClan());
         }
         break;
         case 223: // 註解: 處理操作碼為223的情況
             c = readC(); // 註解: 讀取一個字節
             allianceLeader = (L1PcInstance)L1World.getInstance().findObject(pc.getTempID()); // 註解: 根據臨時ID查找聯盟領袖玩家對象
             if (allianceLeader == null) { // 註解: 如果找不到聯盟領袖
                 return; // 註解: 返回，停止後續執行
             }
             if (pc.getLevel() <= Config.ServerAdSetting.CROWNBLOOD_ALLIANCE_LEVEL) { // 註解: 如果玩家等級小於等於聯盟加入的最低要求等級
                 pc.sendPackets("等級 " + Config.ServerAdSetting.CROWNBLOOD_ALLIANCE_LEVEL + " 以上才可以加入聯盟。"); // 註解: 發送消息通知玩家等級不足以加入聯盟
                 return; // 註解: 返回，停止後續執行
             }
         pc.setTempID(0);
         PcClanId = pc.getClanid();
         TargetClanId = allianceLeader.getClanid();
         PcClanName = pc.getClanname();
         TargetClanName = allianceLeader.getClanname();
         PcClan = L1World.getInstance().getClan(PcClanId);
         TargetClan = L1World.getInstance().getClan(TargetClanId);
         if (c == 1) {
           PcClan.setAlliance_leader(true);
           PcClan.addAlliance(TargetClanId);
           TargetClan.addAlliance(PcClanId);
           if (PcClan.AllianceSize() > 1) {
             Integer[] arrayOfInteger; int j; byte b; for (arrayOfInteger = PcClan.Alliance(), j = arrayOfInteger.length, b = 0; b < j; ) { int clanid = arrayOfInteger[b].intValue();
               L1Clan AllianceClan = L1World.getInstance().getClan(clanid);
               AllianceClan.setAllianceList(PcClan.getAllianceList().toString());
               for (L1PcInstance tempPc : AllianceClan.getOnlineClanMember()) {
                 SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, TargetClanId, true, false);
               }
               ClanTable.getInstance().updateClan(AllianceClan); b++; }

           }
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(1200, TargetClanName));
           allianceLeader.sendPackets((ServerBasePacket)new S_ServerMessage(224, PcClanName, TargetClanName)); break;
         }  if (c == 0) {
           allianceLeader.sendPackets(1198);
         }
         break;
       case 1210:
         if (readC() == 1) {
           L1Clan leave_clan = L1World.getInstance().getClan(pc.getClanid());
           if (leave_clan.isAlliance_leader()) {
             return;
           }
           if (pc.getClan() != null && pc.getClan().AllianceSize() > 0) {
             Integer[] arrayOfInteger; int j; byte b; for (arrayOfInteger = pc.getClan().Alliance(), j = arrayOfInteger.length, b = 0; b < j; ) { int clanid = arrayOfInteger[b].intValue();
               if (clanid != 0) {

                 L1Clan clan = L1World.getInstance().getClan(clanid);
                 if (clan != null)

                 {
                   clan.removeAlliance(pc.getClanid());
                   if (clan.AllianceSize() == 1) {
                     clan.AllianceDelete();
                     clan.setAlliance_leader(false);
                     for (L1PcInstance tempPc : clan.getOnlineClanMember()) {
                       tempPc.sendPackets((ServerBasePacket)new S_ServerMessage(1204, leave_clan.getClanName()));
                       SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, clan.getClanId(), false, true);
                     }
                   } else {
                     for (L1PcInstance tempPc : clan.getOnlineClanMember()) {
                       tempPc.sendPackets((ServerBasePacket)new S_ServerMessage(1204, pc.getClan().getClanName()));
                       SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, clan.getClanId(), false, true);
                     }
                     for (L1PcInstance tempPc : pc.getClan().getOnlineClanMember()) {
                       SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, pc.getClanid(), false, true);
                     }
                   }
                   ClanTable.getInstance().updateClan(clan); }
               }  b++; }

           }  pc.sendPackets((ServerBasePacket)new S_ServerMessage(1204, pc.getClan().getClanName()));
           pc.getClan().AllianceDelete();
           leave_clan.setAlliance_leader(false);
           ClanTable.getInstance().updateClan(pc.getClan());
         }
         break;
         case 178: // 註解: 處理操作碼為178的情況
             System.out.println("使用技能"); // 註解: 輸出 "使用技能"
             break; // 註解: 跳出 switch 語句
         case 180: // 註解: 處理操作碼為180的情況
             readC(); // 註解: 讀取一個字節
             name = readS(); // 註解: 讀取一個字符串
             maple = false; // 註解: 初始化 maple 為假
             ring = false; // 註解: 初始化 ring 為假
             ring2 = false; // 註解: 初始化 ring2 為假
             if (name.startsWith("maple")) { // 註解: 如果名稱以 "maple" 開頭
                 String aa = name; // 註解: 將名稱賦值給變量 aa
                 String bb = aa.replace("maple ", ""); // 註解: 將 aa 中的 "maple " 替換為空字符串，並賦值給變量 bb
                 name = bb; // 註解: 將 bb 賦值回名稱
                 maple = true; // 註解: 設置 maple 為真
             }

           if (name.equalsIgnoreCase("ranking class polymorph")) { // 註解: 如果名稱忽略大小寫等於 "ranking class polymorph"
               if (!MJRankUserLoader.getInstance().isRankPoly(pc)) { // 註解: 如果玩家不符合變身排名要求
                   return; // 註解: 返回，停止後續執行
               }
               if (pc.getRankLevel() < 3) // 註解: 如果玩家的排名等級小於3
                   return; // 註解: 返回，停止後續執行
               switch (pc.getType()) { // 註解: 根據玩家的類型進行處理
                   case 0: // 註解: 玩家類型為0 (王子)
                       if (pc.get_sex() == 0) { // 註解: 如果玩家性別為男性
                           name = "ranking prince male"; // 註解: 設置名稱為"ranking prince male"
                           break; // 註解: 跳出 switch 語句
                       }
                       name = "ranking prince female"; // 註解: 設置名稱為"ranking prince female"
                       break; // 註解: 跳出 switch 語句
                   case 1: // 註解: 玩家類型為1 (騎士)
                       if (pc.get_sex() == 0) { // 註解: 如果玩家性別為男性
                           name = "ranking knight male"; // 註解: 設置名稱為"ranking knight male"
                           break; // 註解: 跳出 switch 語句
                       }
                       name = "ranking knight female"; // 註解: 設置名稱為"ranking knight female"
                       break; // 註解: 跳出 switch 語句
                   case 2: // 註解: 玩家類型為2 (妖精)
                       if (pc.get_sex() == 0) { // 註解: 如果玩家性別為男性
                           name = "ranking elf male"; // 註解: 設置名稱為"ranking elf male"
                           break; // 註解: 跳出 switch 語句
                       }
                       name = "ranking elf female"; // 註解: 設置名稱為"ranking elf female"
                       break; // 註解: 跳出 switch 語句
                   case 3: // 註解: 玩家類型為3 (巫師)
                       if (pc.get_sex() == 0) { // 註解: 如果玩家性別為男性
                           name = "ranking wizard male"; // 註解: 設置名稱為"ranking wizard male"
                           break; // 註解: 跳出 switch 語句
                       }
                       name = "ranking wizard female"; // 註解: 設置名稱為"ranking wizard female"
                       break; // 註解: 跳出 switch 語句
                   case 4: // 註解: 玩家類型為4 (黑暗妖精)
                       if (pc.get_sex() == 0) { // 註解: 如果玩家性別為男性
                           name = "ranking darkelf male"; // 註解: 設置名稱為"ranking darkelf male"
                           break; // 註解: 跳出 switch 語句
                       }
                       name = "ranking darkelf female"; // 註解: 設置名稱為"ranking darkelf female"
                       break; // 註解: 跳出 switch 語句
                   case 5: // 註解: 玩家類型為5 (龍騎士)
                       if (pc.get_sex() == 0) { // 註解: 如果玩家性別為男性
                           name = "ranking dragonknight male"; // 註解: 設置名稱為"ranking dragonknight male"
                           break; // 註解: 跳出 switch 語句
                       }
                       name = "ranking dragonknight female"; // 註解: 設置名稱為"ranking dragonknight female"
                       break; // 註解: 跳出 switch 語句
                   case 6: // 註解: 玩家類型為6 (幻術師)
                       if (pc.get_sex() == 0) { // 註解: 如果玩家性別為男性
                           name = "ranking illusionist male"; // 註解: 設置名稱為"ranking illusionist male"
                           break; // 註解: 跳出 switch 語句
                       }
                       name = "ranking illusionist female"; // 註解: 設置名稱為"ranking illusionist female"
                       break; // 註解: 跳出 switch 語句
                   case 7: // 註解: 玩家類型為7 (戰士)
                       if (pc.get_sex() == 0) { // 註解: 如果玩家性別為男性
                           name = "ranking warrior male"; // 註解: 設置名稱為"ranking warrior male"
                           break; // 註解: 跳出 switch 語句
                       }
                       name = "ranking warrior female"; // 註解: 設置名稱為"ranking warrior female"
                       break; // 註解: 跳出 switch 語句
                   case 8: // 註解: 玩家類型為8 (劍士)
                       if (pc.get_sex() == 0) { // 註解: 如果玩家性別為男性
                           name = "ranking fencer male"; // 註解: 設置名稱為"ranking fencer male"
                           break; // 註解: 跳出 switch 語句
                       }
                       name = "ranking fencer female"; // 註解: 設置名稱為"ranking fencer female"
                       break; // 註解: 跳出 switch 語句
               }

           }
         if (name != null && name.length() > 0) {
           L1PolyMorph poly = PolyTable.getInstance().getTemplate(name);
           if (poly != null || name.equals("")) {
             if (name.equals("")) {
               int spriteId = pc.getCurrentSpriteId();
               if (spriteId != 6034 && spriteId != 6035) {

                 if (pc.hasSkillEffect(80012)) {
                   pc.removeSkillEffect(80012);
                 } else if (pc.hasSkillEffect(80013)) {
                   pc.removeSkillEffect(80013);
                 }
                 pc.removeSkillEffect(67);
               }
             } else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
               if (pc.isPolyRingMaster2() && maple) {
                 ring = false;
                 ring2 = true;
               } else if (pc.isPolyRingMaster() && maple) {
                 ring = true;
                 ring2 = false;
               }
               if (ring && ring2) {
                 ring = false;
                 ring2 = true;
               }
               L1PolyMorph.doPoly((L1Character)pc, poly.getPolyId(), (pc.getId() == msgIdx) ? 7200 : 3600, 1, ring, ring2);
             } else if (Config.ServerAdSetting.PolyEvent2) {
               int minlevel = 0;
               switch (poly.getPolyId()) {
                 case 15986:
                 case 16002:
                 case 16008:
                 case 16014:
                 case 16027:
                 case 16040:
                 case 16053:
                 case 16056:
                 case 16074:
                 case 16284:
                 case 17515:
                 case 17531:
                 case 17535:
                 case 17541:
                 case 17545:
                 case 17549:
                   minlevel = 80;
                   break;
                 case 11389:
                 case 12681:
                 case 13152:
                 case 13153:
                 case 13631:
                 case 13635:
                 case 15528:
                 case 15531:
                 case 15534:
                 case 15537:
                 case 15539:
                 case 15545:
                 case 15548:
                 case 15550:
                 case 15814:
                 case 15830:
                 case 15831:
                 case 15832:
                 case 15833:
                 case 15866:
                 case 15868:
                   minlevel = 65;
                   break;
                 case 11385:
                 case 12240:
                 case 12702:
                 case 13346:
                 case 15599:
                 case 15847:
                 case 15848:
                 case 15849:
                 case 15850:
                 case 15865:
                   minlevel = 60;
                   break;
                 default:
                   minlevel = poly.getMinLevel();
                   break;
               }
                 if (minlevel <= pc.getLevel()) { // 註解: 如果玩家等級大於等於最小等級
                     if (pc.isPolyRingMaster2() && maple) { // 註解: 如果玩家是PolyRingMaster2且maple為真
                         ring = false; // 註解: 設置ring為假
                         ring2 = true; // 註解: 設置ring2為真
                     } else if (pc.isPolyRingMaster() && maple) { // 註解: 如果玩家是PolyRingMaster且maple為真
                         ring = true; // 註解: 設置ring為真
                         ring2 = false; // 註解: 設置ring2為假
                     }
                     if (ring && ring2) { // 註解: 如果ring和ring2都為真
                         ring = false; // 註解: 設置ring為假
                         ring2 = true; // 註解: 設置ring2為真
                     }
                     L1PolyMorph.doPoly((L1Character)pc, poly.getPolyId(), (pc.getId() == msgIdx) ? 7200 : 3600, 1, ring, ring2); // 註解: 執行變身操作，變身時間根據id是否等於msgIdx決定
                     return; // 註解: 返回，停止後續執行
                 }
                 pc.sendPackets("現在無法進行這種變身。"); // 註解: 發送消息通知玩家無法進行變身
             } else {
                 pc.sendPackets("現在無法進行這種變身。"); // 註解: 發送消息通知玩家無法進行變身
             }
           }

         if (msgIdx == pc.getId() &&
           pc.polyTrigger() != null) {
           pc.polyTrigger().onWork();
         }
         break;

       case 97:
         c = readH();
         joinPc = (L1PcInstance)L1World.getInstance().findObject(pc.getTempID());
         pc.setTempID(0);
         if (joinPc != null) {
           if (c == 0) {
             joinPc.sendPackets((ServerBasePacket)new S_ServerMessage(96, pc.getName())); break;
           }  if (c == 1) {
             L1ClanJoin.getInstance().ClanJoin(pc.getClan(), joinPc);
           }
         }
         break;
       case 3348:
         c = readC();
         if (c != 0 &&
           c == 1) {
           L1PcInstance GazePc = (L1PcInstance)L1World.getInstance().findObject(pc.getTempID());
           pc.setTempID(0);
           if (GazePc == null)
             return;
           L1Clan targetClan = L1World.getInstance().findClan(GazePc.getClanname());
           if (targetClan == null) {
             return;
           }

           L1Clan pcClan = L1World.getInstance().findClan(pc.getClanname());
           if (pcClan == null) {
             return;
           }

           targetClan.addGazelist(pcClan.getClanName());
           pcClan.addGazelist(targetClan.getClanName());

           for (L1PcInstance member : pcClan.getOnlineClanMember()) {
             member.sendPackets((ServerBasePacket)new S_ClanAttention(true, targetClan.getClanName()));
             member.sendPackets((ServerBasePacket)new S_ClanAttention(pcClan.getGazeSize(), pcClan.getGazeList()));
           }

           for (L1PcInstance member : targetClan.getOnlineClanMember()) {
             member.sendPackets((ServerBasePacket)new S_ClanAttention(true, pcClan.getClanName()));
             member.sendPackets((ServerBasePacket)new S_ClanAttention(targetClan.getGazeSize(), targetClan.getGazeList()));
           }
         }
         break;
       case 217:
       case 221:
       case 222:
         c = readC();
         enemyLeader = (L1PcInstance)L1World.getInstance().findObject(pc.getTempID());
         if (enemyLeader == null) {
           return;
         }
         pc.setTempID(0);
         clanName = pc.getClanname();
         enemyClanName = enemyLeader.getClanname();
         if (c == 0) {
           if (i == 217) {
             enemyLeader.sendPackets((ServerBasePacket)new S_ServerMessage(236, clanName)); break;
           }  if (i == 221 || i == 222)
             enemyLeader.sendPackets((ServerBasePacket)new S_ServerMessage(237, clanName));  break;
         }
         if (c == 1) {
           L1Clan defense = L1World.getInstance().findClan(clanName);
           L1Clan offense = L1World.getInstance().findClan(enemyClanName);
           if (defense == null || offense == null) {
             return;
           }
           if (i == 217) {
             MJWar mJWar = MJWarFactory.createNormalWar(defense);
             mJWar.register(offense); break;
           }
           MJWar war = offense.getCurrentWar();
           if (war == null) {
             return;
           }
           war.notifySurrender(defense, offense);
           war.dispose();
         }
         break;

       case 252:
         c = readC();
         trading_partner = (L1PcInstance)L1World.getInstance().findObject(pc.getTradeID());
         npc = NpcTable.getInstance().getTemplate(400064);
         npc3 = NpcTable.getInstance().getTemplate(300027);
         if (trading_partner != null) {
           if (c == 0) {
             trading_partner.sendPackets((ServerBasePacket)new S_ServerMessage(253, pc.getName()));

             pc.setTradeID(0);
             trading_partner.setTradeID(0); break;
           }  if (c == 1) {
             pc.sendPackets((ServerBasePacket)new S_Trade(trading_partner.getName()));
             trading_partner.sendPackets((ServerBasePacket)new S_Trade(pc.getName()));
           }
           break;
         }
         if (c == 0) {
           pc.setTradeID(0); break;
         }  if (c == 1) {
           if (pc.getX() == 33507 && pc.getY() == 32851 && pc.getMapId() == 4) {
             pc.sendPackets((ServerBasePacket)new S_Trade(npc.get_name())); break;
           }  if (pc.getX() == 33515 && pc.getY() == 32851 && pc.getMapId() == 4) {
             pc.sendPackets((ServerBasePacket)new S_Trade(npc3.get_name()));
           }
         }
         break;

       case 321:
         c = readC();
         resusepc1 = (L1PcInstance)L1World.getInstance().findObject(pc.getTempID());
         pc.setTempID(0);
         if (resusepc1 == null ||
           c == 0)
           break;
         if (c == 1) {
           pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 230));
           pc.broadcastPacket((ServerBasePacket)new S_SkillSound(pc.getId(), 230));


           pc.resurrect(pc.getMaxHp() / 2);
           pc.setCurrentHp(pc.getMaxHp() / 2);



           pc.sendPackets((ServerBasePacket)new S_Resurrection(pc, resusepc1, 0));
           pc.broadcastPacket((ServerBasePacket)new S_Resurrection(pc, resusepc1, 0));
           pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));
           pc.broadcastPacket((ServerBasePacket)new S_CharVisualUpdate(pc));
         }
         break;




       case 322:
         c = readC();
         resusepc2 = (L1PcInstance)L1World.getInstance().findObject(pc.getTempID());
         pc.setTempID(0);
         if (resusepc2 != null &&
           c != 0 &&
           c == 1) {
           pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 230));
           pc.broadcastPacket((ServerBasePacket)new S_SkillSound(pc.getId(), 230));
           pc.resurrect(pc.getMaxHp());
           pc.setCurrentHp(pc.getMaxHp());
           pc.sendPackets((ServerBasePacket)new S_Resurrection(pc, resusepc2, 0));
           pc.broadcastPacket((ServerBasePacket)new S_Resurrection(pc, resusepc2, 0));
           pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));
           pc.broadcastPacket((ServerBasePacket)new S_CharVisualUpdate(pc));


           if (pc.get_exp_res() == 1L && pc.isGres() && pc.isGresValid()) {
             pc.resExp();
             pc.set_exp_res(0L);
             pc.setGres(false);
             int index = 0;
             List<MJDeathPenaltyExpModel> deathpenalityexp = pc.get_deathpenalty_exp();
             if (deathpenalityexp != null) {
               for (int j = 0; j < deathpenalityexp.size(); j++) {
                 int indexid = ((MJDeathPenaltyExpModel)deathpenalityexp.get(i)).getId();
                 if (index < indexid) {
                   index = indexid;
                 }
               }
               pc.delete_deathpenalty_exp(index);
               MJDeathPenaltyexpDatabaseLoader.getInstance().do_Select(pc);
             }
           }
         }
         break;

         case 325: // 註解: 處理操作碼為325的情況
             c = readC(); // 註解: 讀取一個字節
             name = readS(); // 註解: 讀取一個字符串
             len = name.length(); // 註解: 獲取字符串的長度
             if (len > 6 || len < 1) { // 註解: 如果字符串長度大於6或小於1
                 pc.sendPackets("請確認寵物名字的長度。"); // 註解: 發送消息通知玩家寵物名字的長度不正確
                 pc.setTempID(0); // 註解: 將臨時ID設置為0
                 break; // 註解: 跳出 switch 語句
             }
         pet = (L1PetInstance)L1World.getInstance().findObject(pc.getTempID());
         pc.setTempID(0);
         renamePet(pet, name);
         break;

       case 512:
         c = readC();
         name = readS();
         houseId = pc.getTempID();
         pc.setTempID(0);
         if (name.length() <= 16) {
           L1House house = HouseTable.getInstance().getHouseTable(houseId);
           house.setHouseName(name);
           HouseTable.getInstance().updateHouse(house); break;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(513));
         break;

         case 6008: // 註解: 處理操作碼為6008的情況
             c = readC(); // 註解: 讀取一個字節
             if (c == 0) { // 註解: 如果讀取的字節為0
                 if (pc.isSpecialBuff()) { // 註解: 如果玩家有特殊增益狀態
                     pc.setSpecialBuff(false); // 註解: 設置玩家的特殊增益狀態為假
                     break; // 註解: 跳出 switch 語句
                 }
                 if (pc.getCustomQuestId() != 0) { // 註解: 如果玩家有自定義任務ID
                     pc.setCustomQuestId(0); // 註解: 設置玩家的自定義任務ID為0
                     pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(pc.getCustomQuestNpcObjId(), "")); // 註解: 發送NPC對話返回封包
                     pc.setCustomQuestNpcObjId(0); // 註解: 設置玩家的自定義任務NPC對象ID為0
                     break; // 註解: 跳出 switch 語句
                 }
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("輸入時間已超過。")); // 註解: 發送系統消息通知玩家輸入時間已超過
                 break; // 註解: 跳出 switch 語句
             }
             if (c == 1) { // 註解: 如果讀取的字節為1
                 if (pc.getRaidGame()) { // 註解: 如果玩家正在進行團隊遊戲
                     pc.setRaidGame(false); // 註解: 設置玩家的團隊遊戲狀態為假
                 } else if (pc.isSpecialBuff()) { // 註解: 如果玩家有特殊增益狀態
                     pc.setSpecialBuff(false); // 註解: 設置玩家的特殊增益狀態為假
                     GeneralThreadPool.getInstance().execute(new 增益任務(pc)); // 註解: 執行一個新的增益任務
                 }
             }
             if (c == 1) { // 註解: 如果讀取的字節為1
                 if (pc.getCustomQuestId() != 0) { // 註解: 如果玩家有自定義任務ID
                     CustomQuest cq = ServerCustomQuestTable.getInstance().getCustomQuest(pc.getCustomQuestId()); // 註解: 從自定義任務表中獲取對應的自定義任務
                     if (cq != null) { // 註解: 如果自定義任務存在
                         if (pc.getLevel() < cq.getMinLevel() || pc.getLevel() > cq.getMaxLevel()) { // 註解: 如果玩家等級不在任務允許的範圍內
                             pc.sendPackets("\fW[任務通知] 該任務僅限Lv." + cq.getMinLevel() + "到Lv." + cq.getMaxLevel() + "的玩家進行。"); // 註解: 發送消息通知玩家等級不符合要求
                         } else { // 註解: 否則
                             pc.addCustomQuest(cq.getQuestId(), cq.getQuestType()); // 註解: 添加玩家自定義任務
                             pc.sendPackets("\fW[任務通知] 你已接受" + cq.getQuestName() + "任務。"); // 註解: 發送消息通知玩家已接受任務
                             pc.sendPackets((ServerBasePacket)new S_Sound(3450)); // 註解: 發送音效封包
                             pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(pc.getCustomQuestNpcObjId(), "")); // 註解: 發送NPC對話返回封包
                             CharacterCustomQuestTable.save(pc); // 註解: 保存玩家的自定義任務狀態
                         }
                     }
                     pc.setCustomQuestId(0); // 註解: 重置玩家的自定義任務ID
                     pc.setCustomQuestNpcObjId(0); // 註解: 重置玩家的自定義任務NPC對象ID
                 }
                 break;
             }
             pc.sendPackets("無效的請求。"); // 註解: 發送消息通知玩家請求無效
             break;

       case 630:
         c = readC();
         fightPc = (L1PcInstance)L1World.getInstance().findObject(pc.getFightId());
         if (c == 0) {
           pc.setFightId(0);
           fightPc.setFightId(0);
           fightPc.sendPackets((ServerBasePacket)new S_ServerMessage(631, pc.getName())); break;
         }  if (c == 1) {
           fightPc.sendPackets((ServerBasePacket)new S_PacketBox(5, fightPc.getFightId(), fightPc.getId()));
           pc.sendPackets((ServerBasePacket)new S_PacketBox(5, pc.getFightId(), pc.getId()));
         }
         break;
       case 653:
         c = readC();
         if (c == 0)
           break;
         if (c == 1) {
           pc.setPartnerId(0);
           pc.save();
         }
         break;
       case 654:
         c = readC();
         partner = (L1PcInstance)L1World.getInstance().findObject(pc.getTempID());
         pc.setTempID(0);
         if (partner != null) {
           if (c == 0) {
             partner.sendPackets((ServerBasePacket)new S_ServerMessage(656, pc

                   .getName())); break;
           }  if (c == 1) {
             pc.setPartnerId(partner.getId());
             pc.save();
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(790));


             pc.sendPackets((ServerBasePacket)new S_ServerMessage(655, partner
                   .getName()));

             partner.setPartnerId(pc.getId());
             partner.save();
             partner.sendPackets((ServerBasePacket)new S_ServerMessage(790));


             partner.sendPackets((ServerBasePacket)new S_ServerMessage(655, pc

                   .getName()));
           }
         }
         break;


       case 729:
         c = readC();
         if (c == 0)
           break;
         if (c == 1) {
           callClan(pc);
         }
         break;
       case 738:
         c = readC();
         if (c == 0)
           break;
         if (c == 1 && pc.get_exp_res() == 1L) {
           int cost = 0;
           int level = pc.getLevel();
           int lawful = pc.getLawful();
           if (level < 45) {
             cost = level * level * 50;
           } else {
             cost = level * level * 150;
           }
           if (lawful >= 0) {
             cost = (int)(cost * 0.7D);
           }
           if (pc.getInventory().consumeItem(40308, cost)) {
             pc.resExpToTemple();

             break;
           }

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(189));
         }
         break;

         case 2551: // 註解: 處理操作碼為2551的情況
             c = readC(); // 註解: 讀取一個字節
             if (c != 0 && c == 1 && pc.get_exp_res() == 1L) { // 註解: 如果讀取的字節不為0且為1，並且玩家的經驗恢復狀態為1
                 if (!pc.getInventory().checkItem(3000049, 1) && !pc.getInventory().checkItem(4100694, 1)) { // 註解: 如果玩家背包中沒有3000049號物品且沒有4100694號物品
                     pc.sendPackets("救援證書不足。"); // 註解: 發送消息通知玩家救援證書不足
                     return; // 註解: 返回，停止後續執行
                 }
                 pc.getInventory().consumeItem(3000049, 1); // 註解: 消耗背包中的一個3000049號物品
                 pc.getInventory().consumeItem(4100694, 1); // 註解: 消耗背包中的一個4100694號物品
                 long needExp = ExpTable.getNeedExpNextLevel(pc.getLevel()); // 註解: 獲取玩家升至下一級所需的經驗值
                 double PobyExp = needExp * 0.05D; // 註解: 計算經驗值的5%
                 pc.add_exp((long)PobyExp); // 註解: 將計算出的經驗值增加到玩家的經驗值中
                 pc.set_exp_res(0L); // 註解: 將玩家的經驗恢復狀態設置為0
             }
         break;
       case 951:
         c = readC();
         chatPc = (L1PcInstance)L1World.getInstance().findObject(pc.getPartyID());
         if (chatPc != null) {
           if (c == 0) {
             chatPc.sendPackets((ServerBasePacket)new S_ServerMessage(423, pc.getName()));
             pc.setPartyID(0); break;
           }  if (c == 1) {
             if (chatPc.isInChatParty()) {
               if (chatPc.getChatParty().isVacancy() || chatPc.isGm()) {
                 chatPc.getChatParty().addMember(pc); break;
               }
               chatPc.sendPackets((ServerBasePacket)new S_ServerMessage(417));
               break;
             }
             L1ChatParty chatParty = new L1ChatParty();
             chatParty.addMember(chatPc);
             chatParty.addMember(pc);
           }
         }
         break;



         case 953: // 註解: 處理操作碼為953的情況
         case 954: // 註解: 處理操作碼為954的情況
             c = readC(); // 註解: 讀取一個字節
             target = (L1PcInstance)L1World.getInstance().findObject(pc.getPartyID()); // 註解: 根據玩家的隊伍ID在世界中查找目標玩家
             if (target != null) { // 註解: 如果目標玩家存在
                 if (target.getMapId() == 621) { // 註解: 如果目標玩家的地圖ID為621
                     target.sendPackets((ServerBasePacket)new S_SystemMessage("在該地圖無法使用隊伍功能。")); // 註解: 發送系統消息通知玩家在該地圖無法使用隊伍功能
                     return; // 註解: 返回，停止後續執行
                 }
             }
           if (c == 0) {
             target.sendPackets((ServerBasePacket)new S_ServerMessage(423, pc.getName()));
             pc.setPartyID(0); break;
           }  if (c == 1) {

             if (target.getMapId() == 5153 || target.getMapId() == 5001 || pc.getMapId() == 5153 || pc
               .getMapId() == 5001) {
               target.sendPackets((ServerBasePacket)new S_ServerMessage(423, pc.getName()));

               return;
             }
             if (target.isInParty()) {
               if (target.getParty().isVacancy() || target.isGm()) {
                 target.getParty().addMember(pc); break;
               }
               target.sendPackets((ServerBasePacket)new S_ServerMessage(417));
               break;
             }
             L1Party party = new L1Party();
             party.addMember(target);
             party.addMember(pc);
             target.sendPackets((ServerBasePacket)new S_ServerMessage(424, pc.getName()));
           }
         }
         break;

         case 2923: // 註解: 處理操作碼為2923的情況
             c = readC(); // 註解: 讀取一個字節
             if (c == 0) { // 註解: 如果讀取的字節為0
                 pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "龍之門戶進入已取消。")); // 註解: 發送聊天信息通知玩家龍之門戶進入已取消
             } else if (c == 1 && pc.DragonPortalLoc[0] != 0) { // 註解: 如果讀取的字節為1且龍之門戶位置不為0
                 Collection<L1PcInstance> templist = L1World.getInstance().getAllPlayers(); // 註解: 獲取所有玩家的集合
                 L1PcInstance[] list = templist.<L1PcInstance>toArray(new L1PcInstance[templist.size()]); // 註解: 將玩家集合轉換為數組
                 int count = 0; // 註解: 初始化計數器
                 for (L1PcInstance player : list) { // 註解: 遍歷玩家數組
                     if (player != null) { // 註解: 如果玩家不為空
                         if (player.getMapId() == pc.DragonPortalLoc[2]) // 註解: 如果玩家的地圖ID與龍之門戶位置的地圖ID相同
                             count++; // 註解: 計數器加1
                     }
                 }
             }
           if (count >= 32) {
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(1536));
             return;
           }
           pc.start_teleport(pc.DragonPortalLoc[0], pc.DragonPortalLoc[1], pc.DragonPortalLoc[2], 5, 18339, true, false);
         }


         pc.DragonPortalLoc[0] = 0;
         pc.DragonPortalLoc[1] = 0;
         pc.DragonPortalLoc[2] = 0;
         break;

         case 479: // 註解: 處理操作碼為479的情況
             if (readC() == 1) { // 註解: 如果讀取的字節為1
                 String s = readS(); // 註解: 讀取字符串
                 try {
                     pc.onStat(s); // 註解: 對玩家角色執行 onStat 方法
                 } catch (Exception e) { // 註解: 捕捉異常
                     System.out.println(s); // 註解: 在控制台輸出字符串
                     e.printStackTrace(); // 註解: 打印異常堆棧跟蹤信息
                     System.out.println(String.format("%s 嘗試進行屬性超過限制。", new Object[] { pc.getName() })); // 註解: 在控制台輸出玩家名稱和嘗試屬性超過限制的信息
                 }
             }
             break; // 註解: 跳出 switch 語句
     }
   }


   private class 통합버프
     implements Runnable
   {
     private L1PcInstance pc;

     private int[] allBuffSkill;


     public 통합버프(L1PcInstance pc) {
       this.allBuffSkill = new int[] { 26, 42, 48, 168, 22000, 7678, 4914, 50007 };
       this.pc = pc;
     }

       public void run() {
           for (int i = 0; i < this.allBuffSkill.length; i++) { // 註解: 遍歷所有的增益技能
               (new L1SkillUse()).handleCommands(this.pc, this.allBuffSkill[i], this.pc.getId(), this.pc.getX(), this.pc.getY(), null, 0, 4); // 註解: 對玩家角色使用每一個增益技能
           }

           this.pc.sendPackets((ServerBasePacket)new S_SkillSound(this.pc.getId(), 4856)); // 註解: 發送技能音效封包，音效ID為4856
           this.pc.sendPackets((ServerBasePacket)new S_ChatPacket(this.pc, "你已經接受了充滿希望和愛的增益效果。", 1)); // 註解: 發送聊天信息通知玩家他已接受增益效果
       }
   }

   private static void renamePet(L1PetInstance pet, String name) {
     if (pet == null || name == null) {
       throw new NullPointerException();
     }

     int petItemObjId = pet.getItemObjId();
     L1Pet petTemplate = PetTable.getInstance().getTemplate(petItemObjId);
     if (petTemplate == null) {
       throw new NullPointerException();
     }

     L1PcInstance pc = pet.getMaster();
     if (PetTable.isNameExists(name)) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(327));
       return;
     }
     L1Npc l1npc = NpcTable.getInstance().getTemplate(pet.getNpcId());
     if (!pet.getName().equalsIgnoreCase(l1npc.get_name())) {
       L1ItemInstance removedItem = pc.getInventory().findItemId(410016);
       if (removedItem == null) {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(326));
         return;
       }
       pc.getInventory().removeItem(removedItem, 1);
     }


     pet.setName(name);
     petTemplate.set_name(name);
     PetTable.getInstance().storePet(petTemplate);
     L1ItemInstance item = pc.getInventory().getItem(pet.getItemObjId());
     pc.getInventory().updateItem(item);


     pc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)pet));
     pc.broadcastPacket((ServerBasePacket)new S_RemoveObject((L1Object)pet));

     SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance((L1NpcInstance)pet);
     pc.broadcastPacket((MJIProtoMessage)noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, true);
   }

   private void callClan(L1PcInstance pc) {
     L1PcInstance callClanPc = (L1PcInstance)L1World.getInstance().findObject(pc.getTempID());
     pc.setTempID(0);
     if (callClanPc == null) {
       return;
     }
     if (!pc.getMap().isEscapable() && !pc.isGm()) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(647));
       pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, false);
       return;
     }
     if (pc.getId() != callClanPc.getCallClanId()) {
       return;
     }

     boolean isInWarArea = false;
     int castleId = L1CastleLocation.getCastleIdByArea((L1Character)callClanPc);
     if (castleId != 0) {
       isInWarArea = true;
       if (MJCastleWarBusiness.getInstance().isNowWar(castleId)) {
         isInWarArea = false;
       }
     }
       short mapId = callClanPc.getMapId(); // 註解: 獲得要召喚的血盟成員所在的地圖ID
       if ((mapId != 0 && mapId != 4 && mapId != 304) || isInWarArea) { // 註解: 如果地圖ID不是0、4或304，或者所在地區是戰區

           pc.sendPackets("由於領主目前處於無法召喚的地點，魔法失敗了。"); // 註解: 發送消息給執行召喚的玩家，告知魔法失敗
           callClanPc.sendPackets("在盟主的位置不能召喚血盟成員。"); // 註解: 發送消息給被召喚的血盟成員，告知無法進行召喚

           return; // 註解: 返回，停止後續執行
       }
     L1Map map = callClanPc.getMap();
     int callCalnX = callClanPc.getX();
     int callCalnY = callClanPc.getY();
     int locX = 0;
     int locY = 0;
     int heading = 0;
     switch (callClanPc.getCallClanHeading()) {
       case 0:
         locX = callCalnX;
         locY = callCalnY - 1;
         heading = 4;
         break;
       case 1:
         locX = callCalnX + 1;
         locY = callCalnY - 1;
         heading = 5;
         break;
       case 2:
         locX = callCalnX + 1;
         locY = callCalnY;
         heading = 6;
         break;
       case 3:
         locX = callCalnX + 1;
         locY = callCalnY + 1;
         heading = 7;
         break;
       case 4:
         locX = callCalnX;
         locY = callCalnY + 1;
         heading = 0;
         break;
       case 5:
         locX = callCalnX - 1;
         locY = callCalnY + 1;
         heading = 1;
         break;
       case 6:
         locX = callCalnX - 1;
         locY = callCalnY;
         heading = 2;
         break;
       case 7:
         locX = callCalnX - 1;
         locY = callCalnY - 1;
         heading = 3;
         break;
     }



     boolean isExistCharacter = false;
     L1Character cha = null;
     for (L1Object object : L1World.getInstance().getVisibleObjects((L1Object)callClanPc, 1)) {
       if (object instanceof L1Character) {
         cha = (L1Character)object;
         if (cha.getX() == locX && cha.getY() == locY && cha.getMapId() == mapId) {
           isExistCharacter = true;

           break;
         }
       }
     }
     if ((locX == 0 && locY == 0) || !map.isPassable(locX, locY) || isExistCharacter) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(627));
       return;
     }
     pc.start_teleport(locX, locY, mapId, heading, 2235, true, false);
   }


   public String getType() {
     return "[C] C_Attr";
   }
 }


