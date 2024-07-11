package l1j.server.MJWebServer.Dispatcher.my;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.cookie.Cookie;
import l1j.server.MJNetServer.ClientManager.MJNSDenialAddress;
import l1j.server.MJTemplate.MJKeyValuePair;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserGroup;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserInfo;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserTokenFactory;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.MJWebServer.protect.MJWebProtectService;
import l1j.server.server.Account;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public abstract class MJMyController {
	private MJHttpRequest request;
	private MJMyUserInfo uInfo;
	private String authToken;
	private boolean onIngameLogin;
	protected MJMyController(MJHttpRequest request){
		this.request = request;
		this.uInfo = null;
		this.authToken = MJString.EmptyString;
		
		String characterName = MJString.EmptyString;
		
		LOOP_END: for(Cookie cookie : request.get_cookies()){
			switch(cookie.name()){
			case "authToken":
				authToken = cookie.value();
				uInfo = MJMyUserGroup.group().get(authToken);
				break LOOP_END;
				
			case "lineage1_charId":
				try {
					characterName = URLDecoder.decode(cookie.value(), "euc-kr");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		
		String account = request.read_parameters_at_once("account");
		String password = request.read_parameters_at_once("password");

		if(uInfo == null && MJString.isNullOrEmpty(authToken) && !MJString.isNullOrEmpty(characterName)) {
			onIngameAccess(characterName);
			return;
		}
		
		if (!MJString.isNullOrEmpty(account) && !MJString.isNullOrEmpty(password)) {
			onWebAccess(account, password);
			return;
		}
	}
	
	private boolean onWebAccess(String accountname, String password) {
		Account account = null;
		account = Account.load(accountname);
		if(account == null) {
			return false;
		}
		
		if(account.validatePassword(accountname, password)) {
			authToken = MJMyUserTokenFactory.createAuthToken(MJMyUserTokenFactory.createAppToken(), account.getName(), account.get_Password());
			uInfo = MJMyUserGroup.group().put(authToken, account.getName(), account.getAccessLevel(), request.get_remote_address_string());
			onIngameLogin = true;
			return true;
		}
		return false;
	}
	
	private boolean onIngameAccess(String characterName) {
		if(MJString.isNullOrEmpty(characterName)) {
			return false;
		}
		
		L1PcInstance pc = L1World.getInstance().getPlayer(characterName);
		if(pc == null || pc.getNetConnection() == null) {
			return false;
		}
		
		Account account = pc.getAccount();
		if(account == null) {
			return false;
		}
		
		authToken = MJMyUserTokenFactory.createAuthToken(MJMyUserTokenFactory.createAppToken(), account.getName(), account.get_Password());
		uInfo = MJMyUserGroup.group().put(authToken, account.getName(), account.getAccessLevel(), pc.getNetConnection().getHostname());
		onIngameLogin = true;
		return true;
	}
	
	public MJHttpRequest request(){
		return request;
	}
	
	public MJMyUserInfo userInfo(){
		return uInfo;
	}
	
	public String authToken(){
		return authToken;
	}
	
	public boolean onIngameLogin() {
		return onIngameLogin;
	}
	
	public boolean loggedIn(){
		return uInfo != null && request().get_remote_address_string().equalsIgnoreCase(uInfo.address());
	}
	
	public boolean gm(){
		return loggedIn() && uInfo.accessLevel() == MJMyResource.construct().auth().gmAccessLevel();
	}
	
	public boolean gm(Account account){
		return account != null && account.getAccessLevel() == MJMyResource.construct().auth().gmAccessLevel();
	}
	
	public boolean mobile(){
		String agent = request.headers().get(HttpHeaderNames.USER_AGENT);
		return agent.matches(".*(LG|SAMSUNG|Samsung|iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
	}
	
	public boolean desktop(){
		return !mobile();
	}
	
	public abstract MJMyModel viewModel() throws MJHttpClosedException;
	
	public MJMyModel newViewModel() throws MJHttpClosedException {
		MJMyModel model = viewModel();
		if(onIngameLogin()) {
			model.addHeaderSetter(MJMyUserTokenFactory.loginSetter(authToken()));
		}
		return model;
	}
	
	public boolean containsContentType(String containsContentType){
		String contentType = request().headers().get(HttpHeaderNames.CONTENT_TYPE);
		if(MJString.isNullOrEmpty(contentType)){
			return false;
		}
		return contentType.length() >= containsContentType.length() && contentType.contains(containsContentType);
	}
	
	public boolean emptyReferer(){
		return MJString.isNullOrEmpty(request().headers().get(HttpHeaderNames.REFERER));
	}
	
	public boolean get(){
		return request().method() == HttpMethod.GET;
	}
	
	public boolean post(){
		return request().method() == HttpMethod.POST;		
	}
	
	protected MJMyModel needLogin() throws MJHttpClosedException{
//		if((request != null && MJWebProtectService.service().acceptOutsideLogin(request.get_remote_address_string()))) {
		if (request != null && !MJNSDenialAddress.getInstance().is_denials_address(request.get_remote_address_string())) {
			if (!MJWebProtectService.service().denialOutsideLogin(request.get_remote_address_string())) {
				MJMonitorCacheModel<String> model = MJMonitorCacheProvider.monitorCache().getContent(MJMyResource.construct().auth().authUri());
				if(model == null){
					return MJMyModel.notFound(request());
				}
				String doc = model.cacheContent();
				ArrayList<MJKeyValuePair<String, String>> params = new ArrayList<MJKeyValuePair<String, String>>(1);
				params.add(new MJKeyValuePair<String, String>("{APP_TOKEN}", MJMyUserTokenFactory.createAppToken()));
				return new MJMyHtmlModel(request(), MJString.replace(doc, params), null);
			}
		}
		throw new MJHttpClosedException();
	}
	
	protected MJMyModel redirect(String uri){
		return MJMyModel.redirect(request(), uri);
	}
	
	protected MJMyModel back(String message){
		return MJMyModel.back(request(), message);
	}
	
	protected MJMyModel notFound(){
		return MJMyModel.notFound(request());
	}
}
