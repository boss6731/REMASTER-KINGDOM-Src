package l1j.server.server.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.server.Account;
import l1j.server.server.AccountAlreadyLoginException;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerFullException;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.serverpackets.S_ServerMessage;

public class LoginController {
	private static LoginController _instance;

	private Map<String, GameClient> _accounts = new ConcurrentHashMap<String, GameClient>(Config.Login.MaximumOnlineUsers);
	private Map<String, Integer> _countIp = new ConcurrentHashMap<String, Integer>(Config.Login.MaximumOnlineUsers);
	private Map<String, Integer> _countCClass = new ConcurrentHashMap<String, Integer>(Config.Login.MaximumOnlineUsers);

	private int _maxAllowedOnlinePlayers;

	private LoginController() {
	}

	public static LoginController getInstance() {
		if (_instance == null) {
			_instance = new LoginController();
		}
		return _instance;
	}

	public GameClient[] getAllAccounts() {
		return _accounts.values().toArray(new GameClient[_accounts.size()]);
	}

	public int getOnlinePlayerCount() {
		return _accounts.size();
	}

	public int getMaxAllowedOnlinePlayers() {
		return _maxAllowedOnlinePlayers;
	}

	public void setMaxAllowedOnlinePlayers(int maxAllowedOnlinePlayers) {
		_maxAllowedOnlinePlayers = maxAllowedOnlinePlayers;
	}

	private void kickClient(final GameClient client) {
		if (client == null) {
			return;
		}

		if (client.getActiveChar() != null) {
			client.getActiveChar().sendPackets(new S_ServerMessage(357));
		}

		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				client.kick();
			}
		}, 1000);
	}

	static public String getCClass(String ip) {
		return ip.substring(0, ip.lastIndexOf('.'));
	}

	public void login(GameClient client, Account account) throws GameServerFullException, AccountAlreadyLoginException {
		synchronized (this) {
			if (!account.isValid()) {
				// 未經密碼驗證或驗證失敗的賬戶被指定。
				// 此代碼存在於此以檢測錯誤。
				throw new IllegalArgumentException("未經認證的賬戶");
			} else if ((getMaxAllowedOnlinePlayers() <= getOnlinePlayerCount()) && !account.isGameMaster()) {
				throw new GameServerFullException();
			} else if (_accounts.containsKey(account.getName())) {
				kickClient(_accounts.remove(account.getName()));
				throw new AccountAlreadyLoginException();
			} else {
				String ip = client.getIp();
				_accounts.put(account.getName(), client);
				_countIp.put(ip, getIpCount(ip) + 1);

				String cClass = getCClass(ip);

				if (_countCClass.containsKey(cClass)) {
					_countCClass.put(cClass, _countCClass.get(cClass) + 1);
				} else {
					_countCClass.put(cClass, 1);
				}
			}
		}
	}

	public boolean containsAccount(String name) {
		return _accounts.containsKey(name);
	}

	public GameClient getClientByAccount(String name) {
		if (_accounts.containsKey(name)) {
			return _accounts.get(name);
		}

		return null;
	}

	public int getIpCount(String ip) {
		if (_countIp.containsKey(ip)) {
			return _countIp.get(ip);
		}
		return 0;
	}

	public int getCClassCount(String ip) {
		if (_countCClass.containsKey(getCClass(ip))) {
			return _countCClass.get(getCClass(ip));
		}

		return 0;
	}

	public boolean logout(GameClient client) {
		synchronized (this) {
			if (client == null || client.getAccountName() == null) {
				return false;
			}
			client.setLoginRecord(false);
			client.setStatus(MJClientStatus.CLNT_STS_CONNECTED);
			String ip = client.getIp();
			if(_countIp.containsKey(ip)) {
				_countIp.put(ip, getIpCount(ip) - 1);
				if (_countIp.get(ip) <= 0) {
					_countIp.remove(ip);
				}
			}

			String cClass = getCClass(ip);
			if(cClass != null && _countCClass.containsKey(cClass)) {
				_countCClass.put(cClass, _countCClass.get(cClass) - 1);
				if (_countCClass.get(cClass) == 0) {
					_countCClass.remove(cClass);
				}
			}
			return _accounts.remove(client.getAccountName()) != null;
		}
	}

	public void removeClientByAccounts(String accounts) {
		_accounts.remove(accounts);
	}
	
	/*public boolean logout(GameClient client) {
		synchronized (this) {
			if (client == null || client.getAccountName() == null) {
				return false;
			}
			client.setLoginRecord(false);
			client.setStatus(MJClientStatus.CLNT_STS_CONNECTED);
			String ip = client.getIp();
			
			if(_countIp.get(ip) == null)
				return false;
			
			_countIp.put(ip, getIpCount(ip) - 1);
			if (_countIp.get(ip) == 0) {
				_countIp.remove(ip);
			}

			String cClass = getCClass(ip);

			_countCClass.put(cClass, _countCClass.get(cClass) - 1);

			if (_countCClass.get(cClass) == 0) {
				_countCClass.remove(cClass);
			}

			return _accounts.remove(client.getAccountName()) != null;
		}
	}*/
}
