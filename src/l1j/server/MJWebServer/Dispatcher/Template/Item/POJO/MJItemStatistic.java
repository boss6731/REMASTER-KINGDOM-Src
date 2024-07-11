package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import java.util.ArrayList;

public class MJItemStatistic {
	public MJMarketPriceSummary marketPriceSummary;
	public ArrayList<MJStatisticInfo> statisticList;
	public MJStoreList storeList;
	public MJNameCodeInfo tradeType;
	public MJItemStatistic(){
		statisticList = new ArrayList<MJStatisticInfo>();
	}
	
	public MJItemStatistic(MJNameCodeInfo trade_type){
		tradeType = trade_type;
	}
}
