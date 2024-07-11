         package l1j.server.server.command.executor;

         import java.util.logging.Logger;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1Object;
         import l1j.server.server.model.L1World;
         import l1j.server.server.model.skill.L1SkillUse;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;








         public class L1RessBuff
           implements L1CommandExecutor
         {
           private static Logger _log = Logger.getLogger(L1RessBuff.class.getName());




           public static L1CommandExecutor getInstance() {
             return new L1RessBuff();
           }



           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               int[] allBuffSkill = { 26, 42, 48, 168, 22000, 7678, 4914, 50007 };
               for (L1PcInstance tg : L1World.getInstance().getVisiblePlayer((L1Object)pc)) {
                 tg.setBuffnoch(1);
                 L1SkillUse l1skilluse = new L1SkillUse();
                 for (int i = 0; i < allBuffSkill.length; i++) {
                   l1skilluse.handleCommands(tg, allBuffSkill[i], tg.getId(), tg.getX(), tg.getY(), null, 0, 4);
                 }

                 tg.setBuffnoch(0);
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 命令錯誤"));
             }
           }
         }


