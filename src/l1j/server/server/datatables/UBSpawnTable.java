 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.L1UbPattern;
 import l1j.server.server.model.L1UbSpawn;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.utils.SQLUtil;


















 public class UBSpawnTable
 {
   private static Logger _log = Logger.getLogger(UBSpawnTable.class.getName());

   private static UBSpawnTable _instance;

   private HashMap<Integer, L1UbSpawn> _spawnTable = new HashMap<>();

   public static UBSpawnTable getInstance() {
     if (_instance == null) {
       _instance = new UBSpawnTable();
     }
     return _instance;
   }

   private UBSpawnTable() {
     loadSpawnTable();
   }


   private void loadSpawnTable() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM spawnlist_ub");
       rs = pstm.executeQuery();
       L1Npc npcTemp = null;
       L1UbSpawn spawnDat = null;
       while (rs.next()) {

         npcTemp = NpcTable.getInstance().getTemplate(rs.getInt(6));
         if (npcTemp == null) {
           continue;
         }

         spawnDat = new L1UbSpawn();
         spawnDat.setId(rs.getInt(1));
         spawnDat.setUbId(rs.getInt(2));
         spawnDat.setPattern(rs.getInt(3));
         spawnDat.setGroup(rs.getInt(4));
         spawnDat.setName(npcTemp.get_name());
         spawnDat.setNpcTemplateId(rs.getInt(6));
         spawnDat.setAmount(rs.getInt(7));
         spawnDat.setSpawnDelay(rs.getInt(8));
         spawnDat.setSealCount(rs.getInt(9));

         this._spawnTable.put(Integer.valueOf(spawnDat.getId()), spawnDat);
       }
     } catch (Exception e) {

         _log.warning("警告: 無法初始化生成::" + e);
            // 註解: 警告: 無法初始化生成: " + e
         System.out.println("輸出到控制台:: 無法初始化生成::" + e);
            // 註解: 輸出到控制台: 無法初始化生成: " + e
     } finally {
         SQLUtil.close(rs); // 註解: 關閉結果集 (ResultSet)
         SQLUtil.close(pstm); // 註解: 關閉預處理語句 (PreparedStatement)
         SQLUtil.close(con); // 註解: 關閉數據庫連接 (Connection)
     }
       _log.config("UB 怪物配置列表 " + this._spawnTable.size() + "條加載完成");
            // 註解: UB 怪物配置列表 " + this._spawnTable.size() + " 條加載完成
   }

   public L1UbSpawn getSpawn(int spawnId) {
     return this._spawnTable.get(Integer.valueOf(spawnId));
   }

   public int getMaxPattern(int ubId) {
     int n = 0;
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       pstm = con.prepareStatement("SELECT MAX(pattern) FROM spawnlist_ub WHERE ub_id=?");
       pstm.setInt(1, ubId);
       rs = pstm.executeQuery();
       if (rs.next()) {
         n = rs.getInt(1);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return n;
   }

   public L1UbPattern getPattern(int ubId, int patternNumer) {
     L1UbPattern pattern = new L1UbPattern();
     for (L1UbSpawn spawn : this._spawnTable.values()) {
       if (spawn.getUbId() == ubId && spawn.getPattern() == patternNumer) {
         pattern.addSpawn(spawn.getGroup(), spawn);
       }
     }
     pattern.freeze();

     return pattern;
   }
 }


