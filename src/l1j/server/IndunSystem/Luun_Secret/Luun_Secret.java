package l1j.server.IndunSystem.Luun_Secret;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.net.ntp.TimeStamp;

import javolution.util.FastTable;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Broadcaster;
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
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 魯恩城秘密地牢
 * &#064;作者  Shaman
 *
 */
public class Luun_Secret implements Runnable {

	private Random rnd = new Random(System.currentTimeMillis());
	private final ArrayList<L1PcInstance> playmember = new ArrayList<L1PcInstance>();
	private ArrayList<L1NpcInstance> MonsterList;

	private int _mapnum = 0;
	private boolean start = false;
	private int time = 0;
	private boolean timer = false;
	private int gametime = 30 * 60;
	private int circletime = 0;
	private int circlenum = 0;

	private int step = 0;
	private int sub_step = 0;
	private int sub_step1 = 0;
	private int sub_step2 = 0;
	private int sub_step3 = 0;
	private int sub_step4 = 0;
	private int sub_step5 = 0;
	private int sub_step6 = 0;
	private int sub_step7 = 0;
	private int sub_step8 = 0;
	private int sub_step9 = 0;
	private int sub_step10 = 0;

	private L1MonsterInstance boss_1; // 洞穴之主
	private L1MonsterInstance boss_2; // 恐怖的傀儡師
	private L1MonsterInstance boss_3; // 偵查隊長
	private L1MonsterInstance final_boss; // 最終頭目
	private L1PcInstance pc;
	private L1NpcInstance portal_1;
	private L1NpcInstance portal_2;

	FastTable<L1NpcInstance> monList = null;

	L1NpcInstance[] _glass = new L1NpcInstance[16];
	L1DoorInstance[] _box = new L1DoorInstance[40];
	L1DoorInstance[] _door = new L1DoorInstance[11];
	L1NpcInstance[] _circle = new L1NpcInstance[16];

	private boolean _close = false;

	public ArrayList<L1NpcInstance> BasicNpcList;
	public FastTable<L1NpcInstance> NpcList;


	public Luun_Secret(int id, L1PcInstance pc){
		_mapnum = (short)id;
		this.pc = pc;
	}

	public void Start(){
		GeneralThreadPool.getInstance().execute(this);
	}

	public Luun_Secret(int mapid){
		_mapnum = mapid;
		spawnDoor();
		spawnGlass();
		spawnBox();
		MonsterList = new ArrayList<L1NpcInstance>();
		GeneralThreadPool.getInstance().schedule(new timer(), 1000);
	}

	class timer implements Runnable{
		boolean playercheck = false;

		@Override
		public void run(){
			try{
				if (_close){
					return new L1PcInstance[0];
				}
				checkMember();
				end_check();
				home_teleport();
				time++;
				if (!playercheck){
					step = 0;
					playercheck = true;
				}
				if (!timer){
					timer = true;
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e){
				e.printStackTrace();
			}
			return new L1PcInstance[0];
		}
	}

	@Override
	public void run() {
		try{
			if (_close){
				return new L1PcInstance[0];
			}
			switch (step){
				case 0: // 一層起點。
					if (sub_step == 0) {
						if (getPlayMembersCount() == 0){
							GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
							return new L1PcInstance[0];
						}
						sub_step = 1;
					} else if (sub_step == 1) {
						sub_step = 2;
						GeneralThreadPool.getInstance().schedule(this, 10 * 1000);
						return new L1PcInstance[0];
					} else if (sub_step == 2){
						for (L1PcInstance pc : getPlayMemberArray()) {
							pc.sendPackets(S_DisplayEffect.newInstance(4));
						}
						sub_step = 3;
					}else if (sub_step == 3){
						GREEN_MSG("安然到達了！有三條路可以通往二層。");
						sub_step = 4;
					} else if (sub_step == 4){
						GREEN_MSG("上方和下方看起來很普通……中央雖然看起來危險，但能獲得通往二層捷徑的鑰匙。");
						sub_step = 5;
					} else if (sub_step == 5){
						GREEN_MSG("移動得越快，能進入黃金房的人就會越多！");

						sub_step = 6;
						GeneralThreadPool.getInstance().schedule(this, 10 * 1000);
						return new L1PcInstance[0];
					}  else if (sub_step == 6){
						_door[1].open();
						_door[0].open();
						checkArea();
					}
					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return new L1PcInstance[0];
				case 1: // 一層第1區域
					if (sub_step1 == 0){
						GREEN_MSG("您選擇了平坦的道路。我們快點突破吧！");
						sub_step1 = 1;
					} else if (sub_step1 == 1){
						sub_step1 = 2;
						spawnMonster();
					} else if (sub_step1 == 2){
						MonsterCheck();
						if (MonsterList != null && MonsterList.isEmpty()){
							MonsterList = null;
							_door[3].open();
							step = 4;
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return new L1PcInstance[0];
				case 2: // 一層第2區域
					if (sub_step2 == 0) {
						GREEN_MSG("感覺到危險的氣息。或許能獲得進入黃金寶物房的鑰匙……");
						boss_1 = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32669, 32802, (short) _mapnum, 7800444, 0, 0, 0);
						sub_step2 = 1;
					} else if (sub_step2 == 1){
						if (boss_1.isDead() && boss_1 !=null){
							_door[5].open();
							int rand = CommonUtil.random(100);
							if (rand > 50){
								int randi = CommonUtil.random(getPlayMembersCount());
								int i = 0;
								for (L1PcInstance pc : getPlayMemberArray()){
									if (i == randi){
										pc.getInventory().storeItem(14000007, 1, true);// 黃金鑰匙生成
									}
									i++;
								}
							}
							step = 4;
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return new L1PcInstance[0];
				case 3: // 一層第3區域
					if (sub_step3 == 0) {
						GREEN_MSG("您選擇了簡單的道路。可能會比想像中花更多時間……");
						sub_step3 = 1;
					} else if (sub_step3 == 1){
						sub_step3 = 2;
						spawnMonster();
					} else if (sub_step3 == 2){
						MonsterCheck();
//					System.out.println(MonsterList);
						if (MonsterList != null && MonsterList.isEmpty()){
							_door[7].open();
							step = 4;
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return new L1PcInstance[0];
				case 4: // 一層傳送門生成區域
					if (sub_step4 == 0) { // 恐怖的人偶術士
						boss_2 = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32713, 32847, (short) _mapnum, 7800233, 0, 0, 0);
						sub_step4 = 1;
					} else if (sub_step4 == 1){
						for (L1PcInstance pc : getPlayMemberArray()){
							if (pc.getX() >= 32695 && pc.getX() <= 32722 && pc.getY() >= 32829 && pc.getY() <= 32840){
								sub_step4 = 2;
								break;
							}
						}
					} else if (sub_step4 == 2){
						GREEN_MSG("我將為您打開通往二層的傳送門！請清除前面的怪物！");
						sub_step4 = 3;
					} else if (sub_step4 == 3){
						if (boss_2 != null && boss_2.isDead()){
							for (L1PcInstance pc : getPlayMemberArray()){
								pc.getInventory().storeItem(14000006, 1, true);// 金色鑰匙
							}
							sub_step4 = 4;
						}
					} else if (sub_step4 == 4){
						portal_1 = L1SpawnUtil.spawn4(32720, 32847, (short) _mapnum, 6, 7800224, 0, 0, 0); // 創建通往二層的傳送門
						boss_3 = (L1MonsterInstance) L1SpawnUtil.spawnnpc(32832, 32746, (short) _mapnum, 7800234, 0, 0, 0); // 二層偵查隊長
						step = 5;
					}

					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return new L1PcInstance[0];
				case 5: // 二層起點
					if (sub_step5 == 0){
						checkArea2();
						checkDoor();
					}
					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return new L1PcInstance[0];
				case 6:
					if (sub_step6 == 0){
						GREEN_MSG("看來無法輕易到達對面……不過讓我們試著移動吧……");
						sub_step6 = 1 ;
					} else if (sub_step6 == 1) {
						sub_step6 = 2 ;
						Glassbrokencheck();
					} else if (sub_step6 == 2) {
						GlassMovecheck();
						GeneralThreadPool.getInstance().schedule(this, 1 * 100);
						return new L1PcInstance[0];
					}
					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return new L1PcInstance[0];
				case 7:
					if (sub_step7 == 0){
						GREEN_MSG("好像快到終點了，加油突破吧！");
						sub_step7 = 1;
					} else if (sub_step7 == 1){
						sub_step7 = 2;
						spawnMonster();
					} else if (sub_step7 == 2){
						MonsterCheck();
						if (MonsterList != null && MonsterList.isEmpty()){
							_door[9].open();
							step = 8;
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return new L1PcInstance[0];
				case 8:
					if (sub_step8 == 0){
						checkArea2();
					} else if (sub_step8 == 1){
						GREEN_MSG("在我為您打開通往三層的傳送門期間，請稍微拖延一點時間！");
						sub_step8 = 2;
					} else if (sub_step8 == 2){
						if (boss_3 != null && boss_3.isDead()){
							portal_2 = L1SpawnUtil.spawn4(32817, 32726, (short) _mapnum, 6, 7800225, 0, 0, 0); // 創建通往三層的傳送門
							_door[10].open();
							timecheck();
							step = 9;
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return new L1PcInstance[0];
				case 9: // 三層起點
					if (sub_step9 == 0) {
						GREEN_MSG("這是最後階段。消滅Boss並攜帶盧恩城的寶物逃脫吧。");
						sub_step9 = 1;
					} else if (sub_step9 == 1) {
						if (final_boss.isDead() && final_boss != null) {
							GREEN_MSG("20秒後傳送門將啟動！入場後，根據所花時間決定可以移動的人數，祝你好運~");
							circletime = time;
							CircleSpawn();
							sub_step9 = 2;
						}
					} else if (sub_step9 == 2){
						if ((circletime + 20) <= time){
							CircleDelete();
							step = 10;
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return new L1PcInstance[0];
				case 10:
					if (sub_step10 == 0){
						sub_step10 = 1;
					} else if (sub_step10 ==1){
						quit();
					}
					GeneralThreadPool.getInstance().schedule(this, 20 * 1000);
					return new L1PcInstance[0];
			}
		} catch (Exception e){
		}
		GeneralThreadPool.getInstance().schedule(this, 1000);
		return new L1PcInstance[0];
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

	private void checkMember() {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				if (!isPlayMember(pc)){
					addPlayMember(pc);
				}
			}
		}
		for (L1PcInstance pc : getPlayMemberArray()) {
			if (!pc.getInventory().checkItem(30001462)) {
				pc.getInventory().storeItem(30001462, 1, true);
			}
		}

		if (getPlayMembersCount() > 0){
			for (L1PcInstance pc : getPlayMemberArray()){
				if (pc.getMapId() != _mapnum){
					removePlayMember(pc);
				}
			}
		}
	}

	private void end_check() {
		if (start && getPlayMembersCount() <= 0) {
			step = 10;
		}

		if (start && time > gametime) {
			quit();
		}
	}

	private void home_teleport() {
		if (portal_1 != null) {
			for (L1PcInstance pc : getPlayMemberArray()) {
				if (pc.getX() == portal_1.getX() && pc.getY() == portal_1.getY()) {
					pc.start_teleport(32902 + rnd.nextInt(5), 32831 + rnd.nextInt(5), _mapnum, 5, 18339, true);
				}
			}
		}
		if (portal_2 != null){
			for (L1PcInstance pc : getPlayMemberArray()) {
				if (pc.getX() == portal_2.getX() && pc.getY() == portal_2.getY()) {
					pc.start_teleport(32995 + rnd.nextInt(5), 32811 + rnd.nextInt(5), _mapnum, 5, 18339, true);
				}
			}
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

	private void checkArea(){// 檢查一層位置
		if (step == 1 || step == 2|| step == 3){
			return;
		}
		for (L1PcInstance pc : getPlayMemberArray()){
			if (step == 0){
				if ((pc.getX() >= 32661 &&pc.getX() <= 32681)&& (pc.getY() >= 32742 && pc.getY() <= 32752)){
					step = 1; // 一層 第1區域
					_door[2].open();
				} else if ((pc.getX() >= 32660 && pc.getX() <= 32666) && (pc.getY() >= 32762 && pc.getY() <= 32778)) {
					step = 2; // 一層 第2區域
					_door[4].open();
				} else if ((pc.getX() >= 32612 && pc.getX() <= 32630) && (pc.getY() >= 32780 && pc.getY() <= 32812)) {
					step = 3; // 一層 第3區域
					_door[6].open();
				}
			}
		}
	}

	private void checkArea2() { // 檢查二層位置
		for (L1PcInstance pc : getPlayMemberArray()) {
			if (step == 5) {
				if ((pc.getX() >= 32843 && pc.getX() <= 32875) && (pc.getY() >= 32844 && pc.getY() <= 32855)) {
					step = 6; // 二層 第1區域
				}
			} else if (step == 8){
				if (pc.getX() >= 32819 && pc.getX() <= 32849 && pc.getY() >= 32738 && pc.getY() <= 32758){
					sub_step8 = 1;
				}
			}
		}
	}
	private void checkDoor(){// 檢查二層門的開啟情況
		for (L1PcInstance pc : getPlayMemberArray()){
			if (step == 5){
				if ((pc.getX() >= 32902 && pc.getX() <= 32905) && (pc.getY() == 32799 || pc.getY() == 32800)){
					if (pc.getInventory().checkItem(14000007, 1)){
						pc.getInventory().consumeItem(14000007, 1);
						_door[8].open();
//						_2ndFloorDoor_1.open();
						step = 8;
					}
				}
			}
		}
	}

	private void timecheck(){
		if (time < 8 * 60){
			final_boss = (L1MonsterInstance) L1SpawnUtil.spawnnpc(33005, 32818, (short) _mapnum, 7800238, 0, 0, 0); // 盧恩三世
		} else if (time >= 8*60 && time < 10*60) {
			final_boss = (L1MonsterInstance) L1SpawnUtil.spawnnpc(33005, 32818, (short) _mapnum, 7800237, 0, 0, 0); // 劍鬼
		} else if (time >= 10*60 && time < 12*60) {
			final_boss = (L1MonsterInstance) L1SpawnUtil.spawnnpc(33005, 32818, (short) _mapnum, 7800236, 0, 0, 0); // 多重面具
		} else if (time >= 14*60) {
			final_boss = (L1MonsterInstance) L1SpawnUtil.spawnnpc(33005, 32818, (short) _mapnum, 7800235, 0, 0, 0); // 尤瑪博士
		}
	}


	private void MonsterCheck(){
		if (MonsterList == null){
			return;
		}
		for (int i = 0; i < MonsterList.size(); i++){
			L1NpcInstance npc = MonsterList.get(i);
			if (npc !=null && npc.isDead()){
				MonsterList.remove(npc);
			}
		}
	}

	private void spawnMonster() {
		FastTable<L1NpcInstance> monList = null;
		if (step == 1) {
			monList = Luun_Secret_Spawn.getInstance().fillSpawnTable(_mapnum, 0, 1, 5, false);
			MonsterList.addAll(monList);
		} else if (step == 3) {
			monList = Luun_Secret_Spawn.getInstance().fillSpawnTable(_mapnum, 0, 3, 5, false);
			MonsterList.addAll(monList);
		} else if (step == 7) {
			monList = Luun_Secret_Spawn.getInstance().fillSpawnTable(_mapnum, 0, 7, 5, false);
			MonsterList.addAll(monList);
		}
	}


	private void spawnDoor(){
		for (int i = 0 ; i < 11; i++) {
			switch (i) {
				case 0:
					_door[0] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32628, 32750, (short) _mapnum, 7800220, 0, 0, 0); // 一層等待室出去的門
					break;
				case 1:
					_door[1] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32616, 32759, (short) _mapnum, 7800221, 0, 0, 0); // 一層等待室出去的門
					break;
				case 2:
					_door[2] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32694, 32763, (short) _mapnum, 7800220, 0, 0, 0); // 一層第1房間進去的門
					break;
				case 3:
					_door[3] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32713, 32778, (short) _mapnum, 7800221, 0, 0, 0); // 一層第1房間出去的門
					break;
				case 4:
					_door[4] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32661, 32786, (short) _mapnum, 7800221, 0, 0, 0); // 一層第2房間進去的門
					break;
				case 5:
					_door[5] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32673, 32817, (short) _mapnum, 7800221, 0, 0, 0); // 一層第2房間出去的門
					break;
				case 6:
					_door[6] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32620, 32830, (short) _mapnum, 7800221, 0, 0, 0); // 一層第3房間進去的門
					break;
				case 7:
					_door[7] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32637, 32853, (short) _mapnum, 7800220, 0, 0, 0); // 一層第3房間出去的門
					break;
				case 8:
					_door[8] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32902, 32798, (short) _mapnum, 7800222, 0, 0, 0); // 二層黃金寶物房進去的門
					break;
				case 9:
					_door[9] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32839, 32773, (short) _mapnum, 7800223, 0, 0, 0); // 二層下路Boss房之前的門
					break;
				case 10:
					_door[10] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32814, 32735, (short) _mapnum, 7800223, 0, 0, 0); // 三層傳送門打開的門
					break;
			}
		}
	}

	private void Glassbrokencheck() {

		for (int i = 0; i < 8;i++) {
			int rand = CommonUtil.random(2);
			if (rand == 0){
				_glass[i].setBrakeable(true);
				_glass[i + 8].setBrakeable(false);
			} else {
				_glass[i].setBrakeable(false);
				_glass[i + 8].setBrakeable(true);
			}
		}
	}

	private void GlassMovecheck() {
		for (int i = 0; i< 18;i++) {
			for (L1PcInstance pc: getPlayMemberArray()) {
				if (pc.getLocation().getTileLineDistance(new Point(_glass[i].getLocation())) < 2 && !pc.isDead()) {
					if(_glass[i].isBrakeable()){
						pc.start_teleport(32849, 32852, _mapnum, pc.getHeading(), 18339, true);
						_glass[i].sendPackets(new S_DoActionGFX(_glass[i].getId(), 18), true);
						Broadcaster.broadcastPacket(_glass[i], new S_DoActionGFX(_glass[i].getId(), 18));
					}
				}
				if ((pc.getX() >= 32835 &&  pc.getX() <= 32862) && (pc.getY() >= 32812 && pc.getY() <= 32822)){
					step = 7;
					for (int j = 0; j < 16; j++){
						_glass[i].setBrakeable(false);
					}
				}
			}
		}
	}

	private void spawnBox() {
		for (int i = 0; i < 5 ; i++) {
			int randx = CommonUtil.random(5);
			int randy = CommonUtil.random(5);
			int randm = CommonUtil.random(3);
			int randK = CommonUtil.random(100);
			int boxid = 0;
			if (randK < 30 ) {
				boxid = 7800240;
			} else if(randK >=30 && randK <50 ) {
				boxid = 7800241;
			} else if(randK >=50 && randK < 70) {
				boxid = 7800242;
			} else if(randK >=70 && randK < 100) {
				boxid = 7800243;
			}
			if (randm == 0) {
				randx *= -1;
				randy *= 1;
			} else if (randm == 1) {
				randx *= -1;
				randy *= -1;
			} else if (randm == 2) {
				randx *= 1;
				randy *= -1;
			} else if (randm == 3) {
				randx *= 1;
				randy *= 1;
			}
			_box[i] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32904 + randx, 32750 + randy, (short) _mapnum, boxid, 0, 0, 0); // 二層寶物倉庫
			_box[i + 5] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32909 + randx, 32755 + randy, (short) _mapnum, boxid, 0, 0, 0); // 二層寶物倉庫
			_box[i + 10] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32894 + randx, 32751 + randy, (short) _mapnum, boxid, 0, 0, 0); // 二層寶物倉庫
			_box[i + 15] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32894 + randx, 32758 + randy, (short) _mapnum, boxid, 0, 0, 0); // 二層寶物倉庫

			_box[i + 20] = (L1DoorInstance) L1SpawnUtil.spawnnpc(33048 + randx, 32854 + randy, (short) _mapnum, boxid, 0, 0, 0); // 三層寶物倉庫
			_box[i + 25] = (L1DoorInstance) L1SpawnUtil.spawnnpc(33044 + randx, 32853 + randy, (short) _mapnum, boxid, 0, 0, 0); // 三層寶物倉庫
			_box[i + 30] = (L1DoorInstance) L1SpawnUtil.spawnnpc(33044 + randx, 32860 + randy, (short) _mapnum, boxid, 0, 0, 0); // 三層寶物倉庫
			_box[i + 35] = (L1DoorInstance) L1SpawnUtil.spawnnpc(33048 + randx, 32866 + randy, (short) _mapnum, boxid, 0, 0, 0); // 三層寶物倉庫
		}

	}

	private void spawnGlass() {
		for (int i = 0; i < 8; i++) {
			_glass[i] = (L1NpcInstance) L1SpawnUtil.spawnnpc(32827, 32846 - (i * 3), (short) _mapnum, 7800226, 0, 0, 0); // 上排
			_glass[i + 8] = (L1NpcInstance) L1SpawnUtil.spawnnpc(32824, 32846 - (i * 3), (short) _mapnum, 7800226, 0, 0, 0); // 下排
		}
		for (int i = 0; i < 16; i++) {
			_glass[i].setFloorOpenStatus(true);
		}
	}


	private void CircleSpawn(){
		circlenum = 4;  // 基本 4 個
		if (time <= 14 * 60) {    // 通關時間 14 分鐘 +4
			circlenum += 4;
		}
		if (time <= 12 * 60) {    // 通關時間 12 分鐘 +4
			circlenum += 4;
		}
		if (time <= 10 * 60) {    // 通關時間 10 分鐘 +4
			circlenum += 4;
		}
		if (circlenum >= 4){
			for (int i = 0; i < 4; i++){
				_circle[i] = L1SpawnUtil.spawn4(33001 + (i * 3), 32829, (short) _mapnum, 6, 7800227, 0, 0, 0, false);
			}
		}
		if (circlenum >= 8){
			for (int i = 4; i < 8; i++){
				_circle[i] = L1SpawnUtil.spawn4(33017, 32843 - ((i - 4) * 3), (short) _mapnum, 6, 7800227, 0, 0, 0, false);
			}
		}
		if (circlenum >= 12){
			for (int i = 8; i < 12; i++){
				_circle[i] = L1SpawnUtil.spawn4(33009 - ((i - 8) * 3), 32807, (short) _mapnum, 6, 7800227, 0, 0, 0, false);
			}
		}
		if (circlenum == 16){
			for (int i = 12; i < 16; i++){
				_circle[i] = L1SpawnUtil.spawn4(32993, 32813 + ((i - 12) * 3), (short) _mapnum, 6, 7800227, 0, 0, 0, false);
			}
		}
/*		for (int i = 0; i<16; i++) {
			if (_circle[i] != null) {
				System.out.println(i+"+"+_circle[i].getX()+"+"+_circle[i].getY()+"+"+_circle[i].getMapId());
			}
		}
		System.out.println(circlenum);*/
	}


	private void CircleDelete(){
		for (int i = 0; i < 16; i++){
			if (_circle[i] == null){
				break;
			}
			for (L1PcInstance pc : getPlayMemberArray()){
				if (pc.getX() == _circle[i].getX() && pc.getY() == _circle[i].getY() && (circletime+20 <= time)){
					pc.start_teleport(33047 + rnd.nextInt(2), 32857 + rnd.nextInt(2), _mapnum, 5, 18339, true);
				}
			}
			for (int wd = 0; wd < 16; wd++) {
				if(_circle[wd] != null){
					_circle[wd].deleteMe();
				}
			}
		}
	}
	private void quit() {
		HOME_TELEPORT();
		Object_Delete();
		Luun_Secret_System.getInstance().Reset(_mapnum);
		_close = true;
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
}
