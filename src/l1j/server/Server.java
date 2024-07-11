package l1j.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.logging.LogManager;

import MJFX.UIAdapter.MJUIAdapter;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.server.GameServer;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.utils.PerformanceTimer;

/**
 * 啟動l1j-jp的伺服器。
 */
public class Server {
	/** 日誌設置文件的文件夾。 */
	private static final String LOG_PROP = "./config/log.properties";

	private static volatile Server uniqueInstance;

	public static Server createServer() {
		if (uniqueInstance == null) {
			synchronized (Server.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new Server();
				}
			}
		}

		return uniqueInstance;
	}

	public static void startLoginServer() {
		LoginController.getInstance().setMaxAllowedOnlinePlayers(Config.Login.MaximumOnlineUsers);
		MJNetServerLoadManager.getInstance().run();
	}

	public void changePort(int port) {
		MJNetServerLoadManager.reload();
	}

	public void shutdown() {
		GameServer.getInstance().shutdown();
	}

	public static PrintStream _dout;

	public class ConsoleScanner implements Runnable {
		@Override
		public void run() {
			try {
				// test
				ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
				_dout = System.out;
				PrintStream ps = new PrintStream(baos);
				System.setOut(ps);
				System.setErr(ps);
				while (true) {
					Thread.sleep(1000L);
					String s = null;
					synchronized (baos) {
						s = baos.toString();
						baos.reset();
					}

					if (s == null)
						continue;
					s = s.trim();
					if (s.length() > 0) {
						String msg = String.format("%s\r\n", s);
						LoggerInstance.getInstance().addCmd(msg);
						_dout.print(msg);
					}
				}
			} catch (InterruptedException e) {
			} finally {
			}
		}
	}

	/**
	 * 伺服器主要。
	 *
	 * @param args
	 *             指令行參數
	 * @throws SQLException
	 * @throws Exception
	 */
	public Server() {
		new Thread(new ConsoleScanner()).start();
		initLogManager();
		initDBFactory();
		try {
			PerformanceTimer timer = new PerformanceTimer();
			timer.reset();
			timer = null;
			/*
			 * startGameServer();
			 * startLoginServer();
			 */
		} catch (Exception e) {
		}
	}

	public static void startGameServer() {
		OpeningMent();
		try {
			GameServer.getInstance().initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		ClosingMent();
	}

	private void initLogManager() {
		File logFolder = new File("log");
		logFolder.mkdir();

		try {
			InputStream is = new BufferedInputStream(new FileInputStream(LOG_PROP));
			LogManager.getLogManager().readConfiguration(is);
			is.close();
		} catch (IOException e) {
			MJUIAdapter.on_exit();
		}
		try {
			// Config.load();
		} catch (Exception e) {
			MJUIAdapter.on_exit();
		}
	}

	private static void OpeningMent() {
		System.out.println("-------------------------------------------------------------------------------");
	}

	private static void ClosingMent() {
		try {
			System.out.println("[執行伺服器連接埠] : (" + Config.Login.GameserverPort + ") | [執行Web連接埠] : ("
					+ Config.Web.webServerPort + ")");
			// System.out.println("-------------------------------------------------------------------------------");
			// System.out.println("[管理員視窗:" + Config.Synchronization.Operation_Manager + " /
			// 在Web GM工具中查看伺服器狀態]");
			// System.out.println("[Web page:" + Config.Login.ExternalAddress + ":" +
			// Config.Web.webServerPort + "/my/index]");
			// System.out.println("[Web page(Secret):" + Config.Login.ExternalAddress + ":"
			// + Config.Web.webServerPort + "/my/secret-login_gm]");

			// System.out.println(" / [CHATTING PORT] : (" +
			// MJMyResource.construct().webSocket().port() + ")");
			System.out.println("-------------------------------------------------------------------------------");
		} catch (Exception localException) {
		}
	}

	private void initDBFactory() {
		/*
		 * L1DatabaseFactory.setDatabaseSettings(Config.DB_DRIVER, Config.DB_URL,
		 * Config.DB_LOGIN, Config.DB_PASSWORD);
		 */
		try {
			L1DatabaseFactory.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

}
