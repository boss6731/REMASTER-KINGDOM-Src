 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_OutputRawString;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.SQLUtil;

 public class ServerExplainTable {
   private static Logger _log = Logger.getLogger(ServerExplainTable.class.getName());

   private static ServerExplainTable _instance;

   public static ServerExplainTable getInstance() {
     if (_instance == null) {
       _instance = new ServerExplainTable();
     }
     return _instance;
   }








   public void server_Explain(L1PcInstance pc, int num) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     String sub = null;
     String text = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM server_explain WHERE num=?");
       pstm.setInt(1, num);
       rs = pstm.executeQuery();
       while (rs.next()) {
         sub = rs.getString("subject");
         text = rs.getString("content");
       }
       pc.sendPackets((ServerBasePacket)new S_OutputRawString(pc.getId(), sub, text));
     } catch (Exception e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }


