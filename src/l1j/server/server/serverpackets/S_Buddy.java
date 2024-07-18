package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayList;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.BuddyTable;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class S_Buddy extends ServerBasePacket {
	private static final String _S_Buddy = "[S] _S_Buddy";

	public S_Buddy(L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(337);
		writeC(0x08);
		writeBit(1);

		// writeD(0x01080151);
		ArrayList<L1Buddy> buddy_list = BuddyTable.getInstance().getBuddyList(pc.getId());
		L1Buddy buddy = null;
		if (buddy_list != null) {
			for (int i = 0; i < buddy_list.size(); i++) {
				buddy = buddy_list.get(i);
				if (buddy != null) {
					writeC(0x12);
					byte[] stats = getBuddyData(buddy);
					writeBit(stats.length);
					writeByte(stats);

				}
			}
		}
		writeH(0);
	}

	private byte[] getBuddyData(L1Buddy buddy) {
		BinaryOutputStream os = new BinaryOutputStream();
		try {
			L1PcInstance target = L1PcInstance.load(buddy.getCharName());

			os.writeC(0x0a);
			os.writeBit(buddy.getCharName().getBytes().length);
			os.writeByte(buddy.getCharName().getBytes());
			os.writeC(0x10);
			os.writeBit(target.getOnlineStatus() > 0 ? 1 : 0);
			if (buddy.getMemo() != null && buddy.getMemo().length() > 1) {
				os.writeC(0x1a);
				os.writeBit(buddy.getMemo().getBytes().length);
				os.writeByte(buddy.getMemo().getBytes());
			} else {
				os.writeC(0x1a);
				os.writeC(0);
			}
			os.writeC(0x20);
			os.writeBit(target.getType());

			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S_Buddy;
	}
}
