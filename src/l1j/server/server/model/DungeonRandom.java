 package l1j.server.server.model;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Random;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.utils.SQLUtil;


 public class DungeonRandom
 {
   private static Logger _log = Logger.getLogger(DungeonRandom.class.getName());

   private static DungeonRandom _instance = null;

   private static Map<String, NewDungeonRandom> _dungeonMap = new HashMap<>();
   private static Random _random = new Random(System.nanoTime());

   public static DungeonRandom getInstance() {
     if (_instance == null) {
       _instance = new DungeonRandom();
     }
     return _instance;
   }

   private DungeonRandom() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

       try {
           // 獲取數據庫連接
           con = L1DatabaseFactory.getInstance().getConnection();

           // 準備並執行SQL查詢
           pstm = con.prepareStatement("SELECT * FROM dungeon_random");
           rs = pstm.executeQuery();
           NewDungeonRandom newDungeonRandom = null;

           // 遍歷查詢結果
           while (rs.next()) {
               int srcMapId = rs.getInt("src_mapid");
               int srcX = rs.getInt("src_x");
               int srcY = rs.getInt("src_y");
               String key = srcMapId + srcX + srcY;

               // 初始化新地圖座標和ID
               int[] newX = new int[5];
               int[] newY = new int[5];
               short[] newMapId = new short[5];
               newX[0] = rs.getInt("new_x1");
               newY[0] = rs.getInt("new_y1");
               newMapId[0] = rs.getShort("new_mapid1");
               newX[1] = rs.getInt("new_x2");
               newY[1] = rs.getInt("new_y2");
               newMapId[1] = rs.getShort("new_mapid2");
               newX[2] = rs.getInt("new_x3");
               newY[2] = rs.getInt("new_y3");
               newMapId[2] = rs.getShort("new_mapid3");
               newX[3] = rs.getInt("new_x4");
               newY[3] = rs.getInt("new_y4");
               newMapId[3] = rs.getShort("new_mapid4");
               newX[4] = rs.getInt("new_x5");
               newY[4] = rs.getInt("new_y5");
               newMapId[4] = rs.getShort("new_mapid5");
               int heading = rs.getInt("new_heading");

               // 創建新的 NewDungeonRandom 實例
               newDungeonRandom = new NewDungeonRandom(newX, newY, newMapId, heading);

               // 檢查 _dungeonMap 中是否已經存在相同的鍵
               if (_dungeonMap.containsKey(key)) {
                   _log.log(Level.WARNING, "存在相同鍵的迷宮數據。 key=" + key);
               }
           }
       }
         _dungeonMap.put(key, newDungeonRandom);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   private static class NewDungeonRandom {
     int[] _newX = new int[5];
     int[] _newY = new int[5];
     short[] _newMapId = new short[5];
     int _heading;

     private NewDungeonRandom(int[] newX, int[] newY, short[] newMapId, int heading) {
       for (int i = 0; i < 5; i++) {
         this._newX[i] = newX[i];
         this._newY[i] = newY[i];
         this._newMapId[i] = newMapId[i];
       }
       this._heading = heading;
     }
   }

   public boolean dg(int locX, int locY, int mapId, L1PcInstance pc) {
     String key = mapId + locX + locY;
     if (_dungeonMap.containsKey(key)) {
       int rnd = _random.nextInt(5);
       NewDungeonRandom newDungeonRandom = _dungeonMap.get(key);
       short newMap = newDungeonRandom._newMapId[rnd];
       int newX = newDungeonRandom._newX[rnd];
       int newY = newDungeonRandom._newY[rnd];
       int heading = newDungeonRandom._heading;

       pc.start_teleport(newX, newY, newMap, heading, 18339, false, false);
       return true;
     }
     return false;
   }
 }


