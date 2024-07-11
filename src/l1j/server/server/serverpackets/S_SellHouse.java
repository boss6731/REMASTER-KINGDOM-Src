 package l1j.server.server.serverpackets;


 public class S_SellHouse
   extends ServerBasePacket
 {
   private static final String S_SELLHOUSE = "[S] S_SellHouse";

   public S_SellHouse(int objectId, String houseNumber) {
     buildPacket(objectId, houseNumber);
   }

   private void buildPacket(int objectId, String houseNumber) {
     writeC(224);
     writeD(objectId);
     writeD(0);
     writeD(100000);
     writeD(100000);
     writeD(2000000000);
     writeH(0);
     writeS("agsell");
     writeS("agsell " + houseNumber);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_SellHouse";
   }
 }


