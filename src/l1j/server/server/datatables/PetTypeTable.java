 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Map;
 import java.util.Set;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1PetType;
 import l1j.server.server.utils.IntRange;
 import l1j.server.server.utils.SQLUtil;



















 public class PetTypeTable
 {
   private static PetTypeTable _instance;
   private Map<Integer, L1PetType> _types = new HashMap<>();
   private Set<String> _defaultNames = new HashSet<>();

   public static void load() {
     _instance = new PetTypeTable();
   }

   public static PetTypeTable getInstance() {
     return _instance;
   }

   private PetTypeTable() {
     loadTypes();
   }

   private void loadTypes() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM pettypes");

       rs = pstm.executeQuery();
       IntRange hpUpRange = null;
       IntRange mpUpRange = null;
       while (rs.next()) {
         int baseNpcId = rs.getInt("BaseNpcId");
         String name = rs.getString("Name");
         int itemIdForTaming = rs.getInt("ItemIdForTaming");
         int hpUpMin = rs.getInt("HpUpMin");
         int hpUpMax = rs.getInt("HpUpMax");
         int mpUpMin = rs.getInt("MpUpMin");
         int mpUpMax = rs.getInt("MpUpMax");
         int npcIdForEvolving = rs.getInt("NpcIdForEvolving");
         int[] msgIds = new int[5];
         for (int i = 0; i < 5; i++) {
           msgIds[i] = rs.getInt("MessageId" + (i + 1));
         }
         int defyMsgId = rs.getInt("DefyMessageId");
         hpUpRange = new IntRange(hpUpMin, hpUpMax);
         mpUpRange = new IntRange(mpUpMin, mpUpMax);
         this._types.put(Integer.valueOf(baseNpcId), new L1PetType(baseNpcId, name, itemIdForTaming, hpUpRange, mpUpRange, npcIdForEvolving, msgIds, defyMsgId));


         this._defaultNames.add(name.toLowerCase());
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public L1PetType get(int baseNpcId) {
     return this._types.get(Integer.valueOf(baseNpcId));
   }

   public boolean isNameDefault(String name) {
     return this._defaultNames.contains(name.toLowerCase());
   }
 }


