package l1j.server.MJWebServer.Dispatcher.my.service.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import l1j.server.MJTemplate.Keyword.MJKeywordRankModel;
import l1j.server.MJTemplate.Keyword.MJKeywordView;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyRecommandItem;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;

public class MJMyItemService {
	
	private static final MJMyItemService service = new MJMyItemService();
	public static MJMyItemService service(){
		return service;
	}
	
	private MJMyItemRankModel itemRankAllModel;
	private MJMyItemRankModel itemRankWeaponModel;
	private MJMyItemRankModel itemRankArmorModel;
	private MJMyItemRankModel itemRankAccessoryModel;
	private MJMyItemRankModel itemRankEtcModel;
	
	private MJMyItemService(){
	}
	
	public MJMyItemRankModel itemRankAllModel(){
		return itemRankAllModel;
	}
	
	public MJMyItemRankModel itemRankWeaponModel(){
		return itemRankWeaponModel;
	}

	public MJMyItemRankModel itemRankArmorModel(){
		return itemRankArmorModel;
	}

	public MJMyItemRankModel itemRankAccessoryModel(){
		return itemRankAccessoryModel;
	}

	public MJMyItemRankModel itemRankEtcModel(){
		return itemRankEtcModel;
	}
	
	public void onNewRecommands(MJMyRecommandItem recommandItem){
		itemRankAllModel = new MJMyItemRankModel(recommandItem.itemRankAllSell, recommandItem.itemRankAllBuy);
		itemRankWeaponModel = new MJMyItemRankModel(recommandItem.itemRankWeaponSell, recommandItem.itemRankWeaponBuy);
		itemRankArmorModel = new MJMyItemRankModel(recommandItem.itemRankArmorSell, recommandItem.itemRankArmorBuy);
		itemRankAccessoryModel = new MJMyItemRankModel(recommandItem.itemRankAccessorySell, recommandItem.itemRankAccessoryBuy);
		itemRankEtcModel = new MJMyItemRankModel(recommandItem.itemRankEtcSell, recommandItem.itemRankEtcBuy);
	}
	
	public List<MJMyItemRealTimeModel> realTimeKeywords(){
		MJKeywordView view = MJMyResource.realTimeKeywordProvider().view();
		if(view == null){
			return Collections.emptyList();
		}
		
		List<MJKeywordRankModel> models = view.models();
		if(models == null || models.isEmpty()){
			return Collections.emptyList();
		}
		
		List<MJMyItemRealTimeModel> realTimeModels = new ArrayList<>(models.size());
		for(MJKeywordRankModel rankModel : models){
			MJMyItemRealTimeModel model = MJMyItemRealTimeModel.newModel(rankModel);
			realTimeModels.add(model);
		}
		return realTimeModels;
	}
}
