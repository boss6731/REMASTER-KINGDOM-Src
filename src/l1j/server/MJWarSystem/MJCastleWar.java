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

	private static final ServerBasePacket[] _messages = new ServerBasePacket[] { S_IconMessage.getMessage(3757, MJSimpleRgb.red(), 6298, 10), // ��������:
	};
	private static final ServerBasePacket[] _messages4ForCastle = new ServerBasePacket[] { S_IconMessage.getMessage("��������: �̹��� ������ ���� ��Ʈ���̴�. ��ȣ���� �Բ� ���� �Զ��϶�!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("��������: �̹��� ������ ���� ��ũ���̴�. ��ȣ���� �Բ� ���� �Զ��϶�!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("��������: �̹��� ������ ���� ���ٿ�强�̴�. ��ȣ���� �Բ� ���� �Զ��϶�!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("��������: �̹��� ������ ���� ������̴�. ��ȣ���� �Բ� ���� �Զ��϶�!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("��������: �̹��� ������ ���� ���̳׼��̴�. ��ȣ���� �Բ� ���� �Զ��϶�!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("��������: �̹��� ������ ���� �������̴�. ��ȣ���� �Բ� ���� �Զ��϶�!", MJSimpleRgb.red(), 6298, 10),
			S_IconMessage.getMessage("��������: �̹��� ������ ���� �Ƶ����̴�. ��ȣ���� �Բ� ���� �Զ��϶�!", MJSimpleRgb.red(), 6298, 10), };

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
                    return new L1PcInstance[0];
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
                return new L1PcInstance[0];
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
		if (!isRun())
			return null;

		L1Clan clan = null;
		if (pc.getRedKnightClanId() > 0)
			clan = ClanTable.getInstance().getRedKnight(_castleId);
		else
			clan = pc.getClan();
		if (MJWar.isOffenseClan(clan)) {
			pc.sendPackets("����: �̹� ���� ��");
			return null;
		}

		register(clan);
		ProtoOutputStream stream = createInjury(clan, SIEGE_KIND.SIEGE_ATTACK);
		clan.createOnlineMembers().forEach((L1Clan.ClanMember m) -> {
			if (m.player != null) {
				if (L1CastleLocation.checkInWarArea(_castleId, m.player)) {
					m.player.sendPackets(stream, false);
					onLordBuff(m.player);
					final L1Location loc = createGetBackLocation();
					L1Location nloc = loc.randomLocation(15, true);
					teleport(m.player, nloc.getX(), nloc.getY(), nloc.getMapId());
				}
			}
		});
		stream.dispose();
		return this;
	}

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
		pc.setSkillEffect(L1SkillId.�ֱ��ǹ���, 3600000);
		pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 490), true);
	}

	public void offLordBuff(L1PcInstance pc) {
		pc.removeSkillEffect(L1SkillId.�ֱ��ǹ���);
		pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 0, 490));
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
				door.setAutoStatus(0);// �ڵ������� ����
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
            return new L1PcInstance[0];
        }
	}

	class WarOpener implements Runnable {
		@Override
		public void run() {
			try {
				spawns();
				// _castleId �� �������� �� ��ȣ. ���� ���� �߰��� �Ʒ� �ּ� ����
				MJCastleWar war = MJCastleWarBusiness.getInstance().get(_castleId);
				L1Clan defense = war.getDefenseClan();
				if (defense.getClanName().equalsIgnoreCase("���� ����")) {
					if (_castleId == 4)
						_boss = spawn(33634, 32678, (short) 15482, 73201247);
				}
				/*
				 * else if (_castleId == 5)// �ش� ����ȣ �Է��� ��ǥ�� ���Ǿ� ���̵� �Է�. _boss = spawn(33634, 32678, (short) 15482, 45018);
				 */
				if (_security == 1)
					onSecurity();
				selectMode();
				doStartup();
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(String.format("���� �������� \'%s\'", _castleName)), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
            return new L1PcInstance[0];
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
            return new L1PcInstance[0];
        }

		private void rewardDefense() {
			ServerBasePacket pck = new S_SystemMessage("\\f3������ �¸� : ������ ���� �Ǿ����ϴ�.");
			try {
				L1World.getInstance().getAllPlayerStream().filter((L1PcInstance pc) -> {
					return !(pc == null || isAIAndRedKnightRemoved(pc));
				}).forEach((L1PcInstance pc) -> {
					L1Clan clan = pc.getClan();
					int WARWINITEMIDLEADER = Config.ServerAdSetting.WAR_WIN_ITEMID_LEADER;
					int WARWINITEMIDCOUNTLEADER = Config.ServerAdSetting.WAR_WIN_ITEMID_COUNT_LEADER;
					int WARWINITEMID = Config.ServerAdSetting.WAR_WIN_ITEMID;
					int WARWINITEMIDCOUNT = Config.ServerAdSetting.WAR_WIN_ITEMID_COUNT;

					if (clan != null && clan.getCastleId() == _castleId) {
						if (pc.getClan().getLeaderId() == pc.getId()) {
							MJCastleWar war = MJCastleWarBusiness.getInstance().get(_castleId);
							int adena = war.getPublicMoney();
							pc.getInventory().storeItem(L1ItemId.ADENA, adena);
							war.addPublicMoney(adena);
							MJCastleWarBusiness.getInstance().updateCastle(_castleId);
							
							pc.getInventory().storeItem(WARWINITEMIDLEADER, WARWINITEMIDCOUNTLEADER, true);// ���� ����
							pc.sendPackets("\\f3���� ����(����)�� ���� �Ǿ����ϴ�.");
						}
						pc.getInventory().storeItem(WARWINITEMID, WARWINITEMIDCOUNT, true);// ���� ����
						pc.sendPackets(pck, false);
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
