package MJShiftObject.Battle;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public abstract class MJShiftBattleTeamInfo<T extends MJShiftBattleCharacterInfo>{
	public int team_id;
	public int homeserverno;
	public String server_description;

	public AtomicLong team_point;

	protected ConcurrentHashMap<Integer, T> m_players;
	protected MJShiftBattleTeamInfo(int teamId){
		team_id = teamId;
		m_players = new ConcurrentHashMap<Integer, T>();
		server_description = MJString.EmptyString;
		team_point = new AtomicLong(0);
	}
	
	public void clear(){
		m_players.clear();
	}
	public ArrayList<T> get_characters(){
		return new ArrayList<T>(m_players.values());
	} 
	
	public void broadcast(ServerBasePacket pck, boolean is_clear){
		for(MJShiftBattleCharacterInfo cInfo : m_players.values()){
			if(cInfo.owner == null || cInfo.owner.getNetConnection() == null || cInfo.owner.getNetConnection().isClosed())
				continue;
			
			cInfo.owner.sendPackets(pck, false);
		}
		if(is_clear)
			pck.clear();
	}
	public void broadcast(ProtoOutputStream stream, boolean is_clear){
		for(MJShiftBattleCharacterInfo cInfo : m_players.values()){
			if(cInfo.owner == null || cInfo.owner.getNetConnection() == null || cInfo.owner.getNetConnection().isClosed())
				continue;
			
			cInfo.owner.sendPackets(stream, false);
		}
		if(is_clear)
			stream.dispose();
	}
	public void broadcast(MJIProtoMessage message, MJEProtoMessages messageid){
		ProtoOutputStream stream = message.writeTo(messageid);
		broadcast(stream, true);
	}
	
	public abstract void do_enter(L1PcInstance pc, int rank);
	public abstract void do_inner_enter(L1PcInstance pc);
	public abstract int[] next_position(L1PcInstance pc);
	public abstract void do_tick();
	public abstract void do_revision_map(L1PcInstance pc);
}
