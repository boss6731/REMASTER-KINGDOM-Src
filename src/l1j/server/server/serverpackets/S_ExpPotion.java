     package l1j.server.server.serverpackets;



     public class S_ExpPotion
       extends ServerBasePacket
     {
       public S_ExpPotion(int time) {
         writeC(108);
         writeC(20); int i;
         for (i = 0; i < 45; ) { writeC(0); i++; }
          writeC((time + 8) / 16);
         for (i = 0; i < 16; ) { writeC(0); i++; }
          writeC(20);
         writeD(0);
         writeC(0);
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


