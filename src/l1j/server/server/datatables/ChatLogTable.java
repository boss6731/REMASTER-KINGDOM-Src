 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.utils.SQLUtil;

 public class ChatLogTable
 {
   private final boolean[] loggingConfig = new boolean[16];

   private ChatLogTable() {
     loadConfig();
   }
   private static ChatLogTable _instance;
   private void loadConfig() {
     this.loggingConfig[0] = Config.LogStatus.LoggingChatNormal;
     this.loggingConfig[1] = Config.LogStatus.LoggingChatWhisper;
     this.loggingConfig[2] = Config.LogStatus.LoggingChatShout;
     this.loggingConfig[3] = Config.LogStatus.LoggingChatWorld;
     this.loggingConfig[4] = Config.LogStatus.LoggingChatClan;
     this.loggingConfig[11] = Config.LogStatus.LoggingChatParty;
     this.loggingConfig[13] = Config.LogStatus.LoggingChatCombined;
     this.loggingConfig[14] = Config.LogStatus.LoggingChatChatParty;
     this.loggingConfig[15] = true;
   }



   public static ChatLogTable getInstance() {
     if (_instance == null) {
       _instance = new ChatLogTable();
     }
     return _instance;
   }

   private boolean isLoggingTarget(int type) {
     return this.loggingConfig[type];
   }

   public void reroding() {
     this.loggingConfig[0] = Config.LogStatus.LoggingChatNormal;
     this.loggingConfig[1] = Config.LogStatus.LoggingChatWhisper;
     this.loggingConfig[2] = Config.LogStatus.LoggingChatShout;
     this.loggingConfig[3] = Config.LogStatus.LoggingChatWorld;
     this.loggingConfig[4] = Config.LogStatus.LoggingChatClan;
     this.loggingConfig[11] = Config.LogStatus.LoggingChatParty;
     this.loggingConfig[13] = Config.LogStatus.LoggingChatCombined;
     this.loggingConfig[14] = Config.LogStatus.LoggingChatChatParty;
     this.loggingConfig[15] = true;
     System.out.println("ChatLogTable 리로드");
   }


   public void storeChat(L1PcInstance pc, L1PcInstance target, String text, int type) {
     if (!isLoggingTarget(type)) {
       return;
     }









     Connection con = null;
     PreparedStatement pstm = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       if (target != null) {
         pstm = con.prepareStatement("INSERT INTO log_chat (account_name, char_id, name, clan_id, clan_name, locx, locy, mapid, type, target_account_name, target_id, target_name, target_clan_id, target_clan_name, target_locx, target_locy, target_mapid, content, datetime) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE())");
         pstm.setString(1, pc.getAccountName());
         pstm.setInt(2, pc.getId());
         pstm.setString(3, pc.getName());
         pstm.setInt(4, pc.getClanid());
         pstm.setString(5, pc.getClanname());
         pstm.setInt(6, pc.getX());
         pstm.setInt(7, pc.getY());
         pstm.setInt(8, pc.getMapId());
         pstm.setInt(9, type);
         pstm.setString(10, target.getAccountName());
         pstm.setInt(11, target.getId());
         pstm.setString(12, target.getName());
         pstm.setInt(13, target.getClanid());
         pstm.setString(14, target.getClanname());
         pstm.setInt(15, target.getX());
         pstm.setInt(16, target.getY());
         pstm.setInt(17, target.getMapId());
         pstm.setString(18, text);
       } else {
         pstm = con.prepareStatement("INSERT INTO log_chat (account_name, char_id, name, clan_id, clan_name, locx, locy, mapid, type, content, datetime) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE())");
         pstm.setString(1, pc.getAccountName());
         pstm.setInt(2, pc.getId());
         pstm.setString(3, pc.getName());
         pstm.setInt(4, pc.getClanid());
         pstm.setString(5, pc.getClanname());
         pstm.setInt(6, pc.getX());
         pstm.setInt(7, pc.getY());
         pstm.setInt(8, pc.getMapId());
         pstm.setInt(9, type);
         pstm.setString(10, text);
       }
       pstm.execute();
     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }


