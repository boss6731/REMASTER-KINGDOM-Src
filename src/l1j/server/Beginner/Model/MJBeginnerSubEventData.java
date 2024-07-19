package l1j.server.Beginner.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerSubEventData {
	private MJBeginnerGerengEventData gereng;

	// 返回子事件模型的映射
	HashMap<Integer, MJBeginnerSubEventListener> subEventModels() {
		// 初始化一個大小為2的 HashMap
		HashMap<Integer, MJBeginnerSubEventListener> subEventModels = new HashMap<>(2);

		// 如果有 Gereng 事件數據
		if (hasGereng()) {
			// 將 Gereng 事件數據的獎勵進行排序
			Collections.sort(gereng.rewards);

			// 將 Gereng 事件數據添加到子事件模型的映射中
			subEventModels.put(gereng.questId(), gereng);
		}

		// 返回子事件模型的映射
		return subEventModels;
	}

	// 檢查是否有 Gereng 事件數據
	boolean hasGereng() {
		return gereng != null;
	}

	// 返回 Gereng 事件數據
	MJBeginnerGerengEventData gereng() {
		return gereng;
	}

	// 靜態內部類，表示子事件經驗獎勵，實現 Comparable 接口
	static class MJBeginnerSubEventExpReward implements Comparable<MJBeginnerSubEventExpReward> {
		private int highLevel;
		private double percent;
		private boolean usePenalty;

		// 返回高級別
		int highLevel() {
			return highLevel;
		}

		// 返回百分比
		double percent() {
			return percent;
		}

		// 返回是否使用懲罰
		boolean usePenalty() {
			return usePenalty;
		}

		// 比較兩個子事件經驗獎勵對象，根據高級別
		@override
		public int compareTo(MJBeginnerSubEventExpReward o) {
			return Integer.compare(highLevel, o.highLevel);
		}
	}
}

// 定義一個靜態抽象類，表示初學者子事件監聽器
static abstract class MJBeginnerSubEventListener {
	// 抽象方法，返回任務ID
	abstract int questId();

	// 抽象方法，處理任務，接受玩家實例和任務ID
	abstract void onQuest(L1PcInstance pc, int questId);
}

// 定義一個靜態類，繼承自 MJBeginnerSubEventListener，表示 Gereng 事件數據
static class MJBeginnerGerengEventData extends MJBeginnerSubEventListener {
	// 私有成員變量
	private int questId;
	private int gerengId;
	private int crystalBallId;
	private int crystalBallLimit;
	private int pocketId;
	private long pocketClickDelayMillis;
	private int soulFragmentId;
	private int soulFragmentAmount;
	private ArrayList<MJBeginnerSubEventExpReward> rewards;
	private HashMap<Integer, MJCrystalBallProviderData> crystalBallProviders;

	// 實現 questId 方法，返回任務ID
	@override
	int questId() {
		return questId;
	}

	// 實現 onQuest 方法，處理任務
	@override
	void onQuest(L1PcInstance pc, int questId) {
		// 實現任務處理邏輯
		// 例如：檢查任務是否符合要求，發放獎勵等
	}

	// 其他相關的 getter 方法和處理邏輯


	// 靜態類，繼承自 MJBeginnerSubEventListener，表示 Gereng 事件數據
	static class MJBeginnerGerengEventData extends MJBeginnerSubEventListener {
		private int questId;
		private int gerengId;
		private int crystalBallId;
		private int crystalBallLimit;
		private int pocketId;
		private long pocketClickDelayMillis;
		private int soulFragmentId;
		private int soulFragmentAmount;
		private ArrayList<MJBeginnerSubEventExpReward> rewards;
		private HashMap<Integer, MJCrystalBallProviderData> crystalBallProviders;

		// 返回 Gereng ID
		int gerengId() {
			return gerengId;
		}

		// 返回任務ID
		@override
		int questId() {
			return questId;
		}

		// 返回水晶球ID
		int crystalBallId() {
			return crystalBallId;
		}

		// 返回水晶球的限制數量
		int crystalBallLimit() {
			return crystalBallLimit;
		}

		// 返回口袋ID
		int pocketId() {
			return pocketId;
		}

		// 返回口袋點擊延遲時間（毫秒）
		long pocketClickDelayMillis() {
			return pocketClickDelayMillis;
		}

		// 返回靈魂碎片ID
		int soulFragmentId() {
			return soulFragmentId;
		}

		// 返回靈魂碎片數量
		int soulFragmentAmount() {
			return soulFragmentAmount;
		}

		// 返回經驗獎勵的列表
		ArrayList<MJBeginnerSubEventExpReward> rewards() {
			return rewards;
		}

		// 返回特定怪物NPC ID的水晶球提供者數據
		MJCrystalBallProviderData crystalBallProviders(int monsterNpcId) {
			return crystalBallProviders.get(monsterNpcId);
		}

		// 處理任務
		@override
		void onQuest(L1PcInstance pc, int questId) {
			new MJBeginnerSubEventHandlers.MJBeginnerGerengEventHandler(pc);
		}


		// 定義一個靜態類，表示水晶球提供者數據
		static class MJCrystalBallProviderData {
			private int suppliersNpc;
			private double probability;
			private int suppliesMinimum;
			private int suppliesMaximum;

			// 返回供應商 NPC ID
			int suppliersNpc() {
				return suppliersNpc;
			}

			// 返回概率值
			double probability() {
				return probability;
			}

			// 返回最小供應數量
			int suppliesMinimum() {
				return suppliesMinimum;
			}

			// 返回最大供應數量
			int suppliesMaximum() {
				return suppliesMaximum;
			}

			// 根據概率值選擇是否成功
			boolean selectProbability() {
				// 如果概率值大於等於 1，直接成功
				if (probability() >= 1D) {
					return true;
				}

				// 否則，根據概率值進行隨機判斷
				return MJRnd.isWinning(1000000, (int)(probability() * 1000000));
			}

			// 返回收集的物品數量
			int collectItemsCount() {
				// 如果最小和最大供應數量相等，直接返回最小數量
				if (suppliesMinimum() == suppliesMaximum()) {
					return suppliesMinimum();
				}

				// 否則，在最小和最大供應數量之間隨機選擇一個數量
				return MJRnd.next(suppliesMinimum(), suppliesMaximum());
			}
		}
