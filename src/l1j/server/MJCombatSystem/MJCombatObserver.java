package l1j.server.MJCombatSystem;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
import l1j.server.MJCombatSystem.Loader.MJCombatTrapsLoader;
import l1j.server.MJTemplate.MJEPcStatus;
import l1j.server.MJTemplate.MJEStatus;
import l1j.server.MJTemplate.MJParty;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Interface.MJSimpleListener;
import l1j.server.MJTemplate.Kda.TeamKda;
import l1j.server.MJTemplate.Kda.UserKda;
import l1j.server.MJTemplate.Lineage2D.MJRectangle;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BOX_ATTR_CHANGE_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BOX_ATTR_CHANGE_NOTI_PACKET.Box;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.ObServer.MJAbstractCopyMapObserver;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.MJTemplate.PacketHelper.MJPacketFactory;
import l1j.server.MJTemplate.Reward.AbstractReward;
import l1j.server.MJTemplate.Trap.AbstractTrap;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.ServerBasePacket;

public abstract class MJCombatObserver extends MJAbstractCopyMapObserver implements Runnable {
	public final static int REWARDS_INDEX_WINNER_KEEP = 0;
	public final static int REWARDS_INDEX_WINNER_DEAD = 1;
	public final static int REWARDS_INDEX_LOSER_KEEP = 2;
	public final static int REWARDS_INDEX_LOSER_DEAD = 3;

	public static MJCombatObserver execute(MJCombatObserver observer) {
		return observer
				.initialize()
				.execute_combat();
	}

	// supplies operating informations.
	protected Lock _lock;
	protected long _start_sec;
	protected LinkedList<MJSimpleListener> _quit_listener;
	protected MJCombatInformation _combat_info;

	// do rewards.
	protected AbstractReward[][] _rewards;

	// team informations.
	protected int _team_size;
	protected LinkedList<Integer> _team_q;
	protected L1Party[] _parties;
	protected MJEStatus _status;
	protected AtomicInteger _team_id;
	protected ProtoOutputStream[] _box_attr;
	protected ProtoOutputStream[][][] _team_box_attr;

	// score meter.
	protected int _remain_knock;
	protected TeamKda[] _team_kda;
	protected HashMap<Integer, UserKda> _user_kda_dictionary;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append("[ID:").append(_combat_info.get_combat_id())
				.append("(").append(_combat_info.get_game_type().to_kr_desc()).append(")");
		sb.append(" 地圖").append(_copyMapId).append(", 狀態:").append(_status.name());
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		cal.setTimeInMillis(_start_sec * 1000L);
		sb.append(", 開始時間:").append(cal.get(Calendar.HOUR_OF_DAY)).append(":")
				.append(cal.get(Calendar.MINUTE)).append(":").append(cal.get(Calendar.SECOND));
		return sb.toString();
	}

	protected MJCombatObserver() {
		_status = MJEStatus.CLOSED;
		_team_id = new AtomicInteger(0);
		_team_size = 2;
		_team_q = new LinkedList<Integer>();
		_lock = new ReentrantLock();
	}

	public int next_team_id() {
		return _team_q.get(_team_id.getAndIncrement() % _team_q.size());
	}

	public boolean is_closed() {
		return _status.equals(MJEStatus.CLOSED);
	}

	public boolean is_ready() {
		return _status.equals(MJEStatus.READY);
	}

	public boolean is_running() {
		return _status.equals(MJEStatus.RUNNING);
	}

	public boolean is_disposing() {
		return _status.equals(MJEStatus.DISPOSING);
	}

	public MJCombatObserver set_status(MJEStatus status) {
		_status = status;
		return this;
	}

	public MJEStatus get_status() {
		return _status;
	}

	public MJCombatObserver set_combat_info(MJCombatInformation combat_info) {
		_combat_info = combat_info;
		return this;
	}

	public MJCombatInformation get_combat_info() {
		return _combat_info;
	}

	public MJCombatObserver set_rewards(AbstractReward[][] rewards) {
		_rewards = rewards;
		return this;
	}

	public AbstractReward[][] get_rewards() {
		return _rewards;
	}

	public MJCombatObserver add_quit_listener(MJSimpleListener dispose_listener) {
		if (_quit_listener == null)
			_quit_listener = new LinkedList<MJSimpleListener>();
		_quit_listener.add(dispose_listener);
		return this;
	}

	protected MJCombatObserver initialize() {
		return initialize_users()
				.initialize_copymap()
				.initialize_box()
				.initialize_team_box();
	}

	protected MJCombatObserver initialize_users() {
		int size = _combat_info.team_informations_size();
		_team_size = 0;
		_team_kda = new TeamKda[size];
		_parties = new L1Party[size];
		_user_kda_dictionary = new HashMap<Integer, UserKda>(size * _combat_info.get_team_max_player());
		for (int i = 0; i < size; ++i) {
			MJCombatTeamInformation tInfo = _combat_info.get_team_informations(i);
			if (tInfo == null || !tInfo.is_use()) {
				continue;
			}
			++_remain_knock;
			++_team_size;
			_team_kda[i] = TeamKda.newInstance(i);
			_team_q.add(new Integer(i));
		}
		return this;
	}

	protected MJCombatObserver initialize_copymap() {
		MJCopyMapObservable.getInstance().register(_combat_info.get_rt_zone().mapId, this);
		L1WorldMap.getInstance().getMap(_copyMapId)
				.set_isEnabledDeathPenalty(false)
				.set_isEscapable(false)
				.set_isMarkable(false)
				.set_isRecallPets(false)
				.set_isRuler(false)
				.set_isTeleportable(false)
				.set_isUseResurrection(false);
		return this;
	}

	protected void dispose_users() {
		Collection<L1PcInstance> col = L1World.getInstance().getVisiblePlayers(_copyMapId);
		ServerBasePacket pck = MJPacketFactory.createTime(MJCombatLoadManager.COMBAT_QUIT_DELAY_SECOND);
		for (L1PcInstance pc : col) {
			if (pc == null || pc.getNetConnection() == null || !pc.getNetConnection().isConnected())
				continue;

			/*
			 * if(pc.isDead()){
			 * pc.setDead(false);
			 * S_SkillSound sound = new S_SkillSound(pc.getId(), '\346');
			 * pc.sendPackets(sound, false);
			 * pc.broadcastPacket(sound, true);
			 * pc.resurrect(pc.getMaxHp());
			 * pc.setCurrentHp(pc.getMaxHp());
			 * pc.startHpMpRegeneration();
			 * pc.startMpRegenerationByDoll();
			 * S_Resurrection res = new S_Resurrection(pc, pc, 0);
			 * pc.sendPackets(res, false);
			 * pc.broadcastPacket(res, true);
			 * S_CharVisualUpdate vu = new S_CharVisualUpdate(pc);
			 * pc.sendPackets(vu, false);
			 * pc.broadcastPacket(vu, true);
			 * }
			 */

			pc.sendPackets(pck, false);
		}
		pck.clear();
		final int copy_map_id = _copyMapId;
		final TeamKda[] team_kda = _team_kda;
		final L1Party[] parties = _parties;
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				try {
					Collection<L1PcInstance> col = L1World.getInstance().getVisiblePlayers(copy_map_id);
					for (L1PcInstance pc : col) {
						if (pc == null || pc.getNetConnection() == null)
							continue;

						leave(pc);
						int team_id = pc.get_current_combat_team_id();
						if (team_id < 0)
							continue;

						L1Party party = pc.getParty();
						if (party != null)
							party.leaveMember(pc);
					}
					for (int i = team_kda.length - 1; i >= 0; --i)
						team_kda[i] = null;
					for (int i = parties.length - 1; i >= 0; --i)
						parties[i] = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, MJCombatLoadManager.COMBAT_QUIT_DELAY_SECOND * 1000L);
		_team_kda = null;
		_parties = null;
		if (_team_q != null) {
			_team_q.clear();
			_team_q = null;
		}
		if (_user_kda_dictionary != null) {
			_user_kda_dictionary.clear();
			_user_kda_dictionary = null;
		}
	}

	protected MJCombatObserver initialize_box() {
		dispose_box();
		Box[] boxes = _combat_info.create_line_box();
		int boxes_size = boxes.length;
		_box_attr = new ProtoOutputStream[boxes_size];
		for (int i = boxes_size - 1; i >= 0; --i)
			_box_attr[i] = SC_BOX_ATTR_CHANGE_NOTI_PACKET.create_box(_copyMapId, boxes[i], 1);
		return this;
	}

	protected void dispose_box() {
		if (_box_attr != null) {
			for (ProtoOutputStream stream : _box_attr)
				stream.dispose();
			_box_attr = null;
		}
	}

	protected MJCombatObserver initialize_team_box() {
		dispose_team_box();
		int size = _combat_info.team_informations_size();
		_team_box_attr = new ProtoOutputStream[size][2][4];
		for (int i = size - 1; i >= 0; --i) {
			MJCombatTeamInformation tInfo = _combat_info.get_team_informations(i);
			if (tInfo == null || !tInfo.is_use())
				continue;

			Box[] boxes = tInfo.create_line_boxes();
			int boxes_size = boxes.length;
			for (int b = boxes_size - 1; b >= 0; --b) {
				_team_box_attr[i][0][b] = SC_BOX_ATTR_CHANGE_NOTI_PACKET.create_box(_copyMapId, boxes[b], 1);
				_team_box_attr[i][1][b] = SC_BOX_ATTR_CHANGE_NOTI_PACKET.create_box(_copyMapId, boxes[b], 0);
			}
		}
		return this;
	}

	protected void dispose_team_box() {
		if (_team_box_attr != null) {
			int size = _team_box_attr[0].length;
			for (int i = size - 1; i >= 0; --i) {
				ProtoOutputStream[][] box_attr_1d = _team_box_attr[i];
				for (int j = 1; j >= 0; --j) {
					if (box_attr_1d[j] != null) {
						for (int b = 3; b >= 0; --b) {
							box_attr_1d[j][b].dispose();
							box_attr_1d[j][b] = null;
						}
						box_attr_1d[j] = null;
					}
				}
			}
		}
	}

	public MJCombatObserver execute_combat() {
		if (is_closed()) {
			_status = MJEStatus.READY;
			long ready_seconds = _combat_info.get_ready_seconds();

			_start_sec = (System.currentTimeMillis() / 1000L) + ready_seconds;
			GeneralThreadPool.getInstance().schedule(this, ready_seconds * 1000L);
		}
		return this;
	}

	public void enter(L1PcInstance pc) {
		if (pc == null)
			return;

		if (!is_ready()) {
			pc.sendPackets("進入時間已超過。");
			return;
		}

		if (!pc.is_world()) {
			pc.sendPackets("目前無法進入。");
			return;
		}

		int next_id = next_team_id();
		MJCombatTeamInformation tInfo = _combat_info.get_team_informations(next_id);
		if (tInfo == null) {
			pc.sendPackets("目前無法進入的狀態。");
			return;
		}

		pc.delInvis();
		pc.set_is_non_action(true);
		pc.set_instance_status(MJEPcStatus.COMBAT_FIELD);
		pc.set_current_combat_id(_combat_info.get_combat_id());
		pc.set_current_combat_team_id(next_id);
		// tInfo.get_rt_position().do_teleport(pc, _copyMapId);
		MJRectangle rt = tInfo.get_rt_position();
		pc.start_teleport(rt.x, rt.y, _copyMapId, pc.getHeading(), 18339, true, false);

		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				pc.sendPackets(MJPacketFactory.createTime(_start_sec - (System.currentTimeMillis() / 1000L)));
				if (_box_attr != null)
					pc.sendPackets(_box_attr, false);
				if (_team_box_attr != null)
					pc.sendPackets(_team_box_attr[next_id][0], false);
			}
		}, 800L);
	}

	protected boolean validation_users(){
		Collection<L1PcInstance> col = L1World.getInstance().getVisiblePlayers(_copyMapId);
		if (col.size() < _team_size) {
			String message = String.format("參加人員不足，%s已結束。", _combat_info.get_game_type().to_kr_desc());
			L1World.getInstance().broadcastPacketToAll(message);
			ProtoOutputStream stream = SC_NOTIFICATION_MESSAGE.make_stream(
				String.format("%s%d秒後將移動到村莊。", message, MJCombatLoadManager.COMBAT_QUIT_DELAY_SECOND),
				MJCombatLoadManager.COMBAT_DELAY_MESSAGE_RGB,
				MJCombatLoadManager.COMBAT_DELAY_MESSAGE_DURATION
			);
			// 其他邏輯
		}
		// 其他邏輯
		return true;
	}
			
			for(L1PcInstance pc : col){
				if(pc == null || pc.getNetConnection() == null || !pc.getNetConnection().isConnected())
					continue;
				
				pc.sendPackets(stream, false);
			}
			stream.dispose();
			stream = null;
			return false;
		}
		
		ProtoOutputStream stream = SC_NOTIFICATION_MESSAGE.make_stream("隊伍重新調整後遊戲將開始。", MJCombatLoadManager.COMBAT_DELAY_MESSAGE_RGB, 5);
		
		int index = 0;
		int team_q_size = _team_q.size();
		ServerBasePacket pck = MJPacketFactory.createTime(_combat_info.get_play_seconds());
		for(L1PcInstance pc : col){
			if(pc == null || pc.getNetConnection() == null || !pc.getNetConnection().isConnected() || !pc.is_combat_field())
				continue;
			
			pc.sendPackets(stream, false);
			if(_team_box_attr != null)
				pc.sendPackets(_team_box_attr[pc.get_current_combat_team_id()][1], false);
			
			L1Party party = pc.getParty();
			if(party != null){
				party.leaveMember(pc);
			}
			
			int team_id = _team_q.get(index % team_q_size);
			party = _parties[team_id];
			if(party == null){
				party = new MJParty();
				_parties[team_id] = party;
			}
			_parties[team_id].addMember(pc);
			pc.setParty(_parties[team_id]);
			
			MJCombatTeamInformation tInfo = _combat_info.get_team_informations(team_id);
			_user_kda_dictionary.put(pc.getId(), UserKda.newInstance(team_id, pc.getId(), pc.getName()));
			pc.set_current_combat_id(_combat_info.get_combat_id());
			pc.set_current_combat_team_id(team_id);
			pc.set_instance_status(MJEPcStatus.COMBAT_FIELD);
			pc.set_mark_status(tInfo.get_mark_id());
			pc.set_is_non_action(false);
			MJRectangle rt = tInfo.get_rt_position();
			pc.start_teleport(rt.x, rt.y, _copyMapId, pc.getHeading(), 18339, true, false);
			pc.sendPackets(pck, false);
			++index;
		}
		stream.dispose();
		pck.clear();
		GeneralThreadPool.getInstance().schedule(new Runnable(){
			@Override
			public void run(){
				for(L1PcInstance pc : L1World.getInstance().getVisiblePlayers(_copyMapId)){
					if(pc == null || pc.getNetConnection() == null || !pc.getNetConnection().isConnected() || !pc.is_combat_field())
						continue;
					
					if(_box_attr != null)
						pc.sendPackets(_box_attr, false);
				}
			}
		}, 800L);
		return true;
	}

	public void leave(L1PcInstance pc) {
		if (pc == null || !pc.is_combat_field())
			return;

		L1Party party = pc.getParty();
		if (party != null) {
			party.leaveMember(pc);
			pc.setParty(null);
		}

		pc.set_instance_status(MJEPcStatus.WORLD);
		pc.set_is_non_action(false);
		pc.set_mark_status(0);
		pc.set_current_combat_id(0);
		pc.set_current_combat_team_id(-1);
		if (!pc.isDead())
			pc.start_teleport(MJCopyMapObservable.RESET_X, MJCopyMapObservable.RESET_Y, MJCopyMapObservable.RESET_MAPID,
					pc.getHeading(), 18339, true, false);
		try {
			pc.sendPackets(MJPacketFactory.create(MJPacketFactory.MSPF_IDX_OFFTIME));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void remove(L1PcInstance pc) {
		int team_id = pc.get_current_combat_team_id();
		pc.set_instance_status(MJEPcStatus.WORLD);
		pc.set_is_non_action(false);
		pc.set_mark_status(0);
		pc.set_current_combat_id(0);
		pc.set_current_combat_team_id(-1);
		if (team_id == -1)
			return;

		if (is_ready())
			return;

		_user_kda_dictionary.remove(pc.getId());
		L1Party party = pc.getParty();
		if (party != null)
			party.leaveMember(pc);
		is_live_and_quit(team_id);
	}

	protected void is_live_and_quit(int team_id) {
		try {
			_lock.lock();
			boolean is_live = is_live_team(team_id);
			if (!is_live && !is_disposing()) {
				set_status(MJEStatus.DISPOSING);
				GeneralThreadPool.getInstance().execute(new Runnable() {
					@Override
					public void run() {
						batch_quit();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			_lock.unlock();
		}
	}

	protected boolean is_live_team(int team_id) {
		Collection<L1PcInstance> col = L1World.getInstance().getVisiblePlayers(_copyMapId);
		for (L1PcInstance p : col) {
			if (p == null || p.getNetConnection() == null)
				continue;

			if (p.get_current_combat_team_id() == team_id && !p.isDead())
				return true;
		}
		return false;
	}

	private void do_rewards(AbstractReward[] rewards, L1PcInstance pc) {
		if (rewards != null) {
			for (AbstractReward reward : rewards) {
				reward.do_reward(pc);
			}
		}
	}

	public void on_damage(L1PcInstance attacker, L1PcInstance receiver, int damage) {
		if (attacker == null || !attacker.is_combat_field() || receiver == null || !receiver.is_combat_field())
			return;

		try {
			UserKda attacker_kda = _user_kda_dictionary.get(attacker.getId());
			UserKda receiver_kda = _user_kda_dictionary.get(receiver.getId());
			if (attacker_kda == null || receiver_kda == null)
				return;

			attacker_kda.add_damage(damage);
			receiver_kda.add_tanking(damage);
			try {
				_lock.lock();
				_team_kda[attacker_kda.get_team_id()].add_damage(damage);
			} finally {
				_lock.unlock();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (receiver.getNetConnection() == null || !receiver.getNetConnection().isConnected()) {
			on_kill(attacker, receiver);
			GeneralThreadPool.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					try {
						receiver.logout();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public void on_kill(L1PcInstance attacker, L1PcInstance receiver) {
		if (attacker == null || !attacker.is_combat_field() || receiver == null || !receiver.is_combat_field())
			return;

		int deather_team_id = -1;
		try {
			_lock.lock();
			UserKda attacker_kda = _user_kda_dictionary.get(attacker.getId());
			UserKda receiver_kda = _user_kda_dictionary.get(receiver.getId());
			if (attacker_kda == null || receiver_kda == null)
				return;

			int attacker_team_id = attacker_kda.get_team_id();
			_team_kda[attacker_team_id].inc_kill();
			attacker_kda.inc_kill();
			deather_team_id = receiver_kda.get_team_id();
			receiver_kda.inc_death();
			_team_kda[deather_team_id].inc_death();
		} finally {
			_lock.unlock();
		}
		if (deather_team_id >= 0)
			is_live_and_quit(deather_team_id);
	}

	protected void on_running() {
	}

	@Override
	public void run() {
		if (!validation_users()) {
			notify_quit();
			dispose();
			return;
		}
		try {
			set_status(MJEStatus.RUNNING);
			on_running();
			execute_traps();
			Thread.sleep(_combat_info.get_play_seconds() * 1000L);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!is_running())
			return;

		set_status(MJEStatus.DISPOSING);
		batch_quit();
	}

	protected void execute_traps() {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				int sec = (int) _combat_info.get_play_seconds();
				try {
					do {
						if (!is_running())
							return;

						if (sec % MJCombatLoadManager.COMBAT_TRAPS_SPAWN_SECOND == 0
								&& MJRnd.isWinning(1000000, MJCombatLoadManager.COMBAT_TRAPS_SPAWN_PROB_BY_MILLION)) {
							Collection<L1PcInstance> col = L1World.getInstance().getVisiblePlayers(_copyMapId);
							AbstractTrap trap = MJCombatTrapsLoader.getInstance().to_rand_trap();
							trap.do_traps(col);
						}

						Thread.sleep(1000L);
					} while (--sec > 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void on_closing() {
		final TeamKda winner_kda = select_winner_kda();
		final UserKda[] ranker_kdas = select_ranker_kda();
		final ProtoOutputStream[] packets = MJCombatLoadManager.create_result_notify(winner_kda, ranker_kdas);
		final int winner_id = winner_kda.get_team_id();
		Collection<L1PcInstance> col = L1World.getInstance().getVisiblePlayers(_copyMapId);
		for (L1PcInstance pc : col) {
			if (pc == null || pc.getNetConnection() == null || !pc.getNetConnection().isConnected()
					|| !pc.is_combat_field())
				continue;

			int t_id = pc.get_current_combat_team_id();
			if (t_id == -1)
				continue;

			pc.set_is_non_action(true);
			if (t_id == winner_id) {
				pc.sendPackets(packets[0], false);
				pc.sendPackets(packets[2], false);
				if (_rewards != null)
					do_rewards(_rewards[pc.isDead() ? REWARDS_INDEX_WINNER_DEAD : REWARDS_INDEX_WINNER_KEEP], pc);
			} else {
				pc.sendPackets(packets[1], false);
				pc.sendPackets(packets[2], false);
				if (_rewards != null)
					do_rewards(_rewards[pc.isDead() ? REWARDS_INDEX_LOSER_DEAD : REWARDS_INDEX_LOSER_KEEP], pc);
			}
		}
		for (ProtoOutputStream pck : packets)
			pck.dispose();
	}

	private TeamKda select_winner_kda() {
		LinkedList<TeamKda> kda_q = new LinkedList<TeamKda>();
		int size = _team_kda.length;
		for (int i = size - 1; i >= 0; --i) {
			TeamKda t_kda = _team_kda[i];
			if (t_kda == null)
				continue;

			kda_q.add(t_kda);
		}

		kda_q.sort(TeamKda.create_winner_comparator());
		TeamKda winner = kda_q.get(0);
		kda_q.clear();
		kda_q = null;
		return winner;
	}

	private UserKda[] select_ranker_kda() {
		UserKda[] kda_array = new UserKda[3];
		for (UserKda kda : _user_kda_dictionary.values()) {
			if (kda_array[0] == null || kda_array[0].get_kill() < kda.get_kill())
				kda_array[0] = kda;
			if (kda_array[1] == null || kda_array[1].get_damage() < kda.get_damage())
				kda_array[1] = kda;
			if (kda_array[2] == null || kda_array[2].get_tanking() < kda.get_tanking())
				kda_array[2] = kda;
		}
		return kda_array;
	}

	public void notify_quit() {
		if (_quit_listener != null) {
			for (MJSimpleListener listener : _quit_listener)
				listener.on(this);
			_quit_listener.clear();
			_quit_listener = null;
		}
	}

	@Override
	public void dispose() {
		_status = MJEStatus.CLOSED;
		if (_quit_listener != null) {
			_quit_listener.clear();
			_quit_listener = null;
		}

		dispose_users();
		dispose_box();
		dispose_team_box();
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				MJCombatObserver.super.dispose();
			}
		}, (MJCombatLoadManager.COMBAT_QUIT_DELAY_SECOND * 2) * 1000L);
	}

	protected void batch_quit() {
		if (get_status().equals(MJEStatus.DISPOSING)) {
			try {
				on_closing();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				notify_quit();
				dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int to_total_remain_seconds() {
		return (int) (_combat_info.get_play_seconds() + _combat_info.get_ready_seconds() + 10);
	}
}
