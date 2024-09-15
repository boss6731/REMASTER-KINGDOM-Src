package l1j.server.server.server.command.executor;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.server.datatables.MapsTable;
import l1j.server.server.server.datatables.NpcSpawnTable;
import l1j.server.server.server.datatables.NpcTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;

public class L1InsertSpawn implements L1CommandExecutor {

	private L1InsertSpawn() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1InsertSpawn();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		String msg = null;

		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String type = tok.nextToken();
			int npcId = Integer.parseInt(tok.nextToken().trim());
			L1Npc template = NpcTable.getInstance().getTemplate(npcId);

			if (template == null) {
				msg = "未發現該 NPC。";
				return;
			}
			if (type.equals("怪物")) {
				if (!template.getImpl().equals("L1Monster")) {
					msg = "指定的 NPC 不是 L1Monster。";
					return;
				}
				// SpawnTable.storeSpawn(pc, template);
				storeSpawn(pc, template);
			} else if (type.equals("NPC")) {
				NpcSpawnTable.getInstance().storeSpawn(pc, template);
				L1SpawnUtil.spawngmcmd(pc, npcId);
				return;
			}
			L1SpawnUtil.spawn(pc, npcId, 0, 0);
			String msg = new StringBuilder()
					.append(template.get_name())
					.append(" (" + npcId + ") ")
					.append(" 已被添加。")
					.toString();
		} catch (Exception e) {
			// _log.log(Level.SEVERE, "", e);
			String msg = cmdName + " [怪物, NPC] [NPCID] 請輸入。";
		} finally {
			if (msg != null) {
				pc.sendPackets(String.valueOf(new S_SystemMessage(msg)));
			}
		}
	}

	public static void storeSpawn(L1PcInstance pc, L1Npc npc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int count = 1;
			int randomXY = 3;
			int movement_distance = 24;
			int spawn_type = 0;
			int minRespawnDelay = 30;
			int maxRespawnDelay = 60;
			String note = npc.get_name();

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO spawnlist_ex_normal SET location_name=?,count=?,npc_id=?,group_id=?,loc_x=?,loc_y=?,area_left=?,area_top=?,area_right=?,area_bottom=?,mapid=?,movement_distance=?,spawn_type=?,min_respawn_seconds=?,max_respawn_seconds=?,note_map_name=?");
			pstm.setString(1, note);
			pstm.setInt(2, count);
			pstm.setInt(3, npc.get_npcId());
			pstm.setInt(4, 0);
			pstm.setInt(5, pc.getX());
			pstm.setInt(6, pc.getY());
			pstm.setInt(7, pc.getX() - randomXY);
			pstm.setInt(8, pc.getY() - randomXY);
			pstm.setInt(9, pc.getX() + randomXY);
			pstm.setInt(10, pc.getY() + randomXY);
			pstm.setInt(11, pc.getMapId());
			pstm.setInt(12, movement_distance);
			pstm.setInt(13, spawn_type);
			pstm.setInt(14, minRespawnDelay);
			pstm.setInt(15, maxRespawnDelay);
			pstm.setString(16, MapsTable.getInstance().getMapName(pc.getMapId()));
			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}
