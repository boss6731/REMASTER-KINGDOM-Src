package l1j.server.MJWebServer.Dispatcher.my.service.gmtools;

import l1j.server.MJWebServer.Dispatcher.my.service.mapview.MJMyMapViewService;
import l1j.server.MJWebServer.ws.MJWebSockRequest;
import l1j.server.MJWebServer.ws.MJWebSockServerProvider;
import l1j.server.MJWebServer.ws.protocol.MJWebSockRecvModel;

public class MJMyGmRecvModel extends MJWebSockRecvModel{
	
	private String command;
	private String subCommand;
	private String param;
	@Override
	public void callback(MJWebSockRequest request) {
		MJMyGmUserInfo uInfo = request.attr(MJMyGmExchangeHandler.gmUserKey).get();
		if(uInfo == null || uInfo.request() == null){
			request.close();
			return;
		}
		
		switch(command){
		case "mapview":		
			MJMyMapViewService.service().onCommand(subCommand, param, uInfo);
			break;
		default:
			MJWebSockServerProvider.provider().print(request, String.format("invalid MJMyGmRecvModel command : %s(%s), param : %s. connection close.", command, subCommand));
			request.close();
			break;
		}
	}
}
