package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.S_ServerVersion;

public class C_ServerVersion extends ClientBasePacket {

	private static final String C_SERVER_VERSION = "[C] C_ServerVersion";

	public C_ServerVersion(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);
		
		if (client == null) {
			return;
		}
		client.sendPacket(new S_ServerVersion());
	}

	@Override
	public String getType() {
		return C_SERVER_VERSION;
	}

}
