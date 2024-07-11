package l1j.server.MJRaidSystem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import l1j.server.MJRaidSystem.BossSkill.MJRaidBossCombo;
import l1j.server.MJRaidSystem.BossSkill.MJRaidBossSkill;
import l1j.server.MJRaidSystem.Compensator.CompensatorElement;
import l1j.server.MJRaidSystem.Compensator.ExpCompensator;
import l1j.server.MJRaidSystem.Compensator.GiveItemCompensator;
import l1j.server.MJRaidSystem.Compensator.ItemCompensator;
import l1j.server.MJRaidSystem.Loader.MJRaidBossSkillLoader;
import l1j.server.MJRaidSystem.Loader.MJRaidCompensatorLoader;
import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
import l1j.server.MJRaidSystem.Loader.MJRaidSpawnLoader;
import l1j.server.MJRaidSystem.Loader.MJRaidUserTimeStore;
import l1j.server.MJRaidSystem.Spawn.MJRaidNpcSpawn;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1RaidDoorInstance;
import l1j.server.server.model.Instance.L1RaidPortalInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_OnlyEffect;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public abstract class MJRaidObject implements Runnable {
	protected static final ServerBasePacket s_quakeDisplay = new S_DisplayEffect(S_DisplayEffect.QUAKE_DISPLAY);

	// RS is Raid State.
	protected static final int RS_CLOSE = 1; // 關閉狀態
	protected static final int RS_OPEN = 2; // 開啟狀態
	protected static final int RS_READY = 4; // 準備狀態
	protected static final int RS_START = 8; // 進行狀態

	protected static Random _rnd = new Random(System.nanoTime());

	protected int _state; // 當前對象的狀態
	protected Object _lock; // 用於同步每個對象實例
	protected int _baseMapId; // 基礎地圖 ID
	protected L1Map _copyMap; // 副本地圖
	protected ArrayList<L1PcInstance> _users; // 當前進入的玩家列表
	protected L1PcInstance _owner; // 當前實例的開啟者
	protected L1RaidPortalInstance _portal; // 傳送門實例
	protected L1RaidDoorInstance _door; // 門實例
	protected L1NpcInstance _boss; // Boss 怪物
	protected MJRaidType _type; // 團隊副本類型
	protected ArrayList<CompensatorElement> _compensators; // 獎勵列表
	protected ArrayList<MJRaidBossSkill> _skills; // 技能列表
	protected ArrayList<MJRaidBossCombo> _combos; // 連招列表
	protected CloseTask _task; // 關閉任務
	protected ArrayList<L1NpcInstance> _npcList;
	protected MJRaidNpcSpawn _bossSpawn;
	private Collection<L1PcInstance> _pcsView;

	public MJRaidObject(L1PcInstance owner, MJRaidType type) {
		_state = RS_CLOSE;
		_lock = new Object();
		_baseMapId = type.getInputMapId();
		_copyMap = null;
		_users = null;
		_owner = owner;
		_portal = null;
		_door = null;
		_type = type;
		_task = null;
		_pcsView = null;
		ArrayList<CompensatorElement> clist = MJRaidCompensatorLoader.getInstance().get(_type.getId());
		if (clist != null)
			setCompensators(clist);

		ArrayList<MJRaidBossSkill> sklist = MJRaidBossSkillLoader.getInstance().get(_type.getId());
		if (sklist != null)
			setSkills(sklist);

		ArrayList<MJRaidBossCombo> cblist = MJRaidBossSkillLoader.getInstance().getCombo(_type.getId());
		if (cblist != null)
			setCombos(cblist);
	}

	/**
	 ********************************************** 定義字段訪問器 **********************************************
	 **/
	/** 團隊副本類型實例 **/
	public MJRaidType getRaidType() {
		return _type;
	}

	/** 基礎地圖信息 **/

	public int getBaseMapId() {
		return _baseMapId;
	}

	/** 副本地圖信息 **/
	public void setCopyMap(L1Map map) {
		_copyMap = map;
	}

	public L1Map getCopyMap() {
		return _copyMap;
	}

	public int getCopyMapId() {
		if (_copyMap == null)
			return -1;
		return _copyMap.getId();
	}

/**
 ********************************************** 定義基本公共方法 **********************************************
 **/
	/** 通知大小超出 **/
	public void notifySizeOver() {
		if (_owner != null) {
			MJRaidMessage.RAID_OPEN_FAIL_SIZEOVER.sendMessage(_owner);
		}
	}

	/** 突襲對象處置... **/
	public void dispose() {
		try {
			_state = RS_CLOSE;
			_baseMapId = -1;
			_copyMap = null;
			_owner = null;
			if (_users != null) {
				_users.clear();
				_users = null;
			}

			if (_portal != null) {
				_portal.deleteMe();
				_portal = null;
			}

			if (_door != null) {
				_door.deleteMe();
				_door = null;
			}

			_type = null;
			_compensators = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 將玩家移動到團隊副本的等待區域。 **/
	public void addUser(L1PcInstance pc) {
		if (pc.hasSkillEffect(L1SkillId.DRAGONRAID_BUFF)) {
			pc.sendPackets(new S_ServerMessage(1626)); // 消息代碼可能表示特定狀態，如 "你不能進入團隊副本。"
			return;
		}

		// 如果團隊副本已經關閉或者已經開始，
		if ((_state & RS_CLOSE) > 0 || (_state & RS_START) > 0) {
			MJRaidMessage.RAID_INPUT_FAIL_BEGIN.sendMessage(pc); // 向玩家發送消息表明無法進入團隊副本
			return;
		}
	}

		synchronized (_lock) {
			if (!_users.contains(pc)) {
				if (_users.size() > _type.getMaxUser()) {
					MJRaidMessage.RAID_INPUT_FAIL_OVERUSER.sendMessage(pc);
					return;
				}
				_users.add(pc);
			}
		}
		_type.inputRaid(pc, getCopyMapId());
	}

	public void delUser(L1PcInstance pc) {
		if (_users == null)
			return;

		synchronized (_lock) {
			if (_users.contains(pc))
				_users.remove(pc);
		}
	}

	/** 將玩家移動到團隊副本的實戰區域。 **/
	public void doorMove(L1PcInstance pc) {

		// 如果團隊副本已經關閉或者已經開始，
		if ((_state & RS_CLOSE) > 0 || (_state & RS_START) > 0) {
			MJRaidMessage.RAID_INPUT_FAIL_BEGIN.sendMessage(pc);
			return;
		}

		int size = _users.size();
		for (int i = 0; i < size; i++) {
			L1PcInstance p = _users.get(i);
			if (p.getId() == pc.getId()) {
				_type.move(pc, getCopyMapId());
				if ((_state & RS_OPEN) > 0) {
					_state = RS_READY;
					GeneralThreadPool.getInstance().execute(this);
				}
				break;
			}
		}
	}

	/** runnable **/
	/** 此實例管理整個實例空間，因此在 run 方法結束時被調用 **/
	@override
	public void run() {
		if ((_state & RS_READY) > 0) { // 如果在準備狀態下被運行，則開始團隊副本
			readyRaid();
		} else if ((_state & RS_START) > 0) { // 如果團隊副本已經開始，則結束它
			runRaid();
		}
	}

	/**
	 ********************************************** 團隊副本方法及抽象方法 **********************************************
	 **/
	/** 初始化團隊副本對象 **/
	public void init() {
		_users = new ArrayList<L1PcInstance>(_type.getMaxUser()); // 初始化玩家列表
		_state = RS_OPEN; // 設置狀態為開啟
		ArrayList<MJRaidNpcSpawn> slist = MJRaidSpawnLoader.getInstance().get(_type.getId()); // 獲取 NPC 生成列表
		if (slist != null) {
			setSpawn(slist); // 如果生成列表不為空，則設置生成點
		}

		_task = new CloseTask(this); // 初始化關閉任務
	}

	protected void setCombos(ArrayList<MJRaidBossCombo> list) {
		_combos = list;
	}

	protected void setSkills(ArrayList<MJRaidBossSkill> list) {
		_skills = list;
	}

	protected void setCompensators(ArrayList<CompensatorElement> list) {
		_compensators = list;
	}

	/** 執行生成 **/
	protected void setSpawn(ArrayList<MJRaidNpcSpawn> spawnList) {
		// spSize 是生成大小。
		// stype 是生成類型。
		int spSize = spawnList.size();
		int stype = 0;
		MJRaidNpcSpawn spawn = null;
		_npcList = new ArrayList<L1NpcInstance>(spSize);
		for (int i = 0; i < spSize; i++) {
			spawn = spawnList.get(i);
			stype = spawn.getSpawnType();

			if ((stype & MJRaidNpcSpawn.ST_NORMAL) > 0) {
				setNormalSpawn(spawn);
			} else if ((stype & MJRaidNpcSpawn.ST_RECT) > 0) {
				setRectSpawn(spawn);
			} else if ((stype & MJRaidNpcSpawn.ST_CIRCLE) > 0) {
				setCircleSpawn(spawn);
			} else if ((stype & MJRaidNpcSpawn.ST_BOSS) > 0) {
				setBossSpawn(spawn);
			} else if ((stype & MJRaidNpcSpawn.ST_PORTAL) > 0) {
				setPortalSpawn(spawn);
			} else if ((stype & MJRaidNpcSpawn.ST_DOOR) > 0) {
				setDoorSpawn(spawn);
			}
		}
	}

	// 指定生成
	protected abstract void setNormalSpawn(MJRaidNpcSpawn spawn); // 正常生成，主要用於 NPC。

	protected abstract void setRectSpawn(MJRaidNpcSpawn spawn); // 矩形生成，在指定區域內隨機生成指定數量的 NPC。

	protected abstract void setCircleSpawn(MJRaidNpcSpawn spawn); // 圓形生成，沿圓形生成對象，因巴拉卡斯團隊副本而設置。

	protected abstract void setBossSpawn(MJRaidNpcSpawn spawn); // Boss 生成，龍團隊副本不會立即生成，但需要傳遞生成對象，
	// 也可在非龍團隊副本中使用。

	protected void setPortalSpawn(MJRaidNpcSpawn spawn) { // 入口生成經常被用到，所以不是抽象的。
		_portal = (L1RaidPortalInstance) spawn.spawn(_owner.getMapId(), _owner.getX(), _owner.getY());
		_portal.setRaidObject(this);
	}

	// 入口生成的方法
	protected abstract void setDoorSpawn(MJRaidNpcSpawn spawn); // 門生成，具體實現由子類提供。

	// 設定門生成
	protected void setDoorSpawn(MJRaidNpcSpawn spawn) { // 門和傳送門是一樣的情況。
		_door = (L1RaidDoorInstance) spawn.spawn(getCopyMapId());
		_door.setRaidObject(this);
		_npcList.add(_door);
	}

	// 準備進行突襲
	protected abstract void readyRaid(); // 將突襲設置為準備狀態。

	// 進行突襲
	protected abstract void runRaid(); // 將突襲設置為進行狀態。

	// 結束突襲
	protected void closeRaid() {	// 結束突襲。由於可能會被公共使用，因此不為abstract。
		_state = RS_CLOSE;
		if (_task != null) {
			_task.cancel();
			_task = null;
		}
	}


		if (_users != null) {
			int size = _users.size();
			int mid = getCopyMapId();
			L1PcInstance pc = null;
			for (int i = 0; i < size; i++) {
				pc = _users.get(i);
				if (pc != null && pc.getMapId() == mid)
					_type.outRaid(pc);
			}
		}

		if (_npcList != null) {
			_npcList.clear();
			_npcList = null;
		}
		_bossSpawn = null;
		MJRaidSpace.getInstance().releaseInstance(this);
	}

	private void compensatePc(CompensatorElement element) {
		int usize = _users.size();
		int mid = getCopyMapId();
		L1PcInstance pc;
		for (int i = 0; i < usize; i++) {
			pc = _users.get(i);
			if (pc != null && !pc.isDead() && pc.getMapId() == mid)
				element.compensate(pc);
		}
	}

	protected void successRaid() {
		if (_compensators != null && _boss != null) {
			int size = _compensators.size();
			int mid = getCopyMapId();
			int x = _boss.getX();
			int y = _boss.getY();
			CompensatorElement element = null;
			for (int i = 0; i < size; i++) {
				element = _compensators.get(i);
				if (element instanceof ExpCompensator || element instanceof GiveItemCompensator)
					compensatePc(element);
				else if (element instanceof ItemCompensator)
					element.compensate(new int[] { mid, x, y });
			}
			_compensators = null;

			size = _users.size();
			S_OnlyEffect s_oe = null;
			L1PcInstance pc = null;
			for (int i = 0; i < size; i++) {
				pc = _users.get(i);
				if (pc == null || pc.getMapId() != mid)
					continue;

				s_oe = new S_OnlyEffect(pc.getId(), 7783);
				pc.sendPackets(s_oe, false);
				Broadcaster.broadcastPacket(pc, s_oe);
				pc.setSkillEffect(L1SkillId.DRAGONRAID_BUFF, (int) MJRaidLoadManager.MRS_RAID_DELAY);
				Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + MJRaidLoadManager.MRS_RAID_DELAY);
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONRAID_BUFF, (int) (MJRaidLoadManager.MRS_RAID_DELAY / 1000)), true);
				pc.getNetConnection().getAccount().setDragonRaid(deleteTime);
			}

			ArrayList<L1PcInstance> pcs = new ArrayList<L1PcInstance>();
			pcs.addAll(L1World.getInstance().getAllPlayers());
			MJRaidMessage.RAID_CLEAR_SUCCESS_MESSAGE.sendMessage(pcs);
			pcs.clear();

			GeneralThreadPool.getInstance().execute(new MJRaidUserTimeStore(_users));
		}
	}

	public void failRaid() {
		closeRaid();
	}

	/**
	 ********************************************** 記錄方法. **********************************************
	 **/
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		sb.append("class name is MJRaidObject.").append("\n");
		sb.append("baseMapId : ").append(getBaseMapId()).append("\n");
		sb.append("copyMapId : ").append(getCopyMapId()).append("\n");
		sb.append("state : ").append(_state).append("\n");
		sb.append("open user : ").append(_owner.getName()).append("\n");
		sb.append("user size : ").append(_users.size()).append("\n");
		sb.append("raid type : ").append(_type.toString()).append("\n");
		return sb.toString();
	}

	protected boolean isInRaid() {
		if (_users == null)
			return false;

		int size = _users.size();
		int cpMap = getCopyMapId();
		L1PcInstance pc = null;
		for (int i = 0; i < size; i++) {
			pc = _users.get(i);
			if (pc == null)
				continue;

			// 如果地圖上只有一個人,
			if (pc.getMapId() == cpMap)
				return true;
		}

		return false;
	}

	protected void resetRaid() {
		Iterator<L1Object> it = L1World.getInstance().getVisibleObjects(getCopyMapId()).values().iterator();
		L1NpcInstance obj = null;
		L1NpcInstance npc = null;
		int npcSize = 0;
		boolean isNpc;
		if (_npcList != null)
			npcSize = _npcList.size();

		while (it.hasNext()) {
			try {
				L1Object l1obj = it.next();
				if (l1obj == null || (l1obj instanceof L1DollInstance) || (l1obj instanceof L1SummonInstance))
					continue;

				if (_portal != null && l1obj.getId() == _portal.getId())
					continue;

				if ((l1obj instanceof L1NpcInstance)) {
					obj = (L1NpcInstance) l1obj;

					isNpc = false;
					for (int i = 0; i < npcSize; i++) {
						npc = _npcList.get(i);
						if (npc.getId() == obj.getId()) {
							isNpc = true;
							break;
						}
					}

					if (!isNpc) {
						obj.deleteMe();
						obj = null;
					}
				} else {
					obj.deleteMe();
					obj = null;
				}
			} catch (Exception e) {
			}
		}

		if (_boss != null) {
			_boss.deleteMe();
			_boss = null;
		}

		if (_users != null)
			_users.clear();
		_state = RS_OPEN;
	}

	/** 物件在指定時間強制終止 **/
	public class CloseTask extends TimerTask {
		private MJRaidObject _obj;
		private Timer _timer;

		public CloseTask(MJRaidObject obj) {
			_obj = obj;
			_timer = new Timer();
			_timer.schedule(this, obj.getRaidType().getRaidTime());
		}

		@Override
		public void run() {
			if ((_obj._state & RS_CLOSE) == 0)
				_obj.failRaid();
		}
	}

	private static final long _divTime = 10 * 1000;

	protected void waitClose() {
		if (_users == null || _users.size() <= 0)
			return;

		long allTime = MJRaidLoadManager.MRS_BOSSSPAWN_DELAY;
		int size = 0;
		int mid = getCopyMapId();
		ServerBasePacket pck = null;
		L1PcInstance pc = null;
		while (allTime > 10) {
			try {
				pck = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format("%d秒後將移動到村莊。", allTime / 1000));
				if (_users == null)
					return;

				size = _users.size();
				for (int i = 0; i < size; i++) {
					pc = _users.get(i);

					if (pc == null || pc.getMapId() != mid)
						continue;

					pc.sendPackets(s_quakeDisplay, false);
					pc.sendPackets(pck, false);
				}
				pck.clear();
				allTime -= _divTime;
				Thread.sleep(_divTime);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public Collection<L1PcInstance> getUsersView() {
		if (_pcsView == null)
			_pcsView = Collections.unmodifiableCollection(_users);
		return _pcsView;
	}

	public void notifyRaidPickupItem(L1PcInstance pc, L1ItemInstance item, int count) {
		try {
			S_SystemMessage msg = new S_SystemMessage(String.format("[%s] 獲得了 %d 個 %s。", pc.getName(), item.getLogName(), count));
			for (L1PcInstance p : getUsersView()) {
				if (p != null)
					p.sendPackets(msg, false);
			}
			msg.clear();
		} catch (NullPointerException excp) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
