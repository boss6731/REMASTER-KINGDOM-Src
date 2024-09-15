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

public class GeneralThreadPool {
	private static GeneralThreadPool _instance;

	// TODO 伺服器啟動時 SCV 角色 -> 根據電腦配置不同，128 適中
	private static final int SCHEDULED_CORE_POOL_SIZE = Config.Connection.SCHEDULEDCOREPOOLSIZE;
	private ThreadPoolExecutor _executor;
	private ScheduledThreadPoolExecutor _scheduler;
	private ScheduledThreadPoolExecutor _pcScheduler;
	// TODO 最大用戶數
	private final int _pcSchedulerPoolSize = 1 + Config.Login.MaximumOnlineUsers / 10;

	// TODO 不使用。以防萬一修改為 32
//	private final int _pcSchedulerPoolSize = 32;

	public static GeneralThreadPool getInstance() {
		if (_instance == null) {
			_instance = new GeneralThreadPool();
		}
		return _instance;
	}

	private GeneralThreadPool() {
		if (Config.Connection.GeneralThreadPoolType == 1) {
			_executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Config.Connection.GeneralThreadPoolSize);
		} else if (Config.Connection.GeneralThreadPoolType == 2) {
			_executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		} else {
			_executor = null;
		}
		_scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(SCHEDULED_CORE_POOL_SIZE, new PriorityThreadFactory("GerenalSTPool", Thread.NORM_PRIORITY));
		_pcScheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(_pcSchedulerPoolSize, new PriorityThreadFactory("PcMonitorSTPool", Thread.NORM_PRIORITY));
	}

	public ThreadPoolExecutor executor(){
		return _executor;
	}
	
	public ScheduledThreadPoolExecutor scheduler(){
		return _scheduler;
	}
	
	public ScheduledThreadPoolExecutor pcScheduler(){
		return _pcScheduler;
	}
	
	public Future<?> submit(Runnable r) {
		return _executor.submit(r);
	}

	public void execute(Runnable r) {
		if (_executor == null) {
			Thread t = new Thread(r);
			t.start();
		} else {
			_executor.execute(r);
		}
	}

	public void execute(Thread t) {
		t.start();
	}

	public ScheduledFuture<?> schedule(Runnable r, long delay) {
		try {
			if (delay <= 0) {
				_executor.execute(r);
				return null;
			}
			return _scheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null;
		}
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long initialDelay, long period) {
		return _scheduler.scheduleAtFixedRate(r, initialDelay, period, TimeUnit.MILLISECONDS);
	}

	public ScheduledFuture<?> scheduleAtFixedRateLong(Runnable r, long initialDelay, long period) {
		return _scheduler.scheduleAtFixedRate(r, initialDelay, period, TimeUnit.MILLISECONDS);
	}

	public ScheduledFuture<?> pcSchedule(L1PcMonitor r, long delay) {
		try {
			if (delay <= 0) {
				_executor.execute(r);
				return null;
			}
			return _pcScheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null;
		}
	}

	public ScheduledFuture<?> pcScheduleAtFixedRate(L1PcMonitor r, long initialDelay, long period) {
		return _pcScheduler.scheduleAtFixedRate(r, initialDelay, period, TimeUnit.MILLISECONDS);
	}

	public ExecutorService createSinglePool(GameClient gc){
		return Executors.newSingleThreadExecutor(new PriorityThreadFactory(String.format("[%s]CPool", gc.getIp()), Thread.NORM_PRIORITY));
	}

	// TODO 從 ThreadPoolManager 調度
	private class PriorityThreadFactory implements ThreadFactory {
		private final int _prio;
		private final String _name;
		private final AtomicInteger _threadNumber = new AtomicInteger(1);
		private final ThreadGroup _group;

		public PriorityThreadFactory(String name, int prio) {
			_prio = prio;
			_name = name;
			_group = new ThreadGroup(_name);
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(_group, r);
			t.setName(_name + "-" + _threadNumber.getAndIncrement());
			t.setPriority(_prio);
			return t;
		}
	}
}
