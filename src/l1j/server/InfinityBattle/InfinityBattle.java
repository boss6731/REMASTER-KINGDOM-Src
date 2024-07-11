package l1j.server.InfinityBattle;

import java.util.ArrayList;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.MJTemplate.Builder.MJLiftGateBuilder;
import l1j.server.MJTemplate.L1Instance.MJL1LiftGateInstance;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.TeamInfo.SC_INFINITY_BATTLE_BOARD_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.TeamInfo.SC_INFINITY_BATTLE_ENTER_MAP_NOTI;
import l1j.server.MJTemplate.MJProto.TeamInfo.SC_INFINITY_BATTLE_LEAVE_MAP_NOTI;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

public class InfinityBattle extends InfinityBattleInfo implements Runnable {
	private static InfinityBattle instance;

	public static InfinityBattle getInstance() {
		if (instance == null) {
			instance = new InfinityBattle();
		}
		return instance;
	}

	private static final int LIMIT_MIN_PLAYER_COUNT = 4; // 4
	private static final int LIMIT_MAX_PLAYER_COUNT = 64;

	public static MJL1LiftGateInstance _team1_1round_door;
	public static MJL1LiftGateInstance _team1_2round_door;
	public static MJL1LiftGateInstance _team2_1round_door;
	public static MJL1LiftGateInstance _team2_2round_door;
	public static MJL1LiftGateInstance _team3_1round_door;
	public static MJL1LiftGateInstance _team3_2round_door;
	public static MJL1LiftGateInstance _team4_1round_door;
	public static MJL1LiftGateInstance _team4_2round_door;

	private int _guage = 0;
	private FastTable<L1NpcInstance> _team1_monster;
	private int _team1_popsition = 0;
	private FastTable<L1NpcInstance> _team2_monster;
	private int _team2_popsition = 0;
	private FastTable<L1NpcInstance> _team3_monster;
	private int _team3_popsition = 0;
	private FastTable<L1NpcInstance> _team4_monster;
	private int _team4_popsition = 0;
	private FastTable<L1NpcInstance> _boss;

	private int Time = 3600;
	private int Remain_time = 0;
	private int _team1_Spwan_Remain_time = 0;
	private int _team2_Spwan_Remain_time = 0;
	private int _team3_Spwan_Remain_time = 0;
	private int _team4_Spwan_Remain_time = 0;
	private int _winner_teamid = -1;

	private boolean Running = false;

	public void setReady(boolean flag) {
		Running = flag;
	}

	public boolean isReady() {
		return Running;
	}

	public InfinityBattle() {
		if (_team1_1round_door == null) {
			MJLiftGateBuilder builder = new MJLiftGateBuilder().setGfx(18727);
			_team1_1round_door = builder.build(32676, 33311, (short) INFINITI_BATTLE_MAPID, false, 16);
			_team1_1round_door.up();
		}
		if (_team1_2round_door == null) {
			MJLiftGateBuilder builder = new MJLiftGateBuilder().setGfx(18727);
			_team1_2round_door = builder.build(32675, 33349, (short) INFINITI_BATTLE_MAPID, false, 16);
			_team1_2round_door.up();
		}
		if (_team2_1round_door == null) {
			MJLiftGateBuilder builder = new MJLiftGateBuilder().setGfx(18728);
			_team2_1round_door = builder.build(32611, 33384, (short) INFINITI_BATTLE_MAPID, true, 16);
			_team2_1round_door.up();
		}
		if (_team2_2round_door == null) {
			MJLiftGateBuilder builder = new MJLiftGateBuilder().setGfx(18728);
			_team2_2round_door = builder.build(32644, 33383, (short) INFINITI_BATTLE_MAPID, true, 16);
			_team2_2round_door.up();
		}
		if (_team3_1round_door == null) {
			MJLiftGateBuilder builder = new MJLiftGateBuilder().setGfx(18727);
			_team3_1round_door = builder.build(32674, 33448, (short) INFINITI_BATTLE_MAPID, false, 16);
			_team3_1round_door.up();
		}
		if (_team3_2round_door == null) {
			MJLiftGateBuilder builder = new MJLiftGateBuilder().setGfx(18727);
			_team3_2round_door = builder.build(32675, 33412, (short) INFINITI_BATTLE_MAPID, false, 16);
			_team3_2round_door.up();
		}
		if (_team4_1round_door == null) {
			MJLiftGateBuilder builder = new MJLiftGateBuilder().setGfx(18728);
			_team4_1round_door = builder.build(32742, 33384, (short) INFINITI_BATTLE_MAPID, true, 16);
			_team4_1round_door.up();
		}
		if (_team4_2round_door == null) {
			MJLiftGateBuilder builder = new MJLiftGateBuilder().setGfx(18728);
			_team4_2round_door = builder.build(32706, 33383, (short) INFINITI_BATTLE_MAPID, true, 16);
			_team4_2round_door.up();
		}
	}

	public void Start() {
		Reset();
		_guage = InfinityBattleSpawn.getInstance().GuageCheck();
		GeneralThreadPool.getInstance().execute(InfinityBattle.getInstance());
		if (!Config.Login.StandbyServer) {
			L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("現在可以進入激戰的競技場。"));
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "現在可以進入激戰的競技場。"));
		}
	}

	private void Reset() {
		Time = 3600;
		Remain_time = 0;
		_team1_Spwan_Remain_time = 0;
		_team2_Spwan_Remain_time = 0;
		_team3_Spwan_Remain_time = 0;
		_team4_Spwan_Remain_time = 0;
		_team1_popsition = 0;
		_team2_popsition = 0;
		_team3_popsition = 0;
		_team4_popsition = 0;
		team1_percent = 0;
		team2_percent = 0;
		team3_percent = 0;
		team4_percent = 0;
		_door_open_count = 0;
		_winner_teamid = -1;

		if (_team1_monster != null)
			_team1_monster.clear();
		if (_team2_monster != null)
			_team2_monster.clear();
		if (_team3_monster != null)
			_team3_monster.clear();
		if (_team4_monster != null)
			_team4_monster.clear();

		if (_team1_1round_door != null)
			_team1_1round_door.up();
		if (_team1_2round_door != null)
			_team1_2round_door.up();
		if (_team2_1round_door != null)
			_team2_1round_door.up();
		if (_team2_2round_door != null)
			_team2_2round_door.up();
		if (_team3_1round_door != null)
			_team3_1round_door.up();
		if (_team3_2round_door != null)
			_team3_2round_door.up();
		if (_team4_1round_door != null)
			_team4_1round_door.up();
		if (_team4_2round_door != null)
			_team4_2round_door.up();

		for (Object obj : L1World.getInstance().getVisibleObjects(INFINITI_BATTLE_MAPID).values()) {
			if (obj != null && obj instanceof L1MonsterInstance) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				deleteNpc(npc);
			}
		}

		setReady(true);
		setMiniGameStatus(Status.REST);
	}

	public void run() {
		while (Running) {
			try {
				CheckInfinityBattlePlayer();
				chart_info_send();
				TimeCheck();
				switch (getMiniGameStatus()) {
				case REST:
					if (Remain_time == 60) {
						if (Remain_time == 60) {
							L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(7535));
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "4分鐘後開始激戰的競技場比賽。"));
							}
							if (Remain_time == 120) {
							L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(7536));
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "3分鐘後開始激戰的競技場比賽。"));
							}
							if (Remain_time == 180) {
							L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(7537));
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "2分鐘後開始激戰的競技場比賽。"));
							}
							if (Remain_time == 240) {
							L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(7538));
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "1分鐘後開始激戰的競技場比賽。"));
							}
							if (Remain_time == 290) {
							for (L1PcInstance pc : Members_pc(4)) {
							pc.sendPackets(7494);
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "10秒後開始比賽。"));
					}
							}
							if (Remain_time == 300) {
							for (L1PcInstance pc : Members_pc(4)) {
							pc.sendPackets(7495);
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "第1回合開始~！"));
					}
							}
						}
						Remain_time = 0;
						setMiniGameStatus(Status.READY);
					}
					Remain_time++;
					break;
				case READY:
					if (getTeamMemberCount(4) < LIMIT_MIN_PLAYER_COUNT) {
						NoReadyInfinityBattle();
						ClearInfinityBattle();
					} else {
						//start game(spawn first monster list insert)
						_team1_monster = InfinityBattleSpawn.getInstance().Spawn_List(INFINITI_BATTLE_MAPID, 1, 1);
						_team2_monster = InfinityBattleSpawn.getInstance().Spawn_List(INFINITI_BATTLE_MAPID, 2, 1);
						_team3_monster = InfinityBattleSpawn.getInstance().Spawn_List(INFINITI_BATTLE_MAPID, 3, 1);
						_team4_monster = InfinityBattleSpawn.getInstance().Spawn_List(INFINITI_BATTLE_MAPID, 4, 1);
						_team1_popsition++;
						_team2_popsition++;
						_team3_popsition++;
						_team4_popsition++;
						Remain_time = 0;
						setMiniGameStatus(Status.ROUND_1);
					}
					break;
				case ROUND_1:
					// Round 1 monster insert
					if (_team1_monster != null)
						spawn_monster(1, Status.ROUND_1);
					if (_team2_monster != null)
						spawn_monster(2, Status.ROUND_1);
					if (_team3_monster != null)
						spawn_monster(3, Status.ROUND_1);
					if (_team4_monster != null)
						spawn_monster(4, Status.ROUND_1);
					
						if (Remain_time == 590) {
							for (L1PcInstance pc : Members_pc(4)) {
							pc.sendPackets(7498);
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "10秒後開始第2回合比賽。"));
							}
					}
							
							if (Remain_time > 600) {
							for (L1PcInstance pc : Members_pc(4)) {
							pc.sendPackets(7499);
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "第2回合開始~！"));
							}
							Remain_time = 0;
							_team1_Spwan_Remain_time = 0;
							_team2_Spwan_Remain_time = 0;
							_team3_Spwan_Remain_time = 0;
							_team4_Spwan_Remain_time = 0;
							_door_open_count = 0;
							setMiniGameStatus(Status.ROUND_2);
						}
					}
					Remain_time++;
					break;
				case ROUND_2:
					// Round 2 monster insert
					if (_team1_monster != null)
						spawn_monster(1, Status.ROUND_2);
					if (_team2_monster != null)
						spawn_monster(2, Status.ROUND_2);
					if (_team3_monster != null)
						spawn_monster(3, Status.ROUND_2);
					if (_team4_monster != null)
						spawn_monster(4, Status.ROUND_2);
					
						if (Remain_time == 480) {
							for (L1PcInstance pc : Members_pc(4)) {
								pc.sendPackets(7502);
								pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "2分钟后开始最终回合。"));
							}
						}
						
						if (Remain_time > 600) {
							for (L1PcInstance pc : Members_pc(4)) {
								pc.sendPackets(7503);
								pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "最终战开始！限制时间为10分钟。"));
							}
							_boss = InfinityBattleSpawn.getInstance().Spawn_List(INFINITI_BATTLE_MAPID, 0, 11);
							Remain_time = 0;
							Time = 600;
							setMiniGameStatus(Status.ROUND_3);
						
					}
					Remain_time++;
					break;
				case ROUND_3:
					if (_boss != null) {
						if (_boss.getFirst().isDead()) {
							for (L1PcInstance pc : Members_pc(_winner_teamid)) {
								// 插入目前項目...
								pc.getAccount().addBlessOfAin(200 * 10000, pc);
							}
							Remain_time = 0;
							Time = 300;
							setMiniGameStatus(Status.END);
						}
					}
					break;
				case END:
					//結束遊戲...
					break;
				}
			}catch(

	Exception e)
	{
		e.printStackTrace();
	}finally
	{
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}}Reset();EndInfinityBattle();ClearInfinityBattle();}

	private double team1_percent = 0;
	private double team2_percent = 0;
	private double team3_percent = 0;
	private double team4_percent = 0;

	public void chart_info_send() {
		double team1_hp_bar = 0;
		double team2_hp_bar = 0;
		double team3_hp_bar = 0;
		double team4_hp_bar = 0;

		for (L1PcInstance pc : Members_pc(4)) {
			if (pc.getMapId() == INFINITI_BATTLE_MAPID) {
				if (pc.getInfinityTeamId() == 1) {
					if (_team1_monster != null) {
						for (L1NpcInstance mon : _team1_monster) {
							team1_hp_bar += mon.getMaxHp() - mon.getCurrentHp();
						}
					}
				}
				if (pc.getInfinityTeamId() == 2) {
					if (_team2_monster != null) {
						for (L1NpcInstance mon : _team2_monster) {
							team2_hp_bar += mon.getMaxHp() - mon.getCurrentHp();
						}
					}
				}
				if (pc.getInfinityTeamId() == 3) {
					if (_team3_monster != null) {
						for (L1NpcInstance mon : _team3_monster) {
							team3_hp_bar += mon.getMaxHp() - mon.getCurrentHp();
						}
					}
				}
				if (pc.getInfinityTeamId() == 4) {
					if (_team4_monster != null) {
						for (L1NpcInstance mon : _team4_monster) {
							team4_hp_bar += mon.getMaxHp() - mon.getCurrentHp();
						}
					}
				}
			}
		}

		team1_percent = (team1_hp_bar / _guage) * 100;
		team2_percent = (team2_hp_bar / _guage) * 100;
		team3_percent = (team3_hp_bar / _guage) * 100;
		team4_percent = (team4_hp_bar / _guage) * 100;

		for (L1PcInstance pc : Members_pc(4)) {
			if (pc.getMapId() == INFINITI_BATTLE_MAPID) {
				SC_INFINITY_BATTLE_BOARD_INFO_NOTI.team_send(pc, (int) team1_percent, (int) team2_percent,
						(int) team3_percent, (int) team4_percent);
			}
		}
	}

	private void TimeCheck() {
		if (Time == 300) {
			for (L1PcInstance pc : Members_pc(4)) {
				pc.sendPackets(7504);
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "5分鐘後決戰的競技場將結束。"));
			}
		}
		if (Time == 240) {
			for (L1PcInstance pc : Members_pc(4)) {
				pc.sendPackets(7505);
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "比賽結束還有4分鐘。"));
			}
		}
		if (Time == 180) {
			for (L1PcInstance pc : Members_pc(4)) {
				pc.sendPackets(7506);
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "比賽結束還有3分鐘。"));
			}
		}
		if (Time == 120) {
			for (L1PcInstance pc : Members_pc(4)) {
				pc.sendPackets(7507);
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "比賽結束還有2分鐘。"));
			}
		}
		if (Time == 60) {
			for (L1PcInstance pc : Members_pc(4)) {
				pc.sendPackets(7508);
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "比賽結束還有1分鐘。"));
			}
		}
		if (Time == 30) {
			for (L1PcInstance pc : Members_pc(4)) {
				pc.sendPackets(7509);
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "30秒後比賽將結束。"));
			}
		}
	}

	}}

	if(Time>0){Time--;}if(Time==0){EndInfinityBattle();ClearInfinityBattle();}}

	public void EndInfinityBattle() {
		for (L1PcInstance pc : Members_pc(4)) {
			MJPoint pt = MJPoint.newInstance(33466, 32753, 10, (short) 4, 50);
			pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true);
			SC_INFINITY_BATTLE_LEAVE_MAP_NOTI.leave_send(pc, pc.getInfinityTeamId());
			pc.setInfinityTeamId(0);
		}
		Running = false;
	}

	public void ClearInfinityBattle() {
		clearPlayerMember();
		setMiniGameStatus(Status.REST);
		Running = false;
	}

	public void CheckInfinityBattlePlayer() {
		// Remain_time = 290;
		for (L1PcInstance pc : Members_pc(5)) {
			if (pc.getMapId() != INFINITI_BATTLE_MAPID) {
				SC_INFINITY_BATTLE_LEAVE_MAP_NOTI.leave_send(pc, pc.getInfinityTeamId());
				pc.setInfinityTeamId(0);
			}
		}
	}

	// 遊戲因缺少玩家而結束
	public void NoReadyInfinityBattle() {
		for (L1PcInstance pc : Members_pc(4)) {
			pc.sendPackets("由於參與人數不足，無法進行遊戲。");
			pc.getInventory().storeItem(40308, 50000);
			MJPoint pt = MJPoint.newInstance(33466, 32753, 10, (short) 4, 50);
			pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true);
			SC_INFINITY_BATTLE_LEAVE_MAP_NOTI.leave_send(pc, pc.getInfinityTeamId());
			pc.setInfinityTeamId(0);
		}
	}

	public ArrayList<L1PcInstance> Members_pc(int team) {
		ArrayList<L1PcInstance> _pc = new ArrayList<L1PcInstance>();
		switch (team) {
			case 0: // 1 team
				for (int i = 0; i < _1st_team_members.size(); i++) {
					if (_1st_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
						_pc.add(_1st_team_members.get(i));
				}
				break;
			case 1: // 2 team
				for (int i = 0; i < _2nd_team_members.size(); i++) {
					if (_2nd_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
						_pc.add(_2nd_team_members.get(i));
				}
				break;
			case 2: // 3 team
				for (int i = 0; i < _3rd_team_members.size(); i++) {
					if (_3rd_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
						_pc.add(_3rd_team_members.get(i));
				}
				break;
			case 3: // 4 team
				for (int i = 0; i < _4th_team_members.size(); i++) {
					if (_4th_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
						_pc.add(_4th_team_members.get(i));
				}
				break;
			case 4: // all team mapcheck
				for (int i = 0; i < _1st_team_members.size(); i++) {
					if (_1st_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
						_pc.add(_1st_team_members.get(i));
				}
				for (int i = 0; i < _2nd_team_members.size(); i++) {
					if (_2nd_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
						_pc.add(_2nd_team_members.get(i));
				}
				for (int i = 0; i < _3rd_team_members.size(); i++) {
					if (_3rd_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
						_pc.add(_3rd_team_members.get(i));
				}
				for (int i = 0; i < _4th_team_members.size(); i++) {
					if (_4th_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
						_pc.add(_4th_team_members.get(i));
				}
				break;
			case 5: // all team check
				for (int i = 0; i < _1st_team_members.size(); i++) {
					_pc.add(_1st_team_members.get(i));
				}
				for (int i = 0; i < _2nd_team_members.size(); i++) {
					_pc.add(_2nd_team_members.get(i));
				}
				for (int i = 0; i < _3rd_team_members.size(); i++) {
					_pc.add(_3rd_team_members.get(i));
				}
				for (int i = 0; i < _4th_team_members.size(); i++) {
					_pc.add(_4th_team_members.get(i));
				}
				break;
		}
		return _pc;
	}

	public void addTeamMembers(L1PcInstance pc) {
		for (L1PcInstance in_pc : Members_pc(5)) {
			if (in_pc.getName().equalsIgnoreCase(pc.getName())) {
				pc.sendPackets(4874);
				return;
			}
		}

		if (getTeamMemberCount(4) >= LIMIT_MAX_PLAYER_COUNT) {
			pc.sendPackets(4871);
			return;
		}

		if (!pc.getInventory().consumeItem(40308, 50000)) {
			pc.sendPackets(4866);
			return;
		}

		if (_1st_team_members.size() <= _2nd_team_members.size() && _1st_team_members.size() <= _3rd_team_members.size()
				&& _1st_team_members.size() <= _4th_team_members.size()) {
			addFirstTeamMember(pc);
			MJPoint pt = MJPoint.newInstance(32680, 33273, 14, (short) INFINITI_BATTLE_MAPID, 50);
			pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true);
			pc.setInfinityTeamId(1);
		} else if (_1st_team_members.size() > _2nd_team_members.size()
				&& _2nd_team_members.size() <= _3rd_team_members.size()
				&& _2nd_team_members.size() <= _4th_team_members.size()) {
			addSecondTeamMember(pc);
			MJPoint pt = MJPoint.newInstance(32580, 33380, 14, (short) INFINITI_BATTLE_MAPID, 50);
			pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true);
			pc.setInfinityTeamId(2);
		} else if (_2nd_team_members.size() > _3rd_team_members.size()
				&& _2nd_team_members.size() > _3rd_team_members.size()
				&& _3rd_team_members.size() <= _4th_team_members.size()) {
			addThirdTeamMember(pc);
			MJPoint pt = MJPoint.newInstance(32678, 33480, 14, (short) INFINITI_BATTLE_MAPID, 50);
			pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true);
			pc.setInfinityTeamId(3);
		} else {
			addFourthTeamMember(pc);
			MJPoint pt = MJPoint.newInstance(32777, 33378, 14, (short) INFINITI_BATTLE_MAPID, 50);
			pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true);
			pc.setInfinityTeamId(4);
		}

		SC_INFINITY_BATTLE_ENTER_MAP_NOTI.enter_send(pc, pc.getInfinityTeamId());
	}

	public int team_size() {
		int team_size = 0;
		boolean _team1 = false;
		boolean _team2 = false;
		boolean _team3 = false;
		boolean _team4 = false;
		for (int i = 0; i < _1st_team_members.size(); i++) {
			if (_1st_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
				_team1 = true;
		}
		for (int i = 0; i < _2nd_team_members.size(); i++) {
			if (_2nd_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
				_team2 = true;
		}
		for (int i = 0; i < _3rd_team_members.size(); i++) {
			if (_3rd_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
				_team3 = true;
		}
		for (int i = 0; i < _4th_team_members.size(); i++) {
			if (_4th_team_members.get(i).getMapId() == INFINITI_BATTLE_MAPID)
				_team4 = true;
		}

		if (_team1)
			team_size++;
		if (_team2)
			team_size++;
		if (_team3)
			team_size++;
		if (_team4)
			team_size++;

		return team_size;
	}

	private int _door_open_count = 0;
	private int _current_team_size = 0;

	public void spawn_monster(int team, Status ready) {
		_current_team_size = team_size();
		switch (team) {
			case 1:
				if (_team1_monster != null) {
					int size = 0;
					for (L1NpcInstance mon : _team1_monster) {
						if (mon.isDead()) {
							size++;
						}
					}
					if (size == _team1_monster.size()) {
						if (_team1_popsition >= 1 && _team1_popsition <= 7 && ready == Status.ROUND_1) {
							if (_team1_popsition < 7) {
								if (_team1_popsition == 6) {
									if (_team1_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(0)) {
											pc.sendPackets(7496);
											pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
													"怪物攻略完成！第1回合的boss即將登場。"));
										}
									}
								}
								if (_team1_Spwan_Remain_time > 5) {
									_team1_popsition++;
									_team1_monster.addAll(InfinityBattleSpawn.getInstance()
											.Spawn_List(INFINITI_BATTLE_MAPID, team, _team1_popsition));
									_team1_Spwan_Remain_time = 0;
								}
							} else if (_team1_popsition == 7) {
								if (_door_open_count < _current_team_size - 1) {
									if (_team1_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(0)) {
											pc.sendPackets(7497);
											pc.sendPackets(
													new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "第1回合已結束。需要前往下一個區域。"));
										}
										_team1_1round_door.down();
										_door_open_count++;
									}
								}
							}
						}

						if (_team1_popsition == 7 && ready == Status.ROUND_2) {
							_team1_popsition++;
							_team1_monster.addAll(InfinityBattleSpawn.getInstance().Spawn_List(INFINITI_BATTLE_MAPID,
									team, _team1_popsition));
							_team1_Spwan_Remain_time = 0;
						}
						if (_team1_popsition >= 8 && _team1_popsition <= 10 && ready == Status.ROUND_2) {
							if (_team1_popsition < 10) {
								if (_team1_popsition == 9) {
									if (_team1_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(0)) {
											pc.sendPackets(7500);
											pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
													"怪物攻略完成！第2回合的boss即將登場。"));
										}
									}
								}
								if (_team1_Spwan_Remain_time > 5) {
									_team1_popsition++;
									_team1_monster.addAll(InfinityBattleSpawn.getInstance()
											.Spawn_List(INFINITI_BATTLE_MAPID, team, _team1_popsition));
									_team1_Spwan_Remain_time = 0;
								}
							} else if (_team1_popsition == 10) {
								if (_door_open_count < 1) {
									if (_team1_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(0)) {
											pc.sendPackets(7501);
											pc.sendPackets(
													new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "第2回合已結束。需要前往下一個區域。"));
										}
										_team1_2round_door.down();
										_team1_popsition++;
										_team1_Spwan_Remain_time = 0;
										_door_open_count++;
										_winner_teamid = 0;
									}
								}
							}
						}
						_team1_Spwan_Remain_time++;
					}
				}
				break;
			case 2:
				if (_team2_monster != null) {
					int size = 0;
					for (L1NpcInstance mon : _team2_monster) {
						if (mon.isDead()) {
							size++;
						}
					}
					if (size == _team2_monster.size()) {
						if (_team2_popsition >= 1 && _team2_popsition <= 7 && ready == Status.ROUND_1) {
							if (_team2_popsition < 7) {
								if (_team2_popsition == 6) {
									if (_team2_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(1)) {
											pc.sendPackets(7496);
											pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
													"怪物攻略完成！第1回合的boss即將登場。"));
										}
									}
								}
								if (_team2_Spwan_Remain_time > 5) {
									_team2_popsition++;
									_team2_monster.addAll(InfinityBattleSpawn.getInstance()
											.Spawn_List(INFINITI_BATTLE_MAPID, team, _team2_popsition));
									_team2_Spwan_Remain_time = 0;
								}
							} else if (_team2_popsition == 7) {
								if (_door_open_count < _current_team_size - 1) {
									if (_team2_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(1)) {
											pc.sendPackets(7497);
											pc.sendPackets(
													new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "第1回合已結束。需要前往下一個區域。"));
										}
										_team2_1round_door.down();
										_door_open_count++;
									}
								}
							}
						}

						if (_team2_popsition == 7 && ready == Status.ROUND_2) {
							_team2_popsition++;
							_team2_monster.addAll(InfinityBattleSpawn.getInstance().Spawn_List(INFINITI_BATTLE_MAPID,
									team, _team2_popsition));
							_team2_Spwan_Remain_time = 0;
						}
						if (_team2_popsition >= 8 && _team2_popsition <= 10 && ready == Status.ROUND_2) {
							if (_team2_popsition < 10) {
								if (_team2_popsition == 9) {
									if (_team2_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(1)) {
											pc.sendPackets(7500);
											pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
													"怪物攻略完成！第2回合的boss即將登場。"));
										}
									}
								}
								if (_team2_Spwan_Remain_time > 5) {
									_team2_popsition++;
									_team2_monster.addAll(InfinityBattleSpawn.getInstance()
											.Spawn_List(INFINITI_BATTLE_MAPID, team, _team2_popsition));
									_team2_Spwan_Remain_time = 0;
								}
							} else if (_team2_popsition == 10) {
								if (_door_open_count < 1) {
									if (_team2_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(1)) {
											pc.sendPackets(7501);
											pc.sendPackets(
													new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "第2回合已結束。需要前往下一個區域。"));
										}
										_team2_2round_door.down();
										_team2_popsition++;
										_team2_Spwan_Remain_time = 0;
										_door_open_count++;
										_winner_teamid = 1;
									}
								}
							}
						}
						_team2_Spwan_Remain_time++;
					}
				}
				break;
			case 3:
				if (_team3_monster != null) {
					int size = 0;
					for (L1NpcInstance mon : _team3_monster) {
						if (mon.isDead()) {
							size++;
						}
					}
					if (size == _team3_monster.size()) {
						if (_team3_popsition >= 1 && _team3_popsition <= 7 && ready == Status.ROUND_1) {
							if (_team3_popsition < 7) {
								if (_team3_popsition == 6) {
									if (_team3_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(2)) {
											pc.sendPackets(7496);
											pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
													"怪物攻略完成！第1回合的boss即將登場。"));
										}
									}
								}
								if (_team3_Spwan_Remain_time > 5) {
									_team3_popsition++;
									_team3_monster.addAll(InfinityBattleSpawn.getInstance()
											.Spawn_List(INFINITI_BATTLE_MAPID, team, _team3_popsition));
									_team3_Spwan_Remain_time = 0;
								}
							} else if (_team3_popsition == 7) {
								if (_door_open_count < _current_team_size - 1) {
									if (_team3_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(2)) {
											pc.sendPackets(7497);
											pc.sendPackets(
													new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "第1回合已結束。需要前往下一個區域。"));
										}
										_team3_1round_door.down();
										_door_open_count++;
									}
								}
							}
						}

						if (_team3_popsition == 7 && ready == Status.ROUND_2) {
							_team3_popsition++;
							_team3_monster.addAll(InfinityBattleSpawn.getInstance().Spawn_List(INFINITI_BATTLE_MAPID,
									team, _team3_popsition));
							_team3_Spwan_Remain_time = 0;
						}
						if (_team3_popsition >= 8 && _team3_popsition <= 10 && ready == Status.ROUND_2) {
							if (_team3_popsition < 10) {
								if (_team3_popsition == 9) {
									if (_team3_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(2)) {
											pc.sendPackets(7500);
											pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
													"怪物攻略完成！第2回合的boss即將登場。"));
										}
									}
								}
								if (_team3_Spwan_Remain_time > 5) {
									_team3_popsition++;
									_team3_monster.addAll(InfinityBattleSpawn.getInstance()
											.Spawn_List(INFINITI_BATTLE_MAPID, team, _team3_popsition));
									_team3_Spwan_Remain_time = 0;
								}
							} else if (_team3_popsition == 10) {
								if (_door_open_count < 1) {
									if (_team3_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(2)) {
											pc.sendPackets(7501);
											pc.sendPackets(
													new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "第2回合已結束。需要前往下一個區域。"));
										}
										_team3_2round_door.down();
										_team3_popsition++;
										_team3_Spwan_Remain_time = 0;
										_door_open_count++;
										_winner_teamid = 2;
									}
								}
							}
						}
						_team3_Spwan_Remain_time++;
					}
				}
				break;
			case 4:
				if (_team4_monster != null) {
					int size = 0;
					for (L1NpcInstance mon : _team4_monster) {
						if (mon.isDead()) {
							size++;
						}
					}
					if (size == _team4_monster.size()) {
						if (_team4_popsition >= 1 && _team4_popsition <= 7 && ready == Status.ROUND_1) {
							if (_team4_popsition < 7) {
								if (_team4_popsition == 6) {
									if (_team4_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(3)) {
											pc.sendPackets(7496);
											pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
													"怪物攻略完成！第1回合的boss即將登場。"));
										}
									}
								}
								if (_team4_Spwan_Remain_time > 5) {
									_team4_popsition++;
									_team4_monster.addAll(InfinityBattleSpawn.getInstance()
											.Spawn_List(INFINITI_BATTLE_MAPID, team, _team4_popsition));
									_team4_Spwan_Remain_time = 0;
								}
							} else if (_team4_popsition == 7) {
								if (_door_open_count < _current_team_size - 1) {
									if (_team4_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(3)) {
											pc.sendPackets(7497);
											pc.sendPackets(
													new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "第1回合已結束。需要前往下一個區域。"));
										}
										_team4_1round_door.down();
										_door_open_count++;
									}
								}
							}
						}

						if (_team4_popsition == 7 && ready == Status.ROUND_2) {
							_team4_popsition++;
							_team4_monster.addAll(InfinityBattleSpawn.getInstance().Spawn_List(INFINITI_BATTLE_MAPID,
									team, _team4_popsition));
							_team4_Spwan_Remain_time = 0;
						}
						if (_team4_popsition >= 8 && _team4_popsition <= 10 && ready == Status.ROUND_2) {
							if (_team4_popsition < 10) {
								if (_team4_popsition == 9) {
									if (_team4_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(3)) {
											pc.sendPackets(7500);
											pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
													"怪物攻略完成！第2回合的boss即將登場。"));
										}
									}
								}
								if (_team4_Spwan_Remain_time > 5) {
									_team4_popsition++;
									_team4_monster.addAll(InfinityBattleSpawn.getInstance()
											.Spawn_List(INFINITI_BATTLE_MAPID, team, _team4_popsition));
									_team4_Spwan_Remain_time = 0;
								}
							} else if (_team4_popsition == 10) {
								if (_door_open_count < 1) {
									if (_team4_Spwan_Remain_time == 1) {
										for (L1PcInstance pc : Members_pc(3)) {
											pc.sendPackets(7501);
											pc.sendPackets(
													new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "第2回合已結束。需要前往下一個區域。"));
										}
										_team4_2round_door.down();
										_team4_popsition++;
										_team4_Spwan_Remain_time = 0;
										_door_open_count++;
										_winner_teamid = 3;
									}
								}
							}
						}
						_team4_Spwan_Remain_time++;
					}
				}
				break;
		}
	}

	private void deleteNpc(L1NpcInstance npc) {
		npc.getMap().setPassable(npc.getX(), npc.getY(), true);
		npc.deleteMe();
	}

	public int getInfinityMapId() {
		return INFINITI_BATTLE_MAPID;
	}
}
