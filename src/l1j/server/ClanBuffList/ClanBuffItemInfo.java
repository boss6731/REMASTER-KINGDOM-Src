package l1j.server.ClanBuffList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClanBuffItemInfo {
	public static ClanBuffItemInfo newInstance(ResultSet rs) throws SQLException{
		ClanBuffItemInfo pInfo = newInstance();
		pInfo._ItemId = rs.getInt("item_id");
		pInfo._ItemName = rs.getString("item_name");
		pInfo._SkillId = rs.getInt("skill_id");
		pInfo._SkillName = rs.getString("skill_name");
		pInfo._SkillTime = rs.getInt("skill_time");
		return pInfo;
	}
	
	public static ClanBuffItemInfo newInstance(){
		return new ClanBuffItemInfo();
	}

	private int _ItemId;
	private String _ItemName;
	private int _SkillId;
	private String _SkillName;
	private int _SkillTime;
	private ClanBuffItemInfo(){
	}

	public ClanBuffItemInfo setItemId(int val){
		_ItemId = val;
		return this;
	}
	public int getItemId(){
		return _ItemId;
	}
	
	public ClanBuffItemInfo setItemName(String val){
		_ItemName = val;
		return this;
	}
	public String getItemName(){
		return _ItemName;
	}

	public ClanBuffItemInfo setSkillId(int val){
		_SkillId = val;
		return this;
	}
	public int getSkillId(){
		return _SkillId;
	}

	public ClanBuffItemInfo setSkillName(String val){
		_SkillName = val;
		return this;
	}
	public String getSkillName(){
		return _SkillName;
	}
	
	public ClanBuffItemInfo setSkillTime(int val){
		_SkillTime = val;
		return this;
	}
	public int getSkillTime(){
		return _SkillTime;
	}
}
