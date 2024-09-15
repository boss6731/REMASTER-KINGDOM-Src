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
				throw new MinigameSellListException(String.format("無法找到購買店鋪。[npcid : %d]", npcId));
			} else {
				System.out.println("[NPC點擊時] : NPCID(無物品/異常)(忽略背景NPC) : " + npcId);
			}
			writeH(0);
			return;
		}
		if (shopItems != null) {
			// 如果是有時間限制的物品，請從用戶的背包中查找相同的物品。
			Date date = new Date(0);
			for (L1ShopItem si : shopItems) {
				if (!si.isTimeLimit())
					continue;
				//
				date.setTime(pc.getFishingShopBuyTime_1());
				int item_day = date.getDate();
				date.setTime(System.currentTimeMillis());
				int current_day = date.getDate();
				if (item_day == current_day)
					passList.add(si);
			}

			for (L1ShopItem si : shopItems) {
				if (si.get_classType() != 10) {
					if (si.get_classType() != pc.getType()) {
						passList.add(si);
					}
				}

				if (si.getBuyLevel() != 0) {
					if (pc.getLevel() > si.getBuyLevel()) {
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

			writeH(shopItems.size() - passList.size());
		} else {
			writeH(0);
			return;
		}

		if (shopItems != null && shopItems.size() <= 0) {
			if (npcId == BugRaceController.RACE_SELLER_NPCID || npcId == 70041 || npcId == 170041 || npcId == 370041) {
				pc.sendPackets(new S_NPCTalkReturn(objId, "maeno3"));
				throw new MinigameSellListException(String.format("票券為空。[npcid : %d]", npcId));
			}
		}

		L1ItemInstance dummy = new L1ItemInstance();
		L1ShopItem shopItem = null;
		L1Item item = null;
		L1Item template = null;
		for (int i = 0; i < shopItems.size(); i++) {
			shopItem = (L1ShopItem) shopItems.get(i);
			String itemname = "";
			if (passList.contains(shopItem))
				continue;

			item = shopItem.getItem();
			int price = calc.layTax((int) (shopItem.getPrice() * Config.ServerRates.RateShopSellingPrice));

			int price1 = shopItem.getPrice();
			writeD(i);
			writeD(0);
			//writeD(shopItem.getItem().getItemDescId());
			try {
				writeH(shopItem.getItem().getGfxId());
			} catch (Exception e) {
				System.out.println("NPC 商店錯誤 NPC 編號 :" + npcId);
			}
			if (npcId == BugRaceController.RACE_SELLER_NPCID || npcId == 70041 || npcId == 170041 || npcId == 70042)
				writeD(price1);
			else {
				writeD(price);
			}

			if (shopItem.getPackCount() > 1) {
				itemname = item.getNameId() + " (" + shopItem.getPackCount() + ")";
			} else if (shopItem.getEnchant() > 0) {
				itemname = "+" + shopItem.getEnchant() + " " + item.getName();
			} else if (shopItem.getItem().getMaxUseTime() > 0) {
				itemname = item.getName() + " [" + item.getMaxUseTime() + "]";
			} else {
				if (item.getItemId() >= 140074 && item.getItemId() <= 140100)
					itemname = "受到祝福的 " + item.getName();
				else if (item.getItemId() >= 240074 && item.getItemId() <= 240087)
					itemname = "受到詛咒的 " + item.getName();
				else
					itemname = item.getName();
			}

			if (shopItem.getAttrEnchant() > 0)
				itemname = getAttrToDisplay(shopItem.getAttrEnchant()) + itemname;

			if (shopItem.isCarving())
				writeS(itemname + "(刻印)");
			else
				writeS(itemname);

			int type = shopItem.getItem().getUseType();
			if (type < 0) {
				type = 0;
			}
			writeD(type);
			template = ItemTable.getInstance().getTemplate(item.getItemId());

			if (template == null) {
				writeC(0);
			} else {
				dummy.setItem(template);
				ShopBuyLimit sbl = ShopBuyLimitInfo.getInstance().getShopBuyLimit(dummy.getItemId());
				if (sbl != null) {
					dummy._cha = pc;
				}
				byte[] status = dummy.getStatusBytes(pc);
			    if (status != null) {
					// 0:無 2:移動 4:刪除 6:移動,刪除 8:強化 10:移動,強化 12:刪除,強化 14:移動,刪除,強化
					int bit = 0;
					if (!template.isTradable())
						bit += 2; // 交換不可
					if (template.isCantDelete())
						bit += 4; // 刪除不可
					if (template.get_safeenchant() < 0)
						bit += 8; // 無法強化
					if (item.getBless() >= 128)
						bit = 14; // 封印道具（無法移動、刪除、或強化）
		               else
		                  bit |= 0x80;
		               
		               writeD(bit);   
		               writeD(0);
		               writeC(0);
		               writeC(status.length);
		               for (byte b : status) {
		                  writeC(b);
		               }
		            } else {
					System.out.println("物品錯誤信息通知 : " + dummy.getItemId());
		            }
			}
		}
		NpcShopCashTable.L1CashType at = NpcShopCashTable.getInstance().getNpcCashType(npcId);
		if (at != null) {
			writeH(at.getCashDesc());
		} else {
			writeH(7);// 標記類型部分
		}
	}

	private static final String[] _attrToDisplay = new String[] { "", "火靈:1級 ", "火靈:2級 ", "火靈:3級 ", "火靈:4級 ", "火靈:5級 ",
			"水靈:1級 ", "水靈:2級 ", "水靈:3級 ", "水靈:4級 ", "水靈:5級 ", "風靈:1級 ", "風靈:2級 ", "風靈:3級 ", "風靈:4級 ", "風靈:5級 ",
			"地靈:1級 ", "地靈:2級 ", "地靈:3級 ", "地靈:4級 ", "地靈:5級 ", };

	public static String getAttrToDisplay(int attr) {
		if (attr < 0 || attr >= _attrToDisplay.length)
			return "";
		return _attrToDisplay[attr];
	}

	public byte[] getContent() throws IOException {
		return getBytes();
	}
}