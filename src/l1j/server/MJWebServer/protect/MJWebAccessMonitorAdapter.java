package l1j.server.MJWebServer.protect;

import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJWebAccessMonitorAdapter {
	public static MJWebAccessMonitorAdapter newAdapter(
			String signature, String uri, long monitorIntervalMillis, int accessPerInterval, int overBanned, boolean alive){
		return new MJWebAccessMonitorAdapter(signature, uri, monitorIntervalMillis, accessPerInterval, overBanned, alive);
	}
	
	private MJWebAccessMonitor accessMonitor;
	private boolean alive;
	MJWebAccessMonitorAdapter(String signature, String uri, long monitorIntervalMillis, int accessPerInterval, int overBanned, boolean alive){
		this.alive = alive;
		this.accessMonitor = new MJWebAccessMonitor(signature, uri, monitorIntervalMillis, accessPerInterval, overBanned);
	}
	
	public void alive(boolean alive){
		this.alive = alive;
	}
	
	public boolean alive(){
		return alive;
	}
	
	public void monitorIntervalMillis(long monitorIntervalMillis){
		accessMonitor.monitorIntervalMillis = monitorIntervalMillis;
	}
	
	public void accessPerInterval(int accessPerInterval){
		accessMonitor.accessPerInterval = accessPerInterval;
	}
	
	public void overBanned(int overBanned){
		accessMonitor.overBanned = overBanned;
	}
	
	public boolean onAccess(MJHttpRequest request){
		if(!alive){
			return false;
		}
		return accessMonitor.onAccess(request);
	}
}
