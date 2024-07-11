 package l1j.server.server.model;

 import java.util.HashMap;
 import java.util.Map;
 import java.util.Random;
 import l1j.server.MJWarSystem.MJCastleWar;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.model.gametime.BaseTime;
 import l1j.server.server.model.gametime.GameTimeAdapter;
 import l1j.server.server.model.gametime.GameTimeClock;
 import l1j.server.server.model.gametime.TimeListener;




 public class L1CastleLocation
 {
   public static final int KENT_CASTLE_ID = 1;
   public static final int OT_CASTLE_ID = 2;
   public static final int WW_CASTLE_ID = 3;
   public static final int GIRAN_CASTLE_ID = 4;
   public static final int HEINE_CASTLE_ID = 5;
   public static final int DOWA_CASTLE_ID = 6;
   public static final int ADEN_CASTLE_ID = 7;
   public static final int DIAD_CASTLE_ID = 8;
   public static final int KENT_TOWER_X = 33168;
   public static final int KENT_TOWER_Y = 32779;
   public static final short KENT_TOWER_MAP = 4;
   public static final int KENT_X1 = 33089;
   public static final int KENT_X2 = 33219;
   public static final int KENT_Y1 = 32717;
   public static final int KENT_Y2 = 32827;
   public static final short KENT_MAP = 4;
   public static final short KENT_INNER_CASTLE_MAP = 15;
   public static final int OT_TOWER_X = 32798;
   public static final int OT_TOWER_Y = 32285;
   public static final short OT_TOWER_MAP = 4;
   public static final int OT_X1 = 32750;
   public static final int OT_X2 = 32850;
   public static final int OT_Y1 = 32250;
   public static final int OT_Y2 = 32350;
   public static final short OT_MAP = 4;
   public static final int WW_TOWER_X = 32623;
   public static final int WW_TOWER_Y = 33379;
   public static final short WW_TOWER_MAP = 4;
   public static final int WW_X1 = 32571;
   public static final int WW_X2 = 32721;
   public static final int WW_Y1 = 33350;
   public static final int WW_Y2 = 33460;
   public static final short WW_MAP = 4;
   public static final short WW_INNER_CASTLE_MAP = 29;
   public static final int GIRAN_TOWER_X = 33631;
   public static final int GIRAN_TOWER_Y = 32678;
   public static final short GIRAN_TOWER_MAP = 15482;
   public static final int GIRAN_X1 = 33559;
   public static final int GIRAN_X2 = 33686;
   public static final int GIRAN_Y1 = 32615;
   public static final int GIRAN_Y2 = 32755;
   public static final short GIRAN_MAP = 15482;
   public static final short GIRAN_INNER_CASTLE_MAP = 15492;
   public static final int HEINE_TOWER_X = 33524;
   public static final int HEINE_TOWER_Y = 33396;
   public static final short HEINE_TOWER_MAP = 4;
   public static final int HEINE_X1 = 33458;
   public static final int HEINE_X2 = 33583;
   public static final int HEINE_Y1 = 33315;
   public static final int HEINE_Y2 = 33490;
   public static final short HEINE_MAP = 4;
   public static final short HEINE_INNER_CASTLE_MAP = 64;
   public static final int DOWA_TOWER_X = 32828;
   public static final int DOWA_TOWER_Y = 32818;
   public static final short DOWA_TOWER_MAP = 66;
   public static final int DOWA_X1 = 32755;
   public static final int DOWA_X2 = 32870;
   public static final int DOWA_Y1 = 32790;
   public static final int DOWA_Y2 = 32920;
   public static final short DOWA_MAP = 66;
   public static final int ADEN_TOWER_X = 34090;
   public static final int ADEN_TOWER_Y = 33260;
   public static final short ADEN_TOWER_MAP = 4;
   public static final int ADEN_X1 = 34007;
   public static final int ADEN_X2 = 34162;
   public static final int ADEN_Y1 = 33172;
   public static final int ADEN_Y2 = 33332;
   public static final short ADEN_MAP = 4;
   public static final short ADEN_INNER_CASTLE_MAP = 300;
   public static final int ADEN_SUB_TOWER1_X = 34057;
   public static final int ADEN_SUB_TOWER1_Y = 33291;
   public static final int ADEN_SUB_TOWER2_X = 34123;
   public static final int ADEN_SUB_TOWER2_Y = 33291;
   public static final int ADEN_SUB_TOWER3_X = 34057;
   public static final int ADEN_SUB_TOWER3_Y = 33230;
   public static final int ADEN_SUB_TOWER4_X = 34123;
   public static final int ADEN_SUB_TOWER4_Y = 33230;
   public static final int DIAD_TOWER_X = 33033;
   public static final int DIAD_TOWER_Y = 32895;
   public static final short DIAD_TOWER_MAP = 320;
   public static final int DIAD_X1 = 32888;
   public static final int DIAD_X2 = 33070;
   public static final int DIAD_Y1 = 32839;
   public static final int DIAD_Y2 = 32953;
   public static final short DIAD_MAP = 320;
   public static final short DIAD_INNER_CASTLE_MAP = 330;
   private static final Map<Integer, L1Location> _towers = new HashMap<>();

   static {
     _towers.put(Integer.valueOf(1), new L1Location(33168, 32779, 4));
     _towers.put(Integer.valueOf(2), new L1Location(32798, 32285, 4));
     _towers.put(Integer.valueOf(3), new L1Location(32623, 33379, 4));
     _towers.put(Integer.valueOf(4), new L1Location(33631, 32678, 15482));
     _towers.put(Integer.valueOf(5), new L1Location(33524, 33396, 4));
     _towers.put(Integer.valueOf(6), new L1Location(32828, 32818, 66));
     _towers.put(Integer.valueOf(7), new L1Location(34090, 33260, 4));
     _towers.put(Integer.valueOf(8), new L1Location(33033, 32895, 320));
   }

   private static final Map<Integer, L1MapArea> _areas = new HashMap<>();

   static {
     _areas.put(Integer.valueOf(1), new L1MapArea(33089, 32717, 33219, 32827, 4));
     _areas.put(Integer.valueOf(2), new L1MapArea(32750, 32250, 32850, 32350, 4));
     _areas.put(Integer.valueOf(3), new L1MapArea(32571, 33350, 32721, 33460, 4));
     _areas.put(Integer.valueOf(4), new L1MapArea(33559, 32615, 33686, 32755, 15482));
     _areas.put(Integer.valueOf(5), new L1MapArea(33458, 33315, 33583, 33490, 4));
     _areas.put(Integer.valueOf(6), new L1MapArea(32755, 32790, 32870, 32920, 66));
     _areas.put(Integer.valueOf(7), new L1MapArea(34007, 33172, 34162, 33332, 4));
     _areas.put(Integer.valueOf(8), new L1MapArea(32888, 32839, 33070, 32953, 320));
   }

   private static final Map<Integer, Integer> _innerTowerMaps = new HashMap<>();

   static {
     _innerTowerMaps.put(Integer.valueOf(1), Integer.valueOf(15));
     _innerTowerMaps.put(Integer.valueOf(3), Integer.valueOf(29));
     _innerTowerMaps.put(Integer.valueOf(4), Integer.valueOf(15492));
     _innerTowerMaps.put(Integer.valueOf(5), Integer.valueOf(64));
     _innerTowerMaps.put(Integer.valueOf(7), Integer.valueOf(300));
     _innerTowerMaps.put(Integer.valueOf(8), Integer.valueOf(330));
   }

   private static final Map<Integer, L1Location> _subTowers = new HashMap<>();

   static {
     _subTowers.put(Integer.valueOf(1), new L1Location(34057, 33291, 4));
     _subTowers.put(Integer.valueOf(2), new L1Location(34123, 33291, 4));
     _subTowers.put(Integer.valueOf(3), new L1Location(34057, 33230, 4));
     _subTowers.put(Integer.valueOf(4), new L1Location(34123, 33230, 4));
   }




   public static int getCastleId(L1Location loc) {
     for (Map.Entry<Integer, L1Location> entry : _towers.entrySet()) {
       if (((L1Location)entry.getValue()).equals(loc)) {
         return ((Integer)entry.getKey()).intValue();
       }
     }
     return 0;
   }

   public static int getCastleByMapId(int castleId) {
     return ((L1MapArea)_areas.get(Integer.valueOf(castleId))).getMapId();
   }




   public static int getCastleId(int locx, int locy, short mapid) {
     return getCastleId(new L1Location(locx, locy, mapid));
   }

   public static int getCastleIdByArea(L1Location loc) {
     for (Map.Entry<Integer, L1MapArea> entry : _areas.entrySet()) {
       if (((L1MapArea)entry.getValue()).contains(loc)) {
         return ((Integer)entry.getKey()).intValue();
       }
     }
     for (Map.Entry<Integer, Integer> entry : _innerTowerMaps.entrySet()) {
       if (((Integer)entry.getValue()).intValue() == loc.getMapId()) {
         return ((Integer)entry.getKey()).intValue();
       }
     }
     return 0;
   }




   public static int getCastleIdByArea(L1Character cha) {
     return getCastleIdByArea(cha.getLocation());
   }

   public static boolean checkInWarArea(int castleId, L1Location loc) {
     return (castleId == getCastleIdByArea(loc));
   }




   public static boolean checkInWarArea(int castleId, L1Character cha) {
     return checkInWarArea(castleId, cha.getLocation());
   }

   public static boolean checkInAllWarArea(L1Location loc) {
     return (0 != getCastleIdByArea(loc));
   }




   public static boolean checkInAllWarArea(int locx, int locy, short mapid) {
     return checkInAllWarArea(new L1Location(locx, locy, mapid));
   }




   public static int[] getTowerLoc(int castleId) {
     int[] result = new int[3];
     L1Location loc = _towers.get(Integer.valueOf(castleId));
     if (loc != null) {
       result[0] = loc.getX();
       result[1] = loc.getY();
       result[2] = loc.getMapId();
     }
     return result;
   }




   public static int[] getWarArea(int castleId) {
     int[] loc = new int[5];
     switch (castleId) {
       case 1:
         loc[0] = 33089;
         loc[1] = 33219;
         loc[2] = 32717;
         loc[3] = 32827;
         loc[4] = 4;
         break;
       case 2:
         loc[0] = 32750;
         loc[1] = 32850;
         loc[2] = 32250;
         loc[3] = 32350;
         loc[4] = 4;
         break;
       case 3:
         loc[0] = 32571;
         loc[1] = 32721;
         loc[2] = 33350;
         loc[3] = 33460;
         loc[4] = 4;
         break;
       case 4:
         loc[0] = 33559;
         loc[1] = 33686;
         loc[2] = 32615;
         loc[3] = 32755;
         loc[4] = 15482;
         break;
       case 5:
         loc[0] = 33458;
         loc[1] = 33583;
         loc[2] = 33315;
         loc[3] = 33490;
         loc[4] = 4;
         break;
       case 6:
         loc[0] = 32755;
         loc[1] = 32870;
         loc[2] = 32790;
         loc[3] = 32920;
         loc[4] = 66;
         break;
       case 7:
         loc[0] = 34007;
         loc[1] = 34162;
         loc[2] = 33172;
         loc[3] = 33332;
         loc[4] = 4;
         break;
       case 8:
         loc[0] = 32888;
         loc[1] = 33070;
         loc[2] = 32839;
         loc[3] = 32953;
         loc[4] = 320;
         break;
     }


     return loc;
   }

   public static int[] getCastleLoc(int castle_id) {
     int[] loc = new int[3];
     switch (castle_id) {
       case 1:
         loc[0] = 32731;
         loc[1] = 32810;
         loc[2] = 15;
         break;
       case 2:
         loc[0] = 32800;
         loc[1] = 32277;
         loc[2] = 4;
         break;
       case 3:
         loc[0] = 32730;
         loc[1] = 32814;
         loc[2] = 29;
         break;
       case 4:
         loc[0] = 32724;
         loc[1] = 32827;
         loc[2] = 15492;
         break;
       case 5:
         loc[0] = 32568;
         loc[1] = 32855;
         loc[2] = 64;
         break;
       case 6:
         loc[0] = 32853;
         loc[1] = 32810;
         loc[2] = 66;
         break;
       case 7:
         loc[0] = 32892;
         loc[1] = 32572;
         loc[2] = 300;
         break;
       case 8:
         loc[0] = 32733;
         loc[1] = 32985;
         loc[2] = 330;
         break;
     }


     return loc;
   }



   public static int[] getGetBackLoc(int castle_id) {
     Random random;
     int rnd;
     switch (castle_id)
     { case 1:
         loc = L1TownLocation.getGetBackLoc(6);




         return loc;case 2: loc = L1TownLocation.getGetBackLoc(4); return loc;case 3: loc = L1TownLocation.getGetBackLoc(5); return loc;case 4: loc = L1TownLocation.getGetBackLoc(20); return loc;case 5: loc = L1TownLocation.getGetBackLoc(8); return loc;case 6: loc = L1TownLocation.getGetBackLoc(9); return loc;case 7: loc = L1TownLocation.getGetBackLoc(12); return loc;case 8: random = new Random(System.nanoTime()); rnd = random.nextInt(3); loc = new int[3]; if (rnd == 0) { loc[0] = 32792; loc[1] = 32807; loc[2] = 310; } else if (rnd == 1) { loc[0] = 32816; loc[1] = 32820; loc[2] = 310; } else if (rnd == 2) { loc[0] = 32823; loc[1] = 32797; loc[2] = 310; }  return loc; }  int[] loc = L1TownLocation.getGetBackLoc(2); return loc;
   }


   public static int getCastleIdByNpcid(int npcid) {
     int castle_id = 0;

     int town_id = L1TownLocation.getTownIdByNpcid(npcid);

     switch (town_id) {
       case 3:
       case 6:
         castle_id = 1;
         break;

       case 4:
         castle_id = 2;
         break;

       case 2:
       case 5:
         castle_id = 3;
         break;

       case 1:
       case 7:
         castle_id = 4;
         break;

       case 8:
         castle_id = 5;
         break;

       case 9:
       case 10:
         castle_id = 6;
         break;

       case 12:
         castle_id = 7;
         break;

       case 14:
         castle_id = 8;
         break;
     }



     return castle_id;
   }


   public static int getCastleTaxRateByNpcId(int npcId) {
     int castleId = getCastleIdByNpcid(npcId);
     if (castleId != 0) {
       return ((Integer)_castleTaxRate.get(Integer.valueOf(castleId))).intValue();
     }
     return 0;
   }


   private static HashMap<Integer, Integer> _castleTaxRate = new HashMap<>();

   private static L1CastleTaxRateListener _listener;


   public static void setCastleTaxRate() {
     for (int i = 1; i <= 8; i++) {
       MJCastleWar war = MJCastleWarBusiness.getInstance().get(i);
       _castleTaxRate.put(Integer.valueOf(i), Integer.valueOf(war.getTaxRate()));
     }
     if (_listener == null) {
       _listener = new L1CastleTaxRateListener();
       GameTimeClock.getInstance().addListener((TimeListener)_listener);
     }
   }

   private static class L1CastleTaxRateListener extends GameTimeAdapter { private L1CastleTaxRateListener() {}

     public void onDayChanged(BaseTime time) {
       L1CastleLocation.setCastleTaxRate();
     } }


   public static int[] getSubTowerLoc(int no) {
     int[] result = new int[3];
     L1Location loc = _subTowers.get(Integer.valueOf(no));
     if (loc != null) {
       result[0] = loc.getX();
       result[1] = loc.getY();
       result[2] = loc.getMapId();
     }
     return result;
   }
 }


