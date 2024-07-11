package l1j.server;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_GMHtml;
import l1j.server.server.serverpackets.S_SystemMessage;

// 特殊事件的處理，涉及遊戲中所有事件
public class SpecialEventHandler {

	private static volatile SpecialEventHandler uniqueInstance = null;

	private SpecialEventHandler() {
	}

	public static SpecialEventHandler getInstance() {
		if (uniqueInstance == null) {
			synchronized (SpecialEventHandler.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new SpecialEventHandler();
				}
			}
		}

		return uniqueInstance;
	}

	//TODO 康瑪的祝福幣
	public void doGiveEventStaff() {
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getNetConnection() != null) {
				pc.getInventory().storeItem(30104, 1);
				L1ItemInstance item = ItemTable.getInstance().createItem(30104);
				pc.sendPackets(new S_GMHtml("贈送者：" + pc.getName() + "", "物品："+item.getLogName()+" 已經送到了你的背包。"));
				pc.sendPackets("\\aH管理員贈送了康瑪的祝福幣。");
			}
		}
	}

	//TODO 龍之石
	public void doGiveEventStaff1() {
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getNetConnection() != null) {
				pc.getInventory().storeItem(7241, 1);
				L1ItemInstance item = ItemTable.getInstance().createItem(7241);
				pc.sendPackets(new S_GMHtml("贈送者：" + pc.getName() + "", "物品："+item.getLogName()+" 已經送到了你的背包。"));
				pc.sendPackets("\\aH管理員贈送了龍之石。");
			}
		}
	}

	//TODO 召喚書
	public void doGiveEventStaff2() {
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getNetConnection() != null) {
				pc.getInventory().storeItem(3000123, 1);
				L1ItemInstance item = ItemTable.getInstance().createItem(3000123);
				pc.sendPackets(new S_GMHtml("贈送者："+pc.getName()+"♥", "♥"+item.getLogName()+" 1張已經送到了你的背包♥　　 　(只能在野外使用)　(與幫會成員一起參加團隊戰鬥吧)"));
				pc.sendPackets("\\aG管理員贈送了召喚書 1張。");
			}
		}
	}

	public void doNotChatEveryone() {
		L1World.getInstance().set_worldChatElabled(false);
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("一會兒後全服聊天將進入非活動狀態。"));
	}

	public void doChatEveryone() {
		L1World.getInstance().set_worldChatElabled(true);
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("一會兒後全服聊天將恢復活動狀態。"));
	}

}
