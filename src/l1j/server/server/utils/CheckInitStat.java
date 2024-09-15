package l1j.server.server.utils;

import l1j.server.server.model.Instance.L1PcInstance;

public class CheckInitStat {

	// 檢查角色的最小屬性值
	private void CheckInitStat() {
	}

	/**
	 * 檢查角色的最小屬性值
	 *
	 * @param pc 角色實例
	 * @return true : 正常或管理員, false : 異常
	 */
	public static boolean CheckPcStat(L1PcInstance pc) {
		if (pc == null) { // 如果 pc 不存在
			return false;
		}
		if (pc.isGm()) { // 如果 pc 是管理員
			return true;
		}

		int str = pc.getAbility().getBaseStr();
		int dex = pc.getAbility().getBaseDex();
		int cha = pc.getAbility().getBaseCha();
		int con = pc.getAbility().getBaseCon();
		int intel = pc.getAbility().getBaseInt();
		int wis = pc.getAbility().getBaseWis();
		int basestr = 0;
		int basedex = 0;
		int basecon = 0;
		int baseint = 0;
		int basewis = 0;
		int basecha = 0;
		switch (pc.getType()) {
		case 0: // 王族
			basestr = 13;
			basedex = 9;
			basecon = 11;
			basewis = 11;
			basecha = 11;//13
			baseint = 9;
			break;
		case 1: // 騎士
			basestr = 16;
			basedex = 12;
			basecon = 16;
			basewis = 9;
			basecha = 10;
			baseint = 8;
			break;
		case 2: // 妖精
			basestr = 10;
			basedex = 12;
			basecon = 12;
			basewis = 12;
			basecha = 9;
			baseint = 12;
			break;
		case 3: // 法師
			basestr = 8;
			basedex = 7;
			basecon = 12;
			basewis = 14;
			basecha = 8;
			baseint = 14;
			break;
		case 4: // 黑暗妖精
			basestr = 15;
			basedex = 12;
			basecon = 12;
			basewis = 10;
			basecha = 7; // 8
			baseint = 11;
			break;
		case 5: // 龍騎士
			basestr = 13;
			basedex = 11;
			basecon = 14;
			basewis = 10;
			basecha = 8;
			baseint = 10;
			break;
		case 6: // 幻術師
			basestr = 9;
			basedex = 10;
			basecon = 12;
			basewis = 14;
			basecha = 8;
			baseint = 12;
			break;
		case 7: // 戰士
			basestr = 16;
			basedex = 13;
			basecon = 16;
			basewis = 7;
			basecha = 9;
			baseint = 10;
			break;
		case 8: // 劍士
			basestr = 16;
			basedex = 13;
			basecon = 15;
			basewis = 11;
			basecha = 5;
			baseint = 11;
			break;
		case 9: // 黃金槍騎
			basestr = 14;
			basedex = 12;
			basecon = 16;
			basewis = 12;
			basecha = 6;
			baseint = 9;
			break;
		}

		if (str < basestr || dex < basedex || con < basecon || cha < basecha || intel < baseint || wis < basewis) { // 如果小于初始屬性值
			return false;
		}
		return true;
	}

}
