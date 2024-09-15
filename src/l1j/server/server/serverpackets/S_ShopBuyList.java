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
			throw new IllegalArgumentException(String.format("無法識別的物件被傳送到商店購買清單中。[objectid : %d, L1Object : %d]", objid, object == null ? 0 : object.getL1Type()));
		}
		L1NpcInstance npc = (L1NpcInstance) object;
		int npcId = npc.getNpcTemplate().get_npcId();
		L1Shop shop = ShopTable.getInstance().get(npcId);
		if (shop == null) {
			pc.sendPackets(new S_NoSell(npc));
			System.out.println(String.format("[錯誤忽略:確認] 無法找到銷售店鋪。[npcid : %d]", npcId));
			throw new IllegalArgumentException(String.format("[錯誤忽略:確認] 無法找到銷售店鋪。[npcid : %d]", npcId));
		}

		List<L1AssessedItem> assessedItems = shop.assessItems(pc.getInventory());

		writeC(Opcodes.S_SELL_LIST);
		writeD(objid);
		writeH(assessedItems.size());

		int real_count = 0;
		for (L1AssessedItem item : assessedItems) {
			writeD(item.getTargetId());
			writeD(item.getAssessedPrice());
			++real_count;
		}
		if(real_count <= 0){
			pc.sendPackets(new S_NoSell(npc));
			throw new IllegalArgumentException(String.format("無法找到銷售項目清單。[npcid : %d]", npcId));
		}
		if(npcId == 8502050) {
			writeH(26532);
		}else {
			writeH(0x07);// 商店銷售清單
		}
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