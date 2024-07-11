package l1j.server.MJWebServer.Dispatcher.my.service.chat;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharSimpleInfo;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserInfo;
import l1j.server.MJWebServer.ws.MJWebSockCallbackService;
import l1j.server.MJWebServer.ws.MJWebSockExchangeComposite;
import l1j.server.server.GeneralThreadPool;

public class MJMyChatService {
	private static final MJMyChatService service = new MJMyChatService();
	public static MJMyChatService service(){
		return service;
	}
	
	private MJWebSockCallbackService callbackService;
	private MJMyChatExchangeHandler exchangeHandler;
	private ConcurrentHashMap<String, MJMyChatUserInfo> registeredUsers;
	private ConcurrentHashMap<String, MJMyChatUserInfo> namesUsers;
	private Map<String, MJMyChatUserInfo> unmodifiedRegisteredUsers;
	private Map<String, MJMyChatUserInfo> unmodifiedNamesUsers;

	private MJMyChatWriter worldWriter;
	private MJMyChatWriter worldGmWriter;
	private MJMyChatWriter pledgeWriter;
	private MJMyChatWriter whisperWriter;
	private MJMyChatWriter systemWriter;
	private MJMyChatService(){
		registeredUsers = new ConcurrentHashMap<>();
		namesUsers = new ConcurrentHashMap<>();
		callbackService = new MJWebSockCallbackService();
		MJWebSockExchangeComposite.composite().appendHandler(exchangeHandler = new MJMyChatExchangeHandler());
		
		worldWriter = MJMyChatWriters.newWorldWriter();
		worldGmWriter = MJMyChatWriters.newWorldGmWriter();
		pledgeWriter = MJMyChatWriters.newPledgeWriter();
		whisperWriter = MJMyChatWriters.newWhisperWriter();
		systemWriter = MJMyChatWriters.newSystemWriter();
	}
	
	MJWebSockCallbackService callbackService(){
		return callbackService;
	}
	
	MJMyChatExchangeHandler exchangeHandler(){
		return exchangeHandler;
	}
	
	private String makeCallbackName(String uid){
		return new StringBuilder(32)
				.append("[")
				.append(uid)
				.append("]onchat")
				.toString();
	}
	
	public boolean containsRegisteredCallback(String authToken){
		return registeredUsers.contains(authToken);
	}
	
	public MJMyChatUserInfo registeredCallback(MJMyCharSimpleInfo cInfo, MJMyUserInfo uInfo){
		String uid = uInfo.authToken();
		if(registeredUsers.containsKey(uid)){
			MJMyChatUserInfo beforeUserInfo = registeredUsers.remove(uid);
			if(beforeUserInfo != null){
				beforeUserInfo.onInactive(beforeUserInfo.request());
			}
			return null;
		}
		
		MJMyChatUserInfo cuInfo = new MJMyChatUserInfo(uInfo);	
		cuInfo.uid(uid);
		cuInfo.character(cInfo);
		final String callbackName = makeCallbackName(cuInfo.uid());
		cuInfo.callbackName(callbackName);
		registeredUsers.put(cuInfo.uid(), cuInfo);
		callbackService.put(callbackName, MJMyChatRecvModel.class);
		namesUsers.put(cInfo.nick(), cuInfo);
		executeSessionMonitor(cuInfo);
		return cuInfo;
	}
	
	private void executeSessionMonitor(MJMyChatUserInfo uInfo){
		GeneralThreadPool.getInstance().schedule(new MJMyChatSessionMonitor(uInfo), MJMyResource.construct().webSocket().sessionMonitorMillis());		
	}
	
	void onInactive(MJMyChatUserInfo cuInfo){
		registeredUsers.remove(cuInfo.uid());
		callbackService.remove(cuInfo.callbackName());
		namesUsers.remove(cuInfo.character().nick());
	}
	
	public Map<String, MJMyChatUserInfo> unmodifiedRegisteredUsers(){
		return unmodifiedRegisteredUsers == null ? 
				unmodifiedRegisteredUsers = Collections.unmodifiableMap(registeredUsers) : unmodifiedRegisteredUsers;
	}
	
	public Map<String, MJMyChatUserInfo> unmodifiedNamesUsers(){
		return unmodifiedNamesUsers == null ? 
				unmodifiedNamesUsers = Collections.unmodifiableMap(namesUsers) : unmodifiedNamesUsers;
	}
	
	public int numOfRegisteredUsers(){
		return registeredUsers.size();
	}
	
	public MJMyChatWriter worldWriter(){
		return worldWriter;
	}
	public MJMyChatWriter worldGmWriter() {
		return worldGmWriter;
	}
	public MJMyChatWriter pledgeWriter(){
		return pledgeWriter;
	}
	public MJMyChatWriter whisperWriter(){
		return whisperWriter;
	}
	public MJMyChatWriter systemWriter(){
		return systemWriter;
	}
	
	private static class MJMyChatSessionMonitor implements Runnable{
		private MJMyChatUserInfo uInfo;
		MJMyChatSessionMonitor(MJMyChatUserInfo uInfo){
			this.uInfo = uInfo;
		}
		@Override
		public void run() {
			if(uInfo.request() == null){
				uInfo.onInactive(null);
			}
		}
		
	}
}
