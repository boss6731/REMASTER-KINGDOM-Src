 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Random;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.IntRange;
 import l1j.server.server.utils.SQLUtil;









 public class RenewalExpTable
 {
   public static RenewalExpTable _instance;
   public static final int MAX_LEVEL = 127;
   public static final int MAX_EXP = 2147483647;
   public static final IntRange EXP_RANGE = new IntRange(0, 2147483647);

   private static final int[] _expTable = new int[127];
   private static final int[] _expPenalty = new int[127];

   public static Map<Integer, Integer> _levelList = new HashMap<>();

   Random _Random = new Random(System.nanoTime());

   public static RenewalExpTable getInstance() {
     if (_instance == null) {
       _instance = new RenewalExpTable();
     }
     return _instance;
   }

   public void loadExp(boolean reload) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       if (reload) {
         _levelList.clear();
       }
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM experience_info");
       rs = pstm.executeQuery();
       while (rs.next()) {
         int level = rs.getInt("level");
         int exp = (int)rs.getLong("exp_min");
         int penalty = (int)rs.getLong("penalty");

         _expTable[level - 1] = exp;
         _expPenalty[level - 1] = penalty;
         _levelList.put(Integer.valueOf(exp), Integer.valueOf(level));
       }

     } catch (Exception e) {
       e.getStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static int getExpByLevel(int level) {
     return _expTable[level - 1];
   }








   public static int getNeedExpNextLevel(int level) {
     return getExpByLevel(level + 1) - getExpByLevel(level);
   }









   public static int getLevelByExp(int exp) {
     int level;
     for (level = 1; level < _expTable.length; level++) {

       if (exp < _expTable[level]) {
         break;
       }
     }
     return Math.min(level, 127);
   }

   public static int getExpPercentage(int level, int exp) {
     return (int)(100.0D * (exp - getExpByLevel(level)) / getNeedExpNextLevel(level));
   }

   public static double getExpPercentagedouble(int level, int exp) {
     return 100.0D * ((exp - getExpByLevel(level)) / getNeedExpNextLevel(level));
   }








   public static double getPenaltyRate(int level) {
     if (level < 50) {
       return 1.0D;
     }
     double expPenalty = 1.0D;
     expPenalty = 1.0D / _expPenalty[level - 1];

     return expPenalty;
   }

   public static int getExpByQuest(int level, int standardLevel, double percent) {
     double totalPer = percent * 0.01D;
     if (level >= 82) {
       totalPer *= 1.4D;
     } else if (level >= 75) {
       totalPer *= 1.2D;
     } else if (level >= 65) {
       totalPer *= 1.1D;
     }

     if (level >= 79) {
       totalPer /= (16 * (level - 78));
     } else if (level >= 75) {
       totalPer /= 8.0D;
     } else if (level >= 70) {
       totalPer /= 4.0D;
     } else if (level >= 65) {
       totalPer /= 2.0D;
     }

     return (int)(getNeedExpNextLevel(standardLevel) * totalPer);
   }
 }


