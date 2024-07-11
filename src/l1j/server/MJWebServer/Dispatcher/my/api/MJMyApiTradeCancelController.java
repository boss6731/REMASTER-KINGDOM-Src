package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import MJNCoinSystem.Commands.MJNCoinExecutor;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJTimesFormatter;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

class MJMyApiTradeCancelController extends MJMyApiController {
	
	private int tradeNo;
	MJMyApiTradeCancelController(MJHttpRequest request) {
		super(request);
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		
		tradeNo = MJString.tryParseInt(postDatas.get("tradeNo"), -1);
	}
	
	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(tradeNo < 0){
			return failModel("알 수 없는 거래입니다.");
		}

		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return cancel("대표로 설정된 캐릭터가 없어 취소가 불가능합니다.");
		}
		L1PcInstance pc = L1World.getInstance().getPlayer(representative);
		if(pc == null){
			return cancel(String.format("월드에 접속하지 않은 대표 캐릭터로는\r\n취소가 불가능합니다.\r\n(캐릭터명: %s)", representative));
		}
		
		MJMyTradeShopModel shop = MJMyShopService.service().selectTradeShopModel(tradeNo);
		if(shop == null){
			return cancel("해당 거래는 지금 이용할 수 없습니다. 잠시 후 시도해주세요.");
		}
		try{
			return cancelInternal(pc, shop);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			MJMyShopService.service().releaseTradeShopModel(tradeNo);
		}
		return cancel("장애가 발생했습니다. 잠시 후 이용해주세요.");
	}
	
	private MJMyJsonModel cancelInternal(L1PcInstance pc, MJMyTradeShopModel shop){
		switch(shop.completed()){
		case CANCELED:
			return cancel("이미 취소된 거래입니다.");
		case COMPLETE:
			return cancel("이미 완료된 거래입니다.");
		case ALIVE:
			if(!shop.onCancel(userInfo().account())){
				System.out.println(String.format("%s(%s) 취소 실패 .복구 확인(거래번호:%d)", pc.getName(), userInfo().account(), tradeNo));
				return cancel("취소에 실패했습니다.");
			}
			
			shop.toItem(pc);
			MJNCoinExecutor.do_write_letter(
				pc.getName(), 
				MJTimesFormatter.BASIC.toString(System.currentTimeMillis()), 
//				String.format("[취소완료] %s(%d)", shop.itemModel().itemName(), shop.itemModel().quantity()), 
				String.format("[판매완료] 거래소 물품"),
				String.format("%s\n합계:%,d", shop.displayPart(), shop.itemModel().price())
				);
			return cancel("성공적으로 취소되었습니다.");
		}
		return cancel("알 수 없는 거래입니다.");
	}
	
	private MJMyJsonModel cancel(String message){
		MJMyApiTradeCancelModel model = new MJMyApiTradeCancelModel();
		model.code = MJMyApiModel.SUCCESS;
		model.tradeNo = tradeNo;
		model.cancelMessage = message;
		return new MJMyJsonModel(request(), model, null);
	}
}