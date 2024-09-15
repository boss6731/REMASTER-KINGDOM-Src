package l1j.server.server.server.command.executor;

import l1j.server.server.NpcShopSystem2;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.logging.Logger;

public class L1NpcShopSwitch2 implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1NpcShopSwitch2.class.getName());

	private L1NpcShopSwitch2() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1NpcShopSwitch2();
	}

	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			boolean power = NpcShopSystem2.getInstance().isPower();
			if (arg.equalsIgnoreCase("開")) {
				if (power) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("已經在執行中。")));
					return;
				} else {
					NpcShopSystem2.getInstance().npcShopStart();
				}
			} else if (arg.equalsIgnoreCase("關")) {
				if (!power) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("尚未執行。")));
					return;
				} else {
					NpcShopSystem2.getInstance().npcShopStop();
				}
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage(".管理商店2 開/關")));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(".管理商店2 方法錯誤")));
		}
	}
}