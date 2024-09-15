/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server;

import java.util.logging.Logger;

import MJFX.UIAdapter.MJUIAdapter;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 
 * This class provides the functions for shutting down and restarting the server
 * It closes all open clientconnections and saves all data.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/11/18 15:43:30 $
 */
public class Shutdown extends Thread {
	private static Logger _log = Logger.getLogger(Shutdown.class.getName());
	private static Shutdown _instance;
	private static Shutdown _counterInstance = null;

	private int secondsShut;

	private int shutdownMode;
	public static final int SIGTERM = 0;
	public static final int GM_SHUTDOWN = 1;
	public static final int GM_RESTART = 2;
	public static final int ABORT = 3;
	private static String[] _modeText = { "SIGTERM", "shuting down", "restarting", "aborting" };

	/**
	 * Default constucter is only used internal to create the shutdown-hook
	 * instance
	 * 
	 */
	public Shutdown() {
		secondsShut = -1;
		shutdownMode = SIGTERM;
	}

	/**
	 * This creates a countdown instance of Shutdown.
	 * 
	 * @param seconds
	 *            how many seconds until shutdown
	 * @param restart
	 *            true is the server shall restart after shutdown
	 * 
	 */
	public Shutdown(int seconds, boolean restart) {
		if (seconds < 0) {
			seconds = 0;
		}
		secondsShut = seconds;
		if (restart) {
			shutdownMode = GM_RESTART;
		} else {
			shutdownMode = GM_SHUTDOWN;
		}
	}

	/**
	 * get the shutdown-hook instance the shutdown-hook instance is created by
	 * the first call of this function, but it has to be registrered externaly.
	 * 
	 * @return instance of Shutdown, to be used as shutdown hook
	 */
	public static Shutdown getInstance() {
		if (_instance == null) {
			_instance = new Shutdown();
		}
		return _instance;
	}

	/**
	 * this function is called, when a new thread starts
	 * 
	 * if this thread is the thread of getInstance, then this is the shutdown
	 * hook and we save all data and disconnect all clients.
	 * 
	 * after this thread ends, the server will completely exit
	 * 
	 * if this is not the thread of getInstance, then this is a countdown
	 * thread. we start the countdown, and when we finished it, and it was not
	 * aborted, we tell the shutdown-hook why we call exit, and then call exit
	 * 
	 * when the exit status of the server is 1, startServer.sh / startServer.bat
	 * will restart the server.
	 * 
	 */
	@Override
	public void run() {
		if (this == _instance) {
			// last byebye, save all data and quit this server
			// logging doesnt work here :(
			saveData();
			// server will quit, when this function ends.
		} else {
			// gm shutdown: send warnings and then call exit to start shutdown
			// sequence
			countdown();
			// last point where logging is operational :(
			_log.warning("GM關機倒數計時已結束。 " + _modeText[shutdownMode] + " 現在！");
			System.out.println("GM關機倒數計時已結束。 " + _modeText[shutdownMode] + " 現在！");
			switch (shutdownMode) {
			case GM_SHUTDOWN:
				_instance.setMode(GM_SHUTDOWN);
				MJUIAdapter.on_exit();
				break;
			case GM_RESTART:
				_instance.setMode(GM_RESTART);
				System.exit(1);
				break;
			}
		}
        return new L1PcInstance[0];
    }

	/**
	 * This functions starts a shutdown countdown
	 * 
	 * @param activeChar
	 *            GM who issued the shutdown command
	 * @param seconds
	 *            seconds until shutdown
	 * @param restart
	 *            true if the server will restart after shutdown
	 */
	public void startShutdown(L1PcInstance activeChar, int seconds, boolean restart) {
		Announcements _an = Announcements.getInstance();
		_log.warning("GM: " + activeChar.getId() + " 發出了關機命令。 " + _modeText[shutdownMode] + " 在 "
				+ seconds + " 秒內！");
		_an.announceToAll("伺服器將在 " + seconds + " 秒內 " + _modeText[shutdownMode] + "！");

		System.out.println("GM: " + activeChar.getId() + " 發出了關機命令。 " + _modeText[shutdownMode] + " 在 "
				+ seconds + " 秒內！");
		_an.announceToAll("伺服器將在 " + seconds + " 秒內 " + _modeText[shutdownMode] + "！");
		if (_counterInstance != null) {
			_counterInstance._abort();
		}

		// the main instance should only run for shutdown hook, so we start a
		// new instance
		_counterInstance = new Shutdown(seconds, restart);
		GeneralThreadPool.getInstance().execute(_counterInstance);
	}

	/**
	 * This function aborts a running countdown
	 * 
	 * @param activeChar
	 *            GM who issued the abort command
	 */
	public void abort(L1PcInstance activeChar) {
		Announcements _an = Announcements.getInstance();
		_log.warning("GM: " + activeChar.getName() + " 發出了中止關機命令。");
		_an.announceToAll("伺服器中止關機並繼續正常運行！");

		System.out.println("GM: " + activeChar.getName() + " 發出了中止關機命令。");
		_an.announceToAll("伺服器中止關機並繼續正常運行！");
		if (_counterInstance != null) {
			_counterInstance._abort();
		}
	}

	/**
	 * set the shutdown mode
	 * 
	 * @param mode
	 *            what mode shall be set
	 */
	private void setMode(int mode) {
		shutdownMode = mode;
	}

	/**
	 * set the shutdown mode
	 * 
	 * @param mode
	 *            what mode shall be set
	 */
	int getMode() {
		return shutdownMode;
	}

	/**
	 * set shutdown mode to ABORT
	 * 
	 */
	private void _abort() {
		shutdownMode = ABORT;
	}

	/**
	 * this counts the countdown and reports it to all players countdown is
	 * aborted if mode changes to ABORT
	 */
	private void countdown() {
		Announcements _an = Announcements.getInstance();

		try {
			while (secondsShut > 0) {

				switch (secondsShut) {
					case 240:
						_an.announceToAll("伺服器將在4分鐘內關閉。");
						break;
					case 180:
						_an.announceToAll("伺服器將在3分鐘內關閉。");
						break;
					case 120:
						_an.announceToAll("伺服器將在2分鐘內關閉。");
						break;
					case 60:
						_an.announceToAll("伺服器將在1分鐘內關閉。");
						break;
					case 30:
						_an.announceToAll("伺服器將在30秒內關閉。");
						break;
					case 10:
						_an.announceToAll("伺服器將在10秒內關閉。");
						break;
					case 5:
						_an.announceToAll("伺服器將在5秒內關閉。");
						break;
					case 4:
						_an.announceToAll("伺服器將在4秒內關閉。");
						break;
					case 3:
						_an.announceToAll("伺服器將在3秒內關閉。");
						break;
					case 2:
						_an.announceToAll("伺服器將在2秒內關閉。");
						break;
					case 1:
						_an.announceToAll("伺服器將在1秒內關閉。");
						break;
				}

				secondsShut--;

				int delay = 1000; // milliseconds
				Thread.sleep(delay);

				if (shutdownMode == ABORT) {
					break;
				}
			}
		} catch (InterruptedException e) {
			// this will never happen
		}
	}

	/**
	 * this sends a last byebye, disconnects all players and saves data
	 * 
	 */
	private void saveData() {
		Announcements _an = Announcements.getInstance();
		switch (shutdownMode) {
			case SIGTERM:
				System.err.println("收到SIGTERM信號。現在關機！");
				break;
			case GM_SHUTDOWN:
				System.err.println("收到GM關機命令。現在關機！");
				break;
			case GM_RESTART:
				System.err.println("收到GM重啟命令。現在重啟！");
				break;
		}
		_an.announceToAll("伺服器現在 " + _modeText[shutdownMode] + "！再見");

		// 我們無法再中止關機，所以我移除了“if”
		GameServer.getInstance().disconnectAllCharacters();

		System.err.println("數據已保存。所有玩家已斷線，正在關機。");
		try {
			int delay = 500;
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// never happens :p
		}
	}

	public void startTelnetShutdown(String IP, int seconds, boolean restart) {
		Announcements _an = Announcements.getInstance();
		_log.warning(
				"IP: " + IP + " 發出了關機命令。 " + _modeText[shutdownMode] + " 在 " + seconds + " 秒內！");
		_an.announceToAll("伺服器將在 " + seconds + " 秒內 " + _modeText[shutdownMode] +  "！");

		System.out.println("IP: " + IP + " 發出了關機命令。 " + _modeText[shutdownMode] + " 在 " + seconds + " 秒內！");
		_an.announceToAll("伺服器將在 " + seconds + " 秒內 " + _modeText[shutdownMode] + "！");
		if (_counterInstance != null) {
			_counterInstance._abort();
		}
		_counterInstance = new Shutdown(seconds, restart);
		GeneralThreadPool.getInstance().execute(_counterInstance);
	}

	/**
	 * This function aborts a running countdown
	 * 
	 * @param IP
	 *            IP Which Issued shutdown command
	 */
	public void Telnetabort(String IP) {
		Announcements _an = Announcements.getInstance();
		_log.warning("IP: " + IP + " 發出了中止關機命令。 " + _modeText[shutdownMode] + " 已被停止！");
		_an.announceToAll("伺服器中止 " + _modeText[shutdownMode] + " 並繼續正常運行！");

		System.out.println("IP: " + IP + " 發出了中止關機命令。 " + _modeText[shutdownMode] + " 已被停止！");
		_an.announceToAll("伺服器中止 " + _modeText[shutdownMode] + " 並繼續正常運行！");
		if (_counterInstance != null) {
			_counterInstance._abort();
		}
	}
}