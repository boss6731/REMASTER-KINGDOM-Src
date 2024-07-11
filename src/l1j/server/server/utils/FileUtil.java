 package l1j.server.server.utils;

 import java.io.File;
 import l1j.server.MJTemplate.MJString;




 public class FileUtil
 {
   public static final String CONFIG = "./conf/";

   public static String getExtension(File file) {
     String fileName = file.getName();
     int index = fileName.lastIndexOf('.');
     if (index != -1) {
       return fileName.substring(index + 1, fileName.length());
     }
     return "";
   }

   public static String getNameWithoutExtension(File file) {
     String fileName = file.getName();
     int index = fileName.lastIndexOf('.');
     if (index != -1) {
       return fileName.substring(0, index);
     }
     return "";
   }



   public static String combine(String... pathes) {
     StringBuilder sb = new StringBuilder(256);
     int length = pathes.length - 1;
     for (int i = 0; i < length; i++) {
       String path = pathes[i];
       char c = path.charAt(path.length() - 1);
       sb.append(path);
       if (!MJString.isDirectorySeparatorChar(c)) {
         sb.append('/');
       }
     }
     sb.append(pathes[length]);
     return sb.toString();
   }



   public static String getExtension(String path) {
     if (MJString.isNullOrEmpty(path)) {
       return "";
     }
     int indexof = path.lastIndexOf('.');
     return (indexof == -1) ? "" : path.substring(indexof + 1, path.length());
   }



   public static String getFileName(String path) {
     if (MJString.isNullOrEmpty(path)) {
       return "";
     }
     return (new File(path)).getName();
   }



   public static String getFileNameWithoutExtension(String path) {
     String fileName = getFileName(path);
     if (MJString.isNullOrEmpty(fileName)) {
       return "";
     }
     int indexof = fileName.lastIndexOf('.');
     return (indexof == -1) ? fileName : fileName.substring(0, indexof);
   }
 }


