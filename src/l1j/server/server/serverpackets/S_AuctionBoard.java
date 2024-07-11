package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.utils.SQLUtil;


public class S_AuctionBoard
        extends ServerBasePacket
{
    private static final String S_AUCTIONBOARD = "[S] S_AuctionBoard";

    public S_AuctionBoard(L1NpcInstance board) {
        buildPacket(board);
    }

    private void buildPacket(L1NpcInstance board) {
        ArrayList<Integer> houseList = new ArrayList<>();
        int houseId = 0;
        int count = 0;
        int[] id = null;
        String[] name = null;
        int[] area = null;
        int[] month = null;
        int[] day = null;
        int[] hour = null;
        int[] price = null;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM board_auction");
            rs = pstm.executeQuery();
            while (rs.next()) {
                houseId = rs.getInt(1);
                if (board.getX() == 33424 && board.getY() == 32824) {
                    if (houseId >= 262145 && houseId <= 262189) {
                        houseList.add(Integer.valueOf(houseId));
                        count++;
                    }  continue;
                }  if (board.getX() == 33585 && board.getY() == 33235) {
                    if (houseId >= 327681 && houseId <= 327691) {
                        houseList.add(Integer.valueOf(houseId));
                        count++;
                    }  continue;
                }  if (board.getX() == 33959 && board.getY() == 33253) {
                    if (houseId >= 458753 && houseId <= 458819) {
                        houseList.add(Integer.valueOf(houseId));
                        count++;
                    }  continue;
                }  if (board.getX() == 32612 && board.getY() == 32775 &&
                        houseId >= 65537 && houseId <= 65542) {
                    houseList.add(Integer.valueOf(houseId));
                    count++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }

        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            id = new int[count];
            name = new String[count];
            area = new int[count];
            month = new int[count];
            day = new int[count];
            hour = new int[count];
            price = new int[count];
            Calendar cal = null;
            for (int j = 0; j < count; j++) {
                pstm = con.prepareStatement("SELECT * FROM board_auction WHERE house_id=?");
                houseId = ((Integer)houseList.get(j)).intValue();
                pstm.setInt(1, houseId);
                rs = pstm.executeQuery();
                while (rs.next()) {
                    id[j] = rs.getInt(1);
                    name[j] = rs.getString(2);
                    area[j] = rs.getInt(3);
                    cal = timestampToCalendar((Timestamp)rs.getObject(4));
                    month[j] = cal.get(2) + 1;
                    day[j] = cal.get(5);
                    hour[j] = cal.get(11);
                    price[j] = rs.getInt(5);
                }
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }

        writeC(0);
        writeD(board.getId());

        writeH(count);

        for (int i = 0; i < count; i++) {
            writeD(id[i]);
            writeS(name[i]);
            writeH(area[i]);
            writeC(month[i]);
            writeC(day[i]);
            writeH(hour[i]);
            writeD(price[i]);
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
        return "[S] S_AuctionBoard";
    }
}


