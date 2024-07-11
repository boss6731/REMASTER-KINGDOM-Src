 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_Karma
   extends ServerBasePacket
 {
   private static final String _TYPE = "[S] S_Karma";

   public S_Karma(L1PcInstance pc) {
     writeC(108);
     writeC(87);


     writeD(pc.getKarma());
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_Karma";
   }
 }


