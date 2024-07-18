package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_WorldPutObject extends ServerBasePacket {
	// 是否顯示遺忘之島標記...(true：使用, false：不使用)
	public static final boolean IS_PRESENTATION_MARK = true;

	private static final int SC_WORLD_PUT_OBJECT_NOTI = 0x77;
	public L1PcInstance _pc = null;


	public static S_WorldPutObject get(byte[] b) {
		S_WorldPutObject s = new S_WorldPutObject();
		s.writeByte(b);
		return s;
	}

	private S_WorldPutObject() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_WORLD_PUT_OBJECT_NOTI);
	}
}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "S_WorldPutObject";
	}
}


