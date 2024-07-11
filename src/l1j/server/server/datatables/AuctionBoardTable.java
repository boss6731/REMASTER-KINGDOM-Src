 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.Timestamp;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1AuctionBoard;
 import l1j.server.server.utils.SQLUtil;

 public class AuctionBoardTable
 {
   private final Map<Integer, L1AuctionBoard> _boards = new ConcurrentHashMap<>();

   private Calendar timestampToCalendar(Timestamp ts) {
     Calendar cal = Calendar.getInstance();
     cal.setTimeInMillis(ts.getTime());
     return cal;
   }

   public AuctionBoardTable() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM board_auction ORDER BY house_id");
       rs = pstm.executeQuery();
       L1AuctionBoard board = null;
       while (rs.next()) {
         board = new L1AuctionBoard();
         board.setHouseId(rs.getInt(1));
         board.setHouseName(rs.getString(2));
         board.setHouseArea(rs.getInt(3));
         board.setDeadline(timestampToCalendar((Timestamp)rs.getObject(4)));
         board.setPrice(rs.getInt(5));
         board.setLocation(rs.getString(6));
         board.setOldOwner(rs.getString(7));
         board.setOldOwnerId(rs.getInt(8));
         board.setBidder(rs.getString(9));
         board.setBidderId(rs.getInt(10));
         this._boards.put(Integer.valueOf(board.getHouseId()), board);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public L1AuctionBoard[] getAuctionBoardTableList() {
     return (L1AuctionBoard[])this._boards.values().toArray((Object[])new L1AuctionBoard[this._boards.size()]);
   }

   public L1AuctionBoard getAuctionBoardTable(int houseId) {
     return this._boards.get(Integer.valueOf(houseId));
   }

   public void insertAuctionBoard(L1AuctionBoard board) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("INSERT INTO board_auction SET house_id=?, house_name=?, house_area=?, deadline=?, price=?, location=?, old_owner=?, old_owner_id=?, bidder=?, bidder_id=?");
       pstm.setInt(1, board.getHouseId());
       pstm.setString(2, board.getHouseName());
       pstm.setInt(3, board.getHouseArea());
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String fm = formatter.format(board.getDeadline().getTime());
       pstm.setString(4, fm);
       pstm.setInt(5, board.getPrice());
       pstm.setString(6, board.getLocation());
       pstm.setString(7, board.getOldOwner());
       pstm.setInt(8, board.getOldOwnerId());
       pstm.setString(9, board.getBidder());
       pstm.setInt(10, board.getBidderId());
       pstm.execute();

       this._boards.put(Integer.valueOf(board.getHouseId()), board);
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public void updateAuctionBoard(L1AuctionBoard board) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("UPDATE board_auction SET house_name=?, house_area=?, deadline=?, price=?, location=?, old_owner=?, old_owner_id=?, bidder=?, bidder_id=? WHERE house_id=?");
       pstm.setString(1, board.getHouseName());
       pstm.setInt(2, board.getHouseArea());
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String fm = formatter.format(board.getDeadline().getTime());
       pstm.setString(3, fm);
       pstm.setInt(4, board.getPrice());
       pstm.setString(5, board.getLocation());
       pstm.setString(6, board.getOldOwner());
       pstm.setInt(7, board.getOldOwnerId());
       pstm.setString(8, board.getBidder());
       pstm.setInt(9, board.getBidderId());
       pstm.setInt(10, board.getHouseId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public void deleteAuctionBoard(int houseId) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM board_auction WHERE house_id=?");
       pstm.setInt(1, houseId);
       pstm.execute();

       this._boards.remove(Integer.valueOf(houseId));
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }


