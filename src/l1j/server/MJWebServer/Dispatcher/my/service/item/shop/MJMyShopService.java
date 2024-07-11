package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel.MJMyTradeShopUserModel;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJMyShopService {
	private static final MJMyShopService service = new MJMyShopService();
	public static MJMyShopService service(){
		return service;
	}
	
	private MJMyShopTradeExcludeService mExcludeService;
	private MJMyShopService(){	
		newExcludeItems();
	}
	
	public boolean excludeItemsContains(L1ItemInstance item) {
		return mExcludeService.containsExcludeItem(item);
	}
	
	public void newExcludeItems() {
		mExcludeService = new MJMyShopTradeExcludeService(); 
	}
	
	public MJMyShopProvider<MJMyPrivateShopDetailModel> privateShopProvider(){
		return MJMyPrivateShopProvider.provider();
	}
	
	public MJMyShopProvider<MJMyTradeShopDetailModel> tradeShopProvider(){
		return MJMyTradeShopProvider.provider();
	}
	
	public MJMyTradeShopModel newTradeModel(L1PcInstance pc, L1ItemInstance item, String address, String displayPart, String subject, int quantity, int price, int pricePerUnit){
		MJMyTradeShopModel model = MJMyTradeShopModel.newModel(pc, item, address, displayPart, subject, quantity, price, pricePerUnit);
		MJMyTradeShopProvider.provider().newTradeModel(model);
		return model;
	}
	
	public MJMyTradeShopUserModel newBuyerModel(int tradeNo, L1PcInstance pc, String address, double commission){
		MJMyTradeShopUserModel model = MJMyTradeShopUserModel.newModel(pc, address);
		return model;
	}
	
	public List<MJMyTradeShopModel> selectMeList(final String account, MJMyShopTradeListType tradeListType){
		return MJMyTradeShopProvider.provider().selectMeList(account, tradeListType);
	}
	
	public MJMyShopStatisticsGmModel selectStatisticsGmModel(MJMyTradeShopCompleteCode code){
		return MJMyTradeShopProvider.provider().selectStatisticsGmModel(code);
	}
	
	private static final ConcurrentHashMap<Integer, Integer> tradeShopLock = new ConcurrentHashMap<>();
	public MJMyTradeShopModel selectTradeShopModel(final int tradeNo){
		if(tradeShopLock.putIfAbsent(tradeNo, Integer.valueOf(tradeNo)) != null){
			return null;
		}
		return MJMyTradeShopProvider.provider().selectTradeShopModel(tradeNo);
	}
	
	public void releaseTradeShopModel(final int tradeNo){
		tradeShopLock.remove(tradeNo);
	}
}
