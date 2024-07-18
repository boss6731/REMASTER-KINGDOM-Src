package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;

public class S_PremiumShopSellList extends ServerBasePacket {

	/**
	 * 顯示商店的物品列表。當角色按下「購買」按鈕時發送。
	 */
	public S_PremiumShopSellList(int objId, L1PcInstance pc) {
		writeC(Opcodes.S_BUY_LIST);
		writeD(objId);
		writeC(0);
		L1Object npcObj = L1World.getInstance().findObject(objId);
		if (!(npcObj instanceof L1NpcInstance)) {
			writeH(0);
			return;
		}
		int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();

		L1Shop shop = ShopTable.getInstance().get(npcId);
		List<L1ShopItem> shopItems = null;
		List<L1ShopItem> passList = new ArrayList<L1ShopItem>();

		try {
			shopItems = shop.getSellingItems();
		} catch (Exception e) {
			System.out.println("商店（NPC）點擊時：NPCID（無物品/異常）： " + npcId);
		}

		if (shopItems != null) {
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
			writeH(shopItems.size() - passList.size());
		} else {
			writeH(0);
			return;
		}

		// 為了使用 L1ItemInstance 的 getStatusBytes
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
			try {
				writeH(shopItem.getItem().getGfxId());
			} catch (Exception e) {
				System.out.println("商店（NPC）點擊時：NPCID（無物品/異常）： " + npcId);
			}
			writeD(price);
			if (shopItem.getPackCount() > 1) {
				writeS(item.getName() + " (" + shopItem.getPackCount() + ")");
			} else if (shopItem.getEnchant() > 0) {
				writeS("+" + shopItem.getEnchant() + " " + item.getName());
			} else if (shopItem.getItem().getMaxUseTime() > 0) {
				writeS(item.getName() + " [" + item.getMaxUseTime() + "]");
			} else {
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
				dummy.setEnchantLevel(shopItem.getEnchant());
				byte[] status = dummy.getStatusBytes();
				if (status != null) {
					// 0:無 2:不可移動 4:不可刪除 6:不可移動, 不可刪除 8:不可強化 10:不可移動, 不可強化 12:不可刪除, 不可強化 14:不可移動, 不可刪除, 不可強化
					int bit = 0;
					if (!template.isTradable())
						bit += 2; // 不可交易
					if (template.isCantDelete())
						bit += 4; // 不可刪除
					if (template.get_safeenchant() < 0)
						bit += 8; // 不可強化
					if (item.getBless() >= 128)
						bit = 14; // 封印物品（不可移動、不可刪除、不可強化）
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
			/** 商店探索、貝瑞等圖片數據包（桌號） **/
// 探索商人
/*if ((npcId == 7200002)) {
writeC(253);
writeC(255);*/
// 貝瑞
/*} else if ((npcId == 7000077)) {
writeH(0x3A49);*/
// 未知 0/0
			if ((npcId == 900107)) {
				writeC(255);
				writeC(255);
				writeC(0);
				writeC(0);
// 金色羽毛
			} else if ((npcId == 6000002 /*|| npcId == 7310101*/)) {
				writeH(0x3ccf);
/*} else if (npcId == 7310119 || npcId == 7310120 || npcId == 7310126 || npcId == 7310124 || npcId == 520) {
writeH(0x4215);*/
/*} else if (npcId == 7310128) { // --如意珠
writeH(0x41D8);*/
/*} else if (npcId == 91245) {// 潘多拉的證書
writeH(0x3F23);*/
/*} else if (npcId == 70017 || npcId == 8502040 || npcId == 8502063) {// 奧利姆的石榴石
[19:28]
writeH(0x4115);*/
/*} else if (npcId == 7320250) {// 哈丁的石榴石
writeH(0x45AF);*/
/*} else if (npcId == 521) {// 魯道夫的鐘
writeH(17619);*/
				/*
				 * } else if (npcId == 7320055) {// N幣
				 * writeH(-2);
				 */
/*} else if (npcId == 8500140) {// 昆特的徽章
writeH(17368);*/
/*} else if (npcId == 2999 || npcId == 2998) {
writeH(30050);*/
			} else if (npcId == 5073 || npcId == 523) {
				writeH(15567);
/*} else if (npcId == 200005 || npcId == 7320222 || npcId == 7320223) {
writeH(4424);*/
/*} else if (npcId == 8502029) {
writeH(17735);*/
			} else if (npcId == 7320121 || npcId == 7320085 || npcId == 73201211 || npcId == 73201212 || npcId == 73201213
					|| npcId == 73201214 || npcId == 73201215 || npcId == 73201216 || npcId == 73201217 || npcId == 73201218
					|| npcId == 73201219 || npcId == 202056 || npcId == 8502049 || npcId == 2020700 || npcId == 2020701
					|| npcId == 2020702 || npcId == 2020703 || npcId == 2020704 || npcId == 2020705 || npcId == 2020706
					|| npcId == 2020707 || npcId == 2020708 || npcId == 2020709) {
				writeH(26532);
			} else {// 否則用羽毛顯示
				writeC(111);
				writeC(10);
			}
		}

		@override
		public byte[] getContent() throws IOException {
			return getBytes();
		}
	}


