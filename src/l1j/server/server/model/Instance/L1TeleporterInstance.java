package l1j.server.server.model.Instance;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.MJTempleantique.MJempleantiqueController;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.templates.L1Npc;

public class L1TeleporterInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	public L1TeleporterInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		L1Attack attack = new L1Attack(player, this);
		attack.calcHit();
		attack.action();
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		if (player == null || this == null)
			return;
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(
				getNpcTemplate().get_npcId());
		int npcid = getNpcTemplate().get_npcId();
		L1Quest quest = player.getQuest();
		String htmlid = null;

		if (talking != null) {
			switch(npcid){
				case 50014: // 迪隆
					if (player.isWizard()) { // 法師
						if (quest.get_step(L1Quest.QUEST_LEVEL30) == 1
								&& !player.getInventory().checkItem(40579)) { // 不死族的骨頭
							htmlid = "dilong1";
						} else {
							htmlid = "dilong3";
						}
					}
					break;
				case 70779: // 螞蟻之門
					if (player.getCurrentSpriteId() == 1037) { // 巨人蟻變身
						htmlid = "ants3";
					} else if (player.getCurrentSpriteId() == 1039) { // 巨人蟻士兵變身
						if (player.isCrown()) { // 君主
							if (quest.get_step(L1Quest.QUEST_LEVEL30) == 1) {
								if (player.getInventory().checkItem(40547)) { // 居民的遺物
									htmlid = "antsn";
								} else {
									htmlid = "ants1";
								}
							} else { // 非Step1
								htmlid = "antsn";
							}
						} else { // 非君主
							htmlid = "antsn";
						}
					}
					break;
				case 70853: // 精靈公主
					if (player.isElf()) { // 精靈
						if (quest.get_step(L1Quest.QUEST_LEVEL30) == 1) {
							if (!player.getInventory().checkItem(40592)) { // 被詛咒的精靈書
								Random random = new Random(System.nanoTime());
								if (random.nextInt(100) < 50) { // 50%機會進入黑暗馬爾丹地牢
									htmlid = "fairyp2";
								} else { // 黑暗精靈地下監獄
									htmlid = "fairyp1";
								}
							}
						}
					}
					break;
				case 50031: // 塞皮亞
					if (player.isElf()) { // 精靈
						if (quest.get_step(L1Quest.QUEST_LEVEL45) == 2) {
							if (!player.getInventory().checkItem(40602)) { // 藍色長笛
								htmlid = "sepia1";
							}
						}
					}
					break;
				case 50043:
					if (quest.get_step(L1Quest.QUEST_LEVEL50) == L1Quest.QUEST_END) {
						htmlid = "ramuda2";
					} else if (quest.get_step(L1Quest.QUEST_LEVEL50) == 1) { // 迪卡爾丁同意已完成
						if (player.isCrown()) { // 君主
							if (_isNowDely) { // 傳送延遲中
								htmlid = "ramuda4";
							} else {
								htmlid = "ramudap1";
							}
						} else { // 非君主
							htmlid = "ramuda1";
						}
					} else {
						htmlid = "ramuda3";
					}
					break;
				case 50082: // 歌唱之島傳送者
					if (player.getLevel() < 13) {
						htmlid = "en0221";
					} else {
						if (player.isElf()) {
							htmlid = "en0222e";
						} else if (player.isDarkelf()) {
							htmlid = "en0222d";
						} else {
							htmlid = "en0222";
						}
					}
					break;
				case 50001: // 바르니아
					if (player.isElf()) {
						htmlid = "barnia3";
					} else if (player.isKnight() || player.isCrown() || player.is전사() || player.isFencer()) {
						htmlid = "barnia2";
					} else if (player.isWizard() || player.isDarkelf()) {
						htmlid = "barnia1";
					}
					break;
				case 50056://메트
					if (player.getLevel() < 45){//숨계
						htmlid = "telesilver4";
					} else if (player.getLevel() >= 99 && player.getLevel() <= 99){//폭풍수련지역
						htmlid = "telesilver5";
					} else {
						htmlid = "telesilver1";
					}
					break;
				case 50020: // 斯坦利
				case 50024: // 阿斯特
				case 50036: // 威爾瑪
				case 5069: // 林茲
				case 50039: // 萊斯利
				case 50044: // 西里斯
				case 50046: // 埃勒里斯
				case 50051: // 基里烏斯
				case 50054: // 特雷
				case 50066: // 里奧爾
				case 7320051: // 吉夫蘭
					if (player.getLevel() < 45){
						htmlid = "starttel1";
					} else if (player.getLevel() >= 45 && player.getLevel() <= 51){
						htmlid = "starttel2";
					} else {
						htmlid = "starttel3";
					}
					break;
				default:
					break;
			}
			// html 顯示
			if (htmlid != null) { // 如果指定了 htmlid
				player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
			} else {
				if (player.getLawful() < -1000) { // 玩家是混亂狀態
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
				} else {
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
				}
			}
		} else {
			if(npcid == 120718 && MJempleantiqueController.getInstance().isOpen) {
				if(MJempleantiqueController.templeantique.clanMode) {
					if(ClanTable.getInstance().TempleantiqueClanId == player.getClanId()) {
						MJPoint pt = MJPoint.newInstance(32616, 32927, 5, (short) 1209, 50);
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(player, pt.x, pt.y, pt.mapId, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_DOM_TOWER);
					} else {
						player.sendPackets("\\aH古代神的神廟僅限攻略過支配之塔死神格林里普的血盟使用。");
						player.sendPackets("\\aH無血攻略時僅限無血使用。");
					}
				} else {
					MJPoint pt = MJPoint.newInstance(32616, 32927, 5, (short) 1209, 50);
					SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(player, pt.x, pt.y, pt.mapId, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_DOM_TOWER);
				}
			/*} else {
				System.out.println("No actions for npc id : ");
				_log.finest((new StringBuilder()).append("No actions for npc id : ").append(objid).toString());*/
			}
		}
	}

	// teleportURL
	private static final HashMap<Integer, String[]> _teleportPrice = new HashMap<Integer, String[]>();
	private static final String[]					_teleportPriceDummy = new String[]{""};
	static{
		// 說話之島 盧卡
		_teleportPrice.put(50015,     new String[]{"1500"});
		// 說話之島 凱斯
		_teleportPrice.put(50017,     new String[]{"50"});
		// 肯特 斯坦利
		_teleportPrice.put(50020,     new String[] { "50", "50", "120", "120", "50", "180", "120", "120", "180", "200", "200", "420", "600", "1155", "7100" });
		// 古魯丁 亞斯特
		_teleportPrice.put(50024,     new String[] { "132", "55", "198", "55", "132", "264", "55", "7777", "7777", "198", "264", "220", "220", "420", "550", "1155", "7480" });
		// 奇岩 威爾瑪
		_teleportPrice.put(50036,     new String[] { "126", "126", "52", "189", "52", "52", "189", "126", "126", "315", "315", "420", "735", "1155", "875" });
		// 奇岩市場 林茲
		_teleportPrice.put(5069,     new String[] { "126", "126", "52", "189", "52", "52", "189", "126", "126", "315", "315", "420", "735", "1155", "875" });
		// 火焰之谷 吉夫蘭
		_teleportPrice.put(7320051,    new String[] { "126", "126", "52", "189", "52", "52", "189", "126", "126", "315", "315", "420", "735", "1155", "875" });
		// 威登 萊斯利
		_teleportPrice.put(50039,     new String[] { "185", "185", "123", "247", "51", "123", "247", "51", "185", "420", "412", "412", "824", "1155", "7931" });
		String[] ss = new String[]{ "259","129","194","129","54","324","194","259","420", "450", "540","540","972","1155", "7992" };
		_teleportPrice.put(50039,     new String[] { "185", "185", "123", "247", "51", "123", "247", "51", "185", "420", "412", "412", "824", "1155", "7931" });
		String[] ss = new String[]{ "259","129","194","129","54","324","194","259","420", "450", "540","540","972","1155", "7992" };
		// 亞丁 西里斯
		_teleportPrice.put(50044,     ss);
		// 亞丁 埃勒里斯
		_teleportPrice.put(50046,     ss);
		// 歐瑞 基里烏斯
		_teleportPrice.put(50051,     new String[] { "240", "240", "180", "300", "120", "180", "300", "50", "240", "420", "500", "500", "900", "1155", "8000" });
		// 風木 特雷
		_teleportPrice.put(50054,     new String[] { "50", "50", "120", "120", "180", "180", "180", "240", "240", "300", "200", "200", "420", "500", "6500" });
		// 銀騎士村 梅特
		_teleportPrice.put(50056,     new String[] {"55","55","55","132","132","132","198","198","270","7777","7777","246","420","770", "7480" });
		// 海音 里奧爾
		_teleportPrice.put(50066,     new String[] { "180", "50", "120", "120", "50", "50", "240", "120", "180", "420", "400", "400",    "800", "1155", "7100" });
		// 迪亞諾斯
		_teleportPrice.put(50068,     new String[] { "1500", "800", "600", "1800", "1800", "1000", "300" });
		// 空間傳送師 迪亞魯茲
		_teleportPrice.put(50072,     new String[] { "2200", "1800", "1000", "1600", "2200", "1200", "1300", "2000", "2000" });
		// 空間傳送師 迪亞貝斯 // 未使用
		_teleportPrice.put(50073,     new String[] { "380", "850", "290", "290", "290", "180", "480", "150", "150", "380", "480", "380", "850" });
		// 魔法師 丹尼爾
		_teleportPrice.put(50079,     new String[] { "550", "550", "600", "550", "700", "600", "600", "750", "750", "550", "550", "700", "650" });
		// 德卡比亞 貝希摩斯
		_teleportPrice.put(3000005, new String[] { "50", "50", "50", "50", "120", "120", "180", "180", "180", "240", "240", "400", "400", "800", "7700" });
		// 西貝利亞 夏里爾
		_teleportPrice.put(3100005, new String[] { "50", "50", "50", "120", "180", "180", "240", "240", "240", "300", "300", "500", "500", "900", "8000" });
		ss = new String[]{ "0","0","0"};
		// 格魯丁市場⇒奇岩市場, 歐瑞市場, 銀騎士村市場
		_teleportPrice.put(50026,     ss);
		// 奇岩市場⇒格魯丁市場, 歐瑞市場, 銀騎士村市場
		_teleportPrice.put(50033,     ss);
		// 歐瑞市場⇒格魯丁市場, 奇岩市場, 銀騎士村市場
		_teleportPrice.put(50049,     ss);
		// 銀騎士村市場⇒格魯丁市場, 奇岩市場, 歐瑞市場
		_teleportPrice.put(50059,     ss);
		// 宮廷首席管家 瑪門
		_teleportPrice.put(6000014, new String[]{"14000"});
		// 神女 弗洛拉
		_teleportPrice.put(6000016, new String[]{"1000"});
		// 象牙塔 皮特
		_teleportPrice.put(900056,     new String[]{"7000","7000","7000","14000","14000"});
		// 艾露娜 [ 精靈之森傳送者 ]
		_teleportPrice.put(5091, 	new String[]{ "57", "57", "57", "138", "138", "138", "138", "207", "207", "230", "230", "690" });
	}

	static class HtmlPricePair{
		HtmlPricePair(String h, String[] p){
			html = h;
			price= p;
		}
		String 		html;
		String[] 	price;
	}

	// teleportURLA
	private static final HashMap<Integer, HtmlPricePair> 		_teleportPriceA 		= new HashMap<Integer, HtmlPricePair>();
	private static final HtmlPricePair							_teleportPriceDummyA	= new HtmlPricePair("", new String[]{""});
	static{
		// 다니엘
		_teleportPriceA.put(50079, 		new HtmlPricePair("telediad3", 	new String[]{ "700","800","800","1000" }));
		// 데카비아
		_teleportPriceA.put(3000005, 	new HtmlPricePair("dekabia3", 	new String[]{ "100","220","220","220","330","330","330","330","440","440" }));
		// 샤리엘
		_teleportPriceA.put(3100005, 	new HtmlPricePair("sharial",	new String[]{ "220","330","330","330","440","440","550","550","550","550" }));
	}

	// teleportURLL
	private static final HashMap<Integer, HtmlPricePair> 		_teleportPriceL 		= new HashMap<Integer, HtmlPricePair>();
	private static final HtmlPricePair							_teleportPriceDummyL	= new HtmlPricePair("telesilver3", new String[] { "780","780","780","780","780","1230","1080","1080","1080","1080" });
	static{
		// 梅特
		_teleportPriceL.put(50056, new HtmlPricePair("guide_0_1", new String[]{ "30","30","30", "70", "80", "90","100", "30" }));

		HtmlPricePair p = new HtmlPricePair("guide_6", new String[]{ "500","500" });
		// 斯坦利
		_teleportPriceL.put(50020, p);
		// 阿斯特
		_teleportPriceL.put(50024, p);
		// 威爾瑪
		_teleportPriceL.put(50036, p);
		// 林茲
		_teleportPriceL.put(5069, p);
			// 萊斯利
		_teleportPriceL.put(50039, p);
		// 西里斯
		_teleportPriceL.put(50044, p);
		//埃勒里斯
		_teleportPriceL.put(50046, p);
		// 基里烏斯
		_teleportPriceL.put(50051, p);
		// 特雷
		_teleportPriceL.put(50054, p);
		// 里奧爾
		_teleportPriceL.put(50066, p);
		_teleportPriceL.put(5069, p);
		_teleportPriceL.put(7320051, p);
	}

	// teleportURLM
	private static final HashMap<Integer, HtmlPricePair> 		_teleportPriceM 		= new HashMap<Integer, HtmlPricePair>();
	private static final HtmlPricePair							_teleportPriceDummyM	= new HtmlPricePair("", new String[]{""});
	static{
		_teleportPriceM.put(50056, new HtmlPricePair("hp_storm1", new String[]{""}));
		HtmlPricePair pair = new HtmlPricePair("guide_7", new String[]{ "500","500","500","500","500","500","500","500","500","500","500" });
		// 스텐리
		_teleportPriceM.put(50020, pair);
		// 아스터
		_teleportPriceM.put(50024, pair);
		// 윌마
		_teleportPriceM.put(50036, pair);
		// 린지
		_teleportPriceM.put(5069, pair);
		// 레슬리
		_teleportPriceM.put(50039, pair);
		// 시리우스
		_teleportPriceM.put(50044, pair);
		// 엘레리스
		_teleportPriceM.put(50046, pair);
		// 키리우스
		_teleportPriceM.put(50051, pair);
		// 트레이
		_teleportPriceM.put(50054, pair);
		// 리올
		_teleportPriceM.put(50066, pair);
		_teleportPriceM.put(5069, pair);
		//지프란
		_teleportPriceM.put(7320051, pair);
	}

	// other
	private static final HashMap<String, HtmlPricePair> 		_teleportPriceOther 	= new HashMap<String, HtmlPricePair>();
	static{
		_teleportPriceOther.put("teleportURLB", new HtmlPricePair("guide_1_1", new String[]{ "450","450","450","450" }));
		_teleportPriceOther.put("teleportURLC", new HtmlPricePair("guide_1_2", new String[]{ "465","465","465","465","1065","1065" }));
		_teleportPriceOther.put("teleportURLD", new HtmlPricePair("guide_1_3", new String[]{ "480","480","480","480","630","1080","630" }));
		_teleportPriceOther.put("teleportURLE", new HtmlPricePair("guide_2_1", new String[]{ "600","600","750","750" }));
		_teleportPriceOther.put("teleportURLF", new HtmlPricePair("guide_2_2", new String[]{ "615","615","915","765" }));
		_teleportPriceOther.put("teleportURLG", new HtmlPricePair("guide_2_3", new String[]{ "630","780","630","1080","930" }));
		_teleportPriceOther.put("teleportURLH", new HtmlPricePair("guide_3_1", new String[]{ "750","750","750","1200","1050" }));
		_teleportPriceOther.put("teleportURLI", new HtmlPricePair("guide_3_2", new String[]{ "765","765","765","765","1515","1215","915" }));
		_teleportPriceOther.put("teleportURLJ", new HtmlPricePair("guide_3_3", new String[]{ "780","780","780","780","780","1230","1080" }));
		_teleportPriceOther.put("teleportURLK", new HtmlPricePair("guide_4",   new String[]{ "780","780","780","780","780","1230","1080" }));
		_teleportPriceOther.put("teleportURLO", new HtmlPricePair("guide_8",   new String[]{ "750" }));
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
		if (this == null || player == null)
			return;
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		if (action.equalsIgnoreCase("teleportURL")) {
			L1NpcHtml html = new L1NpcHtml(talking.getTeleportURL());
			String[] price = null;
			int npcid = getNpcTemplate().get_npcId();
			price = _teleportPrice.get(npcid);
			if(price == null) price = _teleportPriceDummy;
			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
		} else if (action.equalsIgnoreCase("teleportURLA")) {
			int npcid = getNpcTemplate().get_npcId();
			HtmlPricePair 		pair = _teleportPriceA.get(npcid);
			if(pair == null) 	pair = _teleportPriceDummyA;
			player.sendPackets(new S_NPCTalkReturn(objid, pair.html, pair.price));
		}else if (action.equalsIgnoreCase("teleportURLL")){
			int npcid = getNpcTemplate().get_npcId();
			HtmlPricePair 		pair = 	_teleportPriceL.get(npcid);
			if(pair == null) 	pair =	_teleportPriceDummyL;
			player.sendPackets(new S_NPCTalkReturn(objid, pair.html, pair.price));
		} else if (action.equalsIgnoreCase("teleportURLM")){
			int npcid = getNpcTemplate().get_npcId();
			HtmlPricePair		pair =	_teleportPriceM.get(npcid);
			if(pair == null)	pair =	_teleportPriceDummyM;
			player.sendPackets(new S_NPCTalkReturn(objid, pair.html, pair.price));
		} else {
			HtmlPricePair 		pair =	_teleportPriceOther.get(action);
			if(pair != null)
				player.sendPackets(new S_NPCTalkReturn(objid, pair.html, pair.price));
		}

		if (action.startsWith("teleport")) {
			_log.finest((new StringBuilder()).append("Setting action to : ").append(action).toString());
			doFinalAction(player, action);
		}
	}

	private void doFinalAction(L1PcInstance player, String action) {
		if (this == null || player == null)
			return;
		int objid = getId();

		int npcid = getNpcTemplate().get_npcId();
		String htmlid = null;
		boolean isTeleport = true;

		if (npcid == 50014) { // 디 론
			if (!player.getInventory().checkItem(40581)) { // 안 데드의 키
				isTeleport = false;
				htmlid = "dilongn";
			}
		} else if (npcid == 50043) { // Lambda
			if (_isNowDely) { // 텔레포트 지연중
				isTeleport = false;
			}
		} else if (npcid == 50625) { // 고대인(Lv50 퀘스트 고대의 공간 2 F)
			if (_isNowDely) { // 텔레포트 지연중
				isTeleport = false;
			}
		}

		if (isTeleport) { // 텔레포트 실행
			try {
				//  뮤탄트안트단젼(군주 Lv30 퀘스트)
				if (action.equalsIgnoreCase("teleport mutant-dungen_la")) {
					// 3 매스 이내의 Pc
					for (L1PcInstance otherPc : L1World.getInstance()
							.getVisiblePlayer(player, 3)) {
						if (otherPc.getClanid() == player.getClanid()
								&& otherPc.getId() != player.getId()) {
							otherPc.start_teleport(32740, 32800, 217, 5, 18339, true, false);
						}
					}
					player.start_teleport(32740, 32800, 217, 5, 18339, true, false);
				}
				// 시련의 지하 감옥(위저드 Lv30 퀘스트)
				else if (action.equalsIgnoreCase("teleport mage-quest-dungen_la")) {
					player.start_teleport(32791, 32788, 201, 5, 18339, true, false);
				} else if (action.equalsIgnoreCase("teleport 29_la")) { // Lambda
					L1PcInstance kni = null;
					L1PcInstance elf = null;
					L1PcInstance wiz = null;
					// 3 매스 이내의 Pc
					L1Quest quest = null;
					for (L1PcInstance otherPc : L1World.getInstance()
							.getVisiblePlayer(player, 3)) {
						quest = otherPc.getQuest();
						if (otherPc.isKnight() // 나이트
								&& quest.get_step(L1Quest.QUEST_LEVEL50) == 1) { // 디가르딘 동의가 끝난 상태
							if (kni == null) {
								kni = otherPc;
							}
						} else if (otherPc.isElf() // 요정
								&& quest.get_step(L1Quest.QUEST_LEVEL50) == 1) { // 디가르딘 동의가 끝난 상태
							if (elf == null) {
								elf = otherPc;
							}
						} else if (otherPc.isWizard() // 마법사
								&& quest.get_step(L1Quest.QUEST_LEVEL50) == 1) { // 디가르딘 동의가 끝난 상태
							if (wiz == null) {
								wiz = otherPc;
							}
						}
					}
					if (kni != null && elf != null && wiz != null) { // 전클래스 갖추어져 있다
						player.start_teleport(32723, 32850, 2000, 2, 18339, true, false);
						kni.start_teleport(32750, 32851, 2000, 6, 18339, true, false);
						elf.start_teleport(32878, 32980, 2000, 6, 18339, true, false);
						wiz.start_teleport(32876, 33003, 2000, 0, 18339, true, false);

						_isNowDely = true;
						TeleportDelyTimer timer = new TeleportDelyTimer();
						GeneralThreadPool.getInstance().schedule(timer, 900000);
					}
				} else if (action.equalsIgnoreCase("teleport barlog_la")) { // 고대인(Lv50 퀘스트 고대의 공간 2 F)
					player.start_teleport(32755, 32844, 2002, 5, 18339, true, false);
					TeleportDelyTimer timer = new TeleportDelyTimer();
					GeneralThreadPool.getInstance().execute(timer);
				}

			} catch (Exception e) {
			}
		}
		if (htmlid != null) { // 표시하는 html가 있는 경우
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
		}
	}

	class TeleportDelyTimer implements Runnable {

		public TeleportDelyTimer() {

		}

		public void run() {
			_isNowDely = false;
		}
	}

	private boolean _isNowDely = false;

	private static Logger _log = Logger.getLogger(l1j.server.server.model.Instance.L1TeleporterInstance.class.getName());

}