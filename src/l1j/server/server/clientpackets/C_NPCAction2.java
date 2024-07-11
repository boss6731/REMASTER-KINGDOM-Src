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
            if (npcid == 200201) {// Á¶¿ìÀÇ µ¹°ñ·½
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
                // ±¤Ç³ÀÇ µµ³¢
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
                // ÆÄ¸êÀÇ ´ë°Ë
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
                // ¾ÆÅ©¸ÞÀÌÁöÀÇ ÁöÆÎÀÌ
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
                // È¤ÇÑÀÇ Ã¢
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
                // ³ú½Å°Ë
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
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200018) {// °æÇèÄ¡
                if (s.equalsIgnoreCase("0")) {// ÇÑ¹ø¾¿ Áö±Þ
                    if (pc.getLevel() < 51) {
                        pc.add_exp_for_ready((ExpTable.getExpByLevel(51) - 1) - pc.get_exp() + ((ExpTable.getExpByLevel(51) - 1) / 100L));
                    } else if (pc.getLevel() >= 51 && pc.getLevel() < Config.ServerAdSetting.Expreturn) {
                        pc.add_exp_for_ready((ExpTable.getExpByLevel(pc.getLevel() + 1) - 1) - pc.get_exp() + 100L);
                        pc.setCurrentHp(pc.getMaxHp());
                        pc.setCurrentMp(pc.getMaxMp());
                    }
                } else if (s.equalsIgnoreCase("1")) {// ÇÑ¹ø¿¡ Áö±Þ
                    if (pc.getLevel() >= Config.ServerAdSetting.Expreturn && pc.getLevel() <= Config.ServerAdSetting.Expreturn) {
                        pc.add_exp_for_ready(
                                (ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) - pc.get_exp() + ((ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) / 30000000));
                    } else if (pc.getLevel() <= Config.ServerAdSetting.Expreturn && pc.getLevel() < Config.ServerAdSetting.Expreturn) {
                        pc.add_exp_for_ready(
                                (ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) - pc.get_exp() + ((ExpTable.getExpByLevel(Config.ServerAdSetting.Expreturn) - 1) / 30000000));
                        pc.setCurrentHp(pc.getMaxHp());
                        pc.setCurrentMp(pc.getMaxMp());
                    }
                } else if (s.equalsIgnoreCase("2")) {// ½Å±ÔÁö¿ø
                    if (pc.getLevel() >= Config.ServerAdSetting.NewCha) {
                        pc.sendPackets(new S_SystemMessage("ì×?ÜôãÀãæâ¢£¬ÙíÛöî¢üòÔðò¨êµ¡£"));
                        return htmlid;
                    } else if (pc.getInventory().checkItem(7241, 1) || pc.getInventory().checkItem(1000004, 1)) {
                        pc.sendPackets(new S_SystemMessage("ÙíÛöñìÜÜÛ¡Û¯?Õä¡£"));
                        return htmlid;
                    }
                    ºÀÀÎÅÛ(pc, 7241, 5, 0, 1, 0, true); // öõ?à´
                    ºÀÀÎÅÛ(pc, 3000231, 3, 0, 1, 0, true); // ÍÔÐäÓìÓ¹
                    ºÀÀÎÅÛ(pc, 1000007, 10, 0, 1, 0, true); // ÍÔÐäÓìÓ¹
                }
/** Ê¥ìýûõßäÑÈÞÍÓ¥ (ÓãÜÅÔþ?) */
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5163) {
                if (s.equalsIgnoreCase("j")) { // Ê¥ìý
                    pc.sendPackets(new S_ServerMessage(3854));
                    return htmlid;
                } else if (s.equalsIgnoreCase("d")) { // ÷Üõó
                    if (pc.getRedKnightClanId() == 0) {
                        pc.sendPackets(new S_SystemMessage("??Ú±Ê¥ìýûõßäÑÈÞÍÓ¥¡£"));
                        return htmlid;
                    }

                    pc.setRedKnightClanId(0);
                    pc.sendPackets(new S_SystemMessage("?ì«÷ÜõóûõßäÑÈÞÍÓ¥¡£"));
                    pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, false);
                    return htmlid;
                }

                /** Ä«ÀÌÀú */
            } else if (npcid == 7000079) {
                if (s.equalsIgnoreCase("1")) { // ´ë¿©
                    int countActiveMaps = BossTrainingSystem.getInstance().countRaidPotal();
                    if (pc.getInventory().checkItem(80500)) {
                        htmlid = "bosskey6";
                        // ÀÌ¹Ì ÈÆ·Ã¼Ò ¿­¼è¸¦ °¡Áö°í °è½Å °Í °°±º¿ä.
                        // ¸¹Àº ºÐµé²²¼­ ÀÌ¿ëÇÏ½Ç ¼ö ÀÖµµ·Ï ÈÆ·Ã¼Ò´Â ÇÑ »ç¶÷ ´ç ÇÏ³ª ¾¿¸¸ ´ë¿©ÇØ µå¸®°í ÀÖ½À´Ï´Ù.
                    } else if (countActiveMaps >= 99) {
                        htmlid = "bosskey3";
                        // ÁË¼ÛÇÕ´Ï´Ù.
                        // Áö±ÝÀº ¸ðµç ÈÆ·Ã¼Ò¿¡¼­ ÈÆ·ÃÀÌ ÁøÇà Áß ÀÔ´Ï´Ù.
                    } else {
                        htmlid = "bosskey4";
                    }
                } else if (s.matches("[2-4]")) {
                    if (!pc.getInventory().checkItem(80500)) { // ¾×¼Ç Á¶ÀÛ ¹æÁö
                        L1ItemInstance item = null;
                        int count = 0;
                        if (s.equalsIgnoreCase("2")) { // 4°³
                            count = 4;
                        } else if (s.equalsIgnoreCase("3")) { // 8°³
                            count = 8;
                        } else if (s.equalsIgnoreCase("4")) { // 16°³
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
                            // °°ÀÌ ÈÆ·ÃÀ» ¹ÞÀ¸½Ç ºÐµé¿¡°Ô ¿­¼è¸¦ ³ª´©¾î ÁÖ½Å ´ÙÀ½ Àú¿¡°Ô º¸¿©ÁÖ½Ã¸é ÈÆ·Ã¼Ò·Î ¾È³»ÇØ
                            // µå¸®°Ú½À´Ï´Ù.
                            // ÈÆ·Ã¼ÒÀÇ ´ë¿©½Ã°£Àº ÃÖ´ë 4½Ã°£ÀÌ¸ç, ÈÆ·Ã ÁßÀÌ¶ó ÇØµµ ´ë¿© ½Ã°£ÀÌ Á¾·áµÇ¸é ´ÙÀ½ »ç¶÷À»
                            // À§ÇØ ÈÆ·Ã¼Ò »ç¿ëÀÌ ÁßÁöµË´Ï´Ù.
                            // ÈÆ·Ã¿ë ¸ó½ºÅÍ¸¦ ¼ÒÈ¯ÇÏ½Ç ¶§¿¡´Â Ç×»ó ÈÆ·Ã¼ÒÀÇ ³²Àº »ç¿ë ½Ã°£À» È®ÀÎÇÏ½Ã±â ¹Ù¶ø´Ï´Ù.
                        } else {
                            htmlid = "bosskey5";
                            // ÁË¼ÛÇÏÁö¸¸, »ç¿ë·á¸¦ ÁöºÒÇÏÁö ¾ÊÀ¸½Ã¸é ÈÆ·Ã¼Ò¸¦ ºô·Áµå¸± ¼ö ¾ø½À´Ï´Ù.
                            // ¾Æµ§ ¿Õ±¹ÀÇ Áö¿ø±Ý¸¸À¸·Î ÀÌ ¸¹Àº ÈÆ·Ã¼Ò¸¦ °ü¸®ÇÏ´Â °ÍÀÌ ½¬¿î ÀÏÀº ¾Æ´Ï¶ó¼­¿ä.
                        }
                    } else {
                        htmlid = "bosskey6";
                        // ÀÌ¹Ì ÈÆ·Ã¼Ò ¿­¼è¸¦ °¡Áö°í °è½Å °Í °°±º¿ä.
                        // ¸¹Àº ºÐµé²²¼­ ÀÌ¿ëÇÏ½Ç ¼ö ÀÖµµ·Ï ÈÆ·Ã¼Ò´Â ÇÑ »ç¶÷ ´ç ÇÏ³ª ¾¿¸¸ ´ë¿©ÇØ µå¸®°í ÀÖ½À´Ï´Ù.
                    }
                } else if (s.equalsIgnoreCase("6")) { // ÀÔÀå
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
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7200022) {// »ýÀÏµµ¿ì¹Ì
                L1NpcInstance npc = (L1NpcInstance) obj;
                if (pc.isInvisble()) {
                    pc.sendPackets(new S_NpcChatPacket(npc, "ÙíÛöî¤ëßû¡ßÒ÷¾ù»òäú¼ó®ðÃíÂ¡£", 0));
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
                if (s.equalsIgnoreCase("1")){ // ÞÝÖÄÛöÞÔ
                    if (pc.getInventory().consumeItem(80464, 1)) {
                        L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45456, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÞÝÖÄÛöÞÔ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("2")) { // ÞÝØÌÑÈÞÍ
                    if (pc.getInventory().consumeItem(80465, 1)) {
                        L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45601, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÞÝØÌÑÈÞÍ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("3")) { // äÂØª
                    if (pc.getInventory().consumeItem(80450, 1)) {
                        L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45649, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[äÂØª]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("4")) { // ÜóüÅ
                    if (pc.getInventory().consumeItem(80479, 1)) {
                        L1SpawnUtil.spawn2(32752, 32836, (short) pc.getMapId(), 45617, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÜôÞÝðè]ì«î¤ñéäçá¯ü°¡£", 2));
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
            /** ßÝà¤×éä¬ */
            else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000080) {
                L1NpcInstance npc = (L1NpcInstance) obj;
                if (s.equalsIgnoreCase("A")) { // ßÚä³÷²ÎÖÚª
                    if (pc.getInventory().consumeItem(80466, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 900076, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ùëïËîÜÝÂãó]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("B")) { // ßÚä³÷²ÎÖÚª
                    if (pc.getInventory().consumeItem(80467, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 900070, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ýÙØªÛöÞÔ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("C")) { // ßÚä³÷²ÎÖÚª
                    if (pc.getInventory().consumeItem(80450, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45649, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[äÂØª]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("D")) { // ßÚä³÷²ÎÖÚª
                    if (pc.getInventory().consumeItem(80451, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45685, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[öåÕª]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
/** ÕÃÞÙ÷²÷éÓìÎÖÚª **/
                if (s.equalsIgnoreCase("E")) {
                    if (pc.getInventory().consumeItem(80452, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45955, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ËÂÑÖ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("F")) {
                    if (pc.getInventory().consumeItem(80453, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45959, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ì¥îèä¬]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("G")) {
                    if (pc.getInventory().consumeItem(80454, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45956, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[Ýïä¬÷²ÞÙ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("H")) {
                    if (pc.getInventory().consumeItem(80455, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45957, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[÷éÔþØÞÞÙ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("I")) {
                    if (pc.getInventory().consumeItem(80456, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45960, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ð«ä¬ØÞÞÙ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("J")) {
                    if (pc.getInventory().consumeItem(80457, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45958, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ëÚîèä¬ÞÙ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("K")) {
                    if (pc.getInventory().consumeItem(80458, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45961, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÕÃÚ·ä¬ÞÙ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("L")) {
                    if (pc.getInventory().consumeItem(80459, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45962, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[÷éÔþÓì]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("M")) {
                    if (pc.getInventory().consumeItem(80460, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45676, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[úÓì³ÛÈëÚ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("N")) {
                    if (pc.getInventory().consumeItem(80461, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45677, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÕÑä¬]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("O")) {
                    if (pc.getInventory().consumeItem(80462, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45844, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[÷éÕµ?]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("P")) {
                    if (pc.getInventory().consumeItem(80463, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45648, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÞÙÖôÜý]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
/** Í¯ÖÛîèÏ±Ëþ?ÎÖÚª **/
                if (s.equalsIgnoreCase("Q")) {
                    if (pc.getInventory().consumeItem(80464, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45456, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÞÝÖÄÛöÞÔ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("S")) {
                    if (pc.getInventory().consumeItem(80465, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45601, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÞÝØÌÑÈÞÍ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                /** çìØ·ñý÷² **/
                if (s.equalsIgnoreCase("T")) {
                    if (pc.getInventory().consumeItem(80468, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310015, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[?ÍØîÜòÒÒùÞÙÒ³èÝ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("U")) {
                    if (pc.getInventory().consumeItem(80469, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310021, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÜôãáîÜßßä¬]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("V")) {
                    if (pc.getInventory().consumeItem(80470, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310028, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÍðÏ«îÜýåúìÐ¡]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("W")) {
                    if (pc.getInventory().consumeItem(80471, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310034, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÞÝØÌîÜ?ã»ÖÅñ«]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("X")) {
                    if (pc.getInventory().consumeItem(80472, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310041, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ò¢è«îÜÍ·Ì«]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("Y")) {
                    if (pc.getInventory().consumeItem(80473, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310046, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÜôÞÝîÜÙÊÒ¬ì¥ÖÅñ«]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("Z")) {
                    if (pc.getInventory().consumeItem(80474, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310051, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[íÑûåîÜäõ×éÞê]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("a")) {
                    if (pc.getInventory().consumeItem(80475, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310056, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ýÙäÞÑÈÞÍè¿ì³Óì]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("b")) {
                    if (pc.getInventory().consumeItem(80476, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310061, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[Üôý®îÜÙãèí]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("c")) {
                    if (pc.getInventory().consumeItem(80477, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310077, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ÞÝãêÌ«×ù×ìÚÖ]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                if (s.equalsIgnoreCase("d")) {
                    if (pc.getInventory().consumeItem(80478, 1)) {
                        L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45600, 0, 3600 * 1000, 0);
                        pc.sendPackets(new S_NpcChatPacket(npc, "[ýÙÑÈÞÍÓéíþÎ¡í¼]ì«î¤ñéäçá¯ü°¡£", 2));
                    } else {
                        htmlid = "bosskey10";
                    }
                }
                // Ëþ?NPCãÀÜú? "³°ÀºÃ¥´õ¹Ì" (ÏÁßö÷Ø)
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 8502036) {
                if (s.equalsIgnoreCase("a")) {
                    // Ëþ?èÌÊ«îÜÛÎøÐñéãÀÜúì«êóID?60032îÜÚªù¡
                    if (pc.getInventory().checkItem(60032)) {
                        // åýÍýì«êóú±Úªù¡£¬öÎÛ¡áêá¼ãÓÍ±ò±èÌÊ«ì«è¶êóú±Úªù¡
                        pc.sendPackets(new S_ChatPacket(pc, "ÏÁßö÷ØÚªù¡ì«ðíî¤¡£"));
                        htmlid = "";
                    } else {
                        // åýÍýÙÒêóú±Úªù¡£¬öÎíâÐìôÕÊ¥ÓðèÌÊ«îÜÛÎøÐñé
                        pc.getInventory().storeItem(60032, 1);
                        htmlid = "oldbook2";
                    }
                }
// Ëþ?NPCãÀÜú?ID?7210007îÜNPC
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7210007) {
                if (s.equalsIgnoreCase("a")) {
                    // Ëþ?èÌÊ«îÜÔõÐäãÀÜúÓÞåÚûäÔõåÚ60
                    if (pc.getLevel() >= 60) {
                        L1Quest quest = pc.getQuest();
                        int questStep = quest.get_step(L1Quest.QUEST_HAMO);
                        // Ëþ?èÌÊ«îÜÛÎøÐñéãÀÜúÙÒêóID?820000îÜÚªù¡ó¦ìòÙâ?Ú±èÇà÷
                        if (!pc.getInventory().checkItem(820000) && questStep != L1Quest.QUEST_END) {
                            // íâìòÙâßÒ÷¾àâöÇ?èÇà÷?ÐååøèÌÊ«ìéËÁID?820000îÜÚªù¡
                            pc.getQuest().set_end(L1Quest.QUEST_HAMO);
                            pc.getInventory().storeItem(820000, 1); // ÇÜÀÇÁÖ¸Ó´Ï (Ham's Pouch)
                            htmlid = "";
                        } else {
                            htmlid = "hamo1";
                        }
                    } else {
                        // åýÍýèÌÊ«ÔõÐäÜôðë60£¬öÎÛ¡áêá¼ãÓÍ±ò±èÌÊ«ÔõÐäÜô?
                        htmlid = "hamo3";
                        pc.sendPackets(new S_SystemMessage("ÐÁùÚ60Ðäì¤ß¾ÊÇßäÊ¦ïÈö¢ó®ìòÙâ¡£"));
                    }
                }
            }
            /** éâÙ¾õ½ Piar **/
        } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7310093) {
            if (s.equalsIgnoreCase("a")) {
//                    if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
                if (!pc.isPcBuff()) {
                    pc.sendPackets(new S_SystemMessage("ÐÁùÚÞÅéÄØÑ?ÏçãÁÊ¦òäú¼ó®ðÃíÂ¡£"));
                    htmlid = "pc_tell2";
                    return htmlid;
                }
                if (pc.getMap().isEscapable() || pc.isGm()) {
                    int rx = _random.nextInt(7);
                    int ux = 32768 + rx;
                    int uy = 32834 + rx; // ßÚä³÷²
                    pc.start_teleport(ux, uy, 622, pc.getHeading(), 18339, true, false);
                }
            }
        }
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70611
                    || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70530) { // ¼ÓÁËÀÇ¼º¼­
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
                 pc.sendPackets("ÙíÛöüáÜÖÛöÓø?¡£");
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
            pc.sendPackets(new S_SystemMessage(sec + "õ©ý­î¦ÒöÞÅéÄ¡£"));
            return htmlid;
        }
        if (pc.getLevel() < 80) {
            pc.sendPackets(new S_SystemMessage("ÐÁùÚ80Ðäì¤ß¾ÊÇßäÊ¦ÞÅéÄ¡£"));
            return htmlid;
        }
                if (s.equals("0")) { // ¸¶¹ýÀ» ¹Þ´Â´Ù.
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
        if (/*(pc.getClanRank() != L1Clan.¼ö·Ã) && */(pc.getClanRank() != L1Clan.áúûÞÑÈÞÍ)
                && (pc.getClanRank() != L1Clan.ìéÚõ) && (pc.getClanRank() != L1Clan.Øïñ«)
                && (pc.getClanRank() != L1Clan.ôêçÈ)) {
            pc.sendPackets(new S_SystemMessage("ÐÁùÚ÷åïÒÍëüåà÷ê¬Ê¦ïÈáô¡£"));
            return htmlid;
        }
    }
                /*
                 * if (s.equals("a")) { // ¸¶¹ýÀ» ¹Þ´Â´Ù. if (!pc.getClan().decWarPoint()) {
                 * pc.sendPackets(new S_SystemMessage("Ç÷¸Í Æ÷ÀÎÆ®°¡ ¸ðÀÚ¶ø´Ï´Ù.")); return htmlid; }
                 */

                int[] allBuffSkill = { 14, 26, 42, 54, 48, 79, 160, 206, 211, 216, 158, 168 };
                // µ¦,Èû,ºí·¹½º¿þÆù,³×ÀÌÃÄ½º
                pc.setBuffnoch(1);
                L1SkillUse l1skilluse = null;
                l1skilluse = new L1SkillUse();
                for (int i = 0; i < allBuffSkill.length; i++) {
                    l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
                            L1SkillUse.TYPE_GMBUFF);
                    // }
                    pc.sendPackets(new S_SkillSound(pc.getId(), 830));
                    pc.curePoison();
                    // pc.setBuffTime(time);// ¹öÇÁµô·¹ÀÌ
                }
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 6200008) {
                long time = System.currentTimeMillis();
                int useTime = 20 * 60 * 1000; // 20ÝÂñ¤ý­

                if (pc.getBuffTime() + (useTime) > time) {
                long sec = ((pc.getBuffTime() + (useTime)) - time) / 1000;
                pc.sendPackets(new S_SystemMessage(sec + "õ©ý­î¦ÒöÞÅéÄ¡£"));
                return htmlid;
                }
                if (s.equals("a")) { // ¸¶¹ýÀ» ¹Þ´Â´Ù.
                    int[] allBuffSkill = { 26, 42, 48, 158 };
                    // µ¦,Èû,ºí·¹½º¿þÆù,³×ÀÌÃÄ½º
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
                htmlid = éÖ×é(s, pc);
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000055) {
    try{
        String ItemName=""; // àâöÇìéËÁÍöîÜí®Ý¬ÍúÜ¨â¦ ItemName
        String Stat=""; // àâöÇìéËÁÍöîÜí®Ý¬ÍúÜ¨â¦ Stat
        String Class=""; // àâöÇìéËÁÍöîÜí®Ý¬ÍúÜ¨â¦ Class
        String Level=""; // àâöÇìéËÁÍöîÜí®Ý¬ÍúÜ¨â¦ Level
        String DelItemName=""; // àâöÇìéËÁÍöîÜí®Ý¬ÍúÜ¨â¦ DelItemName
        int itemid=0; // àâöÇìéËÁïÚâ¦Ü¨â¦ itemid ?ôøã·ûù? 0
        int Delitemid=0; // àâöÇìéËÁïÚâ¦Ü¨â¦ Delitemid ?ôøã·ûù? 0

        if (s.equals("str55")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "str55"

            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

        else if (pc.isîúÞÍ() || pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍûäÑÈÞÍ

            Class = "(ÑÈÞÍ,îúÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ,îúÞÍ"

        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                Level = "(55)"; // àâöÇÔõÐä? "55"

        Stat = "ÕôÕá"; // àâöÇáÕàõ? "ÕôÕá"
        } else if (s.equals("dex55")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "dex55"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

        else if (pc.isîúÞÍ() || pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍûäÑÈÞÍ
            Class = "(ÑÈÞÍ,îúÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ,îúÞÍ"

        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                Level = "(55)"; // àâöÇÔõÐä? "55"

        Stat = "ÚÂôß"; // àâöÇáÕàõ? "ÚÂôß"
        } else if (s.equals("con55")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "con55"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

        else if (pc.isîúÞÍ() || pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍûäÑÈÞÍ
            Class = "(ÑÈÞÍ,îúÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ,îúÞÍ"

        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                Level = "(55)"; // àâöÇÔõÐä? "55"

        Stat = "ô÷Õô"; // àâöÇáÕàõ? "ô÷Õô"
        } else if (s.equals("int55")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "int55"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

        else if (pc.isîúÞÍ() || pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍûäÑÈÞÍ
            Class = "(ÑÈÞÍ,îúÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ,îúÞÍ"

        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


                Level = "(55)"; // àâöÇÔõÐä? "55"

        Stat = "ïñãê"; // àâöÇáÕàõ? "ïñãê"
        } else if (s.equals("wis55")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "wis55"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
        else if (pc.isîúÞÍ() || pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍûäÑÈÞÍ

            Class = "(ÑÈÞÍ,îúÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ,îúÞÍ"
        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                Level = "(55)"; // àâöÇÔõÐä? "55"

        Stat = "ïñãê"; // àâöÇáÕàõ? "ïñãê"
        } else if (s.equals("str70")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "str70"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
        else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
            Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
        (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
        Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                Level = "(70)"; // àâöÇÔõÐä? "70"
        Stat = "ÕôÕá"; // àâöÇáÕàõ? "ÕôÕá"
        } else if (s.equals("dex70")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "dex70"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
        else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
            Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
        else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
            Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                Level = "(70)"; // àâöÇÔõÐä? "70"
        Stat = "ÚÂôß"; // àâöÇáÕàõ? "ÚÂôß"
        } else if (s.equals("con70")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "con70"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
        else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
            Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
        else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
            Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                Level = "(70)"; // àâöÇÔõÐä? "70"
        Stat = "ô÷Õô"; // àâöÇáÕàõ? "ô÷Õô"
        } else if (s.equals("int70")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "int70"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
        else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
            Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
        else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
            Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                Level = "(70)"; // àâöÇÔõÐä? "70"
        Stat = "ïñãê"; // àâöÇáÕàõ? "ïñãê"
        } else if (s.equals("wis70")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "wis70"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
        else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
            Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
        else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
            Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                Level = "(70)"; // àâöÇÔõÐä? "70"
        Stat = "ïñãê"; // àâöÇáÕàõ? "ïñãê"
        } else if (s.equals("str80")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "str80"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
        else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
            Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
        else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
            Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                Level = "(80)"; // àâöÇÔõÐä? "80"
        Stat = "ÕôÕá"; // àâöÇáÕàõ? "ÕôÕá"
        } else if (s.equals("dex80")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "dex80"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
        else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
            Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
        else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
            Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

               Level = "(80)"; // àâöÇÔõÐä? "80"
        Stat = "ÚÂôß"; // àâöÇáÕàõ? "ÚÂôß"
        } else if (s.equals("con80")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "con80"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
        else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
            Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
        else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
            Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
        else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
            Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
            Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
        else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
            Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
        else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
            Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
        else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
            Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
        else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
            Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                Level = "(80)"; // àâöÇÔõÐä? "80"
        Stat = "ô÷Õô"; // àâöÇáÕàõ? "ô÷Õô"
        } else if (s.equals("int80")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "int80"
               if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                   Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
           else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
               Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
           else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
               Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
           else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
               Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
           else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
               Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
           else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
               Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
           else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
               Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
           else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
               Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
           else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
               Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                   Level = "(80)"; // àâöÇÔõÐä? "80"
           Stat = "ò±ãÛ"; // àâöÇáÕàõ? "ò±ãÛ"
        } else if (s.equals("wis80")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "wis80"
            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
            else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
                Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
            else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
                Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
            else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
                Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
            else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
                Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
            else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
                Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
            else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
                Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
            else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
                Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
            else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
                Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                     Level = "(80)"; // àâöÇÔõÐä? "80"
            Stat = "ïñãê"; // àâöÇáÕàõ? "ïñãê"
        } else if (s.equals("str85")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "str85"
                if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                    Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
            else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
                Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
            else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
                Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
            else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
                Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
            else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
                Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
            else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
                Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
            else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
                Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
            else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
                Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
            else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
                Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                    Level = "(85)"; // àâöÇÔõÐä? "85"
            Stat = "ÕôÕá"; // àâöÇáÕàõ? "ÕôÕá"
            } else if (s.equals("dex85")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "dex85"
                if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ
                    Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"
            else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ
                Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"
            else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ
                Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"
            else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé
                Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"
            else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ
                Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"
            else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ
                Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"
            else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ
                Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"
            else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ
                Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"
            else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ
                Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                     Level = "(85)"; // àâöÇÔõÐä? "85"
            Stat = "ÚÂôß"; // àâöÇáÕàõ? "ÚÂôß"

        } else if (s.equals("con85")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "con85"

                if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                        Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

                else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                    Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

                else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                    Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

                else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                    Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

                else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                    Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

                else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                    Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

                else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                    Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

                else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                    Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

                else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                    Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


                        Level = "(85)"; // àâöÇÔõÐä? "85"

                Stat = "ô÷Õô"; // àâöÇáÕàõ? "ô÷Õô"

        } else if (s.equals("int85")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "int85"

                if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                        Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

                else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                    Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

                else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                    Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

                else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                    Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

                else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                    Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

                else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                    Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

                else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                    Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

                else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                    Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

                else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                    Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                        Level = "(85)"; // àâöÇÔõÐä? "85"

                Stat = "òªÕô"; // àâöÇáÕàõ? "òªÕô"

        } else if (s.equals("wis85")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "wis85"

                if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                        Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

                else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                    Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

                else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                    Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

                else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                    Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

                else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                    Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

                else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                    Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

                else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                    Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

                else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                    Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

                else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                    Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


                        Level = "(85)"; // àâöÇÔõÐä? "85"

                Stat = "ïñãê"; // àâöÇáÕàõ? "ïñãê"

        } else if (s.equals("str90")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "str90"

                if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                        Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

                else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                    Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

                else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                    Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

                else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                    Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

                else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                    Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

                else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                    Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

                else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                    Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

                else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                    Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

                else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                    Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                        Level = "(90)"; // àâöÇÔõÐä? "90"

                Stat = "ÕôÕá"; // àâöÇáÕàõ? "ÕôÕá"

        } else if (s.equals("dex90")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "dex90"

                if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                    Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

            else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

            else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

            else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

            else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

            else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

            else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

            else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

            else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


                    Level = "(90)"; // àâöÇÔõÐä? "90"

            Stat = "ÚÂôß"; // àâöÇáÕàõ? "ÚÂôß"

        } else if (s.equals("con90")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "con90"

                if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                       Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

               else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                   Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

               else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                   Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

               else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                   Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

               else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                   Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

               else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                   Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

               else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                   Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

               else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                   Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

               else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                   Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


                       Level = "(90)"; // àâöÇÔõÐä? "90"

               Stat = "ô÷Õô"; // àâöÇáÕàõ? "ô÷Õô"

        } else if (s.equals("int90")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "int90"

                if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                    Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

            else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

            else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

            else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

            else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

            else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

            else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

            else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

            else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


                    Level = "(90)"; // àâöÇÔõÐä? "90"

            Stat = "òªÕô"; // àâöÇáÕàõ? "òªÕô"

        } else if (s.equals("wis90")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "wis90"

                 if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                       Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

               else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                   Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

               else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                   Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

               else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                   Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

               else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                   Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

               else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                   Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

               else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                   Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

               else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                   Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

               else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                   Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


                       Level = "(90)"; // àâöÇÔõÐä? "90"

               Stat = "ïñãê"; // àâöÇáÕàõ? "ïñãê"

        } else if (s.equals("str91")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "str91"

                if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                        Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

                else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                    Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

                else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                    Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

                else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                    Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

                else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                    Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

                else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                    Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

                else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                    Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

                else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                    Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

                else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                    Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"



                        Level = "(91)"; // àâöÇÔõÐä? "91"

        Stat = "ÕôÕá"; // àâöÇáÕàõ? "ÕôÕá"

        } else if (s.equals("dex91")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "dex91"

            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

               else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                   Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

               else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                   Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

               else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                   Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

                       (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                   Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

               else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                   Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

               else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                   Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

               else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                   Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

               else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                   Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"



                       Level = "(91)"; // àâöÇÔõÐä? "91"

               Stat = "ÚÂôß"; // àâöÇáÕàõ? "ÚÂôß"

        } else if (s.equals("con91")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "con91"

            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                   Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

           else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

               Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

                   else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

               Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

           else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

               Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

           else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

               Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

           else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

               Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

           else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

               Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

           else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

               Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

           else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

               Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


           Level = "(91)"; // àâöÇÔõÐä? "91"

           Stat = "ô÷Õô"; // àâöÇáÕàõ? "ô÷Õô"

            } else if (s.equals("int91")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "int91"

                if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                    Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

            else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

            else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

            else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

                    (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

            else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

            else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

            else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

                    (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                        Level = "(91)"; // àâöÇÔõÐä? "91"

                Stat = "òªÕô"; // àâöÇáÕàõ? "òªÕô"

        } else if (s.equals("wis91")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "wis91"

                    if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                        Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

                       (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                    Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

                else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                    Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

                else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                    Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

                       else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                    Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

                else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                    Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

                else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                    Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

                else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                    Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

                else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                    Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


                        Level = "(91)"; // àâöÇÔõÐä? "91"

             Stat = "ïñãê"; // àâöÇáÕàõ? "ïñãê"

        } else if (s.equals("str92")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "str92"

                 if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                     Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

             else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                 Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

             else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                 Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

             else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                 Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

             else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                 Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

             else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                 Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

                     (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                 Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

             else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                 Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

             else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                 Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"

                     Level = "(92)"; // àâöÇÔõÐä? "92"

             Stat = "ÕôÕá"; // àâöÇáÕàõ? "ÕôÕá"

        } else if (s.equals("dex92")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "dex92"

               if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                   Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

           else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

               Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

           else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

               Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

           else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

               Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

           else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

               Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

           else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

               Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

           else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

               Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

           else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

               Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

           else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

              Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


                      Level = "(92)"; // àâöÇÔõÐä? "92"

             Stat = "ÚÂôß"; // àâöÇáÕàõ? "ÚÂôß"

        } else if (s.equals("con92")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "con92"

                    if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                        Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

                       else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                    Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

                 else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                    Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

                else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                    Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

                else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                    Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

                else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                    Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

                else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                    Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

                else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                    Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

                else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                    Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


                        Level = "(92)"; // àâöÇÔõÐä? "92"

                Stat = "ô÷Õô"; // àâöÇáÕàõ? "ô÷Õô"

        } else if (s.equals("int92")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "int92"

            if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                      Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"


                    else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ



                        Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"


                    else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ



                        Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"


                    else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé



                        Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"


                    else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ



                  Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"


                   else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ



                       Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"



                   else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ



                       Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"



                   else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ



                       Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"


                   else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ



                       Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"



                         Level = "(92)"; // àâöÇÔõÐä? "92"

                 Stat = "òªÕô"; // àâöÇáÕàõ? "òªÕô"

                    } else if (s.equals("wis92")) { // åýÍýí®Ý¬Íú s îÜ?ÔõåÚ "wis92"

                        if (pc.isElf()) // åýÍýèÌÊ«ÊÇßäãÀèíïñ

                            Class = "(èíïñ)"; // àâöÇòÅåöÙ£öà? "èíïñ"

                    else if (pc.isWizard()) // åýÍýèÌÊ«ÊÇßäãÀÛöÞÔ

                        Class = "(ÛöÞÔ)"; // àâöÇòÅåöÙ£öà? "ÛöÞÔ"

                    else if (pc.isDarkelf()) // åýÍýèÌÊ«ÊÇßäãÀýÙäÞèíïñ

                        Class = "(ýÙäÞèíïñ)"; // àâöÇòÅåöÙ£öà? "ýÙäÞèíïñ"

                    else if (pc.isCrown()) // åýÍýèÌÊ«ÊÇßäãÀèÝðé

                        Class = "(èÝðé)"; // àâöÇòÅåöÙ£öà? "èÝðé"

                        else if (pc.isDragonknight()) // åýÍýèÌÊ«ÊÇßäãÀ×£ÑÈÞÍ

                        Class = "(×£ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "×£ÑÈÞÍ"

                    else if (pc.isBlackwizard()) // åýÍýèÌÊ«ÊÇßäãÀü³âúÞÔ

                        Class = "(ü³âúÞÔ)"; // àâöÇòÅåöÙ£öà? "ü³âúÞÔ"

                    else if (pc.isîúÞÍ()) // åýÍýèÌÊ«ÊÇßäãÀîúÞÍ

                        Class = "(îúÞÍ)"; // àâöÇòÅåöÙ£öà? "îúÞÍ"

                    else if (pc.isKnight()) // åýÍýèÌÊ«ÊÇßäãÀÑÈÞÍ

                        Class = "(ÑÈÞÍ)"; // àâöÇòÅåöÙ£öà? "ÑÈÞÍ"

                    else if (pc.isFencer()) // åýÍýèÌÊ«ÊÇßäãÀËüÞÍ

                        Class = "(ËüÞÍ)"; // àâöÇòÅåöÙ£öà? "ËüÞÍ"


                    Level = "(92)"; // àâöÇÔõÐä? "92"

                    Stat = "ïñãê"; // àâöÇáÕàõ? "ïñãê"
                    }
        // àâöÇèÇïÚÔ³ÎýÙ£öà
        ItemName = Stat + "îÜÖÄå·Ý¬Ùþ" + Level + Class; // ÖÇåý "òªÕôîÜÖÄå·Ý¬Ùþ(92)(ÛöÞÔ)"
        DelItemName = "ã÷ËÛØªÕôîÜÝ¬Ùþ" + Level; // ÖÇåý "ã÷ËÛØªÕôîÜÝ¬Ùþ(92)"


        // ÐÆËàÙ£öà??ÓßëëîÜÔ³ÎýID
        itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(ItemName);
        Delitemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(DelItemName);


        // á¼ÙÄìéËÁ "ã÷ËÛØªÕôîÜÝ¬Ùþ" Ô³Îý£¬?ôÕÊ¥ìéËÁ "ÖÄå·Ý¬Ùþ" Ô³Îý
        if (pc.getInventory().consumeItem(Delitemid, 1)) {
        // íâãæîÜÔ³ÎýôÕÊ¥ÓðèÌÊ«îÜÍ·ðíñé
        pc.getInventory().storeItem(itemid, 1, true); // ÖÇåý£¬"ÚÂôßîÜÖÄå·"

        // üòö¢ãæÔ³ÎýîÜÙ¼÷ù
        L1Item run = ItemTable.getInstance().getTemplate(itemid);

        // ú¾èÌÊ«Û¡áêá¼ãÓ£¬÷×ò±Ô³Îýì«ÌÚÜ¨
        pc.sendPackets(run.getNameId() + "ì«ÌÚËÇ¡£"); // ÖÇåý "òªÕôîÜÖÄå·Ý¬Ùþì«ÌÚËÇ¡£"
        } else {
        // åýÍýÙÒêó "ã÷ËÛØªÕôîÜÝ¬Ùþ" Ô³Îý£¬àâöÇHTML ID?"riddle2"
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
                                pc.getInventory().storeItem(60036, 1); // ÈûÀÇ ¿¤¸¯¼­
                                // ·éÁÖ¸Ó´Ï
                            } else if (s.equals("B")) {
                                pc.getInventory().storeItem(60037, 1); // ¹ÎÃ¸ÀÇ
                                // ¿¤¸¯¼­ ·é
                            } else if (s.equals("C")) {
                                pc.getInventory().storeItem(60038, 1); // Ã¼·ÂÀÇ
                                // ¿¤¸¯¼­ ·é
                            } else if (s.equals("D")) {
                                pc.getInventory().storeItem(60039, 1); // Áö½ÄÀÇ
                                // ¿¤¸¯¼­ ·é
                            } else if (s.equals("E")) {
                                pc.getInventory().storeItem(60040, 1); // ÁöÇýÀÇ
                                // ¿¤¸¯¼­ ·é
                            }
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune6"));

                        } else {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune5"));
                        }
                    } else {
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune5"));
                    }
                }
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 1000001) {// ¶¥±¼°³¹Ì
                int locx = 0, locy = 0, map = 0;
                if (s.equalsIgnoreCase("b")) {// 1¹øµ¿±¼
                    locx = 32783;
                    locy = 32751;
                    map = 43;
                } else if (s.equalsIgnoreCase("c")) {// 2¹øµ¿±¼
                    locx = 32798;
                    locy = 32754;
                    map = 44;
                } else if (s.equalsIgnoreCase("d")) {// 3¹øµ¿±¼
                    locx = 32776;
                    locy = 32731;
                    map = 45;
                } else if (s.equalsIgnoreCase("e")) {// 4¹øµ¿±¼
                    locx = 32787;
                    locy = 32795;
                    map = 46;
                } else if (s.equalsIgnoreCase("f")) {// 5¹øµ¿±¼
                    locx = 32796;
                    locy = 32745;
                    map = 47;
                } else if (s.equalsIgnoreCase("g")) {// 6¹øµ¿±¼
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
                // ±ºÅÍ
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 60169) {
                if (s.equalsIgnoreCase("a")) {
                    new L1SkillUse().handleCommands(pc, L1SkillId.BUFF_GUNTER, pc.getId(), pc.getX(), pc.getY(), null,
                            0, L1SkillUse.TYPE_SPELLSC);
                }
                // Å©·¹ÀÌ
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
                // ÀúÁÖ¹ÞÀº ¹«³à »ç¿¤ (ÀÔ±¸ npc)
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
} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 522) { // åýÍýNPCîÜIDãÀ522
    if (s.equalsIgnoreCase("giveto")) { // åýÍýí®Ý¬Íú s ÔõåÚ "giveto"£¨ûìÕÔÓÞá³ÞÐ£©
        if (pc.getInventory().checkItem(40308, 200000)) { // åýÍýèÌÊ«îÜÍ·ðíñéêó200,000ËÁID?40308îÜÚªù¡
            pc.getInventory().consumeItem(40308, 200000); // ðôèÌÊ«îÜÍ·ðíñéá¼ÙÄ200,000ËÁID?40308îÜÚªù¡
        L1SkillUse aa = new L1SkillUse(); // óÜËïãæîÜ L1SkillUse ãùÖÇ
        aa.handleCommands(pc, L1SkillId.HUNTER_BLESS, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF); // ÞÅéÄ "HUNTER_BLESS" ÐüÒö
        Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 9009)); // ÎÆ÷ëÐüÒöëåüù
        pc.sendPackets(new S_SkillSound(pc.getId(), 9009)); // ú¾èÌÊ«Û¡áêÐüÒöëåüù
        htmlid = ""; // àâöÇHTML ID?Íö
        } else {
            pc.sendPackets(new S_SystemMessage("æÈñüñýîñ...é¸ÞÍëëú±à»ÍÓà­í»ÐùîÜäÌîï...?îÜÑÑøÇÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±ÑÑøÇÜôðë
        htmlid = ""; // àâöÇHTML ID?Íö
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
                } else if (s.equalsIgnoreCase("D")) {// ¹ß¶ó µÕÁö
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
                } else if (s.equalsIgnoreCase("J")) {// ¿ÀÅ© ½£
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
                } else if (s.equalsIgnoreCase("V")) {// µ¥Æ÷·ùÁî¾Õ
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
        // ¸®Å° ¼ö·ÃÀÇÅÚ·¹Æ÷Æ®
// ßúÐôîÜýºÖ£îîáê
 } else if (npcid == 70798) { // åýÍýNPCîÜIDãÀ70798
     if (s.equalsIgnoreCase("a")) { // ëßíúÍÛ
         if (pc.getLevel() >= 1 && pc.getLevel() <= 45) { // åýÍýèÌÊ«ÔõÐäî¤1Óð45ñýÊà
             pc.start_teleport(32684, 32851, 2005, pc.getHeading(), 18339, true, false); // îîáêèÌÊ«ÓðëßíúÍÛ
         } else {
             pc.sendPackets(
                     new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\fQßúÐô: \f3[Lv.45]\fQì¤ù»î¦Òöòäìý¡£¡£")); // ú¾èÌÊ«Û¡áêá¼ãÓ£¬÷×ò±ÔõÐäùÚð¤
         }
      } else if (s.equalsIgnoreCase("b")) { // ÑÎÕµ?
         pc.start_teleport(33436, 32799, 4, pc.getHeading(), 18339, true, false); // îîáêèÌÊ«ÓðÑÎÕµ?
      } else if (s.equalsIgnoreCase("c")) { // ÔþÜýãêîü
         if (pc.getLevel() >= 10 && pc.getLevel() <= 29) { // åýÍýèÌÊ«ÔõÐäî¤10Óð29ñýÊà
             pc.start_teleport(33184, 33449, 4, pc.getHeading(), 18339, true, false); // îîáêèÌÊ«ÓðÔþÜýãêîü
         } else {
             pc.sendPackets(new S_ChatPacket(pc, "ëÞÑÈÞÍÖÅò¢ì¹ÔÑÊ¦éÄÔõÐä 10 ~ 29")); // ú¾èÌÊ«Û¡áêá¼ãÓ£¬÷×ò±ÔõÐäùÚð¤
         }
      } else if (s.equalsIgnoreCase("d")) { // ?çóãêîü
         if (pc.getLevel() >= 10 && pc.getLevel() <= 29) { // åýÍýèÌÊ«ÔõÐäî¤10Óð29ñýÊà
             pc.start_teleport(33066, 33218, 4, pc.getHeading(), 18339, true, false); // îîáêèÌÊ«Óð?çóãêîü
         } else {
             pc.sendPackets(new S_ChatPacket(pc, "ëÞÑÈÞÍÖÅò¢ì¹ÔÑÊ¦éÄÔõÐä 10 ~ 29")); // ú¾èÌÊ«Û¡áêá¼ãÓ£¬÷×ò±ÔõÐäùÚð¤
         }
     }

} else if (s.equalsIgnoreCase("f")) { // ýºÖ£ò¢Öï

    if (pc.getLevel() >= 10 && pc.getLevel() < 20) {
        pc.start_teleport(32801, 32806, 25, pc.getHeading(), 18339, true, false);

    } else if (pc.getLevel() >= 20 && pc.getLevel() < 30) {
        pc.start_teleport(32806, 32746, 26, pc.getHeading(), 18339, true, false);

    } else if (pc.getLevel() >= 30 && pc.getLevel() < 40) {
        pc.start_teleport(32808, 32766, 27, pc.getHeading(), 18339, true, false);

    } else if (pc.getLevel() >= 40 && pc.getLevel() < 44) {
        pc.start_teleport(32796, 32799, 28, pc.getHeading(), 18339, true, false);

    } else {
        pc.sendPackets(new S_ChatPacket(pc, "ýºÖ£ò¢ÖïÊ¦òäìýÔõÐä 10 ~ 44"));

    }

} else if (s.equalsIgnoreCase("e")) { // øìù¦ò¢Öï Üôãáìò Lv 45~51

    if (pc.getLevel() >= 45 && pc.getLevel() <= 51) {
        pc.start_teleport(32807, 32789, 2010, pc.getHeading(), 18339, true, false);

    } else {
        pc.sendPackets(new S_ChatPacket(pc, "øìù¦ýºÖ£ò¢ÖïÊ¦òäìýÔõÐä 45 ~ 51"));

    }

}

} else if (npcid == 50078) {

    if (pc.getLevel() <= 99) {
        pc.sendPackets(new S_SystemMessage("ðôÊ¦ë÷îÜô¸Íöü£ê®£¨PC£©÷×Î¦ïñÖÄÜÄñÁì¹ÔÑ¡£"));
        return htmlid;

    }

} else if (npcid == 7310174) { // åýÍýNPCîÜIDãÀ7310174

    if (s.equalsIgnoreCase("a")) { // ô¥×â "a" í®Ý¬Íú
        if (pc.getInventory().checkItem(3000211, 300)) { // Ëþ?èÌÊ«îÜÍ·ðíñéãÀÜúêó300ËÁID?3000211îÜÚªù¡
            pc.getInventory().consumeItem(3000211, 300); // ðôèÌÊ«îÜÍ·ðíñéá¼ÙÄ300ËÁID?3000211îÜÚªù¡
        pc.getInventory().storeItem(3000210, 1); // ÐååøèÌÊ«1ËÁID?3000210îÜÚªù¡
        pc.sendPackets(new S_SystemMessage("ÊïÞó?¡£ôëù»ó­î¢ÕÎ¡£")); // ú¾èÌÊ«Û¡áêÊïÞóîÜÍ§÷Öá¼ãÓ
        } else {
            pc.sendPackets(new S_SystemMessage("âÍé© [300] ËÁäõèßîÜëÚÞô..")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±âÍé©ÌÚÒýÚªù¡
        }

    } else if (s.equalsIgnoreCase("b")) { // ô¥×â "b" í®Ý¬Íú
        if (pc.getInventory().checkItem(3000211, 500)) { // Ëþ?èÌÊ«îÜÍ·ðíñéãÀÜúêó500ËÁID?3000211îÜÚªù¡
            pc.getInventory().consumeItem(3000211, 500); // ðôèÌÊ«îÜÍ·ðíñéá¼ÙÄ500ËÁID?3000211îÜÚªù¡
        pc.getInventory().storeItem(3000210, 2); // ÐååøèÌÊ«2ËÁID?3000210îÜÚªù¡
        pc.sendPackets(new S_SystemMessage("ÊïÞó?¡£ôëù»ó­î¢ÕÎ¡£")); // ú¾èÌÊ«Û¡áêÊïÞóîÜÍ§÷Öá¼ãÓ
        } else {
            pc.sendPackets(new S_SystemMessage("âÍé© [500] ËÁäõèßîÜëÚÞô..")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±âÍé©ÌÚÒýÚªù¡
        }

    } else if (s.equalsIgnoreCase("c")) { // ô¥×â "c" í®Ý¬Íú
        if (pc.getInventory().checkItem(3000211, 1000)) { // Ëþ?èÌÊ«îÜÍ·ðíñéãÀÜúêó1000ËÁID?3000211îÜÚªù¡
            pc.getInventory().consumeItem(3000211, 1000); // ðôèÌÊ«îÜÍ·ðíñéá¼ÙÄ1000ËÁID?3000211îÜÚªù¡
        pc.getInventory().storeItem(3000210, 5); // ÐååøèÌÊ«5ËÁID?3000210îÜÚªù¡
        pc.sendPackets(new S_SystemMessage("ÊïÞó?¡£ôëù»ó­î¢ÕÎ¡£")); // ú¾èÌÊ«Û¡áêÊïÞóîÜÍ§÷Öá¼ãÓ
        } else {
            pc.sendPackets(new S_SystemMessage("âÍé© [1000] ËÁäõèßîÜëÚÞô..")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±âÍé©ÌÚÒýÚªù¡
        }

    }


    // ¹Ù¹«Æ® Á¦ÀÛ¸®´º¾ó
        // ÷éÙÏ÷åð²íÂÌÚãæ

} else if (npcid == 70690) { // åýÍýNPCîÜIDãÀ70690

    if (s.equalsIgnoreCase("a")) { // ô¥×â "a" í®Ý¬Íú
        if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40053, 10)
        && pc.getInventory().checkItem(40393, 5)) { // Ëþ?èÌÊ«îÜÍ·ðíãÀÜúêóðë?îÜî§Öù
            pc.getInventory().consumeItem(410061, 50); // á¼ÙÄ50ËÁID?410061îÜÚªù¡
        pc.getInventory().consumeItem(40053, 10); // á¼ÙÄ10ËÁID?40053îÜÚªù¡
        pc.getInventory().consumeItem(40393, 5); // á¼ÙÄ5ËÁID?40393îÜÚªù¡
        pc.getInventory().storeItem(222307, 1); // ÐååøèÌÊ«1ËÁID?222307îÜÚªù¡£¨èÇÕôñýü¨£©
        htmlid = ""; // ?ÍöHTML ID
        } else { // åýÍýî§ÖùÜôðë
            pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÚªîÜÑ¨ãÓ(50)¡¢ÍÔÐäûõÜÄà´(10)¡¢ûý×£ñý×÷(5)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´
        }

    } else if (s.equalsIgnoreCase("b")) { // ô¥×â "b" í®Ý¬Íú
        if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40052, 10)
        && pc.getInventory().checkItem(40396, 5)) { // Ëþ?èÌÊ«îÜÍ·ðíãÀÜúêóðë?îÜî§Öù
            pc.getInventory().consumeItem(410061, 50); // á¼ÙÄ50ËÁID?410061îÜÚªù¡
        pc.getInventory().consumeItem(40052, 10); // á¼ÙÄ10ËÁID?40052îÜÚªù¡
        pc.getInventory().consumeItem(40396, 5); // á¼ÙÄ5ËÁID?40396îÜÚªù¡
        pc.getInventory().storeItem(22359, 1); // ÐååøèÌÊ«1ËÁID?22359îÜÚªù¡£¨òªû´ñýü¨£©
        htmlid = ""; // ?ÍöHTML ID
        } else { // åýÍýî§ÖùÜôðë
            pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÚªîÜÑ¨ãÓ(50)¡¢ÍÔÐäóÈà´(10)¡¢ò¢×£ñý×÷(5)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´
        }

    }

} else if (s.equalsIgnoreCase("b")) { // ô¥×â "b" í®Ý¬Íú

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40055, 10)
        && pc.getInventory().checkItem(40394, 5)) { // Ëþ?èÌÊ«îÜÍ·ðíãÀÜúêóðë?îÜî§Öù
        pc.getInventory().consumeItem(410061, 50); // á¼ÙÄ50ËÁID?410061îÜÚªù¡
        pc.getInventory().consumeItem(40055, 10); // á¼ÙÄ10ËÁID?40055îÜÚªù¡
        pc.getInventory().consumeItem(40394, 5); // á¼ÙÄ5ËÁID?40394îÜÚªù¡
        pc.getInventory().storeItem(222308, 1); // ÐååøèÌÊ«1ËÁID?222308îÜÚªù¡£¨ÚÂôßñýü¨£©
        htmlid = ""; // ?ÍöHTML ID

    } else { // åýÍýî§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÚªîÜÑ¨ãÓ(50)¡¢ÍÔÐäÝýö¨(10)¡¢ù¦×£ñý×÷(5)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´

    }

} else if (s.equalsIgnoreCase("d")) { // ô¥×â "d" í®Ý¬Íú

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40054, 10)
        && pc.getInventory().checkItem(40395, 5)) { // Ëþ?èÌÊ«îÜÍ·ðíãÀÜúêóðë?îÜî§Öù
        pc.getInventory().consumeItem(410061, 50); // á¼ÙÄ50ËÁID?410061îÜÚªù¡
        pc.getInventory().consumeItem(40054, 10); // á¼ÙÄ10ËÁID?40054îÜÚªù¡
        pc.getInventory().consumeItem(40395, 5); // á¼ÙÄ5ËÁID?40395îÜÚªù¡
        pc.getInventory().storeItem(222309, 1); // ÐååøèÌÊ«1ËÁID?222309îÜÚªù¡£¨ò±ãÛñýü¨£©
        htmlid = ""; // ?ÍöHTML ID

    } else { // åýÍýî§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÚªîÜÑ¨ãÓ(50)¡¢ÍÔÐäÕÀÜÄà´(10)¡¢â©×£ñý×÷(5)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´

    }

}

} else if (s.equalsIgnoreCase("e")) { // ô¥×â "e" í®Ý¬Íú

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560030)) { // Ëþ?èÌÊ«îÜÍ·ðíãÀÜúêóðë?îÜî§Öù
        pc.getInventory().consumeItem(410061, 50); // á¼ÙÄ50ËÁID?410061îÜÚªù¡
        pc.getInventory().consumeItem(560030, 1); // á¼ÙÄ1ËÁID?560030îÜÚªù¡
        pc.getInventory().storeItem(222307, 1); // ÐååøèÌÊ«1ËÁID?222307îÜÚªù¡£¨èÇÕôñýü¨£©
        htmlid = ""; // ?ÍöHTML ID

    } else { // åýÍýî§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÚªîÜÑ¨ãÓ(50)¡¢ûýÖÄáÕàõï®üµÏéõî(1)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´

    }

} else if (s.equalsIgnoreCase("f")) { // ô¥×â "f" í®Ý¬Íú

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560033)) { // Ëþ?èÌÊ«îÜÍ·ðíãÀÜúêóðë?îÜî§Öù
        pc.getInventory().consumeItem(410061, 50); // á¼ÙÄ50ËÁID?410061îÜÚªù¡
        pc.getInventory().consumeItem(560033, 1); // á¼ÙÄ1ËÁID?560033îÜÚªù¡
        pc.getInventory().storeItem(22359, 1); // ÐååøèÌÊ«1ËÁID?22359îÜÚªù¡£¨òªû´ñýü¨£©
        htmlid = ""; // ?ÍöHTML ID

    } else { // åýÍýî§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÚªîÜÑ¨ãÓ(50)¡¢ò¢ÖÄáÕàõï®üµÏéõî(1)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´

    }

}

} else if (s.equalsIgnoreCase("g")) { // ô¥×â "g" í®Ý¬Íú

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560032)) { // Ëþ?èÌÊ«îÜÍ·ðíãÀÜúêóðë?îÜî§Öù
        pc.getInventory().consumeItem(410061, 50); // á¼ÙÄ50ËÁID?410061îÜÚªù¡
        pc.getInventory().consumeItem(560032, 1); // á¼ÙÄ1ËÁID?560032îÜÚªù¡
        pc.getInventory().storeItem(222308, 1); // ÐååøèÌÊ«1ËÁID?222308îÜÚªù¡£¨ÚÂôßñýü¨£©
        htmlid = ""; // ?ÍöHTML ID

    } else { // åýÍýî§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÚªîÜÑ¨ãÓ(50)¡¢ù¦ÖÄáÕàõï®üµÏéõî(1)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´

    }

} else if (s.equalsIgnoreCase("h")) { // ô¥×â "h" í®Ý¬Íú

    if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560031)) { // Ëþ?èÌÊ«îÜÍ·ðíãÀÜúêóðë?îÜî§Öù
        pc.getInventory().consumeItem(410061, 50); // á¼ÙÄ50ËÁID?410061îÜÚªù¡
        pc.getInventory().consumeItem(560031, 1); // á¼ÙÄ1ËÁID?560031îÜÚªù¡
        pc.getInventory().storeItem(222309, 1); // ÐååøèÌÊ«1ËÁID?222309îÜÚªù¡£¨ò±ãÛñýü¨£©
        htmlid = ""; // ?ÍöHTML ID

    } else { // åýÍýî§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÚªîÜÑ¨ãÓ(50)¡¢â©ÖÄáÕàõï®üµÏéõî(1)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´

    }

}

// Áêºñµå ±â¾ïÀÇ È®Àå ±¸½½ Á¦ÀÛ
        // ð²íÂñ¹ù¨ÓìÑÀåãüªî÷â©ïÜ

} else if (npcid == 7310149) { // åýÍýNPCîÜIDãÀ7310149

    if (s.equalsIgnoreCase("request memory crystal")) { // ô¥×âí®Ý¬Íú "request memory crystal"
        if (pc.getInventory().checkItem(3000200, 1) && pc.getInventory().checkItem(40308, 20000)) { // Ëþ?èÌÊ«îÜÍ·ðíãÀÜúêóðë?îÜî§Öù
            pc.getInventory().consumeItem(3000200, 1); // á¼ÙÄ1ËÁID?3000200îÜÚªù¡
        pc.getInventory().consumeItem(40308, 20000); // á¼ÙÄ20,000ËÁID?40308îÜÚªù¡
        pc.getInventory().storeItem(700022, 1); // ÐååøèÌÊ«1ËÁID?700022îÜÚªù¡£¨ÑÀåãüªî÷â©ïÜ£©
        pc.sendPackets(new S_SystemMessage("ð²íÂèÇà÷¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±ð²íÂèÇà÷
        htmlid = ""; // ?ÍöHTML ID
        } else { // åýÍýî§ÖùÜôðë
            pc.sendPackets(new S_SystemMessage("Üôðë?îÜä¹Ó¡ÑÖ£¨20,000£©ûäÑÀåãáïø¸£¨1£©ËÁ¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
        }

    }
// } else if (npcid == 50045) { //¸ù¼¶ ÇìÀÌÆ® ¸®´º¾ó
// åýÍýNPCîÜIDãÀ50045£¨ÙÓÓöê³ùÏÌÚãæ£©
//        if (s.equalsIgnoreCase("a")) { // ÀÔÀå
// ô¥×âí®Ý¬Íú "a"£¨òäìý£©
//        if (pc.getInventory().checkItem(810000, 1)) { // Ëþ?èÌÊ«îÜÍ·ðíãÀÜúêó1ËÁID?810000îÜÚªù¡
//       pc.getInventory().consumeItem(810000, 1); // á¼ÙÄ1ËÁID?810000îÜÚªù¡
//        pc.start_teleport(32800, 32798, 1935, pc.getHeading(), 18339, true, false); // îîáêèÌÊ«Óðò¦ïÒîÜñ¨øö£¨32800, 32798, 1935£©
//        } else { // åýÍýî§ÖùÜôðë
//        pc.sendPackets(new S_SystemMessage("òäìýâÍé©Ô¼ÊÇâ®ãêîüîÜ?ãµ¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±âÍé©?ãµ
//       }
//        } else if (s.equalsIgnoreCase("b")) { // ±â¶õ¸¶À»
// ô¥×âí®Ý¬Íú "b"£¨îîáêÓðÐñÕµõ½£©
        pc.start_teleport(33436, 32799, 4, pc.getHeading(), 18339, true, false); // îîáêèÌÊ«ÓðÐñÕµõ½£¨33436, 32799, 4£©
        }
            }else if (npcid == 21015) { //Àº±â»ç ´øÀü ÀÔÀå °Ô¶óµå  Team The Day by.Áêµå
                if (s.equalsIgnoreCase("a")) {//Àº±â»ç ´øÀü 1Ãþ ÀÔÀå
                    if (pc.getLevel() >= 80 & pc.getLevel() <= 84) {
                        if (pc.getInventory().checkItem(4200255,1) && pc.getInventory().checkItem(40308, 15000)) {//Àº±â»ç ´øÀü ÀÔÀå±Ç(3½Ã°£)
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4200256,1) && pc.getInventory().checkItem(40308, 15000)) {//Àº±â»ç ´øÀü ÀÔÀå±Ç(30ÀÏ)
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4100121,1) && pc.getInventory().checkItem(40308, 15000)) {//°í±Þ ºÒ¸êÀÇ °¡È£
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        } else {
                            htmlid = "hanggelf";
                        }
                    }
                } else if (s.equalsIgnoreCase("b")) { // Àº±â»ç ´øÀü 2Ãþ ÀÔÀå
                    if (pc.getLevel() >= 80 & pc.getLevel() <= 92) {
                        if (pc.getInventory().checkItem(4200255,1) && pc.getInventory().checkItem(40308, 15000)) {//Àº±â»ç ´øÀü ÀÔÀå±Ç(3½Ã°£)
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32769, 32759, 7532, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4200256,1) && pc.getInventory().checkItem(40308, 15000)) {//Àº±â»ç ´øÀü ÀÔÀå±Ç(30ÀÏ)
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32769, 32759, 7532, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4100121,1) && pc.getInventory().checkItem(40308, 15000)) {//°í±Þ ºÒ¸êÀÇ °¡È£
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        } else {
                            htmlid = "hanggelf";
                        }
                    }
                } else if (s.equalsIgnoreCase("c")) { // Àº±â»ç ´øÀü 3Ãþ ÀÔÀå
                    if (pc.getLevel() >= 80 & pc.getLevel() <= 92) {
                        if (pc.getInventory().checkItem(4200255,1) && pc.getInventory().checkItem(40308, 15000)) {//Àº±â»ç ´øÀü ÀÔÀå±Ç(3½Ã°£)
                            pc.getInventory().consumeItem(40308, 20000);
                            pc.start_teleport(32791, 32857, 7533, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4200256,1) && pc.getInventory().checkItem(40308, 20000)) {//Àº±â»ç ´øÀü ÀÔÀå±Ç(30ÀÏ)
                            pc.getInventory().consumeItem(40308, 20000);
                            pc.start_teleport(32791, 32857, 7533, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4100121,1) && pc.getInventory().checkItem(40308, 15000)) {//°í±Þ ºÒ¸êÀÇ °¡È£
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        } else {
                            htmlid = "hanggelf";
                        }
                    }
                } else if (s.equalsIgnoreCase("d")) { // Àº±â»ç ´øÀü 4Ãþ ÀÔÀå
                    if (pc.getLevel() >= 80 & pc.getLevel() <= 92) {
                        if (pc.getInventory().checkItem(4200255,1) && pc.getInventory().checkItem(40308, 15000)) {//Àº±â»ç ´øÀü ÀÔÀå±Ç(3½Ã°£)
                            pc.getInventory().consumeItem(40308, 20000);
                            pc.start_teleport(32860, 32760, 7534, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4200256,1) && pc.getInventory().checkItem(40308, 20000)) {//Àº±â»ç ´øÀü ÀÔÀå±Ç(30ÀÏ)
                            pc.getInventory().consumeItem(40308, 20000);
                            pc.start_teleport(32860, 32760, 7534, pc.getHeading(), 18339, true, false);
                        }else if (pc.getInventory().checkItem(4100121,1) && pc.getInventory().checkItem(40308, 15000)) {//°í±Þ ºÒ¸êÀÇ °¡È£
                            pc.getInventory().consumeItem(40308, 15000);
                            pc.start_teleport(32811, 32801, 7531, pc.getHeading(), 18339, true, false);
                        } else {
                            htmlid = "hanggelf";
                        }
                    }
                }
            } else if (npcid == 7200000) { // ¸ù¼¶ ¿¡Å²½º
                if (s.equalsIgnoreCase("d")) {
                    if (pc.getInventory().checkItem(3000215, 1) && pc.getInventory().checkItem(1000004, 1)) {
                        pc.getInventory().consumeItem(3000215, 1);
                        pc.getInventory().consumeItem(1000004, 1);
                        pc.getInventory().storeItem(810010, 8);
                        addQuestExp(pc, 4);
                    } else {
                        htmlid = "ekins5";
                    }
                } else if (s.equalsIgnoreCase("c")) { // ÃË¸Å: À¯´ÏÄÜÀÇ ¼ºÀå Â¡Ç¥
                    if (pc.getInventory().checkItem(3000215, 1)) {
                        pc.getInventory().consumeItem(3000215, 1);
                        pc.getInventory().storeItem(810010, 5);
                        addQuestExp(pc, 4);
                    } else {
                        htmlid = "ekins5";
                    }
                } else if (s.equalsIgnoreCase("b")) { // ÃË¸Å: ¼ºÀåÀÇ ±¸½½
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
                } else if (s.equalsIgnoreCase("a")) { // ÃË¸Å: ¼ºÀåÀÇ ±¸½½ Á¶°¢
                    if (pc.getInventory().checkItem(810001, 1)) {
                        pc.getInventory().consumeItem(810001, 1);
                        pc.getInventory().storeItem(810010, 1);
                        addQuestExp(pc, 4);
                    } else {
                        htmlid = "ekins5";
                    }
                }
        /** ÇÏ¹öÆ® **/
        /** ùëÛ×÷å **/
 } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70641) { // åýÍýNPCîÜIDãÀ70641
     if (s.equalsIgnoreCase("a")) { // ô¥×âí®Ý¬Íú "a"
         if (pc.getInventory().checkItem(40395, 1) // ¼ö·æºñ´Ã
        // â©×£ñý×÷
        && pc.getInventory().checkItem(410061, 10) // ¸¶¹°ÀÇ±â¿î

        // ØªÚªîÜÑ¨ãÓ
        && pc.getInventory().checkItem(820004, 300)) { // ¸¶·ÂÀÇ½ÇÅ¸·¡
             // ØªÕôîÜÞêàÊ

        pc.getInventory().consumeItem(40395, 1); // á¼ÙÄ1ËÁID?40395îÜÚªù¡
        pc.getInventory().consumeItem(410061, 10); // á¼ÙÄ10ËÁID?410061îÜÚªù¡
        pc.getInventory().consumeItem(820004, 300); // á¼ÙÄ300ËÁID?820004îÜÚªù¡
        pc.getInventory().storeItem(20273, 1); // ÐååøèÌÊ«1ËÁID?20273îÜÚªù¡£¨ØªÕôñýâ¢÷ß£©
        htmlid = ""; // ?ÍöHTML ID
         } else { // åýÍýî§ÖùÜôðë
             pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("âÍé©£ºâ©×£ñý×÷(1)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÚªîÜÑ¨ãÓ(10)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÕôîÜÞêàÊ(300)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´
         }
        } else if (s.equalsIgnoreCase("b")) { // ô¥×âí®Ý¬Íú "b"
        if (pc.getInventory().checkEnchantItem(20273, 7, 1) // +7 ¸¶·ÂÀÇÀå°©
        // +7 ØªÕôñýâ¢÷ß
        && pc.getInventory().checkItem(40395, 1) // ¼ö·æºñ´Ã
        // â©×£ñý×÷
        && pc.getInventory().checkItem(410061, 10) // ¸¶¹°ÀÇ±â¿î
        // ØªÚªîÜÑ¨ãÓ
        && pc.getInventory().checkItem(820004, 300) // ¸¶·ÂÀÇ½ÇÅ¸·¡
        // ØªÕôîÜÞêàÊ
        && pc.getInventory().checkItem(820005, 1)) { // ¸¶·ÂÀÇÇÙ
        // ØªÕôîÜú·ãý

        pc.getInventory().consumeEnchantItem(20273, 7, 1); // á¼ÙÄ1ËÁ+7îÜID?20273îÜÚªù¡
        pc.getInventory().consumeItem(40395, 1); // á¼ÙÄ1ËÁID?40395îÜÚªù¡
        pc.getInventory().consumeItem(410061, 10); // á¼ÙÄ10ËÁID?410061îÜÚªù¡
        pc.getInventory().consumeItem(820004, 300); // á¼ÙÄ300ËÁID?820004îÜÚªù¡
        pc.getInventory().consumeItem(820005, 1); // á¼ÙÄ1ËÁID?820005îÜÚªù¡
        pc.getInventory().storeItem(20274, 1); // ÐååøèÌÊ«1ËÁID?20274îÜÚªù¡£¨ÎÃýÊîÜØªÕôñýâ¢÷ß£©
        htmlid = ""; // ?ÍöHTML ID
        } else { // åýÍýî§ÖùÜôðë
            pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
        pc.sendPackets(new S_SystemMessage("âÍé©£ºâ©×£ñý×÷(1)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÚªîÜÑ¨ãÓ(10)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÕôîÜÞêàÊ(300)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´
        pc.sendPackets(new S_SystemMessage("âÍé©£ºØªÕôîÜú·ãý(1)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´
        pc.sendPackets(new S_SystemMessage("âÍé©£º+7 ØªÕôñýâ¢÷ß(1)")); // ú¾èÌÊ«Û¡áêÎýô÷îÜî§ÖùâÍÏ´
        }
            } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71180) { // Á¦ÀÌÇÁ
                if (s.equalsIgnoreCase("A")) { // ²Þ²Ù´Â °õÀÎÇü
                    if (pc.getInventory().consumeItem(49026, 1000)) {
                        pc.getInventory().storeItem(41093, 1);
                        htmlid = "jp6";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("B")) { // Çâ¼ö
                    if (pc.getInventory().consumeItem(49026, 5000)) {
                        pc.getInventory().storeItem(41094, 1);
                        htmlid = "jp6";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("C")) { // µå·¹½º
                    if (pc.getInventory().consumeItem(49026, 10000)) {
                        pc.getInventory().storeItem(41095, 1);
                        htmlid = "jp6";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("D")) { // ¹ÝÁö
                    if (pc.getInventory().consumeItem(49026, 100000)) {
                        pc.getInventory().storeItem(41096, 1);
                        htmlid = "jp6";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("E")) { // À§ÀÎÀü
                    if (pc.getInventory().consumeItem(49026, 1000)) {
                        pc.getInventory().storeItem(41098, 1);
                        htmlid = "jp8";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("F")) { // ¼¼·ÃµÈ ¸ðÀÚ
                    if (pc.getInventory().consumeItem(49026, 5000)) {
                        pc.getInventory().storeItem(41099, 1);
                        htmlid = "jp8";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("G")) { // ÃÖ°í±Þ ¿ÍÀÎ
                    if (pc.getInventory().consumeItem(49026, 10000)) {
                        pc.getInventory().storeItem(41100, 1);
                        htmlid = "jp8";
                    } else {
                        htmlid = "jp5";
                    }
                } else if (s.equalsIgnoreCase("H")) { // ¾Ë ¼ö ¾ø´Â ¿­¼è
                    if (pc.getInventory().consumeItem(49026, 100000)) {
                        pc.getInventory().storeItem(41101, 1);
                        htmlid = "jp8";
                    } else {
                        htmlid = "jp5";
                    }
                }

                // Á¶¿ìÀÇ ºÒ°ñ·½ ¸®´º¾ó
            } else if (npcid == 5066) {
                int enchant = 0;
                int itemId = 0;
                int oldArmor = 0;
                L1NpcInstance npc = (L1NpcInstance) obj;
                String npcName = npc.getNpcTemplate().get_name();
                if (s.equalsIgnoreCase("1")) { // [+7]¸¶·ÂÀÇ ´Ü°Ë
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
                        pc.getInventory().consumeItem(40308, 5000000); // á¼ÙÄ500Ø¿ËÁID?40308îÜÚªù¡£¨ä¹Ó¡ÑÖ£©
        Û¡Û¯?ûùÚªù¡(pc, 602, 1, 7); // ÐååøèÌÊ«1ËÁID?602îÜÚªù¡?íâÐì?ûùò¸+7
        htmlid = ""; // ?ÍöHTML ID
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë¡£")); // ú¾èÌÊ«Û¡áêÍ§÷Öá¼ãÓ£¬÷×ò±î§ÖùÜôðë
                    }
                } else if (s.equalsIgnoreCase("2")) {// [+8]¸¶·ÂÀÇ ´Ü°Ë
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
                        Û¡Û¯?ûùÚªù¡(pc, 602, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }
                } else if (s.equalsIgnoreCase("3")) {// [+7]È¯¿µÀÇ Ã¼ÀÎ¼Òµå
                    if ((pc.getInventory().checkEnchantItem(500, 8, 1) || pc.getInventory().checkEnchantItem(501, 8, 1))
                            && pc.getInventory().checkItem(40308, 5000000)) {
                        if (pc.getInventory().consumeEnchantItem(500, 8, 1)
                                || pc.getInventory().consumeEnchantItem(501, 8, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(40308, 5000000);
                        Û¡Û¯?ûùÚªù¡(pc, 202001, 1, 7);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }
                } else if (s.equalsIgnoreCase("4")) {// [+8]È¯¿µÀÇ Ã¼ÀÎ¼Òµå
                    if ((pc.getInventory().checkEnchantItem(500, 9, 1) || pc.getInventory().checkEnchantItem(501, 9, 1))
                            && pc.getInventory().checkItem(40308, 10000000)) {
                        if (pc.getInventory().consumeEnchantItem(500, 9, 1)
                                || pc.getInventory().consumeEnchantItem(501, 9, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(40308, 10000000);
                        Û¡Û¯?ûùÚªù¡(pc, 202001, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }
                } else if (s.equalsIgnoreCase("5")) {// [+7]°ø¸íÀÇ Å°¸µÅ©
                    if ((pc.getInventory().checkEnchantItem(503, 8, 1) || pc.getInventory().checkEnchantItem(504, 8, 1))
                            && pc.getInventory().checkItem(40308, 5000000)) {
                        if (pc.getInventory().consumeEnchantItem(503, 8, 1)
                                || pc.getInventory().consumeEnchantItem(504, 8, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(40308, 5000000);
                        Û¡Û¯?ûùÚªù¡(pc, 1135, 1, 7);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }
                } else if (s.equalsIgnoreCase("6")) {// [+8]°ø¸íÀÇ Å°¸µÅ©
                    if ((pc.getInventory().checkEnchantItem(503, 9, 1) || pc.getInventory().checkEnchantItem(504, 9, 1))
                            && pc.getInventory().checkItem(40308, 10000000)) {
                        if (pc.getInventory().consumeEnchantItem(503, 9, 1)
                                || pc.getInventory().consumeEnchantItem(504, 9, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(40308, 10000000);
                        Û¡Û¯?ûùÚªù¡(pc, 1135, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }
                } else if (s.equalsIgnoreCase("7")) {// [+7]ÆÄ±«ÀÇ Å©·Î¿ì
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
                        Û¡Û¯?ûùÚªù¡(pc, 1124, 1, 7);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }
                } else if (s.equalsIgnoreCase("8")) {// [+8]ÆÄ±«ÀÇ Å©·Î¿ì
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
                        Û¡Û¯?ûùÚªù¡(pc, 1124, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }
                } else if (s.equalsIgnoreCase("9")) {// [+7]ÆÄ±«ÀÇ ÀÌµµ·ù
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
                        Û¡Û¯?ûùÚªù¡(pc, 1125, 1, 7);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }
                } else if (s.equalsIgnoreCase("10")) {// [+8]ÆÄ±«ÀÇ ÀÌµµ·ù
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
                        Û¡Û¯?ûùÚªù¡(pc, 1125, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }
                } else if (s.equalsIgnoreCase("11")) {// [+0]Á¦·Î½ºÀÇ ÁöÆÎÀÌ
                    if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 9, 1)
                            && pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
                        pc.getInventory().consumeEnchantItem(119, 5, 1);
                        pc.getInventory().consumeEnchantItem(121, 9, 1);
                        pc.getInventory().consumeItem(700077, 1);
                        pc.getInventory().consumeItem(41246, 100000);
                        pc.getInventory().storeItem(202003, 1);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }

                } else if (s.equalsIgnoreCase("12")) {// [+8]Á¦·Î½ºÀÇ ÁöÆÎÀÌ
                    if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 10, 1)
                            && pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
                        pc.getInventory().consumeEnchantItem(119, 5, 1);
                        pc.getInventory().consumeEnchantItem(121, 10, 1);
                        pc.getInventory().consumeItem(700077, 1);
                        pc.getInventory().consumeItem(41246, 100000);
                        Û¡Û¯?ûùÚªù¡(pc, 202003, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }
                } else if (s.equalsIgnoreCase("13")) {// [+9]Á¦·Î½ºÀÇ ÁöÆÎÀÌ
                    if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 11, 1)
                            && pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
                        pc.getInventory().consumeEnchantItem(119, 5, 1);
                        pc.getInventory().consumeEnchantItem(121, 11, 1);
                        pc.getInventory().consumeItem(700077, 1);
                        pc.getInventory().consumeItem(41246, 100000);
                        Û¡Û¯?ûùÚªù¡(pc, 202003, 1, 9);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }

                } else if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") // ÆÇ±Ý
                        || s.equals("E") || s.equals("F") || s.equals("G") || s.equals("H") // ºñ´Ã
                        || s.equals("I") || s.equals("J") || s.equals("K") || s.equals("L") // °¡Á×
                        || s.equals("M") || s.equals("N") || s.equals("O") || s.equals("P")) { // ·Îºê
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
                        pc.getInventory().consumeItem(41246, 100000); // ¿ëÇØÁ¦
                        pc.getInventory().consumeItem(oldArmor, 1); // °í´ëÀÇ
                        createNewItem(pc, npcName, itemId, 1, enchant - 7);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                    }
                } else if (s.equals("a")) {// []ÁúÇ³ÀÇµµ³¢
                    if ((pc.getInventory().checkEnchantItem(605, 8, 1)) && pc.getInventory().checkItem(41246, 100000)) {
                        if (pc.getInventory().consumeEnchantItem(605, 8, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 100000);
                        Û¡Û¯?ûùÚªù¡(pc, 203015, 1, 0);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("âÍé© +8 ÎÊù¦ñýÝ¨ûú 100,000 ËÁÌ¿ïÜô÷."));
                    }
                } else if (s.equals("b")) {// [+8]ÁúÇ³ÀÇµµ³¢
                    if ((pc.getInventory().checkEnchantItem(605, 9, 1)) && pc.getInventory().checkItem(41246, 100000)) {
                        if (pc.getInventory().consumeEnchantItem(605, 9, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 100000);
                        Û¡Û¯?ûùÚªù¡(pc, 203015, 1, 8);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("âÍé© +9 ÎÊù¦ñýÝ¨ûú 100,000 ËÁÌ¿ïÜô÷."));
                    }
                } else if (s.equals("c")) {// [+9]ÁúÇ³ÀÇµµ³¢
                    if ((pc.getInventory().checkEnchantItem(605, 10, 1))
                            && pc.getInventory().checkItem(41246, 100000)) {
                        if (pc.getInventory().consumeEnchantItem(605, 10, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 100000);
                        Û¡Û¯?ûùÚªù¡(pc, 203015, 1, 9);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("âÍé© +10 ÎÊù¦ñýÝ¨ûú 100,000 ËÁÌ¿ïÜô÷."));
                    }
                } else if (s.equals("d")) {// []¸¶¹°ÀÇµµ³¢
                    if ((pc.getInventory().checkEnchantItem(151, 0, 1)) && pc.getInventory().checkItem(41246, 200000)) {
                        if (pc.getInventory().consumeEnchantItem(151, 0, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 200000);
                        Û¡Û¯?ûùÚªù¡(pc, 203016, 1, 0);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("+0 äÂØªñýÝ¨£¬âÍé© 100,000 ËÁÌ¿ïÜô÷¡£"));
                    }
                } else if (s.equals("e")) { // [+1] ØªÚªñýÝ¨
                    if ((pc.getInventory().checkEnchantItem(151, 3, 1)) && pc.getInventory().checkItem(41246, 200000)) {
                        if (pc.getInventory().consumeEnchantItem(151, 3, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 200000);
                        Û¡Û¯?ûùÚªù¡(pc, 203016, 1, 1);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("+3 äÂØªñýÝ¨£¬âÍé© 100,000 ËÁÌ¿ïÜô÷¡£"));
                    }
                } else if (s.equals("f")) { // [+3] ØªÚªñýÝ¨
                    if ((pc.getInventory().checkEnchantItem(151, 5, 1)) && pc.getInventory().checkItem(41246, 200000)) {
                        if (pc.getInventory().consumeEnchantItem(151, 5, 1)) {
                            ;
                        }
                        pc.getInventory().consumeItem(41246, 200000);
                        Û¡Û¯?ûùÚªù¡(pc, 203016, 1, 3);
                        htmlid = "";
                    } else {
                        pc.sendPackets(new S_SystemMessage("+5 äÂØªñýÝ¨£¬âÍé© 100,000 ËÁÌ¿ïÜô÷¡£"));
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
                if (s.equals("a")) {// ÀÏ¹Ýº¸»ó
                    if (pc.getLevel() >= 52) {
                        if (pc.getInventory().checkItem(30151, 1)) {
                            pc.getInventory().consumeItem(30151, 1);
                            pc.getInventory().storeItem(30149, 1);
                            ÔõÐä 52 ?ÐññÞîÜ?ÕäÌèúÐ?(pc, 1);
                            htmlid = "anold3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                            htmlid = "anold4";
                        }
                    } else {
                        htmlid = "anold2";
                    }
                } else if (s.equals("b")) {// Æ¯º°ÇÑº¸»ó
                    if (pc.getLevel() >= 52) {
                        if (pc.getInventory().checkItem(30151, 1) && pc.getInventory().checkItem(1000004, 1)) {
                            pc.getInventory().consumeItem(30151, 1);
                            pc.getInventory().consumeItem(1000004, 1);
                            pc.getInventory().storeItem(30149, 1);
                            ÔõÐä 52 ?ÐññÞîÜ?ÕäÌèúÐ?(pc, 2);
                            htmlid = "anold3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                            htmlid = "anold4";
                        }
                    } else {
                        htmlid = "anold2";
                    }
                } else if (s.equals("c")) {// ºû³ª´Â Æ¯º°ÇÑº¸»ó
                    if (pc.getLevel() >= 52) {
                        if (pc.getInventory().checkItem(30151, 1) && pc.getInventory().checkItem(1000007, 1)) {
                            pc.getInventory().consumeItem(30151, 1);
                            pc.getInventory().consumeItem(1000007, 1);
                            pc.getInventory().storeItem(30149, 1);
                            ÔõÐä 52 ?ÐññÞîÜ?ÕäÌèúÐ?(pc, 3);
                            htmlid = "anold3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                            htmlid = "anold4";
                        }
                    } else {
                        htmlid = "anold2";
                    }
                }
                // ³ª·çÅÍ
            } else if (npcid == 9) {
                if (s.equals("a")) {// ÀÏ¹Ýº¸»ó
                    if (pc.getLevel() >= 30) {
                        if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)) {
                            pc.getInventory().consumeItem(9992, 5);
                            pc.getInventory().consumeItem(9993, 1);
                            pc.getInventory().storeItem(9994, 1);
                            ÔõÐä 52 ?ÐññÞîÜ?ÕäÌèúÐ?(pc, 1);
                            htmlid = "naruto3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                            htmlid = "naruto4";
                        }
                    } else {
                        htmlid = "naruto2";
                    }
                } else if (s.equals("b")) {// Æ¯º°ÇÑº¸»ó
                    if (pc.getLevel() >= 30) {
                        if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)
                                && pc.getInventory().checkItem(1000004, 1)) {
                            pc.getInventory().consumeItem(9992, 5);
                            pc.getInventory().consumeItem(9993, 1);
                            pc.getInventory().consumeItem(1000004, 1);
                            pc.getInventory().storeItem(9994, 1);
                            ÔõÐä 52 ?ÐññÞîÜ?ÕäÌèúÐ?(pc, 1);
                            htmlid = "naruto3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
                            htmlid = "naruto4";
                        }
                    } else {
                        htmlid = "naruto2";
                    }
                } else if (s.equals("c")) {// ºû³ª´Â Æ¯º°ÇÑº¸»ó
                    if (pc.getLevel() >= 30) {
                        if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)
                                && pc.getInventory().checkItem(1000007, 1)) {
                            pc.getInventory().consumeItem(9992, 5);
                            pc.getInventory().consumeItem(9993, 1);
                            pc.getInventory().consumeItem(1000007, 1);
                            pc.getInventory().storeItem(9994, 1);
                            ÔõÐä 52 ?ÐññÞîÜ?ÕäÌèúÐ?(pc, 1);
                            htmlid = "naruto3";
                        } else {
                            pc.sendPackets(new S_SystemMessage("ð²íÂÔ³ÎýÜôðë."));
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
                // ¾Ëµå¶õ
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

        /** Åõ¼®±â **/
/** ÷áà´Ñ¦ **/

        } else if (npcid == 7000082 || npcid == 7000083 || npcid == 7000084 || npcid == 7000085 || npcid == 7000086
        || npcid == 7000087) {
        if (s.equalsIgnoreCase("0-5") // ¿Ü¼º¹® ¹æÇâÀ¸·Î ¹ß»ç!
        // ú¾èâàòÚ¦Û°ú¾Û¡ÞÒ!
        || s.equalsIgnoreCase("0-6") // ³»¼º¹® ¹æÇâÀ¸·Î ¹ß»ç!
        // ú¾Ò®àòÚ¦Û°ú¾Û¡ÞÒ!
        || s.equalsIgnoreCase("0-7") // ¼öÈ£Å¾ ¹æÇâÀ¸·Î ¹ß»ç!
        // ú¾áúûÞ÷²Û°ú¾Û¡ÞÒ!
        || s.equalsIgnoreCase("1-16") // ¿Ü¼º¹® ¹æÇâÀ¸·Î Ä§¹¬Æ÷Åº ¹ß»ç!
        // ú¾èâàòÚ¦Û°ú¾Û¡ÞÒ?Ùù?÷¥!
        || s.equalsIgnoreCase("1-17") // ³»¼º¹® ¾ÕÂÊÀ¸·Î Ä§¹¬Æ÷Åº ¹ß»ç!
        // ú¾Ò®àòÚ¦îñÛ°Û¡ÞÒ?Ùù?÷¥!
        || s.equalsIgnoreCase("1-18") // ³»¼º¹® ÁÂÃøÀ¸·Î Ä§¹¬Æ÷Åº ¹ß»ç!
        // ú¾Ò®àòÚ¦ñ§ö°Û¡ÞÒ?Ùù?÷¥!
        || s.equalsIgnoreCase("1-19") // ³»¼º¹® ¿ìÃøÀ¸·Î Ä§¹¬Æ÷Åº ¹ß»ç!
        // ú¾Ò®àòÚ¦éÓö°Û¡ÞÒ?Ùù?÷¥!
        || s.equalsIgnoreCase("1-20") // ¼öÈ£Å¾ ¹æÇâÀ¸·Î Ä§¹¬Æ÷Åº ¹ß»ç!
        // ú¾áúûÞ÷²Û°ú¾Û¡ÞÒ?Ùù?÷¥!
        // ¼ö¼º
        // áúàò
        || s.equalsIgnoreCase("0-9") // ¿Ü¼º¹® ¹æÇâÀ¸·Î ¹ß»ç!
        // ú¾èâàòÚ¦Û°ú¾Û¡ÞÒ!
        ) {
        // îÏ×ëÊ¦ì¤ôÕÊ¥ßÓëëîÜÓÛØ§ÕÎô¥×âÜôÔÒîÜÛ¡ÞÒàÔú£
                            }
                        }
                        ) {
                        int locx = 0;
                        int locy = 0;
                        int gfxid = 0;
                            int castleid = 0;
                            int npcId = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
                            if (s.equalsIgnoreCase("0-5")) { // ¿Ü¼º¹® ¹æÇâÀ¸·Î ¹ß»ç!
                                // ú¾èâàòÚ¦Û°ú¾Û¡ÞÒ!
                            switch (npcId) {
                                case 7000086: // 5½Ã ¹æÇâ °ø¼º ¿ÀÅ©¿ä»õ °ø¼ºÃø
                    // 5ïÇÛ°ú¾Íôàò£¬çóÐºé©ßÝÍôàòö°
                            locx = 32795;
                            locy = 32315;
                            gfxid = 12197; // ¿ìÃø
                    // éÓö°
                            castleid = 2;
                            break;
                            case 7000082: // 5½Ã ¹æÇâ °ø¼º ±â¶õ¼º °ø¼ºÃø
                    // 5ïÇÛ°ú¾Íôàò£¬ÐôÕµàòÍôàòö°
                            locx = 33632;
                            locy = 32731;
                            gfxid = 12197; // ¿ìÃø
                    // éÓö°
                            castleid = 15482;
                            break;
                            case 7000084: // 7½Ã ¹æÇâ °ø¼º ÄËÆ®¼º °ø¼ºÃø
                    // 7ïÇÛ°ú¾Íôàò£¬Ðé÷åàòÍôàòö°
                            locx = 33114;
                            locy = 32771;
                            gfxid = 12193; // ÁÂÃø
                    // ñ§ö°
                            castleid = 1;
                            break;
                            }
                    } else if (s.equalsIgnoreCase("0-6")) { // ³»¼º¹® ¹æÇâÀ¸·Î ¹ß»ç!
                        switch (npcId) {
                            case 7000086: // 11½Ã ¹æÇâ °ø¼º ¿ÀÅ©¿ä»õ °ø¼ºÃø
                                locx = 32798;
                                locy = 32268;
                                gfxid = 12197; // ¿ìÃø
                                castleid = 2;
                                break;
                            case 7000082: // 11½Ã ¹æÇâ °ø¼º ±â¶õ¼º °ø¼ºÃø
                                locx = 33632;
                                locy = 32664;
                                gfxid = 12197; // ¿ìÃø
                                castleid = 15482;
                                break;
                            case 7000084: // 2½Ã ¹æÇâ °ø¼º ÄËÆ®¼º °ø¼ºÃø
                                locx = 33171;
                                locy = 32763;
                                gfxid = 12197; // ÁÂÃø
                                castleid = 1;
                                break;
                        }
                    } else if (s.equalsIgnoreCase("0-7")) { // ¼öÈ£Å¾ ¹æÇâÀ¸·Î ¹ß»ç!
                        switch (npcId) {
                            case 7000086: // 11½Ã ¹æÇâ °ø¼º ¿ÀÅ©¿ä»õ °ø¼ºÃø
                                locx = 32798;
                                locy = 32285;
                                gfxid = 12197; // ¿ìÃø
                                castleid = 2;
                                break;
                            case 7000082: // 11½Ã ¹æÇâ °ø¼º ±â¶õ¼º °ø¼ºÃø
                                locx = 33631;
                                locy = 32678;
                                gfxid = 12197; // ¿ìÃø
                                castleid = 15482;
                                break;
                            case 7000084: // 2½Ã ¹æÇâ °ø¼º ÄËÆ®¼º °ø¼ºÃø
                                locx = 33168;
                                locy = 32779;
                                gfxid = 12197; // ÁÂÃø
                                castleid = 1;
                                break;
                        }
                    } else if (s.equalsIgnoreCase("0-9")) { // ¿Ü¼º¹® ¹æÇâÀ¸·Î ¹ß»ç!
                        int pcCastleId = 0;
                        if (pc.getClanid() != 0) {
                            L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
                            if (clan != null) {
                                pcCastleId = clan.getCastleId();
                            }
                        }
                        switch (npcId) {
                            case 7000087: // 11½Ã ¹æÇâ °ø¼º ¿ÀÅ©¿ä»õ ¼ö¼ºÃø
                                if (isExistDefenseClan(L1CastleLocation.OT_CASTLE_ID)) {
                                    if (pcCastleId != L1CastleLocation.OT_CASTLE_ID) {
                                        pc.sendPackets(new S_ServerMessage(3682));
                                        // Åõ¼®±â »ç¿ë: ½ÇÆÐ(¼ºÀ» ¼öÈ£ÇÏ´Â ¼ºÇ÷ ±ºÁÖ¸¸ »ç¿ë °¡´É)
                                        return htmlid;
                                    }
                                }
                                locx = 32794;
                                locy = 32320;
                                gfxid = 12193; // ¿ìÃø
                                castleid = 2;
                                break;
                            case 7000083: // 11½Ã ¹æÇâ °ø¼º ±â¶õ¼º ¼ö¼ºÃø
                                if (isExistDefenseClan(L1CastleLocation.GIRAN_CASTLE_ID)) {
                                    if (pcCastleId != L1CastleLocation.GIRAN_CASTLE_ID) {
                                        pc.sendPackets(new S_ServerMessage(3682));
                                        // Åõ¼®±â »ç¿ë: ½ÇÆÐ(¼ºÀ» ¼öÈ£ÇÏ´Â ¼ºÇ÷ ±ºÁÖ¸¸ »ç¿ë °¡´É)
                                        return htmlid;
                                    }
                                }
                                locx = 33631;
                                locy = 32738;
                                gfxid = 12193; // ¿ìÃø
                                castleid = 15482;
                                break;
                            case 7000085: // 2½Ã ¹æÇâ °ø¼º ÄËÆ®¼º ¼ö¼ºÃø
                                if (isExistDefenseClan(L1CastleLocation.KENT_CASTLE_ID)) {
                                    if (pcCastleId != L1CastleLocation.KENT_CASTLE_ID) {
                                        pc.sendPackets(new S_ServerMessage(3682));
                                        // Åõ¼®±â »ç¿ë: ½ÇÆÐ(¼ºÀ» ¼öÈ£ÇÏ´Â ¼ºÇ÷ ±ºÁÖ¸¸ »ç¿ë °¡´É)
                                        return htmlid;
                                    }
                                }
                                locx = 33107;
                                locy = 32770;
                                gfxid = 12197; // ¿ìÃø
                                castleid = 1;
                                break;
                        }

        // Û¡ÞÒ?Ùù?÷¥îÜÜôÔÒàÔú£
/*
* <a action="1-16">èâàòÚ¦Û°ú¾Û¡ÞÒ?Ùù?÷¥!</a><br> <a action="1-17">Ò®àòÚ¦îñÛ°Û¡ÞÒ?Ùù?÷¥!</a><br>
* <a action="1-18">Ò®àòÚ¦ñ§ö°Û¡ÞÒ?Ùù?÷¥!</a><br> <a action="1-19">Ò®àòÚ¦éÓö°Û¡ÞÒ?Ùù?÷¥!</a><br>
* <a action="1-20">áúûÞ÷²Û°ú¾Û¡ÞÒ?Ùù?÷¥!</a><br><br>
} else if (s.equalsIgnoreCase("0-9")) {
    // èâàòÚ¦Û°ú¾Û¡ÞÒ?Ùù?÷¥!
*/

        } else {
        pc.sendPackets(new S_SystemMessage("?Ùù?÷¥ãÀÙíÛöÞÅéÄîÜ¡£"));
        // Û¡áêÍ§÷Öá¼ãÓ£º?Ùù?÷¥ãÀÙíÛöÞÅéÄîÜ¡£
        return htmlid;
        }

        boolean isNowWar = false;
        isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleid);
        if (!isNowWar) {
        pc.sendPackets(new S_ServerMessage(3683));
        // Û¡áêÜ×ÙâÐïá¼ãÓ£º÷áà´Ñ¦ÞÅéÄã÷ø¨£¨ÐÁÒöî¤ÍôàòãÁÊàÞÅéÄ£©
        return htmlid;
        }

        boolean inWar = MJWar.isNowWar(pc.getClan());
        if (!(pc.isCrown() && inWar && isNowWar)) {
        pc.sendPackets(new S_ServerMessage(3681));
        // Û¡áêÜ×ÙâÐïá¼ãÓ£º÷áà´Ñ¦ÞÅéÄã÷ø¨£¨ÐÁà¾îúîÜÏÖñ«Ê¦ì¤ÞÅéÄ£©
        return htmlid;
        }

        if (pc.getlastShellUseTime() + 10000L > System.currentTimeMillis()) {
        pc.sendPackets(new S_ServerMessage(3680));
        // Û¡áêÜ×ÙâÐïá¼ãÓ£º÷áà´Ñ¦ÞÅéÄã÷ø¨£¨âÍé©ñìãæíû?ãÁÊà£©
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
                                // Ä§¹¬Æ÷Åº(locx, locy); // Ä§¹¬Æ÷Åº Å×½ºÆ®
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

    private static boolean ºÀÀÎÅÛ(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr,
                               boolean identi) {
        // ºÀÀÎÅÛ(pc, 5000045, 1, 5, 128);
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
            } else { // °¡Áú ¼ö ¾ø´Â °æ¿ì´Â Áö¸é¿¡ ¶³¾î¶ß¸®´Â Ã³¸®ÀÇ Äµ½½Àº ÇÏÁö ¾Ê´Â´Ù(ºÎÁ¤ ¹æÁö)
                pc.sendPackets(new S_ServerMessage(82));
                // ¹«°Ô °ÔÀÌÁö°¡ ºÎÁ·ÇÏ°Å³ª ÀÎº¥Åä¸®°¡ ²ËÂ÷¼­ ´õ µé ¼ö ¾ø½À´Ï´Ù.
                return false;
            }
            pc.sendPackets(new S_ServerMessage(403, item.getLogName())); //
            return true;
        } else {
            return false;
        }
    }

    private boolean Û¡Û¯?ûùÚªù¡(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
        L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
        if (item != null) {
            item.setCount(count);
            item.setEnchantLevel(EnchantLevel);
            item.setIdentified(true);
            if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
                pc.getInventory().storeItem(item);
            } else {
                pc.sendPackets(new S_ServerMessage(82));
                // ¹«°Ô °ÔÀÌÁö°¡ ºÎÁ·ÇÏ°Å³ª ÀÎº¥Åä¸®°¡ ²ËÂ÷¼­ ´õ µé ¼ö ¾ø½À´Ï´Ù.
                return false;
            }
            pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0¸¦
            // ¼Õ¿¡
            // ³Ö¾ú½À´Ï´Ù.
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

    private void ÔõÐä 52 ?ÐññÞîÜ?ÕäÌèúÐ?(L1PcInstance pc, int type) {
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
            // pc.sendPackets(new S_ChatPacket(pc, time + " ÃÊ ÈÄ »ç¿ë ÇÏ½Ã±æ ¹Ù¶ø´Ï´Ù."));
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

        int level = ExpTable.getLevelByExp(pc.get_exp()); // ÐÆËàÌèúÐ?üòö¢èÌÊ«ÔõÐä
        if (pc.getInventory().checkItem(itemid, 1)) { // Ëþ?èÌÊ«ãÀÜúêóò¦ïÒîÜÔ³Îý
            if (pc.getLevel() >= limitlvmin && pc.getLevel() < limitlvMax) { // Ëþ?èÌÊ«ÔõÐäãÀÜúî¤ëÃúÉÛôêÌÒ®
                if (level >= limitlvMax) {
                    pc.sendPackets("ÙíÛöî¢üòö¢ÌèúÐ?¡£"); // Û¡áêá¼ãÓ£ºÙíÛöî¢üòö¢ÌèúÐ?¡£
        return;
                } else {
                    pc.getInventory().consumeItem(itemid, 1); // á¼ÙÄìéËÁò¦ïÒÔ³Îý
        pc.add_exp(exp); // ñòÊ¥ÌèúÐ?
        pc.setCurrentHp(pc.getMaxHp()); // àâöÇÓ×îñßæÙ¤??õÌÓÞ?
        pc.setCurrentMp(pc.getMaxMp()); // àâöÇÓ×îñØªÛö??õÌÓÞ?
        pc.sendPackets(new S_SkillSound(pc.getId(), 3944)); // Û¡áêÐüÒöëåüùÐåèÌÊ«
        Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944)); // ÎÆ÷ëÐüÒöëåüù
                }
            } else {
                pc.sendPackets(String.format("%d Ðäì¤ß¾ %d Ðäì¤ù»Ê¦ÞÅéÄ¡£", limitlvmin, limitlvMax)); // Û¡áêá¼ãÓ£ºÐÁùÚåÚò¦ïÒÔõÐäÛôêÌÒ®ÞÅéÄ
        return;
            }
        } else {
            pc.sendPackets("ÙÒêóà÷íþÏç¡£"); // Û¡áêá¼ãÓ£ºÙÒêóà÷íþÏç
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

    private String À¯¸®¿¡(String s, L1PcInstance pc) {
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
