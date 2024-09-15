package l1j.server.server.server.clientpackets;

import java.util.logging.Logger;

import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;

import l1j.server.server.server.model.Instance.L1PcInstance;

public class C_Disconnect extends l1j.server.server.clientpackets.ClientBasePacket {
	private static final String C_DISCONNECT = "[C] C_Disconnect";
	private static Logger _log = Logger.getLogger(C_Disconnect.class.getName());

	public C_Disconnect(byte[] decrypt, GameClient client) {
		super(decrypt);
		client.setStatus(MJClientStatus.CLNT_STS_HANDSHAKE);
		L1PcInstance pc = client.getActiveChar();
		System.out.println("C_DISCONNECT: 懷疑該連接角色的其他封包，已斷開連接，請重新連接！");
		if (pc != null) {
			_log.fine("斷開連線： " + pc.getName());
			pc.logout();
			client.setActiveChar(null);
		} else {
			_log.fine("與帳戶斷開連線的請求： " + client.getAccountName());
		}
	}

	@Override
	public String getType() {
		return C_DISCONNECT;
	}
}
