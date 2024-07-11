package l1j.server.IndunEx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import l1j.server.IndunEx.PlayInfo.CrocodileInDungeon;
import l1j.server.IndunEx.PlayInfo.CrocodileInDungeonSpawn;
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
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

public class CrocodileInDungeonEx implements Runnable {

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
	private int sub_step6 = 0;
	private int sub_step7 = 0;
	private int sub_step8 = 0;

	private L1NpcInstance Gerang;
	private L1NpcInstance circle1;
	private L1NpcInstance circle2;
	private L1NpcInstance circle3;

	private L1MonsterInstance boss_1 = null;
	private L1MonsterInstance boss_2 = null;
	private L1MonsterInstance boss_3 = null;
	private L1MonsterInstance boss_4 = null;

	private static final int[] _First_MonsterList = { 7800106, 7800107 };
	private static final int[] _Second_MonsterList = { 7800108, 7800109, 7800110, 7800111 };
	private static final int[] _Third_MonsterList = { 7800112, 7800113 };

	private MJIndunRoomModel _model;

	private int current_point = 0;
	private static final int BOSS4_POINT = 80; // 召喚夢幻魅魔所需積分

	private boolean _close = false;

	L1NpcInstance[] _firewall = new L1NpcInstance[9];

	public void Start() {
		GeneralThreadPool.getInstance().execute(this);
	}

	public CrocodileInDungeonEx(int mapid, int price, MJIndunRoomModel model) {
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

			MJPoint pt = MJPoint.newInstance(33384, 33195, 2, (short) mapid, 50);
			member_model.member.start_teleport(pt.x, pt.y, pt.mapId, member_model.member.getHeading(), 18339, false);
		}

		Gerang = L1SpawnUtil.spawn4(33385, 33192, (short) mapid, 4, 7800100, 0, 0, 0);// 格倫

		for (int i = 0; i < 9; i++) {
			_firewall[i] = L1SpawnUtil.spawnnpc(33391, 33191 + i, (short) _mapnum, 50000270, 0, 0, 0);
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
		// TODO 自動產生的方法存根
		try {
			if (_close)
				return;
			switch (step) {
			case 0:
//				System.out.println("step: "+step+"sub_step: "+sub_step);
				if (sub_step == 0){
					for (L1PcInstance pc : getPlayMemberArray()) {
						pc.sendPackets(S_DisplayEffect.newInstance(4));
					}
					sub_step = 1;
				} else if (sub_step == 1){//Axum啟動訊息框
					int[] surf = new int[] {3515, 3515, 3515};
					int[] dialogs = new int[] {2018, 2019, 2020};
					QUEST_MSG(surf, dialogs);
					sub_step = 2;
				} else if (sub_step == 2){
					GREEN_MSG("怪物们即将蜂拥而至。");
					GREEN_MSG("最好做好萬全的準備。");
					sub_step = 3;
				} else if (sub_step ==3){
					step = 1;
				}
				GeneralThreadPool.getInstance().schedule(this, 10000);
				return;
//				break;

			case 1: //wait_step
//				System.out.println("step: "+step);
				time = gametime;
				for (L1PcInstance pc : getPlayMemberArray()){
					SC_ARENA_PLAY_STATUS_NOTI.time_send(pc, gametime + 3, true);
				}
				start = true;
				step = 2;
				GeneralThreadPool.getInstance().schedule(this, 7000);
				return;
			case 2://FIRST_STEP
//				System.out.println("step: "+step+"sub_step2: "+sub_step2);

				if (sub_step2 == 0){
					CrocodileInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 0, 1, 12, true);
					sub_step2 = 1;
				} else if (sub_step2 == 1){
					for (L1PcInstance pc : getPlayMemberArray()){
						pc.sendPackets(S_DisplayEffect.newInstance(6));
					}
					for (int wd = 0; wd < 9; wd++){
						if(_firewall[wd] !=null){
							_firewall[wd].deleteMe();
						}
					}
//					System.out.println("보스1번 소환");
					sub_step2 = 2;
					boss_1 = (L1MonsterInstance) L1SpawnUtil.spawnnpc(33430, 33233, (short) _mapnum, 7800101, 0, 0, 0);
				} else if (sub_step2 == 2){
//					System.out.println(boss_1.isDead()+"+"+boss_1);
					if (boss_1.isDead() && boss_1 != null){
						int number = CommonUtil.random(33);
						current_point += number +1;
						GREEN_MSG("去東方盡頭的燈塔。通過那裡可以進入地下水道。");
						GREEN_MSG("水道的盡頭連接著鱷魚島。");
						GREEN_MSG("如果你不打算停止腳步，我就不攔著你了……但是小心點。");
						sub_step2 = 3;
					}
				} else if(sub_step2 == 3){
					circle1 = L1SpawnUtil.spawn4(33430, 33233, (short) _mapnum, 6, 50000262, 0, 0, 0);
					boss_2 = (L1MonsterInstance) L1SpawnUtil.spawnnpc(33493, 33415, (short) _mapnum, 7800102, 0, 0, 0);
					step = 3;
					CrocodileInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 0, 2, 3, true);
				}
				
				current_point -= 1;
				GeneralThreadPool.getInstance().schedule(this, 4000);
				return;

			case 3: //SECOND_STEP
//				System.out.println("step: "+step+"sub_step3: "+sub_step3);

				if (sub_step3 == 0){
//					System.out.println(boss_2.isDead()+"+"+boss_2);
					if (boss_2.isDead() && boss_2 != null){
						int number1 = CommonUtil.random(33);
						current_point += number1 +1;
						GREEN_MSG("你真的打算去那個我們幾十年來都不敢接近的地方嗎？");
						GREEN_MSG("這裡是封印著變成怪物的鱷魚王羅瑟斯的地方。");
						GREEN_MSG("願神的庇佑與你同在……");
						sub_step3 = 1;
					}
				} else if (sub_step3 == 1){
					circle2 = L1SpawnUtil.spawn4(33493, 33415, (short) _mapnum, 6, 50000262, 0, 0, 0);
					step = 4;
				}
				GeneralThreadPool.getInstance().schedule(this, 4000);
				return;
			case 4: //THIRD_STEP
//				System.out.println("step: "+step+"sub_step4: "+sub_step3);
				if (sub_step4 == 0){
					sub_step4 = 1;
				} else if (sub_step4 >= 1 && sub_step4 <= 4){
					CrocodileInDungeonSpawn.getInstance().fillSpawnTable(_mapnum, 0, 3, 13, true);
					sub_step4 += 1;
				} else if (sub_step4 == 5){
					boss_3 = (L1MonsterInstance) L1SpawnUtil.spawnnpc(33580, 33208, (short) _mapnum, 7800103, 0, 0, 0);
					sub_step4 = 6;
				} else if (sub_step4 == 6){
//					System.out.println(boss_3.isDead()+"+"+boss_3);
					if (boss_3.isDead() && boss_3 != null){
						int number2 = CommonUtil.random(33);
						current_point += number2 +1;
						
						
						int[] surf = new int[] {3515, 3515, 3515};
						int[] dialogs = new int[] {2021, 2022, 2023};
						QUEST_MSG(surf, dialogs);
						sub_step4 = 7;
					}
				} else if (sub_step4 == 7){
					if (current_point >= BOSS4_POINT){
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(4));
						}
						Thread.sleep(2000L);
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(4));
						}
						Thread.sleep(4000L);
						
						boss_4 = (L1MonsterInstance) L1SpawnUtil.spawnnpc(33580, 33208, (short) _mapnum, 7800104, 0, 0, 0);
						sub_step4 = 8;
					}else {
						step = 5;
					}
				} else if (sub_step4 == 8){
					if (boss_4.isDead() && boss_4 != null){
						int[] surf = new int[] {3515};
						int[] dialogs = new int[] {2024};
						QUEST_MSG(surf, dialogs);
						step = 5;
					}
				}
				GeneralThreadPool.getInstance().schedule(this, 5000);
				return;
			case 5: //END
				if (sub_step5 == 0){
					int[] surf = new int[] {3515};
					int[] dialogs = new int[] {2025};
					QUEST_MSG(surf, dialogs);
					GREEN_MSG("辛苦了。多虧了你，暫時可以和平了。");
					GREEN_MSG("現在你可以回到你的世界了。");
					sub_step5 = 1;
					GeneralThreadPool.getInstance().schedule(this, 1000);
					return;
				} else if (sub_step5 == 1){
					for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
						if (ob instanceof L1MonsterInstance) {
							L1MonsterInstance npc = (L1MonsterInstance) ob;
							for (L1PcInstance pc : getPlayMemberArray()) {
								pc.sendPackets(new S_RemoveObject(npc));
								pc.removeKnownObject(npc);
							}
						}
					}
					circle3 = L1SpawnUtil.spawn4(33580, 33208, (short) _mapnum, 6, 50000262, 0, 0, 0);
					GeneralThreadPool.getInstance().schedule(this, 1000);
					sub_step5 = 2;
					return;
				} else if (sub_step5 == 2){
					if (sub_step5 == 2) {
						GREEN_MSG("30秒後將移動到村莊。");
						sub_step5 = 3;
						GeneralThreadPool.getInstance().schedule(this, 10000);
						return;
					} else if (sub_step5 == 3) {
						GREEN_MSG("20秒後將移動到村莊。");
						sub_step5 = 4;
						GeneralThreadPool.getInstance().schedule(this, 10000);
						return;
					} else if (sub_step5 == 4) {
						GREEN_MSG("10秒後將移動到村莊。");
					}
					step = 6;
					GeneralThreadPool.getInstance().schedule(this, 10000);
					return;
				}
				return;
			case 6:
				quit();//방나가기
				GeneralThreadPool.getInstance().schedule(this, 1000);
				return;
			case 7://時間끝
				if (sub_step7 == 0){
					sub_step7++;
				} else if (sub_step7 == 1){
					if (sub_step7 == 1) {
						GREEN_MSG("糟了，在這裡逗留的時間已經到了。");
						sub_step7 = 2;
					} else if (sub_step7 == 2) {
						GREEN_MSG("雖然很遺憾，但你得回到你原來的地方了。");
						sub_step7 = 3;
					} else if (sub_step7 == 3) {
						quit();
					}
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
					
					case 8: // 全部死亡时
					if (sub_step8 == 0) {
						sub_step8++;
					} else if (sub_step8 == 1) {
						GREEN_MSG("唉... 因為我的貪心，連你也失去了。");
						sub_step8 = 2;
					} else if (sub_step8 == 2) {
						GREEN_MSG("回到你的世界，好好休息吧。");
					}
					sub_step8 = 3;
				} else if (sub_step8 == 3){
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

	private void quit() {
		System.out.println("副本結束地圖編號： " + _mapnum);
		MJIndunRoomController.getInstance().clear_room_info(_model);
		MJIndunRoomController.getInstance().reload_room_info();

		for (L1PcInstance pc : getPlayMemberArray()) {
			MJIndunRoomController.getInstance().end_user_room(pc, -1);
			_model.onquit(pc);
		}
		for (L1PcInstance pc : getPlayMemberArray()) {
			for (int i = 420101; i <= 420111; i++) {
				if (pc.getInventory().checkItem(i)) {
					continue;
				}
				pc.getInventory().consumeItem(i);

			}
		}

		HOME_TELEPORT();
		Object_Delete();
		clearPlayMember();
		CrocodileInDungeon.getInstance().Reset(_mapnum);
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
		if (circle1 != null) {
			for (L1PcInstance pc : getPlayMemberArray()) {
				if (pc.getX() == circle1.getX() && pc.getY() == circle1.getY()) {
					pc.start_teleport(33459 + rnd.nextInt(2), 33383 + rnd.nextInt(2), _mapnum, 5, 18339, true);
				}
			}
		}
		if (circle2 != null) {
			for (L1PcInstance pc : getPlayMemberArray()) {
				if (pc.getX() == circle2.getX() && pc.getY() == circle2.getY()) {
					pc.start_teleport(33580 + rnd.nextInt(2), 33208 + rnd.nextInt(2), _mapnum, 5, 18339, true);
				}
			}
		}
		if (circle3 != null) {
			for (L1PcInstance pc : getPlayMemberArray()) {
				if (pc.getX() == circle3.getX() && pc.getY() == circle3.getY()) {
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
	}
}