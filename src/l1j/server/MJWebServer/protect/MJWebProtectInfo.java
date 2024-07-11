package l1j.server.MJWebServer.protect;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class MJWebProtectInfo {
//	private HashMap<String, String> acceptOutsideAddresses;
	private HashMap<String, String> denialOutsideAddresses;
	private WebSocketAccessInfo websockAccess;
	private ConnectionMonitorInfo connectionMonitor;
	private UriMonitor uriMonitor;
/*	MJWebProtectInfo(){
		acceptOutsideAddresses = new HashMap<>();
	}*/
	MJWebProtectInfo(){
		denialOutsideAddresses = new HashMap<>();
	}
	WebSocketAccessInfo websockAccess(){
		return websockAccess;
	}
	
	ConnectionMonitorInfo connectionMonitor(){
		return connectionMonitor;
	}
	
	UriMonitor uriMonitor(){
		return uriMonitor;
	}
	
/*	HashMap<String, String> acceptOutsideAddresses(){
		return acceptOutsideAddresses;
	}*/
	HashMap<String, String> denialOutsideAddresses(){
		return denialOutsideAddresses;
	}
	
	static class WebSocketAccessInfo{
		private int concurrentAccessCountByAddress;
		
		int concurrentAccessCountByAddress(){
			return concurrentAccessCountByAddress;
		}
	}
	
	static class ConnectionMonitorInfo{
		private long monitorIntervalMillis;
		private int accessPerInterval;
		private int bannedPerInterval;
		
		long monitorIntervalMillis(){
			return monitorIntervalMillis;
		}
		
		int accessPerInterval(){
			return accessPerInterval;
		}
		
		int bannedPerInterval() {
			return bannedPerInterval;
		}
	}
	
	static class UriMonitor{
		private int uriLengthLimit;
		private UriMonitorInfo apiMonitor;
		private List<UriMonitorInfo> monitors;
		UriMonitor(){
			uriLengthLimit = 1024;
			monitors = Collections.emptyList();
			apiMonitor = null;
		}
		
		int uriLengthLimit(){
			return uriLengthLimit;
		}
		
		List<UriMonitorInfo> monitors(){
			return monitors;
		}
		
		UriMonitorInfo apiMonitor(){
			return apiMonitor;
		}
	}
	
	static class UriMonitorInfo{
		private String uri;
		private long monitorIntervalMillis;
		private int accessPerInterval;
		private int overBanned;
		
		String uri(){
			return uri;
		}
		
		long monitorIntervalMillis(){
			return monitorIntervalMillis;
		}
		
		int accessPerInterval(){
			return accessPerInterval;
		}
		
		int overBanned(){
			return overBanned;
		}
	}
}
