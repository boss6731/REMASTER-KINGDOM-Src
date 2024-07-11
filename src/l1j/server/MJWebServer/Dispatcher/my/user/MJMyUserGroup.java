package l1j.server.MJWebServer.Dispatcher.my.user;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.MJString;

public class MJMyUserGroup {
	private static final MJMyUserGroup group = new MJMyUserGroup();
	public static MJMyUserGroup group(){
		return group;
	}
	
	private ConcurrentHashMap<String, MJMyUserInfo> userGroup;
	private MJMyUserGroup(){
		userGroup = new ConcurrentHashMap<>();
	}
	
	public MJMyUserInfo put(String authToken, String account, int accessLevel, String address){
		MJMyUserInfo uInfo = new MJMyUserInfo();
		uInfo.authToken(authToken);
		uInfo.account(account);
		uInfo.accessLevel(accessLevel);
		uInfo.authTimeMillis(System.currentTimeMillis());
		uInfo.address(address);
		putInternal(uInfo);
		return uInfo;
	}
	
	void putInternal(MJMyUserInfo uInfo){
		if(MJString.isNullOrEmpty(uInfo.authToken())){
			throw new NullPointerException(String.format("authToken null...\r\n%s", uInfo.toString()));
		}
		remove(uInfo.authToken());
		userGroup.put(uInfo.authToken(), uInfo);
		uInfo.executeExpirationor();
	}
	
	public MJMyUserInfo remove(String authToken){
		MJMyUserInfo uInfo = removeInternal(authToken);
		if(uInfo != null){
			uInfo.stopExpirationor();
		}
		return uInfo;
	}
	
	MJMyUserInfo removeInternal(String authToken){
		return userGroup.remove(authToken);
	}
	
	public MJMyUserInfo get(String authToken){
		return userGroup.get(authToken);
	}
}
