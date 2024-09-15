package l1j.server.server.server.monitor;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.server.monitor.FileLogger;

public class LoggerInstance extends FileLogger implements Runnable {
	private static LoggerInstance _instance = null;

	private LoggerInstance() {
	};

	public static LoggerInstance getInstance() {
		if (_instance == null) {
			_instance = new LoggerInstance();
			GeneralThreadPool.getInstance().execute(_instance);
			//new Thread(_instance).start();
		}
		return _instance;
	}

	public void run() {
		try{
			flush();
		}catch(Exception e){
			
		}finally{
			GeneralThreadPool.getInstance().schedule(this, 60000);
		}
		/*while (true) {
			try {
				flush();
				Thread.sleep(1000 * 60);
			} catch (Exception e) {
			}
		}*/
        return new l1j.server.server.model.Instance.L1PcInstance[0];
    }

	public void flush() {

	}

	public void addCommand(String s) {

	}
}
