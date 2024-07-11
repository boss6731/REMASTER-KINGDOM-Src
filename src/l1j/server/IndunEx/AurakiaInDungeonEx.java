package l1j.server.IndunEx;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.IndunEx.PlayInfo.AurakiaInDungeon;
import l1j.server.IndunEx.PlayInfo.AurakiaInDungeonSpawn;
import l1j.server.IndunEx.RoomInfo.MJIndunRoomController;
import l1j.server.IndunEx.RoomInfo.MJIndunRoomMemberModel;
import l1j.server.IndunEx.RoomInfo.MJIndunRoomModel;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_DIALOGUE_MESSAGE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SCENE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo.SC_ARENA_GAME_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo.SC_ARENA_PLAY_STATUS_NOTI;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

public class AurakiaInDungeonEx implements Runnable {

	private Random rnd = new Random(System.currentTimeMillis());
	private final ArrayList<L1PcInstance> playmember = new ArrayList<L1PcInstance>();
	private int _mapnum = 0;
	private boolean start = false;
	private int time = 0;
	private boolean timer = false;
	private int gametime = 60 * 30;
	private static long oldtime = 0;

	private int step = 0;
	private int sub_step = 0;
	private int sub_step1 = 0;
	private int sub_step2 = 0;
	private int sub_step3 = 0;
	private int sub_step4 = 0;
	private int sub_step5 = 0;
	private int sub_step6 = 0;
	private int sub_step10 = 0;
	private int sub_step11 = 0;

	private int spawnCnt = 0;
	private int gateNumber = 0;
	private static int arrestedAttackCnt = 0;
	private int arrestedLevel = 0;

	private static final int FIRST_CNT = 2;
	private static final int GATE_CNT = 12;
	private static final int ARRESTED_CNT = 50;

	private ArrayList<L1NpcInstance> firstMonsterList;
	private L1NpcInstance arrestedAurakia;

	private L1NpcInstance circle_1;
	private L1NpcInstance circle_2;
	private L1NpcInstance circle_3;
	private L1NpcInstance circle_4;
	private L1NpcInstance circle_5;

	private L1NpcInstance monsterGate_1;
	private L1NpcInstance monsterGate_2;
	private L1NpcInstance monsterGate_3;
	private L1NpcInstance monsterGate_4;

	private L1DoorInstance enterDoor;
	private L1DoorInstance eyeGate;

	private L1DoorInstance stone_1;
	private L1DoorInstance stone_2;
	private L1DoorInstance stone_3;
	private L1DoorInstance stone_4;

	private L1NpcInstance darkWatch;
	private L1NpcInstance darkTransWatch;
	private L1NpcInstance egg;

	private MJIndunRoomModel _model;
	private boolean gate1Active = true;
	private boolean gate2Active = true;
	private boolean gate3Active = true;
	private boolean gate4Active = true;
	private int gatecnt = 0;

	private boolean _close = false;

	public void Start() {
		GeneralThreadPool.getInstance().execute(this);
	}

	public AurakiaInDungeonEx(int mapid, int price, MJIndunRoomModel model) {
		_mapnum = mapid;
		_model = model;

		for (MJIndunRoomMemberModel member_model : model.get_members()) {
			if (member_model.member == model.get_owner().member) {
				member_model.member.getInventory().consumeItem(model.get_indun_key(), 1);
			} else {
				member_model.member.getInventory().consumeItem(40308, price);
			}

			for (int i = 420100; i <= 420111; i++) {
				if (!member_model.member.getInventory().checkItem(i)) {
					continue;
				}
				member_model.member.getInventory().consumeItem(i);// 刪除奧拉西亞之箭
			}

			if (member_model.member.getInventory().checkItem(420122)) {
				member_model.member.getInventory().consumeItem(420122);// 刪除奧拉西亞之箭
			}

			MJPoint pt = MJPoint.newInstance(32798, 32826, 5, (short) mapid, 50);
			member_model.member.start_teleport(pt.x, pt.y, pt.mapId, member_model.member.getHeading(), 18339, false);
		}

		firstMonsterList = new ArrayList<L1NpcInstance>();

		spawn_door();
		spawn_egg();

		GeneralThreadPool.getInstance().schedule(new timer(), 1000);
	}

	class timer implements Runnable {
		boolean playerck = false;

		@Override
		public void run() {
			try {
				if (_close)
					return;
				checkMember();
				end_check();
				// home_teleport();
				if (!playerck) {
					step = 0;
					playerck = true;
				}

				if (!timer) {
					timer = true;
				}

				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO 自動產生的方法存根
		try {
			if (_close)
				return;
			switch (step) {
				case 0:
					// System.out.println("階段: "+step+" 附加階段: "+sub_step);
					if (sub_step == 0) {
						sub_step = 1;
					} else if (sub_step == 1) {
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(4));
						}
						sub_step = 2;
					} else if (sub_step == 2) {
						int[] surf = new int[] { 3515, 3515, 3515 };
						int[] dialogs = new int[] { 2070, 2071, 2072 };
						QUEST_MSG(surf, dialogs);
						sub_step = 3;
					} else if (sub_step == 3) {

						enterDoor.open();
						// eyeGate.setDead(true);
						eyeGate.open();
						sub_step = 4;

						GeneralThreadPool.getInstance().schedule(this, 800);
						return;
						// eyeGate.setOpenStatus(28);
					} else if (sub_step == 4) {
						sub_step = 5;
						GeneralThreadPool.getInstance().schedule(this, 800);
						return;
					} else if (sub_step == 5) {

						// eyeGate.deleteMe();
						time = gametime;
						for (L1PcInstance pc : getPlayMemberArray()) {
							SC_ARENA_PLAY_STATUS_NOTI.time_send(pc, gametime + 3, true);
						}
						start = true;
						GREEN_MSG("誰敢打擾我們的儀式？");
						GREEN_MSG("確認一下你是否有資格打擾我們。");
						step = 1;

					}
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				case 1:
					// System.out.println("누가: "+step+" 추가단계: "+sub_step1);
					// System.out.println(spawnCnt+"+"+firstMonsterList);
					if (sub_step1 == 0) {
						firstMonsterCheck();
						if (spawnCnt >= FIRST_CNT && firstMonsterList != null && firstMonsterList.isEmpty()) {
							firstMonsterList = null;
							sub_step1 = 1;
							GeneralThreadPool.getInstance().schedule(this, 5000);
							return;
						}
						GeneralThreadPool.getInstance().schedule(this, 1000);
						return;
					} else if (sub_step1 == 1) {
						GREEN_MSG("黑暗守望者，去處理那些傢伙。");
						// System.out.println("감時자 소환");
						darkWatch = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32851, 32826, (short) _mapnum, 7800305, 0,
								0, 0);
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(SC_SCENE_NOTI.make_stream(true, System.currentTimeMillis(), 73600000,
									darkWatch.getX(), darkWatch.getY()), true);
						}
						// System.out.println("監視者召喚完成");
						sub_step1 = 2;
					} else if (sub_step1 == 2) {
						// System.out.println(darkWatch+"+"+darkWatch.isDead());
						if (darkWatch.isDead() && darkWatch != null) {
							step = 2;
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				case 2:
					// System.out.println("단계: "+step+" 추가단계: "+sub_step2);
					if (sub_step2 == 0) {
						spawnCnt = 0;
						sub_step2 = 1;
					} else if (sub_step2 == 1) {
						GREEN_MSG("讓你見識一下黑暗守望者的真面目。");
						sub_step2 = 2;
					} else if (sub_step2 == 2) {
						darkTransWatch = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32851, 32826, (short) _mapnum,
								7800306, 0, 0, 0);
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(SC_SCENE_NOTI.make_stream(true, System.currentTimeMillis(), 73600000,
									darkTransWatch.getX(), darkTransWatch.getY()), true);
						}
						GREEN_MSG("沒想到會讓你看到我的真面目。");
						sub_step2 = 3;
					} else if (sub_step2 == 3) {
						if (darkTransWatch.isDead() && darkTransWatch != null) {
							step = 3;
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				case 3:
					// System.out.println("階段: " + step + " 添加階段: " + sub_step3);
					if (sub_step3 == 0) {
						stoneActive(true);
						arrestedAurakia = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32851, 32826, (short) _mapnum,
								7800300, 0, 0, 0);
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(SC_SCENE_NOTI.make_stream(true, System.currentTimeMillis(), 73600004,
									arrestedAurakia.getX() + 5, arrestedAurakia.getY() - 5), true);
						}
						arrestedAttackCnt = 0;
						step = 4;
					}
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				case 4:
					// System.out.println("階段: " + step + " 添加階段: " + sub_step4);
					if (sub_step4 == 0) {
						if (stone_1.getOpenStatus() == ActionCodes.ACTION_Close
								&& stone_2.getOpenStatus() == ActionCodes.ACTION_Close
								&& stone_3.getOpenStatus() == ActionCodes.ACTION_Close
								&& stone_4.getOpenStatus() == ActionCodes.ACTION_Close) {
							stoneActive(false);
							sub_step4 = 1;
						}
					} else if (sub_step4 == 1) {
						Timestamp deltime = new Timestamp(System.currentTimeMillis() + time * 1000);
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.getInventory().storeItem(420122, 1, 0, deltime); // 剩餘時間後刪除（失敗時）
						}
						GREEN_MSG("在背包中生成阿烏拉基亞的箭矢。.");
						GREEN_MSG("請用阿烏拉基亞的箭矢解除對阿烏拉基亞的束縛。");
						sub_step4 = 2;

					} else if (sub_step4 == 2) {
						circleCheck();
						arrestedCheck();
						if (arrestedAurakia != null && arrestedAurakia.isDead()) {
							step = 5;
							stoneActive(false);
							monsterGateOff();
							arrestedDestroy();
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 1000);
					return;
				case 5:
					// System.out.println("階段: " + step + " 添加階段: " + sub_step5);
					if (sub_step5 == 0) {
						sub_step5 = 1;
						GeneralThreadPool.getInstance().schedule(this, 5000);
						return;
					} else if (sub_step5 == 1) {
						GREEN_MSG("不... 阿烏拉基亞的束縛被解除了...");
						sub_step5 = 2;
						GeneralThreadPool.getInstance().schedule(this, 15000);
						return;
					} else if (sub_step5 == 2) {
						GREEN_MSG("10秒後將傳送到村莊。");
						step = 6;
						GeneralThreadPool.getInstance().schedule(this, 5000);
						return;
					}

					break;
				case 6:
					// System.out.println("階段: " + step + " 添加階段: " + sub_step6);
					if (sub_step6 == 0) {
						GREEN_MSG("10秒後將傳送到村莊。");
						sub_step6 = 1;
						GeneralThreadPool.getInstance().schedule(this, 5000);
						return;
					} else if (sub_step6 == 1) {
						quit();
						return;
					}
					return;
				case 10:
					if (sub_step10 == 0) {
						GREEN_MSG("果然，你們還不是我的對手。");
						sub_step10 = 1;
					} else if (sub_step10 == 1) {
						GREEN_MSG("稍後將傳送到村莊。.");
						sub_step10 = 2;
					} else if (sub_step10 == 2) {
						quit();
					}
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				default:
					break;
			}
		} catch (Exception e) {
		}
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}

	public int getMapId() {
		return _mapnum;
	}

	private void sendEffect(String msg) {
		L1PcInstance[] temp = getPlayMemberArray();
		for (L1PcInstance pc : temp) {
			pc.sendPackets(SC_SCENE_NOTI.make_stream(msg), true);
		}
		temp = null;
	}

	private void firstMonsterCheck() {
		if (firstMonsterList == null) {
			return;
		}
		for (int i = 0; i < firstMonsterList.size(); i++) {
			L1NpcInstance npc = firstMonsterList.get(i);
			if (npc != null && npc.isDead()) {
				firstMonsterList.remove(npc);
			}
		}
	}

	private void quit() {
		System.out.println("副本結束地圖編號： " + _mapnum);
		MJIndunRoomController.getInstance().clear_room_info(_model);
		MJIndunRoomController.getInstance().reload_room_info();

		for (L1PcInstance pc : getPlayMemberArray()) {
			MJIndunRoomController.getInstance().end_user_room(pc, -1);
			_model.onquit(pc);
		}

		HOME_TELEPORT();
		Object_Delete();
		clearPlayMember();
		AurakiaInDungeon.getInstance().Reset(_mapnum);
		_close = true;
	}

	private void QUEST_MSG(int[] surf, int[] dialogs) {
		for (L1PcInstance pc : getPlayMemberArray()) {
			if (pc == null)
				continue;
			SC_DIALOGUE_MESSAGE_NOTI.quest_send(pc, surf, dialogs);
		}
	}

	private void GREEN_MSG(String msg) {
		for (L1PcInstance pc : getPlayMemberArray()) {
			try {
				if (pc == null)
					continue;
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void HOME_TELEPORT() {
		for (L1PcInstance pc : getPlayMemberArray()) {
			try {
				if (pc == null)
					continue;
				pc.start_teleport(33436 + rnd.nextInt(12), 32795 + rnd.nextInt(14), 4, 5, 18339, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

	public void addPlayMember(L1PcInstance pc) {
		playmember.add(pc);
	}

	public int getPlayMembersCount() {
		return playmember.size();
	}

	public void removePlayMember(L1PcInstance pc) {
		playmember.remove(pc);
	}

	public void clearPlayMember() {
		playmember.clear();
	}

	public boolean isPlayMember(L1PcInstance pc) {
		return playmember.contains(pc);
	}

	public L1PcInstance[] getPlayMemberArray() {
		return (L1PcInstance[]) playmember.toArray(new L1PcInstance[getPlayMembersCount()]);
	}

	private void home_teleport() {
		if (step == 6 && sub_step6 >= 9) {
			for (L1PcInstance pc : getPlayMemberArray()) {
				pc.start_teleport(33436 + rnd.nextInt(12), 32795 + rnd.nextInt(14), 4, 5, 18339, true);
			}
		}
	}

	private void checkMember() {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
			if (ob instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) ob;
				if (!isPlayMember(pc)) {
					addPlayMember(pc);
					SC_ARENA_GAME_INFO_NOTI.send_info(pc, gametime + 3);
				}
			}
		}

		for (L1PcInstance member : getPlayMemberArray()) {
			try {
				L1Party party = new L1Party();
				if (((member == null ? 1 : 0) | (member.getMapId() != _mapnum ? 1 : 0)) != 0) {
					MJIndunRoomController.getInstance().end_user_room(member, -1);
					removePlayMember(member);
					SC_ARENA_PLAY_STATUS_NOTI.end_time_send(member);
					if (member.getParty() != null) {
						member.getParty().leaveMember(member);
					}
				}

				if (getPlayMembersCount() > 1) {
					if (member.getParty() == null) {
						party.addMember(member);
					} else {
						party = member.getParty();
					}

					for (L1PcInstance Targetpc : L1World.getInstance().getVisiblePlayer(member)) {
						if (member.getName().equals(Targetpc.getName())) {
							continue;
						}
						if (Targetpc.getMapId() != _mapnum) {
							continue;
						}

						if (Targetpc.getParty() != null) {
							continue;
						}
						if (Targetpc.isPrivateShop()) {
							continue;
						}
						party.addMember(Targetpc);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void end_check() {
		if (getPlayMembersCount() <= 0) {
			step = 10;
		}

		if (start && time-- < 0) {
			for (L1PcInstance member : getPlayMemberArray()) {
				SC_ARENA_PLAY_STATUS_NOTI.end_time_send(member);
			}
			step = 10;
		}

		if (((step == 1 && spawnCnt < FIRST_CNT) || (step == 4 && spawnCnt < GATE_CNT)) && time % 10 == 0) {
			monsterGateAction();
		}
		if (step == 4 && sub_step4 == 2 && time % 15 == 0) {
			circleAction();
		}
	}

	private void circleAction() {
		if (circle_1 != null && circle_2 != null && circle_3 != null && circle_4 != null) {
			circle_1.deleteMe();
			circle_2.deleteMe();
			circle_3.deleteMe();
			circle_4.deleteMe();
			circle_5.deleteMe();
			circleCheck();
		}
		circle_1 = L1SpawnUtil.spawnnpc(32845, 32819, (short) _mapnum, 7800320, 5, 13000, 0);
		circle_2 = L1SpawnUtil.spawnnpc(32841, 32830, (short) _mapnum, 7800320, 5, 13000, 0);
		circle_3 = L1SpawnUtil.spawnnpc(32848, 32836, (short) _mapnum, 7800320, 5, 13000, 0);
		circle_4 = L1SpawnUtil.spawnnpc(32856, 32833, (short) _mapnum, 7800320, 5, 13000, 0);
		circle_5 = L1SpawnUtil.spawnnpc(32851, 32826, (short) _mapnum, 7800320, 14, 13000, 0);
	}

	private void deleteCircle() {
		circle_1.deleteMe();
		circle_2.deleteMe();
		circle_3.deleteMe();
		circle_4.deleteMe();
		circle_5.deleteMe();
	}

	private void circleCheck() {
		if (circle_1 == null || circle_2 == null || circle_3 == null || circle_4 == null || circle_5 == null) {
			return;
		}
		for (L1PcInstance pc : getPlayMemberArray()) {
			if (pc.getX() == circle_1.getX() && pc.getY() == circle_1.getY()) {
				pc.setAurakiaCircle(true);
			} else if (pc.getX() == circle_2.getX() && pc.getY() == circle_2.getY()) {
				pc.setAurakiaCircle(true);
			} else if (pc.getX() == circle_3.getX() && pc.getY() == circle_3.getY()) {
				pc.setAurakiaCircle(true);
			} else if (pc.getX() == circle_4.getX() && pc.getY() == circle_4.getY()) {
				pc.setAurakiaCircle(true);
			} else if (pc.getX() == circle_5.getX() && pc.getY() == circle_5.getY()) {
				pc.setAurakiaCircle(true);
			} else {
				pc.setAurakiaCircle(false);
			}
		}
	}

	private void arrestedCheck() {
		if (arrestedLevel < 1 && arrestedAttackCnt >= ARRESTED_CNT) {
			arrestedAction(73600005);
		} else if (arrestedLevel < 2 && arrestedAttackCnt >= ARRESTED_CNT * 2) {
			arrestedAction(73600006);
		} else if (arrestedLevel < 3 && arrestedAttackCnt >= ARRESTED_CNT * 3) {
			arrestedAction(73600007);
		} else if (arrestedLevel < 4 && arrestedAttackCnt >= ARRESTED_CNT * 4) {
			arrestedAction(0);
		}
	}

	private void arrestedAction(int scriptId) {
		if (scriptId > 0) {
			arrestedLevel++;
			for (L1PcInstance pc : getPlayMemberArray()) {
				pc.sendPackets(SC_SCENE_NOTI.make_stream(true, System.currentTimeMillis(), scriptId,
						arrestedAurakia.getX() + 5, arrestedAurakia.getY() - 5), true);// 約束球影像輸出
			}
			arrestedAurakia.broadcastPacket(new S_DoActionGFX(arrestedAurakia.getId(), ActionCodes.ACTION_SkillBuff),
					true);
		} else {
			arrestedLevel++;
			arrestedAurakia.broadcastPacket(new S_DoActionGFX(arrestedAurakia.getId(), ActionCodes.ACTION_SkillBuff),
					true);
			arrestedAurakia.setCurrentHp(0);
			arrestedAurakia.setDead(true);
		}
	}

	private void monsterGateAction() {
		FastTable<L1NpcInstance> monList = null;
		// System.out.println(gatecnt+"+"+gate1Active+"+"+gate2Active+"+"+gate3Active+"+"+gate4Active);

		if (gate1Active == true || gate2Active == true || gate3Active == true || gate4Active == true) {
			if (gate1Active) {// 1時
				monList = AurakiaInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 1);
				monsterGate_1.setStatus(ActionCodes.ACTION_Die);
				monsterGate_1.broadcastPacket(new S_DoActionGFX(monsterGate_1.getId(), ActionCodes.ACTION_Die), true);
				gate1Active = false;
			} else if (gate2Active) {// 4時
				monList = AurakiaInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 2);
				monsterGate_2.setStatus(ActionCodes.ACTION_Die);
				monsterGate_2.broadcastPacket(new S_DoActionGFX(monsterGate_2.getId(), ActionCodes.ACTION_Die), true);
				gate2Active = false;
			} else if (gate3Active) {// 7時
				monList = AurakiaInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 3);
				monsterGate_3.setStatus(ActionCodes.ACTION_Die);
				monsterGate_3.broadcastPacket(new S_DoActionGFX(monsterGate_3.getId(), ActionCodes.ACTION_Die), true);
				gate3Active = false;
			} else if (gate4Active) {// 10時
				monList = AurakiaInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 4);
				monsterGate_4.setStatus(ActionCodes.ACTION_Die);
				monsterGate_4.broadcastPacket(new S_DoActionGFX(monsterGate_4.getId(), ActionCodes.ACTION_Die), true);
				gate4Active = false;
			}
			gatecnt++;
		}

		if (step == 1 && firstMonsterList != null) {
			firstMonsterList.addAll(monList);
		}
		if (gatecnt >= 4) {
			if (gatecnt % 4 == 0) {
				spawnCnt++;
			}
		}

		if (gatecnt >= 4 && ((step == 1 && spawnCnt < FIRST_CNT) || (step == 4 && spawnCnt < GATE_CNT))) {
			int ran = CommonUtil.random(3);
			gateNumber = ran;
			// System.out.println(gateNumber);
			switch (gateNumber) {
				case 0:
					monsterGate_1 = (L1NpcInstance) L1SpawnUtil.spawnnpc(32863, 32829, (short) _mapnum, 7800308, 0,
							10 * 1000, 0);// 怪物之門1點
					gate1Active = true;
					break;
				case 1:
					monsterGate_2 = (L1NpcInstance) L1SpawnUtil.spawnnpc(32848, 32840, (short) _mapnum, 7800309, 0,
							10 * 1000, 0);// 怪物之門4點鐘方向
					gate2Active = true;
					break;
				case 2:
					monsterGate_3 = (L1NpcInstance) L1SpawnUtil.spawnnpc(32837, 32829, (short) _mapnum, 7800310, 0,
							10 * 1000, 0);// 怪物之門7點鐘方向
					gate3Active = true;
					break;
				case 3:
					monsterGate_4 = (L1NpcInstance) L1SpawnUtil.spawnnpc(32848, 32814, (short) _mapnum, 7800311, 0,
							10 * 1000, 0);// 怪物之門 11點
					gate4Active = true;
					break;
				default:
					break;
			}
		}
	}

	// 雕像啟動
	private void stoneActive(boolean active) {
		if (active) {
			stone_1.open();
			stone_2.open();
			stone_3.open();
			stone_4.open();
		} else {
			stone_1.close();
			stone_2.close();
			stone_3.close();
			stone_4.close();
		}
	}

	private void monsterGateOff() {
		monsterGate_1.setStatus(ActionCodes.ACTION_Die);
		monsterGate_2.setStatus(ActionCodes.ACTION_Die);
		monsterGate_3.setStatus(ActionCodes.ACTION_Die);
		monsterGate_4.setStatus(ActionCodes.ACTION_Die);
		monsterGate_1.broadcastPacket(new S_DoActionGFX(monsterGate_1.getId(), ActionCodes.ACTION_Die), true);
		monsterGate_2.broadcastPacket(new S_DoActionGFX(monsterGate_2.getId(), ActionCodes.ACTION_Die), true);
		monsterGate_3.broadcastPacket(new S_DoActionGFX(monsterGate_3.getId(), ActionCodes.ACTION_Die), true);
		monsterGate_4.broadcastPacket(new S_DoActionGFX(monsterGate_4.getId(), ActionCodes.ACTION_Die), true);
	}

	private void spawn_door() {
		enterDoor = (L1DoorInstance) L1SpawnUtil.spawnnpc(32816, 32829, (short) _mapnum, 7800314, 0, 0, 0);
		eyeGate = (L1DoorInstance) L1SpawnUtil.spawnnpc(32812, 32830, (short) _mapnum, 7800315, 0, 0, 0);// 眼球之門
		monsterGate_1 = (L1NpcInstance) L1SpawnUtil.spawnnpc(32863, 32829, (short) _mapnum, 7800308, 0, time * 1000, 0);// 怪物
																														// 門
																														// 1點
		gate1Active = true;
		monsterGate_2 = (L1NpcInstance) L1SpawnUtil.spawnnpc(32848, 32840, (short) _mapnum, 7800309, 0, time * 1000, 0);// 怪物
																														// 門
																														// 4點
		gate2Active = true;
		monsterGate_3 = (L1NpcInstance) L1SpawnUtil.spawnnpc(32837, 32829, (short) _mapnum, 7800310, 0, time * 1000, 0);// 怪物
																														// 門
																														// 7點
		gate3Active = true;
		monsterGate_4 = (L1NpcInstance) L1SpawnUtil.spawnnpc(32848, 32814, (short) _mapnum, 7800311, 0, time * 1000, 0);// 怪物
																														// 門
																														// 11點
		gate4Active = true;
		stone_1 = (L1DoorInstance) L1SpawnUtil.spawnnpc(32862, 32817, (short) _mapnum, 7800316, 0, 0, 0);// 石像12點
		stone_2 = (L1DoorInstance) L1SpawnUtil.spawnnpc(32859, 32835, (short) _mapnum, 7800317, 0, 0, 0);// 石像3點鐘方向
		stone_3 = (L1DoorInstance) L1SpawnUtil.spawnnpc(32840, 32837, (short) _mapnum, 7800318, 0, 0, 0);// 6點鐘
		stone_4 = (L1DoorInstance) L1SpawnUtil.spawnnpc(32842, 32817, (short) _mapnum, 7800319, 0, 0, 0);// 9點鐘方向的石像
	}

	private void spawn_egg() {
		AurakiaInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 5);
	}

	// 破壞束縛
	private void arrestedDestroy() {
		for (L1PcInstance pc : getPlayMemberArray()) {
			pc.sendPackets(SC_SCENE_NOTI.make_stream(false, 0, 73600004, 0, 0), true); // ARRESTED_END_1
			pc.sendPackets(SC_SCENE_NOTI.make_stream(false, 0, 73600005, 0, 0), true); // ARRESTED_END_2
			pc.sendPackets(SC_SCENE_NOTI.make_stream(false, 0, 73600006, 0, 0), true); // ARRESTED_END_3
			pc.sendPackets(SC_SCENE_NOTI.make_stream(false, 0, 73600007, 0, 0), true); // ARRESTED_END_4
			pc.sendPackets(SC_SCENE_NOTI.make_stream(true, System.currentTimeMillis(), 73600002, 0, 0), true); // 地圖變化
			pc.sendPackets(SC_SCENE_NOTI.make_stream(false, 0, 73600001, 0, 0), true); // FOF_OFF
			// pc.sendPackets(S_GfxMessage.AURAKIA_END_MENT); // 註釋
			pc.getInventory().consumeItem(420122); // 移除 아우라키아 箭
			if (pc.isDead()) {
				continue; // 排除獎勵
			}
			pc.getInventory().storeItem(1000007, 1); // 添加 1 個 드래곤의 고급다이아
			// pc.einGetExcute(300); // 아인하사드 獎勵
			pc.getInventory().storeItem(420120, 1); // 添加 1 個 아우라키아의 선물
		}

		GREEN_MSG(""); // 顯示綠色消息

		arrestedAurakia.broadcastPacket(new S_SkillSound(arrestedAurakia.getId(), 20364), true);
		arrestedAurakia.deleteMe();

		// 記憶體清理
		arrestedAurakia = null;
	}

	public static void increaseArrestedAttackCount() {
		arrestedAttackCnt++;
	}

	public int getArrestedAttackCount() {
		return arrestedAttackCnt;
	}

	public static void useArrow(L1PcInstance pc, L1MonsterInstance target) {
		long time1 = System.currentTimeMillis();
		if (time1 - oldtime <= 2000) {// 재사용時間 2초
			return;
		}
		if (target == null) {
			return;
		}

		// System.out.println(pc.isAurakiaCircle());
		if (!pc.isAurakiaCircle()) {
			pc.sendPackets(new S_SystemMessage("只能在魔法陣上使用。."));
			return;
		}

		pc.sendPackets(new S_UseAttackSkill(pc, target.getId(), 20336, target.getX(), target.getY(), 0, false), false);
		Broadcaster.broadcastPacket(pc,
				(new S_UseAttackSkill(pc, target.getId(), 20336, target.getX(), target.getY(), 0, false)));
		target.send_effect(20338);
		increaseArrestedAttackCount();
		oldtime = time1;
	}

}
