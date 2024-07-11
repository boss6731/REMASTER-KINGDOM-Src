package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJTemplate.MJString;

class MJMyLogOutModel extends MJMyApiModel {
	String authToken;
	MJMyLogOutModel(){
		super();
		authToken = MJString.EmptyString;
	}
}
