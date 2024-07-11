 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;


 public class PhoneTable
 {
   public void Phone(String char_id, String param) {
     Connection con = null;
     PreparedStatement pstm = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("INSERT INTO phone SET char_id = ?, phone_id = ?");
       pstm.setString(1, char_id);
       pstm.setString(2, param);
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {

       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }


