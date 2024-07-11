 package l1j.server.server.model;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.utils.SQLUtil;

 public class Beginner
 {
   private static Logger _log = Logger.getLogger(Beginner.class.getName());

   private static Beginner _instance;

   private static int _weapont_count = 0;

   public static Beginner getInstance() {
     if (_instance == null) {
       _instance = new Beginner();
     }
     return _instance;
   }




   public static void reload() {
     Beginner oldInstance = _instance;
     _instance = new Beginner();
     if (oldInstance != null);
   }

   public int GiveItemToActivePc(final L1PcInstance pc) {
     Selector.exec("select * from beginner where activate=전체 or activate=?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setString(1, Beginner.classid_to_db_name(pc.getClassId()));
           }

           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               int itemid = rs.getInt("item_id");
               int count = rs.getInt("count");
               int enchant = rs.getInt("enchantlvl");
               L1Item temp = ItemTable.getInstance().getTemplate(itemid);
               if (temp != null &&
                 !temp.isStackable()) {
                 L1ItemInstance item = null;
                 int createCount;
                 for (createCount = 0; createCount < count; ) {
                   item = ItemTable.getInstance().createItem(itemid);
                   item.setEnchantLevel(enchant);
                   if (pc.getInventory().checkAddItem(item, 1) == 0) {
                     pc.getInventory().storeItem(item);

                     createCount++;
                   }
                 }
                 if (createCount > 0) {
                   pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item
                         .getLogName() + "(ID:" + itemid + ")"));
                 }
               }
             }
           }
         });

     return 0;
   }



   public void writeBookmark(L1PcInstance pc) {
     Connection c = null;
     PreparedStatement p = null;
     PreparedStatement p1 = null;
     ResultSet r = null;

     try {
       c = L1DatabaseFactory.getInstance().getConnection();
       p = c.prepareStatement("SELECT * FROM beginner_teleport");

       r = p.executeQuery();
       while (r.next()) {
         p1 = c.prepareStatement("INSERT INTO character_teleport SET id = ?, char_id = ?, name = ?, locx = ?, locy = ?, mapid = ?, speed =?, num=?");
         p1.setInt(1, IdFactory.getInstance().nextId());
         p1.setInt(2, pc.getId());
         p1.setString(3, r.getString("name"));
         p1.setInt(4, r.getInt("locx"));
         p1.setInt(5, r.getInt("locy"));
         p1.setShort(6, r.getShort("mapid"));
         p1.setInt(7, -1);
         p1.setInt(8, 0);

            // 執行PreparedStatement
           p1.execute();
           SQLUtil.close(p1); // 關閉PreparedStatement
       }
     } catch (Exception e) {
            // 記錄嚴重等級的日志，並附帶異常信息
         _log.log(Level.SEVERE, "添加書籤時發生錯誤。", e);
     } finally {
            // 無論是否發生異常，都關閉資源
         SQLUtil.close(r); // 關閉ResultSet
         SQLUtil.close(p1); // 關閉PreparedStatement
         SQLUtil.close(p); // 關閉PreparedStatement
         SQLUtil.close(c); // 關閉Connection
     }
   }
     private static String classid_to_db_name(int class_id) {
         switch (class_id) {
             case 0:
             case 1:
                 return "王族"; // 군주
             case 48:
             case 20553:
                 return "騎士"; // 기사
             case 37:
             case 138:
                 return "妖精"; // 요정
             case 20278:
             case 20279:
                 return "魔法師"; // 마법사
             case 2786:
             case 2796:
                 return "黑暗妖精"; // 다크엘프
             case 6658:
             case 6661:
                 return "龍騎士"; // 용기사
             case 6650:
             case 6671:
                 return "幻術師"; // 환술사
             case 20567:
             case 20577:
                 return "戰士"; // 전사
             case 18499:
             case 18520:
                 return "劍士"; // 검사
             case 19296:
             case 19299:
                 return "黃金槍騎"; // 창기사
         }
         return "全部"; // 전체
     }

     public int GiveItem(final L1PcInstance pc) {
         _weapont_count = 0; // 初始化武器計數
         Selector.exec("select * from beginner where activate='職業' or activate=?", new SelectorHandler() {
             // 準備SQL語句
             public void handle(PreparedStatement pstm) throws Exception {
                 pstm.setString(1, Beginner.classid_to_db_name(pc.getClassId())); // 設置玩家職業對應的名稱
             }

             // 處理查詢結果
             public void result(ResultSet rs) throws Exception {
                 while (rs.next()) {
                     final int count = rs.getInt("count");
                     if (count <= 0) {
                         continue; // 如果數量小於等於0，跳過
                     }
                     final int item_id = rs.getInt("item_id");

                     L1Item tem = ItemTable.getInstance().getTemplate(item_id);
                     if (tem == null) {
                         System.out.println("嘗試發放不存在於DB中的物品。 [ID: " + item_id + "]");
                         continue; // 如果物品模板不存在，跳過
                     }

                            // 生成物品實例並設置屬性
                     L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
                     if (item != null) {
                         item.setCount(count);
                         pc.getInventory().storeItem(item); // 將物品存入玩家背包
                         _weapont_count++; // 增加武器計數
                     }
                 }
             }
         });
         return _weapont_count; // 返回發放的武器數量
     }
               final String item_name = rs.getString("item_name");
               final int enchant_level = rs.getInt("enchantlvl");
               final int charge_count = rs.getInt("charge_count");
               final int bless = rs.getInt("bless");
               Updator.exec("INSERT INTO character_items SET id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, special_enchant = 0", new Handler()
                   {
                     public void handle(PreparedStatement pstm) throws Exception
                     {
                       int idx = 0;
                       pstm.setInt(++idx, IdFactory.getInstance().nextId());
                       pstm.setInt(++idx, item_id);
                       pstm.setInt(++idx, pc.getId());
                       pstm.setString(++idx, item_name);
                       pstm.setInt(++idx, count);
                       if ((item_id >= 22300 && item_id <= 22312) || item_id == 22337 || item_id == 203012 || item_id == 22073 || item_id == 22339 || item_id == 22338 || item_id == 48 || item_id == 120 || item_id == 147 || item_id == 156 || item_id == 174 || item_id == 175 || item_id == 35 || item_id == 505013 || item_id == 7000222) {




                         if (item_id == 203012 || item_id == 48 || item_id == 120 || item_id == 147 || item_id == 156 || item_id == 174 || item_id == 175 || item_id == 35 || item_id == 505013 || item_id == 7000222) {



                           if (Beginner._weapont_count == 1) {
                             pstm.setInt(++idx, 0);
                           } else {
                             pstm.setInt(++idx, 1);
                             Beginner._weapont_count++;
                           }
                         } else {
                           pstm.setInt(++idx, 1);
                         }
                       } else {
                         pstm.setInt(++idx, 0);
                       }
                       pstm.setInt(++idx, enchant_level);
                       pstm.setInt(++idx, 1);
                       pstm.setInt(++idx, 0);
                       pstm.setInt(++idx, charge_count);
                       pstm.setInt(++idx, 0);
                       pstm.setTimestamp(++idx, null);
                       pstm.setInt(++idx, bless);
                       pstm.setInt(++idx, 0);
                     }
                   });
             }
           }
         });
     return 0;
   }
 }


