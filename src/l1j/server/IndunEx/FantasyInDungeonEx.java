package l1j.server.IndunEx;

import static l1j.server.server.model.skill.L1SkillId.ADVANCE_SPIRIT;
import static l1j.server.server.model.skill.L1SkillId.IRON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;
import static l1j.server.server.model.skill.L1SkillId.SKILLS_END;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.IndunEx.PlayInfo.FantasyInDungeon;
import l1j.server.IndunEx.PlayInfo.FantasyInDungeonSpawn;
import l1j.server.IndunEx.RoomInfo.MJIndunRoomController;
import l1j.server.IndunEx.RoomInfo.MJIndunRoomMemberModel;
import l1j.server.IndunEx.RoomInfo.MJIndunRoomModel;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_DIALOGUE_MESSAGE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo.SC_ARENA_GAME_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo.SC_ARENA_PLAY_STATUS_NOTI;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

public class FantasyInDungeonEx implements Runnable {

	private Random rnd = new Random(System.currentTimeMillis());
	private final ArrayList<L1PcInstance> playmember = new ArrayList<L1PcInstance>();
	private int _mapnum = 0;
	private boolean start = false;
	private int time = 0;
	private boolean timer = false;
	private int gametime = 60 * 15;

	private int step = 0;
	private int sub_step = 0;
	private int sub_step2 = 0;
	private int sub_step3 = 0;
	private int sub_step4 = 0;
	private int sub_step5 = 0;
	private int sub_step7 = 0;
	private int sub_step8 = 0;

	private boolean elementalResult = true;
	private boolean WaterElementalResult = true;
	private boolean WindElementalResult = true;
	private boolean FireElementalResult = true;
	private boolean EarthElementalResult = true;

	private ArrayList<L1NpcInstance> WaterMinionList;
	private ArrayList<L1NpcInstance> FireMinionList;
	private ArrayList<L1NpcInstance> EarthMinionList;
	private ArrayList<L1NpcInstance> WindMinionList;
	private L1NpcInstance unicon1;
	private L1NpcInstance circle;

	private L1MonsterInstance unicon2 = null;
	private L1MonsterInstance nightmare = null;
	private L1MonsterInstance sucubus = null;
	private L1NpcInstance water = null;
	private L1NpcInstance fire = null;
	private L1NpcInstance earth = null;
	private L1NpcInstance wind = null;

	private MJIndunRoomModel _model;

	private boolean waterdead = false;
	private boolean firedead = false;
	private boolean earthdead = false;
	private boolean winddead = false;

	private int current_point = 0;
	private static final int SUCUBUS_POINT = 80; // 召喚夢幻魅魔所需積分
	private static final String[] MENT_ARRAY = {
		"我以為被你打倒的機率連1成都不到.. 我的錯誤。", // 31901
		"等了你三天兩夜。終於見到你了。",
		"你這傢伙…！好像在傲慢之塔3層見過！",
		"被沒用四階屬性強化的你這傢伙的刀打倒… 真是氣人！！",
		"殺了我你只能得到5張瞬間移動卷軸。",
		"看到你手拿日本刀喜悅的樣子。這是你的過去嗎…",
		"帶著騎士團防具來挑戰我，真是井底之蛙。",
		"你們8個人一起上吧。覺得能打倒我嗎。",
		"居然試圖用騎士團的匕首來打倒我… 真是可笑。",
		"刀劍無情。可你連享受那情分的資格都沒有。",
		"去第11條街把你的裝備升級再來吧。",
		"在12點之前趕緊滾吧！",
		"13年前，奪走我藥水的人就是你！",
		"14年後，你依然會在這裡。",
		"這個地方，即使花上15億也會成為窮光蛋。記住這點！",
		"你以為能對付精通16進制的我嗎。",
		"你能安全離開這裡的機率只有17%。",
		"18..."
		"第一次见到你的时候，你还是个19岁的少年。这段时间你长大了不少。哈哈哈。",
		"睁大眼睛看看吧。为了这个时刻，我坚持了20年！",
		"等了21年。只为再次见到你这家伙...",
		"你这家伙比22元的药水还微不足道...",
		"已经23点了。和你玩到此为止。",
		"24节气都过完了，你还是没有进步啊。",
		"看你这可怜的身体，连体质25都没加满吧。",
		"你能见到我的概率是26%。尽力试试吧。",
		"你的ER连27都没有吧。看你这家伙，一箭就能被打败...",
		"向我献出生命的人已经超过28万了...",
		"你是第29万个献出灵魂的人吗！",
		"想想30级是封顶的时代吧！",
		"听好了，31秒后，你会后悔现在没逃跑。",
		"再见了。拿着连32点伤害都打不出的刀来挑战我...",
		"只要有33个精灵跟随我，这个地方就不会倒下。",
		"34街的奇迹般的奇迹不会有...",
		"被我夺走的35个药水，你这段时间都做了什么。",
		"哈哈哈。36计走为上计。最后一次机会！",
		"闭上眼睛看看吧。你看到了吗。未来37年你将经历的事情。",
		"你打败我的概率就像38光明的概率一样。",
		"如果收购你的灵魂，大概值39元吧...？",
		"40岁不惑之年居然还如此迷惑。真是可笑。",
		"曾经是清爽青年的你已经41岁了...岁月真无情。",
		"该去阴间了！一切都4.2好！",
		"认证号码是43... 是时候变强了！",
		"嘖嘖.. 命中率连44都没有的家伙竟敢挑战我！",
		"呃... 竟然被45点伤害打倒了..."
	};

	private boolean _close = false;

	L1NpcInstance[] _firewall = new L1NpcInstance[12];

	public void Start() {
		GeneralThreadPool.getInstance().execute(this);
	}

	public FantasyInDungeonEx(int mapid, int price, MJIndunRoomModel model) {
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
				member_model.member.getInventory().consumeItem(i);// 아우라키아의 화살 삭제
			}

			if (member_model.member.getInventory().checkItem(420122)) {
				member_model.member.getInventory().consumeItem(420122);// 아우라키아의 화살 삭제
			}

			MJPoint pt = MJPoint.newInstance(32770, 32762, 5, (short) mapid, 50);
			member_model.member.start_teleport(pt.x, pt.y, pt.mapId, member_model.member.getHeading(), 18339, false);
		}

		unicon1 = L1SpawnUtil.spawn4(32768, 32764, (short) mapid, 4, 7800200, 0, 0, 0);// 속박된 유니콘

		FireMinionList = new ArrayList<L1NpcInstance>();
		WaterMinionList = new ArrayList<L1NpcInstance>();
		WindMinionList = new ArrayList<L1NpcInstance>();
		EarthMinionList = new ArrayList<L1NpcInstance>();

		for (int i = 0; i < 3; i++) {
			_firewall[i] = L1SpawnUtil.spawnnpc(32779, 32766 + i, (short) _mapnum, 50000221, 0, 0, 0);
		}

		for (int i = 3; i < 6; i++) {
			_firewall[i] = L1SpawnUtil.spawnnpc(32767 + i - 3, 32778, (short) _mapnum, 50000221, 0, 0, 0);
		}

		for (int i = 6; i < 9; i++) {
			_firewall[i] = L1SpawnUtil.spawnnpc(32757, 32766 + i - 6, (short) _mapnum, 50000221, 0, 0, 0);
		}

		for (int i = 9; i < 12; i++) {
			_firewall[i] = L1SpawnUtil.spawnnpc(32766 + i - 9, 32752, (short) _mapnum, 50000221, 0, 0, 0);
		}

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
				home_teleport();

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
		// TODO 自动生成的方法桩
		try {
			if (_close)
				return;
			switch (step) {
				case 0:
					// System.out.println("step: "+step+"sub_step: "+sub_step);
					if (sub_step == 0) {
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(4));
						}
						sub_step = 1;
					} else if (sub_step == 1) {// Mongseom開始訊息框
						int[] surf = new int[] { 22997, 22997, 23014, 23014 };
						int[] dialogs = new int[] { 2026, 2027, 2028, 2029 };
						QUEST_MSG(surf, dialogs);
						sub_step = 2;
					} else if (sub_step == 2) {
						step = 1;
					}

					GeneralThreadPool.getInstance().schedule(this, 7000);
					return;
				// break;

				case 1: // wait_step
					// System.out.println("step: "+step);
					time = gametime;

					for (L1PcInstance pc : getPlayMemberArray()) {
						SC_ARENA_PLAY_STATUS_NOTI.time_send(pc, gametime + 3, true);
					}
					start = true;
					GREEN_MSG("請擊敗各屬性房間中的大精靈");
					step = 2;
					GeneralThreadPool.getInstance().schedule(this, 7000);
					return;
				case 2:// BOSS_STEP
					// System.out.println("step: "+step+"sub_step2: "+sub_step2);
					if (sub_step2 == 0) {
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(6));
						}
						water = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32741, 32767, (short) _mapnum, 7800202, 0, 0,
								0);
						fire = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32794, 32766, (short) _mapnum, 7800204, 0, 0,
								0);
						earth = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32768, 32738, (short) _mapnum, 7800208, 0, 0,
								0);
						wind = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32767, 32794, (short) _mapnum, 7800206, 0, 0,
								0);
						sub_step2 = 1;
					} else if (sub_step2 == 1) {
						for (int wd = 0; wd < 12; wd++) {
							if (_firewall[wd] != null) {
								_firewall[wd].deleteMe();
							}
						}
						sub_step2 = 2;
					} else if (sub_step2 == 2) {
						checkElemental();
						if (elementalResult) {// 成功擊敗大靈
							/*
							 * if (water.isDead() && water !=null && fire.isDead() && fire != null
							 * && earth.isDead() && earth != null && wind.isDead() && wind != null){
							 */
							if (waterdead && firedead && earthdead && winddead) {
								sub_step2 = 3;
							}
						} else if (!elementalResult) {// 未能殺死大靈
							WindMinionCheck();
							WaterMinionCheck();
							FireMinionCheck();
							EarthMinionCheck();
							if (!WindElementalResult) {
								if (WindMinionList != null && WindMinionList.isEmpty()) {
									WindElementalResult = true;
								}
							}
							if (!WaterElementalResult) {
								if (WaterMinionList != null && WaterMinionList.isEmpty()) {
									WaterElementalResult = true;
								}
							}
							if (!FireElementalResult) {
								if (FireMinionList != null && FireMinionList.isEmpty()) {
									FireElementalResult = true;
								}
							}
							if (!EarthElementalResult) {
								if (EarthMinionList != null && EarthMinionList.isEmpty()) {
									EarthElementalResult = true;
								}
							}
							if (WaterElementalResult && WindElementalResult && FireElementalResult
									&& EarthElementalResult) {
								sub_step2 = 3;
							}
						}
					} else if (sub_step2 == 3) {
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(4));
						}
						Thread.sleep(2000L);
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(4));
						}
						Thread.sleep(2000L);
						unicon1.deleteMe();// 刪除了拴著的獨角獸
						for (L1PcInstance pc : getPlayMemberArray()) { // 獨角獸召喚訊息框
							int[] surf = new int[] { 22906 };
							int[] dialogs = new int[] { 2030 };
							QUEST_MSG(surf, dialogs);
						}
						// 召喚獨角獸
						unicon2 = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32768, 32764, (short) _mapnum, 7800217, 0, 0,
								0);

						sub_step2 = 4;
					} else if (sub_step2 == 4) {
						if (unicon2.isDead() && unicon2 != null) {// 當獨角獸死去時
							int number = CommonUtil.random(50);
							current_point += number + 1;
							step = 3;
						}
					}

					current_point -= 1;
					GeneralThreadPool.getInstance().schedule(this, 2000);
					return;

				case 3: // LAST_STEP
					// System.out.println("step: "+step+"sub_step3: "+sub_step3);

					if (sub_step3 == 0) {
						for (L1PcInstance pc : getPlayMemberArray()) { // 獨角獸死亡訊息框
							int[] surf = new int[] { 22997 };
							int[] dialogs = new int[] { 2031 };
							QUEST_MSG(surf, dialogs);
						}

						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(4));
						}
						Thread.sleep(2000L);
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(4));
						}
						Thread.sleep(2000L);
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(4));
						}
						Thread.sleep(2000L);
						nightmare = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32768, 32764, (short) _mapnum, 7800218, 0,
								0, 0);
						unicon2 = null;
						sub_step3 = 1;
					} else if (sub_step3 == 1) {
						if (nightmare.isDead() && nightmare != null) {
							int number1 = CommonUtil.random(50);
							current_point += number1 + 1;
							sub_step3 = 2;
						}
					} else if (sub_step3 == 2) {

						for (L1PcInstance pc : getPlayMemberArray()) { // 惡夢死亡留言箱
							int[] surf = new int[] { 23015, 23015 };
							int[] dialogs = new int[] { 2032, 2033 };
							QUEST_MSG(surf, dialogs);
						}
						if (current_point >= SUCUBUS_POINT) {
							for (L1PcInstance pc : getPlayMemberArray()) {
								pc.sendPackets(S_DisplayEffect.newInstance(4));
							}
							Thread.sleep(2000L);
							for (L1PcInstance pc : getPlayMemberArray()) {
								pc.sendPackets(S_DisplayEffect.newInstance(4));
							}
							Thread.sleep(2000L);
							for (L1PcInstance pc : getPlayMemberArray()) {
								pc.sendPackets(S_DisplayEffect.newInstance(4));
							}
							Thread.sleep(2000L);
							sucubus = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32768, 32764, (short) _mapnum, 7800219,
									0, 0, 0);
							step = 4;
						} else {
							step = 5;
						}
					}
					current_point -= 1;
					GeneralThreadPool.getInstance().schedule(this, 10000);
					break;
				case 4: // EVENT_STEP
					if (sucubus.isDead() && sucubus != null) {
						for (L1PcInstance pc : getPlayMemberArray()) { // 魅魔死亡訊息箱
							int[] surf = new int[] { 22997, 22997 };
							int[] dialogs = new int[] { 2034, 2035 };
							QUEST_MSG(surf, dialogs);
						}
					}

					step = 5;
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				case 5: // END
					if (sub_step5 == 0) {
						GREEN_MSG("现在请回到你的世界去");
						sub_step5 = 1;
						GeneralThreadPool.getInstance().schedule(this, 1000);
						return;
					} else if (sub_step5 == 1) {
						for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
							if (ob instanceof L1MonsterInstance) {
								L1MonsterInstance npc = (L1MonsterInstance) ob;
								for (L1PcInstance pc : getPlayMemberArray()) {
									pc.sendPackets(new S_RemoveObject(npc));
									pc.removeKnownObject(npc);
								}
							}
						}
						circle = L1SpawnUtil.spawn4(32768, 32764, (short) _mapnum, 6, 50000262, 0, 0, 0);
						GeneralThreadPool.getInstance().schedule(this, 1000);
						sub_step5 = 2;
						return;
					} else if (sub_step5 == 2) {
						GREEN_MSG("30秒後將移動到村莊.");
						sub_step5 = 3;
						GeneralThreadPool.getInstance().schedule(this, 10000);
						return;
					} else if (sub_step5 == 3) {
						GREEN_MSG("20秒後將移動到村莊。.");
						sub_step5 = 4;
						GeneralThreadPool.getInstance().schedule(this, 10000);
						return;
					} else if (sub_step5 == 4) {
						GREEN_MSG("10秒後將移動到村莊。");
						step = 6;
						GeneralThreadPool.getInstance().schedule(this, 10000);
						return;
					}
					return;
				case 6:
					quit(); // 房間退出
					GeneralThreadPool.getInstance().schedule(this, 1000);
					return;
				case 7: // 時間限制
					if (sub_step7 == 0) {
						sub_step7 = 1;
					} else if (sub_step7 == 1) {
						GREEN_MSG("時空旅行即將結束。");
						sub_step7 = 2;
					} else if (sub_step7 == 2) {
						GREEN_MSG("請下次再挑戰。");
						sub_step7 = 3;
					} else if (sub_step7 == 3) {
						quit();
					}
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				case 8: // 隊員死亡
					if (sub_step8 == 0) {
						sub_step8 = 1;
					} else if (sub_step8 == 1) {
						GREEN_MSG("所有隊員已死亡。");
						sub_step8 = 2;
					} else if (sub_step8 == 2) {
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

	private void WaterMinionCheck() {
		if (WaterMinionList == null) {
			return;
		}
		for (int i = 0; i < WaterMinionList.size(); i++) {
			L1NpcInstance npc = WaterMinionList.get(i);
			if (npc != null && npc.isDead()) {
				WaterMinionList.remove(npc);
			}
		}
	}

	private void FireMinionCheck() {
		if (FireMinionList == null) {
			return;
		}
		for (int i = 0; i < FireMinionList.size(); i++) {
			L1NpcInstance npc = FireMinionList.get(i);
			if (npc != null && npc.isDead()) {
				FireMinionList.remove(npc);
			}
		}
	}

	private void EarthMinionCheck() {
		if (EarthMinionList == null) {
			return;
		}
		for (int i = 0; i < EarthMinionList.size(); i++) {
			L1NpcInstance npc = EarthMinionList.get(i);
			if (npc != null && npc.isDead()) {
				EarthMinionList.remove(npc);
			}
		}
	}

	private void WindMinionCheck() {
		if (WindMinionList == null) {
			return;
		}
		for (int i = 0; i < WindMinionList.size(); i++) {
			L1NpcInstance npc = WindMinionList.get(i);
			if (npc != null && npc.isDead()) {
				WindMinionList.remove(npc);
			}
		}
	}

	public int getMapId() {
		return _mapnum;
	}

	private void quit() {
		System.out.println("副本結束地圖編號 : " + _mapnum);
		MJIndunRoomController.getInstance().clear_room_info(_model);
		MJIndunRoomController.getInstance().reload_room_info();

		for (L1PcInstance pc : getPlayMemberArray()) {
			MJIndunRoomController.getInstance().end_user_room(pc, -1);
			_model.onquit(pc);
		}

		HOME_TELEPORT();
		Object_Delete();
		clearPlayMember();
		FantasyInDungeon.getInstance().Reset(_mapnum);
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

	public void startAllBuffThread() {
		new AllBuffThread().begin();
	}

	public class AllBuffThread implements Runnable {
		AllBuffThread() {
		}

		public void run() {
			try {
				ArrayList<Integer> buffList = null;
				buffList = getBuffList_1();
				Iterator<Integer> iter = buffList.iterator();
				while (iter.hasNext()) {
					int skillId = ((Integer) iter.next()).intValue();
					Collection<L1PcInstance> pcList = new ArrayList<L1PcInstance>();

					for (L1PcInstance pc : getPlayMemberArray()) {
						try {
							if (pc == null)
								continue;
							pcList.add(pc);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					for (L1PcInstance target : pcList) {
						if ((target != null) && (!target.isDead())) {
							int eachSkillId = skillId;
							target.setBuffnoch(1);
							if (eachSkillId <= SKILLS_END) {
								new L1SkillUse().handleCommands(target, eachSkillId, target.getId(), target.getX(),
										target.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
							} else {
								L1BuffUtil.useBuff(target, eachSkillId);
							}
							target.setBuffnoch(0);
						}
					}
					Thread.sleep(1000L);
				}
			} catch (Exception localException) {
			}
		}

		private void begin() {
			GeneralThreadPool.getInstance().execute(this);
		}
	}

	private ArrayList<Integer> getBuffList_1() {
		ArrayList<Integer> buffList = new ArrayList<Integer>();
		buffList.add(Integer.valueOf(IRON_SKIN));
		buffList.add(Integer.valueOf(PHYSICAL_ENCHANT_DEX));
		buffList.add(Integer.valueOf(PHYSICAL_ENCHANT_STR));
		buffList.add(Integer.valueOf(ADVANCE_SPIRIT));
		return buffList;
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
		if (circle != null) {
			for (L1PcInstance pc : getPlayMemberArray()) {
				if (pc.getX() == circle.getX() && pc.getY() == circle.getY()) {
					pc.start_teleport(33436 + rnd.nextInt(12), 32795 + rnd.nextInt(14), 4, 5, 18339, true);
				}
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

	private void checkElemental() {

		if (water != null && water.isDead()) {
			int number = CommonUtil.random(45);
			GREEN_MSG(MENT_ARRAY[number]);
			water = null;
			waterdead = true;
		}
		if (fire != null && fire.isDead()) {
			int number = CommonUtil.random(45);
			GREEN_MSG(MENT_ARRAY[number]);
			fire = null;
			firedead = true;
		}
		if (wind != null && wind.isDead()) {
			int number = CommonUtil.random(45);
			GREEN_MSG(MENT_ARRAY[number]);
			wind = null;
			winddead = true;
		}
		if (earth != null && earth.isDead()) {
			int number = CommonUtil.random(45);
			GREEN_MSG(MENT_ARRAY[number]);
			// _util.sendPacket(_pclist, new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
			// MENT_ARRAY[number]), true);
			earth = null;
			earthdead = true;
		}

	}

	private void failElemental() {
		FastTable<L1NpcInstance> waterList = null;
		FastTable<L1NpcInstance> windList = null;
		FastTable<L1NpcInstance> fireList = null;
		FastTable<L1NpcInstance> earthList = null;
		if (water != null && !water.isDead()) {
			waterList = FantasyInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 1);
			/*
			 * water.deleteMe();
			 * water = null;
			 */
			WaterElementalResult = false;
			if (WaterMinionList != null) {
				WaterMinionList.addAll(waterList);
			}
		}
		if (fire != null && !fire.isDead()) {
			fireList = FantasyInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 2);
			/*
			 * fire.deleteMe();
			 * fire = null;
			 */
			FireElementalResult = false;
			if (FireMinionList != null) {
				FireMinionList.addAll(fireList);
			}
		}
		if (wind != null && !wind.isDead()) {
			windList = FantasyInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 3);
			/*
			 * wind.deleteMe();
			 * wind = null;
			 */
			WindElementalResult = false;
			if (WindMinionList != null) {
				WindMinionList.addAll(windList);
			}

		}
		if (earth != null && !earth.isDead()) {
			earthList = FantasyInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 4);
			/*
			 * earth.deleteMe();
			 * earth = null;
			 */
			EarthElementalResult = false;
			if (EarthMinionList != null) {
				EarthMinionList.addAll(earthList);
			}
		}
	}

	private void end_check() {
		if (getPlayMembersCount() <= 0) {
			step = 8;
			// quit();
		}
		if (start && time-- < 0) {
			for (L1PcInstance member : getPlayMemberArray()) {
				SC_ARENA_PLAY_STATUS_NOTI.end_time_send(member);
			}
			step = 7;
			// quit();
		}
		if (time == 60 * 12) {
			checkElemental();
			failElemental();
		}
	}
}