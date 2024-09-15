package l1j.server.server.server.command.executor;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Visible implements L1CommandExecutor {

	private L1Visible() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Visible();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			pc.setGmInvis(false);
			pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
			pc.sendPackets(String.valueOf(new S_Invis(pc.getId(), 0)));
			pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
			pc.sendPackets(String.valueOf(new S_SystemMessage("已解除隱身狀態。")));
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " 命令錯誤")));
		}
	}
}
