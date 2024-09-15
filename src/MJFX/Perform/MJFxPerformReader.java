package MJFX.Perform;

import MJFX.MJFxController;
import com.sun.management.OperatingSystemMXBean;
import javafx.application.Platform;
import l1j.server.server.utils.SystemUtil;

import java.lang.management.ManagementFactory;
import java.util.Calendar;
import java.util.TimeZone;

public class MJFxPerformReader implements Runnable{
	public static void execute(MJFxPerformInfo cpu_info, MJFxPerformInfo mem_info, MJFxPerformInfo thread_info){
		new Thread(new MJFxPerformReader(cpu_info, mem_info, thread_info)).start();
	}
	
	private MJFxPerformInfo m_cpu_info;
	private MJFxPerformInfo m_mem_info;
	private MJFxPerformInfo m_thread_info;
	
	private MJFxPerformReader(MJFxPerformInfo cpu_info, MJFxPerformInfo mem_info, MJFxPerformInfo thread_info){
		m_cpu_info = cpu_info;
		m_mem_info = mem_info;
		m_thread_info = thread_info;
	}
	
	@Override
	public void run() {
		try{
			while(true){
				Thread.sleep(1000L);
				if(MJFxController.getInstance() == null)
					continue;
				
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
				String time = String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), (cal.get(Calendar.SECOND)));
				do_update(
						time, 
						(int) (getUseCpu() * 100D), 
						(int) SystemUtil.getUsedMemoryMB(),
						Thread.activeCount()
						);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
        return new l1j.server.server.model.Instance.L1PcInstance[0];
    }
	
	private void do_update(final String time, final int cpu_usage, final int mem_usage, final int thread_count){
		Platform.runLater(()->{
			m_cpu_info.on_update(time, cpu_usage);
			m_mem_info.on_update(time, mem_usage);
			m_thread_info.on_update(time, thread_count);
		});
	}
	
	private double getUseCpu() {
		return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad();
	}
}
