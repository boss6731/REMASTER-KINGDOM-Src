package l1j.server.EQCSystem;

import java.io.IOException;
import java.util.ArrayList;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_EQCPacket extends ServerBasePacket {
	public static S_EQCPacket get(L1PcInstance pc, ArrayList<EQCModel> list, int enchant, int attrEnchant, int bless) {
		S_EQCPacket pck = new S_EQCPacket();

		ArrayList<L1ItemInstance> items = pc.getEQCList();
		if (items != null && items.size() <= 0) {
			for (L1ItemInstance itm : items) {
				if (itm == null)
					continue;
				L1World.getInstance().removeObject(itm);
			}
			items.clear();
		}

		pck.writeC(8);
		pck.writeBit(pc.getId());

		if (list == null || list.size() <= 0) {
			pck.writeC(16);
			pck.writeBit(0);
			return pck;
		}

		/** 檢查獲取次數 */
		int size = list.size();
		pck.writeC(16);
		pck.writeBit(size);

		pck.writeC(24);
		pck.writeBit(3);

		/** 金額部分 */
		pck.writeC(32);
		pck.writeBit(0);

		pck.writeC(40);
		pck.writeBit(size);

		EQCModel model = null;
		L1ItemInstance item = null;
		items = new ArrayList<L1ItemInstance>(size);

		if (list.size() > 0) {
			for (int i = 0; i < size; i++) {
				pck.writeC(50);
				model = list.get(i);
				item = ItemTable.getInstance().createItem(model.itemId);
				if (item == null) {
					item = ItemTable.getInstance().createItem(40005);
					item.getItem().setNameId(String.format("[物品ID錯誤 : %d]", model.itemId));
					byte[] stats = warehouseItemInfo(pc, i, item);
					pck.writeBit(stats.length);
					pck.writeByte(stats);
					continue;
				}
				item.setEnchantLevel(enchant);
				item.setAttrEnchantLevel(attrEnchant);
				byte[] stats = warehouseItemInfo(pc, i, item);
				pck.writeBit(stats.length);
				pck.writeByte(stats);
				items.add(item);
			}
		}

		pck.writeC(56);
		pck.writeBit(1L);

		pck.writeH(0);
		pc.setEQCList(items);
		return pck;
	}

	private static byte[] warehouseItemInfo(L1PcInstance pc, int index, L1ItemInstance item) {
		BinaryOutputStream os = new BinaryOutputStream();
		os.writeC(8);
		os.writeBit(index);
		os.writeC(18);
		byte[] stats = pc.sendItemPacket(pc, item);
		os.writeBit(stats.length);
		os.writeByte(stats);
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.getBytes();
	}

	private S_EQCPacket() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(1032);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
