package l1j.server.server.server.monitor;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;

public interface Logger {
	public enum LoggerChatType {
		Normal, Global, Clan, Alliance, Guardian, Party, Group, Shouting
        /* Trade 修改為不記錄交易聊天日誌 */
	}

	public enum ItemActionType {
		Pickup, Drop, Delete, del, DeathDrop
        /* AutoLoot 修改為不記錄自動拾取日誌 */
	}

	public enum WarehouseType {
		Private, Clan, Package, Elf
	}

	public void addChat(LoggerChatType type, L1PcInstance pc, String msg);

	public void addWhisper(L1PcInstance pcfrom, L1PcInstance pcto, String msg);

	public void addCommand(String msg);

	public void addConnection(String msg);
	
	public void addError(String msg);

	public void addWarehouse(WarehouseType type, boolean put, L1PcInstance pc, L1ItemInstance item, int count);

	public void addTrade(boolean success, L1PcInstance pcfrom, L1PcInstance pcto, L1ItemInstance item, int count);

	/** 交易成功時記錄日誌 */
	public void addEnchant(L1PcInstance pc, L1ItemInstance item, boolean success);

	public void addAll(String msg);

	public void addItemAction(ItemActionType type, L1PcInstance pc, L1ItemInstance item, int count);

	/** 從78級開始升級時記錄levellog */
	public void addLevel(L1PcInstance pc, int level);

	public void flush() throws IOException;
}
