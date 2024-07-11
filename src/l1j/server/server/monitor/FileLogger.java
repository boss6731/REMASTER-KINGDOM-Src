         package l1j.server.server.monitor;

         import MJFX.UIAdapter.MJUIAdapter;
         import java.io.BufferedWriter;
         import java.io.File;
         import java.io.FileWriter;
         import java.io.IOException;
         import java.io.PrintWriter;
         import java.text.SimpleDateFormat;
         import java.util.ArrayList;
         import java.util.Calendar;
         import java.util.GregorianCalendar;
         import java.util.List;
         import java.util.Locale;
         import java.util.StringTokenizer;
         import l1j.server.Config;
         import l1j.server.server.model.Instance.L1ItemInstance;
         import l1j.server.server.model.Instance.L1PcInstance;

         public class FileLogger
           implements Logger
         {
           private static String date = "";



           private ArrayList<String> _chatlog = new ArrayList<>(1024);
           private ArrayList<String> _commandlog = new ArrayList<>(512);
           private ArrayList<String> _connectionlog = new ArrayList<>(1024);
           private ArrayList<String> _enchantlog = new ArrayList<>(1024);
           private ArrayList<String> _tradelog = new ArrayList<>(512);
           private ArrayList<String> _warehouselog = new ArrayList<>(512);
           private ArrayList<String> _itemactionlog = new ArrayList<>(512);
           private ArrayList<String> _levellog = new ArrayList<>(512);
           private ArrayList<String> _error = new ArrayList<>(1024);
           private ArrayList<String> _cmd = new ArrayList<>(1024);
           private ArrayList<String> _shop = new ArrayList<>(1024);
           private ArrayList<String> _dollMake = new ArrayList<>(1024);
           private ArrayList<String> _blessOfAinPointCard = new ArrayList<>(1024); private ArrayList<String> _smeltingMake;
           private ArrayList<String> _dollPotential = new ArrayList<>(1024);
           private ArrayList<String> _craft_log = new ArrayList<>(1024);


           public void addBlessOfAinPointCard(String s) {
             String msg = String.format("[%s] " + s + "\r\n", new Object[] { getLocalTime() });
             synchronized (this._blessOfAinPointCard) {
               this._blessOfAinPointCard.add(msg);
             }
           }

           public void addDollPotencial(String s) {
             String msg = String.format("[%s] " + s + "\r\n", new Object[] { getLocalTime() });
             synchronized (this._dollPotential) {
               this._dollPotential.add(msg);
             }
           }

           public void addCmd(String s) {
             synchronized (this._cmd) {
               this._cmd.add(s);
             }
           }

           public void addDollMake(String s) {
             synchronized (this._dollMake) {
               this._dollMake.add(s);
             }
           }
           public void addSmeltingMake(String s) {
             synchronized (this._smeltingMake) {
               this._smeltingMake.add(s);
             }
           }


           public void addcraftMake(String s) {
             synchronized (this._craft_log) {
               this._craft_log.add(s);
             }
           }

           public void addDollMake(L1PcInstance pc, L1ItemInstance item, boolean success) {
             String msg = String.format("[%s][帳號]:%s[角色名稱]:%s[是否]:%s[物件/物品名稱]:%s\r\n", new Object[] { getLocalTime(), pc.getAccountName(), pc.getName(), success ? "成功" : "失敗", getFormatItemName(item, 1) });
             addDollMake(msg);
           }

           public void addSmeltingMake(L1PcInstance pc, L1ItemInstance item, boolean success) {
             String msg = String.format("[%s][帳號]:%s[角色名稱]:%s[是否]:%s[物件/物品名稱]:%s\r\n", new Object[] { getLocalTime(), pc.getAccountName(), pc.getName(), success ? "成功" : "失敗", getFormatItemName(item, 1) });
             System.out.println(msg);
             addSmeltingMake(msg);
           }

           public void addShop(String itemName, int count, long price, String npc, String pc) {
             addShop(String.format("[%s][NPC購買]:%s[角色名稱]:%s[物品]:%s[數量]:%d[價格]:%d\r\n", new Object[] { getLocalTime(), npc, pc, itemName, Integer.valueOf(count), Long.valueOf(price) }));
           }

           public void addShopSell(String itemName, int count, long price, String npc, String pc) {
             addShop(String.format("[%s][NPC銷售]:%s[角色名稱]:%s[物品]:%s[數量]:%d[價格]:%d\r\n", new Object[] { getLocalTime(), npc, pc, itemName, Integer.valueOf(count), Long.valueOf(price) }));
           }

           public void addcraftMake(L1PcInstance pc, int desc, String string, int count) {
             String msg = String.format("[%s][帳號]:%s[角色名稱]:%s[是否]:%s[輸出描述]:$%s[數量(僅記錄一個)]:%d\r\n", new Object[] { getLocalTime(), pc.getAccountName(), pc.getName(), string, Integer.valueOf(desc), Integer.valueOf(count) });
             addcraftMake(msg);
           }

           public void addShop(String s) {
             synchronized (this._shop) {
               this._shop.add(s);
             }
             if (Config.Synchronization.Operation_Manager)
               MJUIAdapter.on_shop_append(s);
           }

           public void addChat(Logger.LoggerChatType type, L1PcInstance pc, String msg) {
             String log = "";

             switch (type) {
               case Pickup:
                 log = String.format("%s\t[血盟]:%s\t[%s]\t%s\r\n", new Object[] { getLocalTime(), pc.getClanname(), pc.getName(), msg });
                 if (Config.Synchronization.Operation_Manager) {
                   MJUIAdapter.on_chat_pledge_append(log);
                 }
                 break;
               case DeathDrop:
                 log = String.format("%s\t全部\t[%s]\t%s\r\n", new Object[] { getLocalTime(), pc.getName(), msg });
                 if (Config.Synchronization.Operation_Manager) {
                   MJUIAdapter.on_chat_world_append(log);
                 }
                 break;
               case Drop:
                 log = String.format("%s\t一般\t[%s]\t%s\r\n", new Object[] { getLocalTime(), pc.getName(), msg });
                 if (Config.Synchronization.Operation_Manager) {
                   MJUIAdapter.on_chat_normal_append(log);
                 }
                 break;
               case Delete:
                 log = String.format("%s\t聯盟\t[%s]\t%s\r\n", new Object[] { getLocalTime(), pc.getName(), msg });
                 if (Config.Synchronization.Operation_Manager) {
                   MJUIAdapter.on_chat_pledge_append(log);
                 }
                 break;
               case del:
                 log = String.format("%s\t守護騎士\t[%s]\t%s\r\n", new Object[] { getLocalTime(), pc.getName(), msg });
                 if (Config.Synchronization.Operation_Manager) {
                   MJUIAdapter.on_chat_pledge_append(log);
                 }
                 break;
               case null:
                 log = String.format("%s\t隊伍\t[%s]\t%s\r\n", new Object[] { getLocalTime(), pc.getName(), msg });
                 if (Config.Synchronization.Operation_Manager) {
                   MJUIAdapter.on_chat_party_append(log);
                 }
                 break;
               case null:
                 log = String.format("%s\t整體\t[%s]\t%s\r\n", new Object[] { getLocalTime(), pc.getName(), msg });
                 if (Config.Synchronization.Operation_Manager) {
                   MJUIAdapter.on_chat_party_append(log);
                 }
                 break;
               case null:
                 log = String.format("%s\t大喊\t[%s]\t%s\r\n", new Object[] { getLocalTime(), pc.getName(), msg });
                 if (Config.Synchronization.Operation_Manager)
                   MJUIAdapter.on_chat_normal_append(log);
                 break;
             }
             synchronized (this._warehouselog) {
               this._chatlog.add(log);
             }
           }


           public void addWhisper(L1PcInstance pcfrom, L1PcInstance pcto, String msg) {
             String log = String.format("%s\t悄悄話\t[%s] -> [%s]\t%s\r\n", new Object[] { getLocalTime(), pcfrom.getName(), pcto.getName(), msg });
             if (Config.Synchronization.Operation_Manager)
               MJUIAdapter.on_chat_whisper_append(log);
             synchronized (this._chatlog) {
               this._chatlog.add(log);
             }
           }

           public void addCommand(String msg) {
             msg = String.format("%s\t%s\r\n", new Object[] { getLocalTime(), msg });
             if (Config.Synchronization.Operation_Manager)
               MJUIAdapter.on_gm_command(msg);
             synchronized (this._commandlog) {
               this._commandlog.add(msg);
             }
           }

           public void addConnection(String msg) {
             msg = String.format("%s\t%s\r\n", new Object[] { getLocalTime(), msg });
             synchronized (this._connectionlog) {
               this._connectionlog.add(msg);
             }
           }

           public void addError(String msg) {
             msg = String.format("%s\t%s\r\n", new Object[] { getLocalTime(), msg });
             synchronized (this._error) {
               this._error.add(msg);
             }
           }


           public void addEnchant(L1PcInstance pc, L1ItemInstance item, boolean success) {
             String msg = String.format("%s\t%s:[%s]\t%s\t%s\r\n", new Object[] { getLocalTime(), pc.getAccountName(), pc.getName(), success ? "成功" : "失敗", getFormatItemName(item, 1) });
             if (Config.Synchronization.Operation_Manager)
               MJUIAdapter.on_enchant_append(msg);
             synchronized (this._enchantlog) {
               this._enchantlog.add(msg);
             }
           }

           public void addEnchant(L1PcInstance pc, L1ItemInstance enchanter, L1ItemInstance enchantee, boolean is_success) {
             String msg = String.format("%s\t%s:[%s]\t%s\t%s\t%s->%s\r\n", new Object[] { getLocalTime(), pc.getAccountName(), pc.getName(), is_success ? "成功" : "失敗", enchanter.getName(), enchantee.getName() });
             if (Config.Synchronization.Operation_Manager)
               MJUIAdapter.on_enchant_append(msg);
             synchronized (this._enchantlog) {
               this._enchantlog.add(msg);
             }
           }



           public void addTrade(boolean success, L1PcInstance pcfrom, L1PcInstance pcto, L1ItemInstance item, int count) {
             if (pcfrom == null || pcto == null || item == null) {
               return;
             }
             String msg = String.format("%s\t%s\t%s:%s\t%s\t%s:[%s]\r\n", new Object[] { getLocalTime(), success ? "OO完成OO" : "XX取消XX", pcfrom.getAccountName(), "[" + pcfrom.getName() + "]",
                   getFormatItemName(item, count), pcto.getAccountName(), pcto.getName() });
             if (Config.Synchronization.Operation_Manager)
               MJUIAdapter.on_trade_append(msg);
             synchronized (this._tradelog) {
               this._tradelog.add(msg);
             }
           }



           public void 개인상점구매(boolean success, L1PcInstance pcfrom, L1PcInstance pcto, L1ItemInstance item, int count) {
             String msg = String.format("%s\t%s\t%s:%s\t%s\t%s:[%s]\r\n", new Object[] { getLocalTime(), success ? "商店購買" : "商店取消", pcfrom.getAccountName(), "[" + pcfrom.getName() + "]", getFormatItemName(item, count), pcto
                   .getAccountName(), pcto.getName() });
             if (Config.Synchronization.Operation_Manager)
               MJUIAdapter.on_trade_append(msg);
             synchronized (this._tradelog) {
               this._tradelog.add(msg);
             }
           }

           public void addWarehouse(Logger.WarehouseType type, boolean put, L1PcInstance pc, L1ItemInstance item, int count) {
             String msg = "";


             switch (type) {
               case Pickup:
                 msg = String.format("[%s][實行]:%s[帳號名稱]:%s[角色名稱]:%s[物件/物品名稱/數量]:%s\r\n", new Object[] { getLocalTime(), put ? "寄存":"取出", pc.getAccountName(), pc.getName(), getFormatItemName(item, count) });
                 break;
               case DeathDrop:
                 msg = String.format("[%s][血盟名稱]:%s[實行]:%s[帳號名稱]:%s[角色名稱]:%s[物件/物品名稱/數量]:%s\r\n", new Object[] { getLocalTime(), pc.getClanname(), put ? "寄存":"取出", pc.getAccountName(), pc.getName(), getFormatItemName(item, count) });
                 break;
               case Drop:
                 msg = String.format("%s\t套裝:%s\t%s:[%s]\t%s\r\n", new Object[] { getLocalTime(), put ? "寄存":"取出", pc.getAccountName(), pc.getName(), getFormatItemName(item, count) });
                 break;
               case Delete:
                 msg = String.format("%s\t妖精:%s\t%s:[%s]\t%s\r\n", new Object[] { getLocalTime(), put ? "寄存":"取出", pc.getAccountName(), pc.getName(), getFormatItemName(item, count) });
                 break;
             }

             if (Config.Synchronization.Operation_Manager)
               MJUIAdapter.on_warehouse_append(msg);
             synchronized (this._warehouselog) {
               this._warehouselog.add(msg);
             }
           }

           public void addItemAction(Logger.ItemActionType type, L1PcInstance pc, L1ItemInstance item, int count) {
             String msg = "";
             switch (type) {
               case Pickup:
                 msg = String.format("[%s][實行]:獲得[帳號名稱]:%s[角色名稱]:%s[物件/物品名稱/數量]:%s\r\n", new Object[] { getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count) });
                 break;



                 switch (eventType) {
                     case DeathDrop:
                         // 記錄死亡掉落事件
                         msg = String.format("[%s][實行]:死亡掉落[帳號名稱]:%s[角色名稱]:%s[物件/物品名稱/數量]:%s\r\n",
                                 getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
                         break;
                     case Drop:
                         // 記錄掉落事件
                         msg = String.format("[%s][實行]:掉落[帳號名稱]:%s[角色名稱]:%s[物件/物品名稱/數量]:%s\r\n",
                                 getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
                         break;
                     case Delete:
                         // 記錄刪除事件
                         msg = String.format("[%s][實行]:刪除[帳號名稱]:%s[角色名稱]:%s[物件/物品名稱/數量]:%s\r\n",
                                 getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
                         break;
                     case del:
                         // 記錄消失事件
                         msg = String.format("[%s][實行]:消失[帳號名稱]:%s[角色名稱]:%s[物件/物品名稱/數量]:%s\r\n",

                                 getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
                         break;
                 }
             if (Config.Synchronization.Operation_Manager)
               MJUIAdapter.on_item_append(msg);
             synchronized (this._itemactionlog) {
               this._itemactionlog.add(msg);
             }
           }


           public void addLevel(L1PcInstance pc, int level) {
             String msg = "";

             msg = String.format("%s\t%s:%s\tLevelUp %d\r\n", new Object[] { getLocalTime(), pc.getAccountName(), "[" + pc.getName() + "]", Integer.valueOf(level) });
             synchronized (this._levellog) {
               this._levellog.add(msg);
             }
           }

           public void addAll(String msg) {
             msg = String.format("%s\t%s\r\n", new Object[] { getLocalTime(), msg });

             synchronized (this._chatlog) {
               this._chatlog.add(msg);
             }

             synchronized (this._commandlog) {
               this._commandlog.add(msg);
             }

             synchronized (this._connectionlog) {
               this._connectionlog.add(msg);
             }

             synchronized (this._error) {
               this._error.add(msg);
             }

             synchronized (this._enchantlog) {
               this._enchantlog.add(msg);
             }

             synchronized (this._tradelog) {
               this._tradelog.add(msg);
             }

             synchronized (this._warehouselog) {
               this._warehouselog.add(msg);
             }

             synchronized (this._itemactionlog) {
               this._itemactionlog.add(msg);
             }

             synchronized (this._levellog) {
               this._levellog.add(msg);
             }
           }

           public void flush() throws IOException {
             synchronized (this._chatlog) {
               if (!this._chatlog.isEmpty()) {
                 writeLog(this._chatlog, "聊天.txt");
                 this._chatlog.clear();
               }
             }

             synchronized (this._commandlog) {
               if (!this._commandlog.isEmpty()) {
                 writeLog(this._commandlog, "命令.txt");
                 this._commandlog.clear();
               }
             }

             synchronized (this._connectionlog) {
               if (!this._connectionlog.isEmpty()) {
                 writeLog(this._connectionlog, "登入.txt");
                 this._connectionlog.clear();
               }
             }

             synchronized (this._error) {
               if (!this._error.isEmpty()) {
                 writeLog(this._error, "錯誤日誌.txt");
                 this._error.clear();
               }
             }

             synchronized (this._enchantlog) {
               if (!this._enchantlog.isEmpty()) {
                 writeLog(this._enchantlog, "強化附魔.txt");
                 this._enchantlog.clear();
               }
             }

             synchronized (this._tradelog) {
               if (!this._tradelog.isEmpty()) {
                 writeLog(this._tradelog, "交換、市場.txt");
                 this._tradelog.clear();
               }
             }

             synchronized (this._warehouselog) {
               if (!this._warehouselog.isEmpty()) {
                 writeLog(this._warehouselog, "倉庫.txt");
                 this._warehouselog.clear();
               }
             }

             synchronized (this._itemactionlog) {
               if (!this._itemactionlog.isEmpty()) {
                 writeLog(this._itemactionlog, "專案日誌.txt");
                 this._itemactionlog.clear();
               }
             }

             synchronized (this._levellog) {
               if (!this._levellog.isEmpty()) {
                 writeLog(this._levellog, "升級.txt");
                 this._levellog.clear();
               }
             }

             synchronized (this._cmd) {
               if (!this._cmd.isEmpty()) {
                 writeLog(this._cmd, "CMD.txt");
                 this._cmd.clear();
               }
             }

             synchronized (this._shop) {
               if (!this._shop.isEmpty()) {
                 writeLog(this._shop, "NPC商店.txt");
                 this._shop.clear();
               }
             }

             synchronized (this._dollMake) {
               if (!this._dollMake.isEmpty()) {
                 writeLog(this._dollMake, "魔法娃娃合成.txt");
                 this._dollMake.clear();
               }
             }

             synchronized (this._blessOfAinPointCard) {
               if (!this._blessOfAinPointCard.isEmpty()) {
                 writeLog(this._blessOfAinPointCard, "艾因哈薩德點數.txt");
                 this._blessOfAinPointCard.clear();
               }
             }

             synchronized (this._dollPotential) {
               if (!this._dollPotential.isEmpty()) {
                 writeLog(this._dollPotential, "魔法娃娃潛力.txt");
                 this._dollPotential.clear();
               }
             }

             synchronized (this._dollPotential) {
               if (!this._dollPotential.isEmpty()) {
                 writeLog(this._dollPotential, "魔法娃娃潛力.txt");
                 this._dollPotential.clear();
               }
             }

             synchronized (this._craft_log) {
               if (!this._craft_log.isEmpty()) {
                 writeLog(this._craft_log, "製作.txt");
                 this._craft_log.clear();
               }
             }

             if (Config.Synchronization.Operation_Manager) {
               MJUIAdapter.on_flush();
             }
           }

           private static String getDate() {
             SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh-ss", Locale.KOREA);
             return s.format(Calendar.getInstance().getTime());
           }


           public void writeLog(List<String> log, String filename) throws IOException {
             File f = null;
             String sTemp = "";
             sTemp = getDate();
             StringTokenizer s = new StringTokenizer(sTemp, " ");
             date = s.nextToken();
             f = new File("LogDB/" + date);
             if (!f.exists()) {
               f.mkdir();
             }

             BufferedWriter w = new BufferedWriter(new FileWriter("LogDB/" + date + "/" + filename, true));
             PrintWriter pw = new PrintWriter(w, true);
             for (int i = 0, n = log.size(); i < n; i++) {
               pw.print(log.get(i));
             }
             pw.close();
             pw = null;
             w.close();
             w = null;
             sTemp = null;
             date = null;
           }


           public String getLocalTime() {
             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             GregorianCalendar localtime = new GregorianCalendar();

             return formatter.format(localtime.getTime());
           }

           public String getFormatItemName(L1ItemInstance item, int count) {
             StringBuilder sb = new StringBuilder(256);
             sb.append("[").append(item.getId()).append("]");
             if (item.getEnchantLevel() > 0) {
               sb.append("+").append(item.getEnchantLevel());
             } else if (item.getEnchantLevel() < 0) {
               sb.append(item.getEnchantLevel());
             }  sb.append(item.getName());
             if (item.isStackable())
               sb.append("(").append(count).append(")");
             return sb.toString();
           }
         }


