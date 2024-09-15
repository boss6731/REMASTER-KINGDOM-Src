package l1j.server.server.server.clientpackets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.server.Account;
import l1j.server.server.AccountAlreadyLoginException;

import l1j.server.server.GameServer;
import l1j.server.server.GameServerFullException;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CommonNews;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.utils.SQLUtil;

public class Authorization {
	private static Authorization uniqueInstance = null;
	private static Logger _log = Logger.getLogger(l1j.server.server.clientpackets.C_AuthLogin.class.getName());

	public static Authorization getInstance() {
		if (uniqueInstance == null) {
			synchronized (Authorization.class) {
				if (uniqueInstance == null)
					uniqueInstance = new Authorization();
			}
		}
		return uniqueInstance;
	}

	public synchronized void auth(final GameClient client, String accountName, String password, String ip, String host)
			throws IOException {
		if (!Config.Login.Allow2PC) {
			if (LoginController.getInstance().getIpCount(ip) > 0) {
				_log.info("拒絕兩台使用相同 IP 登錄的電腦。帳號=" + accountName + " IP=" + ip);
				System.out.println("拒絕兩台使用相同 IP 登錄的電腦。帳號=" + accountName + " IP=" + ip);
				client.sendPacket(new S_CommonNews("目前已有其他帳號使用此 IP 登錄。"));
				try {
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		} else if (LoginController.getInstance().getIpCount(ip) > MJNetServerLoadManager.NETWORK_CLIENT_PERMISSION && !ip.equals(MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT)) {
			_log.info("拒絕兩台使用相同 IP 登錄的電腦。帳號=" + accountName + " IP=" + ip);
			System.out.println("拒絕兩台使用相同 IP 登錄的電腦。帳號=" + accountName + " IP=" + ip);
			client.sendPacket(new S_CommonNews("目前已有其他帳號使用此 IP 登錄。"));
			try {
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		Account account = Account.load(accountName);
		if (account == null) {
			if (Config.Synchronization.AutoCreateAccounts) {
				//if (Account.checkLoginIP(ip)) {
				if (isCheckIP(ip)) {
					_log.info("    ■■■■■■■■ 帳號創建超過限制：請確認帳號是否正確 -> accountName = " + accountName);
					System.out.println("    ■■■■■■■■ 帳號創建超過限制：請確認帳號是否正確 -> accountName = " + accountName);
					client.sendPacket(new S_CommonNews(String.format("無法再創建帳號。(最多 %d 個)", MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT, MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT)));
					try {
						Runnable r = () -> {
							client.kick();
						};
						GeneralThreadPool.getInstance().schedule(r, 1500L);
					} catch (Exception e1) {
					}
					return;
				} else {
					if (!isValidAccount(accountName)) { // -- remove
						client.sendPacket(new S_LoginResult(S_LoginResult.REASON_WRONG_ACCOUNT));
						return;
					}
					if (!isValidPassword(password)) { // -- remove
						client.sendPacket(new S_LoginResult(S_LoginResult.REASON_WRONG_PASSWORD));
						return;
					} // 到此為止
					account = Account.create(accountName, password, ip, host, null);
					account = Account.load(accountName);
				}
			} else {
				_log.warning("account missing for user " + accountName);
				System.out.println("account missing for user " + accountName);
			}
		}
		if (account == null || !account.validatePassword(accountName, password)) {
			client.sendPacket(new S_LoginResult(S_LoginResult.REASON_USER_OR_PASS_WRONG));
			return;
		}
		{
			Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();
			boolean find = false;
			for (L1PcInstance bugpc : pcs) {
				if (bugpc.getAccountName().equalsIgnoreCase(accountName)&& (bugpc.isPrivateShop() == false || bugpc.getNetConnection() != null)) {
					if (bugpc.getMapId() >= 6000 && bugpc.getMapId() <= 6999 || bugpc.getMapId() >= 9000 && bugpc.getMapId() <= 9100){
						continue;
					}
					bugpc.getMap().setPassable(bugpc.getLocation(), true);
					bugpc.setX(33080);
					bugpc.setY(33392);
					bugpc.setMap((short) 4);
					System.out.println("─────────────────────────────────");
					System.out.println("因同一帳號同時連線而終止。▶帳號名稱◀: " + accountName + "");
					System.out.println("─────────────────────────────────");
					GameServer.disconnectChar(bugpc);
					bugpc.sendPackets(new S_Disconnect());
					find = true;
				}
			}
			if (find) {
				client.kick();
				return;
			}

			pcs = null;
		}

		int reason = account.getBannedCode();
		if (reason != 0) { // 禁止帳號
			System.out.println("\n┌───────────────────────────────┐");
			System.out.println("拒絕凍結帳號的登錄。帳號=" + accountName + " IP=" + ip);
			System.out.println("└───────────────────────────────┘\n");

			client.sendPacket(new S_LoginResult(reason));// 系統錯誤相關的普通通知
			return;
		}

		if (account.getAccessLevel() == Config.ServerAdSetting.GMCODE) {
			Random random = new Random();
			ip = Integer.toString(random.nextInt(80) + 100) + "." + Integer.toString(random.nextInt(100) + 50) + "."
					+ Integer.toString(random.nextInt(100) + 50) + "." + Integer.toString(random.nextInt(100) + 50);
			account.setIp(ip);
		}
		try {
			LoginController.getInstance().login(client, account);
			Account.updateLastActive(account, ip); // 更新最後登錄日期
			client.setAccount(account);
			sendNotice(client);
		} catch (GameServerFullException e) {
			client.kick();
			_log.info("最大連線人數超過: (" + client.getIp() + ") 已終止登錄。");
			System.out.println("最大連線人數超過: (" + client.getIp() + ") 已終止登錄。");
			return;
		} catch (AccountAlreadyLoginException e) {
			_log.info("同一帳號的連線: (" + client.getIp() + ") 已強制終止。");
			System.out.println("同一帳號的連線: (" + client.getIp() + ") 已強制終止。");
			client.sendPacket(new S_CommonNews("已經在連線中。強制終止連線。"));
			client.kick();
			return;
		} catch (Exception e) {
			_log.info("異常登錄錯誤。帳號=" + accountName + " 主機=" + host);
			System.out.println("異常登錄錯誤。帳號=" + accountName + " 主機=" + host);
			client.kick();
			return;
		} finally {
			account = null;
		}
	}

	private void sendNotice(GameClient client) {
		String accountName = client.getAccountName();

		// 檢查是否有需要閱讀的公告
		if (S_CommonNews.NoticeCount(accountName) > 0) {
			client.sendPacket(new S_CommonNews(accountName, client));
		} else {
			new C_CommonClick(client);
		}
		accountName = null;
	}

	private boolean isValidAccount(String account) {
		if (account.length() < 5 || account.length() > 12) {
			System.out.println("帳號長度檢查(忽略): " + account.length()); // 有時候在輸入密碼時顯示錯誤時
			return false;
		}

		char[] chars = account.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!Character.isLetterOrDigit(chars[i])) {
				return false;
			}
		}

		return true;
	}

	private boolean isValidPassword(String password) {
		if (password.length() < 6) {
			return false;
		}
		if (password.length() > 16) {
			return false;
		}

		boolean hasLetter = false;
		boolean hasDigit = false;

		char[] chars = password.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i])) {
				hasLetter = true;
			} else if (Character.isDigit(chars[i])) {
				hasDigit = true;
			} else {
				return false;
			}
		}

		if (!hasLetter || !hasDigit) {
			return false;
		}

		return true;
	}

	private boolean isCheckIP(String ip) {
		int num = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(ip) as cnt FROM accounts WHERE ip=? ");

			pstm.setString(1, ip);
			rs = pstm.executeQuery();

			if (rs.next()) num = rs.getInt("cnt");

			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

			// 同一IP創建的帳號少於3個時
			if (num < MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT) // 外部化帳號創建
				return false;
			else
				return true;
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}return false;
	}
}
