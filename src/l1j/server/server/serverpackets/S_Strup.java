 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_Strup extends ServerBasePacket {
   private static final String _S__25_S_Strup = "[S] S_Strup";

   public S_Strup(L1PcInstance pc, int type, int time) {
     writeC(4);
     writeH(time);
     writeC(pc.getAbility().getTotalStr());
     writeC(pc.getInventory().getWeight100());
     writeC(0);
     writeD(type);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_Strup";
   }
 }


