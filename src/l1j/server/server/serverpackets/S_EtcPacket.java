     package l1j.server.server.serverpackets;


     public class S_EtcPacket
       extends ServerBasePacket
     {
       public S_EtcPacket(int type) {
         writeC(108);
         switch (type) {

           case 0:
             writeC(71);
             writeC(500);
             break;

           case 1:
             writeC(70);
             writeC(147);
             writeC(92);
             writeC(151);
             writeC(220);
             writeC(42);
             writeC(74);
             break;

           case 3:
             writeC(64);
             writeC(5);
             writeC(129);
             writeC(252);
             writeC(125);
             writeC(110);
             writeC(17);
             break;

           case 4:
             writeC(65);
             writeC(100);
             writeC(0);
             writeC(0);
             writeC(0);
             writeC(0);
             writeC(0);
             break;

           case 5:
             writeC(72);
             break;
         }
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


