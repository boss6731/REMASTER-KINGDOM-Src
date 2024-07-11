     package l1j.server.server.serverpackets;

     import java.sql.Connection;
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import l1j.server.L1DatabaseFactory;
     import l1j.server.server.utils.SQLUtil;

     public class S_CharacterConfig
       extends ServerBasePacket
     {
       private static final String S_CHARACTER_CONFIG = "[S] S_CharacterConfig";

       public S_CharacterConfig(int objectId) {
         buildPacket(objectId);
       }

       private void buildPacket(int objectId) {
         int length = 0;
         byte[] data = null;
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         try {
           con = L1DatabaseFactory.getInstance().getConnection();
           pstm = con.prepareStatement("SELECT * FROM character_config WHERE object_id=?");
           pstm.setInt(1, objectId);
           rs = pstm.executeQuery();
           while (rs.next()) {
             length = rs.getInt(2);
             data = rs.getBytes(3);
           }
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           SQLUtil.close(rs);
           SQLUtil.close(pstm);
           SQLUtil.close(con);
         }

         if (length != 0) {
           writeC(108);
           writeC(41);
           writeD(length);
           writeByte(data);
           writeH(0);
         } else {
           writeC(108);
           writeC(41);
           writeD(0);
           writeH(0);
         }
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_CharacterConfig";
       }
     }


