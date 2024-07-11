package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJTemplate.MJString;

class MJMyLoginModel extends MJMyApiModel{
	String newAppToken;
	String authToken;
	MJMyLoginModel(){
		super();
		newAppToken = MJString.EmptyString;
		authToken = MJString.EmptyString;
	}	
}
