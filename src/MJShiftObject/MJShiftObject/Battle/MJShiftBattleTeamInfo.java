package MJShiftObject.Battle;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;


public abstract class MJShiftBattleTeamInfo<T extends MJShiftBattleCharacterInfo> {
	public int team_id;
	public int homeserverno;
	public String server_description;
	public AtomicLong team_point;
	protected ConcurrentHashMap<Integer, T> m_players;

	protected MJShiftBattleTeamInfo(int teamId) {
		this.team_id = teamId;
		this.m_players = new ConcurrentHashMap<>();
		this.server_description = "";
		this.team_point = new AtomicLong(0L);
	}

	public void clear() {
		this.m_players.clear();
	}

	public ArrayList<T> get_characters() {
		return new ArrayList<>(this.m_players.values());
	}

	public void broadcast(ServerBasePacket pck, boolean is_clear) {
		for (MJShiftBattleCharacterInfo cInfo : this.m_players.values()) {
			if (cInfo.owner == null || cInfo.owner.getNetConnection() == null || cInfo.owner.getNetConnection().isClosed()) {
				continue;
			}
			cInfo.owner.sendPackets(pck, false);
		}
		if (is_clear)
			pck.clear();
	}

	public void broadcast(ProtoOutputStream stream, boolean is_clear) {
		for (MJShiftBattleCharacterInfo cInfo : this.m_players.values()) {
			if (cInfo.owner == null || cInfo.owner.getNetConnection() == null || cInfo.owner.getNetConnection().isClosed()) {
				continue;
			}
			cInfo.owner.sendPackets(stream, false);
		}
		if (is_clear)
			stream.dispose();
	}

	public void broadcast(MJIProtoMessage message, MJEProtoMessages messageid) {
		ProtoOutputStream stream = message.writeTo(messageid);
		broadcast(stream, true);
	}

	public abstract void do_enter(L1PcInstance paramL1PcInstance, int paramInt);

	public abstract void do_inner_enter(L1PcInstance paramL1PcInstance);

	public abstract int[] next_position(L1PcInstance paramL1PcInstance);

	public abstract void do_tick();

	public abstract void do_revision_map(L1PcInstance paramL1PcInstance);
}


