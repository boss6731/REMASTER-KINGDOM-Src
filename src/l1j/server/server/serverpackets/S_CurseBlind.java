package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CurseBlind extends ServerBasePacket {

	private static final String S_CurseBlind = "[S] S_CurseBlind";

	public S_CurseBlind(int type) {
		// type 0：關閉 1：除了你自己之外不可見 2：你周圍的人物可見
		buildPacket(type);
	}

	private void buildPacket(int type) {
		writeC(Opcodes.S_BLIND);
		writeH(type);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_CurseBlind;
	}
}


