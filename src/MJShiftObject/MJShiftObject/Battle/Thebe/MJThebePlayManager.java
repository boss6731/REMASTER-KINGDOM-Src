package MJShiftObject.Battle.Thebe;

import MJShiftObject.Battle.MJShiftBattleArgs;
import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import MJShiftObject.Battle.MJShiftBattlePlayManager;
import MJShiftObject.Battle.MJShiftBattleTeamInfo;
import MJShiftObject.MJShiftObjectHelper;
import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Object.MJShiftObject;

import java.util.ArrayList;
import java.util.Comparator;

import l1j.server.MJTemplate.Builder.MJMonsterSpawnBuilder;
import l1j.server.MJTemplate.Chain.Action.MJDeadRestartChain;
import l1j.server.MJTemplate.Chain.Action.MJIDeadRestartHandler;
import l1j.server.MJTemplate.Chain.Action.MJIPickupHandler;
import l1j.server.MJTemplate.Chain.Action.MJIRestartHandler;
import l1j.server.MJTemplate.Chain.Action.MJITeleportHandler;
import l1j.server.MJTemplate.Chain.Action.MJIWalkFilterHandler;
import l1j.server.MJTemplate.Chain.Action.MJPickupChain;
import l1j.server.MJTemplate.Chain.Action.MJTeleportChain;
import l1j.server.MJTemplate.Chain.Action.MJWalkFilterChain;
import l1j.server.MJTemplate.Chain.KillChain.MJCharacterKillChain;
import l1j.server.MJTemplate.Chain.KillChain.MJICharacterKillHandler;
import l1j.server.MJTemplate.Chain.KillChain.MJIMonsterKillHandler;
import l1j.server.MJTemplate.Chain.KillChain.MJMonsterKillChain;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.Lineage2D.MJRectangle;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHANGE_TEAM_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EVENT_COUNTDOWN_NOTI_PACKET;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.PacketHelper.MJPacketFactory;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.IntRange;


public class MJThebePlayManager
		extends MJShiftBattlePlayManager<MJThebeTeamInfo>
		implements MJIWalkFilterHandler, MJIMonsterKillHandler, MJICharacterKillHandler, MJITeleportHandler, MJIPickupHandler, MJIRestartHandler, MJIDeadRestartHandler {
	public static int REVISION_TEAM_ID = 0;
	private ArrayList<MJThebeCharacterInfo> m_players_rank;
	private boolean m_is_boss_spawn;
	private MJMonsterSpawnBuilder m_boss_builder;

	public MJThebePlayManager(long ended_millis, boolean is_local_server) {
		super(ended_millis, is_local_server);
		this.m_is_boss_spawn = false;
		this.m_teams.put(Integer.valueOf(4 + REVISION_TEAM_ID), MJThebeTeamInfo.newInstance(4 + REVISION_TEAM_ID,
				MJRectangle.from_radius(32736, 32759, 4, 4, (short) 10500),
				MJRectangle.from_radius(32771, 32815, 4, 4, (short) 10502),
				MJPoint.newInstance(32726, 32876, 0, (short) 10500)));
		this.m_teams.put(Integer.valueOf(5 + REVISION_TEAM_ID), MJThebeTeamInfo.newInstance(5 + REVISION_TEAM_ID,
				MJRectangle.from_radius(32666, 32895, 4, 4, (short) 10500),
				MJRectangle.from_radius(32691, 32895, 4, 4, (short) 10502),
				MJPoint.newInstance(32713, 32896, 0, (short) 10500)));
		this.m_teams.put(Integer.valueOf(6 + REVISION_TEAM_ID), MJThebeTeamInfo.newInstance(6 + REVISION_TEAM_ID,
				MJRectangle.from_radius(32735, 33037, 4, 4, (short) 10500),
				MJRectangle.from_radius(32772, 32975, 4, 4, (short) 10502),
				MJPoint.newInstance(32725, 32915, 0, (short) 10500)));
		this.m_players_rank = new ArrayList<>();
		this.m_boss_builder = (new MJMonsterSpawnBuilder()).setNpc(new int[]{MJShiftBattleArgs.THEBE_BOSS_NPC_ID});

		MJWalkFilterChain.getInstance().add_handler(this);
		MJMonsterKillChain.getInstance().add_handler(this);
		MJCharacterKillChain.getInstance().add_handler(this);
		MJTeleportChain.getInstance().add_handler(this);
		MJDeadRestartChain.getInstance().add_handler(this);
		MJPickupChain.getInstance().add_handler(this);
	}


	protected String get_default_countdown_ready_message() {
		return "\\f2特維拉等待時間";
	}

	protected String get_default_countdown_play_message() {
		return "\\f3特維拉斯之戰";
	}

	protected int get_default_ready_seconds() {
		return MJShiftBattleArgs.THEBE_READY_SECONDS;
	}

	protected int get_default_auto_message_remain_seconds() {
		return MJShiftBattleArgs.THEBE_AUTO_MESSAGE_REMAIN_SECONDS;
	}

	protected String get_default_auto_message_db_name() {
		return "server_battle_message_thebes";
	}

	protected boolean get_default_is_auto_sequence_message() {
		return MJShiftBattleArgs.THEBE_IS_AUTO_SEQUENCE_MESSAGE;
	}

	protected void do_ended_ready() {
		this.m_is_ready = false;
		GeneralThreadPool.getInstance().execute(new Runnable() {
			public void run() {
				try {
					SC_EVENT_COUNTDOWN_NOTI_PACKET noti = MJThebePlayManager.this.create_countdown();
					ServerBasePacket[] pcks = MJPacketFactory.create_duplicate_message_packets("爭奪特維拉斯統治權的戰爭開始了.");
					ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET);
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						if (pc == null || pc.getNetConnection() == null || pc.getNetConnection().isClosed()) {
							continue;
						}
						if (pc.get_battle_info() == null) {
							continue;
						}
						pc.sendPackets(pcks, false);
						pc.sendPackets(stream, false);
					}
					stream.dispose();
					for (ServerBasePacket pck : pcks)
						pck.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	protected void on_play_tick() {
		for (MJShiftBattleTeamInfo<?> tInfo : (Iterable<MJShiftBattleTeamInfo<?>>) this.m_teams.values()) {
			tInfo.do_tick();
			if (this.m_is_boss_spawn || tInfo.team_point.longValue() < MJShiftBattleArgs.THEBE_INNER_MOVE_TEAM_POINT) {
				continue;
			}
			do_spawn_boss(tInfo);
		}
	}

	private void do_spawn_boss(MJShiftBattleTeamInfo<?> tInfo) {
		this.m_is_boss_spawn = true;
		this.m_boss_builder.build(32771, 32895, 0, (short) 10502);
		String message = String.format("%s伺服器首先達到 %d 點，Teveras Boss 出現.", new Object[]{tInfo.server_description, Long.valueOf(MJShiftBattleArgs.THEBE_INNER_MOVE_TEAM_POINT)});
		broadcast(MJPacketFactory.create_duplicate_message_packets(message));
	}


	public int next_update_tick() {
		return MJShiftBattleArgs.THEBE_TEAM_CHART_UPDATE_SECONDS;
	}


	public void on_update_tick() {
		ArrayList<MJThebeCharacterInfo> team_4 = ((MJThebeTeamInfo) this.m_teams.get(Integer.valueOf(4))).get_characters();
		ArrayList<MJThebeCharacterInfo> team_5 = ((MJThebeTeamInfo) this.m_teams.get(Integer.valueOf(5))).get_characters();
		ArrayList<MJThebeCharacterInfo> team_6 = ((MJThebeTeamInfo) this.m_teams.get(Integer.valueOf(6))).get_characters();
		int size = team_4.size() + team_5.size() + team_6.size();
		if (size <= 0) {
			return;
		}
		ArrayList<MJThebeCharacterInfo> characters = new ArrayList<>(size);
		characters.addAll(team_4);
		characters.addAll(team_5);
		characters.addAll(team_6);
		characters.sort(new ChartSorter());

		for (int i = 0; i < size; i++) {
			MJThebeCharacterInfo bInfo = characters.get(i);
			bInfo.battle_rank = i + 1;
		}
		this.m_players_rank = characters;
		for (MJThebeTeamInfo tInfo : this.m_teams.values())
			tInfo.on_update_rank(this.m_teams.values(), this.m_players_rank);
	}

	class ChartSorter
			implements Comparator<MJThebeCharacterInfo> {
		public int compare(MJThebeCharacterInfo b1, MJThebeCharacterInfo b2) {
			if (b1.battle_point > b2.battle_point)
				return -1;
			if (b1.battle_point < b2.battle_point)
				return 1;
			return 0;
		}
	}


	public void on_enter(L1PcInstance pc) {
		int object_id = pc.getId();
		MJThebeTeamInfo tInfo = (MJThebeTeamInfo) this.m_players_to_team.get(Integer.valueOf(object_id));
		if (tInfo == null) {
			int team_id = 0;
			if (this.m_is_local_server) {
				team_id = this.m_local_server_team_index.getAndIncrement() % 3 + 4;
			} else {
				team_id = MJShiftObjectManager.getInstance().get_receiver_team_id(pc.get_server_identity());
			}
			if (!IntRange.includes(team_id, 4, 6)) {
				System.out.println(String.format("%s找不到您的團隊 ID.(%d,%s)", new Object[]{pc.getName(), Integer.valueOf(team_id), pc.getNetConnection().get_server_identity()}));
				return;
			}
			tInfo = (MJThebeTeamInfo) this.m_teams.get(Integer.valueOf(team_id + REVISION_TEAM_ID));
			this.m_players_to_team.put(Integer.valueOf(object_id), tInfo);
		}
		tInfo.do_enter(pc, this.m_players_rank.size() + 1);
	}


	public void on_closed() {
		MJWalkFilterChain.getInstance().remove_handler(this);
		MJMonsterKillChain.getInstance().remove_handler(this);
		MJCharacterKillChain.getInstance().remove_handler(this);
		MJTeleportChain.getInstance().remove_handler(this);
		MJPickupChain.getInstance().remove_handler(this);
		MJDeadRestartChain.getInstance().remove_handler(this);
		for (MJThebeTeamInfo bInfo : this.m_teams.values())
			bInfo.clear();
		this.m_players_to_team.clear();
	}


	public void on_kill(L1PcInstance attacker, L1PcInstance victim) {
		if (this.m_is_ready) {
			return;
		}
		MJShiftBattleCharacterInfo victim_binfo = victim.get_battle_info();
		MJShiftBattleCharacterInfo bInfo = attacker.get_battle_info();
		if (victim_binfo == null || victim_binfo.team_info.team_id == bInfo.team_info.team_id) {
			return;
		}
		if (bInfo != null && bInfo instanceof MJThebeCharacterInfo) {
			on_kill((MJThebeCharacterInfo) bInfo, MJShiftBattleArgs.THEBE_CHARACTER_KILL_POINT);
		}
	}

	public void on_kill(L1PcInstance attacker, L1MonsterInstance m) {
		if (this.m_is_ready) {
			return;
		}
		MJShiftBattleCharacterInfo bInfo = attacker.get_battle_info();
		if (bInfo != null && bInfo instanceof MJThebeCharacterInfo) {
			int npc_id = m.getNpcId();
			boolean is_stone_npcid = MJShiftBattleArgs.is_thebe_stone_npcid(npc_id);
			on_kill((MJThebeCharacterInfo) bInfo, is_stone_npcid ? MJShiftBattleArgs.THEBE_STONE_KILL_POINT : MJShiftBattleArgs.THEBE_MONSTER_KILL_POINT);
			if (is_stone_npcid) {
				broadcast(MJPacketFactory.create_duplicate_message_packets(String.format("%s摧毀了控製石.", new Object[]{bInfo.to_name_pair()})));
			} else if (npc_id == MJShiftBattleArgs.THEBE_BOSS_NPC_ID) {
				broadcast(MJPacketFactory.create_duplicate_message_packets(String.format("%s你擊敗了特維拉斯頭目.", new Object[]{bInfo.to_name_pair()})));
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					public void run() {
						MJThebePlayManager.this.broadcast(MJPacketFactory.create_duplicate_message_packets("剩下的時間你可以自由活動."));
					}
				}, 1000L);
			}
		}
	}


	private void on_kill(MJThebeCharacterInfo bInfo, long added_point) {
		// Byte code:
		//   0: aload_0
		//   1: getfield m_is_ready : Z
		//   4: ifeq -> 8
		//   7: return
		//   8: aload_1
		//   9: dup
		//   10: getfield battle_point : J
		//   13: lload_2
		//   14: ladd
		//   15: putfield battle_point : J
		//   18: aload_1
		//   19: getfield team_info : LMJShiftObject/Battle/MJShiftBattleTeamInfo;
		//   22: checkcast MJShiftObject/Battle/Thebe/MJThebeTeamInfo
		//   25: astore #4
		//   27: aload #4
		//   29: getfield team_point : Ljava/util/concurrent/atomic/AtomicLong;
		//   32: lload_2
		//   33: invokevirtual addAndGet : (J)J
		//   36: lstore #5
		//   38: aload #4
		//   40: lload #5
		//   42: l2i
		//   43: getstatic MJShiftObject/Battle/MJShiftBattleArgs.THEBE_CHART_BALL_POINT : I
		//   46: idiv
		//   47: putfield capture_point : I
		//   50: lload #5
		//   52: getstatic MJShiftObject/Battle/MJShiftBattleArgs.THEBE_INNER_MOVE_TEAM_POINT : J
		//   55: lcmp
		//   56: iflt -> 100
		//   59: lload #5
		//   61: lload_2
		//   62: lsub
		//   63: getstatic MJShiftObject/Battle/MJShiftBattleArgs.THEBE_INNER_MOVE_TEAM_POINT : J
		//   66: lcmp
		//   67: ifge -> 100
		//   70: aload_0
		//   71: ldc '%s님의 득점으로 %s서버는 내부 입장이 가능해졌습니다.'
		//   73: iconst_2
		//   74: anewarray java/lang/Object
		//   77: dup
		//   78: iconst_0
		//   79: aload_1
		//   80: invokevirtual to_name_pair : ()Ljava/lang/String;
		//   83: aastore
		//   84: dup
		//   85: iconst_1
		//   86: aload_1
		//   87: getfield home_server_name : Ljava/lang/String;
		//   90: aastore
		//   91: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
		//   94: invokestatic create_duplicate_message_packets : (Ljava/lang/String;)[Ll1j/server/server/serverpackets/ServerBasePacket;
		//   97: invokevirtual broadcast : ([Ll1j/server/server/serverpackets/ServerBasePacket;)V
		//   100: return
		// Line number table:
		//   Java source line number -> byte code offset
		//   #271	-> 0
		//   #272	-> 7
		//   #274	-> 8
		//   #275	-> 18
		//   #276	-> 27
		//   #277	-> 38
		//   #278	-> 50
		//   #280	-> 70
		//   #282	-> 100
		// Local variable table:
		//   start	length	slot	name	descriptor
		//   0	101	0	this	LMJShiftObject/Battle/Thebe/MJThebePlayManager;
		//   0	101	1	bInfo	LMJShiftObject/Battle/Thebe/MJThebeCharacterInfo;
		//   0	101	2	added_point	J
		//   27	74	4	tInfo	LMJShiftObject/Battle/Thebe/MJThebeTeamInfo;
		//   38	63	5	team_point	J
	}


	public boolean on_teleported(L1PcInstance owner, int next_x, int next_y, int map_id, int old_mapid) {
		MJShiftBattleCharacterInfo cInfo = owner.get_battle_info();
		if (cInfo == null || !(cInfo instanceof MJThebeCharacterInfo)) {
			return false;
		}
		cInfo.team_info.do_revision_map(owner);
		((MJThebeCharacterInfo) cInfo).on_update_rank(owner, this.m_players_rank);
		owner.sendPackets((MJIProtoMessage) create_countdown(), MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET, true);
		SC_CHANGE_TEAM_NOTI_PACKET noti = SC_CHANGE_TEAM_NOTI_PACKET.newInstance();
		noti.set_object_id(owner.getId());
		noti.set_object_team_id(cInfo.team_info.team_id);
		owner.sendPackets((MJIProtoMessage) noti, MJEProtoMessages.SC_CHANGE_TEAM_NOTI_PACKET, true);


		owner.sendPackets(MJPacketFactory.create_duplicate_message_packets(this.m_is_ready ? "目前等待時間." : "與特維拉斯的戰鬥正在進行中."), true);
		return false;
	}


	public boolean is_teleport(L1PcInstance owner, int next_x, int next_y, int map_id) {
		return false;
	}


	public boolean is_moved(L1PcInstance owner, int next_x, int next_y) {
		int map_id = owner.getMapId();
		if (map_id != 10500 || next_x != 32810 || (next_y != 32890 && next_y != 32891)) {
			return false;
		}
		MJShiftBattleCharacterInfo bInfo = owner.get_battle_info();
		if (bInfo == null) {
			if (!owner.isGm()) {
				return false;
			}
			MJThebeTeamInfo tInfo = (MJThebeTeamInfo) this.m_teams.get(Integer.valueOf(4));
			tInfo.do_inner_enter(owner);
			return true;
		}
		if (bInfo.team_info.team_point.longValue() < MJShiftBattleArgs.THEBE_INNER_MOVE_TEAM_POINT) {
			return false;
		}
		bInfo.team_info.do_inner_enter(owner);
		return true;
	}


	public boolean on_pickup(L1PcInstance pc, L1ItemInstance item, int amount) {
		if (!pc.is_shift_battle() || pc.getNetConnection() == null) {
			return false;
		}
		MJShiftBattleCharacterInfo bInfo = pc.get_battle_info();
		if (bInfo == null || !(bInfo instanceof MJThebeCharacterInfo)) {
			return false;
		}
		String homeserver_identity = pc.getNetConnection().get_server_identity();
		if (MJString.isNullOrEmpty(homeserver_identity)) {
			return false;
		}
		MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_object(pc);
		if (sobject == null) {
			return false;
		}
		L1ItemInstance external_item = ItemTable.getInstance().createItem(item.getItemId());
		external_item.setCount(amount);
		external_item.setEnchantLevel(item.getEnchantLevel());
		external_item.setIdentified(item.isIdentified());
		external_item.set_durability(item.get_durability());
		external_item.setChargeCount(item.getChargeCount());
		external_item.setRemainingTime(item.getRemainingTime());
		external_item.setLastUsed(item.getLastUsed());
		external_item.setBless(item.getItem().getBless());
		external_item.setAttrEnchantLevel(item.getAttrEnchantLevel());
		external_item.setSpecialEnchant(item.getSpecialEnchant());
		external_item.set_bless_level(item.get_bless_level());
		external_item.set_item_level(item.get_item_level());
		external_item.setHotel_Town(item.getHotel_Town());
		MJShiftObjectHelper.update_pickup_items(sobject.get_source_id(), external_item, homeserver_identity);
		return false;
	}


	public int[] get_death_location(L1PcInstance pc) {
		MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
		if (cInfo == null || !(cInfo instanceof MJThebeCharacterInfo)) {
			return null;
		}


		return cInfo.team_info.next_position(pc);
	}


	public void on_death_restarted(final L1PcInstance pc) {
		MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
		if (cInfo == null || !(cInfo instanceof MJThebeCharacterInfo)) {
			return;
		}
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			public void run() {
				MJThebePlayManager.this.on_teleported(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getMapId());
			}
		} 10L);
	}


	public boolean is_restart(L1PcInstance pc) {
		if (pc.get_battle_info() != null) {
			pc.sendPackets("無法從此處重新啟動.");
			return true;
		}
		return false;
	}

	public void on_restarted(L1PcInstance pc) {
	}
}


