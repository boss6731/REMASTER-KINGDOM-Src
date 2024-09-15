package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.FindItemTable.TableFindItemFilter;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin.SC_CRAFT_LIST_ALL_ACK.Craft.CraftOutputList.CraftOutputSFList.CraftOutputItem;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_DESIGN_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SYNTHESIS_SMELTING_DESIGN_ACK;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.PowerBall.PowerBallController;
import l1j.server.PowerBall.PowerBallTicket;
import l1j.server.server.IdFactory;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
//import l1j.server.server.model.item.function.ItemSelector.SelectorType;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1RaceTicket;
import l1j.server.server.templates.L1Weapon;
import l1j.server.server.templates.WareHouseLeaveType;
import l1j.server.server.utils.SQLUtil;

public class ItemTable {
	private static Logger _log = Logger.getLogger(ItemTable.class.getName());

	private static ItemTable _instance;
	private HashMap<Integer, L1Item> _allTemplates;

	public HashMap<Integer, L1Item> getAllTemplates() {
		return _allTemplates;
	}

	public final Map<Integer, L1EtcItem> _etcitems;
	public final Map<Integer, L1Armor> _armors;
	public final Map<Integer, L1Weapon> _weapons;
	public final ConcurrentHashMap<Integer, List<L1Item>> _descCached;
	/** 添加盾牆 **/
	private final Map<Integer, L1RaceTicket> _race = new HashMap<Integer, L1RaceTicket>(64);

	public static ItemTable getInstance() {
		if (_instance == null) {
			_instance = new ItemTable();
		}
		return _instance;
	}

	private ItemTable() {
		_etcitems = allEtcItem();
		_weapons = allWeapon();
		_armors = allArmor();
		_descCached = new ConcurrentHashMap<Integer, List<L1Item>>(64);

		buildFastLookupTable();
	}

	public static void reload() {
		ItemTable oldInstance = _instance;
		ItemTable newInstance = new ItemTable();
		newInstance.subLoad();
		_instance = newInstance;
		oldInstance._etcitems.clear();
		oldInstance._weapons.clear();
		oldInstance._armors.clear();
		oldInstance._allTemplates.clear();
		oldInstance._descCached.clear();
	}

	private Map<Integer, L1EtcItem> allEtcItem() {
		Map<Integer, L1EtcItem> result = new HashMap<Integer, L1EtcItem>(5120);

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1EtcItem item = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from etcitem");

			rs = pstm.executeQuery();
			while (rs.next()) {
				item = new L1EtcItem();
				item.setItemId(rs.getInt("item_id"));
				item.setItemDescId(rs.getInt("desc_id"));
				item.setGfxId(rs.getInt("icon_id"));
				item.setGroundGfxId(rs.getInt("sprite"));
				item.setNameId(rs.getString("real_name_id"));
				item.setName(rs.getString("real_name_id_view"));
				item.setNameView(rs.getString("name_id_view"));
				item.setType(parseEtcType(rs.getString("item_type")));
				item.setUseType(parseUseType(rs.getString("use_type")));
				item.setType2(0);
				item.setMaterial(parseMaterial(rs.getString("material")));
				item.setUseRoyal(rs.getInt("use_royal") == 0 ? false : true);
				item.setUseKnight(rs.getInt("use_knight") == 0 ? false : true);
				item.setUseElf(rs.getInt("use_elf") == 0 ? false : true);
				item.setUseMage(rs.getInt("use_mage") == 0 ? false : true);
				item.setUseDarkelf(rs.getInt("use_darkelf") == 0 ? false : true);
				item.setUseDragonKnight(rs.getInt("use_dragonknight") == 0 ? false : true);
				item.setUseBlackwizard(rs.getInt("use_blackwizard") == 0 ? false : true);
				item.setUseWarrior(rs.getInt("use_warrior") == 0 ? false : true);
				item.setUseFencer(rs.getInt("use_fencer") == 0 ? false : true);
				item.setUseLancer(rs.getInt("use_lancer") == 0 ? false : true);
				item.setWeight(rs.getInt("weight_1000_ea"));
				item.setMinLevel(rs.getInt("min_lvl"));
				item.setMaxLevel(rs.getInt("max_lvl"));
				item.setBless(rs.getInt("bless"));
				item.setTradable(rs.getInt("trade") == 0 ? true : false);
				item.setCantDelete(rs.getInt("cant_delete") == 1 ? true : false);
				item.setHitModifier(rs.getInt("hit"));
				item.setDmgModifier(rs.getInt("dmg"));
				item.set_stackable(rs.getInt("stackable") == 1 ? true : false);
				item.setMaxChargeCount(rs.getInt("max_charge_count"));
				item.set_locx(rs.getInt("locx"));
				item.set_locy(rs.getInt("locy"));
				item.set_mapid(rs.getShort("mapid"));
				item.set_delayid(rs.getInt("delay_id"));
				item.set_delaytime(rs.getInt("delay_time"));
				item.set_delayEffect(rs.getInt("delay_effect"));
				item.setFoodVolume(rs.getInt("food_volume"));
				item.setToBeSavedAtOnce((rs.getInt("save_at_once") == 1) ? true : false);
				item.setEndedTimeMessage(rs.getBoolean("isEndedTimeMessage"));
				item.setUseEffetId(rs.getInt("use_effect_id"));
				item.setOverlaySurfId(rs.getInt("overlay_surf_id"));
				// item.setNoWareHouse((rs.getInt("no_warehouse") == 1) ? true : false);
				item.setWareHouseLimitType(WareHouseLeaveType.fromString(rs.getString("warehouse_type")));
				item.setWareHouseLimitLevel(rs.getInt("warehouse_limit_enchant"));

				if (Config.ServerAdSetting.IsValidItemId) {
					if (_weapons != null && _weapons.containsKey(item.getItemId()))
						System.out.println(String.format("ETCITEM %d(%s) 與武器重疊。", item.getItemId(), item.getName()));
					if (_armors != null && _armors.containsKey(item.getItemId()))
						System.out.println(String.format("ETCITEM %d(%s) 與防具重疊。", item.getItemId(), item.getName()));
				}
				result.put(new Integer(item.getItemId()), item);
			}
		} catch (NullPointerException e) {
			_log.log(Level.SEVERE, new StringBuilder().append(item.getName()).append("(" + item.getItemId() + ")").append(" 的讀取失敗。").toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	private Map<Integer, L1Weapon> allWeapon() {
		Map<Integer, L1Weapon> result = new HashMap<Integer, L1Weapon>(1024);

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Weapon weapon = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from weapon");

			rs = pstm.executeQuery();
			while (rs.next()) {
				weapon = new L1Weapon();
				weapon.setItemId(rs.getInt("item_id"));
				weapon.setItemDescId(rs.getInt("desc_id"));
				weapon.setGfxId(rs.getInt("icon_id"));
				weapon.setGroundGfxId(rs.getInt("sprite"));
				weapon.setNameId(rs.getString("real_name_id"));
				weapon.setName(rs.getString("real_name_id_view"));
				weapon.setNameView(rs.getString("name_id_view"));
				weapon.setType(parseWeaponType(rs.getString("type")));
				weapon.setType1(parseWeaponId(rs.getString("type")));
				weapon.setType2(1);
				weapon.setUseType(1);
				weapon.setMaterial(parseMaterial(rs.getString("material")));
				weapon.setWeight(rs.getInt("weight_1000_ea"));
				weapon.setDmgSmall(rs.getInt("dmg_small"));
				weapon.setDmgLarge(rs.getInt("dmg_large"));
				weapon.set_safeenchant(rs.getInt("safenchant"));
				weapon.setUseRoyal(rs.getInt("use_royal") == 0 ? false : true);
				weapon.setUseKnight(rs.getInt("use_knight") == 0 ? false : true);
				weapon.setUseElf(rs.getInt("use_elf") == 0 ? false : true);
				weapon.setUseMage(rs.getInt("use_mage") == 0 ? false : true);
				weapon.setUseDarkelf(rs.getInt("use_darkelf") == 0 ? false : true);
				weapon.setUseDragonKnight(rs.getInt("use_dragonknight") == 0 ? false : true);
				weapon.setUseBlackwizard(rs.getInt("use_blackwizard") == 0 ? false : true);
				weapon.setUseWarrior(rs.getInt("use_warrior") == 0 ? false : true);
				weapon.setUseFencer(rs.getInt("use_fencer") == 0 ? false : true);
				weapon.setUseLancer(rs.getInt("use_lancer") == 0 ? false : true);
				weapon.setHitModifier(rs.getInt("hitmodifier"));
				weapon.setDmgModifier(rs.getInt("dmgmodifier"));
				weapon.set_adddex(rs.getByte("add_dex"));
				weapon.set_addstr(rs.getByte("add_str"));
				weapon.set_addint(rs.getByte("add_int"));
				weapon.set_addwis(rs.getByte("add_wis"));
				weapon.set_addcon(rs.getByte("add_con"));
				weapon.set_addcha(rs.getByte("add_cha"));
				weapon.set_addhp(rs.getInt("add_hp"));
				weapon.set_addmp(rs.getInt("add_mp"));
				weapon.set_addhpr(rs.getInt("add_hpr"));
				weapon.set_addmpr(rs.getInt("add_mpr"));
				weapon.set_addsp(rs.getInt("add_sp"));
				weapon.setAddExp(rs.getInt("add_exp"));
				weapon.set_mdef(rs.getInt("m_def"));
				weapon.setDoubleDmgChance(rs.getInt("double_dmg_chance"));
				weapon.set_double_dmg_enchant_value(rs.getInt("double_dmg_enchant_value"));
				weapon.set_weak_point_chance(rs.getInt("weak_point_chance"));
				weapon.set_weak_point_enchant_value(rs.getInt("weak_point_enchant_value"));
				weapon.setMagicDmgModifier(rs.getInt("magicdmgmodifier"));
				weapon.set_canbedmg(rs.getInt("canbedmg"));
				weapon.setMinLevel(rs.getInt("min_lvl"));
				weapon.setMaxLevel(rs.getInt("max_lvl"));
				weapon.setBless(rs.getInt("bless"));
				weapon.setTradable(rs.getInt("trade") == 0 ? true : false);
				weapon.setCantDelete(rs.getInt("cant_delete") == 1 ? true : false);
				weapon.setHasteItem(rs.getInt("haste_item") == 0 ? false : true);
				weapon.setMaxUseTime(rs.getInt("max_use_time"));
				weapon.setMagicName(rs.getString("Magic_name"));
				weapon.setEndedTimeMessage(rs.getBoolean("isEndedTimeMessage"));
				weapon.set_missile_critical_probability(rs.getInt("missile_critical_probability"));
				weapon.set_melee_critical_probability(rs.getInt("melee_critical_probability"));
				weapon.set_magic_critical_probability(rs.getInt("magic_critical_probability"));
				weapon.setWeaponReductionCancel(rs.getInt("add_reduction_cancel"));
				weapon.setPVPWeaponReductionCancel(rs.getInt("PVPDmgReducIgnore"));
				weapon.setPVPMagicReduction(rs.getInt("PVPMdmgReduction"));
				weapon.setPVPMagicReductionCancel(rs.getInt("PVPMdmgReducIgnore"));
				weapon.setIIg(rs.getInt("ImmuneIgnore"));
				weapon.setSpecialResistance(eKind.ABILITY, rs.getInt("ability_resis"));
				weapon.setSpecialPierce(eKind.ABILITY, rs.getInt("ability_pierce"));
				weapon.setSpecialResistance(eKind.SPIRIT, rs.getInt("spirit_resis"));
				weapon.setSpecialPierce(eKind.SPIRIT, rs.getInt("spirit_pierce"));
				weapon.setSpecialResistance(eKind.DRAGON_SPELL, rs.getInt("dragonS_resis"));
				weapon.setSpecialPierce(eKind.DRAGON_SPELL, rs.getInt("dragonS_pierce"));
				weapon.setSpecialResistance(eKind.FEAR, rs.getInt("fear_resis"));
				weapon.setSpecialPierce(eKind.FEAR, rs.getInt("fear_pierce"));
				weapon.setSpecialResistance(eKind.ALL, rs.getInt("all_resis"));
				weapon.setSpecialPierce(eKind.ALL, rs.getInt("all_pierce"));
				weapon.setTitanPercent(rs.getInt("titan_percent"));
				weapon.setMagicHitup(rs.getInt("magic_hit_up"));
				weapon.setOverlaySurfId(rs.getInt("overlay_surf_id"));
				// weapon.setNoWareHouse((rs.getInt("no_warehouse") == 1) ? true : false);
				weapon.setWareHouseLimitType(WareHouseLeaveType.fromString(rs.getString("warehouse_type")));
				weapon.setWareHouseLimitLevel(rs.getInt("warehouse_limit_enchant"));

				// XXX 未來使用時取消註解 - 源碼處理完成
				weapon.setAttackDelayRate(rs.getInt("attack_speed"));
				weapon.setMoveDelayRate(rs.getInt("move_speed"));

				if (Config.ServerAdSetting.IsValidItemId) {
					if (_etcitems != null && _etcitems.containsKey(weapon.getItemId()))
						System.out.println(String.format("武器 %d(%s) 與 etcItem 重疊。", weapon.getItemId(), weapon.getName()));
					if (_armors != null && _armors.containsKey(weapon.getItemId()))
						System.out.println(String.format("武器 %d(%s) 與防具重疊。", weapon.getItemId(), weapon.getName()));
				}
				result.put(new Integer(weapon.getItemId()), weapon);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			_log.log(Level.SEVERE, new StringBuilder().append(weapon.getName()).append("(" + weapon.getItemId() + ")").append(" 的讀取失敗。").toString());
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
		return result;
	}

	private Map<Integer, L1Armor> allArmor() {
		Map<Integer, L1Armor> result = new HashMap<Integer, L1Armor>(2048);
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Armor armor = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from armor");

			rs = pstm.executeQuery();
			while (rs.next()) {
				armor = new L1Armor();
				armor.setItemId(rs.getInt("item_id"));
				armor.setItemDescId(rs.getInt("desc_id"));
				armor.setGfxId(rs.getInt("icon_id"));
				armor.setGroundGfxId(rs.getInt("sprite"));
				armor.setNameId(rs.getString("real_name_id"));
				armor.setName(rs.getString("real_name_id_view"));
				armor.setNameView(rs.getString("name_id_view"));
				armor.setType(parseArmorType(rs.getString("type")));
				armor.setType2(2);
				armor.setUseType(parseUseType(rs.getString("type")));
				armor.setGrade(rs.getInt("grade"));
				armor.setMaterial(parseMaterial(rs.getString("material")));
				armor.setWeight(rs.getInt("weight_1000_ea"));
				armor.set_ac(rs.getInt("ac"));
				armor.set_safeenchant(rs.getInt("safenchant"));
				armor.setUseRoyal(rs.getInt("use_royal") == 0 ? false : true);
				armor.setUseKnight(rs.getInt("use_knight") == 0 ? false : true);
				armor.setUseElf(rs.getInt("use_elf") == 0 ? false : true);
				armor.setUseMage(rs.getInt("use_mage") == 0 ? false : true);
				armor.setUseDarkelf(rs.getInt("use_darkelf") == 0 ? false : true);
				armor.setUseDragonKnight(rs.getInt("use_dragonknight") == 0 ? false : true);
				armor.setUseBlackwizard(rs.getInt("use_blackwizard") == 0 ? false : true);
				armor.setUseWarrior(rs.getInt("use_warrior") == 0 ? false : true);
				armor.setUseFencer(rs.getInt("use_fencer") == 0 ? false : true);
				armor.setUseLancer(rs.getInt("use_lancer") == 0 ? false : true);
				armor.set_adddex(rs.getByte("add_dex"));
				armor.set_addstr(rs.getByte("add_str"));
				armor.set_addint(rs.getByte("add_int"));
				armor.set_addwis(rs.getByte("add_wis"));
				armor.set_addcon(rs.getByte("add_con"));
				armor.set_addcha(rs.getByte("add_cha"));
				armor.set_addhp(rs.getInt("add_hp"));
				armor.set_addmp(rs.getInt("add_mp"));
				armor.set_addhpr(rs.getInt("add_hpr"));
				armor.set_addmpr(rs.getInt("add_mpr"));
				armor.set_addsp(rs.getInt("add_sp"));
				armor.setAddExp(rs.getInt("add_exp"));
				armor.set_addeinhasadper(rs.getInt("add_einhasadper"));
				armor.setMinLevel(rs.getInt("min_lvl"));
				armor.setMaxLevel(rs.getInt("max_lvl"));
				armor.set_mdef(rs.getInt("m_def"));
				armor.setWeightReduction(rs.getInt("weight_reduction"));
				armor.set_damage_reduction(rs.getInt("damage_reduction"));
				armor.setHitRate(rs.getInt("hit_rate"));
				armor.setDmgRate(rs.getInt("dmg_rate"));
				armor.setBowHitRate(rs.getInt("bow_hit_rate"));
				armor.setBowDmgRate(rs.getInt("bow_dmg_rate"));
				armor.setHasteItem(rs.getInt("haste_item") == 0 ? false : true);
				armor.setBless(rs.getInt("bless"));
				armor.setTradable(rs.getInt("trade") == 0 ? true : false);
				armor.setCantDelete(rs.getInt("cant_delete") == 1 ? true : false);
				armor.set_defense_earth(rs.getInt("defense_earth"));
				armor.set_defense_water(rs.getInt("defense_water"));
				armor.set_defense_wind(rs.getInt("defense_wind"));
				armor.set_defense_fire(rs.getInt("defense_fire"));
				armor.set_defense_all(rs.getInt("defense_all"));
				armor.setSpecialResistance(eKind.ABILITY, rs.getInt("ability_resis"));
				armor.setSpecialPierce(eKind.ABILITY, rs.getInt("ability_pierce"));
				armor.setSpecialResistance(eKind.SPIRIT, rs.getInt("spirit_resis"));
				armor.setSpecialPierce(eKind.SPIRIT, rs.getInt("spirit_pierce"));
				armor.setSpecialResistance(eKind.DRAGON_SPELL, rs.getInt("dragonS_resis"));
				armor.setSpecialPierce(eKind.DRAGON_SPELL, rs.getInt("dragonS_pierce"));
				armor.setSpecialResistance(eKind.FEAR, rs.getInt("fear_resis"));
				armor.setSpecialPierce(eKind.FEAR, rs.getInt("fear_pierce"));
				armor.setSpecialResistance(eKind.ALL, rs.getInt("all_resis"));
				armor.setSpecialPierce(eKind.ALL, rs.getInt("all_pierce"));

				armor.set_regist_calcPcDefense(rs.getInt("PVPcalcPcDefense"));
				armor.set_regist_PVPweaponTotalDamage(rs.getInt("PVPweaponTotalDamage"));
				armor.setMaxUseTime(rs.getInt("max_use_time"));
				armor.setMagicName(rs.getString("Magic_name"));
				armor.setEndedTimeMessage(rs.getBoolean("isEndedTimeMessage"));
				armor.set_missile_critical_probability(rs.getInt("missile_critical_probability"));
				armor.set_melee_critical_probability(rs.getInt("melee_critical_probability"));
				armor.set_magic_critical_probability(rs.getInt("magic_critical_probability"));
				armor.setMagicHitup(rs.getInt("magic_hit_up"));
				armor.setArmorReductionCancel(rs.getInt("add_reduction_cancel"));
				armor.setPVPWeaponReductionCancel(rs.getInt("PVPDmgReducIgnore"));
				armor.setPVPMagicReduction(rs.getInt("PVPMdmgReduction"));
				armor.setPVPMagicReductionCancel(rs.getInt("PVPMdmgReducIgnore"));
				armor.setIIg(rs.getInt("ImmuneIgnore"));
				armor.setTitanPercent(rs.getInt("titan_percent"));
				armor.setOverlaySurfId(rs.getInt("overlay_surf_id"));
				// armor.setNoWareHouse((rs.getInt("no_warehouse") == 1) ? true : false);
				armor.setWareHouseLimitType(WareHouseLeaveType.fromString(rs.getString("warehouse_type")));
				armor.setWareHouseLimitLevel(rs.getInt("warehouse_limit_enchant"));

				// XXX 未來使用時取消註解 - 源碼處理完成
				armor.setAttackDelayRate(rs.getInt("attack_speed"));
				armor.setMoveDelayRate(rs.getInt("move_speed"));
				armor.setMpAr16(rs.getInt("mpAr16"));


				if (Config.ServerAdSetting.IsValidItemId) {
					if (_etcitems != null && _etcitems.containsKey(armor.getItemId()))
						System.out.println(String.format("防具 %d(%s) 與 etcItem 重疊。", armor.getItemId(), armor.getName()));
					if (_weapons != null && _weapons.containsKey(armor.getItemId()))
						System.out.println(String.format("防具 %d(%s) 與武器重疊。", armor.getItemId(), armor.getName()));
				}

				result.put(new Integer(armor.getItemId()), armor);
			}
		} catch (NullPointerException e) {
			_log.log(Level.SEVERE, new StringBuilder().append(armor.getName()).append("(" + armor.getItemId() + ")").append(" 的讀取失敗。").toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
		return result;
	}

	public void initRace() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			{
				pstm = con.prepareStatement("delete from character_items where item_id > 8000000 and item_id < 12000000");
				pstm.execute();
				pstm.close();
			}
			{
				pstm = con.prepareStatement("delete from character_elf_warehouse where item_id > 8000000 and item_id < 12000000");
				pstm.execute();
				pstm.close();
			}
			{
				pstm = con.prepareStatement("delete from character_warehouse where item_id > 8000000 and item_id < 12000000");
				pstm.execute();
				pstm.close();
			}
			{
				pstm = con.prepareStatement("delete from clan_warehouse where item_id > 8000000 and item_id < 12000000");
				pstm.execute();
				pstm.close();
			}
			{
				pstm = con.prepareStatement("delete from character_package_warehouse where item_id > 8000000 and item_id < 12000000");
				pstm.execute();
				pstm.close();
			}
			pstm = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
	}

	private void buildFastLookupTable() {
		_allTemplates = new HashMap<Integer, L1Item>(_etcitems.size() + _weapons.size() + _armors.size() + _race.size());
		_allTemplates.putAll(_etcitems);
		_allTemplates.putAll(_weapons);
		_allTemplates.putAll(_armors);
		_allTemplates.putAll(_race);
		/*
		 * HashMap<Integer, L1RaceTicket> map = BugRaceController.getInstance().getAllTemplates(); for(L1RaceTicket t : map.values()){ if(t != null) _allTemplates.put(t.getItemId(), t); }
		 */
	}

	private void subLoad() {
		//HashMap<Integer, PowerBallTicket> tickets = PowerBallController.getInstance().getAllTemplates();
		//for (PowerBallTicket t : tickets.values()) {
		//	if (t != null)
		//		_allTemplates.put(t.getItemId(), t);
		//}
		// MJIRTicketLoader.getInstance().storeToItemTable(this);
	}

	public L1Item getTemplate(int id) {
		return _allTemplates.get(id);
	}
	
	public L1Item findDescId(int descId, int bless) {
		for(L1Item item : _allTemplates.values()){
			if(item != null && item.getItemDescId() == descId && item.getBless() == bless)
				return item;
		}
		return null;
	}
	
	public L1ItemInstance createItem(int itemId) {
		return createItem(itemId, true);
	}
	
	public L1ItemInstance createItem(int itemId, int objId, boolean identified) {
		L1Item temp = getTemplate(itemId);
		if (temp == null) {
			return null;
		}
		L1ItemInstance item = new L1ItemInstance();
		item.setId(objId);
		item.setItem(temp);
		item.setBless(temp.getBless());
		item.setIdentified(identified);

		L1World.getInstance().storeObject(item);
		return item;
	}

	public L1ItemInstance createItem(int itemId, boolean identified) {
		L1Item temp = getTemplate(itemId);
		if (temp == null) {
			return null;
		}
		L1ItemInstance item = new L1ItemInstance();
		item.setId(IdFactory.getInstance().nextId());
		item.setItem(temp);
		item.setBless(temp.getBless());
		item.setIdentified(identified);

		L1World.getInstance().storeObject(item);
		return item;
	}

	public L1ItemInstance createItem(L1Item temp) {
		if (temp == null)
			return null;
		L1ItemInstance item = new L1ItemInstance();
		item.setId(IdFactory.getInstance().nextId());
		item.setItem(temp);
		item.setBless(temp.getBless());
		L1World.getInstance().storeObject(item);
		return item;
	}

	public ArrayList<L1Item> findItemByName(String name) {
		ArrayList<L1Item> result = new ArrayList<L1Item>(4);
		int nl = name.length();
		for (L1Item item : _allTemplates.values()) {
			if (item == null)
				continue;

			String s = item.getName();
			if (s.length() != nl)
				continue;

			if (s.equals(name))
				result.add(item);
		}
		return result;
	}

	public L1Item findItem(Matcher<L1Item> matcher) {
		for (L1Item item : _allTemplates.values()) {
			if (item == null)
				continue;

			if (matcher.matches(item)) {
				return item;
			}
		}
		return null;
	}
	
	public L1Item findStoreCachedItem(int descid, int bless){
		List<L1Item> items = findDescCachedItems(descid);
		if(items == null)
			return null;
		
		for(L1Item item : items){
			
			if(item.getBless() != bless)
				continue;
			
			return item;
		}
		return null;
	}

	public L1ItemInstance createItem(String itemName) {
		int itemId = findItemIdByName(itemName);
		L1Item temp = getTemplate(itemId);
		if (temp == null) {
			return null;
		}
		L1ItemInstance item = new L1ItemInstance();
		item.setId(IdFactory.getInstance().nextId());
		item.setItem(temp);
		item.setBless(temp.getBless());
		L1World.getInstance().storeObject(item);
		return item;
	}

	public String findItemIdByName(int itemid) {
		L1Item item = _allTemplates.get(itemid);
		if (item != null)
			return item.getName();
		return null;
	}

	public int findItemIdByName(String name) {
		int nl = name.length();
		for (L1Item item : _allTemplates.values()) {
			if (item == null)
				continue;

			String s = item.getName();
			if (s.length() != nl)
				continue;

			if (s.equals(name))
				return item.getItemId();
		}
		return 0;
	}

	public int findItemIdByNameWithoutSpace(String name) {
		for (L1Item item : _allTemplates.values()) {
			if (item == null)
				continue;

			String s = item.getName();
			String new_name = name.replace(" ", "");
			if (s.replace(" ", "").equalsIgnoreCase(new_name))
				return item.getItemId();
		}
		return 0;
	}

	public int findItemIdByNameWithoutSpace(String name, int bless) {
		for (L1Item item : _allTemplates.values()) {
			if (item == null)
				continue;

			String s = item.getName();
			String new_name = name.replace(" ", "");
			if (s.replace(" ", "").equalsIgnoreCase(new_name) && item.getBless() == bless)
				return item.getItemId();
		}
		return 0;
	}

	/* 增加緩衝區 */
	public void AddTicket(L1RaceTicket race) {
		_race.put(new Integer(race.getItemId()), race);
		_allTemplates.put(race.getItemId(), race);
	}

	public int GetIssuedTicket() {
		return _race.size();
	}

	public L1Item create_dogfight_ticket(L1Item item, int new_item_id, String name) {
		L1EtcItem etc = new L1EtcItem();
		etc.setItemId(new_item_id);
		etc.setItemDescId(item.getItemDescId());
		etc.setGfxId(item.getGfxId());
		etc.setGroundGfxId(item.getGroundGfxId());
		etc.setName(name);
		etc.setNameId(name);
		etc.setType(item.getType());
		etc.setUseType(item.getUseType());
		etc.setType2(0);
		etc.setMaterial(item.getMaterial());
		etc.setUseRoyal(item.isUseRoyal());
		etc.setUseKnight(item.isUseKnight());
		etc.setUseMage(item.isUseMage());
		etc.setUseElf(item.isUseElf());
		etc.setUseDarkelf(item.isUseDarkelf());
		etc.setUseDragonKnight(item.isUseDragonKnight());
		etc.setUseBlackwizard(item.isUseBlackwizard());
		etc.setUseWarrior(item.isUseWarrior());
		etc.setUseFencer(item.isUseFencer());
		etc.setUseLancer(item.isUseLancer());
		etc.setWeight(item.getWeight());
		etc.set_stackable(item.isStackable());
		etc.setMaxChargeCount(item.getMaxChargeCount());
		
		etc.setHitModifier(item.getHitModifier());
		etc.setDmgModifier(item.getDmgModifier());
		etc.setMinLevel(item.getMinLevel());
		etc.setMaxLevel(item.getMaxLevel());
		
		etc.set_locx(item.get_locx());
		etc.set_locy(item.get_locy());
		etc.set_mapid(item.get_mapid());
		
		etc.setBless(item.getBless());
		
		etc.setTradable(item.isTradable());
		etc.setWareHouseLimitType(item.getWareHouseLimitType());
		etc.setWareHouseLimitLevel(item.getWareHouseLimitLevel());
		etc.setCantDelete(item.isCantDelete());
		etc.set_delayid(item.get_delayid());
		etc.set_delaytime(item.get_delaytime());
		etc.set_delayEffect(item.get_delayEffect());
		etc.setFoodVolume(item.getFoodVolume());
		etc.setToBeSavedAtOnce(item.isToBeSavedAtOnce());
		etc.setEndedTimeMessage(item.isEndedTimeMessage());
		etc.setUseEffetId(item.getUseEffectId());

//		etc.set_missile_critical_probability(item.get_missile_critical_probability());
//		etc.set_melee_critical_probability(item.get_melee_critical_probability());
		_allTemplates.put(new_item_id, etc);
		_etcitems.put(new_item_id, etc);
//		System.out.println("票券創建完成: " + new_item_id);
		return etc;
	}

	/** 創建（複製）一個新的 Template 對象 */
	public L1Item clone(L1Item item, String name) {
		// 因為名稱而使用武器
		if (item.getType2() == 1) {
			L1Weapon weapon = new L1Weapon();
			weapon.setItemId(item.getItemId());
			weapon.setName(item.getName());
			weapon.setNameId(item.getNameId());
			weapon.setType(item.getType());
			weapon.setType1(item.getType1());
			weapon.setType2(1);
			weapon.setUseType(1);
			weapon.setMaterial(item.getMaterial());
			weapon.setWeight(item.getWeight());
			weapon.setGfxId(item.getGfxId());
			weapon.setGroundGfxId(item.getGroundGfxId());
			weapon.setItemDescId(item.getItemDescId());
			weapon.setDmgSmall(item.getDmgSmall());
			weapon.setDmgLarge(item.getDmgLarge());
			weapon.set_safeenchant(item.get_safeenchant());
			weapon.setUseRoyal(item.isUseRoyal());
			weapon.setUseKnight(item.isUseKnight());
			weapon.setUseElf(item.isUseElf());
			weapon.setUseMage(item.isUseMage());
			weapon.setUseDarkelf(item.isUseDarkelf());
			weapon.setUseDragonKnight(item.isUseDragonKnight());
			weapon.setUseBlackwizard(item.isUseBlackwizard());
			weapon.setUseWarrior(item.isUseWarrior());
			weapon.setHitModifier(item.getHitModifier());
			weapon.setDmgModifier(item.getDmgModifier());
			weapon.set_addstr(item.get_addstr());
			weapon.set_adddex(item.get_adddex());
			weapon.set_addcon(item.get_addcon());
			weapon.set_addint(item.get_addint());
			weapon.set_addwis(item.get_addwis());
			weapon.set_addcha(item.get_addcha());
			weapon.set_addhp(item.get_addhp());
			weapon.set_addmp(item.get_addmp());
			weapon.set_addhpr(item.get_addhpr());
			weapon.set_addmpr(item.get_addmpr());
			weapon.set_addsp(item.get_addsp());
			weapon.set_mdef(item.get_mdef());
			weapon.setDoubleDmgChance(item.getDoubleDmgChance());
			weapon.set_double_dmg_enchant_value(item.get_double_dmg_enchant_value());
			weapon.set_weak_point_chance(item.get_weak_point_chance());
			weapon.set_weak_point_enchant_value(item.get_weak_point_enchant_value());
			weapon.setMagicDmgModifier(item.getMagicDmgModifier());
			weapon.set_canbedmg(item.get_canbedmg());
			weapon.setMinLevel(item.getMinLevel());
			weapon.setMaxLevel(item.getMaxLevel());
			weapon.setBless(item.getBless());
			weapon.setTradable(item.isTradable());
			weapon.setCantDelete(item.isCantDelete());
			weapon.setHasteItem(item.isHasteItem());
			weapon.setMaxUseTime(item.getMaxUseTime());
			weapon.set_missile_critical_probability(item.get_missile_critical_probability());
			weapon.set_melee_critical_probability(item.get_melee_critical_probability());
			return weapon;
			// 防具
		} else if (item.getType2() == 2) {
			L1Armor armor = new L1Armor();
			armor.setItemId(item.getItemId());
			armor.setName(item.getName());
			armor.setNameId(item.getNameId());
			armor.setType(item.getType());
			armor.setType2(2);
			armor.setUseType(item.getUseType());
			armor.setMaterial(item.getMaterial());
			armor.setWeight(item.getWeight());
			armor.setGfxId(item.getGfxId());
			armor.setGroundGfxId(item.getGroundGfxId());
			armor.setItemDescId(item.getItemDescId());
			armor.set_ac(item.get_ac());
			armor.set_safeenchant(item.get_safeenchant());
			armor.setUseRoyal(item.isUseRoyal());
			armor.setUseKnight(item.isUseKnight());
			armor.setUseElf(item.isUseElf());
			armor.setUseMage(item.isUseMage());
			armor.setUseDarkelf(item.isUseDarkelf());
			armor.setUseDragonKnight(item.isUseDragonKnight());
			armor.setUseBlackwizard(item.isUseBlackwizard());
			armor.setUseWarrior(item.isUseWarrior());
			armor.set_addstr(item.get_addstr());
			armor.set_addcon(item.get_addcon());
			armor.set_adddex(item.get_adddex());
			armor.set_addint(item.get_addint());
			armor.set_addwis(item.get_addwis());
			armor.set_addcha(item.get_addcha());
			armor.set_addhp(item.get_addhp());
			armor.set_addmp(item.get_addmp());
			armor.set_addhpr(item.get_addhpr());
			armor.set_addmpr(item.get_addmpr());
			armor.set_addsp(item.get_addsp());
			armor.set_addeinhasadper(item.get_addeinhasadper());
			armor.setMinLevel(item.getMinLevel());
			armor.setMaxLevel(item.getMaxLevel());
			armor.set_mdef(item.get_mdef());
			armor.set_damage_reduction(item.get_damage_reduction());
			armor.setWeightReduction(item.getWeightReduction());
			armor.setBowHitRate(item.getBowHitRate());
			armor.setHasteItem(item.isHasteItem());
			armor.setBless(item.getBless());
			armor.setTradable(item.isTradable());
			armor.setCantDelete(item.isCantDelete());
			armor.set_defense_earth(item.get_defense_earth());
			armor.set_defense_water(item.get_defense_water());
			armor.set_defense_wind(item.get_defense_wind());
			armor.set_defense_fire(item.get_defense_fire());
			armor.set_defense_all(item.get_defense_all());
			armor.setSpecialResistanceMap(item.getSpecialResistanceMap());
			armor.setSpecialPierceMap(item.getSpecialPierceMap());
			armor.set_regist_calcPcDefense(item.get_regist_calcPcDefense());
			armor.set_regist_PVPweaponTotalDamage(item.get_regist_PVPweaponTotalDamage());
			armor.setMaxUseTime(item.getMaxUseTime());
			armor.set_missile_critical_probability(item.get_missile_critical_probability());
			armor.set_melee_critical_probability(item.get_melee_critical_probability());
			armor.setHitModifier(item.getHitModifier());
			armor.setDmgModifier(item.getDmgModifier());
			armor.setMagicHitup(item.getMagicHitup());
			armor.setArmorReductionCancel(item.getArmorReductionCancel());
			return armor;
		} else if (item.getType2() == 0) {
			L1EtcItem etc = new L1EtcItem();
			etc.setItemId(item.getItemId());
			etc.setName(name);
			etc.setNameId(item.getNameId());
			etc.setType(item.getType());
			etc.setUseType(item.getUseType());
			etc.setType2(0);
			etc.setMaterial(item.getMaterial());
			etc.setUseRoyal(item.isUseRoyal());
			etc.setUseKnight(item.isUseKnight());
			etc.setUseElf(item.isUseElf());
			etc.setUseMage(item.isUseMage());
			etc.setUseDarkelf(item.isUseDarkelf());
			etc.setUseDragonKnight(item.isUseDragonKnight());
			etc.setUseBlackwizard(item.isUseBlackwizard());
			etc.setUseWarrior(item.isUseWarrior());
			etc.setWeight(item.getWeight());
			etc.setGfxId(item.getGfxId());
			etc.setGroundGfxId(item.getGroundGfxId());
			etc.setItemDescId(item.getItemDescId());
			etc.setMinLevel(item.getMinLevel());
			etc.setMaxLevel(item.getMaxLevel());
			etc.setBless(item.getBless());
			etc.setTradable(item.isTradable());
			etc.setCantDelete(item.isCantDelete());
			etc.setDmgSmall(item.getDmgSmall());
			etc.setDmgLarge(item.getDmgLarge());
			etc.set_stackable(item.isStackable());
			etc.setMaxChargeCount(item.getMaxChargeCount());
			etc.set_locx(item.get_locx());
			etc.set_locy(item.get_locy());
			etc.set_mapid(item.get_mapid());
			etc.set_delayid(item.get_delayid());
			etc.set_delaytime(item.get_delaytime());
			etc.set_delayEffect(item.get_delayEffect());
			etc.setFoodVolume(item.getFoodVolume());
			etc.setToBeSavedAtOnce(item.isToBeSavedAtOnce());
			etc.set_missile_critical_probability(item.get_missile_critical_probability());
			etc.set_melee_critical_probability(item.get_melee_critical_probability());
			etc.setHitModifier(item.getHitModifier());
			etc.setDmgModifier(item.getDmgModifier());
			return etc;
		}
		return null;
	}

	public boolean findItemId(int id) {
		return _allTemplates.containsKey(id);
	}

	public static int parseWeaponId(String s) {
		if (s.equalsIgnoreCase("sword"))
			return 4;
		else if (s.equalsIgnoreCase("dagger"))
			return 46;
		else if (s.equalsIgnoreCase("tohandsword"))
			return 50;
		else if (s.equalsIgnoreCase("bow"))
			return 20;
		else if (s.equalsIgnoreCase("spear"))
			return 24;
		else if (s.equalsIgnoreCase("blunt"))
			return 11;
		else if (s.equalsIgnoreCase("staff"))
			return 40;
		else if (s.equalsIgnoreCase("throwingknife"))
			return 2922;
		else if (s.equalsIgnoreCase("arrow"))
			return 66;
		else if (s.equalsIgnoreCase("gauntlet"))
			return 62;
		else if (s.equalsIgnoreCase("claw"))
			return 58;
		else if (s.equalsIgnoreCase("edoryu"))
			return 54;
		else if (s.equalsIgnoreCase("singlebow"))
			return 20;
		else if (s.equalsIgnoreCase("singlespear"))
			return 24;
		else if (s.equalsIgnoreCase("tohandblunt"))
			return 11;
		else if (s.equalsIgnoreCase("tohandstaff"))
			return 40;
		else if (s.equalsIgnoreCase("keyring"))
			return 58;
		else if (s.equalsIgnoreCase("chainsword"))
			return 24;
		System.out.println(String.format("invalid item weapon Id %s", s));
		return 0; // isnone.
	}

	public static int parseWeaponType(String s) {
		if (s.equalsIgnoreCase("sword"))
			return 1;
		else if (s.equalsIgnoreCase("dagger"))
			return 2;
		else if (s.equalsIgnoreCase("tohandsword"))
			return 3;
		else if (s.equalsIgnoreCase("bow"))
			return 4;
		else if (s.equalsIgnoreCase("spear"))
			return 5;
		else if (s.equalsIgnoreCase("blunt"))
			return 6;
		else if (s.equalsIgnoreCase("staff"))
			return 7;
		else if (s.equalsIgnoreCase("throwingknife"))
			return 8;
		else if (s.equalsIgnoreCase("arrow"))
			return 9;
		else if (s.equalsIgnoreCase("gauntlet"))
			return 10;
		else if (s.equalsIgnoreCase("claw"))
			return 11;
		else if (s.equalsIgnoreCase("edoryu"))
			return 12;
		else if (s.equalsIgnoreCase("singlebow"))
			return 13;
		else if (s.equalsIgnoreCase("singlespear"))
			return 14;
		else if (s.equalsIgnoreCase("tohandblunt"))
			return 15;
		else if (s.equalsIgnoreCase("tohandstaff"))
			return 16;
		else if (s.equalsIgnoreCase("keyring"))
			return 17;
		else if (s.equalsIgnoreCase("chainsword"))
			return 18;
		System.out.println(String.format("invalid item weapon type %s", s));
		return 0; // isnone.
	}

	public static int parseArmorType(String s) {
		if (s.equalsIgnoreCase("none"))
			return 0;
		else if (s.equalsIgnoreCase("helm"))
			return 1;
		else if (s.equalsIgnoreCase("armor"))
			return 2;
		else if (s.equalsIgnoreCase("T"))
			return 3;
		else if (s.equalsIgnoreCase("cloak"))
			return 4;
		else if (s.equalsIgnoreCase("glove"))
			return 5;
		else if (s.equalsIgnoreCase("boots"))
			return 6;
		else if (s.equalsIgnoreCase("shield"))
			return 7;
		else if (s.equalsIgnoreCase("amulet"))
			return 8;
		else if (s.equalsIgnoreCase("ring"))
			return 9;
		else if (s.equalsIgnoreCase("belt"))
			return 10;
		else if (s.equalsIgnoreCase("ring2"))
			return 11;
		else if (s.equalsIgnoreCase("earring"))
			return 12;
		else if (s.equalsIgnoreCase("garder"))
			return 13;
		else if (s.equalsIgnoreCase("ron"))
			return 25;
		else if (s.equalsIgnoreCase("pair"))
			return 15;
		else if (s.equalsIgnoreCase("ron2"))
			return 30;
		else if (s.equalsIgnoreCase("should"))
			return 31;
		else if (s.equalsIgnoreCase("badge"))
			return 32;
		else if (s.equalsIgnoreCase("Pendant"))
			return 33;
			// TODO 伺服器特化符文（不顯示在槽窗口中）如果之後增加99，則需要更改
		else if (s.equalsIgnoreCase("ron_bonus"))
			return 90;
		else if (s.equalsIgnoreCase("ron_bonus1"))
			return 91;
		else if (s.equalsIgnoreCase("ron_bonus2"))
			return 92;
		else if (s.equalsIgnoreCase("ron_bonus3"))
			return 93;
		else if (s.equalsIgnoreCase("ron_bonus4"))
			return 94;
		else if (s.equalsIgnoreCase("ron_bonus5"))
			return 95;
		else if (s.equalsIgnoreCase("ron_bonus6"))
			return 96;
		else if (s.equalsIgnoreCase("ron_bonus7"))
			return 97;
		else if (s.equalsIgnoreCase("ron_bonus8"))
			return 98;
		else if (s.equalsIgnoreCase("ron_bonus9"))
			return 99;

		System.out.println(String.format("invalid item armor type %s", s));
		return 0;
	}

	public static int parseEtcType(String s) {
		if (s.equalsIgnoreCase("arrow"))
			return 0;
		else if (s.equalsIgnoreCase("wand"))
			return 1;
		else if (s.equalsIgnoreCase("light"))
			return 2;
		else if (s.equalsIgnoreCase("gem"))
			return 3;
		else if (s.equalsIgnoreCase("totem"))
			return 4;
		else if (s.equalsIgnoreCase("firecracker"))
			return 5;
		else if (s.equalsIgnoreCase("potion"))
			return 6;
		else if (s.equalsIgnoreCase("food"))
			return 7;
		else if (s.equalsIgnoreCase("scroll"))
			return 8;
		else if (s.equalsIgnoreCase("questitem"))
			return 9;
		else if (s.equalsIgnoreCase("spellbook"))
			return 10;
		else if (s.equalsIgnoreCase("petitem"))
			return 11;
		else if (s.equalsIgnoreCase("other"))
			return 12;
		else if (s.equalsIgnoreCase("material"))
			return 13;
		else if (s.equalsIgnoreCase("event"))
			return 14;
		else if (s.equalsIgnoreCase("sting"))
			return 15;
		else if (s.equalsIgnoreCase("treasure_box"))
			return 16;
		else if (s.equalsIgnoreCase("teleport_scroll"))
			return 17;
		else if (s.equalsIgnoreCase("protect"))
			return 21;
		else if (s.equalsIgnoreCase("spwan_wand"))
			return 74;
		else if (s.equalsIgnoreCase("meterialchoice"))
			return 80;
		else if (s.equalsIgnoreCase("box_item"))
			return 81;
		else if (s.equalsIgnoreCase("armor_smelting_stone"))
			return 100;
		else if (s.equalsIgnoreCase("smelting_remove"))
			return 101;
		else if (s.equalsIgnoreCase("weapon_smelting_stone"))
			return 102;
		else if (s.equalsIgnoreCase("temp_skill"))
			return 201;
		else if (s.equalsIgnoreCase("week_box"))
			return 301;
		else if (s.equalsIgnoreCase("dol_potential"))
			return 401;
		else if (s.equalsIgnoreCase("bless_assign"))
			return 501;
		else if (s.equalsIgnoreCase("select_item"))
			return 601;

		System.out.println(String.format("invalid item etc type %s", s));
		return -1; // isnone.
	}

	public static int parseMaterial(String s) {
		if (s.equalsIgnoreCase("NONE(未知)"))
			return 0;
		else if (s.equalsIgnoreCase("LIQUID(液體)"))
			return 1;
		else if (s.equalsIgnoreCase("WAX(蠟)"))
			return 2;
		else if (s.equalsIgnoreCase("VEGGY(植物性)"))
			return 3;
		else if (s.equalsIgnoreCase("FLESH(動物性)"))
			return 4;
		else if (s.equalsIgnoreCase("PAPER(紙)"))
			return 5;
		else if (s.equalsIgnoreCase("CLOTH(布)"))
			return 6;
		else if (s.equalsIgnoreCase("LEATHER(皮革)"))
			return 7;
		else if (s.equalsIgnoreCase("WOOD(木材)"))
			return 8;
		else if (s.equalsIgnoreCase("BONE(骨頭)"))
			return 9;
		else if (s.equalsIgnoreCase("DRAGON_HIDE(龍鱗)"))
			return 10;
		else if (s.equalsIgnoreCase("IRON(鐵)"))
			return 11;
		else if (s.equalsIgnoreCase("METAL(金屬)"))
			return 12;
		else if (s.equalsIgnoreCase("COPPER(銅)"))
			return 13;
		else if (s.equalsIgnoreCase("SILVER(銀)"))
			return 14;
		else if (s.equalsIgnoreCase("GOLD(金)"))
			return 15;
		else if (s.equalsIgnoreCase("PLATINUM(白金)"))
			return 16;
		else if (s.equalsIgnoreCase("MITHRIL(秘銀)"))
			return 17;
		else if (s.equalsIgnoreCase("PLASTIC(黑秘銀)"))
			return 18;
		else if (s.equalsIgnoreCase("GLASS(玻璃)"))
			return 19;
		else if (s.equalsIgnoreCase("GEMSTONE(寶石)"))
			return 20;
		else if (s.equalsIgnoreCase("MINERAL(礦物)"))
			return 21;
		else if (s.equalsIgnoreCase("ORIHARUKON(奧里哈魯根)"))
			return 22;
		else if (s.equalsIgnoreCase("DRANIUM(德萊尼姆)"))
			return 23;
		System.out.println(String.format("invalid item material %s", s));
		return 0;
	}

	public static int parseUseType(String s) {
		if (s.equalsIgnoreCase("none"))
			return -1;
		else if (s.equalsIgnoreCase("normal"))
			return 0;
		else if (s.equalsIgnoreCase("weapon"))
			return 1;
		else if (s.equalsIgnoreCase("armor"))
			return 2;
		else if (s.equalsIgnoreCase("spell_long"))
			return 5;
		else if (s.equalsIgnoreCase("ntele"))
			return 6;
		else if (s.equalsIgnoreCase("identify"))
			return 7;
		else if (s.equalsIgnoreCase("res"))
			return 8;
		else if (s.equalsIgnoreCase("teleport"))
			return 9;
		else if (s.equalsIgnoreCase("letter"))
			return 12;
		else if (s.equalsIgnoreCase("letter_w"))
			return 13;
		else if (s.equalsIgnoreCase("choice"))
			return 14;
		else if (s.equalsIgnoreCase("instrument"))
			return 15;
		else if (s.equalsIgnoreCase("sosc"))
			return 16;
		else if (s.equalsIgnoreCase("spell_short"))
			return 17;
		else if (s.equalsIgnoreCase("T"))
			return 18;
		else if (s.equalsIgnoreCase("cloak"))
			return 19;
		else if (s.equalsIgnoreCase("glove"))
			return 20;
		else if (s.equalsIgnoreCase("boots"))
			return 21;
		else if (s.equalsIgnoreCase("helm"))
			return 22;
		else if (s.equalsIgnoreCase("ring"))
			return 23;
		else if (s.equalsIgnoreCase("amulet"))
			return 24;
		else if (s.equalsIgnoreCase("shield") || s.equalsIgnoreCase("garder"))
			return 25;
		else if (s.equalsIgnoreCase("dai"))
			return 26;
		else if (s.equalsIgnoreCase("zel"))
			return 27;
		else if (s.equalsIgnoreCase("blank"))
			return 28;
		else if (s.equalsIgnoreCase("btele"))
			return 29;
		else if (s.equalsIgnoreCase("spell_buff"))
			return 30;
		else if (s.equalsIgnoreCase("ccard"))
			return 31;
		else if (s.equalsIgnoreCase("ccard_w"))
			return 32;
		else if (s.equalsIgnoreCase("vcard"))
			return 33;
		else if (s.equalsIgnoreCase("vcard_w"))
			return 34;
		else if (s.equalsIgnoreCase("wcard"))
			return 35;
		else if (s.equalsIgnoreCase("wcard_w"))
			return 36;
		else if (s.equalsIgnoreCase("belt"))
			return 37;
		else if (s.equalsIgnoreCase("spell_long2"))
			return 39;
		else if (s.equalsIgnoreCase("earring"))
			return 40;
		else if (s.equalsIgnoreCase("fishing_rod"))
			return 42;
		else if (s.equalsIgnoreCase("ron"))
			return 44;
		else if (s.equalsIgnoreCase("acczel"))
			return 46;
		else if (s.equalsIgnoreCase("healing"))
			return 51;
		else if(s.equalsIgnoreCase("spell_extractor"))		
			return 64;
		else if (s.equalsIgnoreCase("tam"))
			return 68;
		else if (s.equalsIgnoreCase("pair"))
			return 70;
		else if (s.equalsIgnoreCase("magic_doll"))
			return 73;
		else if (s.equalsIgnoreCase("ron2"))
			return 74;
		else if (s.equalsIgnoreCase("should"))
			return 75;
		else if (s.equalsIgnoreCase("pet_potion"))
			return 77;
		else if (s.equalsIgnoreCase("badge"))
			return 78;
		else if (s.equalsIgnoreCase("poly_controll"))
			return 79;
		else if (s.equalsIgnoreCase("Pendant"))
			return 80;
			// TODO 伺服器特化符文（不顯示在槽窗口中）如果之後增加99，則需要更改
		else if (s.equalsIgnoreCase("ron_bonus"))
			return 90;
		else if (s.equalsIgnoreCase("ron_bonus1"))
			return 91;
		else if (s.equalsIgnoreCase("ron_bonus2"))
			return 92;
		else if (s.equalsIgnoreCase("ron_bonus3"))
			return 93;
		else if (s.equalsIgnoreCase("ron_bonus4"))
			return 94;
		else if (s.equalsIgnoreCase("ron_bonus5"))
			return 95;
		else if (s.equalsIgnoreCase("ron_bonus6"))
			return 96;
		else if (s.equalsIgnoreCase("ron_bonus7"))
			return 97;
		else if (s.equalsIgnoreCase("ron_bonus8"))
			return 98;
		else if (s.equalsIgnoreCase("ron_bonus9"))
			return 99;
		else if (s.equalsIgnoreCase("use_time"))
			return 100;

		System.out.println(String.format("invalid item use type %s", s));
		return -1; // isnone.
	}

	public List<L1Item> findItemId(TableFindItemFilter filter) {
		List<L1Item> list = new LinkedList<L1Item>();
		for (L1Item item : _allTemplates.values()) {
			if (item == null)
				continue;

			if (!filter.isFilter(item))
				list.add(item);
		}
		return list;
	}

	public List<L1Item> findDescCachedItems(final int itemDescId) {
		List<L1Item> items = _descCached.get(itemDescId);
		if (items != null)
			return items;

		items = findItemId(new TableFindItemFilter() {
			@Override
			public boolean isFilter(L1Item item) {
				return item.getItemDescId() != itemDescId;
			}
		});

		if (items == null || items.size() <= 0)
			return null;

		_descCached.put(itemDescId, items);
		return items;
	}

	public L1Item findDescCachedItemsAtOnce(final int itemDescId) {
		List<L1Item> list = findDescCachedItems(itemDescId);
		return list == null || list.size() <= 0 ? null : list.get(0);
	}


	public L1Item findSmeltingCachedItem(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Item item) {
		List<L1Item> items = findDescCachedItems(item.get_name_id());
		if (items == null) {
			System.out.println(String.format("[(精煉石合成) 無法找到對應精煉石合成結果的物品。] 桌面 ID：%d，圖標 ID：%d", item.get_name_id(), item.get_icon()));
			return null;
		}

		int iconId = item.get_icon();
		for (L1Item l1Item : items) {
			if (l1Item.getGfxId() == iconId)
				return l1Item;
		}

		System.out.println(String.format("[(精煉石合成) 無法找到對應精煉石合成結果的物品。] 桌面 ID: %d，圖標 ID: %d", item.get_name_id(), item.get_icon()));
		return null;
	}
	
	public L1Item findAlchemyCachedItem(SC_ALCHEMY_DESIGN_ACK.Item item) {
		List<L1Item> items = findDescCachedItems(item.get_name_id());
		if (items == null) {
			System.out.println(String.format("[(娃娃合成) 無法找到對應娃娃合成結果的物品。] 桌面 ID: %d，圖標 ID: %d", item.get_name_id(), item.get_icon()));
			return null;
		}

		int iconId = item.get_icon();
		for (L1Item l1Item : items) {
			if (l1Item.getGfxId() == iconId)
				return l1Item;
		}

		System.out.println(String.format("[(娃娃合成)無法找到對應娃娃合成結果的物品。]桌面ID：%d，圖標ID：%d", item.get_name_id(), item.get_icon()));
		return null;
	}

	public L1Item findCraftCachedItem(CraftOutputItem outputItem) {
		List<L1Item> items = findDescCachedItems(outputItem.get_name_id());
		if (items == null) {
			return null;
		}
		int iconId = outputItem.get_iconId();
		int bless = outputItem.get_bless();
		for (L1Item item : items) {
			if (item.getGfxId() != iconId) {
				continue;
			}

			if (bless != 3) {
				if (item.getBless() != bless) {
					continue;
				}
			}

			return item;
		}
		return null;
	}
	
	public L1Item findCraftCachedFavorItem(CraftOutputItem outputItem) {
		List<L1Item> items = findDescCachedItems(outputItem.get_name_id());
		if (items == null) {
			return null;
		}
		int iconId = outputItem.get_iconId();
		int bless = outputItem.get_bless();
		for (L1Item item : items) {
			if (item.getGfxId() != iconId) {
				continue;
			}

			if (item.getType() != 16 && bless != 3) {
				if (item.getBless() != bless) {
					continue;
				}
			}

			return item;
		}
		return null;
	}
}
