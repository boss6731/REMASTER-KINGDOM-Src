package l1j.server.BonusDropSystem;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.model.Instance.L1PcInstance;

public class BonusDropSystemInfo {
	static BonusDropSystemInfo newInstance(ResultSet rs) throws SQLException {
		BonusDropSystemInfo pInfo = newInstance();
		pInfo._npc_id = rs.getInt("npc_id");
		pInfo._npc_name = rs.getString("npc_name");
		pInfo._item_id = rs.getString("item_id");
		pInfo._item_name = rs.getString("item_name");
		pInfo._item_enchant = rs.getString("item_enchant");
		pInfo._min_item_count = rs.getString("min_count");
		pInfo._max_item_count = rs.getString("max_count");
		pInfo._min_probability = rs.getString("min_probability");
		pInfo._max_probability = rs.getString("max_probability");
		pInfo._Effect = rs.getBoolean("Effect");
		pInfo._EffectId = rs.getInt("EffectId");
		pInfo._isMent = rs.getBoolean("isMent");
		pInfo._isMentMess = rs.getString("isMentMess");	
		pInfo._is_loggin = rs.getBoolean("is_loggin");
		return pInfo;
	}
	
	private static BonusDropSystemInfo newInstance() {
		return new BonusDropSystemInfo();
	}
	
	private int _npc_id;
	private String _npc_name;
	private String _item_id;
	private String _item_name;
	private String _item_enchant;
	private String _min_item_count;
	private String _max_item_count;
	private String _min_probability;
	private String _max_probability;
	private boolean _Effect;
	private int _EffectId;
	private boolean _isMent;
	private String _isMentMess;
	private boolean _is_loggin;
	
	public int get_npcid() {
		return _npc_id;
	}
	
	public String get_npc_name() {
		return _npc_name;
	}

	public String get_item_id() {
		return _item_id;
	}
	
	public String get_item_name() {
		return _item_name;
	}
	
	public String get_item_enchant() {
		return _item_enchant;
	}

	public String get_min_count() {
		return _min_item_count;
	}
	
	public String get_max_count() {
		return _max_item_count;
	}
	
	public String get_min_probability() {
		return _min_probability;
	}
	
	public String get_max_probability() {
		return _max_probability;
	}
	
	public boolean get_Effect() {
		return _Effect;
	}

	public int get_EffectId() {
		return _EffectId;
	}
	
	public boolean get_isMent() {
		return _isMent;
	}
	
	public String get_isMentMess() {
		return _isMentMess;
	}
	
	void print() {
	}

	public int calc_probability(L1PcInstance pc, int i) {
		int probability = 0;

			// 解析最小概率的字符串並分割成數組
		String[] drop_min_chance = (String[]) MJArrangeParser.parsing(_min_probability, ",", MJArrangeParseeFactory.createStringArrange()).result();
		// 解析最大概率的字符串並分割成數組
		String[] drop_max_chance = (String[]) MJArrangeParser.parsing(_max_probability, ",", MJArrangeParseeFactory.createStringArrange()).result();

		// 轉換指定索引的字符串為整數
		int min_chance = Integer.parseInt(drop_min_chance[i]);
		int max_chance = Integer.parseInt(drop_max_chance[i]);

		// 如果最小概率等於最大概率，則設置概率為最大概率
		if (min_chance == max_chance) {
			probability = max_chance;
		} else {
			// 否則，隨機產生一個概率值
			probability = (int) (Math.random() * (max_chance - min_chance)) + min_chance;
		}

		// 如果啟用了日誌記錄
		if (_is_loggin) {
			// 如果玩家是 GM（遊戲管理員）
			if (pc.isGm()) {
				System.out.println(String.format("獎勵發放怪物 : %s , 物品 : +%d %s , 掉落概率:(%d)", _npc_name, _item_enchant, _item_name, probability));
			}
		}

		return probability;
	}

	public int calc_count(L1PcInstance pc, int i) {
		// 解析最小物品數量的字符串並分割成數組
		String[] drop_min_count = (String[]) MJArrangeParser.parsing(_min_item_count, ",", MJArrangeParseeFactory.createStringArrange()).result();
		// 解析最大物品數量的字符串並分割成數組
		String[] drop_max_count = (String[]) MJArrangeParser.parsing(_max_item_count, ",", MJArrangeParseeFactory.createStringArrange()).result();

		// 轉換指定索引的字符串為整數
		int min_count = Integer.parseInt(drop_min_count[i]);
		int max_count = Integer.parseInt(drop_max_count[i]);

		int count = 0;
		// 如果最小數量等於最大數量，則設置數量為最大數量
		if (min_count == max_count) {
			count = max_count;
		} else {
			// 否則，隨機產生一個數量值
			count = (int) (Math.random() * (max_count - min_count)) + min_count;
		}

		// 如果啟用了日誌記錄
		if (_is_loggin) {
			// 如果玩家是 GM（遊戲管理員）
			if (pc.isGm()) {
				System.out.println(String.format("獎勵發放怪物 : %s , 物品 : +%d %s , 掉落數量:(%d)", _npc_name, _item_enchant, _item_name, count));
			}
		}

		return count;
	}
	
			/*public int calc_probability(L1PcInstance pc) {
		int probability = 0;

		// 如果最大概率等於最小概率，則設置概率為最大概率
		if (_max_probability == _min_probability)
		    probability = _max_probability;
		else
		    // 否則，隨機產生一個概率值
		    probability = (int) (Math.random() * (_max_probability - _min_probability)) + _min_probability;

		// 如果啟用了日誌記錄
		if (_is_loggin) {
		    // 如果玩家是 GM（遊戲管理員）
		    if (pc.isGm()) {
		        System.out.println(String.format("獎勵當前怪物 : %s , 物品 : +%d %s , 掉落概率:(%d)", _npc_name, _item_enchant, _item_name, probability));
		    }
		}

		return probability;
		}

		public int calc_count(L1PcInstance pc) {
		int count = 0;

		// 如果最大物品數量等於最小物品數量，則設置數量為最大物品數量
		if (_max_item_count == _min_item_count)
		    count = _max_item_count;
		else
		    // 否則，隨機產生一個數量值
		    count = (int) (Math.random() * (_max_item_count - _min_item_count)) + _min_item_count;

		// 如果啟用了日誌記錄
		if (_is_loggin) {
		    // 如果玩家是 GM（遊戲管理員）
		    if (pc.isGm()) {
		        System.out.println(String.format("獎勵當前怪物 : %s , 物品 : +%d %s , 掉落數量:(%d)", _npc_name, _item_enchant, _item_name, count));
		    }
		}
		return count;
		}*/
}
