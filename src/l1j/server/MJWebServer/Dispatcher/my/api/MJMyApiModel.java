package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJTemplate.MJJsonUtil.MJToJsonable;
import l1j.server.MJTemplate.MJString;

class MJMyApiModel extends MJToJsonable{
	static final String SUCCESS = "success";
	static final String FAILURE = "fail";
	static final String REDIRECT = "redirect";
	
	String code;
	String nextLocation;
	String message;
	MJMyApiModel(){
		code = REDIRECT;
		nextLocation = MJString.EmptyString;
		message = MJString.EmptyString;
	}
}
