package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import MJNCoinSystem.MJNCoinSettings;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopService;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1ItemInstance.ItemTradableStatus;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.Instance.L1PcInstance;

class MJMyApiTradeRegSellController extends MJMyApiController {
	private int objectId;
	private int quantity;
	private int price;
	private String subject;
	private String displayPart;
	MJMyApiTradeRegSellController(MJHttpRequest request) {
		super(request);
		
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		objectId = MJString.tryParseInt(postDatas.get("objectId"), -1);
		quantity = MJString.tryParseInt(postDatas.get("quantity"), -1);
		price = MJString.tryParseInt(postDatas.get("price"), -1);
		subject = postDatas.get("subject");
		displayPart = postDatas.get("displayPart");
	}
	
	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(objectId < 0){
			return failModel(String.format("invalid parameters. objectId:%d", objectId));
		}
		if(quantity <= 0){
			return failModel(String.format("invalid parameters. quantity:%d", quantity));
		}
		if(price < 0){
			return failModel(String.format("invalid parameters. price:%d", price));
		}
		if(MJString.isNullOrEmpty(subject) || subject.length() > 100){
			return back("잘못된 제목입니다.");
		}
		if(MJString.isNullOrEmpty(displayPart)){
			return failModel(String.format("invalid parameters. displayPart:%s", displayPart));	
		}
		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return failModel("대표로 설정된 캐릭터가 없어 물품 등록이 불가능합니다.");
		}
		L1PcInstance pc = L1World.getInstance().getPlayer(representative);
		if(pc == null){
			return failModel(String.format("월드에 접속하지 않은 대표 캐릭터로는\r\n물품 등록이 불가능합니다.\r\n(캐릭터명: %s)", representative));
		}
		
		L1ItemInstance item = pc.getInventory().findItemObjId(objectId);
		if(item == null){
			return back("인벤토리에서 등록할 아이템을 찾지 못했습니다.");			
		}
		int quantityUnit = quantity;
		int pricePerUnit = price;
		if(item.getItemId() == L1ItemId.ADENA){
			if(quantity > MJNCoinSettings.ADENA_GENERATE_MAX){		
				return back("잘못된 아덴 수량입니다.");
			}
			if(price < MJNCoinSettings.ADENA_MARKET_PRICE){
				return back("잘못된 아덴 가격입니다.");
			}
			quantity *= MJNCoinSettings.ADENA_GENERATE_UNIT;
			
		}
		price *= quantityUnit;
		if(item.getCount() < quantity || quantity <= 0 || quantity > 2000000000){			
			return back("잘못된 수량입니다.");			
		}
		if(!item.isStackable() && quantity > 1){
			return back("잘못된 수량입니다.");
		}
		ItemTradableStatus status = L1ItemInstance.tradableItem(pc, item);
		if(status != ItemTradableStatus.tradable){
			return back("거래 불가능한 아이템입니다.");			
		}
		
		quantity = pc.getInventory().removeItem(item, quantity);
		if(quantity <= 0){
			System.out.println(String.format("%s(%s) 거래 수량 문제 발생. 버그의심", pc.getName(), pc.getAccountName()));
			return back("잘못된 수량입니다.");
		}
		MJMyShopService.service().newTradeModel(pc, item, request().get_remote_address_string(), displayPart, subject, quantity, price, pricePerUnit);
		return redirect("/my/trade-me");
	}
}