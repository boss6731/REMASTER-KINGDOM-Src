package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Bookmarks extends ServerBasePacket {
	
	private static final String _S__1F_S_Bookmarks = "[S] S_Bookmarks";

	public S_Bookmarks(String name, int map, int x, int y, int bookid) {
		buildPacket(name, map, x, y, bookid);
	}

	private void buildPacket(String name, int map, int x, int y, int bookid) {
		writeC(Opcodes.S_ADD_BOOKMARK);
		writeS(name);
		writeH(map);
		writeH(x);
		writeH(y);
		writeD(bookid);
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__1F_S_Bookmarks;
	}
}