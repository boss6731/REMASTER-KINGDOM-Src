package l1j.server.server.server.command.executor;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1ChatNG2 implements l1j.server.server.command.executor.L1CommandExecutor {

	private L1ChatNG2() {	}

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		return new L1ChatNG2();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			int time = Integer.parseInt(st.nextToken());
			String reason = st.nextToken();

			L1PcInstance tg = L1World.getInstance(). getPlayer(name);

			if (tg != null) {
				tg.setSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED, time * 60 * 1000);
				tg.sendPackets(new S_SkillIconGFX(36, time * 60).toString());
				tg.sendPackets(String.valueOf(new S_ServerMessage(286, String.valueOf(time))));
				L1World.getInstance().broadcastPacketToAll(String.valueOf(new S_SystemMessage(name + " 角色 " + String.valueOf(time) + " 分鐘禁止聊天 (原因: " + reason + ")")));
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("該角色未登入。")));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [角色名] [分鐘] [禁言原因] 輸入。")));
		}
	}

	@Override
	public void execute(L1Object pc, String cmdName, String arg) {

	}
}
