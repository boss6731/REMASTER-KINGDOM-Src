     package l1j.server.server.serverpackets;

     import java.sql.Connection;
     import java.sql.ResultSet;
     import java.sql.Statement;
     import l1j.server.L1DatabaseFactory;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.utils.SQLUtil;


     public class S_ClanWareHouseHistory
       extends ServerBasePacket
     {
       private static final String S_ClanWareHouseHistory = "[C] S_ClanWareHouseHistory";

       public S_ClanWareHouseHistory(L1PcInstance pc) {
         buildPacket(pc);
       }

       private void buildPacket(L1PcInstance pc) {
         Connection con = null;
         Statement pstm = null;
         ResultSet rs = null;
         int time = 0;
         int realtime = (int)(System.currentTimeMillis() / 1000L);
         String itemName = null;
         String itemIndex = null;
         String charName = null;
         int itemCount = 0;

         try {
           con = L1DatabaseFactory.getInstance().getConnection();
           pstm = con.createStatement();
           rs = pstm.executeQuery("SELECT * FROM clan_warehousehistory WHERE clan_id=" + pc.getClanid() + " order by elapsed_time desc");
           rs.last();
           int count = rs.getRow();
           rs.beforeFirst();
           writeC(108);
           writeC(117);
           writeD(count);
           while (rs.next()) {
             time = (realtime - rs.getInt("elapsed_time")) / 60;
             charName = rs.getString("char_name");
             itemName = rs.getString("item_name");
             itemCount = rs.getInt("item_count");
             itemIndex = rs.getString("item_getorput");
             writeS(charName);
             if (itemIndex.equalsIgnoreCase("交給你了.")) {
               writeC(0);
             } else {
               writeC(1);
             }
             writeS(itemName);
             writeD(itemCount);
             writeD(time);
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
         return "[C] S_ClanWareHouseHistory";
       }
     }


