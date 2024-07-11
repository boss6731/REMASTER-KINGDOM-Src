package l1j.server.MJWebServer.Dispatcher.my.user;

import java.util.LinkedList;
import java.util.concurrent.ScheduledFuture;

import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.server.GeneralThreadPool;

public class MJMyUserInfo {
	private String authToken;
	private String account;
	private int accessLevel;
	private long authTimeMillis;
	private String address;
	private ScheduledFuture<?> future;
	private LinkedList<MJMyUserExpirationListener> listeners;
	MJMyUserInfo(){}
	
	void executeExpirationor(){
		stopExpirationor();
		future = GeneralThreadPool.getInstance().schedule(
				new MJMyUserExpirationor(this), MJMyResource.construct().auth().authExpirationMillis());
	}
	
	void stopExpirationor(){
		if(future != null){
			future.cancel(true);
			future = null;
		}
		fireExpirationor();
	}
	
	void fireExpirationor(){
		if(listeners != null){
			for(MJMyUserExpirationListener listener : listeners){
				listener.onExpiration(this);
			}
		}
	}
	
	void authToken(String authToken){
		this.authToken = authToken;
	}
	
	void account(String account){
		this.account = account;
	}
	
	void accessLevel(int accessLevel){
		this.accessLevel = accessLevel;
	}
	
	void authTimeMillis(long authTimeMillis){
		this.authTimeMillis = authTimeMillis;
	}
	
	void address(String address){
		this.address = address;
	}
	
	public String authToken(){
		return authToken;
	}
	
	public String account(){
		return account;
	}
	
	public int accessLevel(){
		return accessLevel;
	}
	
	public long authTimeMillis(){
		return authTimeMillis;
	}
	
	public String address(){
		return address;
	}
	
	public ScheduledFuture<?> future(){
		return future;
	}
	
	public void registeredListener(MJMyUserExpirationListener listener){
		if(listeners == null){
			listeners = new LinkedList<>();
		}
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}
	
	public void unregisteredListener(MJMyUserExpirationListener listener){
		listeners.remove(listener);
	}
	
	@Override
	public String toString(){
		return new StringBuilder()
				.append("[MJMyUserInfo]\r\n")
				.append("authToken : ").append(authToken).append("\r\n")
				.append("account : ").append(account).append("\r\n")
				.append("accessLevel : ").append(accessLevel).append("\r\n")
				.append("authTimeMillis : ").append(authTimeMillis)
				.toString();
	}
}
