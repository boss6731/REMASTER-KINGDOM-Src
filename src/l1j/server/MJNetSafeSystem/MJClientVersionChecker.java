package l1j.server.MJNetSafeSystem;

import l1j.server.MJNetServer.ClientManager.MJNSDenialAddress;
import l1j.server.MJTemplate.MJWhiteIP;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;

public class MJClientVersionChecker implements Runnable{
	public static void execute(GameClient clnt) {
		GeneralThreadPool.getInstance().schedule(new MJClientVersionChecker(clnt), MJNetSafeLoadManager.NS_CLIENT_HANDHSAKE_CHECK_MILLIS);
	}
	
	private GameClient m_clnt;
	private MJClientVersionChecker(GameClient clnt) {
		m_clnt = clnt;
	}
	
	@Override
	public void run() {
		try {
			if(m_clnt == null)
				return;

			if(!m_clnt.is_non_handshake()) {
				String address = m_clnt.getIp();
				m_clnt.close();
				MJNSDenialAddress.getInstance().insert_address_handshake(address, MJNSDenialAddress.REASON_FILTER_HANDSHAKE);
			}else {
				MJWhiteIP.getInstance().put(m_clnt.getIp());
			}
		}catch(Exception e) {}
	}

}
