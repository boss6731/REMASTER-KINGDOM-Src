package l1j.server.Beginner.Model;

import l1j.server.Beginner.Model.MJBeginnerQuestData.MJBeginnerRewardListData.MJBeginnerReward;
import l1j.server.Beginner.Model.MJBeginnerQuestData.MJBeginnerRewardTypeData;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

class MJBeginnerRewardProvider {
	private static final MJBeginnerRewardProvider provider = new MJBeginnerRewardProvider();
	static MJBeginnerRewardProvider provider(){
		return provider;
	}
	
	private MJBeginnerRewardProvider(){	
	}
	
	static abstract class MJBeginnerRewardInvoker{
		abstract void invoke(L1PcInstance pc, MJBeginnerReward reward);
	}
	
	MJBeginnerRewardInvoker invoker(MJBeginnerRewardTypeData rewardTypeData){
		switch(rewardTypeData){
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
		return emptyInvoker;
	}

	private static final MJBeginnerRewardInvoker itemInvoker = new MJBeginnerRewardInvoker(){
		//@override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			L1Item item = ItemTable.getInstance().findDescCachedItemsAtOnce(reward.assetId());
			if(item == null){
				System.out.println(String.format("無法找到獎勵物品。描述:%d", reward.assetId()));
				return;
			}
			pc.getInventory().storeItem(item.getItemId(), (int)reward.amount(), true);
		}
	};
	
	private static final MJBeginnerRewardInvoker expInvoker = new MJBeginnerRewardInvoker(){
		@Override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
			pc.add_exp((long) (reward.amount() * exppenalty));
			pc.save();
		}
	};
	
	private static final MJBeginnerRewardInvoker spellBuffInvoker = new MJBeginnerRewardInvoker(){
		@Override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			System.out.println("unimplemented reward invoker(SPELL_BUFF)");
		}
	};

	private static final MJBeginnerRewardInvoker autoUseItemInvoker = new MJBeginnerRewardInvoker(){
		//@override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			L1Item item = ItemTable.getInstance().findDescCachedItemsAtOnce(reward.assetId());
			if(item == null){
				System.out.println(String.format("無法找到自動使用的獎勵物品。描述:%d", reward.assetId()));
				return;
			}
			pc.getInventory().storeItem(item.getItemId(), (int)reward.amount(), true);
		}
	};

	private static final MJBeginnerRewardInvoker buffStatueInvoker = new MJBeginnerRewardInvoker(){
		//@override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			System.out.println("未實作的獎勵調用器 (BUFF_STATUE)");
		}
	};

	private static final MJBeginnerRewardInvoker emptyInvoker = new MJBeginnerRewardInvoker(){
		//@override
		void invoke(L1PcInstance pc, MJBeginnerReward reward) {
			System.out.println(String.format("空的獎勵調用器 (%d)", reward.eType()));
		}
	};
}
