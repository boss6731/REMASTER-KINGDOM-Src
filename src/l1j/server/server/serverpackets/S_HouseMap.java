     package l1j.server.server.serverpackets;


     public class S_HouseMap
       extends ServerBasePacket
     {
       private static final String S_HOUSEMAP = "[S] S_HouseMap";

       public S_HouseMap(int objectId, String house_number) {
         buildPacket(objectId, house_number);
       }

       private void buildPacket(int objectId, String house_number) {
         int number = Integer.valueOf(house_number).intValue();

         writeC(18);
         writeD(objectId);
         writeD(number);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_HouseMap";
       }
     }


