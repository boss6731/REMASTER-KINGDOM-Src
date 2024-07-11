 package l1j.server.server.clientpackets;

 import MJFX.UIAdapter.MJUIAdapter;
 import MJShiftObject.MJShiftObjectManager;
 import MJShiftObject.Object.MJShiftObject;
 import MJShiftObject.Object.MJShiftObjectOneTimeToken;
 import java.io.FileNotFoundException;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Calendar;
 import java.util.Collection;
 import java.util.GregorianCalendar;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.logging.Logger;
 import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
 import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
 import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Info;
 import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Loader;
 import l1j.server.CPMWBQSystem.CPMWBQSystemProvider;
 import l1j.server.ClanBuffList.ClanBuffListLoader;
 import l1j.server.Config;
 import l1j.server.FatigueProperty;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
 import l1j.server.MJAttendanceSystem.MJAttendanceRewardsHistory;
 import l1j.server.MJBookQuestSystem.Loader.BQSCharacterDataLoader;
 import l1j.server.MJDungeonTimer.Loader.DungeonTimeProgressLoader;
 import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
 import l1j.server.MJInstanceSystem.MJInstanceSpace;
 import l1j.server.MJKDASystem.MJKDALoader;
 import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
 import l1j.server.MJNetServer.Codec.MJNSHandler;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJPassiveSkill.MJPassiveUserLoader;
 import l1j.server.MJPushitem.MJPushProvider;
 import l1j.server.MJRaidSystem.MJRaidSpace;
 import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
 import l1j.server.MJTemplate.MJEPcStatus;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.AttendanceGroupType;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_GROUP_INFO;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_INFO_EXTEND;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_INFO_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_TAB_OPEN_ACK;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EXP_BOOSTING_INFO_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_INFO_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_POLYMORPH_EVENT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_USER_START_SUNDRY_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_BUFF_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_LIST_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_POINT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_STAT_INFO_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_GameGate.SC_GAMEGATE_PCCAFE_CHARGE_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ADD_INVENTORY_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOODPLEDGE_USER_INFO_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_ENTER_NOTICE_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
 import l1j.server.MJTemplate.MJProto.Mainserver_Client_Equip.SC_EXTEND_SLOT_INFO;
 import l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_FAVOR_UPDATE_NOTI;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
 import l1j.server.MJWarSystem.MJCastleWar;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.MJWarSystem.MJWar;
 import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
 import l1j.server.server.Account;
 import l1j.server.server.Controller.LoginController;
 import l1j.server.server.GMCommands;
 import l1j.server.server.GameClient;
 import l1j.server.server.GameServer;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.SkillCheck;
 import l1j.server.server.datatables.CharacterCustomQuestTable;
 import l1j.server.server.datatables.CharacterFreeShieldTable;
 import l1j.server.server.datatables.CharacterSlotItemTable;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.datatables.ExpTable;
 import l1j.server.server.datatables.GetBackRestartTable;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.datatables.SpamTable;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.EventAlramTick;
 import l1j.server.server.model.Getback;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1SummonInstance;
 import l1j.server.server.model.L1CastleLocation;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Cooking;
 import l1j.server.server.model.L1ExcludingList;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1PolyMorph;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.Warehouse.SupplementaryService;
 import l1j.server.server.model.Warehouse.WarehouseManager;
 import l1j.server.server.model.item.itemdelay.ItemDelayTimer;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.skill.L1SkillId;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.monitor.LoggerInstance;
 import l1j.server.server.serverpackets.S_ACTION_UI;
 import l1j.server.server.serverpackets.S_ACTION_UI2;
 import l1j.server.server.serverpackets.S_BookMarkLoad;
 import l1j.server.server.serverpackets.S_ChangeCharName;
 import l1j.server.server.serverpackets.S_CharVisualUpdate;
 import l1j.server.server.serverpackets.S_CharacterConfig;
 import l1j.server.server.serverpackets.S_ClanAttention;
 import l1j.server.server.serverpackets.S_CollectionNoti;
 import l1j.server.server.serverpackets.S_Disconnect;
 import l1j.server.server.serverpackets.S_ElfIcon;
 import l1j.server.server.serverpackets.S_FairlyConfig;
 import l1j.server.server.serverpackets.S_HPUpdate;
 import l1j.server.server.serverpackets.S_InventoryIcon;
 import l1j.server.server.serverpackets.S_Karma;
 import l1j.server.server.serverpackets.S_LetterList;
 import l1j.server.server.serverpackets.S_Liquor;
 import l1j.server.server.serverpackets.S_LoginResult;
 import l1j.server.server.serverpackets.S_MPUpdate;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.S_OwnCharStatus;
 import l1j.server.server.serverpackets.S_OwnCharStatus2;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ReturnedStat;
 import l1j.server.server.serverpackets.S_SPMR;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillBrave;
 import l1j.server.server.serverpackets.S_SkillHaste;
 import l1j.server.server.serverpackets.S_SkillIconAura;
 import l1j.server.server.serverpackets.S_SkillIconGFX;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_SlotChange;
 import l1j.server.server.serverpackets.S_SummonPack;
 import l1j.server.server.serverpackets.S_SurvivalCry;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.S_UnityIcon;
 import l1j.server.server.serverpackets.S_Unknown1;
 import l1j.server.server.serverpackets.S_Weather;
 import l1j.server.server.serverpackets.S_Weight;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1BookMark;
 import l1j.server.server.templates.L1FreeShield;
 import l1j.server.server.templates.L1GetBackRestart;
 import l1j.server.server.templates.L1ItemBookMark;
 import l1j.server.server.utils.CheckInitStat;
 import l1j.server.server.utils.SQLUtil;
 import l1j.server.server.utils.SystemUtil;






 public class C_LoginToServer
   extends ClientBasePacket
 {
   private static final String C_LOGIN_TO_SERVER = "[C] C_LoginToServer";

   static class BuffInfo
   {
     public int skillId;
     public int remainTime;
     public int polyId;
   }
   private static Logger _log = Logger.getLogger(C_LoginToServer.class.getName()); private static EventAlramTick es;

   private static void print_message(GameClient clnt, String message, boolean is_kick) throws Exception {
     System.out.println("─────────────────────────────────");
     System.out.println(message);
     System.out.println("─────────────────────────────────");
     if (is_kick) {
       clnt.kick();
       clnt.close();
     }
   }



     public static void do_direct_enter_world(String charName, GameClient client) throws FileNotFoundException, Exception {
         L1PcInstance pc = null;
         try {
            // 設置登錄記錄
             client.setLoginRecord(true);

            // 獲取當前時間
             Calendar cal = Calendar.getInstance();
             int 小時 = 10;
             int 分鐘 = 12;
             String 上午下午 = "下午";
             if (cal.get(Calendar.AM_PM) == Calendar.AM) {
                 上午下午 = "上午";
             }

             // 獲取客戶端的登錄帳戶名稱
             String login = client.getAccountName();
             int loginid = client.getAccount().getAccountId();

            // 加載角色
             pc = L1PcInstance.load(charName);
             Account account = Account.load(pc.getAccountName());
             if (account == null) {
                 print_message(client, String.format("[系統信息: 帳號為空 %s]", new Object[] { charName }), true);
                 return;
             }

            // 檢查帳戶是否為空
             if (client.getAccount() == null) {
                 print_message(client, String.format("[系統信息: 嘗試使用無效帳號 %s]", new Object[] { charName }), true);
                 return;
             }

            // 檢查是否已有活動角色
             if (client.getActiveChar() != null) {
                 print_message(client, String.format("[系統信息: 重複登錄，強制斷開 %s]", new Object[] { client.getIp() }), true);
                 return;
             }

             L1PcInstance OtherPc = L1World.getInstance().getPlayer(charName);
             if (OtherPc != null) {
                 // 檢查 OtherPc 是否在私人商店模式
                 boolean isPrivateShop = OtherPc.isPrivateShop();
                 // 強制斷開 OtherPc 的鏈接
                 GameServer.disconnectChar(OtherPc);
                 OtherPc = null;

                 // 如果 OtherPc 不是在私人商店模式
                 if (!isPrivateShop) {
                     print_message(client, String.format("[系統信息: 重複登錄，強制斷開 (%s:%s)]", client.getIp(), charName), true);
                     return;
                 }
             }

                    // 獲取所有玩家
             Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();
             for (L1PcInstance bugpc : pcs) {
                 // 檢查是否有與當前帳戶名相同的玩家且不在私人商店模式或者有連接
                 if (bugpc.getAccountName().equals(client.getAccountName()) && (!bugpc.isPrivateShop() || bugpc.getNetConnection() != null)) {
                     print_message(client, String.format("[系統信息: 重複登錄，強制斷開 (%s)]", client.getIp()), true);
                     GameServer.disconnectChar(bugpc);
                 }
             }

             pcs = null;

             if (pc == null || !login.equals(pc.getAccountName())) {
                 print_message(client, String.format("[系統信息: 嘗試登錄不存在於當前帳號的角色 : %s 帳號: %s]", charName, client.getAccountName()), true);
                 return;
             }

                // 檢查是否超過降級範圍限制
             if (!pc.isGm() && Config.Login.LevelDownRange != 0 && pc.getHighLevel() - pc.getLevel() >= Config.Login.LevelDownRange) {
                 print_message(client, String.format("[系統信息: 超過降級範圍: %s 帳號: %s 主機: %s]", charName, login, client.getIp()), true);
                 return;
             }


             // 檢查玩家的防禦等級是否低於設定值
             if (pc.getAC().getAc() < -Config.CharSettings.aclevel) {
                // 如果是，則禁止該玩家的帳戶
                 Account.ban(pc.getAccountName(), 95);
                // 踢出玩家並關閉連接
                 client.kick();
                 client.close();
                // 在控制台打印信息
                 System.out.println("▶ 防禦等級異常玩家被踢出：" + pc.getName());

                 return;
             }

                // 檢查玩家的等級是否高於其曾達到的最高等級
             if (pc.getLevel() > pc.getHighLevel()) {
                // 如果是，踢出玩家並關閉連接
                 client.kick();
                 client.close();
                // 在控制台打印信息
                 System.out.println("▶ 等級異常玩家被踢出：" + pc.getName());
                 return;
             }

                // 檢查玩家的等級是否超過設定的上限並且該玩家不是GM
             if (pc.getLevel() >= Config.CharSettings.LimitLevel && !pc.isGm()) {
                // 如果是，則禁止該玩家的帳戶
                 Account.ban(pc.getAccountName(), 95);
                // 通知玩家帳戶被禁
                 pc.sendPackets(new S_SystemMessage(pc.getName() + " 您的帳號已被凍結。"));
                // 斷開玩家的連接
                 pc.sendPackets(new S_Disconnect());

                // 如果玩家目前在線，則斷開連接
                 if (pc.getOnlineStatus() == 1) {
                     pc.sendPackets(new S_Disconnect());
                 }
                // 踢出玩家並關閉連接
                 client.kick();
                 client.close();
                // 在控制台打印信息
                 System.out.println("▶ 由於等級上限異常重新連接已被凍結：" + pc.getName());

                 return;
             }

                // 檢查玩家的職業編號是否在正常範圍內
             if (pc.getType() < 0 || pc.getType() > 9) {
                // 如果不在，踢出玩家並關閉連接
                 client.kick();
                 client.close();
                // 在控制台打印信息
                 System.out.println("▶ 嘗試以刪除的角色進行登錄，玩家被踢出：" + pc.getName());
             }

             // 檢查玩家是否在無人商店狀態
             if (pc.is無人商店()) {
                 pc.set無人商店(false);
             }

             // 打印登錄信息到控制台
             System.out.println(String.format("[系統信息: 登錄]:[登錄帳號:%s] [登錄角色:%s] [%s:%d:%d分] [登入IP位址:%s] [記憶體:%dMB]",
                     new Object[] { login, charName, 上午下午, Integer.valueOf(cal.get(時間)), Integer.valueOf(cal.get(分鐘)), client.getIp(), Long.valueOf(SystemUtil.getUsedMemoryMB()) }));

             // 將登錄信息記錄到日誌
             LoggerInstance.getInstance().addConnection("系統信息:登入角色=" + charName + "\\t登入帳號=" + login + "\\t登入IP位址=" + client.getHostname());

             // 更新代表角色信息
             MJMyRepresentativeService.service().updateRepresentativeCharacter(pc.getAccountName(), pc.getName());

             // 設置角色的實例狀態和在線狀態
             pc.set_instance_status(MJEPcStatus.WORLD);
             pc.setOnlineStatus(1);

             // 創建驗證碼
             pc.create_captcha();

             // 更新角色在線狀態到數據庫
             CharacterTable.updateOnlineStatus(pc);

             // 將角色對象存儲到遊戲世界中
             L1World.getInstance().storeObject(pc);

             // 設置客戶端的活動角色
             pc.setNetConnection(client);
             client.setActiveChar(pc);

             // 發送未知數據包（可能是初始化數據）
             pc.sendPackets(new S_Unknown1(pc));

             // 如果在服務器端啟用了角色配置，則發送角色配置數據包
             if (Config.Login.CharacterConfigInServerSide) {
                 pc.sendPackets(new S_CharacterConfig(pc.getId()));
             }

       pc.createTimeCollection();
       AinhasadFaithLoad(pc);


       AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
       if (Info == null) {
         AinhasadSpecialStatLoader.getInstance().addSpecialStat(pc.getId(), pc.getName());
       }
       SC_EINHASAD_POINT_STAT_INFO_NOTI.send_point(pc, Info);



       loadSkills(pc);
       MJPassiveUserLoader.load(pc);

       CharacterSlotItemTable.getInstance().selectCharSlot(pc, 0);
       CharacterSlotItemTable.getInstance().selectCharSlot(pc, 1);
       CharacterSlotItemTable.getInstance().selectCharSlot(pc, 2);
       CharacterSlotItemTable.getInstance().selectCharSlot(pc, 3);
       CharacterSlotItemTable.getInstance().selectCharSlotcolor(pc);
       MJCopyMapObservable.getInstance().resetPosition(pc);
       L1BookMark.bookmarkDB(pc);
       getItemBookMark(pc);
       pc.sendPackets((ServerBasePacket)new S_BookMarkLoad(pc));

       pc.sendPackets((ServerBasePacket)new S_ACTION_UI2(233, pc.getElixirStats()));

       GetBackRestartTable gbrTable = GetBackRestartTable.getInstance();
       L1GetBackRestart[] gbrList = gbrTable.getGetBackRestartTableList();
       for (L1GetBackRestart gbr : gbrList) {
         if (pc.getMapId() == gbr.getArea()) {
           pc.setX(gbr.getLocX());
           pc.setY(gbr.getLocY());
           pc.setMap(gbr.getMapId());

           break;
         }
       }
       MJCopyMapObservable.getInstance().resetPosition(pc);
       MJRaidSpace.getInstance().getBackPc(pc);
       DungeonTimeProgressLoader.load(pc);


       MJInstanceSpace.getInstance().getBackPc(pc);

       if (Config.ServerAdSetting.GETBACKREST) {
         int[] loc = Getback.GetBack_Location(pc, true);
         pc.setX(loc[0]);
         pc.setY(loc[1]);
         pc.setMap((short)loc[2]);
       }


       int castle_id = L1CastleLocation.getCastleIdByArea((L1Character)pc);
       if (pc.getMapId() == 66) {
         castle_id = 6;
       }
       if (0 < castle_id &&
         MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
         L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
         if (clan != null && clan.getCastleId() != castle_id) {
           int[] loc = new int[3];
           loc = L1CastleLocation.getGetBackLoc(castle_id);
           pc.setX(loc[0]);
           pc.setY(loc[1]);
           pc.setMap((short)loc[2]);
           loc = null;
         } else if (pc.getMapId() == 4) {
           int[] loc = new int[3];
           loc = L1CastleLocation.getGetBackLoc(castle_id);
           pc.setX(loc[0]);
           pc.setY(loc[1]);
           pc.setMap((short)loc[2]);
           loc = null;
         }
       }


       L1Map l1map = pc.getMap();
       if (l1map == null || !l1map.isInMap(pc.getX(), pc.getY())) {
         MJCopyMapObservable.getInstance().resetAlwaysPositon(pc);
       }

       pc.sendPackets((ServerBasePacket)S_CollectionNoti.LOGIN_START);

       pc.beginGameTimeCarrier();
       pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
       pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
       MJExpAmplifierLoader.getInstance().set(pc);
       pc.sendPackets((ServerBasePacket)new S_Weather(L1World.getInstance().getWeather()));
       pc.sendPackets((ServerBasePacket)new S_ReturnedStat(68, 0, 0));
       pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
       pc.load_private_probability();
       L1World.getInstance().addVisibleObject((L1Object)pc);
       pc.createFavorBookInventory();





       List<BuffInfo> buffList = loadBuff(pc);
       processBuff(pc, buffList);
       PcBuffCheck(pc);
       FreeShieldLoad(loginid, login);
       loadItems(pc, false);
       pc.sendPackets((ServerBasePacket)new S_ReturnedStat(67, 2, 1));
       loadItems(pc, true);

       for (Iterator<L1ItemInstance> partner = pc.getInventory().getItems().iterator(); partner.hasNext(); ) {
         L1ItemInstance item = partner.next();
         if (item.getItemId() == 700024)
           pc.sendPackets((ServerBasePacket)new S_PacketBox(142, item.getItemId(), "$13719",
                 L1BookMark.ShowBookmarkitem(pc, item.getItemId())));
         if (item.getItemId() == 700025) {
           pc.sendPackets((ServerBasePacket)new S_PacketBox(142, item.getId(), "$13719",
                 L1BookMark.ShowBookmarkitem(pc, item.getItemId())));
         }
       }
       pc.sendPackets((ServerBasePacket)new S_PacketBox(88, 0));
       pc.sendPackets((ServerBasePacket)new S_PacketBox(101, 0));
       pc.getLight().turnOnOffLight();
       pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
       pc.sendPackets((ServerBasePacket)new S_PacketBox(189));
       pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
       pc.on_regeneration();
       pc.startObjectAutoUpdate();
       pc.beginExpMonitor();

             if (Config.Login.UseShiftServer) {
                 // 檢查是否啟用了 ShiftServer
                 if (client.is_shift_transfer()) {
                     // 如果客戶端處於轉移狀態

                     final L1PcInstance t = pc;
                     // 使用 GeneralThreadPool 安排新任務
                     GeneralThreadPool.getInstance().schedule(new Runnable() {
                         public void run() {
                             // 傳送玩家到指定位置
                             t.start_teleportForGM(32738, 32872, 2236, t.getHeading(), 18339, true, true);

                             // 向玩家發送訊息，要求輸入新的登錄帳號
                             t.sendPackets(new S_PacketBox(84, "3請在聊天窗口輸入您將在當前伺服器使用的登錄帳號。"));

                             // 向玩家發送訊息，告知密碼將保持不變
                             t.sendPackets(new S_PacketBox(84, "3密碼將保持與之前伺服器相同。"));

                             // 發送普通訊息，要求輸入新的登錄帳號
                             t.sendPackets("H請在聊天窗口輸入您將在當前伺服器使用的登錄帳號。");

                             // 發送普通訊息，告知密碼將保持不變
                             t.sendPackets("H密碼將保持與之前伺服器相同。");
                         }
                     });
                 }
             }
               }1000L);
         } else if (client.is_shift_battle()) {
           final L1PcInstance t = pc;
           GeneralThreadPool.getInstance().schedule(new Runnable()
               {
                 public void run() {
                   MJShiftObjectManager.getInstance().do_enter_battle_character(t);
                 }
               },  500L);
         }
       }
       if (pc.isPcBuff()) {
         SC_PC_MASTER_FAVOR_UPDATE_NOTI.newInstance().send(pc);

         SC_USER_START_SUNDRY_NOTI.send(pc, pc.isPcBuff());
         SC_GAMEGATE_PCCAFE_CHARGE_NOTI.send(pc, pc.isPcBuff());
       }


       PcReset(pc);


               // 送出角色狀態數據包
               pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 65));

               // 在登錄時顯示視覺效果
               pc.sendVisualEffectAtLogin();

               // 送出包裝框數據包
               pc.sendPackets((ServerBasePacket)new S_PacketBox(32, 1));

               // 再次送出角色狀態數據包
               pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 4));

               // 送出公會標誌
               pc.sendClanMarks();

               // 設置客戶端狀態為進入世界
               client.setStatus(MJClientStatus.CLNT_STS_ENTERWORLD);

               // 設定角色當前的HP和MP
               pc.setCurrentHp(pc.getCurrentHpDB());
               pc.setCurrentMp(pc.getCurrentMpDB());

               // 檢查玩家是否存在於世界中
               L1PcInstance jonje = L1World.getInstance().getPlayer(pc.getName());
               if (jonje == null) {
                   // 如果玩家不存在，送出系統消息並踢出玩家
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("存在錯誤，強制退出！請重新連接。"));
               client.kick();
               return;
               }
       if (pc.getCurrentHp() > 0) {
         pc.setDead(false);
         pc.setStatus(0);
       } else {
         pc.setDead(true);
         pc.setStatus(8);
       }

       serchSummon(pc);
       MJCastleWarBusiness.getInstance().viewNowCastleWarState(pc);
       Clan_Check(pc);
       loadNBuff(pc);
       L1SkillId.recycleTam(pc);
       pc.setSkillEffect(90008, 30000L);


       if (pc.getLevel() > 5) {

         int einhasad = pc.getAccount().getBlessOfAin() + ((pc.getLastLoginTime() == null) ? 0 : ((int)(System.currentTimeMillis() - pc.getLastLoginTime().getTime()) / 900000));
         einhasad = Math.min(SC_REST_EXP_INFO_NOTI.EINHASAD_LIMIT, einhasad);
         pc.getAccount().setBlessOfAin(einhasad, pc);

         if (pc.getZoneType() == 1) {
           pc.startEinhasadTimer();
         }
         if (einhasad > 10000) {
           SC_REST_EXP_INFO_NOTI.send(pc);
         }
       }
       SC_EXP_BOOSTING_INFO_NOTI.send(pc);


               // 獲取排除名單
               L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(pc.getId());
               if (exList != null) {
                // 如果排除名單不為空，設置排除名單
               setExcludeList(pc, exList);
               }

               // 更新角色屬性
               pc.RenewStat();

               // 发送负重信息包
               pc.sendPackets((ServerBasePacket)new S_Weight(pc));

               // 通知用戶登錄
               MJUIAdapter.on_login_user(client, pc);

               // 加載角色自訂任務表
               CharacterCustomQuestTable.load(pc);

               // 設置角色狀態為在世界中
               pc.isWorld = true;

               L1ItemInstance temp = null;

               try {
                   // 檢查角色背包中的所有物品
               for (L1ItemInstance item : pc.getInventory().getItems()) {
                   temp = item;
                   if (item.isEquipped()) {
                       // 如果物品已裝備，更新物品槽包
               pc.getInventory().toSlotPacket(pc, item, true);
                   }
               }
               } catch (Exception e) {
                   // 捕捉異常並輸出錯誤訊息
               System.out.println("疑似有問題的物品 ->> " + temp.getItem().getName());
               }

               // 執行公會相關操作
               Clanclan(pc);

               // 发送公平配置包
               pc.sendPackets((ServerBasePacket)new S_FairlyConfig(pc));

               // 設置安全區域
               safetyzone(pc);

               // 如果角色在地獄時間內，開始地獄模式
               if (pc.getHellTime() > 0) {
               pc.beginHell(false);
               }
       pc.sendPackets((ServerBasePacket)new S_LetterList(pc, 0, 40), true);
       pc.sendPackets((ServerBasePacket)new S_LetterList(pc, 1, 80), true);
       pc.sendPackets((ServerBasePacket)new S_LetterList(pc, 2, 10), true);

       if (CheckMail(pc) > 0) {
         pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 1091));
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(428));
       }
       pc.checkStatus();
       if (!CheckInitStat.CheckPcStat(pc)) {
         client.kick();
         return;
       }
       pc.sendPackets((ServerBasePacket)new S_Karma(pc));
       pc.sendPackets((ServerBasePacket)new S_SlotChange(32, pc));
       pc.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(pc, 0, false));



       BQSCharacterDataLoader.in(pc);
       CPMWBQSystemProvider.provider().BQload(pc);
       MJPushProvider.provider().userLoading(pc);





       pc.load_lateral_status();
       if (MJAttendanceLoadManager.ATTEN_IS_RUNNING &&
         !client.is_shift_battle()) {
         SC_ATTENDANCE_BONUS_INFO_EXTEND.send(pc);
         SC_ATTENDANCE_BONUS_GROUP_INFO.send(pc);
         SC_ATTENDANCE_USER_DATA_EXTEND.send(pc);
         SC_ATTENDANCE_BONUS_GROUP_INFO.openinfo(pc);
         MJAttendanceRewardsHistory.send_history(pc);
         if (account.getAttendance_Premium()) {
           pc.sendPackets(
               SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(2), 0), true);

           pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(2), 0), true);
         }

         if (account.getAttendance_Special()) {
           pc.sendPackets(
               SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(3), 0), true);

           pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(3), 0), true);
         }

         if (account.getAttendance_Brave_Warrior()) {
           pc.sendPackets(
               SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(4), 1), true);

           pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(4), 1), true);
         }

         if (account.getAttendance_Aden_World()) {
           pc.sendPackets(
               SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(5), 1), true);

           pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(5), 1), true);
         }

         if (account.getAttendance_Bravery_Medal()) {
           pc.sendPackets(
               SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(6), 1), true);

           pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(6), 1), true);
         }

         SC_ATTENDANCE_INFO_NOTI.send1(pc);
         SC_ATTENDANCE_INFO_NOTI.send2(pc);
       }



       Buff_Individual(pc);
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
       pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
       on_fatigue(pc);
       pc.special_resistance_skill(pc, 0, 0, true);
       SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
       ClanBuffListLoader.getInstance().load_clan_buff(pc);



       if (pc.getMap().getBaseMapId() == 1400) {
         pc.start_teleport(33491, 32762, 4, 0, 18339, false, false);
       }

       if (pc.getMapId() == 430) {
         pc.start_teleport(32779, 32831, 622, 0, 18339, false, false);
       }


       if ((pc.getMapId() >= 2101 && pc.getMapId() <= 2151) || (pc.getMapId() >= 2151 && pc.getMapId() <= 2201)) {
         pc.start_teleport(33442, 32809, 4, 5, 18339, false, false);
       }


       if ((pc.getMapId() < 2101 || pc.getMapId() > 2151) && (pc.getMapId() < 2151 || pc.getMapId() > 2201)) {
         for (L1ItemInstance item : pc.getInventory().getItems()) {
           if ((item.getItemId() == 30055 || item.getItemId() == 30056) &&
             item != null) {
             pc.getInventory().removeItem(item, item.getCount());
           }
         }
       }


       if (pc.getReturnStat() != 0L) {
         屬性重置(pc);
       } else {
         pc.sendBonusStats();
       }


         for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
             // 遍歷所有在線玩家
         if (player.isGm()) {
             // 如果該玩家是GM（遊戲管理員）
         player.sendPackets("\\aG- [\\aA登錄\\aG] " + pc.getName() + " " + client.getAccountName() + " " + client
         .getIp() + "");
         // 向GM發送玩家登錄通知，包含角色名、帳號名和IP地址
         }
         }

         // 發送聯絡公告
         Contact_Notice(pc);
 } catch (Exception e) {
    // 捕捉異常並輸出錯誤訊息
         e.printStackTrace();
         System.out.println("◆(登錄)登錄失敗◆ : 請截圖並提供上述錯誤訊息。");
         }



         public static L1PcInstance doEnterWorld(String charName, GameClient client, boolean is_getback, int getback_x, int getback_y, int getback_mapid) throws FileNotFoundException, Exception {
     L1PcInstance pc = null;
      boolean is_returner = false;


       try {
           if (client.is_shift_battle()) {
               // 如果客戶端處於戰鬥轉移狀態
         MJShiftObject sobject = client.get_shift_object();
         if (sobject != null) {
             // 如果轉移對象不為空
         charName = sobject.get_destination_character_name();
         // 設置角色名稱為目標角色名稱
         is_returner = true;
         // 設置為返回者
         }
           }
           if (Config.Login.UseShiftServer) {
               // 如果配置中啟用了 ShiftServer

         MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_sender_object_from_account(client.getAccountName());
         if (sobject != null && !sobject.get_source_character_name().equals(charName)) {
             // 如果轉移對象不為空且來源角色名不等於當前角色名
         System.out.println(String.format("【帳號】%s 【參賽中】%s 【登錄】%s 【%s】【IP】%s 嘗試使用不同角色登入參加大賽。", new Object[] { client
         .getAccountName(), sobject.get_source_character_name(), charName,
         MJNSHandler.getLocalTime(), client.getIp() }));
         // 打印錯誤消息，提示帳號在參加大賽時嘗試使用不同角色登錄

         SC_CUSTOM_MSGBOX.do_kick(client, String.format("%s 在參加大賽，因此不允許使用其他角色登錄。", new Object[] { sobject
         .get_source_character_name() }));
         // 斷開客戶端連接，提示消息不允許使用其他角色登錄
         return null;
         // 返回空，表示登錄失敗
         }
       }


         client.setLoginRecord(true);
        // 獲取當前日曆實例
         Calendar cal = Calendar.getInstance();

        // 定義登錄時間
         int 시간 = 10;
         int 분 = 12;

        // 確定是上午還是下午
         String 上午下午 = "下午";
         if (cal.get(Calendar.AM_PM) == Calendar.AM) {
         上午下午 = "上午";
         }

        // 獲取帳號名稱
         String login = client.getAccountName();
        // 獲取帳號ID
         int loginid = client.getAccount().getAccountId();

        // 檢查角色是否被封禁
         if (GMCommands.isCharacterBlock(charName)) {
         System.out.println("─────────────────────────────────");
         System.out.println(String.format("角色 [%s] 被封禁，嘗試登錄。", new Object[] { charName }));
         System.out.println("─────────────────────────────────");
         client.sendPacket((ServerBasePacket)new S_LoginResult(52)); // 發送封禁提示
         return null; // 返回空值，終止登錄過程
         }

            // 加載角色數據
         pc = L1PcInstance.load(charName);
            // 確認帳號是否存在
         Account account = null;
         if (pc == null) {
         print_message(client, String.format("找不到角色. (世界連接): %s(%s)", new Object[] { charName, client.getIp() }), true);
         return null; // 返回空值，終止登錄過程
         }

       if (pc.getAccountName() != null) {
         account = Account.load(pc.getAccountName());
       } else {
         System.out.println("─────────────────────────────────");
         System.out.println("pc.getAccountName  Null  " + charName);
         System.out.println("─────────────────────────────────");
         client.kick();
         client.close();
         return null;
       }
       if (account == null) {
         System.out.println("─────────────────────────────────");
         System.out.println("account Null  " + charName);
         System.out.println("─────────────────────────────────");
         client.kick();
         client.close();
         return null;
       }
         if (client.getAccount() == null) {
             // 如果客戶端的帳號為空
         System.out.println("─────────────────────────────────");
         System.out.println("帳號 Null 嘗試登錄 " + charName);
         System.out.println("─────────────────────────────────");
         client.kick();
         // 踢出客戶端
         client.close();
         // 關閉客戶端連接
         return null;
         // 返回空值，終止登錄過程
         }

         if (client.getActiveChar() != null) {
             // 如果客戶端已有活躍角色
         System.out.println("─────────────────────────────────");
         System.out.println("同一 ID 重複登錄，因此強制中斷 (" + client.getIp() + ") 的連接。");
         System.out.println("─────────────────────────────────");
         client.close();
         // 關閉客戶端連接
         return null;
         // 返回空值，終止登錄過程
         }

         if (!client.is_shift_client()) {
             // 如果客戶端不是轉移客戶端
         GameClient clientByAccount = LoginController.getInstance().getClientByAccount(login);
         if (clientByAccount == null || clientByAccount != client) {
             // 如果獲取不到客戶端或獲取到的客戶端不等於當前客戶端
         System.out.println(clientByAccount);
         System.out.println(client);
         System.out.println("─────────────────────────────────");
         System.out.println("同一帳號重複登錄，因此強制中斷 (" + client.getIp() + ") 的連接。");
         System.out.println("─────────────────────────────────");
         client.close();
         // 關閉客戶端連接
         return null;
         // 返回空值，終止登錄過程
         }
         }


         L1PcInstance OtherPc = L1World.getInstance().getPlayer(charName);
         // 獲取指定角色名稱的玩家實例


         if (OtherPc != null) {

             // 如果該玩家實例存在

         boolean isPrivateShop = OtherPc.isPrivateShop();

         // 檢查該玩家是否在開設私人商店

         GameServer.disconnectChar(OtherPc);

         // 斷開該玩家的連接
         OtherPc = null;
         // 將變量置空

         if (!isPrivateShop) {
             // 如果該玩家不是在開設私人商店
         print_message(client,
         String.format("同一 ID 重複登錄，因此強制中斷 (%s:%s) 的連接。2", new Object[] { client.getIp(), charName }), true);
         // 打印重複登錄消息並強制斷開連接
         return null;
         // 返回空值，終止登錄過程
         }
         }

         Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();
         // 獲取所有在線玩家的實例集合
         for (L1PcInstance bugpc : pcs) {
             if (bugpc.getAccountName().equals(client.getAccountName()) && (
                     !bugpc.isPrivateShop() || bugpc.getNetConnection() != null)) {
                 // 檢查是否有相同帳號的玩家且不是在開設私人商店或有網絡連接
         print_message(client, String.format("同一帳號重複登錄，因此強制中斷 (%s) 的連接。", new Object[] { client.getIp() }), true);
         // 打印重複登錄消息並強制斷開連接

         GameServer.disconnectChar(bugpc);
         // 斷開該玩家的連接
             }
         }

         pcs = null;
         // 將玩家實例集合置空

         if (pc == null || !login.equals(pc.getAccountName())) {
             // 如果指定角色不存在或登錄帳號名稱不匹配
         System.out.println("─────────────────────────────────");
         System.out.println("嘗試登錄不屬於當前帳號的角色: " + charName + " 帳號: " + client.getAccountName());
         System.out.println("─────────────────────────────────");
         client.kick();
         // 踢出客戶端
         client.close();
         // 關閉客戶端連接
         return null;
         // 返回空值，終止登錄過程
         }
         if (!pc.isGm() && Config.Login.LevelDownRange != 0 &&
         pc.getHighLevel() - pc.getLevel() >= Config.Login.LevelDownRange) {
         System.out.println("─────────────────────────────────");
         _log.info("超過允許的降級範圍: " + charName + " 帳號= " + login + " 主機=" + client.getIp());
         System.out.println("超過允許的降級範圍: " + charName + " 帳號= " + login + " 主機=" + client.getIp());
         System.out.println("─────────────────────────────────");
         client.kick();
         return null;
         }

         if (pc.getAC().getAc() < -Config.CharSettings.aclevel) {
         Account.ban(pc.getAccountName(), 95);
         client.kick();
         client.close();
         System.out.println("▶ 防禦漏洞封禁: " + pc.getName());
         return null;
         }

         if (pc.getLevel() > pc.getHighLevel()) {
         client.kick();
         client.close();
         System.out.println("▶ 等級漏洞封禁: " + pc.getName());
         return null;
         }

         if (pc.getLevel() >= Config.CharSettings.LimitLevel && !pc.isGm()) {
         Account.ban(pc.getAccountName(), 95);
         pc.sendPackets(new S_SystemMessage(pc.getName() + " 已被封禁。"));
         pc.sendPackets(new S_Disconnect());

         if (pc.getOnlineStatus() == 1) {
         pc.sendPackets(new S_Disconnect());
         }
         client.kick();
         client.close();
         System.out.println("▶ 由於等級漏洞重連而被封禁: " + pc.getName());
         return null;
         }
         if (pc.getType() < 0 || pc.getType() > 9) {
         client.kick();
         client.close();
         System.out.println("▶ 嘗試以被刪除的角色登錄，踢出: " + pc.getName());
         }
            // 如果玩家的類型小於0或大於9，踢出客戶端並打印錯誤信息

         if (pc.is無人商店()) {
         pc.set無人商店(false);
         }
            // 如果玩家處於無人商店狀態，將其狀態設置為非無人商店

         if (!is_getback) {
         pc.setX(getback_x);
         pc.setY(getback_y);
         pc.setMap((short)getback_mapid);
         }
            // 如果不是回到過去的位置，設置玩家的坐標和地圖ID

         System.out.println(String.format("[一般:登錄] [登入帳號:%s] [角色名稱:%s] [%s:%d:%d分] [登入IP位址:%s] [記憶體:%d]",
         new Object[] { login, charName, 오전오후, cal.get(시간), cal.get(分), client.getIp(), SystemUtil.getUsedMemoryMB() }));
         LoggerInstance.getInstance()
         .addConnection("登錄角色=" + charName + " \t登入帳號=" + login + " \t登入IP位址=" + client.getHostname());
            // 打印登錄信息並記錄登錄詳情

         MJMyRepresentativeService.service().updateRepresentativeCharacter(pc.getAccountName(), pc.getName());
            // 更新代表性角色信息

         pc.set_instance_status(MJEPcStatus.WORLD);
         pc.setOnlineStatus(1);
         pc.create_captcha();
         CharacterTable.updateOnlineStatus(pc);
         L1World.getInstance().storeObject(pc);
         pc.setNetConnection(client);
         client.setActiveChar(pc);
         pc.sendPackets(new S_Unknown1(pc));
            // 設置玩家狀態和在線狀態，創建驗證碼，更新在線狀態，將玩家對象存儲到世界中，設置網絡連接並發送初始數據包

         if (Config.Login.CharacterConfigInServerSide) {
         pc.sendPackets(new S_CharacterConfig(pc.getId()));
         }
            // 如果服務器端配置啟用了角色配置，發送角色配置數據包

         pc.createTimeCollection();
         AinhasadFaithLoad(pc);
            // 創建時間收集並加載Ainhasad信仰數據

         AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
         if (Info == null) {
         AinhasadSpecialStatLoader.getInstance().addSpecialStat(pc.getId(), pc.getName());
         }
            // 如果找不到玩家的特殊統計信息，則添加新的特殊統計信息

       SC_EINHASAD_POINT_STAT_INFO_NOTI.send_point(pc, Info);










       loadSkills(pc);
       MJPassiveUserLoader.load(pc);

       CharacterSlotItemTable.getInstance().selectCharSlot(pc, 0);
       CharacterSlotItemTable.getInstance().selectCharSlot(pc, 1);
       CharacterSlotItemTable.getInstance().selectCharSlot(pc, 2);
       CharacterSlotItemTable.getInstance().selectCharSlot(pc, 3);
       CharacterSlotItemTable.getInstance().selectCharSlotcolor(pc);
       MJCopyMapObservable.getInstance().resetPosition(pc);
       L1BookMark.bookmarkDB(pc);
       getItemBookMark(pc);
       pc.sendPackets((ServerBasePacket)new S_BookMarkLoad(pc));

       pc.sendPackets((ServerBasePacket)new S_ACTION_UI2(233, pc.getElixirStats()));

       if (is_getback) {
         GetBackRestartTable gbrTable = GetBackRestartTable.getInstance();
         L1GetBackRestart[] gbrList = gbrTable.getGetBackRestartTableList();
         for (L1GetBackRestart gbr : gbrList) {
           if (pc.getMapId() == gbr.getArea()) {
             pc.setX(gbr.getLocX());
             pc.setY(gbr.getLocY());
             pc.setMap(gbr.getMapId());

             break;
           }
         }
       }
       if (is_getback) {
         MJCopyMapObservable.getInstance().resetPosition(pc);
         MJRaidSpace.getInstance().getBackPc(pc);
       }
       DungeonTimeProgressLoader.load(pc);

       if (is_getback)
       {
         MJInstanceSpace.getInstance().getBackPc(pc);
       }

       if (is_getback && Config.ServerAdSetting.GETBACKREST) {
         int[] loc = Getback.GetBack_Location(pc, true);
         pc.setX(loc[0]);
         pc.setY(loc[1]);
         pc.setMap((short)loc[2]);
       }

       if (pc.getMapId() >= 732 && pc.getMapId() <= 776) {
         pc.setX(33443);
         pc.setY(32797);
         pc.setMap((short)4);
         for (int i = 420100; i <= 420111; i++) {
           if (pc.getInventory().checkItem(i))
           {

             pc.getInventory().consumeItem(i);
           }
         }
       }

       int castle_id = L1CastleLocation.getCastleIdByArea((L1Character)pc);
       if (pc.getMapId() == 66) {
         castle_id = 6;
       }
       if (0 < castle_id &&
         MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
         L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
         if (clan != null && clan.getCastleId() != castle_id) {
           int[] loc = new int[3];
           loc = L1CastleLocation.getGetBackLoc(castle_id);
           pc.setX(loc[0]);
           pc.setY(loc[1]);
           pc.setMap((short)loc[2]);
           loc = null;
         } else if (pc.getMapId() == 4) {
           int[] loc = new int[3];
           loc = L1CastleLocation.getGetBackLoc(castle_id);
           pc.setX(loc[0]);
           pc.setY(loc[1]);
           pc.setMap((short)loc[2]);
           loc = null;
         }
       }


       L1Map l1map = pc.getMap();
       if (l1map == null || !l1map.isInMap(pc.getX(), pc.getY())) {
         MJCopyMapObservable.getInstance().resetAlwaysPositon(pc);
       }

       pc.sendPackets((ServerBasePacket)S_CollectionNoti.LOGIN_START);
       pc.beginGameTimeCarrier();
       pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
       pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
       MJExpAmplifierLoader.getInstance().set(pc);
       pc.sendPackets((ServerBasePacket)new S_Weather(L1World.getInstance().getWeather()));
       pc.sendPackets((ServerBasePacket)new S_ReturnedStat(68, 0, 0));
       pc.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
       pc.load_private_probability();
       L1World.getInstance().addVisibleObject((L1Object)pc);
       pc.createFavorBookInventory();





       List<BuffInfo> buffList = loadBuff(pc);

       processBuff(pc, buffList);

       PcBuffCheck(pc);
       FreeShieldLoad(loginid, login);

       loadItems(pc, false);
       pc.sendPackets((ServerBasePacket)new S_ReturnedStat(67, 2, 1));
       loadItems(pc, true);

       for (Iterator<L1ItemInstance> partner = pc.getInventory().getItems().iterator(); partner.hasNext(); ) {
         L1ItemInstance item = partner.next();
         if (item.getItemId() == 700024)
           pc.sendPackets((ServerBasePacket)new S_PacketBox(142, item.getItemId(), "$13719",
                 L1BookMark.ShowBookmarkitem(pc, item.getItemId())));
         if (item.getItemId() == 700025) {
           pc.sendPackets((ServerBasePacket)new S_PacketBox(142, item.getId(), "$13719",
                 L1BookMark.ShowBookmarkitem(pc, item.getItemId())));
         }
       }

       pc.sendPackets((ServerBasePacket)new S_PacketBox(88, 0));
       pc.sendPackets((ServerBasePacket)new S_PacketBox(101, 0));
       pc.getLight().turnOnOffLight();
       pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
       pc.sendPackets((ServerBasePacket)new S_PacketBox(189));
       pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
       pc.on_regeneration();
       pc.startObjectAutoUpdate();
       pc.beginExpMonitor();

       if (pc.isPcBuff()) {
         SC_PC_MASTER_FAVOR_UPDATE_NOTI.newInstance().send(pc);

         SC_USER_START_SUNDRY_NOTI.send(pc, pc.isPcBuff());
         SC_GAMEGATE_PCCAFE_CHARGE_NOTI.send(pc, pc.isPcBuff());
       }



       PcReset(pc);

         pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 65));
         pc.sendVisualEffectAtLogin();
         pc.sendPackets((ServerBasePacket)new S_PacketBox(32, 1));
         pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 4));
         pc.sendClanMarks();
            // 向玩家發送狀態和效果數據包

         client.setStatus(MJClientStatus.CLNT_STS_ENTERWORLD);
            // 設置客戶端狀態為進入世界

         pc.setCurrentHp(pc.getCurrentHpDB());
         pc.setCurrentMp(pc.getCurrentMpDB());
            // 設置玩家當前HP和MP為數據庫中的值

         L1PcInstance jonje = L1World.getInstance().getPlayer(pc.getName());
         if (jonje == null) {
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("存在漏洞，強制退出！請重新登錄"));
         client.kick();
         return null;
         }
            // 獲取玩家實例，如果不存在，向玩家發送系統消息並踢出客戶端，返回空值

         if (pc.getCurrentHp() > 0) {
         pc.setDead(false);
         pc.setStatus(0);
         } else {
         pc.setDead(true);
         pc.setStatus(8);
         }
            // 根據玩家當前HP設置死亡狀態和狀態碼

       serchSummon(pc);
       MJCastleWarBusiness.getInstance().viewNowCastleWarState(pc);
       Clan_Check(pc);
       loadNBuff(pc);
       L1SkillId.recycleTam(pc);
       pc.setSkillEffect(90008, 30000L);


       if (pc.getLevel() > 5) {

         int einhasad = pc.getAccount().getBlessOfAin() + ((pc.getLastLoginTime() == null) ? 0 : ((int)(System.currentTimeMillis() - pc.getLastLoginTime().getTime()) / 900000));
         einhasad = Math.min(SC_REST_EXP_INFO_NOTI.EINHASAD_LIMIT, einhasad);
         pc.getAccount().setBlessOfAin(einhasad, pc);

         if (pc.getZoneType() == 1) {
           pc.startEinhasadTimer();
         }
         if (einhasad > 10000) {
           SC_REST_EXP_INFO_NOTI.send(pc);
         }
       }
       SC_EXP_BOOSTING_INFO_NOTI.send(pc);


         L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(pc.getId());
         if (exList != null) {
         setExcludeList(pc, exList);
         }
            // 獲取垃圾郵件排除列表，如果存在，設置給玩家

         pc.RenewStat();
         pc.sendPackets((ServerBasePacket)new S_Weight(pc));
         MJUIAdapter.on_login_user(client, pc);
            // 更新玩家狀態，發送重量信息數據包，並通知UI適配器用戶登錄

         CharacterCustomQuestTable.load(pc);
            // 加載玩家的自定義任務

         pc.isWorld = true;
            // 設置玩家為已進入世界

         L1ItemInstance temp = null;

         try {
         for (L1ItemInstance item : pc.getInventory().getItems()) {
         temp = item;
         if (item.isEquipped()) {
         pc.getInventory().toSlotPacket(pc, item, true);
         }
         }
         } catch (Exception e) {
         System.out.println("出現錯誤，懷疑的物品是 ->> " + temp.getItem().getName());
         }
            // 遍歷玩家的物品，如果裝備了物品，則更新槽位數據包，如果出現錯誤，打印錯誤信息和懷疑物品的名稱

         Clanclan(pc);
         pc.sendPackets((ServerBasePacket)new S_FairlyConfig(pc));
         safetyzone(pc);
            // 執行Clanclan函數，發送公平配置數據包，執行安全區域函數

         if (pc.getHellTime() > 0) {
         pc.beginHell(false);
         }
            // 如果玩家的地獄時間大於0，開始地獄狀態

         pc.sendPackets((ServerBasePacket)new S_LetterList(pc, 0, 40), true);
         pc.sendPackets((ServerBasePacket)new S_LetterList(pc, 1, 80), true);
         pc.sendPackets((ServerBasePacket)new S_LetterList(pc, 2, 10), true);
            // 發送信件列表數據包

         if (CheckMail(pc) > 0) {
         pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 1091));
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(428));
         }
            // 檢查郵件，如果有新郵件，發送技能聲音和伺服器消息

         pc.checkStatus();
         if (!CheckInitStat.CheckPcStat(pc)) {
         client.kick();
         return null;
         }
            // 檢查玩家狀態，如果初始狀態檢查失敗，踢出客戶端並返回空值

         pc.sendPackets((ServerBasePacket)new S_Karma(pc));
         pc.sendPackets((ServerBasePacket)new S_SlotChange(32, pc));
         pc.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(pc, 0, false));
            // 發送業力數據包，槽位變更數據包，通知信息數據包




       BQSCharacterDataLoader.in(pc);

       CPMWBQSystemProvider.provider().BQload(pc);

       MJPushProvider.provider().userLoading(pc);






       pc.load_lateral_status();

       if (MJAttendanceLoadManager.ATTEN_IS_RUNNING &&
         !client.is_shift_battle()) {
         SC_ATTENDANCE_BONUS_INFO_EXTEND.send(pc);

         SC_ATTENDANCE_BONUS_GROUP_INFO.send(pc);
         SC_ATTENDANCE_USER_DATA_EXTEND.send(pc);
         SC_ATTENDANCE_BONUS_GROUP_INFO.openinfo(pc);
         MJAttendanceRewardsHistory.send_history(pc);

         if (account.getAttendance_Premium()) {
           pc.sendPackets(
               SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(2), 0), true);

           pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(2), 0), true);
         }

         if (account.getAttendance_Special()) {
           pc.sendPackets(
               SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(3), 0), true);

           pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(3), 0), true);
         }


         if (account.getAttendance_Brave_Warrior()) {
           pc.sendPackets(
               SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(4), 1), true);

           pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(4), 1), true);
         }

         if (account.getAttendance_Aden_World()) {
           pc.sendPackets(
               SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(5), 1), true);

           pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(5), 1), true);
         }

         if (account.getAttendance_Bravery_Medal()) {
           pc.sendPackets(
               SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(pc, AttendanceGroupType.fromInt(6), 1), true);

           pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(pc, AttendanceGroupType.fromInt(6), 1), true);
         }

         SC_ATTENDANCE_INFO_NOTI.send1(pc);
         SC_ATTENDANCE_INFO_NOTI.send2(pc);
       }


       Buff_Individual(pc);
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
       pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
       on_fatigue(pc);

       pc.special_resistance_skill(pc, 0, 0, true);
       SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
       ClanBuffListLoader.getInstance().load_clan_buff(pc);




       if (pc.getMap().getBaseMapId() == 1400) {
         pc.start_teleport(33491, 32762, 4, 0, 18339, false, false);
       }

       if (pc.getMapId() == 430) {
         pc.start_teleport(32779, 32831, 622, 0, 18339, false, false);
       }


       if ((pc.getMapId() >= 2101 && pc.getMapId() <= 2151) || (pc.getMapId() >= 2151 && pc.getMapId() <= 2201)) {
         pc.start_teleport(33442, 32809, 4, 5, 18339, false, false);
       }


       if ((pc.getMapId() < 2101 || pc.getMapId() > 2151) && (pc.getMapId() < 2151 || pc.getMapId() > 2201)) {
         for (L1ItemInstance item : pc.getInventory().getItems()) {
           if ((item.getItemId() == 30055 || item.getItemId() == 30056) &&
             item != null) {
             pc.getInventory().removeItem(item, item.getCount());
           }
         }
       }


         if (pc.getReturnStat() != 0L) {
         屬性重置(pc);
         } else {
         pc.sendBonusStats();
         }
            // 如果玩家有返還的屬性點數，則重置屬性，否則發送獎勵屬性點數

         for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
         if (player.isGm()) {
         player.sendPackets("\aG- [\aA登入\aG] " + pc.getName() + " " + client.getAccountName() + " " + client
         .getIp() + "");
         }
         }
            // 遍歷所有在線玩家，如果玩家是GM，則向其發送登錄通知數據包

         Contact_Notice(pc);
            // 發送聯絡通知

         } catch (Exception e) {
         e.printStackTrace();
         System.out.println("◆(登入)連接失敗◆ : 請截圖上述錯誤信息。");
            // 捕獲異常，打印堆棧軌跡並顯示錯誤信息

         if (pc != null && is_returner) {
 final L1PcInstance p = pc;
         GeneralThreadPool.getInstance().execute(new Runnable() {
 public void run() {
         MJShiftObjectManager.getInstance().do_returner(p);
         }
         });
            // 如果玩家實例不為空且是回歸玩家，則提交一個任務到線程池執行返回操作
         }

         pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false);
            // 開始傳送玩家到指定位置

         if (pc != null && Config.Login.UseShiftServer) {
         MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_sender_object(pc.getId());
         if (sobject != null) {
         if (MJShiftObjectManager.getInstance().is_battle_server_running()) {
         MJShiftObjectManager.getInstance().do_send_battle_server(pc, sobject.get_convert_parameters());
         System.out.println(String.format("【登入帳號】%s【角色名稱】%s【%s】【登入IP位址】%s 參與對抗戰的角色重新連接！移動處理完成。", new Object[] { pc
         .getAccountName(), charName, MJNSHandler.getLocalTime(), client.getIp() }));
         return pc;
         }
            // 如果戰鬥服務器正在運行，將玩家移動到戰鬥服務器，並打印重新連接信息

         String homeserveridentity = MJShiftObjectManager.getInstance().get_home_server_identity();
         MJShiftObjectManager.getInstance().do_receive(client, pc.getId(), (new MJShiftObjectOneTimeToken(homeserveridentity, true, sobject, homeserveridentity, false))
         .to_onetime_token());
         return pc;
         }
            // 如果戰鬥服務器未運行，將玩家移動到主服務器，並生成一次性令牌處理
         }
         }


     return pc;
   }

   public C_LoginToServer(byte[] abyte0, GameClient client) throws FileNotFoundException, Exception {
     super(abyte0);

     String charName = readS();

     if (charName.startsWith("_L")) {
       client.sendPacket((ServerBasePacket)S_ChangeCharName.getChangedStart());
       return;
     }
     doEnterWorld(charName, client, true, 0, 0, 0);
   }

   private static void getItemBookMark(L1PcInstance pc) {
     L1ItemInstance[] items = pc.getInventory().findItemsId(700023);
     for (int i = 0; i < items.length; i++) {
       L1ItemBookMark.bookmarItemkDB(pc, items[i]);
     }
   }


   private static void loadItems(L1PcInstance pc, boolean sendOption) {
     if (sendOption) {
       pc.getInventory().sendOptioon();
     } else {

       CharacterTable.getInstance().restoreInventory(pc);
       SC_ADD_INVENTORY_NOTI.sendLoginInventoryNoti(pc);
     }
     if (sendOption) {
       MJRankUserLoader.getInstance().onUser(pc);
     }
   }


   private static void safetyzone(L1PcInstance pc) {
     if (pc.getZoneType() == 0) {
       if (pc.getSafetyZone() == true) {
         pc.sendPackets((ServerBasePacket)new S_ACTION_UI(207, false));
         pc.setSafetyZone(false);
       }

     } else if (!pc.getSafetyZone()) {
       pc.sendPackets((ServerBasePacket)new S_ACTION_UI(207, true));
       pc.setSafetyZone(true);
     }
   }


   private static int CheckMail(L1PcInstance pc) {
     int count = 0;
     Connection con = null;
     PreparedStatement pstm1 = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm1 = con.prepareStatement(" SELECT count(*) as cnt FROM letter where receiver = ? AND isCheck = 0");
       pstm1.setString(1, pc.getName());

       rs = pstm1.executeQuery();
       if (rs.next()) {
         count = rs.getInt("cnt");
       }
     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm1);
       SQLUtil.close(con);
     }

     return count;
   }




   public static void AinhasadFaithLoad(L1PcInstance pc) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_special_stat2 WHERE obj_id=?");
       pstm.setInt(1, pc.getId());
       rs = pstm.executeQuery();

       HashMap<Integer, Timestamp> ainhasadFaithList = new HashMap<>();
       SC_EINHASAD_FAITH_LIST_NOTI noti = SC_EINHASAD_FAITH_LIST_NOTI.newInstance();

       while (rs.next()) {
         int index = rs.getInt("index_id");
         int group = rs.getInt("group_id");
         int type = rs.getInt("type");


         AinhasadSpecialStat2Info info = AinhasadSpecialStat2Loader.getInstance().getSpecialStat(index);
         int desc_id = info.get_desc_id();

         Timestamp endTime = rs.getTimestamp("endTime");
         String endTimeStr = endTime.toString();
         long ts = Timestamp.valueOf(endTimeStr).getTime();
         long currentTime = System.currentTimeMillis();

         long diffTime = ts - currentTime;
         int remainTime = Long.valueOf(ts / 1000L).intValue();
         if (type == 2 &&
           diffTime < 0L) {
           AinhasadSpecialStat2Loader.getInstance().deleteSpecialStat2(pc.getId(), pc.getName(), index);
           SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI.send(pc, index);

           continue;
         }
         ainhasadFaithList.put(Integer.valueOf(index), endTime);
       }
       if (ainhasadFaithList.containsKey(Integer.valueOf(1)) && ainhasadFaithList.containsKey(Integer.valueOf(2)) && ainhasadFaithList.containsKey(Integer.valueOf(3)) && ainhasadFaithList.containsKey(Integer.valueOf(4))) {
         noti.send(pc, 1, 0, 1, null, true);
       } else {
         noti.send(pc, 1, 0, 1, null, false);
       }
       if (ainhasadFaithList.containsKey(Integer.valueOf(5)) && ainhasadFaithList.containsKey(Integer.valueOf(6)) && ainhasadFaithList.containsKey(Integer.valueOf(7)) && ainhasadFaithList.containsKey(Integer.valueOf(8))) {
         noti.send(pc, 1, 0, 2, null, true);
       } else {
         noti.send(pc, 1, 0, 2, null, false);
       }
       int i;
       for (i = 1; i <= 8; i++) {
         if (!ainhasadFaithList.containsKey(Integer.valueOf(i))) {
           noti.send(pc, 2, i, (i >= 1 && i <= 4) ? 1 : 2, null, false);
         } else {
           noti.send(pc, 2, i, (i >= 1 && i <= 4) ? 1 : 2, ainhasadFaithList.get(Integer.valueOf(i)), true);
         }
       }
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_EINHASAD_FAITH_LIST_NOTI, true);

       for (i = 1; i <= 8; i++) {
         if (ainhasadFaithList.containsKey(Integer.valueOf(i))) {
           SC_EINHASAD_FAITH_BUFF_NOTI.send_index(pc, 2, 9280, i);

         }
       }

     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static void loadSkills(L1PcInstance pc) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;




     try {
       if (pc.isElf()) {
         Glory_Earth_Attr(pc);
       }

       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=?");
       pstm.setInt(1, pc.getId());
       rs = pstm.executeQuery();
       List<Integer> skillIdList = new ArrayList<>();
       SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
       while (rs.next()) {
         int skillId = rs.getInt("skill_id");
         noti.appendNewSpell(skillId, true);
         skillIdList.add(Integer.valueOf(skillId));
         pc.setSkillMastery(skillId);
       }

       SkillCheck.getInstance().AddSkill(pc.getId(), skillIdList);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   private static void 增益效果(L1PcInstance pc) {
     pc.getAC().addAc(-1);
     pc.sendPackets((ServerBasePacket)new S_PacketBox(pc, 125));
     pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
   }

   private static void serchSummon(L1PcInstance pc) {
     try {
       for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
         if (summon.getMaster().getId() == pc.getId()) {
           summon.setMaster((L1Character)pc);
           pc.addPet((L1NpcInstance)summon);
           for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer((L1Object)summon)) {
             visiblePc.sendPackets((ServerBasePacket)new S_SummonPack(summon, visiblePc));
           }
         }
       }
     } catch (Exception exception) {}
   }





   private static void Clanclan(L1PcInstance pc) {
     L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
     if (clan == null && pc.isCrown()) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(3247));
     } else if (clan != null && pc.isCrown()) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(3246));
     } else if (clan == null && !pc.isCrown()) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(3245));
     }
   }

   private static List<BuffInfo> loadBuff(L1PcInstance pc) {
     List<BuffInfo> buffList = new ArrayList<>();

     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT skill_id, remaining_time, poly_id FROM character_buff WHERE char_obj_id=?");

       pstm.setInt(1, pc.getId());
       rs = pstm.executeQuery();

       while (rs.next()) {
         BuffInfo buffInfo = new BuffInfo();
         buffInfo.skillId = rs.getInt("skill_id");
         buffInfo.remainTime = rs.getInt("remaining_time");
         buffInfo.polyId = rs.getInt("poly_id");

         buffList.add(buffInfo);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }

     return buffList;
   }

   private static void processBuff(final L1PcInstance pc, List<BuffInfo> buffList) {
     int[] icon = new int[177];
     Arrays.fill(icon, 0);

     for (BuffInfo buffInfo : buffList) {
       int reduc_armor_value, poly_id, playerAttr, i; SC_SPELL_BUFF_NOTI EXP_POTION_Event; long hasad; int time, time2, time3, time4, time5, time6, attr; SC_SPELL_BUFF_NOTI STR_BUFF, DEX_BUFF, INT_BUFF; L1SkillUse l1skilluse; int skillid = buffInfo.skillId;
       int remaining_time = buffInfo.remainTime;

       if ((skillid >= 3000 && skillid <= 3077) || skillid == 1541 || skillid == 3100 || skillid == 3101 || skillid == 3102 || skillid == 3103 || skillid == 3000130 || skillid == 3000129 || skillid == 1541)
       {

         if (skillid != 13069) {
           L1Cooking.eatCooking(pc, skillid, remaining_time);
           continue;
         }
       }
       if (skillid == 80009) {
         final int r_time = remaining_time;
         pc.setSkillEffect(skillid, (remaining_time * 1000));
         GeneralThreadPool.getInstance().schedule(new Runnable()
             {
               public void run() {
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(r_time, 2, true, true));
               }
             }1000L);
         SC_REST_EXP_INFO_NOTI.send(pc);
         continue;
       }
       if (skillid == 11223) {
         if (pc.isPcBuff()) {

           pc.setSkillEffect(11223, (remaining_time * 1000));
           L1SkillUse.on_icons(pc, skillid, remaining_time);
         } else {
           pc.killSkillEffectTimer(11223);
           L1SkillUse.off_icons(pc, 11223);
         }
       }
       switch (skillid) {
         case 10676:
           pc.setSkillEffect(10676, (remaining_time * 1000));
           L1SkillUse.on_icons(pc, skillid, remaining_time);
           break;
         case 88:
           reduc_armor_value = (pc.getLevel() >= 50) ? ((pc.getLevel() - 45) / 5) : 0;
           pc.addDamageReductionByArmor(reduc_armor_value);
           pc.set_reducreduction_value(reduc_armor_value);
           if (pc != null && pc.isPassive(MJPassiveID.REDUCTION_ARMOR_VETERAN.toInt())) {
             int bonus = 0;
             if (pc.getLevel() >= 80 && pc.getLevel() <= 100) {
               bonus = (pc.getLevel() - 80) / 4 + 1;
             } else {
               bonus = 5;
             }
             pc.set_pvp_defense(bonus);
             pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, 3);
             pc.set_reduction_armor_veteran(true);
             SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
           }
           SC_SPELL_BUFF_NOTI.sendDatabaseIcon(pc, SkillsTable.getInstance().getTemplate(skillid), remaining_time, true);
           break;

         case 32423423:
           pc.setSkillEffect(32423423, -1L);
           pc.set_Wanted_Level(1);
           pc.doWanted(true, true);
           break;
         case 32423424:
           pc.setSkillEffect(32423424, -1L);
           pc.set_Wanted_Level(2);
           pc.doWanted(true, true);
           break;
         case 32423425:
           pc.setSkillEffect(32423425, -1L);
           pc.set_Wanted_Level(3);
           pc.doWanted(true, true);
           break;
         case 4914:
           pc.getAC().addAc(-2);
           pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, 10);
           pc.addMaxHp(20);
           pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
           pc.addMaxMp(13);
           pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
           pc.sendPackets((ServerBasePacket)new S_PacketBox(154, 4914, remaining_time));
           break;
         case 67:
           poly_id = buffInfo.polyId;
           L1PolyMorph.doPoly((L1Character)pc, poly_id, remaining_time, 0, false, false);
           break;
         case 1000:
           pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 1, remaining_time));
           pc.broadcastPacket((ServerBasePacket)new S_SkillBrave(pc.getId(), 1, 0));
           pc.setBraveSpeed(1);
           break;
         case 1016:
           pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 3, remaining_time));
           pc.broadcastPacket((ServerBasePacket)new S_SkillBrave(pc.getId(), 3, 0));
           pc.setBraveSpeed(3);
           break;
         case 1001:
           pc.sendPackets((ServerBasePacket)new S_SkillHaste(pc.getId(), 1, remaining_time));
           pc.broadcastPacket((ServerBasePacket)new S_SkillHaste(pc.getId(), 1, 0));
           pc.setMoveSpeed(1);
           break;
         case 1002:
         case 20082:
           pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(34, remaining_time, true));
           break;
         case 1005:
           pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(36, remaining_time));
           break;

         case 134:
           pc.addWeightReduction(300);
           break;
         case 14:
         case 218:
           icon[0] = remaining_time / 16;
           pc.addWeightReduction(180);
           break;
         case 71:
           icon[1] = remaining_time / 4;
           break;
         case 64:
           icon[2] = remaining_time / 4;
           break;
         case 104:
           icon[3] = remaining_time / 4;
           break;
         case 47:
           icon[4] = remaining_time / 4;
           pc.addDmgup(-5);
           pc.addHitup(-1);
           break;
         case 70704:
           icon[5] = remaining_time / 4;
           pc.addHitup(-6);
           pc.getAC().addAc(12);
           break;
         case 55:
           if (pc.isWizard()) {
             pc.addDmgup(2);
             pc.addHitup(8);
           } else {
             pc.getAC().addAc(10);
             pc.addDmgup(2);
             pc.addHitup(8);
           }
           L1SkillUse.on_icons(pc, 55, remaining_time);
           break;
         case 158:
           icon[8] = remaining_time / 4;
           break;
         case 777777:
           icon[9] = remaining_time / 4;
           break;
         case 153:
           icon[10] = remaining_time / 4;
           break;
         case 176:
           icon[11] = remaining_time / 4;
           break;
         case 133:
           icon[12] = remaining_time / 4;
           playerAttr = pc.getElfAttr();
           i = -50;
           switch (playerAttr) {
             case 0:
               pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
               break;
             case 1:
               pc.getResistance().addEarth(i);
               pc.setAddAttrKind(1);
               break;
             case 2:
               pc.getResistance().addFire(i);
               pc.setAddAttrKind(2);
               break;
             case 4:
               pc.getResistance().addWater(i);
               pc.setAddAttrKind(4);
               break;
             case 8:
               pc.getResistance().addWind(i);
               pc.setAddAttrKind(8);
               break;
           }

           break;

         case 174:
           icon[14] = remaining_time / 4;
           break;
         case 175:
           icon[15] = remaining_time / 4;
           break;
         case 173:
           icon[16] = remaining_time / 4;
         case 50006:
           icon[30] = (remaining_time + 16) / 32;
           icon[31] = 40;
           pc.getAbility().addAddedCon(1);
           pc.getAbility().addAddedDex(5);
           pc.getAbility().addAddedStr(5);
           pc.addHitRate(3);
           pc.getAC().addAc(-3);
           break;
         case 50007:
           icon[30] = (remaining_time + 16) / 32;
           icon[31] = 41;
           pc.getAbility().addSp(1);
           pc.getAbility().addAddedCon(3);
           pc.getAbility().addAddedDex(5);
           pc.getAbility().addAddedStr(5);
           pc.addHitRate(5);
           pc.getAC().addAc(-8);
           pc.add_item_exp_bonus(20.0D);
           break;
         case 23069:
           pc.send_effect(false, 134, remaining_time);
           L1SkillUse.on_icons(pc, skillid, remaining_time);
           break;
         case 13069:
           EXP_POTION_Event = SC_SPELL_BUFF_NOTI.newInstance();
           EXP_POTION_Event.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
           EXP_POTION_Event.set_spell_id(13069);
           EXP_POTION_Event.set_duration(remaining_time);
           EXP_POTION_Event.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
           EXP_POTION_Event.set_on_icon_id(3069);
           EXP_POTION_Event.set_off_icon_id(6768);
           EXP_POTION_Event.set_icon_priority(10);
           EXP_POTION_Event.set_tooltip_str_id(5095);
           EXP_POTION_Event.set_new_str_id(5095);
           EXP_POTION_Event.set_end_str_id(0);
           EXP_POTION_Event.set_is_good(true);
           pc.sendPackets((MJIProtoMessage)EXP_POTION_Event, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
           break;
         case 7116:
         pc.setSkillEffect(7116, (remaining_time * 1000));
            // 設置玩家技能效果，持續時間為剩餘時間乘以1000毫秒

         pc.sendPackets((ServerBasePacket)new S_ACTION_UI(1020, pc));
            // 發送UI動作數據包，參數為1020和玩家對象

         pc.sendPackets((ServerBasePacket)new S_ACTION_UI2("艾因祝福", remaining_time * 1000L), true);
            // 發送另一個UI動作數據包，顯示"아인가호"（"神之祝福"）及其持續時間

         SC_EXP_BOOSTING_INFO_NOTI.send(pc);
            // 發送經驗值提升信息通知給玩家

         break;
            // 結束此case分支

         case 8382:
         hasad = pc.getAccount().getBlessOfAin();
         if (hasad < 10000L) {
         pc.sendPackets((ServerBasePacket)S_InventoryIcon.iconNewUnLimit(8383, 5087, true)); break;
         }
            // 如果玩家帳號中的艾因祝福少於10000，則發送無限時間的圖標數據包，並結束此case分支

         pc.sendPackets((ServerBasePacket)S_InventoryIcon.icoNew(8382, 5087, remaining_time, true));
            // 發送限時圖標數據包，顯示物品ID 8382，圖標ID 5087和剩餘時間

         break;
            // 結束此case分支

         case 7893:
           time = remaining_time / 4 - 255;
           if (time <= 0) {
             time += 255;
           }
           icon[18] = time;
           icon[19] = 61;
           icon[38] = (remaining_time <= 1020) ? 0 : 1;
           pc.addMaxHp(50);
           pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
           if (pc.isInParty())
           {
             pc.getParty().refreshPartyMemberStatus(pc);
           }
           pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
           break;
         case 7894:
           time2 = remaining_time / 4 - 255;
           if (time2 <= 0) {
             time2 += 255;
           }
           icon[18] = time2;
           icon[19] = 62;
           icon[38] = (remaining_time <= 1020) ? 0 : 1;
           pc.addMaxMp(40);
           pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
           break;
         case 7895:
           time3 = remaining_time / 4 - 255;
           if (time3 <= 0) {
             time3 += 255;
           }
           icon[18] = time3;
           icon[19] = 63;
           icon[38] = (remaining_time <= 1020) ? 0 : 1;
           pc.addDmgup(3);
           pc.addHitup(3);
           pc.getAbility().addSp(3);
           break;
         case 16553:
           time4 = remaining_time / 4 - 255;
           if (time4 <= 0) {
             time4 += 255;
           }
           icon[18] = time4;
           icon[19] = 63;
           icon[38] = (remaining_time <= 1020) ? 0 : 1;
           pc.getAbility().addSp(3);
           pc.addBaseMagicHitUp(5);
           pc.getResistance().addcalcPcDefense(3);
           break;
         case 16552:
           time5 = remaining_time / 4 - 255;
           if (time5 <= 0) {
             time5 += 255;
           }
           icon[18] = time5;
           icon[19] = 63;
           icon[38] = (remaining_time <= 1020) ? 0 : 1;
           pc.addBowDmgup(3);
           pc.addBowHitup(5);
           pc.getResistance().addcalcPcDefense(3);
           break;
         case 16551:
           time6 = remaining_time / 4 - 255;
           if (time6 <= 0) {
             time6 += 255;
           }
           icon[18] = time6;
           icon[19] = 63;
           icon[38] = (remaining_time <= 1020) ? 0 : 1;
           pc.addDmgRate(3);
           pc.addHitup(5);
           pc.getResistance().addcalcPcDefense(3);
           break;
         case 206:
           icon[20] = remaining_time / 16;
           break;
         case 216:
           icon[21] = remaining_time / 16;
           pc.getAbility().addAddedStr(1);
           pc.getAbility().addAddedDex(1);
           pc.getAbility().addAddedCon(1);
           pc.getAbility().addAddedInt(1);
           pc.getAbility().addAddedWis(1);
           pc.resetBaseMr();
           break;
         case 217:
           icon[22] = remaining_time / 16;
           pc.getAbility().addAddedStr(-1);
           pc.getAbility().addAddedDex(-1);
           pc.getAbility().addAddedCon(-1);
           pc.getAbility().addAddedInt(-1);
           pc.getAbility().addAddedWis(-1);
           pc.getAbility().addAddedCha(-1);
           pc.resetBaseMr();
           break;
         case 394:
           icon[23] = remaining_time / 4;
           break;



         case 20079:
           icon[29] = remaining_time / 4;
           break;
         case 129:
           pc.getResistance().addMr(10);
           pc.sendPackets((ServerBasePacket)new S_ElfIcon(remaining_time / 16, 0, 0, 0));
           break;
         case 137:
           pc.getAbility().addAddedStr(1);
           pc.getAbility().addAddedDex(1);
           pc.getAbility().addAddedInt(1);
           break;
         case 147:
           attr = pc.getElfAttr();
           if (attr == 1) {
             pc.getResistance().addEarth(50);
           } else if (attr == 2) {
             pc.getResistance().addFire(50);
           } else if (attr == 4) {
             pc.getResistance().addWater(50);
           } else if (attr == 8) {
             pc.getResistance().addWind(50);
           }
           pc.sendPackets((ServerBasePacket)new S_ElfIcon(0, 0, 0, remaining_time / 16));
           break;
         case 22000:
           icon[36] = remaining_time / 16;
           icon[37] = 70;
           pc.addHpr(3);
           pc.addMpr(3);
           pc.addDmgup(2);
           pc.addHitup(2);
           pc.addMaxHp(50);
           pc.addMaxMp(30);

           pc.getAbility().addSp(2);
           break;
         case 22001:
           icon[36] = remaining_time / 16;
           icon[37] = 71;
           pc.addHitup(2);

           pc.getAbility().addSp(1);
           pc.addMaxHp(50);
           pc.addMaxMp(30);
           break;
         case 22002:
           icon[36] = remaining_time / 16;
           icon[37] = 72;
           pc.addMaxHp(50);
           pc.addMaxMp(30);
           pc.getAC().addAc(-2);
           break;
         case 22003:
           icon[36] = remaining_time / 16;
           icon[37] = 73;
           pc.getAC().addAc(-1);
           break;
         case 22015:
           pc.getAC().addAc(-2);
           pc.getResistance().addWater(50);
           pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
           pc.sendPackets((ServerBasePacket)new S_PacketBox(100, 82, remaining_time / 60));
           break;
         case 22016:
           pc.addHpr(3);
           pc.addMpr(1);
           pc.getResistance().addWind(50);
           pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
           pc.sendPackets((ServerBasePacket)new S_PacketBox(100, 85, remaining_time / 60));
           break;
         case 22060:
           pc.addHitup(3);
           pc.addBowHitup(3);
           pc.getResistance().addFire(50);
           pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
           pc.sendPackets((ServerBasePacket)new S_PacketBox(100, 88, remaining_time / 60));
           break;
         case 186:
           pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 6, remaining_time));
           pc.broadcastPacket((ServerBasePacket)new S_SkillBrave(pc.getId(), 6, remaining_time));
           pc.setBraveSpeed(1);
           break;
         case 179:
           pc.setBraveSpeed(1);
           pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 1, remaining_time));
           Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_SkillBrave(pc.getId(), 1, 0));
           pc.setAttackSpeed();
           break;
         case 155:
           pc.setBraveSpeed(1);
           pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 8, remaining_time));
           Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_SkillBrave(pc.getId(), 8, 0));
           pc.sendPackets((ServerBasePacket)new S_SkillIconAura(154, remaining_time));
           pc.setAttackSpeed();
           break;
         case 177:
           pc.setBraveSpeed(1);
           pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 10, remaining_time));
           Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_SkillBrave(pc.getId(), 10, 0));
           pc.setAttackSpeed();
           break;
         case 178:
           pc.setBraveSpeed(9);
           pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 9, remaining_time));
           Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_SkillBrave(pc.getId(), 9, 0));
           pc.setAttackSpeed();
           break;
         case 22017:
           pc.sendPackets((ServerBasePacket)new S_Liquor(pc.getId(), 8));
           pc.setPearl(1);
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(1065, remaining_time));
           break;
         case 8001:
           L1SkillUse.on_icons(pc, skillid, remaining_time);
           break;
         case 80007:
           pc.sendPackets((ServerBasePacket)new S_PacketBox(remaining_time, true, true));
           break;
         case 80008:
           pc.sendPackets((ServerBasePacket)new S_PacketBox(remaining_time, 1, true, true));
           break;
         case 22019:
           pc.sendPackets((ServerBasePacket)new S_PacketBox(86, 1, remaining_time));
           break;
         case 22018:
           pc.sendPackets((ServerBasePacket)new S_PacketBox(86, 2, remaining_time));
           break;
         case 80004:
           pc.sendPackets((ServerBasePacket)new S_PacketBox(154, 12536, remaining_time));
           break;
         case 90008:
           remaining_time = 30;
           break;
         case 80012:
           poly_id = buffInfo.polyId;
           L1PolyMorph.doPoly((L1Character)pc, poly_id, remaining_time, 0, true, false);
           break;
         case 80013:
           poly_id = buffInfo.polyId;
           L1PolyMorph.doPoly((L1Character)pc, poly_id, remaining_time, 0, false, true);
           break;
         case 7679:
           pc.sendPackets((ServerBasePacket)new S_PacketBox(154, 14646, remaining_time));
           break;
         case 7680:
           pc.sendPackets((ServerBasePacket)new S_PacketBox(154, 14647, remaining_time));
           break;
         case 7681:
           pc.sendPackets((ServerBasePacket)new S_PacketBox(154, 14648, remaining_time));
           break;
         case 7682:
           pc.sendPackets((ServerBasePacket)new S_PacketBox(154, 14649, remaining_time));
           break;
         case 888810:
         case 888811:
         case 888812:
         case 888813:
         case 888814:
         case 888815:
         case 888816:
         case 888817:
         case 888818:
           pc.addMaxHp(200);
           pc.getResistance().addcalcPcDefense(10);
           L1SkillUse.on_icons(pc, skillid, remaining_time);
           break;
         case 888822:
         case 888823:
         case 888824:
         case 888825:
         case 888826:
         case 888827:
         case 888828:
         case 888829:
         case 888830:
           pc.addMaxHp(150);
           pc.getResistance().addcalcPcDefense(5);
           L1SkillUse.on_icons(pc, skillid, remaining_time);
           break;
         case 888832:
         case 888833:
         case 888834:
         case 888835:
         case 888836:
         case 888837:
         case 888838:
         case 888839:
         case 888840:
           pc.addMaxHp(100);
           pc.getResistance().addcalcPcDefense(3);
           L1SkillUse.on_icons(pc, skillid, remaining_time);
           break;
         case 60208:
           pc.addHitup(5);
           pc.addDmgup(3);
           pc.getAbility().addAddedStr(1);

           pc.setSkillEffect(60208, remaining_time);
           pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc), true);

           STR_BUFF = SC_SPELL_BUFF_NOTI.newInstance();
           STR_BUFF.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
           STR_BUFF.set_spell_id(60208);
           STR_BUFF.set_duration(remaining_time);
           STR_BUFF.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
           STR_BUFF.set_on_icon_id(4354);
           STR_BUFF.set_off_icon_id(4354);
           STR_BUFF.set_tooltip_str_id(1720);
           STR_BUFF.set_new_str_id(1720);
           STR_BUFF.set_end_str_id(2854);
           STR_BUFF.set_is_good(true);
           pc.sendPackets((MJIProtoMessage)STR_BUFF, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
           break;
         case 60209:
           pc.addBowHitup(5);
           pc.addBowDmgup(3);
           pc.getAbility().addAddedDex(1);
           pc.setSkillEffect(60209, remaining_time);
           pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc), true);
           pc.sendPackets((ServerBasePacket)new S_PacketBox(132, pc.getTotalER()), true);

           DEX_BUFF = SC_SPELL_BUFF_NOTI.newInstance();
           DEX_BUFF.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
           DEX_BUFF.set_spell_id(60209);
           DEX_BUFF.set_duration(remaining_time);
           DEX_BUFF.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
           DEX_BUFF.set_on_icon_id(4354);
           DEX_BUFF.set_off_icon_id(4354);
           DEX_BUFF.set_tooltip_str_id(1719);
           DEX_BUFF.set_new_str_id(1719);
           DEX_BUFF.set_end_str_id(2854);
           DEX_BUFF.set_is_good(true);
           pc.sendPackets((MJIProtoMessage)DEX_BUFF, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
           break;
         case 60210:
           pc.addMaxMp(50);
           pc.getAbility().addSp(2);
           pc.getAbility().addAddedInt(1);
           pc.setSkillEffect(60210, remaining_time);
           pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc), true);
           pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);

           INT_BUFF = SC_SPELL_BUFF_NOTI.newInstance();
           INT_BUFF.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
           INT_BUFF.set_spell_id(60210);
           INT_BUFF.set_duration(remaining_time);
           INT_BUFF.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
           INT_BUFF.set_on_icon_id(4354);
           INT_BUFF.set_off_icon_id(4354);
           INT_BUFF.set_tooltip_str_id(1721);
           INT_BUFF.set_new_str_id(1721);
           INT_BUFF.set_end_str_id(2854);
           INT_BUFF.set_is_good(true);
           pc.sendPackets((MJIProtoMessage)INT_BUFF, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
           break;
         case 85000:
           pc.getAC().addAc(-5);
           pc.addBowHitup(5);
           pc.addHitup(5);
           pc.addBaseMagicHitUp(2);
           pc.getResistance().addcalcPcDefense(5);
           pc.getResistance().addPVPweaponTotalDamage(5);
           pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
           pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
           L1SkillUse.on_icons(pc, skillid, remaining_time);
           break;
         case 85001:
           pc.getAbility().addAddedStr(1);
           pc.getAbility().addAddedDex(1);
           pc.getAbility().addAddedInt(1);
           pc.addBowHitup(3);
           pc.addHitup(3);
           pc.addBaseMagicHitUp(3);
           pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, 3);
           pc.getResistance().addPVPweaponTotalDamage(3);
           pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
           pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
           SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
           L1SkillUse.on_icons(pc, skillid, remaining_time);
           break;

         case 80000:
         case 80001:
           break;
         default:
           l1skilluse = new L1SkillUse();
           l1skilluse.handleCommands(pc, skillid, pc.getId(), pc.getX(), pc.getY(), null, remaining_time, 1);
           break;
       }
       pc.setSkillEffect(skillid, (remaining_time * 1000));
       if (skillid == 20079) {
         if (pc.isElfBrave())
         { pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 3, remaining_time)); }
         else
         { pc.sendPackets((ServerBasePacket)new S_SkillBrave(pc.getId(), 4, remaining_time)); }
       } else if (skillid == 80000) {
         L1SkillId.onEinhasadPrimiumFlat(pc);
       } else if (skillid == 80001) {
         L1SkillId.onEinhasadGreatFlat(pc);
       }

       L1SkillId.onSlowHandle(pc);
     }
     SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
     pc.sendPackets((ServerBasePacket)new S_UnityIcon(icon[0], icon[1], icon[2], icon[3], icon[4], icon[5], icon[6], icon[7], icon[8], icon[9], icon[10], icon[11], icon[12], icon[13], icon[14], icon[15], icon[16], icon[17], icon[18], icon[19], icon[20], icon[21], icon[22], icon[23], icon[24], icon[25], icon[26], icon[27], icon[28], icon[29], icon[30], icon[31], icon[32], icon[33], icon[34], icon[35], icon[36], icon[37], icon[38]));
   }





   private static void setExcludeList(L1PcInstance pc, L1ExcludingList exList) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_exclude WHERE char_id = ?");
       pstm.setInt(1, pc.getId());
       rs = pstm.executeQuery();

       while (rs.next()) {
         int type = rs.getInt("type");
         String name = rs.getString("exclude_name");
         if (!exList.contains(type, name)) {
           exList.add(type, name);
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

 public static void on_fatigue(L1PcInstance pc) {
         if (!FatigueProperty.getInstance().use_fatigue()) {
         return;
         }
            // 如果疲勞系統未啟用，則直接返回

         Account account = pc.getAccount();
            // 獲取玩家的帳號信息

         pc.sendPackets(String.format("目前帳號中的格蘭卡因憤怒點數為 %,d。", new Object[] { Integer.valueOf(account.get_fatigue_point()) }));
            // 向玩家發送當前帳號的格蘭卡因憤怒點數

         if (!account.has_fatigue()) {
         return;
         }
            // 如果帳號沒有疲勞狀態，則直接返回

         account.send_fatigue(pc);
            // 向帳號發送疲勞信息

         pc.sendPackets(String.format("\f3目前帳號中的格蘭卡因憤怒還剩 %,d秒。", new Object[] { Long.valueOf(account.remain_fatigue() / 1000L) }));
            // 向玩家發送剩餘疲勞時間，以秒為單位
         }

   public static void loadNBuff(L1PcInstance pc) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       if (Config.ServerAdSetting.AccountNBuff) {
         pstm = con.prepareStatement("SELECT * FROM character_tams WHERE skill_id BETWEEN ? AND ? AND account_id=?");
       }
       else {

         pstm = con.prepareStatement("SELECT * FROM character_tams WHERE skill_id BETWEEN ? AND ? AND char_id=?");
       }
       pstm.setInt(1, 4075);
       pstm.setInt(2, 4095);
       if (Config.ServerAdSetting.AccountNBuff) {
         pstm.setInt(3, pc.getAccount().getAccountId());
       } else {
         pstm.setInt(3, pc.getId());
       }
       rs = pstm.executeQuery();
       while (rs.next()) {
         int skillId = rs.getInt("skill_id");
         Timestamp expirationTime = rs.getTimestamp("expiration_time");
         if (Config.ServerAdSetting.AccountNBuff) {
           if (expirationTime.getTime() <= System.currentTimeMillis()) {
             SQLUtil.execute("DELETE FROM character_tams WHERE account_id=? AND skill_id=?", new Object[] {
                   Integer.valueOf(pc.getAccount().getAccountId()), Integer.valueOf(skillId)
                 });
             continue;
           }
         } else if (expirationTime.getTime() <= System.currentTimeMillis()) {
           SQLUtil.execute("DELETE FROM character_tams WHERE char_id=? AND skill_id=?", new Object[] {
                 Integer.valueOf(pc.getId()), Integer.valueOf(skillId)
               });

           continue;
         }
         if (pc.hasSkillEffect(skillId)) {
           continue;
         }
         int time = (int)(expirationTime.getTime() - System.currentTimeMillis()) / 1000;

         (new L1SkillUse()).handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, time, 1);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static void 屬性重置(L1PcInstance pc) {
     try {
       if (pc.getAbility().getBaseAmount() != 75) {
         int basestr = 0;
         int basedex = 0;
         int basecon = 0;
         int baseint = 0;
         int basewis = 0;
         int basecha = 0;
         switch (pc.getType()) {
           case 0:
             basestr = 13;
             basedex = 9;
             basecon = 11;
             basewis = 11;
             basecha = 13;
             baseint = 9;
             break;
           case 1:
             basestr = 16;
             basedex = 12;
             basecon = 16;
             basewis = 9;
             basecha = 10;
             baseint = 8;
             break;
           case 2:
             basestr = 10;
             basedex = 12;
             basecon = 12;
             basewis = 12;
             basecha = 9;
             baseint = 12;
             break;
           case 3:
             basestr = 8;
             basedex = 7;
             basecon = 12;
             basewis = 14;
             basecha = 8;
             baseint = 14;
             break;
           case 4:
             basestr = 15;
             basedex = 12;
             basecon = 12;
             basewis = 10;
             basecha = 8;
             baseint = 11;
             break;
           case 5:
             basestr = 13;
             basedex = 11;
             basecon = 14;
             basewis = 10;
             basecha = 8;
             baseint = 10;
             break;
           case 6:
             basestr = 9;
             basedex = 10;
             basecon = 12;
             basewis = 14;
             basecha = 8;
             baseint = 12;
             break;
           case 7:
             basestr = 16;
             basedex = 13;
             basecon = 16;
             basewis = 7;
             basecha = 9;
             baseint = 10;
             break;
           case 8:
             basestr = 16;
             basedex = 13;
             basecon = 11;
             basewis = 11;
             basecha = 15;
             baseint = 5;
             break;
           case 9:
             basestr = 14;
             basedex = 12;
             basecon = 16;
             basewis = 12;
             basecha = 6;
             baseint = 9;
             break;
           default:
             System.out.println(String.format("無效類型：%d", new Object[] { Integer.valueOf(pc.getType()) }));
             break;
         }
         pc.getAbility().init();
         pc.getAbility().setBaseStr(basestr);
         pc.getAbility().setBaseInt(baseint);
         pc.getAbility().setBaseWis(basewis);
         pc.getAbility().setBaseDex(basedex);
         pc.getAbility().setBaseCon(basecon);
         pc.getAbility().setBaseCha(basecha);
         pc.save();
       }


         L1SkillUse l1skilluse = new L1SkillUse();
         l1skilluse.handleCommands(pc, 44, pc.getId(), pc.getX(), pc.getY(), null, 0, 1);
            // 創建 L1SkillUse 對象並處理命令，施放技能ID為44的技能

         if (pc.getWeapon() != null) {
         pc.getInventory().setEquipped(pc.getWeapon(), false, false, false, false);
         }
            // 如果玩家裝備有武器，卸下武器

         for (L1ItemInstance armor : pc.getInventory().getItems()) {
         if (armor != null && armor.isEquipped()) {
         pc.getInventory().setEquipped(armor, false, false, false, false);
         }
            // 遍歷玩家的裝備，如果裝備了護甲，卸下護甲
         }

         pc.sendPackets((ServerBasePacket)new S_CharVisualUpdate(pc));
         pc.sendPackets((ServerBasePacket)new S_OwnCharStatus2(pc));
            // 發送角色外觀更新數據包和角色狀態數據包

         pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
         pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
         pc.sendPackets((ServerBasePacket)new S_OwnCharStatus2(pc));
         pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 1));
            // 發送角色魔法抗性數據包、屬性防禦數據包、狀態數據包和返回屬性數據包

         } catch (Exception e) {
         System.out.println("屬性重置命令錯誤");
         e.printStackTrace();
            // 捕獲並打印異常，顯示錯誤信息
         }


 public static void Buff_Individual(L1PcInstance pc) {
         if (Config.ServerAdSetting.DelayTimer) {
         ItemDelayTimer.loadItemDelay(pc);
         }
        // 如果服務器配置中啟用了延遲計時器，則加載玩家的物品延遲計時器

         MJKDALoader.getInstance().install(pc, false);
            // 安裝KDA相關設置，參數為玩家對象和布爾值false

         (pc.getKDA()).assassination_level = 0;
         pc.getKDA().do_assassination(pc);
            // 將玩家的暗殺等級設置為0，並調用暗殺方法

         if (GMCommands.IS_PROTECTION) {
         SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
         noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
         noti.set_spell_id(707100);
         noti.set_duration(-1);
         noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
         noti.set_on_icon_id(4760);
         noti.set_off_icon_id(4760);
         noti.set_icon_priority(10);
         noti.set_tooltip_str_id(4017);
         noti.set_is_good(false);
         pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
            // 如果保護模式啟用，設置保護模式的相應通知屬性並發送給玩家

         pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "[保護模式開始] 死亡時不會有懲罰。"));
            // 發送數據包通知玩家保護模式已啟用，死亡時不會受到懲罰

         pc.sendPackets("\f3[保護模式開始] 死亡時經驗值不會下降，物品不會掉落。");
            // 發送文本消息通知玩家保護模式已啟用，死亡時經驗值不會下降，物品不會掉落
         }
         }
     if (pc.getMapId() == 1501) {
       pc.getAC().addAc(-3);
       pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
       SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
       noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
       noti.set_spell_id(4066);
       noti.set_duration(-1);
       noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_UNLIMIT);
       noti.set_on_icon_id(7235);
       noti.set_off_icon_id(0);
       noti.set_icon_priority(-1);
       noti.set_tooltip_str_id(5402);
       noti.set_new_str_id(5402);
       noti.set_end_str_id(0);
       noti.set_is_good(true);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
     }
     if (pc.getInventory().checkItem(4100121)) {
       L1SkillUse.on_icons(pc, 12000, -1);
     }
     if (pc.getLevel() == 1) {
       pc.set_exp(ExpTable.getExpByLevel(Config.CharSettings.STARTLEVEL));
     }

     SC_EXTEND_SLOT_INFO.slot_info_send(pc);

     Account a = pc.getAccount();
     if (a.getCPW() != null && a.getCPW().length() >= 6) {
       增益效果(pc);
     }

     if (pc.getLevel() > 1 && pc.getLevel() < Config.ServerAdSetting.StartCharBoho) {
       pc.sendPackets((ServerBasePacket)S_InventoryIcon.iconNewUnLimit(2563, 3804, true));
     } else {
       pc.sendPackets((ServerBasePacket)S_InventoryIcon.icoEnd(2563));
     }

     long curTime = System.currentTimeMillis() / 1000L;
     if (pc.getCryOfSurvivalTime() + 10800L <= curTime) {
       pc.sendPackets((ServerBasePacket)new S_SurvivalCry(1));
     } else {
       long time = pc.getCryOfSurvivalTime() + 10800L - System.currentTimeMillis() / 1000L;
       pc.sendPackets((ServerBasePacket)new S_SurvivalCry(time));
     }

     long sysTime = System.currentTimeMillis();
     if (pc.getNetConnection().getAccount().getDragonRaid() != null &&
       sysTime <= pc.getNetConnection().getAccount().getDragonRaid().getTime()) {
       long BloodTime = pc.getNetConnection().getAccount().getDragonRaid().getTime() - sysTime;
       pc.removeSkillEffect(55005);
       pc.setSkillEffect(55005, (int)BloodTime);
       pc.sendPackets((ServerBasePacket)new S_PacketBox(179, (int)BloodTime / 1000), true);
     }


     pc.getInventory().consumeItem(810006);
     pc.getInventory().consumeItem(810007);
   }


   public static void Start_Event_Alram(L1PcInstance pc) {
     long inteval = 3600000L;
     es = new EventAlramTick(pc, 3600000L, true);
     GeneralThreadPool.getInstance().schedule((Runnable)es, 10L);
   }

   public static void PcReset(L1PcInstance pc) {
     Timestamp time = pc.getAccount().getLastLogOut();
     GregorianCalendar today = new GregorianCalendar();
     int year = today.get(1);
     int month = today.get(2);
     int day = today.get(5);
     int hour = today.get(10);





     if (time == null) {
       CharacterFreeShieldTable.getInstance().resetGaho(pc);


     }
     else if (time.getDate() < day || time.getMonth() < month) {
       CharacterFreeShieldTable.getInstance().resetGaho(pc);


     }
     else if (time.getDate() == day &&
       time.getHours() < 6) {
       CharacterFreeShieldTable.getInstance().resetGaho(pc);
     }
   }




   public static void FreeShieldLoad(int loginid, String loginname) {
     L1FreeShield shield = CharacterFreeShieldTable.getInstance().getFreeShieldLogin(loginid, loginname);
   }



   public static void PcBuffCheck(L1PcInstance pc) {
     long sysTime = System.currentTimeMillis();
     if (pc.isPcBuff()) {
       long 피씨타임 = pc.getAccount().getBuff_PC방().getTime() - sysTime;
       pc.sendPackets((ServerBasePacket)new S_PacketBox(84,
             String.format(Config.Message.PC_BUFF_MESSAGE + " %s", new Object[] { MJString.remainTimeString((int)(피씨타임 / 1000L)) })));

       L1SkillId.onPcCafeBuff(pc, 피씨타임);
     } else {
       SC_USER_START_SUNDRY_NOTI.send(pc, false);
     }
   }


   public static void Clan_Check(L1PcInstance pc) {
     L1Clan clan = L1World.getInstance().getClan(pc.getClanid());

     if (clan != null) {
       clan.updateClanMemberOnline(pc);
     }


     if (pc.getClanid() != 0) {
       if (clan != null) {
         if (clan.getEntranceNotice() != null) {
           SC_BLOOD_PLEDGE_ENTER_NOTICE_NOTI.send(pc);
         }
         if (clan.getBless() != 0) {
           (new L1SkillUse()).handleCommands(pc, 504 + clan.getBless(), pc.getId(), pc.getX(), pc.getY(), null, clan
               .getBuffTime()[clan.getBless() - 1], 1);
         }
         pc.sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo(clan.getClanName(), pc.getClanRank(), pc));
         pc.sendPackets((ServerBasePacket)new S_PacketBox(173, pc.getClan().getEmblemStatus()));
         if (clan.getGazeSize() != 0) {
           pc.sendPackets((ServerBasePacket)new S_ClanAttention(clan.getGazeSize(), clan.getGazeList()));
         }
         if (pc.getClanid() == clan.getClanId() && pc

           .getClanname().toLowerCase().equals(clan.getClanName().toLowerCase())) {
           for (L1PcInstance clanMember : clan.getOnlineClanMember()) {
             if (clanMember.getId() != pc.getId()) {
               clanMember.sendPackets((ServerBasePacket)new S_ServerMessage(843, pc.getName()));
             }
           }


           MJWar war = clan.getCurrentWar();
           if (war != null) {
             war.notifyEnenmy(pc);
             if (war instanceof MJCastleWar) {
               MJCastleWar castleWar = (MJCastleWar)war;
               if (castleWar.isRun()) {
                 castleWar.onLordBuff(pc);
               }
             }
           }
         } else {
           pc.setClanid(0);
           pc.setClanname("");
           pc.setClanRank(0);
           pc.save();
         }
       } else {
         pc.setClanid(0);
         pc.setClanname("");
         pc.setClanRank(0);
         pc.save();
       }
     }
     if (pc.getPartnerId() != 0) {
       L1PcInstance partner = (L1PcInstance)L1World.getInstance().findObject(pc.getPartnerId());
       if (partner != null && partner.getPartnerId() != 0 &&
         pc.getPartnerId() == partner.getId() && partner.getPartnerId() == pc.getId()) {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(548));

         partner.sendPackets((ServerBasePacket)new S_ServerMessage(549));
       }
     }
   }




 public static void Contact_Notice(L1PcInstance pc) {
         SupplementaryService warehouse = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
         int size = warehouse.getSize();
         if (size > 0) {
         SC_GOODS_INVEN_NOTI.do_send(pc);
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aG附加物品倉庫中有未領取的物品。"));
         pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "附加物品倉庫中有未領取的物品。"));
         }
            // 檢查附加物品倉庫是否有未領取的物品，如果有，發送通知和系統消息

         if (Config.Login.StandbyServer) {
         pc.sendPackets((ServerBasePacket)new S_PacketBox(84, Config.Message.OpenTimeMont));
         pc.sendPackets(Config.Message.OpenTimeMont);
         }
            // 如果服務器配置啟用了待機服務器，發送服務器開放時間的消息

         if (pc.getLevel() <= 95) {
         pc.sendPackets((ServerBasePacket)new S_PacketBox(84, Config.Message.GAMESERVERMENT));
         }
            // 如果玩家等級小於等於95，發送遊戲服務器公告消息

         if (pc.getLevel() >= Config.CharSettings.MaxLevel) {
         pc.sendPackets(Config.Message.MAX_LEVEL_MESSAGE);
         pc.sendPackets((ServerBasePacket)new S_PacketBox(84, Config.Message.MAX_LEVEL_MESSAGE));
         }
            // 如果玩家等級大於等於配置的最大等級，發送最大等級消息

         if (!Config.Web.webServerOnOff) {
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aG應用中心正在維護中。"));
         pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "\aG應用中心正在維護中，無法使用。"));
         }
            // 如果Web服務器未啟用，發送應用中心維護中的消息

         if (Config.ServerAdSetting.FEATHER) {
         pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "[活動進行中] 幸運商店活動"));
         }
            // 如果服務器配置啟用了羽毛活動，發送羽毛活動的消息

         if (Config.ServerAdSetting.PolyEvent2) {
         SC_POLYMORPH_EVENT_NOTI noti = SC_POLYMORPH_EVENT_NOTI.newInstance();
         noti.set_eventEnable(true);
         pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_POLYMORPH_EVENT_NOTI, true);
         pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "變身活動正在進行中。"));
         }
            // 如果服務器配置啟用了變身活動，設置並發送變身活動的通知
         }

     if (pc.getLevel() > 1 && pc.getLevel() <= Config.ServerAdSetting.LineageBuff) {
       pc.sendPackets((ServerBasePacket)S_InventoryIcon.iconNewUnLimit(9904, 4126, true));
     } else {
       pc.sendPackets((ServerBasePacket)S_InventoryIcon.icoEnd(9904));
     }
   }


   private static void Glory_Earth_Attr(L1PcInstance pc) {
     if (pc.getElfAttr() == 1 && pc.getGlory_Earth_Attr() == 0) {
       pc.setElfAttr(1);
       pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(15, 1));
     } else if (pc.getElfAttr() == 2 && pc.getGlory_Earth_Attr() == 0) {
       pc.setElfAttr(2);
       pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(15, 2));
     } else if (pc.getElfAttr() == 4 && pc.getGlory_Earth_Attr() == 0) {
       pc.setElfAttr(4);
       pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(15, 4));
     } else if (pc.getElfAttr() == 8 && pc.getGlory_Earth_Attr() == 0) {
       pc.setElfAttr(8);
       pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(15, 8));

     }
     else if (pc.getElfAttr() == 1 && pc.getGlory_Earth_Attr() == 2) {
       pc.setElfAttr(1);
       pc.setGlory_Earth_Attr(2);
       pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));
     } else if (pc.getElfAttr() == 1 && pc.getGlory_Earth_Attr() == 4) {
       pc.setElfAttr(1);
       pc.setGlory_Earth_Attr(4);
       pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));
     } else if (pc.getElfAttr() == 1 && pc.getGlory_Earth_Attr() == 8) {
       pc.setElfAttr(1);
       pc.setGlory_Earth_Attr(8);
       pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));
     } else if (pc.getElfAttr() == 2 && pc.getGlory_Earth_Attr() == 4) {
       pc.setElfAttr(2);
       pc.setGlory_Earth_Attr(4);
       pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));
     } else if (pc.getElfAttr() == 2 && pc.getGlory_Earth_Attr() == 8) {
       pc.setElfAttr(2);
       pc.setGlory_Earth_Attr(8);
       pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));
     } else if (pc.getElfAttr() == 4 && pc.getGlory_Earth_Attr() == 8) {
       pc.setElfAttr(4);
       pc.setGlory_Earth_Attr(8);
       pc.sendPackets((ServerBasePacket)new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));
     }
   }




   public String getType() {
     return "[C] C_LoginToServer";
   }
 }


