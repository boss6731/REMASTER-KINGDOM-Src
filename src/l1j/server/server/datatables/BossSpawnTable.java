 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashSet;
 import java.util.Set;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.L1BossSpawn;
 import l1j.server.server.templates.L1BossSpawnData;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.utils.SQLUtil;

 public class BossSpawnTable
 {
   private static Logger _log = Logger.getLogger(BossSpawnTable.class.getName());

   private static final Set<Integer> _bossIdSet = new HashSet<>();

   private static final Set<L1BossSpawnData> _data = new HashSet<>();




   public static boolean isBoss(int templateId) {
     return _bossIdSet.contains(Integer.valueOf(templateId));
   }

   public static L1BossSpawnData getSpawnData(int templateId) {
     for (L1BossSpawnData dd : _data) {
       if (dd.getBossSpawnId() == templateId) {
         return dd;
       }
     }
     return null;
   }

   public static void fillBossData() {
     Connection con = null; // 聲明數據庫連接
     PreparedStatement pstm = null; // 聲明預處理語句
     ResultSet rs = null; // 聲明結果集

     try {
       con = L1DatabaseFactory.getInstance().getConnection(); // 獲取數據庫連接
       pstm = con.prepareStatement("SELECT * FROM spawnlist_boss"); // 準備SQL查詢語句
       rs = pstm.executeQuery(); // 執行查詢並返回結果集

       while (rs.next()) { // 遍歷查詢結果
         int npcTemplateId = rs.getInt("npc_id"); // 獲取npc_id字段的值
         String name_map = rs.getString("map_name"); // 獲取map_name字段的值
         if (name_map == "" || name_map == null || name_map.equalsIgnoreCase("不知道")) { // 如果map_name為空或為空字符串或等於"모름"
           name_map = "未知區域"; // 將name_map設置為"알수 없는 지역"（翻譯：未知區域）
         }

// 在這裡可以繼續其他邏輯，例如將boss數據存儲到某個集合中
         L1BossSpawnData data = new L1BossSpawnData();
         data.setBossSpawnId(npcTemplateId);
         data.setBossSpawnMapName(name_map);
         _data.add(data);
       }
     } catch (Exception e) {
       e.getStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static void fillSpawnTable() {
     int spawnCount = 0;
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM spawnlist_boss");
       rs = pstm.executeQuery();


       while (rs.next()) { // 遍歷結果集
         int npcTemplateId = rs.getInt("npc_id"); // 獲取npc_id字段的值
         L1Npc template1 = NpcTable.getInstance().getTemplate(npcTemplateId); // 通過npc_id從NpcTable中獲取模板

         if (template1 == null) { // 如果模板為空，表示在NpcTable中找不到對應的npc數據
           _log.warning("mob data for id:" + npcTemplateId + " missing in npc table"); // 記錄警告日誌
           System.out.println("mob data for id:" + npcTemplateId + " missing in npc table"); // 輸出錯誤信息到控制台
           L1BossSpawn l1BossSpawn = null; // 初始化L1BossSpawn為空
           continue; // 跳過當前循環，進入下一次迭代
         }
// 在這裡可以繼續其他邏輯，例如創建並配置L1BossSpawn對象
         _bossIdSet.add(Integer.valueOf(npcTemplateId));

         L1BossSpawn spawnDat = new L1BossSpawn(template1);
         spawnDat.setId(rs.getInt("id"));
         spawnDat.setNpcid(npcTemplateId);
         spawnDat.setLocation(rs.getString("location"));
         spawnDat.setCycleType(rs.getString("cycle_type"));
         spawnDat.setAmount(rs.getInt("count"));
         spawnDat.setGroupId(rs.getInt("group_id"));
         spawnDat.setLocX(rs.getInt("locx"));
         spawnDat.setLocY(rs.getInt("locy"));
         spawnDat.setRandomx(rs.getInt("randomx"));
         spawnDat.setRandomy(rs.getInt("randomy"));
         spawnDat.setLocX1(rs.getInt("locx1"));
         spawnDat.setLocY1(rs.getInt("locy1"));
         spawnDat.setLocX2(rs.getInt("locx2"));
         spawnDat.setLocY2(rs.getInt("locy2"));
         spawnDat.setHeading(rs.getInt("heading"));
         spawnDat.setMapId(rs.getShort("mapid"));
         spawnDat.setRespawnScreen(rs.getBoolean("respawn_screen"));
         spawnDat.setMovementDistance(rs.getInt("movement_distance"));
         spawnDat.setRest(rs.getBoolean("rest"));
         spawnDat.setSpawnType(rs.getInt("spawn_type"));
         spawnDat.setPercentage(rs.getInt("percentage"));
         spawnDat.setName(template1.get_name());

         spawnDat.init();
         spawnCount += spawnDat.getAmount();
       }

     }
     catch (Exception e) {
         e.printStackTrace(); // 捕獲並打印異常堆棧跟蹤
     } finally {
         SQLUtil.close(rs); // 關閉結果集
         SQLUtil.close(pstm); // 關閉準備語句
         SQLUtil.close(con); // 關閉連接
     }
       _log.log(Level.FINE, "記錄總Boss怪物數量 " + spawnCount + "隻"); // 記錄總Boss怪物數量 " + spawnCount + "隻
   }
 }


