 package l1j.server.server.serverpackets;

 public class S_Unknown2
   extends ServerBasePacket
 {
   public S_Unknown2(int type) {
     writeC(108);
     switch (type) {
       case 0:
         writeC(61);
         writeD(0);
         writeC(0);
         writeC(41);
         break;
       case 1:
         writeC(42);
         writeD(0);
         writeH(0);
         break;
     }
   }



   public byte[] getContent() {
     return getBytes();
   }
 }


