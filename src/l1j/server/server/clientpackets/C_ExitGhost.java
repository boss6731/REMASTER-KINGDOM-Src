 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;






 public class C_ExitGhost
   extends ClientBasePacket
 {
   private static final String C_EXIT_GHOST = "[C] C_ExitGhost";

   public C_ExitGhost(byte[] decrypt, GameClient client) throws Exception {
     super(decrypt);

     L1PcInstance pc = client.getActiveChar();

     if (pc == null || !pc.isGhost()) {
       return;
     }

     pc.makeReadyEndGhost();
   }


   public String getType() {
     return "[C] C_ExitGhost";
   }
 }


