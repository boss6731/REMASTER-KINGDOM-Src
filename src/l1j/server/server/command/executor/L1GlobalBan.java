package l1j.server.server.server.command.executor;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.server.GameClient;
import l1j.server.server.server.datatables.IpTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;

public class L1GlobalBan implements L1CommandExecutor {

	public static L1CommandExecutor getInstance() {
		return new L1GlobalBan();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(arg);

			if (target != null) {
				GameClient client = target.getNetConnection();

				if (client == null) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("無法查詢未連線的角色。")));
					return;
				}

				String targetIp = client.getIp();
				String cClass = getCClass(targetIp);

				Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();

				for (L1PcInstance otherPc : pcs) {

					if (otherPc.getNetConnection() != null) {
						String otherPcIp = otherPc.getNetConnection().getIp();

						if (cClass.equals(getCClass(otherPcIp))) {
							otherPc.sendPackets(new S_Disconnect().toString());
							pc.sendPackets(String.valueOf(new S_SystemMessage("已驅逐正在連線的角色 [" + otherPc.getName() + "]。")));
						}
					}
				}

				banGlobalIp(targetIp);
				banGlobalAccounts(targetIp);

			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("名為這樣的角色在世界中不存在。 ")));
			}

		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [角色名] 請輸入。")));
		}
	}

	private void banGlobalIp(String ip) {
		String cClass = getCClass(ip);

		for (int i = 1; i <= 255; ++i) {
			String newIp = cClass + "." + i;

			IpTable iptable = IpTable.getInstance();

			if (!iptable.isBannedIp(newIp)) {
				iptable.banIp(newIp);
			}
		}
	}

	private void banGlobalAccounts(String ip) {
		Connection conn = null;
		PreparedStatement pstm = null;
		try {

			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn.prepareStatement("UPDATE accounts SET banned = 1 WHERE ip like CONCAT(?, '.%')");
			pstm.setString(1, getCClass(ip));
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(conn);
		}

	}

	static private String getCClass(String ip) {
		return ip.substring(0, ip.lastIndexOf('.'));
	}
}
