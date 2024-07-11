package l1j.server.MJWebServer.Dispatcher.my.service.item;

import java.util.List;

public class MJMyItemRankModel {
	private List<MJMyItemRankInfoModel> sellList;
	private List<MJMyItemRankInfoModel> buyList;
	MJMyItemRankModel(int[] sellList, int[] buyList){
		this.sellList = MJMyItemRankInfoModel.fromItemsId(sellList);
		this.buyList = MJMyItemRankInfoModel.fromItemsId(buyList);
	}
	
	public List<MJMyItemRankInfoModel> sellList(){
		return sellList;
	}
	
	public List<MJMyItemRankInfoModel> buyList(){
		return buyList;
	}
}
