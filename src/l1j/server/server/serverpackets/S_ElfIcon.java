     package l1j.server.server.serverpackets;


     public class S_ElfIcon
       extends ServerBasePacket
     {
       public S_ElfIcon(int a, int b, int c, int d) {
         writeC(108);
         writeC(21);
         writeC(a);
         writeC(b);
         writeC(c);
         writeC(d);
         writeC(0);
         writeC(0);
       }



       public byte[] getContent() {
         return getBytes();
       }
     }


