 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;


 public class S_RenewLetter
   extends ServerBasePacket
 {
   private static final String S_RENEWLETTER = "[S] S_RenewLetter";

   public S_RenewLetter(L1PcInstance pc, int type, int id) {
     buildPacket(pc, type, id);
   }
   private void buildPacket(L1PcInstance pc, int type, int id) {
     writeC(99);
     writeC(type);
     writeD(id);
     writeC(1);
   }

   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_RenewLetter";
   }
 }


