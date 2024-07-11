 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Spawn;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.utils.NumberUtil;
 import l1j.server.server.utils.SQLUtil;



















 public class SpawnTable
 {
   private static Logger _log = Logger.getLogger(SpawnTable.class.getName());

   private static SpawnTable _instance;

   private Map<Integer, L1Spawn> _spawntable = new HashMap<>();

   private int _highestId;

   public static SpawnTable getInstance() {
     if (_instance == null) {
       _instance = new SpawnTable();
     }
     return _instance;
   }

   private SpawnTable() {
     fillSpawnTable();
   }

   public void reload() {
     SpawnTable oldInstance = _instance;
     _instance = new SpawnTable();
     oldInstance._spawntable.clear();
   }

   public void reload(int mpaid) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;



     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM spawnlist WHERE mapid =?");
       pstm.setInt(1, mpaid);
       rs = pstm.executeQuery();

       while (rs.next()) {
         L1Spawn spawnDat; int spawnid = rs.getInt("id");

         if (this._spawntable.containsKey(Integer.valueOf(spawnid))) {
           this._spawntable.remove(Integer.valueOf(spawnid));
         }
         int npcTemplateId = rs.getInt("npc_templateid");
         L1Npc template1 = NpcTable.getInstance().getTemplate(npcTemplateId);

         if (template1 == null) {
           _log.warning("mob data for id:" + npcTemplateId + " missing in npc table");
           System.out.println("mob data for id:" + npcTemplateId + " missing in npc table");
           spawnDat = null;
         } else {
           if (rs.getInt("count") == 0) {
             continue;
           }

           double amount_rate = MapsTable.getInstance().getMonsterAmount(rs.getShort("mapid"));
           int count = calcCount(template1, rs.getInt("count"), amount_rate);

           if (count == 0) {
             continue;
           }

           spawnDat = new L1Spawn(template1);
           spawnDat.setId(rs.getInt("id"));
           spawnDat.setAmount(count);
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
           spawnDat.setMinRespawnDelay(rs.getInt("min_respawn_delay"));
           spawnDat.setMaxRespawnDelay(rs.getInt("max_respawn_delay"));
           spawnDat.setMapId(rs.getShort("mapid"));
           spawnDat.setRespawnScreen(rs.getBoolean("respawn_screen"));
           spawnDat.setMovementDistance(rs.getInt("movement_distance"));
           spawnDat.setRest(rs.getBoolean("rest"));
           spawnDat.setSpawnType(rs.getInt("near_spawn"));
           spawnDat.setName(template1.get_name());

           if (count > 1 && spawnDat.getLocX1() == 0) {


             int range = Math.min(count * 6, 30);
             spawnDat.setLocX1(spawnDat.getLocX() - range);
             spawnDat.setLocY1(spawnDat.getLocY() - range);
             spawnDat.setLocX2(spawnDat.getLocX() + range);
             spawnDat.setLocY2(spawnDat.getLocY() + range);
           }





           spawnDat.init();
         }


         this._spawntable.put(new Integer(spawnDat.getId()), spawnDat);
         if (spawnDat.getId() > this._highestId) {
           this._highestId = spawnDat.getId();
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


   private void fillSpawnTable() {
     int spawnCount = 0;
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM spawnlist");
       rs = pstm.executeQuery();



       while (rs.next()) {
         L1Spawn spawnDat;
         int npcTemplateId = rs.getInt("npc_templateid");

         L1Npc template1 = NpcTable.getInstance().getTemplate(npcTemplateId);


         if (template1 == null) {
           _log.warning("mob data for id:" + npcTemplateId + " missing in npc table");
           System.out.println("mob data for id:" + npcTemplateId + " missing in npc table");
           spawnDat = null;
         } else {
           if (rs.getInt("count") == 0) {
             continue;
           }
           double amount_rate = MapsTable.getInstance().getMonsterAmount(rs.getShort("mapid"));
           int count = calcCount(template1, rs.getInt("count"), amount_rate);
           if (count == 0) {
             continue;
           }

           spawnDat = new L1Spawn(template1);
           spawnDat.setId(rs.getInt("id"));
           spawnDat.setAmount(count);
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
           spawnDat.setMinRespawnDelay(rs.getInt("min_respawn_delay"));
           spawnDat.setMaxRespawnDelay(rs.getInt("max_respawn_delay"));
           spawnDat.setMapId(rs.getShort("mapid"));
           spawnDat.setRespawnScreen(rs.getBoolean("respawn_screen"));
           spawnDat.setMovementDistance(rs.getInt("movement_distance"));
           spawnDat.setRest(rs.getBoolean("rest"));
           spawnDat.setSpawnType(rs.getInt("near_spawn"));
           spawnDat.setName(template1.get_name());

           if (count > 1 && spawnDat.getLocX1() == 0) {


             int range = Math.min(count * 6, 30);
             spawnDat.setLocX1(spawnDat.getLocX() - range);
             spawnDat.setLocY1(spawnDat.getLocY() - range);
             spawnDat.setLocX2(spawnDat.getLocX() + range);
             spawnDat.setLocY2(spawnDat.getLocY() + range);
           }


           spawnDat.init();
           spawnCount += spawnDat.getAmount();
         }

         this._spawntable.put(new Integer(spawnDat.getId()), spawnDat);
         if (spawnDat.getId() > this._highestId) {
           this._highestId = spawnDat.getId();
         }
       }

     } catch (Exception e) {
         e.printStackTrace(); // 註解: 打印異常堆棧信息
     } finally {
         SQLUtil.close(rs); // 註解: 關閉結果集 (ResultSet)
         SQLUtil.close(pstm); // 註解: 關閉預處理語句 (PreparedStatement)
         SQLUtil.close(con); // 註解: 關閉數據庫連接 (Connection)
     }
       _log.fine("總共 " + spawnCount + "隻怪物");
// 註解: 總共 " + spawnCount + " 隻怪物
   }

   public L1Spawn getTemplate(int Id) {
     return this._spawntable.get(new Integer(Id));
   }


   public void delspawntable(int Id) {
     if (this._spawntable.containsKey(Integer.valueOf(Id))) {
       this._spawntable.remove(Integer.valueOf(Id));
     }
   }

   public void addNewSpawn(L1Spawn spawn) {
     this._highestId++;
     spawn.setId(this._highestId);
     this._spawntable.put(new Integer(spawn.getId()), spawn);
   }

   public static void storeSpawn(L1PcInstance pc, L1Npc npc) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       int count = 1;
       int randomXY = 12;
       int minRespawnDelay = 60;
       int maxRespawnDelay = 120;
       String note = npc.get_name();

       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("INSERT INTO spawnlist SET location=?,count=?,npc_templateid=?,group_id=?,locx=?,locy=?,randomx=?,randomy=?,heading=?,min_respawn_delay=?,max_respawn_delay=?,mapid=?");
       pstm.setString(1, note);
       pstm.setInt(2, count);
       pstm.setInt(3, npc.get_npcId());
       pstm.setInt(4, 0);
       pstm.setInt(5, pc.getX());
       pstm.setInt(6, pc.getY());
       pstm.setInt(7, randomXY);
       pstm.setInt(8, randomXY);
       pstm.setInt(9, pc.getHeading());
       pstm.setInt(10, minRespawnDelay);
       pstm.setInt(11, maxRespawnDelay);
       pstm.setInt(12, pc.getMapId());
       pstm.execute();
     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   private static int calcCount(L1Npc npc, int count, double rate) {
     if (rate == 0.0D) {
       return 0;
     }
     if (rate == 1.0D || npc.isAmountFixed()) {
       return count;
     }
     return NumberUtil.randomRound(count * rate);
   }
 }


