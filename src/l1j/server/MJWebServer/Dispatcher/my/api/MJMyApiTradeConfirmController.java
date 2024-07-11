package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import MJNCoinSystem.MJNCoinSettings;
import MJNCoinSystem.Commands.MJNCoinExecutor;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJTimesFormatter;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.Account;
import l1j.server.server.GMCommands;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ManagerInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_PacketBox;

class MJMyApiTradeConfirmController extends MJMyApiController {
	
	private int tradeNo;
	MJMyApiTradeConfirmController(MJHttpRequest request) {
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
			return confirm("대표로 설정된 캐릭터가 없어 구매가 불가능합니다.");
		}
		L1PcInstance pc = L1World.getInstance().getPlayer(representative);
		if(pc == null){
			return confirm(String.format("월드에 접속하지 않은 대표 캐릭터로는\r\n거래가 불가능합니다.\r\n(캐릭터명: %s)", representative));
		}
		
		MJMyTradeShopModel shop = MJMyShopService.service().selectTradeShopModel(tradeNo);
		if(shop == null){			
			return confirm("해당 거래는 지금 이용할 수 없습니다. 잠시 후 시도해주세요.");
		}
		
		try{
			return confirmInternal(pc, shop);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			MJMyShopService.service().releaseTradeShopModel(tradeNo);
		}
		return confirm("장애가 발생했습니다. 잠시 후 이용해주세요.");
	}
	
	private MJMyJsonModel confirmInternal(L1PcInstance pc, MJMyTradeShopModel shop){
		switch(shop.completed()){
		case CANCELED:
			return confirm("이미 취소된 거래입니다.");
		case COMPLETE:
			return confirm("이미 완료된 거래입니다.");
		case ALIVE:
			Account account = pc.getAccount();
			if(account == null){
				return confirm("계정 정보 로딩에 실패했습니다.");				
			}

			int confirmPrice = shop.itemModel().price();
			if(account.Ncoin_point < confirmPrice){
				return confirm("N코인이 부족합니다.");
			}
			account.Ncoin_point -= confirmPrice;
			account.updateNcoin();
			double commission = 0D;
			int fee = 0;
			if(shop.itemModel().itemId() == L1ItemId.ADENA){
				commission = pc.isGm() ? MJNCoinSettings.GM_COMMISSION : MJNCoinSettings.USER_COMMISSION;
			}
			if(commission > 0){
				fee = (int) Math.ceil(confirmPrice * commission);
				confirmPrice -= fee;
			}
			if(!shop.onBuy(pc, request().get_remote_address_string(), commission)){
				account.Ncoin_point += shop.itemModel().price();
				account.updateNcoin();
				System.out.println(String.format("%s(%s) 거래 실패 .복구 확인(%,d코인)", pc.getName(), account.getName(), shop.itemModel().price()));
				return confirm("거래에 실패했습니다.");
			}

			shop.toItem(pc);
			String completeDate = MJTimesFormatter.BASIC.toString(shop.consumerModel().actionMillis());
			GMCommands.increaseNcoinshop(L1ManagerInstance.getInstance(), shop.registeredModel().characterName(), confirmPrice, shop);
			MJNCoinExecutor.do_write_letter(
					shop.registeredModel().characterName(),
					completeDate,
//					String.format("[판매완료] %s", shop.itemModel().itemName()),
					String.format("[판매완료] 거래소 물품"),
					String.format("N코인 입금액:%,d\n적용수수료:%,d(%d%%)\n\n감사합니다. 또 이용해주세요.", confirmPrice, fee, (int)(commission * 100)));
			MJNCoinExecutor.do_write_letter(
					pc.getName(), 
					completeDate, 
//					String.format("[구매완료] %s", shop.itemModel().itemName()),
					String.format("[구매완료] 거래소 물품"),
					String.format("%s\n합계:%,d\n\n구매한 아이템은 부가 아이템 창고에서 확인해주세요.\n감사합니다.", shop.displayPart(), shop.itemModel().price()));
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aG앱센터(CTRL+Z) 거래소에서 물품을 구매하여 N코인이 차감 되었습니다."));
					pc.sendPackets(String.format("\\aG거래소에서 물품을 구매하여 N코인 (%,d)원이 차감 되었습니다.", shop.itemModel().price()));
			
			return confirm("거래가 완료 되었습니다.", shop.itemModel().price(), account.Ncoin_point);
		}
		return confirm("알 수 없는 거래입니다.");
	}
	
	private MJMyJsonModel confirm(String message){
		MJMyApiTradeConfirmModel model = new MJMyApiTradeConfirmModel();
		model.code = MJMyApiModel.SUCCESS;
		model.tradeNo = tradeNo;
		model.confirmMessage = message;
		return new MJMyJsonModel(request(), model, null);
	}
	
	private MJMyJsonModel confirm(String message, int useCoin, int remainCoin){
		MJMyApiTradeConfirmModel model = new MJMyApiTradeConfirmModel();
		model.code = MJMyApiModel.SUCCESS;
		model.confirmMessage = message;
		model.tradeNo = tradeNo;
		model.useCoin = useCoin;
		model.remainCoin = remainCoin;
		return new MJMyJsonModel(request(), model, null);
	}
}