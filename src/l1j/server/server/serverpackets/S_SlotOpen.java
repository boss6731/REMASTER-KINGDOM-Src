 package l1j.server.server.serverpackets;


 public class S_SlotOpen
   extends ServerBasePacket
 {
   public S_SlotOpen(int subType, int value) {
     writeC(43);
     writeC(67);
     writeD(subType);
     writeC(value);
     writeD(0);
     writeD(0);
     writeD(0);
     writeD(0);
     writeD(0);
     writeD(0);
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "S_SlotOpen";
   }
 }


