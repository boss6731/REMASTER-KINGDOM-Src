     package l1j.server.server.serverpackets;


     public class S_Coma
       extends ServerBasePacket
     {
       public S_Coma(int j, int time) {
         writeC(108);
         writeC(20);
         for (int i = 0; i < 64; ) { writeC(0); i++; }
          writeC((time + 16) / 32);
         writeC(j);
         writeC(20);
         writeD(0);
         writeC(0);
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


