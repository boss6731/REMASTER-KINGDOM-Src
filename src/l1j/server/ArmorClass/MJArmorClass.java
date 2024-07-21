package l1j.server.ArmorClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.utils.IntRange;

// MJ防禦等級類別
// MJ防禦等級類別
public class MJArmorClass {
	
	
	
}

	// 靜態方法，用於創建並初始化新的 MJArmorClass 實例
	private static MJArmorClass newInstance(ResultSet rs) throws SQLException {
		return newInstance()
				.set_armor_min(rs.getInt("armor_min"))
				.set_armor_max(rs.getInt("armor_max"))
				.set_to_pc_dodge(rs.getInt("to_pc_dodge"))
				.set_to_pc_reduction(rs.getInt("to_pc_reduction"))
				.set_to_pc_er(rs.getInt("to_pc_er"))
				.set_to_pc_long_reduction(rs.getInt("to_pc_long_reduction"))
				.set_to_npc_dodge(rs.getInt("to_npc_dodge"))
				.set_to_npc_reduction(rs.getInt("to_npc_reduction"))
				.set_to_npc_er(rs.getInt("to_npc_er"))
				.set_to_npc_long_reduction(rs.getInt("to_npc_long_reduction"));
	}

	// 靜態方法，用於創建新的 MJArmorClass 實例
	private static MJArmorClass newInstance() {
		return new MJArmorClass();
	}

	// 實例變數，表示防禦等級的最小值
	private int m_armor_min;

	// 實例變數，表示防禦等級的最大值
	private int m_armor_max;

	// 實例變數，表示對玩家的閃避值
	private int m_to_pc_dodge;

	// 實例變數，表示對玩家的減傷值
	private int m_to_pc_reduction;

	// 實例變數，表示對玩家的 ER 值
	private int m_to_pc_er;

	// 實例變數，表示對玩家的遠程減傷值
	private int m_to_pc_long_reduction;

	// 實例變數，表示對 NPC 的閃避值
	private int m_to_npc_dodge;

	// 實例變數，表示對 NPC 的減傷值
	private int m_to_npc_reduction;

	// 實例變數，表示對 NPC 的 ER 值
	private int m_to_npc_er;

	// 實例變數，表示對 NPC 的遠程減傷值
	private int m_to_npc_long_reduction;

	/**
	 * 設置防禦等級的最小值
	 * @param armor_min 最小防禦值
	 * @return 當前 MJArmorClass 實例
	 */
	public MJArmorClass set_armor_min(int armor_min) {
		this.m_armor_min = armor_min;
		return this;
	}

	/**
	 * 設置防禦等級的最大值
	 * @param armor_max 最大防禦值
	 * @return 當前 MJArmorClass 實例
	 */
	public MJArmorClass set_armor_max(int armor_max) {
		this.m_armor_max = armor_max;
		return this;
	}

	/**
	 * 設置對玩家的閃避值
	 * @param to_pc_dodge 對玩家的閃避值
	 * @return 當前 MJArmorClass 實例
	 */
	public MJArmorClass set_to_pc_dodge(int to_pc_dodge) {
		this.m_to_pc_dodge = to_pc_dodge;
		return this;
	}

	/**
	 * 設置對玩家的減傷值
	 * @param to_pc_reduction 對玩家的減傷值
	 * @return 當前 MJArmorClass 實例
	 */
	public MJArmorClass set_to_pc_reduction(int to_pc_reduction) {
		this.m_to_pc_reduction = to_pc_reduction;
		return this;
	}

	/**
	 * 設置對玩家的 ER 值
	 * @param to_pc_er 對玩家的 ER 值
	 * @return 當前 MJArmorClass 實例
	 */
	public MJArmorClass set_to_pc_er(int to_pc_er) {
		this.m_to_pc_er = to_pc_er;
		return this;
	}

	/**
	 * 設置對玩家的遠程減傷值
	 * @param to_pc_long_reduction 對玩家的遠程減傷值
	 * @return 當前 MJArmorClass 實例
	 */
	public MJArmorClass set_to_pc_long_reduction(int to_pc_long_reduction) {
		this.m_to_pc_long_reduction = to_pc_long_reduction;
		return this;
	}

	/**
	 * 設置對 NPC 的閃避值
	 * @param to_npc_dodge 對 NPC 的閃避值
	 * @return 當前 MJArmorClass 實例
	 */
	public MJArmorClass set_to_npc_dodge(int to_npc_dodge) {
		this.m_to_npc_dodge = to_npc_dodge;
		return this;
	}

	/**
	 * 設置對 NPC 的減傷值
	 * @param to_npc_reduction 對 NPC 的減傷值
	 * @return 當前 MJArmorClass 實例
	 */
	public MJArmorClass set_to_npc_reduction(int to_npc_reduction) {
		this.m_to_npc_reduction = to_npc_reduction;
		return this;
	}

	/**
	 * 設置對 NPC 的 ER 值
	 * @param to_npc_er 對 NPC 的 ER 值
	 * @return 當前 MJArmorClass 實例
	 */
	public MJArmorClass set_to_npc_er(int to_npc_er) {
		this.m_to_npc_er = to_npc_er;
		return this;
	}

	/**
	 * 設置對 NPC 的遠程減傷值
	 * @param to_npc_long_reduction 對 NPC 的遠程減傷值
	 * @return 當前 MJArmorClass 實例
	 */
	public MJArmorClass set_to_npc_long_reduction(int to_npc_long_reduction) {
		this.m_to_npc_long_reduction = to_npc_long_reduction;
		return this;
	}
}
	// MJ防禦等級類別
	public class MJArmorClass {

		// 防止直接創建實例的私有構造方法
		private MJArmorClass() {}

		// 設置防禦等級的最小值
		public MJArmorClass set_armor_min(int armor_min) {
			m_armor_min = armor_min;
			return this;
		}

		// 設置防禦等級的最大值
		public MJArmorClass set_armor_max(int armor_max) {
			m_armor_max = armor_max;
			return this;
		}

		// 設置對玩家的閃避值
		public MJArmorClass set_to_pc_dodge(int to_pc_dodge) {
			m_to_pc_dodge = to_pc_dodge;
			return this;
		}

		// 設置對玩家的減傷值
		public MJArmorClass set_to_pc_reduction(int to_pc_reduction) {
			m_to_pc_reduction = to_pc_reduction;
			return this;
		}

		// 設置對玩家的 ER 值
		public MJArmorClass set_to_pc_er(int to_pc_er) {
			m_to_pc_er = to_pc_er;
			return this;
		}

		// 設置對玩家的遠程減傷值
		public MJArmorClass set_to_pc_long_reduction(int to_pc_long_reduction) {
			m_to_pc_long_reduction = to_pc_long_reduction;
			return this;
		}

		// 設置對 NPC 的閃避值
		public MJArmorClass set_to_npc_dodge(int to_npc_dodge) {
			m_to_npc_dodge = to_npc_dodge;
			return this;
		}

		// 設置對 NPC 的減傷值
		public MJArmorClass set_to_npc_reduction(int to_npc_reduction) {
			m_to_npc_reduction = to_npc_reduction;
			return this;
		}

		// 設置對 NPC 的 ER 值
		public MJArmorClass set_to_npc_er(int to_npc_er) {
			m_to_npc_er = to_npc_er;
			return this;
		}

		// 設置對 NPC 的遠程減傷值
		public MJArmorClass set_to_npc_long_reduction(int to_npc_long_reduction) {
			m_to_npc_long_reduction = to_npc_long_reduction;
			return this;
		}

		// 獲取防禦等級的最小值
		public int get_armor_min() {
			return m_armor_min;
		}

		// 獲取防禦等級的最大值
		public int get_armor_max() {
			return m_armor_max;
		}

		// 獲取對玩家的閃避值
		public int get_to_pc_dodge() {
			return m_to_pc_dodge;
		}

		// 獲取對玩家的減傷值
		public int get_to_pc_reduction() {
			return m_to_pc_reduction;
		}

		// 獲取對玩家的 ER 值
		public int get_to_pc_er() {
			return m_to_pc_er;
		}

		// 獲取對玩家的遠程減傷值
		public int get_to_pc_long_reduction() {
			return m_to_pc_long_reduction;
		}

		// 獲取對 NPC 的閃避值
		public int get_to_npc_dodge() {
			return m_to_npc_dodge;
		}

		// 獲取對 NPC 的減傷值
		public int get_to_npc_reduction() {
			return m_to_npc_reduction;
		}

		// 獲取對 NPC 的 ER 值
		public int get_to_npc_er() {
			return m_to_npc_er;
		}

		// 獲取對 NPC 的遠程減傷值
		public int get_to_npc_long_reduction() {
			return m_to_npc_long_reduction;
		}

		// 實例變數，表示防禦等級的最小值
		private int m_armor_min;

		// 實例變數，表示防禦等級的最大值
		private int m_armor_max;

		// 實例變數，表示對玩家的閃避值
		private int m_to_pc_dodge;

		// 實例變數，表示對玩家的減傷值
		private int m_to_pc_reduction;

		// 實例變數，表示對玩家的 ER 值
		private int m_to_pc_er;

		// 實例變數，表示對玩家的遠程減傷值
		private int m_to_pc_long_reduction;

		// 實例變數，表示對 NPC 的閃避值
		private int m_to_npc_dodge;

		// 實例變數，表示對 NPC 的減傷值
		private int m_to_npc_reduction;

		// 實例變數，表示對 NPC 的 ER 值
		private int m_to_npc_er;

		// 實例變數，表示對 NPC 的遠程減傷值
		private int m_to_npc_long_reduction;
}

