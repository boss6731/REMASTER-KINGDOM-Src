         package l1j.server.server.command.executor;

         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.skill.L1BuffUtil;


         public class L1Speed
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Speed();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               L1BuffUtil.haste(pc, 9999000);
               L1BuffUtil.brave(pc, 9999000);
             } catch (Exception e) {
               e.printStackTrace();
               pc.sendPackets(".速度指令錯誤");
             }
           }
         }


