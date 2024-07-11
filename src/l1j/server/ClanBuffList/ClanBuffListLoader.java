package l1j.server.ClanBuffList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_SPMR;

public class ClanBuffListLoader {
	private static ClanBuffListLoader _instance;

	public static ClanBuffListLoader getInstance() {
		if (_instance == null)
			_instance = new ClanBuffListLoader();
		return _instance;
	}

	public static void reload() {
		if (_instance != null) {
			_instance = new ClanBuffListLoader();
		}
	}

	private HashMap<Integer, ArrayList<ClanBuffListInfo>> _clan_buff_list;
	private HashMap<Integer, ClanBuffItemInfo> _clan_buff_items;

	private ClanBuffListLoader() {
		Infoload();
		Itemload();
	}

	private void Infoload() {
		final HashMap<Integer, ArrayList<ClanBuffListInfo>> buff_list_map = new HashMap<Integer, ArrayList<ClanBuffListInfo>>();
		Selector.exec("select * from clan_buff_list", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					try {
						ClanBuffListInfo Info = ClanBuffListInfo.newInstance(rs);
						if (Info == null)
							continue;

						ArrayList<ClanBuffListInfo> inputList = buff_list_map.get(Info.get_clan_id());
						if (inputList == null) {
							inputList = new ArrayList<ClanBuffListInfo>();
							buff_list_map.put(new Integer(Info.get_clan_id()), inputList);
						}
						inputList.add(Info);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		_clan_buff_list = buff_list_map;
	}

	private void Itemload() {
		HashMap<Integer, ClanBuffItemInfo> items = new HashMap<Integer, ClanBuffItemInfo>();
		Selector.exec("select * from clan_buff_items", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					try {
						ClanBuffItemInfo Info = ClanBuffItemInfo.newInstance(rs);
						items.put(Info.getItemId(), Info);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		_clan_buff_items = items;
	}

	public void addClanBuff(L1Clan clan, int SkillId, String Skill_name, Timestamp skilltime) {
		Updator.exec(
				"insert into clan_buff_list set clan_id=?, clan_name=?, skill_id=?, skill_name=?, remaining_time=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, clan.getClanId());
						pstm.setString(++idx, clan.getClanName());
						pstm.setInt(++idx, SkillId);
						pstm.setString(++idx, Skill_name);
						pstm.setTimestamp(++idx, skilltime);
					}
				});

		ClanBuffListInfo Info = new ClanBuffListInfo();
		Info.set_clan_id(clan.getClanId());
		Info.set_clan_name(clan.getClanName());
		Info.set_skill_id(SkillId);
		Info.set_skill_name(Skill_name);
		Info.set_remaining_time(skilltime);

		ArrayList<ClanBuffListInfo> inputList = _clan_buff_list.get(clan.getClanId());
		if (inputList == null) {
			inputList = new ArrayList<ClanBuffListInfo>();
			_clan_buff_list.put(new Integer(clan.getClanId()), inputList);
		}
		inputList.add(Info);

		_clan_buff_list.put(clan.getClanId(), inputList);
	}

	void deleteClanBuff(L1Clan clan, int SkillId) {
		Updator.exec("delete from clan_buff_list where clan_id=? and skill_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, clan.getClanId());
				pstm.setInt(++idx, SkillId);
			}
		});
	}

	public ArrayList<ClanBuffListInfo> getClanBuffList(L1Clan clan) {
		if (clan != null) {
			if (_clan_buff_list.containsKey(clan.getClanId())) {
				return _clan_buff_list.get(clan.getClanId());
			}
		}
		return null;
	}

	public ArrayList<ClanBuffItemInfo> values() {
		return new ArrayList<ClanBuffItemInfo>(_clan_buff_items.values());
	}

	public ClanBuffItemInfo getItemId(int itemid) {
		ArrayList<ClanBuffItemInfo> items = values();
		if (items == null)
			return null;

		for (ClanBuffItemInfo pInfo : items) {
			if (pInfo.getItemId() == itemid)
				return pInfo;
		}
		return null;
	}

	public boolean useItem(L1PcInstance pc, L1ItemInstance item) {
		ClanBuffItemInfo pInfo = getItemId(item.getItemId());
		if (pInfo == null)
			return false;

		if (insert_clan_buff(pc, pInfo))
			pc.getInventory().removeItem(item, 1);
		return true;
	}

	public boolean insert_clan_buff(L1PcInstance pc, ClanBuffItemInfo info) {
		L1Clan clan = pc.getClan();
		int skill_id = info.getSkillId();
		String skill_name = info.getSkillName();
		int skill_time = info.getSkillTime();
		Timestamp time = null;
		if (clan == null) {
			pc.sendPackets("血盟不存在。");
			return false;
		}

		ArrayList<ClanBuffListInfo> info_list = ClanBuffListLoader.getInstance().getClanBuffList(clan);
		if (info_list == null) {
			ArrayList<ClanBuffListInfo> inputList = _clan_buff_list.get(clan.getClanId());
			if (info_list == null) {
				info_list = new ArrayList<ClanBuffListInfo>();
				_clan_buff_list.put(new Integer(clan.getClanId()), inputList);
			}
		}

		for (ClanBuffListInfo buff_info : info_list) {
			if (skill_id == L1SkillId.CLAN_EXP_BUFF_1ST) {
				if (buff_info.get_skill_id() == L1SkillId.CLAN_EXP_BUFF_2ND) {
					// deleteClanBuff(clan, L1SkillId.CLAN_EXP_BUFF_2ND);
					pc.sendPackets("相同增益1級、2級不能重複套用");
					return false;
				}
			}

			if (skill_id == L1SkillId.CLAN_EXP_BUFF_2ND) {
				if (buff_info.get_skill_id() == L1SkillId.CLAN_EXP_BUFF_1ST) {
					// deleteClanBuff(clan, L1SkillId.CLAN_EXP_BUFF_1ST);
					pc.sendPackets("相同增益1級、2級不能重複套用");
					return false;
				}
			}

			if (skill_id == L1SkillId.CLAN_PVP_BUFF_1ST) {
				if (buff_info.get_skill_id() == L1SkillId.CLAN_PVP_BUFF_2ND) {
					// deleteClanBuff(clan, L1SkillId.CLAN_PVP_BUFF_2ND);
					pc.sendPackets("相同增益1級、2級不能重複套用");
					return false;
				}
			}

			if (skill_id == L1SkillId.CLAN_PVP_BUFF_2ND) {
				if (buff_info.get_skill_id() == L1SkillId.CLAN_PVP_BUFF_1ST) {
					// deleteClanBuff(clan, L1SkillId.CLAN_PVP_BUFF_1ST);
					pc.sendPackets("相同增益1級、2級不能重複套用");
					return false;
				}
			}

			if (skill_id == L1SkillId.CLAN_DEFENCE_BUFF_1ST) {
				if (buff_info.get_skill_id() == L1SkillId.CLAN_DEFENCE_BUFF_2ND) {
					// deleteClanBuff(clan, L1SkillId.CLAN_DEFENCE_BUFF_2ND);
					pc.sendPackets("相同增益1級、2級不能重複套用");
					return false;
				}
			}
			if (skill_id == L1SkillId.CLAN_DEFENCE_BUFF_2ND) {
				if (buff_info.get_skill_id() == L1SkillId.CLAN_DEFENCE_BUFF_1ST) {
					// deleteClanBuff(clan, L1SkillId.CLAN_DEFENCE_BUFF_1ST);
					pc.sendPackets("相同增益1級、2級不能重複套用");
					return false;
				}
			}

			if (buff_info.get_skill_id() == skill_id) {
				deleteClanBuff(clan, skill_id);
				pc.sendPackets("增益更新");
				// pc.sendPackets("相同增益1級、2級不能重複套用");
				// return false;
			}
		}

		time = new Timestamp(((3600000) * skill_time) + System.currentTimeMillis());
		for (L1PcInstance member : clan.getOnlineClanMember()) {
			skill_info(member, skill_id, (3600) * skill_time);
		}
		ClanBuffListLoader.getInstance().addClanBuff(clan, skill_id, skill_name, time);
		return true;
	}

	public void load_clan_buff(L1PcInstance pc) {
		L1Clan clan = pc.getClan();
		if (clan == null)
			return;

		ArrayList<ClanBuffListInfo> info_list = ClanBuffListLoader.getInstance().getClanBuffList(clan);
		long skill_time = 0;
		if (info_list == null)
			return;

		Iterator<ClanBuffListInfo> iter = info_list.iterator();
		while (iter.hasNext()) {
			ClanBuffListInfo info = iter.next();
			skill_time = (info.get_remaining_time().getTime() - System.currentTimeMillis()) / 1000;
			if (skill_time >= 0) {
				ClanBuffListLoader.getInstance().skill_info(pc, info.get_skill_id(), skill_time);
			} else {
				ClanBuffListLoader.getInstance().deleteClanBuff(clan, info.get_skill_id());
				iter.remove();
			}
		}
	}

	public void leave_clan_remove_buff(L1PcInstance pc) {
		ArrayList<ClanBuffItemInfo> skills = values();
		for (ClanBuffItemInfo pInfo : skills) {
			if (pc.hasSkillEffect(pInfo.getSkillId())) {
				pc.removeSkillEffect(pInfo.getSkillId());
			}
		}
	}

	public void remove_skill(L1PcInstance pc, int skillId) {
		L1Clan clan = pc.getClan();
		if (clan == null)
			return;

		ArrayList<ClanBuffListInfo> info_list = ClanBuffListLoader.getInstance().getClanBuffList(clan);
		if (info_list == null)
			return;

		Iterator<ClanBuffListInfo> iter = info_list.iterator();
		while (iter.hasNext()) {
			ClanBuffListInfo info = iter.next();
			if (info.get_skill_id() == skillId) {
				ClanBuffListLoader.getInstance().deleteClanBuff(clan, info.get_skill_id());
				iter.remove();
			}
		}
	}

	public void skill_info(L1PcInstance pc, int skillid, long skill_time) {
		if (pc.hasSkillEffect(skillid)) {
			pc.removeSkillEffect(skillid);
		}
		switch (skillid) {
			case L1SkillId.CLAN_EXP_BUFF_1ST:
				if (pc.getClanRank() == L1Clan.盟主 || pc.getClanRank() == L1Clan.副盟主) {
					pc.add_item_exp_bonus(15);
				} else if (pc.getClanRank() == L1Clan.守護騎士) {
					pc.add_item_exp_bonus(14);
				} else if (pc.getClanRank() == L1Clan.菁英) {
					pc.add_item_exp_bonus(13);
				} else if (pc.getClanRank() == L1Clan.一般) {
					pc.add_item_exp_bonus(10);
				}
				break;
			case L1SkillId.CLAN_EXP_BUFF_2ND:
				if (pc.getClanRank() == L1Clan.盟主 || pc.getClanRank() == L1Clan.副盟主) {
					pc.add_item_exp_bonus(35);
				} else if (pc.getClanRank() == L1Clan.守護騎士) {
					pc.add_item_exp_bonus(34);
				} else if (pc.getClanRank() == L1Clan.菁英) {
					pc.add_item_exp_bonus(33);
				} else if (pc.getClanRank() == L1Clan.一般) {
					pc.add_item_exp_bonus(30);
				}
				break;
			case L1SkillId.CLAN_PVP_BUFF_1ST:
				if (pc.getClanRank() == L1Clan.盟主 || pc.getClanRank() == L1Clan.副盟主 || pc.getClanRank() == L1Clan.菁英
						|| pc.getClanRank() == L1Clan.守護騎士) {
					// 生命值+50，命中率+2，附加傷害+1，魔力+3，魔法命中率+1
					pc.addMagicHit(1);
				}
				// 生命值+50，命中+2，附加傷害+1，魔法+3
				pc.addMaxHp(50);
				pc.addDmgup(1);
				pc.addBowDmgup(1);
				pc.addHitup(2);
				pc.addBowHitup(2);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				break;
			case L1SkillId.CLAN_PVP_BUFF_2ND:
				if (pc.getClanRank() == L1Clan.盟主 || pc.getClanRank() == L1Clan.副盟主 || pc.getClanRank() == L1Clan.菁英
						|| pc.getClanRank() == L1Clan.守護騎士) {
					pc.addMagicHit(1);
					pc.addHitup(1);
					pc.addBowHitup(1);
				}
				// 生命值+100，命中+3，附加傷害+2，所有命中+1
				pc.addMaxHp(100);
				pc.addDmgup(2);
				pc.addBowDmgup(2);
				pc.addHitup(2);
				pc.addBowHitup(2);
				pc.addSpecialPierce(eKind.ALL, 1);

				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));

				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				break;
			case L1SkillId.CLAN_DEFENCE_BUFF_1ST:
				if (pc.getClanRank() == L1Clan.盟主 || pc.getClanRank() == L1Clan.副盟主 || pc.getClanRank() == L1Clan.菁英
						|| pc.getClanRank() == L1Clan.守護騎士) {
					pc.addDamageReductionByArmor(1);
					pc.addMaxHp(50);
				}
				// 防禦+1，減傷+1，魔防+5
				pc.getAC().addAc(-1);
				pc.addDamageReductionByArmor(1);
				pc.addMr(5);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				break;
			case L1SkillId.CLAN_DEFENCE_BUFF_2ND:
				// 防禦+2，減傷+2，魔防+10，全抗性+1
				if (pc.getClanRank() == L1Clan.盟主 || pc.getClanRank() == L1Clan.副盟主 || pc.getClanRank() == L1Clan.菁英
						|| pc.getClanRank() == L1Clan.守護騎士騎) {
					pc.addDamageReductionByArmor(1);
					pc.addMaxHp(50);
				}
				pc.getAC().addAc(-2);
				pc.addDamageReductionByArmor(2);
				pc.addMr(10);
				pc.addSpecialResistance(eKind.ALL, 1);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				break;
			default:
				return;
		}
		L1SkillUse.on_icons(pc, skillid, (int) skill_time);
		pc.setSkillEffect(skillid, skill_time * 1000);
	}

}
