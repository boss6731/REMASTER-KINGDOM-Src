 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_Teleport
   extends ServerBasePacket
 {
   private static final String S_TELEPORT = "[S] S_Teleport";

   public S_Teleport(L1PcInstance pc) {
     writeC(52);
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_Teleport";
   }
 }


