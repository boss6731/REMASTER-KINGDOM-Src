package l1j.server.server.server.command.executor;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.Spawn.DayAndNight.MJDayAndNightSpawnLoader;
import l1j.server.MJTemplate.Spawn.Normal.MJNormalSpawnLoader;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.server.datatables.NpcSpawnTable;
import l1j.server.server.server.datatables.SpawnTable;
import l1j.server.server.server.model.Instance.L1PcInstance;

import java.util.StringTokenizer;

public class L1NpcDown implements L1CommandExecutor {

	public static L1CommandExecutor getInstance() {
		return new L1NpcDown();
	}

	public void execute(L1PcInstance pc, String cmdName, String poby) {
		try {
			StringTokenizer token = new StringTokenizer(poby);
			String type = token.nextToken();
			String spawn = token.nextToken();

			if (type.equals("怪物")) {
				if (spawn.equals("1")) {
					SpawnTable.getInstance().reload();
					MJNormalSpawnLoader.getInstance().do_load();
					MJDayAndNightSpawnLoader.getInstance().do_load();

					L1World.getInstance().broadcastPacketToAll(SC_NOTIFICATION_MESSAGE.make_stream("稍後亞丁世界的所有怪物將重新安置。", MJSimpleRgb.green(), 5));
					pc.sendPackets("\f3稍後亞丁世界的所有怪物將重新安置。");
				} else if (spawn.equals("2")) {
					for (L1Object obj : L1World.getInstance().getAllObj()) {
						if ((obj != null) && ((obj instanceof L1MonsterInstance))) {
							L1MonsterInstance mon = (L1MonsterInstance) obj;
							mon.setRespawn(false);
							mon.re();
							MJNormalSpawnLoader.getInstance().do_clear();
							MJDayAndNightSpawnLoader.getInstance().do_clear();
						}
					}
					L1World.getInstance().broadcastPacketToAll(SC_NOTIFICATION_MESSAGE.make_stream("稍後亞丁世界的所有怪物將被刪除。", MJSimpleRgb.green(), 5));
					pc.sendPackets("\f3稍後亞丁世界的所有怪物將被刪除。");
				} else {
					throw new Exception();
				}
			} else if (type.equals("日元")) {
				if (spawn.equals("1")) {
					NpcSpawnTable.getInstance().reload();

					L1World.getInstance().broadcastPacketToAll(SC_NOTIFICATION_MESSAGE.make_stream("稍後亞丁世界的所有NPC將重新安置。", MJSimpleRgb.green(), 5));
					pc.sendPackets("\f2稍後亞丁世界的所有NPC將重新安置。");
					L1Reload.onEventAlram(pc);
				} else if (spawn.equals("2")) {
					for (L1Object npc : L1World.getInstance().getAllObj()) {
						if ((npc != null) && npc instanceof L1MerchantInstance) {
							L1MerchantInstance merchent = (L1MerchantInstance) npc;
							merchent.setRespawn(false);
							merchent.deleteMe();
						}
					}
					L1World.getInstance().broadcastPacketToAll(SC_NOTIFICATION_MESSAGE.make_stream("稍後亞丁世界的所有NPC將被刪除。", MJSimpleRgb.green(), 5));
					pc.sendPackets("\f2稍後亞丁世界的所有NPC將被刪除。");
				} else {
					throw new Exception();
				}
			} else
				throw new Exception();
		} catch (Exception e) {
			pc.sendPackets("." + cmdName + " 日元/怪物 (安置:1/刪除:2)");
		}
	}
}