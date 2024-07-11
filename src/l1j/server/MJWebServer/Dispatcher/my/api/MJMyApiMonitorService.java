package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyConstruct;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyConstruct.MJMyApiMonitor;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.MJWebServer.protect.MJWebAccessMonitorAdapter;

public class MJMyApiMonitorService implements Matcher<MJHttpRequest>{
	private static final MJMyApiMonitorService service = new MJMyApiMonitorService();
	public static MJMyApiMonitorService service(){
		return service;
	}

	private MJWebAccessMonitorAdapter adapter;
	private MJMyApiMonitorService(){
		adapter = MJWebAccessMonitorAdapter.newAdapter("MJMyApiMonitorService", "/my/api/...", 0, 0, 0, false);
	}
	
	public void onConstructChanged(MJMyConstruct construct){
		if(construct != null && construct.apiMonitor() != null){
			MJMyApiMonitor monitor = construct.apiMonitor();
			adapter.monitorIntervalMillis(monitor.monitorIntervalMillis());
			adapter.accessPerInterval(monitor.accessPerInterval());
			adapter.overBanned(monitor.overBanned());
			adapter.alive(true);
		}else{
			adapter.alive(false);
		}
	}

	@Override
	public boolean matches(MJHttpRequest request) {
		if(!adapter.alive()){
			return false;
		}
		
		return adapter.onAccess(request);
	}
}
