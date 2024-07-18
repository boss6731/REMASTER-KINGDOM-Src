package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class S_EnchantRanking extends ServerBasePacket {

	private static final String S_EnchantRanking = "[C] S_EnchantRanking";

	private byte[] _byte = null;
	private int j = 0;
	static String[] name;
	static String[] name1;
	static String[] castlename;
	static String[] clanname;
	static String[] leadername;
	static int[] enchantlvl;
	static int[] aden;
	static int[] armor;
	static int[] level;
	static int[] Ac;
	static int[] priaden;
	static int[] castleid;
	static int[] hascastle;
	static int[] taxrate;
	static int[] castleaden;
	static int[] MaxHp;
	static int[] MaxMp;

	public S_EnchantRanking(L1PcInstance pc, int number) {
		name = new String[10];
		name1 = new String[10];
		enchantlvl = new int[10];
		aden = new int[10];
		armor = new int[10];
		level = new int[10];
		Ac = new int[10];
		priaden = new int[10];
		castlename = new String[10];
		clanname = new String[10];
		leadername = new String[10];
		castleid = new int[10];
		hascastle = new int[10];
		taxrate = new int[10];
		castleaden = new int[10];
		MaxHp = new int[10];
		MaxMp = new int[10];
		buildPacket(pc, number);
	}

	private void buildPacket(L1PcInstance pc, int number) {

		String date = time();

		String type = null;
		String title = null;
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS("GM");//作者

		switch (number) {
			case 1:
				title = "強化附魔排名";
				break;
			case 2:
				title = "防具排名";
				break;
			case 3:
				title = "金幣排名";
				break;
			case 4:
				title = "等級排名";
				break;
			case 5:
				title = "神秘的羽毛排名";
				break;
			case 6:
				title = "倉庫金幣排名";
				break;
			case 7:
				title = "生命值排名";
				break;

			case 8:
				title = "魔法值排名";
				break;
		}
		writeS(title);
		writeS(date);
		switch (pc.getType()) {
			case 0:
				type = "王族";
				break;
			case 1:
				type = "騎士";
				break;
			case 2:
				type = "妖精";
				break;
			case 3:
				type = "法師";
				break;
			case 4:
				type = "黑暗妖精";
				break;
			case 5:
				type = "龍騎士";
				break;
			case 6:
				type = "幻術師";
				break;
			case 7:
				type = "戰士";
				break;
			case 8:
				type = "劍士";
				break;
			case 9:
				type = "黃金槍騎";
				break;
		}


		int p = Rank(pc, number);
		if (number == 1) { // 添加部分
			writeS("\n\r" + "  第1名 " + "+" + enchantlvl[0] + " " + name[0] + "\n\r" + "  所有者  : " + name1[0] + "\n\r" + "  第2名 " + "+" + enchantlvl[1] + " "
					+ name[1] + "\n\r" + "  所有者  : " + name1[1] + "\n\r" + "  第3名 " + "+" + enchantlvl[2] + " " + name[2] + "\n\r" + "  所有者  : " + name1[2]
					+ "\n\r" + "  第4名 " + "+" + enchantlvl[3] + " " + name[3] + "\n\r" + "  所有者  : " + name1[3] + "\n\r" + "  第5名 " + "+" + enchantlvl[4] + " "
					+ name[4] + "\n\r" + "  所有者  : " + name1[4] + "\n\r" + "  第6名  " + "+" + enchantlvl[5] + " " + name[5] + "\n\r" + "  所有者  : " + name1[5]
					+ "\n\r" + "  第7名 " + "+" + enchantlvl[6] + " " + name[6] + "\n\r" + "  所有者  : " + name1[6] + "\n\r" + "  第8名 " + "+" + enchantlvl[7] + " "
					+ name[7] + "\n\r" + "  所有者  : " + name1[7] + "\n\r" + "  第9名 " + "+" + enchantlvl[8] + " " + name[8] + "\n\r" + "  所有者  : " + name1[8]
					+ "\n\r" + " 第10名 " + "+" + enchantlvl[9] + " " + name[9] + "\n\r" + "  所有者  : " + name1[9] + "\n\r" + "      ");
		} else if (number == 2) { // 添加部分
			writeS("\n\r" + "  第1名  " + "+" + armor[0] + " " + name[0] + "\n\r" + "  所有者  : " + name1[0] + "\n\r" + "  第2名  " + "+" + armor[1] + " " + name[1]
					+ "\n\r" + "  所有者  : " + name1[1] + "\n\r" + "  第3名  " + "+" + armor[2] + " " + name[2] + "\n\r" + "  所有者  : " + name1[2] + "\n\r" + "  第4名  "
					+ "+" + armor[3] + " " + name[3] + "\n\r" + "  所有者  : " + name1[3] + "\n\r" + "  第5名  " + "+" + armor[4] + " " + name[4] + "\n\r" + "  所有者  : "
					+ name1[4] + "\n\r" + "  第6名  " + "+" + armor[5] + " " + name[5] + "\n\r" + "  所有者  : " + name1[5] + "\n\r" + "  第7名  " + "+" + armor[6] + " "
					+ name[6] + "\n\r" + "  所有者  : " + name1[6] + "\n\r" + "  第8名  " + "+" + armor[7] + " " + name[7] + "\n\r" + "  所有者  : " + name1[7] + "\n\r"
					+ "  第9名  " + "+" + armor[8] + " " + name[8] + "\n\r" + "  所有者  : " + name1[8] + "\n\r" + " 第10名  " + "+" + armor[9] + " " + name[9] + "\n\r"
					+ "  所有者  : " + name1[9] + "\n\r" + "      ");
		} else if (number == 3) { // 添加部分
			writeS("\n\r" + "  第1名  " + "$ " + aden[0] + " 金幣 \n\r" + "  所有者  : " + name[0] + "\n\r" + "  第2名  " + "$ " + aden[1] + " 金幣 \n\r" + "  所有者  : " + name[1]
					+ "\n\r" + "  第3名  " + "$ " + aden[2] + " 金幣 \n\r" + "  所有者  : " + name[2] + "\n\r" + "  第4名  " + "$ " + aden[3] + " 金幣 \n\r" + "  所有者  : "
					+ name[3] + "\n\r" + "  第5名  " + "$ " + aden[4] + " 金幣 \n\r" + "  所有者  : " + name[4] + "\n\r" + "  第6名  " + "$ " + aden[5] + " 金幣 \n\r"
					+ "  所有者  : " + name[5] + "\n\r" + "  第7名  " + "$ " + aden[6] + " 金幣 \n\r" + "  所有者  : " + name[6] + "\n\r" + "  第8名  " + "$ " + aden[7]
					+ " 金幣 \n\r" + "  所有者  : " + name[7] + "\n\r" + "  第9名  " + "$ " + aden[8] + " 金幣 \n\r" + "  所有者  : " + name[8] + "\n\r" + " 第10名  " + "$ "
					+ aden[9] + " 金幣 \n\r" + "  所有者  : " + name[9] + "\n\r" + "      ");
		} else if (number == 4) { // 添加部分
			writeS("\n\r" + "  第1名  " + name[0] + " \n\r" + "  目前等級 : " + level[0] + "\n\r" + "  第2名  " + name[1] + " \n\r" + "  目前等級 : " + level[1] + "\n\r"
					+ "  第3名  " + name[2] + " \n\r" + "  目前等級 : " + level[2] + "\n\r" + "  第4名  " + name[3] + " \n\r" + "  目前等級 : " + level[3] + "\n\r" + "  第5名  "
					+ name[4] + " \n\r" + "  目前等級 : " + level[4] + "\n\r" + "  第6名  " + name[5] + " \n\r" + "  目前等級 : " + level[5] + "\n\r" + "  第7名  " + name[6]
					+ " \n\r" + "  目前等級 : " + level[6] + "\n\r" + "  第8名  " + name[7] + " \n\r" + "  目前等級 : " + level[7] + "\n\r" + "  第9名  " + name[8] + " \n\r"
					+ "  目前等級 : " + level[8] + "\n\r" + " 第10名  " + name[9] + " \n\r" + "  目前等級 : " + level[9] + "\n\r" + "      ");
		} else if (number == 5) { // 添加部分
			writeS("\n\r" + "  第1名  " + priaden[0] + "神秘的黃金羽毛\n\r" + "  所有者  : " + name[0] + "\n\r" + "  第2名  " + priaden[1] + "神秘的黃金羽毛\n\r" + "  所有者  : " + name[1]
					+ "\n\r" + "  第3名  " + priaden[2] + "神秘的黃金羽毛\n\r" + "  所有者  : " + name[2] + "\n\r" + "  第4名  " + priaden[3] + "神秘的黃金羽毛\n\r" + "  所有者  : " + name[3]
					+ "\n\r" + "  第5名  " + priaden[4] + "神秘的黃金羽毛\n\r" + "  所有者  : " + name[4] + "\n\r" + "  第6名  " + priaden[5] + "神秘的黃金羽毛\n\r" + "  所有者  : " + name[5]
					+ "\n\r" + "  第7名  " + priaden[6] + "神秘的黃金羽毛\n\r" + "  所有者  : " + name[6] + "\n\r" + "  第8名  " + priaden[7] + "神秘的黃金羽毛\n\r" + "  所有者  : " + name[7]
					+ "\n\r" + "  第9名  " + priaden[8] + "神秘的黃金羽毛\n\r" + "  所有者  : " + name[8] + "\n\r" + " 第10名  " + priaden[9] + "神秘的黃金羽毛\n\r" + "  所有者  : " + name[9]
					+ "\n\r" + "      ");
		} else if (number == 6) { // 添加部分
			writeS("\n\r" + "  第1名  $ : " + priaden[0] + " 金幣 \n\r" + "  使用者名稱 : " + name[0] + "\n\r" + "  第2名  $ : " + priaden[1] + " 金幣 \n\r" + "  使用者名稱 : " + name[1]
					+ "\n\r" + "  第3名  $ : " + priaden[2] + " 金幣 \n\r" + "  使用者名稱 : " + name[2] + "\n\r" + "  第4名  $ : " + priaden[3] + " 金幣 \n\r" + "  使用者名稱 : "
					+ name[3] + "\n\r" + "  第5名  $ : " + priaden[4] + " 金幣 \n\r" + "  使用者名稱 : " + name[4] + "\n\r" + "  第6名  $ : " + priaden[5] + " 金幣 \n\r"
					+ "  使用者名稱 : " + name[5] + "\n\r" + "  第7名  $ : " + priaden[6] + " 金幣 \n\r" + "  使用者名稱 : " + name[6] + "\n\r" + "  第8名  $ : " + priaden[7]
					+ " 金幣 \n\r" + "  使用者名稱 : " + name[7] + "\n\r" + "  第9名  $ : " + priaden[8] + " 金幣 \n\r" + "  使用者名稱 : " + name[8] + "\n\r" + " 第10名  $ : "
					+ priaden[9] + " 金幣 \n\r" + "  使用者名稱 : " + name[9] + "\n\r" + "      ");
		} else if (number == 7) { // 添加部分
			writeS("\n\r" + "  第1名 . " + name[0] + " " + MaxHp[0] + "\n\r" + "  第2名 . " + name[1] + " " + MaxHp[1] + "\n\r" + "  第3名 . " + name[2] + " " + MaxHp[2]
					+ "\n\r" + "  第4名 . " + name[3] + " " + MaxHp[3] + "\n\r" + "  第5名 . " + name[4] + " " + MaxHp[4] + "\n\r" + "  第6名 . " + name[5] + " " + MaxHp[5]
					+ "\n\r" + "  第7名 . " + name[6] + " " + MaxHp[6] + "\n\r" + "  第8名 . " + name[7] + " " + MaxHp[7] + "\n\r" + "  第9名 . " + name[8] + " " + MaxHp[8]
					+ "\n\r" + " 第10名 . " + name[9] + " " + MaxHp[9] + "\n\r" + "      ");
		} else if (number == 8) { // 添加部分
			writeS("\n\r" + "  第1名 . " + name[0] + " " + MaxMp[0] + "\n\r" + "  第2名 . " + name[1] + " " + MaxMp[1] + "\n\r" + "  第3名 . " + name[2] + " " + MaxMp[2]
					+ "\n\r" + "  第4名 . " + name[3] + " " + MaxMp[3] + "\n\r" + "  第5名 . " + name[4] + " " + MaxMp[4] + "\n\r" + "  第6名 . " + name[5] + " " + MaxMp[5]
					+ "\n\r" + "  第7名 . " + name[6] + " " + MaxMp[6] + "\n\r" + "  第8名 . " + name[7] + " " + MaxMp[7] + "\n\r" + "  第9名 . " + name[8] + " " + MaxMp[8]
					+ "\n\r" + " 第10名 . " + name[9] + " " + MaxMp[9] + "\n\r" + "      ");
		}

	}

	private int Rank(L1PcInstance pc, int number) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int objid = pc.getId();
		int i = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			switch (number) {
			case 1: // 添加部分
				pstm = con.prepareStatement("SELECT enchantlvl, weapon.name, characters.char_name  FROM character_items, weapon, characters WHERE character_items.item_id in(select item_id from weapon) And character_items.char_id in(select objid from characters where AccessLevel = 0) And character_items.item_id=weapon.item_id And character_items.char_id=characters.objid And count = 1 order by character_items.enchantlvl desc limit 10");
				break;
			case 2: // 添加部分
				pstm = con.prepareStatement("SELECT enchantlvl, armor.name, characters.char_name  FROM character_items, armor, characters WHERE character_items.item_id in(select item_id from armor) And character_items.char_id in(select objid from characters where AccessLevel = 0) And character_items.item_id=armor.item_id And character_items.char_id=characters.objid And count = 1 order by character_items.enchantlvl desc limit 10");
				break;
			case 3:
				pstm = con.prepareStatement("SELECT count, characters.char_name FROM character_items, characters WHERE item_id in(select item_id from etcitem) And char_id in(select objid from characters where AccessLevel = 0) And character_items.char_id=characters.objid And item_id = 40308 order by count desc limit 10");
				break;
			case 4: // 添加部分
				pstm = con.prepareStatement("SELECT level, char_name FROM characters WHERE AccessLevel = 0 order by level desc limit 10");
				break;
			case 5:
				pstm = con.prepareStatement("SELECT count, characters.char_name FROM character_items, characters WHERE item_id in(select item_id from etcitem) And char_id in(select objid from characters where AccessLevel = 0) And character_items.char_id=characters.objid And item_id = 41921 order by count desc limit 10");
				break;
			case 6:
				pstm = con.prepareStatement("SELECT count, accounts.login FROM character_warehouse, accounts WHERE  login in(select login from accounts where access_level = 0) And character_warehouse.account_name =accounts.login And item_id = 40308 order by count desc limit 10");
				break;
			case 7: // 添加部分
				pstm = con.prepareStatement("SELECT MaxHp, char_name FROM characters WHERE AccessLevel = 0 order by MaxHp desc limit 10");
				break;
			case 8: // 添加部分
				pstm = con.prepareStatement("SELECT MaxMp, char_name FROM characters WHERE AccessLevel = 0 order by MaxMp desc limit 10");
				break;
			default:
				pstm = con.prepareStatement("SELECT char_name FROM characters WHERE AccessLevel = 0 order by Exp desc limit 10");
				break;
			}

			rs = pstm.executeQuery();
			if (number == 1) { // 添加部分
				while (rs.next()) {
					enchantlvl[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					name1[i] = rs.getString(3);
					i++;
				}
			} else if (number == 2) { // 添加部分
				while (rs.next()) {
					armor[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					name1[i] = rs.getString(3);
					i++;
				}
			} else if (number == 3) { // 添加部分
				while (rs.next()) {
					aden[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 4) { // 添加部分
				while (rs.next()) {
					level[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 5) { // 添加部分
				while (rs.next()) {
					priaden[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 6) { // 添加部分
				while (rs.next()) {
					priaden[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;

				}

			} else if (number == 7) { // 添加部分
				while (rs.next()) {
					MaxHp[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 8) { // 添加部分
				while (rs.next()) {
					MaxMp[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}

			} else {
				while (rs.next()) {
					name[i] = rs.getString(1);
					i++;
				}

				// 當記錄不存在或小於5時
				while (i < 10) {
					name[i] = "不存在.";
					i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// _log.log(Level.SEVERE, "S_EnchantRanking[]Error", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return i;
	}

	private static String time() {
		TimeZone tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
		Calendar cal = Calendar.getInstance(tz);
		int year = cal.get(Calendar.YEAR) - 2000;
		String year2;
		if (year < 10) {
			year2 = "0" + year;
		} else {
			year2 = Integer.toString(year);
		}
		int Month = cal.get(Calendar.MONTH) + 1;
		String Month2 = null;
		if (Month < 10) {
			Month2 = "0" + Month;
		} else {
			Month2 = Integer.toString(Month);
		}
		int date = cal.get(Calendar.DATE);
		String date2 = null;
		if (date < 10) {
			date2 = "0" + date;
		} else {
			date2 = Integer.toString(date);
		}
		return year2 + "/" + Month2 + "/" + date2;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_EnchantRanking;
	}

}
