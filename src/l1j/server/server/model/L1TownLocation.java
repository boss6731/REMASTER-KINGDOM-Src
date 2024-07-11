 package l1j.server.server.model;

 import java.util.Random;
 import l1j.server.server.datatables.TownNpcTax;
 import l1j.server.server.datatables.TownTable;
 import l1j.server.server.templates.L1Town;
 import l1j.server.server.types.Point;




 public class L1TownLocation
 {
   public static final int TOWNID_TALKING_ISLAND = 1;
   public static final int TOWNID_SILVER_KNIGHT_TOWN = 2;
   public static final int TOWNID_GLUDIO = 3;
   public static final int TOWNID_ORCISH_FOREST = 4;
   public static final int TOWNID_WINDAWOOD = 5;
   public static final int TOWNID_KENT = 6;
   public static final int TOWNID_GIRAN = 7;
   public static final int TOWNID_HEINE = 8;
   public static final int TOWNID_WERLDAN = 9;
   public static final int TOWNID_OREN = 10;
   public static final int TOWNID_ELVEN_FOREST = 11;
   public static final int TOWNID_ADEN = 12;
   public static final int TOWNID_SILENT_CAVERN = 13;
   public static final int TOWNID_OUM_DUNGEON = 14;
   public static final int TOWNID_RESISTANCE = 15;
   public static final int TOWNID_PIRATE_ISLAND = 16;
   public static final int TOWNID_RECLUSE_VILLAGE = 17;
   public static final int TOWNID_HIDDEN_VALLEY = 18;
   public static final int TOWNID_claudia = 19;
   public static final int TOWNID_GIRAN_CASTLE = 20;
   private static final short GETBACK_MAP_TALKING_ISLAND = 0;
   private static final Point[] GETBACK_LOC_TALKING_ISLAND = new Point[] { new Point(32600, 32942), new Point(32574, 32944), new Point(32580, 32923), new Point(32557, 32975), new Point(32594, 32917), new Point(32580, 32974) };

   private static final short GETBACK_MAP_SILVER_KNIGHT_TOWN = 4;

   private static final Point[] GETBACK_LOC_SILVER_KNIGHT_TOWN = new Point[] { new Point(33071, 33402), new Point(33084, 33392), new Point(33085, 33402), new Point(33097, 33366), new Point(33110, 33365), new Point(33072, 33392) };

   private static final short GETBACK_MAP_GLUDIO = 4;

   private static final Point[] GETBACK_LOC_GLUDIO = new Point[] { new Point(32601, 32757), new Point(32625, 32809), new Point(32609, 32728), new Point(32612, 32781), new Point(32605, 32761), new Point(32614, 32739), new Point(32611, 32795) };


   private static final short GETBACK_MAP_ORCISH_FOREST = 4;

   private static final Point[] GETBACK_LOC_ORCISH_FOREST = new Point[] { new Point(32750, 32435), new Point(32745, 32447), new Point(32738, 32452), new Point(32741, 32436), new Point(32749, 32446) };

   private static final short GETBACK_MAP_WINDAWOOD = 4;

   private static final Point[] GETBACK_LOC_WINDAWOOD = new Point[] { new Point(32617, 33176), new Point(32621, 33188), new Point(32630, 33179), new Point(32623, 33193), new Point(32638, 33203), new Point(32621, 33179) };

   private static final short GETBACK_MAP_KENT = 4;

   private static final Point[] GETBACK_LOC_KENT = new Point[] { new Point(33048, 32750), new Point(33059, 32768), new Point(33047, 32761), new Point(33059, 32759), new Point(33051, 32775), new Point(33048, 32778), new Point(33064, 32773), new Point(33057, 32748) };


   private static final short GETBACK_MAP_GIRAN = 4;

   private static final Point[] GETBACK_LOC_GIRAN = new Point[] { new Point(33435, 32803), new Point(33439, 32817), new Point(33440, 32809), new Point(33419, 32810), new Point(33426, 32823), new Point(33418, 32818), new Point(33432, 32824) };


   private static final short TOWNID_MAP_claudia = 7783;

   private static final Point[] GETBACK_LOC_claudia = new Point[] { new Point(32640, 32872), new Point(32638, 32869), new Point(32639, 32857), new Point(32647, 32865), new Point(32648, 32861), new Point(32631, 32862), new Point(32635, 32865) };


   private static final short GETBACK_MAP_HEINE = 4;

   private static final Point[] GETBACK_LOC_HEINE = new Point[] { new Point(33593, 33242), new Point(33593, 33248), new Point(33604, 33236), new Point(33599, 33236), new Point(33610, 33247), new Point(33610, 33241), new Point(33599, 33252), new Point(33605, 33252) };


   private static final short GETBACK_MAP_WERLDAN = 4;

   private static final Point[] GETBACK_LOC_WERLDAN = new Point[] { new Point(33702, 32492), new Point(33747, 32508), new Point(33696, 32498), new Point(33723, 32512), new Point(33710, 32521), new Point(33724, 32488), new Point(33693, 32513) };


   private static final short GETBACK_MAP_OREN = 4;

   private static final Point[] GETBACK_LOC_OREN = new Point[] { new Point(34072, 32280), new Point(34037, 32230), new Point(34022, 32254), new Point(34021, 32269), new Point(34044, 32290), new Point(34049, 32316), new Point(34081, 32249), new Point(34074, 32313), new Point(34064, 32230) };


   private static final short GETBACK_MAP_ELVEN_FOREST = 4;

   private static final Point[] GETBACK_LOC_ELVEN_FOREST = new Point[] { new Point(33065, 32358), new Point(33052, 32313), new Point(33030, 32342), new Point(33068, 32320), new Point(33071, 32314), new Point(33030, 32370), new Point(33076, 32324), new Point(33068, 32336) };


   private static final short GETBACK_MAP_ADEN = 4;

   private static final Point[] GETBACK_LOC_ADEN = new Point[] { new Point(33915, 33114), new Point(34061, 33115), new Point(34090, 33168), new Point(34011, 33136), new Point(34093, 33117), new Point(33959, 33156), new Point(33992, 33120), new Point(34047, 33156) };


   private static final short GETBACK_MAP_SILENT_CAVERN = 304;

   private static final Point[] GETBACK_LOC_SILENT_CAVERN = new Point[] { new Point(32856, 32898), new Point(32860, 32916), new Point(32868, 32893), new Point(32875, 32903), new Point(32855, 32898) };

   private static final short GETBACK_MAP_OUM_DUNGEON = 310;

   private static final Point[] GETBACK_LOC_OUM_DUNGEON = new Point[] { new Point(32818, 32805), new Point(32800, 32798), new Point(32815, 32819), new Point(32823, 32811), new Point(32817, 32828) };

   private static final short GETBACK_MAP_RESISTANCE = 400;

   private static final Point[] GETBACK_LOC_RESISTANCE = new Point[] { new Point(32570, 32667), new Point(32559, 32678), new Point(32564, 32683), new Point(32574, 32661), new Point(32576, 32669), new Point(32572, 32662) };

   private static final short GETBACK_MAP_PIRATE_ISLAND = 440;

   private static final Point[] GETBACK_LOC_PIRATE_ISLAND = new Point[] { new Point(32431, 33058), new Point(32407, 33054) };

   private static final short GETBACK_MAP_RECLUSE_VILLAGE = 400;
   private static final Point[] GETBACK_LOC_RECLUSE_VILLAGE = new Point[] { new Point(32599, 32916), new Point(32599, 32923), new Point(32603, 32908), new Point(32595, 32908), new Point(32591, 32918) };

   private static final short GETBACK_MAP_HIDDEN_VALLEY = 2005;

   private static final Point[] GETBACK_LOC_HIDDEN_VALLEY = new Point[] { new Point(32691, 32853), new Point(32692, 32864), new Point(32688, 32876), new Point(32673, 32871), new Point(32670, 32857) };

   private static final short GETBACK_MAP_GIRAN_CASTLE = 15482;

   private static final Point[] GETBACK_LOC_GIRAN_CASTLE = new Point[] { new Point(33643, 32778), new Point(33621, 32780), new Point(33626, 32788), new Point(33635, 32794), new Point(33615, 32795), new Point(33628, 32802), new Point(33631, 32775) };







   private static final int[] TOWN_LOC_MIN_TALK_LOCATION = new int[] { 32549, 32923 };
   private static final int[] TOWN_LOC_MAX_TALK_LOCATION = new int[] { 32602, 33008 };
   private static final int[] TOWN_LOC_MIN_SILVER_LOCATION = new int[] { 33056, 33351 };
   private static final int[] TOWN_LOC_MAX_SILVER_LOCATION = new int[] { 33144, 33418 };
   private static final int[] TOWN_LOC_MIN_GLUDIO_LOCATION = new int[] { 32592, 32716 };
   private static final int[] TOWN_LOC_MAX_GLUDIO_LOCATION = new int[] { 32634, 32827 };
   private static final int[] TOWN_LOC_MIN_ORCISH_LOCATION = new int[] { 32710, 32421 };
   private static final int[] TOWN_LOC_MAX_ORCISH_LOCATION = new int[] { 32779, 32472 };
   private static final int[] TOWN_LOC_MIN_WINDAWOOD_LOCATION = new int[] { 32573, 32586 };
   private static final int[] TOWN_LOC_MAX_WINDAWOOD_LOCATION = new int[] { 32662, 33287 };
   private static final int[] TOWN_LOC_MIN_KENT_LOCATION = new int[] { 33031, 32723 };
   private static final int[] TOWN_LOC_MAX_KENT_LOCATION = new int[] { 33082, 32817 };
   private static final int[] TOWN_LOC_MIN_GIRAN_LOCATION = new int[] { 33340, 32652 };
   private static final int[] TOWN_LOC_MAX_GIRAN_LOCATION = new int[] { 33536, 32892 };
   private static final int[] TOWN_LOC_MIN_HEINE_LOCATION = new int[] { 33573, 33207 };
   private static final int[] TOWN_LOC_MAX_HEINE_LOCATION = new int[] { 33661, 33452 };
   private static final int[] TOWN_LOC_MIN_WERLDAN_LOCATION = new int[] { 33681, 32481 };
   private static final int[] TOWN_LOC_MAX_WERLDAN_LOCATION = new int[] { 33747, 32532 };
   private static final int[] TOWN_LOC_MIN_OREN_LOCATION = new int[] { 33996, 32203 };
   private static final int[] TOWN_LOC_MAX_OREN_LOCATION = new int[] { 34081, 32340 };
   private static final int[] TOWN_LOC_MIN_ADEN_LOCATION = new int[] { 33862, 33095 };
   private static final int[] TOWN_LOC_MAX_ADEN_LOCATION = new int[] { 34168, 33423 };




   public static int getTownIdByLoc(int x, int y) {
     int townid = 0;
     if (x >= TOWN_LOC_MIN_TALK_LOCATION[0] && y >= TOWN_LOC_MIN_TALK_LOCATION[1] && x <= TOWN_LOC_MAX_TALK_LOCATION[0] && y <= TOWN_LOC_MAX_TALK_LOCATION[1]) {



       townid = 1;
     } else if (x >= TOWN_LOC_MIN_SILVER_LOCATION[0] && y >= TOWN_LOC_MIN_SILVER_LOCATION[1] && x <= TOWN_LOC_MAX_SILVER_LOCATION[0] && y <= TOWN_LOC_MAX_SILVER_LOCATION[1]) {



       townid = 2;
     } else if (x >= TOWN_LOC_MIN_GLUDIO_LOCATION[0] && y >= TOWN_LOC_MIN_GLUDIO_LOCATION[1] && x <= TOWN_LOC_MAX_GLUDIO_LOCATION[0] && y <= TOWN_LOC_MAX_GLUDIO_LOCATION[1]) {



       townid = 3;
     } else if (x >= TOWN_LOC_MIN_ORCISH_LOCATION[0] && y >= TOWN_LOC_MIN_ORCISH_LOCATION[1] && x <= TOWN_LOC_MAX_ORCISH_LOCATION[0] && y <= TOWN_LOC_MAX_ORCISH_LOCATION[1]) {



       townid = 4;
     } else if (x >= TOWN_LOC_MIN_WINDAWOOD_LOCATION[0] && y >= TOWN_LOC_MIN_WINDAWOOD_LOCATION[1] && x <= TOWN_LOC_MAX_WINDAWOOD_LOCATION[0] && y <= TOWN_LOC_MAX_WINDAWOOD_LOCATION[1]) {



       townid = 5;
     } else if (x >= TOWN_LOC_MIN_KENT_LOCATION[0] && y >= TOWN_LOC_MIN_KENT_LOCATION[1] && x <= TOWN_LOC_MAX_KENT_LOCATION[0] && y <= TOWN_LOC_MAX_KENT_LOCATION[1]) {



       townid = 6;
     } else if (x >= TOWN_LOC_MIN_GIRAN_LOCATION[0] && y >= TOWN_LOC_MIN_GIRAN_LOCATION[1] && x <= TOWN_LOC_MAX_GIRAN_LOCATION[0] && y <= TOWN_LOC_MAX_GIRAN_LOCATION[1]) {



       townid = 7;
     } else if (x >= TOWN_LOC_MIN_HEINE_LOCATION[0] && y >= TOWN_LOC_MIN_HEINE_LOCATION[1] && x <= TOWN_LOC_MAX_HEINE_LOCATION[0] && y <= TOWN_LOC_MAX_HEINE_LOCATION[1]) {



       townid = 8;
     } else if (x >= TOWN_LOC_MIN_WERLDAN_LOCATION[0] && y >= TOWN_LOC_MIN_WERLDAN_LOCATION[1] && x <= TOWN_LOC_MAX_WERLDAN_LOCATION[0] && y <= TOWN_LOC_MAX_WERLDAN_LOCATION[1]) {



       townid = 9;
     } else if (x >= TOWN_LOC_MIN_OREN_LOCATION[0] && y >= TOWN_LOC_MIN_OREN_LOCATION[1] && x <= TOWN_LOC_MAX_OREN_LOCATION[0] && y <= TOWN_LOC_MAX_OREN_LOCATION[1]) {



       townid = 10;
     } else if (x >= TOWN_LOC_MIN_ADEN_LOCATION[0] && y >= TOWN_LOC_MIN_ADEN_LOCATION[1] && x <= TOWN_LOC_MAX_ADEN_LOCATION[0] && y <= TOWN_LOC_MAX_ADEN_LOCATION[1]) {



       townid = 12;
     }
     return townid;
   }

   public static int[] getGetBackLoc(int town_id) {
     Random random = new Random(System.nanoTime());
     int[] loc = new int[3];

     switch (town_id)
     { case 1:
         rnd = random.nextInt(GETBACK_LOC_TALKING_ISLAND.length);
         loc[0] = GETBACK_LOC_TALKING_ISLAND[rnd].getX();
         loc[1] = GETBACK_LOC_TALKING_ISLAND[rnd].getY();
         loc[2] = 0;




         return loc;case 2: rnd = random.nextInt(GETBACK_LOC_SILVER_KNIGHT_TOWN.length); loc[0] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getX(); loc[1] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getY(); loc[2] = 4; return loc;case 6: rnd = random.nextInt(GETBACK_LOC_KENT.length); loc[0] = GETBACK_LOC_KENT[rnd].getX(); loc[1] = GETBACK_LOC_KENT[rnd].getY(); loc[2] = 4; return loc;case 3: rnd = random.nextInt(GETBACK_LOC_GLUDIO.length); loc[0] = GETBACK_LOC_GLUDIO[rnd].getX(); loc[1] = GETBACK_LOC_GLUDIO[rnd].getY(); loc[2] = 4; return loc;case 4: rnd = random.nextInt(GETBACK_LOC_ORCISH_FOREST.length); loc[0] = GETBACK_LOC_ORCISH_FOREST[rnd].getX(); loc[1] = GETBACK_LOC_ORCISH_FOREST[rnd].getY(); loc[2] = 4; return loc;case 5: rnd = random.nextInt(GETBACK_LOC_WINDAWOOD.length); loc[0] = GETBACK_LOC_WINDAWOOD[rnd].getX(); loc[1] = GETBACK_LOC_WINDAWOOD[rnd].getY(); loc[2] = 4; return loc;case 7: rnd = random.nextInt(GETBACK_LOC_GIRAN.length); loc[0] = GETBACK_LOC_GIRAN[rnd].getX(); loc[1] = GETBACK_LOC_GIRAN[rnd].getY(); loc[2] = 4; return loc;case 19: rnd = random.nextInt(GETBACK_LOC_claudia.length); loc[0] = GETBACK_LOC_claudia[rnd].getX(); loc[1] = GETBACK_LOC_claudia[rnd].getY(); loc[2] = 7783; return loc;case 8: rnd = random.nextInt(GETBACK_LOC_HEINE.length); loc[0] = GETBACK_LOC_HEINE[rnd].getX(); loc[1] = GETBACK_LOC_HEINE[rnd].getY(); loc[2] = 4; return loc;case 9: rnd = random.nextInt(GETBACK_LOC_WERLDAN.length); loc[0] = GETBACK_LOC_WERLDAN[rnd].getX(); loc[1] = GETBACK_LOC_WERLDAN[rnd].getY(); loc[2] = 4; return loc;case 10: rnd = random.nextInt(GETBACK_LOC_OREN.length); loc[0] = GETBACK_LOC_OREN[rnd].getX(); loc[1] = GETBACK_LOC_OREN[rnd].getY(); loc[2] = 4; return loc;case 11: rnd = random.nextInt(GETBACK_LOC_ELVEN_FOREST.length); loc[0] = GETBACK_LOC_ELVEN_FOREST[rnd].getX(); loc[1] = GETBACK_LOC_ELVEN_FOREST[rnd].getY(); loc[2] = 4; return loc;case 12: rnd = random.nextInt(GETBACK_LOC_ADEN.length); loc[0] = GETBACK_LOC_ADEN[rnd].getX(); loc[1] = GETBACK_LOC_ADEN[rnd].getY(); loc[2] = 4; return loc;case 13: rnd = random.nextInt(GETBACK_LOC_SILENT_CAVERN.length); loc[0] = GETBACK_LOC_SILENT_CAVERN[rnd].getX(); loc[1] = GETBACK_LOC_SILENT_CAVERN[rnd].getY(); loc[2] = 304; return loc;case 14: rnd = random.nextInt(GETBACK_LOC_OUM_DUNGEON.length); loc[0] = GETBACK_LOC_OUM_DUNGEON[rnd].getX(); loc[1] = GETBACK_LOC_OUM_DUNGEON[rnd].getY(); loc[2] = 310; return loc;case 15: rnd = random.nextInt(GETBACK_LOC_RESISTANCE.length); loc[0] = GETBACK_LOC_RESISTANCE[rnd].getX(); loc[1] = GETBACK_LOC_RESISTANCE[rnd].getY(); loc[2] = 400; return loc;case 16: rnd = random.nextInt(GETBACK_LOC_PIRATE_ISLAND.length); loc[0] = GETBACK_LOC_PIRATE_ISLAND[rnd].getX(); loc[1] = GETBACK_LOC_PIRATE_ISLAND[rnd].getY(); loc[2] = 440; return loc;case 17: rnd = random.nextInt(GETBACK_LOC_RECLUSE_VILLAGE.length); loc[0] = GETBACK_LOC_RECLUSE_VILLAGE[rnd].getX(); loc[1] = GETBACK_LOC_RECLUSE_VILLAGE[rnd].getY(); loc[2] = 400; return loc;case 18: rnd = random.nextInt(GETBACK_LOC_HIDDEN_VALLEY.length); loc[0] = GETBACK_LOC_HIDDEN_VALLEY[rnd].getX(); loc[1] = GETBACK_LOC_HIDDEN_VALLEY[rnd].getY(); loc[2] = 2005; return loc;case 20: rnd = random.nextInt(GETBACK_LOC_GIRAN_CASTLE.length); loc[0] = GETBACK_LOC_GIRAN_CASTLE[rnd].getX(); loc[1] = GETBACK_LOC_GIRAN_CASTLE[rnd].getY(); loc[2] = 15482; return loc; }  int rnd = random.nextInt(GETBACK_LOC_SILVER_KNIGHT_TOWN.length); loc[0] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getX(); loc[1] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getY(); loc[2] = 4; return loc;
   }

   public static int getTownTaxRateByNpcid(int npcid) {
     int tax_rate = 0;

     int town_id = getTownIdByNpcid(npcid);
     if (town_id >= 1 && town_id <= 10) {
       L1Town town = TownTable.getInstance().getTownTable(town_id);

       tax_rate = town.get_tax_rate();
     }
     return tax_rate;
   }

   public static int getTownIdByNpcid(int npcid) {
     int town_id = 0;
     if (TownNpcTax.getInstance().getTownNpcInfo(npcid) != null) {
       TownNpcTax.L1TownNpcTax temp = TownNpcTax.getInstance().getTownNpcInfo(npcid);
       if (temp != null) {
         return temp.getTownId();
       }
     }
     return town_id;
   }
 }


