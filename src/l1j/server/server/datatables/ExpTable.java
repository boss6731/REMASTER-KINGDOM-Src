 package l1j.server.server.datatables;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

 public class ExpTable {
   private static ArrayList<ExpTable> m_experience_info;
   private static int m_max_level;
   private static long m_max_exp;

   public static void do_load() {
     final ArrayList<ExpTable> experience_info = new ArrayList<>(128);
     Selector.exec("select * from experience_info order by level asc", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               ExpTable o = ExpTable.newInstance(rs);
               experience_info.add(o);
             }
           }
         });
     m_experience_info = experience_info;
     ExpTable eInfo = m_experience_info.get(m_experience_info.size() - 1);
     m_max_level = eInfo.get_level();

     m_max_exp = 2868888136L;
   }
   private int m_level; private long m_exp; private double m_penalty;
   public static int get_max_level() {
     return m_max_level;
   }

   public static long get_max_exp() {
     return m_max_exp;
   }






   public static long getExpByLevel(int level) {
     if (level > m_max_level) {
       return m_max_exp;
     }
     return ((ExpTable)m_experience_info.get(level - 1)).get_exp();
   }






   public static long getNeedExpNextLevel(int level) {
     if (level >= m_max_level) {
       return m_max_exp - getExpByLevel(level);
     }
     return getExpByLevel(level + 1) - getExpByLevel(level);
   }







   public static int getLevelByExp(long exp) {
     int size = m_experience_info.size();
     int lvl;
     for (lvl = 1; lvl < size; lvl++) {
       ExpTable expInfo = m_experience_info.get(lvl);
       if (exp < expInfo.get_exp())
         break;
     }
     return Math.min(lvl, m_max_level);
   }

   public static int getExpPercentage(int level, long exp) {
     return (int)(100.0D * (exp - getExpByLevel(level)) / getNeedExpNextLevel(level));
   }

   public static double getExpPercentagedouble(int level, int exp) {
     return 100.0D * (exp - getExpByLevel(level)) / getNeedExpNextLevel(level);
   }






   public static double getPenaltyRate(int level) {
     return ((ExpTable)m_experience_info.get(level - 1)).get_penalty();
   }


   private static ExpTable newInstance(ResultSet rs) throws SQLException {
     return newInstance()
       .set_level(rs.getInt("level"))
       .set_exp(Long.parseLong(rs.getString("exp").replace("0x", ""), 16))

       .set_penalty(rs.getDouble("penalty"));
   }

   private static ExpTable newInstance() {
     return new ExpTable();
   }






   public ExpTable set_level(int level) {
     this.m_level = level;
     return this;
   }
   public ExpTable set_exp(long exp) {
     this.m_exp = exp;
     return this;
   }
   public ExpTable set_penalty(double penalty) {
     this.m_penalty = penalty;
     return this;
   }
   public int get_level() {
     return this.m_level;
   }
   public long get_exp() {
     return this.m_exp;
   }
   public double get_penalty() {
     return this.m_penalty;
   }
 }


