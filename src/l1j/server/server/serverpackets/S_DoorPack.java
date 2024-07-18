package l1j.server.server.serverpackets;

import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1DoorInstance;

public class S_DoorPack extends ServerBasePacket {



	private static final String S_DOOR_PACK = "[S] S_DoorPack";

	private static final int STATUS_POISON = 1;

	public S_DoorPack(L1DoorInstance door) {
		buildPacket(door);
	}

	private void buildPacket(L1DoorInstance door) {
		writeC(Opcodes.S_PUT_OBJECT); // 寫入物件放置操作碼
		writeH(door.getX()); // 寫入門的 X 座標
		writeH(door.getY()); // 寫入門的 Y 座標
		writeD(door.getId()); // 寫入門的 ID

		int spriteId = door.getCurrentSpriteId(); // 取得門的當前精靈 ID
		writeH(spriteId); // 寫入精靈 ID
		int doorStatus = door.getStatus(); // 取得門的狀態
		int openStatus = door.getOpenStatus(); // 取得門的開啟狀態

		if (door.isDead()) { // 如果門已經損壞
			writeC(doorStatus); // 寫入門的狀態
		} else if (openStatus == ActionCodes.ACTION_Open) { // 如果門處於開啟狀態
			writeC(openStatus); // 寫入開啟狀態
		} else if (door.getMaxHp() > 1 && doorStatus != 0) { // 如果門的最大 HP 大於 1 且門狀態不為 0
			writeC(doorStatus); // 寫入門的狀態
		} else {
			writeC(openStatus); // 否則寫入開啟狀態
		}









		writeC(0); // 寫入 0
		writeC(0); // 寫入 0
		writeC(0); // 寫入 0
		writeD(1); // 寫入 1
		writeH(0); // 寫入 0

		if (spriteId == 12164 || spriteId == 12167 || spriteId == 12170 // 64~70 奧克要塞外城門
				|| spriteId == 12987 || spriteId == 12989 || spriteId == 12991 // 87~91 肯特城外城門
				|| spriteId == 12127 || spriteId == 12129 || spriteId == 12131 || spriteId == 12133 // 29~33 奇岩城外城門
		) {
			writeS("$440"); // 寫入字符串 "$440"
		} else if (spriteId == 339 // 肯特城內城門
				|| spriteId == 1336 // 奇岩城內城門
				|| spriteId == 12163 // 奧克要塞內城門
		) {
			writeS("$441"); // 寫入字符串 "$441"
		} else {
			writeS(null); // 否則寫入空字符串
		}

		writeS(null); // 寫入空字符串

		int status = 0; // 初始化狀態為 0

		if (door.getPoison() != null) { // 如果門有中毒狀態
			if (door.getPoison().getEffectId() == 1) { // 如果中毒效果 ID 為 1
				status |= STATUS_POISON; // 將中毒狀態設為有效
			}
		}
	}
		writeC(status);
		writeD(0);
		writeS(null);
		writeS(null);
		writeC(0);
		writeC(0xFF);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0xFF);
		writeC(0xFF);
		writeC(0);
		writeC(0);
		writeC(0xFF);
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_DOOR_PACK;
	}

}
