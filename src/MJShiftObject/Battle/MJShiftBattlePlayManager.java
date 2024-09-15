package MJShiftObject.Battle;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import MJShiftObject.Battle.MJShiftBattleMessage.MessageInfo;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EVENT_COUNTDOWN_NOTI_PACKET;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public abstract class MJShiftBattlePlayManager<T extends MJShiftBattleTeamInfo<? extends MJShiftBattleCharacterInfo>> {

	protected HashMap<Integer, T> m_teams;
	protected ConcurrentHashMap<Integer, T> m_players_to_team;
	protected long m_ended_millis;
	protected boolean m_is_ready;
	protected int m_ready_remain_seconds;	
	protected MJShiftBattleMessage m_message;
	protected int m_message_tick;
	protected boolean m_is_local_server;
	protected AtomicInteger m_local_server_team_index;

	protected MJShiftBattlePlayManager(long ended_millis, boolean is_local_server){
		m_teams = new HashMap<Integer, T>();
		m_is_local_server = is_local_server;
		m_players_to_team = new ConcurrentHashMap<Integer, T>();
		m_is_ready = true;
		m_ended_millis = ended_millis;
		m_ready_remain_seconds = get_default_ready_seconds();
		m_local_server_team_index = new AtomicInteger(0);
		m_message_tick = get_default_auto_message_remain_seconds();
	}
	
	public void broadcast(ServerBasePacket[] pcks){
		for(T tInfo : m_teams.values()){
			for(ServerBasePacket pck : pcks)
				tInfo.broadcast(pck, false);
		}
		for(ServerBasePacket pck : pcks)
			pck.clear();
	}
	
	public void broadcast(ServerBasePacket pck){
		for(T tInfo : m_teams.values()){
			tInfo.broadcast(pck, false);
		}
		pck.clear();
	}
	
	public void broadcast(ProtoOutputStream stream){
		for(T tInfo : m_teams.values()){
			tInfo.broadcast(stream, false);
		}
		stream.dispose();
	}
	
	protected SC_EVENT_COUNTDOWN_NOTI_PACKET create_countdown(){
		if(m_is_ready)
			return SC_EVENT_COUNTDOWN_NOTI_PACKET.create(m_ready_remain_seconds, get_default_countdown_ready_message());
		long remain_seconds = (m_ended_millis - System.currentTimeMillis()) / 1000;			
		return SC_EVENT_COUNTDOWN_NOTI_PACKET.create((int)remain_seconds, get_default_countdown_play_message());
	}
	
	protected void do_message(){
		m_message_tick = get_default_auto_message_remain_seconds();
		GeneralThreadPool.getInstance().execute(new Runnable(){
			@Override
			public void run() {
				if(m_message == null)
					m_message = new MJShiftBattleMessage(get_default_auto_message_db_name(), get_default_is_auto_sequence_message());
				m_message.set_is_auto_sequence_message(get_default_is_auto_sequence_message());
				MessageInfo mInfo = m_message.next_message();
				if(mInfo == null)
                    return new L1PcInstance[0];
				
				ProtoOutputStream stream = mInfo.create_stream();
				broadcast(stream);
				broadcast(new S_SystemMessage(mInfo.message));
                return new L1PcInstance[0];
            }
		});
	}
	public void on_tick(){
		if(--m_message_tick == 0){
			do_message();
		}
		
		if(m_is_ready){
			if(--m_ready_remain_seconds > 0)
				return;

			do_ended_ready();
		}
		on_play_tick();
	}
	public boolean is_ready(){
		return m_is_ready;
	}
	protected abstract String get_default_countdown_ready_message();
	protected abstract String get_default_countdown_play_message();
	protected abstract int get_default_ready_seconds();
	protected abstract int get_default_auto_message_remain_seconds();
	protected abstract String get_default_auto_message_db_name();
	protected abstract boolean get_default_is_auto_sequence_message();
	protected abstract void do_ended_ready();
	protected abstract void on_play_tick();
	public abstract int next_update_tick();
	public abstract void on_update_tick();
	public abstract void on_enter(L1PcInstance pc);
	public abstract void on_closed();

	public static boolean is_shift_battle(L1PcInstance pc) {
		if(pc.is_shift_battle()) {
			pc.sendPackets("對抗戰進行中無法使用。");
			return true;
		}
		return false;
	}
}
