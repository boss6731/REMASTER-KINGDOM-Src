 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1MobGroup;
 import l1j.server.server.utils.SQLUtil;




















 public class MobGroupTable
 {
   private static Logger _log = Logger.getLogger(MobGroupTable.class
       .getName());

   private static MobGroupTable _instance;

   private final HashMap<Integer, L1MobGroup> _mobGroupIndex = new HashMap<>();


   public static MobGroupTable getInstance() {
     if (_instance == null) {
       _instance = new MobGroupTable();
     }
     return _instance;
   }

   private MobGroupTable() {
     loadMobGroup();
   }

   private void loadMobGroup() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       int mobGroupId = 0;

       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM mobgroup");
       rs = pstm.executeQuery();
       while (rs.next()) {
         L1MobGroup mobGroup = new L1MobGroup();
         mobGroupId = rs.getInt("id");
         mobGroup.setId(mobGroupId);
         mobGroup.setRemoveGroupIfLeaderDie(rs
             .getBoolean("remove_group_if_leader_die"));
         mobGroup.setLeaderId(rs.getInt("leader_id"));
         mobGroup.setMinion1Id(rs.getInt("minion1_id"));
         mobGroup.setMinion1Count(rs.getInt("minion1_count"));
         mobGroup.setMinion2Id(rs.getInt("minion2_id"));
         mobGroup.setMinion2Count(rs.getInt("minion2_count"));
         mobGroup.setMinion3Id(rs.getInt("minion3_id"));
         mobGroup.setMinion3Count(rs.getInt("minion3_count"));
         mobGroup.setMinion4Id(rs.getInt("minion4_id"));
         mobGroup.setMinion4Count(rs.getInt("minion4_count"));
         mobGroup.setMinion5Id(rs.getInt("minion5_id"));
         mobGroup.setMinion5Count(rs.getInt("minion5_count"));
         mobGroup.setMinion6Id(rs.getInt("minion6_id"));
         mobGroup.setMinion6Count(rs.getInt("minion6_count"));
         mobGroup.setMinion7Id(rs.getInt("minion7_id"));
         mobGroup.setMinion7Count(rs.getInt("minion7_count"));
         mobGroup.setMinion8Id(rs.getInt("minion8_id"));
         mobGroup.setMinion8Count(rs.getInt("minion8_count"));
         this._mobGroupIndex.put(Integer.valueOf(mobGroupId), mobGroup);
       }
         _log.config("已加載 MOB 群組列表 " + this._mobGroupIndex.size() + "條");
// _log.config("已加載 MOB 群組列表 " + this._mobGroupIndex.size() + " 條");

     } catch (Exception e) {
         _log.log(Level.SEVERE, "創建 mobgroup 表時出錯", e);
// _log.log(Level.SEVERE, "創建 mobgroup 表時出錯", e);

     } finally {
         SQLUtil.close(rs);
         SQLUtil.close(pstm);
         SQLUtil.close(con);
// 調用 SQLUtil 的 close 方法，關閉 rs、pstm 和 con 以釋放資源
     }
   }

   public L1MobGroup getTemplate(int mobGroupId) {
     return this._mobGroupIndex.get(Integer.valueOf(mobGroupId));
   }
 }


