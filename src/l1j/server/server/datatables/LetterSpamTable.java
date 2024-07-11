 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.Server;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.utils.SQLUtil;

 public class LetterSpamTable
 {
   private static volatile LetterSpamTable uniqueInstance = null;




   public static LetterSpamTable getInstance() {
     if (uniqueInstance == null) {
       synchronized (Server.class) {
         if (uniqueInstance == null) {
           uniqueInstance = new LetterSpamTable();
         }
       }
     }
     return uniqueInstance;
   }







   public boolean spamLetterCheck(String senderName, String receiverName) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM letter_spam WHERE name = ? AND spamname = ?");
       pstm.setString(1, receiverName);
       pstm.setString(2, senderName);
       rs = pstm.executeQuery();
       if (rs.next()) return false;
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return true;
   }














































   public void spamLetterDel(L1PcInstance pc, String excludeName) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM letter_spam WHERE name = ? AND spamname = ?");
       pstm.setString(1, pc.getName());
       pstm.setString(2, excludeName);
       pstm.execute();
     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }







   public boolean spamList(String PcName, String spamname) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM letter_spam WHERE name = ? AND spamname = ?");
       pstm.setString(1, PcName);
       pstm.setString(2, spamname);
       rs = pstm.executeQuery();
       if (rs.next()) return true;
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return false;
   }







   public void loadSpamList(L1PcInstance pc) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     String spamname = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM letter_spam WHERE name = ?");
       pstm.setString(1, pc.getName());
       rs = pstm.executeQuery();
       while (rs.next()) {
         spamname = rs.getString("spamname");
       }
     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }


