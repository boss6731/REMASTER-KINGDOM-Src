 package l1j.server.server.model;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.gametime.GameTimeClock;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.SQLUtil;


 public class Dungeon
 {
   private static Logger _log = Logger.getLogger(Dungeon.class.getName());

   private static Dungeon _instance = null;

   private Map<String, NewDungeon> _dungeonMap = null;

   private enum DungeonType {
     NONE, SHIP_FOR_FI, SHIP_FOR_HEINE, SHIP_FOR_PI, SHIP_FOR_HIDDENDOCK, SHIP_FOR_GLUDIN, SHIP_FOR_TI;
   }

   public static Dungeon getInstance() {
     if (_instance == null) {
       _instance = new Dungeon();
     }
     return _instance;
   }

   public static void reload() {
     Dungeon oldInstance = _instance;
     _instance = new Dungeon();
     oldInstance._dungeonMap.clear();
   }

   private Dungeon() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM dungeon");
       rs = pstm.executeQuery();
       this._dungeonMap = new HashMap<>(SQLUtil.calcRows(rs));
       NewDungeon newDungeon = null;
       while (rs.next()) {
         int srcMapId = rs.getInt("src_mapid");
         int srcX = rs.getInt("src_x");
         int srcY = rs.getInt("src_y");
         String key = srcMapId + srcX + srcY;
         int newX = rs.getInt("new_x");
         int newY = rs.getInt("new_y");
         int newMapId = rs.getInt("new_mapid");
         int heading = rs.getInt("new_heading");
         boolean Effect = rs.getBoolean("Effect");
         int min_lvl = rs.getInt("min_lvl");
         int max_lvl = rs.getInt("max_lvl");
         String Ment = rs.getString("Ment");
         int Effect_ID = rs.getInt("Effect_ID");

         DungeonType dungeonType = DungeonType.NONE;
         if (((srcX == 33423 || srcX == 33424 || srcX == 33425 || srcX == 33426) && srcY == 33502 && srcMapId == 4) || ((srcX == 32733 || srcX == 32734 || srcX == 32735 || srcX == 32736) && srcY == 32794 && srcMapId == 83)) {

           dungeonType = DungeonType.SHIP_FOR_FI;
         } else if (((srcX == 32935 || srcX == 32936 || srcX == 32937) && srcY == 33058 && srcMapId == 70) || ((srcX == 32732 || srcX == 32733 || srcX == 32734 || srcX == 32735) && srcY == 32796 && srcMapId == 84)) {

           dungeonType = DungeonType.SHIP_FOR_HEINE;
         } else if (((srcX == 32750 || srcX == 32751 || srcX == 32752) && srcY == 32874 && srcMapId == 445) || ((srcX == 32731 || srcX == 32732 || srcX == 32733) && srcY == 32796 && srcMapId == 447)) {

           dungeonType = DungeonType.SHIP_FOR_PI;
         } else if (((srcX == 32296 || srcX == 32297 || srcX == 32298) && srcY == 33087 && srcMapId == 440) || ((srcX == 32735 || srcX == 32736 || srcX == 32737) && srcY == 32794 && srcMapId == 446)) {

           dungeonType = DungeonType.SHIP_FOR_HIDDENDOCK;
         } else if (((srcX == 32630 || srcX == 32631 || srcX == 32632) && srcY == 32983 && srcMapId == 0) || ((srcX == 32733 || srcX == 32734 || srcX == 32735) && srcY == 32796 && srcMapId == 5)) {

           dungeonType = DungeonType.SHIP_FOR_GLUDIN;
         } else if (((srcX == 32540 || srcX == 32542 || srcX == 32543 || srcX == 32544 || srcX == 32545) && srcY == 32728 && srcMapId == 4) || ((srcX == 32734 || srcX == 32735 || srcX == 32736 || srcX == 32737) && srcY == 32794 && srcMapId == 6)) {

           dungeonType = DungeonType.SHIP_FOR_TI;
         }
            // 創建新的 NewDungeon 實例
           newDungeon = new NewDungeon(newX, newY, (short)newMapId, heading, Effect_ID, Effect, min_lvl, max_lvl, Ment, dungeonType);

        // 檢查 _dungeonMap 中是否已經存在相同的鍵
           if (this._dungeonMap.containsKey(key)) {
               _log.log(Level.WARNING, "存在相同鍵的迷宮數據。 key=" + key);
           }

        // 將新建的迷宮對象放入 _dungeonMap
           this._dungeonMap.put(key, newDungeon);
       }
     } catch (Exception e) {
         e.printStackTrace();
     } finally {
            // 釋放資源
         SQLUtil.close(rs);
         SQLUtil.close(pstm);
         SQLUtil.close(con);
     }
   }
   private static class NewDungeon {
     int _newX;
     int _newY;
     short _newMapId;
     int _heading;
     int _Effect_ID;
     boolean _Effect;
     int _min_lvl;
     int _max_lvl;
     String _Ment;
     Dungeon.DungeonType _dungeonType;

     private NewDungeon(int newX, int newY, short newMapId, int heading, int Effect_ID, boolean Effect, int min_lvl, int max_lvl, String Ment, Dungeon.DungeonType dungeonType) {
       this._newX = newX;
       this._newY = newY;
       this._newMapId = newMapId;
       this._heading = heading;
       this._Effect = Effect;
       this._Effect_ID = Effect_ID;
       this._min_lvl = min_lvl;
       this._max_lvl = max_lvl;
       this._Ment = Ment;
       this._dungeonType = dungeonType;
     }
   }


   public boolean dg(int locX, int locY, int mapId, L1PcInstance pc) {
     int servertime = GameTimeClock.getInstance().getGameTime().getSeconds();
     int nowtime = servertime % 86400;
     String key = mapId + locX + locY;
     if (this._dungeonMap.containsKey(key)) {
       NewDungeon newDungeon = this._dungeonMap.get(key);
       short newMap = newDungeon._newMapId;
       int newX = newDungeon._newX;
       int newY = newDungeon._newY;
       int heading = newDungeon._heading;
       boolean Effect = newDungeon._Effect;
       int Effect_ID = newDungeon._Effect_ID;
       int min_lvl = newDungeon._min_lvl;
       int max_lvl = newDungeon._max_lvl;
       String Ment = newDungeon._Ment;
       DungeonType dungeonType = newDungeon._dungeonType;
       boolean teleportable = false;

       if (dungeonType == DungeonType.NONE) {
         teleportable = true;
       }
       else if ((nowtime >= 5400 && nowtime < 9000) || (nowtime >= 16200 && nowtime < 19800) || (nowtime >= 27000 && nowtime < 30600) || (nowtime >= 37800 && nowtime < 41400) || (nowtime >= 48600 && nowtime < 52200) || (nowtime >= 59400 && nowtime < 63000) || (nowtime >= 70200 && nowtime < 73800) || (nowtime >= 81000 && nowtime < 84600)) {









         if ((pc.getInventory().checkItem(40299, 1) && dungeonType == DungeonType.SHIP_FOR_GLUDIN) || (pc
           .getInventory().checkItem(40301, 1) && dungeonType == DungeonType.SHIP_FOR_HEINE) || (pc
           .getInventory().checkItem(40302, 1) && dungeonType == DungeonType.SHIP_FOR_PI)) {
           teleportable = true;
         }
       } else if ((nowtime >= 0 && nowtime < 360) || (nowtime >= 10800 && nowtime < 14400) || (nowtime >= 21600 && nowtime < 25200) || (nowtime >= 32400 && nowtime < 36000) || (nowtime >= 43200 && nowtime < 46800) || (nowtime >= 54000 && nowtime < 57600) || (nowtime >= 64800 && nowtime < 68400) || (nowtime >= 75600 && nowtime < 79200)) {


         if ((pc.getInventory().checkItem(40298, 1) && dungeonType == DungeonType.SHIP_FOR_TI) || (pc
           .getInventory().checkItem(40300, 1) && dungeonType == DungeonType.SHIP_FOR_FI) || (pc
           .getInventory().checkItem(40303, 1) && dungeonType == DungeonType.SHIP_FOR_HIDDENDOCK)) {
           teleportable = true;
         }
       }


       if (Ment != null) {
         String message = String.format(Ment, new Object[0]);
         String message2 = String.format(Ment, new Object[0]);
         L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { (ServerBasePacket)new S_SystemMessage(message), (ServerBasePacket)new S_PacketBox(84, message2) });
       }
       if (pc.getLevel() >= min_lvl && pc.getLevel() <= max_lvl) {
         if (teleportable && newMap != 15492 && newMap != 15482) {
           pc.start_teleport(newX, newY, newMap, heading, Effect_ID, Effect, true);
           return true;
         }



         pc.send_effect(Effect_ID, Effect);
         SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, newX, newY, newMap, 8);
         return true;
       }





         pc.sendPackets("最低等級 " + min_lvl + " 到 最高等級 " + max_lvl + " 以下才能進入。");
         return false;
     }

// 這裡可能需要關閉或結束某些資源或操作，可以添加相應的代碼

       return false;
   }
 }

