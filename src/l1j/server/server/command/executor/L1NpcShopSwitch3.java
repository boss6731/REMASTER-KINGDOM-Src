/**
 * 무인 엔피씨 상점 시작 명령어
 * by - Eva Team.
 */
package l1j.server.server.server.command.executor;

import l1j.server.server.NpcShopSystem3;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.logging.Logger;

public class L1NpcShopSwitch3 implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1NpcShopSwitch3.class.getName());

	private L1NpcShopSwitch3() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1NpcShopSwitch3();
	}

	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			boolean power = NpcShopSystem3.getInstance().isPower();
			if (arg.equalsIgnoreCase("開")) {
				if (power) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("已經在執行中。")));
					return;
				} else {
					NpcShopSystem3.getInstance().npcShopStart();
				}
			} else if (arg.equalsIgnoreCase("關")) {
				if (!power) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("尚未執行。")));
					return;
				} else {
					NpcShopSystem3.getInstance().npcShopStop();
				}
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage(".管理商店3 開/關")));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(".管理商店3 方法錯誤")));
		}
	}
}
	
	