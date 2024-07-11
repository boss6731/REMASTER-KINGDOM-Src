package l1j.server.MJWarSystem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import l1j.server.Config;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJBotSystem.Business.MJAIScheduler;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_INJURY_TIME_NOIT;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_INJURY_TIME_NOIT.SIEGE_KIND;
import l1j.server.MJWarSystem.MJWarFactory.WAR_TYPE;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1WarSpawn;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1CrownInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CastleMaster;
import l1j.server.server.serverpackets.S_IconMessage;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_WarStartMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.MJCommons;

public class MJCastleWar extends MJWar {

	private static final ServerBasePacket[] _messages = new ServerBasePacket[] { S_IconMessage.getMessage(3757, MJSimpleRgb.red(), 6298, 10), // 데포로쥬:
	};
	private static final ServerBasePacket[] _messages4ForCastle = new ServerBasePacket[] {
			S_IconMessage.getMessage("德波羅祖: 這次攻擊的城堡是肯特城堡。與守護城堡一起攻陷城堡!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("德波羅祖: 這次攻擊的城堡是奧克城堡。與守護城堡一起攻陷城堡!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("德波羅祖: 這次攻擊的城堡是溫達烏德城堡。與守護城堡一起攻陷城堡!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("德波羅祖: 這次攻擊的城堡是奇岩城堡。與守護城堡一起攻陷城堡!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("德波羅祖: 這次攻擊的城堡是海音城堡。與守護城堡一起攻陷城堡!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("德波羅祖: 這次攻擊的城堡是地下城堡。與守護城堡一起攻陷城堡!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("德波羅祖: 這次攻擊的城堡是亞丁城堡。與守護城堡一起攻陷城堡!", MJSimpleRgb.red(), 6298, 10),
	};

	public static MJCastleWar newInstance(L1Clan defense, int id, int castleId, String castleName) {
		return new MJCastleWar(defense, id, castleId, castleName);
	}

	private int _castleId;
	private String _castleName;
	private Calendar _nextCal;
	private Calendar _readyCal;
	private Calendar _endCal;
	private Calendar _limitCal;
	private int _taxRate;
	private int _publicMoney;
	private int _security;
	private MJCastleWarEState _state;
	public L1MonsterInstance _boss;

	protected MJCastleWar(L1Clan defense, int id, int castleId, String castleName) {
		super(defense, WAR_TYPE.CASTLE, id);

		_state = MJCastleWarEState.IDLE;
		_castleId = castleId;
		_castleName = castleName;
	}

	public Calendar get_next_cal() {
		return _nextCal;
	}

	public int getCastleId() {
		return _castleId;
	}

	public String getCastleName() {
		return _castleName;
	}

	public Calendar nextCal() {
		return _nextCal;
	}

	public int getTaxRate() {
		return _taxRate;
	}

	public void setTaxRate(int i) {
		_taxRate = i;
	}

	public int getPublicMoney() {
		return _publicMoney;
	}

	public void setPublicMoney(int i) {
		if (_publicMoney < 0)
			i = 0;
		_publicMoney = i;
	}
	
	public void addPublicMoney(int i) {
		_publicMoney -= i;
	}

	public int getCastleSecurity() {
		return _security;
	}

	public void setCastleSecurity(int i) {
		_security = i;
	}

	public void setState(MJCastleWarEState state) {
		_state = state;
	}

	public boolean isIdle() {
		return _state.equals(MJCastleWarEState.IDLE);
	}

	public boolean isRun() {
		return _state.equals(MJCastleWarEState.RUN);
	}

	public boolean isReady() {
		return _state.equals(MJCastleWarEState.READY);
	}

	public boolean isClosing() {
		return _state.equals(MJCastleWarEState.CLOSING);
	}

	public int getSpareSeconds() {
		Calendar curCal = RealTimeClock.getInstance().getRealTimeCalendar();
		int result = (int) ((_endCal.getTimeInMillis() - curCal.getTimeInMillis()) / 1000L);
		if (result > 0)
			return result;
		return 0;
	}

	public void nextCalendar(Timestamp ts) {
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		cal.setTimeInMillis(ts.getTime());
		nextCalendar(cal);
	}

	public void nextCalendar(Calendar cal) {
		disposesCalendar();
		_nextCal = cal;
		_readyCal = (Calendar) _nextCal.clone();
		_readyCal.add(Calendar.MINUTE, -5);
		_endCal = (Calendar) _nextCal.clone();
		_endCal.add(Config.ServerAdSetting.WarTime.unit, Config.ServerAdSetting.WarTime.time);
		_limitCal = (Calendar) _endCal.clone();
	}

	public void updateTime(int increaseMinute) {
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		Calendar tmp = (Calendar) cal.clone();
		tmp.add(Calendar.MINUTE, increaseMinute);
		if (tmp.getTimeInMillis() > _limitCal.getTimeInMillis()) {
			_endCal = (Calendar) _limitCal.clone();
			tmp.clear();
		} else {
			_endCal = tmp;
		}
	}

	@Override
	public void updateDefense(L1Clan nextDefense) {
		L1Clan oldDefense = _defense;
		_defense.setCastleId(0);
		super.updateDefense(nextDefense);
		_defense.setCastleId(_castleId);
		updateTime(20);
		MJCastleWarBusiness.getInstance().updateCastle(_castleId);
		final ServerBasePacket oncrown = new S_CastleMaster(_castleId, nextDefense.getLeaderId());
		final ServerBasePacket message = new S_ServerMessage(643);
		try {
			final L1Location loc = createGetBackLocation();
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null)
					continue;
				MJBotAI ai = pc.getAI();
				if (ai != null) {
					MJBotType type = ai.getBotType();
					if (type == MJBotType.REDKNIGHT || type == MJBotType.PROTECTOR) {
						ai.setRemoved(true);
						pc.setDead(true);
					}
					ai.setStatus(MJBotStatus.SETTING);
				} else {
					L1Clan clan = pc.getClan();
					if ((clan == null || clan.getCastleId() != _castleId)) {
						if (L1CastleLocation.checkInWarArea(_castleId, pc) && !pc.isGm()) {
							L1Location nloc = loc.randomLocation(15, true);
							teleport(pc, nloc.getX(), nloc.getY(), nloc.getMapId());
						}
						offLordBuff(pc);
					} else {
						pc.sendPackets(message, false);
					}
					MJCastleWarBusiness.move(pc);
					pc.sendPackets(oncrown, false);
				}
			}
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							if (pc == null || pc.getAI() != null)
								continue;

							L1Clan clan = pc.getClan();
							if ((clan == null || clan.getCastleId() != _castleId)) {
								if (L1CastleLocation.checkInWarArea(_castleId, pc) && !pc.isGm()) {
									L1Location nloc = loc.randomLocation(15, true);
									pc.start_teleport(nloc.getX(), nloc.getY(), nloc.getMapId(), pc.getHeading(), 18339, true);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 1000L);
			/*
			 * L1World.getInstance().getAllPlayerStream().filter((L1PcInstance pc) -> pc != null) .forEach((L1PcInstance pc) -> { try { MJBotAI ai = pc.getAI(); if (ai != null) { MJBotType type = ai.getBotType(); if (type == MJBotType.REDKNIGHT || type ==
			 * MJBotType.PROTECTOR) { ai.setRemoved(true); pc.setDead(true); } ai.setStatus(MJBotStatus.SETTING); } else { L1Clan clan = pc.getClan(); if ((clan == null || clan.getCastleId() != _castleId)) { if (L1CastleLocation.checkInWarArea(_castleId, pc) &&
			 * !pc.isGm()) { L1Location nloc = loc.randomLocation(15, true); teleport(pc, nloc.getX(), nloc.getY(), nloc.getMapId()); } offLordBuff(pc); } else { pc.sendPackets(message, false); } MJCastleWarBusiness.move(pc); pc.sendPackets(oncrown, false); } } catch
			 * (Exception e) { e.printStackTrace(); } });
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		oncrown.clear();
		message.clear();
		deleteTower();
		spawnTower();
		spawnDoor();
		MJBotType.REDKNIGHT.dispose();
		MJBotType.PROTECTOR.dispose();
		if (_defense.isRedKnight()) {
			MJAIScheduler.getInstance().setRKSchedule(_defense, _castleId, true);
		} else {
			ClanTable.getInstance().updateClan(nextDefense);
		}
		if (oldDefense != null && !oldDefense.isRedKnight())
			ClanTable.getInstance().updateClan(oldDefense);

		GeneralThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				_defense.broadcast(createInjury(_defense, SIEGE_KIND.SIEGE_DEFFENCE), true);
			}
		});
	}

	@Override
	public void dispose() {
		super.dispose();
		disposesCalendar();
	}

	private void disposesCalendar() {
		if (_nextCal != null) {
			_nextCal.clear();
			_nextCal = null;
		}
		if (_readyCal != null) {
			_readyCal.clear();
			_readyCal = null;
		}
		if (_endCal != null) {
			_endCal.clear();
			_endCal = null;
		}
		if (_limitCal != null) {
			_limitCal.clear();
			_limitCal = null;
		}
	}

	public boolean isRunTime(Calendar cal) {
		return !isRun() && cal.after(_nextCal) && (cal.before(_endCal) || cal.before(_limitCal));
	}

	public boolean isReadyTime(Calendar cal) {
		return isIdle() && cal.after(_readyCal) && cal.before(_nextCal);
	}

	public boolean isClosingTime(Calendar cal) {
		return isRun() && (cal.after(_limitCal) || cal.after(_endCal));
	}

	public void ready() {
		if (!isReady()) {
			setState(MJCastleWarEState.READY);
			// GeneralThreadPool.getInstance().execute(new WarReadier());
		}
	}

	public void run() {
		if (!isRun()) {
			setState(MJCastleWarEState.RUN);
			GeneralThreadPool.getInstance().execute(new WarOpener());
		}
	}

	public void close() {
		if (!isClosing() && isRun()) {
			setState(MJCastleWarEState.CLOSING);
			GeneralThreadPool.getInstance().execute(new WarCloser());
		}
	}

	private void teleport(L1PcInstance pc, int x, int y, int mid) {
		if (MJCommons.isNonAction(pc) || pc.hasSkillEffect(L1SkillId.DESPERADO) || pc.hasSkillEffect(L1SkillId.ETERNITI) || pc.hasSkillEffect(L1SkillId.TEMPEST)
				|| pc.hasSkillEffect(L1SkillId.PHANTOM))
			pc.start_teleport(x, y, mid, pc.getHeading(), 18339, false, false);
		pc.start_teleport(x, y, mid, pc.getHeading(), 18339, true, false);
	}

	public MJCastleWar proclaim(L1Clan clan) {
		if (!isRun())
			return null;

		if (MJWar.isOffenseClan(clan))
			return null;

		register(clan);
		ProtoOutputStream stream = createInjury(clan, SIEGE_KIND.SIEGE_ATTACK);
		clan.createOnlineMembers().forEach((L1Clan.ClanMember m) -> {
			if (m.player != null) {
				if (L1CastleLocation.checkInWarArea(_castleId, m.player))
					m.player.sendPackets(stream, false);
				onLordBuff(m.player);
			}
		});
		stream.dispose();
		return this;
	}

	public MJCastleWar proclaim(L1PcInstance pc) {
		// 如果戰爭?未開始，返回 null
		if (!isRun())
			return null;

		L1Clan clan = null;
		// 檢?玩家是否屬於紅騎士家族
		if (pc.getRedKnightClanId() > 0)
			clan = ClanTable.getInstance().getRedKnight(_castleId); // 如果屬於紅騎士家族，從 ClanTable 獲取紅騎士家族
		else
			clan = pc.getClan(); // 否則獲取玩家當前的家族

		// 如果該家族已經在進行戰爭，發送提示信息?返回 null
		if (MJWar.isOffenseClan(clan)) {
			pc.sendPackets("戰爭: 已經在戰爭中"); // ?譯「전쟁: 이미 전쟁 중」?「戰爭: 已經在戰爭中」
			return null;
		}

		// 其他代碼（假設在其他地方定義）
	}

		register(clan); // 註冊家族
		ProtoOutputStream stream = createInjury(clan, SIEGE_KIND.SIEGE_ATTACK); // 創建攻擊戰?傷害流
	clan.createOnlineMembers().forEach((L1Clan.ClanMember m) -> {
			if (m.player != null) { // 檢?成員是否在線
				if (L1CastleLocation.checkInWarArea(_castleId, m.player)) { // 檢?成員是否在戰區內
					m.player.sendPackets(stream, false); // 發送傷害流數據包給玩家
					onLordBuff(m.player); // 給玩家添加領主增益
					final L1Location loc = createGetBackLocation(); // 創建返回位置
					L1Location nloc = loc.randomLocation(15, true); // 獲取隨機位置
					teleport(m.player, nloc.getX(), nloc.getY(), nloc.getMapId()); // 傳送玩家到隨機位置
				}
			}
		});
	stream.dispose(); // 釋放流資源
	return this; // 返回當前對象

	public ProtoOutputStream createInjury(L1Clan clan, SIEGE_KIND kind) {
		SC_SIEGE_INJURY_TIME_NOIT noti = SC_SIEGE_INJURY_TIME_NOIT.newInstance();
		noti.set_remainSecond(getSpareSeconds());
		noti.set_siegeKind(kind);
		noti.set_pledgeName(clan.getClanName());
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_SIEGE_INJURY_TIME_NOIT);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream createInjury() {
		SC_SIEGE_INJURY_TIME_NOIT noti = SC_SIEGE_INJURY_TIME_NOIT.newInstance();
		noti.set_siegeKind(SIEGE_KIND.SIEGE_ATTACK);
		noti.set_remainSecond(0);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_SIEGE_INJURY_TIME_NOIT);
		noti.dispose();
		return stream;
	}

	public void onLordBuff(L1PcInstance pc) {
		// 給玩家添加主君的增益效果
		pc.setSkillEffect(L1SkillId.盟主增益, 3600000); // 設置主君的增益效果，持續時間?3600000毫秒
		pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 490), true); // 發送無限時間圖標數據包，圖標ID?490，狀態?1
	}

	public void offLordBuff(L1PcInstance pc) {
		// 移除玩家的主君的增益效果
		pc.removeSkillEffect(L1SkillId.盟主增益); // 移除主君的增益效果
		pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 0, 490)); // 發送無限時間圖標數據包，圖標ID?490，狀態?0
	}

	public boolean isAIAndRedKnightRemoved(L1PcInstance pc) {
		MJBotAI ai = pc.getAI();
		if (ai != null) {
			if (ai.getBotType() == MJBotType.REDKNIGHT || ai.getBotType() == MJBotType.PROTECTOR)
				MJAIScheduler.getInstance().removeSchedule(ai);
			return true;
		}
		return false;
	}

	public void exit() {
		setState(MJCastleWarEState.IDLE);
		initializeOffenses();

		final L1Location loc = createGetBackLocation();
		L1World.getInstance().getAllPlayerStream().filter((L1PcInstance pc) -> pc != null && pc.getAI() == null).forEach((L1PcInstance pc) -> {
			try {
				L1Clan clan = pc.getClan();
				if (pc.getMapId() == 15482) {
					int[] back = null;
					back = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
					pc.start_teleport(back[0], back[1], back[2], pc.getHeading(), 18339, true);
				} else if ((clan == null || clan.getCastleId() != _castleId)) {
					if (L1CastleLocation.checkInWarArea(_castleId, pc) && !pc.isGm()) {
						L1Location nloc = loc.randomLocation(15, true);
						teleport(pc, nloc.getX(), nloc.getY(), nloc.getMapId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void spawnTower() {
		L1WarSpawn wsp = new L1WarSpawn();
		wsp.SpawnTower(_castleId);
	}

	private void spawnDoor() {
		ArrayList<L1DoorInstance> doors = DoorSpawnTable.getInstance().getDoorList();
		for (L1DoorInstance door : doors) {
			if (L1CastleLocation.checkInWarArea(_castleId, door)) {
				door.setAutoStatus(0);// 자동수리를 해제
				door.repairGate();
			}
		}
		doors = null;
	}

	private void spawns() {
		L1WarSpawn wsp = new L1WarSpawn();
		wsp.SpawnFlag(_castleId);
		spawnDoor();
	}

	private void deleteTower() {
		L1TowerInstance tower = null;
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object instanceof L1TowerInstance) {
				tower = (L1TowerInstance) l1object;
				if (L1CastleLocation.checkInWarArea(_castleId, tower)) {
					tower.deleteMe();
				}
			}
		}
	}

	public L1Location createGetBackLocation() {
		int[] arr = L1CastleLocation.getGetBackLoc(_castleId);
		return new L1Location(arr[0], arr[1], arr[2]);
	}

	class WarReadier implements Runnable {
		@Override
		public void run() {
			int idx = -1;
			while (isReady()) {
				if (idx++ == -1) {
					L1World.getInstance().broadcastPacketToAll(_messages4ForCastle[_castleId - 1], false);
				} else if (idx >= _messages.length) {
					idx = -1;
				} else {
					L1World.getInstance().broadcastPacketToAll(_messages[idx], false);
				}
				try {
					Thread.sleep(30000L);
				} catch (Exception e) {
				}
			}
		}
	}

	class WarOpener implements Runnable {
		@override
		public void run() {
			try {
				spawns(); // 執行生成操作
				// _castleId 是正在進行攻城戰的城堡編號。添加Boss時參考下面的註釋
				MJCastleWar war = MJCastleWarBusiness.getInstance().get(_castleId); // 獲取攻城戰對象
				L1Clan defense = war.getDefenseClan(); // 獲取防守家族
				if (defense.getClanName().equalsIgnoreCase("紅色騎士團")) { // 如果防守家族名稱是 "紅色騎士團"
					if (_castleId == 4) // 如果城堡編號是4
						_boss = spawn(33634, 32678, (short) 15482, 73201247); // 生成Boss
				}
				/*
				 * else if (_castleId == 5)// 在輸入相應的城堡編號後輸入座標和NPC ID。
				 * _boss = spawn(33634, 32678, (short) 15482, 45018);
				 */
				if (_security == 1) // 如果安全模式為1
					onSecurity(); // 啟用安全模式
				selectMode(); // 選擇模式
				doStartup(); // 啟動
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(String.format("目前進行中的 '%s'", _castleName)), true); // 廣播攻城戰信息
			} catch (Exception e) {
				e.printStackTrace(); // 捕獲異常並打印堆棧跟蹤信息
			}
		}
	}

		private void onSecurity() {
			if (_castleId >= 1 && _castleId <= 4) {
				final int[] loc = L1CastleLocation.getGetBackLoc(_castleId);
				final int defenseId = _defenseId;
				L1World.getInstance().getAllPlayerStream().filter((L1PcInstance pc) -> {
					if (pc == null || pc.isGm() || pc.getClanid() == defenseId)
						return false;
					int mapId = pc.getMapId();
					return (mapId == 52 || mapId == 248 || mapId == 249 || mapId == 250 || mapId == 251) || pc.getClanid() <= 0;
				}).forEach((L1PcInstance pc) -> {
					teleport(pc, loc[0], loc[1], loc[2]);
				});
			}
			setCastleSecurity(0);
			MJCastleWarBusiness.getInstance().updateCastle(_castleId);
			CharacterTable.getInstance().updateLoc(_castleId, 52, 248, 249, 250, 251);
		}

		private void selectMode() {
			if (_defense.isRedKnight()) {
				MJAIScheduler.getInstance().setRKSchedule(_defense, _castleId, true);
			} else {
				L1Clan clan = ClanTable.getInstance().getRedKnight(_castleId);
				MJAIScheduler.getInstance().setRKSchedule(clan, _castleId, false);
				proclaim(clan);
			}
			L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { S_IconMessage.getMessage(1510, MJSimpleRgb.red(), 6298, 10), S_WarStartMessage.get() });
		}

		private void doStartup() {
			ProtoOutputStream injury = createInjury(_defense, SIEGE_KIND.SIEGE_DEFFENCE);
			int[] loc = L1CastleLocation.getGetBackLoc(_castleId);
			L1World.getInstance().getAllPlayerStream().filter((L1PcInstance pc) -> pc != null).forEach((L1PcInstance pc) -> {
				try {
					if (pc.getAI() != null && pc.getAI().getBotType() == MJBotType.SIEGELEADER) {
						MJBotAI ai = pc.getAI();
						if (ai.getWarCastle() == -1)
							ai.setWarCastle(_castleId);
					} else if (L1CastleLocation.checkInWarArea(_castleId, pc)) {
						L1Clan clan = pc.getClan();
						if (clan != null) {
							if (clan.getCastleId() == _castleId) {
								MJCastleWarBusiness.move(pc);
								pc.sendPackets(injury, false);
								onLordBuff(pc);
							} else {
								teleport(pc, loc[0], loc[1], loc[2]);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			try {
				injury.dispose();
			} catch (Exception e) {
			}
		}
	}

	class WarCloser implements Runnable {
		@Override
		public void run() {
			try {
				Calendar cal = (Calendar) _nextCal.clone();
				cal.add(Config.ServerAdSetting.WarInterval.unit, Config.ServerAdSetting.WarInterval.time);
				nextCalendar(cal);
				_taxRate = Config.ServerAdSetting.TaxRate;
				MJCastleWarBusiness.getInstance().updateCastle(_castleId);

				L1World.getInstance().createVisibleObjectsStream(L1CastleLocation.getCastleByMapId(_castleId)).filter((L1Object obj) -> obj != null).forEach((L1Object obj) -> {
					if (obj.instanceOf(MJL1Type.L1TYPE_PC)) {
						L1PcInstance pc = (L1PcInstance) obj;
						if (!isAIAndRedKnightRemoved(pc)) {
							MJCastleWarBusiness.move(pc);
							offLordBuff(pc);
						}
					} else if (obj instanceof L1FieldObjectInstance || obj instanceof L1CrownInstance || obj instanceof L1TowerInstance) {
						L1NpcInstance npc = (L1NpcInstance) obj;
						if (L1CastleLocation.checkInWarArea(_castleId, npc))
							npc.deleteMe();
					}
				});
				notifyTheEnd();
				spawnTower();
				if (_boss != null)
					_boss.deleteMe();
				spawnDoor();
				rewardDefense();
				MJBotType.REDKNIGHT.dispose();
				MJBotType.PROTECTOR.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				exit();
			}
		}

		private void rewardDefense() {
// 創建一個新的系統消息，表示攻城戰勝利並發放獎勵
			ServerBasePacket pck = new S_SystemMessage("\f3攻城戰勝利 : 獎勵已發放。");
			try {
				// 遍歷所有玩家，過濾出有效的玩家並且不是AI或紅騎士
				L1World.getInstance().getAllPlayerStream().filter((L1PcInstance pc) -> {
					return !(pc == null || isAIAndRedKnightRemoved(pc));
				}).forEach((L1PcInstance pc) -> {
					// 獲取玩家所在的家族
					L1Clan clan = pc.getClan();
					// 獲取獎勵物品的ID和數量
					int WARWINITEMIDLEADER = Config.ServerAdSetting.WAR_WIN_ITEMID_LEADER;
					int WARWINITEMIDCOUNTLEADER = Config.ServerAdSetting.WAR_WIN_ITEMID_COUNT_LEADER;
					int WARWINITEMID = Config.ServerAdSetting.WAR_WIN_ITEMID;
					int WARWINITEMIDCOUNT = Config.ServerAdSetting.WAR_WIN_ITEMID_COUNT;

					// 如果玩家所在的家族正在防守本城堡
					if (clan != null && clan.getCastleId() == _castleId) {
						// 如果玩家是家族的領袖
						if (pc.getClan().getLeaderId() == pc.getId()) {
							// 獲取攻城戰對象和公共金錢
							MJCastleWar war = MJCastleWarBusiness.getInstance().get(_castleId);
							int adena = war.getPublicMoney();
							// 將公共金錢存入玩家的庫存
							pc.getInventory().storeItem(L1ItemId.ADENA, adena);
							war.addPublicMoney(adena);
							// 更新城堡信息
							MJCastleWarBusiness.getInstance().updateCastle(_castleId);

							// 給予領袖獎勵物品
							pc.getInventory().storeItem(WARWINITEMIDLEADER, WARWINITEMIDCOUNTLEADER, true); // 軍主獎勵
							pc.sendPackets("\f3攻城獎勵(軍主)已發放。");
						}
						// 給予家族成員獎勵物品
						pc.getInventory().storeItem(WARWINITEMID, WARWINITEMIDCOUNT, true); // 血盟成員獎勵
						pc.sendPackets(pck, false); // 發送系統消息
					}
				});
			} catch (Exception e) {
				e.printStackTrace(); // 捕獲異常並打印堆棧跟蹤信息
			}
		}
				});
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (pck != null)
					pck.clear();
			}
		}
	}

	public void notifyTheEnd() {
		try {
			_warLock.lock();
			for (L1Clan clan : _offenses.values()) {
				broadcastEndWar(_defense, clan);
				clan.setCurrentWar(null);
			}
			_offenses.clear();
		} finally {
			_warLock.unlock();
		}
	}

	private static L1MonsterInstance spawn(int x, int y, short MapId, int npcId) {
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			int tryCount = 0;
			do {
				tryCount++;
				npc.setX(x);
				npc.setY(y);
				if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
					break;
				}
				Thread.sleep(1);
			} while (tryCount < 50);
			if (tryCount >= 50) {
				npc.getLocation().forward(5);
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(5);

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return npc instanceof L1MonsterInstance ? (L1MonsterInstance) npc : null;
	}
}
