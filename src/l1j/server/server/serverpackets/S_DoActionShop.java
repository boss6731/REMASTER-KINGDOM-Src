     package l1j.server.server.serverpackets;



     public class S_DoActionShop
       extends ServerBasePacket
     {
       public S_DoActionShop(int object, int gfxid, byte[] message) {
         writeC(226);
         writeD(object);
         writeC(gfxid);
         writeByte(message);
       }

       public S_DoActionShop(int object, int gfxid, String message) {
         writeC(226);
         writeD(object);
         writeC(gfxid);
         writeS(message);
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


