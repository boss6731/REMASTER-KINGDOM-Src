package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;

import java.util.Random;

import l1j.server.Config;
import l1j.server.IndunSystem.Training.BossTrainingSystem;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.KeyTable;
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
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
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
            if (npcid == 200201) {// ������ ����
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
                // ��ǳ�� ����
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
                // �ĸ��� ���
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
                // ��ũ�������� ������
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
                // Ȥ���� â
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
                // ���Ű�
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
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200018) {// ����ġ
                if (s.equalsIgnoreCase("0")) {// �ѹ��� ����
                    if (pc.getLevel() < 51) {
                        pc.add_exp_for_ready((ExpTable.getExpByLevel(51) - 1) - pc.get_exp() + ((ExpTable.getExpByLevel(51) - 1) / 100L));
                    } else if (pc.getLevel() >= 51 && pc.getLevel() < Config.ServerAdSetting.Expreturn) {
                        pc.add_exp_for_ready((ExpTable.getExpByLevel(pc.getLevel() + 1) - 1) - pc.get_exp() + 100L);
                        pc.setCurrentHp(pc.getMaxHp());
                        pc.setCurrentMp(pc.getMaxMp());
                    }
                } else if (s.equalsIgnoreCase("1")) {// �ѹ��� ����
                    if (pc.getLevel() >= Config.ServerAdSetting.Expreturn && pc.getLevel() <= Config.ServerAdSetting.Expreturn) {
                        pc.add_exp_for_ready(
                                (ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) - pc.get_exp() + ((ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) / 30000000));
                    } else if (pc.getLevel() <= Config.ServerAdSetting.Expreturn && pc.getLevel() < Config.ServerAdSetting.Expreturn) {
                        pc.add_exp_for_ready(
                                (ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) - pc.get_exp() + ((ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) / 30000000));
                        pc.setCurrentHp(pc.getMaxHp());
                        pc.setCurrentMp(pc.getMaxMp());
                    }
                } else if (s.equalsIgnoreCase("2")) {// �ű�����
                    if (pc.getLevel() >= Config.ServerAdSetting.NewCha) {
                        pc.sendPackets(new S_SystemMessage("��?������⢣�����������굡�"));
                        return htmlid;
                    } else if (pc.getInventory().checkItem(7241, 1) || pc.getInventory().checkItem(1000004, 1)) {
                        pc.sendPackets(new S_SystemMessage("��������ۡۯ?�䡣"));
                        return htmlid;
                    }
                    ������(pc, 7241, 5, 0, 1, 0, true); // ��?�
                    ������(pc, 3000231, 3, 0, 1, 0, true); // ������ӹ
                    ������(pc, 1000007, 10, 0, 1, 0, true); // ������ӹ
                }
/** ʥ����������ӥ (������?) */
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5163) {
                if (s.equalsIgnoreCase("j")) { // ʥ��
                    pc.sendPackets(new S_ServerMessage(3854));
                    return htmlid;
                } else if (s.equalsIgnoreCase("d")) { // ����
                    if (pc.getRedKnightClanId() == 0) {
                        pc.sendPackets(new S_SystemMessage("??ڱʥ����������ӥ��"));
                        return htmlid;
                    }

                    pc.setRedKnightClanId(0);
                    pc.sendPackets(new S_SystemMessage("?�������������ӥ��"));
                    pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, false);
                    return htmlid;
                }

                /** ī���� */
            } else if (npcid == 7000079) {
                if (s.equalsIgnoreCase("1")) { // �뿩
                    int countActiveMaps = BossTrainingSystem.getInstance().countRaidPotal();
                    if (pc.getInventory().checkItem(80500)) {
                        htmlid = "bosskey6";
                        // �̹� �Ʒü� ���踦 ������ ��� �� ������.
                        // ���� �е鲲�� �̿��Ͻ� �� �ֵ��� �ƷüҴ� �� ��� �� �ϳ� ���� �뿩�� �帮�� �ֽ��ϴ�.
                    } else if (countActiveMaps >= 99) {
                        htmlid = "bosskey3";
                        // �˼��մϴ�.
                        // ������ ��� �Ʒüҿ��� �Ʒ��� ���� �� �Դϴ�.
                    } else {
                        htmlid = "bosskey4";
                    }
                } else if (s.matches("[2-4]")) {
                    if (!pc.getInventory().checkItem(80500)) { // �׼� ���� ����
                        L1ItemInstance item = null;
                        int count = 0;
                        if (s.equalsIgnoreCase("2")) { // 4��
                            count = 4;
                        } else if (s.equalsIgnoreCase("3")) { // 8��
                            count = 8;
                        } else if (s.equalsIgnoreCase("4")) { // 16��
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
                            // ���� �Ʒ��� ������ �е鿡�� ���踦 ������ �ֽ� ���� ������ �����ֽø� �Ʒüҷ� �ȳ���
                            // �帮�ڽ��ϴ�.
                            // �Ʒü��� �뿩�ð��� �ִ� 4�ð��̸�, �Ʒ� ���̶� �ص� �뿩 �ð��� ����Ǹ� ���� �����
                            // ���� �Ʒü� ����� �����˴ϴ�.
                            // �Ʒÿ� ���͸� ��ȯ�Ͻ� ������ �׻� �Ʒü��� ���� ��� �ð��� Ȯ���Ͻñ� �ٶ��ϴ�.
                        } else {
                            htmlid = "bosskey5";
                            // �˼�������, ���Ḧ �������� �����ø� �ƷüҸ� �����帱 �� �����ϴ�.
                            // �Ƶ� �ձ��� �����ݸ����� �� ���� �ƷüҸ� �����ϴ� ���� ���� ���� �ƴ϶󼭿�.
                        }
                    } else {
                        htmlid = "bosskey6";
                        // �̹� �Ʒü� ���踦 ������ ��� �� ������.
                        // ���� �е鲲�� �̿��Ͻ� �� �ֵ��� �ƷüҴ� �� ��� �� �ϳ� ���� �뿩�� �帮�� �ֽ��ϴ�.
                    }
                } else if (s.equalsIgnoreCase("6")) { // ����
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
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7200022) {// ���ϵ����
                L1NpcInstance npc = (L1NpcInstance) obj;
                if (pc.isInvisble()) {
                    pc.sendPackets(new S_NpcChatPacket(npc, "�����������������������¡�", 0));
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
                if (s.equalsIgnoreCase("1")){ // ��������
                    if (pc.getInventory().consumeItem(80464, 1)) {
                        L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45456, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("2")) { // ��������
                    if (pc.getInventory().consumeItem(80465, 1)) {
                        L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45601, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("3")) { // ��ت
                    if (pc.getInventory().consumeItem(80450, 1)) {
                        L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45649, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��ت]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("4")) { // ����
                    if (pc.getInventory().consumeItem(80479, 1)) {
                        L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45617, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[������]�����������", 2));
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
            /** ������ */
            else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000080) {
                L1NpcInstance npc = (L1NpcInstance) obj;
                if (s.equalsIgnoreCase("A")) { // �������ڪ
                    if (pc.getInventory().consumeItem(80466, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 900076, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[����������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("B")) { // �������ڪ
                    if (pc.getInventory().consumeItem(80467, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 900070, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��ت����]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("C")) { // �������ڪ
                    if (pc.getInventory().consumeItem(80450, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45649, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��ت]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("D")) { // �������ڪ
                    if (pc.getInventory().consumeItem(80451, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45685, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��ժ]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
/** ������������ڪ **/
                if (s.equalsIgnoreCase("E")) {
                    if (pc.getInventory().consumeItem(80452, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45955, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[����]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("F")) {
                    if (pc.getInventory().consumeItem(80453, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45959, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[����]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("G")) {
                    if (pc.getInventory().consumeItem(80454, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45956, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[�������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("H")) {
                    if (pc.getInventory().consumeItem(80455, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45957, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("I")) {
                    if (pc.getInventory().consumeItem(80456, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45960, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("J")) {
                    if (pc.getInventory().consumeItem(80457, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45958, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[�������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("K")) {
                    if (pc.getInventory().consumeItem(80458, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45961, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��ڷ���]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("L")) {
                    if (pc.getInventory().consumeItem(80459, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45962, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("M")) {
                    if (pc.getInventory().consumeItem(80460, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45676, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[�������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("N")) {
                    if (pc.getInventory().consumeItem(80461, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45677, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[���]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("O")) {
                    if (pc.getInventory().consumeItem(80462, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45844, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��յ?]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("P")) {
                    if (pc.getInventory().consumeItem(80463, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45648, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
/** ͯ����ϱ��?��ڪ **/
                if (s.equalsIgnoreCase("Q")) {
                    if (pc.getInventory().consumeItem(80464, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45456, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("S")) {
                    if (pc.getInventory().consumeItem(80465, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45601, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                /** ��ط���� **/
                if (s.equalsIgnoreCase("T")) {
                    if (pc.getInventory().consumeItem(80468, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310015, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[?����������ҳ��]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("U")) {
                    if (pc.getInventory().consumeItem(80469, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310021, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[���������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("V")) {
                    if (pc.getInventory().consumeItem(80470, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310028, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��ϫ������С]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("W")) {
                    if (pc.getInventory().consumeItem(80471, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310034, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[������?����]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("X")) {
                    if (pc.getInventory().consumeItem(80472, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310041, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[����ͷ̫]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("Y")) {
                    if (pc.getInventory().consumeItem(80473, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310046, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[��������Ҭ����]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("Z")) {
                    if (pc.getInventory().consumeItem(80474, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310051, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[������������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("a")) {
                    if (pc.getInventory().consumeItem(80475, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310056, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[������������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("b")) {
                    if (pc.getInventory().consumeItem(80476, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310061, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[����������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("c")) {
                    if (pc.getInventory().consumeItem(80477, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310077, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[����̫������]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("d")) {
                    if (pc.getInventory().consumeItem(80478, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45600, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[����������Ρ�]�����������", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                // ��?NPC����? "����å����" (������)
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 8502036) {
                if (s.equalsIgnoreCase("a")) {
                    // ��?��ʫ���������������ID?60032��ڪ��
                    if (pc.getInventory().checkItem(60032)) {
                        // ���������ڪ������ۡ�����ͱ���ʫ������ڪ��
                        pc.sendPackets(new S_ChatPacket(pc, "������ڪ������"));
                        htmlid = "";
                    } else {
                        // ����������ڪ������������ʥ����ʫ��������
                        pc.getInventory().storeItem(60032, 1);
                        htmlid = "oldbook2";
                    }
                }
// ��?NPC����?ID?7210007��NPC
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7210007) {
                if (s.equalsIgnoreCase("a")) {
                    // ��?��ʫ��������������������60
                    if (pc.getLevel() >= 60) {
                        L1Quest quest = pc.getQuest();
                        int questStep = quest.get_step(L1Quest.QUEST_HAMO);
                        // ��?��ʫ����������������ID?820000��ڪ�������?ڱ����
                        if (!pc.getInventory().checkItem(820000) && questStep != L1Quest.QUEST_END) {
                            // ��������������?����?������ʫ����ID?820000��ڪ��
                            pc.getQuest().set_end(L1Quest.QUEST_HAMO);
                            pc.getInventory().storeItem(820000, 1); // �����ָӴ� (Ham's Pouch)
                            htmlid = "";
                        } else {
                            htmlid = "hamo1";
                        }
                    } else {
                        // ������ʫ��������60����ۡ�����ͱ���ʫ������?
                        htmlid = "hamo3";
                        pc.sendPackets(new S_SystemMessage("����60���߾����ʦ��������⡣"));
                    }
                }
            }
            /** ��پ�� Piar **/
        } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7310093) {
            if (s.equalsIgnoreCase("a")) {
//                    if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
                if (!pc.isPcBuff()) {
                    pc.sendPackets(new S_SystemMessage("����������?����ʦ��������¡�"));
                    htmlid = "pc_tell2";
                    return htmlid;
                }
                if (pc.getMap().isEscapable() || pc.isGm()) {
                    int rx = _random.nextInt(7);
                    int ux = 32768 + rx;
                    int uy = 32834 + rx; // �����
                    pc.start_teleport(ux, uy, 622, pc.getHeading(), 18339, true, false);
                }
            }
        }
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70611
                    || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70530) { // �����Ǽ���
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
                 pc.sendPackets("������������?��");
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
            pc.sendPackets(new S_SystemMessage(sec + "����������ġ�"));
            return htmlid;
        }
        if (pc.getLevel() < 80) {
            pc.sendPackets(new S_SystemMessage("����80���߾����ʦ���ġ�"));
            return htmlid;
        }
                if (s.equals("0")) { // ������ �޴´�.
                    int[] allBuffSkill = { 4048 };
                    pc.setBuffnoch(1);
                    L1SkillUse l1skilluse = null;
                    l1skilluse = new L1SkillUse();
                    for (int i = 0; i < allBuffSkill.length; i++) {
                        l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
                                L1SkillUse.TYPE_GMBUFF);
                    }
                    htmlid = "merisha2";
                    pc.curePoison();
                    pc.setBuffTime(time);
                }
    } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7310121
            || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7320030) {
        if (/*(pc.getClanRank() != L1Clan.����) && */(pc.getClanRank() != L1Clan.��������)
                && (pc.getClanRank() != L1Clan.����) && (pc.getClanRank() != L1Clan.���)
                && (pc.getClanRank() != L1Clan.����)) {
            pc.sendPackets(new S_SystemMessage("���������������ʦ������"));
            return htmlid;
        }
    }
                /*
                 * if (s.equals("a")) { // ������ �޴´�. if (!pc.getClan().decWarPoint()) {
                 * pc.sendPackets(new S_SystemMessage("���� ����Ʈ�� ���ڶ��ϴ�.")); return htmlid; }
                 */

                int[] allBuffSkill = { 14, 26, 42, 54, 48, 79, 160, 206, 211, 216, 158, 168 };
                // ��,��,��������,�����Ľ�
                pc.setBuffnoch(1);
                L1SkillUse l1skilluse = null;
                l1skilluse = new L1SkillUse();
                for (int i = 0; i < allBuffSkill.length; i++) {
                    l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
                            L1SkillUse.TYPE_GMBUFF);
                    // }
                    pc.sendPackets(new S_SkillSound(pc.getId(), 830));
                    pc.curePoison();
                    // pc.setBuffTime(time);// ����������
                }
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 6200008) {
                long time = System.currentTimeMillis();
                int useTime = 20 * 60 * 1000; // 20�����

                if (pc.getBuffTime() + (useTime) > time) {
                long sec = ((pc.getBuffTime() + (useTime)) - time) / 1000;
                pc.sendPackets(new S_SystemMessage(sec + "����������ġ�"));
                return htmlid;
                }
                if (s.equals("a")) { // ������ �޴´�.
                    int[] allBuffSkill = { 26, 42, 48, 158 };
                    // ��,��,��������,�����Ľ�
                    pc.setBuffnoch(1);
                    L1SkillUse l1skilluse = null;
                    l1skilluse = new L1SkillUse();
                    for (int i = 0; i < allBuffSkill.length; i++) {
                        l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
                                L1SkillUse.TYPE_GMBUFF);
                    }
                    pc.sendPackets(new S_SkillSound(pc.getId(), 830));
                    pc.curePoison();
                    pc.setBuffTime(time);
                }
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 900135) {
                htmlid = ����(s, pc);
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000055) {
    try{
        String ItemName=""; // �������������ݬ��ܨ� ItemName
        String Stat=""; // �������������ݬ��ܨ� Stat
        String Class=""; // �������������ݬ��ܨ� Class
        String Level=""; // �������������ݬ��ܨ� Level
        String DelItemName=""; // �������������ݬ��ܨ� DelItemName
        int itemid=0; // �����������ܨ� itemid ?�����? 0
        int Delitemid=0; // �����������ܨ� Delitemid ?�����? 0

        if (s.equals("str55")) { // �����ݬ�� s ��?���� "str55"

            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"

        else if (pc.isWizard()) // ������ʫ����������

            Class = "(����)"; // ��������٣��? "����"

        else if (pc.isDarkelf()) // ������ʫ��������������

            Class = "(��������)"; // ��������٣��? "��������"

        else if (pc.isCrown()) // ������ʫ����������

            Class = "(����)"; // ��������٣��? "����"

        else if (pc.isDragonknight()) // ������ʫ������ף����

            Class = "(ף����)"; // ��������٣��? "ף����"

        else if (pc.isBlackwizard()) // ������ʫ������������

            Class = "(������)"; // ��������٣��? "������"

        else if (pc.is����() || pc.isKnight()) // ������ʫ����������������

            Class = "(����,����)"; // ��������٣��? "����,����"

        else if (pc.isFencer()) // ������ʫ����������

            Class = "(����)"; // ��������٣��? "����"

                Level = "(55)"; // ��������? "55"

        Stat = "����"; // ��������? "����"
        } else if (s.equals("dex55")) { // �����ݬ�� s ��?���� "dex55"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"

        else if (pc.isWizard()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

        else if (pc.isDarkelf()) // ������ʫ��������������
            Class = "(��������)"; // ��������٣��? "��������"

        else if (pc.isCrown()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

        else if (pc.isDragonknight()) // ������ʫ������ף����
            Class = "(ף����)"; // ��������٣��? "ף����"

        else if (pc.isBlackwizard()) // ������ʫ������������
            Class = "(������)"; // ��������٣��? "������"

        else if (pc.is����() || pc.isKnight()) // ������ʫ����������������
            Class = "(����,����)"; // ��������٣��? "����,����"

        else if (pc.isFencer()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

                Level = "(55)"; // ��������? "55"

        Stat = "����"; // ��������? "����"
        } else if (s.equals("con55")) { // �����ݬ�� s ��?���� "con55"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"

        else if (pc.isWizard()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

        else if (pc.isDarkelf()) // ������ʫ��������������
            Class = "(��������)"; // ��������٣��? "��������"

        else if (pc.isCrown()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

        else if (pc.isDragonknight()) // ������ʫ������ף����
            Class = "(ף����)"; // ��������٣��? "ף����"

        else if (pc.isBlackwizard()) // ������ʫ������������
            Class = "(������)"; // ��������٣��? "������"

        else if (pc.is����() || pc.isKnight()) // ������ʫ����������������
            Class = "(����,����)"; // ��������٣��? "����,����"

        else if (pc.isFencer()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

                Level = "(55)"; // ��������? "55"

        Stat = "����"; // ��������? "����"
        } else if (s.equals("int55")) { // �����ݬ�� s ��?���� "int55"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"

        else if (pc.isWizard()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

        else if (pc.isDarkelf()) // ������ʫ��������������
            Class = "(��������)"; // ��������٣��? "��������"

        else if (pc.isCrown()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

        else if (pc.isDragonknight()) // ������ʫ������ף����
            Class = "(ף����)"; // ��������٣��? "ף����"

        else if (pc.isBlackwizard()) // ������ʫ������������
            Class = "(������)"; // ��������٣��? "������"

        else if (pc.is����() || pc.isKnight()) // ������ʫ����������������
            Class = "(����,����)"; // ��������٣��? "����,����"

        else if (pc.isFencer()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"


                Level = "(55)"; // ��������? "55"

        Stat = "����"; // ��������? "����"
        } else if (s.equals("wis55")) { // �����ݬ�� s ��?���� "wis55"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
        else if (pc.isWizard()) // ������ʫ����������

            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDarkelf()) // ������ʫ��������������

            Class = "(��������)"; // ��������٣��? "��������"
        else if (pc.isCrown()) // ������ʫ����������

            Class = "(����)"; // ��������٣��? "����"

        else if (pc.isDragonknight()) // ������ʫ������ף����

            Class = "(ף����)"; // ��������٣��? "ף����"

        else if (pc.isBlackwizard()) // ������ʫ������������

            Class = "(������)"; // ��������٣��? "������"
        else if (pc.is����() || pc.isKnight()) // ������ʫ����������������

            Class = "(����,����)"; // ��������٣��? "����,����"
        else if (pc.isFencer()) // ������ʫ����������

            Class = "(����)"; // ��������٣��? "����"

                Level = "(55)"; // ��������? "55"

        Stat = "����"; // ��������? "����"
        } else if (s.equals("str70")) { // �����ݬ�� s ��?���� "str70"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
        else if (pc.isWizard()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDarkelf()) // ������ʫ��������������
            Class = "(��������)"; // ��������٣��? "��������"
        else if (pc.isCrown()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDragonknight()) // ������ʫ������ף����
            Class = "(ף����)"; // ��������٣��? "ף����"
        else if (pc.isBlackwizard()) // ������ʫ������������
            Class = "(������)"; // ��������٣��? "������"
        else if (pc.is����()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        (pc.isKnight()) // ������ʫ����������
        Class = "(����)"; // ��������٣��? "����"
        else if (pc.isFencer()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

                Level = "(70)"; // ��������? "70"
        Stat = "����"; // ��������? "����"
        } else if (s.equals("dex70")) { // �����ݬ�� s ��?���� "dex70"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
        else if (pc.isWizard()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDarkelf()) // ������ʫ��������������
            Class = "(��������)"; // ��������٣��? "��������"
        else if (pc.isCrown()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDragonknight()) // ������ʫ������ף����
            Class = "(ף����)"; // ��������٣��? "ף����"
        else if (pc.isBlackwizard()) // ������ʫ������������
            Class = "(������)"; // ��������٣��? "������"
        else if (pc.is����()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isKnight()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isFencer()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

                Level = "(70)"; // ��������? "70"
        Stat = "����"; // ��������? "����"
        } else if (s.equals("con70")) { // �����ݬ�� s ��?���� "con70"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
        else if (pc.isWizard()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDarkelf()) // ������ʫ��������������
            Class = "(��������)"; // ��������٣��? "��������"
        else if (pc.isCrown()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDragonknight()) // ������ʫ������ף����
            Class = "(ף����)"; // ��������٣��? "ף����"
        else if (pc.isBlackwizard()) // ������ʫ������������
            Class = "(������)"; // ��������٣��? "������"
        else if (pc.is����()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isKnight()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isFencer()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

                Level = "(70)"; // ��������? "70"
        Stat = "����"; // ��������? "����"
        } else if (s.equals("int70")) { // �����ݬ�� s ��?���� "int70"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
        else if (pc.isWizard()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDarkelf()) // ������ʫ��������������
            Class = "(��������)"; // ��������٣��? "��������"
        else if (pc.isCrown()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDragonknight()) // ������ʫ������ף����
            Class = "(ף����)"; // ��������٣��? "ף����"
        else if (pc.isBlackwizard()) // ������ʫ������������
            Class = "(������)"; // ��������٣��? "������"
        else if (pc.is����()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isKnight()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isFencer()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

                Level = "(70)"; // ��������? "70"
        Stat = "����"; // ��������? "����"
        } else if (s.equals("wis70")) { // �����ݬ�� s ��?���� "wis70"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
        else if (pc.isWizard()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDarkelf()) // ������ʫ��������������
            Class = "(��������)"; // ��������٣��? "��������"
        else if (pc.isCrown()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDragonknight()) // ������ʫ������ף����
            Class = "(ף����)"; // ��������٣��? "ף����"
        else if (pc.isBlackwizard()) // ������ʫ������������
            Class = "(������)"; // ��������٣��? "������"
        else if (pc.is����()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isKnight()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isFencer()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

                Level = "(70)"; // ��������? "70"
        Stat = "����"; // ��������? "����"
        } else if (s.equals("str80")) { // �����ݬ�� s ��?���� "str80"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
        else if (pc.isWizard()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDarkelf()) // ������ʫ��������������
            Class = "(��������)"; // ��������٣��? "��������"
        else if (pc.isCrown()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDragonknight()) // ������ʫ������ף����
            Class = "(ף����)"; // ��������٣��? "ף����"
        else if (pc.isBlackwizard()) // ������ʫ������������
            Class = "(������)"; // ��������٣��? "������"
        else if (pc.is����()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isKnight()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isFencer()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

                Level = "(80)"; // ��������? "80"
        Stat = "����"; // ��������? "����"
        } else if (s.equals("dex80")) { // �����ݬ�� s ��?���� "dex80"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
        else if (pc.isWizard()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDarkelf()) // ������ʫ��������������
            Class = "(��������)"; // ��������٣��? "��������"
        else if (pc.isCrown()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDragonknight()) // ������ʫ������ף����
            Class = "(ף����)"; // ��������٣��? "ף����"
        else if (pc.isBlackwizard()) // ������ʫ������������
            Class = "(������)"; // ��������٣��? "������"
        else if (pc.is����()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isKnight()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isFencer()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

               Level = "(80)"; // ��������? "80"
        Stat = "����"; // ��������? "����"
        } else if (s.equals("con80")) { // �����ݬ�� s ��?���� "con80"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
        else if (pc.isWizard()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDarkelf()) // ������ʫ��������������
            Class = "(��������)"; // ��������٣��? "��������"
        else if (pc.isCrown()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isDragonknight()) // ������ʫ������ף����
            Class = "(ף����)"; // ��������٣��? "ף����"
        else if (pc.isBlackwizard()) // ������ʫ������������
            Class = "(������)"; // ��������٣��? "������"
        else if (pc.is����()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isKnight()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"
        else if (pc.isFencer()) // ������ʫ����������
            Class = "(����)"; // ��������٣��? "����"

                Level = "(80)"; // ��������? "80"
        Stat = "����"; // ��������? "����"
        } else if (s.equals("int80")) { // �����ݬ�� s ��?���� "int80"
               if (pc.isElf()) // ������ʫ����������
                   Class = "(����)"; // ��������٣��? "����"
           else if (pc.isWizard()) // ������ʫ����������
               Class = "(����)"; // ��������٣��? "����"
           else if (pc.isDarkelf()) // ������ʫ��������������
               Class = "(��������)"; // ��������٣��? "��������"
           else if (pc.isCrown()) // ������ʫ����������
               Class = "(����)"; // ��������٣��? "����"
           else if (pc.isDragonknight()) // ������ʫ������ף����
               Class = "(ף����)"; // ��������٣��? "ף����"
           else if (pc.isBlackwizard()) // ������ʫ������������
               Class = "(������)"; // ��������٣��? "������"
           else if (pc.is����()) // ������ʫ����������
               Class = "(����)"; // ��������٣��? "����"
           else if (pc.isKnight()) // ������ʫ����������
               Class = "(����)"; // ��������٣��? "����"
           else if (pc.isFencer()) // ������ʫ����������
               Class = "(����)"; // ��������٣��? "����"

                   Level = "(80)"; // ��������? "80"
           Stat = "���"; // ��������? "���"
        } else if (s.equals("wis80")) { // �����ݬ�� s ��?���� "wis80"
            if (pc.isElf()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isWizard()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isDarkelf()) // ������ʫ��������������
                Class = "(��������)"; // ��������٣��? "��������"
            else if (pc.isCrown()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isDragonknight()) // ������ʫ������ף����
                Class = "(ף����)"; // ��������٣��? "ף����"
            else if (pc.isBlackwizard()) // ������ʫ������������
                Class = "(������)"; // ��������٣��? "������"
            else if (pc.is����()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isKnight()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isFencer()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"

                     Level = "(80)"; // ��������? "80"
            Stat = "����"; // ��������? "����"
        } else if (s.equals("str85")) { // �����ݬ�� s ��?���� "str85"
                if (pc.isElf()) // ������ʫ����������
                    Class = "(����)"; // ��������٣��? "����"
            else if (pc.isWizard()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isDarkelf()) // ������ʫ��������������
                Class = "(��������)"; // ��������٣��? "��������"
            else if (pc.isCrown()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isDragonknight()) // ������ʫ������ף����
                Class = "(ף����)"; // ��������٣��? "ף����"
            else if (pc.isBlackwizard()) // ������ʫ������������
                Class = "(������)"; // ��������٣��? "������"
            else if (pc.is����()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isKnight()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isFencer()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"

                    Level = "(85)"; // ��������? "85"
            Stat = "����"; // ��������? "����"
            } else if (s.equals("dex85")) { // �����ݬ�� s ��?���� "dex85"
                if (pc.isElf()) // ������ʫ����������
                    Class = "(����)"; // ��������٣��? "����"
            else if (pc.isWizard()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isDarkelf()) // ������ʫ��������������
                Class = "(��������)"; // ��������٣��? "��������"
            else if (pc.isCrown()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isDragonknight()) // ������ʫ������ף����
                Class = "(ף����)"; // ��������٣��? "ף����"
            else if (pc.isBlackwizard()) // ������ʫ������������
                Class = "(������)"; // ��������٣��? "������"
            else if (pc.is����()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isKnight()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"
            else if (pc.isFencer()) // ������ʫ����������
                Class = "(����)"; // ��������٣��? "����"

                     Level = "(85)"; // ��������? "85"
            Stat = "����"; // ��������? "����"

        } else if (s.equals("con85")) { // �����ݬ�� s ��?���� "con85"

                if (pc.isElf()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"

                else if (pc.isWizard()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDarkelf()) // ������ʫ��������������

                    Class = "(��������)"; // ��������٣��? "��������"

                else if (pc.isCrown()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDragonknight()) // ������ʫ������ף����

                    Class = "(ף����)"; // ��������٣��? "ף����"

                else if (pc.isBlackwizard()) // ������ʫ������������

                    Class = "(������)"; // ��������٣��? "������"

                else if (pc.is����()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isKnight()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isFencer()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"


                        Level = "(85)"; // ��������? "85"

                Stat = "����"; // ��������? "����"

        } else if (s.equals("int85")) { // �����ݬ�� s ��?���� "int85"

                if (pc.isElf()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"

                else if (pc.isWizard()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDarkelf()) // ������ʫ��������������

                    Class = "(��������)"; // ��������٣��? "��������"

                else if (pc.isCrown()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDragonknight()) // ������ʫ������ף����

                    Class = "(ף����)"; // ��������٣��? "ף����"

                else if (pc.isBlackwizard()) // ������ʫ������������

                    Class = "(������)"; // ��������٣��? "������"

                else if (pc.is����()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isKnight()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isFencer()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                        Level = "(85)"; // ��������? "85"

                Stat = "���"; // ��������? "���"

        } else if (s.equals("wis85")) { // �����ݬ�� s ��?���� "wis85"

                if (pc.isElf()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"

                else if (pc.isWizard()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDarkelf()) // ������ʫ��������������

                    Class = "(��������)"; // ��������٣��? "��������"

                else if (pc.isCrown()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDragonknight()) // ������ʫ������ף����

                    Class = "(ף����)"; // ��������٣��? "ף����"

                else if (pc.isBlackwizard()) // ������ʫ������������

                    Class = "(������)"; // ��������٣��? "������"

                else if (pc.is����()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isKnight()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isFencer()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"


                        Level = "(85)"; // ��������? "85"

                Stat = "����"; // ��������? "����"

        } else if (s.equals("str90")) { // �����ݬ�� s ��?���� "str90"

                if (pc.isElf()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"

                else if (pc.isWizard()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDarkelf()) // ������ʫ��������������

                    Class = "(��������)"; // ��������٣��? "��������"

                else if (pc.isCrown()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDragonknight()) // ������ʫ������ף����

                    Class = "(ף����)"; // ��������٣��? "ף����"

                else if (pc.isBlackwizard()) // ������ʫ������������

                    Class = "(������)"; // ��������٣��? "������"

                else if (pc.is����()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isKnight()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isFencer()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                        Level = "(90)"; // ��������? "90"

                Stat = "����"; // ��������? "����"

        } else if (s.equals("dex90")) { // �����ݬ�� s ��?���� "dex90"

                if (pc.isElf()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

            else if (pc.isWizard()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

            else if (pc.isDarkelf()) // ������ʫ��������������

                Class = "(��������)"; // ��������٣��? "��������"

            else if (pc.isCrown()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

            else if (pc.isDragonknight()) // ������ʫ������ף����

                Class = "(ף����)"; // ��������٣��? "ף����"

            else if (pc.isBlackwizard()) // ������ʫ������������

                Class = "(������)"; // ��������٣��? "������"

            else if (pc.is����()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

            else if (pc.isKnight()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

            else if (pc.isFencer()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"


                    Level = "(90)"; // ��������? "90"

            Stat = "����"; // ��������? "����"

        } else if (s.equals("con90")) { // �����ݬ�� s ��?���� "con90"

                if (pc.isElf()) // ������ʫ����������

                       Class = "(����)"; // ��������٣��? "����"

               else if (pc.isWizard()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

               else if (pc.isDarkelf()) // ������ʫ��������������

                   Class = "(��������)"; // ��������٣��? "��������"

               else if (pc.isCrown()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

               else if (pc.isDragonknight()) // ������ʫ������ף����

                   Class = "(ף����)"; // ��������٣��? "ף����"

               else if (pc.isBlackwizard()) // ������ʫ������������

                   Class = "(������)"; // ��������٣��? "������"

               else if (pc.is����()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

               else if (pc.isKnight()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

               else if (pc.isFencer()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"


                       Level = "(90)"; // ��������? "90"

               Stat = "����"; // ��������? "����"

        } else if (s.equals("int90")) { // �����ݬ�� s ��?���� "int90"

                if (pc.isElf()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

            else if (pc.isWizard()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

            else if (pc.isDarkelf()) // ������ʫ��������������

                Class = "(��������)"; // ��������٣��? "��������"

            else if (pc.isCrown()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

            else if (pc.isDragonknight()) // ������ʫ������ף����

                Class = "(ף����)"; // ��������٣��? "ף����"

            else if (pc.isBlackwizard()) // ������ʫ������������

                Class = "(������)"; // ��������٣��? "������"

            else if (pc.is����()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

            else if (pc.isKnight()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

            else if (pc.isFencer()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"


                    Level = "(90)"; // ��������? "90"

            Stat = "���"; // ��������? "���"

        } else if (s.equals("wis90")) { // �����ݬ�� s ��?���� "wis90"

                 if (pc.isElf()) // ������ʫ����������

                       Class = "(����)"; // ��������٣��? "����"

               else if (pc.isWizard()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

               else if (pc.isDarkelf()) // ������ʫ��������������

                   Class = "(��������)"; // ��������٣��? "��������"

               else if (pc.isCrown()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

               else if (pc.isDragonknight()) // ������ʫ������ף����

                   Class = "(ף����)"; // ��������٣��? "ף����"

               else if (pc.isBlackwizard()) // ������ʫ������������

                   Class = "(������)"; // ��������٣��? "������"

               else if (pc.is����()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

               else if (pc.isKnight()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

               else if (pc.isFencer()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"


                       Level = "(90)"; // ��������? "90"

               Stat = "����"; // ��������? "����"

        } else if (s.equals("str91")) { // �����ݬ�� s ��?���� "str91"

                if (pc.isElf()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"

                else if (pc.isWizard()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDarkelf()) // ������ʫ��������������

                    Class = "(��������)"; // ��������٣��? "��������"

                else if (pc.isCrown()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDragonknight()) // ������ʫ������ף����

                    Class = "(ף����)"; // ��������٣��? "ף����"

                else if (pc.isBlackwizard()) // ������ʫ������������

                    Class = "(������)"; // ��������٣��? "������"

                else if (pc.is����()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isKnight()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isFencer()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"



                        Level = "(91)"; // ��������? "91"

        Stat = "����"; // ��������? "����"

        } else if (s.equals("dex91")) { // �����ݬ�� s ��?���� "dex91"

            if (pc.isElf()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

               else if (pc.isWizard()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

               else if (pc.isDarkelf()) // ������ʫ��������������

                   Class = "(��������)"; // ��������٣��? "��������"

               else if (pc.isCrown()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

                       (pc.isDragonknight()) // ������ʫ������ף����

                   Class = "(ף����)"; // ��������٣��? "ף����"

               else if (pc.isBlackwizard()) // ������ʫ������������

                   Class = "(������)"; // ��������٣��? "������"

               else if (pc.is����()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

               else if (pc.isKnight()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

               else if (pc.isFencer()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"



                       Level = "(91)"; // ��������? "91"

               Stat = "����"; // ��������? "����"

        } else if (s.equals("con91")) { // �����ݬ�� s ��?���� "con91"

            if (pc.isElf()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

           else if (pc.isWizard()) // ������ʫ����������

               Class = "(����)"; // ��������٣��? "����"

                   else if (pc.isDarkelf()) // ������ʫ��������������

               Class = "(��������)"; // ��������٣��? "��������"

           else if (pc.isCrown()) // ������ʫ����������

               Class = "(����)"; // ��������٣��? "����"

           else if (pc.isDragonknight()) // ������ʫ������ף����

               Class = "(ף����)"; // ��������٣��? "ף����"

           else if (pc.isBlackwizard()) // ������ʫ������������

               Class = "(������)"; // ��������٣��? "������"

           else if (pc.is����()) // ������ʫ����������

               Class = "(����)"; // ��������٣��? "����"

           else if (pc.isKnight()) // ������ʫ����������

               Class = "(����)"; // ��������٣��? "����"

           else if (pc.isFencer()) // ������ʫ����������

               Class = "(����)"; // ��������٣��? "����"


           Level = "(91)"; // ��������? "91"

           Stat = "����"; // ��������? "����"

            } else if (s.equals("int91")) { // �����ݬ�� s ��?���� "int91"

                if (pc.isElf()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

            else if (pc.isWizard()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

            else if (pc.isDarkelf()) // ������ʫ��������������

                Class = "(��������)"; // ��������٣��? "��������"

            else if (pc.isCrown()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

                    (pc.isDragonknight()) // ������ʫ������ף����

                Class = "(ף����)"; // ��������٣��? "ף����"

            else if (pc.isBlackwizard()) // ������ʫ������������

                Class = "(������)"; // ��������٣��? "������"

            else if (pc.is����()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

            else if (pc.isKnight()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

                    (pc.isFencer()) // ������ʫ����������

                Class = "(����)"; // ��������٣��? "����"

                        Level = "(91)"; // ��������? "91"

                Stat = "���"; // ��������? "���"

        } else if (s.equals("wis91")) { // �����ݬ�� s ��?���� "wis91"

                    if (pc.isElf()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"

                       (pc.isWizard()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDarkelf()) // ������ʫ��������������

                    Class = "(��������)"; // ��������٣��? "��������"

                else if (pc.isCrown()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                       else if (pc.isDragonknight()) // ������ʫ������ף����

                    Class = "(ף����)"; // ��������٣��? "ף����"

                else if (pc.isBlackwizard()) // ������ʫ������������

                    Class = "(������)"; // ��������٣��? "������"

                else if (pc.is����()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isKnight()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isFencer()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"


                        Level = "(91)"; // ��������? "91"

             Stat = "����"; // ��������? "����"

        } else if (s.equals("str92")) { // �����ݬ�� s ��?���� "str92"

                 if (pc.isElf()) // ������ʫ����������

                     Class = "(����)"; // ��������٣��? "����"

             else if (pc.isWizard()) // ������ʫ����������

                 Class = "(����)"; // ��������٣��? "����"

             else if (pc.isDarkelf()) // ������ʫ��������������

                 Class = "(��������)"; // ��������٣��? "��������"

             else if (pc.isCrown()) // ������ʫ����������

                 Class = "(����)"; // ��������٣��? "����"

             else if (pc.isDragonknight()) // ������ʫ������ף����

                 Class = "(ף����)"; // ��������٣��? "ף����"

             else if (pc.isBlackwizard()) // ������ʫ������������

                 Class = "(������)"; // ��������٣��? "������"

                     (pc.is����()) // ������ʫ����������

                 Class = "(����)"; // ��������٣��? "����"

             else if (pc.isKnight()) // ������ʫ����������

                 Class = "(����)"; // ��������٣��? "����"

             else if (pc.isFencer()) // ������ʫ����������

                 Class = "(����)"; // ��������٣��? "����"

                     Level = "(92)"; // ��������? "92"

             Stat = "����"; // ��������? "����"

        } else if (s.equals("dex92")) { // �����ݬ�� s ��?���� "dex92"

               if (pc.isElf()) // ������ʫ����������

                   Class = "(����)"; // ��������٣��? "����"

           else if (pc.isWizard()) // ������ʫ����������

               Class = "(����)"; // ��������٣��? "����"

           else if (pc.isDarkelf()) // ������ʫ��������������

               Class = "(��������)"; // ��������٣��? "��������"

           else if (pc.isCrown()) // ������ʫ����������

               Class = "(����)"; // ��������٣��? "����"

           else if (pc.isDragonknight()) // ������ʫ������ף����

               Class = "(ף����)"; // ��������٣��? "ף����"

           else if (pc.isBlackwizard()) // ������ʫ������������

               Class = "(������)"; // ��������٣��? "������"

           else if (pc.is����()) // ������ʫ����������

               Class = "(����)"; // ��������٣��? "����"

           else if (pc.isKnight()) // ������ʫ����������

               Class = "(����)"; // ��������٣��? "����"

           else if (pc.isFencer()) // ������ʫ����������

              Class = "(����)"; // ��������٣��? "����"


                      Level = "(92)"; // ��������? "92"

             Stat = "����"; // ��������? "����"

        } else if (s.equals("con92")) { // �����ݬ�� s ��?���� "con92"

                    if (pc.isElf()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"

                       else if (pc.isWizard()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                 else if (pc.isDarkelf()) // ������ʫ��������������

                    Class = "(��������)"; // ��������٣��? "��������"

                else if (pc.isCrown()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isDragonknight()) // ������ʫ������ף����

                    Class = "(ף����)"; // ��������٣��? "ף����"

                else if (pc.isBlackwizard()) // ������ʫ������������

                    Class = "(������)"; // ��������٣��? "������"

                else if (pc.is����()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isKnight()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"

                else if (pc.isFencer()) // ������ʫ����������

                    Class = "(����)"; // ��������٣��? "����"


                        Level = "(92)"; // ��������? "92"

                Stat = "����"; // ��������? "����"

        } else if (s.equals("int92")) { // �����ݬ�� s ��?���� "int92"

            if (pc.isElf()) // ������ʫ����������

                      Class = "(����)"; // ��������٣��? "����"


                    else if (pc.isWizard()) // ������ʫ����������



                        Class = "(����)"; // ��������٣��? "����"


                    else if (pc.isDarkelf()) // ������ʫ��������������



                        Class = "(��������)"; // ��������٣��? "��������"


                    else if (pc.isCrown()) // ������ʫ����������



                        Class = "(����)"; // ��������٣��? "����"


                    else if (pc.isDragonknight()) // ������ʫ������ף����



                  Class = "(ף����)"; // ��������٣��? "ף����"


                   else if (pc.isBlackwizard()) // ������ʫ������������



                       Class = "(������)"; // ��������٣��? "������"



                   else if (pc.is����()) // ������ʫ����������



                       Class = "(����)"; // ��������٣��? "����"



                   else if (pc.isKnight()) // ������ʫ����������



                       Class = "(����)"; // ��������٣��? "����"


                   else if (pc.isFencer()) // ������ʫ����������



                       Class = "(����)"; // ��������٣��? "����"



                         Level = "(92)"; // ��������? "92"

                 Stat = "���"; // ��������? "���"

                    } else if (s.equals("wis92")) { // �����ݬ�� s ��?���� "wis92"

                        if (pc.isElf()) // ������ʫ����������

                            Class = "(����)"; // ��������٣��? "����"

                    else if (pc.isWizard()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"

                    else if (pc.isDarkelf()) // ������ʫ��������������

                        Class = "(��������)"; // ��������٣��? "��������"

                    else if (pc.isCrown()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"

                        else if (pc.isDragonknight()) // ������ʫ������ף����

                        Class = "(ף����)"; // ��������٣��? "ף����"

                    else if (pc.isBlackwizard()) // ������ʫ������������

                        Class = "(������)"; // ��������٣��? "������"

                    else if (pc.is����()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"

                    else if (pc.isKnight()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"

                    else if (pc.isFencer()) // ������ʫ����������

                        Class = "(����)"; // ��������٣��? "����"


                    Level = "(92)"; // ��������? "92"

                    Stat = "����"; // ��������? "����"
                    }
        // ��������Գ��٣��
        ItemName = Stat + "�����ݬ��" + Level + Class; // ���� "��������ݬ��(92)(����)"
        DelItemName = "����ت����ݬ��" + Level; // ���� "����ت����ݬ��(92)"


        // ����٣��??������Գ��ID
        itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(ItemName);
        Delitemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(DelItemName);


        // ������� "����ت����ݬ��" Գ����?��ʥ���� "���ݬ��" Գ��
        if (pc.getInventory().consumeItem(Delitemid, 1)) {
        // ������Գ����ʥ����ʫ��ͷ����
        pc.getInventory().storeItem(itemid, 1, true); // ������"���������"

        // ������Գ����ټ��
        L1Item run = ItemTable.getInstance().getTemplate(itemid);

        // ����ʫۡ����ӣ����Գ�����ܨ
        pc.sendPackets(run.getNameId() + "����ǡ�"); // ���� "��������ݬ������ǡ�"
        } else {
        // �������� "����ت����ݬ��" Գ��������HTML ID?"riddle2"
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
                                pc.getInventory().storeItem(60036, 1); // ���� ������
                                // ���ָӴ�
                            } else if (s.equals("B")) {
                                pc.getInventory().storeItem(60037, 1); // ��ø��
                                // ������ ��
                            } else if (s.equals("C")) {
                                pc.getInventory().storeItem(60038, 1); // ü����
                                // ������ ��
                            } else if (s.equals("D")) {
                                pc.getInventory().storeItem(60039, 1); // ������
                                // ������ ��
                            } else if (s.equals("E")) {
                                pc.getInventory().storeItem(60040, 1); // ������
                                // ������ ��
                            }
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune6"));

                        } else {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune5"));
                        }
                    } else {
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune5"));
                    }
                }
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 1000001) {// ��������
                int locx = 0, locy = 0, map = 0;
                if (s.equalsIgnoreCase("b")) {// 1������
                    locx = 32783;
                    locy = 32751;
                    map = 43;
                } else if (s.equalsIgnoreCase("c")) {// 2������
                    locx = 32798;
                    locy = 32754;
                    map = 44;
                } else if (s.equalsIgnoreCase("d")) {// 3������
                    locx = 32776;
                    locy = 32731;
                    map = 45;
                } else if (s.equalsIgnoreCase("e")) {// 4������
                    locx = 32787;
                    locy = 32795;
                    map = 46;
                } else if (s.equalsIgnoreCase("f")) {// 5������
                    locx = 32796;
                    locy = 32745;
                    map = 47;
                } else if (s.equalsIgnoreCase("g")) {// 6������
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
                // ����
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 60169) {
                if (s.equalsIgnoreCase("a")) {
                    new L1SkillUse().handleCommands(pc, L1SkillId.BUFF_GUNTER, pc.getId(), pc.getX(), pc.getY(), null,
                            0, L1SkillUse.TYPE_SPELLSC);
                }
                // ũ����
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
                // ���ֹ��� ���� �翤 (�Ա� npc)
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
} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 522) { // ����NPC��ID��522
    if (s.equalsIgnoreCase("giveto")) { // �����ݬ�� s ���� "giveto"����������У�
        if (pc.getInventory().checkItem(40308, 200000)) { // ������ʫ��ͷ������200,000��ID?40308��ڪ��
            pc.getInventory().consumeItem(40308, 200000); // ����ʫ��ͷ�������200,000��ID?40308��ڪ��
        L1SkillUse aa = new L1SkillUse(); // �������� L1SkillUse ����
        aa.handleCommands(pc, L1SkillId.HUNTER_BLESS, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF); // ���� "HUNTER_BLESS" ����
        Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 9009)); // ������������
        pc.sendPackets(new S_SkillSound(pc.getId(), 9009)); // ����ʫۡ����������
        htmlid = ""; // ����HTML ID?��
        } else {
            pc.sendPackets(new S_SystemMessage("��������...��������������������...?���������롣")); // ����ʫۡ��ͧ����ӣ������������
        htmlid = ""; // ����HTML ID?��
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
                } else if (s.equalsIgnoreCase("D")) {// �߶� ����
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
                } else if (s.equalsIgnoreCase("J")) {// ��ũ ��
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
                } else if (s.equalsIgnoreCase("V")) {// ���������
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
        // ��Ű �������ڷ���Ʈ
// ��������֣����
 } else if (npcid == 70798) { // ����NPC��ID��70798
     if (s.equalsIgnoreCase("a")) { // ������
         if (pc.getLevel() >= 1 && pc.getLevel() <= 45) { // ������ʫ�����1��45����
             pc.start_teleport(32684, 32851, 2005, pc.getHeading(), 18339, true, false); // ������ʫ��������
         } else {
             pc.sendPackets(
                     new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fQ����: \f3[Lv.45]\fQ��������������")); // ����ʫۡ����ӣ�����������
         }
      } else if (s.equalsIgnoreCase("b")) { // ��յ?
         pc.start_teleport(33436, 32799, 4, pc.getHeading(), 18339, true, false); // ������ʫ����յ?
      } else if (s.equalsIgnoreCase("c")) { // ��������
         if (pc.getLevel() >= 10 && pc.getLevel() <= 29) { // ������ʫ�����10��29����
             pc.start_teleport(33184, 33449, 4, pc.getHeading(), 18339, true, false); // ������ʫ����������
         } else {
             pc.sendPackets(new S_ChatPacket(pc, "������������ʦ������ 10 ~ 29")); // ����ʫۡ����ӣ�����������
         }
      } else if (s.equalsIgnoreCase("d")) { // ?������
         if (pc.getLevel() >= 10 && pc.getLevel() <= 29) { // ������ʫ�����10��29����
             pc.start_teleport(33066, 33218, 4, pc.getHeading(), 18339, true, false); // ������ʫ��?������
         } else {
             pc.sendPackets(new S_ChatPacket(pc, "������������ʦ������ 10 ~ 29")); // ����ʫۡ����ӣ�����������
         }
     }

} else if (s.equalsIgnoreCase("f")) { // ��֣���

    if (pc.getLevel() >= 10 && pc.getLevel() < 20) {
        pc.start_teleport(32801, 32806, 25, pc.getHeading(), 18339, true, false);

    } else if (pc.getLevel() >= 20 && pc.getLevel() < 30) {
        pc.start_teleport(32806, 32746, 26, pc.getHeading(), 18339, true, false);

    } else if (pc.getLevel() >= 30 && pc.getLevel() < 40) {
        pc.start_teleport(32808, 32766, 27, pc.getHeading(), 18339, true, false);

    } else if (pc.getLevel() >= 40 && pc.getLevel() < 44) {
        pc.start_teleport(32796, 32799, 28, pc.getHeading(), 18339, true, false);

    } else {
        pc.sendPackets(new S_ChatPacket(pc, "��֣���ʦ�������� 10 ~ 44"));

    }

} else if (s.equalsIgnoreCase("e")) { // ������� ������ Lv 45~51

    if (pc.getLevel() >= 45 && pc.getLevel() <= 51) {
        pc.start_teleport(32807, 32789, 2010, pc.getHeading(), 18339, true, false);

    } else {
        pc.sendPackets(new S_ChatPacket(pc, "������֣���ʦ�������� 45 ~ 51"));

    }

}

} else if (npcid == 50078) {

    if (pc.getLevel() <= 99) {
        pc.sendPackets(new S_SystemMessage("��ʦ����������ꮣ�PC����Φ����������ѡ�"));
        return htmlid;

    }

} else if (npcid == 7310174) { // ����NPC��ID��7310174

    if (s.equalsIgnoreCase("a")) { // ���� "a" �ݬ��
        if (pc.getInventory().checkItem(3000211, 300)) { // ��?��ʫ��ͷ����������300��ID?3000211��ڪ��
            pc.getInventory().consumeItem(3000211, 300); // ����ʫ��ͷ�������300��ID?3000211��ڪ��
        pc.getInventory().storeItem(3000210, 1); // ������ʫ1��ID?3000210��ڪ��
        pc.sendPackets(new S_SystemMessage("����?���������Ρ�")); // ����ʫۡ��������ͧ�����
        } else {
            pc.sendPackets(new S_SystemMessage("��� [300] ������������..")); // ����ʫۡ��ͧ����ӣ�����������ڪ��
        }

    } else if (s.equalsIgnoreCase("b")) { // ���� "b" �ݬ��
        if (pc.getInventory().checkItem(3000211, 500)) { // ��?��ʫ��ͷ����������500��ID?3000211��ڪ��
            pc.getInventory().consumeItem(3000211, 500); // ����ʫ��ͷ�������500��ID?3000211��ڪ��
        pc.getInventory().storeItem(3000210, 2); // ������ʫ2��ID?3000210��ڪ��
        pc.sendPackets(new S_SystemMessage("����?���������Ρ�")); // ����ʫۡ��������ͧ�����
        } else {
            pc.sendPackets(new S_SystemMessage("��� [500] ������������..")); // ����ʫۡ��ͧ����ӣ�����������ڪ��
        }

    } else if (s.equalsIgnoreCase("c")) { // ���� "c" �ݬ��
        if (pc.getInventory().checkItem(3000211, 1000)) { // ��?��ʫ��ͷ����������1000��ID?3000211��ڪ��
            pc.getInventory().consumeItem(3000211, 1000); // ����ʫ��ͷ�������1000��ID?3000211��ڪ��
        pc.getInventory().storeItem(3000210, 5); // ������ʫ5��ID?3000210��ڪ��
        pc.sendPackets(new S_SystemMessage("����?���������Ρ�")); // ����ʫۡ��������ͧ�����
        } else {
            pc.sendPackets(new S_SystemMessage("��� [1000] ������������..")); // ����ʫۡ��ͧ����ӣ�����������ڪ��
        }

    }


    // �ٹ�Ʈ ���۸�����
        // �������������

} else if (npcid == 70690) { // ����NPC��ID��70690

    if (s.equalsIgnoreCase("a")) { // ���� "a" �ݬ��
        if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40053, 10)
        && pc.getInventory().checkItem(40393, 5)) { // ��?��ʫ��ͷ����������?�����
            pc.getInventory().consumeItem(410061, 50); // ���50��ID?410061��ڪ��
        pc.getInventory().consumeItem(40053, 10); // ���10��ID?40053��ڪ��
        pc.getInventory().consumeItem(40393, 5); // ���5��ID?40393��ڪ��
        pc.getInventory().storeItem(222307, 1); // ������ʫ1��ID?222307��ڪ��������������
        htmlid = ""; // ?��HTML ID
        } else { // �����������
            pc.sendPackets(new S_SystemMessage("���Գ�����롣")); // ����ʫۡ��ͧ����ӣ�����������
        pc.sendPackets(new S_SystemMessage("��驣�تڪ��Ѩ��(50)�����������(10)����ף����(5)")); // ����ʫۡ�������������ϴ
        }

    } else if (s.equalsIgnoreCase("b")) { // ���� "b" �ݬ��
        if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40052, 10)
        && pc.getInventory().checkItem(40396, 5)) { // ��?��ʫ��ͷ����������?�����
            pc.getInventory().consumeItem(410061, 50); // ���50��ID?410061��ڪ��
        pc.getInventory().consumeItem(40052, 10); // ���10��ID?40052��ڪ��
        pc.getInventory().consumeItem(40396, 5); // ���5��ID?40396��ڪ��
        pc.getInventory().storeItem(22359, 1); // ������ʫ1��ID?22359��ڪ�������������
        htmlid = ""; // ?��HTML ID
        } else { // �����������
            pc.sendPackets(new S_SystemMessage("���Գ�����롣")); // ����ʫۡ��ͧ����ӣ�����������
        pc.sendPackets(new S_SystemMessage("��驣�تڪ��Ѩ��(50)���������(10)���ף����(5)")); // ����ʫۡ�������������ϴ
        }

    }

} else if (s.equalsIgnoreCase("b")) { // ���� "b" �ݬ��

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40055, 10)
        && pc.getInventory().checkItem(40394, 5)) { // ��?��ʫ��ͷ����������?�����
        pc.getInventory().consumeItem(410061, 50); // ���50��ID?410061��ڪ��
        pc.getInventory().consumeItem(40055, 10); // ���10��ID?40055��ڪ��
        pc.getInventory().consumeItem(40394, 5); // ���5��ID?40394��ڪ��
        pc.getInventory().storeItem(222308, 1); // ������ʫ1��ID?222308��ڪ��������������
        htmlid = ""; // ?��HTML ID

    } else { // �����������
        pc.sendPackets(new S_SystemMessage("���Գ�����롣")); // ����ʫۡ��ͧ����ӣ�����������
        pc.sendPackets(new S_SystemMessage("��驣�تڪ��Ѩ��(50)����������(10)����ף����(5)")); // ����ʫۡ�������������ϴ

    }

} else if (s.equalsIgnoreCase("d")) { // ���� "d" �ݬ��

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40054, 10)
        && pc.getInventory().checkItem(40395, 5)) { // ��?��ʫ��ͷ����������?�����
        pc.getInventory().consumeItem(410061, 50); // ���50��ID?410061��ڪ��
        pc.getInventory().consumeItem(40054, 10); // ���10��ID?40054��ڪ��
        pc.getInventory().consumeItem(40395, 5); // ���5��ID?40395��ڪ��
        pc.getInventory().storeItem(222309, 1); // ������ʫ1��ID?222309��ڪ�������������
        htmlid = ""; // ?��HTML ID

    } else { // �����������
        pc.sendPackets(new S_SystemMessage("���Գ�����롣")); // ����ʫۡ��ͧ����ӣ�����������
        pc.sendPackets(new S_SystemMessage("��驣�تڪ��Ѩ��(50)�����������(10)���ף����(5)")); // ����ʫۡ�������������ϴ

    }

}

} else if (s.equalsIgnoreCase("e")) { // ���� "e" �ݬ��

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560030)) { // ��?��ʫ��ͷ����������?�����
        pc.getInventory().consumeItem(410061, 50); // ���50��ID?410061��ڪ��
        pc.getInventory().consumeItem(560030, 1); // ���1��ID?560030��ڪ��
        pc.getInventory().storeItem(222307, 1); // ������ʫ1��ID?222307��ڪ��������������
        htmlid = ""; // ?��HTML ID

    } else { // �����������
        pc.sendPackets(new S_SystemMessage("���Գ�����롣")); // ����ʫۡ��ͧ����ӣ�����������
        pc.sendPackets(new S_SystemMessage("��驣�تڪ��Ѩ��(50)�����������������(1)")); // ����ʫۡ�������������ϴ

    }

} else if (s.equalsIgnoreCase("f")) { // ���� "f" �ݬ��

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560033)) { // ��?��ʫ��ͷ����������?�����
        pc.getInventory().consumeItem(410061, 50); // ���50��ID?410061��ڪ��
        pc.getInventory().consumeItem(560033, 1); // ���1��ID?560033��ڪ��
        pc.getInventory().storeItem(22359, 1); // ������ʫ1��ID?22359��ڪ�������������
        htmlid = ""; // ?��HTML ID

    } else { // �����������
        pc.sendPackets(new S_SystemMessage("���Գ�����롣")); // ����ʫۡ��ͧ����ӣ�����������
        pc.sendPackets(new S_SystemMessage("��驣�تڪ��Ѩ��(50)����������������(1)")); // ����ʫۡ�������������ϴ

    }

}

} else if (s.equalsIgnoreCase("g")) { // ���� "g" �ݬ��

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560032)) { // ��?��ʫ��ͷ����������?�����
        pc.getInventory().consumeItem(410061, 50); // ���50��ID?410061��ڪ��
        pc.getInventory().consumeItem(560032, 1); // ���1��ID?560032��ڪ��
        pc.getInventory().storeItem(222308, 1); // ������ʫ1��ID?222308��ڪ��������������
        htmlid = ""; // ?��HTML ID

    } else { // �����������
        pc.sendPackets(new S_SystemMessage("���Գ�����롣")); // ����ʫۡ��ͧ����ӣ�����������
        pc.sendPackets(new S_SystemMessage("��驣�تڪ��Ѩ��(50)�����������������(1)")); // ����ʫۡ�������������ϴ

    }

} else if (s.equalsIgnoreCase("h")) { // ���� "h" �ݬ��

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560031)) { // ��?��ʫ��ͷ����������?�����
        pc.getInventory().consumeItem(410061, 50); // ���50��ID?410061��ڪ��
        pc.getInventory().consumeItem(560031, 1); // ���1��ID?560031��ڪ��
        pc.getInventory().storeItem(222309, 1); // ������ʫ1��ID?222309��ڪ�������������
        htmlid = ""; // ?��HTML ID

    } else { // �����������
        pc.sendPackets(new S_SystemMessage("���Գ�����롣")); // ����ʫۡ��ͧ����ӣ�����������
        pc.sendPackets(new S_SystemMessage("��驣�تڪ��Ѩ��(50)����������������(1)")); // ����ʫۡ�������������ϴ

    }

}

// ���� ����� Ȯ�� ���� ����
        // �������������������

} else if (npcid == 7310149) { // ����NPC��ID��7310149

    if (s.equalsIgnoreCase("request memory crystal")) { // �����ݬ�� "request memory crystal"
        if (pc.getInventory().checkItem(3000200, 1) && pc.getInventory().checkItem(40308, 20000)) { // ��?��ʫ��ͷ����������?�����
            pc.getInventory().consumeItem(3000200, 1); // ���1��ID?3000200��ڪ��
        pc.getInventory().consumeItem(40308, 20000); // ���20,000��ID?40308��ڪ��
        pc.getInventory().storeItem(700022, 1); // ������ʫ1��ID?700022��ڪ��������������ܣ�
        pc.sendPackets(new S_SystemMessage("���������")); // ����ʫۡ��ͧ����ӣ�����������
        htmlid = ""; // ?��HTML ID
        } else { // �����������
            pc.sendPackets(new S_SystemMessage("����?���ӡ�֣�20,000��������������1������")); // ����ʫۡ��ͧ����ӣ�����������
        }

    }
// } else if (npcid == 50045) { //���� ����Ʈ ������
// ����NPC��ID��50045������������棩
//        if (s.equalsIgnoreCase("a")) { // ����
// �����ݬ�� "a"��������
//        if (pc.getInventory().checkItem(810000, 1)) { // ��?��ʫ��ͷ��������1��ID?810000��ڪ��
//       pc.getInventory().consumeItem(810000, 1); // ���1��ID?810000��ڪ��
//        pc.start_teleport(32800, 32798, 1935, pc.getHeading(), 18339, true, false); // ������ʫ������������32800, 32798, 1935��
//        } else { // �����������
//        pc.sendPackets(new S_SystemMessage("�������Լ���������?㵡�")); // ����ʫۡ��ͧ����ӣ�������?�
//       }
//        } else if (s.equalsIgnoreCase("b")) { // �������
// �����ݬ�� "b"����������յ����
        pc.start_teleport(33436, 32799, 4, pc.getHeading(), 18339, true, false); // ������ʫ����յ����33436, 32799, 4��
        }
            }else if (npcid == 21015) { //����� ���� ���� �Զ��  Team The Day by.���
                if (s.equalsIgnoreCase("a")) {//����� ���� 1�� ����
                    if (pc.getLevel() >= 80 & pc.getLevel() <= 84) {
                        if (pc.getInventory().checkItem(4200255,1) && pc.getInventory().checkItem(40308, 15000)) {//����� ���� �����(3�ð�)
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4200256,1) && pc.getInventory().checkItem(40308, 15000)) {//����� ���� �����(30��)
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4100121,1) && pc.getInventory().checkItem(40308, 15000)) {//��� �Ҹ��� ��ȣ
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        } else {
                            htmlid = "hanggelf";
                        }
                    }
                } else if (s.equalsIgnoreCase("b")) { // ����� ���� 2�� ����
                    if (pc.getLevel() >= 80 & pc.getLevel() <= 92) {
                        if (pc.getInventory().checkItem(4200255,1) && pc.getInventory().checkItem(40308, 15000)) {//����� ���� �����(3�ð�)
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32769, 32759, 7532, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4200256,1) && pc.getInventory().checkItem(40308, 15000)) {//����� ���� �����(30��)
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32769, 32759, 7532, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4100121,1) && pc.getInventory().checkItem(40308, 15000)) {//��� �Ҹ��� ��ȣ
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        } else {
                            htmlid = "hanggelf";
                        }
                    }
                } else if (s.equalsIgnoreCase("c")) { // ����� ���� 3�� ����
                    if (pc.getLevel() >= 80 & pc.getLevel() <= 92) {
                        if (pc.getInventory().checkItem(4200255,1) && pc.getInventory().checkItem(40308, 15000)) {//����� ���� �����(3�ð�)
                            pc.getInventory().consumeItem(40308, 20000);
                            pc.start_teleport(32791, 32857, 7533, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4200256,1) && pc.getInventory().checkItem(40308, 20000)) {//����� ���� �����(30��)
                            pc.getInventory().consumeItem(40308, 20000);
                            pc.start_teleport(32791, 32857, 7533, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4100121,1) && pc.getInventory().checkItem(40308, 15000)) {//��� �Ҹ��� ��ȣ
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        } else {
                            htmlid = "hanggelf";
                        }
                    }
                } else if (s.equalsIgnoreCase("d")) { // ����� ���� 4�� ����
                    if (pc.getLevel() >= 80 & pc.getLevel() <= 92) {
                        if (pc.getInventory().checkItem(4200255,1) && pc.getInventory().checkItem(40308, 15000)) {//����� ���� �����(3�ð�)
                            pc.getInventory().consumeItem(40308, 20000);
                            pc.start_teleport(32860, 32760, 7534, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4200256,1) && pc.getInventory().checkItem(40308, 20000)) {//����� ���� �����(30��)
                            pc.getInventory().consumeItem(40308, 20000);
                            pc.start_teleport(32860, 32760, 7534, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4100121,1) && pc.getInventory().checkItem(40308, 15000)) {//��� �Ҹ��� ��ȣ
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        } else {
                            htmlid = "hanggelf";
                        }
                    }
                }
            } else if (npcid == 7200000) { // ���� ��Ų��
                if (s.equalsIgnoreCase("d")) {
                    if (pc.getInventory().checkItem(3000215, 1) && pc.getInventory().checkItem(1000004, 1)) {
                        pc.getInventory().consumeItem(3000215, 1);
                        pc.getInventory().consumeItem(1000004, 1);
                        pc.getInventory().storeItem(810010, 8);
                        addQuestExp(pc, 4);
                    } else {
                        htmlid = "ekins5";
                    }
                } else if (s.equalsIgnoreCase("c")) { // �˸�: �������� ���� ¡ǥ
                    if (pc.getInventory().checkItem(3000215, 1)) {
                        pc.getInventory().consumeItem(3000215, 1);
                        pc.getInventory().storeItem(810010, 5);
                        addQuestExp(pc, 4);
                    } else {
                        htmlid = "ekins5";
                    }
                } else if (s.equalsIgnoreCase("b")) { // �˸�: ������ ����
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
                } else if (s.equalsIgnoreCase("a")) { // �˸�: ������ ���� ����
                    if (pc.getInventory().checkItem(810001, 1)) {
                        pc.getInventory().consumeItem(810001, 1);
                        pc.getInventory().storeItem(810010, 1);
                        addQuestExp(pc, 4);
                    } else {
                        htmlid = "ekins5";
                    }
                }
        /** �Ϲ�Ʈ **/
        /** ������ **/
 } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70641) { // ����NPC��ID��70641
     if (s.equalsIgnoreCase("a")) { // �����ݬ�� "a"
         if (pc.getInventory().checkItem(40395, 1) // ������
        // �ף����
        && pc.getInventory().checkItem(410061, 10) // �����Ǳ��

        // تڪ��Ѩ��
        && pc.getInventory().checkItem(820004, 300)) { // �����ǽ�Ÿ��
             // ت��������

        pc.getInventory().consumeItem(40395, 1); // ���1��ID?40395��ڪ��
        pc.getInventory().consumeItem(410061, 10); // ���10��ID?410061��ڪ��
        pc.getInventory().consumeItem(820004, 300); // ���300��ID?820004��ڪ��
        pc.getInventory().storeItem(20273, 1); // ������ʫ1��ID?20273��ڪ����ت������ߣ�
        htmlid = ""; // ?��HTML ID
         } else { // �����������
             pc.sendPackets(new S_SystemMessage("���Գ�����롣")); // ����ʫۡ��ͧ����ӣ�����������
        pc.sendPackets(new S_SystemMessage("��驣��ף����(1)")); // ����ʫۡ�������������ϴ
        pc.sendPackets(new S_SystemMessage("��驣�تڪ��Ѩ��(10)")); // ����ʫۡ�������������ϴ
        pc.sendPackets(new S_SystemMessage("��驣�ت��������(300)")); // ����ʫۡ�������������ϴ
         }
        } else if (s.equalsIgnoreCase("b")) { // �����ݬ�� "b"
        if (pc.getInventory().checkEnchantItem(20273, 7, 1) // +7 �������尩
        // +7 ت�������
        && pc.getInventory().checkItem(40395, 1) // ������
        // �ף����
        && pc.getInventory().checkItem(410061, 10) // �����Ǳ��
        // تڪ��Ѩ��
        && pc.getInventory().checkItem(820004, 300) // �����ǽ�Ÿ��
        // ت��������
        && pc.getInventory().checkItem(820005, 1)) { // ��������
        // ت��������

        pc.getInventory().consumeEnchantItem(20273, 7, 1); // ���1��+7��ID?20273��ڪ��
        pc.getInventory().consumeItem(40395, 1); // ���1��ID?40395��ڪ��
        pc.getInventory().consumeItem(410061, 10); // ���10��ID?410061��ڪ��
        pc.getInventory().consumeItem(820004, 300); // ���300��ID?820004��ڪ��
        pc.getInventory().consumeItem(820005, 1); // ���1��ID?820005��ڪ��
        pc.getInventory().storeItem(20274, 1); // ������ʫ1��ID?20274��ڪ����������ت������ߣ�
        htmlid = ""; // ?��HTML ID
        } else { // �����������
            pc.sendPackets(new S_SystemMessage("���Գ�����롣")); // ����ʫۡ��ͧ����ӣ�����������
        pc.sendPackets(new S_SystemMessage("��驣��ף����(1)")); // ����ʫۡ�������������ϴ
        pc.sendPackets(new S_SystemMessage("��驣�تڪ��Ѩ��(10)")); // ����ʫۡ�������������ϴ
        pc.sendPackets(new S_SystemMessage("��驣�ت��������(300)")); // ����ʫۡ�������������ϴ
        pc.sendPackets(new S_SystemMessage("��驣�ت��������(1)")); // ����ʫۡ�������������ϴ
        pc.sendPackets(new S_SystemMessage("��驣�+7 ت�������(1)")); // ����ʫۡ�������������ϴ
        }
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71180) { // ������
                if (s.equalsIgnoreCase("A")) { // �޲ٴ� ������
                    if (pc.getInventory().consumeItem(49026, 1000)) {
                        pc.getInventory().storeItem(41093, 1);
                        htmlid = "jp6";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("B")) { // ���
                    if (pc.getInventory().consumeItem(49026, 5000)) {
                        pc.getInventory().storeItem(41094, 1);
                        htmlid = "jp6";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("C")) { // �巹��
                    if (pc.getInventory().consumeItem(49026, 10000)) {
                        pc.getInventory().storeItem(41095, 1);
                        htmlid = "jp6";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("D")) { // ����
                    if (pc.getInventory().consumeItem(49026, 100000)) {
                        pc.getInventory().storeItem(41096, 1);
                        htmlid = "jp6";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("E")) { // ������
                    if (pc.getInventory().consumeItem(49026, 1000)) {
                        pc.getInventory().storeItem(41098, 1);
                        htmlid = "jp8";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("F")) { // ���õ� ����
                    if (pc.getInventory().consumeItem(49026, 5000)) {
                        pc.getInventory().storeItem(41099, 1);
                        htmlid = "jp8";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("G")) { // �ְ�� ����
                    if (pc.getInventory().consumeItem(49026, 10000)) {
                        pc.getInventory().storeItem(41100, 1);
                        htmlid = "jp8";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("H")) { // �� �� ���� ����
                    if (pc.getInventory().consumeItem(49026, 100000)) {
                        pc.getInventory().storeItem(41101, 1);
                        htmlid = "jp8";
                    } else {
                        htmlid = "jp5";
                    }
                }

                // ������ �Ұ� ������
            } else if (npcid == 5066) {
                int enchant = 0;
                int itemId = 0;
                int oldArmor = 0;
                L1NpcInstance npc = (L1NpcInstance) obj;
                String npcName = npc.getNpcTemplate().get_name();
                if (s.equalsIgnoreCase("1")) { // [+7]������ �ܰ�
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
                        pc.getInventory().consumeItem(40308, 5000000); // ���500ؿ��ID?40308��ڪ�����ӡ�֣�
        ۡۯ?��ڪ��(pc, 602, 1, 7); // ������ʫ1��ID?602��ڪ��?����?���+7
        htmlid = ""; // ?��HTML ID
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ�����롣")); // ����ʫۡ��ͧ����ӣ�����������
                    }
                } else if (s.equalsIgnoreCase("2")) {// [+8]������ �ܰ�
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
                        ۡۯ?��ڪ��(pc, 602, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }
                } else if (s.equalsIgnoreCase("3")) {// [+7]ȯ���� ü�μҵ�
                    if ((pc.getInventory().checkEnchantItem(500, 8, 1) || pc.getInventory().checkEnchantItem(501, 8, 1))
                            && pc.getInventory().checkItem(40308, 5000000)) {
                        if (pc.getInventory().consumeEnchantItem(500, 8, 1)
                                || pc.getInventory().consumeEnchantItem(501, 8, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(40308, 5000000);
                        ۡۯ?��ڪ��(pc, 202001, 1, 7);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }
                } else if (s.equalsIgnoreCase("4")) {// [+8]ȯ���� ü�μҵ�
                    if ((pc.getInventory().checkEnchantItem(500, 9, 1) || pc.getInventory().checkEnchantItem(501, 9, 1))
                            && pc.getInventory().checkItem(40308, 10000000)) {
                        if (pc.getInventory().consumeEnchantItem(500, 9, 1)
                                || pc.getInventory().consumeEnchantItem(501, 9, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(40308, 10000000);
                        ۡۯ?��ڪ��(pc, 202001, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }
                } else if (s.equalsIgnoreCase("5")) {// [+7]������ Ű��ũ
                    if ((pc.getInventory().checkEnchantItem(503, 8, 1) || pc.getInventory().checkEnchantItem(504, 8, 1))
                            && pc.getInventory().checkItem(40308, 5000000)) {
                        if (pc.getInventory().consumeEnchantItem(503, 8, 1)
                                || pc.getInventory().consumeEnchantItem(504, 8, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(40308, 5000000);
                        ۡۯ?��ڪ��(pc, 1135, 1, 7);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }
                } else if (s.equalsIgnoreCase("6")) {// [+8]������ Ű��ũ
                    if ((pc.getInventory().checkEnchantItem(503, 9, 1) || pc.getInventory().checkEnchantItem(504, 9, 1))
                            && pc.getInventory().checkItem(40308, 10000000)) {
                        if (pc.getInventory().consumeEnchantItem(503, 9, 1)
                                || pc.getInventory().consumeEnchantItem(504, 9, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(40308, 10000000);
                        ۡۯ?��ڪ��(pc, 1135, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }
                } else if (s.equalsIgnoreCase("7")) {// [+7]�ı��� ũ�ο�
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
                        ۡۯ?��ڪ��(pc, 1124, 1, 7);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }
                } else if (s.equalsIgnoreCase("8")) {// [+8]�ı��� ũ�ο�
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
                        ۡۯ?��ڪ��(pc, 1124, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }
                } else if (s.equalsIgnoreCase("9")) {// [+7]�ı��� �̵���
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
                        ۡۯ?��ڪ��(pc, 1125, 1, 7);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }
                } else if (s.equalsIgnoreCase("10")) {// [+8]�ı��� �̵���
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
                        ۡۯ?��ڪ��(pc, 1125, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }
                } else if (s.equalsIgnoreCase("11")) {// [+0]���ν��� ������
                    if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 9, 1)
                            && pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
                        pc.getInventory().consumeEnchantItem(119, 5, 1);
                        pc.getInventory().consumeEnchantItem(121, 9, 1);
                        pc.getInventory().consumeItem(700077, 1);
                        pc.getInventory().consumeItem(41246, 100000);
                        pc.getInventory().storeItem(202003, 1);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }

                } else if (s.equalsIgnoreCase("12")) {// [+8]���ν��� ������
                    if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 10, 1)
                            && pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
                        pc.getInventory().consumeEnchantItem(119, 5, 1);
                        pc.getInventory().consumeEnchantItem(121, 10, 1);
                        pc.getInventory().consumeItem(700077, 1);
                        pc.getInventory().consumeItem(41246, 100000);
                        ۡۯ?��ڪ��(pc, 202003, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }
                } else if (s.equalsIgnoreCase("13")) {// [+9]���ν��� ������
                    if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 11, 1)
                            && pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
                        pc.getInventory().consumeEnchantItem(119, 5, 1);
                        pc.getInventory().consumeEnchantItem(121, 11, 1);
                        pc.getInventory().consumeItem(700077, 1);
                        pc.getInventory().consumeItem(41246, 100000);
                        ۡۯ?��ڪ��(pc, 202003, 1, 9);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }

                } else if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") // �Ǳ�
                        || s.equals("E") || s.equals("F") || s.equals("G") || s.equals("H") // ���
                        || s.equals("I") || s.equals("J") || s.equals("K") || s.equals("L") // ����
                        || s.equals("M") || s.equals("N") || s.equals("O") || s.equals("P")) { // �κ�
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
                        pc.getInventory().consumeItem(41246, 100000); // ������
                        pc.getInventory().consumeItem(oldArmor, 1); // �����
                        createNewItem(pc, npcName, itemId, 1, enchant - 7);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("���Գ������."));
                    }
                } else if (s.equals("a")) {// []��ǳ�ǵ���
                    if ((pc.getInventory().checkEnchantItem(605, 8, 1)) && pc.getInventory().checkItem(41246, 100000)) {
                        if (pc.getInventory().consumeEnchantItem(605, 8, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 100000);
                        ۡۯ?��ڪ��(pc, 203015, 1, 0);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("��� +8 ������ݨ�� 100,000 ��̿����."));
                    }
                } else if (s.equals("b")) {// [+8]��ǳ�ǵ���
                    if ((pc.getInventory().checkEnchantItem(605, 9, 1)) && pc.getInventory().checkItem(41246, 100000)) {
                        if (pc.getInventory().consumeEnchantItem(605, 9, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 100000);
                        ۡۯ?��ڪ��(pc, 203015, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("��� +9 ������ݨ�� 100,000 ��̿����."));
                    }
                } else if (s.equals("c")) {// [+9]��ǳ�ǵ���
                    if ((pc.getInventory().checkEnchantItem(605, 10, 1))
                            && pc.getInventory().checkItem(41246, 100000)) {
                        if (pc.getInventory().consumeEnchantItem(605, 10, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 100000);
                        ۡۯ?��ڪ��(pc, 203015, 1, 9);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("��� +10 ������ݨ�� 100,000 ��̿����."));
                    }
                } else if (s.equals("d")) {// []�����ǵ���
                    if ((pc.getInventory().checkEnchantItem(151, 0, 1)) && pc.getInventory().checkItem(41246, 200000)) {
                        if (pc.getInventory().consumeEnchantItem(151, 0, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 200000);
                        ۡۯ?��ڪ��(pc, 203016, 1, 0);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("+0 ��ت��ݨ����� 100,000 ��̿������"));
                    }
                } else if (s.equals("e")) { // [+1] تڪ��ݨ
                    if ((pc.getInventory().checkEnchantItem(151, 3, 1)) && pc.getInventory().checkItem(41246, 200000)) {
                        if (pc.getInventory().consumeEnchantItem(151, 3, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 200000);
                        ۡۯ?��ڪ��(pc, 203016, 1, 1);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("+3 ��ت��ݨ����� 100,000 ��̿������"));
                    }
                } else if (s.equals("f")) { // [+3] تڪ��ݨ
                    if ((pc.getInventory().checkEnchantItem(151, 5, 1)) && pc.getInventory().checkItem(41246, 200000)) {
                        if (pc.getInventory().consumeEnchantItem(151, 5, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 200000);
                        ۡۯ?��ڪ��(pc, 203016, 1, 3);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("+5 ��ت��ݨ����� 100,000 ��̿������"));
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
                if (s.equals("a")) {// �Ϲݺ���
                    if (pc.getLevel() >= 52) {
                        if (pc.getInventory().checkItem(30151, 1)) {
                            pc.getInventory().consumeItem(30151, 1);
                            pc.getInventory().storeItem(30149, 1);
                            ���� 52 ?������?������?(pc, 1);
                            htmlid = "anold3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("���Գ������."));
                            htmlid = "anold4";
                        }
                    } else {
                        htmlid = "anold2";
                    }
                } else if (s.equals("b")) {// Ư���Ѻ���
                    if (pc.getLevel() >= 52) {
                        if (pc.getInventory().checkItem(30151, 1) && pc.getInventory().checkItem(1000004, 1)) {
                            pc.getInventory().consumeItem(30151, 1);
                            pc.getInventory().consumeItem(1000004, 1);
                            pc.getInventory().storeItem(30149, 1);
                            ���� 52 ?������?������?(pc, 2);
                            htmlid = "anold3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("���Գ������."));
                            htmlid = "anold4";
                        }
                    } else {
                        htmlid = "anold2";
                    }
                } else if (s.equals("c")) {// ������ Ư���Ѻ���
                    if (pc.getLevel() >= 52) {
                        if (pc.getInventory().checkItem(30151, 1) && pc.getInventory().checkItem(1000007, 1)) {
                            pc.getInventory().consumeItem(30151, 1);
                            pc.getInventory().consumeItem(1000007, 1);
                            pc.getInventory().storeItem(30149, 1);
                            ���� 52 ?������?������?(pc, 3);
                            htmlid = "anold3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("���Գ������."));
                            htmlid = "anold4";
                        }
                    } else {
                        htmlid = "anold2";
                    }
                }
                // ������
            } else if (npcid == 9) {
                if (s.equals("a")) {// �Ϲݺ���
                    if (pc.getLevel() >= 30) {
                        if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)) {
                            pc.getInventory().consumeItem(9992, 5);
                            pc.getInventory().consumeItem(9993, 1);
                            pc.getInventory().storeItem(9994, 1);
                            ���� 52 ?������?������?(pc, 1);
                            htmlid = "naruto3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("���Գ������."));
                            htmlid = "naruto4";
                        }
                    } else {
                        htmlid = "naruto2";
                    }
                } else if (s.equals("b")) {// Ư���Ѻ���
                    if (pc.getLevel() >= 30) {
                        if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)
                                && pc.getInventory().checkItem(1000004, 1)) {
                            pc.getInventory().consumeItem(9992, 5);
                            pc.getInventory().consumeItem(9993, 1);
                            pc.getInventory().consumeItem(1000004, 1);
                            pc.getInventory().storeItem(9994, 1);
                            ���� 52 ?������?������?(pc, 1);
                            htmlid = "naruto3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("���Գ������."));
                            htmlid = "naruto4";
                        }
                    } else {
                        htmlid = "naruto2";
                    }
                } else if (s.equals("c")) {// ������ Ư���Ѻ���
                    if (pc.getLevel() >= 30) {
                        if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)
                                && pc.getInventory().checkItem(1000007, 1)) {
                            pc.getInventory().consumeItem(9992, 5);
                            pc.getInventory().consumeItem(9993, 1);
                            pc.getInventory().consumeItem(1000007, 1);
                            pc.getInventory().storeItem(9994, 1);
                            ���� 52 ?������?������?(pc, 1);
                            htmlid = "naruto3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("���Գ������."));
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
                // �˵��
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

        /** ������ **/
/** ���Ѧ **/

        } else if (npcid == 7000082 || npcid == 7000083 || npcid == 7000084 || npcid == 7000085 || npcid == 7000086
        || npcid == 7000087) {
        if (s.equalsIgnoreCase("0-5") // �ܼ��� �������� �߻�!
        // ������ڦ۰��ۡ��!
        || s.equalsIgnoreCase("0-6") // ������ �������� �߻�!
        // ��Ү��ڦ۰��ۡ��!
        || s.equalsIgnoreCase("0-7") // ��ȣž �������� �߻�!
        // ��������۰��ۡ��!
        || s.equalsIgnoreCase("1-16") // �ܼ��� �������� ħ����ź �߻�!
        // ������ڦ۰��ۡ��?��?��!
        || s.equalsIgnoreCase("1-17") // ������ �������� ħ����ź �߻�!
        // ��Ү��ڦ��۰ۡ��?��?��!
        || s.equalsIgnoreCase("1-18") // ������ �������� ħ����ź �߻�!
        // ��Ү��ڦ���ۡ��?��?��!
        || s.equalsIgnoreCase("1-19") // ������ �������� ħ����ź �߻�!
        // ��Ү��ڦ����ۡ��?��?��!
        || s.equalsIgnoreCase("1-20") // ��ȣž �������� ħ����ź �߻�!
        // ��������۰��ۡ��?��?��!
        // ����
        // ����
        || s.equalsIgnoreCase("0-9") // �ܼ��� �������� �߻�!
        // ������ڦ۰��ۡ��!
        ) {
        // ����ʦ���ʥ��������ا������������ۡ������
                            }
                        }
                        ) {
                        int locx = 0;
                        int locy = 0;
                        int gfxid = 0;
                            int castleid = 0;
                            int npcId = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
                            if (s.equalsIgnoreCase("0-5")) { // �ܼ��� �������� �߻�!
                                // ������ڦ۰��ۡ��!
                            switch (npcId) {
                                case 7000086: // 5�� ���� ���� ��ũ��� ������
                    // 5��۰��������к���������
                            locx = 32795;
                            locy = 32315;
                            gfxid = 12197; // ����
                    // ����
                            castleid = 2;
                            break;
                            case 7000082: // 5�� ���� ���� ����� ������
                    // 5��۰��������յ��������
                            locx = 33632;
                            locy = 32731;
                            gfxid = 12197; // ����
                    // ����
                            castleid = 15482;
                            break;
                            case 7000084: // 7�� ���� ���� ��Ʈ�� ������
                    // 7��۰������������������
                            locx = 33114;
                            locy = 32771;
                            gfxid = 12193; // ����
                    // ���
                            castleid = 1;
                            break;
                            }
                    } else if (s.equalsIgnoreCase("0-6")) { // ������ �������� �߻�!
                        switch (npcId) {
                            case 7000086: // 11�� ���� ���� ��ũ��� ������
                                locx = 32798;
                                locy = 32268;
                                gfxid = 12197; // ����
                                castleid = 2;
                                break;
                            case 7000082: // 11�� ���� ���� ����� ������
                                locx = 33632;
                                locy = 32664;
                                gfxid = 12197; // ����
                                castleid = 15482;
                                break;
                            case 7000084: // 2�� ���� ���� ��Ʈ�� ������
                                locx = 33171;
                                locy = 32763;
                                gfxid = 12197; // ����
                                castleid = 1;
                                break;
                        }
                    } else if (s.equalsIgnoreCase("0-7")) { // ��ȣž �������� �߻�!
                        switch (npcId) {
                            case 7000086: // 11�� ���� ���� ��ũ��� ������
                                locx = 32798;
                                locy = 32285;
                                gfxid = 12197; // ����
                                castleid = 2;
                                break;
                            case 7000082: // 11�� ���� ���� ����� ������
                                locx = 33631;
                                locy = 32678;
                                gfxid = 12197; // ����
                                castleid = 15482;
                                break;
                            case 7000084: // 2�� ���� ���� ��Ʈ�� ������
                                locx = 33168;
                                locy = 32779;
                                gfxid = 12197; // ����
                                castleid = 1;
                                break;
                        }
                    } else if (s.equalsIgnoreCase("0-9")) { // �ܼ��� �������� �߻�!
                        int pcCastleId = 0;
                        if (pc.getClanid() != 0) {
                            L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
                            if (clan != null) {
                                pcCastleId = clan.getCastleId();
                            }
                        }
                        switch (npcId) {
                            case 7000087: // 11�� ���� ���� ��ũ��� ������
                                if (isExistDefenseClan(L1CastleLocation.OT_CASTLE_ID)) {
                                    if (pcCastleId != L1CastleLocation.OT_CASTLE_ID) {
                                        pc.sendPackets(new S_ServerMessage(3682));
                                        // ������ ���: ����(���� ��ȣ�ϴ� ���� ���ָ� ��� ����)
                                        return htmlid;
                                    }
                                }
                                locx = 32794;
                                locy = 32320;
                                gfxid = 12193; // ����
                                castleid = 2;
                                break;
                            case 7000083: // 11�� ���� ���� ����� ������
                                if (isExistDefenseClan(L1CastleLocation.GIRAN_CASTLE_ID)) {
                                    if (pcCastleId != L1CastleLocation.GIRAN_CASTLE_ID) {
                                        pc.sendPackets(new S_ServerMessage(3682));
                                        // ������ ���: ����(���� ��ȣ�ϴ� ���� ���ָ� ��� ����)
                                        return htmlid;
                                    }
                                }
                                locx = 33631;
                                locy = 32738;
                                gfxid = 12193; // ����
                                castleid = 15482;
                                break;
                            case 7000085: // 2�� ���� ���� ��Ʈ�� ������
                                if (isExistDefenseClan(L1CastleLocation.KENT_CASTLE_ID)) {
                                    if (pcCastleId != L1CastleLocation.KENT_CASTLE_ID) {
                                        pc.sendPackets(new S_ServerMessage(3682));
                                        // ������ ���: ����(���� ��ȣ�ϴ� ���� ���ָ� ��� ����)
                                        return htmlid;
                                    }
                                }
                                locx = 33107;
                                locy = 32770;
                                gfxid = 12197; // ����
                                castleid = 1;
                                break;
                        }

        // ۡ��?��?������������
/*
* <a action="1-16">����ڦ۰��ۡ��?��?��!</a><br> <a action="1-17">Ү��ڦ��۰ۡ��?��?��!</a><br>
* <a action="1-18">Ү��ڦ���ۡ��?��?��!</a><br> <a action="1-19">Ү��ڦ����ۡ��?��?��!</a><br>
* <a action="1-20">������۰��ۡ��?��?��!</a><br><br>
} else if (s.equalsIgnoreCase("0-9")) {
    // ����ڦ۰��ۡ��?��?��!
*/

        } else {
        pc.sendPackets(new S_SystemMessage("?��?�������������ܡ�"));
        // ۡ��ͧ����ӣ�?��?�������������ܡ�
        return htmlid;
        }

        boolean isNowWar = false;
        isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleid);
        if (!isNowWar) {
        pc.sendPackets(new S_ServerMessage(3683));
        // ۡ����������ӣ����Ѧ��������������������������ģ�
        return htmlid;
        }

        boolean inWar = MJWar.isNowWar(pc.getClan());
        if (!(pc.isCrown() && inWar && isNowWar)) {
        pc.sendPackets(new S_ServerMessage(3681));
        // ۡ����������ӣ����Ѧ��������������������ʦ����ģ�
        return htmlid;
        }

        if (pc.getlastShellUseTime() + 10000L > System.currentTimeMillis()) {
        pc.sendPackets(new S_ServerMessage(3680));
        // ۡ����������ӣ����Ѧ�������������������?���ࣩ
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
                                // ħ����ź(locx, locy); // ħ����ź �׽�Ʈ
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

    private static boolean ������(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr,
                               boolean identi) {
        // ������(pc, 5000045, 1, 5, 128);
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
            } else { // ���� �� ���� ���� ���鿡 ����߸��� ó���� ĵ���� ���� �ʴ´�(���� ����)
                pc.sendPackets(new S_ServerMessage(82));
                // ���� �������� �����ϰų� �κ��丮�� ������ �� �� �� �����ϴ�.
                return false;
            }
            pc.sendPackets(new S_ServerMessage(403, item.getLogName())); //
            return true;
        } else {
            return false;
        }
    }

    private boolean ۡۯ?��ڪ��(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
        L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
        if (item != null) {
            item.setCount(count);
            item.setEnchantLevel(EnchantLevel);
            item.setIdentified(true);
            if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
                pc.getInventory().storeItem(item);
            } else {
                pc.sendPackets(new S_ServerMessage(82));
                // ���� �������� �����ϰų� �κ��丮�� ������ �� �� �� �����ϴ�.
                return false;
            }
            pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0��
            // �տ�
            // �־����ϴ�.
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

    private void ���� 52 ?������?������?(L1PcInstance pc, int type) {
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
            // pc.sendPackets(new S_ChatPacket(pc, time + " �� �� ��� �Ͻñ� �ٶ��ϴ�."));
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

        int level = ExpTable.getLevelByExp(pc.get_exp()); // ��������?������ʫ����
        if (pc.getInventory().checkItem(itemid, 1)) { // ��?��ʫ�����������Գ��
            if (pc.getLevel() >= limitlvmin && pc.getLevel() < limitlvMax) { // ��?��ʫ�����������������Ү
                if (level >= limitlvMax) {
                    pc.sendPackets("�������������?��"); // ۡ����ӣ��������������?��
        return;
                } else {
                    pc.getInventory().consumeItem(itemid, 1); // ����������Գ��
        pc.add_exp(exp); // ��ʥ����?
        pc.setCurrentHp(pc.getMaxHp()); // ����������٤??����?
        pc.setCurrentMp(pc.getMaxMp()); // ��������ت��??����?
        pc.sendPackets(new S_SkillSound(pc.getId(), 3944)); // ۡ��������������ʫ
        Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944)); // ������������
                }
            } else {
                pc.sendPackets(String.format("%d ���߾ %d �����ʦ���ġ�", limitlvmin, limitlvMax)); // ۡ����ӣ������������������Ү����
        return;
            }
        } else {
            pc.sendPackets("���������硣"); // ۡ����ӣ�����������
        return;
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

    private String ������(String s, L1PcInstance pc) {
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
