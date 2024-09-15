package l1j.server.server.clientpackets;

import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_PERSONAL_SHOP_ITEM_LIST_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_PERSONAL_SHOP_ITEM_LIST_NOTI.ePersonalShopType;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_ShopList extends ClientBasePacket {

	private static final String C_SHOP_LIST = "[C] C_ShopList";

	public C_ShopList(byte abyte0[], GameClient clientthread) {
		super(abyte0);
		int type = readC();
		int objectId = readD();

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}

		L1Object shopPc = L1World.getInstance().findObject(objectId);

		if (shopPc instanceof L1PcInstance) {

			L1PcInstance cha = (L1PcInstance) shopPc;

			if (pc.getAccountName().equalsIgnoreCase(cha.getAccountName())) {
				pc.sendPackets("同一帳號內無人商店無法使用。");
				return;
			}
			SC_PERSONAL_SHOP_ITEM_LIST_NOTI.do_send(pc, objectId, ePersonalShopType.fromInt(type));
		} else if (shopPc instanceof L1NpcShopInstance) {
			SC_PERSONAL_SHOP_ITEM_LIST_NOTI.do_send_for_npc(pc, objectId, ePersonalShopType.fromInt(type));
		} else {
			if (shopPc != null && shopPc.instanceOf(MJL1Type.L1TYPE_CLANJOIN)) {
				pc.sendPackets("想加入血盟請進行砍殺。");
			} else {
				pc.sendPackets("未發現商店對象");
			}
			return;
		}
	}

	@Override
	public String getType() {
		return C_SHOP_LIST;
	}

}
