 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.utils.SQLUtil;




















 public class NPCTalkDataTable
 {
   private static Logger _log = Logger.getLogger(NPCTalkDataTable.class
       .getName());

   private static NPCTalkDataTable _instance;

   private HashMap<Integer, L1NpcTalkData> _datatable = new HashMap<>();

   public static NPCTalkDataTable getInstance() {
     if (_instance == null) {
       _instance = new NPCTalkDataTable();
     }
     return _instance;
   }

   public static void reload() {
     NPCTalkDataTable oldInstance = _instance;
     _instance = new NPCTalkDataTable();
     oldInstance._datatable.clear();
   }

   private NPCTalkDataTable() {
     parseList();
   }

   private void parseList() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM npcaction");

       rs = pstm.executeQuery();
       L1NpcTalkData l1npctalkdata = null;
       while (rs.next()) {
         l1npctalkdata = new L1NpcTalkData();
         l1npctalkdata.setNpcID(rs.getInt(1));
         l1npctalkdata.setNormalAction(rs.getString(2));
         l1npctalkdata.setCaoticAction(rs.getString(3));
         l1npctalkdata.setTeleportURL(rs.getString(4));
         l1npctalkdata.setTeleportURLA(rs.getString(5));
         this._datatable.put(new Integer(l1npctalkdata.getNpcID()), l1npctalkdata);
       }

         _log.config("加載 NPC 動作列表 " + this._datatable.size() + "個 NPC");
// 註解: 加載 NPC 動作列表，共加載了 this._datatable.size() 個 NPC

     } catch (Exception e) {
         _log.warning("創建 NPC 動作表時發生錯誤 " + e);
         // 註解: 創建 NPC 動作表時發生錯誤 " + e

         System.out.println("創建 NPC 動作表時發生錯誤 " + e);
         // 註解: 創建 NPC 動作表時發生錯誤 " + e
     } finally {
         SQLUtil.close(rs);
         // 註解: 關閉結果集 (ResultSet)

         SQLUtil.close(pstm);
         // 註解: 關閉預處理語句 (PreparedStatement)

         SQLUtil.close(con);
         // 註解: 關閉數據庫連接 (Connection)
     }
   }

     public L1NpcTalkData getTemplate(int i) {
         return this._datatable.get(new Integer(i));
         // 註解: 根據給定的 ID 返回對應的 NPC 對話數據
     }
 }


