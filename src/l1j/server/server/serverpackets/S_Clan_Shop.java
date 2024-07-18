package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;

public class S_Clan_Shop extends ServerBasePacket {
	public S_Clan_Shop(L1PcInstance pc) {
		writeC(Opcodes.S_BUY_LIST);
		int npcId = 0;
		writeD(npcId);
		writeC(1);
		
		L1Shop shop = ShopTable.getInstance().get(npcId);
		List<L1ShopItem> shopItems = null;
		//List<L1ShopItem> shopItems = shop.getSellingItems();
		try {
			shopItems = shop.getSellingItems();
		} catch (Exception e) {
			System.out.println("S_Clan_Shop 沒有商店銷售清單。 ：" + npcId);
		}
		if (shopItems == null) {
			writeH(0);
			return;
		}
		writeH(shopItems.size());

		L1ItemInstance dummy = new L1ItemInstance();
		L1ShopItem shopItem = null;
		L1Item item = null;
		L1Item template = null;

		for (int i = 0; i < shopItems.size(); i++) {

			shopItem = shopItems.get(i);
			item = shopItem.getItem();
			int price = shopItem.getPrice();
			writeD(i);
			writeD(0);
			writeH(shopItem.getItem().getGfxId());
			writeD(price);
			if (shopItem.getPackCount() > 1)
				writeS(item.getName() + " (" + shopItem.getPackCount() + ")");
			else {
				writeS(item.getName());
			}
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
				writeD(dummy.getStatusBit());
				writeD(0x00);
				writeC(0x00);
				byte[] status = dummy.getStatusBytes(pc);
				
				//TODO nul點取消註釋，出現錯誤時查看
				/*if (status == null)
					System.out.println(dummy.getItemId());*/
				writeC(status.length);
				for (byte b : status) {
					writeC(b);
				}
				
				writeC(0x01);
				writeC(shopItem.getClanShopType());	
				//	08 王子/公主/守護騎士/菁英/將軍
				//	09 王子/公主/守護騎士
				//	0a 王子
			}
		}

		writeH(268377);// 商店購買標籤包圖片

	}







	@Override
	public byte[] getContent() throws IOException {
		return this._bao.toByteArray();
	}
}


