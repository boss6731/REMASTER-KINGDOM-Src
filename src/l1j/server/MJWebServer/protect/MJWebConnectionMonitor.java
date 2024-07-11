package l1j.server.MJWebServer.protect;

import java.util.HashMap;

import l1j.server.MJNetServer.ClientManager.MJNSDenialAddress;
import l1j.server.MJTemplate.MJReadWriteLock;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJWebServer.protect.MJWebProtectInfo.ConnectionMonitorInfo;

class MJWebConnectionMonitor implements Matcher<String> {
	private static final MJWebConnectionMonitor monitor = new MJWebConnectionMonitor();
	static MJWebConnectionMonitor monitor(){
		return monitor;
	}

	private final HashMap<String, MJWebConnectionInfo> attachSockets;
	private final MJReadWriteLock lock;
	private MJWebConnectionMonitor(){
		attachSockets = new HashMap<>(256);
		lock = new MJReadWriteLock();
	}
	
	MJWebConnectionInfo safeConnection(String address){
		MJWebConnectionInfo connection = null;
		try{
			lock.writeLock();
			connection = attachSockets.get(address);
			if(connection == null){
				connection = new MJWebConnectionInfo();
				connection.address = address;
				attachSockets.put(address, connection);
			}
		}finally{
			lock.writeUnlock();
		}
		return connection;
	}
	
	@Override
	public boolean matches(String address) {
		ConnectionMonitorInfo mInfo = MJWebProtectService
				.service()
				.protectDefine()
				.connectionMonitor();
		
		MJWebConnectionInfo connection = safeConnection(address);
		synchronized(connection){	
			long currentMillis = System.currentTimeMillis();
			long takedMillis = currentMillis - connection.latestAccessMillis;
			if(takedMillis > mInfo.monitorIntervalMillis()){
				connection.accessCount = 0;
				connection.latestAccessMillis = currentMillis;
			}
			++connection.totalAccessCount;
			if(++connection.accessCount > mInfo.accessPerInterval()){
				if(connection.accessCount > mInfo.bannedPerInterval()) {
					MJNSDenialAddress.getInstance().insert_address(address, MJNSDenialAddress.REASON_WEB_ACCES_OVER);
					System.out.println(String.format("[MJWeb連線監視器]訪問太多。進程被禁止！\r\n%s %d/%d", connection.toString(), connection.accessCount, mInfo.bannedPerInterval()));
				}else {
					System.out.println(String.format("[MJWeb連線監視器]訪問過多\r\n%s", connection.toString()));
				}
				return true;
			}
		}
		return false;
	}
	
	private static class MJWebConnectionInfo{
		String address;
		long latestAccessMillis;
		int accessCount;
		int totalAccessCount;
		MJWebConnectionInfo(){
			address = MJString.EmptyString;
			latestAccessMillis = 0L;
			accessCount = 0;
			totalAccessCount = 0;
		}
		
		@Override
		public String toString(){
			return new StringBuilder(256)
					.append(" -IP位址： ").append(address).append("\r\n")
					.append(" -最後訪問毫秒： ").append(latestAccessMillis).append("\r\n")
					.append(" -使用者： current(").append(accessCount).append("), total(").append(totalAccessCount).append(")")
					.toString();
		}
	}

}
