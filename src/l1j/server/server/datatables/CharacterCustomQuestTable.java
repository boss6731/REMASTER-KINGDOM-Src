 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.Iterator;
 import java.util.Set;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.templates.CustomQuestUser;
 import l1j.server.server.templates.eCustomQuestType;
 import l1j.server.server.utils.SQLUtil;


 public class CharacterCustomQuestTable
 {
   public static void load(L1PcInstance pc) {
     pc.getCustomQuestList().clear();

     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_custom_quest WHERE char_id=?");
       pstm.setInt(1, pc.getId());
       rs = pstm.executeQuery();
       CustomQuestUser cqu = null;
       while (rs.next()) {
         int questid = rs.getInt("quest_id");
         eCustomQuestType type = eCustomQuestType.fromInt(rs.getInt("quest_type"));
         cqu = new CustomQuestUser(questid, type);

         cqu.setQuestState(rs.getInt("quest_state"));
         cqu.setSuccessCount(rs.getInt("success_count"));

         pc.addCustomQuest(questid, cqu);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static void save(L1PcInstance pc) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM character_custom_quest WHERE char_id = ?");
       pstm.setInt(1, pc.getId());
       pstm.execute();

       Set<Integer> keys = pc.getCustomQuestList().keySet();

       CustomQuestUser cm = null;
       for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext(); ) {
         int questid = ((Integer)iterator.next()).intValue();
         cm = (CustomQuestUser)pc.getCustomQuestList().get(Integer.valueOf(questid));
         String sql = "INSERT INTO character_custom_quest (char_id,char_name,quest_id,quest_state,success_count,quest_type)VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE char_id=?,char_name=?,quest_id=?,quest_state=?,success_count=?,quest_type=?";



         SQLUtil.execute(con, sql, new Object[] {
               Integer.valueOf(pc.getId()), pc.getName(), Integer.valueOf(cm.getQuestId()), Integer.valueOf(cm.getQuestState()), Integer.valueOf(cm.getSuccessCount()), Integer.valueOf(cm.getQuestType().toInt()),
               Integer.valueOf(pc.getId()), pc.getName(), Integer.valueOf(cm.getQuestId()), Integer.valueOf(cm.getQuestState()), Integer.valueOf(cm.getSuccessCount()), Integer.valueOf(cm.getQuestType().toInt()) });
       }


       pc.getCustomQuestList().clear();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm, con);
     }
   }
 }


