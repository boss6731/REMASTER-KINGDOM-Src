 package MJNCoinSystem.Commands;

 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.MJTemplate.Command.MJCommand;
 import l1j.server.MJTemplate.MJObjectWrapper;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.datatables.LetterTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_LetterList;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public abstract class MJNCoinExecutor implements MJCommand {
   public abstract String get_command_name();

   protected void do_write_letter_command(L1PcInstance pc, final int notify_id) {
     final MJObjectWrapper<String> subject = new MJObjectWrapper();
     final MJObjectWrapper<String> content = new MJObjectWrapper();
     Selector.exec("select * from letter_command where id=?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, notify_id);
           }


           public void result(ResultSet rs) throws Exception {
             if (rs.next()) {
               subject.value = rs.getString("subject");
               content.value = rs.getString("content");
             } else {
               subject.value = "";
               content.value = "";
             }
           }
         });
     if (MJString.isNullOrEmpty((String)subject.value) && MJString.isNullOrEmpty((String)content.value)) {
       try {
         throw new Exception(String.format("未找到命令字母。 ID：%d堆疊追蹤", new Object[] { Integer.valueOf(notify_id) }));
       } catch (Exception e) {
         e.printStackTrace();

         return;
       }
     }
     String current_date = MJString.get_current_datetime();
     do_write_letter(pc.getName(), current_date, (String)subject.value, (String)content.value);
   }

   public static void do_write_letter_togm(String generate_date, String subject, String content) {
     do_write_letter("REMASTER KINGDOM", generate_date, subject, content);
   }

   public static void do_write_letter(String receiver, String generate_date, String subject, String content) {
     int id = LetterTable.getInstance().writeLetter(949, generate_date, "REMASTER KINGDOM", receiver, 0, subject, content);
     L1PcInstance pc = L1World.getInstance().getPlayer(receiver);
     if (pc != null) {
       pc.sendPackets((ServerBasePacket)new S_LetterList(80, id, 0, "REMASTER KINGDOM", subject));

       pc.send_effect(1091);
       pc.sendPackets(428);
     }
   }
 }


