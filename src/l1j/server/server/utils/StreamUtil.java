 package l1j.server.server.utils;

 import java.io.Closeable;
 import java.io.IOException;




 public class StreamUtil
 {
   public static void close(Closeable... closeables) {
     for (Closeable c : closeables) {
       try {
         if (c != null) {
           c.close();
         }
       } catch (IOException e) {
         e.printStackTrace();
       }
     }
   }
 }


