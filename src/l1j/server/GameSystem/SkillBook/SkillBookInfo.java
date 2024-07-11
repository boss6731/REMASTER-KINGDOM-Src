package l1j.server.GameSystem.SkillBook;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillBookInfo {
	public static SkillBookInfo newInstance(ResultSet rs) throws SQLException {
		return newInstance()
				.setSkillBookId(rs.getInt("skill_book_id"))
				.setItemName(rs.getString("itemName"))
				.setSkillId(rs.getInt("skill_id"))
				.setClassType(rs.getString("class"))
				.setUseMinLevel(rs.getInt("level"))
				.setLawful(rs.getString("lawful"))
				.setMasterPossible(rs.getString("master_possible"))
				.setSkillStep(rs.getInt("skill_step"))
				.setNormalMagic(rs.getString("normal_magic"));
	}

	public static SkillBookInfo newInstance() {
		return new SkillBookInfo();
	}

	private int _skill_book_id;
	private String _skill_book_name;
	private int _skill_id;
	private int _class_type;
	private int _use_min_level;
	private boolean _lawful;
	private boolean _master_possible;
	private int _skill_step;
	private boolean _normal_magic;

	public SkillBookInfo setSkillBookId(int skillbookid) {
		_skill_book_id = skillbookid;
		return this;
	}

	public int getSkillBookId() {
		return _skill_book_id;
	}

	public SkillBookInfo setItemName(String name) {
		_skill_book_name = name;
		return this;
	}

	public String getItemName() {
		return _skill_book_name;
	}

	public SkillBookInfo setSkillId(int skillid) {
		_skill_id = skillid;
		return this;
	}

	public int getSkillId() {
		return _skill_id;
	}

	public SkillBookInfo setClassType(int val) {
		_class_type = val;
		return this;
	}

	public SkillBookInfo setClassType(String val) {
		int nClassType = -1;
		switch (val) {
			case "王族":
				nClassType = 0;
				break;
			case "騎士":
				nClassType = 1;
				break;
			case "妖精":
				nClassType = 2;
				break;
			case "法師":
				nClassType = 3;
				break;
			case "黑暗妖精":
				nClassType = 4;
				break;
			case "龍騎士":
				nClassType = 5;
				break;
			case "幻術師":
				nClassType = 6;
				break;
			case "戰士":
				nClassType = 7;
				break;
			case "劍士":
				nClassType = 8;
				break;
			case "黃金槍騎":
				nClassType = 9;
				break;
			case "全部":
				nClassType = 100;
			default:
				throw new IllegalArgumentException(String.format("無效的類型（classType）：%s", val));
		}
		return setClassType(nClassType);
	}

	public int getClassType() {
		return _class_type;
	}

	public SkillBookInfo setUseMinLevel(int val) {
		_use_min_level = val;
		return this;
	}

	public int getUseMinLevel() {
		return _use_min_level;
	}

	public SkillBookInfo setLawful(String val) {
		if (val.equalsIgnoreCase("true")) {
			_lawful = true;
		} else if (val.equalsIgnoreCase("false")) {
			_lawful = false;
		} else {
			throw new IllegalArgumentException(String.format("無效的類型(取向) : %s", val));
		}
		return this;
	}

	public boolean isLawful() {
		return _lawful;
	}

	public SkillBookInfo setMasterPossible(String val) {
		if (val.equalsIgnoreCase("true")) {
			_master_possible = true;
		} else if (val.equalsIgnoreCase("false")) {
			_master_possible = false;
		} else {
			throw new IllegalArgumentException(String.format("無效類型（master_possible）：%s", val));
		}
		return this;
	}

	public boolean isMasterPossible() {
		return _master_possible;
	}

	public SkillBookInfo setSkillStep(int val) {
		_skill_step = val;
		return this;
	}

	public int getSkillStep() {
		return _skill_step;
	}

	public SkillBookInfo setNormalMagic(String val) {
		if (val.equalsIgnoreCase("true")) {
			_normal_magic = true;
		} else if (val.equalsIgnoreCase("false")) {
			_normal_magic = false;
		} else {
			throw new IllegalArgumentException(String.format("無效類型（normal_magic）：%s", val));
		}
		return this;
	}

	public boolean isNormalMagic() {
		return _normal_magic;
	}
}
