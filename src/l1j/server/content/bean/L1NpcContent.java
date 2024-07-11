package l1j.server.content.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

public class L1NpcContent {
	private int classId;
	private int spriteId;
	private String name;
	private String nameId;
	private int level;
	private int ac;
	private int hp;
	private int mp;
	private int str;
	private int dex;
	private int con;
	private int wis;
	private int intel;
	private int cha;
	private int mr;
	private int magicLevel;
	private int magicBonus;
	private int magicEvasion;
	private int fireResistance;
	private int waterResistance;
	private int airResistance;
	private int earthResistance;
	private int alignment;
	private boolean big;
	public L1NpcContent(ResultSet rs) throws SQLException {
		this.classId			= rs.getInt("class_id");
		this.spriteId			= rs.getInt("sprite_id");
		this.name				= rs.getString("name");
		this.nameId				= rs.getString("name_id");
		this.level				= rs.getInt("level");
		this.ac					= rs.getInt("ac");
		this.hp					= rs.getInt("hp");
		this.mp					= rs.getInt("mp");
		this.str				= rs.getInt("str");
		this.dex				= rs.getInt("dex");
		this.con				= rs.getInt("con");
		this.wis				= rs.getInt("wis");
		this.intel				= rs.getInt("intel");
		this.cha				= rs.getInt("cha");
		this.mr					= rs.getInt("mr");
		this.magicLevel			= rs.getInt("magic_level");
		this.magicBonus			= rs.getInt("magic_bonus");
		this.magicEvasion		= rs.getInt("magic_evasion");
		this.fireResistance		= rs.getInt("fire_resistance");
		this.waterResistance	= rs.getInt("water_resistance");
		this.airResistance		= rs.getInt("air_resistance");
		this.earthResistance	= rs.getInt("earth_resistance");
		this.alignment			= rs.getInt("alignment");
		this.big				= Boolean.valueOf(rs.getString("big"));
	}
	public int getClassId() {
		return classId;
	}
	public int getSpriteId() {
		return spriteId;
	}
	public String getName() {
		return name;
	}
	public String getNameId() {
		return nameId;
	}
	public int getLevel() {
		return level;
	}
	public int getAc() {
		return ac;
	}
	public int getHp() {
		return hp;
	}
	public int getMp() {
		return mp;
	}
	public int getStr() {
		return str;
	}
	public int getDex() {
		return dex;
	}
	public int getCon() {
		return con;
	}
	public int getWis() {
		return wis;
	}
	public int getIntel() {
		return intel;
	}
	public int getCha() {
		return cha;
	}
	public int getMr() {
		return mr;
	}
	public int getMagicLevel() {
		return magicLevel;
	}
	public int getMagicBonus() {
		return magicBonus;
	}
	public int getMagicEvasion() {
		return magicEvasion;
	}
	public int getFireResistance() {
		return fireResistance;
	}
	public int getWaterResistance() {
		return waterResistance;
	}
	public int getAirResistance() {
		return airResistance;
	}
	public int getEarthResistance() {
		return earthResistance;
	}
	public int getAlignment() {
		return alignment;
	}
	public boolean isBig() {
		return big;
	}
}
