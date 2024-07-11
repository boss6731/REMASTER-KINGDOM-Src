package l1j.server.MJWebServer.Dispatcher.my.service.chat;

import java.util.StringTokenizer;

import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharSimpleInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatCommandInstance.MJMyChatCommandListener;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserExpirationListener;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserGroup;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserInfo;
import l1j.server.MJWebServer.ws.MJWebSockInactiveListener;
import l1j.server.MJWebServer.ws.MJWebSockRequest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJMyChatUserInfo implements MJMyUserExpirationListener, MJWebSockInactiveListener, MJMyChatCommandListener {
	private String uid;
	private String authToken;
	private MJMyCharSimpleInfo character;
	private String callbackName;
	private MJWebSockRequest request;
	MJMyChatUserInfo(MJMyUserInfo uInfo){
		this.authToken = uInfo.authToken();
		uInfo.registeredListener(this);
	}
	
	void uid(String uid){
		this.uid = uid;
	}
	
	public String uid(){
		return uid;
	}
	
	void character(MJMyCharSimpleInfo character){
		this.character = character;
	}
	
	public MJMyCharSimpleInfo character(){
		return character;
	}
	
	void callbackName(String callbackName){
		this.callbackName = callbackName;
	}
	
	void request(MJWebSockRequest request){
		this.request = request;
	}
	
	public String callbackName(){
		return callbackName;
	}
	
	public MJWebSockRequest request(){
		return request;
	}

	@Override
	public void onExpiration(MJMyUserInfo uInfo) {
		if(request != null){
			request.close();
		}
		MJMyChatCommandInstance.instance().removeCommandListener(this);
	}

	@Override
	public void onInactive(MJWebSockRequest request) {
		MJMyChatService.service().onInactive(this);
		MJMyChatService.service().callbackService().remove(callbackName());
		MJMyUserInfo uInfo = MJMyUserGroup.group().get(authToken);
		MJMyChatCommandInstance.instance().removeCommandListener(this);
		if(uInfo != null){
			uInfo.unregisteredListener(this);
		}
	}

	private StringBuilder commandsQ = new StringBuilder();
	@Override
	public void appendCommandResult(String s) {
		MJMyCharSimpleInfo cInfo = character();
		if(cInfo == null || !cInfo.gm()){
			return;
		}
		
		L1PcInstance pc = L1World.getInstance().getPlayer(cInfo.nick());
		if(pc == null || !pc.isGm()){
			return;
		}
		if(s.indexOf("\r\n") == -1){
			if(commandsQ.length() > 0){
				commandsQ.append("</br>");
			}
			commandsQ.append(s);
		}else{
			StringTokenizer st = new StringTokenizer(s, "\r\n");
			while(st.hasMoreTokens()){
				commandsQ.append("</br>");
				commandsQ.append(st.nextToken());
			}
		}
	}
	
	@Override
	public void onCommandFlush(){
		if(commandsQ.length() > 0 && request() != null){
			MJMyChatRecvModel.system(request(), commandsQ.toString());			
			commandsQ = new StringBuilder(commandsQ.length());
		}
	}
}
