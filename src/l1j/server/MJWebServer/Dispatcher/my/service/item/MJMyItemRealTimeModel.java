package l1j.server.MJWebServer.Dispatcher.my.service.item;

import l1j.server.MJTemplate.Keyword.MJKeywordRankModel;

public class MJMyItemRealTimeModel {
	static MJMyItemRealTimeModel newModel(MJKeywordRankModel rankModel){
		MJMyItemRealTimeModel model = new MJMyItemRealTimeModel();
		model.display = rankModel.keyword();
		model.rankFluctuations = rankModel.fluctuations();
		model.isNew = rankModel.isNew();
		return model;
	}
	
	String display;
	int rankFluctuations;
	boolean isNew;
	MJMyItemRealTimeModel(){
	}
	
	public String display(){
		return display;
	}
	
	public int rankFluctuations(){
		return rankFluctuations;
	}
	
	public boolean isNew(){
		return isNew;
	}
}
