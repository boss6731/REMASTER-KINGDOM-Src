 package MJFX.Perform;

 import MJFX.MJFxController;
 import com.sun.management.OperatingSystemMXBean;
 import java.lang.management.ManagementFactory;
 import java.util.Calendar;
 import java.util.TimeZone;
 import javafx.application.Platform;
 import l1j.server.server.utils.SystemUtil;

 public class MJFxPerformReader implements Runnable {
   private MJFxPerformInfo m_cpu_info;

   public static void execute(MJFxPerformInfo cpu_info, MJFxPerformInfo mem_info, MJFxPerformInfo thread_info) {
     (new Thread(new MJFxPerformReader(cpu_info, mem_info, thread_info))).start();
   }


   private MJFxPerformInfo m_mem_info;
   private MJFxPerformInfo m_thread_info;

   private MJFxPerformReader(MJFxPerformInfo cpu_info, MJFxPerformInfo mem_info, MJFxPerformInfo thread_info) {
     this.m_cpu_info = cpu_info;
     this.m_mem_info = mem_info;
     this.m_thread_info = thread_info;
   }


   public void run() {
     try {
       while (true) {
         Thread.sleep(1000L);
         if (MJFxController.getInstance() == null) {
           continue;
         }
         Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
         String time = String.format("%02d:%02d:%02d", new Object[] { Integer.valueOf(cal.get(11)), Integer.valueOf(cal.get(12)), Integer.valueOf(cal.get(13)) });
         do_update(time,

             (int)(getUseCpu() * 100.0D),
             (int)SystemUtil.getUsedMemoryMB(),
             Thread.activeCount());
       }

     } catch (Exception e) {
       e.printStackTrace();
       return;
     }
   }
   private void do_update(String time, int cpu_usage, int mem_usage, int thread_count) {
     Platform.runLater(() -> {
           this.m_cpu_info.on_update(time, cpu_usage);
           this.m_mem_info.on_update(time, mem_usage);
           this.m_thread_info.on_update(time, thread_count);
         });
   }

   private double getUseCpu() {
     return ((OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad();
   }
 }


