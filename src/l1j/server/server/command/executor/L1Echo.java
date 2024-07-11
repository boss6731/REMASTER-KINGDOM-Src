 package l1j.server.server.command.executor;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public class L1Echo
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1Echo();
   }


   public void execute(L1PcInstance pc, String cmdName, String arg) {
     pc.sendPackets((ServerBasePacket)new S_SystemMessage(arg));
   }
 }


