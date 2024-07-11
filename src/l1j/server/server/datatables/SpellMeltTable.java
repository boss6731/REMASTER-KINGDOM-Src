 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import javolution.util.FastMap;
 import javolution.util.FastTable;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

 public class SpellMeltTable
 {
   private static SpellMeltTable _instance;

   public static SpellMeltTable getInstance() {
     if (_instance == null) _instance = new SpellMeltTable();
     return _instance;
   }
   public class MeltData { public int skillId;
     public int passiveId;
     public int classType;

     public MeltData(ResultSet rs) throws SQLException {
       this.skillId = rs.getInt("skillId");
       this.skillName = rs.getString("skillName");
       this.passiveId = rs.getInt("passiveId");

       this.skillItemId = rs.getInt("skillItemId");
       this.meltItemId = rs.getInt("meltItemId");
     }
     public int skillItemId; public int meltItemId;
     public String skillName; }
   private static final FastMap<Integer, MeltData> DATA = new FastMap();
   private static final FastMap<Integer, FastMap<Integer, FastTable<Integer>>> CLASS_DATA = new FastMap();

   public static MeltData getActiveData(int skillId) {
     return DATA.containsKey(Integer.valueOf(skillId)) ? (MeltData)DATA.get(Integer.valueOf(skillId)) : null;
   }

   public static MeltData getPassiveData(int id) {
     for (MeltData melt : DATA.values()) {
       if (melt.passiveId == id) {
         return melt;
       }
     }
     return null;
   }

   public static boolean isSpellMeltItem(int itemId) {
     return CLASS_DATA.containsKey(Integer.valueOf(itemId));
   }

   public static FastTable<Integer> getClassSkillItemList(int itemId, int classType) {
     FastMap<Integer, FastTable<Integer>> meltItemData = (FastMap<Integer, FastTable<Integer>>)CLASS_DATA.get(Integer.valueOf(itemId));
     if (meltItemData == null) return null;
     return meltItemData.containsKey(Integer.valueOf(classType)) ? (FastTable<Integer>)meltItemData.get(Integer.valueOf(classType)) : null;
   }

   private SpellMeltTable() {
     load();
   }

   private void load() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     MeltData melt = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM spell_melt");
       rs = pstm.executeQuery();
       while (rs.next()) {
         melt = new MeltData(rs);
         DATA.put(Integer.valueOf(melt.skillId), melt);

         FastMap<Integer, FastTable<Integer>> meltItems = (FastMap<Integer, FastTable<Integer>>)CLASS_DATA.get(Integer.valueOf(melt.meltItemId));
         if (meltItems == null) {
           meltItems = new FastMap();
           CLASS_DATA.put(Integer.valueOf(melt.meltItemId), meltItems);
         }

         FastTable<Integer> itemList = (FastTable<Integer>)meltItems.get(Integer.valueOf(melt.classType));
         if (itemList == null) {
           itemList = new FastTable();
           meltItems.put(Integer.valueOf(melt.classType), itemList);
         }
         itemList.add(Integer.valueOf(melt.skillItemId));
       }
     } catch (SQLException e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public void reload() {
     DATA.clear();
     load();
   }
 }


