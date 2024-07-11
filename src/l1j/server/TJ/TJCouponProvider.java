package l1j.server.TJ;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_WAREHOUSE_ITEM_LIST_NOTI;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.server.clientpackets.C_Result;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.serverpackets.S_Disconnect;

import java.util.List;

public class TJCouponProvider {
	// TJ 優惠券的物品 ID
	private static final int TJ_COUPON_ID = 4100476;
	
	// 單例模式實例
	private static final TJCouponProvider provider = new TJCouponProvider();
	
	// 返回實例
	public static TJCouponProvider provider() {
		return provider;
	}
	
	// 私有構造函式，單例模式
	private TJCouponProvider() {
		
	}
	
	// 使用 TJ 優惠券
	public boolean onUseCoupon(L1PcInstance pc, L1ItemInstance item) {
		// 檢查物品 ID 是否為 TJ 優惠券
		if(item.getItemId() != TJ_COUPON_ID) {
			return false;
		}
		
		// 查詢角色遺失的物品記錄
		List<TJLostItemModel> models = TJLostItemDatabaseProvider.provider().selectCharacterLostItems(pc.getId());
		// 若未開啟物品遺失日誌功能，則返回
		if(!Config.ServerEnchant.enchantLostLog) {
			pc.sendPackets("目前系統未啟動。");
			return false;
		}
		
		// 若無遺失物品，則返回
		if (models.isEmpty()) {
			pc.sendPackets("沒有需要復原的物品。");
			return false;
		}
		
		// 發送物品列表給客戶端
		SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.createTJCoupon(models);
		noti.set_checker(pc.getId());
		pc.sendPackets(noti, MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI);
		return true;
	}
	
	// 選擇物品
	public void onChoiceItem(L1PcInstance pc, List<C_Result.ItemCountInfo> items) {
		if (pc.isTwoLogin(pc)) {
			return;
		}
		
		for(C_Result.ItemCountInfo iInfo : items) {
			if (!pc.getInventory().checkItem(TJ_COUPON_ID)) {
				pc.sendPackets("你沒有 TJ 優惠券。");
				return;
			}

			TJLostItemModel model = TJLostItemDatabaseProvider.provider().selectCharacterLostItem(pc.getId(), iInfo.itemObjectId);
			if(model == null) {
				continue;
			}
			if(model.recoveryCount != 0) {
				continue;
			}
			L1ItemInstance item = model.itemInstance();
			if(item == null) {
				continue;
			}
			if (!item.isStackable() && iInfo.itemCount != 1) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			if (iInfo.itemCount <= 0 || item.getCount() <= 0) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			if (pc.getInventory().consumeItem(TJ_COUPON_ID, 1)) {
				++model.recoveryCount;
				item.set_Carving(1);
				TJLostItemDatabaseProvider.provider().updateRecovery(model);
				SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
				SC_GOODS_INVEN_NOTI.do_send(pc);//啟用附加物品倉庫通知封包
				pwh.storeTradeItem(item);
				if(item.get_bless_level() > 0) {
					item.setBless(0);
					pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
					pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
					
					pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS_LEVEL);
					pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS_LEVEL);
				}
				pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("\\f3[復原通知] 附加物品倉庫中有新物品到達。", MJSimpleRgb.green(), 5));
				pc.sendPackets(String.format("物品復原: %s 的 (%s) \\fH已復原。", pc.getName(), item.getLogName()));	
			}
		}
	}
	
	// 記錄遺失物品
	public void onLostItem(L1PcInstance pc, L1ItemInstance item) {
		// 若物品不可交易，則返回
		if(!item.getItem().isTradable()) {
			if(item.getItem().getWareHouseLimitType().toInt() == 0) {
				return;
			}
		}
		// 若物品已被記錄過，則返回
		if(item.get_Carving() == 1) {
			return;
		}
		
		int type2 = item.getItem().getType2();
		if(type2 == 1) {
			onLostWeapon(pc, item);
		}else if(type2 == 2) {
			int type = item.getItem().getType();
			switch(type) {
			case 8: // 項鍊
			case 9: // 戒指
			case 10: // 腰帶
			case 11: // 戒指2
			case 12: // 耳環
			case 14: // 守護石
			case 28: // 守護石2
				onLostAccessory(pc, item);
				break;
			default:
				onLostArmor(pc, item);
				break;
			}
		}
	}
	
	// 記錄遺失武器
	private void onLostWeapon(L1PcInstance pc, L1ItemInstance item) {
		// 若未開啟物品遺失日誌功能，則返回
		if(!Config.ServerEnchant.enchantLostLog) {
			return;
		}
		
		int currentEnchant = item.getEnchantLevel();
		int safeEnchant = item.getItem().get_safeenchant();
		// 若安全強化等級為 6，且當前強化等級小於 7，則返回
		if(safeEnchant == 6 && currentEnchant < 7) {
			return;
		}
		
		TJLostItemDatabaseProvider.provider().newLostItem(pc, item);
	}
	
	// 記錄遺失防具
	private void onLostArmor(L1PcInstance pc, L1ItemInstance item) {
		// 若未開啟物品遺失日誌功能，則返回
		if(!Config.ServerEnchant.enchantLostLog) {
			return;
		}
		
		int currentEnchant = item.getEnchantLevel();
		int safeEnchant = item.getItem().get_safeenchant();
		// 若安全強化等級為 4，且當前強化等級小於 5，則返回
		if(safeEnchant == 4) {
			if(currentEnchant < 5) {
				return;
			}
		}else if(safeEnchant == 6) {
			// 若安全強化等級為 6，且當前強化等級小於 7，則返回
			if(currentEnchant < 7) {
				return;
			}
		}
		TJLostItemDatabaseProvider.provider().newLostItem(pc, item);
	}
	
	// 記錄遺失飾品
	private void onLostAccessory(L1PcInstance pc, L1ItemInstance item) {
		// 若未開啟物品遺失日誌功能，則返回
		if(!Config.ServerEnchant.enchantLostLog) {
			return;
		}
		
		if(item.getEnchantLevel() < 5) {
			return;
		}

		TJLostItemDatabaseProvider.provider().newLostItem(pc, item);
	}
}
