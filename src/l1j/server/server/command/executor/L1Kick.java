package l1j.server.server.server.command.executor;

import l1j.server.server.model.Instance.L1ClanJoinInstance;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Kick implements L1CommandExecutor {

	private L1Kick() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Kick();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(arg);

			if (target != null) {
				pc.sendPackets(String.valueOf(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(" 已被驅逐。").toString())));
				L1ClanJoinInstance.ban_user(target);
				target.getNetConnection().kick(); // 以防萬一
				target.getNetConnection().close(); // 以防萬一
				target.sendPackets(new S_Disconnect());
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("該名稱的角色在世界中不存在。")));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " 請輸入角色名。")));
		}
	}
}