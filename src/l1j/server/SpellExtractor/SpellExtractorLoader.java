package l1j.server.SpellExtractor;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.MJPassiveSkill.MJPassiveUserLoader;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.C_LoginToServer;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.templates.L1Skills;

public class SpellExtractorLoader {
    private static SpellExtractorLoader _instance;

    public static SpellExtractorLoader getInstance() {
        if (_instance == null)
            _instance = new SpellExtractorLoader();
        return _instance;
    }

    public static void reload() {
        if (_instance != null) {
            _instance = new SpellExtractorLoader();
        }
    }

    private HashMap<Integer, SpellExtractorInfo> _info;

    private SpellExtractorLoader() {
        Infoload();
    }

    // 加載資料
    private void Infoload() {
        HashMap<Integer, SpellExtractorInfo> items = new HashMap<Integer, SpellExtractorInfo>();
        Selector.exec("select * from spell_extractor", new FullSelectorHandler() {
            @Override
            public void result(ResultSet rs) throws Exception {
                while (rs.next()) {
                    try {
                        SpellExtractorInfo Info = SpellExtractorInfo.newInstance(rs);
                        items.put(Info.getSkillId(), Info);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        _info = items;
    }

    // 獲取所有資訊
    public ArrayList<SpellExtractorInfo> values() {
        return new ArrayList<SpellExtractorInfo>(_info.values());
    }

    // 根據類型和技能 ID 獲取資訊
    public SpellExtractorInfo getSkillId(int type, int skillid) {
        ArrayList<SpellExtractorInfo> skills = values();
        if (skills == null)
            return null;

        for (SpellExtractorInfo pInfo : skills) {
            if (pInfo.getSkillType() == type) {
                if (pInfo.getSkillId() == skillid)
                    return pInfo;
            }
        }
        return null;
    }

    // 使用道具
    public boolean useItem(L1PcInstance pc, L1ItemInstance item, int type, int skillid) {
        if (item.getItem().getUseType() != 64)
            return false;

        if (type == 0) {
            skillid -= 1;
        }

        // 處理不同的技能 ID
        switch (skillid) {
            // XXX 其他
            case 137:
                skillid = 5001;
                break;
            case 138:
                skillid = 5002;
                break;
            case 139:
                skillid = 5003;
                break;
            case 140:
                skillid = 5004;
                break;
            case 141:
                skillid = 5005;
                break;
            case 153:
                skillid = 5017;
                break;
            case 248:
                skillid = 5112;
                break;
            case 249:
                skillid = 5113;
                break;
            case 250:
                skillid = 5114;
                break;
            case 251:
                skillid = 5115;
                break;
            case 252:
                skillid = 5159;
                break;
            case 253:
                skillid = 5117;
                break;
            // XXX 關於其他職業的處理
            case 187:
                skillid = 5051;
                break;
            case 188:
                skillid = 5052;
                break;
            case 189:
                skillid = 5053;
                break;
            case 190:
                skillid = 5054;
                break;
            case 191:
                skillid = 5055;
                break;
            case 192:
                skillid = 5056;
                break;
            // XXX 關於精靈
            case 38:
                skillid = 5158;
                break;
            // XXX 關於騎士
            case 37:
                skillid = 5157;
                break;
        }

        SpellExtractorInfo pInfo = getSkillId(type, skillid);
        if (pInfo == null) {
            if (pc.isGm()) {
                System.out.println(String.format("技能 ID：%d 的資訊不存在。", skillid));
                return false;
            }
        }

        if (Spell_Extractor(pc, pInfo, type))
            pc.getInventory().removeItem(item, 1);
        return true;
    }

    // 技能提取
    public boolean Spell_Extractor(L1PcInstance pc, SpellExtractorInfo info, int type) {
        if (type == 0) {
            if (!pc.isPassive(info.getSkillId())) {
                pc.sendPackets("未習得該技能。");
                return false;
            }
            if (!pc.getInventory().checkItem(4100718, 1)) {
                pc.sendPackets("缺少技能提取道具。");
                return false;
            }
            if (ItemTable.getInstance().getTemplate(info.getItemId()) == null) {
                if (pc.isGm()) {
                    pc.sendPackets("\\f3- 在 spell_extractor 表中找不到 item_id(列)：" + info.getItemId() + "。");
                    return false;
                } else {
                    pc.sendPackets("設定錯誤，請聯繫管理員。");
                    return false;
                }
            }

            pc.getInventory().storeItem(info.getItemId(), info.get_ItemCount());
            pc.sendPackets(7896, info.getSkillName());
            MJPassiveUserLoader.spellLost(pc, info.getSkillId());

            pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
            pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("請稍等片刻，技能將被移除。", MJSimpleRgb.green(), 5));
            pc.sendPackets("請稍等片刻，技能將被移除。");
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
            GeneralThreadPool.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    GameClient clnt = pc.getNetConnection();
                    String name = pc.getName();
                    int x = pc.getX();
                    int y = pc.getY();
                    int mapId = pc.getMapId();
                    C_NewCharSelect.restartProcess(pc);
                    try {
                        Thread.sleep(700L);
                        pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
                        C_LoginToServer.doEnterWorld(name, clnt, false, x, y, mapId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1500);
        } else {
            if (!pc.isSkillMastery(info.getSkillId())) {
                pc.sendPackets("未習得該技能。");
                return false;
            } else {
                if (pc.isTempSkillActive(info.getSkillId())) {
                    pc.sendPackets("無法提取暫時技能。");
                    return false;
                }
            }

            if (!pc.getInventory().checkItem(4100718, 1)) {
                pc.sendPackets("缺少技能提取道具。");
                return false;
            }
            if (ItemTable.getInstance().getTemplate(info.getItemId()) == null) {
                if (pc.isGm()) {
                    pc.sendPackets("\\f3- 在 spell_extractor 表中找不到 item_id(列)：" + info.getItemId() + "。");
                    return false;
                } else {
                    pc.sendPackets("設定錯誤，請聯繫管理員。");
                    return false;
                }
            }

            L1Skills l1skills = SkillsTable.getInstance().getTemplate(info.getSkillId());
            if (l1skills != null) {
                SkillsTable.getInstance().spellLost(pc.getId(), info.getSkillId());
                pc.getInventory().storeItem(info.getItemId(), info.get_ItemCount());
                pc.sendPackets(7896, info.getSkillName());
                SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
                noti.appendNewSpell(info.getSkillId(), false);
                pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
            }
        }
        return true;
    }
}
