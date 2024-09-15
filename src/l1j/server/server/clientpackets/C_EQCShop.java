package l1j.server.server.server.clientpackets;

import java.util.ArrayList;


import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;

public class C_EQCShop extends l1j.server.server.clientpackets.ClientBasePacket {
	public C_EQCShop(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		try {
			int PcObjectId = readD();
			readC();
			readC();
			readC();

			L1PcInstance pc = clientthread.getActiveChar();
			L1Object findObject = L1World.getInstance().findObject(PcObjectId);

			if (findObject instanceof L1PcInstance) {
				if (findObject.getId() == pc.getId()) {
					int objid = readD();
					int count = readD();
					if (pc.getEQCList() == null || pc.getEQCList().size() <= 0 || count > 1)
						return;

					ArrayList<L1ItemInstance> list = pc.getEQCList();

					int lsz = list.size();
					for (int i = 0; i < lsz; i++) {
						L1ItemInstance item = list.get(objid);

						if (pc.isTwoLogin(pc)) {
							return;
						}

						if (item == null) {
							return;
						}

						if (!item.isStackable() && count != 1) {
							return;
						}
						if (count <= 0 || item.getCount() <= 0) {
							return;
						}

						if (count > 1) {
							count = 1;
						}

						if (objid == i) {
							L1ItemInstance targetItem = pc.getEquipmentChangeItem();
							if (targetItem == null)
								return;
							if (!pc.getInventory().checkItem(targetItem.getItem().getItemId(), 1,
									targetItem.getEnchantLevel(), targetItem.getItem().getBless(),
									targetItem.getAttrEnchantLevel())
									|| !pc.getInventory().checkItem(pc.getEquipmentChangeUseItemId(), 1)) {
								pc.sendPackets("您沒有裝備交換券。");
								return;
							}

							pc.getInventory().consumeItem(pc.getEquipmentChangeUseItemId(), 1);
							pc.getInventory().removeItem(pc.getEquipmentChangeItem());
							pc.getInventory().storeTradeItem(item);
							break;
						} else {
							L1World.getInstance().removeObject(item);
						}
					}
					list.clear();
					pc.setEQCList(null);
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return "[C] C_EQCShop";
	}
}