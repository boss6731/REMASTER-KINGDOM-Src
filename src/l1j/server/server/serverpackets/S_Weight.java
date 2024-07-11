 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_Weight extends ServerBasePacket {
   private static final String S_Weight = "[S] S_Weight";

   public S_Weight(L1PcInstance pc) {
     writeC(19);
     writeC(229);
     writeC(1);
     writeC(8);
     writeC(pc.getInventory().getWeight100());
     writeC(16);
     write7B(pc.getInventory().getWeight());
     writeC(24);
     write7B(pc.getMaxWeight());
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_Weight";
   }
 }


