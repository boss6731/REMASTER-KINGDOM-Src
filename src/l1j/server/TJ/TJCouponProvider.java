package l1j.server.TJ;

import java.util.List;

import l1j.server.Config;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_WAREHOUSE_ITEM_LIST_NOTI;
import l1j.server.server.clientpackets.C_Result;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.serverpackets.S_Disconnect;

public class TJCouponProvider {
	private static final int TJ_COUPON_ID = 4100476;
	
	private static final TJCouponProvider provider = new TJCouponProvider();
	
	public static TJCouponProvider provider() {
		return provider;
	}
	
	private TJCouponProvider() {
		
	}
	
	public boolean onUseCoupon(L1PcInstance pc, L1ItemInstance item) {
		if(item.getItemId() != TJ_COUPON_ID) {
			return false;
		}
		
		List<TJLostItemModel> models = TJLostItemDatabaseProvider.provider().selectCharacterLostItems(pc.getId());
		if(!Config.ServerEnchant.enchantLostLog) {
			pc.sendPackets("目前系統未運行。");
			return false;
		}
		
		if (models.isEmpty()) {
			pc.sendPackets("不存在可恢復的物品。");
			return false;
		}
		
		SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.createTJCoupon(models);
		noti.set_checker(pc.getId());
		pc.sendPackets(noti, MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI);
		return true;
	}
	
	public void onChoiceItem(L1PcInstance pc, List<C_Result.ItemCountInfo> items) {
		if (pc.isTwoLogin(pc)) {
			return;
		}
		
		for(C_Result.ItemCountInfo iInfo : items) {
			if (!pc.getInventory().checkItem(TJ_COUPON_ID)) {
				pc.sendPackets("未持有TJ優惠券。");
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
				SC_GOODS_INVEN_NOTI.do_send(pc);// 副物品倉庫啟用數據包
				pwh.storeTradeItem(item);
				//pc.getInventory().storeItem(item, true);
				if(item.get_bless_level() > 0) {
					item.setBless(0);
					pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
					pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
					
					pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS_LEVEL);
					pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS_LEVEL);
				}
				pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("\f3[恢復通知] 副物品倉庫有新商品已到達。", MJSimpleRgb.green(), 5));
				pc.sendPackets(String.format("物品恢復: %s的 (%s) \fH已恢復。", pc.getName(), item.getLogName()));
			}
		}
	}
	
	public void onLostItem(L1PcInstance pc, L1ItemInstance item) {
		if(!item.getItem().isTradable()) {
			if(item.getItem().getWareHouseLimitType().toInt() == 0) {
				return;
			}
		}
		if(item.get_Carving() == 1) {
			return;
		}
		
		int type2 = item.getItem().getType2();
		if(type2 == 1) {
			onLostWeapon(pc, item);
		}else if(type2 == 2) {
			int type = item.getItem().getType();
			switch(type) {
			case 8: // amulet
			case 9: // ring
			case 10: // belt
			case 11: // ring2
			case 12: // earring
			case 14: // ron
			case 28: // ron2
				onLostAccessory(pc, item);
				break;
			default:
				onLostArmor(pc, item);
				break;
			}
		}
	}
	
	private void onLostWeapon(L1PcInstance pc, L1ItemInstance item) {
		if(!Config.ServerEnchant.enchantLostLog) {
			return;
		}
		
		int currentEnchant = item.getEnchantLevel();
		int safeEnchant = item.getItem().get_safeenchant();
		if(safeEnchant == 6 && currentEnchant < 7) {
			return;
		}
		
		TJLostItemDatabaseProvider.provider().newLostItem(pc, item);
	}
	
	private void onLostArmor(L1PcInstance pc, L1ItemInstance item) {
		if(!Config.ServerEnchant.enchantLostLog) {
			return;
		}
		
		int currentEnchant = item.getEnchantLevel();
		int safeEnchant = item.getItem().get_safeenchant();
		if(safeEnchant == 4) {
			if(currentEnchant < 5) {
				return;
			}
		}else if(safeEnchant == 6) {
			if(currentEnchant < 7) {
				return;
			}
		}
		TJLostItemDatabaseProvider.provider().newLostItem(pc, item);
	}
	
	private void onLostAccessory(L1PcInstance pc, L1ItemInstance item) {
		if(!Config.ServerEnchant.enchantLostLog) {
			return;
		}
		
		if(item.getEnchantLevel() < 5) {
			return;
		}

		TJLostItemDatabaseProvider.provider().newLostItem(pc, item);
	}
}
