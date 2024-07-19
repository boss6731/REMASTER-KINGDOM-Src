package l1j.server.Beginner.Model;

import java.sql.Timestamp;

import l1j.server.Beginner.Model.MJBeginnerSubEventData.MJBeginnerGerengEventData;
import l1j.server.Beginner.Model.MJBeginnerSubEventData.MJBeginnerGerengEventData.MJCrystalBallProviderData;
import l1j.server.Beginner.Model.MJBeginnerSubEventData.MJBeginnerSubEventExpReward;
import l1j.server.MJTemplate.ObjectEvent.MJInventoryEventFactory;
import l1j.server.MJTemplate.ObjectEvent.MJInventoryEventFactory.MJInventoryItemClickedArgs;
import l1j.server.MJTemplate.ObjectEvent.MJMonsterEventFactory;
import l1j.server.MJTemplate.ObjectEvent.MJMonsterEventFactory.MJMonsterKillEventArgs;
import l1j.server.MJTemplate.ObjectEvent.MJNpcEventFactory;
import l1j.server.MJTemplate.ObjectEvent.MJNpcEventFactory.MJNpcTalkEventArgs;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventArgs;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventListener;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJPcQuestFinishedArgs;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_ServerMessage;

public class MJBeginnerSubEventHandlers {

	// 定義一個靜態內部類，表示 Gereng 事件處理器
	static class MJBeginnerGerengEventHandler {
		private boolean active;
		private final L1PcInstance pc;

		// 構造函數，接受一個玩家實例作為參數
		MJBeginnerGerengEventHandler(L1PcInstance pc) {
			this.active = true;
			this.pc = pc;

		// 創建各種特定的事件處理器
			new GerengFinishedHandler(pc);
			new GerengPocketClickHandler(pc);
			new GerengMonsterKillHandler(pc);
			new GerengTalkHandler(pc);
		}

		// 返回當前的 Gereng 事件數據模型
		MJBeginnerGerengEventData currentDataModel() {
			return MJBeginnerModelProvider.provider().subEventModel().subEventDataModel().gereng();
		}

		// 返回玩家實例
		private L1PcInstance pc() {
			return pc;
		}

		// 檢查事件是否活動，如果不活動則移除事件
		private boolean active(MJObjectEventArgs args) {
			if (!active) {
				args.remove();
				return false;
			}
			return true;
		}

		// 其他相關的事件處理邏輯可以放在這裡
	}
}

// 定義一個私有內部類，繼承自 MJObjectEventListener，處理 Gereng 任務完成的事件
private class GerengFinishedHandler extends MJObjectEventListener<MJPcEventFactory.MJPcQuestFinishedArgs> {

	// 構造函數，接受一個玩家實例作為參數
	private GerengFinishedHandler(L1PcInstance pc) {
		// 添加事件監聽器，監聽玩家任務完成的事件
		pc.eventHandler().addListener(MJObjectEventProvider.provider().pcEventFactory().pcQuestFinishedKey(), this);
	}

	// 重寫 onEvent 方法，處理任務完成的事件
	@override
	public void onEvent(MJPcEventFactory.MJPcQuestFinishedArgs args) {
		// 如果完成的任務ID不是當前的 Gereng 任務ID，則直接返回
		if (args.questId != currentDataModel().questId()) {
			return;
		}

		// 任務完成，將事件處理器設置為非活動狀態
		MJBeginnerGerengEventHandler.this.active = false;

		// 獲取當前的 Gereng 事件數據模型
		MJBeginnerGerengEventData data = currentDataModel();

		// 消耗玩家背包中的相關物品
		args.pc.getInventory().consumeItem(data.crystalBallId());
		args.pc.getInventory().consumeItem(data.soulFragmentId());
		args.pc.getInventory().consumeItem(data.pocketId());

		// 移除事件
		args.remove();
	}
}

// 定義一個私有內部類，繼承自 MJObjectEventListener，處理口袋點擊事件
private class GerengPocketClickHandler extends MJObjectEventListener<MJInventoryEventFactory.MJInventoryItemClickedArgs> {

	// 構造函數，接受一個玩家實例作為參數
	private GerengPocketClickHandler(L1PcInstance pc) {
		// 添加事件監聽器，監聽玩家口袋點擊事件
		pc.eventHandler().addListener(MJObjectEventProvider.provider().inventoryEventFactory().inventoryItemClickedKey(), this);
	}

	// 重寫 onEvent 方法，處理口袋點擊事件
	@override
	public void onEvent(MJInventoryEventFactory.MJInventoryItemClickedArgs args) {
		// 檢查事件是否活動，如果不活動則返回
		if (!active(args)) {
			return;
		}

		// 獲取當前的 Gereng 事件數據模型
		MJBeginnerGerengEventData data = currentDataModel();

		// 如果點擊的物品不是口袋，則返回
		if (data.pocketId() != args.item.getItemId()) {
			return;
		}

		// 獲取玩家實例
		L1PcInstance pc = pc();

		// 檢查玩家背包中的水晶球數量是否達到限制
		if (pc.getInventory().checkItemCount(data.crystalBallId()) >= data.crystalBallLimit()) {
			pc.sendPackets(String.format("你最多只能擁有 %d 顆水晶球。", data.crystalBallLimit()));
			return;
		}

		// 獲取當前時間（毫秒）
		long currentMillis = System.currentTimeMillis();

		// 檢查口袋的最後使用時間，如果還在冷卻時間內，則返回提示信息
		if (args.item.getLastUsedMillis() > 0 && currentMillis - args.item.getLastUsedMillis() < data.pocketClickDelayMillis()) {
			long afterMillis = data.pocketClickDelayMillis() - (currentMillis - args.item.getLastUsedMillis());
			pc.sendPackets(String.format("你需要等待 %d 分鐘才能使用。", (int)(afterMillis / 60000)));
			return;
		}

		// 設置口袋的最後使用時間為當前時間
		args.item.setLastUsed(new Timestamp(currentMillis));

		// 更新和保存物品的延遲效果
		pc.getInventory().updateItem(args.item, L1PcInventory.COL_DELAY_EFFECT);
		pc.getInventory().saveItem(args.item, L1PcInventory.COL_DELAY_EFFECT);

		// 在玩家背包中存儲一個水晶球
		L1ItemInstance item = pc.getInventory().storeItem(data.crystalBallId(), 1);

		// 發送獲取物品的消息給玩家
		pc.sendPackets(new S_ServerMessage(143, "物品", String.format("%s (%,d)", item.getName(), 1)));
	}
}

// 定義一個私有內部類，繼承自 MJObjectEventListener，處理擊殺怪物事件
private class GerengMonsterKillHandler extends MJObjectEventListener<MJMonsterEventFactory.MJMonsterKillEventArgs> {

	// 構造函數，接受一個玩家實例作為參數
	private GerengMonsterKillHandler(L1PcInstance pc) {
		// 添加事件監聽器，監聽玩家擊殺怪物事件
		pc.eventHandler().addListener(MJObjectEventProvider.provider().monsterEventFactory().monsterKillKey(), this);
	}

	// 重寫 onEvent 方法，處理擊殺怪物事件
	@override
	public void onEvent(MJMonsterEventFactory.MJMonsterKillEventArgs args) {
		// 檢查事件是否活動，如果不活動則返回
		if (!active(args)) {
			return;
		}

		// 獲取當前的 Gereng 事件數據模型
		MJBeginnerGerengEventData data = currentDataModel();

		// 獲取擊殺怪物的 NPC ID 對應的水晶球提供者數據
		MJCrystalBallProviderData providerData = data.crystalBallProviders(args.monster.getNpcId());
		if (providerData == null) {
			return;
		}

		// 獲取玩家實例
		L1PcInstance pc = pc();

		// 檢查玩家背包中是否有水晶球
		if (!pc.getInventory().checkItem(data.crystalBallId())) {
			return;
		}

		// 根據概率判斷是否選中
		if (!providerData.selectProbability()) {
			return;
		}

		// 獲取收集的物品數量
		int added = providerData.collectItemsCount();
		if (added <= 0) {
			return;
		}

		// 在玩家背包中存儲靈魂碎片
		L1ItemInstance item = pc.getInventory().storeItem(data.soulFragmentId(), added);

		// 發送獲取物品的消息給玩家
		pc.sendPackets(new S_ServerMessage(143, args.monster.getName(), String.format("%s (%,d)", item.getName(), added)));
	}
}

// 定義一個私有內部類，繼承自 MJObjectEventListener，處理 NPC 對話事件
private class GerengTalkHandler extends MJObjectEventListener<MJNpcEventFactory.MJNpcTalkEventArgs> {

	// 構造函數，接受一個玩家實例作為參數
	private GerengTalkHandler(L1PcInstance pc) {
	// 添加事件監聽器，監聽 NPC 對話事件
		pc.eventHandler().addListener(MJObjectEventProvider.provider().npcEventFactory().npcTalkKey(), this);
	}

	// 重寫 onEvent 方法，處理 NPC 對話事件
	@override
	public void onEvent(MJNpcEventFactory.MJNpcTalkEventArgs args) {
		// 檢查事件是否活動，如果不活動則返回
		if (!active(args)) {
			return;
		}

		// 獲取當前的 Gereng 事件數據模型
		MJBeginnerGerengEventData data = currentDataModel();

		// 檢查 NPC ID 是否匹配
		if (args.npc.getNpcId() != data.gerengId()) {
			return;
		}

		// 檢查玩家的動作是否為 "a"
		if (!args.action.equalsIgnoreCase("a")) {
			return;
		}

		// 獲取玩家實例
		L1PcInstance pc = pc();

		// 檢查玩家背包中是否有足夠的靈魂碎片和水晶球
		L1ItemInstance soulItem = pc.getInventory().findItemId(data.soulFragmentId());
		L1ItemInstance crystalItem = pc.getInventory().findItemId(data.crystalBallId());
		if (soulItem == null || crystalItem == null || soulItem.getCount() < data.soulFragmentAmount() || crystalItem.getCount() < 1) {
			pc.sendPackets(new S_NPCTalkReturn(args.npc.getId(), "gereng02"));
			return;
		}

		// 移除玩家背包中的靈魂碎片和消耗一個水晶球
		pc.getInventory().removeItem(soulItem);
		pc.getInventory().consumeItem(crystalItem, 1);

		// 獲取玩家當前等級和經驗值
		int level = pc.getLevel();
		long exp = ExpTable.getNeedExpNextLevel(52);

		// 根據玩家的等級和獎勵設置給予經驗值
		for (MJBeginnerSubEventExpReward reward : data.rewards()) {
			if (level <= reward.highLevel()) {
				exp = reward.usePenalty() ? (long) (exp * reward.percent() * ExpTable.getPenaltyRate(level)) : (long) (exp * reward.percent());
				pc.add_exp(exp);
				pc.send_effect(3944);
			}
		}
	}
}
