package l1j.server.MJWebServer.Dispatcher.my.service.mapview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import l1j.server.MJTemplate.matcher.Matchers;
import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmModel;
import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmUserInfo;
import l1j.server.MJWebServer.ws.MJWebSockRequestGroup;
import l1j.server.MJWebServer.ws.protocol.MJWebSockSendModel;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

class MJMyMapViewEventWatcher {
	private static final String CALLBACK_OBJECT_APPEND = "onobject-append";
	private static final String CALLBACK_OBJECT_REMOVE = "onobject-remove";
	private static final String CALLBACK_OBJECT_MOVE = "onobject-move";
	
	private static MJWebSockSendModel<MJMyGmModel> makeSendModel(String callbackName, MJMyGmModel model){
		return new MJWebSockSendModel<MJMyGmModel>(callbackName, model);
	}
	
	private final int mapId;
	private final MJWebSockRequestGroup group;
	MJMyMapViewEventWatcher(int mapId){
		this.mapId = mapId;
		this.group = new MJWebSockRequestGroup(String.format("mapEventWater-%d", mapId), 4);
	}
	
	int mapId(){
		return mapId;
	}
	
	void onModel(String callbackName, MJMyMapViewObjectModel model){
		group.write(Matchers.all(), makeSendModel(callbackName, model));
	}
	
	void onPauseWatcher(MJMyGmUserInfo uInfo){
		group.remove(uInfo.request());
	}
	
	List<MJMyGmModel> onNewWatcher(MJMyGmUserInfo uInfo){
		Collection<L1Object> col = L1World.getInstance().getVisibleObjects(mapId()).values();
		ArrayList<MJMyGmModel> objects = new ArrayList<MJMyGmModel>(col.size());
		for(L1Object obj : col){
			if(obj instanceof L1NpcInstance){
				if(obj instanceof L1DoorInstance){
					continue;
				}
				
				if(obj instanceof L1MonsterInstance){					
					objects.add(MJMyMapViewObjectModel.newInfoModel((L1MonsterInstance)obj));
				}else{
					objects.add(MJMyMapViewObjectModel.newInfoModel((L1NpcInstance)obj));					
				}
			}else if(obj instanceof L1PcInstance){
				objects.add(MJMyMapViewObjectModel.newInfoModel((L1PcInstance)obj));
			}
		}
		group.add(uInfo.request());
		return objects;
	}
	
	void onAppendObject(L1Object object){
		if(group.size() <= 0){
			return;
		}
		if(object instanceof L1NpcInstance){
			if(object instanceof L1DoorInstance){
				return;
			}
			if(object instanceof L1MonsterInstance){
				onModel(CALLBACK_OBJECT_APPEND, MJMyMapViewObjectModel.newInfoModel((L1MonsterInstance)object));				
			}else{
				onModel(CALLBACK_OBJECT_APPEND, MJMyMapViewObjectModel.newInfoModel((L1NpcInstance)object));
			}
		}else if(object instanceof L1PcInstance){
			onModel(CALLBACK_OBJECT_APPEND, MJMyMapViewObjectModel.newInfoModel((L1PcInstance)object));
		}
	}
	
	void onRemoveObject(int objectId){
		if(group.size() <= 0){
			return;
		}
		onModel(CALLBACK_OBJECT_REMOVE, MJMyMapViewObjectModel.newRemove(objectId));
	}
	
	void onMoveObject(int objectId, int x, int y, int moveSpeed){
		if(group.size() <= 0){
			return;
		}
		onModel(CALLBACK_OBJECT_MOVE, MJMyMapViewObjectModel.newMove(objectId, x, y, moveSpeed));
	}
}
