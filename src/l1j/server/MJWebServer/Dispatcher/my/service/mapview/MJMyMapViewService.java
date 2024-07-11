package l1j.server.MJWebServer.Dispatcher.my.service.mapview;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmModel;
import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmUserInfo;
import l1j.server.MJWebServer.ws.MJWebSockServerProvider;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;

public class MJMyMapViewService {
	private static final MJMyMapViewService service = new MJMyMapViewService();
	public static MJMyMapViewService service(){
		return service;
	}
	
	private final ConcurrentHashMap<Integer, MJMyMapViewEventWatcher> watchers;
	private MJMyMapViewService(){
		watchers = new ConcurrentHashMap<>();
	}
	
	public MJMyMapViewTileInfo mapViewInfo(int mapId){
		return MJMyMapViewTileProvider.provider().mapView(mapId);
	}
	
	public List<MJMyGmModel> onNewWatcher(int mapId, MJMyGmUserInfo uInfo){
		if(uInfo.request() == null) {
			uInfo.onInactive(null);
			return Collections.emptyList();
		}
		MJMyMapViewEventWatcher watcher = watchers.get(mapId);
		if(watcher == null){
			watcher = new MJMyMapViewEventWatcher(mapId);
			watchers.put(watcher.mapId(), watcher);
		}
		return watcher.onNewWatcher(uInfo);
	}
	
	public void onAppendObject(int mapId, L1Object object){
		MJMyMapViewEventWatcher watcher = watchers.get(mapId);
		if(watcher != null){
			watcher.onAppendObject(object);
		}
	}
	
	public void onRemoveObject(int mapId, int objectId){
		MJMyMapViewEventWatcher watcher = watchers.get(mapId);
		if(watcher != null){
			watcher.onRemoveObject(objectId);
		}
	}
	
	public void onMoveObject(L1Character character){
		MJMyMapViewEventWatcher watcher = watchers.get((int)character.getMapId());
		if(watcher != null){
			watcher.onMoveObject(character.getId(), character.getX(), character.getY(), (int)character.getCurrentSpriteInterval(EActionCodes.walk));
		}
	}
	
	public void onCommand(String command, String param, MJMyGmUserInfo uInfo){
		switch(command){
		case "pause":
			onPauseCommand(Integer.parseInt(param), uInfo);
			break;
			
		default:
			MJWebSockServerProvider.provider().print(uInfo.request(), String.format("invalid MJMyMapViewService command : %s, param : %s. connection close.", command, param));
			uInfo.request().close();
			break;
		}
	}
	
	private void onPauseCommand(int mapId, MJMyGmUserInfo uInfo){
		MJMyMapViewEventWatcher watcher = watchers.get(mapId);
		if(watcher == null){
			return;
		}
		
		watcher.onPauseWatcher(uInfo);
	}
}
