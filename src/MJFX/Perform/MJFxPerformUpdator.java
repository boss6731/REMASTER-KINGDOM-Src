 package MJFX.Perform;

 import MJFX.MJFxController;
 import MJFX.UIAdapter.MJPerformAdapter;
 import com.sun.management.OperatingSystemMXBean;
 import java.lang.management.ManagementFactory;
 import java.util.Calendar;
 import java.util.TimeZone;
 import javafx.application.Platform;
 import l1j.server.server.utils.SystemUtil;

 public class MJFxPerformUpdator implements Runnable {
   private MJFxPerformInfo m_cpu_info;
   private MJFxPerformInfo m_mem_info;

   public static void execute(MJFxPerformInfo cpu_info, MJFxPerformInfo mem_info, MJFxPerformInfo thread_info, MJFxPerformInfoByL1Object object_info) {
     (new Thread(new MJFxPerformUpdator(cpu_info, mem_info, thread_info, object_info))).start();
   }


   private MJFxPerformInfo m_thread_info;
   private MJFxPerformInfoByL1Object m_object_info;

   private MJFxPerformUpdator(MJFxPerformInfo cpu_info, MJFxPerformInfo mem_info, MJFxPerformInfo thread_info, MJFxPerformInfoByL1Object object_info) {
     this.m_cpu_info = cpu_info;
     this.m_mem_info = mem_info;
     this.m_thread_info = thread_info;
     this.m_object_info = object_info;
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

             MJPerformAdapter.CPU_USAGE = (int)(getUseCpu() * 100.0D),
             MJPerformAdapter.MEM_USAGE = (int)SystemUtil.getUsedMemoryMB(),
             MJPerformAdapter.THREAD_USAGE = Thread.activeCount());
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
           this.m_object_info.on_update(time);
         });
   }

   private double getUseCpu() {
     return ((OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad();
   }
 }


