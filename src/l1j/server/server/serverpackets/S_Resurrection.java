 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;


 public class S_Resurrection
   extends ServerBasePacket
 {
   private static final String _S__FF_RESURRECTION = "[S] S_Resurrection";

   public S_Resurrection(L1PcInstance target, L1PcInstance use, int type) {
     writeC(197);
     writeD(target.getId());
     writeC(type);
     writeD(use.getId());
     writeD(target.getClassId());
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_Resurrection";
   }
 }


