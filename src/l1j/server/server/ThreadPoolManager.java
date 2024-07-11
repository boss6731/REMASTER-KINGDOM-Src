     package l1j.server.server;

     import java.util.concurrent.LinkedBlockingQueue;
     import java.util.concurrent.RejectedExecutionException;
     import java.util.concurrent.ScheduledFuture;
     import java.util.concurrent.ScheduledThreadPoolExecutor;
     import java.util.concurrent.ThreadFactory;
     import java.util.concurrent.ThreadPoolExecutor;
     import java.util.concurrent.TimeUnit;
     import java.util.concurrent.atomic.AtomicInteger;
     import javolution.text.TextBuilder;
     import l1j.server.Config;




     public class ThreadPoolManager
     {
       private static ThreadPoolManager _instance;
       private final ScheduledThreadPoolExecutor _effectsScheduledThreadPool;
       private final ScheduledThreadPoolExecutor _generalScheduledThreadPool;
       private final ThreadPoolExecutor _generalPacketsThreadPool;
       private final ThreadPoolExecutor _ioPacketsThreadPool;
       private final ThreadPoolExecutor _aiThreadPool;
       private final ThreadPoolExecutor _generalThreadPool;
       private final ScheduledThreadPoolExecutor _aiScheduledThreadPool;
       private boolean _shutdown;

       public static ThreadPoolManager getInstance() {
         if (_instance == null) {
           _instance = new ThreadPoolManager();
         }
         return _instance;
       }

       private ThreadPoolManager() {
         this._effectsScheduledThreadPool = new ScheduledThreadPoolExecutor(Config.THREAD_P_EFFECTS, new PriorityThreadFactory("EffectsSTPool", 1));
         this._generalScheduledThreadPool = new ScheduledThreadPoolExecutor(Config.THREAD_P_GENERAL, new PriorityThreadFactory("GerenalSTPool", 5));


         this._ioPacketsThreadPool = new ThreadPoolExecutor(8, 2147483647, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new PriorityThreadFactory("I/O Packet Pool", 6));


         this._generalPacketsThreadPool = new ThreadPoolExecutor(16, 24, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new PriorityThreadFactory("Normal Packet Pool", 6));


         this._generalThreadPool = new ThreadPoolExecutor(8, 16, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new PriorityThreadFactory("General Pool", 5));



         this._aiThreadPool = new ThreadPoolExecutor(2, Config.AI_MAX_THREAD, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

         this._aiScheduledThreadPool = new ScheduledThreadPoolExecutor(Config.AI_MAX_THREAD, new PriorityThreadFactory("AISTPool", 5));
       }

       public ScheduledFuture<?> scheduleEffect(Runnable r, long delay) {
         try {
           if (delay < 0L) {
             delay = 0L;
           }
           return this._effectsScheduledThreadPool.schedule(r, delay, TimeUnit.MILLISECONDS);
         }
         catch (RejectedExecutionException e) {
           return null;
         }
       }


       public ScheduledFuture<?> scheduleEffectAtFixedRate(Runnable r, long initial, long delay) {
         try {
           if (delay < 0L) {
             delay = 0L;
           }
           if (initial < 0L) {
             initial = 0L;
           }
           return this._effectsScheduledThreadPool.scheduleAtFixedRate(r, initial, delay, TimeUnit.MILLISECONDS);
         }
         catch (RejectedExecutionException e) {
           return null;
         }
       }

       public ScheduledFuture<?> scheduleGeneral(Runnable r, long delay) {
         try {
           if (delay < 0L) {
             delay = 0L;
           }
           return this._generalScheduledThreadPool.schedule(r, delay, TimeUnit.MILLISECONDS);
         }
         catch (RejectedExecutionException e) {
           return null;
         }
       }


       public ScheduledFuture<?> scheduleGeneralAtFixedRate(Runnable r, long initial, long delay) {
         try {
           if (delay < 0L) {
             delay = 0L;
           }
           if (initial < 0L) {
             initial = 0L;
           }
           return this._generalScheduledThreadPool.scheduleAtFixedRate(r, initial, delay, TimeUnit.MILLISECONDS);
         }
         catch (RejectedExecutionException e) {
           return null;
         }
       }

       public ScheduledFuture<?> scheduleAi(Runnable r, long delay) {
         try {
           if (delay < 0L) {
             delay = 0L;
           }
           return this._aiScheduledThreadPool.schedule(r, delay, TimeUnit.MILLISECONDS);
         }
         catch (RejectedExecutionException e) {
           return null;
         }
       }


       public ScheduledFuture<?> scheduleAiAtFixedRate(Runnable r, long initial, long delay) {
         try {
           if (delay < 0L) {
             delay = 0L;
           }
           if (initial < 0L) {
             initial = 0L;
           }
           return this._aiScheduledThreadPool.scheduleAtFixedRate(r, initial, delay, TimeUnit.MILLISECONDS);
         }
         catch (RejectedExecutionException e) {
           return null;
         }
       }








       public void executeTask(Runnable r) {
         this._generalThreadPool.execute(r);
       }

       public void executeAi(Runnable r) {
         this._aiThreadPool.execute(r);
       }

       public String[] getStats() {
         return new String[] { "STP:", " + Effects:", " |- ActiveThreads:   " + this._effectsScheduledThreadPool



             .getActiveCount(), " |- getCorePoolSize: " + this._effectsScheduledThreadPool

             .getCorePoolSize(), " |- PoolSize:        " + this._effectsScheduledThreadPool

             .getPoolSize(), " |- MaximumPoolSize: " + this._effectsScheduledThreadPool

             .getMaximumPoolSize(), " |- CompletedTasks:  " + this._effectsScheduledThreadPool

             .getCompletedTaskCount(), " |- ScheduledTasks:  " + (this._effectsScheduledThreadPool

             .getTaskCount() - this._effectsScheduledThreadPool
             .getCompletedTaskCount()), " | -------", " + General:", " |- ActiveThreads:   " + this._generalScheduledThreadPool



             .getActiveCount(), " |- getCorePoolSize: " + this._generalScheduledThreadPool

             .getCorePoolSize(), " |- PoolSize:        " + this._generalScheduledThreadPool

             .getPoolSize(), " |- MaximumPoolSize: " + this._generalScheduledThreadPool

             .getMaximumPoolSize(), " |- CompletedTasks:  " + this._generalScheduledThreadPool

             .getCompletedTaskCount(), " |- ScheduledTasks:  " + (this._generalScheduledThreadPool

             .getTaskCount() - this._generalScheduledThreadPool
             .getCompletedTaskCount()), " | -------", " + AI:", " |- ActiveThreads:   " + this._aiScheduledThreadPool



             .getActiveCount(), " |- getCorePoolSize: " + this._aiScheduledThreadPool

             .getCorePoolSize(), " |- PoolSize:        " + this._aiScheduledThreadPool
             .getPoolSize(), " |- MaximumPoolSize: " + this._aiScheduledThreadPool

             .getMaximumPoolSize(), " |- CompletedTasks:  " + this._aiScheduledThreadPool

             .getCompletedTaskCount(), " |- ScheduledTasks:  " + (this._aiScheduledThreadPool

             .getTaskCount() - this._aiScheduledThreadPool
             .getCompletedTaskCount()), "TP:", " + Packets:", " |- ActiveThreads:   " + this._generalPacketsThreadPool



             .getActiveCount(), " |- getCorePoolSize: " + this._generalPacketsThreadPool

             .getCorePoolSize(), " |- MaximumPoolSize: " + this._generalPacketsThreadPool

             .getMaximumPoolSize(), " |- LargestPoolSize: " + this._generalPacketsThreadPool

             .getLargestPoolSize(), " |- PoolSize:        " + this._generalPacketsThreadPool

             .getPoolSize(), " |- CompletedTasks:  " + this._generalPacketsThreadPool

             .getCompletedTaskCount(), " |- QueuedTasks:     " + this._generalPacketsThreadPool

             .getQueue().size(), " | -------", " + I/O Packets:", " |- ActiveThreads:   " + this._ioPacketsThreadPool


             .getActiveCount(), " |- getCorePoolSize: " + this._ioPacketsThreadPool

             .getCorePoolSize(), " |- MaximumPoolSize: " + this._ioPacketsThreadPool

             .getMaximumPoolSize(), " |- LargestPoolSize: " + this._ioPacketsThreadPool

             .getLargestPoolSize(), " |- PoolSize:        " + this._ioPacketsThreadPool
             .getPoolSize(), " |- CompletedTasks:  " + this._ioPacketsThreadPool

             .getCompletedTaskCount(), " |- QueuedTasks:     " + this._ioPacketsThreadPool

             .getQueue().size(), " | -------", " + General Tasks:", " |- ActiveThreads:   " + this._generalThreadPool


             .getActiveCount(), " |- getCorePoolSize: " + this._generalThreadPool
             .getCorePoolSize(), " |- MaximumPoolSize: " + this._generalThreadPool

             .getMaximumPoolSize(), " |- LargestPoolSize: " + this._generalThreadPool

             .getLargestPoolSize(), " |- PoolSize:        " + this._generalThreadPool
             .getPoolSize(), " |- CompletedTasks:  " + this._generalThreadPool

             .getCompletedTaskCount(), " |- QueuedTasks:     " + this._generalThreadPool
             .getQueue().size(), " | -------", " + AI:", " |- Not Done" };
       }


       private class PriorityThreadFactory
         implements ThreadFactory
       {
         private final int _prio;
         private final String _name;
         private final AtomicInteger _threadNumber = new AtomicInteger(1);

         private final ThreadGroup _group;

         public PriorityThreadFactory(String name, int prio) {
           this._prio = prio;
           this._name = name;
           this._group = new ThreadGroup(this._name);
         }






         public Thread newThread(Runnable r) {
           Thread t = new Thread(this._group, r);
           t.setName(this._name + "-" + this._threadNumber.getAndIncrement());
           t.setPriority(this._prio);
           return t;
         }

         public ThreadGroup getGroup() {
           return this._group;
         }
       }




       public void shutdown() {
         this._shutdown = true;
         try {
           this._effectsScheduledThreadPool.awaitTermination(1L, TimeUnit.SECONDS);
           this._generalScheduledThreadPool.awaitTermination(1L, TimeUnit.SECONDS);
           this._generalPacketsThreadPool.awaitTermination(1L, TimeUnit.SECONDS);
           this._ioPacketsThreadPool.awaitTermination(1L, TimeUnit.SECONDS);
           this._generalThreadPool.awaitTermination(1L, TimeUnit.SECONDS);
           this._aiThreadPool.awaitTermination(1L, TimeUnit.SECONDS);
           this._effectsScheduledThreadPool.shutdown();
           this._generalScheduledThreadPool.shutdown();
           this._generalPacketsThreadPool.shutdown();
           this._ioPacketsThreadPool.shutdown();
           this._generalThreadPool.shutdown();
           this._aiThreadPool.shutdown();
           System.out.println("All ThreadPools are now stoped");
         }
         catch (InterruptedException e) {
           e.printStackTrace();
         }
       }


       public boolean isShutdown() {
         return this._shutdown;
       }




       public void purge() {
         this._effectsScheduledThreadPool.purge();
         this._generalScheduledThreadPool.purge();
         this._aiScheduledThreadPool.purge();
         this._ioPacketsThreadPool.purge();
         this._generalPacketsThreadPool.purge();
         this._generalThreadPool.purge();
         this._aiThreadPool.purge();
       }




       public String getPacketStats() {
         TextBuilder tb = new TextBuilder();
         ThreadFactory tf = this._generalPacketsThreadPool.getThreadFactory();
         if (tf instanceof PriorityThreadFactory) {
           tb.append("General Packet Thread Pool:\r\n");
           tb.append("Tasks in the queue: " + this._generalPacketsThreadPool
               .getQueue().size() + "\r\n");
           tb.append("Showing threads stack trace:\r\n");
           PriorityThreadFactory ptf = (PriorityThreadFactory)tf;
           int count = ptf.getGroup().activeCount();
           Thread[] threads = new Thread[count + 2];
           ptf.getGroup().enumerate(threads);
           tb.append("There should be " + count + " Threads\r\n");
           for (Thread t : threads) {
             if (t != null) {


               tb.append(t.getName() + "\r\n");
               for (StackTraceElement ste : t.getStackTrace()) {
                 tb.append(ste.toString());
                 tb.append("\r\n");
               }
             }
           }
         }  tb.append("Packet Tp stack traces printed.\r\n");
         return tb.toString();
       }

       public String getIOPacketStats() {
         TextBuilder tb = new TextBuilder();
         ThreadFactory tf = this._ioPacketsThreadPool.getThreadFactory();
         if (tf instanceof PriorityThreadFactory) {
           tb.append("I/O Packet Thread Pool:\r\n");
           tb.append("Tasks in the queue: " + this._ioPacketsThreadPool
               .getQueue().size() + "\r\n");
           tb.append("Showing threads stack trace:\r\n");
           PriorityThreadFactory ptf = (PriorityThreadFactory)tf;
           int count = ptf.getGroup().activeCount();
           Thread[] threads = new Thread[count + 2];
           ptf.getGroup().enumerate(threads);
           tb.append("There should be " + count + " Threads\r\n");
           for (Thread t : threads) {
             if (t != null) {


               tb.append(t.getName() + "\r\n");
               for (StackTraceElement ste : t.getStackTrace()) {
                 tb.append(ste.toString());
                 tb.append("\r\n");
               }
             }
           }
         }  tb.append("Packet Tp stack traces printed.\r\n");
         return tb.toString();
       }

       public String getGeneralStats() {
         TextBuilder tb = new TextBuilder();
         ThreadFactory tf = this._generalThreadPool.getThreadFactory();
         if (tf instanceof PriorityThreadFactory) {
           tb.append("General Thread Pool:\r\n");
           tb.append("Tasks in the queue: " + this._generalThreadPool
               .getQueue().size() + "\r\n");
           tb.append("Showing threads stack trace:\r\n");
           PriorityThreadFactory ptf = (PriorityThreadFactory)tf;
           int count = ptf.getGroup().activeCount();
           Thread[] threads = new Thread[count + 2];
           ptf.getGroup().enumerate(threads);
           tb.append("There should be " + count + " Threads\r\n");
           for (Thread t : threads) {
             if (t != null) {


               tb.append(t.getName() + "\r\n");
               for (StackTraceElement ste : t.getStackTrace()) {
                 tb.append(ste.toString());
                 tb.append("\r\n");
               }
             }
           }
         }  tb.append("Packet Tp stack traces printed.\r\n");
         return tb.toString();
       }
     }


