package l1j.server.server.utils;

import static l1j.server.server.model.skill.L1SkillId.BONE_BREAK;
import static l1j.server.server.model.skill.L1SkillId.DESPERADO;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;
import static l1j.server.server.model.skill.L1SkillId.CHAINSWORD_STUN;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import l1j.server.CPMWReNewClan.ClanDungeon.ClanDugeon;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class CommonUtil {
	/**
	 * 2011.08.05 ÑÑäşúéãÆ
	 * @param number
	 * @return
	 */
	public static String numberFormat(int number) {
		try {
			NumberFormat nf = NumberFormat.getInstance();

			return nf.format(number);
		} catch (Exception e) {
			return Integer.toString(number);
		}
	}

	/**
	 * 2011.08.05 âËÑ¦ùŞâ¦
	 * @param number
	 * @return
	 */
	public static int random(int number) {
		Random rnd = new Random();
		return rnd.nextInt(number);
	}

	/**
	 * 2011.08.05 âËÑ¦ùŞâ¦
	 * @param lbound ù»Í£
	 * @param ubound ß¾Í£
	 * @return
	 */
	public static int random(int lbound, int ubound) {
		return (int) ((Math.random() * (ubound - lbound + 1)) + lbound);
	}

	/**
	 * 2011.08.30 íÀÖùÌ«ãÒ
	 * @param type ×¾úş
	 * @return
	 */
	public static String dateFormat(String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.KOREA);
		return sdf.format(Calendar.getInstance().getTime());
	}

	/**
	 * 2011.08.30 íÀÖùÌ«ãÒ
	 * @param type ×¾úş
	 * @return
	 */
	public static String dateFormat(String type, Timestamp date) {
		SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.KOREA);
		return sdf.format(date.getTime());
	}

	/**
	 * 2011.08.31 ú£ÙÍÌ¿áÖãÁÊà
	 * @param item ú£ÙÍ
	 * @param minute İÂñ¤
	 */
	public static void SetTodayDeleteTime(L1ItemInstance item, int minute) {
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * minute));
		item.setEndTime(deleteTime);
	}

	/**
	 * 2017.12.02 ËÔíĞ?ãµ?
	 * @param item ú£ÙÍ
	 * @param townname ?Ù£
	 */
	public static void SetHotelTownName(L1ItemInstance item, String townname) {
		item.setHotel_Town(townname);;
	}

	/**
	 * 2011.08.31 ò¦ïÒú£ÙÍÌ¿áÖãÁÊà(Íªß©ĞÑô¸í¥æ®ãÁÊà) - äÅ÷âÍ§÷Ö
	 * @param item ú£ÙÍ
	 */
	public static void SetTodayDeleteTime(L1ItemInstance item) {
		int hour = Integer.parseInt(dateFormat("HH"));
		int minute = Integer.parseInt(dateFormat("mm"));
		int time = 0;

		if (hour <= ClanDugeon.getInstance().ClanDugeonInfo.hour_Clan && minute < 30) {
			time = (ClanDugeon.getInstance().ClanDugeonInfo.hour_Clan - hour) * 60 + (60 - minute) - 60;
		} else {
			time = (ClanDugeon.getInstance().ClanDugeonInfo.hour_Clan - hour) * 60 + (60 - minute) - 60 + (24 * 60);
		}

		Timestamp deleteTime = null;

		deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * time));
		item.setEndTime(deleteTime);
	}

	/**
	 * 2011.08.31 Ëå×îò¦ïÒãÁÊàîÜí¥æ®ãÁÊà
	 * @param item ú£ÙÍ
	 */
	public static int getRestTime(int hh) {		
		int hour = Integer.parseInt(dateFormat("HH"));
		int minute = Integer.parseInt(dateFormat("mm"));

		int time = 0;

		time = (hh - hour) * 60 - minute;	

		return time;
	}
	/**
	 * àâïÒì¤ü¬ÜÁâ¦?Üôõ±Î¦ò¦ïÒîÜõÌÓŞ?ûúõÌá³?¡£
	 * @param i Ó×îñâ¦?
	 * @param min õÌá³?
	 * @param max õÌÓŞ?
	 * @return
	 */
	public static int get_current(int i, int min, int max) {
		int current = i;
		if (current <= min) {
			current = min;
		} else if (current >= max) {
			current = max;
		}
		return current;
	}
	/**
	 * Ëş?îîáêíºîÜßÒ÷¾ì¶ßÈ
	 * @param pc èÌÊ«ÊÇßä
	 * @param item ú£ÙÍ
	 * @return
	 */
	public static boolean teleport_check(L1PcInstance pc, L1ItemInstance item) {
		if (pc.isDead()) {
			return true;
		} else if (pc.isParalyzed() || pc.isSleeped()) {
			return true;
		} else if ((pc.hasSkillEffect(L1SkillId.EMPIRE) || pc.hasSkillEffect(SHOCK_STUN) || pc.hasSkillEffect(CHAINSWORD_STUN) || pc.hasSkillEffect(ICE_LANCE) || pc.hasSkillEffect(BONE_BREAK)
				|| pc.hasSkillEffect(L1SkillId.ETERNITI) || pc.hasSkillEffect(L1SkillId.OSIRIS)
				|| pc.hasSkillEffect(EARTH_BIND) || pc.hasSkillEffect(DESPERADO) || pc.hasSkillEffect(L1SkillId.FORCE_STUN) 
				|| pc.hasSkillEffect(L1SkillId.TEMPEST) || pc.hasSkillEffect(L1SkillId.PHANTOM) || pc.hasSkillEffect(L1SkillId.CRUEL)) && item.getItem().getType() == 17) {
			return true;
		} else if (pc.isGm()) {
			return false;
		} else if (!(pc.getInventory().checkItem(900111) && pc.getMap().isRuler()) && !pc.getMap().isEscapable() && (item.getItem().getType() == 0 && item.getItem().getType() == 17)) {
			return true;
		} 
		return false;
	}
	
	public static boolean isNumber(String number) {
		boolean flag = true;
		if (number == null || "".equals(number))
			return false;

		int size = number.length();
		int st_no = 0;

		if (number.charAt(0) == 45)// ÷÷Ó¨ãÀÜú?İ¶â¦¡£åıÍıãÀİ¶â¦£¬öÎÑÃã·êÈöÇğô 1 ËÒã·
			st_no = 1;

		for (int i = st_no; i < size; ++i) {
			if (!(48 <= ((int) number.charAt(i)) && 57 >= ((int) number.charAt(i)))) {
				flag = false;
				break;
			}

		}
		return flag;
	}
}