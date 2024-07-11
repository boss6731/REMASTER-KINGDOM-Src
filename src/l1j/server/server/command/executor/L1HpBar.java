         package l1j.server.server.command.executor;

         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1Character;
         import l1j.server.server.model.L1Object;
         import l1j.server.server.serverpackets.S_HPMeter;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;







         public class L1HpBar
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1HpBar();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             if (arg.equalsIgnoreCase("開")) {
               pc.setSkillEffect(2001, -1L);
               for (L1Object obj : pc.getKnownObjects()) {
                 if (isHpBarTarget(obj)) {
                   pc.sendPackets((ServerBasePacket)new S_HPMeter((L1Character)obj));
                 }
               }
             } else if (arg.equalsIgnoreCase("關")) {
               pc.removeSkillEffect(2001);

               for (L1Object obj : pc.getKnownObjects()) {
                 if (isHpBarTarget(obj)) {
                   pc.sendPackets((ServerBasePacket)new S_HPMeter(obj.getId(), 255, 255));
                 }
               }
             } else {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + "請輸入[開、關]。 "));
             }
           }

           public static boolean isHpBarTarget(L1Object obj) {
             if (obj instanceof l1j.server.server.model.Instance.L1MonsterInstance) {
               return true;
             }
             if (obj instanceof L1PcInstance) {
               return true;
             }
             if (obj instanceof l1j.server.server.model.Instance.L1SummonInstance) {
               return true;
             }
             if (obj instanceof l1j.server.server.model.Instance.L1PetInstance) {
               return true;
             }
             if (obj instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance)
               return true;
             return false;
           }
         }


