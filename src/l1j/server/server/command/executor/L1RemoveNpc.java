package l1j.server.server.server.command.executor;


import l1j.server.server.Controller.NpcDeleteController;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.server.datatables.NpcSpawnTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;


public class L1RemoveNpc implements L1CommandExecutor {

	private L1RemoveNpc() {}

	public static <L1CommandExecutor> L1CommandExecutor getInstance() {
		return (L1CommandExecutor) new L1RemoveNpc();
	}
	
	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		// TODO Auto-generated method stub
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			
			int npcid = Integer.parseInt(tok.nextToken());
			int time;

			try {
				time = Integer.parseInt(tok.nextToken());
			} catch (Exception e) {
				time = 0;
			}
			
			for (L1Object obj : L1World.getInstance().getVisibleObjects(pc)) {
				if (obj instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) obj;

					if (npc.getNpcId() == npcid) {
						NpcSpawnTable.getInstance().removeSpawn(npc);
						npc.setRespawn(false);
						npc.NpcDeleteTime = System.currentTimeMillis() + (time * 60 * 1000);
						NpcDeleteController.getInstance().addNpcDelete(npc);
						pc.sendPackets(String.valueOf(new S_SystemMessage(npc.getName() + " 將在 " + time + " 分鐘後刪除。")));
					}
				}
			}
			
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(".刪除 [時間(分鐘)] (輸入視野內NPC的ID後，將在指定時間後刪除，並且應用到資料庫)")));
		}
	}
}
