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

import l1j.server.IndunEx.PlayInfo.GludioInDungeon;
import l1j.server.IndunEx.PlayInfo.GludioInDungeonSpawn;
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
import l1j.server.server.utils.L1SpawnUtil;

public class GludioInDungeonEx implements Runnable {

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
	private int round = 0;
	private L1NpcInstance crystal;
	private L1NpcInstance circle;
	private L1MonsterInstance merkior = null;
	private L1MonsterInstance balterzar = null;
	private L1MonsterInstance sema = null;
	private L1MonsterInstance kaspa = null;

	private MJIndunRoomModel _model;
	private boolean crystal_hp = false;
	private boolean ck = false;
	private boolean ck2 = false;
	private boolean ck3 = false;
	private boolean crystal_destroy = false;

	private boolean _floor = false;
	private boolean _close = false;

	private boolean water = true;
	private boolean wind = true;
	private boolean earth = true;
	private boolean fire = true;

	L1NpcInstance[] _firewall = new L1NpcInstance[28];

	public void Start() {
		GeneralThreadPool.getInstance().execute(this);
	}

	public GludioInDungeonEx(int mapid, int price, MJIndunRoomModel model, boolean floor) {
		_mapnum = mapid;
		_model = model;
		_floor = floor;

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
			MJPoint pt = MJPoint.newInstance(32801, 32862, 5, (short) mapid, 50);
			member_model.member.start_teleport(pt.x, pt.y, pt.mapId, member_model.member.getHeading(), 18339, false);
		}

		GludioInDungeonSpawn.getInstance().fillSpawnTable(mapid, 0);
		GludioInDungeonSpawn.getInstance().fillSpawnTable(mapid, 1);
		crystal = L1SpawnUtil.spawn4(32801, 32862, (short) mapid, 4, 50000220, 0, 0, 0);

		for (int i = 0; i < 6; i++) {
			_firewall[i] = L1SpawnUtil.spawnnpc(32797 + i, 32851, (short) _mapnum, 50000221, 0, 0, 0);
		}

		for (int i = 6; i < 13; i++) {
			_firewall[i] = L1SpawnUtil.spawnnpc(32812, 32860 + i - 6, (short) _mapnum, 50000221, 0, 0, 0);
		}

		for (int i = 13; i < 20; i++) {
			_firewall[i] = L1SpawnUtil.spawnnpc(32796 + i - 13, 32875, (short) _mapnum, 50000221, 0, 0, 0);
		}

		for (int i = 20; i < 27; i++) {
			_firewall[i] = L1SpawnUtil.spawnnpc(32787, 32860 + i - 20, (short) _mapnum, 50000221, 0, 0, 0);
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
				if (_floor)
					kaspa_check();

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

	class KaspaFamily implements Runnable {
		@Override
		public void run() {
			try {
				if (_close)
					return;

				// TODO 자동 생성된 메소드 스텁
				if (sub_step3 == 0) {
					merkior = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32799, 32901, (short) _mapnum, 50000222, 0, 0,
							0);
					balterzar = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32763, 32864, (short) _mapnum, 50000223, 0, 0,
							0);
					sema = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32800, 32825, (short) _mapnum, 50000224, 0, 0, 0);
					kaspa = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32838, 32862, (short) _mapnum, 50000225, 0, 0, 0);
					for (int wd = 0; wd < 27; wd++) {
						if (_firewall[wd] != null)
							_firewall[wd].deleteMe();
					}
				} else if (sub_step3 == 1) {
					GludioInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 5, 3, crystal, false);
				} else if (sub_step3 == 2) {
					GludioInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 5, 3, crystal, true);
				} else if (sub_step3 == 3) {
					GludioInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 5, 5, crystal, false);
				}

				sub_step3++;
				GeneralThreadPool.getInstance().schedule(this, 20000);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class LastBoss implements Runnable {
		private int Bossstep = 0;
		private L1MonsterInstance mon = null;
		int BossNum = 0;

		@Override
		public void run() {
			try {
				if (_close)
					return;

				// TODO 자동 생성된 메소드 스텁
				if (_floor) {
					BossNum = 50000256;
				} else {
					BossNum = 50000255;
				}

				switch (Bossstep) {
					case 0:
						Bossstep++;
						break;
					case 1:
						mon = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32794, 32861, (short) _mapnum, BossNum, 3, 0, 0);
						Bossstep++;
						break;
					case 2:
						if (mon == null || mon._destroyed || mon.isDead()) {
							GREEN_MSG("擋住了所有攻擊.");

							int[] surf = new int[] { 22906, 22906, 22906, 22906, 22906 };
							int[] dialogs = new int[] { 2001, 2002, 2003, 2004, 2005 };
							QUEST_MSG(surf, dialogs);

							crystal.setCurrentHp(crystal.getMaxHp());
							step = 4;
							return;
						}
						break;
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO 자동 생성된 메소드 스텁
		try {
			if (_close)
				return;
			switch (step) {
				case 0:
					if (sub_step == 0) {
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(4));
						}
					} else if (sub_step == 1) {
						int[] surf = new int[] { 22906, 22906, 22906, 22906, 22906 };
						int[] dialogs = new int[] { 1996, 1997, 1998, 1999, 2000 };
						QUEST_MSG(surf, dialogs);
					} else if (sub_step == 2) {
						if (sub_step == 2) {
							GREEN_MSG("請保護中央的水晶球。");
						} else if (sub_step == 3) {
							GREEN_MSG("如果時間超過或水晶球被破壞，則任務失敗。");
						} else if (sub_step == 4) {
							startAllBuffThread(); // 增加增益
							GREEN_MSG("奧林給你提供了一些力量。");
						} else if (sub_step == 5) {
							GREEN_MSG("如果時間超過或水晶球被破壞，則任務失敗。");
						} else if (sub_step == 6) {
							time = gametime;
							for (L1PcInstance pc : getPlayMemberArray()) {
								SC_ARENA_PLAY_STATUS_NOTI.time_send(pc, gametime + 3, true);
							}
							start = true;
							GREEN_MSG("受詛咒的群體開始湧來。");
						}
					}

					if (sub_step++ >= 6) {
						step++;
					} else {
						GeneralThreadPool.getInstance().schedule(this, 7000);
						return;
					}
					break;
				case 1:
					if (sub_step >= 4) {
						round++;
						sub_step = 0;
					}
					step++;
					if (round == 2)
						sub_step2 = 1;
					else if (round == 3)
						sub_step2 = 2;
					else if (round == 4)
						sub_step2 = 3;
					GeneralThreadPool.getInstance().schedule(this, 1000);
					return;
				case 2:
					if (sub_step2 > 0) {
						if (sub_step == 0) {
							if (sub_step2 == 1) {
								if (_floor) {
									GeneralThreadPool.getInstance().schedule(new KaspaFamily(), 7000);
								}
							} else if (sub_step2 == 2) {
								GREEN_MSG("受詛咒的氣息開始靠近.");
							} else if (sub_step2 == 3) {
								GeneralThreadPool.getInstance().schedule(new LastBoss(), 7000);
							}
						}
					}

					if (sub_step++ < 4) {
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(6));
						}

						if (water)
							GludioInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 1, 2, crystal, false);
						if (wind)
							GludioInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 2, 2, crystal, false);
						if (earth)
							GludioInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 3, 2, crystal, false);
						if (fire)
							GludioInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 4, 2, crystal, false);
					}

					if (round >= 4) {
						step++;
						GeneralThreadPool.getInstance().schedule(this, 1000);
						return;
					}
					step = 1;
					GeneralThreadPool.getInstance().schedule(this, 20000);
					return;
				case 3:
					GeneralThreadPool.getInstance().schedule(this, 1000);
					return;
				case 4:
					step++;
					sub_step = 0;
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				case 5:
					if (sub_step == 0) {
						crystal.setCurrentSprite(12493);
						sub_step++;
						GeneralThreadPool.getInstance().schedule(this, 1000);
						return;
					} else if (sub_step == 1) {
						GREEN_MSG("成功幫助了奧林。");
						sub_step++;
						GeneralThreadPool.getInstance().schedule(this, 5000);
						return;
					} else if (sub_step == 2) {
						GREEN_MSG("時空旅行即將結束。");
						sub_step++;
						GeneralThreadPool.getInstance().schedule(this, 5000);
						return;
					} else if (sub_step == 3) {
						GREEN_MSG("請移動到奧林前生成的傳送魔法陣。");
						// 插入奧林和魔法陣
						for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
							if (ob instanceof L1MonsterInstance) {
								L1MonsterInstance npc = (L1MonsterInstance) ob;
								for (L1PcInstance pc : getPlayMemberArray()) {
									pc.sendPackets(new S_RemoveObject(npc));
									pc.removeKnownObject(npc);
								}
								npc.receiveDamage(crystal, 10000);
							}
						}

						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(new S_RemoveObject(crystal));
							pc.removeKnownObject(crystal);
						}
						crystal.teleport(32771, 32835, 6);

						L1SpawnUtil.spawn4(32811, 32862, (short) _mapnum, 6, 50000261, 0, 0, 0);
						circle = L1SpawnUtil.spawn4(32807, 32862, (short) _mapnum, 6, 50000262, 0, 0, 0);

						sub_step++;
						GeneralThreadPool.getInstance().schedule(this, 1000);
						return;
					} else if (sub_step == 4) {
						GREEN_MSG("願艾因哈萨德的祝福伴随你的未来..");
						sub_step = 0;
						step++;
						GeneralThreadPool.getInstance().schedule(this, 1000);
						return;
					}
					break;
				case 6:
					if (sub_step == 0)
						GREEN_MSG("30秒後將移動到村莊。");

					if (sub_step == 10) {
						sub_step = 0;
						step++;
						GeneralThreadPool.getInstance().schedule(this, 1000);
						return;
					}
					sub_step++;
					GeneralThreadPool.getInstance().schedule(this, 1000);
					return;
				case 7:
					if (sub_step == 0)
						GREEN_MSG("20秒後將移動到村莊。");

					if (sub_step == 10) {
						sub_step = 0;
						step++;
						GeneralThreadPool.getInstance().schedule(this, 1000);
						return;
					}
					sub_step++;
					GeneralThreadPool.getInstance().schedule(this, 1000);
					return;
				case 8:
					if (sub_step == 0)
						GREEN_MSG("10秒後將移動到村莊。");

					if (sub_step == 10) {
						sub_step = 0;
						step++;
						GeneralThreadPool.getInstance().schedule(this, 1000);
						return;
					}
					sub_step++;
					GeneralThreadPool.getInstance().schedule(this, 1000);
					return;
				case 9: // 正常結束
					quit();
					GeneralThreadPool.getInstance().schedule(this, 1000);
					return;
				case 10: // 水晶球破壞
					GREEN_MSG("黑魔法水晶球已被摧毀。");
					for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
						if (ob instanceof L1MonsterInstance) {
							L1MonsterInstance npc = (L1MonsterInstance) ob;
							if (npc == null || npc._destroyed || npc.isDead())
								continue;
							npc.deleteMe();
						}
					}
					step = 6;
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				case 11:
					GREEN_MSG("無法單獨進行遊戲.");
					for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
						if (ob instanceof L1MonsterInstance) {
							L1MonsterInstance npc = (L1MonsterInstance) ob;
							if (npc == null || npc._destroyed || npc.isDead())
								continue;
							npc.deleteMe();
						}
					}
					step = 9;
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
		GludioInDungeon.getInstance().Reset(_mapnum);
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

	private void kaspa_check() {
		if (sema != null) {
			if (sema._destroyed || sema.isDead()) {
				fire = false;
			}
		}

		if (kaspa != null) {
			if (kaspa != null && kaspa._destroyed || kaspa.isDead()) {
				earth = false;
			}
		}

		if (merkior != null) {
			if (merkior._destroyed || merkior.isDead()) {
				water = false;
			}
		}

		if (balterzar != null) {
			if (balterzar._destroyed || balterzar.isDead()) {
				wind = false;
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

	private void end_check() {
		if (getPlayMembersCount() <= 0) {
			quit();
		}
		
		if (!crystal_hp) {
			int percent = (int) Math.round((double) crystal.getCurrentHp() / (double) crystal.getMaxHp() * 100);
			if (percent < 75) {
			for (L1PcInstance member : getPlayMemberArray()) {
			crystal.setCurrentSprite(17813);
			member.sendPackets(new S_ChangeShape(crystal.getId(), 17813), true);
			GREEN_MSG("黑魔法水晶球已受損。");
			}
			crystal_hp = true;
			}
		}
			
			if (!ck) {
			int percent = (int) Math.round((double) crystal.getCurrentHp() / (double) crystal.getMaxHp() * 100);
			if (percent < 50) {
			for (L1PcInstance member : getPlayMemberArray()) {
			crystal.setCurrentSprite(17813);
			member.sendPackets(new S_ChangeShape(crystal.getId(), 17813), true);
			GREEN_MSG("黑魔法水晶球受到了大量傷害。")
			}
			ck = true;
			}
		}
			
			if (!ck2) {
			int percent = (int) Math.round((double) crystal.getCurrentHp() / (double) crystal.getMaxHp() * 100);
			if (percent < 25) {
			for (L1PcInstance member : getPlayMemberArray()) {
			crystal.setCurrentSprite(17814);
			member.sendPackets(new S_ChangeShape(crystal.getId(), 17814), true);
			GREEN_MSG("黑魔法水晶球受到了過多的損害。");
			}
			ck2 = true;
			}
		}
		if (!ck3) {
			if (crystal.getCurrentHp() <= 0) {
				for (L1PcInstance member : getPlayMemberArray()) {
					crystal.setCurrentSprite(17815);
					member.sendPackets(new S_ChangeShape(crystal.getId(), 17815), true);
					crystal.broadcastPacket(new S_DoActionGFX(crystal.getId(), 0));
				}
				ck3 = true;
			}
		}
		
		
		if (crystal == null || crystal._destroyed || crystal.isDead()) {
			if (!crystal_destroy) {
				step = 10;
				crystal_destroy = true;
			}
		}

		if (start && time-- < 0) {
			for (L1PcInstance member : getPlayMemberArray()) {
				SC_ARENA_PLAY_STATUS_NOTI.end_time_send(member);
			}
			quit();
		}
	}
}