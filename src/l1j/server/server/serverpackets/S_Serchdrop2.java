 package l1j.server.server.serverpackets;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.utils.SQLUtil;



 public class S_Serchdrop2
   extends ServerBasePacket
 {
   private static final String S_Serchdrop2 = "[C] S_Serchdrop2";
   private static Logger _log = Logger.getLogger(S_Serchdrop2.class.getName());

   int[] itemid = new int[30];

   String[] itemname = new String[30];

   public S_Serchdrop2(int itemid) {
     buildPacket(itemid);
   }

   private void buildPacket(int npcid) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     Connection con1 = null;
     PreparedStatement pstm1 = null;
     ResultSet rs1 = null;

     int i = 0;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT `itemId` FROM `droplist` WHERE mobId=? ORDER BY `itemId` DESC LIMIT 30");
       pstm.setInt(1, npcid);
       rs = pstm.executeQuery();

       while (rs.next()) {
         this.itemid[i] = rs.getInt(1);
         this.itemname[i] = ItemTable.getInstance().getTemplate(this.itemid[i]).getName();
         i++;
       }

       writeC(248);
       writeD(0);
       writeS("梅蒂斯");
       writeS("下拉清單");
       writeS("");
       writeS("\r要搜尋的暴民 : " + NpcTable.getInstance().getTemplate(npcid).get_name() + "\n\n\r****** 掉落的物品 ******\n\n\r" + this.itemname[0] + " | " + this.itemname[1] + " | " + this.itemname[2] + " | " + this.itemname[3] + " | " + this.itemname[4] + " | " + this.itemname[5] + " | " + this.itemname[6] + " | " + this.itemname[7] + " | " + this.itemname[8] + " | " + this.itemname[9] + " | " + this.itemname[10] + " | " + this.itemname[11] + " | " + this.itemname[12] + " | " + this.itemname[13] + " | " + this.itemname[14] + " | " + this.itemname[15] + " | " + this.itemname[16] + " | " + this.itemname[17] + " | " + this.itemname[18] + " | " + this.itemname[19] + " | " + this.itemname[20] + " | " + this.itemname[21] + " | " + this.itemname[22] + " | " + this.itemname[23] + " | " + this.itemname[24] + " | " + this.itemname[25] + " | " + this.itemname[26] + " | " + this.itemname[27] + " | " + this.itemname[28] + " | " + this.itemname[29]);

     }
     catch (Exception e) {
       _log.log(Level.SEVERE, "S_Serchdrop2[]Error", e);
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
       SQLUtil.close(rs1);
       SQLUtil.close(pstm1);
       SQLUtil.close(con1);
     }
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[C] S_Serchdrop2";
   }
 }


