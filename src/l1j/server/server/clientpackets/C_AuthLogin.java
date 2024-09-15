package l1j.server.server.server.clientpackets;

import java.io.IOException;
import java.util.logging.Logger;

import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.server.clientpackets.Authorization;
import l1j.server.server.server.datatables.IpTable;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.utils.DelayClose;

public class C_AuthLogin extends l1j.server.server.clientpackets.ClientBasePacket {
	private static final String C_AUTH_LOGIN = "[C] C_AuthLogin";
	private static Logger _log = Logger.getLogger(C_AuthLogin.class.getName());

	public C_AuthLogin(byte[] decrypt, GameClient client) throws IOException {
		super(decrypt);
		try {
			
			String ip = client.getIp();
			String host = client.getHostname();
			
			if (IpTable.getInstance().isBannedIp(ip)) {
				System.out.println("\n┌───────────────────────────────┐");
				System.out.println("\t被封鎖的IP，連線已被阻止！IP地址=" + ip);
				System.out.println("└───────────────────────────────┘\n");
				client.sendPacket(new S_LoginResult(S_LoginResult.REASON_ACCOUNT_ALREADY_EXISTS));
				GeneralThreadPool.getInstance().schedule(new DelayClose(client), 5000);
				return;
			}
			
			String accountName = readS().toLowerCase();
			String password = readS();
			_log.finest("Request AuthLogin from user : " + accountName);

//            正常連線
			Authorization.getInstance().auth(client, accountName, password, ip, host);

//            迴環
//			Authorization.getInstance().auth(client,  "test123", "test123", ip, host);
		} catch (Exception e) {
		}
	}

	@Override
	public String getType() {
		return C_AUTH_LOGIN;
	}
}