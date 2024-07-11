         package l1j.server.server.serverpackets;

         import java.sql.Connection;
         import java.sql.PreparedStatement;
         import java.sql.ResultSet;
         import l1j.server.L1DatabaseFactory;
         import l1j.server.server.utils.SQLUtil;


         public class S_ApplyAuction
           extends ServerBasePacket
         {
           private static final String S_APPLYAUCTION = "[S] S_ApplyAuction";

           public S_ApplyAuction(int objectId, String houseNumber) {
             buildPacket(objectId, houseNumber);
           }

           private void buildPacket(int objectId, String houseNumber) {
             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT * FROM board_auction WHERE house_id=?");
               int number = Integer.valueOf(houseNumber).intValue();
               pstm.setInt(1, number);
               rs = pstm.executeQuery();
               while (rs.next()) {
                 int nowPrice = rs.getInt(5);
                 int bidderId = rs.getInt(10);
                 writeC(224);
                 writeD(objectId);
                 writeD(0);
                 if (bidderId == 0) {
                   writeD(nowPrice);
                   writeD(nowPrice);
                 } else {
                   writeD(nowPrice + 1);
                   writeD(nowPrice + 1);
                 }
                 writeD(2000000000);
                 writeH(0);
                 writeS("agapply");
                 writeS("agapply " + houseNumber);
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
             return "[S] S_ApplyAuction";
           }
         }


