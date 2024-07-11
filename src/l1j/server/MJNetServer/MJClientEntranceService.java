package l1j.server.MJNetServer;

import java.util.ArrayList;

import io.netty.util.AttributeKey;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ENTRANCE_INFO_NOTI;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;

public class MJClientEntranceService {
	private static final AttributeKey<EntranceClientModel> entranceAttribute = AttributeKey.newInstance("EntranceAttribute");
	private static final MJClientEntranceService service = new MJClientEntranceService();
	public static MJClientEntranceService service() {
		return service;
	}
	
	private boolean running;
	private MJClientEntranceModel entranceModel;
	private MJClientEntranceService() {
		running = true;
		MJMonitorCacheModel<MJClientEntranceModel> model = MJMonitorCacheProvider.newJsonFileCacheModel("mj-entrance-model", "./config/entrance-model.json", MJClientEntranceModel.class, MJEncoding.MS949);
		
		model.cacheListener(new EntranceModelConverter());
		
		/**
		 * 자동 Json 리로드 현상때문에 주석처리
		 */
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}
	
	MJClientEntranceModel entranceModel() {
		return entranceModel;
	}
	
	private class EntranceModelConverter implements MJMonitorCacheConverter<MJClientEntranceModel>{
		@Override
		public MJClientEntranceModel onNewCached(MJClientEntranceModel t, long modifiedMillis) {
			entranceModel = t;
			return null;
		}
	}
	
	public boolean useWaitQueue() {
		return entranceModel().useWaitQueue;
	}
	
	public void useWaitQueue(boolean flag) {
		entranceModel().useWaitQueue = flag;
	}
	
	public void release() {
		running = false;
	}
	
	public void offer(GameClient clnt, Runnable waitCallback) {
		if(!running || waiting(clnt)) {
			return;
		}
		
		EntranceClientModel model = new EntranceClientModel();
		model.client = clnt;
		model.waiting = true;
		model.canceled = false;
		model.waitSeconds = entranceModel().nextSeconds();
		model.waitCallback = waitCallback;
		newAttribute(model);
		GeneralThreadPool.getInstance().execute(new EntranceExecuteCallback(model));
	}
	
	public boolean waiting(GameClient clnt) {
		EntranceClientModel model = model(clnt);
		return model != null && model.waiting;
	}
	
	public void cancel(GameClient clnt) {
		EntranceClientModel model = model(clnt);
		if(model == null) {
			return;
		}
		model.canceled = true;
		removeAttribute(model);
	}
	
	void newAttribute(EntranceClientModel model) {
		model.client.getChannel().attr(entranceAttribute).set(model);
	}
	
	void removeAttribute(EntranceClientModel model) {
		model.client.getChannel().attr(entranceAttribute).set(null);
	}
	
	private EntranceClientModel model(GameClient clnt) {
		return clnt.getChannel().attr(entranceAttribute).get();
	}
	
	private static class EntranceExecuteCallback implements Runnable{
		private EntranceClientModel model;
		EntranceExecuteCallback(EntranceClientModel model){
			this.model = model;
		}
		
		
		private ArrayList<Integer> newQueueCountArray(int currentQueueCount){
			int tick = (currentQueueCount - 3) / model.waitSeconds;
			ArrayList<Integer> queueCountArray = new ArrayList<>(model.waitSeconds + 1);
			for(int i=0; i<model.waitSeconds; ++i) {
				currentQueueCount -= tick;
				queueCountArray.add(currentQueueCount);
			}
			queueCountArray.add(1);
			return queueCountArray;
		}

		@Override
		public void run() {
			try {
				int currentQueueCount = MJClientEntranceService.service().entranceModel().nextQueueCount();
				ArrayList<Integer> queueCountArray = newQueueCountArray(currentQueueCount);
				sendRemainQueue(currentQueueCount);
				Thread.sleep(1000);
				for(Integer queueCount : queueCountArray) {
					if(model.canceled) {
						return;
					}
					currentQueueCount = MJClientEntranceService.service().entranceModel().nextQueueCount(queueCount, currentQueueCount);
					
					if(MJRnd.isBoolean()) {
						sendRemainQueue(currentQueueCount);
					}
					Thread.sleep(1000);
				}
				if(model.canceled) {
					return;
				}
				if(currentQueueCount > 5) {
					sendRemainQueue(MJClientEntranceService.service().entranceModel().nextQueueCount(1, 4));
					Thread.sleep(1000);
				}
				if(model.canceled) {
					return;
				}
				model.waiting = false;
				model.waitCallback.run();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				MJClientEntranceService.service().removeAttribute(model);
			}
		}
		
		private void sendRemainQueue(int queueCount) {
			SC_ENTRANCE_INFO_NOTI noti = SC_ENTRANCE_INFO_NOTI.newInstance();
			noti.set_waiting_user_count(queueCount);
			model.client.sendPacket(noti, MJEProtoMessages.SC_ENTRANCE_INFO_NOTI.toInt());
		}
	}
	
	private static class EntranceClientModel{
		GameClient client;
		int waitSeconds;
		boolean waiting;
		boolean canceled;
		Runnable waitCallback;
	}
}
