package l1j.server.MJCompanion.Instance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJCompanion.MJCompanionSettings;
import l1j.server.MJCompanion.Basic.Buff.MJCompanionBuffInfo;
import l1j.server.MJCompanion.Basic.ClassInfo.MJCompanionClassInfo;
import l1j.server.MJCompanion.Basic.Exp.MJCompanionExpPenalty;
import l1j.server.MJCompanion.Basic.FriendShip.MJCompanionFriendShipBonus;
import l1j.server.MJCompanion.Basic.Skills.MJCompanionClassSkillInfo;
import l1j.server.MJCompanion.Basic.Skills.MJCompanionSkillEffect;
import l1j.server.MJCompanion.Basic.Skills.MJCompanionSkillInfo;
import l1j.server.MJCompanion.Basic.Stat.MJCompanionStatEffect;
import l1j.server.MJKDASystem.MJKDA;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.L1Instance.MJEffectTriggerInstance;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CompanionT.eCommand;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_SKILL_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_SKILL_NOTI.Skill;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_STATUS_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_UPDATE_INVENTORY_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Ability;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1GroundInventory;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_Petskill;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.storage.mysql.MySqlCharacterStorage;
import l1j.server.server.templates.L1Npc;

public class MJCompanionInstance extends L1NpcInstance {

	public static void do_store_companions() {
		for (MJCompanionInstance companion : L1World.getInstance().getAllCompanions()) {
			companion.update_instance();
			companion.update_buffs();
			companion.update_skills();
		}
	}

	private static boolean unload_companion(L1PcInstance master, L1ItemInstance item) {
		MJCompanionInstance companion = master.get_companion();
		if (companion == null)
			return false;

		if (companion.get_control_object_id() == item.getId()) {
			master.remove_companion();
			return true;
		}
		return false;
	}

	private static boolean use_stats_initialized(L1PcInstance master, L1ItemInstance item) {
		if (item.getItemId() != MJCompanionSettings.STATS_INITIALIZED_ITEM_ID)
			return false;

		MJCompanionInstance companion = master.get_companion();
		if (companion == null) {
			master.sendPackets("請在召喚寵物的狀態下使用。");
			return true;
		}
		if (companion.getLevel() < MJCompanionSettings.STATS_INITIALIZED_NEED_LEVEL) {
			master.sendPackets(String.format("重置屬性需要至少達到 %d 級。",
					MJCompanionSettings.STATS_INITIALIZED_NEED_LEVEL));
			return true;
		}
		if (!master.getInventory().consumeItem(MJCompanionSettings.STATS_INITIALIZED_ITEM_ID, 1)) {
			master.sendPackets("找不到物品資訊。");
			return true;
		}
		companion.do_stats_initialized();
		return true;
	}

	private static boolean use_name_change_initialized(L1PcInstance master, L1ItemInstance item) {
		if (item.getItemId() != MJCompanionSettings.NAME_CHANGED_ITEM_ID)
			return false;

		MJCompanionInstance companion = master.get_companion();
		if (companion == null) {
			master.sendPackets("請在召喚寵物的狀態下使用。");
			return true;
		}

		if (!master.getInventory().consumeItem(MJCompanionSettings.NAME_CHANGED_ITEM_ID, 1)) {
			master.sendPackets("找不到物品資訊。");
			return true;
		}
		String source_name = companion.getName();
		companion.set_name_changed_count(0);
		companion.do_name_change(companion.get_class_info().get_class_npc_name_id());
		master.sendPackets(String.format("已重置 %s 的名稱。", source_name));
		return true;
	}

	private static boolean use_friend_ship_bonuses(L1PcInstance master, L1ItemInstance item) {
		MJCompanionFriendShipBonus bonus = MJCompanionFriendShipBonus.from_item_id(item.getItemId());
		if (bonus == null)
			return false;

		MJCompanionInstance companion = master.get_companion();
		if (companion == null) {
			master.sendPackets("請在召喚寵物的狀態下使用。");
			return true;
		}
		if (!master.getInventory().consumeItem(item.getItemId(), 1)) {
			master.sendPackets("找不到物品資訊。");
			return true;
		}
		int friend_ship_guage = bonus.get_friend_ship_guage();
		int friend_ship_marble = bonus.get_friend_ship_marble();
		int update_flag = MJCompanionUpdateFlag.UPDATE_FRIEND_SHIP;
		if (friend_ship_guage > 0) {
			companion.add_friend_ship_guage(friend_ship_guage);
			if (companion.get_friend_ship_guage() >= MJCompanionSettings.FRIEND_SHIP_MAX_GUAGE)
				companion.do_combo_run();
			update_flag |= MJCompanionUpdateFlag.UPDATE_COMBO_TIME | MJCompanionUpdateFlag.UPDATE_DELAY_REDUCE;
			master.sendPackets(String.format("已提升 %s 的鬥志等級 %d。", companion.getName(), friend_ship_guage));
		}
		if (friend_ship_marble > 0) {
			companion.add_friend_ship_marble(friend_ship_marble);
			companion.update_friend_ship_marble();
			master.sendPackets(String.format("已提升 %s 的友誼點數 %d。", companion.getName(), friend_ship_marble));
		}
		SC_COMPANION_STATUS_NOTI.send(master, companion, update_flag);
		if (bonus.get_effect_id() > 0)
			companion.send_effect(bonus.get_effect_id());
		return true;
	}

	private static boolean use_buff_item(L1PcInstance master, L1ItemInstance item) {
		MJCompanionBuffInfo bInfo = MJCompanionBuffInfo.from_item_id(item.getItemId()); // 查找技能。
		if (bInfo == null)
			return false;

		MJCompanionInstance companion = master.get_companion();
		if (companion == null) {
			master.sendPackets("請在召喚寵物的狀態下使用。");
			return true;
		}
		if (!master.getInventory().consumeItem(item.getItemId(), 1)) {
			master.sendPackets("找不到物品資訊。");
			return true;
		}

		for (int skill_id : bInfo.get_deduplications_spell_id()) { // 清除寵物身上所有指定技能效果。
			if (companion.hasSkillEffect(skill_id))
				companion.removeSkillEffect(skill_id);
		}

		SC_COMPANION_BUFF_NOTI.send(master, bInfo.get_spell_id(), bInfo.get_duration());

		companion.setSkillEffect(bInfo.get_spell_id(), bInfo.get_duration() * 1000);

		if (bInfo.get_pvp_offense() > 0)
			SC_COMPANION_STATUS_NOTI.send(master, companion, MJCompanionUpdateFlag.UPDATE_PVP_DAMAGE);

		if (bInfo.get_effect_id() > 0)
			companion.send_effect(bInfo.get_effect_id());

		if (item.getItemId() == 4100282) {
			companion.sendSkillContinueEff();
		}

		/*
		 * if(bInfo.get_trigger_effect_id() > 0){ MJEffectTriggerInstance trigger =
		 * MJEffectTriggerInstance.newInstance( bInfo.get_spell_id(),
		 * bInfo.get_trigger_effect_id(), 0, companion.getX(), companion.getY(),
		 * companion.getMapId()); companion.add_trigger(trigger); }
		 */
		return true;
	}

	public void sendSkillContinueEff() {
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.sendPackets(new S_Petskill(S_Petskill.DogBlood, this, true), true);
		}
	}

	private static boolean use_elixir(L1PcInstance master, L1ItemInstance item) {
		int item_id = item.getItemId();
		if (item_id == MJCompanionSettings.ELIXIR_STR_ITEM_ID) {
			use_elixir(master, item_id, new int[] { 1, 0, 0 });
		} else if (item_id == MJCompanionSettings.ELIXIR_CON_ITEM_ID) {
			use_elixir(master, item_id, new int[] { 0, 1, 0 });
		} else if (item_id == MJCompanionSettings.ELIXIR_INT_ITEM_ID) {
			use_elixir(master, item_id, new int[] { 0, 0, 1 });
		} else {
			return false;
		}
		return true;
	}

	private static void use_elixir(L1PcInstance master, int item_id, int[] added_stats) {
		MJCompanionInstance companion = master.get_companion();
		if (companion == null) {
			master.sendPackets("請在召喚寵物的狀態下使用。");
			return;
		}

		if (companion.get_remain_elixir(companion) <= 0) {
			master.sendPackets("寵物剩餘的靈藥數量不足。");
			return;
		}

		Ability ability = companion.getAbility();
		if (ability.getAddedStr() + added_stats[0] > MJCompanionSettings.MAX_STAT
				|| ability.getAddedCon() + added_stats[1] > MJCompanionSettings.MAX_STAT
				|| ability.getAddedInt() + added_stats[2] > MJCompanionSettings.MAX_STAT) {
			master.sendPackets(String.format("所有屬性最多只能提升至 %d。", MJCompanionSettings.MAX_STAT));
			return;
		}
		if (!master.getInventory().consumeItem(item_id, 1)) {
			master.sendPackets("找不到物品資訊。");
			return;
		}
		/*
		 * ability.addAddedStr(added_stats[0]); ability.addAddedCon(added_stats[1]);
		 * ability.addAddedInt(added_stats[2]);
		 */
		ability.addHunt(added_stats[0]);
		ability.addSurvival(added_stats[1]);
		ability.addSacred(added_stats[2]);
		companion.set_elixir_use_count(companion.get_elixir_use_count() + 1);
		companion.update_stats();
		companion.update_elixir_use_count();
		MJCompanionInstanceCache.update_cache(companion, true);
		SC_COMPANION_STATUS_NOTI.send(master, companion, MJCompanionUpdateFlag.UPDATE_ALL);

		/*
		 * ability.addAddedStr(added_stats[0]); ability.addAddedCon(added_stats[1]);
		 * ability.addAddedInt(added_stats[2]);
		 * companion.set_elixir_use_count(companion.get_elixir_use_count() + 1);
		 * companion.update_stats(); companion.update_elixir_use_count();
		 * MJCompanionInstanceCache.update_cache(companion, true);
		 * SC_COMPANION_STATUS_NOTI.send(master, companion,
		 * MJCompanionUpdateFlag.UPDATE_ALL);
		 */
	}

	public static boolean use_item(L1PcInstance master, L1ItemInstance item) {
		if (use_stats_initialized(master, item))
			return true;

		if (use_friend_ship_bonuses(master, item))
			return true;

		if (use_buff_item(master, item))
			return true;

		if (use_elixir(master, item))
			return true;

		if (use_name_change_initialized(master, item))
			return true;

		MJCompanionClassInfo cInfo = MJCompanionClassInfo.from_item_id(item.getItemId());
		if (cInfo == null)
			return false;

		if (unload_companion(master, item))
			return true;

		if (master.get_companion() != null) {
			master.remove_companion();
			return true;
		}

		MJCompanionInstance companion = from_database(master, item.getId());
		if (companion == null) {
			companion = newInstance(master, cInfo, item.getId());
			companion.update_instance();
			companion.update_skills();
		} else {
			if (companion.getCurrentHp() <= 0) {
				if (!master.getInventory().checkItem(41246, 1000)) {
					master.sendPackets("決定體不足。");
					return true;
				}
				master.getInventory().consumeItem(41246, 1000);
				companion.setDead(true);
				companion.resurrect(companion.get_base_max_hp() / 4);
				companion.setResurrect(true);
			}
			companion.set_is_oblivion(false);
			companion.load_skills();
			companion.load_buffs();
		}

		companion.update_skill_effect();
		companion.update_stats();
		master.set_companion(companion);
		companion.set_master(master);
		companion.set_master_id(master.getId());
		companion.set_master_name(master.getName());
		companion.setX(master.getX() + (MJRnd.next(3) * (MJRnd.isBoolean() ? 1 : -1)));
		companion.setY(master.getY() + (MJRnd.next(3) * (MJRnd.isBoolean() ? 1 : -1)));
		companion.setMap(master.getMapId());
		companion.setHeading(master.getHeading());
		companion.setLightSize(companion.getNpcTemplate().getLightSize());
		companion.do_store_object();
		SC_COMPANION_SKILL_NOTI.send(master, companion);
		SC_COMPANION_BUFF_NOTI.send(master, companion);
		companion.set_command_state(eCommand.TM_Aggressive);
		companion.stopHpRegeneration();
		companion.stopMpRegeneration();
		MJCompanionInstanceCache.update_cache(companion, true);
		return true;
	}

	private static MJCompanionInstance from_database(final L1PcInstance master, final int control_object_id) {
		final MJObjectWrapper<MJCompanionInstance> wrapper = new MJObjectWrapper<MJCompanionInstance>();
		Selector.exec("select * from companion_instance where control_object_id=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, control_object_id);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					wrapper.value = newInstance(master, rs);
				}
			}
		});
		return wrapper.value;
	}

	private static MJCompanionInstance newInstance(L1PcInstance master, MJCompanionClassInfo cInfo,
			int control_object_id) {
		MJCompanionInstance companion = newInstance();
		companion.setId(IdFactory.getInstance().nextId());
		companion.set_control_object_id(control_object_id);
		companion.set_class_info(cInfo);

		L1Npc npc = companion.getNpcTemplate();
		companion.setName(cInfo.get_class_npc_name_id());
		companion.setLevel(1);
		companion.set_exp(1);
		companion.setMaxHp(npc.get_hp());
		companion.setMaxMp(npc.get_mp());
		companion.setCurrentHp(companion.getMaxHp());
		companion.setCurrentMp(companion.getMaxMp());
		companion.setLawful(npc.get_lawful());
		companion.set_tier(1);
		companion.set_is_oblivion(false);
		companion.set_name_changed_count(0);
		MJCompanionClassSkillInfo class_skill_info = MJCompanionClassSkillInfo
				.get_companion_skills(cInfo.get_class_name(), companion.get_tier());
		companion.do_open_tier(class_skill_info);
		return companion;
	}

	private static MJCompanionInstance newInstance(L1PcInstance master, ResultSet rs) throws SQLException {
		MJCompanionInstance companion = newInstance();
		companion.setId(rs.getInt("object_id"));
		companion.set_control_object_id(rs.getInt("control_object_id"));
		MJCompanionClassInfo cInfo = MJCompanionClassInfo.from_class_name(rs.getString("class_name"));
		if (cInfo == null)
			throw new IllegalArgumentException(String
					.format("MJCompanionInstance::newInstance()...invalid class info %s", rs.getString("class_name")));
		companion.set_class_info(cInfo);
		companion.setName(rs.getString("companion_name"));
		companion.setLevel(rs.getInt("level"));
		companion.set_exp(rs.getLong("exp"));
		companion.set_keep_exp(rs.getLong("keep_exp"));
		companion.set_friend_ship_guage(rs.getInt("friend_ship_guage"));
		companion.set_friend_ship_marble(rs.getInt("friend_ship_marble"));
		companion.setMaxHp(rs.getInt("max_hp"));
		companion.setMaxMp(rs.getInt("max_mp"));
		companion.setCurrentHp(rs.getInt("current_hp"));
		companion.setCurrentMp(rs.getInt("current_mp"));
		companion.setLawful(rs.getInt("lawful"));
		companion.set_tier(rs.getInt("tier"));
		companion.getAbility().addAddedStr(rs.getInt("str"));
		companion.getAbility().setHunt(rs.getInt("elixir_str"));
		companion.getAbility().addAddedCon(rs.getInt("con"));
		companion.getAbility().setSurvival(rs.getInt("elixir_con"));
		companion.getAbility().addAddedInt(rs.getInt("intel"));
		companion.getAbility().setSacred(rs.getInt("elixir_intel"));
		companion.set_elixir_use_count(rs.getInt("elixir_use_count"));
		companion.set_is_oblivion(rs.getInt("is_oblivion") != 0);
		companion.set_is_traning(rs.getInt("is_traning") != 0);
		companion.set_name_changed_count(rs.getInt("name_changed_count"));
		return companion;
	}

	private static MJCompanionInstance newInstance() {
		return new MJCompanionInstance();
	}

	private static final long serialVersionUID = 1L;
	private int m_control_object_id;
	private MJCompanionClassInfo m_class_info;
	private eCommand m_command_state;
	private L1PcInstance m_master;
	private int m_master_id;
	private String m_master_name;
	private int m_tier;
	private long m_keep_exp;
	private int m_elixir_use_count;
	private int m_friend_ship_guage;
	private int m_friend_ship_marble;
	private int m_pvp_dmg_ratio;
	private boolean m_is_oblivion;
	private boolean m_is_traning;
	private boolean m_is_combo_time;
	private int m_combo_count;
	private int m_name_changed_count;
	private ArrayList<Skill> m_exercise_skills;
	private ArrayList<Skill> m_non_exercise_skills;
	private MJCompanionSkillEffect m_skill_effect;
	private MJCompanionStatEffect m_stat_effect;
	private boolean m_is_teleporting;
	private long m_pink_name_millis;
	private Object m_death_lock;
	private ConcurrentHashMap<Integer, MJEffectTriggerInstance> m_triggers;

	private MJCompanionInstance() {
		super(null);

		m_exercise_skills = new ArrayList<Skill>();
		m_non_exercise_skills = new ArrayList<Skill>();
		m_is_combo_time = false;
		m_is_teleporting = false;
		m_is_traning = false;
		m_pink_name_millis = 0;
		m_death_lock = new Object();
		m_combo_count = 0;
		m_name_changed_count = 0;
		m_triggers = new ConcurrentHashMap<Integer, MJEffectTriggerInstance>();
	}

	public void set_control_object_id(int control_object_id) {
		m_control_object_id = control_object_id;
	}

	public int get_control_object_id() {
		return m_control_object_id;
	}

	public void set_class_info(MJCompanionClassInfo cInfo) {
		m_class_info = cInfo;
		L1Npc npc = NpcTable.getInstance().getTemplate(cInfo.get_npc_id());
		if (npc == null)
			throw new IllegalArgumentException(String.format(
					"MJCompanionInstance::set_class_info()... 找到寵物 NPC 模板... 類名 %s, NPC 編號: %d ",
					cInfo.get_class_name(), cInfo.get_npc_id()));
		setting_template(npc);
	}

	public MJCompanionClassInfo get_class_info() {
		return m_class_info;
	}

	public void set_command_state(eCommand command_state) {
		m_command_state = command_state;
		if (m_command_state.equals(eCommand.TM_GetItem)) {
			find_items();
		} else {
			if (!isAiRunning())
				startAI();
		}
	}

	public eCommand get_command_state() {
		return m_command_state;
	}

	public void set_master(L1PcInstance master) {
		m_master = master;
	}

	public L1PcInstance get_master() {
		return m_master;
	}

	public void set_master_id(int master_id) {
		m_master_id = master_id;
	}

	public int get_master_id() {
		return m_master_id;
	}

	public void set_master_name(String master_name) {
		m_master_name = master_name;
	}

	public String get_master_name() {
		return m_master_name;
	}

	public void set_tier(int tier) {
		m_tier = tier;
	}

	public int get_tier() {
		return m_tier;
	}

	public void set_keep_exp(long keep_exp) {
		m_keep_exp = keep_exp;
	}

	public long get_keep_exp() {
		return m_keep_exp;
	}

	public void set_elixir_use_count(int elixir_use_count) {
		m_elixir_use_count = elixir_use_count;
	}

	public int get_elixir_use_count() {
		return m_elixir_use_count;
	}

	public void set_friend_ship_guage(int friend_ship_guage) {
		m_friend_ship_guage = friend_ship_guage;
	}

	public void add_friend_ship_guage(int friend_ship_guage) {
		m_friend_ship_guage = Math.min(m_friend_ship_guage + (int) friend_ship_guage,
				MJCompanionSettings.FRIEND_SHIP_MAX_GUAGE);
	}

	public int get_friend_ship_guage() {
		return m_friend_ship_guage;
	}

	public boolean get_is_combo_time() {
		return m_is_combo_time;
	}

	public void set_is_combo_time(boolean is_combo_time) {
		m_is_combo_time = is_combo_time;
	}

	public int get_combo_count() {
		return m_combo_count;
	}

	public void set_combo_count(int combo_count) {
		m_combo_count = combo_count;
	}

	public int increase_combo_count() {
		return ++m_combo_count;
	}

	public void set_name_changed_count(int name_changed_count) {
		m_name_changed_count = name_changed_count;
	}

	public int get_name_changed_count() {
		return m_name_changed_count;
	}

	public int increase_name_changed_count() {
		return ++m_name_changed_count;
	}

	public void set_friend_ship_marble(int friend_ship_marble) {
		m_friend_ship_marble = friend_ship_marble;
	}

	public void add_friend_ship_marble(int friend_ship_marble) {
		m_friend_ship_marble += friend_ship_marble;
	}

	public int get_friend_ship_marble() {
		return m_friend_ship_marble;
	}

	public double get_attackdelay_reduce() {
		return (m_skill_effect == null ? 0D : m_skill_effect.get_attack_delay_reduce())
				+ (get_is_combo_time() ? MJCompanionSettings.COMBO_ATTACK_DELAY_REDUCE : 0D);
	}

	public double get_movedelay_reduce() {
		return m_skill_effect == null ? 0D
				: m_skill_effect.get_move_delay_reduce()
						+ (get_is_combo_time() ? MJCompanionSettings.COMBO_MOVE_DELAY_REDUCE : 0D);
	}

	public int get_pvp_dmg_ratio() {
		return m_pvp_dmg_ratio + MJCompanionBuffInfo.get_pvp_melee_offense_bonus(this);
	}

	public void set_pvp_dmg_ratio(int pvp_dmg_ratio) {
		m_pvp_dmg_ratio = pvp_dmg_ratio;
	}

	public boolean get_is_oblivion() {
		return m_is_oblivion;
	}

	public void set_is_oblivion(boolean is_oblivion) {
		m_is_oblivion = is_oblivion;
	}

	public boolean get_is_traning() {
		return m_is_traning;
	}

	public void set_is_traning(boolean is_traning) {
		m_is_traning = is_traning;
	}

	public void set_exercise_skills(ArrayList<Skill> exercise_skills) {
		m_exercise_skills = exercise_skills;
	}

	public void add_exercise_skills(Skill sk) {
		m_exercise_skills.add(sk);
	}

	public ArrayList<Skill> get_exercise_skills() {
		return m_exercise_skills;
	}

	public MJCompanionSkillInfo select_exercise_skills() {
		if (m_exercise_skills == null || m_exercise_skills.size() <= 0)
			return null;

		Skill sk = m_exercise_skills.get(MJRnd.next(m_exercise_skills.size()));
		return sk == null ? null : MJCompanionSkillInfo.get_companion_skills(sk.get_id(), sk.get_enchant());
	}

	public void set_non_exercise_skills(ArrayList<Skill> non_exercise_skills) {
		m_non_exercise_skills = non_exercise_skills;
	}

	public void add_non_exercise_skills(Skill sk) {
		m_non_exercise_skills.add(sk);
	}

	public ArrayList<Skill> get_non_exercise_skills() {
		return m_non_exercise_skills;
	}

	public ArrayList<Skill> get_all_skills() {
		ArrayList<Skill> skills = new ArrayList<Skill>(m_exercise_skills);
		skills.addAll(m_non_exercise_skills);
		return skills;
	}

	public Skill find_skills(int skill_id) {
		Skill sk = find_skills(skill_id, m_non_exercise_skills);
		return sk == null ? find_skills(skill_id, m_exercise_skills) : sk;
	}

	public Skill find_skills(int skill_id, ArrayList<Skill> skills) {
		for (Skill sk : skills) {
			if (skill_id == sk.get_id())
				return sk;
		}
		return null;
	}

	public MJCompanionSkillEffect get_skill_effect() {
		return m_skill_effect;
	}

	public int get_melee_dmg() {
		return m_skill_effect.get_melee_dmg() + m_stat_effect.get_melee_dmg();
	}

	public int get_melee_hit() {
		return m_skill_effect.get_melee_hit() + m_stat_effect.get_melee_hit();
	}

	public int get_melee_cri_hit() {
		return m_skill_effect.get_melee_cri_hit() + m_stat_effect.get_melee_cri();
	}

	public int get_ignore_reduction() {
		return m_skill_effect.get_ignore_reduction();
	}

	public int get_blood_suck_hit() {
		return m_skill_effect.get_blood_suck_hit();
	}

	public int get_blood_suck_heal() {
		return m_skill_effect.get_blood_suck_heal();
	}

	public int get_dmg_reduction() {
		return m_skill_effect.get_dmg_reduction() + m_stat_effect.get_reduction();
	}

	public int get_spell_dmg_multi() {
		return m_skill_effect.get_spell_dmg_multi() + m_stat_effect.get_skill_dmg();
	}

	public int get_spell_hit() {
		return m_skill_effect.get_spell_hit() + m_stat_effect.get_skill_ratio();
	}

	public int get_spell_dmg() {
		return m_skill_effect.get_spell_dmg_add() + m_stat_effect.get_skill_dmg();
	}

	public int get_spell_cri() {
		return m_stat_effect.get_skill_cri();
	}

	public int get_fire_elemental_dmg() {
		return m_skill_effect.get_fire_elemental_dmg();
	}

	public int get_water_elemental_dmg() {
		return m_skill_effect.get_water_elemental_dmg();
	}

	public int get_air_elemental_dmg() {
		return m_skill_effect.get_air_elemental_dmg();
	}

	public int get_earth_elemental_dmg() {
		return m_skill_effect.get_earth_elemental_dmg();
	}

	public int get_light_elemental_dmg() {
		return m_skill_effect.get_light_elemental_dmg();
	}

	public int get_combo_dmg() {
		return m_skill_effect.get_combo_dmg_multi();
	}

	public void use_healing_potion(int heal_hp) {
		setCurrentHp(getCurrentHp() + m_skill_effect.get_potion_hp() + heal_hp);
	}

	public void set_is_teleporting(boolean is_teleporting) {
		m_is_teleporting = is_teleporting;
	}

	public boolean get_is_teleporting() {
		return m_is_teleporting;
	}

	public void update_pink_name_millis() {
		m_pink_name_millis = System.currentTimeMillis();
	}

	public long get_pink_name_millis() {
		return m_pink_name_millis;
	}

	public void add_trigger(MJEffectTriggerInstance trigger) {
		m_triggers.put(trigger.get_identity(), trigger);
	}

	public void remove_trigger(int identity) {
		MJEffectTriggerInstance trigger = m_triggers.remove(identity);
		if (trigger != null) {
			trigger.dispose();
		}
	}

	public void remove_triggers() {
		ArrayList<MJEffectTriggerInstance> triggers = get_triggers();
		m_triggers.clear();
		if (triggers != null) {
			for (MJEffectTriggerInstance trigger : triggers) {
				trigger.dispose();
			}
		}
	}

	public ArrayList<MJEffectTriggerInstance> get_triggers() {
		return m_triggers.size() <= 0 ? null : new ArrayList<MJEffectTriggerInstance>(m_triggers.values());
	}

	public int get_pink_name_remain_seconds() {
		long diff = System.currentTimeMillis() - get_pink_name_millis();
		return (int) (diff >= 1000 && diff < 20000 ? diff / 1000 : 0);
	}

	public void do_name_change(String replace_name) {
		setName(replace_name);
		update_name();
		S_RemoveObject remove = new S_RemoveObject(this);
		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance(this);
		for (L1PcInstance visible : L1World.getInstance().getRecognizePlayer(this)) {
			visible.sendPackets(remove, false);
			visible.sendPackets(noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, false);
		}
		remove.clear();
		noti.dispose();
		if (m_master != null)
			SC_COMPANION_STATUS_NOTI.send(m_master, this, MJCompanionUpdateFlag.UPDATE_NAME);
	}

	public void do_open_tier(MJCompanionClassSkillInfo class_skill_info) {
		set_tier(class_skill_info.get_tier());
		ArrayList<Skill> skills = new ArrayList<Skill>();
		for (int skill_id : class_skill_info.get_skills_id()) {
			Skill sk = Skill.newInstance();
			sk.set_id(skill_id);
			sk.set_enchant(0);
			skills.add(sk);
			if (MJCompanionSkillInfo.is_exercise_skill(skill_id))
				add_exercise_skills(sk);
			else
				add_non_exercise_skills(sk);
		}
		if (skills.size() > 0)
			update_skills(skills);
	}

	public int get_remain_stats() {
		int level = getLevel();
		Ability ability = getAbility();
		int total_added_stats = ability.getAddedStr() + ability.getAddedCon() + ability.getAddedInt()/*
																										 * -
																										 * get_elixir_use_count
																										 * ()
																										 */;
		int added_stat = (Math.min(level, 50)) / 10;
		if (level > 50)
			added_stat += (level - 50);

		return added_stat - total_added_stats;
	}

	public int get_remain_elixir(MJCompanionInstance companion) {
		int level = getLevel();
		if (level < MJCompanionSettings.ELIXIR_MIN_USE_LEVEL)
			return 0;

		int remain_level = (level - MJCompanionSettings.ELIXIR_MIN_USE_LEVEL) + MJCompanionSettings.ELIXIR_BY_LEVEL;
		int remain_elixir = remain_level / MJCompanionSettings.ELIXIR_BY_LEVEL;

		if (companion.get_elixir_use_count() < remain_elixir) {
			if (companion.get_elixir_use_count() >= MJCompanionSettings.ELIXIR_MAX_USE_COUNT) {
				m_master.sendPackets(4472);
				return 0;
			}
		} else {
			m_master.sendPackets(4472);
			return 0;
		}

		return Math.min(remain_elixir, MJCompanionSettings.ELIXIR_MAX_USE_COUNT);
	}

	public void set_target(L1Character target) {
		if (target == null)
			return;

		if (m_command_state.equals(eCommand.TM_Aggressive)) {
			setHate(target, 0);
			if (_target != null) {
				setHate(_target, 0);
				_target = target;
			}
			if (!isAiRunning())
				startAI();
		}
	}

	private void set_target_by_defensive(L1Character target) {
		if (target == null)
			return;

		if (m_command_state.equals(eCommand.TM_Defensive)) {
			setHate(target, 0);
			if (_target != null) {
				setHate(_target, 0);
				_target = target;
			}
			if (!isAiRunning())
				startAI();
		}
	}

	public void do_store_object() {
		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
	}

	private void do_death_penalty() {
		int old_level = getLevel();
		if (old_level <= 30)
			return;

		if (m_master != null && m_master.getInventory().checkItem(MJCompanionSettings.EXP_SAFETY_ITEMID)
				&& m_master.getInventory().consumeItem(MJCompanionSettings.EXP_SAFETY_ITEMID, 1)) {
			m_master.sendPackets("由於祝福，寵物的經驗值得到了保護。");
			return;
		}
		long need_exp = ExpTable.getNeedExpNextLevel(old_level);
		long exp = (long) (need_exp * 0.05);
		set_keep_exp(exp);
		update_exp(-exp);
		update_keep_exp();
	}

	public void do_restore_keep_exp(boolean is_pass_needed) {
		long keep_exp = get_keep_exp();
		if (keep_exp <= 0) {
			m_master.sendPackets("沒有可恢復的經驗值。");
			return;
		}
		if (!is_pass_needed) {
			if (MJCompanionSettings.EXP_RESTORE_NEED_ADENA > 0) {
				if (!m_master.getInventory().checkItem(L1ItemId.ADENA, MJCompanionSettings.EXP_RESTORE_NEED_ADENA)
						|| !m_master.getInventory().consumeItem(L1ItemId.ADENA,
								MJCompanionSettings.EXP_RESTORE_NEED_ADENA)) {
					m_master.sendPackets(String.format("恢復經驗值至少需要 %d 金幣。", MJCompanionSettings.EXP_RESTORE_NEED_ADENA));
					return;
				}
			}
		}

		set_keep_exp(0);
		update_exp(get_exp() + keep_exp, MJCompanionUpdateFlag.UPDATE_EXP);
		update_keep_exp();
	}

	public void do_combo_run() {
		set_is_combo_time(true);
		set_friend_ship_guage(0);
		add_friend_ship_marble(MJCompanionSettings.FRIEND_SHIP_TO_MARBLE);
		update_friend_ship_marble();
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!get_is_combo_time())
					return;

				set_is_combo_time(false);
				if (m_master != null) {
					SC_COMPANION_STATUS_NOTI.send(m_master, MJCompanionInstance.this,
							MJCompanionUpdateFlag.UPDATE_FRIEND_SHIP | MJCompanionUpdateFlag.UPDATE_DELAY_REDUCE
									| MJCompanionUpdateFlag.UPDATE_COMBO_TIME);
				}
			}
		}, MJCompanionSettings.COMBO_DURATION_MILLIS);
	}

	public void do_update_regen_tick() {
		int hpr = m_skill_effect.get_regen_hp();
		hpr += m_stat_effect.get_hpr();
		hpr = Math.max(hpr, 1);
		setCurrentHp(getCurrentHp() + hpr);
		setCurrentMp(getCurrentMp() + 1);
	}

	public void do_oblivion(boolean is_pass_needed) {
		if (!is_pass_needed) {
			if (MJCompanionSettings.OBLIVION_NEED_ADENA > 0) {
				if (!m_master.getInventory().checkItem(L1ItemId.ADENA, MJCompanionSettings.OBLIVION_NEED_ADENA)
						|| !m_master.getInventory().consumeItem(L1ItemId.ADENA,
								MJCompanionSettings.OBLIVION_NEED_ADENA)) {
					m_master.sendPackets(
							String.format("遺忘至少需要 %d 金幣。", MJCompanionSettings.OBLIVION_NEED_ADENA));
					return;
				}
			}
		}

		m_master.sendPackets(String.format("已遺忘寵物 %s。", getName()));
		set_is_oblivion(true);
		Ability ability = getAbility();
		ability.addAddedStr(-ability.getAddedStr());
		ability.addAddedCon(-ability.getAddedCon());
		ability.addAddedInt(-ability.getAddedInt());
		oblivion_buffs();
		oblivion_skills();
		set_tier(1);
		MJCompanionClassSkillInfo class_skill_info = MJCompanionClassSkillInfo
				.get_companion_skills(get_class_info().get_class_name(), get_tier());
		do_open_tier(class_skill_info);
		setName(m_class_info.get_class_npc_name());
		set_name_changed_count(0);
		deleteMe();
	}

	public void do_stats_initialized() {
		if (m_master == null)
			return;

		Ability ability = getAbility();
		ability.addAddedStr(-ability.getAddedStr());
		ability.addAddedCon(-ability.getAddedCon());
		ability.addAddedInt(-ability.getAddedInt());
		update_stats();
		update_instance();
		SC_COMPANION_STATUS_NOTI.send(m_master, this, MJCompanionUpdateFlag.UPDATE_ALL);
	}

	private void do_move_master() {
		if (m_master != null) {
			MJCompanionTeleportHandler handler = new MJCompanionTeleportHandler();
			handler.on_teleported(m_master, m_master.getX(), m_master.getY(), m_master.getMapId(), m_master.getMapId());
		}
	}

	public void do_traning(boolean is_pass_needed) {
		if (m_master == null)
			return;

		if (get_is_traning()) {
			m_master.sendPackets("今天已經訓練過了。");
			return;
		}

		if (getLevel() < MJCompanionSettings.TRANING_MIN_LEVEL) {
			m_master.sendPackets(String.format("訓練需寵物等級至少達到 %d。", MJCompanionSettings.TRANING_MIN_LEVEL));
			return;
		}

		if (!is_pass_needed) {
			if (get_friend_ship_marble() < MJCompanionSettings.TRANING_NEED_FRIEND_SHIP) {
				m_master.sendPackets(
						String.format("訓練需要 %d 友誼點數。", MJCompanionSettings.TRANING_NEED_FRIEND_SHIP));
				return;
			}

			if (!m_master.getInventory().checkItem(MJCompanionSettings.TRANING_NEED_ITEM_ID,
					MJCompanionSettings.TRANING_NEED_ITEM_COUNT)
					|| !m_master.getInventory().consumeItem(MJCompanionSettings.TRANING_NEED_ITEM_ID,
							MJCompanionSettings.TRANING_NEED_ITEM_COUNT)) {
				m_master.sendPackets(
						String.format("訓練需要 %d 顆重生寶石。", MJCompanionSettings.TRANING_NEED_ITEM_COUNT));
				return;
			}
			set_friend_ship_marble(get_friend_ship_marble() - MJCompanionSettings.TRANING_NEED_FRIEND_SHIP);
		}
		set_is_traning(true);
		update_traning();
		double exp = MJCompanionSettings.TRANING_PERCENT_BY_EXP
				* ExpTable.getNeedExpNextLevel(MJCompanionSettings.TRANING_EXP_BY_LEVEL);
		update_exp((long) (get_exp() + exp), MJCompanionUpdateFlag.UPDATE_EXP);
		m_master.sendPackets(String.format("已訓練寵物 %s。", getName()));
	}

	private void find_items() {
		if (m_master == null)
			return;

		ArrayList<L1GroundInventory> ground_inventories = new ArrayList<L1GroundInventory>();
		for (L1Object obj : L1World.getInstance().getVisibleObjects(m_master, 10)) {
			if (obj != null && obj instanceof L1GroundInventory)
				ground_inventories.add((L1GroundInventory) obj);
		}

		for (L1GroundInventory inventory : ground_inventories) {
			for (L1ItemInstance item : inventory.getItems()) {
				if (item.getItemOwner() != null && item.getItemOwner().getId() != m_master.getId())
					continue;

				if (_inventory.checkAddItem(item, item.getCount()) == L1Inventory.OK) {
					_targetItem = item;
					_targetItemList.add(_targetItem);
				}
			}
		}
	}

	private void do_drain_items() {
		L1PcInventory master_inventory = m_master.getInventory();
		List<L1ItemInstance> items = _inventory.getItems();
		for (L1ItemInstance item : items) {
			if (master_inventory.checkAddItem(item, item.getCount()) == L1Inventory.OK) {
				int count = item.getCount();
				_inventory.tradeItem(item, count, master_inventory);
				m_master.sendPackets(
						new S_ServerMessage(143, getName(), String.format("%s (%,d)", item.getName(), count)));
				// m_master.sendPackets(new S_ServerMessage(143, getName(), item.getLogName()));
			} else {
				_inventory.tradeItem(item, item.getCount(),
						L1World.getInstance().getInventory(getX(), getY(), getMapId()));
			}
		}
	}

	private L1Inventory find_drop_inventory() {
		if (m_master != null) {
			return m_master.getInventory();
		}
		L1Object obj = L1World.getInstance().findObject(m_master_id);
		if (obj != null && obj instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) obj;
			if (pc.getNetConnection() != null && pc.getNetConnection().isConnected())
				return pc.getInventory();
		}
		return MySqlCharacterStorage.isValidUserObjectId(m_master_id) ? null
				: L1World.getInstance().getInventory(getX(), getY(), getMapId());
	}

	private void do_drop_items() {
		List<L1ItemInstance> items = _inventory.getItems();
		if (items == null || items.size() <= 0)
			return;

		L1Inventory target_inventory = find_drop_inventory();
		if (target_inventory == null) {
			CharactersItemStorage storage = CharactersItemStorage.create();
			for (L1ItemInstance item : items) {
				try {
					storage.storeItem(m_master_id, item);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			_inventory.clearItems();
		} else {
			for (L1ItemInstance item : items) {
				_inventory.tradeItem(item, item.getCount(), target_inventory);
			}
		}
	}

	public void do_pink_name() {
		update_pink_name_millis();
		send_pink_name(20);
	}

	public void on_master_attacked(L1PcInstance receiver) {
		if (m_command_state.equals(eCommand.TM_Aggressive)) {
			update_pink_name_millis();
			send_pink_name(20);
		}
	}

	public boolean on_buff_stopped(int skill_id) {
		MJCompanionBuffInfo bInfo = MJCompanionBuffInfo.from_spell_id(skill_id);
		MJCompanionInstance companion = m_master.get_companion();
		if (bInfo == null)
			return false;

		if (m_master != null) {
			SC_COMPANION_BUFF_NOTI.send(m_master, skill_id, 0);
			if (bInfo.get_pvp_offense() > 0)
				SC_COMPANION_STATUS_NOTI.send(m_master, this, MJCompanionUpdateFlag.UPDATE_PVP_DAMAGE);
		}
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(companion)) {
			pc.sendPackets(new S_Petskill(S_Petskill.DogBlood, companion, true), true);
		}
		return true;
	}

	public void death(L1Character last_attacker) {
		synchronized (m_death_lock) {
			if (isDead())
				return;

			setDead(true);
		}
		if (getZoneType() != 1 && getZoneType() != -1) {
			if (last_attacker instanceof L1PcInstance && get_pink_name_remain_seconds() <= 0) {
				L1PcInstance pc = (L1PcInstance) last_attacker;
				int lawful = MJKDA.calculate_lawful(pc);
				pc.setLawful(lawful);
				pc.send_lawful();
			}

			if (getMap().isEnabledDeathPenalty())
				do_death_penalty();
		}
		if (get_is_combo_time()) {
			set_is_combo_time(false);
		}

		setStatus(ActionCodes.ACTION_Die);
		setCurrentHp(0);
		getMap().setPassable(getLocation(), true);
		send_action(ActionCodes.ACTION_Die);
		MJCompanionInstanceCache.update_cache(this, true);
		if (m_master != null)
			SC_COMPANION_STATUS_NOTI.send(m_master, this, MJCompanionUpdateFlag.UPDATE_ALL);
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (isDead() && getCurrentHp() <= 0 && L1World.getInstance().contains_companion(getId())) {
					if (m_master != null)
						m_master.remove_companion();
					else
						deleteMe();
				} else {
					MJCompanionInstanceCache.update_cache(MJCompanionInstance.this, false);
				}
			}
		}, 60000);
	}

	@Override
	public void deleteMe() {
		if (get_is_combo_time()) {
			set_is_combo_time(false);
		}
		MJCompanionInstanceCache.update_cache(this, false);
		update_instance();
		update_skills();
		update_buffs();
		do_drop_items();
		if (m_master != null) {
			m_master.set_companion(null);
			m_master = null;
		}
		remove_triggers();
		super.deleteMe();
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.addKnownObject(this);
		if (perceivedFrom.getAI() != null)
			return;

		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance(this);
		perceivedFrom.sendPackets(noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true);
		if (perceivedFrom.getId() == get_master_id()) {
			SC_COMPANION_STATUS_NOTI.send(perceivedFrom, this, MJCompanionUpdateFlag.UPDATE_ALL);
		}
		if (isDead()) {
			perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));
		} else {
			int remain_pink_name_seconds = get_pink_name_remain_seconds();
			if (remain_pink_name_seconds > 0)
				perceivedFrom.sendPackets(new S_PinkName(getId(), remain_pink_name_seconds));
		}
	}

	public int get_temp_max_hp() {
		return m_skill_effect == null ? 0 : m_skill_effect.get_max_hp();
	}

	public int get_base_max_hp() {
		return super.getMaxHp();
	}

	@Override
	protected void on_moved() {
		for (MJEffectTriggerInstance trigger : m_triggers.values())
			trigger.do_move(getX(), getY(), getHeading());
	}

	public void on_attacked() {
		// for(MJEffectTriggerInstance trigger : m_triggers.values())
		// trigger.do_action(0);
	}

	@Override
	public int getMaxHp() {
		return get_base_max_hp() + get_temp_max_hp();
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
		if (m_master != null) {
			SC_COMPANION_STATUS_NOTI.send(m_master, this, MJCompanionUpdateFlag.UPDATE_HP);
			m_master.sendPackets(new S_HPMeter(this));
		}
	}

	@Override
	public void setCurrentMp(int i) {
		super.setCurrentMp(i);
	}

	@Override
	public boolean noTarget() {

		if (m_master == null) {
			deleteMe();
			return true;
		}
		if (m_master.getMapId() != getMapId())
			return true;

		if (m_command_state.equals(eCommand.TM_GetItem)) {
			find_items();
			if (_targetItem != null)
				return false;
		}

		if (getLocation().getTileLineDistance(m_master.getLocation()) > 2) {
			int dir = moveDirection(m_master.getMapId(), m_master.getX(), m_master.getY());
			if (dir == -1) {
				do_move_master();
				return true;
			}
			setSleepTime(setDirectionMoveSpeed(dir));
			if (m_command_state.equals(eCommand.TM_GetItem)) {
				do_drain_items();
			}
		}
		return false;
	}

	@Override
	public void checkTargetItem() {
		if (m_master == null) {
			_targetItem = null;
			_targetItemList.clear();
			return;
		}
		if (_targetItem == null || _targetItem.getMapId() != getMapId()
				|| m_master.getLocation().getTileDistance(_targetItem.getLocation()) > 15) {
			if (!_targetItemList.isEmpty()) {
				_targetItem = _targetItemList.get(0);
				_targetItemList.remove(0);
				checkTargetItem();
			} else {
				_targetItem = null;
			}
		}
	}

	@Override
	public int moveDirection(int mapid, int x, int y, double d) {
		int dir = 0;
		if (hasSkillEffect(L1SkillId.DARKNESS) == true && d >= 2D) {
			return -1;
		} else if (_target != null && getLocation().getTileLineDistance(_target.getLocation()) > 18) {
			allTargetClear();
		} else if (d > courceRange) {
			dir = targetDirection(x, y);
			dir = checkObject(getX(), getY(), getMapId(), dir);
		} else {
			dir = _serchCource(x, y, mapid);
			if (dir == -1) {
				dir = targetDirection(x, y);
				if (!isExsistCharacterBetweenTarget(dir)) {
					dir = checkObject(getX(), getY(), getMapId(), dir);
				}
			}
		}
		return dir;
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {

		if (attacker instanceof L1PcInstance) {
			if (m_master == null) {
				deleteMe();
				return;
			}
			L1PinkName.onAction(m_master, attacker);
		}

		if (getCurrentHp() <= 0) {
			if (!isDead())
				death(attacker);
			return;
		}
		if (hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
			removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
		} else if (hasSkillEffect(L1SkillId.PHANTASM)) {
			removeSkillEffect(L1SkillId.PHANTASM);
		}

		int reduction = get_dmg_reduction();
		damage -= reduction;
		damage -= MJCompanionBuffInfo.get_pvp_melee_defense_bonus(this);
		if (damage <= 0)
			return;

		if (attacker instanceof L1PcInstance) {
			((L1PcInstance) attacker).set_pet_target(this);
			damage = (int) (damage * MJCompanionSettings.MAGNIFICATION_BY_PC_TO_PET);
			set_target_by_defensive(attacker);
		}
		int new_hp = getCurrentHp() - damage;
		if (new_hp <= 0)
			death(attacker);
		else
			setCurrentHp(new_hp);
	}

	@Override
	public void onAction(L1PcInstance player) {
		if (player == null)
			return;

		if (m_master == null) {
			deleteMe();
			return;
		}

		if (m_master.get_teleport())
			return;

		if (getZoneType() == 1) {
			L1Attack attack_mortion = new L1Attack(player, this);
			attack_mortion.action();
			return;
		}
		if (player.checkNonPvP(player, this))
			return;

		L1Attack attack = new L1Attack(player, this);
		if (attack.calcHit()) {
			attack.calcDamage();
		}
		attack.action();
		attack.commit();
	}

	@Override
	public void onGetItem(L1ItemInstance item) {
		if (item == null || item.getItem() == null || m_master == null)
			return;

		L1PcInventory master_inventory = m_master.getInventory();
		if (master_inventory.checkAddItem(item, item.getCount()) == L1Inventory.OK) {
			int count = item.getCount();
			_inventory.tradeItem(item, count, master_inventory);
			m_master.sendPackets(new S_ServerMessage(143, getName(), String.format("%s (%,d)", item.getName(), count)));
			// m_master.sendPackets(new S_ServerMessage(143, getName(), item.getLogName()));
		} else {
			_inventory.tradeItem(item, item.getCount(), L1World.getInstance().getInventory(getX(), getY(), getMapId()));
		}
	}

	@Override
	public boolean checkCondition() {
		if (m_master == null) {
			deleteMe();
			return true;
		}

		if (m_master.isInWarArea()) {
			m_master.remove_companion();
			return true;
		}
		return false;
	}

	private void update_friend_ship_by_exp(long exp) {
		double guage = exp * MJCompanionSettings.FRIEND_SHIP_BY_EXP;
		double guage_bonus = MJCompanionBuffInfo.get_friend_ship_bonus_percent(this);
		if (guage_bonus > 0)
			guage += (exp * guage_bonus);
		add_friend_ship_guage((int) guage);
	}

	private long calculate_total_exp(long exp) {
		long total_exp = (long) ((exp > 0 ? (exp * MJCompanionSettings.EXP_SCALE) : exp) + get_exp());
		long exp_by_level = ExpTable.getExpByLevel(MJCompanionSettings.COMPANION_MAX_LEVEL + 1);
		total_exp = Math.min(total_exp, exp_by_level - 1);
		return total_exp;
	}

	public void update_exp(long exp) {
		if (m_master == null)
			return;

		if (exp > 0) {
			double bonus_percent = MJCompanionBuffInfo.get_exp_bonus_percent(this);
			if (bonus_percent > 0)
				exp += (exp * bonus_percent);
			exp *= MJCompanionExpPenalty.get_companion_penalty_rate(getLevel());
		}

		int update_flag = MJCompanionUpdateFlag.UPDATE_EXP;
		long total_exp = calculate_total_exp(exp);
		if (exp > 0) {
			update_flag |= MJCompanionUpdateFlag.UPDATE_FRIEND_SHIP;
			update_friend_ship_by_exp(exp);
			if (!get_is_combo_time() && m_friend_ship_guage >= MJCompanionSettings.FRIEND_SHIP_MAX_GUAGE) {
				update_flag |= MJCompanionUpdateFlag.UPDATE_COMBO_TIME | MJCompanionUpdateFlag.UPDATE_DELAY_REDUCE;
				do_combo_run();
			}
		}
		update_exp(total_exp, update_flag);
	}

	public void update_exp(long total_exp, int update_flag) {
		set_exp(total_exp);
		int before_level = getLevel();
		int next_level = ExpTable.getLevelByExp(total_exp);
		if (next_level > before_level) {
			setLevel(next_level);
			int gap = getLevel() - before_level;
			if (gap > 0) {
				for (int i = gap - 1; i >= 0; --i) {
					addMaxHp(MJRnd.next(m_stat_effect.get_add_min_hp(), m_stat_effect.get_add_max_hp()));
					addMaxMp(getAbility().getTotalWis() / 2);
				}
				// broadcastPacket(new S_SkillSound(getId(), 6353));
				m_master.sendPackets(new S_ServerMessage(320, getName()));
				update_instance();
				MJCompanionInstanceCache.update_cache(this, true);
				SC_COMPANION_STATUS_NOTI.send(m_master, this, MJCompanionUpdateFlag.UPDATE_ALL | update_flag);
				SC_UPDATE_INVENTORY_NOTI.send_companion_control_item_name(m_master, get_control_object_id(),
						get_class_info().get_class_npc_name_id(), getLevel());
			}
		} else {
			SC_COMPANION_STATUS_NOTI.send(m_master, this, update_flag);
		}
	}

	public void load_skills() {
		ArrayList<Skill> exercise = new ArrayList<Skill>();
		ArrayList<Skill> non_exercise = new ArrayList<Skill>();
		Selector.exec("select * from companion_instance_skills where object_id=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, getId());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					Skill sk = Skill.newInstance();
					sk.set_id(rs.getInt("skill_id"));
					sk.set_enchant(rs.getInt("enchant"));
					if (MJCompanionSkillInfo.is_exercise_skill(sk.get_id())) {
						exercise.add(sk);
					} else {
						non_exercise.add(sk);
					}
				}
			}
		});
		set_exercise_skills(exercise);
		set_non_exercise_skills(non_exercise);
	}

	private void oblivion_skills() {
		Updator.exec("delete from companion_instance_skills where object_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, getId());
			}
		});
		m_exercise_skills.clear();
		m_non_exercise_skills.clear();
	}

	public void update_skills() {
		final ArrayList<Skill> skills = get_all_skills();
		update_skills(skills);
	}

	public void update_skills(ArrayList<Skill> skills) {
		Updator.batch("insert into companion_instance_skills set object_id=?, skill_id=?, enchant=? "
				+ "on duplicate key update " + "enchant=?", new BatchHandler() {
					@Override
					public void handle(PreparedStatement pstm, int callNumber) throws Exception {
						int idx = 0;
						Skill sk = skills.get(callNumber);
						pstm.setInt(++idx, getId());
						pstm.setInt(++idx, sk.get_id());
						pstm.setInt(++idx, sk.get_enchant());
						pstm.setInt(++idx, sk.get_enchant());
					}
				}, skills.size());
	}

	public void load_buffs() {
		Selector.exec("select * from companion_instance_buff where object_id=?", new SelectorHandler() {

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, getId());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					int spell_id = rs.getInt("spell_id");
					int remain_seconds = rs.getInt("remain_seconds");
					if (remain_seconds > 0)
						setSkillEffect(spell_id, remain_seconds * 1000);
				}
			}
		});
	}

	public void update_skill_effect() {
		if (m_skill_effect != null) {
			getAC().addAc(-m_skill_effect.get_ac());
			getResistance().addMr(-m_skill_effect.get_mr());
		}

		MJCompanionSkillEffect buff_effect = MJCompanionSkillEffect.newInstance();
		for (Skill skInfo : get_non_exercise_skills()) {
			MJCompanionSkillInfo cskInfo = MJCompanionSkillInfo.get_companion_skills(skInfo.get_id(),
					skInfo.get_enchant());
			if (cskInfo == null)
				continue;

			buff_effect.add_melee_dmg(cskInfo.get_melee_dmg()).add_melee_hit(cskInfo.get_melee_hit())
					.add_melee_cri_hit(cskInfo.get_melee_cri_hit()).add_ignore_reduction(cskInfo.get_ignore_reduction())
					.add_blood_suck_hit(cskInfo.get_blood_suck_hit()).add_blood_suck_heal(cskInfo.get_blood_suck_heal())
					.add_regen_hp(cskInfo.get_regen_hp()).add_ac(cskInfo.get_ac()).add_mr(cskInfo.get_mr())
					.add_potion_hp(cskInfo.get_potion_hp()).add_dmg_reduction(cskInfo.get_dmg_reduction())
					.add_max_hp(cskInfo.get_max_hp()).add_spell_dmg_multi(cskInfo.get_spell_dmg_multi())
					.add_spell_hit(cskInfo.get_spell_hit()).add_move_delay_reduce(cskInfo.get_move_delay_reduce())
					.add_attack_delay_reduce(cskInfo.get_attack_delay_reduce())
					.add_fire_elemental_dmg(cskInfo.get_fire_elemental_dmg())
					.add_water_elemental_dmg(cskInfo.get_water_elemental_dmg())
					.add_air_elemental_dmg(cskInfo.get_air_elemental_dmg())
					.add_earth_elemental_dmg(cskInfo.get_earth_elemental_dmg())
					.add_light_elemental_dmg(cskInfo.get_light_elemental_dmg())
					.add_combo_dmg_multi(cskInfo.get_combo_dmg_multi()).add_spell_dmg_add(cskInfo.get_spell_dmg_add());
		}
		m_skill_effect = buff_effect;
		getAC().addAc(m_skill_effect.get_ac());
		getResistance().addMr(m_skill_effect.get_mr());
	}

	public void update_stats() {
		if (m_stat_effect != null) {
			getAC().addAc(-m_stat_effect.get_ac());
			getResistance().addMr(-m_stat_effect.get_mr());
		}
		Ability ability = getAbility();
		m_stat_effect = MJCompanionStatEffect.createInstance(ability.getTotalStr(), ability.getTotalCon(),
				ability.getTotalInt());
		getAC().addAc(m_stat_effect.get_ac());
		getResistance().addMr(m_stat_effect.get_mr());
	}

	private void oblivion_buffs() {
		for (int spell_id : L1SkillId.COMPANION_BUFFS) {
			if (!hasSkillEffect(spell_id))
				continue;

			removeSkillEffect(spell_id);
		}
		Updator.exec("delete from companion_instance_buff where object_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, getId());
			}
		});
	}

	public void update_buffs() {
		ArrayList<Integer[]> store_items = new ArrayList<Integer[]>(2);
		for (int spell_id : L1SkillId.COMPANION_BUFFS) {
			if (!hasSkillEffect(spell_id))
				continue;

			store_items.add(new Integer[] { spell_id, getSkillEffectTimeSec(spell_id) });
		}
		if (store_items.size() > 0)
			update_buffs(store_items);
	}

	private void update_buffs(final ArrayList<Integer[]> store_items) {
		final int object_id = getId();
		Updator.batch("insert into companion_instance_buff set object_id=?, spell_id=?, remain_seconds=? "
				+ "on duplicate key update " + "remain_seconds=?", new BatchHandler() {
					@Override
					public void handle(PreparedStatement pstm, int callNumber) throws Exception {
						int idx = 0;
						Integer[] integers = store_items.get(callNumber);
						pstm.setInt(++idx, object_id);
						pstm.setInt(++idx, integers[0]);
						pstm.setInt(++idx, integers[1]);
						pstm.setInt(++idx, integers[1]);
					}
				}, store_items.size());
	}

	public void update_elixir_use_count() {
		MJCompanionInstanceCache.update_cache(this, true);
		Updator.exec(
				"update companion_instance set elixir_use_count=?, elixir_str=?, elixir_con=?, elixir_intel=? where control_object_id=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						Ability ability = getAbility();
						pstm.setInt(1, get_elixir_use_count());
						pstm.setInt(2, ability.getHunt());
						pstm.setInt(3, ability.getSurvival());
						pstm.setInt(4, ability.getSacred());
						pstm.setInt(5, get_control_object_id());
					}
				});
	}

	public void update_friend_ship_marble() {
		Updator.exec("update companion_instance set friend_ship_marble=? where control_object_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, get_friend_ship_marble());
				pstm.setInt(2, get_control_object_id());
			}
		});
	}

	public void update_name() {
		MJCompanionInstanceCache.update_cache(this, true);
		Updator.exec("update companion_instance set companion_name=?, name_changed_count=? where control_object_id=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setString(1, getName());
						pstm.setInt(2, get_name_changed_count());
						pstm.setInt(3, get_control_object_id());
					}
				});
	}

	public void update_tier() {
		Updator.exec("update companion_instance set tier=? where control_object_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, get_tier());
				pstm.setInt(2, get_control_object_id());
			}
		});
	}

	public void update_name_changed_count() {
		Updator.exec("update companion_instance set name_changed_count=? where control_object_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, get_name_changed_count());
				pstm.setInt(2, get_control_object_id());
			}
		});
	}

	public void update_keep_exp() {
		Updator.exec("update companion_instance set keep_exp=? where control_object_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setLong(1, get_keep_exp());
				pstm.setInt(2, get_control_object_id());
			}
		});
	}

	public void update_traning() {
		Updator.exec("update companion_instance set is_traning=? where control_object_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, get_is_traning() ? 1 : 0);
				pstm.setInt(2, get_control_object_id());
			}
		});
	}

	public void update_instance() {
		Updator.exec(
				"insert into companion_instance set object_id=?, control_object_id=?, class_name=?, companion_name=?, friend_ship_guage=?, friend_ship_marble=?, level=?, exp=?, keep_exp=?, current_hp=?, current_mp=?, max_hp=?, max_mp=?, lawful=?, tier=?, str=?, elixir_str=?, con=?, elixir_con=?, intel=?, elixir_intel=?, elixir_use_count=?, is_oblivion=?, is_traning=?, name_changed_count=? "
						+ "on duplicate key update "
						+ "companion_name=?, friend_ship_guage=?, friend_ship_marble=?, level=?, exp=?, keep_exp=?, current_hp=?, current_mp=?, max_hp=?, max_mp=?, lawful=?, tier=?, str=?, elixir_str=?, con=?, elixir_con=?, intel=?, elixir_intel=?, elixir_use_count=?, is_oblivion=?, is_traning=?, name_changed_count=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						MJCompanionClassInfo cInfo = get_class_info();
						int idx = 0;
						pstm.setInt(++idx, getId());
						pstm.setInt(++idx, get_control_object_id());
						pstm.setString(++idx, cInfo.get_class_name());
						pstm.setString(++idx, getName());
						pstm.setInt(++idx, get_friend_ship_guage());
						pstm.setInt(++idx, get_friend_ship_marble());
						pstm.setInt(++idx, getLevel());
						pstm.setLong(++idx, get_exp());
						pstm.setLong(++idx, get_keep_exp());
						pstm.setInt(++idx, getCurrentHp());
						pstm.setInt(++idx, getCurrentMp());
						pstm.setInt(++idx, get_base_max_hp());
						pstm.setInt(++idx, getMaxMp());
						pstm.setInt(++idx, getLawful());
						pstm.setInt(++idx, get_tier());
						pstm.setInt(++idx, getAbility().getAddedStr());
						pstm.setInt(++idx, getAbility().getHunt());
						pstm.setInt(++idx, getAbility().getAddedCon());
						pstm.setInt(++idx, getAbility().getSurvival());
						pstm.setInt(++idx, getAbility().getAddedInt());
						pstm.setInt(++idx, getAbility().getSacred());
						pstm.setInt(++idx, get_elixir_use_count());
						pstm.setInt(++idx, get_is_oblivion() ? 1 : 0);
						pstm.setInt(++idx, get_is_traning() ? 1 : 0);
						pstm.setInt(++idx, get_name_changed_count());

						pstm.setString(++idx, getName());
						pstm.setInt(++idx, get_friend_ship_guage());
						pstm.setInt(++idx, get_friend_ship_marble());
						pstm.setInt(++idx, getLevel());
						pstm.setLong(++idx, get_exp());
						pstm.setLong(++idx, get_keep_exp());
						pstm.setInt(++idx, getCurrentHp());
						pstm.setInt(++idx, getCurrentMp());
						pstm.setInt(++idx, get_base_max_hp());
						pstm.setInt(++idx, getMaxMp());
						pstm.setInt(++idx, getLawful());
						pstm.setInt(++idx, get_tier());
						pstm.setInt(++idx, getAbility().getAddedStr());
						pstm.setInt(++idx, getAbility().getHunt());
						pstm.setInt(++idx, getAbility().getAddedCon());
						pstm.setInt(++idx, getAbility().getSurvival());
						pstm.setInt(++idx, getAbility().getAddedInt());
						pstm.setInt(++idx, getAbility().getSacred());
						pstm.setInt(++idx, get_elixir_use_count());
						pstm.setInt(++idx, get_is_oblivion() ? 1 : 0);
						pstm.setInt(++idx, get_is_traning() ? 1 : 0);
						pstm.setInt(++idx, get_name_changed_count());
					}
				});
	}
}
