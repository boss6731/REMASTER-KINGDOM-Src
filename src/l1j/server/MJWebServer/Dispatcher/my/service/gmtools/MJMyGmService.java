package l1j.server.MJWebServer.Dispatcher.my.service.gmtools;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;

import MJFX.UIAdapter.MJPerformAdapter;
import l1j.server.Config;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJTemplate.matcher.Matchers;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserInfo;
import l1j.server.MJWebServer.ws.MJWebSockCallbackService;
import l1j.server.MJWebServer.ws.MJWebSockExchangeComposite;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJMyGmService {
	private static final MJMyGmService service = new MJMyGmService();
	public static MJMyGmService service(){
		return service;
	}
	
	private MJWebSockCallbackService callbackService;
	private MJMyGmExchangeHandler exchangeHandler;
	private ConcurrentHashMap<String, MJMyGmUserInfo> registeredUsers;
	private Map<String, MJMyGmUserInfo> unmodifiedRegisteredUsers;
	private ScheduledFuture<?> future;
	
	private MJMyGmWriter resourceWriter;
	private MJMyGmWriter characterInWriter;
	private MJMyGmWriter characterOutWriter;
	private MJMyGmService(){
		registeredUsers = new ConcurrentHashMap<>();
		callbackService = new MJWebSockCallbackService();
		MJWebSockExchangeComposite.composite().appendHandler(exchangeHandler = new MJMyGmExchangeHandler());
		resourceWriter = MJMyGmWriters.newResourceWriter();
		characterInWriter = MJMyGmWriters.newCharacterInWriter();
		characterOutWriter = MJMyGmWriters.newCharacterOutWriter();
	}

	MJWebSockCallbackService callbackService(){
		return callbackService;
	}
	
	MJMyGmExchangeHandler exchangeHandler(){
		return exchangeHandler;
	}
	
	public boolean containsRegisteredCallback(String authToken){
		return registeredUsers.contains(authToken);
	}
	
	public Map<String, MJMyGmUserInfo> unmodifiedRegisteredUsers(){
		return unmodifiedRegisteredUsers == null ? 
				unmodifiedRegisteredUsers = Collections.unmodifiableMap(registeredUsers) : unmodifiedRegisteredUsers;
	}
	
	public int numOfRegisteredUsers(){
		return registeredUsers.size();
	}
	
	private String makeCallbackName(String uid){
		return new StringBuilder(32)
				.append("[")
				.append(uid)
				.append("]gmTools")
				.toString();
	}
	
	public MJMyGmUserInfo registeredCallback(MJMyUserInfo uInfo){
		String uid = uInfo.authToken();
		if(registeredUsers.containsKey(uid)){
			MJMyGmUserInfo beforeUserInfo = registeredUsers.remove(uid);
			if(beforeUserInfo != null){
				beforeUserInfo.onInactive(beforeUserInfo.request());
			}
			return null;
		}
		MJMyGmUserInfo gInfo = new MJMyGmUserInfo(uInfo);
		gInfo.uid(uid);
		final String callbackName = makeCallbackName(gInfo.uid());
		gInfo.callbackName(callbackName);
		registeredUsers.put(gInfo.uid(), gInfo);
		callbackService.put(callbackName, MJMyGmRecvModel.class);
		executeSessionMonitor(gInfo);
		executePerform();
		return gInfo;
	}
	
	void onInactive(MJMyGmUserInfo gInfo){
		registeredUsers.remove(gInfo.uid());
		callbackService.remove(gInfo.callbackName());
		stopPerform();
	}
	
	public MJMyGmCharViewProvider charViewProvider(){
		return MJMyGmCharViewProvider.provider();
	}
	
	public MJMyGmWriter resourceWriter(){
		return resourceWriter;
	}
	
	public MJMyGmWriter characterInWriter(){
		return characterInWriter;
	}
	
	public MJMyGmWriter characterOutWriter(){
		return characterOutWriter;
	}
	
	public void onEnterCharacter(L1PcInstance pc){
		GameClient clnt = pc.getNetConnection();
		if(clnt == null){
			return;
		}
		MJMyGmCharViewModel model = charViewProvider().newModel(clnt);
		if(model == null){
			return;
		}
		characterInWriter().write(Matchers.all(), model);
	}
	
	public void onRemoveCharacter(L1PcInstance pc){
		MJMyGmCharacterOutModel model = charViewProvider().newCharacterOut(pc.getAccountName());
		characterOutWriter().write(Matchers.all(), model);
	}
	
	private void executeSessionMonitor(MJMyGmUserInfo gInfo){
		GeneralThreadPool.getInstance().schedule(new MJMyGmSessionMonitor(gInfo), MJMyResource.construct().webSocket().sessionMonitorMillis());
	}
	
	void executePerform(){
		if(future != null){
			return;
		}
		if(numOfRegisteredUsers() > 0){
			future = GeneralThreadPool.getInstance().scheduleAtFixedRate(
					new MJPerformUpdator(), 0, MJMyResource.construct().webSocket().gm().resourcePerformUpdateMillis());			
		}
	}
	
	void stopPerform(){
		if(future == null){
			return;
		}
		if(numOfRegisteredUsers() <= 0){
			future.cancel(true);
			future = null;
		}
	}
	
	private static class MJMyGmSessionMonitor implements Runnable{
		private MJMyGmUserInfo gInfo;
		MJMyGmSessionMonitor(MJMyGmUserInfo uInfo){
			this.gInfo = uInfo;
		}
		@Override
		public void run() {
			if(gInfo.request() == null){
				gInfo.onInactive(null);
			}
		}	
	}
	
	private static class MJPerformUpdator implements Runnable{
		@SuppressWarnings("unused")
		private static class MJMyResourceModel extends MJMyGmModel{
			int cpuUsage;
			int totalMemory;
			int freeMemory;
			int usedMemory;
			int maxMemory;
			int threadCount;
			MJMyResourceThreadModel executor;
			MJMyResourceThreadModel scheduler;
			MJMyResourceThreadModel pcScheduler;
			int clients;
			int clientsLimit;
			int npcs;
			int items;
			MJMyResourceModel(){
				super();
				executor = new MJMyResourceThreadModel();
				scheduler = new MJMyResourceThreadModel();
				pcScheduler = new MJMyResourceThreadModel();
			}
			
			static class MJMyResourceThreadModel{
				int active;
				int poolSize;
				int queue;
				long task;
				long complete;
			}
		}
		
		private MJMyResourceModel model;
		MJPerformUpdator(){
			model = new MJMyResourceModel();
		}
		
		private void updateExecutor(ThreadPoolExecutor executor, MJMyResourceModel.MJMyResourceThreadModel model){
			model.active = executor.getActiveCount();
			model.poolSize = executor.getPoolSize();
			model.queue = executor.getQueue().size();
			model.task = executor.getTaskCount();
			model.complete = executor.getCompletedTaskCount();
		}
		
		@Override
		public void run() {
			if(MJMyGmService.service().numOfRegisteredUsers() <= 0){
				return;
			}
			
			model.cpuUsage = MJPerformAdapter.CPU_USAGE;
			model.threadCount = MJPerformAdapter.THREAD_USAGE;
			
			Runtime r = Runtime.getRuntime();
			model.totalMemory = (int) (r.totalMemory() / 1024);
			model.freeMemory = (int) (r.freeMemory() / 1024);
			model.usedMemory = model.totalMemory - model.freeMemory;
			model.maxMemory = (int) (r.maxMemory() / 1024);
			updateExecutor(GeneralThreadPool.getInstance().executor(), model.executor);
			updateExecutor(GeneralThreadPool.getInstance().scheduler(), model.scheduler);
			updateExecutor(GeneralThreadPool.getInstance().pcScheduler(), model.pcScheduler);
			model.clients = MJNSHandler.getClientSize();
			model.clientsLimit = Config.Login.MaximumOnlineUsers;
			model.npcs = L1World.getInstance().get_npc_size();
			model.items = L1World.getInstance().get_item_size();
			
			MJMyGmService
			.service()
			.resourceWriter()
			.write(Matchers.all(), model);
		}
	}
	
	
}
