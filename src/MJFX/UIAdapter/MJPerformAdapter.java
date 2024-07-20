package MJFX.UIAdapter;

public class MJPerformAdapter {
	// 靜態變量來存儲 CPU 使用率
	public static int CPU_USAGE = 0;

	// 靜態變量來存儲內存使用率
	public static int MEM_USAGE = 0;

	// 靜態變量來存儲線程使用率
	public static int THREAD_USAGE = 0;
}

/**這個類通過靜態變量提供了一種簡單的方法來存儲和訪問性能數據。
 代碼中沒有顯示明顯的錯誤。若需增加此類的功能，
可以考慮添加方法來更新和獲取這些數據，例如：**/

/*package MJFX.UIAdapter;

public class MJPerformAdapter {
	// 靜態變量來存儲 CPU 使用率
	public static int CPU_USAGE = 0;

	// 靜態變量來存儲內存使用率
	public static int MEM_USAGE = 0;

	// 靜態變量來存儲線程使用率
	public static int THREAD_USAGE = 0;

	// 方法來更新 CPU 使用率
	public static void setCpuUsage(int cpuUsage) {
		CPU_USAGE = cpuUsage;
	}

	// 方法來獲取 CPU 使用率
	public static int getCpuUsage() {
		return CPU_USAGE;
	}

	// 方法來更新內存使用率
	public static void setMemUsage(int memUsage) {
		MEM_USAGE = memUsage;
	}

	// 方法來獲取內存使用率
	public static int getMemUsage() {
		return MEM_USAGE;
	}

	// 方法來更新線程使用率
	public static void setThreadUsage(int threadUsage) {
		THREAD_USAGE = threadUsage;
	}

	// 方法來獲取線程使用率
	public static int getThreadUsage() {
		return THREAD_USAGE;
	}
}/*
