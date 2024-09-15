package l1j.server.server.server.command.executor;

import l1j.server.server.IdFactory;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.server.datatables.NpcTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1EventSpawn implements L1CommandExecutor {

	private L1EventSpawn() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1EventSpawn();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String nameid = tok.nextToken();
			String time1 = tok.nextToken();
			int npcId = 0;
			try {
				npcId = Integer.parseInt(nameid);
			} catch (NumberFormatException e) {
				npcId = NpcTable.getInstance().findNpcIdByNameWithoutSpace(nameid);
				if (npcId == 0) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("無法找到該 NPC。")));
					return;
				}
			}
			int time = Integer.parseInt(time1);

			nameid = NpcTable.getInstance().getTemplate(npcId).get_name();
			Eventspawn(pc, npcId, 60000 * time);

			pc.sendPackets(String.valueOf(new S_SystemMessage("(" + nameid + ") (ID:" + npcId + ") (" + time + ") 分鐘召喚")));
			L1World.getInstance().broadcastServerMessage("(" + nameid + ") 將在 (" + time + ") 分鐘內被召喚。");
			tok = null;
			nameid = null;
		} catch (Exception e) {
			// _log.log(Level.SEVERE, "", e);
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + "[NPCID] [時間(分)]")));
		}
	}

	private void Eventspawn(L1PcInstance pc, int npcId, int timeMinToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());

			npc.getLocation().set(pc.getLocation());
			npc.getLocation().forward(pc.getHeading());

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(pc.getHeading());

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMinToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMinToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
