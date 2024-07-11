         package l1j.server.server.command.executor;

         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.trap.L1WorldTraps;



         public class L1ReloadTrap
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1ReloadTrap();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             L1WorldTraps.reloadTraps();
           }
         }


