package MJShiftObject.Object.Converter.Selector;

import MJShiftObject.DB.MJEShiftDBName;
import MJShiftObject.Template.CharacterBuffInfo;
import MJShiftObject.Template.CharacterConfigInfo;
import MJShiftObject.Template.CharacterPassiveInfo;
import MJShiftObject.Template.CharacterQuestInfo;
import MJShiftObject.Template.CharacterSkillInfo;
import MJShiftObject.Template.CharacterSlotInfo;
import MJShiftObject.Template.CharacterTamInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import l1j.server.Config;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.Account;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.templates.L1Item;


public class MJShiftObjectSelector {
	private String m_server_identity;
	private Selector m_selector;

	public static MJShiftObjectSelector newInstance(String server_identity, Selector selector) {
		MJShiftObjectSelector sSelector = newInstance();
		sSelector.m_server_identity = server_identity;
		sSelector.m_selector = selector;
		return sSelector;
	}

	private static MJShiftObjectSelector newInstance() {
		return new MJShiftObjectSelector();
	}


	public String get_server_identity() {
		return this.m_server_identity;
	}

	public L1PcInstance select_character_info(final int object_id) {
		final MJObjectWrapper<L1PcInstance> wrapper = new MJObjectWrapper();
		this.m_selector.execute(String.format("select * from %s where objid=?", new Object[]{MJEShiftDBName.CHARACTERS.get_table_name(this.m_server_identity)}), (Handler) new SelectorHandler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}


			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					L1PcInstance pc = (L1PcInstance) (wrapper.value = new L1PcInstance());
					pc.setAccountName(rs.getString("account_name"));
					pc.setId(rs.getInt("objid"));
					pc.setName(rs.getString("char_name"));
					pc.setHighLevel(rs.getInt("HighLevel"));
					pc.set_exp(rs.getLong("Exp"));
					pc.addBaseMaxHp(rs.getShort("MaxHp"));
					pc.setCurrentHp((short) Math.max(rs.getInt("CurHp"), 1));
					pc.setDead(false);
					pc.setStatus(0);
					pc.addBaseMaxMp(rs.getShort("MaxMp"));
					pc.setCurrentMp(rs.getShort("CurMp"));
					pc.getAbility().setBaseStr(rs.getByte("BaseStr"));
					pc.getAbility().setStr(rs.getByte("Str"));
					pc.getAbility().setBaseCon(rs.getByte("BaseCon"));
					pc.getAbility().setCon(rs.getByte("Con"));
					pc.getAbility().setBaseDex(rs.getByte("BaseDex"));
					pc.getAbility().setDex(rs.getByte("Dex"));
					pc.getAbility().setBaseCha(rs.getByte("BaseCha"));
					pc.getAbility().setCha(rs.getByte("Cha"));
					pc.getAbility().setBaseInt(rs.getByte("BaseIntel"));
					pc.getAbility().setInt(rs.getByte("Intel"));
					pc.getAbility().setBaseWis(rs.getByte("BaseWis"));
					pc.getAbility().setWis(rs.getByte("Wis"));
					pc.setCurrentWeapon(rs.getInt("Status"));
					int classId = rs.getInt("Class");
					pc.setClassId(classId);
					pc.setCurrentSprite(classId);
					pc.set_sex(rs.getInt("Sex"));
					pc.setType(rs.getInt("Type"));
					pc.setHeading(rs.getInt("Heading") % 8);
					pc.setX(rs.getInt("locX"));
					pc.setY(rs.getInt("locY"));
					pc.setMap(rs.getShort("MapID"));
					pc.set_food(rs.getInt("Food"));
					pc.setLawful(rs.getInt("Lawful"));
					pc.setElixirStats(rs.getInt("ElixirStatus"));
					pc.setElfAttr(rs.getInt("ElfAttr"));
					pc.setGlory_Earth_Attr(rs.getInt("GloryEarthAttr"));
					pc.set_PKcount(rs.getInt("PKcount"));
					pc.set_exp_res(rs.getLong("ExpRes"));
					pc.setPartnerId(rs.getInt("PartnerID"));
					pc.setAccessLevel(rs.getShort("AccessLevel"));
					pc.setGm((pc.getAccessLevel() == Config.ServerAdSetting.GMCODE));
					pc.setMonitor(false);
					pc.setOnlineStatus(rs.getInt("OnlineStatus"));
					pc.setHomeTownId(rs.getInt("HomeTownID"));
					pc.setContribution(rs.getInt("Contribution"));
					pc.setHellTime(rs.getInt("HellTime"));
					pc.setBanned(rs.getBoolean("Banned"));
					pc.setKarma(rs.getInt("Karma"));
					pc.setLastPk(rs.getTimestamp("LastPk"));
					pc.setDeleteTime(rs.getTimestamp("DeleteTime"));
					pc.setReturnStat(rs.getInt("ReturnStat"));
					pc.setSealingPW(rs.getString("sealingPW"));
					pc.setSealScrollTime(rs.getInt("sealScrollTime"));
					pc.setSealScrollCount(rs.getInt("sealScrollCount"));
					pc.setLastLoginTime(rs.getTimestamp("lastLoginTime"));
					pc.setAinState(rs.getInt("AinState"));
					pc.setSurvivalGauge(rs.getInt("SurvivalGauge"));
					pc.setMark_count(rs.getInt("Mark_Count"));
					pc.setAge(rs.getInt("Age"));
					pc.setAddDamage(rs.getInt("AddDamage"));
					pc.setAddDamageRate(rs.getInt("AddDamageRate"));
					pc.setAddReduction(rs.getInt("AddReduction"));
					pc.setAddReductionRate(rs.getInt("AddReductionRate"));
					pc.setTamTime(rs.getTimestamp("TamEndTime"));
					pc.set_SpecialSize(rs.getInt("SpecialSize"));
					pc.setSlotNumber(rs.getInt("slot_number"));
					pc.setElfAttrInitCount(rs.getInt("elf_attrinit_count"));
					pc.setLastTopBless(rs.getTimestamp("last_topBless"));

					pc.refresh();
					pc.setMoveSpeed(0);
					pc.setBraveSpeed(0);
					pc.setGmInvis(false);
					try {
						pc.setFishingShopBuyTime_1(rs.getTimestamp("FishingShopBuyTime_1").getTime());
					} catch (Exception e) {
						pc.setFishingShopBuyTime_1(0L);
					}
				}
			}
		});
		return (L1PcInstance) wrapper.value;
	}

	public Account select_accounts(final String account_name) {
		final MJObjectWrapper<Account> wrapper = new MJObjectWrapper();
		this.m_selector.execute(String.format("select * from %s where login=?", new Object[]{MJEShiftDBName.ACCOUNTS.get_table_name(this.m_server_identity)}), (Handler) new SelectorHandler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, account_name);
			}


			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					Account account = (Account) (wrapper.value = new Account());
					account.setName(rs.getString("login"));
					account.setPassword(rs.getString("password"));
					account.setLastActive(rs.getTimestamp("lastactive"));
					account.setAccessLevel(rs.getInt("access_level"));
					account.setIp(rs.getString("ip"));
					account.setHost(rs.getString("host"));
					account.setBannedCode(rs.getInt("banned"));
					account.setCharSlot(rs.getInt("charslot"));
					account.setGamePassword(rs.getInt("gamepassword"));
					account.setphone(rs.getString("phone"));
					account.setTamPoint(rs.getInt("Tam_Point"));
					account._Buff_PC방 = rs.getTimestamp("BUFF_PCROOM_Time");
					account.setCPW(rs.getString("CharPassword"));
					account.Ncoin_point = rs.getInt("Ncoin_Point");
					account.Shop_open_count = rs.getInt("Shop_open_count");
					account.setDragonRaid(rs.getTimestamp("raid_buff"));
					account.setBlessOfAin(rs.getInt("bless_of_ain"), null);
					account.setBlessOfAinCharge(rs.getInt("bless_of_ain_charge"));
					account.setBlessOfAinBonusPoint(rs.getInt("bless_of_ain_bonus_point"));
					account.setLastLogOut(rs.getTimestamp("last_log_out"));
					account.read_fatigue_resultset(rs);
				}
			}
		});
		return (Account) wrapper.value;
	}

	public Account select_accounts_for_returner(final String account_name) {
		final MJObjectWrapper<Account> wrapper = new MJObjectWrapper();
		this.m_selector.execute(String.format("select * from %s where login=?", new Object[]{MJEShiftDBName.ACCOUNTS.get_table_name(this.m_server_identity)}), (Handler) new SelectorHandler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, account_name);
			}


			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					Account account = (Account) (wrapper.value = new Account());
					account.setAccountId(rs.getInt("id"));
					account.setName(rs.getString("login"));
					account.setPassword(rs.getString("password"));
					account.setLastActive(rs.getTimestamp("lastactive"));
					account.setAccessLevel(rs.getInt("access_level"));
					account.setIp(rs.getString("ip"));
					account.setHost(rs.getString("host"));
					account.setBannedCode(rs.getInt("banned"));
					account.setCharSlot(rs.getInt("charslot"));
					account.setGamePassword(rs.getInt("gamepassword"));
					account.setphone(rs.getString("phone"));
					account.setTamPoint(rs.getInt("Tam_Point"));
					account._Buff_PC방 = rs.getTimestamp("BUFF_PCROOM_Time");
					account.setCPW(rs.getString("CharPassword"));
					account.Ncoin_point = rs.getInt("Ncoin_Point");
					account.Shop_open_count = rs.getInt("Shop_open_count");
					account.setDragonRaid(rs.getTimestamp("raid_buff"));
					account.setBlessOfAin(rs.getInt("bless_of_ain"), null);
					account.setBlessOfAinCharge(rs.getInt("bless_of_ain_charge"));
					account.setBlessOfAinBonusPoint(rs.getInt("bless_of_ain_bonus_point"));
					account.setLastLogOut(rs.getTimestamp("last_log_out"));
					account.read_fatigue_resultset(rs);
				}
			}
		});
		return (Account) wrapper.value;
	}

	public List<CharacterBuffInfo> select_character_buff(final int object_id) {
		final MJObjectWrapper<ArrayList<CharacterBuffInfo>> wrapper = new MJObjectWrapper();
		wrapper.value = new ArrayList();
		this.m_selector.execute(String.format("select * from %s where char_obj_id=?", new Object[]{MJEShiftDBName.CHARACTERS_BUFF.get_table_name(this.m_server_identity)}), (Handler) new SelectorHandler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}


			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					CharacterBuffInfo bInfo = new CharacterBuffInfo();
					bInfo.poly_id = rs.getInt("poly_id");
					bInfo.skill_id = rs.getInt("skill_id");
					bInfo.time_sec = rs.getInt("remaining_time");
					((ArrayList<CharacterBuffInfo>) wrapper.value).add(bInfo);
				}
			}
		});
		return (List<CharacterBuffInfo>) wrapper.value;
	}

	public CharacterConfigInfo select_character_config(final int object_id) {
		final MJObjectWrapper<CharacterConfigInfo> wrapper = new MJObjectWrapper();
		this.m_selector.execute(String.format("select * from %s where object_id=?", new Object[]{MJEShiftDBName.CHARACTERS_CONFIG.get_table_name(this.m_server_identity)}), (Handler) new SelectorHandler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}


			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					wrapper.value = new CharacterConfigInfo();
					((CharacterConfigInfo) wrapper.value).length = rs.getInt("length");
					((CharacterConfigInfo) wrapper.value).buff = rs.getBytes("data");
				}
			}
		});
		return (CharacterConfigInfo) wrapper.value;
	}

	public List<L1ItemInstance> select_character_items(final int object_id) {
		final MJObjectWrapper<List<L1ItemInstance>> wrapper = new MJObjectWrapper();
		wrapper.value = new ArrayList();
		this.m_selector.execute(String.format("select * from %s where char_id=?", new Object[]{MJEShiftDBName.CHARACTERS_ITEMS
				.get_table_name(this.m_server_identity)}), (Handler) new SelectorHandler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}


			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					int item_id = rs.getInt("item_id");
					L1Item template = ItemTable.getInstance().getTemplate(item_id);
					if (template == null) {
						System.out.println(String.format("如果伺服器之間缺少某個項目，則該項目將被取消。 ID：%d", new Object[]{Integer.valueOf(item_id)}));
						continue;
					}
					L1ItemInstance item = new L1ItemInstance();
					item.setId(rs.getInt("id"));
					item.setItem(template);
					item.setCount(rs.getInt("count"));
					item.setEquipped((rs.getInt("is_equipped") != 0));
					item.setEnchantLevel(rs.getInt("enchantlvl"));
					item.setIdentified((rs.getInt("is_id") != 0));
					item.set_durability(rs.getInt("durability"));
					item.setChargeCount(rs.getInt("charge_count"));
					item.setRemainingTime(rs.getInt("remaining_time"));
					item.setLastUsed(rs.getTimestamp("last_used"));
					item.setBless(rs.getInt("bless"));
					item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
					item.setEndTime(rs.getTimestamp("end_time"));
					item.setPackage((rs.getInt("package") != 0));
					item.set_bless_level(rs.getInt("bless_level"));
					item.set_item_level(rs.getInt("item_level"));
					item.setHotel_Town(rs.getString("Hotel_Town"));
					item.setSupportItem((rs.getInt("is_support_item") != 0));
					item.set_Carving(rs.getInt("Carving"));
					item.set_Doll_Bonus_Level(rs.getInt("doll_bonus_level"));
					item.set_Doll_Bonus_Value(rs.getInt("doll_bonus_value"));
					item.setBlessType(rs.getInt("bless_type"));
					item.setBlessTypeValue(rs.getInt("bless_type_value"));
					((List<L1ItemInstance>) wrapper.value).add(item);
				}
			}
		});
		return (List<L1ItemInstance>) wrapper.value;
	}

	public List<L1ItemInstance> select_character_items_euqipped(final int object_id) {
		final MJObjectWrapper<List<L1ItemInstance>> wrapper = new MJObjectWrapper();
		wrapper.value = new ArrayList();
		L1Object obj = L1World.getInstance().findObject(object_id);
		if (obj != null && obj instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) obj;
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (item.isEquipped())
					((List<L1ItemInstance>) wrapper.value).add(item);
			}
			if (((List) wrapper.value).size() > 0) {
				return (List<L1ItemInstance>) wrapper.value;
			}
		}
		this.m_selector.execute(String.format("select * from %s where char_id=? and is_equipped=1", new Object[]{MJEShiftDBName.CHARACTERS_ITEMS
				.get_table_name(this.m_server_identity)}), (Handler) new SelectorHandler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}


			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					int item_id = rs.getInt("item_id");
					L1Item template = ItemTable.getInstance().getTemplate(item_id);
					if (template == null) {
						System.out.println(String.format("如果伺服器之間缺少某個項目，則該項目將被取消。 ID：%d", new Object[]{Integer.valueOf(item_id)}));
						continue;
					}
					L1ItemInstance item = new L1ItemInstance();
					item.setId(rs.getInt("id"));
					item.setItem(template);
					item.setCount(rs.getInt("count"));
					item.setEquipped((rs.getInt("is_equipped") != 0));
					item.setEnchantLevel(rs.getInt("enchantlvl"));
					item.setIdentified((rs.getInt("is_id") != 0));
					item.set_durability(rs.getInt("durability"));
					item.setChargeCount(rs.getInt("charge_count"));
					item.setRemainingTime(rs.getInt("remaining_time"));
					item.setLastUsed(rs.getTimestamp("last_used"));
					item.setBless(rs.getInt("bless"));
					item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
					item.setEndTime(rs.getTimestamp("end_time"));
					item.setPackage((rs.getInt("package") != 0));
					item.set_bless_level(rs.getInt("bless_level"));
					item.set_item_level(rs.getInt("item_level"));
					item.setHotel_Town(rs.getString("Hotel_Town"));
					item.setSupportItem((rs.getInt("is_support_item") != 0));
					item.set_Carving(rs.getInt("Carving"));
					item.set_Doll_Bonus_Level(rs.getInt("doll_bonus_level"));
					item.set_Doll_Bonus_Value(rs.getInt("doll_bonus_value"));
					item.setBlessType(rs.getInt("bless_type"));
					item.setBlessTypeValue(rs.getInt("bless_type_value"));
					((List<L1ItemInstance>) wrapper.value).add(item);
				}
			}
		});
		return (List<L1ItemInstance>) wrapper.value;
	}

	public List<CharacterSkillInfo> select_character_skills(final int object_id) {
		final MJObjectWrapper<ArrayList<CharacterSkillInfo>> wrapper = new MJObjectWrapper();
		wrapper.value = new ArrayList();
		this.m_selector.execute(String.format("select * from %s WHERE char_obj_id=?", new Object[]{MJEShiftDBName.CHARACTERS_SKILLS

				.get_table_name(this.m_server_identity)}), (Handler) new SelectorHandler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}


			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					CharacterSkillInfo csInfo = new CharacterSkillInfo();
					csInfo.skill_id = rs.getInt("skill_id");
					csInfo.skill_name = rs.getString("skill_name");
					csInfo.is_active = (rs.getInt("is_active") != 0);
					csInfo.active_time_left = rs.getInt("activetimeleft");
					((ArrayList<CharacterSkillInfo>) wrapper.value).add(csInfo);
				}
			}
		});
		return (List<CharacterSkillInfo>) wrapper.value;
	}

	public List<CharacterPassiveInfo> select_character_passive(final int object_id) {
		final MJObjectWrapper<List<CharacterPassiveInfo>> wrapper = new MJObjectWrapper();
		wrapper.value = new ArrayList();
		this.m_selector.execute(String.format("select * from %s where character_id=?", new Object[]{MJEShiftDBName.PASSIVE_USER_INFO
				.get_table_name(this.m_server_identity)}), (Handler) new SelectorHandler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}


			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					CharacterPassiveInfo pInfo = new CharacterPassiveInfo();
					pInfo.passive_id = rs.getInt("passive_id");
					pInfo.passive_name = rs.getString("passive_name");
					((List<CharacterPassiveInfo>) wrapper.value).add(pInfo);
				}
			}
		});
		return (List<CharacterPassiveInfo>) wrapper.value;
	}

	public List<CharacterSlotInfo> select_character_slot(final int object_id) {
		final MJObjectWrapper<ArrayList<CharacterSlotInfo>> wrapper = new MJObjectWrapper();
		wrapper.value = new ArrayList();
		this.m_selector.execute(
				String.format("select * from %s where char_id=?", new Object[]{

						MJEShiftDBName.CHARACTERS_SLOT_ITEMS.get_table_name(this.m_server_identity)
				}), (Handler) new SelectorHandler() {
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, object_id);
					}


					public void result(ResultSet rs) throws Exception {
						while (rs.next()) {
							CharacterSlotInfo csInfo = new CharacterSlotInfo();
							csInfo.source_item_id = rs.getInt("item_objid");
							csInfo.slot_number = rs.getInt("slot_number");
							((ArrayList<CharacterSlotInfo>) wrapper.value).add(csInfo);
						}
					}
				});
		return (List<CharacterSlotInfo>) wrapper.value;
	}

	public List<CharacterTamInfo> select_character_tams(final int object_id, final int account_id) {
		final MJObjectWrapper<ArrayList<CharacterTamInfo>> wrapper = new MJObjectWrapper();
		wrapper.value = new ArrayList();
		this.m_selector.execute(
				String.format("select * from %s where char_id=? or account_id=?", new Object[]{

						MJEShiftDBName.CHARACTERS_TAMS.get_table_name(this.m_server_identity)
				}), (Handler) new SelectorHandler() {
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, object_id);
						pstm.setInt(2, account_id);
					}


					public void result(ResultSet rs) throws Exception {
						while (rs.next()) {
							CharacterTamInfo tInfo = new CharacterTamInfo();
							tInfo.account_id = rs.getInt("account_id");
							tInfo.character_id = object_id;
							tInfo.character_name = rs.getString("char_name");
							tInfo.skill_id = rs.getInt("skill_id");
							tInfo.expiration_time = rs.getTimestamp("expiration_time");
							tInfo.reserve = rs.getInt("reserve");
							((ArrayList<CharacterTamInfo>) wrapper.value).add(tInfo);
						}
					}
				});
		return (List<CharacterTamInfo>) wrapper.value;
	}

	public Integer select_character_level_bonus(final int object_id) {
		final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper();
		wrapper.value = Integer.valueOf(0);
		this.m_selector.execute(String.format("select * from %s where objid=?", new Object[]{MJEShiftDBName.CHARACTERS_BONUS
				.get_table_name(this.m_server_identity)}), (Handler) new SelectorHandler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}


			public void result(ResultSet rs) throws Exception {
				if (rs.next())
					wrapper.value = Integer.valueOf(rs.getInt("level"));
			}
		});
		return (Integer) wrapper.value;
	}

	public List<CharacterQuestInfo> select_character_quest_info(final int object_id) {
		final MJObjectWrapper<ArrayList<CharacterQuestInfo>> wrapper = new MJObjectWrapper();
		wrapper.value = new ArrayList();
		this.m_selector.execute(
				String.format("select * from %s where char_id=?", new Object[]{

						MJEShiftDBName.CHARACTERS_QUEST.get_table_name(this.m_server_identity)
				}), (Handler) new SelectorHandler() {
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, object_id);
					}


					public void result(ResultSet rs) throws Exception {
						while (rs.next()) {
							CharacterQuestInfo qInfo = new CharacterQuestInfo();
							qInfo.quest_id = rs.getInt("quest_id");
							qInfo.quest_step = rs.getInt("quest_step");
							((ArrayList<CharacterQuestInfo>) wrapper.value).add(qInfo);
						}
					}
				});
		return (List<CharacterQuestInfo>) wrapper.value;
	}
}


