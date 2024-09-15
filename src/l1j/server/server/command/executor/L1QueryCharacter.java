package l1j.server.server.command.executor;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.server.GameClient;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

public class L1QueryCharacter implements L1CommandExecutor {
	private L1QueryCharacter() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1QueryCharacter();
	}

	static private String getCClass(String ip) {
		return ip.substring(0, ip.lastIndexOf('.'));
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(arg);

			if (target != null) {
				long totalAdena = 0;
				GameClient client = target.getNetConnection();

				if (client == null) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("無法查詢未在線上的角色。")));
					return;
				}

				String cClass = getCClass(client.getIp());

				Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();

				for (L1PcInstance otherPc : pcs) {

					if (otherPc.getNetConnection() != null) {
						String otherPcIp = otherPc.getNetConnection().getIp();

						if (cClass.equals(getCClass(otherPcIp))) {
							totalAdena += printInfo(pc, otherPc, otherPcIp);
						}
					}
				}

				pc.sendPackets(String.valueOf(new S_SystemMessage("所有帳號內的金幣總合為 [" + totalAdena + "]。")));

			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("該名稱的角色在世界中不存在。")));
			}

		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [角色名] 請這樣輸入。")));
		}
	}

	private long printInfo(L1PcInstance master, L1PcInstance pc, String ip) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		long storageAdena = 0;
		long characterAdena = 0;
		try {

			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn.prepareStatement(
					"select ifnull(sum(count), 0) as 'adena' from character_warehouse where item_id = 40308 and account_name = ?");
			pstm.setString(1, pc.getAccountName());
			rs = pstm.executeQuery();
			if (rs.next()) {
				storageAdena = rs.getInt("adena");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(conn);
		}

		try {

			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn.prepareStatement(
					"select ifnull(sum(count), 0) as 'adena' from character_items where item_id = 40308 and char_id IN (select objid FROM characters WHERE account_name = ?)");
			pstm.setString(1, pc.getAccountName());
			rs = pstm.executeQuery();
			if (rs.next()) {
				characterAdena = rs.getInt("adena");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(conn);
		}

		master.sendPackets(String.valueOf(new S_SystemMessage(ip + " 的 [" + pc.getName() + "] 正在連線中，帳號倉庫中的金幣為 [" + storageAdena+ "]，角色內的金幣總和為 [" + characterAdena + "]。")));

		return storageAdena + characterAdena;
	}
}
