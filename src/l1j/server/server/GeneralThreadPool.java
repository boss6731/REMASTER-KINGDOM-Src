 package l1j.server.server;

 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.Future;
 import java.util.concurrent.RejectedExecutionException;
 import java.util.concurrent.ScheduledFuture;
 import java.util.concurrent.ScheduledThreadPoolExecutor;
 import java.util.concurrent.ThreadFactory;
 import java.util.concurrent.ThreadPoolExecutor;
 import java.util.concurrent.TimeUnit;
 import java.util.concurrent.atomic.AtomicInteger;
 import l1j.server.Config;
 import l1j.server.server.model.monitor.L1PcMonitor;



 public class GeneralThreadPool
 {
   private static GeneralThreadPool _instance;
   private static final int SCHEDULED_CORE_POOL_SIZE = Config.Connection.SCHEDULEDCOREPOOLSIZE;

   private ThreadPoolExecutor _executor;
   private ScheduledThreadPoolExecutor _scheduler;
   private ScheduledThreadPoolExecutor _pcScheduler;
   private final int _pcSchedulerPoolSize = 1 + Config.Login.MaximumOnlineUsers / 10;




   public static GeneralThreadPool getInstance() {
     if (_instance == null) {
       _instance = new GeneralThreadPool();
     }
     return _instance;
   }

   private GeneralThreadPool() {
     if (Config.Connection.GeneralThreadPoolType == 1) {
       this._executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(Config.Connection.GeneralThreadPoolSize);
     } else if (Config.Connection.GeneralThreadPoolType == 2) {
       this._executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
     } else {
       this._executor = null;
     }
     this._scheduler = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(SCHEDULED_CORE_POOL_SIZE, new PriorityThreadFactory("GerenalSTPool", 5));
     this._pcScheduler = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(this._pcSchedulerPoolSize, new PriorityThreadFactory("PcMonitorSTPool", 5));
   }

   public ThreadPoolExecutor executor() {
     return this._executor;
   }

   public ScheduledThreadPoolExecutor scheduler() {
     return this._scheduler;
   }

   public ScheduledThreadPoolExecutor pcScheduler() {
     return this._pcScheduler;
   }

   public Future<?> submit(Runnable r) {
     return this._executor.submit(r);
   }

   public void execute(Runnable r) {
     if (this._executor == null) {
       Thread t = new Thread(r);
       t.start();
     } else {
       this._executor.execute(r);
     }
   }

   public void execute(Thread t) {
     t.start();
   }

   public ScheduledFuture<?> schedule(Runnable r, long delay) {
     try {
       if (delay <= 0L) {
         this._executor.execute(r);
         return null;
       }
       return this._scheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
     } catch (RejectedExecutionException e) {
       return null;
     }
   }

   public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long initialDelay, long period) {
     return this._scheduler.scheduleAtFixedRate(r, initialDelay, period, TimeUnit.MILLISECONDS);
   }

   public ScheduledFuture<?> scheduleAtFixedRateLong(Runnable r, long initialDelay, long period) {
     return this._scheduler.scheduleAtFixedRate(r, initialDelay, period, TimeUnit.MILLISECONDS);
   }

   public ScheduledFuture<?> pcSchedule(L1PcMonitor r, long delay) {
     try {
       if (delay <= 0L) {
         this._executor.execute((Runnable)r);
         return null;
       }
       return this._pcScheduler.schedule((Runnable)r, delay, TimeUnit.MILLISECONDS);
     } catch (RejectedExecutionException e) {
       return null;
     }
   }

   public ScheduledFuture<?> pcScheduleAtFixedRate(L1PcMonitor r, long initialDelay, long period) {
     return this._pcScheduler.scheduleAtFixedRate((Runnable)r, initialDelay, period, TimeUnit.MILLISECONDS);
   }

   public ExecutorService createSinglePool(GameClient gc) {
     return Executors.newSingleThreadExecutor(new PriorityThreadFactory(String.format("[%s]CPool", new Object[] { gc.getIp() }), 5));
   }

   private class PriorityThreadFactory
     implements ThreadFactory {
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
   }
 }


