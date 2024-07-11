 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_Unknown1
   extends ServerBasePacket {
   public S_Unknown1(L1PcInstance pc) {
     writeC(117);
     writeC(3);
   }

   public byte[] getContent() {
     return getBytes();
   }
 }


