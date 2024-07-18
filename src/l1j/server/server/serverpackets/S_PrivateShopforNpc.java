package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.NpcCashShopTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcShopTable2;
import l1j.server.server.datatables.NpcShopTable3;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcCashShopInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;

public class S_PrivateShopforNpc extends ServerBasePacket{




	public S_PrivateShopforNpc(L1PcInstance pc, int objId, int type){
		L1NpcInstance npc = (L1NpcInstance) L1World.getInstance().findObject(objId);

		if (npc == null) {
			return;
		}

		writeC(Opcodes.S_PERSONAL_SHOP_LIST);
		writeC(type);
		writeD(objId);

		boolean isNormalShop = false;
		if (type == 0) {
			int npcId = npc.getNpcTemplate().get_npcId();
			L1Shop shop = null;
			if (npc instanceof L1NpcShopInstance) {
				shop = NpcShopTable.getInstance().get(npcId);
				if(shop == null)
					shop = NpcShopTable2.getInstance().get(npcId);
				if(shop == null)
					shop = NpcShopTable3.getInstance().get(npcId);
			} else if (npc instanceof L1NpcCashShopInstance) {
				shop = NpcCashShopTable.getInstance().get(npcId);
			}else{
				shop = ShopTable.getInstance().get(npcId);
				isNormalShop = true;
			}
			if (shop == null) {
				System.out.println("NPC 商店錯誤：編號" + npc.getNpcId() + " x :" + npc.getX() + " y :" + npc.getY() + " 地圖：" + npc.getMapId());
				return;
			}
			List<L1ShopItem> shopItems = shop.getSellingItems();
			int size = shopItems.size();
			pc.setPartnersPrivateShopItemCount(size);
			writeH(size);
			L1ItemInstance dummy = new L1ItemInstance();
			for (int i = 0; i < size; i++) {
				L1ShopItem shopItem = shopItems.get(i);
				L1Item item = shopItem.getItem();

				dummy.setItem(item);
				if (dummy != null) {
					dummy.setEnchantLevel(shopItem.getEnchant());
					writeC(i);
					if(isNormalShop){
						int price 	= shopItem.getPrice();
						int count	= 1;
						if(price > 100000000)
							count = 1;
						else
							count =99;
						writeD(count);
					}else
						writeD(shopItem.getCount());
					writeD(shopItem.getPrice());
					writeH(dummy.getItem().getGfxId());
					writeC(shopItem.getEnchant());
					writeC(1);
					writeC(dummy.getItem().getBless());
					if(shopItem.getEnchant() > 0){
						if(shopItem.getCount() > 1) writeS("+"+shopItem.getEnchant()+" "+ dummy.getName() + " (" + shopItem.getCount() + ")");
						else writeS("+"+shopItem.getEnchant()+" "+ dummy.getName());
					}else{
						if(shopItem.getCount() > 1) writeS(dummy.getName() + " (" + shopItem.getCount() + ")");
						else writeS(dummy.getName());
					}
					byte[] status = dummy.getStatusBytes(pc);
					writeC(status.length);
					for (byte b : status)
						writeC(b);
				}
			}
			writeH(0);
		} else if (type == 1) {			
			int npcId = npc.getNpcTemplate().get_npcId();
			L1Shop shop = null;
			if (npc instanceof L1NpcShopInstance) {
				shop = NpcShopTable.getInstance().get(npcId);
				if(shop == null)
					shop = NpcShopTable2.getInstance().get(npcId);
				if(shop == null)
					shop = NpcShopTable3.getInstance().get(npcId);
			} else if (npc instanceof L1NpcCashShopInstance) {
				shop = NpcCashShopTable.getInstance().get(npcId);
			}
			List<L1ShopItem> shopItems = shop.getBuyingItems();
	
			int size = shopItems.size();
			//pc.setPartnersPrivateShopItemCount(size);
			writeH(size);
			
			for (int i = 0; i < size; i++) {
				L1ShopItem shopItem = shopItems.get(i);				
				//System.out.println(shopItem.getEnchant() + " " + shopItem.getItemId());			
				for (L1ItemInstance pcItem : pc.getInventory().getItems()) {						
					if (shopItem.getItemId() == pcItem.getItemId() && shopItem.getEnchant() == pcItem.getEnchantLevel()) {		
						//System.out.println("%" + pcItem.getEnchantLevel() + " " + pcItem.getItemId());
						writeC(i);
						writeD(pcItem.getId());
						writeD(1);
						writeD(1);
					}
				}
			} 
		}
	}
	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


