     package l1j.server.server.serverpackets;

     import java.sql.Connection;
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import l1j.server.L1DatabaseFactory;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.utils.SQLUtil;



     public class S_FairlyConfig
       extends ServerBasePacket
     {
       private static final String S_CHARACTER_CONFIG = "[S] S_CharacterConfig";

       public S_FairlyConfig(L1PcInstance pc) {
         buildPacket(pc);
       }

       private void buildPacket(L1PcInstance pc) {
         byte[] data = null;
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         int ok = 0;
         try {
           con = L1DatabaseFactory.getInstance().getConnection();

           pstm = con.prepareStatement("SELECT * FROM character_Fairly_Config WHERE object_id=?");
           pstm.setInt(1, pc.getId());
           rs = pstm.executeQuery();
           while (rs.next()) {
             data = rs.getBytes(2);
             ok = 1;
           }
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           SQLUtil.close(rs);
           SQLUtil.close(pstm);
           SQLUtil.close(con);
         }
         writeC(108);
         writeC(188);
         if (ok != 0) {
           writeByte(data);

           pc.精靈訊息 = data;
         } else {
           for (int i = 0; i < 512; i++) {
             writeC(0);
           }
         }
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_CharacterConfig";
       }
     }


