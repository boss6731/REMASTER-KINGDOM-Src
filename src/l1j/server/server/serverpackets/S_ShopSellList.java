package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import l1j.server.Config;
import l1j.server.NpcShopCash.NpcShopCashTable;
import l1j.server.server.Opcodes;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopBuyLimitInfo;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TaxCalculator;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.templates.ShopBuyLimit;
import l1j.server.server.templates.eShopBuyLimitType;

public class S_ShopSellList extends ServerBasePacket {
	public static class ShopSellListException extends Exception {
		private static final long serialVersionUID = 1L;

		public ShopSellListException() {
			super();
		}

		public ShopSellListException(String message) {
			super(message);
		}
	}

	public static class MinigameSellListException extends Exception {
		private static final long serialVersionUID = 1L;

		public MinigameSellListException() {
			super();
		}

		public MinigameSellListException(String message) {
			super(message);
		}
	}

	@SuppressWarnings("deprecation")
	public S_ShopSellList(int objId, L1PcInstance pc) throws MinigameSellListException {
		writeC(Opcodes.S_BUY_LIST);
		writeD(objId);
		writeC(0);
		L1Object npcObj = L1World.getInstance().findObject(objId);
		if (!(npcObj instanceof L1NpcInstance)) {
			writeH(0);
			return;
		}
		int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();
		L1TaxCalculator calc = new L1TaxCalculator(npcId);
		L1Shop shop = ShopTable.getInstance().get(npcId);
		
		if(shop == null) {
			writeH(0);

			return;
		}

		List<L1ShopItem> shopItems = null;
		List<L1ShopItem> passList = new ArrayList<L1ShopItem>();
		try {
			shopItems = shop.getSellingItems();
		} catch (Exception e) {
			e.printStackTrace();
			if (npcId == BugRaceController.RACE_SELLER_NPCID || npcId == 70041 || npcId == 170041 || npcId == 370041) {
				pc.sendPackets(new S_NPCTalkReturn(objId, "maeno3"));
				throw new MinigameSellListException(String.format("找不到購買商店。[npcid : %d]", npcId));
			} else {
				System.out.println("[點擊NPC時] : NPCID(無物品/異常)(忽略BUG NPC) : " + npcId);
			}
			writeH(0);
			return;
		}
		if (shopItems != null) {
			// 如果是有時間限制的物品，查看用戶背包中是否有相同的物品
			Date date = new Date(0);
			for (L1ShopItem si : shopItems) {
				if (!si.isTimeLimit()) continue;
				date.setTime(pc.getFishingShopBuyTime_1());
				int item_day = date.getDate();
				date.setTime(System.currentTimeMillis());
				int current_day = date.getDate();
				if (item_day == current_day) passList.add(si);
			}

			for (L1ShopItem si : shopItems) {
				if (si.get_classType() != 10 && si.get_classType() != pc.getType()) {
					passList.add(si);
				}
				if (si.getBuyLevel() != 0 && pc.getLevel() > si.getBuyLevel()) {
					passList.add(si);
				}
			}
		}

			for (L1ShopItem st : shopItems) {
				ShopBuyLimit sbl = ShopBuyLimitInfo.getInstance().getShopBuyLimit(st.getItemId());
				if (sbl != null) {
					if (sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT
							|| sbl.get_type() == eShopBuyLimitType.CHARACTER_WEEK_LIMIT) {
						ShopBuyLimit char_sbl = ShopBuyLimitInfo.getInstance().findShopBuyLimitByObjid(pc.getId(),
								st.getItemId());
						if (char_sbl != null && char_sbl.get_end_time() != null) {
							if (char_sbl.get_count() <= 0
									&& char_sbl.get_end_time().getTime() > System.currentTimeMillis()) {
								passList.add(st);
							}
						}
					}
					if (sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT
							|| sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT) {
						ShopBuyLimit char_sbl_account = ShopBuyLimitInfo.getInstance()
								.findShopBuyLimitByAccount(pc.getAccount().getName(), st.getItemId());
						if (char_sbl_account != null && char_sbl_account.get_end_time() != null) {
							if (char_sbl_account.get_count() <= 0
									&& char_sbl_account.get_end_time().getTime() > System.currentTimeMillis()) {
								passList.add(st);
							}
						}
					}
				}
			}

			writeH(shopItems.size() - passList.size()); // 設置過濾後的物品數量
	} else {
		writeH(0); // 設置物品數量為0
		return;
	}

	if (shopItems != null && shopItems.size() <= 0) {
		if (npcId == BugRaceController.RACE_SELLER_NPCID || npcId == 70041 || npcId == 170041 || npcId == 370041) {
			pc.sendPackets(new S_NPCTalkReturn(objId, "maeno3")); // 發送 NPC 封包
			throw new MinigameSellListException(String.format("票券已經被清空。[npcid : %d]", npcId));
		}
	}

	L1ItemInstance dummy = new L1ItemInstance(); // 初始化臨時變量
	L1ShopItem shopItem = null;
	L1Item item = null;
	L1Item template = null;
	for (int i = 0; i < shopItems.size(); i++) {
		shopItem = (L1ShopItem) shopItems.get(i); // 獲取商店物品
		String itemname = "";
		if (passList.contains(shopItem)) continue; // 跳過在過濾列表中的物品

		item = shopItem.getItem(); // 獲取物品信息
		int price = calc.layTax((int) (shopItem.getPrice() * Config.ServerRates.RateShopSellingPrice)); // 計算價格

		int price1 = shopItem.getPrice();
		writeD(i); // 設置索引
		writeD(0); // 設置初始值
		// writeD(shopItem.getItem().getItemDescId()); // 註釋掉的代碼
		try {
			writeH(shopItem.getItem().getGfxId()); // 設置物品圖形ID
		} catch (Exception e) {
			System.out.println("NPC商店錯誤，NPC編號：" + npcId); // 打印錯誤信息
		}
		if (npcId == BugRaceController.RACE_SELLER_NPCID || npcId == 70041 || npcId == 170041 || npcId == 70042) {
			writeD(price1); // 設置原價
		} else {
			writeD(price); // 設置計算後的價格
		}

		if (shopItem.getPackCount() > 1) { // 包裝數量大於1
			itemname = item.getNameId() + " (" + shopItem.getPackCount() + ")";
		} else if (shopItem.getEnchant() > 0) { // 有附魔等級
			itemname = "+" + shopItem.getEnchant() + " " + item.getName();
		} else if (shopItem.getItem().getMaxUseTime() > 0) { // 有最大使用時間
			itemname = item.getName() + " [" + item.getMaxUseTime() + "]";



		} else {
			if (item.getItemId() >= 140074 && item.getItemId() <= 140100)
				itemname = "祝福的 " + item.getName();
			else if (item.getItemId() >= 240074 && item.getItemId() <= 240087)
				itemname = "詛咒的 " + item.getName();
			else
				itemname = item.getName();
		}

		if (shopItem.getAttrEnchant() > 0) // 處理屬性附魔
			itemname = getAttrToDisplay(shopItem.getAttrEnchant()) + itemname;

		if (shopItem.isCarving()) // 處理刻印
			writeS(itemname + " (刻印)");
		else
			writeS(itemname);

		int type = shopItem.getItem().getUseType(); // 處理物品使用類型
		if (type < 0) {
			type = 0;
		}
		writeD(type); // 寫入物品使用類型
		template = ItemTable.getInstance().getTemplate(item.getItemId());

		if (template == null) { // 如果模板為空
			writeC(0);
		} else {
			dummy.setItem(template); // 設置物品模板
			ShopBuyLimit sbl = ShopBuyLimitInfo.getInstance().getShopBuyLimit(dummy.getItemId());
			if (sbl != null) {
				dummy._cha = pc; // 設置角色
			}
			byte[] status = dummy.getStatusBytes(pc); // 獲取物品狀態字節數組
			if (status != null) {
				// 0: 沒有 2: 移動 4: 刪除 6: 移動, 刪除 8: 強化 10: 移動, 強化 12: 刪除, 強化 14: 移動, 刪除, 強化
				int bit = 0;
				if (!template.isTradable())
					bit += 2; // 交換不可
				if (template.isCantDelete())
					bit += 4; // 刪除不可
				if (template.get_safeenchant() < 0)
					bit += 8; // 附魔不可
				if (item.getBless() >= 128)
					bit = 14; // 封印物品(移動, 刪除, 強化不可)
				else
					bit |= 0x80;

				writeD(bit); // 寫入位掩碼
				writeD(0); // 寫入0值
				writeC(0); // 寫入0值
				writeC(status.length); // 寫入狀態字節數組的長度
				for (byte b : status) {
					writeC(b); // 寫入狀態字節
				}
			} else {
				System.out.println("物品錯誤信息通知: " + dummy.getItemId()); // 打印錯誤信息
			}
		}
	}
	NpcShopCashTable.L1CashType at = NpcShopCashTable.getInstance().getNpcCashType(npcId); // 獲取NPC現金描述類型
	if (at != null) {
		writeH(at.getCashDesc()); // 寫入現金描述
	} else {
		writeH(7); // 設置默認顯示類型為7
	}
}

	private static final String[] _attrToDisplay = new String[] { "", "火靈:1段 ", "火靈:2段 ", "火靈:3段 ", "火靈:4段 ", "火靈:5段 ",
			"水靈:1段 ", "水靈:2段 ", "水靈:3段 ", "水靈:4段 ", "水靈:5段 ", "風靈:1段 ", "風靈:2段 ", "風靈:3段 ", "風靈:4段 ", "風靈:5段 ",
			"地靈:1段 ", "地靈:2段 ", "地靈:3段 ", "地靈:4段 ", "地靈:5段 ", };

	public static String getAttrToDisplay(int attr) {
		if (attr < 0 || attr >= _attrToDisplay.length) // 檢查屬性值是否在範圍內
			return "";
		return _attrToDisplay[attr]; // 返回對應的屬性顯示名稱
	}

	public byte[] getContent() throws IOException {
		return getBytes(); // 返回內容的字節數組
	}


