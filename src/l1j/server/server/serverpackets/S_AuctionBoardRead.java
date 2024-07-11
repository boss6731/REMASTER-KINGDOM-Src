     package l1j.server.server.serverpackets;

     import java.sql.Connection;
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import java.sql.Timestamp;
     import java.util.Calendar;
     import l1j.server.L1DatabaseFactory;
     import l1j.server.server.utils.SQLUtil;




     public class S_AuctionBoardRead
       extends ServerBasePacket
     {
       private static final String S_AUCTIONBOARDREAD = "[S] S_AuctionBoardRead";

       public S_AuctionBoardRead(int objectId, String house_number) {
         buildPacket(objectId, house_number);
       }

       private void buildPacket(int objectId, String house_number) {
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         try {
           int number = Integer.valueOf(house_number).intValue();
           con = L1DatabaseFactory.getInstance().getConnection();
           pstm = con.prepareStatement("SELECT * FROM board_auction WHERE house_id=?");
           pstm.setInt(1, number);
           rs = pstm.executeQuery();
           Calendar cal = null;
           while (rs.next()) {
             writeC(144);
             writeD(objectId);
             writeS("agsel");
             writeS(house_number);
             writeH(9);
             writeS(rs.getString(2));
             writeS(rs.getString(6));
             writeS(String.valueOf(rs.getString(3)));
             writeS(rs.getString(7));
             writeS(rs.getString(9));
             writeS(String.valueOf(rs.getInt(5)));
             cal = timestampToCalendar((Timestamp)rs.getObject(4));
             int month = cal.get(2) + 1;
             int day = cal.get(5);
             int hour = cal.get(11);
             writeS(String.valueOf(month));
             writeS(String.valueOf(day));
             writeS(String.valueOf(hour));
           }
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           SQLUtil.close(rs);
           SQLUtil.close(pstm);
           SQLUtil.close(con);
         }
       }

       private Calendar timestampToCalendar(Timestamp ts) {
         Calendar cal = Calendar.getInstance();
         cal.setTimeInMillis(ts.getTime());
         return cal;
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_AuctionBoardRead";
       }
     }


