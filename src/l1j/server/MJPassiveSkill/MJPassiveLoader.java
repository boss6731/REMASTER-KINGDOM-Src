package l1j.server.MJPassiveSkill;

import static l1j.server.server.model.skill.L1SkillId.STATUS_FRUIT;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eBoostType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillSound;

public class MJPassiveLoader {
	private static MJPassiveLoader _instance;

	public static MJPassiveLoader getInstance() {
		if (_instance == null)
			_instance = new MJPassiveLoader();
		return _instance;
	}

	public static void release() {
		if (_instance != null) {
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload() {
		MJPassiveLoader old = _instance;
		_instance = new MJPassiveLoader();
		if (old != null) {
			old.dispose();
			old = null;
		}
	}

	private HashMap<Integer, MJPassiveInfo> m_passives;

	private MJPassiveLoader() {
		m_passives = load();
	}

	private HashMap<Integer, MJPassiveInfo> load() {
		HashMap<Integer, MJPassiveInfo> passives = new HashMap<Integer, MJPassiveInfo>();
		Selector.exec("select * from passive_book_mapped", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					try {
						MJPassiveInfo pInfo = MJPassiveInfo.newInstance(rs);
						passives.put(pInfo.getPassiveId(), pInfo);
						passives.put(pInfo.getSpellBookId(), pInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		return passives;
	}

	public MJPassiveInfo fromPassiveId(int passiveId) {
		return m_passives.get(passiveId);
	}

	public MJPassiveInfo fromBookId(int bookId) {
		ArrayList<MJPassiveInfo> passives = values();
		// System.out.println(passives);
		if (passives == null)
			return null;

		for (MJPassiveInfo pInfo : passives) {
			// System.out.println(pInfo.getSpellBookId());
			if (pInfo.getSpellBookId() == bookId)
				return pInfo;
		}
		return null;
	}

	public ArrayList<MJPassiveInfo> fromClassType(int type) {
		ArrayList<MJPassiveInfo> passives = values();
		if (passives == null)
			return null;

		ArrayList<MJPassiveInfo> result = new ArrayList<MJPassiveInfo>();
		for (MJPassiveInfo pInfo : passives) {
			if (pInfo.getClassType() == type)
				result.add(pInfo);
		}
		return result;
	}

	public ArrayList<MJPassiveInfo> values() {
		return new ArrayList<MJPassiveInfo>(m_passives.values());
	}

	private static final String[] CLASS_NAMES = new String[] { "王族", "騎士", "妖精", "魔法師", "黑暗精靈", "龍騎士", "幻術師", "戰士",
			"劍士", "槍騎士" };

	public boolean masterPassive(L1PcInstance pc, MJPassiveInfo pInfo, boolean temp) {
		// System.out.println(pc.getPassive(pInfo.getPassiveId()));
		// System.out.println(pInfo.getPassiveId());
		if (!pc.isTempSkillPassive(pInfo.getPassiveId())) {
			if (pc.getPassive(pInfo.getPassiveId()) != null) {
				pc.sendPackets("已經學習過的魔法。");
				return false;
			}
		}
		if (pc.getLevel() < pInfo.getUseMinLevel()) {
			pc.sendPackets(String.format("%d 等級才能使用。", pInfo.getUseMinLevel()));
			return false;
		}
		if (pc.getClassNumber() != pInfo.getClassType()) {
			pc.sendPackets(String.format("該物品僅 %s 可使用。", CLASS_NAMES[pInfo.getClassType()]));
			return false;
		}
		if (pInfo.getPassiveId() == MJPassiveID.FOU_SLAYER_FORCE.toInt()) {
			if (!pc.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt())) {
				pc.sendPackets("前置被動技能：請先學習『弗奴斯殺手：勇敢』。");
				return false;
			}
		}
		if (pInfo.getPassiveId() == MJPassiveID.PHANTOM_REAPER.toInt()) {
			if (!pc.isSkillMastery(L1SkillId.PHANTOM)) {
				pc.sendPackets("前置技能：請先學習『幽靈』。");
				return false;
			}
		}

		if (pInfo.getPassiveId() == MJPassiveID.PHANTOM_DEATH.toInt()) {
			if (!pc.isPassive(MJPassiveID.PHANTOM_REAPER.toInt())) {
				pc.sendPackets("前置被動技能：請先學習『幽靈：收割者』。");
				return false;
			} else {
				if (pc.isTempSkillPassive(MJPassiveID.PHANTOM_REAPER.toInt())) {
					pc.sendPackets("前置被動技能『幽靈：收割者』已被『安沙爾的祝福』激活。");
					return false;
				}
			}
		}
		if (pInfo.getPassiveId() == MJPassiveID.PHANTOM_REQUEM.toInt()) {
			if (!pc.isPassive(MJPassiveID.PHANTOM_REAPER.toInt())) {
				pc.sendPackets("前置被動技能：請先學習『幽靈：收割者』。");
				return false;
			} else {
				if (pc.isTempSkillPassive(MJPassiveID.PHANTOM_REAPER.toInt())) {
					pc.sendPackets("前置被動技能『幽靈：收割者』已被『安沙爾的祝福』激活。");
					return false;
				}
			}
		}
		if (pInfo.getPassiveId() == MJPassiveID.REDUCTION_ARMOR_VETERAN.toInt()) {
			if (!pc.isSkillMastery(88)) {
				pc.sendPackets("前置技能：請先學習『減少護甲』。");
				return false;
			}
		}

		int passiveId = pInfo.getPassiveId();
		if (passiveId == MJPassiveID.DOUBLE_BREAK_DESTINY.toInt()) {
			if (pc.hasSkillEffect(L1SkillId.DOUBLE_BRAKE))
				pc.removeSkillEffect(L1SkillId.DOUBLE_BRAKE);
		}

		/*
		 * pc.addPassive(pInfo);
		 * MJPassiveUserLoader.store(pc, pInfo);
		 * S_SkillSound s_skillSound = new S_SkillSound(pc.getId(), 231);
		 * pc.sendPackets(s_skillSound, false);
		 * Broadcaster.broadcastPacket(pc, s_skillSound);
		 */
		if (pInfo.getPassiveId() == MJPassiveID.DARK_HORSE.toInt()) {
			L1SkillUse.on_icons_passive(pc, MJPassiveID.DARK_HORSE, -1);
			if (pc.hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
				int sec = pc.getSkillEffectTimeSec(L1SkillId.STATUS_FRUIT);
				pc.removeSkillEffect(STATUS_FRUIT);
				pc.setSkillEffect(STATUS_FRUIT, sec * 1000);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 3, pc.getSkillEffectTimeSec(L1SkillId.STATUS_FRUIT)));
			}
		}

		if (pInfo.getPassiveId() == MJPassiveID.SHADOW_STEP_CHASER.toInt()) {

		}

		if (pInfo.getPassiveId() == MJPassiveID.RESIST_ELEMENT.toInt()) {
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharAttrDef(pc));
		}
		if (pInfo.getPassiveId() == MJPassiveID.INFINITI_ARMOR.toInt()) {
			pc.sendPackets(new S_OwnCharAttrDef(pc));
		}
		if (pInfo.getPassiveId() == MJPassiveID.INFINITI_BLOOD.toInt()) {
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
		}

		if (pInfo.getPassiveId() == MJPassiveID.MORTAL_BODY.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(MJPassiveID.TACTICAL_ADVANCE.toInt());
			buff.set_on_icon_id(10856);
			buff.set_tooltip_str_id(1361);
			buff.set_is_good(true);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
		}

		if (pInfo.getPassiveId() == MJPassiveID.INFINITI_BULLET.toInt()) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()), true);
		}
		if (pInfo.getPassiveId() == MJPassiveID.DRESS_EVASION_PASSIVE.toInt()) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()), true);
		}
		if (pInfo.getPassiveId() == MJPassiveID.INFINITI_DODGE.toInt()) {
			pc.sendPackets(new S_OwnCharAttrDef(pc));
		}
		if (pInfo.getPassiveId() == MJPassiveID.IllUSION_DIAMONDGOLEM_PASSIVE.toInt()) {
			/*
			 * if (pc.hasSkillEffect(L1SkillId.CUBE_GOLEM)) {
			 * pc.removeSkillEffect(L1SkillId.CUBE_GOLEM);
			 * }
			 */
			L1SkillUse.on_icons_passive(pc, MJPassiveID.IllUSION_DIAMONDGOLEM_PASSIVE, -1);
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
		if (pInfo.getPassiveId() == MJPassiveID.IllUSION_LICH_PASSIVE.toInt()) {
			if (pc.hasSkillEffect(L1SkillId.CUBE_RICH)) {
				pc.removeSkillEffect(L1SkillId.CUBE_RICH);
			}
			L1SkillUse.on_icons_passive(pc, MJPassiveID.IllUSION_LICH_PASSIVE, -1);
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharAttrDef(pc));
		}
		/*
		 * if (pInfo.getPassiveId() == MJPassiveID.FOU_SLAYER_BRAVE.toInt()) {
		 * L1SkillUse.on_icons_passive(pc, MJPassiveID.FOU_SLAYER_BRAVE, -1);
		 * }
		 */
		if (pInfo.getPassiveId() == MJPassiveID.SOLID_CARRIAGE_PASS.toInt()) {
			pc.addEffectedER(10);
		}
		if (pInfo.getPassiveId() == MJPassiveID.PRIDE_PASS.toInt()) {
			if (pc.hasSkillEffect(L1SkillId.ADVANCE_SPIRIT)) {
				pc.removeSkillEffect(L1SkillId.ADVANCE_SPIRIT);
			}

			double percent = pc.getLevel() / 4;
			int addHp = (int) Math.round(pc.getBaseMaxHp() * (percent * 0.01));
			pc.setMagicBuffHp(addHp);
			pc.addMaxHp(pc.getMagicBuffHp());
			if (pc.isInParty()) {
				// 파티 프로토
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
		}

		if (passiveId == MJPassiveID.RISING_POSS.toInt()) {
			int point = 0;
			int RealSteelLevel = pc.getLevel();
			if (RealSteelLevel < 80) {
				RealSteelLevel = 80;
			}
			point = (int) ((RealSteelLevel - 80) / 3) + 1;
			pc.addSpecialPierce(eKind.ABILITY, point);

			int rd_point = 0;
			int rd_RealSteelLevel = pc.getLevel();
			if (rd_RealSteelLevel < 80) {
				rd_RealSteelLevel = 80;
			}
			rd_point = (int) ((rd_RealSteelLevel - 80) / 4) + 1;

			if (rd_point > 5)
				rd_point = 5;

			pc.getResistance().addPVPweaponTotalDamage(rd_point);
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
		}

		if (passiveId == MJPassiveID.TACTICAL_ADVANCE.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(MJPassiveID.TACTICAL_ADVANCE.toInt());
			buff.set_on_icon_id(10148);
			buff.set_tooltip_str_id(8030);
			buff.set_is_good(true);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.sendPackets(new S_SPMR(pc));
		}
		if (pInfo.getPassiveId() == MJPassiveID.ADVANCE_SPIRIT_PA.toInt()) {
			pc.setAdvenHp(pc.getBaseMaxHp() / 5);
			pc.setAdvenMp(pc.getBaseMaxMp() / 5);
			pc.addMaxHp(pc.getAdvenHp());
			pc.addMaxMp(pc.getAdvenMp());
			if (pc.isInParty()) {
				// 파티 프로토
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			L1SkillUse.on_icons_passive(pc, MJPassiveID.ADVANCE_SPIRIT_PA, -1);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		}

		if (passiveId == MJPassiveID.MAJESTY.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(118 - 1);
			buff.set_on_icon_id(9518);
			buff.set_tooltip_str_id(5893);
			buff.set_is_good(true);
			int reduction = 0;
			if (pc.getLevel() >= 80) {
				reduction = (pc.getLevel() - 80) / 2;
				if (reduction >= 10) {
					reduction = 10;
				}
			}
			pc.getResistance().addcalcPcDefense(reduction);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
		}

		if (passiveId == MJPassiveID.GIGANTIC.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(226 - 1);
			buff.set_on_icon_id(6168);
			buff.set_tooltip_str_id(3918);
			buff.set_is_good(true);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);

			double percent = pc.getLevel() / 2;
			int addHp = (int) Math.round(pc.getBaseMaxHp() * (percent * 0.01));
			pc.setMagicBuffHp(addHp);
			pc.addMaxHp(pc.getMagicBuffHp());
			if (pc.isInParty()) {
				// TODO 파티 프로토
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
		}

		if (passiveId == MJPassiveID.RAISING_WEAPON.toInt()) {
			int type = pc.getWeapon().getItem().getType();
			if (type == 3) {
				pc.addAttackDelayRate(10);
			}
		}
		if (passiveId == MJPassiveID.MOEBIUS.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(eNotiType.NEW);
			buff.set_spell_id(224 - 1);
			buff.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
			buff.set_on_icon_id(11050); // 10650
			buff.set_tooltip_str_id(8611);
			buff.set_is_good(true);
			pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
			// int range_reduction = 0;
			// if (pc.getLevel()>=85){
			// range_reduction = ((pc.getLevel() - 85) / 2) + 9;
			// if (range_reduction >= 15){
			// range_reduction = 15;
			// }
			// } else {
			// range_reduction = 9;
			// }
			// pc.getResistance().addcalcPcDefense(range_reduction);
			// pc.sendPackets(buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
		}

		// 전사 패시브 슬레이어
		if (passiveId == MJPassiveID.SLAYER.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.NEW);
			buff.set_spell_id(10003);
			buff.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_UNLIMIT);
			buff.set_on_icon_id(10650);
			buff.set_tooltip_str_id(8611);
			buff.set_is_good(true);
			pc.sendPackets((MJIProtoMessage) buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
			if (pc.getWeapon() != null) {
				int type = pc.getWeapon().getItem().getType1();
				if (type == 11) {
					pc.addAttackDelayRate(10.0D); // 슬레이어 공속 추가
				}
			}
		}

		if (passiveId == MJPassiveID.DEMOLITION.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.NEW);
			buff.set_spell_id(10090);
			buff.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_UNLIMIT);
			buff.set_on_icon_id(10660);
			buff.set_tooltip_str_id(8609);
			buff.set_is_good(true);
			pc.sendPackets((MJIProtoMessage) buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
		}
		if (passiveId == MJPassiveID.TITAN_BEAST.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.NEW);
			buff.set_spell_id(10652);
			buff.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_UNLIMIT);
			buff.set_on_icon_id(8610);
			buff.set_tooltip_str_id(8613);
			buff.set_is_good(true);
			pc.sendPackets((MJIProtoMessage) buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
		}
		if (passiveId == MJPassiveID.CONQUEROR.toInt()) {
			SC_SPELL_BUFF_NOTI buff = SC_SPELL_BUFF_NOTI.newInstance();
			buff.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.NEW);
			buff.set_spell_id(10653);
			buff.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_UNLIMIT);
			buff.set_on_icon_id(11441);
			buff.set_tooltip_str_id(9420);
			buff.set_is_good(true);
			pc.sendPackets((MJIProtoMessage) buff, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
		}
		if (passiveId == MJPassiveID.BERSERK.toInt()) {
			int percent = (int) Math.round(((double) pc.getCurrentHp() / (double) pc.getMaxHp()) * 100);
			if (percent < 50) {
				SC_SPELL_BUFF_NOTI.sendBerserk(pc, true);
				pc.setTitanBerserk(true);

				pc.addSpecialResistance(eKind.ALL, 10);
				pc.getResistance().addPVPweaponTotalDamage(10);
				pc.getResistance().addcalcPcDefense(16);

			} else {
				SC_SPELL_BUFF_NOTI.sendBerserk(pc, false);
				pc.setTitanBerserk(false);

				pc.addSpecialResistance(eKind.ALL, 5);
				pc.getResistance().addPVPweaponTotalDamage(5);
				pc.getResistance().addcalcPcDefense(8);
			}
		}

		if (temp) {
		} else {
			S_SkillSound s_skillSound = new S_SkillSound(pc.getId(), 231);
			pc.sendPackets(s_skillSound, false);
			Broadcaster.broadcastPacket(pc, s_skillSound);
		}
		pc.addPassive(pInfo);
		MJPassiveUserLoader.store(pc, pInfo, temp);

		return true;
	}

	public boolean useItem(L1PcInstance pc, L1ItemInstance item) {
		MJPassiveInfo pInfo = fromBookId(item.getItemId());
		if (pInfo == null) {
			return false;
		}
		// System.out.println(pc+"+"+pInfo);

		if (masterPassive(pc, pInfo, false)) {
			pc.getInventory().removeItem(item, 1);
		}
		return true;
	}

	public void dispose() {
		if (m_passives != null) {
			m_passives.clear();
			m_passives = null;
		}
	}
}
