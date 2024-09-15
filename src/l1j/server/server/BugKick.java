package l1j.server.server;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;

public class BugKick {
	//private static Logger _log = Logger.getLogger(BugKick.class.getName());
	
	private static BugKick _instance;

	private BugKick() {
	}

	public static BugKick getInstance() {
		if (_instance == null) {
			_instance = new BugKick();
		}
		return _instance;
	}

	public void KickPlayer(L1PcInstance pc){
		try {
//		L1Teleport.teleport(pc, 32737, 32796, (short) 99, 5, true);
		pc.start_teleport(32737, 32796, 99, 5, 18339, true, false);
			pc.sendPackets(new S_Poison(pc.getId(), 2)); // 已進入凍結狀態
			pc.broadcastPacket(new S_Poison(pc.getId(), 2)); // 已進入凍結狀態
		pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
		pc.killSkillEffectTimer(87);
		pc.setSkillEffect(87, 24 * 60 * 60 * 1000);//到此為止，暈眩

			pc.sendPackets(new S_ChatPacket(pc, "如果沒有使用漏洞，你沒理由來這裡吧??"));

			L1World.getInstance().broadcastServerMessage("\fY漏洞使用者 [" + pc.getName() + "] 請舉報!!");
		} catch (Exception e) {
			System.out.println(pc.getName() + " Execution ground registration error");
		}
	}
}
	