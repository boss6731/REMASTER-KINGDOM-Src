package l1j.server.MJTemplate.Reward;

import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1PcInstance;

public class ExpReward extends AbstractReward {

	// 創建新的 ExpReward 實例的方法
	public static ExpReward newInstance() {
		return new ExpReward();
	}

	// 私有構造函數，防止外部創建實例
	private ExpReward() {
	}

	// 重寫 do_reward 方法，執行經驗值獎勵
	@override
	public void do_reward(L1PcInstance pc) {
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel()); // 獲取經驗值懲罰率
		long real_exp = (long) (get_reward_amount() * exppenalty); // 計算實際經驗值
		double need_exp = ExpTable.getNeedExpNextLevel(pc.getLevel()); // 獲取升級所需經驗值
		pc.sendPackets(String.format("獲得了 %.6f%% 的經驗值獎勵。", ((double)real_exp / need_exp) * 100D)); // 發送經驗值獎勵信息
		pc.add_exp(real_exp); // 添加經驗值
		pc.save(); // 保存角色狀態
	}
}