 package l1j.server.server.serverpackets;

 import java.io.IOException;

 public class S_Notice
   extends ServerBasePacket
 {
   public S_Notice() {
     writeC(212);
     writeC(181);
     writeC(1);
   }


   public byte[] getContent() throws IOException {
     return getBytes();
   }
 }


