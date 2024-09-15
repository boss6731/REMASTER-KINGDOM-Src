package l1j.server.server.server.command.executor;

import l1j.server.server.model.L1World;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1ToPC implements L1CommandExecutor {

	private L1ToPC() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1ToPC();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(arg);

			if (target != null) {
				pc.start_teleport(target.getX(), target.getY(), target.getMapId(), 5, 18339, false, false);
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage((new StringBuilder()).append(arg).append("：該角色不存在。").toString())));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [角色名稱] 來輸入。")));
		}
	}
}
