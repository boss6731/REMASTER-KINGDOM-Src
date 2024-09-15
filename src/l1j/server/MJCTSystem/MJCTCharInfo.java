package l1j.server.MJCTSystem;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
/** 
 * MJCTCharInfo
 * MJSoft Character TradeSystem - Character Info
 * made by mjsoft, 2016.
 **/
public class MJCTCharInfo {
	private static final SimpleDateFormat _dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.KOREA);

	public String name;
	public int level;
	public int type;
	public int einhasad;
	public Timestamp tamEndTime;
	public int sex;
	public String clanName;
	public int str;
	public int dex;
	public int con;
	public int wis;
	public int intel;
	public int cha;
	public int elixir;
	public int hp;
	public int mp;
	public int ac;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append("\f2〓〓〓〓 角色查詢結果 〓〓〓〓").append("\n");
		sb.append("\f3名稱 : ").append(name).append("\n");
		sb.append("\f3等級 : ").append(level).append("\n");
		sb.append("\f3職業 : ").append(toMoreClass()).append("\n");
		sb.append("\f3血盟 : ").append(clanName).append("\n");
		sb.append("\f3艾因哈薩德 : ").append(einhasad).append("\n");
		if (tamEndTime != null)
			sb.append("\f3探險結束時間 : ").append(_dateFormat.format(tamEndTime.getTime()));
		sb.append("\f3力量 (STR) : ").append(str).append("\n");
		sb.append("\f3敏捷 (DEX) : ").append(dex).append("\n");
		sb.append("\f3體質 (CON) : ").append(con).append("\n");
		sb.append("\f3智慧 (WIS) : ").append(wis).append("\n");
		sb.append("\f3智力 (INT) : ").append(intel).append("\n");
		sb.append("\f3魅力 (CHA) : ").append(cha).append("\n");
		sb.append("\f3萬能藥 : ").append(elixir).append("個").append("\n");
		sb.append("\f3HP : ").append(hp).append("\n");
		sb.append("\f3MP : ").append(mp).append("\n");
		sb.append("\f3AC : ").append(String.valueOf(ac)).append("\n");
		sb.append("\f2〓〓〓〓 角色查詢完畢 〓〓〓〓");
		return sb.toString();
	}

	private String toMoreClass() {
		return String.format("%s(%s)", toClass(), sex == 0 ? "男" : "女");
	}

	private String toClass() {
		switch (type) {
			case 0:
				return "王族";
			case 1:
				return "騎士";
			case 2:
				return "妖精";
			case 3:
				return "法師";
			case 4:
				return "黑暗精靈";
			case 5:
				return "龍騎士";
			case 6:
				return "幻術師";
			case 7:
				return "戰士";
			case 8:
				return "劍士";
			case 9:
				return "黃金槍騎";
			default:
				return "";
		}
	}
}