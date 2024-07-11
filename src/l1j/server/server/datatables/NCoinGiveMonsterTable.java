 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Set;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1NCoinMonster;
 import l1j.server.server.utils.SQLUtil;



 public class NCoinGiveMonsterTable
 {
   public static NCoinGiveMonsterTable _instance;
   public Map<Integer, L1NCoinMonster> _list = new HashMap<>();

   public static NCoinGiveMonsterTable getInstance() {
     if (_instance == null) {
       _instance = new NCoinGiveMonsterTable();
     }
     return _instance;
   }

   public static void reload() {
     NCoinGiveMonsterTable oldInstance = _instance;
     _instance = new NCoinGiveMonsterTable();
     oldInstance._list.clear();
   }

   private NCoinGiveMonsterTable() {
     loadGiveMonster();
   }

     private void loadGiveMonster() {
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         try {
             // 從 L1DatabaseFactory 取得資料庫連接
             con = L1DatabaseFactory.getInstance().getConnection();

             // 預先準備 SQL 查詢語句
             pstm = con.prepareStatement("SELECT * FROM ncoin_give_monster");

             // 執行查詢並取得結果集
             rs = pstm.executeQuery();

             // 遍歷結果集中的每一行資料
             while (rs.next()) {
                 // 建立一個新的 L1NCoinMonster 物件
                 L1NCoinMonster CM = new L1NCoinMonster();

                 // 從結果集中取得資料並設置到 L1NCoinMonster 物件中
                 int npcid = rs.getInt("NPC ID"); // NPC ID
                 CM.setNpcName(rs.getString("NPC 名稱")); // NPC 名稱
                 CM.setNCoin(rs.getInt("NCoin 數量")); // 發放的 NCoin 數量
                 CM.setEffectNum(rs.getInt("效果編號")); // 效果編號
                 CM.setAllEffect((rs.getInt("顯示全部效果") == 1)); // 是否顯示全部效果
                 CM.setMent((rs.getInt("提示訊息") == 1)); // 是否顯示提示訊息
                 CM.setGiveItem((rs.getInt("發放物品") == 1)); // 是否發放物品
                 CM.setItemId(rs.getInt("物品編號")); // 物品編號
                 CM.setItemCount(rs.getInt("物品數量")); // 物品數量

                 // 將 NPC ID 和對應的 L1NCoinMonster 物件放入 _list 中
                 this._list.put(Integer.valueOf(npcid), CM);
             }
         } catch (Exception e) {
             // 捕獲並輸出異常
             e.printStackTrace();
         } finally {
             // 最後關閉結果集、準備語句和連接，釋放資源
             SQLUtil.close(rs, pstm, con);
         }
     }


     public L1NCoinMonster getNCoinGiveMonster(int npcid) {
     return this._list.get(Integer.valueOf(npcid));
   }

   public boolean isNCoinMonster(int npcid) {
     Set<Integer> keys = this._list.keySet();

     boolean OK = false;
     for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext(); ) {
       int givemon = ((Integer)iterator.next()).intValue();
       if (givemon == npcid) {
         OK = true;
         break;
       }
     }
     return OK;
   }
 }


