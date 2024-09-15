package l1j.server.server.server.serverpackets;

import l1j.server.NpcShopCash.NpcShopCashTable;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopBuyLimitInfo;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.templates.ShopBuyLimit;
import l1j.server.server.templates.eShopBuyLimitType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class S_AdenStore extends ServerBasePacket {
	public enum Currency{
		ADENA(7),
		NCOIN(-2),
		;
		private int val;
		private Currency(int val){
			this.val = val;
		}
		
		int val() {
			return val;
		}
	}

	public S_AdenStore(L1PcInstance pc, Currency currency) {
		writeC(Opcodes.S_BUY_LIST);
		writeD(7626); // 購買時檢查的封包
		writeC(0);
		// 亞丁商店 NPC 編號
		int npcId = 5; // 亞丁商店所需的 NPC 編號，即使在世界中不存在也可以

		L1Shop shop = ShopTable.getInstance().get(npcId);
		List<L1ShopItem> shopItems = shop.getSellingItems();
		List<L1ShopItem> passList = new ArrayList<L1ShopItem>();
		
		
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
			
			if (st.getBuyLevel() != 0) {
				if (pc.getLevel() > st.getBuyLevel()) {
					passList.add(st);
				}
			}
		}
		
		writeH(shopItems.size() - passList.size());

		L1ItemInstance dummy = new L1ItemInstance();
		L1ShopItem shopItem = null;
		L1Item item = null;
		L1Item template = null;
		for (int i = 0; i < shopItems.size(); i++) {
			shopItem = (L1ShopItem) shopItems.get(i);
			
			if (passList.contains(shopItem))
				continue;
			
			item = shopItem.getItem();
			int price = shopItem.getPrice();
			writeD(i);
			writeD(shopItem.getItem().getItemDescId());
			writeH(shopItem.getItem().getGfxId());
			writeD(price);
			if (shopItem.getPackCount() > 1)
				writeS(item.getName() + " (" + shopItem.getPackCount() + ")");
			else {
				writeS(item.getName());
			}
			int type = shopItem.getItem().getUseType();
			if (type < 0){
				type = 0;
			}
			writeD(type);
			template = ItemTable.getInstance().getTemplate(item.getItemId());
			if (template == null) {
				writeC(0);
			} else {
				dummy.setItem(template);
				
				ShopBuyLimit sbl = ShopBuyLimitInfo.getInstance().getShopBuyLimit(dummy.getItemId());
				if(sbl != null) {
					dummy._cha = pc;
				}
				
				byte[] status = dummy.getStatusBytes(pc);
				if (status != null) {
					// 0:無 2:移動 4:刪除 6:移動,刪除 8:強化 10:移動,強化 12:刪除,強化 14:移動,刪除,強化
					int bit = 0;
					if (!template.isTradable())
						bit += 2; // 無法交易
					if (template.isCantDelete())
						bit += 4; // 無法刪除
					if (template.get_safeenchant() < 0)
						bit += 8; // 無法強化
					if (item.getBless() >= 128)
						bit = 14; // 封印物品（無法移動, 刪除, 強化）
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
			writeH(7); // 標記類型部分
		}
	}

	public byte[] getContent() throws IOException {
		return getBytes();
	}
}