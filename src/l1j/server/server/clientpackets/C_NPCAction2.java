package l1j.server.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;

import java.util.Random;

import l1j.server.Config;
import l1j.server.IndunSystem.Training.BossTrainingSystem;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.ActionCodes;
import l1j.server.server.server.datatables.ExpTable;
import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.server.datatables.KeyTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1CataInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.L1SpawnUtil;

public class C_NPCAction2 {

	private static C_NPCAction2 _instance;

	private static Random _random = new Random(System.nanoTime());

	public static C_NPCAction2 getInstance() {
		if (_instance == null) {
			_instance = new C_NPCAction2();
		}
		return _instance;
	}

	int[] materials = null;
	int[] counts = null;

	public String NpcAction(L1PcInstance pc, L1Object obj, String s, String htmlid) {
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		try {
			if (npcid == 200201) {// 遇見的石巨人
				if (s.equalsIgnoreCase("A")) {
					if (pc.getInventory().checkEnchantItem(5, 7, 1) && pc.getInventory().checkEnchantItem(6, 7, 1)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().consumeEnchantItem(5, 7, 1);
						pc.getInventory().consumeEnchantItem(6, 7, 1);
						pc.getInventory().consumeItem(41246, 3000);

						pc.getInventory().storeItem(602, 1);
						htmlid = "joegolem9";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 狂風的斧頭
				if (s.equalsIgnoreCase("B")) {
					if (pc.getInventory().checkEnchantItem(145, 7, 1) && pc.getInventory().checkEnchantItem(148, 7, 1)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().consumeEnchantItem(145, 7, 1);
						pc.getInventory().consumeEnchantItem(148, 7, 1);
						pc.getInventory().consumeItem(41246, 30000);

						pc.getInventory().storeItem(605, 1);
						htmlid = "joegolem10";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 毀滅的大劍
				if (s.equalsIgnoreCase("C")) {
					if (pc.getInventory().checkEnchantItem(52, 7, 1) && pc.getInventory().checkEnchantItem(64, 7, 1)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().consumeEnchantItem(52, 7, 1);
						pc.getInventory().consumeEnchantItem(64, 7, 1);
						pc.getInventory().consumeItem(41246, 30000);

						pc.getInventory().storeItem(601, 1);
						htmlid = "joegolem11";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 大法師的法杖
				if (s.equalsIgnoreCase("D")) {
					if (pc.getInventory().checkEnchantItem(125, 7, 1) && pc.getInventory().checkEnchantItem(129, 7, 1)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().consumeEnchantItem(125, 7, 1);
						pc.getInventory().consumeEnchantItem(129, 7, 1);
						pc.getInventory().consumeItem(41246, 30000);

						pc.getInventory().storeItem(603, 1);
						htmlid = "joegolem12";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 혹한의 창
				if (s.equalsIgnoreCase("E")) {
					if (pc.getInventory().checkEnchantItem(99, 7, 1) && pc.getInventory().checkEnchantItem(104, 7, 1)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().consumeEnchantItem(99, 7, 1);
						pc.getInventory().consumeEnchantItem(104, 7, 1);
						pc.getInventory().consumeItem(41246, 30000);

						pc.getInventory().storeItem(604, 1);
						htmlid = "joegolem13";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 뇌신검
				if (s.equalsIgnoreCase("F")) {
					if (pc.getInventory().checkEnchantItem(32, 7, 1) && pc.getInventory().checkEnchantItem(42, 7, 1)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().consumeEnchantItem(32, 7, 1);
						pc.getInventory().consumeEnchantItem(42, 7, 1);
						pc.getInventory().consumeItem(41246, 30000);

						pc.getInventory().storeItem(600, 1);
						htmlid = "joegolem14";
					} else {
						htmlid = "joegolem15";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200018) {// 경험치
				if (s.equalsIgnoreCase("0")) {// 한번씩 지급
					if (pc.getLevel() < 51) {
						pc.add_exp_for_ready((ExpTable.getExpByLevel(51) - 1) - pc.get_exp() + ((ExpTable.getExpByLevel(51) - 1) / 100L));
					} else if (pc.getLevel() >= 51 && pc.getLevel() < Config.ServerAdSetting.Expreturn) {
						pc.add_exp_for_ready((ExpTable.getExpByLevel(pc.getLevel() + 1) - 1) - pc.get_exp() + 100L);
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
					}
				} else if (s.equalsIgnoreCase("1")) {// 한번에 지급
					if (pc.getLevel() >= Config.ServerAdSetting.Expreturn && pc.getLevel() <= Config.ServerAdSetting.Expreturn) {
						pc.add_exp_for_ready(
								(ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) - pc.get_exp() + ((ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) / 30000000));
					} else if (pc.getLevel() <= Config.ServerAdSetting.Expreturn && pc.getLevel() < Config.ServerAdSetting.Expreturn) {
						pc.add_exp_for_ready(
								(ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) - pc.get_exp() + ((ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) / 30000000));
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
					}
				} else if (s.equalsIgnoreCase("2")) { // 新規支援
					if (pc.getLevel() >= Config.ServerAdSetting.NewCha) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("新規不是因此，無法再支援。")));
						return htmlid;
					} else if (pc.getInventory().checkItem(7241, 1) || pc.getInventory().checkItem(1000004, 1)) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("無法重複給予。")));
						return htmlid;
					}
					addSealedItem(pc, 7241, 5, 0, 1, 0, true); // 托帕石
					addSealedItem(pc, 3000231, 3, 0, 1, 0, true); // 最高級大達
					addSealedItem(pc, 1000007, 10, 0, 1, 0, true); // 高級大達
				}
				/** 紅色騎士團成員加入德波羅祖 */
				if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5163) {
					if (s.equalsIgnoreCase("j")) { // 加入
						pc.sendPackets(String.valueOf(new S_ServerMessage(3854)));
						return htmlid;
					} else if (s.equalsIgnoreCase("d")) { // 退出
						if (pc.getRedKnightClanId() == 0) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("您尚未加入。")));
							return htmlid;
						}

						pc.setRedKnightClanId(0);
						pc.sendPackets(String.valueOf(new S_SystemMessage("您已退出紅色騎士團。")));
						pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, false);
						return htmlid;
					}
				}

				/** 凱撒 */
			} else if (npcid == 7000079) {
				if (s.equalsIgnoreCase("1")) { // 租借
					int countActiveMaps = BossTrainingSystem.getInstance().countRaidPotal();
					if (pc.getInventory().checkItem(80500)) {
						htmlid = "bosskey6";
						// 您似乎已經擁有訓練所鑰匙。
						// 為了讓更多人使用，每人只能租借一把訓練所鑰匙。
					} else if (countActiveMaps >= 99) {
						htmlid = "bosskey3";
						// 抱歉。
						// 現在所有訓練所都在進行訓練。
					} else {
						htmlid = "bosskey4";
					}
				} else if (s.matches("[2-4]")) {
					if (!pc.getInventory().checkItem(80500)) { // 防止動作操作
						L1ItemInstance item = null;
						int count = 0;
						if (s.equalsIgnoreCase("2")) { // 4個
							count = 4;
						} else if (s.equalsIgnoreCase("3")) { // 8個
							count = 8;
						} else if (s.equalsIgnoreCase("4")) { // 16個
							count = 16;
						}
						if (pc.getInventory().consumeItem(40308, count * 300)) {
							int id = BossTrainingSystem.getInstance().blankMapId();
							BossTrainingSystem.getInstance().startRaid(pc, id);
							for (int i = 0; i < count; i++) {
								item = pc.getInventory().storeItem(80500, 1);
								item.setKeyId(id);
								if (KeyTable.checkey(item)) {
									KeyTable.DeleteKey(item);
									KeyTable.StoreKey(item);
								} else {
									KeyTable.StoreKey(item);
								}
							}
							htmlid = "bosskey7";
							// 請將訓練所鑰匙分給一起訓練的人，然後向我展示，我會引導您進入訓練所。
							// 訓練所的租借時間最多為4小時，即使在訓練中，租借時間結束後也會停止使用，以便下一個人使用。
							// 召喚訓練用怪物時，請隨時確認訓練所的剩餘使用時間。
						} else {
							htmlid = "bosskey5";
							// 抱歉，如果不支付使用費，我們無法借給您訓練所。
							// 單靠亞登王國的資助，管理這麼多的訓練所並非易事。
						}
					} else {
						htmlid = "bosskey6";
						// 看起來您已經擁有訓練所的鑰匙了。
						// 為了讓更多人能夠使用，每個人只能租借一個訓練所。
					}
				} else if (s.equalsIgnoreCase("6")) { // 입장
					int countActiveMaps = BossTrainingSystem.getInstance().countRaidPotal();
					if (countActiveMaps < 100) {
						L1ItemInstance item = pc.getInventory().findItemId(80500);
						if (item != null) {
							int id = item.getKeyId();
							pc.start_teleport(32901, 32814, id, pc.getHeading(), 18339, true, false);
						} else {
							htmlid = "bosskey2";
						}
					} else {
						htmlid = "bosskey3";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7200022) {// 生日助手
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (pc.isInvisble()) {
					pc.sendPackets(String.valueOf(new S_NpcChatPacket(npc, "無法在隱形狀態下接收。", 0)));
					return htmlid;
				}
				if (s.equalsIgnoreCase("a")) {
					htmlid = "birthday6";
				}
				if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().consumeItem(3000048, 1)) {
						new L1SkillUse().handleCommands(pc, L1SkillId.COMA_B, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_SPELLSC);
						htmlid = "birthday4";
					} else {
						htmlid = "birthday6";
					}
				}
				
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 130005) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (s.equalsIgnoreCase("1")) { // 死靈法師
					if (pc.getInventory().consumeItem(80464, 1)) {
						L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45456, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[死靈法師]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("2")) {// 데스나이트
					if (pc.getInventory().consumeItem(80465, 1)) {
						L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45601, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[死亡騎士]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("3")) { // 惡魔
					if (pc.getInventory().consumeItem(80450, 1)) {
						L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45649, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[惡魔]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("4")) { // 不死鳥
					if (pc.getInventory().consumeItem(80479, 1)) {
						L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45617, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[不死鳥]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("delcall")) {
					for (L1Object objnpc : L1World.getInstance().getVisibleObjects(pc.getMapId()).values()) {
						if (objnpc instanceof L1NpcInstance) {
							L1NpcInstance boss = (L1NpcInstance) objnpc;
							if (boss.getNpcId() == 45456 || boss.getNpcId() == 45601 || boss.getNpcId() == 45649 || boss.getNpcId() == 45617) {
								boss.deleteMe();
							}
						}
					}
				}
			}
			/** 塞西莉亞 */
			else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000080) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (s.equalsIgnoreCase("A")) { // 象牙塔怪物
					if (pc.getInventory().consumeItem(80466, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 900076, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[哈丁的分身]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("B")) { // 象牙塔怪物
					if (pc.getInventory().consumeItem(80467, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 900070, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[黑魔法師]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("C")) { // 象牙塔怪物
					if (pc.getInventory().consumeItem(80450, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45649, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[惡魔]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("D")) { // 象牙塔怪物
					if (pc.getInventory().consumeItem(80451, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45685, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[塔拉克]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				/** 拉斯塔巴德怪物 **/
				if (s.equalsIgnoreCase("E")) {
					if (pc.getInventory().consumeItem(80452, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45955, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[凱娜]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("F")) {
					if (pc.getInventory().consumeItem(80453, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45959, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[伊迪亞]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("G")) {
					if (pc.getInventory().consumeItem(80454, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45956, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[比亞塔斯]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("H")) {
					if (pc.getInventory().consumeItem(80455, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45957, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[巴羅梅斯]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("I")) {
					if (pc.getInventory().consumeItem(80456, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45960, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[提亞梅斯]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("J")) {
					if (pc.getInventory().consumeItem(80457, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45958, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[恩迪亞斯]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("K")) {
					if (pc.getInventory().consumeItem(80458, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45961, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[拉米亞斯]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("L")) {
					if (pc.getInventory().consumeItem(80459, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45962, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[巴羅德]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("M")) {
					if (pc.getInventory().consumeItem(80460, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45676, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[海爾拜因]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("N")) {
					if (pc.getInventory().consumeItem(80461, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45677, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[萊亞]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("O")) {
					if (pc.getInventory().consumeItem(80462, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45844, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[巴蘭卡]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("P")) {
					if (pc.getInventory().consumeItem(80463, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45648, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[斯雷夫]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
					/** 格魯迪歐檢查怪物 **/
				if (s.equalsIgnoreCase("Q")) {
					if (pc.getInventory().consumeItem(80464, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45456, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[死靈法師]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("S")) {
					if (pc.getInventory().consumeItem(80465, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45601, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[死騎士]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
					/** 傲慢之塔 **/
				if (s.equalsIgnoreCase("T")) {
					if (pc.getInventory().consumeItem(80468, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310015, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[扭曲的殘暴女王]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("U")) {
					if (pc.getInventory().consumeItem(80469, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310021, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[不信的預言者]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("V")) {
					if (pc.getInventory().consumeItem(80470, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310028, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[恐怖的吸血鬼]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("W")) {
					if (pc.getInventory().consumeItem(80471, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310034, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[死亡的殭屍領主]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("X")) {
					if (pc.getInventory().consumeItem(80472, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310041, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[地獄的美洲獅]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("Y")) {
					if (pc.getInventory().consumeItem(80473, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310046, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[不死的木乃伊領主]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("Z")) {
					if (pc.getInventory().consumeItem(80474, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310051, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[殘酷的伊莉絲]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().consumeItem(80475, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310056, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[黑暗的騎士瓦爾德]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().consumeItem(80476, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310061, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[不死的巫妖]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("c")) {
					if (pc.getInventory().consumeItem(80477, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310077, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[死神格林瑞帕]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("d")) {
					if (pc.getInventory().consumeItem(80478, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45600, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "[黑騎士團長庫茲]已經在中央被召喚出來了。", 2));
					} else {
						htmlid = "bosskey10";
					}
				}
				// 老舊的書堆
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 8502036) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(60032)) {
						pc.sendPackets(new S_ChatPacket(pc, "已經擁有老書的物品了。"));
						htmlid = "";
					} else {
						pc.getInventory().storeItem(60032, 1);
						htmlid = "oldbook2";
					}
				}
					// 如果對象是NPC且NPC ID為8502036，玩家輸入"a"時，
					// 檢查玩家是否已經有物品ID為60032的物品。
					// 如果有，向玩家發送一條聊天包，告訴他們已經有該物品。
					// 如果沒有，則在玩家的背包中新增這個物品，並且將htmlid設為"oldbook2"。

			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7210007) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getLevel() >= 60) {
						L1Quest quest = pc.getQuest();
						int questStep = quest.get_step(L1Quest.QUEST_HAMO);
						if (!pc.getInventory().checkItem(820000) && questStep != L1Quest.QUEST_END) {
							pc.getQuest().set_end(L1Quest.QUEST_HAMO);
							pc.getInventory().storeItem(820000, 1);// 漢的袋子
							htmlid = "";
						} else {
							htmlid = "hamo1";
						}
					} else {
						htmlid = "hamo3";
						pc.sendPackets(new S_SystemMessage("只有60級以上的角色才能接收。"));
					}
				}
					// 如果對象是NPC且NPC ID為7210007，玩家輸入"a"時，
					// 檢查玩家等級是否大於或等於60。
					// 如果是，獲取玩家的任務資訊並檢查任務步驟。
					// 如果玩家的背包中沒有物品ID為820000的物品，且任務步驟不是結束狀態，
					// 則將任務標記為結束並在玩家的背包中新增物品ID為820000的物品。
					// 如果不符合條件，則根據具體情況設置htmlid。
					// 如果玩家等級不足60，則設置htmlid為"hamo3"，並向玩家發送系統消息提示等級不足。
				/** 깃털마을 피아르 **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7310093) {
				if (s.equalsIgnoreCase("a")) {
//                    if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_SystemMessage("只有在使用PC房間優待券的時候才能進行的動作。"));
						htmlid = "pc_tell2";
						return htmlid;
					}
					if (pc.getMap().isEscapable() || pc.isGm()) {
						int rx = _random.nextInt(7);
						int ux = 32768 + rx;
						int uy = 32834 + rx; // 象牙塔
						pc.start_teleport(ux, uy, 622, pc.getHeading(), 18339, true, false);
					}
				}
						// 如果對象是NPC且NPC ID為7310093，玩家輸入"a"時，
						// 檢查玩家是否在PC房間效果中。
						// 如果不在PC房間效果中，向玩家發送系統消息提示並將htmlid設為"pc_tell2"，然後返回該htmlid。
						// 如果玩家位於可以逃脫的地圖或玩家是GM，則隨機在象牙塔附近的一個位置進行傳送。
						// rx是隨機生成的一個0到6之間的數字，ux和uy計算得出傳送目的地的座標。
						// 最後，使用pc.start_teleport方法執行傳送。
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70611
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70530) { // 속죄의성서
				int lawful;
				byte count = 0;
				lawful = 0;
				switch (s) {
				case "0":
					count = 1;
					lawful = 3000;
					break;
				case "1":
					count = 3;
					lawful = 9000;
					break;
				case "2":
					count = 5;
					lawful = 15000;
					break;
				case "3":
					count = 10;
					lawful = 30000;
					break;
				}

				if (pc.getLawful() > 32767 - lawful) {
					pc.sendPackets("無法恢復善惡值的狀態。");
					return null;
				}

				if (pc.getInventory().consumeItem(3000155, count)) {
					pc.addLawful(lawful);
					pc.sendPackets(new S_ServerMessage(674));
					pc.send_effect(8473);
					htmlid = "yuris2";
				} else {
					htmlid = "yuris3";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 73201236) {
				if (s.equalsIgnoreCase("a")) {
					L1SkillUse aa = new L1SkillUse();
					aa.handleCommands(pc, L1SkillId.DRAGON_HALPAS, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					pc.send_effect(15881);
					htmlid = "halpas_jaken1";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 8500339) {
				if (s.equalsIgnoreCase("a")) {
					L1SkillUse aa = new L1SkillUse();
					aa.handleCommands(pc, L1SkillId.miso_Buff, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					aa.handleCommands(pc, L1SkillId.miso_Buff1, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					aa.handleCommands(pc, L1SkillId.miso_Buff2, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					pc.send_effect(3944);
					htmlid = "";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50015) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 120836) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50024) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 9000) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(140100, 1)) {
						pc.getInventory().consumeItem(140100, 1);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(4158));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50082) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50054) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50056) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50020) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50036) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5069) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7320051) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50066) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50039) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50051) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50046) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50079) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3000005) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3100005) {
				if (s.equalsIgnoreCase("T_pcbang")) {
//					if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
					if (!pc.isPcBuff()) {
						pc.sendPackets(new S_ServerMessage(4487));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5126) {
				long time = System.currentTimeMillis();
				int useTime = 10 * 60 * 1000;

				if (pc.getBuffTime() + (useTime) > time) {
					long sec = ((pc.getBuffTime() + (useTime)) - time) / 1000;
					pc.sendPackets(new S_SystemMessage(sec + "秒後才能使用。"));
					return htmlid;
				}
				if (pc.getLevel() < 80) {
					pc.sendPackets(new S_SystemMessage("最低等級80才可以使用。"));
					return htmlid;
				}
				if (s.equals("0")) { // 接受魔法
					int[] allBuffSkill = { 4048 };
					pc.setBuffnoch(1);
					L1SkillUse l1skilluse = null;
					l1skilluse = new L1SkillUse();
					for (int i = 0; i < allBuffSkill.length; i++) {
						l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					}
					htmlid = "merisha2";
					pc.curePoison();
					pc.setBuffTime(time);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7310121
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7320030) {
				if (/*(pc.getClanRank() != L1Clan.TRAINEE) && */(pc.getClanRank() != L1Clan.GUARDIAN)
						&& (pc.getClanRank() != L1Clan.MEMBER) && (pc.getClanRank() != L1Clan.LEADER)
						&& (pc.getClanRank() != L1Clan.ELITE)) {
					pc.sendPackets(new S_SystemMessage("只有特定血盟成員才能接收。"));
					return htmlid;
				}
					// if (s.equals("a")) { // 接受魔法
					// if (!pc.getClan().decWarPoint()) {
					// pc.sendPackets(new S_SystemMessage("血盟點數不足。"));
					// return htmlid; }
					// }

				int[] allBuffSkill = { 14, 26, 42, 54, 48, 79, 160, 206, 211, 216, 158, 168 };
				// 提升敏捷、力量、武器祝福、自然的祝福
				pc.setBuffnoch(1);
				L1SkillUse l1skilluse = null;
				l1skilluse = new L1SkillUse();
				for (int i = 0; i < allBuffSkill.length; i++) {
					l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					// }
					pc.sendPackets(new S_SkillSound(pc.getId(), 830));
					pc.curePoison();
// 					pc.setBuffTime(time);// 增益延遲
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 6200008) {
				long time = System.currentTimeMillis();
				int useTime = 20 * 60 * 1000; // 20分鐘後
				if (pc.getBuffTime() + (useTime) > time) {
					long sec = ((pc.getBuffTime() + (useTime)) - time) / 1000;
					pc.sendPackets(new S_SystemMessage(sec + "秒後才能使用。"));
					return htmlid;
				}
				if (s.equals("a")) { // 接受魔法
					int[] allBuffSkill = { 26, 42, 48, 158 };
					// 敏捷, 力量, 武器祝福, 自然祝福
					pc.setBuffnoch(1);
					L1SkillUse l1skilluse = null;
					l1skilluse = new L1SkillUse();
					for (int i = 0; i < allBuffSkill.length; i++) {
						l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					}
					pc.sendPackets(new S_SkillSound(pc.getId(), 830));
					pc.curePoison();
					pc.setBuffTime(time);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 900135) {
				htmlid = Yuri(s, pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000055) {
				try {
					String ItemName = "";
					String Stat = "";
					String Class = "";
					String Level = "";
					String DelItemName = "";
					int itemid = 0;
					int Delitemid = 0;
					if (s.equals("str55")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior() || pc.isKnight())
							Class = "(騎士,戰士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						Class = "(黃金槍騎)";
					 			(pc.isGoldenLancer())  // 新增黃金槍騎職業判斷
						Level = "(55)";
						Stat = "力量";
					} else if (s.equals("dex55")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior() || pc.isKnight())
							Class = "(騎士,戰士)";
						else if (pc.isFencer())
							Class = "(劍士)";
								(pc.isGoldenLancer())  // 新增黃金槍騎職業判斷
						Class = "(黃金槍騎)";
						Level = "(55)";
						Stat = "敏捷";
					} else if (s.equals("con55")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior() || pc.isKnight())
							Class = "(騎士,戰士)";
						else if (pc.isFencer())
							Class = "(劍士)";
								(pc.isGoldenLancer())  // 新增黃金槍騎職業判斷
							Class = "(黃金槍騎)";
						Level = "(55)";
						Stat = "體質";
					} else if (s.equals("int55")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior() || pc.isKnight())
							Class = "(騎士,戰士)";
						else if (pc.isFencer())
						Class = "(劍士)";
						else if (pc.isFencer())
							Class = "(黃金槍騎)";
							(pc.isGoldenLancer())  // 新增黃金槍騎職業判斷
						Level = "(55)";
						Stat = "智力";
					} else if (s.equals("wis55")) {  // 新增判斷條件
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
						Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior() || pc.isKnight())
							Class = "(騎士,戰士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())  // 新增黃金槍騎職業判斷
							Class = "(黃金槍騎)";
						Level = "(55)";
						Stat = "精神";
					} else if (s.equals("str70")) {  // 新增判斷條件
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())  // 新增黃金槍騎職業判斷
							Class = "(黃金槍騎)";
						Level = "(70)";
						Stat = "力量";
					} else if (s.equals("dex70")) {  // 新增判斷條件
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())  // 新增黃金槍騎職業判斷
							Class = "(黃金槍騎)";
						Level = "(70)";
						Stat = "敏捷";
					} else if (s.equals("con70")) {  // 新增判斷條件
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())  // 新增黃金槍騎職業判斷
							Class = "(黃金槍騎)";
						Level = "(70)";
						Stat = "體質";
					} else if (s.equals("int70")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())  // 新增黃金槍騎職業判斷
							Class = "(黃金槍騎)";
						Level = "(70)";
						Stat = "智力";
					} else if (s.equals("wis70")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())  // 新增黃金槍騎職業判斷
							Class = "(黃金槍騎)";
						Level = "(70)";
						Stat = "精神";
					} else if (s.equals("str80")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(80)";
						Stat = "力量";
					} else if (s.equals("dex80")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(80)";
						Stat = "敏捷";
					} else if (s.equals("con80")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
							Level = "(80)";
							Stat = "體質";
					} else if (s.equals("int80")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
							Level = "(80)";
							Stat = "智力";
					} else if (s.equals("wis80")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(80)";
						Stat = "精神";
					} else if (s.equals("str85")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkElf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonKnight())
							Class = "(龍騎士)";
						else if (pc.isBlackWizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(85)";
						Stat = "力量";
					} else if (s.equals("dex85")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(85)";
						Stat = "敏捷";
					} else if (s.equals("con85")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(85)";
						Stat = "體質";
					} else if (s.equals("int85")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(85)";
						Stat = "智力";
					} else if (s.equals("wis85")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(85)";
						Stat = "精神";
					} else if (s.equals("str90")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(90)";
						Stat = "力量";
					} else if (s.equals("dex90")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(90)";
						Stat = "敏捷";
					} else if (s.equals("con90")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(90)";
						Stat = "體質";
					} else if (s.equals("int90")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(90)";
						Stat = "智力";
					} else if (s.equals("wis90")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(90)";
						Stat = "精神";
					} else if (s.equals("str91")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(91)";
						Stat = "力量";
					} else if (s.equals("dex91")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(91)";
						Stat = "敏捷";
					} else if (s.equals("con91")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(91)";
						Stat = "體質";
					} else if (s.equals("int91")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(91)";
						Stat = "智力";
					} else if (s.equals("wis91")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(91)";
						Stat = "精神";
					}
					else if (s.equals("str92")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(92)";
						Stat = "力量";
					} else if (s.equals("dex92")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(92)";
						Stat = "敏捷";
					} else if (s.equals("con92")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(92)";
						Stat = "體質";
					} else if (s.equals("int92")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(92)";
						Stat = "智力";
					} else if (s.equals("wis92")) {
						if (pc.isElf())
							Class = "(妖精)";
						else if (pc.isWizard())
							Class = "(法師)";
						else if (pc.isDarkelf())
							Class = "(黑暗妖精)";
						else if (pc.isCrown())
							Class = "(王族)";
						else if (pc.isDragonknight())
							Class = "(龍騎士)";
						else if (pc.isBlackwizard())
							Class = "(幻術師)";
						else if (pc.isWarrior())
							Class = "(戰士)";
						else if (pc.isKnight())
							Class = "(騎士)";
						else if (pc.isFencer())
							Class = "(劍士)";
						else if (pc.isGoldenLancer())
							Class = "(黃金槍騎)";
						Level = "(92)";
						Stat = "精神";
					}
					ItemName = Stat + "的精靈藥水符文" + Level + Class;
					DelItemName = "失去魔力的符文" + Level;

					itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(ItemName);
					Delitemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(DelItemName);
					if (pc.getInventory().consumeItem(Delitemid, 1)) {
						pc.getInventory().storeItem(itemid, 1, true); // 敏捷的精靈藥水
						L1Item run = ItemTable.getInstance().getTemplate(itemid);
						pc.sendPackets(run.getNameId() + "已經變更。");
					} else {
						htmlid = "riddle2";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000054) {
				if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") || s.equals("E")) {
					if (pc.getLevel() >= 55) {
						if (pc.getInventory().checkItem(60031, 1) && pc.getInventory().checkItem(60032, 1)) {
							pc.getInventory().consumeItem(60031, 1);
							pc.getInventory().consumeItem(60032, 1);
							if (s.equals("A")) {
								pc.getInventory().storeItem(60036, 1); // 力量的精靈藥水
								// 符文袋
							} else if (s.equals("B")) {
								pc.getInventory().storeItem(60037, 1); // 敏捷的
								// 精靈藥水符文
							} else if (s.equals("C")) {
								pc.getInventory().storeItem(60038, 1); // 體力的
								// 精靈藥水符文
							} else if (s.equals("D")) {
								pc.getInventory().storeItem(60039, 1); // 智力的
									// 精靈藥水符文
							} else if (s.equals("E")) {
								pc.getInventory().storeItem(60040, 1); // 精神的
								// 精靈藥水符文
							}
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune6"));

						} else {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune5"));
						}
					} else {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune5"));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 1000001) {// 地洞蟻
				int locx = 0, locy = 0, map = 0;
				if (s.equalsIgnoreCase("b")) {// 第一個洞穴
					locx = 32783;
					locy = 32751;
					map = 43;
				} else if (s.equalsIgnoreCase("c")) {// 第二個洞穴
					locx = 32798;
					locy = 32754;
					map = 44;
				} else if (s.equalsIgnoreCase("d")) {// 第三個洞穴
					locx = 32776;
					locy = 32731;
					map = 45;
				} else if (s.equalsIgnoreCase("e")) {// 第四個洞穴
					locx = 32787;
					locy = 32795;
					map = 46;
				} else if (s.equalsIgnoreCase("f")) {// 第五個洞穴
					locx = 32796;
					locy = 32745;
					map = 47;
				} else if (s.equalsIgnoreCase("g")) {// 第六個洞穴
					locx = 32768;
					locy = 32805;
					map = 50;
				}
				if (pc.getInventory().checkItem(40308, 500)) {
					pc.getInventory().consumeItem(40308, 500);
					pc.start_teleport(locx, locy, map, pc.getHeading(), 18339, true, false);
				} else {
					htmlid = "cave2";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50000265) {
				if (s.equalsIgnoreCase("1")) {
					if (pc.getInventory().checkItem(830012) || pc.getInventory().checkItem(830022)) {
						pc.start_teleport(32735, 32798, 101, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(560028) || pc.getInventory().checkItem(4100135)) {
						pc.start_teleport(32735, 32798, 101, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(830001)) {
						pc.getInventory().consumeItem(830001, 1);
						pc.start_teleport(32735, 32798, 101, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(5359);
					}
				} else if (s.equalsIgnoreCase("2")) {
					if (pc.getInventory().checkItem(830013) || pc.getInventory().checkItem(830023)) {
						pc.start_teleport(32727, 32803, 102, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(560028) || pc.getInventory().checkItem(4100135)) {
						pc.start_teleport(32727, 32803, 102, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(830002)) {
						pc.getInventory().consumeItem(830002, 1);
						pc.start_teleport(32727, 32803, 102, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(5359);
					}
				} else if (s.equalsIgnoreCase("3")) {
					if (pc.getInventory().checkItem(830014) || pc.getInventory().checkItem(830024)) {
						pc.start_teleport(32726, 32803, 103, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(560028) || pc.getInventory().checkItem(4100135)) {
						pc.start_teleport(32726, 32803, 103, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(830003)) {
						pc.getInventory().consumeItem(830003, 1);
						pc.start_teleport(32726, 32803, 103, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(5359);
					}
				} else if (s.equalsIgnoreCase("4")) {
					if (pc.getInventory().checkItem(830015) || pc.getInventory().checkItem(830025)) {
						pc.start_teleport(32620, 32859, 104, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(560028) || pc.getInventory().checkItem(4100135)) {
						pc.start_teleport(32620, 32859, 104, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(830004)) {
						pc.getInventory().consumeItem(830004, 1);
						pc.start_teleport(32620, 32859, 104, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(5359);
					}
				} else if (s.equalsIgnoreCase("5")) {
					if (pc.getInventory().checkItem(830016) || pc.getInventory().checkItem(830026)) {
						pc.start_teleport(32601, 32866, 105, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(560028) || pc.getInventory().checkItem(4100135)) {
						pc.start_teleport(32601, 32866, 105, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(830005)) {
						pc.getInventory().consumeItem(830005, 1);
						pc.start_teleport(32601, 32866, 105, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(5359);
					}
				} else if (s.equalsIgnoreCase("6")) {
					if (pc.getInventory().checkItem(830017) || pc.getInventory().checkItem(830027)) {
						pc.start_teleport(32611, 32863, 106, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(560028) || pc.getInventory().checkItem(4100135)) {
						pc.start_teleport(32611, 32863, 106, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(830006)) {
						pc.getInventory().consumeItem(830006, 1);
						pc.start_teleport(32611, 32863, 106, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(5359);
					}
				} else if (s.equalsIgnoreCase("7")) {
					if (pc.getInventory().checkItem(830018) || pc.getInventory().checkItem(830028)) {
						pc.start_teleport(32618, 32866, 107, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(560028) || pc.getInventory().checkItem(4100135)) {
						pc.start_teleport(32618, 32866, 107, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(830007)) {
						pc.getInventory().consumeItem(830007, 1);
						pc.start_teleport(32618, 32866, 107, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(5359);
					}
				} else if (s.equalsIgnoreCase("8")) {
					if (pc.getInventory().checkItem(830019) || pc.getInventory().checkItem(830029)) {
						pc.start_teleport(32602, 32867, 108, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(560028) || pc.getInventory().checkItem(4100135)) {
						pc.start_teleport(32602, 32867, 108, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(830008)) {
						pc.getInventory().consumeItem(830008, 1);
						pc.start_teleport(32602, 32867, 108, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(5359);
					}
				} else if (s.equalsIgnoreCase("9")) {
					if (pc.getInventory().checkItem(830020) || pc.getInventory().checkItem(830030)) {
						pc.start_teleport(32613, 32866, 109, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(560028) || pc.getInventory().checkItem(4100135)) {
						pc.start_teleport(32613, 32866, 109, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(830009)) {
						pc.getInventory().consumeItem(830009, 1);
						pc.start_teleport(32613, 32866, 109, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(5359);
					}
				} else if (s.equalsIgnoreCase("10")) {
					if (pc.getInventory().checkItem(830021) || pc.getInventory().checkItem(830031)) {
						pc.start_teleport(32730, 32803, 110, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(560028) || pc.getInventory().checkItem(4100135)) {
						pc.start_teleport(32730, 32803, 110, pc.getHeading(), 18339, true, false);
					} else if (pc.getInventory().checkItem(830010)) {
						pc.getInventory().consumeItem(830010, 1);
						pc.start_teleport(32730, 32803, 110, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(5359);
					}
				}
				// 군터
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 60169) {
				if (s.equalsIgnoreCase("a")) {
					new L1SkillUse().handleCommands(pc, L1SkillId.BUFF_GUNTER, pc.getId(), pc.getX(), pc.getY(), null,
							0, L1SkillUse.TYPE_SPELLSC);
				}
				// 크레이
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7200026) {
				if (s.equalsIgnoreCase("a")) {
					new L1SkillUse().handleCommands(pc, L1SkillId.BUFF_CRAY, pc.getId(), pc.getX(), pc.getY(), null, 0,
							L1SkillUse.TYPE_SPELLSC);
					htmlid = "grayknight2";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7135) {
				if (s.equalsIgnoreCase("a")) {
					new L1SkillUse().handleCommands(pc, L1SkillId.BUFF_Vala, pc.getId(), pc.getX(), pc.getY(), null, 0,
							L1SkillUse.TYPE_SPELLSC);
					htmlid = "vdeath2";
				}
				// 저주받은 무녀 사엘 (입구 npc)
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039009) {
				if (s.equals("a")) {
					new L1SkillUse().handleCommands(pc, L1SkillId.BUFF_SAEL, pc.getId(), pc.getX(), pc.getY(), null, 0,
							L1SkillUse.TYPE_SPELLSC);
					if (!pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) {
						pc.setSkillEffect(STATUS_UNDERWATER_BREATH, 1800 * 1000);
						pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 1800));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50015) {
				if (s.equalsIgnoreCase("teleport island-silver")) {//
					if (pc.getInventory().checkItem(40308, 1500)) {
						pc.getInventory().consumeItem(40308, 1500);
						pc.start_teleport(33080, 33392, 4, pc.getHeading(), 18339, true, false);
						htmlid = "";
					} else {
						pc.sendPackets(new S_ServerMessage(5359));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 522) {
				if (s.equalsIgnoreCase("giveto")) {
					if (pc.getInventory().checkItem(40308, 200000)) {
						pc.getInventory().consumeItem(40308, 200000);
						L1SkillUse aa = new L1SkillUse();
						aa.handleCommands(pc, L1SkillId.HUNTER_BLESS, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 9009));
						pc.sendPackets(new S_SkillSound(pc.getId(), 9009));
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("在捐贈之前…您應該擔心的是…您的金幣不夠。"));
						htmlid = "";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81210) {
				int locx = 0, locy = 0, mapid = 0;
				if (s.equalsIgnoreCase("b")) {//
					locx = 33442;
					locy = 32797;
					mapid = 4;
				} else if (s.equalsIgnoreCase("C")) {//
					locx = 34056;
					locy = 32279;
					mapid = 4;
				} else if (s.equalsIgnoreCase("D")) {// 발라 둥지
					locx = 33705;
					locy = 32504;
					mapid = 4;
				} else if (s.equalsIgnoreCase("E")) {//
					locx = 33614;
					locy = 33253;
					mapid = 4;
				} else if (s.equalsIgnoreCase("F")) {//
					locx = 33050;
					locy = 32780;
					mapid = 4;
				} else if (s.equalsIgnoreCase("G")) {//
					locx = 32631;
					locy = 32770;
					mapid = 4;
				} else if (s.equalsIgnoreCase("H")) {//
					locx = 33080;
					locy = 33392;
					mapid = 4;
				} else if (s.equalsIgnoreCase("I")) {//
					locx = 32617;
					locy = 33201;
					mapid = 4;
				} else if (s.equalsIgnoreCase("J")) {// 오크 숲
					locx = 32741;
					locy = 32450;
					mapid = 4;
				} else if (s.equalsIgnoreCase("K")) {//
					locx = 32581;
					locy = 32940;
					mapid = 0;
				} else if (s.equalsIgnoreCase("L")) {//
					locx = 33958;
					locy = 33364;
					mapid = 4;
				} else if (s.equalsIgnoreCase("N")) {//
					locx = 32800;
					locy = 32927;
					mapid = 800;
				} else if (s.equalsIgnoreCase("V")) {// 데포류즈앞
					locx = 32595;
					locy = 33163;
					mapid = 4;
				}
				if (pc.getInventory().checkItem(40308, 100)) {
					pc.getInventory().consumeItem(40308, 100);
					pc.start_teleport(locx, locy, mapid, pc.getHeading(), 18339, true, false);
					htmlid = "";
				} else {
					htmlid = "pctel2";
				}
				// Ricky 修煉的傳送
			} else if (npcid == 70798) {
				if (s.equalsIgnoreCase("a")) {// 隱藏的山谷
					if (pc.getLevel() >= 1 & pc.getLevel() <= 45) {
						pc.start_teleport(32684, 32851, 2005, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(
								new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fQ Ricky: \f3[Lv.45]\fQ以下才能進入。"));
					}
				} else if (s.equalsIgnoreCase("b")) {// 奇岩村
					pc.start_teleport(33436, 32799, 4, pc.getHeading(), 18339, true, false);
				} else if (s.equalsIgnoreCase("c")) {// 羅孚神殿
					if (pc.getLevel() >= 10 & pc.getLevel() <= 29) {
						pc.start_teleport(33184, 33449, 4, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ChatPacket(pc, "銀騎士領地可移動等級 10 ~ 29"));
					}
				} else if (s.equalsIgnoreCase("d")) {// 混沌神殿
					if (pc.getLevel() >= 10 & pc.getLevel() <= 29) {
						pc.start_teleport(33066, 33218, 4, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ChatPacket(pc, "銀騎士領地可移動等級 10 ~ 29"));
					}
				} else if (s.equalsIgnoreCase("f")) {// 修煉地下城
					if (pc.getLevel() >= 10 & pc.getLevel() < 20) {
						pc.start_teleport(32801, 32806, 25, pc.getHeading(), 18339, true, false);
					} else if (pc.getLevel() >= 20 & pc.getLevel() < 30) {
						pc.start_teleport(32806, 32746, 26, pc.getHeading(), 18339, true, false);
					} else if (pc.getLevel() >= 30 & pc.getLevel() < 40) {
						pc.start_teleport(32808, 32766, 27, pc.getHeading(), 18339, true, false);
					} else if (pc.getLevel() >= 40 & pc.getLevel() < 44) {
						pc.start_teleport(32796, 32799, 28, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ChatPacket(pc, "修煉地下城可移動等級 10 ~ 44"));
					}
				} else if (s.equalsIgnoreCase("e")) {// 暴風地下城 不信任 Lv 45~51
					if (pc.getLevel() >= 45 & pc.getLevel() <= 51) {
						pc.start_teleport(32807, 32789, 2010, pc.getHeading(), 18339, true, false);
					} else {
						pc.sendPackets(new S_ChatPacket(pc, "暴風修煉地下城可移動等級 45 ~ 51"));
					}
				}
			} else if (npcid == 50078) {
				if (pc.getLevel() <= 99) {
					pc.sendPackets(new S_SystemMessage("請通過可疑的天空花園(PC)中的精靈之珠移動。"));
					return htmlid;
				}
			} else if (npcid == 7310174) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(3000211, 300)) {
						pc.getInventory().consumeItem(3000211, 300);
						pc.getInventory().storeItem(3000210, 1);// 給予
						pc.sendPackets(new S_SystemMessage("謝謝，下次再來。"));
					} else {
						pc.sendPackets(new S_SystemMessage("需要總共 [300] 個愛娃的恩賜。"));
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkItem(3000211, 500)) {
						pc.getInventory().consumeItem(3000211, 500);
						pc.getInventory().storeItem(3000210, 2);// 給予
						pc.sendPackets(new S_SystemMessage("謝謝，下次再來。"));
					} else {
						pc.sendPackets(new S_SystemMessage("需要總共 [500] 個愛娃的恩賜。"));
					}
				} else if (s.equalsIgnoreCase("c")) {
					if (pc.getInventory().checkItem(3000211, 1000)) {
						pc.getInventory().consumeItem(3000211, 1000);
						pc.getInventory().storeItem(3000210, 5);// 給予
						pc.sendPackets(new S_SystemMessage("謝謝，下次再來。"));
					} else {
						pc.sendPackets(new S_SystemMessage("需要總共 [1000] 個愛娃的恩賜。"));
					}
				}
				// Bahamut 製作更新
			} else if (npcid == 70690) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40053, 10)
							&& pc.getInventory().checkItem(40393, 5)) {
						pc.getInventory().consumeItem(410061, 50);
						pc.getInventory().consumeItem(40053, 10);
						pc.getInventory().consumeItem(40393, 5);
						pc.getInventory().storeItem(222307, 1);// 力量之靴
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作道具不足。"));
						pc.sendPackets(new S_SystemMessage("魔物的氣息 (50), 高級紅寶石 (10), 火龍鱗片 (5)"));
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40052, 10)
							&& pc.getInventory().checkItem(40396, 5)) {
						pc.getInventory().consumeItem(410061, 50);
						pc.getInventory().consumeItem(40052, 10);
						pc.getInventory().consumeItem(40396, 5);
						pc.getInventory().storeItem(22359, 1);// 智慧之靴
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作道具不足。"));
						pc.sendPackets(new S_SystemMessage("魔物的氣息 (50), 高級鑽石 (10), 地龍鱗片 (5)"));
					}
					// Bahamut 製作更新
				} else if (npcid == 70690) {
					if (s.equalsIgnoreCase("a")) {
						if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40053, 10)
								&& pc.getInventory().checkItem(40393, 5)) {
							pc.getInventory().consumeItem(410061, 50);
							pc.getInventory().consumeItem(40053, 10);
							pc.getInventory().consumeItem(40393, 5);
							pc.getInventory().storeItem(222307, 1);// 力量之靴
							htmlid = "";
						} else {
							pc.sendPackets(new S_SystemMessage("製作道具不足。"));
							pc.sendPackets(new S_SystemMessage("魔物的氣息 (50), 高級紅寶石 (10), 火龍鱗片 (5)"));
						}
					} else if (s.equalsIgnoreCase("b")) {
						if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40055, 10)
								&& pc.getInventory().checkItem(40394, 5)) {
							pc.getInventory().consumeItem(410061, 50);
							pc.getInventory().consumeItem(40055, 10);
							pc.getInventory().consumeItem(40394, 5);
							pc.getInventory().storeItem(222308, 1);// 敏捷之靴
							htmlid = "";
						} else {
							pc.sendPackets(new S_SystemMessage("製作道具不足。"));
							pc.sendPackets(new S_SystemMessage("魔物的氣息 (50), 高級祖母綠 (10), 風龍鱗片 (5)"));
						}
					} else if (s.equalsIgnoreCase("d")) {
						if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40054, 10)
								&& pc.getInventory().checkItem(40395, 5)) {
							pc.getInventory().consumeItem(410061, 50);
							pc.getInventory().consumeItem(40054, 10);
							pc.getInventory().consumeItem(40395, 5);
							pc.getInventory().storeItem(222309, 1);// 知識之靴
							htmlid = "";
						} else {
							pc.sendPackets(new S_SystemMessage("製作道具不足。"));
							pc.sendPackets(new S_SystemMessage("魔物的氣息 (50), 高級藍寶石 (10), 水龍鱗片 (5)"));
						}
				} else if (s.equalsIgnoreCase("e")) {
					if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560030)) {
						pc.getInventory().consumeItem(410061, 50);
						pc.getInventory().consumeItem(560030, 1);
						pc.getInventory().storeItem(222307, 1);// 力量之靴
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作道具不足。"));
						pc.sendPackets(new S_SystemMessage("魔物的氣息 (50), 火靈屬性轉換卷軸 (1)"));
					}
					} else if (s.equalsIgnoreCase("f")) {
						if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560033)) {
							pc.getInventory().consumeItem(410061, 50);
							pc.getInventory().consumeItem(560033, 1);
							pc.getInventory().storeItem(22359, 1);// 智力之靴
							htmlid = "";
						} else {
							pc.sendPackets(new S_SystemMessage("製作道具不足。"));
							pc.sendPackets(new S_SystemMessage("魔物的氣息 (50), 地靈屬性轉換卷軸 (1)"));
						}
					} else if (s.equalsIgnoreCase("g")) {
						if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560032)) {
							pc.getInventory().consumeItem(410061, 50);
							pc.getInventory().consumeItem(560032, 1);
							pc.getInventory().storeItem(222308, 1);// 敏捷之靴
							htmlid = "";
						} else {
							pc.sendPackets(new S_SystemMessage("製作道具不足。"));
							pc.sendPackets(new S_SystemMessage("魔物的氣息 (50), 風靈屬性轉換卷軸 (1)"));
						}
					} else if (s.equalsIgnoreCase("h")) {
						if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560031)) {
							pc.getInventory().consumeItem(410061, 50);
							pc.getInventory().consumeItem(560031, 1);
							pc.getInventory().storeItem(222309, 1);// 智力之靴
							htmlid = "";
						} else {
							pc.sendPackets(new S_SystemMessage("製作道具不足。"));
							pc.sendPackets(new S_SystemMessage("魔物的氣息 (50), 水靈屬性轉換卷軸 (1)"));
						htmlid = "";
					} else {
							pc.sendPackets(new S_SystemMessage("製作道具不足。"));
							pc.sendPackets(new S_SystemMessage("魔物的氣息 (50), 水靈屬性轉換卷軸 (1)"));
						}
					}
							// 製作記憶擴展水晶
				} else if (npcid == 7310149) {
					if (s.equalsIgnoreCase("request memory crystal")) {
						if (pc.getInventory().checkItem(3000200, 1) && pc.getInventory().checkItem(40308, 20000)) {
							pc.getInventory().consumeItem(3000200, 1);
							pc.getInventory().consumeItem(40308, 20000);
							pc.getInventory().storeItem(700022, 1);// 記憶擴展水晶
							pc.sendPackets(new S_SystemMessage("製作已完成。"));
							htmlid = "";
						} else {
							pc.sendPackets(new S_SystemMessage("金幣 (20,000) 或記憶碎片 (1) 不足"));
						}
					}
//            } else if (npcid == 50045) { // 夢島任務更新
//                if (s.equalsIgnoreCase("a")) { // 進入
//                    if (pc.getInventory().checkItem(810000, 1)) {
//                        pc.getInventory().consumeItem(810000, 1);
//                        pc.start_teleport(32800, 32798, 1935, pc.getHeading(), 18339, true, false);
//                    } else {
//                        pc.sendPackets(new S_SystemMessage("進入需要獨角獸神殿的鑰匙。"));
//                    }
//                } else if (s.equalsIgnoreCase("b")) {// 吉蘭村
//                    pc.start_teleport(33436, 32799, 4, pc.getHeading(), 18339, true, false);
//                }
				}else if (npcid == 21015) { // 銀騎士地牢入口 Gerard  Team The Day by. Jude
					if (s.equalsIgnoreCase("a")) {// 進入銀騎士地牢一樓
						if (pc.getLevel() >= 80 & pc.getLevel() <= 84) {
							if (pc.getInventory().checkItem(4200255,1) && pc.getInventory().checkItem(40308, 15000)) {// 銀騎士地牢入場券 (3小時)
								pc.getInventory().consumeItem(40308, 15000);
								pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
							}else if (pc.getInventory().checkItem(4200256,1) && pc.getInventory().checkItem(40308, 15000)) {// 銀騎士地牢入場券 (30天)
								pc.getInventory().consumeItem(40308, 15000);
								pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
							}else if (pc.getInventory().checkItem(4100121,1) && pc.getInventory().checkItem(40308, 15000)) {// 高級不朽的祝福
								pc.getInventory().consumeItem(40308, 15000);
								pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
							} else {
								htmlid = "hanggelf";
							}
						}
					} else if (s.equalsIgnoreCase("b")) { // 進入銀騎士地牢二樓
						if (pc.getLevel() >= 80 & pc.getLevel() <= 92) {
							if (pc.getInventory().checkItem(4200255,1) && pc.getInventory().checkItem(40308, 15000)) {// 銀騎士地牢入場券 (3小時)
								pc.getInventory().consumeItem(40308, 15000);
								pc.start_teleport(32769, 32759, 7532, pc.getHeading(), 18339, true, false);
							}else if (pc.getInventory().checkItem(4200256,1) && pc.getInventory().checkItem(40308, 15000)) {// 銀騎士地牢入場券 (30天)
								pc.getInventory().consumeItem(40308, 15000);
								pc.start_teleport(32769, 32759, 7532, pc.getHeading(), 18339, true, false);
							}else if (pc.getInventory().checkItem(4100121,1) && pc.getInventory().checkItem(40308, 15000)) {// 高級不朽的祝福
								pc.getInventory().consumeItem(40308, 15000);
								pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
							} else {
								htmlid = "hanggelf";
							}
						}
					} else if (s.equalsIgnoreCase("c")) { // 進入銀騎士地牢三樓
						if (pc.getLevel() >= 80 & pc.getLevel() <= 92) {
							if (pc.getInventory().checkItem(4200255, 1) && pc.getInventory().checkItem(40308, 20000)) {// 銀騎士地牢入場券 (3小時)
								pc.getInventory().consumeItem(40308, 20000);
								pc.start_teleport(32791, 32857, 7533, pc.getHeading(), 18339, true, false);
							}else if (pc.getInventory().checkItem(4200256, 1) && pc.getInventory().checkItem(40308, 20000)) {// 銀騎士地牢入場券 (30天)
								pc.getInventory().consumeItem(40308, 20000);
								pc.start_teleport(32791, 32857, 7533, pc.getHeading(), 18339, true, false);
							}else if (pc.getInventory().checkItem(4100121, 1) && pc.getInventory().checkItem(40308, 15000)) {// 高級不朽的祝福
								pc.getInventory().consumeItem(40308, 15000);
								pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
							} else {
								htmlid = "hanggelf";
							}
						}
					} else if (s.equalsIgnoreCase("d")) { // 進入銀騎士地牢四樓
						if (pc.getLevel() >= 80 & pc.getLevel() <= 92) {
							if (pc.getInventory().checkItem(4200255, 1) && pc.getInventory().checkItem(40308, 20000)) {// 銀騎士地牢入場券 (3小時)
								pc.getInventory().consumeItem(40308, 20000);
								pc.start_teleport(32860, 32760, 7534, pc.getHeading(), 18339, true, false);
							}else if (pc.getInventory().checkItem(4200256, 1) && pc.getInventory().checkItem(40308, 20000)) {// 銀騎士地牢入場券 (30天)
								pc.getInventory().consumeItem(40308, 20000);
								pc.start_teleport(32860, 32760, 7534, pc.getHeading(), 18339, true, false);
							}else if (pc.getInventory().checkItem(4100121, 1) && pc.getInventory().checkItem(40308, 15000)) {// 高級不朽的祝福
								pc.getInventory().consumeItem(40308, 15000);
								pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
							} else {
								htmlid = "hanggelf";
							}
						}
					}
				} else if (npcid == 7200000) { // 夢島 艾金斯
					if (s.equalsIgnoreCase("d")) {
						if (pc.getInventory().checkItem(3000215, 1) && pc.getInventory().checkItem(1000004, 1)) {
							pc.getInventory().consumeItem(3000215, 1);
							pc.getInventory().consumeItem(1000004, 1);
							pc.getInventory().storeItem(810010, 8);
							addQuestExp(pc, 4);
						} else {
							htmlid = "ekins5";
						}
					} else if (s.equalsIgnoreCase("c")) { // 催化劑: 獨角獸的成長標誌
						if (pc.getInventory().checkItem(3000215, 1)) {
							pc.getInventory().consumeItem(3000215, 1);
							pc.getInventory().storeItem(810010, 5);
							addQuestExp(pc, 4);
						} else {
							htmlid = "ekins5";
						}
					} else if (s.equalsIgnoreCase("b")) { // 催化劑: 成長的珠子
						if (pc.getInventory().checkItem(810002, 1)) {
							pc.getInventory().consumeItem(810002, 1);
							pc.getInventory().storeItem(810010, 1);
							if (pc.getLevel() >= 52 && pc.getLevel() <= 64) {
								addQuestExp(pc, 1);
							} else if (pc.getLevel() >= 65 && pc.getLevel() <= 74) {
								addQuestExp(pc, 2);
							} else if (pc.getLevel() >= 75 && pc.getLevel() <= 81) {
								addQuestExp(pc, 3);
							} else if (pc.getLevel() == 82/* && pc.getLevel() <= 83 */) {
								addQuestExp(pc, 4);
							} else {
								htmlid = "ekins5";
							}
						}
					} else if (s.equalsIgnoreCase("a")) { // 催化劑: 成長的珠子碎片
						if (pc.getInventory().checkItem(810001, 1)) {
							pc.getInventory().consumeItem(810001, 1);
							pc.getInventory().storeItem(810010, 1);
							addQuestExp(pc, 4);
						} else {
							htmlid = "ekins5";
						}
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkEnchantItem(20273, 7, 1) // +7
						// 魔力的手套
							&& pc.getInventory().checkItem(40395, 1) // 水龍鱗片
							&& pc.getInventory().checkItem(410061, 10) // 魔物的氣息
							&& pc.getInventory().checkItem(820004, 300) // 魔力的絲線
							&& pc.getInventory().checkItem(820005, 1)) { // 魔力的核心

						pc.getInventory().consumeEnchantItem(20273, 7, 1);
						pc.getInventory().consumeItem(40395, 1);
						pc.getInventory().consumeItem(410061, 10);
						pc.getInventory().consumeItem(820004, 300);
						pc.getInventory().consumeItem(820005, 1);
						pc.getInventory().storeItem(20274, 1);// 閃耀的魔力手套
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
						pc.sendPackets(new S_SystemMessage("水龍鱗片(1)"));
						pc.sendPackets(new S_SystemMessage("魔物的氣息(10)"));
						pc.sendPackets(new S_SystemMessage("魔力的絲線(300)"));
						pc.sendPackets(new S_SystemMessage("魔力的核心(1)"));
						pc.sendPackets(new S_SystemMessage("+7 魔力的手套(1)"));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71180) { // 賈伊普
				if (s.equalsIgnoreCase("A")) { // 夢想中的玩具熊
					if (pc.getInventory().consumeItem(49026, 1000)) {
						pc.getInventory().storeItem(41093, 1);
						htmlid = "jp6";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("B")) { // 香水
					if (pc.getInventory().consumeItem(49026, 5000)) {
						pc.getInventory().storeItem(41094, 1);
						htmlid = "jp6";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("C")) { // 禮服
					if (pc.getInventory().consumeItem(49026, 10000)) {
						pc.getInventory().storeItem(41095, 1);
						htmlid = "jp6";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("D")) { // 戒指
					if (pc.getInventory().consumeItem(49026, 100000)) {
						pc.getInventory().storeItem(41096, 1);
						htmlid = "jp6";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("E")) { // 偉人傳
					if (pc.getInventory().consumeItem(49026, 1000)) {
						pc.getInventory().storeItem(41098, 1);
						htmlid = "jp8";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("F")) { // 時髦的帽子
					if (pc.getInventory().consumeItem(49026, 5000)) {
						pc.getInventory().storeItem(41099, 1);
						htmlid = "jp8";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("G")) { // 頂級葡萄酒
					if (pc.getInventory().consumeItem(49026, 10000)) {
						pc.getInventory().storeItem(41100, 1);
						htmlid = "jp8";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("H")) { // 神秘的鑰匙
					if (pc.getInventory().consumeItem(49026, 100000)) {
						pc.getInventory().storeItem(41101, 1);
						htmlid = "jp8";
					} else {
						htmlid = "jp5";
					}
				}

				// 再遇的火焰魔像更新
			} else if (npcid == 5066) {
				int enchant = 0;
				int itemId = 0;
				int oldArmor = 0;
				L1NpcInstance npc = (L1NpcInstance) obj;
				String npcName = npc.getNpcTemplate().get_name();
				if (s.equalsIgnoreCase("1")) { // [+7]魔力短劍
					if ((pc.getInventory().checkEnchantItem(5, 8, 1) || pc.getInventory().checkEnchantItem(6, 8, 1)
							|| pc.getInventory().checkEnchantItem(32, 8, 1)
							|| pc.getInventory().checkEnchantItem(37, 8, 1)
							|| pc.getInventory().checkEnchantItem(41, 8, 1)
							|| pc.getInventory().checkEnchantItem(42, 8, 1)
							|| pc.getInventory().checkEnchantItem(52, 8, 1)
							|| pc.getInventory().checkEnchantItem(64, 8, 1)
							|| pc.getInventory().checkEnchantItem(99, 8, 1)
							|| pc.getInventory().checkEnchantItem(104, 8, 1)
							|| pc.getInventory().checkEnchantItem(125, 8, 1)
							|| pc.getInventory().checkEnchantItem(129, 8, 1)
							|| pc.getInventory().checkEnchantItem(131, 8, 1)
							|| pc.getInventory().checkEnchantItem(145, 8, 1)
							|| pc.getInventory().checkEnchantItem(148, 8, 1)
							|| pc.getInventory().checkEnchantItem(180, 8, 1)
							|| pc.getInventory().checkEnchantItem(181, 8, 1))
							&& pc.getInventory().checkItem(40308, 5000000)) {
						if (pc.getInventory().consumeEnchantItem(5, 8, 1)
								|| pc.getInventory().consumeEnchantItem(6, 8, 1)
								|| pc.getInventory().consumeEnchantItem(32, 8, 1)
								|| pc.getInventory().consumeEnchantItem(37, 8, 1)
								|| pc.getInventory().consumeEnchantItem(41, 8, 1)
								|| pc.getInventory().consumeEnchantItem(42, 8, 1)
								|| pc.getInventory().consumeEnchantItem(52, 8, 1)
								|| pc.getInventory().consumeEnchantItem(64, 8, 1)
								|| pc.getInventory().consumeEnchantItem(99, 8, 1)
								|| pc.getInventory().consumeEnchantItem(104, 8, 1)
								|| pc.getInventory().consumeEnchantItem(125, 8, 1)
								|| pc.getInventory().consumeEnchantItem(129, 8, 1)
								|| pc.getInventory().consumeEnchantItem(131, 8, 1)
								|| pc.getInventory().consumeEnchantItem(145, 8, 1)
								|| pc.getInventory().consumeEnchantItem(148, 8, 1)
								|| pc.getInventory().consumeEnchantItem(180, 8, 1)
								|| pc.getInventory().consumeEnchantItem(181, 8, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 5000000);
						giveEnchant(pc, 602, 1, 7); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equalsIgnoreCase("2")) { // [+8]魔力短劍
					if ((pc.getInventory().checkEnchantItem(5, 9, 1) || pc.getInventory().checkEnchantItem(6, 9, 1)
							|| pc.getInventory().checkEnchantItem(32, 9, 1)
							|| pc.getInventory().checkEnchantItem(37, 9, 1)
							|| pc.getInventory().checkEnchantItem(41, 9, 1)
							|| pc.getInventory().checkEnchantItem(42, 9, 1)
							|| pc.getInventory().checkEnchantItem(52, 9, 1)
							|| pc.getInventory().checkEnchantItem(64, 9, 1)
							|| pc.getInventory().checkEnchantItem(99, 9, 1)
							|| pc.getInventory().checkEnchantItem(104, 9, 1)
							|| pc.getInventory().checkEnchantItem(125, 9, 1)
							|| pc.getInventory().checkEnchantItem(129, 9, 1)
							|| pc.getInventory().checkEnchantItem(131, 9, 1)
							|| pc.getInventory().checkEnchantItem(145, 9, 1)
							|| pc.getInventory().checkEnchantItem(148, 9, 1)
							|| pc.getInventory().checkEnchantItem(180, 9, 1)
							|| pc.getInventory().checkEnchantItem(181, 9, 1))
							&& pc.getInventory().checkItem(40308, 10000000)) {
						if (pc.getInventory().consumeEnchantItem(5, 9, 1)
								|| pc.getInventory().consumeEnchantItem(6, 9, 1)
								|| pc.getInventory().consumeEnchantItem(32, 9, 1)
								|| pc.getInventory().consumeEnchantItem(37, 9, 1)
								|| pc.getInventory().consumeEnchantItem(41, 9, 1)
								|| pc.getInventory().consumeEnchantItem(42, 9, 1)
								|| pc.getInventory().consumeEnchantItem(52, 9, 1)
								|| pc.getInventory().consumeEnchantItem(64, 9, 1)
								|| pc.getInventory().consumeEnchantItem(99, 9, 1)
								|| pc.getInventory().consumeEnchantItem(104, 9, 1)
								|| pc.getInventory().consumeEnchantItem(125, 9, 1)
								|| pc.getInventory().consumeEnchantItem(129, 9, 1)
								|| pc.getInventory().consumeEnchantItem(131, 9, 1)
								|| pc.getInventory().consumeEnchantItem(145, 9, 1)
								|| pc.getInventory().consumeEnchantItem(148, 9, 1)
								|| pc.getInventory().consumeEnchantItem(180, 9, 1)
								|| pc.getInventory().consumeEnchantItem(181, 9, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 10000000);
						giveEnchant(pc, 602, 1, 8); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equalsIgnoreCase("3")) { // [+7]幻影鏈鋸劍
					if ((pc.getInventory().checkEnchantItem(500, 8, 1) || pc.getInventory().checkEnchantItem(501, 8, 1))
							&& pc.getInventory().checkItem(40308, 5000000)) {
						if (pc.getInventory().consumeEnchantItem(500, 8, 1)
								|| pc.getInventory().consumeEnchantItem(501, 8, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 5000000);
						giveEnchant(pc, 202001, 1, 7); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equalsIgnoreCase("4")) { // [+8]幻影鏈鋸劍
					if ((pc.getInventory().checkEnchantItem(500, 9, 1) || pc.getInventory().checkEnchantItem(501, 9, 1))
							&& pc.getInventory().checkItem(40308, 10000000)) {
						if (pc.getInventory().consumeEnchantItem(500, 9, 1)
								|| pc.getInventory().consumeEnchantItem(501, 9, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 10000000);
						giveEnchant(pc, 202001, 1, 8); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equalsIgnoreCase("5")) { // [+7]共鳴奇靈克
					if ((pc.getInventory().checkEnchantItem(503, 8, 1) || pc.getInventory().checkEnchantItem(504, 8, 1))
							&& pc.getInventory().checkItem(40308, 5000000)) {
						if (pc.getInventory().consumeEnchantItem(503, 8, 1)
								|| pc.getInventory().consumeEnchantItem(504, 8, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 5000000);
						giveEnchant(pc, 1135, 1, 7); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equalsIgnoreCase("6")) { // [+8]共鳴奇靈克
					if ((pc.getInventory().checkEnchantItem(503, 9, 1) || pc.getInventory().checkEnchantItem(504, 9, 1))
							&& pc.getInventory().checkItem(40308, 10000000)) {
						if (pc.getInventory().consumeEnchantItem(503, 9, 1)
								|| pc.getInventory().consumeEnchantItem(504, 9, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 10000000);
						giveEnchant(pc, 1135, 1, 8); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equalsIgnoreCase("7")) { // [+7]破壞之爪
					if ((pc.getInventory().checkEnchantItem(81, 8, 1) || pc.getInventory().checkEnchantItem(177, 8, 1)
							|| pc.getInventory().checkEnchantItem(194, 8, 1)
							|| pc.getInventory().checkEnchantItem(13, 8, 1))
							&& pc.getInventory().checkItem(40308, 5000000)) {
						if (pc.getInventory().consumeEnchantItem(81, 8, 1)
								|| pc.getInventory().consumeEnchantItem(177, 8, 1)
								|| pc.getInventory().consumeEnchantItem(194, 8, 1)
								|| pc.getInventory().consumeEnchantItem(13, 8, 1)) {

						}
						pc.getInventory().consumeItem(40308, 5000000);
						giveEnchant(pc, 1124, 1, 7); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equalsIgnoreCase("8")) { // [+8]破壞之爪
					if ((pc.getInventory().checkEnchantItem(81, 9, 1) || pc.getInventory().checkEnchantItem(177, 9, 1)
							|| pc.getInventory().checkEnchantItem(194, 9, 1)
							|| pc.getInventory().checkEnchantItem(13, 9, 1))
							&& pc.getInventory().checkItem(40308, 10000000)) {
						if (pc.getInventory().consumeEnchantItem(81, 9, 1)
								|| pc.getInventory().consumeEnchantItem(177, 9, 1)
								|| pc.getInventory().consumeEnchantItem(194, 9, 1)
								|| pc.getInventory().consumeEnchantItem(13, 9, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 10000000);
						giveEnchant(pc, 1124, 1, 8); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equalsIgnoreCase("9")) { // [+7]破壞的雙刀
					if ((pc.getInventory().checkEnchantItem(81, 8, 1) || pc.getInventory().checkEnchantItem(177, 8, 1)
							|| pc.getInventory().checkEnchantItem(194, 8, 1)
							|| pc.getInventory().checkEnchantItem(13, 8, 1))
							&& pc.getInventory().checkItem(40308, 5000000)) {
						if (pc.getInventory().consumeEnchantItem(81, 8, 1)
								|| pc.getInventory().consumeEnchantItem(177, 8, 1)
								|| pc.getInventory().consumeEnchantItem(194, 8, 1)
								|| pc.getInventory().consumeEnchantItem(13, 8, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 5000000);
						giveEnchant(pc, 1125, 1, 7); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equalsIgnoreCase("10")) { // [+8]破壞的雙刀
					if ((pc.getInventory().checkEnchantItem(81, 9, 1) || pc.getInventory().checkEnchantItem(177, 9, 1)
							|| pc.getInventory().checkEnchantItem(194, 9, 1)
							|| pc.getInventory().checkEnchantItem(13, 9, 1))
							&& pc.getInventory().checkItem(40308, 10000000)) {
						if (pc.getInventory().consumeEnchantItem(81, 9, 1)
								|| pc.getInventory().consumeEnchantItem(177, 9, 1)
								|| pc.getInventory().consumeEnchantItem(194, 9, 1)
								|| pc.getInventory().consumeEnchantItem(13, 9, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 10000000);
						giveEnchant(pc, 1125, 1, 8); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equalsIgnoreCase("11")) { // [+0]杰羅斯的杖
					if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 9, 1)
							&& pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
						pc.getInventory().consumeEnchantItem(119, 5, 1);
						pc.getInventory().consumeEnchantItem(121, 9, 1);
						pc.getInventory().consumeItem(700077, 1);
						pc.getInventory().consumeItem(41246, 100000);
						pc.getInventory().storeItem(202003, 1);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}

				} else if (s.equalsIgnoreCase("12")) { // [+8]杰羅斯的杖
					if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 10, 1)
							&& pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
						pc.getInventory().consumeEnchantItem(119, 5, 1);
						pc.getInventory().consumeEnchantItem(121, 10, 1);
						pc.getInventory().consumeItem(700077, 1);
						pc.getInventory().consumeItem(41246, 100000);
						giveEnchant(pc, 202003, 1, 8); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equalsIgnoreCase("13")) { // [+9]杰羅斯的杖
					if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 11, 1)
							&& pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
						pc.getInventory().consumeEnchantItem(119, 5, 1);
						pc.getInventory().consumeEnchantItem(121, 11, 1);
						pc.getInventory().consumeItem(700077, 1);
						pc.getInventory().consumeItem(41246, 100000);
						giveEnchant(pc, 202003, 1, 9); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}

				} else if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") // 鎧甲
						|| s.equals("E") || s.equals("F") || s.equals("G") || s.equals("H") // 鱗甲
						|| s.equals("I") || s.equals("J") || s.equals("K") || s.equals("L") // 皮甲
						|| s.equals("M") || s.equals("N") || s.equals("O") || s.equals("P")) { // 長袍
					if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D")) {
						if (s.equals("A")) {
							enchant = 7;
						} else if (s.equals("B")) {
							enchant = 8;
						} else if (s.equals("C")) {
							enchant = 9;
						} else if (s.equals("D")) {
							enchant = 10;
						}
						oldArmor = 20095;
						itemId = 222300;
					} else if (s.equals("E") || s.equals("F") || s.equals("G") || s.equals("H")) {
						if (s.equals("E")) {
							enchant = 7;
						} else if (s.equals("F")) {
							enchant = 8;
						} else if (s.equals("G")) {
							enchant = 9;
						} else if (s.equals("H")) {
							enchant = 10;
						}
						oldArmor = 20094;
						itemId = 222301;
					} else if (s.equals("I") || s.equals("J") || s.equals("K") || s.equals("L")) {
						if (s.equals("I")) {
							enchant = 7;
						} else if (s.equals("J")) {
							enchant = 8;
						} else if (s.equals("K")) {
							enchant = 9;
						} else if (s.equals("L")) {
							enchant = 10;
						}
						oldArmor = 20092;
						itemId = 222302;
					} else if (s.equals("M") || s.equals("N") || s.equals("O") || s.equals("P")) {
						if (s.equals("M")) {
							enchant = 7;
						} else if (s.equals("N")) {
							enchant = 8;
						} else if (s.equals("O")) {
							enchant = 9;
						} else if (s.equals("P")) {
							enchant = 10;
						}
						oldArmor = 20093;
						itemId = 222303;
					}
					if (pc.getInventory().checkEnchantItem(20110, enchant, 1)
							&& pc.getInventory().checkItem(41246, 100000) && pc.getInventory().checkItem(oldArmor, 1)) {
						pc.getInventory().consumeEnchantItem(20110, enchant, 1);
						pc.getInventory().consumeItem(41246, 100000); // 溶劑
						pc.getInventory().consumeItem(oldArmor, 1); // 古代的
						createNewItem(pc, npcName, itemId, 1, enchant - 7);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("製作材料不足。"));
					}
				} else if (s.equals("a")) { // []疾風之斧
					if ((pc.getInventory().checkEnchantItem(605, 8, 1)) && pc.getInventory().checkItem(41246, 100000)) {
						if (pc.getInventory().consumeEnchantItem(605, 8, 1)) {

						}
						pc.getInventory().consumeItem(41246, 100000);
						giveEnchant(pc, 203015, 1, 0); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+8 狂風之斧需要結晶體(100,000)個。"));
					}
				} else if (s.equals("b")) { // [+8]疾風之斧
					if ((pc.getInventory().checkEnchantItem(605, 9, 1)) && pc.getInventory().checkItem(41246, 100000)) {
						if (pc.getInventory().consumeEnchantItem(605, 9, 1)) {

						}
						pc.getInventory().consumeItem(41246, 100000);
						giveEnchant(pc, 203015, 1, 8); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+9 狂風之斧需要結晶體(100,000)個。"));
					}
				} else if (s.equals("c")) { // [+9]疾風之斧
					if ((pc.getInventory().checkEnchantItem(605, 10, 1))
							&& pc.getInventory().checkItem(41246, 100000)) {
						if (pc.getInventory().consumeEnchantItem(605, 10, 1)) {

						}
						pc.getInventory().consumeItem(41246, 100000);
						giveEnchant(pc, 203015, 1, 9); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+10 狂風之斧需要結晶體(100,000)個。"));
					}
				} else if (s.equals("d")) { // []魔物之斧
					if ((pc.getInventory().checkEnchantItem(151, 0, 1)) && pc.getInventory().checkItem(41246, 200000)) {
						if (pc.getInventory().consumeEnchantItem(151, 0, 1)) {
							;
						}
						pc.getInventory().consumeItem(41246, 200000); // 消耗溶劑
						giveEnchant(pc, 203016, 1, 0); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+0 惡魔之斧需要結晶體(100,000)個。"));
					}
				} else if (s.equals("e")) { // [+1]魔物之斧
					if ((pc.getInventory().checkEnchantItem(151, 3, 1)) && pc.getInventory().checkItem(41246, 200000)) {
						if (pc.getInventory().consumeEnchantItem(151, 3, 1)) {
							;
						}
						pc.getInventory().consumeItem(41246, 200000);
						giveEnchant(pc, 203016, 1, 1); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+3 惡魔之斧需要結晶體(100,000)個。"));
					}
				} else if (s.equals("f")) { // [+3]魔物之斧
					if ((pc.getInventory().checkEnchantItem(151, 5, 1)) && pc.getInventory().checkItem(41246, 200000)) {
						if (pc.getInventory().consumeEnchantItem(151, 5, 1)) {
							;
						}
						pc.getInventory().consumeItem(41246, 200000); // 消耗溶劑
						giveEnchant(pc, 203016, 1, 3); // 給予強化的物品
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+5 惡魔之斧需要結晶體(100,000)個。"));
					}
				}
			} else if (npcid == 8502052) {
				if (s.equals("a")) {
					GrowthCoupon(pc, 1);
				} else if (s.equals("b")) {
					GrowthCoupon(pc, 2);
				} else if (s.equals("c")) {
					GrowthCoupon(pc, 3);
				}
			} else if (npcid == 7) {
				if (s.equals("a")) { // 一般獎勵
					if (pc.getLevel() >= 52) {
						if (pc.getInventory().checkItem(30151, 1)) {
							pc.getInventory().consumeItem(30151, 1); // 消耗製作材料
							pc.getInventory().storeItem(30149, 1); // 獲得獎勵物品
							rewardExperienceAtLevel52(pc, 1); // 獎勵52級經驗值
							htmlid = "anold3";
						} else {
							pc.sendPackets(new S_SystemMessage("製作材料不足。"));
							htmlid = "anold4";
						}
					} else {
						htmlid = "anold2";
					}
				} else if (s.equals("b")) { // 特別獎勵
					if (pc.getLevel() >= 52) {
						if (pc.getInventory().checkItem(30151, 1) && pc.getInventory().checkItem(1000004, 1)) {
							pc.getInventory().consumeItem(30151, 1);
							pc.getInventory().consumeItem(1000004, 1);
							pc.getInventory().storeItem(30149, 1);
							rewardExperienceAtLevel52(pc, 2); // 獎勵52級經驗值
							htmlid = "anold3";
						} else {
							pc.sendPackets(new S_SystemMessage("製作材料不足。"));
							htmlid = "anold4";
						}
					} else {
						htmlid = "anold2";
					}
				} else if (s.equals("c")) { // 閃亮的特別獎勵
					if (pc.getLevel() >= 52) {
						if (pc.getInventory().checkItem(30151, 1) && pc.getInventory().checkItem(1000007, 1)) {
							pc.getInventory().consumeItem(30151, 1); // 消耗製作材料
							pc.getInventory().consumeItem(1000007, 1); // 消耗額外材料
							pc.getInventory().storeItem(30149, 1); // 獲得獎勵物品
							rewardExperienceAtLevel52(pc, 3); // 獎勵52級經驗值
							htmlid = "anold3";
						} else {
							pc.sendPackets(new S_SystemMessage("製作材料不足。"));
							htmlid = "anold4";
						}
					} else {
						htmlid = "anold2";
					}
				}
				// Naruto
			} else if (npcid == 9) {
				if (s.equals("a")) { // 一般獎勵
					if (pc.getLevel() >= 30) {
						if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)) {
							pc.getInventory().consumeItem(9992, 5); // 消耗材料9992
							pc.getInventory().consumeItem(9993, 1); // 消耗材料9993
							pc.getInventory().storeItem(9994, 1); // 獲得獎勵物品9994
							rewardExperienceAtLevel52(pc, 1); // 獎勵52級經驗值
							htmlid = "naruto3";
						} else {
							pc.sendPackets(new S_SystemMessage("製作材料不足。"));
							htmlid = "naruto4";
						}
					} else {
						htmlid = "naruto2";
					}
				} else if (s.equals("b")) { // 特別獎勵
					if (pc.getLevel() >= 30) {
						if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)
								&& pc.getInventory().checkItem(1000004, 1)) {
							pc.getInventory().consumeItem(9992, 5);
							pc.getInventory().consumeItem(9993, 1);
							pc.getInventory().consumeItem(1000004, 1);
							pc.getInventory().storeItem(9994, 1);
							rewardExperienceAtLevel52(pc, 1); // 獎勵52級經驗值
							htmlid = "naruto3";
						} else {
							pc.sendPackets(new S_SystemMessage("製作材料不足。"));
							htmlid = "naruto4";
						}
					} else {
						htmlid = "naruto2";
					}
				} else if (s.equals("c")) { // 閃亮的特別獎勵
					if (pc.getLevel() >= 30) {
						if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)
								&& pc.getInventory().checkItem(1000007, 1)) {
							pc.getInventory().consumeItem(9992, 5); // 消耗材料9992
							pc.getInventory().consumeItem(9993, 1); // 消耗材料9993
							pc.getInventory().consumeItem(1000007, 1); // 消耗額外材料
							pc.getInventory().storeItem(9994, 1); // 獲得獎勵物品9994
							rewardExperienceAtLevel52(pc, 1); // 獎勵52級經驗值
							htmlid = "naruto3";
						} else {
							pc.sendPackets(new S_SystemMessage("製作材料不足。"));
							htmlid = "naruto4";
						}
					} else {
						htmlid = "naruto2";
					}
				}
			} else if (npcid == 8500314) {
				if (s.equals("d")) {
					if (pc.getLevel() >= 60 && pc.getLevel() <= 69) {
						if (pc.getInventory().checkItem(4100321, 1) && pc.getInventory().checkItem(4100349, 100)
								&& pc.getInventory().checkItem(3000231, 1)) {
							pc.getInventory().consumeItem(4100321, 1);
							pc.getInventory().consumeItem(4100349, 100);
							pc.getInventory().consumeItem(3000231, 1);

							pc.getInventory().storeItem(4100350, 2);
							Level52Exp(pc, 1);
							htmlid = "ev_dafne";
						} else {
							pc.sendPackets(3565);
							htmlid = "ev_dafne";
						}
					} else {
						htmlid = "ev_dafne";
					}
				}
				if (s.equals("c")) {
					if (pc.getLevel() >= 60 && pc.getLevel() <= 69) {
						if (pc.getInventory().checkItem(4100321, 1) && pc.getInventory().checkItem(4100349, 100)
								&& pc.getInventory().checkItem(1000007, 1)) {
							pc.getInventory().consumeItem(4100321, 1);
							pc.getInventory().consumeItem(4100349, 100);
							pc.getInventory().consumeItem(1000007, 1);

							pc.getInventory().storeItem(4100350, 2);
							Level52Exp(pc, 2);
							htmlid = "ev_dafne";
						} else {
							pc.sendPackets(3565);
							htmlid = "ev_dafne";
						}
					} else {
						htmlid = "ev_dafne";
					}
				}
				if (s.equals("b")) {
					if (pc.getLevel() >= 60 && pc.getLevel() <= 69) {
						if (pc.getInventory().checkItem(4100321, 1) && pc.getInventory().checkItem(4100349, 100)
								&& pc.getInventory().checkItem(1000004, 1)) {
							pc.getInventory().consumeItem(4100321, 1);
							pc.getInventory().consumeItem(4100349, 100);
							pc.getInventory().consumeItem(1000004, 1);

							pc.getInventory().storeItem(4100350, 2);
							Level52Exp(pc, 3);
							htmlid = "ev_dafne";
						} else {
							pc.sendPackets(3565);
							htmlid = "ev_dafne";
						}
					} else {
						htmlid = "ev_dafne";
					}
				}
				if (s.equals("a")) {
					if (pc.getLevel() >= 60 && pc.getLevel() <= 69) {
						if (pc.getInventory().checkItem(4100321, 1) && pc.getInventory().checkItem(4100349, 100)) {
							pc.getInventory().consumeItem(4100321, 1);
							pc.getInventory().consumeItem(4100349, 100);

							pc.getInventory().storeItem(4100350, 1);
							Level52Exp(pc, 4);
							htmlid = "ev_dafne";
						} else {
							pc.sendPackets(3565);
							htmlid = "ev_dafne";
						}
					} else {
						htmlid = "ev_dafne";
					}
				}
				// 알드란
			} else if (npcid == 80077) {
				if (s.equals("a")) {
					if (pc.getInventory().checkItem(41207, 1)) {
						pc.start_teleport(32674, 32871, 550, pc.getHeading(), 18339, true, false);
						htmlid = "";
					} else {
						htmlid = "aldran9";
					}
				} else if (s.equals("b")) {
					if (pc.getInventory().checkItem(41207, 1)) {
						pc.start_teleport(32778, 33009, 550, pc.getHeading(), 18339, true, false);
						htmlid = "";
					} else {
						htmlid = "aldran9";
					}
				} else if (s.equals("c")) {
					if (pc.getInventory().checkItem(41207, 1)) {
						pc.start_teleport(32471, 32766, 550, pc.getHeading(), 18339, true, false);
						htmlid = "";
					} else {
						htmlid = "aldran9";
					}
				} else if (s.equals("d")) {
					if (pc.getInventory().checkItem(41207, 1)) {
						pc.start_teleport(32511, 32998, 550, pc.getHeading(), 18339, true, false);
						htmlid = "";
					} else {
						htmlid = "aldran9";
					}
				} else if (s.equals("e")) {
					if (pc.getInventory().checkItem(41207, 1)) {
						pc.start_teleport(32998, 33028, 558, pc.getHeading(), 18339, true, false);

						htmlid = "";
					} else {
						htmlid = "aldran9";
					}
				}

				/** 投石機 **/

			} else if (npcid == 7000082 || npcid == 7000083 || npcid == 7000084 || npcid == 7000085 || npcid == 7000086
					|| npcid == 7000087) {
				if (s.equalsIgnoreCase("0-5") // 向外城門方向發射！
						|| s.equalsIgnoreCase("0-6") // 向內城門方向發射！
						|| s.equalsIgnoreCase("0-7") // 向守護塔方向發射！
						|| s.equalsIgnoreCase("1-16") // 向外城門方向發射沉默炸彈！
						|| s.equalsIgnoreCase("1-17") // 向內城門前方發射沉默炸彈！
						|| s.equalsIgnoreCase("1-18") // 向內城門左側發射沉默炸彈！
						|| s.equalsIgnoreCase("1-19") // 向內城門右側發射沉默炸彈！
						|| s.equalsIgnoreCase("1-20") // 向守護塔方向發射沉默炸彈！
						// 守城
						|| s.equalsIgnoreCase("0-9") // 向外城門方向發射！
				) {
					int locx = 0;
					int locy = 0;
					int gfxid = 0;
					int castleid = 0;
					int npcId = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
					if (s.equalsIgnoreCase("0-5")) { // 向外城門方向發射！
						switch (npcId) {
							case 7000086: // 5點方向攻城，奧克堡攻城方
								locx = 32795;
								locy = 32315;
								gfxid = 12197; // 右側
								castleid = 2;
								break;
							case 7000082: // 5點方向攻城，基蘭城攻城方
								locx = 33632;
								locy = 32731;
								gfxid = 12197; // 右側
								castleid = 15482;
								break;
							case 7000084: // 7點方向攻城，肯特城攻城方
								locx = 33114;
								locy = 32771;
								gfxid = 12193; // 左側
								castleid = 1;
								break;
						}
					} else if (s.equalsIgnoreCase("0-6")) { // 向內城門方向發射！
						switch (npcId) {
							case 7000086: // 11點方向攻城，奧克堡攻城方
								locx = 32798;
								locy = 32268;
								gfxid = 12197; // 右側
								castleid = 2;
								break;
							case 7000082: // 11點方向攻城，基蘭城攻城方
								locx = 33632;
								locy = 32664;
								gfxid = 12197; // 右側
								castleid = 15482;
								break;
							case 7000084: // 2點方向攻城，肯特城攻城方
								locx = 33171;
								locy = 32763;
								gfxid = 12197; // 左側
								castleid = 1;
								break;
						}
					} else if (s.equalsIgnoreCase("0-7")) { // 수호탑 방향으로 발사!
						switch (npcId) {
						case 7000086: // 11시 방향 공성 오크요새 공성측
							locx = 32798;
							locy = 32285;
							gfxid = 12197; // 우측
							castleid = 2;
							break;
						case 7000082: // 11시 방향 공성 기란성 공성측
							locx = 33631;
							locy = 32678;
							gfxid = 12197; // 우측
							castleid = 15482;
							break;
						case 7000084: // 2시 방향 공성 켄트성 공성측
							locx = 33168;
							locy = 32779;
							gfxid = 12197; // 좌측
							castleid = 1;
							break;
						}
					} else if (s.equalsIgnoreCase("0-9")) { // 向外城門方向發射！
						int pcCastleId = 0;
						if (pc.getClanid() != 0) {
							L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
							if (clan != null) {
								pcCastleId = clan.getCastleId();
							}
						}
						switch (npcId) {
							case 7000087: // 11點方向攻城，奧克堡防守方
								if (isExistDefenseClan(L1CastleLocation.OT_CASTLE_ID)) {
									if (pcCastleId != L1CastleLocation.OT_CASTLE_ID) {
										pc.sendPackets(new S_ServerMessage(3682));
										// 投石機使用失敗：僅能由守護城堡的血盟君主使用
										return htmlid;
									}
								}
								locx = 32794;
								locy = 32320;
								gfxid = 12193; // 右側
								castleid = 2;
								break;
							case 7000083: // 11點方向攻城，基蘭城防守方
								if (isExistDefenseClan(L1CastleLocation.GIRAN_CASTLE_ID)) {
									if (pcCastleId != L1CastleLocation.GIRAN_CASTLE_ID) {
										pc.sendPackets(new S_ServerMessage(3682));
										// 投石機使用失敗：僅能由守護城堡的血盟君主使用
									return htmlid;
								}
							}
							locx = 33631;
							locy = 32738;
							gfxid = 12193; // 우측
							castleid = 15482;
							break;
						case 7000085: // 2시 방향 공성 켄트성 수성측
							if (isExistDefenseClan(L1CastleLocation.KENT_CASTLE_ID)) {
								if (pcCastleId != L1CastleLocation.KENT_CASTLE_ID) {
									pc.sendPackets(new S_ServerMessage(3682));
									// 투석기 사용: 실패(성을 수호하는 성혈 군주만 사용 가능)
									return htmlid;
								}
							}
							locx = 33107;
							locy = 32770;
							gfxid = 12197; // 우측
							castleid = 1;
							break;
						}

						/*
						 * <a action="1-16">向外城門方向發射沉默炸彈！</a><br> <a action="1-17">向內城門前方發射沉默炸彈！</a><br>
						 * <a action="1-18">向內城門左側發射沉默炸彈！</a><br> <a action="1-19">向內城門右側發射沉默炸彈！</a><br>
						 * <a action="1-20">向守護塔方向發射沉默炸彈！</a><br><br> } else
						 * if (s.equalsIgnoreCase("0-9")) { //向外城門方向發射沉默炸彈！
						 */

					} else {
						pc.sendPackets(new S_SystemMessage("無法使用沉默炸彈。"));
						return htmlid;
					}

					boolean isNowWar = false;
					isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleid);
					if (!isNowWar) {
						pc.sendPackets(new S_ServerMessage(3683));
						// 投石機使用失敗：僅能在攻城時間使用
						return htmlid;
					}
//
					boolean inWar = MJWar.isNowWar(pc.getClan());
					if (!(pc.isCrown() && inWar && isNowWar)) {
						pc.sendPackets(new S_ServerMessage(3681));
						// 投石機使用失敗：僅能由宣戰的君主使用
						return htmlid;
					}
					if (pc.getlastShellUseTime() + 10000L > System.currentTimeMillis()) {
						pc.sendPackets(new S_ServerMessage(3680));
						// 投石機使用失敗：需要重裝時間
						return htmlid;
					}

					if (obj != null) {
						if (obj instanceof L1CataInstance) {
							L1CataInstance npc = (L1CataInstance) obj;
							if (pc.getInventory().consumeItem(30124, 1)) {
								Broadcaster.broadcastPacket(npc,
										new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Attack));
								S_EffectLocation packet = new S_EffectLocation(locx, locy, gfxid);
								pc.sendPackets(packet);
								Broadcaster.wideBroadcastPacket(pc, packet, 100);
								getShellDmg(locx, locy);
								// 침묵포탄(locx, locy); // 침묵포탄 테스트
								pc.updatelastShellUseTime();
							} else {
								pc.sendPackets(new S_ServerMessage(337, "$16785"));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlid;
	}

	private static boolean SealedItem(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr,boolean identi) {
		// SealedItem(pc, 5000045, 1, 5, 128);
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(identi);
			item.setEnchantLevel(EnchantLevel);
			item.setAttrEnchantLevel(attr);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				item.setBless(Bless);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			} else { // 無法持有的情況下，不取消掉落到地面的處理（防止作弊）
				pc.sendPackets(new S_ServerMessage(82));
				// 無法再持有更多物品，因為重量過重或者背包已滿。
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName())); //
			return true;
		} else {
			return false;
		}
	}

	private boolean grantEnchant(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(new S_ServerMessage(82));
				// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0를
			// 손에
			// 넣었습니다.
			return true;
		} else {
			return false;
		}
	}

	private void Level52Exp(L1PcInstance pc, int type) {
		long needExp = ExpTable.getNeedExpNextLevel(52);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		long exp = 0;
		if (type == 1) {
			exp = (long) (needExp * 32.00D * exppenalty);
		} else if (type == 2) {
			exp = (long) (needExp * 14.00D * exppenalty);
		} else if (type == 3) {
			exp = (long) (needExp * 3.50D * exppenalty);
		} else if (type == 4) {
			exp = (long) (needExp * 1.50D * exppenalty);
		} else {
			pc.sendPackets(3564);
		}
		pc.add_exp(exp);
		pc.send_effect(3944, true);
	}

	private void rewardExperienceBasedOnLevel52(L1PcInstance pc, int type) {
		long needExp = ExpTable.getNeedExpNextLevel(52);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		long exp = 0;
		if (type == 1) {
			exp = (long) (needExp * 0.02D * exppenalty);
		} else if (type == 2) {
			exp = (long) (needExp * 0.05D * exppenalty);
		} else if (type == 3) {
			exp = (long) (needExp * 0.20D * exppenalty);
		} else {
			pc.sendPackets(3564);
		}
		pc.add_exp(exp);
		pc.send_effect(3944, true);
	}

	public void addQuestExp(L1PcInstance pc, int type) {
		long curtimeN = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime() + 1 > curtimeN) {
			// long time = (pc.getQuizTime() + 1) - curtimeN;
			// pc.sendPackets(new S_ChatPacket(pc, time + " 秒後再使用。"));
			return;
		}

		long needExp = ExpTable.getNeedExpNextLevel(52);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		long exp = 0;
		if (type == 1) {
			exp = (long) (needExp * 0.05D * exppenalty);
		} else if (type == 2) {
			exp = (long) (needExp * 0.06D * exppenalty);
		} else if (type == 3) {
			exp = (long) (needExp * 0.05D * exppenalty);
		} else if (type == 4) {
			exp = (long) (needExp * 0.01D * exppenalty);
		} else {
			pc.sendPackets(3564);
		}
		pc.setQuizTime(curtimeN);
		pc.add_exp(exp);
		pc.send_effect(3944, true);
	}

	private void GrowthCoupon(L1PcInstance pc, int type) {
		long exp = 0;
		int itemid = 0;
		int limitlvmin = 0;
		int limitlvMax = 0;

		if (type == 1) {
			exp = (long) ((ExpTable.getExpByLevel(pc.getLevel() + 1) - 1) - pc.get_exp() + 100L);
			itemid = 4100470;
			limitlvmin = 55;
			limitlvMax = 70;
		} else if (type == 2) {
			exp = (long) ((ExpTable.getExpByLevel(pc.getLevel() + 1) - 1) - pc.get_exp() + 100L);
			itemid = 4100471;
			limitlvmin = 71;
			limitlvMax = 75;
		} else if (type == 3) {
			exp = (long) ((ExpTable.getExpByLevel(pc.getLevel() + 1) - 1) - pc.get_exp() + 100L);
			itemid = 4100472;
			limitlvmin = 76;
			limitlvMax = 80;
		} else {
			pc.sendPackets(3564);
		}

		int level = ExpTable.getLevelByExp(pc.get_exp());
		if (pc.getInventory().checkItem(itemid, 1)) {
			if (pc.getLevel() >= limitlvmin && pc.getLevel() < limitlvMax) {
				if (level >= limitlvMax) {
					pc.sendPackets("無法再獲得經驗值。");
					return;
				} else {
					pc.getInventory().consumeItem(itemid, 1);
					pc.add_exp(exp);
					pc.setCurrentHp(pc.getMaxHp());
					pc.setCurrentMp(pc.getMaxMp());
					pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 3944)));
					Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
				}
			} else {
				pc.sendPackets(String.format("只能在等級 %d 以上，%d 以下使用。", limitlvmin, limitlvMax));
				return;
			}
		} else {
			pc.sendPackets("沒有成長優惠券。");
			return;
		}
	}

	private boolean createNewItem(L1PcInstance pc, String npcName, int item_id, int count, int enchant) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(enchant);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
			}
			pc.sendPackets(new S_ServerMessage(143, npcName, item.getLogName()));
			return true;
		} else {
			return false;
		}
	}

	private boolean isExistDefenseClan(int castleId) {
		boolean isExistDefenseClan = false;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (castleId == clan.getCastleId()) {
				isExistDefenseClan = true;
				break;
			}
		}
		return isExistDefenseClan;
	}

	private void getShellDmg(int locx, int locy) {
		L1PcInstance targetPc = null;
		L1NpcInstance targetNpc = null;
		L1EffectInstance effect = L1EffectSpawn.getInstance().spawnEffect(81154, 1 * 1000, locx, locy, (short) 4);
		for (L1Object object : L1World.getInstance().getVisibleObjects(effect, 3)) {
			if (object == null) {
				continue;
			}
			if (!(object instanceof L1Character)) {
				continue;
			}
			if (object.getId() == effect.getId()) {
				continue;
			}

			if (object instanceof L1PcInstance) {
				targetPc = (L1PcInstance) object;
				targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
				Broadcaster.broadcastPacket(targetPc, new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
				targetPc.receiveDamage(targetPc, 100, 3);
			} else if (object instanceof L1SummonInstance || object instanceof L1PetInstance) {
				targetNpc = (L1NpcInstance) object;
				Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(targetNpc.getId(), ActionCodes.ACTION_Damage));
				targetNpc.receiveDamage(targetNpc, (int) 100);
			}
		}
	}

	private String convertToYurie(String s, L1PcInstance pc) {
		String htmlid = null;
		int level = 80;
		if (s.equalsIgnoreCase("b")) {
			if (pc.getLevel() >= level) {
				if (pc.getInventory().checkItem(40308, 4000000)) {
					if (!pc.getInventory().checkItem(4200254)) {
						pc.getInventory().consumeItem(40308, 4000000);
						pc.getInventory().storeItem(4200253, 1);
						htmlid = "id_yurie06";
					} else {
						htmlid = "id_yurie04";
					}
				} else {
					htmlid = "id_yurie05";
				}
			} else {
				htmlid = "id_yurie07";
			}
		}
		return htmlid;
	}
}
