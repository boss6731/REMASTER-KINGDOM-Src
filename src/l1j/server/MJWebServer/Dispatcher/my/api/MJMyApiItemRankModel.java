package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.item.MJMyItemRankModel;
import l1j.server.MJWebServer.Dispatcher.my.service.item.MJMyItemRealTimeModel;

class MJMyApiItemRankModel extends MJMyApiModel{
	List<MJMyItemRealTimeModel> realTimeKeywords;
	MJMyItemRankModel itemRankAllModel;
	MJMyItemRankModel itemRankWeaponModel;
	MJMyItemRankModel itemRankArmorModel;
	MJMyItemRankModel itemRankAccessoryModel;
	MJMyItemRankModel itemRankEtcModel;
	boolean gm;
	MJMyApiItemRankModel(){
		super();
	}
}
