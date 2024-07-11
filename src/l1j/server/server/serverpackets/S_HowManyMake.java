     package l1j.server.server.serverpackets;

     import java.io.IOException;



     public class S_HowManyMake
       extends ServerBasePacket
     {
       public S_HowManyMake(int objId, int max, String htmlId) {
         writeC(224);
         writeD(objId);
         writeD(0);
         writeD(0);
         writeD(0);
         writeD(max);
         writeH(0);
         writeS("request");
         writeS(htmlId);
       }


       public byte[] getContent() throws IOException {
         return getBytes();
       }
     }


