package MJShiftObject.Battle.Thebe;

import java.util.ArrayList;
import java.util.Comparator;

import MJShiftObject.MJShiftObjectHelper;
import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Battle.MJShiftBattleArgs;
import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import MJShiftObject.Battle.MJShiftBattleMessage;
import MJShiftObject.Battle.MJShiftBattlePlayManager;
import MJShiftObject.Battle.MJShiftBattleTeamInfo;
import MJShiftObject.Object.MJShiftObject;
import l1j.server.MJTemplate.MJString;
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
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHANGE_TEAM_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EVENT_COUNTDOWN_NOTI_PACKET;
import l1j.server.MJTemplate.PacketHelper.MJPacketFactory;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.IntRange;

public class MJThebePlayManager extends MJShiftBattlePlayManager<MJThebeTeamInfo> implements 
						MJIWalkFilterHandler, 
						MJIMonsterKillHandler, 
						MJICharacterKillHandler, 
						MJITeleportHandler, 
						MJIPickupHandler, 
						MJIRestartHandler,
						MJIDeadRestartHandler {

	public static int REVISION_TEAM_ID = 0;

	private ArrayList<MJThebeCharacterInfo> m_players_rank;
	private boolean m_is_boss_spawn;
	private MJMonsterSpawnBuilder m_boss_builder;

	public MJThebePlayManager(long ended_millis, boolean is_local_server) {
		super(ended_millis, is_local_server);
		m_is_boss_spawn = false;
		m_teams.put(4 + REVISION_TEAM_ID, MJThebeTeamInfo.newInstance(4 + REVISION_TEAM_ID,
				MJRectangle.from_radius(32736, 32759, 4, 4, (short) 10500),
				MJRectangle.from_radius(32771, 32815, 4, 4, (short) 10502),
				MJPoint.newInstance(32726, 32876, 0, (short) 10500)));
		m_teams.put(5 + REVISION_TEAM_ID, MJThebeTeamInfo.newInstance(5 + REVISION_TEAM_ID,
				MJRectangle.from_radius(32666, 32895, 4, 4, (short) 10500),
				MJRectangle.from_radius(32691, 32895, 4, 4, (short) 10502),
				MJPoint.newInstance(32713, 32896, 0, (short) 10500)));
		m_teams.put(6 + REVISION_TEAM_ID, MJThebeTeamInfo.newInstance(6 + REVISION_TEAM_ID,
				MJRectangle.from_radius(32735, 33037, 4, 4, (short) 10500),
				MJRectangle.from_radius(32772, 32975, 4, 4, (short) 10502),
				MJPoint.newInstance(32725, 32915, 0, (short) 10500)));
		m_players_rank = new ArrayList<MJThebeCharacterInfo>();
		m_boss_builder = new MJMonsterSpawnBuilder().setNpc(new int[]{MJShiftBattleArgs.THEBE_BOSS_NPC_ID});

		MJWalkFilterChain.getInstance().add_handler(this);
		MJMonsterKillChain.getInstance().add_handler(this);
		MJCharacterKillChain.getInstance().add_handler(this);
		MJTeleportChain.getInstance().add_handler(this);
		MJDeadRestartChain.getInstance().add_handler(this);
		MJPickupChain.getInstance().add_handler(this);
	}

	@override
	protected String get_default_countdown_ready_message() {
		return "\f2特貝拉斯等待時間";
	}

	@override
	protected String get_default_countdown_play_message() {
		return "\f3特貝拉斯支配戰";
	}

	@Override
	protected int get_default_ready_seconds() {
		return MJShiftBattleArgs.THEBE_READY_SECONDS;
	}

	@Override
	protected int get_default_auto_message_remain_seconds() {
		return MJShiftBattleArgs.THEBE_AUTO_MESSAGE_REMAIN_SECONDS;
	}

	@Override
	protected String get_default_auto_message_db_name() {
		return MJShiftBattleMessage.DBNAME_THEBES;
	}

	@Override
	protected boolean get_default_is_auto_sequence_message() {
		return MJShiftBattleArgs.THEBE_IS_AUTO_SEQUENCE_MESSAGE;
	}

	@Override
	protected void do_ended_ready() {
		m_is_ready = false;
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				try {
					SC_EVENT_COUNTDOWN_NOTI_PACKET noti = create_countdown();
					ServerBasePacket[] pcks = MJPacketFactory.create_duplicate_message_packets("特貝拉斯支配戰即將開始。");
					ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET);
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						if (pc == null || pc.getNetConnection() == null || pc.getNetConnection().isClosed())
							continue;

						if (pc.get_battle_info() == null)
							continue;

						pc.sendPackets(pcks, false);
						pc.sendPackets(stream, false);
					}
					stream.dispose();
					for (ServerBasePacket pck : pcks)
						pck.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}
                return new L1PcInstance[0];
            }
		});
	}

	@Override
	protected void on_play_tick() {
		for (MJShiftBattleTeamInfo<?> tInfo : m_teams.values()) {
			tInfo.do_tick();
			if (m_is_boss_spawn || tInfo.team_point.longValue() < MJShiftBattleArgs.THEBE_INNER_MOVE_TEAM_POINT)
				continue;

			do_spawn_boss(tInfo);
		}
	}

	private void do_spawn_boss(MJShiftBattleTeamInfo<?> tInfo) {
		m_is_boss_spawn = true;
		m_boss_builder.build(32771, 32895, 0, (short) 10502);
		String message = String.format("%s伺服器率先達成%d點，特貝拉斯首領已生成。", tInfo.server_description, MJShiftBattleArgs.THEBE_INNER_MOVE_TEAM_POINT);
		broadcast(MJPacketFactory.create_duplicate_message_packets(message));
	}

	@Override
	public int next_update_tick() {
		return MJShiftBattleArgs.THEBE_TEAM_CHART_UPDATE_SECONDS;
	}

	@Override
	public void on_update_tick() {
		ArrayList<MJThebeCharacterInfo> team_4 = m_teams.get(4).get_characters();
		ArrayList<MJThebeCharacterInfo> team_5 = m_teams.get(5).get_characters();
		ArrayList<MJThebeCharacterInfo> team_6 = m_teams.get(6).get_characters();
		int size = team_4.size() + team_5.size() + team_6.size();
		if (size <= 0)
			return;

		ArrayList<MJThebeCharacterInfo> characters = new ArrayList<MJThebeCharacterInfo>(size);
		characters.addAll(team_4);
		characters.addAll(team_5);
		characters.addAll(team_6);
		characters.sort(new ChartSorter());

		for (int i = 0; i < size; ++i) {
			MJThebeCharacterInfo bInfo = characters.get(i);
			bInfo.battle_rank = i + 1;
		}
		m_players_rank = characters;
		for (MJThebeTeamInfo tInfo : m_teams.values())
			tInfo.on_update_rank(m_teams.values(), m_players_rank);
	}

	class ChartSorter implements Comparator<MJThebeCharacterInfo> {
		@Override
		public int compare(MJThebeCharacterInfo b1, MJThebeCharacterInfo b2) {
			if (b1.battle_point > b2.battle_point)
				return -1;
			else if (b1.battle_point < b2.battle_point)
				return 1;
			return 0;
		}
	}

	@Override
	public void on_enter(final L1PcInstance pc) {
		int object_id = pc.getId();
		MJThebeTeamInfo tInfo = m_players_to_team.get(object_id);
		if (tInfo == null) {
			int team_id = 0;
			if (m_is_local_server) {
				team_id = (m_local_server_team_index.getAndIncrement() % 3) + 4;
			} else {
				team_id = MJShiftObjectManager.getInstance().get_receiver_team_id(pc.get_server_identity());
			}
			if (!IntRange.includes(team_id, 4, 6)) {
				System.out.println(String.format("%s玩家的隊伍ID無法找到。(隊伍ID: %d, 伺服器: %s)", pc.getName(), team_id, pc.getNetConnection().get_server_identity()));
				return;
			}
			tInfo = m_teams.get(team_id + REVISION_TEAM_ID);
			m_players_to_team.put(object_id, tInfo);
		}
		tInfo.do_enter(pc, m_players_rank.size() + 1);
	}

	@Override
	public void on_closed() {
		MJWalkFilterChain.getInstance().remove_handler(this);
		MJMonsterKillChain.getInstance().remove_handler(this);
		MJCharacterKillChain.getInstance().remove_handler(this);
		MJTeleportChain.getInstance().remove_handler(this);
		MJPickupChain.getInstance().remove_handler(this);
		MJDeadRestartChain.getInstance().remove_handler(this);
		for (MJThebeTeamInfo bInfo : m_teams.values())
			bInfo.clear();
		m_players_to_team.clear();
	}

	@Override
	public void on_kill(L1PcInstance attacker, L1PcInstance victim) {
		if (m_is_ready)
			return;

		MJShiftBattleCharacterInfo victim_binfo = victim.get_battle_info();
		MJShiftBattleCharacterInfo bInfo = attacker.get_battle_info();
		if (victim_binfo == null || victim_binfo.team_info.team_id == bInfo.team_info.team_id)
			return;

		if (bInfo != null && bInfo instanceof MJThebeCharacterInfo)
			on_kill((MJThebeCharacterInfo) bInfo, MJShiftBattleArgs.THEBE_CHARACTER_KILL_POINT);
	}

	@Override
	public void on_kill(L1PcInstance attacker, L1MonsterInstance m) {
		if (m_is_ready)
			return;

		MJShiftBattleCharacterInfo bInfo = attacker.get_battle_info();
		if (bInfo != null && bInfo instanceof MJThebeCharacterInfo) {
			int npc_id = m.getNpcId();
			boolean is_stone_npcid = MJShiftBattleArgs.is_thebe_stone_npcid(npc_id);
			on_kill((MJThebeCharacterInfo) bInfo, is_stone_npcid ? MJShiftBattleArgs.THEBE_STONE_KILL_POINT : MJShiftBattleArgs.THEBE_MONSTER_KILL_POINT);
			if (is_stone_npcid) {
				broadcast(MJPacketFactory.create_duplicate_message_packets(String.format("%s玩家摧毁了支配石。", bInfo.to_name_pair())));
			} else if (npc_id == MJShiftBattleArgs.THEBE_BOSS_NPC_ID) {
				broadcast(MJPacketFactory.create_duplicate_message_packets(String.format("%s玩家擊敗了特貝拉斯首領。", bInfo.to_name_pair())));
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@override
					public void run() {
						broadcast(MJPacketFactory.create_duplicate_message_packets("剩餘時間內可自由活動。"));
                        return new L1PcInstance[0];
                    }
				}, 1000L);
			}
		}

		private void on_kill (MJThebeCharacterInfo bInfo,long added_point){
			if (m_is_ready)
				return;

			bInfo.battle_point += added_point;
			MJThebeTeamInfo tInfo = (MJThebeTeamInfo) bInfo.team_info;
			long team_point = tInfo.team_point.addAndGet(added_point);
			tInfo.capture_point = (int) team_point / MJShiftBattleArgs.THEBE_CHART_BALL_POINT;
			if (team_point >= MJShiftBattleArgs.THEBE_INNER_MOVE_TEAM_POINT &&
					(team_point - added_point) < MJShiftBattleArgs.THEBE_INNER_MOVE_TEAM_POINT) {
				broadcast(MJPacketFactory.create_duplicate_message_packets(String.format("%s 的得分使 %s 伺服器成為內部入口。", bInfo.to_name_pair(), bInfo.home_server_name)));

				@Override
				public boolean on_teleported (L1PcInstance owner,int next_x, int next_y, int map_id, int old_mapid){
					MJShiftBattleCharacterInfo cInfo = owner.get_battle_info();
					if (cInfo == null || !(cInfo instanceof MJThebeCharacterInfo))
						return false;

					cInfo.team_info.do_revision_map(owner);
					((MJThebeCharacterInfo) cInfo).on_update_rank(owner, m_players_rank);
					owner.sendPackets(create_countdown(), MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET, true);
					SC_CHANGE_TEAM_NOTI_PACKET noti = SC_CHANGE_TEAM_NOTI_PACKET.newInstance();
					noti.set_object_id(owner.getId());
					noti.set_object_team_id(cInfo.team_info.team_id);
					owner.sendPackets(noti, MJEProtoMessages.SC_CHANGE_TEAM_NOTI_PACKET, true);
		
		/*SC_TEAM_ID_SERVER_NO_MAPPING_INFO info = SC_TEAM_ID_SERVER_NO_MAPPING_INFO.newInstance();
		for(int i=4; i<=6; ++i){
			MAPPING mapping = MAPPING.newInstance();
			mapping.set_server_no(i);
			mapping.set_team_id(i);
			info.add_mapping_info(mapping);
		}
		owner.sendPackets(info, MJEProtoMessages.SC_TEAM_ID_SERVER_NO_MAPPING_INFO, true);*/
					owner.sendPackets(MJPacketFactory.create_duplicate_message_packets(m_is_ready ? "目前是等待時間。" : "特貝拉斯抗戰正在進行中。"), true);
					return false;
				}

				@Override
				public boolean is_teleport (L1PcInstance owner,int next_x, int next_y, int map_id){
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean is_moved (L1PcInstance owner,int next_x, int next_y){
					int map_id = owner.getMapId();
					if (map_id != 10500 || next_x != 32810 || (next_y != 32890 && next_y != 32891))
						return false;

					MJShiftBattleCharacterInfo bInfo = owner.get_battle_info();
					if (bInfo == null) {
						if (!owner.isGm())
							return false;

						MJThebeTeamInfo tInfo = m_teams.get(4);
						tInfo.do_inner_enter(owner);
						return true;
					}
					if (bInfo.team_info.team_point.longValue() < MJShiftBattleArgs.THEBE_INNER_MOVE_TEAM_POINT)
						return false;

					bInfo.team_info.do_inner_enter(owner);
					return true;
				}

				@Override
				public boolean on_pickup (L1PcInstance pc, L1ItemInstance item,int amount){
					if (!pc.is_shift_battle() || pc.getNetConnection() == null)
						return false;

					MJShiftBattleCharacterInfo bInfo = pc.get_battle_info();
					if (bInfo == null || !(bInfo instanceof MJThebeCharacterInfo))
						return false;

					String homeserver_identity = pc.getNetConnection().get_server_identity();
					if (MJString.isNullOrEmpty(homeserver_identity))
						return false;

					MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_object(pc);
					if (sobject == null)
						return false;

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

				@Override
				public int[] get_death_location (L1PcInstance pc){
					MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
					if (cInfo == null || !(cInfo instanceof MJThebeCharacterInfo))
						return null;


					return cInfo.team_info.next_position(pc);
				}

				@Override
				public void on_death_restarted ( final L1PcInstance pc){
					MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
					if (cInfo == null || !(cInfo instanceof MJThebeCharacterInfo))
						return;

					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							on_teleported(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getMapId());
                            return new L1PcInstance[0];
                        }
					}, 10L);
				}

				@override
				public boolean is_restart (L1PcInstance pc){
					if (pc.get_battle_info() != null) {
						pc.sendPackets("此處無法重新開始。");
						return true;
					}
					return false;
				}

				@Override
				public void on_restarted (L1PcInstance pc){

				}
			}
		}
	}
}