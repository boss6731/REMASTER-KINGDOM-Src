         package l1j.server.server.command.executor;

         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.trap.L1WorldTraps;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;


         public class L1ResetTrap
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1ResetTrap();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             L1WorldTraps.getInstance().resetAllTraps();
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("陷阱已重新定位"));
           }
         }


