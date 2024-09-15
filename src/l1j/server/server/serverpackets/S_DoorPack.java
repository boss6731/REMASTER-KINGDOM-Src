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
		writeC(Opcodes.S_PUT_OBJECT);
		writeH(door.getX());
		writeH(door.getY());
		writeD(door.getId());

		int spriteId = door.getCurrentSpriteId();
		writeH(spriteId);
		int doorStatus = door.getStatus();
		int openStatus = door.getOpenStatus();
		if (door.isDead()) {
			writeC(doorStatus);
		} else if (openStatus == ActionCodes.ACTION_Open) {
			writeC(openStatus);
		} else if (door.getMaxHp() > 1 && doorStatus != 0) {
			writeC(doorStatus);
		} else {
			writeC(openStatus);
		}
		writeC(0);
		writeC(0);
		writeC(0);
		writeD(1);
		writeH(0);
		if (spriteId == 12164 || spriteId == 12167 || spriteId == 12170 // 64~70 亞丁堡外城門
				|| spriteId == 12987 || spriteId == 12989 || spriteId == 12991 // 87~91 肯特城外城門
				|| spriteId == 12127 || spriteId == 12129 || spriteId == 12131 || spriteId == 12133 // 29~33 奇岩城外城門
		) {
			writeS("$440");
		} else if (spriteId == 339 // 肯特城內城門
				|| spriteId == 1336 // 奇岩城內城門
				|| spriteId == 12163 // 亞丁堡內城門
		) {
			writeS("$441");
		} else {
			writeS(null);
		}
		writeS(null);
		int status = 0;
		if (door.getPoison() != null) {
			if (door.getPoison().getEffectId() == 1) {
				status |= STATUS_POISON;
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
