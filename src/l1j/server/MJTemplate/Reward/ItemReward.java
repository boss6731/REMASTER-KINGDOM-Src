package l1j.server.MJTemplate.Reward;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class ItemReward extends AbstractReward {

	// 創建新的 ItemReward 實例的方法
	public static ItemReward newInstance() {
		return new ItemReward();
	}

	// 私有構造函數，防止外部創建實例
	private ItemReward() {}

	// 重寫 do_reward 方法，執行物品獎勵
	@override
	public void do_reward(L1PcInstance pc) {
		int item_id = get_reward_asset_id(); // 獲取物品ID
		int item_amount = get_reward_amount(); // 獲取物品數量
		L1Item item = ItemTable.getInstance().getTemplate(item_id); // 根據物品ID獲取物品模板
		pc.sendPackets(String.format("獲得了 %d 個 %s 物品獎勵。", item_amount, item.getName())); // 發送物品獎勵信息
		if (!item.isStackable() && item_amount > 1) { // 如果物品不能堆疊且數量大於1
			for (int i = item_amount - 1; i >= 0; --i) // 將物品逐個添加到角色背包
				pc.getInventory().storeItem(item_id, 1);
		} else {
			pc.getInventory().storeItem(item_id, item_amount); // 如果物品能堆疊，直接添加數量
		}
	}
}