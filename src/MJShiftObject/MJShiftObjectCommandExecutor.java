     package MJShiftObject;

     import MJShiftObject.Battle.DomTower.MJDomTowerPlayManager;
     import MJShiftObject.Battle.ForgottenIsland.MJFIslandPlayManager;
     import MJShiftObject.Battle.MJShiftBattleItemWhiteList;
     import MJShiftObject.Battle.MJShiftBattlePlayManager;
     import MJShiftObject.Battle.Thebe.MJThebePlayManager;
     import MJShiftObject.Template.CommonServerBattleInfo;
     import MJShiftObject.Template.CommonServerInfo;
     import java.util.Calendar;
     import java.util.List;
     import l1j.server.MJTemplate.Command.MJCommand;
     import l1j.server.MJTemplate.Command.MJCommandArgs;
     import l1j.server.MJTemplate.Exceptions.MJCommandArgsIndexException;
     import l1j.server.MJTemplate.MJString;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1World;
     import l1j.server.server.model.gametime.RealTimeClock;
     import l1j.server.server.serverpackets.S_PacketBox;
     import l1j.server.server.serverpackets.S_SystemMessage;
     import l1j.server.server.serverpackets.ServerBasePacket;

     public class MJShiftObjectCommandExecutor
       implements MJCommand
     {
       private static final String[] OPENING_MESSAGES = new String[] { "爭奪特維拉斯統治權的戰爭已經開始。如果您打算參加，請速速報名。", "統治之塔已經開始。如果您打算參加，請速速報名。", "被遺忘的島嶼開始了。如果您打算參加，請速速報名。" };





       public void execute(MJCommandArgs args) {
         try {
           switch (args.nextInt()) {
             case 1:
               show_transfer_info(args);
               break;
             case 2:
               do_character_transfer(args);
               break;
             case 3:
               show_battle_info(args);
               break;
             case 4:
               enter_servers_battle(args);
               break;
             case 5:
               cancel_server_battle(args);
               break;
             case 6:
               reservation_server_battle(args);
               break;
             case 7:
               enter_all_player_server_battle(args);
               break;
             case 8:
               MJShiftObjectManager.getInstance().reload_config();
               args.notify("mj_shiftserver.properties 已重新載入");
               break;
             case 9:
               cancel_server_battle_manage(args);
               break;
             default:
               throw new Exception();
           }
         } catch (Exception e) {
           args.notify(".server [1.伺服器傳輸資訊][2.伺服器傳輸]");
           args.notify(".server【3.比賽資訊】【4.比賽參與】");
           args.notify(".server【5.取消參加比賽】【6.比賽開始】");
           args.notify(".server【7.全面進入比賽】");
           args.notify(".server [8.設定重新載入][9.取消比賽]");
         } finally {
           args.dispose();
         }
       }

       private void show_transfer_info(MJCommandArgs args) {
         List<CommonServerInfo> servers = MJShiftObjectManager.getInstance().get_commons_servers(false);
         if (servers == null || servers.size() <= 0) {
           args.notify("目前沒有可傳輸的伺服器訊息.");
           return;
         }
         int success_count = servers.size();
         for (CommonServerInfo csInfo : servers) {
           String message = "可轉讓的";
           if (!csInfo.server_is_on) {
             success_count--;
             message = "無法傳輸（伺服器關閉）";
           }
           if (!csInfo.server_is_transfer) {
             success_count--;
             message = "不可轉讓（功能已關閉）";
           }
           args.notify(String.format("- [%s] %s", new Object[] { csInfo.server_description, message }));
         }
         if (success_count <= 0) {
           args.notify("目前沒有可用於傳輸的伺服器.");
         }
       }


       private void do_character_transfer(MJCommandArgs args) {
         try {
           String character_name = args.nextString();
           String server_identity = args.nextString();
           if (MJString.isNullOrEmpty(character_name) || MJString.isNullOrEmpty(server_identity)) {
             throw new Exception();
           }
           L1PcInstance pc = L1World.getInstance().findpc(character_name);
           if (pc == null) {
             args.notify(String.format("%s 目前未連接到世界地圖。", new Object[] { character_name }));
             return;
           }
           if (MJShiftObjectManager.getInstance().is_battle_server_running()) {
             args.notify("比賽進行期間無法使用伺服器傳輸。");

             return;
           }
           try {
             MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.TRANSFER, server_identity, "");
             args.notify(String.format("%s 目前未連接到世界地圖。", new Object[] { character_name, server_identity }));
           } catch (Exception e) {
             e.printStackTrace();
           }
         } catch (Exception e) {
           args.notify(".serverwap2[角色名稱][前伺服器識別名]");
         }
       }

       private void show_battle_info(MJCommandArgs args) {
         List<CommonServerBattleInfo> servers = MJShiftObjectManager.getInstance().get_battle_servers_info();
         if (servers == null || servers.size() <= 0) {
           args.notify("目前沒有關於當前競爭伺服器的資訊。");
           return;
         }
         String enter_server_identity = MJShiftObjectManager.getInstance().get_battle_server_identity();
         int success_count = servers.size();
         for (CommonServerBattleInfo bInfo : servers) {
           Calendar start_cal = RealTimeClock.getInstance().getRealTimeCalendar();
           Calendar ended_cal = (Calendar)start_cal.clone();
           start_cal.setTimeInMillis(bInfo.get_start_millis());
           ended_cal.setTimeInMillis(bInfo.get_ended_millis());
           String message = String.format("--[%s] 目前 %s %02d:%02d:%02d ~ %02d:%02d:%02d", new Object[] { bInfo
                 .get_server_identity(),
                 enter_server_identity.equals(bInfo.get_server_identity()) ? "參與" : (bInfo.is_ended() ? "終止" : (bInfo.is_run() ? "論文集" : "預訂")),
                 Integer.valueOf(start_cal.get(11)), Integer.valueOf(start_cal.get(12)), Integer.valueOf(start_cal.get(13)),
                 Integer.valueOf(ended_cal.get(11)), Integer.valueOf(ended_cal.get(12)), Integer.valueOf(ended_cal.get(13)) });

           if (!bInfo.is_run()) {
             success_count--;
           }
           args.notify(message);
         }
         if (success_count <= 0) {
           args.notify("目前沒有伺服器運行比賽。");
         }
       }


       private void enter_servers_battle(MJCommandArgs args) {
         try {
           if (MJShiftObjectManager.getInstance().is_battle_server_enter()) {
             args.notify(String.format("您目前正在參加 %s 伺服器競賽，因此不可能有新條目。", new Object[] { MJShiftObjectManager.getInstance().get_battle_server_identity() }));

             return;
           }
           String server_identity = args.nextString();
           CommonServerBattleInfo bInfo = MJShiftObjectHelper.get_battle_server_info(server_identity);
           if (bInfo == null) {
             args.notify(String.format("%s 沒有預定的比賽", new Object[] { server_identity }));
             return;
           }
           String opening_message = "";
           int enter_type = 0;
           switch (bInfo.get_battle_name()) {
             case "底比斯":
               opening_message = OPENING_MESSAGES[0];
               enter_type = 4;
               break;
             case "控制":
               opening_message = OPENING_MESSAGES[1];
               enter_type = 8;
               break;
             case "遺忘~它!!":
               opening_message = OPENING_MESSAGES[2];
               enter_type = 16;
               break;
             default:
               args.notify(String.format("為 %s 安排的比賽類型未知。 %s", new Object[] { server_identity, opening_message }));
               break;
           }
           MJShiftObjectHelper.truncate_shift_datas(MJShiftObjectManager.getInstance().get_home_server_identity(), true, true);
           MJShiftObjectManager.getInstance().do_enter_battle_server(bInfo, enter_type, bInfo.get_current_kind());
           args.notify(String.format("%s參加比賽", new Object[] { server_identity }));
           L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { (ServerBasePacket)new S_SystemMessage(opening_message), (ServerBasePacket)new S_PacketBox(84, opening_message) });


         }
         catch (MJCommandArgsIndexException e) {
           args.notify(".server4 [參與伺服器識別名稱]");
         } catch (Exception e) {
           e.printStackTrace();
           args.notify(".server4 [參與伺服器識別名稱]");
         }
       }

       private void cancel_server_battle(MJCommandArgs args) {
         if (MJShiftObjectManager.getInstance().is_my_battle_server()) {
           args.notify("直接舉辦的比賽是不能隨意取消的。");
           return;
         }
         MJShiftObjectManager.getInstance().do_cancel_battle_server();
         args.notify("我取消參加比賽。");
         MJShiftObjectHelper.delete_battle_server(MJShiftObjectManager.getInstance().get_home_server_identity());
       }

       private void cancel_server_battle_manage(MJCommandArgs args) {
         if (!MJShiftObjectManager.getInstance().is_battle_server_running()) {
           args.notify("比賽沒有進行.");
           return;
         }
         if (!MJShiftObjectManager.getInstance().is_my_battle_server()) {
           args.notify("除非遊戲在您的伺服器上，否則您無法取消遊戲。");
           return;
         }
         MJShiftObjectManager.getInstance().do_cancel_battle_server();
         args.notify("我取消參加比賽。");
         MJShiftObjectHelper.delete_battle_server(MJShiftObjectManager.getInstance().get_home_server_identity()); } private void reservation_server_battle(MJCommandArgs args) {
         try {
           MJThebePlayManager mJThebePlayManager;
           MJDomTowerPlayManager mJDomTowerPlayManager;
           MJFIslandPlayManager mJFIslandPlayManager;
           if (MJShiftObjectManager.getInstance().is_battle_server_enter()) {
             args.notify(String.format("您目前正在 %s 伺服器上參加比賽，因此您無法註冊參加新的比賽。", new Object[] { MJShiftObjectManager.getInstance().get_battle_server_identity() }));

             return;
           }
           int minute = args.nextInt();
           String battle_name = args.nextString();
           boolean is_local_server = (args.nextInt() == 1);
           MJShiftBattlePlayManager<?> manager = null;
           MJShiftBattleItemWhiteList whitelist = null;

           long current_millis = System.currentTimeMillis();
           long ended_millis = (minute * 60000) + current_millis;






           CommonServerBattleInfo bInfo = CommonServerBattleInfo.newInstance().set_server_identity(MJShiftObjectManager.getInstance().get_home_server_identity()).set_start_millis(current_millis).set_ended_millis(ended_millis - (MJShiftObjectManager.getInstance().get_my_server_battle_ready_seconds() * 1000)).set_battle_name(battle_name);

           int kind = 3;
           String opening_message = "";
           switch (battle_name) {
             case "底比斯":
               mJThebePlayManager = new MJThebePlayManager(bInfo.get_ended_millis(), is_local_server);
               whitelist = new MJShiftBattleItemWhiteList("server_battle_white_list_thebes");
               opening_message = OPENING_MESSAGES[0];
               kind = 3;
               break;
             case "控制":
               mJDomTowerPlayManager = new MJDomTowerPlayManager(bInfo.get_ended_millis(), is_local_server);
               whitelist = new MJShiftBattleItemWhiteList("server_battle_white_list_domtower");
               opening_message = OPENING_MESSAGES[1];
               kind = 7;
               break;
             case "遺忘~它!!":
               mJFIslandPlayManager = new MJFIslandPlayManager(bInfo.get_ended_millis(), is_local_server);
               whitelist = new MJShiftBattleItemWhiteList("server_battle_white_list_forisland");
               opening_message = OPENING_MESSAGES[2];
               kind = 4;
               break;
             default:
               throw new Exception();
           }
           bInfo.set_current_kind(kind);
           if (!is_local_server) {
             MJShiftObjectHelper.reservation_server_battle(
                 MJShiftObjectManager.getInstance().get_home_server_identity(), current_millis, ended_millis - (

                 MJShiftObjectManager.getInstance().get_my_server_battle_store_ready_seconds() * 1000), bInfo
                 .get_current_kind(), bInfo
                 .get_battle_name());
           }

           Calendar start_cal = RealTimeClock.getInstance().getRealTimeCalendar();
           Calendar ended_cal = (Calendar)start_cal.clone();
           start_cal.setTimeInMillis(current_millis);
           ended_cal.setTimeInMillis(ended_millis);
           String message = String.format("[伺服器大戰開啟%s] %02d:%02d:%02d ~ %02d:%02d:%02d", new Object[] { is_local_server ? "限本地" : "競賽",
                 Integer.valueOf(start_cal.get(11)), Integer.valueOf(start_cal.get(12)), Integer.valueOf(start_cal.get(13)),
                 Integer.valueOf(ended_cal.get(11)), Integer.valueOf(ended_cal.get(12)), Integer.valueOf(ended_cal.get(13)) });

           args.notify(message);
           MJShiftObjectHelper.truncate_shift_datas(MJShiftObjectManager.getInstance().get_home_server_identity(), true, true);
           MJShiftObjectManager.getInstance().do_enter_battle_server(bInfo, (MJShiftBattlePlayManager<?>)mJFIslandPlayManager, whitelist, bInfo.get_current_kind());
           L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { (ServerBasePacket)new S_SystemMessage(opening_message), (ServerBasePacket)new S_PacketBox(84, opening_message) });


         }
         catch (Exception e) {
           args.notify(".server6【對戰持續時間（分鐘）】【對戰類型】【伺服器類型】");
           args.notify("競賽類型 => 底比斯、統治、遺忘");
           args.notify("伺服器類型 => 1=本地，0=競爭");
         }
       }

       private void enter_all_player_server_battle(MJCommandArgs args) {
         try {
           if (!MJShiftObjectManager.getInstance().is_battle_server_running()) {
             args.notify("目前沒有任何比賽參加。");
             return;
           }
           String parameters = "12852";
           for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
             MJShiftObjectManager.getInstance().do_send_battle_server(pc, parameters);
           }
         } catch (Exception e) {
           e.printStackTrace();
         }
       }
     }


