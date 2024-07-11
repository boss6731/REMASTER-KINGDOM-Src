         package l1j.server.server.command.executor;

         import java.sql.Connection;
         import java.sql.PreparedStatement;
         import l1j.server.L1DatabaseFactory;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_LetterList;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;
         import l1j.server.server.utils.SQLUtil;

         public class L1RemoveLetter
           implements L1CommandExecutor {
           public static L1CommandExecutor getInstance() {
             return new L1RemoveLetter();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               checkLetter(pc.getName());
               pc.sendPackets((ServerBasePacket)new S_LetterList(pc, 0, 200));
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("該信件已被刪除。"));
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("請輸入.刪除字母。"));
             }
           }

           public void checkLetter(String name) {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("DELETE FROM letter WHERE receiver = ?");
               pstm.setString(1, name);
               pstm.execute();
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }
         }


