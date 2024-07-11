 package MJShiftObject.Battle;

 import java.util.HashMap;
 import java.util.concurrent.ConcurrentHashMap;
 import java.util.concurrent.atomic.AtomicInteger;
 import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EVENT_COUNTDOWN_NOTI_PACKET;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public abstract class MJShiftBattlePlayManager<T extends MJShiftBattleTeamInfo<? extends MJShiftBattleCharacterInfo>>
 {
   protected HashMap<Integer, T> m_teams;
   protected ConcurrentHashMap<Integer, T> m_players_to_team;
   protected long m_ended_millis;
   protected boolean m_is_ready;
   protected int m_ready_remain_seconds;
   protected MJShiftBattleMessage m_message;
   protected int m_message_tick;
   protected boolean m_is_local_server;
   protected AtomicInteger m_local_server_team_index;

   protected MJShiftBattlePlayManager(long ended_millis, boolean is_local_server) {
     this.m_teams = new HashMap<>();
     this.m_is_local_server = is_local_server;
     this.m_players_to_team = new ConcurrentHashMap<>();
     this.m_is_ready = true;
     this.m_ended_millis = ended_millis;
     this.m_ready_remain_seconds = get_default_ready_seconds();
     this.m_local_server_team_index = new AtomicInteger(0);
     this.m_message_tick = get_default_auto_message_remain_seconds();
   }

   public void broadcast(ServerBasePacket[] pcks) {
     for (MJShiftBattleTeamInfo mJShiftBattleTeamInfo : this.m_teams.values()) {
       for (ServerBasePacket pck : pcks)
         mJShiftBattleTeamInfo.broadcast(pck, false);
     }
     for (ServerBasePacket pck : pcks)
       pck.clear();
   }

   public void broadcast(ServerBasePacket pck) {
     for (MJShiftBattleTeamInfo mJShiftBattleTeamInfo : this.m_teams.values()) {
       mJShiftBattleTeamInfo.broadcast(pck, false);
     }
     pck.clear();
   }

   public void broadcast(ProtoOutputStream stream) {
     for (MJShiftBattleTeamInfo mJShiftBattleTeamInfo : this.m_teams.values()) {
       mJShiftBattleTeamInfo.broadcast(stream, false);
     }
     stream.dispose();
   }

   protected SC_EVENT_COUNTDOWN_NOTI_PACKET create_countdown() {
     if (this.m_is_ready)
       return SC_EVENT_COUNTDOWN_NOTI_PACKET.create(this.m_ready_remain_seconds, get_default_countdown_ready_message());
     long remain_seconds = (this.m_ended_millis - System.currentTimeMillis()) / 1000L;
     return SC_EVENT_COUNTDOWN_NOTI_PACKET.create((int)remain_seconds, get_default_countdown_play_message());
   }

   protected void do_message() {
     this.m_message_tick = get_default_auto_message_remain_seconds();
     GeneralThreadPool.getInstance().execute(new Runnable()
         {
           public void run() {
             if (MJShiftBattlePlayManager.this.m_message == null)
               MJShiftBattlePlayManager.this.m_message = new MJShiftBattleMessage(MJShiftBattlePlayManager.this.get_default_auto_message_db_name(), MJShiftBattlePlayManager.this.get_default_is_auto_sequence_message());
             MJShiftBattlePlayManager.this.m_message.set_is_auto_sequence_message(MJShiftBattlePlayManager.this.get_default_is_auto_sequence_message());
             MJShiftBattleMessage.MessageInfo mInfo = MJShiftBattlePlayManager.this.m_message.next_message();
             if (mInfo == null) {
               return;
             }
             ProtoOutputStream stream = mInfo.create_stream();
             MJShiftBattlePlayManager.this.broadcast(stream);
             MJShiftBattlePlayManager.this.broadcast((ServerBasePacket)new S_SystemMessage(mInfo.message));
           }
         });
   }
   public void on_tick() {
     if (--this.m_message_tick == 0) {
       do_message();
     }

     if (this.m_is_ready) {
       if (--this.m_ready_remain_seconds > 0) {
         return;
       }
       do_ended_ready();
     }
     on_play_tick();
   }
   public boolean is_ready() {
     return this.m_is_ready;
   }

   protected abstract String get_default_countdown_ready_message();

   protected abstract String get_default_countdown_play_message();

   protected abstract int get_default_ready_seconds();

   protected abstract int get_default_auto_message_remain_seconds();

   protected abstract String get_default_auto_message_db_name();

   protected abstract boolean get_default_is_auto_sequence_message();

   public static boolean is_shift_battle(L1PcInstance pc) {
     if (pc.is_shift_battle()) {
       pc.sendPackets("比賽正在進行時無法使用。");
       return true;
     }
     return false;
   }

   protected abstract void do_ended_ready();

   protected abstract void on_play_tick();

   public abstract int next_update_tick();

   public abstract void on_update_tick();

   public abstract void on_enter(L1PcInstance paramL1PcInstance);

   public abstract void on_closed();
 }


