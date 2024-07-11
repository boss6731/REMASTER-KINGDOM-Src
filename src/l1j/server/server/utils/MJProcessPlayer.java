 package l1j.server.server.utils;

 import java.io.IOException;
 import java.io.InputStream;
 import java.io.OutputStream;
 import java.lang.management.ManagementFactory;
 import java.util.Calendar;
 import l1j.server.server.model.gametime.RealTimeClock;

 public class MJProcessPlayer {
   public static String getPid() {
     String s = ManagementFactory.getRuntimeMXBean().getName();
     return s.substring(0, s.indexOf("@"));
   }

   public static String dumpLog() {
     Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
     String path = String.format("dump\\[%02d-%02d-%02d]dump.txt", new Object[] { Integer.valueOf(cal.get(10)), Integer.valueOf(cal.get(12)), Integer.valueOf(cal.get(13)) });

     String[] command = { "cmd", "/C", "C:\\Program Files\\Java\\jdk1.8.0_144\\bin\\jstack", getPid(), ">", path };


     MJProcessPlayer mpp = new MJProcessPlayer();
     try {
       mpp.byRuntime(command);
       return path;
     } catch (Exception e) {
       e.printStackTrace();

       return "";
     }
   }
   public void byRuntime(String[] command) throws IOException, InterruptedException {
     Runtime runtime = Runtime.getRuntime();
     Process process = runtime.exec(command);
     printStream(process);
   }


   public void byProcessBuilder(String[] command) throws IOException, InterruptedException {
     ProcessBuilder builder = new ProcessBuilder(command);
     Process process = builder.start();
     printStream(process);
   }

   private void printStream(Process process) throws IOException, InterruptedException {
     process.waitFor();
     try (InputStream psout = process.getInputStream()) {
       copy(psout, System.out);
     }
   }

   public void copy(InputStream input, OutputStream output) throws IOException {
     byte[] buffer = new byte[1024];
     int n = 0;
     while ((n = input.read(buffer)) != -1)
       output.write(buffer, 0, n);
   }
 }


