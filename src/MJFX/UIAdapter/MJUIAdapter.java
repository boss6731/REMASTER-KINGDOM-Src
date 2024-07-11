 package MJFX.UIAdapter;

 import MJFX.Letter.MJFxLetterManager;
 import MJFX.Logger.MJFxLogger;
 import MJFX.MJFxEntry;
 import java.sql.Timestamp;
 import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmService;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.swing.chocco;

 public class MJUIAdapter
 {
   private static boolean is_mjfx() {
     return (MJFxEntry.getInstance() != null);
   }

   public static void on_exit() {
     if (is_mjfx()) {
       MJFxEntry.getInstance().invoke_exit();
     } else {
       System.exit(0);
     }
   }

   public static void on_delete_letter(int id) {
     if (is_mjfx()) {
       MJFxLetterManager.getInstance().on_delete_letter(id);
     }
   }



   public static void on_check_letter(int id) {
     if (is_mjfx()) {
       MJFxLetterManager.getInstance().on_check_letter(id);
     }
   }



   public static void on_receive_letter(int id, String sender, String title, String content, Timestamp ts, String receiver) {
     if (is_mjfx()) {
       MJFxLetterManager.getInstance().on_receive_letter(id, sender, title, content, ts, receiver);
     }
   }



   public static void on_login_user(GameClient clnt, L1PcInstance pc) {
     if (is_mjfx()) {
       MJFxLogger.LOGIN_CHARACTER.append_log(String.format("<登入> (%d)%s -%s", new Object[] { Integer.valueOf(L1World.getInstance().get_player_size()), pc.getName(), (clnt == null) ? "" : clnt.getHostname() }));
     } else {
       try {
         chocco.count++;
         chocco.label2.setText(" " + chocco.count);
         chocco.userlist.add(pc.getName());
       } catch (Exception exception) {}
     }
     MJMyGmService.service().onEnterCharacter(pc);
   }

   public static void on_logout_user(GameClient clnt, L1PcInstance pc) {
     if (is_mjfx()) {
       MJFxLogger.LOGIN_CHARACTER.append_log(String.format("<退出> (%d)%s -%s", new Object[] { Integer.valueOf(L1World.getInstance().get_player_size()), pc.getName(), (clnt == null) ? "" : clnt.getHostname() }));
     } else {
       try {
         chocco.count--;
         chocco.label2.setText(" " + chocco.count);
         chocco.userlist.remove(pc.getName());
       } catch (Exception exception) {}
     }
     MJMyGmService.service().onRemoveCharacter(pc);
   }

   public static void on_banip_clear() {
     if (!is_mjfx())
     {
       chocco.iplist.removeAll();
     }
   }

   public static void on_banip_append(String ip) {
     if (!is_mjfx())
     {
       chocco.iplist.add(ip);
     }
   }

   public static void on_shop_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.TRADE.update_log(s);
     } else {
       chocco.txtShop.append(s);
     }
   }
   public static void on_chat_world_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.CHAT_WORLD.update_log(s);
     } else {
       chocco.txtGlobalChat.append(s);
     }
   }
   public static void on_chat_normal_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.CHAT_NORMAL.update_log(s);
     }
   }


   public static void on_chat_pledge_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.CHAT_PLEDGE.update_log(s);
     } else {
       chocco.txtClanChat.append(s);
     }
   }
   public static void on_chat_party_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.CHAT_PARTY.update_log(s);
     } else {
       chocco.txtPartyChat.append(s);
     }
   }
   public static void on_chat_whisper_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.CHAT_WHISPER.update_log(s);
     } else {
       chocco.txtWhisper.append(s);
     }
   }
   public static void on_chat_trade_append(String s) {
     if (is_mjfx());
   }


   public static void on_trade_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.TRADE.update_log(s);
     } else {
       chocco.txtTrade.append(s);
     }
   }
   public static void on_warehouse_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.WAREHOUSE.update_log(s);
     } else {
       chocco.txtWarehouse.append(s);
     }
   }
   public static void on_enchant_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.ENCHANT_MONITOR.update_log(s);
     } else {
       chocco.txtEnchant.append(s);
     }
   }
   public static void on_item_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.ITEM.update_log(s);
     } else {
       chocco.txtPickup.append(s);
     }
   }

   public static void on_create_account(String account, String hostname) {
     if (is_mjfx()) {
       MJFxLogger.ACCOUNT_CREATE.append_log(String.format("<建立帳號> %s -%s", new Object[] { account, hostname }));
     }
   }


   public static void on_gm_command(String s) {
     if (is_mjfx()) {
       MJFxLogger.GM_COMMAND.update_log(s);
     }
   }

   public static void on_gm_command_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.GM_COMMAND.append_log(String.format("<GM指令結果>%s", new Object[] { s }));
     } else {
       System.out.println(s);
     }
   }
   public static void on_minigame_append(String s) {
     if (is_mjfx()) {
       MJFxLogger.MINIGAME.append_log(s);
     }
   }



   public static void on_boss_append(int npc_id, String boss_name, int x, int y, int map_id) {
     if (is_mjfx()) {
       MJFxLogger.BOSS_TIMER.append_log(String.format("<老闆通知> %s(%d) %d,%d,%d", new Object[] { boss_name, Integer.valueOf(npc_id), Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(map_id) }));
     }
   }

   public static void on_flush() {
     if (!is_mjfx()) {
       chocco.txtGlobalChat.setText("");
       chocco.txtClanChat.setText("");
       chocco.txtPartyChat.setText("");
       chocco.txtWhisper.setText("");
       chocco.txtShop.setText("");
       chocco.txtTrade.setText("");
       chocco.txtWarehouse.setText("");
       chocco.txtEnchant.setText("");
       chocco.txtPickup.setText("");
     }
   }
 }


