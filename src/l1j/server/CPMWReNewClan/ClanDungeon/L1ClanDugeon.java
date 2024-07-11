package l1j.server.CPMWReNewClan.ClanDungeon;

import java.util.ArrayList;

import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE_NOT;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.L1SpawnUtil;

public class L1ClanDugeon implements Runnable {
	private int _mapnum = 0;
	private int _DugeonType = 0;
	private boolean _isNowCD;
	private boolean _active = false;
	private int DRAmount = 0;
	private int DSRAmount = 0;
	private boolean _close = false;
	private int stage = 0;
	private int counter = 0;
	L1NpcInstance[] stageboss;
	L1NpcInstance icegolemking;
	ArrayList<L1NpcInstance> stageunit;
	L1NpcInstance[] _RoundPortal = new L1NpcInstance[4];
	L1DoorInstance[] _Door = new L1DoorInstance[6];
	L1NpcInstance[] _HurryCainPortal;
	private L1NpcInstance Portal;

	private final ArrayList<L1PcInstance> _members = new ArrayList<L1PcInstance>();

	public void Start(int type) {
		if (type == 1) {
			DRAmount = 3;
			DSRAmount = 6;
		} else if (type == 2) {
			DRAmount = 11;
			DSRAmount = 6;
		} else {
			DRAmount = 5;
			DSRAmount = 2;
		}
		GeneralThreadPool.getInstance().execute(this);
	}

	public L1ClanDugeon() {
	}

	public L1ClanDugeon(int mapid, int type, L1PcInstance pc) {
		_DugeonType = type;
		_mapnum = mapid;
		Portal = L1SpawnUtil.spawnnpc(pc.getX(), pc.getY(), pc.getMapId(), type == 3 ? 120620 : 120599 + type, 0,
				ClanDugeon.getInstance().ClanDugeonInfo.waitingtimes * 1000, _mapnum);
		initdoor();
		GeneralThreadPool.getInstance().schedule(new timer(),
				ClanDugeon.getInstance().ClanDugeonInfo.waitingtimes * 1000);
	}

	class timer implements Runnable {
		@Override
		public void run() {
			try {
				if (_close) {
					return;
				}

				end_check();

				if (_DugeonType == 3) {
					if (Dooropencheack()) {
						if (stage == 1) {
							_Door[0].open();
							_Door[1].open();
							_Door[0].deleteMe();
							_Door[1].deleteMe();
						} else if (stage == 2) {
							_Door[2].open();
							_Door[3].open();
							_Door[2].deleteMe();
							_Door[3].deleteMe();
						} else if (stage == 3) {
							_Door[4].open();
							_Door[5].open();
							_Door[4].deleteMe();
							_Door[5].deleteMe();
						} else if (stage == 4) {
							_HurryCainPortal = new L1NpcInstance[6];
							_HurryCainPortal[0] = L1SpawnUtil.spawnnpc(32784, 32956, (short) _mapnum, 120815, 3,
									3600 * 1000, 0);
							_HurryCainPortal[1] = L1SpawnUtil.spawnnpc(32793, 32960, (short) _mapnum, 120815, 3,
									3600 * 1000, 0);
							_HurryCainPortal[2] = L1SpawnUtil.spawnnpc(32791, 32970, (short) _mapnum, 120815, 3,
									3600 * 1000, 0);
							_HurryCainPortal[3] = L1SpawnUtil.spawnnpc(32784, 32971, (short) _mapnum, 120815, 3,
									3600 * 1000, 0);
							_HurryCainPortal[4] = L1SpawnUtil.spawnnpc(32779, 32967, (short) _mapnum, 120815, 3,
									3600 * 1000, 0);
							_HurryCainPortal[5] = L1SpawnUtil.spawnnpc(32778, 32960, (short) _mapnum, 120815, 3,
									3600 * 1000, 0);
							_RoundPortal[3].deleteMe();
							_RoundPortal[3] = null;
						}
					}
					laststageteleport();
				}
				removeRetiredMembers();

				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		try {
			setActive(true);
			countDown();
			setNowCD(true);
			givekey();
			for (int round = 1; round <= DRAmount; round++) {
				if (_close) {
					break;
				}
				stage = round;
				for (int subround = 1; subround <= DSRAmount; subround++) {
					if (_close)
						break;

					if (_DugeonType == 3 && subround == 2 && round <= 4) {
						for (int i = 0; i < stageunit.size(); i++) {
							while (!stageunit.get(i).isDead() && counter <= 6000 && getMembersCount() > 0) {
								if (_close) {
									break;
								}

								if (counter >= 6000) {
									sendMessage("超過該回合限制時間10分鐘，將被強制返回。", 5000);
									quit();
									break;
								}

								Thread.sleep(100);
								removeRetiredMembers();
								counter++;
							}
						}
						counter = 0;
						roundeffect(round);
						Thread.sleep(5000);
					}

					if (getMembersCount() > 0) {
						ClanDugeonSpawn.getInstance().fillSpawnTable(_mapnum, _DugeonType, round, subround);
					}
					Thread.sleep(ClanDugeon.getInstance().ClanDugeonInfo.waitingtimeSR * 1000);
				}
				if (stageboss == null) {
					waitForNextRound(round);
				} else {
					for (int i = 0; i < stageboss.length; i++) {
						while (!stageboss[i].isDead()) {
							if (_close) {
								break;
							}

							if (counter > 6000) {
								sendMessage("超過該回合Boss限制時間10分鐘，將被強制返回。", 5000);
								quit();
								break;
							}

							Thread.sleep(100);
							removeRetiredMembers();
							counter++;
						}
					}
					bosskill(_DugeonType, round);
					waitForNextRound(round);
				}
			}
			if (!_close) {
				for (L1PcInstance pc : getMembersArray()) {
					int[] loc = Getback.GetBack_Location(pc, true);
					pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true);
					removeMember(pc);
				}
				quit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void roundeffect(int round) {
		switch (round) {
			case 1:
				for (L1PcInstance pc : getMembersArray()) {
					pc.sendPackets(S_DisplayEffect.newInstance(S_DisplayEffect.BLOOD_DUNGEON_PUPLE));
				}
				break;
			case 2:
				for (L1PcInstance pc : getMembersArray()) {
					pc.sendPackets(S_DisplayEffect.newInstance(S_DisplayEffect.BLOOD_DUNGEON_RED));
				}
				break;
			case 3:
				for (L1PcInstance pc : getMembersArray()) {
					pc.sendPackets(S_DisplayEffect.newInstance(S_DisplayEffect.BLOOD_DUNGEON_BLUE));
				}
				break;
			case 4:
				for (L1PcInstance pc : getMembersArray()) {
					pc.sendPackets(S_DisplayEffect.newInstance(S_DisplayEffect.BLOOD_DUNGEON_BLUE));
				}
				break;
		}
	}

	private void waitForNextRound(int curRound) throws InterruptedException {
		int wait = 0;
		if (_DugeonType == 1) {
			wait = ClanDugeon.getInstance().ClanDugeonInfo.waitingtimer;
		} else if (_DugeonType == 3) {
			wait = ClanDugeon.getInstance().ClanDugeonInfo.waitingtimer;
		} else {
			wait = (int) (ClanDugeon.getInstance().ClanDugeonInfo.waitingtimer / 100);
		}
		for (int i = 0; i < wait; i++) {
			Thread.sleep(1000);
			if ((_DugeonType == 1 && curRound < 3 || _DugeonType == 2 && curRound < 11
					|| _DugeonType == 3 && curRound < 5) && !_close) {
				switch (ClanDugeon.getInstance().ClanDugeonInfo.waitingtimer - i) {
					case 60:
						if (_DugeonType == 1) {
							removeRetiredMembers();
							sendMessage(curRound + 1 + "軍隊怪物出現前還有1分鐘。", 5000);
						} else if (_DugeonType == 2) {
							removeRetiredMembers();
						} else {
							removeRetiredMembers();
						}
						break;
					case 30:
						if (_DugeonType == 1) {
							removeRetiredMembers();
							sendMessage(curRound + 1 + "距離軍事怪物出現還有 30 秒。", 5000);
						} else if (_DugeonType == 2) {
							removeRetiredMembers();
						} else {
							removeRetiredMembers();
						}
						break;
					case 20:
						if (_DugeonType == 1) {
							removeRetiredMembers();
							sendMessage(curRound + 1 + "距離軍事怪物出現還有 20 秒。", 5000);
						} else if (_DugeonType == 2) {
							removeRetiredMembers();
						} else {
							removeRetiredMembers();
						}
						break;
					case 10:
						if (_DugeonType == 1) {
							removeRetiredMembers();
							sendMessage(curRound + 1 + "距離軍事怪物出現還有 10 秒。", 5000);
						} else if (_DugeonType == 2) {
							removeRetiredMembers();
						} else {
							removeRetiredMembers();
							sendMessage(curRound + 1 + "區域啟動還剩10秒。", 5000);
						}
						break;
					case 1:
						if (_DugeonType == 1) {
							removeRetiredMembers();
							sendMessage("怪物即將襲擊。", 1000);
							for (L1PcInstance pc : getMembersArray()) {
								pc.sendPackets(S_DisplayEffect.newInstance(10));
								pc.sendPackets(S_DisplayEffect.newInstance(7));
							}
						} else if (_DugeonType == 3 && curRound < 4) {
							removeRetiredMembers();
							sendMessage("怪物即將襲擊。", 1000);
							for (L1PcInstance pc : getMembersArray()) {
								pc.sendPackets(S_DisplayEffect.newInstance(10));
								pc.sendPackets(S_DisplayEffect.newInstance(7));
							}
						} else {
							removeRetiredMembers();
						}
						break;
					default:
						removeRetiredMembers();
						break;
				}
			} else {
				switch (ClanDugeon.getInstance().ClanDugeonInfo.waitingtimer - i) {
					case 60:
						removeRetiredMembers();
						sendMessage("請返回。60秒後將強制返回。", 5000);
						break;
					case 30:
						removeRetiredMembers();
						sendMessage("請返回。 30秒後您將強制返回。", 5000);
						break;
					case 20:
						removeRetiredMembers();
						sendMessage("請返回。 20秒後您將強制返回。", 5000);
						break;
					case 10:
						removeRetiredMembers();
						sendMessage("請返回。 10秒後您將強制返回。", 5000);
						break;
					case 1:
						removeRetiredMembers();
						sendMessage("請返回。不久後將強制返回。", 1000);
						break;
					default:
						removeRetiredMembers();
						break;
				}
			}
		}
		removeRetiredMembers();
	}

	private void bosskill(int DugeonType, int round) throws InterruptedException {
		if (DugeonType == 1) {
			switch (round) {
				case 1:
				case 2:
				case 3:
					for (L1PcInstance pc : getMembersArray()) {
						addQuestExp(pc, DugeonType);
					}
					if (round == 3) {
						sendMessage("所有壞蛋都被打敗了。", 5000);
					}
					break;
				default:
					break;
			}
		} else if (DugeonType == 2) {
			switch (round) {
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
					for (L1PcInstance pc : getMembersArray()) {
						addQuestExp(pc, DugeonType);
					}
					if (round == 11) {
						sendMessage("所有壞蛋都被打敗了。", 5000);
					}
					break;
				default:
					break;
			}
		} else {
			switch (round) {
				case 1:
					if (MJRnd.isWinning(100, ClanDugeon.getInstance().ClanDugeonInfo.step2per)) {
						_RoundPortal[0] = L1SpawnUtil.spawnnpc(32804, 32826, (short) _mapnum, 8503183, 0, 0, 0);
					} else {
						_close = true;
					}
					break;
				case 2:
					if (MJRnd.isWinning(100, ClanDugeon.getInstance().ClanDugeonInfo.step3per)) {
						_RoundPortal[1] = L1SpawnUtil.spawnnpc(32899, 32869, (short) _mapnum, 8503183, 0, 0, 0);
					} else {
						_close = true;
					}
					break;
				case 3:
					if (MJRnd.isWinning(100, ClanDugeon.getInstance().ClanDugeonInfo.step4per)) {
						_RoundPortal[2] = L1SpawnUtil.spawnnpc(32867, 32962, (short) _mapnum, 8503183, 0, 0, 0);
					} else {
						_close = true;
					}
					break;
				case 4:
					if (MJRnd.isWinning(100, ClanDugeon.getInstance().ClanDugeonInfo.step5per)) {
						_RoundPortal[3] = L1SpawnUtil.spawnnpc(32786, 32964, (short) _mapnum, 8503183, 0, 0, 0);
					} else {
						_close = true;
					}
					break;
				default:
					break;
			}
			for (L1PcInstance pc : getMembersArray()) {
				addQuestExp(pc, DugeonType);
			}
			if (round == 5) {
				sendMessage("所有壞蛋都被打敗了。", 5000);
			}
		}
	}

	public void addQuestExp(L1PcInstance pc, int type) {
		long needExp = ExpTable.getNeedExpNextLevel(52);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		long exp = 0;
		if (type == 1) {
			exp = (long) (needExp * 0.01D * exppenalty);
		} else {
			exp = (long) (needExp * 0.01D * exppenalty);
		}
		pc.add_exp(exp);
		if (type == 1) {
			pc.send_effect(3944, true);
		}
	}

	private void countDown() throws InterruptedException {
		for (int loop = 0; loop < ClanDugeon.getInstance().ClanDugeonInfo.waitingtimes; loop++) {
			Thread.sleep(1000);
		}
		removeRetiredMembers();
		if (_DugeonType == 1 || _DugeonType == 2) {
			sendMessage("距離第一組怪物出現還有1分鐘。", 5000); // 10 秒前
			Thread.sleep(30000);
			removeRetiredMembers();
			sendMessage("距離第 1 組怪物出現還有 30 秒。", 5000); // 10 秒前
			Thread.sleep(10000);
			sendMessage("距離第 1 組怪物出現還有 20 秒。", 1000); // 5 秒前
			removeRetiredMembers();
			Thread.sleep(10000);
			sendMessage("距離第 1 組怪物出現還有 10 秒。", 1000); // 4 秒前
			removeRetiredMembers();
			Thread.sleep(10000);
			sendMessage("怪物即將襲擊。", 1000); // 3초전
			removeRetiredMembers();
			for (L1PcInstance pc : getMembersArray()) {
				pc.sendPackets(S_DisplayEffect.newInstance(10));
				pc.sendPackets(S_DisplayEffect.newInstance(7));
			}
		} else {
			sendMessage("亞丁勇士集結中....", 5000); // 10 秒前
			Thread.sleep(8000);
			removeRetiredMembers();
			sendMessage("發現暗黑龍的死神正率領軍隊準備入侵的跡象...”", 1000); // 10 秒前
			Thread.sleep(8000);
			sendMessage("為了亞丁的和平，決定在入侵前找到並消滅暗黑龍的死神....", 1000); // 5 秒前
			removeRetiredMembers();
			Thread.sleep(8000);
			sendMessage("必須突破總共4個地區才能到達暗黑龍的死神....", 1000); // 4 秒前
			removeRetiredMembers();
			Thread.sleep(8000);
			sendMessage("需要實力……運氣……所有的一切……都得具備……", 1000); // 3 秒前
			Thread.sleep(8000);
			sendMessage("祝勇士們的旅程幸運相伴....", 1000); // 3 秒前
			Thread.sleep(10000);
			sendMessage("距離啟動區域 1 還剩 10 秒......", 1000); // 3 秒前
			removeRetiredMembers();
			Thread.sleep(10000);
			sendMessage("怪物即將襲擊。", 1000); // 3 秒前
			removeRetiredMembers();
			for (L1PcInstance pc : getMembersArray()) {
				pc.sendPackets(S_DisplayEffect.newInstance(10));
				pc.sendPackets(S_DisplayEffect.newInstance(7));
			}
		}
	}

	private void end_check() {
		if (getMembersCount() <= 0) {
			quit();
		}
	}

	public void addMember(L1PcInstance pc) {
		if (!_members.contains(pc)) {
			_members.add(pc);
		}
	}

	public void removeMember(L1PcInstance pc) {
		_members.remove(pc);
	}

	public void clearMembers() {
		_members.clear();
	}

	public boolean isMember(L1PcInstance pc) {
		return _members.contains(pc);
	}

	public L1PcInstance[] getMembersArray() {

		return _members.toArray(new L1PcInstance[_members.size()]);
	}

	public int getMembersCount() {
		return _members.size();
	}

	private void setNowCD(boolean i) {
		_isNowCD = i;
	}

	public boolean isNowCD() {
		return _isNowCD;
	}

	private void setActive(boolean f) {
		_active = f;
	}

	public boolean isActive() {
		return _active;
	}

	private void removeRetiredMembers() {
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] == null || temp[i].getMapId() != _mapnum) {
				removeMember(temp[i]);
			}
		}
		temp = null;
	}

	public void appearaction(L1NpcInstance npc) {
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != null || temp[i].getMapId() == _mapnum) {
				npc.onPerceive(temp[i]);
				S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Appear);
				temp[i].sendPackets(gfx);
			}
		}
		temp = null;
	}

	private void sendMessage(String msg, int time) {
		L1PcInstance[] temp = getMembersArray();
		S_SystemMessage sm = new S_SystemMessage(msg);
		for (L1PcInstance pc : temp) {
			SC_NOTIFICATION_MESSAGE_NOT noti = SC_NOTIFICATION_MESSAGE_NOT.newInstance();
			noti.set_suffileNumber(-1);
			noti.set_notificationMessage("\\f=" + msg);
			noti.set_messageRGB(new MJSimpleRgb(0, 250, 0));
			noti.set_duration(time / 1000);
			pc.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_MESSAGE_NOT, true);
			pc.sendPackets(sm);
		}
		temp = null;
	}

	private void clearClanDugeon() {
		L1MonsterInstance mob = null;
		L1PcInstance pc = null;
		L1Inventory inventory = null;
		MJBotLocation loc = MJBotUtil.createRandomLocation(33428, 32796, 33447, 32831, 4);
		for (Object obj : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
			if (obj instanceof L1MonsterInstance) {
				mob = (L1MonsterInstance) obj;
				if (!mob.isDead()) {
					mob.setDead(true);
					mob.setStatus(ActionCodes.ACTION_Die);
					mob.setCurrentHp(0);
					mob.deleteMe();
				}
			} else if (obj instanceof L1PcInstance) {
				pc = (L1PcInstance) obj;
				pc.start_teleport(loc.x, loc.y, loc.map, pc.getHeading(), 169, true, true);
			} else if (obj instanceof L1Inventory) {
				inventory = (L1Inventory) obj;
				inventory.clearItems();
			}
		}
	}

	private void quit() {
		_close = true;
		System.out.println("血盟地下城結束地圖編號： " + _mapnum);
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			removeMember(temp[i]);
		}
		temp = null;
		Object_Delete();
		clearClanDugeon();
		clearMembers();
		ClanDugeon.getInstance().Reset(_mapnum);
		Portal.deleteMe();
	}

	private void Object_Delete() {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
			if (ob == null || ob instanceof L1DollInstance
					|| ob instanceof L1SummonInstance
					|| ob instanceof L1PetInstance)
				continue;
			if (ob instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) ob;
				npc.deleteMe();
			}
		}
		for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
			if (obj.getMapId() != _mapnum)
				continue;
			L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
	}

	private void initdoor() {
		_Door[0] = ClanDugeonSpawn.getInstance().fillSpawndoor(32810, 32829, _mapnum, 0, 120825);
		_Door[1] = ClanDugeonSpawn.getInstance().fillSpawndoor(32841, 32827, _mapnum, 0, 120826);
		_Door[2] = ClanDugeonSpawn.getInstance().fillSpawndoor(32893, 32876, _mapnum, 0, 120827);
		_Door[3] = ClanDugeonSpawn.getInstance().fillSpawndoor(32892, 32905, _mapnum, 0, 120828);
		_Door[4] = ClanDugeonSpawn.getInstance().fillSpawndoor(32862, 32966, _mapnum, 0, 120829);
		_Door[5] = ClanDugeonSpawn.getInstance().fillSpawndoor(32825, 32967, _mapnum, 0, 120830);
	}

	private void givekey() {
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != null || temp[i].getMapId() == _mapnum) {
				if (ClanDugeon.getInstance().ClanDugeonInfo.dayplay) {
					temp[i].getInventory().storeKeyItem(4101007, 1, true);
				} else {
					temp[i].getInventory().storeKeyItem(4101008, 1, true);
				}
			}
		}
	}

	private boolean Dooropencheack() {
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != null || temp[i].getMapId() == _mapnum) {
				if (_RoundPortal[0] != null && _Door[0] != null && _Door[1] != null && stage == 1) {
					if (temp[i].getX() == _RoundPortal[0].getX() && temp[i].getY() == _RoundPortal[0].getY()) {
						return true;
					}
				}
				if (_RoundPortal[1] != null && _Door[2] != null && _Door[3] != null && stage == 2) {
					if (temp[i].getX() == _RoundPortal[1].getX() && temp[i].getY() == _RoundPortal[1].getY()) {
						return true;
					}
				}
				if (_RoundPortal[2] != null && _Door[4] != null && _Door[5] != null && stage == 3) {
					if (temp[i].getX() == _RoundPortal[2].getX() && temp[i].getY() == _RoundPortal[2].getY()) {
						return true;
					}
				}

				if (_RoundPortal[3] != null && stage == 4) {
					if (temp[i].getX() == _RoundPortal[3].getX() && temp[i].getY() == _RoundPortal[3].getY()) {
						return true;
					}
				}
			}
		}
		temp = null;
		return false;
	}

	private void laststageteleport() {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
			if (ob instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) ob;
				if (_HurryCainPortal == null) {
					return;
				}
				if (_HurryCainPortal.length <= 0) {
					return;
				}
				for (int i = 0; i < 6; i++) {
					if (_HurryCainPortal[i] == null) {
						return;
					}
					if (pc.getX() == _HurryCainPortal[i].getX() && pc.getY() == _HurryCainPortal[i].getY()) {
						pc.start_teleport(32758, 32891, _mapnum, pc.getHeading(), 169, true);
						_HurryCainPortal[i].deleteMe();
					}
				}
			}
		}
	}
}