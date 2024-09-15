package l1j.server.server.model;

import java.util.List;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1DeleteItemOnGround {
	private DeleteTimer _deleteTimer;

	public static final int EXECUTE_STATUS_NONE = 0;
	public static final int EXECUTE_STATUS_READY = 2;

	private int _executeStatus = EXECUTE_STATUS_NONE;

	private static final int INTERVAL = Config.ServerAdSetting.ITEMDELETIONTIME * 60 * 1000 - 10 * 1000;

	private static final Logger _log = Logger.getLogger(L1DeleteItemOnGround.class.getName());

	public L1DeleteItemOnGround() {
	}

	private class DeleteTimer implements Runnable {
		public DeleteTimer() {
		}

		@Override
		public void run() {
			switch (_executeStatus) {
				case EXECUTE_STATUS_NONE: {
					L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(Config.Message.WorldDeleteCleaning));
					_executeStatus = EXECUTE_STATUS_READY;
					GeneralThreadPool.getInstance().schedule(this, 10000);
				}
				break;

				case EXECUTE_STATUS_READY: {
					deleteItem();
					// L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(166, "世界地圖上的物品", "已被刪除"));
					_executeStatus = EXECUTE_STATUS_NONE;
					GeneralThreadPool.getInstance().schedule(this, INTERVAL);
				}
				break;
			}
		}
	}

	public void initialize() {
		if (!Config.ServerAdSetting.ITEMDELETIONTYPE.equalsIgnoreCase("auto")) {
			return;
		}

		_deleteTimer = new DeleteTimer();
		GeneralThreadPool.getInstance().schedule(_deleteTimer, INTERVAL); // 타이머 개시
	}

	private void deleteItem() {
		int numOfDeleted = 0;
		L1ItemInstance item = null;
		List<L1PcInstance> players = null;
		L1Inventory groundInventory = null;
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (!(obj instanceof L1ItemInstance)) {
				continue;
			}

			item = (L1ItemInstance) obj;
			if (item.getX() == 0 && item.getY() == 0) { // 不是在地面上的物品，而是某人的所有物
				continue;
			}
			if (item.getItem().getItemId() == 40515) { // 精靈的石頭
				continue;
			}
			if (L1HouseLocation.isInHouse(item.getX(), item.getY(), item.getMapId())) { // 在據點內
				continue;
			}

			players = L1World.getInstance().getVisiblePlayer(item, Config.ServerAdSetting.ITEMDELETIONRANGE);
			if (players.isEmpty()) { // 指定範圍內沒有玩家則刪除
				groundInventory = L1World.getInstance().getInventory(item.getX(), item.getY(), item.getMapId());
				groundInventory.removeItem(item);
				numOfDeleted++;
			}
		}
		_log.fine("自動刪除世界地圖上的物品。刪除數量: " + numOfDeleted);
	}
}
