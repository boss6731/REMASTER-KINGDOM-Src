package l1j.server.InfinityBattle;

import java.sql.ResultSet;

import javolution.util.FastTable;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.templates.L1Npc;

public class InfinityBattleSpawn {
	private static InfinityBattleSpawn _instance;

	public static InfinityBattleSpawn getInstance() {
		if (_instance == null) {
			_instance = new InfinityBattleSpawn();
		}
		return _instance;
	}
	
	public FastTable<L1NpcInstance> Spawn_List(int mapid, int teamid, int stage) {
		final FastTable<L1NpcInstance> list = new FastTable<L1NpcInstance>();
		Selector.exec("select * from spawnlist_infinity_battle", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				L1NpcInstance npc = null;
				L1Npc l1npc = null;
				int x = 0;
				int y = 0;
				
				if (teamid == 1 && stage <= 7) {
					x = 32680;
					y = 33273;
				} else if (teamid == 2 && stage <= 7) {
					x = 32580;
					y = 33380;
				} else if (teamid == 3 && stage <= 7) {
					x = 32678;
					y = 33480;
				} else if (teamid == 4 && stage <= 7) {
					x = 32777;
					y = 33378;
				} else if (teamid == 1 && stage <= 10) {
					x = 32680;
					y = 33329;
				} else if (teamid == 2 && stage <= 10) {
					x = 32628;
					y = 33380;
				} else if (teamid == 3 && stage <= 10) {
					x = 32679;
					y = 33431;
				} else if (teamid == 4 && stage <= 10) {
					x = 32726;
					y = 33380;
				} else {
					x = 32679;
					y = 33378;
				}
				
				while(rs.next()) {
					InfinityBattleSpawnInfo Info = InfinityBattleSpawnInfo.newInstance(rs);
					if(Info == null)
						continue;
					for (int i = 0; i < Info.get_count(); i++) {
						l1npc = NpcTable.getInstance().getTemplate(Info.get_npc_id());
						if (l1npc != null) {
							try {
								if (stage != Info.get_stage())
									continue;
								npc = NpcTable.getInstance().newNpcInstance(Info.get_npc_id());
								npc.setId(IdFactory.getInstance().nextId());
								MJPoint pt = MJPoint.newInstance(x, y, 14, (short) mapid, 50);
								
								if (stage == 7 || (stage == 9 && npc.getNpcId() == 73201256) || stage == 10 || stage == 11) {
									npc.setX(x);
									npc.setY(y);
								} else {
									npc.setX(pt.x);
									npc.setY(pt.y);
								}
								
								npc.setMap((short) mapid);
								npc.setMap(L1WorldMap.getInstance().getMap((short) mapid));
								npc.setHomeX(npc.getX());
								npc.setHomeY(npc.getY());
								npc.setHeading(MJRnd.next(8));
								npc.setLightSize(l1npc.getLightSize());
								npc.getLight().turnOnOffLight();
								L1World.getInstance().storeObject(npc);
								L1World.getInstance().addVisibleObject(npc);
								list.add(npc);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		return list;
	}
	
	private int guage = 0;
	
	public int GuageCheck() {
		guage = 0;
		Selector.exec("select * from spawnlist_infinity_battle", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				L1NpcInstance npc = null;
				L1Npc l1npc = null;		
				while(rs.next()) {
					InfinityBattleSpawnInfo Info = InfinityBattleSpawnInfo.newInstance(rs);
					if(Info == null)
						continue;
					if(Info.get_stage() == 11)
						continue;
					for (int i = 0; i < Info.get_count(); i++) {
						l1npc = NpcTable.getInstance().getTemplate(Info.get_npc_id());
						if (l1npc != null) {
							try {
								npc = NpcTable.getInstance().newNpcInstance(Info.get_npc_id());
								guage += npc.getMaxHp();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		return guage;
	}
}
