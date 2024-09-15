package l1j.server.server.command.executor;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetSafeSystem.DriveSafe.MJHddIdChecker;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX.ButtonType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX.IconType;
import l1j.server.server.Account;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.server.GameClient;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.datatables.IpTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class L1PowerKick implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1PowerKick.class.getName());

	private L1PowerKick() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1PowerKick();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String				sname	= st.nextToken();
			if(sname == null || sname.equalsIgnoreCase(""))
				throw new Exception("");

			Integer reason = S_LoginResult.banServerCodes.get(Integer.parseInt(st.nextToken()));
			if(reason == null)
				throw new Exception("");

			L1PcInstance target = L1World.getInstance().getPlayer(sname);
			IpTable iptable = IpTable.getInstance();
			if (target != null) {
				if (target.isGm()) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("管理者不會被封禁。")));
					return;
				}
				Account.ban(target.getAccountName(), reason); // 封禁帳號。

				if (!iptable.isBannedIp(target.getNetConnection().getIp())) {
					iptable.banIp(target.getNetConnection().getIp()); // 將IP添加到封禁列表。
				} else {
					pc.sendPackets(new S_SystemMessage("已註冊的IP: " + target.getNetConnection().getIp()));
				}
				pc.sendPackets(String.valueOf(new S_SystemMessage(new StringBuilder().append(target.getName()).append(" 已被永久封禁。").toString())));

				MJHddIdChecker.update_denials(target, MJHddIdChecker.DENIALS_TYPE_POWER_KICK);
				duplicateKick(target.getNetConnection().getIp(), target, reason);
			} else {
				String name = loadCharacter(sname);
				if (name != null) {
					MJHddIdChecker.update_denials(name, MJHddIdChecker.DENIALS_TYPE_POWER_KICK);
					Account.ban(name, reason);
					String nc = Account.checkIP(name);
					if (nc != null) {
						duplicateKick(nc, null, reason);
						if (!iptable.isBannedIp(nc)) {
							iptable.banIp(nc);
						} else {
							pc.sendPackets(String.valueOf(new S_SystemMessage(name + " 已註冊的IP: " + nc)));
						}
					}

					pc.sendPackets(String.valueOf(new S_SystemMessage(name + " 的帳號已被封禁。")));
				}
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [角色名] [封禁原因編號] 請輸入。")));
			pc.sendPackets(String.valueOf(new S_SystemMessage("原因1: 使用非法程式/ 原因2: 公共安全與秩序/ 原因3: 商業目的廣告")));
		}
	}

	public static void duplicateKick(String addr, final L1PcInstance target, int reason){
		try{
			Collection<GameClient> cList = MJNSHandler.getClients();
			if(target != null){
				MJRankUserLoader.getInstance().banUser(target);
				SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
				box.set_button_type(ButtonType.MB_OK);
				box.set_icon_type(IconType.MB_ICONHAND);
				box.set_message("由於您的帳號涉及危害公共安全與秩序、違反公序良俗或欺詐行為等原因，遊戲使用已被限制。詳細情況請諮詢伺服器網站的客服中心。");
				box.set_title(Config.Message.GameServerName);
				box.set_message_id(target.getId());
				target.sendPackets(box, MJEProtoMessages.SC_CUSTOM_MSGBOX, true);

				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						target.getNetConnection().kick();
						target.logout();
					}
				}, 3000L);
			}
			if(cList != null){
				for(GameClient clnt : cList){
					if(clnt == null)
						continue;
					if(clnt.getIp().equalsIgnoreCase(addr)){
						L1PcInstance tt = clnt.getActiveChar();
						if (tt == null)
							continue;

						if(tt != null){
							MJRankUserLoader.getInstance().banUser(tt);
						}

						if(clnt.getAccountName() != null && !clnt.getAccountName().equalsIgnoreCase("")) {
							Account.ban(clnt.getAccountName(), reason);
							MJHddIdChecker.update_denials(clnt.getAccountName(), MJHddIdChecker.DENIALS_TYPE_POWER_KICK_DUPLICATE);
						}
						SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
						box.set_button_type(ButtonType.MB_OK);
						box.set_icon_type(IconType.MB_ICONHAND);
						box.set_message("由於您的帳號涉及危害公共安全與秩序、違反公序良俗或欺詐行為等原因，已被限制使用遊戲。詳細情況請諮詢伺服器網站的客服中心。");
						box.set_title(Config.Message.GameServerName);
						box.set_message_id(tt.getId());
						clnt.sendPacket(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt(), true);

						final GameClient c = clnt;
						GeneralThreadPool.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								c.kick();
							}
						}, 3000L);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String loadCharacter(String charName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String name = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
			pstm.setString(1, charName);

			rs = pstm.executeQuery();

			if (rs.next()) {
				name = rs.getString("account_name");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return name;
	}
}
