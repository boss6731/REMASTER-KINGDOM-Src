         package l1j.server.server.command.executor;

         import java.sql.Connection;
         import java.sql.PreparedStatement;
         import java.sql.ResultSet;
         import java.sql.Timestamp;
         import java.util.StringTokenizer;
         import java.util.logging.Logger;
         import l1j.server.L1DatabaseFactory;
         import l1j.server.server.datatables.LetterTable;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_LetterList;
         import l1j.server.server.serverpackets.S_ServerMessage;
         import l1j.server.server.serverpackets.S_SkillSound;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;
         import l1j.server.server.utils.SQLUtil;



         public class L1LetterCommand
           implements L1CommandExecutor
         {
           private static Logger _log = Logger.getLogger(L1LetterCommand.class.getName());


           private static L1LetterCommand _instance;


           public static L1CommandExecutor getInstance() {
             return new L1LetterCommand();
           }


           public static void reload() {
             L1LetterCommand oldInstance = _instance;
             _instance = new L1LetterCommand();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               StringTokenizer st = new StringTokenizer(arg);
               String name = st.nextToken();
               int letter_id = Integer.parseInt(st.nextToken());






               if (name != null) {
                 WritePrivateMail(pc, name, letter_id);
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[角色名稱][編號]。"));
             }
           }


           private void WritePrivateMail(L1PcInstance sender, String receiverName, int letter_id) {
             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;


             try {
               Timestamp dTime = new Timestamp(System.currentTimeMillis());

               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT subject, content FROM letter_command WHERE id = ?");
               pstm.setInt(1, letter_id);
               rs = pstm.executeQuery();
               if (!rs.next()) {
                 sender.sendPackets((ServerBasePacket)new S_SystemMessage("該編號沒有任何內容。"));
                 return;
               }
               String subject = rs.getString("subject");
               String content = rs.getString("content");

               pstm.close();
               rs.close();

               if (subject == null || content == null) {
                 sender.sendPackets((ServerBasePacket)new S_SystemMessage("編號未登記標題或內容。"));

                 return;
               }
               L1PcInstance target = L1World.getInstance().getPlayer(receiverName);
               LetterTable.getInstance().writeLetter(949, dTime, sender.getName(), receiverName, 0, subject, content);
               sendMessageToReceiver(target, sender, 0, 50);








               sender.sendPackets((ServerBasePacket)new S_SystemMessage(receiverName + "我寄了一封信給你。"));
             }
             catch (Exception e) {
               sender.sendPackets((ServerBasePacket)new S_SystemMessage(".回覆錯誤"));
             } finally {

               SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }

           private void LetterList(L1PcInstance pc, int type, int count) {
             pc.sendPackets((ServerBasePacket)new S_LetterList(pc, type, count));
           }

           private void sendMessageToReceiver(L1PcInstance receiver, L1PcInstance sender, int type, int MAILBOX_SIZE) {
             if (receiver != null && receiver.getOnlineStatus() != 0) {
               LetterList(receiver, type, MAILBOX_SIZE);
               receiver.sendPackets((ServerBasePacket)new S_SkillSound(receiver.getId(), 1091));
               receiver.sendPackets((ServerBasePacket)new S_ServerMessage(428));
               sender.sendPackets((ServerBasePacket)new S_LetterList(sender, type, MAILBOX_SIZE));
               return;
             }
           }
         }


