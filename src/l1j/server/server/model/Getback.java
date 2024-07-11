 package l1j.server.server.model;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Random;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.utils.SQLUtil;


 public class Getback
 {
   private static Logger _log = Logger.getLogger(Getback.class.getName());

   private static Random _random = new Random(System.nanoTime());

   private static Getback _instance;

   private static HashMap<Integer, ArrayList<Getback>> _getback = new HashMap<>();

   private int _areaX1;

   private int _areaY1;

   private int _areaX2;

   private int _areaY2;
   private int _areaMapId;
   private int _getbackX1;
   private int _getbackY1;
   private int _getbackX2;
   private int _getbackY2;
   private int _getbackX3;
   private int _getbackY3;
   private int _getbackMapId;
   private int _getbackTownId;
   private int _getbackTownIdForElf;
   private int _getbackTownIdForDarkelf;

   public static Getback getInstance() {
     if (_instance == null) {
       _instance = new Getback();
     }
     return _instance;
   }


   public void reload() {
     loadGetBack();
   }

   private boolean isSpecifyArea() {
     return (this._areaX1 != 0 && this._areaY1 != 0 && this._areaX2 != 0 && this._areaY2 != 0);
   }

   public static void loadGetBack() {
     _getback.clear();
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       String sSQL = "SELECT * FROM getback ORDER BY area_mapid,area_x1 DESC ";
       pstm = con.prepareStatement(sSQL);
       rs = pstm.executeQuery();
       Getback getback = null;
       while (rs.next()) {
         getback = new Getback();
         getback._areaX1 = rs.getInt("area_x1");
         getback._areaY1 = rs.getInt("area_y1");
         getback._areaX2 = rs.getInt("area_x2");
         getback._areaY2 = rs.getInt("area_y2");
         getback._areaMapId = rs.getInt("area_mapid");
         getback._getbackX1 = rs.getInt("getback_x1");
         getback._getbackY1 = rs.getInt("getback_y1");
         getback._getbackX2 = rs.getInt("getback_x2");
         getback._getbackY2 = rs.getInt("getback_y2");
         getback._getbackX3 = rs.getInt("getback_x3");
         getback._getbackY3 = rs.getInt("getback_y3");
         getback._getbackMapId = rs.getInt("getback_mapid");
         getback._getbackTownId = rs.getInt("getback_townid");
         getback._getbackTownIdForElf = rs.getInt("getback_townid_elf");
         getback._getbackTownIdForDarkelf = rs.getInt("getback_townid_darkelf");
         rs.getBoolean("scrollescape");
         ArrayList<Getback> getbackList = _getback.get(Integer.valueOf(getback._areaMapId));
         if (getbackList == null) {
           getbackList = new ArrayList<>();
           _getback.put(Integer.valueOf(getback._areaMapId), getbackList);
         }
         getbackList.add(getback);
       }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "could not Get Getback data", e);
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public static int[] GetBack_Location(L1PcInstance pc, boolean bScroll_Escape) {
     int[] loc = new int[3];

     int nPosition = _random.nextInt(3);

     int pcLocX = pc.getX();
     int pcLocY = pc.getY();
     int pcMapId = pc.getMapId();
     ArrayList<Getback> getbackList = _getback.get(Integer.valueOf(pcMapId));
     if (pc.isInParty() &&
       pc.isDead()) {
       pc.getParty().refresh(pc);
     }

     if (getbackList != null) {
       Getback getback = null;
       for (Getback gb : getbackList) {
         if (gb.isSpecifyArea()) {
           if (gb._areaX1 <= pcLocX && pcLocX <= gb._areaX2 && gb._areaY1 <= pcLocY && pcLocY <= gb._areaY2) {
             getback = gb; break;
           }
           continue;
         }
         getback = gb;
       }


       if (getback == null) {
         loc[0] = 33442;
         loc[1] = 32798;
         loc[2] = 4;
       } else {
         loc = ReadGetbackInfo(getback, nPosition);


         if (pc.isElf() && getback._getbackTownIdForElf > 0) {
           loc = L1TownLocation.getGetBackLoc(getback._getbackTownIdForElf);
         } else if (pc.isDarkelf() && getback._getbackTownIdForDarkelf > 0) {
           loc = L1TownLocation.getGetBackLoc(getback._getbackTownIdForDarkelf);
         } else if (getback._getbackTownId > 0) {
           loc = L1TownLocation.getGetBackLoc(getback._getbackTownId);
         }
       }
     } else {

       loc[0] = 33442;
       loc[1] = 32798;
       loc[2] = 4;
     }
     if (loc[0] == 0 || loc[1] == 0) {
       loc[0] = 33442;
       loc[1] = 32798;
       loc[2] = 4;
     }

     return loc;
   }

   public static int[] GetBack_Restart(L1PcInstance pc) {
     int[] loc = new int[3];

     try {
       loc = GetBack_Location(pc, true);

       if (pc.getClanid() != 0) {
         int castle_id = 0;
         int house_id = 0;
         L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
         if (clan != null) {
           castle_id = clan.getCastleId();
           house_id = clan.getHouseId();
         }
         if (castle_id != 0) {
           loc = L1CastleLocation.getCastleLoc(castle_id);
         } else if (house_id != 0) {
           loc = L1HouseLocation.getHouseLoc(house_id);
         }
       }

       return loc;
     } catch (Exception e) {

       loc[0] = 33437;
       loc[1] = 32812;
       loc[2] = 4;
       return loc;
     }
   }

   private static int[] ReadGetbackInfo(Getback getback, int nPosition) {
     int[] loc = new int[3];
     switch (nPosition) {
       case 0:
         loc[0] = getback._getbackX1;
         loc[1] = getback._getbackY1;
         break;
       case 1:
         loc[0] = getback._getbackX2;
         loc[1] = getback._getbackY2;
         break;
       case 2:
         loc[0] = getback._getbackX3;
         loc[1] = getback._getbackY3;
         break;
     }
     loc[2] = getback._getbackMapId;

     return loc;
   }
 }


