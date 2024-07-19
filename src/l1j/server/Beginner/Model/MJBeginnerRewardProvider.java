package l1j.server.Beginner.Model;

import l1j.server.Beginner.Model.MJBeginnerQuestData.MJBeginnerRewardListData.MJBeginnerReward;
import l1j.server.Beginner.Model.MJBeginnerQuestData.MJBeginnerRewardTypeData;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

class MJBeginnerRewardProvider {
	private static final MJBeginnerRewardProvider provider = new MJBeginnerRewardProvider(); // 單例模式實例
	static MJBeginnerRewardProvider provider() {
		return provider;
	}

	private MJBeginnerRewardProvider() {
	}

	// 抽象類別，用於獎勵的具體執行
	static abstract class MJBeginnerRewardInvoker {
		abstract void invoke(L1PcInstance pc, MJBeginnerReward reward);
	}

	// 根據獎勵類型數據返回相應的獎勵執行器
	MJBeginnerRewardInvoker invoker(MJBeginnerRewardTypeData rewardTypeData) {
		switch (rewardTypeData) {
			case ITEM:
				return itemInvoker;
			case EXP:
				return expInvoker;
			case SPELL_BUFF:
				return spellBuffInvoker;
			case AUTO_USE_ITEM:
				return autoUseItemInvoker;
			case BUFF_STATUE:
				return buffStatueInvoker;
		}
		return emptyInvoker; // 默認返回空執行器
	}

	// 獎勵類型的具體執行器實現（例如：物品獎勵）
	private static final MJBeginnerRewardInvoker itemInvoker = new MJBeginnerRewardInvoker() {
		@override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			L1Item item = ItemTable.getInstance().findDescCachedItemsAtOnce(reward.assetId());
			if (item == null) {
				System.out.println(String.format("無法找到獎勵物品。描述ID: %d", reward.assetId()));
				return;
			}
			pc.getInventory().storeItem(item.getItemId(), (int)reward.amount(), true);
		}
	};
	// ... 假設其他執行器和 emptyInvoker 實現已定義
}

class MJBeginnerRewardProvider {
	private static final MJBeginnerRewardProvider provider = new MJBeginnerRewardProvider(); // 單例模式實例
	static MJBeginnerRewardProvider provider() {
		return provider;
	}

	private MJBeginnerRewardProvider() {
	}

	// 抽象類別，用於獎勵的具體執行
	static abstract class MJBeginnerRewardInvoker {
		abstract void invoke(L1PcInstance pc, MJBeginnerReward reward);
	}

	// 根據獎勵類型數據返回相應的獎勵執行器
	MJBeginnerRewardInvoker invoker(MJBeginnerRewardTypeData rewardTypeData) {
		switch (rewardTypeData) {
			case ITEM:
				return itemInvoker;
			case EXP:
				return expInvoker;
			case SPELL_BUFF:
				return spellBuffInvoker;
			case AUTO_USE_ITEM:
				return autoUseItemInvoker;
			case BUFF_STATUE:
				return buffStatueInvoker;
		}
		return emptyInvoker; // 默認返回空執行器
	}

	// 獎勵類型的具體執行器實現（例如：物品獎勵）
	private static final MJBeginnerRewardInvoker itemInvoker = new MJBeginnerRewardInvoker() {
		@override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			L1Item item = ItemTable.getInstance().findDescCachedItemsAtOnce(reward.assetId());
			if (item == null) {
				System.out.println(String.format("無法找到獎勵物品。描述ID: %d", reward.assetId()));
				return;
			}
			pc.getInventory().storeItem(item.getItemId(), (int)reward.amount(), true);
		}
	};

	// 經驗值獎勵的具體執行器實現
	private static final MJBeginnerRewardInvoker expInvoker = new MJBeginnerRewardInvoker() {
		@override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
			pc.add_exp((long) (reward.amount() * exppenalty));
			pc.save();
		}
	};

	// 法術增益獎勵的具體執行器實現
	private static final MJBeginnerRewardInvoker spellBuffInvoker = new MJBeginnerRewardInvoker() {
		@override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			System.out.println("未實現的獎勵執行器(SPELL_BUFF)");
		}
	};

	// 自動使用物品獎勵的具體執行器實現
	private static final MJBeginnerRewardInvoker autoUseItemInvoker = new MJBeginnerRewardInvoker() {
		@override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			L1Item item = ItemTable.getInstance().findDescCachedItemsAtOnce(reward.assetId());
			if (item == null) {
				System.out.println(String.format("無法找到自動使用的獎勵物品。描述ID: %d", reward.assetId()));
				return;
			}
			pc.getInventory().storeItem(item.getItemId(), (int)reward.amount(), true);
		}
	};

	// 增益雕像獎勵的具體執行器實現
	private static final MJBeginnerRewardInvoker buffStatueInvoker = new MJBeginnerRewardInvoker() {
		@override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			System.out.println("未實現的獎勵執行器(BUFF_STATUE)");
		}
	};

	// 空獎勵執行器
	private static final MJBeginnerRewardInvoker emptyInvoker = new MJBeginnerRewardInvoker() {
		@override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			System.out.println(String.format("空的獎勵執行器(%d)", reward.eType()));
		}
	};
}
