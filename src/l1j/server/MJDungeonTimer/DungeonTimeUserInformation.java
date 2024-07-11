package l1j.server.MJDungeonTimer;

import java.util.HashMap;

import l1j.server.MJDungeonTimer.Loader.DungeonTimeProgressLoader;
import l1j.server.MJDungeonTimer.Progress.AccountTimeProgress;
import l1j.server.MJDungeonTimer.Progress.CharacterTimeProgress;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;

public class DungeonTimeUserInformation {
	public static DungeonTimeUserInformation newInstance() {
		return new DungeonTimeUserInformation();
	}

	private HashMap<Integer, DungeonTimeProgress<?>> _character_progresses;
	private HashMap<Integer, DungeonTimeProgress<?>> _account_progresses;

	private DungeonTimeUserInformation() {
		_character_progresses = new HashMap<Integer, DungeonTimeProgress<?>>(8);
		_account_progresses = new HashMap<Integer, DungeonTimeProgress<?>>(8);
	}

	public void put_dungeon_progress(int timer_id, CharacterTimeProgress progress) {
		_character_progresses.put(timer_id, progress);
	}

	public void put_dungeon_progress(int timer_id, AccountTimeProgress progress) {
		_account_progresses.put(timer_id, progress);
	}

	public DungeonTimeProgress<?> remove_dungeon_progress(int timer_id) {
		DungeonTimeProgress<?> p = _character_progresses.remove(timer_id);
		if (p == null)
			p = _account_progresses.remove(timer_id);
		return p;
	}

	public HashMap<Integer, DungeonTimeProgress<?>> get_character_progresses() {
		return _character_progresses;
	}

	public HashMap<Integer, DungeonTimeProgress<?>> get_account_progresses() {
		return _account_progresses;
	}

	public DungeonTimeProgress<?> get_progress(DungeonTimeInformation dtInfo) {
		if (dtInfo.get_is_account_share()) {
			return _account_progresses.get(dtInfo.get_timer_id());
		}
		return _character_progresses.get(dtInfo.get_timer_id());
	}

	public DungeonTimeProgress<?> get_progress(int timer_id) {
		DungeonTimeProgress<?> progress = _account_progresses.get(timer_id);
		if (progress == null)
			progress = _character_progresses.get(timer_id);
		return progress;
	}

	public void dec_dungeon_progress(L1PcInstance pc, DungeonTimeInformation dtInfo) {
		int timer_id = dtInfo.get_timer_id();
		HashMap<Integer, DungeonTimeProgress<?>> progresses = dtInfo.get_is_account_share() ? _account_progresses
				: _character_progresses;
		DungeonTimeProgress<?> progress = progresses.get(timer_id);
		if (progress == null) {
			progress = dtInfo.to_progress(pc);
			progresses.put(timer_id, progress);
			DungeonTimeProgressLoader.update(progress);
			sendDungeonTime(pc, progress, true);
		}

		if (progress.dec_remain_seconds() <= 0) {
			// TODO地下城時間結束，但由於重新進入嘗試，-1++被強制為0（充電藥水需要0~-1）
			progress.set_remain_seconds(0);
			// System.out.println(progress.dec_remain_seconds());
			pc.do_simple_teleport(MJCopyMapObservable.RESET_X, MJCopyMapObservable.RESET_Y,
					MJCopyMapObservable.RESET_MAPID);
			pc.sendPackets(String.format("\\f2使用帳號的限時地下城 (%s) 使用時間已超過。(每天早上6點重置)", dtInfo.get_description()));
		}
	}

	public void insert_dungeon_progress(L1PcInstance pc, DungeonTimeInformation dtInfo) {
		int timer_id = dtInfo.get_timer_id();
		HashMap<Integer, DungeonTimeProgress<?>> progresses = dtInfo.get_is_account_share() ? _account_progresses
				: _character_progresses;
		DungeonTimeProgress<?> progress = progresses.get(timer_id);

		progress = dtInfo.to_progress(pc);
		progresses.put(timer_id, progress);
		DungeonTimeProgressLoader.update(progress);
		sendDungeonTime(pc, progress, true);
		if (progress.dec_remain_seconds() <= 0) {
			// TODO地下城時間結束，但由於重新進入嘗試，-1++被強制為0（充電藥水需要0~-1）
			progress.set_remain_seconds(0);
		} else {
			progress.set_remain_seconds(progress.dec_remain_seconds());
		}

		DungeonTimeProgressLoader.load(pc);

	}

	public void send_dungeon_progress(L1PcInstance pc, DungeonTimeInformation dtInfo, boolean send) {
		int timer_id = dtInfo.get_timer_id();
		HashMap<Integer, DungeonTimeProgress<?>> progresses = dtInfo.get_is_account_share() ? _account_progresses
				: _character_progresses;
		DungeonTimeProgress<?> progress = progresses.get(timer_id);
		if (progress == null) {
			progress = dtInfo.to_progress(pc);
			progresses.put(timer_id, progress);
			DungeonTimeProgressLoader.update(progress);
		}
		// SC_EVENT_COUNTDOWN_NOTI_PACKET.send(pc, progress.get_remain_seconds(),
		// dtInfo.get_description());
		sendDungeonTime(pc, progress, send);
	}

	private void sendDungeonTime(L1PcInstance pc, DungeonTimeProgress<?> progress, boolean send) {
		if (send)
			pc.sendPackets(new S_PacketBox(S_PacketBox.MAP_TIMER, progress.get_remain_seconds()));

		int remain_seconds = progress.get_remain_seconds();
		int remain_hours = (remain_seconds / RealTimeClock.HOUR_SECONDS);
		int remain_minute = (remain_seconds / RealTimeClock.MINUTE_SECONDS);
		remain_seconds -= (remain_hours * RealTimeClock.HOUR_SECONDS);
		remain_seconds -= (remain_minute * RealTimeClock.MINUTE_SECONDS);
		int hour = 0;
		int minute = 0;

		if (remain_minute > 60) {
			hour = remain_minute / 60;
			minute = remain_minute - hour * 60;
		} else {
			minute = remain_minute;
		}

		if (hour > 0) {
			pc.sendPackets(new S_ServerMessage(1525, String.valueOf(hour), String.valueOf(minute)));
		} else if (minute > 0) {
			pc.sendPackets(new S_ServerMessage(1527, String.valueOf(minute)));
		}

		/*
		 * if (remain_hours > 0) {
		 * pc.sendPackets(new S_ServerMessage(1525, String.valueOf(remain_hours),
		 * String.valueOf(remain_minute)));
		 * } else if (remain_minute > 0) {
		 * pc.sendPackets(new S_ServerMessage(1527, String.valueOf(remain_minute)));
		 * }
		 */ else {
			if (remain_seconds < 0)
				remain_seconds = 0;
			pc.sendPackets(new S_ServerMessage(1528, String.valueOf(remain_seconds)));
		}
		if (pc.getMapId() == 54) {
			if (GameTimeClock.getInstance().is_night())
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "在夜間無法進行隨機傳送的區域。"));
		}
	}

	public void initialize() {
		_character_progresses.clear();
		_account_progresses.clear();
	}
}
