package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_RetrievePackageList extends ServerBasePacket {
	public boolean NonValue = false;
	public S_RetrievePackageList(int objid, L1PcInstance pc) {
//		if (pc.getInventory().getSize() < 180) {
		if (pc.getInventory().getSize() < 200) {
			int size = pc.getDwarfForPackageInventory().getSize();
			if (size > 0) {
				writeC(Opcodes.S_RETRIEVE_LIST);
				writeD(objid);
				writeH(size);
				writeC(15); // 6: 無反應 7: 斷線 8: 存放至妖精倉庫 9: 妖精查找 15: 套裝商城
				L1ItemInstance item = null;
				for (Object itemObject : pc.getDwarfForPackageInventory().getItems()) {
					item = (L1ItemInstance) itemObject;
					writeD(item.getId());
					writeC(item.getItem().getType2());
					writeH(item.get_gfxid());
					writeC(item.getItem().getBless());
					writeD(item.getCount());
					writeC(item.isIdentified() ? 1 : 0);
					writeS(item.getViewName());
				}
				writeC(0x1E);//30원
			} else {
				this.NonValue = true;
			}
		} else {
			pc.sendPackets(new S_ServerMessage(263)); // 1一個角色最多能攜帶的物品為 180 個
		}
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

}
