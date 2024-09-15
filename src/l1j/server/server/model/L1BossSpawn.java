package l1j.server.server.server.model;

import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import MJFX.UIAdapter.MJUIAdapter;
import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.templates.L1Npc;

public class L1BossSpawn extends L1Spawn {

	private static Logger _log = Logger.getLogger(L1BossSpawn.class.getName());

	private class SpawnTask implements Runnable {
		private int _spawnNumber;
		private int _objectId;

		private SpawnTask(int spawnNumber, int objectId) {
			_spawnNumber = spawnNumber;
			_objectId = objectId;
		}

		@Override
		public void run() {
			try {
				doSpawn(_spawnNumber, _objectId);
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected L1NpcInstance doSpawn(int spawnNumber, int objectId) {
		L1NpcInstance npc = super.doSpawn(spawnNumber, objectId);
		if (npc != null)
			MJUIAdapter.on_boss_append(npc.getNpcId(), npc.getName(), npc.getX(), npc.getY(), npc.getMapId());
		return npc;
	}

	public L1BossSpawn(L1Npc mobTemplate) throws SecurityException, ClassNotFoundException {
		super(mobTemplate);
	}

	/**
	 * 啟動 SpawnTask。
	 *
	 * @param spawnNumber
	 *            由 L1Spawn 管理的編號。如果沒有主點，則可指定任意值。
	 */
	@Override
	public void executeSpawnTask(int spawnNumber, int objectId) {
		// 減少 count 並檢查是否全部死亡
		if (subAndGetCount() != 0) {
			return; // 並未全部死亡
		}
		// TODO 需要修改和測試
		Calendar spawnTime = calcNextSpawnTime(RealTimeClock.getInstance().getRealTimeCalendar());
		// 根據上次的出現時間，計算下一次的出現時間
		/*
		 * Calendar spawnTime; Calendar now = Calendar.getInstance(); // 現在時間 Calendar latestStart = _cycle.getLatestStartTime(now); // 現在時間對應的最近週期的開始時間
		 *
		 * Calendar activeStart = _cycle.getSpawnStartTime(_activeSpawnTime); // 活動週期的開始時間 // 如果活動週期的開始時間 >= 最近週期的開始時間，則計算下一次出現時間 if (!activeStart.before(latestStart)) { spawnTime = calcNextSpawnTime(activeStart); } else { // 如果活動週期的開始時間 < 最近週期的開始時間，則在最近週期出現 // 雖然難以理解，但為了概率計算，強制通過 calcNextSpawnTime 計算 latestStart.add(Calendar.SECOND, -1); spawnTime = calcNextSpawnTime(_cycle.getLatestStartTime(latestStart)); }
		 */
		spawnBoss(spawnTime, objectId);
	}

	private int _spawnCount;

	private synchronized int subAndGetCount() {
		return --_spawnCount;
	}

	private String _cycleType;

	public void setCycleType(String type) {
		_cycleType = type;
	}

	private int _percentage;

	public void setPercentage(int percentage) {
		_percentage = percentage;
	}

	private L1BossCycle _cycle;

	private Calendar _activeSpawnTime;

	private static Random _rnd = new Random();

	@Override
	public void init() {
		if (_percentage <= 0) {
			return;
		}
		_cycle = L1BossCycle.getBossCycle(_cycleType);
		if (_cycle == null) {
			throw new RuntimeException(_cycleType + " not found");
		}
		Calendar now = Calendar.getInstance();
		// 출현 시간
		Calendar spawnTime;
		if (Config.ServerAdSetting.INITBOSSSPAWN && _percentage > _rnd.nextInt(100)) {
			spawnTime = _cycle.calcSpawnTime(now);

		} else {
			spawnTime = calcNextSpawnTime(now);
		}
		spawnBoss(spawnTime, 0);
	}

	// 확률 계산해 다음의 출현 시간을 산출
	private Calendar calcNextSpawnTime(Calendar cal) {
		do {
			cal = _cycle.nextSpawnTime(cal);
		} while (!(_percentage > _rnd.nextInt(100)));
		return cal;
	}

	// 在指定的時間安排Boss出現
	private void spawnBoss(Calendar spawnTime, int objectId) {
		// 保存這次出現的時間。同樣的註釋或語句也可以在其他地方使用。
		_activeSpawnTime = spawnTime;
		long delay = spawnTime.getTimeInMillis() - System.currentTimeMillis();

		int cnt = _spawnCount;
		_spawnCount = getAmount();
		while (cnt < getAmount()) {
			cnt++;
			GeneralThreadPool.getInstance().schedule(new SpawnTask(0, objectId), delay);
		}
		_log.log(Level.FINE, toString());
	}

	/**
	 * 表示當前活動的Boss的週期和出現時間。
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[MOB]npcid:" + getNpcId());
		builder.append(" name:" + getName());
		builder.append("[Type]" + _cycle.getName());
		builder.append("[當前的週期]");
		builder.append(_cycle.getSpawnStartTime(_activeSpawnTime).getTime());
		builder.append(" - ");
		builder.append(_cycle.getSpawnEndTime(_activeSpawnTime).getTime());
		builder.append("[出現時間]");
		builder.append(_activeSpawnTime.getTime());
		return builder.toString();
	}
}
