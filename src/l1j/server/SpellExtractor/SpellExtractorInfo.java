package l1j.server.SpellExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpellExtractorInfo {
    // 從 ResultSet 中創建新的 SpellExtractorInfo 實例
    public static SpellExtractorInfo newInstance(ResultSet rs) throws SQLException {
        SpellExtractorInfo Info = newInstance();
        Info._SkillId = rs.getInt("spell_id");
        Info._SkillName = rs.getString("spell_name");
        Info._SkillType = skilltype(rs.getString("spell_type"));
        Info._ItemId = rs.getInt("item_id");
        Info._ItemName = rs.getString("item_name");
        Info._ItemCount = rs.getInt("count");
        return Info;
    }
    
    // 創建新的 SpellExtractorInfo 實例
    public static SpellExtractorInfo newInstance() {
        return new SpellExtractorInfo();
    }

    private int _SkillId; // 技能 ID
    private String _SkillName; // 技能名稱
    private int _SkillType; // 技能類型
    private int _ItemId; // 道具 ID
    private String _ItemName; // 道具名稱
    private int _ItemCount; // 道具數量

    private SpellExtractorInfo() {
    }
    
    // 根據類型設置技能 ID
    public SpellExtractorInfo setSkillId(int val) {
        _SkillId = val;
        return this;
    }
    
    // 獲取技能 ID
    public int getSkillId() {
        return _SkillId;
    }

    // 設置技能名稱
    public SpellExtractorInfo setSkillName(String val) {
        _SkillName = val;
        return this;
    }
    
    // 獲取技能名稱
    public String getSkillName() {
        return _SkillName;
    }
    
    // 獲取技能類型
    public int getSkillType() {
        return _SkillType;
    }

    // 設置道具 ID
    public SpellExtractorInfo setItemId(int val) {
        _ItemId = val;
        return this;
    }
    
    // 獲取道具 ID
    public int getItemId() {
        return _ItemId;
    }
    
    // 設置道具名稱
    public SpellExtractorInfo setItemName(String val) {
        _ItemName = val;
        return this;
    }
    
    // 獲取道具名稱
    public String getItemName() {
        return _ItemName;
    }

    // 設置道具數量
    public SpellExtractorInfo set_ItemCount(int val) {
        _ItemCount = val;
        return this;
    }
    
    // 獲取道具數量
    public int get_ItemCount() {
        return _ItemCount;
    }

    // 根據類型將技能類型轉換為整數
    public static int skilltype(String type) {
        int spell_type = 0;
        if (type.equalsIgnoreCase("Active"))
            spell_type = 1;
        
        return spell_type;
    }
}
