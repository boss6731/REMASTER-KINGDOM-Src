 package l1j.server.server.serverpackets;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.utils.SQLUtil;

 public class S_ReadLetter
   extends ServerBasePacket
 {
   private static final String S_READLETTER = "[S] S_ReadLetter";

   public S_ReadLetter(L1PcInstance pc, int type, int letterType, int id) {
     buildPacket(pc, type, letterType, id);
   }
   private void buildPacket(L1PcInstance pc, int type, int letterType, int id) {
     Connection con = null;
     PreparedStatement pstm = null;

     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM letter WHERE item_object_id = ? ");
       pstm.setInt(1, id);
       rs = pstm.executeQuery();

       writeC(99);
       writeC(type);

       if (rs.next()) {
         writeD(rs.getInt(1));
         writeSS(rs.getString(8));
         writeC(id);
         writeS(rs.getString(3));
         writeSS(rs.getString(7));
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_ReadLetter";
   }
 }


