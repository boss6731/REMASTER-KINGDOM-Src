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
	static class MJBeginnerGerengEventHandler {
		private boolean active;
		private final L1PcInstance pc;

		MJBeginnerGerengEventHandler(L1PcInstance pc) {
			this.active = true;
			this.pc = pc;
			new GerengFinishedHandler(pc);
			new GerengPocketClickHandler(pc);
			new GerengMonsterKillHandler(pc);
			new GerengTalkHandler(pc);
		}

		MJBeginnerGerengEventData currentDataModel() {
			return MJBeginnerModelProvider.provider().subEventModel().subEventDataModel().gereng();
		}

		private L1PcInstance pc() {
			return pc;
		}

		private boolean active(MJObjectEventArgs args) {
			if (!active) {
				args.remove();
				return false;
			}
			return true;
		}

		private class GerengFinishedHandler extends MJObjectEventListener<MJPcEventFactory.MJPcQuestFinishedArgs> {
			private GerengFinishedHandler(L1PcInstance pc) {
				pc.eventHandler().addListener(MJObjectEventProvider.provider().pcEventFactory().pcQuestFinishedKey(),
						this);
			}

			@Override
			public void onEvent(MJPcQuestFinishedArgs args) {
				if (args.questId != currentDataModel().questId()) {
					return;
				}

				MJBeginnerGerengEventHandler.this.active = false;
				MJBeginnerGerengEventData data = currentDataModel();
				args.pc.getInventory().consumeItem(data.crystalBallId());
				args.pc.getInventory().consumeItem(data.soulFragmentId());
				args.pc.getInventory().consumeItem(data.pocketId());
				args.remove();
			}
		}

		private class GerengPocketClickHandler
				extends MJObjectEventListener<MJInventoryEventFactory.MJInventoryItemClickedArgs> {
			private GerengPocketClickHandler(L1PcInstance pc) {
				pc.eventHandler().addListener(
						MJObjectEventProvider.provider().inventoryEventFactory().inventoryItemClickedKey(), this);
			}

			@Override
			public void onEvent(MJInventoryItemClickedArgs args) {
				if (!active(args)) {
					return;
				}
				MJBeginnerGerengEventData data = currentDataModel();
				if (data.pocketId() != args.item.getItemId()) {
					return;
				}
				L1PcInstance pc = pc();
				if (pc.getInventory().checkItemCount(data.crystalBallId()) >= data.crystalBallLimit()) {
					pc.sendPackets(String.format("只能擁有%d個蓋靈的水晶球", data.crystalBallLimit()));
					return;
				}

				long currentMillis = System.currentTimeMillis();
				if (args.item.getLastUsedMillis() > 0
						&& currentMillis - args.item.getLastUsedMillis() < data.pocketClickDelayMillis()) {
					long afterMillis = data.pocketClickDelayMillis() - (currentMillis - args.item.getLastUsedMillis());
					pc.sendPackets(String.format("%d 分鐘後可以使用", (int) (afterMillis / 60000)));
					return;
				}
				args.item.setLastUsed(new Timestamp(currentMillis));
				pc.getInventory().updateItem(args.item, L1PcInventory.COL_DELAY_EFFECT);
				pc.getInventory().saveItem(args.item, L1PcInventory.COL_DELAY_EFFECT);
				L1ItemInstance item = pc.getInventory().storeItem(data.crystalBallId(), 1);
				pc.sendPackets(new S_ServerMessage(143, "道具", String.format("%s (%,d)", item.getName(), 1)));
			}
		}

		private class GerengMonsterKillHandler
				extends MJObjectEventListener<MJMonsterEventFactory.MJMonsterKillEventArgs> {
			private GerengMonsterKillHandler(L1PcInstance pc) {
				pc.eventHandler().addListener(MJObjectEventProvider.provider().monsterEventFactory().monsterKillKey(),
						this);
			}

			@Override
			public void onEvent(MJMonsterKillEventArgs args) {
				if (!active(args)) {
					return;
				}
				MJBeginnerGerengEventData data = currentDataModel();
				MJCrystalBallProviderData providerData = data.crystalBallProviders(args.monster.getNpcId());
				if (providerData == null) {
					return;
				}
				L1PcInstance pc = pc();
				if (!pc.getInventory().checkItem(data.crystalBallId())) {
					return;
				}
				if (!providerData.selectProbability()) {
					return;
				}
				int added = providerData.collectItemsCount();
				if (added <= 0) {
					return;
				}
				L1ItemInstance item = pc.getInventory().storeItem(data.soulFragmentId(), added);
				pc.sendPackets(new S_ServerMessage(143, args.monster.getName(),
						String.format("%s (%,d)", item.getName(), added)));
			}
		}

		private class GerengTalkHandler extends MJObjectEventListener<MJNpcEventFactory.MJNpcTalkEventArgs> {
			private GerengTalkHandler(L1PcInstance pc) {
				pc.eventHandler().addListener(MJObjectEventProvider.provider().npcEventFactory().npcTalkKey(), this);
			}

			@Override
			public void onEvent(MJNpcTalkEventArgs args) {
				if (!active(args)) {
					return;
				}
				MJBeginnerGerengEventData data = currentDataModel();
				if (args.npc.getNpcId() != data.gerengId()) {
					return;
				}

				if (!args.action.equalsIgnoreCase("a")) {
					return;
				}

				L1PcInstance pc = pc();
				L1ItemInstance soulItem = pc.getInventory().findItemId(data.soulFragmentId());
				L1ItemInstance crystalItem = pc.getInventory().findItemId(data.crystalBallId());
				if (soulItem == null || crystalItem == null || soulItem.getCount() < data.soulFragmentAmount()
						|| crystalItem.getCount() < 1) {
					pc.sendPackets(new S_NPCTalkReturn(args.npc.getId(), "gereng02"));
					return;
				}

				pc.getInventory().removeItem(soulItem);
				pc.getInventory().consumeItem(crystalItem, 1);
				int level = pc.getLevel();
				long exp = ExpTable.getNeedExpNextLevel(52);
				for (MJBeginnerSubEventExpReward reward : data.rewards()) {
					if (level <= reward.highLevel()) {
						exp = reward.usePenalty() ? (long) (exp * reward.percent() * ExpTable.getPenaltyRate(level))
								: (long) (exp * reward.percent());
						pc.add_exp(exp);
						pc.send_effect(3944);
					}
				}
			}
		}
	}
}
