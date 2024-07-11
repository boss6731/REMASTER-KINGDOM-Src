package l1j.server.MJWebServer.protect;

import java.util.HashMap;

import l1j.server.MJNetServer.ClientManager.MJNSDenialAddress;
import l1j.server.MJTemplate.MJReadWriteLock;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJWebAccessMonitor {
	private final String signature;
	private final HashMap<String, SocketUriAccessInfo> attachSockets;
	private final MJReadWriteLock attachSocketsLock;
	private final String uri;
	public long monitorIntervalMillis;
	public int accessPerInterval;
	public int overBanned;
	MJWebAccessMonitor(String signature, String uri, long monitorIntervalMillis, int accessPerInterval, int overBanned){
		this.attachSockets = new HashMap<>(256);
		this.attachSocketsLock = new MJReadWriteLock();
		this.signature = signature;
		this.uri = uri;
		this.monitorIntervalMillis = monitorIntervalMillis;
		this.accessPerInterval = accessPerInterval;
		this.overBanned = overBanned;
	}
	
	private SocketUriAccessInfo safeAccessInfo(String address){
		SocketUriAccessInfo access = null;
		try{
			attachSocketsLock.writeLock();
			access = attachSockets.get(address);
			if(access == null){
				access = new SocketUriAccessInfo();
				access.address = address;
				attachSockets.put(address, access);
			}
		}finally{
			attachSocketsLock.writeUnlock();
		}
		return access;
	}
	
	boolean onAccess(MJHttpRequest request){
		String address = request.get_remote_address_string();
		SocketUriAccessInfo access = safeAccessInfo(address);
		synchronized(access){
			long currentMillis = System.currentTimeMillis();
			long takedMillis = currentMillis - access.latestAccessMillis;
			if(takedMillis > monitorIntervalMillis){
				access.accessCount = 0;
				access.latestAccessMillis = currentMillis;
			}
			++access.totalAccessCount;
			int over = ++access.accessCount - accessPerInterval;
			if(over > 0){
				if(overBanned > 0 && over > overBanned){
					MJNSDenialAddress.getInstance().insert_address(address, MJNSDenialAddress.REASON_WEB_ACCES_OVER);
					System.out.println(String.format("[%s]禁止的訪問過多！\r\n%s %d ", signature,access.toString(), access.totalAccessCount));
				}else{						
					System.out.println(String.format("[%s]造訪次數過多\r\n%s %d", signature, access.toString(), access.totalAccessCount));
				}
				return true;
			}
		}
		return false;
	}
	
	private class SocketUriAccessInfo{
		String address;
		long latestAccessMillis;
		int accessCount;
		int totalAccessCount;
		SocketUriAccessInfo(){
			address = MJString.EmptyString;
			latestAccessMillis = 0L;
			accessCount = 0;
			totalAccessCount = 0;
		}
		
		@Override
		public String toString(){
			return new StringBuilder(256)
					.append(" 類型： ").append(uri).append("\r\n")
					.append(" -IP位址： ").append(address).append("\r\n")
					.append(" -最後訪問毫秒： ").append(latestAccessMillis).append("\r\n")
					.append(" -access: current(").append(accessCount).append("), total(").append(totalAccessCount).append(")")
					.toString();
		}
	}
}
