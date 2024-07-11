     package l1j.server.server.serverpackets;

     import java.io.BufferedInputStream;
     import java.io.File;
     import java.io.FileInputStream;
     import java.io.IOException;


     public class S_Emblem
       extends ServerBasePacket
     {
       private static final String S_EMBLEM = "[S] S_Emblem";
       private static final String PATH = "emblem/";

       public S_Emblem(int emblemId) {
         BufferedInputStream bis = null;
         FileInputStream fis = null;
         File readFile = null;
         try {
           String emblem_file = String.valueOf(emblemId);
           readFile = new File("emblem/" + emblem_file);
           if (readFile.exists()) {
             byte[] buff = null;
             fis = new FileInputStream(readFile);
             bis = new BufferedInputStream(fis);
             writeC(161);
             writeC(1);
             writeD(0);

             writeD(emblemId);

             buff = new byte[(int)fis.getChannel().size()];
             bis.read(buff, 0, buff.length);
             writeByte(buff);
           }
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           if (bis != null) {
             try {
               bis.close();
             } catch (IOException iOException) {}
             bis = null;
           }
           if (fis != null) {
             try {
               fis.close();
             } catch (IOException iOException) {}
             fis = null;
           }
           readFile = null;
         }
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_Emblem";
       }
     }


