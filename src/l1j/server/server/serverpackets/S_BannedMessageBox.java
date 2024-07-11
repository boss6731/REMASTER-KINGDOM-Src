     package l1j.server.server.serverpackets;

     import java.io.IOException;
     import java.io.UnsupportedEncodingException;

     public class S_BannedMessageBox extends ServerBasePacket {
       public static S_BannedMessageBox do_banned(String message, String title) {
         S_BannedMessageBox mbox = new S_BannedMessageBox();
         try {
           byte[] buff = message.getBytes("MS949");
           mbox.writeD(buff.length);
           mbox.writeByte(buff);
           mbox.writeC(0);

           buff = title.getBytes("MS949");
           mbox.writeD(buff.length);
           mbox.writeByte(buff);
           mbox.writeC(0);
           mbox.writeC(0);
         } catch (UnsupportedEncodingException e) {

           e.printStackTrace();
         }
         return mbox;
       }

       private S_BannedMessageBox() {
         writeC(3);
       }



       public byte[] getContent() throws IOException {
         return getBytes();
       }
     }


