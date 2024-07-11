package l1j.server.MJ3SEx.Loader;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJ3SEx.ActionInformation;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.IntervalDecorator.ActionIntervalDecoratorFactory;
import l1j.server.MJ3SEx.MJSprBoundary;
import l1j.server.MJ3SEx.SpriteInformation;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeHelper;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.stream.Stream;

public class SpriteInformationLoader {
    private static Integer[] _levelToIdx;

    // 將等級映射到索引
    public static int levelToIndex(int level, int sprId) {
        /*
         * TODO 將事件轉換外部化以實現相同的速度，無論附加層級為何
         */
        MJSprBoundary boundary = MJSprBoundary.get_boundary(sprId);
        if (boundary != null)
            level = boundary.get_boundary_level();
        return _levelToIdx == null ? 0 : _levelToIdx[level];
    }

    private static SpriteInformationLoader _instance;

    public static SpriteInformationLoader getInstance() {
        if (_instance == null)
            _instance = new SpriteInformationLoader();
        return _instance;
    }

    public static void release() {
        if (_instance != null) {
            _instance.clear();
            _instance = null;
        }
    }

    public static void reload() {
        SpriteInformationLoader tmp = _instance;
        _instance = new SpriteInformationLoader().loadSpriteInformation();
        if (tmp != null) {
            tmp.clear();
            tmp = null;
        }
    }

    private HashMap<Integer, SpriteInformation> _sprInfos;
    private HashMap<Integer, SpellDelayInfo> _spellInfos;

    private SpriteInformationLoader() {
    }

    // 讀取技能延遲信息
    private HashMap<Integer, SpellDelayInfo> loadSpellDelayInformation() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        HashMap<Integer, SpellDelayInfo> sps = null;
        try {
            sps = new HashMap<Integer, SpellDelayInfo>(320);
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("select * from tb_magicdelay");
            rs = pstm.executeQuery();
            while (rs.next()) {
                SpellDelayInfo si = new SpellDelayInfo();
                si.setSkillId(rs.getInt("skill_id"));
                si.setDelay(rs.getLong("delay"));
                si.setGlobalDelay(rs.getLong("global_delay"));
                si.setDelayGroupId(rs.getInt("delay_group_id"));
                si.setType(rs.getBoolean("fix_delay"));
                si.setRecord(rs.getBoolean("is_record"));
                sps.put(si.getSkillId(), si);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        return sps;
    }

    // 重新加載技能延遲信息
    public void reloadSpellDelayInformation() {
        HashMap<Integer, SpellDelayInfo> tmp = _spellInfos;
        _spellInfos = loadSpellDelayInformation();
        if (tmp != null) {
            tmp.clear();
            tmp = null;
        }
    }

    // 內部類，用於存儲技能延遲信息
    public class SpellDelayInfo {
        private int _skillid;
        private long _delay;
        private long _globaldelay;
        private int _delay_group_id;
        private boolean _type;
        private boolean _record;

        public int getSkillId() {
            return _skillid;
        }

        public void setSkillId(int id) {
            _skillid = id;
        }

        public long getDelay() {
            return _delay;
        }

        public void setDelay(long id) {
            _delay = id;
        }

        public long getGlobalDelay() {
            return _globaldelay;
        }

        public void setGlobalDelay(long id) {
            _globaldelay = id;
        }

        public int getDelayGroupId() {
            return _delay_group_id;
        }

        public void setDelayGroupId(int id) {
            _delay_group_id = id;
        }

        public boolean getType() {
            return _type;
        }

        public void setType(boolean id) {
            _type = id;
        }

        public void setRecord(boolean f) {
            _record = f;
        }

        public boolean isRecord() {
            return _record;
        }
    }

    // 加載精靈信息
    public SpriteInformationLoader loadSpriteInformation() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("select spr_id, type, width, height, action_count from spr_info");
            rs = pstm.executeQuery();
            _sprInfos = new HashMap<Integer, SpriteInformation>(SQLUtil.calcRows(rs));
            while (rs.next()) {
                SpriteInformation sInfo = loadActionInformation(SpriteInformation.create(rs));
                _sprInfos.put(sInfo.getSpriteId(), sInfo);
            }
            loadUserActionInformation();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
            _spellInfos = loadSpellDelayInformation();
        }
        return this;
    }

    // 加載動作信息
    private SpriteInformation loadActionInformation(SpriteInformation sInfo) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("select act_id, framecount, framerate from spr_action where spr_id=?");
            pstm.setInt(1, sInfo.getSpriteId());
            rs = pstm.executeQuery();
            while (rs.next())
                sInfo.put(ActionInformation.create(rs));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        return sInfo;
    }

    // 加載用戶動作信息
    private void loadUserActionInformation() {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new File("./data/xml/PolyFrame/polyframerate.xml"));

            loadLevelInformation(doc);
            loadActionRates(doc);
            loadExceptionActionRates(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 加載等級信息
    private void loadLevelInformation(Document doc) {
        Integer[] lvlToIdx = new Integer[128];
        Element element = (Element) doc.getElementsByTagName("Level").item(0);
        Integer[] levels = (Integer[]) MJArrangeParser.parsing(element.getAttribute("range"), ",",
                MJArrangeParseeFactory.createIntArrange()).result();

        for (int i = 0; i < levels.length; i++) {
            if (i + 1 >= levels.length) {
                MJArrangeHelper.<Integer>setArrayValues(lvlToIdx, levels[i], 127, i);
                break;
            }
            MJArrangeHelper.<Integer>setArrayValues(lvlToIdx, levels[i], levels[i + 1] - 1, i);
        }

        _levelToIdx = lvlToIdx;
    }

    // 加載動作率
    private void loadActionRates(Document doc) {
        Integer[] targets = loadTargetSprite(doc);
        NodeList nodes = ((Element) ((NodeList) doc.getElementsByTagName("PolyFrameRate")).item(0)).getChildNodes();
        int size = nodes.getLength();
        for (int i = size - 1; i >= 0; --i) {
            Node node = nodes.item(i);
            if (Node.ELEMENT_NODE != node.getNodeType())
                continue;

            Element element = (Element) node;
            Double[] rates = (Double[]) MJArrangeParser.parsing(element.getAttribute("rate"), ",",
                    MJArrangeParseeFactory.createFrameRateArrange()).result();
            Integer[] actions = (Integer[]) MJArrangeParser.parsing(element.getAttribute("action"), ",",
                    MJArrangeParseeFactory.createIntArrange()).result();
            for (Integer a : actions) {
                if (EActionCodes.fromInt(a) == null) {
                    continue;
                }

                for (Integer target : targets) {
                    SpriteInformation sInfo = get(target);
                    if (sInfo == null) {
                        System.out.println(String.format("無效的用戶精靈 : %d", target));
                    } else {
                        sInfo.registerUserActions(a, rates);
                    }
                }
            }
        }
    }

    // 加載例外動作率
    private void loadExceptionActionRates(Document doc) {
        Integer[] targets = loadExceptionTargetSprite(doc);
        NodeList nodes = ((Element) ((NodeList) doc.getElementsByTagName("PolyFrameException")).item(0))
                .getChildNodes();
        int size = nodes.getLength();
        for (int i = size - 1; i >= 0; --i) {
            Node node = nodes.item(i);
            if (Node.ELEMENT_NODE != node.getNodeType())
                continue;

            if (!node.getNodeName().equalsIgnoreCase("Frame"))
                continue;

            Element element = (Element) node;
            Double[] rates = (Double[]) MJArrangeParser.parsing(element.getAttribute("rate"), ",",
                    MJArrangeParseeFactory.createFrameRateArrange()).result();
            Integer[] actions = (Integer[]) MJArrangeParser.parsing(element.getAttribute("action"), ",",
                    MJArrangeParseeFactory.createIntArrange()).result();

            for (Integer a : actions) {
                for (Integer target : targets) {
                    SpriteInformation sInfo = get(target);
                    if (sInfo == null) {
                        System.out.println(String.format("無效的用戶例外精靈 : %d", target));
                    } else {
                        sInfo.registerUserActions(a, rates);
                    }
                }
            }
            break;
        }
    }

    // 根據sprite ID獲取SpriteInformation對象
    public SpriteInformation get(int n) {
        SpriteInformation sInfo = _sprInfos.get(n);
        if (sInfo == null) {
            try {
                throw new Exception(String.format("未找到精靈 ID : %d", n));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return _sprInfos.get(1120);
        }
        return sInfo;
    }

    // 清空所有數據
    public void clear() {
        if (_spellInfos != null) {
            _spellInfos.clear();
            _spellInfos = null;
        }

        Stream<SpriteInformation> stream = createValuesStream();
        if (stream == null)
            return;

        stream.forEach((SpriteInformation sInfo) -> {
            sInfo.dispose();
        });
        _sprInfos.clear();
        _sprInfos = null;
    }

    // 獲取使用技能的間隔時間
    public long getUseSpellInterval(L1PcInstance pc, int skillId) {
        SpellDelayInfo spell = _spellInfos.get(skillId);
        if (spell == null)
            return 0L;

        long l = spell.getDelay();

        if (spell.getType())
            return l;

        return (long) ActionIntervalDecoratorFactory.PC_MAGIC_DECORATOR.decoration(pc, l);
    }

    // 獲取使用技能的全局間隔時間
    public long getUseSpellGlobalInterval(L1PcInstance pc, int skillId) {
        SpellDelayInfo spell = _spellInfos.get(skillId);
        if (spell == null)
            return 0L;

        long l = spell.getGlobalDelay();

        if (spell.getType())
            return l;

        return (long) ActionIntervalDecoratorFactory.PC_MAGIC_DECORATOR.decoration(pc, l);
    }

    // 獲取使用技能的組ID
    public int getUseSpellGroupId(int skillId) {
        SpellDelayInfo spell = _spellInfos.get(skillId);
        if (spell == null)
            return 0;

        int groupid = spell.getDelayGroupId();

        return groupid;
    }

    // 判斷是否使用技能延遲記錄
    public boolean isUseSpellDelayRecord(int skillId) {
        SpellDelayInfo spell = _spellInfos.get(skillId);
        if (spell == null) {
            return false;
        }
        return spell.isRecord();
    }

    // 創建SpriteInformation對象的Stream
    public Stream<SpriteInformation> createValuesStream() {
        return _sprInfos == null ? null : _sprInfos.size() > 1024 ? _sprInfos.values().parallelStream()
                : _sprInfos.values().stream();
    }

    // 創建SpriteInformation對象的Key的Stream
    public Stream<Integer> createKeysStream() {
        return _sprInfos == null ? null : _sprInfos.size() > 1024 ? _sprInfos.keySet().parallelStream()
                : _sprInfos.keySet().stream();
    }
}
