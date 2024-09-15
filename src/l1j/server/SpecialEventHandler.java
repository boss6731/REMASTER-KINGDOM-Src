package l1j.server;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_GMHtml;
import l1j.server.server.serverpackets.S_SystemMessage;

enum SpecialEvent {
	BugRace, AllBuf, InfinityFight, DoNotChatEveryone, DoChatEveryone
};

// 負責處理遊戲內所有事件
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

	//TODO 科馬的祝福硬幣
	public void doGiveEventStaff() {
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getNetConnection() != null) {
				pc.getInventory().storeItem(30104, 1);
				L1ItemInstance item = ItemTable.getInstance().createItem(30104);
				pc.sendPackets(new S_GMHtml("發件人:" + pc.getName() + "", "物品:"+item.getLogName()+" 已經到達。"));
				pc.sendPackets("\\aH梅蒂斯給予了您科馬的祝福硬幣。");
			}
		}
	}

	//TODO 龍的黃玉
	public void doGiveEventStaff1() {
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getNetConnection() != null) {
				pc.getInventory().storeItem(7241, 1);
				L1ItemInstance item = ItemTable.getInstance().createItem(7241);
				pc.sendPackets(new S_GMHtml("發件人:" + pc.getName() + "", "物品:"+item.getLogName()+" 已經到達。"));
				pc.sendPackets("\\aH梅蒂斯給予了您龍的黃玉。");
			}
		}
	}

	//TODO BOSS召喚卷軸
	public void doGiveEventStaff2() {
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getNetConnection() != null) {
				pc.getInventory().storeItem(3000123, 1);
				L1ItemInstance item = ItemTable.getInstance().createItem(3000123);
				pc.sendPackets(new S_GMHtml("發件人:"+pc.getName()+"♥", "♥"+item.getLogName()+" 1張已經到達♥　(只能在野外使用)　(與血盟成員一起突襲)"));
				pc.sendPackets("\\aG梅蒂斯給予了您1張召喚卷軸。");
			}
		}
	}

	public void doNotChatEveryone() {
		L1World.getInstance().set_worldChatElabled(false);
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("即將進入星際對話停用狀態。"));
	}

	public void doChatEveryone() {
		L1World.getInstance().set_worldChatElabled(true);
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("即將進入星際對話啟用狀態。"));
	}

}
