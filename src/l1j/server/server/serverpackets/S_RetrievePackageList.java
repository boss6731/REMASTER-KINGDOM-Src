package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_RetrievePackageList extends ServerBasePacket {
	public boolean NonValue = false;

	public S_RetrievePackageList(int objid, L1PcInstance pc) {
//        if (pc.getInventory().getSize() < 180) {
		if (pc.getInventory().getSize() < 200) {
			int size = pc.getDwarfForPackageInventory().getSize();
			if (size > 0) {
				writeC(Opcodes.S_RETRIEVE_LIST);
				writeD(objid);
				writeH(size);
				writeC(15); // 6: 無反應 7: 斷線 8: 精靈倉庫寄存 9: 精靈提取 15: 包裹商店
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
				writeC(0x1E); // 30 元
			} else {
				this.NonValue = true;
			}
		} else {
			pc.sendPackets(new S_ServerMessage(263)); // 1一個角色可以攜帶的最大物品數量是180個。
		}
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}




}
