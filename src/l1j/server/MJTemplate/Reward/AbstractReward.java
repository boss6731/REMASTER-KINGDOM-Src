package l1j.server.MJTemplate.Reward;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.server.model.Instance.L1PcInstance;

public abstract class AbstractReward {
	// 獎勵類型常量
	public static final int REWARD_TYPE_EXP = 0; // 經驗值獎勵
	public static final int REWARD_TYPE_ITEM = 1; // 物品獎勵

	// 解析獎勵類型字串的方法
	public static int parse_reward_type(String s) {
		switch (s) {
			case "EXP":
				return REWARD_TYPE_EXP;
			case "ITEM":
				return REWARD_TYPE_ITEM;
			default:
				throw new IllegalArgumentException(String.format("無效的獎勵類型: %s", s));
		}
	}

	// 從 ResultSet 創建 AbstractReward 實例的方法
	public static AbstractReward newInstance(ResultSet rs) throws SQLException {
		// 解析獎勵類型
		int type = parse_reward_type(rs.getString("reward_type"));
		AbstractReward reward = null;

		// 根據類型創建對應的獎勵實例
		switch (type) {
			case REWARD_TYPE_EXP:
				reward = ExpReward.newInstance();
				break;
			case REWARD_TYPE_ITEM:
				reward = ItemReward.newInstance();
				break;
			default:
				break;
		}

		// 如果成功創建獎勵實例，設置其屬性
		if (reward != null) {
			reward
					.set_reward_id(rs.getInt("reward_id"))
					.set_reward_asset_id(rs.getInt("reward_asset_id"))
					.set_reward_amount(rs.getInt("reward_amount"));
		}
		return reward;
	}

	// 抽象方法，用於設置獎勵的各種屬性
	public abstract AbstractReward set_reward_id(int reward_id);
	public abstract AbstractReward set_reward_asset_id(int reward_asset_id);
	public abstract AbstractReward set_reward_amount(int reward_amount);
}
	
	private int _reward_id;
	private int _reward_asset_id;
	private int _reward_amount;
	public int get_reward_id(){
		return _reward_id;
	}
	
	public AbstractReward set_reward_id(int reward_id){
		_reward_id = reward_id;
		return this;
	}
	
	public int get_reward_asset_id(){
		return _reward_asset_id;
	}
	
	public AbstractReward set_reward_asset_id(int reward_asset_id){
		_reward_asset_id = reward_asset_id;
		return this;
	}
	
	public int get_reward_amount(){
		return _reward_amount;
	}
	
	public AbstractReward set_reward_amount(int reward_amount){
		_reward_amount = reward_amount;
		return this;
	}
	
	public abstract void do_reward(L1PcInstance pc);
}
