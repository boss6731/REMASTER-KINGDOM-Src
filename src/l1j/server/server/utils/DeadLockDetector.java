 package l1j.server.server.utils;

 import java.lang.management.LockInfo;
 import java.lang.management.ManagementFactory;
 import java.lang.management.MonitorInfo;
 import java.lang.management.ThreadInfo;
 import java.lang.management.ThreadMXBean;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;



















 public class DeadLockDetector
   implements Runnable
 {
   private static String INDENT = "    ";
   private StringBuilder sb = null;
   private L1PcInstance pc = null;

   public DeadLockDetector(L1PcInstance _pc) {
     this.pc = _pc;
   }




   public void run() {
     try {
       ThreadMXBean bean = ManagementFactory.getThreadMXBean();
       long[] threadIds = bean.findDeadlockedThreads();
       if (threadIds != null) {
         if (this.pc != null && this.pc.getNetConnection() != null) {
           this.pc.sendPackets((ServerBasePacket)new S_SystemMessage("偵測到死鎖！ - 參考伺服器命令"));
         } else {
           System.out.println("偵測到死鎖！");
         }  this.sb = new StringBuilder();


         ThreadInfo[] infos = bean.getThreadInfo(threadIds);
         this.sb.append("\n線程鎖定訊息: \n");
         for (ThreadInfo threadInfo : infos) {
           printThreadInfo(threadInfo);
           LockInfo[] lockInfos = threadInfo.getLockedSynchronizers();
           MonitorInfo[] monitorInfos = threadInfo.getLockedMonitors();

           printLockInfo(lockInfos);
           printMonitorInfo(threadInfo, monitorInfos);
         }

         this.sb.append("\n線程轉儲: \n");
         for (ThreadInfo ti : bean.dumpAllThreads(true, true)) {
           printThreadInfo(ti);
         }
         System.out.println(this.sb.toString());
       }
       else if (this.pc != null && this.pc.getNetConnection() != null) {
         this.pc.sendPackets((ServerBasePacket)new S_SystemMessage("沒有死鎖"));
       } else {
         System.out.println("沒有死鎖.");
       }

     } catch (Exception ex) {
       ex.printStackTrace();
     }
   }


   private void printThreadInfo(ThreadInfo threadInfo) {
     printThread(threadInfo);
     this.sb.append(INDENT + threadInfo.toString() + "\n");
     StackTraceElement[] stacktrace = threadInfo.getStackTrace();
     MonitorInfo[] monitors = threadInfo.getLockedMonitors();

     for (int i = 0; i < stacktrace.length; i++) {
       StackTraceElement ste = stacktrace[i];
       this.sb.append(INDENT + "at " + ste.toString() + "\n");
       for (MonitorInfo mi : monitors) {
         if (mi.getLockedStackDepth() == i) {
           this.sb.append(INDENT + "  - locked " + mi + "\n");
         }
       }
     }
   }

   private void printThread(ThreadInfo ti) {
     this.sb.append("\n執行緒輸出\n");
     this.sb.append("\"" + ti.getThreadName() + "\" Id=" + ti.getThreadId() + " in " + ti
         .getThreadState() + "\n");
     if (ti.getLockName() != null) {
       this.sb.append(" on lock=" + ti.getLockName() + "\n");
     }
     if (ti.isSuspended()) {
       this.sb.append(" (暫停)\n");
     }
     if (ti.isInNative()) {
       this.sb.append(" (啟用)\n");
     }
     if (ti.getLockOwnerName() != null) {
       this.sb.append(INDENT + " owned by " + ti.getLockOwnerName() + " Id=" + ti
           .getLockOwnerId() + "\n");
     }
   }


   private void printMonitorInfo(ThreadInfo threadInfo, MonitorInfo[] monitorInfos) {
     this.sb.append(INDENT + "鎖定的顯示器: " + monitorInfos.length + "個\n");
     for (MonitorInfo monitorInfo : monitorInfos) {
       this.sb.append(INDENT + "  - " + monitorInfo + " locked at \n");
       this.sb.append(INDENT + "      " + monitorInfo.getLockedStackDepth() + " " + monitorInfo
           .getLockedStackFrame() + "\n");
     }
   }

   private void printLockInfo(LockInfo[] lockInfos) {
     this.sb.append(INDENT + "堵塞的水槽: " + lockInfos.length + "個\n");
     for (LockInfo lockInfo : lockInfos)
       this.sb.append(INDENT + "  - " + lockInfo + "\n");
   }
 }


