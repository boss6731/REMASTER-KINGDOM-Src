package l1j.server.server.server.command.executor;

import java.util.Random;
import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.RepeatTask;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Userinc implements L1CommandExecutor {
	private L1Userinc() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Userinc();
	}

	private static int _buffMaxCount = 0;
	private static int _remainBuffTime = 0;
	private static int _totalBuffTime = 0;

	private static Random _random = new Random(System.nanoTime());

	class UserCountBuffTimer extends RepeatTask {
		public UserCountBuffTimer() {
			super(2000);
		}

		@Override
		public void execute() {

			_remainBuffTime = _remainBuffTime - 2;

			if (_remainBuffTime < 1) {
				_remainBuffTime = 0;

				Config.ServerAdSetting.WHOISCONTER = _buffMaxCount;
				cancel();
				_UserCountBuffTimer = null;
			} else {
				int incCount = (_buffMaxCount * 2 / _totalBuffTime);

				int additionalBuffRatio = ((_buffMaxCount * 1000) / _totalBuffTime) % 1000;

				if (_random.nextInt(1000) < additionalBuffRatio) {
					incCount += 2;
				}

				Config.ServerAdSetting.WHOISCONTER += incCount;
			}
		}
	}

	private static UserCountBuffTimer _UserCountBuffTimer = null;

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String inc = st.nextToken();

			if (inc.equalsIgnoreCase("初始化")) {
				if (_UserCountBuffTimer != null) {
					_UserCountBuffTimer.cancel();
					_UserCountBuffTimer = null;
				}

				Config.ServerAdSetting.WHOISCONTER = 0;
				_buffMaxCount = 0;
				_remainBuffTime = 0;
				_totalBuffTime = 0;
				return;
			} else if (inc.equalsIgnoreCase("0")) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("目前膨脹人數: " + Config.ServerAdSetting.WHOISCONTER)));
				pc.sendPackets(String.valueOf(new S_SystemMessage("總膨脹人數: " + _buffMaxCount)));
				pc.sendPackets(String.valueOf(new S_SystemMessage("剩餘膨脹人數: " + (_buffMaxCount - Config.ServerAdSetting.WHOISCONTER))));
				pc.sendPackets(String.valueOf(new S_SystemMessage("總膨脹時間: " + _totalBuffTime + "秒")));
				pc.sendPackets(String.valueOf(new S_SystemMessage("剩餘膨脹時間: " + _remainBuffTime + "秒")));
				return;
			}

			int count = Integer.parseInt(st.nextToken());

			if (count < 0) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("請輸入大於等於0的數字。")));
				return;
			}

			if (inc.equalsIgnoreCase("~")) {
				int time = Integer.parseInt(st.nextToken());

				if (time < 1) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("請輸入大於等於0的時間。")));
					return;
				}

				if (time < _totalBuffTime) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("輸入的時間少於當前設定的膨脹時間。")));
					return;
				}

				if (count < _buffMaxCount) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("輸入的數字少於當前設定的膨脹人數。")));
					return;
				}

				if (_UserCountBuffTimer != null) {
					_UserCountBuffTimer.cancel();
					_UserCountBuffTimer = null;
				}

				_remainBuffTime += time - _totalBuffTime;
				_totalBuffTime = time;
				_buffMaxCount = count;

				_UserCountBuffTimer = new UserCountBuffTimer();
				GeneralThreadPool.getInstance().execute(_UserCountBuffTimer);

				return;

			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("+膨 [0] 來確認當前狀態。")));
			pc.sendPackets(String.valueOf(new S_SystemMessage("+膨 [~] [數字] [時間] (時間單位為秒) 來自動膨脹。")));
			pc.sendPackets(String.valueOf(new S_SystemMessage("+膨 [初始化] 來初始化。")));
		}
	}
}
