 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_SPMR
   extends ServerBasePacket
 {
   private static final String S_SPMR = "[S] S_S_SPMR";

   public S_SPMR(L1PcInstance pc) {
     buildPacket(pc);
   }

   private void buildPacket(L1PcInstance pc) {
     writeC(25);
     writeH(pc.getAbility().getSp() - pc.getAbility().getTrueSp());
     if (pc.getResistance() == null) {
       writeH(0);
     } else {
       writeH(pc.getResistance().getMr() - pc.getResistance().getBaseMr() - pc.getResistance().getLevelMr());
     }
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_S_SPMR";
   }
 }


