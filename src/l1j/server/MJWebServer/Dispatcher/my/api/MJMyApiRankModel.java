package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.rank.MJMyRankServiceModel;

class MJMyApiRankModel extends MJMyApiModel{
	boolean service;
	int limit;
	int minLevel;
	long lastUpdateMillis;
	List<MJMyRankServiceModel> items;
	MJMyApiRankModel(){
	}
}
