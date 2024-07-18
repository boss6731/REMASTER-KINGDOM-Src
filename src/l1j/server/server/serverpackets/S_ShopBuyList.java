package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1AssessedItem;
import l1j.server.server.model.shop.L1Shop;

public class S_ShopBuyList extends ServerBasePacket {


	private static final String S_SHOP_BUY_LIST = "[S] S_ShopBuyList";

	public S_ShopBuyList(int objid, L1PcInstance pc) {
		L1Object object = L1World.getInstance().findObject(objid);
		if (!(object instanceof L1NpcInstance)) {
			throw new IllegalArgumentException(String.format("未知的對象被傳送到商店購買列表。[objectid : %d, L1Object : %d]", objid, object == null ? 0 : object.getL1Type()));
		}
		L1NpcInstance npc = (L1NpcInstance) object;
		int npcId = npc.getNpcTemplate().get_npcId();
		L1Shop shop = ShopTable.getInstance().get(npcId);
		if (shop == null) {
			pc.sendPackets(new S_NoSell(npc));
			System.out.println(String.format("[錯誤忽略:確認] 找不到銷售商店。[npcid : %d]", npcId));
			throw new IllegalArgumentException(String.format("[錯誤忽略:確認] 找不到銷售商店。[npcid : %d]", npcId));
		}

		List<L1AssessedItem> assessedItems = shop.assessItems(pc.getInventory());

		writeC(Opcodes.S_SELL_LIST); // 設置操作碼，顯示銷售列表
		writeD(objid); // 設置對象ID
		writeH(assessedItems.size()); // 設置物品數量

		int real_count = 0;
		for (L1AssessedItem item : assessedItems) {
			writeD(item.getTargetId()); // 設置目標ID
			writeD(item.getAssessedPrice()); // 設置評估價格
			++real_count;
		}
		if (real_count <= 0) {
			pc.sendPackets(new S_NoSell(npc)); // 發送無可銷售物品的封包
			throw new IllegalArgumentException(String.format("找不到銷售的物品列表。[npcid : %d]", npcId));
		}
		if (npcId == 8502050) {
			writeH(26532); // 設置額外信息
		} else {
			writeH(0x07); // 設置商店銷售列表
		}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_SHOP_BUY_LIST;
	}
}


