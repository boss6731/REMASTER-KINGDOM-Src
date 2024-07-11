package l1j.server.MJWebServer.protect;

import java.util.HashMap;
import java.util.List;

import l1j.server.MJTemplate.MJReadWriteLock;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.MJWebServer.protect.MJWebProtectInfo.UriMonitorInfo;

class MJWebUriMonitor implements Matcher<MJHttpRequest>{
	private static final MJWebUriMonitor monitor = new MJWebUriMonitor();
	static MJWebUriMonitor monitor(){
		return monitor;
	}
	
	private final HashMap<String, MJWebAccessMonitor> attachUri;
	private final MJReadWriteLock attachUriLock;
		private MJWebUriMonitor(){	
		attachUri = new HashMap<>();
		attachUriLock = new MJReadWriteLock();
	}
		
	private MJWebAccessMonitor safeMonitor(String uri){
		try{
			attachUriLock.readLock();
			return attachUri.get(uri);
		}finally{
			attachUriLock.readUnlock();
		}
	}

	@Override
	public boolean matches(MJHttpRequest request) {
		MJWebAccessMonitor monitor = safeMonitor(request.get_request_uri());
		if(monitor == null){
			return false;
		}
		
		return monitor.onAccess(request);
	}
		
	void onUpdateMonitorSettings(List<UriMonitorInfo> settings){
		try{
			attachUriLock.writeLock();
			for(UriMonitorInfo mInfo : settings){
				MJWebAccessMonitor monitor = attachUri.get(mInfo.uri());
				if(monitor != null){
					monitor.monitorIntervalMillis = mInfo.monitorIntervalMillis();
					monitor.accessPerInterval = mInfo.accessPerInterval();
					monitor.overBanned = mInfo.overBanned();
				}else{
					attachUri.put(mInfo.uri(), new MJWebAccessMonitor("MJWebUriMonitor", mInfo.uri(), mInfo.monitorIntervalMillis(), mInfo.accessPerInterval(), mInfo.overBanned()));
				}
			}
		}finally{
			attachUriLock.writeUnlock();
		}
	}
}
