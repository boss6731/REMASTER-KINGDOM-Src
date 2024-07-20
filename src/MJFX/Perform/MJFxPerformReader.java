package MJFX.Perform;

import MJFX.MJFxController;
import com.sun.management.OperatingSystemMXBean;
import javafx.application.Platform;
import l1j.server.server.utils.SystemUtil;

import java.lang.management.ManagementFactory;
import java.util.Calendar;
import java.util.TimeZone;

public class MJFxPerformReader implements Runnable {
	// 成員變量，用於存儲性能信息
	private MJFxPerformInfo m_cpu_info;
	private MJFxPerformInfo m_mem_info;
	private MJFxPerformInfo m_thread_info;

	// 靜態方法來啟動性能讀取器
	public static void execute(MJFxPerformInfo cpu_info, MJFxPerformInfo mem_info, MJFxPerformInfo thread_info) {
		(new Thread(new MJFxPerformReader(cpu_info, mem_info, thread_info))).start();
	}

	// 私有構造函數
	private MJFxPerformReader(MJFxPerformInfo cpu_info, MJFxPerformInfo mem_info, MJFxPerformInfo thread_info) {
		this.m_cpu_info = cpu_info;
		this.m_mem_info = mem_info;
		this.m_thread_info = thread_info;
	}

	// run 方法，執行性能讀取	@override
	
	public void run() {
		try {
			while (true) {
				Thread.sleep(1000L);

				if (MJFxController.getInstance() == null) {
					continue;
				}

				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
				String time = String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));

				do_update(time,
						(int) (getUseCpu() * 100.0D),
						(int) SystemUtil.getUsedMemoryMB(),
						Thread.activeCount());
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	// 更新性能信息的方法
	private void do_update(String time, int cpu_usage, int mem_usage, int thread_count) {
		Platform.runLater(() -> {
			this.m_cpu_info.on_update(time, cpu_usage);
			this.m_mem_info.on_update(time, mem_usage);
			this.m_thread_info.on_update(time, thread_count);
		});
	}

	// 獲取 CPU 使用率的方法
	private double getUseCpu() {
		return ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad();
	}
}


