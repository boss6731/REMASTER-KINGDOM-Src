     package l1j.server.server.serverpackets;

     import java.sql.Connection;
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import java.util.logging.Level;
     import java.util.logging.Logger;
     import l1j.server.L1DatabaseFactory;
     import l1j.server.server.model.Instance.L1NpcInstance;
     import l1j.server.server.utils.SQLUtil;


     public class S_AuctionSystemBoard
       extends ServerBasePacket
     {
       private static final String S_AuctionSystemBoard = "[S] S_AuctionSystemBoard";
       private static Logger _log = Logger.getLogger(S_AuctionSystemBoard.class.getName());

       private byte[] _byte = null;

       public S_AuctionSystemBoard(L1NpcInstance board) {
         buildPacket(board, 0);
       }

       public S_AuctionSystemBoard(L1NpcInstance board, int number) {
         buildPacket(board, number);
       }

       public S_AuctionSystemBoard(int number) {
         buildPacket(number);
       }

       private void buildPacket(L1NpcInstance board, int number) {
         int count = 0;
         String[][] db = (String[][])null;
         int[] id = null;
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         try {
           db = new String[8][4];
           id = new int[8];
           con = L1DatabaseFactory.getInstance().getConnection();
           pstm = con.prepareStatement("SELECT * FROM Auction order by id desc");
           rs = pstm.executeQuery();
           while (rs.next() && count < 8) {
             if (rs.getInt(1) <= number || number == 0) {
               id[count] = rs.getInt(1);
               db[count][0] = rs.getString(2);
               db[count][1] = rs.getString(8);
               db[count][2] = rs.getString(5);
               db[count][3] = rs.getString(4);
               count++;
             }
           }
         } catch (Exception e) {
           _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
         } finally {
           SQLUtil.close(rs);
           SQLUtil.close(pstm);
           SQLUtil.close(con);
         }
         writeC(152);
         writeC(0);
         writeD(board.getId());
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(127);
         writeH(count);
         writeH(300);
         for (int i = 0; i < count; i++) {
           writeD(id[i]);
           writeS(db[i][0]);
           writeS(db[i][1]);
           writeS("" + db[i][2] + " " + db[i][3]);
         }
       }

         public void buildPacket(int number) {
             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;
             try {
                 con = L1DatabaseFactory.getInstance().getConnection();
                 pstm = con.prepareStatement("SELECT * FROM Auction WHERE id=?");
                 pstm.setInt(1, number);
                 rs = pstm.executeQuery();
                 while (rs.next()) {
                     writeC(248);
                     writeD(number);
                     writeS(rs.getString(2));
                     writeS("" + rs.getString(5) + " " + rs.getString(4));
                     writeS(rs.getString(8));
                     writeS("▣內容 點擊時可能會斷線▣\n\n" + rs.getString(4) + " : " + rs.getString(5) + " 個\n銷售金額 : " + rs.getString(6) + " 元\n\n▣購買方法▣\n\n.輸入購買申請（公告號碼）後\n確認信件內容.");

                 }
             } catch (SQLException e) {
                 // 處理 SQL 例外
                 e.printStackTrace();
             } finally {
                 // 確保資源釋放
                 if (rs != null) {
                     try {
                         rs.close();
                     } catch (SQLException e) {
                         e.printStackTrace();
                     }
                 }
                 if (pstm != null) {
                     try {
                         pstm.close();
                     } catch (SQLException e) {
                         e.printStackTrace();
                     }
                 }
                 if (con != null) {
                     try {
                         con.close();
                     } catch (SQLException e) {
                         e.printStackTrace();
                     }
                 }
             }
         }

         }
         catch (Exception e) {
           _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
         } finally {
           SQLUtil.close(rs);
           SQLUtil.close(pstm);
           SQLUtil.close(con);
         }
       }


       public byte[] getContent() {
         if (this._byte == null) {
           this._byte = getBytes();
         }
         return this._byte;
       }


       public String getType() {
         return "[S] S_AuctionSystemBoard";
       }
     }


