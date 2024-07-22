package MJShiftObject.Battle.Thebe;

import MJShiftObject.Battle.MJShiftBattleArgs;
import MJShiftObject.MJShiftObjectManager;

import java.util.ArrayList;
import java.util.Collection;

import l1j.server.MJTemplate.Builder.MJMonsterSpawnBuilder;
import l1j.server.MJTemplate.Interface.MJMonsterDeathHandler;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.Lineage2D.MJRectangle;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_THEBE_CAPTURE_INFO_NOTI_PACKET;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.types.Point;

public class MJThebeTeamInfo extends MJShiftBattleTeamInfo<MJThebeCharacterInfo> {
	public int capture_point;
	public int stone_kill_count;
	public int stone_summon_seconds;
	private boolean is_stone_summon;

	public static MJThebeTeamInfo newInstance(int team_id, MJRectangle team_rt, MJRectangle inner_team_rt, MJPoint stone_pt) {
		MJThebeTeamInfo bInfo = newInstance(team_id);
		bInfo.capture_point = 0;
		bInfo.m_rt = team_rt;
		bInfo.m_inner_rt = inner_team_rt;
		bInfo.m_stone_pt = stone_pt;
		return bInfo;
	}

	private MJRectangle m_rt;
	private MJRectangle m_inner_rt;
	private MJPoint m_stone_pt;
	private MJMonsterSpawnBuilder m_stone_builder;
	private L1MonsterInstance m_stone;

	public static MJThebeTeamInfo newInstance(int team_id) {
		return new MJThebeTeamInfo(team_id);
	}


	private MJThebeTeamInfo(int team_id) {
		super(team_id);
		this.team_id = team_id;
		this.m_players = new ConcurrentHashMap<>();
		this.stone_summon_seconds = MJShiftBattleArgs.THEBE_STONE_REGEN_SECONDS;
		this.is_stone_summon = false;
		this.m_stone_builder = (new MJMonsterSpawnBuilder()).setNpc(new int[]{MJShiftBattleArgs.THEBE_STONE_NPC_ID[team_id - 4]});
		this.stone_kill_count = 0;
		this.server_description = "";
	}


	public void clear() {
		super.clear();
		if (this.m_stone != null) {
			this.m_stone.deleteMe();
			this.m_stone = null;
		}
	}


	public void do_enter(L1PcInstance pc, int rank) {
		int object_id = pc.getId();
		MJThebeCharacterInfo cInfo = (MJThebeCharacterInfo) this.m_players.get(Integer.valueOf(object_id));
		if (cInfo == null) {
			String source_name = MJShiftObjectManager.getInstance().get_source_character_name(pc);
			if (MJString.isNullOrEmpty(source_name))
				source_name = pc.getName();
			cInfo = MJThebeCharacterInfo.newInstance(pc, object_id, source_name, this);
			cInfo.battle_rank = rank;
			this.server_description = cInfo.home_server_name;
			this.m_players.put(Integer.valueOf(object_id), cInfo);
		} else {
			cInfo.owner = pc;
		}
		MJPoint pt = this.m_rt.toRandPoint(50);
		pc.set_battle_info(cInfo);
		pc.start_teleportForGM(pt.x, pt.y, this.m_rt.mapId, pc.getHeading(), 18339, true, true);
	}


	public void do_inner_enter(L1PcInstance pc) {
		MJPoint pt = this.m_inner_rt.toRandPoint(50);
		pc.start_teleportForGM(pt.x, pt.y, this.m_inner_rt.mapId, pc.getHeading(), 18339, true, true);
	}


	public int[] next_position(L1PcInstance pc) {
		MJPoint pt = this.m_rt.toRandPoint(50);
		return new int[]{pt.x, pt.y, pt.mapId};
	}


	public void do_tick() {
		if (this.is_stone_summon) {
			return;
		}
		if (--this.stone_summon_seconds <= 0) {
			this.is_stone_summon = true;
			this.m_stone = this.m_stone_builder.build(this.m_stone_pt.x, this.m_stone_pt.y, 1, this.m_stone_pt.mapId, new MJMonsterDeathHandler() {
				public boolean onDeathNotify(L1MonsterInstance m) {
					if (m._destroyed) {
						return false;
					}
					MJThebeTeamInfo.this.m_stone = null;
					m.broadcastPacket((ServerBasePacket) new S_DoActionGFX(m.getId(), 11));
					m.setDeathProcessing(true);
					m.allTargetClear();
					m.setCurrentHp(0);
					m.setDead(true);
					m.setStatus(8);
					m.getMap().setPassable((Point) m.getLocation(), true);
					m.deleteMe();

					if (++MJThebeTeamInfo.this.stone_kill_count >= 3) {
						return true;
					}
					MJThebeTeamInfo.this.is_stone_summon = false;
					MJThebeTeamInfo.this.stone_summon_seconds = MJShiftBattleArgs.THEBE_STONE_REGEN_SECONDS;
					return true;
				}
			});
		}
	}

	public void do_revision_map(L1PcInstance pc) {
		int map_id = pc.getMapId();
		if (map_id != 10500 && map_id != 10502) {
			MJPoint pt = this.m_rt.toRandPoint(50);
			pc.start_teleportForGM(pt.x, pt.y, this.m_rt.mapId, pc.getHeading(), 18339, true, true);
		}
	}

	public SC_THEBE_CAPTURE_INFO_NOTI_PACKET.CapturePointT to_capture_point() {
		SC_THEBE_CAPTURE_INFO_NOTI_PACKET.CapturePointT cpt = SC_THEBE_CAPTURE_INFO_NOTI_PACKET.CapturePointT.newInstance();
		cpt.set_capture_point(this.capture_point);
		cpt.set_team_id(this.team_id);
		cpt.set_homeserverno(this.homeserverno);
		return cpt;
	}

	public void on_update_rank(Collection<MJThebeTeamInfo> teams, ArrayList<MJThebeCharacterInfo> ranks) {
		SC_THEBE_CAPTURE_INFO_NOTI_PACKET noti = SC_THEBE_CAPTURE_INFO_NOTI_PACKET.newInstance();
		for (MJThebeTeamInfo tInfo : teams)
			noti.add_points(tInfo.to_capture_point());
		noti.set_remain_time_for_next_capture_event(this.stone_summon_seconds);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_THEBE_CAPTURE_INFO_NOTI_PACKET);
		for (MJThebeCharacterInfo cInfo : this.m_players.values()) {
			if (cInfo.owner == null || cInfo.owner.getNetConnection() == null || cInfo.owner.getNetConnection().isClosed()) {
				continue;
			}
			cInfo.on_update_rank(cInfo.owner, ranks);
			cInfo.owner.sendPackets(stream, false);
		}
		stream.dispose();
	}
}


