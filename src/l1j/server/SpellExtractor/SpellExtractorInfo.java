package l1j.server.SpellExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpellExtractorInfo {
	public static SpellExtractorInfo newInstance(ResultSet rs) throws SQLException{
		SpellExtractorInfo Info = newInstance();
		Info._SkillId = rs.getInt("spell_id");
		Info._SkillName = rs.getString("spell_name");
		Info._SkillType = skilltype(rs.getString("spell_type"));
		Info._ItemId = rs.getInt("item_id");
		Info._ItemName = rs.getString("item_name");
		Info._ItemCount = rs.getInt("count");
		return Info;
	}
	
	public static SpellExtractorInfo newInstance(){
		return new SpellExtractorInfo();
	}

	private int _SkillId;
	private String _SkillName;
	private int _SkillType;
	private int _ItemId;
	private String _ItemName;
	private int _ItemCount;
	private SpellExtractorInfo(){
	}
	
	public static int skilltype(String type) {
		int spell_type = 0;
		if (type.equalsIgnoreCase("Active"))
			spell_type = 1;
		
		return spell_type;
	}
	
	public SpellExtractorInfo setSkillId(int val){
		_SkillId = val;
		return this;
	}
	public int getSkillId(){
		return _SkillId;
	}

	public SpellExtractorInfo setSkillName(String val){
		_SkillName = val;
		return this;
	}
	public String getSkillName(){
		return _SkillName;
	}
	
	public int getSkillType(){
		return _SkillType;
	}

	public SpellExtractorInfo setItemId(int val){
		_ItemId = val;
		return this;
	}
	public int getItemId(){
		return _ItemId;
	}
	
	public SpellExtractorInfo setItemName(String val){
		_ItemName = val;
		return this;
	}
	public String getItemName(){
		return _ItemName;
	}

	public SpellExtractorInfo set_ItemCount(int val){
		_ItemCount = val;
		return this;
	}
	public int get_ItemCount(){
		return _ItemCount;
	}
}
