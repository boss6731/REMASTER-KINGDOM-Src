package l1j.server.server.server.command.executor;

import l1j.server.server.GameServer;
import l1j.server.server.model.L1World;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.datatables.IpTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1SKick implements L1CommandExecutor {
	private L1SKick() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1SKick();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(arg);

			IpTable iptable = IpTable.getInstance();
			if (target != null) {
				if (target.getNetConnection() != null) {
					iptable.banIp(target.getNetConnection().getIp());
				}
				pc.sendPackets(String.valueOf(new S_SystemMessage(target.getName() + " 被強制驅逐。")));
				target.setX(33080);
				target.setY(33392);
				target.setMap((short) 4);
				GameServer.disconnectChar(String.valueOf(target));
				target.sendPackets(new S_Disconnect().toString());
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("該名稱的角色在世界中不存在。")));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [角色名稱] 請輸入。")));
		}
	}
}