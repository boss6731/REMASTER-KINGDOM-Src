package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmCharViewModel;

class MJMyApiGmToolLoginModel extends MJMyApiModel{
	boolean service;
	int port;
	String uri;
	String uid;
	String callbackName;
	List<MJMyGmCharViewModel> characters;
	MJMyApiGmToolLoginModel(){
		super();
	}
}
