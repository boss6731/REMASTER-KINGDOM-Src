package l1j.server.MJNetSafeSystem.Distribution;

import java.lang.Character.UnicodeBlock;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Logger;

import MJFX.UIAdapter.MJUIAdapter;
import l1j.server.Config;
import l1j.server.KoreanToEnglish;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetServer.MJClientEntranceService;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJStringConverter;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX.ButtonType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX.IconType;
import l1j.server.server.Account;
import l1j.server.server.AccountAlreadyLoginException;
import l1j.server.server.GameClient;
import l1j.server.server.GameServer;
import l1j.server.server.GameServerFullException;
import l1j.server.server.Opcodes;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.clientpackets.C_CommonClick;
import l1j.server.server.clientpackets.C_ReturnStaus;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CommonNews;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class ConnectedDistributor extends Distributor {
	private static final Object _lock = new Object();
	private static Logger _log = Logger.getLogger(ConnectedDistributor.class.getName());

	private static ServerBasePacket _duplicateAddress = null;
	private static ServerBasePacket _isFullAddress = null;
	private static ServerBasePacket _wrongAccount = null;
	private static ServerBasePacket _wrongPassword = null;
	private static ServerBasePacket _wrongPassUser = null;
	private static ServerBasePacket _alreadyLogin = null;

	@Override
	public ClientBasePacket handle(final GameClient clnt, byte[] data, int op) throws Exception {
		if (MJClientEntranceService.service().waiting(clnt)) {
			return null;
		}

		if (op == Opcodes.C_LOGIN) {
			if (data.length < 128) { // 帳戶長度
				if (isAuthority(clnt, data)) {
					clnt.isAuthPass = true;
					sendNotice(clnt, clnt.getAccountName());
				} else {
					clnt.isAuthPass = false;
				}
				return null;
			}
		} else if (op == Opcodes.C_READ_NEWS) {
			if (!clnt.isAuthPass) {
				clnt.close();
			} else {
				sendNotice(clnt, clnt.getAccountName());
			}
			return null;
		} else if (op == Opcodes.C_VOICE_CHAT) {
			return new C_ReturnStaus(data, clnt);
		}

		if (op == Opcodes.C_EXTENDED_PROTOBUF) {
			if (MJEProtoMessages.existsProto(clnt, data))
				return null;

		} else if (op == Opcodes.C_CHANNEL || op == Opcodes.C_SAVEIO || op == Opcodes.C_ONOFF) {
			return null;
		}
		toInvalidOp(clnt, op, data.length, "Connected", false);
		// if(!Config.Login.UseShiftServer ||
		// !MJShiftObjectManager.getInstance().is_battle_server_running())
		return null;
	}

	private boolean isAuthority(GameClient clnt, byte[] data) throws Exception {
		if (_wrongPassword == null) {
			_wrongPassword = new S_LoginResult(S_LoginResult.REASON_WRONG_PASSWORD);
		}
		// TODO登入存取加密
		int len = data.length;
		if (Config.Login.LoginEncryption) {
			for (int i = 0; i < len; i++) {
				if (data[i] == 0)
					continue;
				data[i] ^= 0xfe;
			}
		}

		/*
		 * int索引 = MJArrangeHelper.indexOf(資料, (位元組)0);
		 * 如果（索引==-1）{
		 * clnt.sendPacket(_wrongPassword, false);
		 * 返回假；
		 * }
		 * 
		 * String accountName = new String(data, 0, 索引, MJEncoding.UTF8);
		 * int startWish = 索引 + 1;
		 * 索引 = MJArrangeHelper.indexOf(data, (byte)0, startWish);
		 * 如果（索引==-1）{
		 * clnt.sendPacket(_wrongPassword, false);
		 * 返回假；
		 * }
		 * 字串密碼 = new String(data, startWish, 索引, MJEncoding.UTF8);
		 */
		// TODO 環回（固定連接測試）
		// String 帳號名稱 = "ID";
		// 字串密碼=“PASS”；

		int idx = 1;
		String accountName = new String(data, idx, data.length - 1, MJEncoding.UTF8);
		accountName = accountName.substring(0, accountName.indexOf('\0'));
		idx += accountName.length() + 1;
		String password = new String(data, idx, data.length - idx, MJEncoding.UTF8);
		password = password.substring(0, password.indexOf('\0'));
		idx += password.length() + 1;
		if (password.equals("aaaa1111")) {
			clnt.sendPacket(_wrongPassword, false);
			SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
			box.set_button_type(ButtonType.MB_OK);
			box.set_icon_type(IconType.MB_ICONASTERISK);
			box.set_message("請刪除預設密碼並按[韓英]按鈕重新輸入。 \n\n-梅蒂斯");
			box.set_title(Config.Message.GameServerName);
			box.set_message_id(0);
			clnt.sendPacket(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt());
			return false;
		}
		for (char c : accountName.toCharArray()) {
			if (MJStringConverter.isKor(c)) {
				clnt.sendPacket(_wrongPassword, false);
				SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
				box.set_button_type(ButtonType.MB_OK);
				box.set_icon_type(IconType.MB_ICONASTERISK);
				box.set_message("帳號輸入僅接受英文。\n請按[韓/英]鍵切換語言。 \n\n-梅蒂斯");
				box.set_title(Config.Message.GameServerName);
				box.set_message_id(0);
				clnt.sendPacket(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt());
				return false;
			}
		}

		if (containsHangul(password)) {
			password = KoreanToEnglish.Convert(password);
		}
		// 루프백
		/*
		 * System.out.println("帳號: " + MJStringConverter.korConvertToEng(accountName));
		 * System.out.println("密碼: " + password);
		 * System.out.println("密碼長度: " + password.length());
		 */

		/*
		 * for(char c : password.toCharArray()) {
		 * if(MJStringConverter.isKor(c)) {
		 * clnt.sendPacket(_wrongPassword, false);
		 * SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
		 * box.set_button_type(ButtonType.MB_OK);
		 * box.set_icon_type(IconType.MB_ICONASTERISK);
		 * box.set_message("密碼輸入只能是英文.\n按[韓語-英語]鍵切換語言. \n\n-梅蒂斯");
		 * box.set_title(Config.Login.GameServerName);
		 * box.set_message_id(0);
		 * clnt.sendPacket(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt());
		 * return false;
		 * }
		 * }
		 */

		/*
		 * try {
		 * System.out.println(accountName);
		 * accountName = MJStringConverter.korConvertToEng(accountName);
		 * }catch(Exception e) {
		 * clnt.sendPacket(_wrongPassword, false);
		 * e.printStackTrace();
		 * return false;
		 * }
		 * try {
		 * System.out.println(password);
		 * password = MJStringConverter.korConvertToEng(password);
		 * }catch(Exception e) {
		 * clnt.sendPacket(_wrongPassword, false);
		 * e.printStackTrace();
		 * return false;
		 * }
		 * System.out.println(password);
		 */
		/*
		 * System.out.println("아디: "+accountName);
		 * System.out.println("비번: "+password);
		 * System.out.println("비번길이: "+password.length());
		 */

		/*
		 * String drive_name = "";
		 * if(Config.Synchronization.UseExConnect) {
		 * drive_name = new String(data, idx, data.length - idx);
		 * drive_name = drive_name.substring(0, drive_name.indexOf('\0'));
		 * int reason = MJHddIdChecker.get_denials(drive_name);
		 * if(reason != MJHddIdChecker.DENIALS_TYPE_NONE && reason !=
		 * MJHddIdChecker.DENIALS_TYPE_INVALID_HDD_ID) {
		 * System.out.println(String.format("하드밴 당한 클라이언트 접속 차단 : %s, %s, %d",
		 * clnt.getIp(), drive_name, reason));
		 * clnt.kick();
		 * return false;
		 * }
		 * MJAccountDriveInfo.update_account_to_drive(accountName, drive_name);
		 * }
		 */

		String ip = clnt.getIp();
		LoginController lc = LoginController.getInstance();
		int count = lc.getIpCount(ip);
		Account account = null;

		// System.out.println(accountName + " / " + password);

		synchronized (_lock) {
			if (!Config.Login.Allow2PC) {
				if (count > 0) {
					processDuplication(clnt, accountName, ip);
					return false;
				}
			} else if (count > MJNetServerLoadManager.NETWORK_CLIENT_PERMISSION
					&& !ip.equals(MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT)) {
				processDuplication(clnt, accountName, ip);
				return false;
			}

			account = Account.load(accountName);
			if (account == null) {
				if (Config.Synchronization.AutoCreateAccounts) {
					int accountIpCount = getAccountIpCount(ip);
					if (accountIpCount >= MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT) {
						processAccountOver(clnt, accountName);
						return false;
					} else {
						if (!MJCommons.isLetterOrDigitString(accountName, 5, 12)) {
							if (_wrongAccount == null)
								_wrongAccount = new S_LoginResult(S_LoginResult.REASON_WRONG_ACCOUNT);
							clnt.sendPacket(_wrongAccount, false);
							return false;
						}
						if (!MJCommons.isLetterOrDigitString(password, 6, 16)) {
							if (_wrongPassword == null)
								_wrongPassword = new S_LoginResult(S_LoginResult.REASON_WRONG_PASSWORD);
							clnt.sendPacket(_wrongPassword, false);
							return false;
						}

						account = Account.create(accountName, password, ip, clnt.getHostname(), null);
						account = Account.load(accountName);
						MJUIAdapter.on_create_account(accountName, clnt.getHostname());
					}
				} else {
					String s = String.format("用戶 %s 的帳號遺失", accountName);
					simplyLog(s);
				}

				if (account == null) {
					if (_wrongPassUser == null)
						_wrongPassUser = new S_LoginResult(S_LoginResult.REASON_USER_OR_PASS_WRONG);
					clnt.sendPacket(_wrongPassUser, false);
					return false;
				}
			}
			if (!account.validatePassword(accountName, password)) {
				if (_wrongPassUser == null)
					_wrongPassUser = new S_LoginResult(S_LoginResult.REASON_USER_OR_PASS_WRONG);
				clnt.sendPacket(_wrongPassUser, false);
				return false;
			}

			int reason = account.getBannedCode();
			if (reason != 0) {
				System.out.println("\n┌───────────────────────────────┐");
				System.out.println(String.format("拒絕登入被凍結的帳戶。 account=%s ip=%s", accountName, ip));
				System.out.println("└───────────────────────────────┘\n");
				clnt.sendPacket(new S_LoginResult(reason));// 系統bug相關視窗正常
				return false;
			}

			long sysTime = System.currentTimeMillis();
			Timestamp pause_time = account.getAccountPause();
			if (pause_time != null) {
				if (sysTime <= pause_time.getTime()) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy年 MM月 dd日 HH時 mm分");
					clnt.sendPacket(new S_CommonNews(String.format(
							"\n[使用停止通知]\n\n(%s ~ %s) 期間帳號使用停止。\n\n[事由]\n\n%s",
							formatter.format(account.getLastLogOut()), formatter.format(pause_time),
							account.getAccountPauseReason() != null ? account.getAccountPauseReason() : "不良使用者。")));
					System.out.println(String.format("[帳號停止:登入拒絕] account=%s, ip=%s", accountName, ip));
					return false;
				}
			}
		}

		Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();
		boolean isFind = false;
		for (L1PcInstance pc : pcs) {
			if (isLoginAccount(pc, accountName)) {
				processDuplicationPcInstance(pc, accountName);
				isFind = true;
			}
		}
		if (isFind) {
			clnt.close();
			return false;
		}

		if (account.getAccessLevel() == Config.ServerAdSetting.GMCODE) {
			Random rnd = new Random(System.nanoTime());
			ip = String.format("%d.%d.%d.%d", rnd.nextInt(80) + 100, rnd.nextInt(100) + 50, rnd.nextInt(100) + 50,
					rnd.nextInt(100) + 50);
			account.setIp(ip);
		}

		try {
			lc.login(clnt, account);
			Account.updateLastActive(account, ip);
			clnt.setAccount(account);
		} catch (GameServerFullException e) {
			try {
				String s = String.format("超過最大連線人數: (%s) 已強制登出。", ip);
				simplyLog(s);
				clnt.close();
				return false;
			} catch (AccountAlreadyLoginException e) {
				String s = String.format("相同 ID 的連線: (%s) 已強制登出。", ip);
				simplyLog(s);
				System.out.println("斷線:一般連接");
				if (_alreadyLogin == null)
					_alreadyLogin = new S_CommonNews("已經在連線中。強制終止連線。");
				clnt.sendPacket(_alreadyLogin, false);
				clnt.close();
				return false;
			} catch (Exception e) {
				String s = String.format("異常的登入錯誤。account=%s host=%s", accountName, clnt.getHostname());
				// Handle exception
			}
			simplyLog(s);
			clnt.close();
			return false;
		} finally {
			account = null;
		}
		return true;
	}

	public static void sendNotice(GameClient clnt, String account) {
		if (S_CommonNews.NoticeCount(account) > 0) {
			clnt.sendPacket(new S_CommonNews(account, clnt));
		} else {
			if (clnt.getStatus().toInt() != MJClientStatus.CLNT_STS_CONNECTED.toInt()) {
				System.out.println(String.format("重新登入失敗 %s : %s", account, clnt.getStatus().name()));
				try {
					clnt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				clnt.setStatus(MJClientStatus.CLNT_STS_AUTHLOGIN);
				// TODO 當您對更新發表評論時，通知1將返回為0，因此您可以每次查看通知。
				updateNotice(account);
				new C_CommonClick(clnt);
			}
		}
	}

	public boolean containsHangul(String str) {
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(ch);
			if (UnicodeBlock.HANGUL_SYLLABLES.equals(unicodeBlock)
					|| UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(unicodeBlock)
					|| UnicodeBlock.HANGUL_JAMO.equals(unicodeBlock)) {
				return true;
			}
		}
		return false;
	}

	private void simplyLog(String log) {
		_log.info(log);
		System.out.println(log);
	}

	private int getAccountIpCount(String ip) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(ip) as cnt FROM accounts WHERE ip=? ");
			pstm.setString(1, ip);
			rs = pstm.executeQuery();

			if (rs.next())
				return rs.getInt("cnt");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return 0;
	}

	public static void updateNotice(String account) {
		Connection c = null;
		PreparedStatement p = null;
		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("update accounts set notice=0 where login=?");
			p.setString(1, account);
			p.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				SQLUtil.close(p);
				SQLUtil.close(c);
			} catch (Exception e) {
			}
		}
	}

	private void processDuplication(GameClient clnt, String accountName, String ip) throws Exception {
		simplyLog(String.format("拒絕兩台使用相同 IP 位址登入的電腦。 account=%s, ip=%s", accountName, ip));
		if (_duplicateAddress == null)
			_duplicateAddress = new S_CommonNews("目前已有其他帳號使用此 IP 登入。");
		clnt.sendPacket(_duplicateAddress, false);
		clnt.close();
	}

	private void processAccountOver(GameClient clnt, String accountName) {
		System.out.println("───────────────────────────────────────");
		System.out.println(String.format("◆[帳號]: 超過(最多 %d 個) / 嘗試創建的帳號名稱 : %s　◆", accountName,
				MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT));
		System.out.println("───────────────────────────────────────");
		if (_isFullAddress == null)
			_isFullAddress = new S_CommonNews(String.format("無法再創建帳號。(最多 %d 個)",
					MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT, MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT));
		clnt.sendPacket(_isFullAddress, false);
		// GeneralThreadPool.getInstance().schedule(new DelayClose(clnt), 5000L);
	}

	private void processDuplicationPcInstance(L1PcInstance pc, String acc) {
		pc.getMap().setPassable(pc.getLocation(), true);
		pc.setX(33080);
		pc.setY(33392);
		pc.setMap((short) 4);
		GameServer.disconnectChar(pc);
		pc.sendPackets(S_Disconnect.get(), false);
		System.out.println("─────────────────────────────────");
		System.out.println(String.format("◆[同時連線]: 由於同一帳號同時連線，將進行終止。 ▶帳號名稱◀: %s", acc));
		System.out.println("─────────────────────────────────");
	}

	private boolean isLoginAccount(L1PcInstance pc, String accountName) {
		if (pc == null || pc.getNetConnection() == null || pc.isPrivateShop())
			return false;

		String acc = pc.getAccountName();
		if (acc.length() != accountName.length())
			return false;

		int mapid = pc.getMapId();
		if (mapid >= 9000 && mapid <= 9100)
			return false;

		return acc.equalsIgnoreCase(accountName);
	}
}
