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
			return back("�߸��� �����Դϴ�.");
		}
		if(MJString.isNullOrEmpty(displayPart)){
			return failModel(String.format("invalid parameters. displayPart:%s", displayPart));	
		}
		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return failModel("��ǥ�� ������ ĳ���Ͱ� ���� ��ǰ ����� �Ұ����մϴ�.");
		}
		L1PcInstance pc = L1World.getInstance().getPlayer(representative);
		if(pc == null){
			return failModel(String.format("���忡 �������� ���� ��ǥ ĳ���ͷδ�\r\n��ǰ ����� �Ұ����մϴ�.\r\n(ĳ���͸�: %s)", representative));
		}
		
		L1ItemInstance item = pc.getInventory().findItemObjId(objectId);
		if(item == null){
			return back("�κ��丮���� ����� �������� ã�� ���߽��ϴ�.");			
		}
		int quantityUnit = quantity;
		int pricePerUnit = price;
		if(item.getItemId() == L1ItemId.ADENA){
			if(quantity > MJNCoinSettings.ADENA_GENERATE_MAX){		
				return back("�߸��� �Ƶ� �����Դϴ�.");
			}
			if(price < MJNCoinSettings.ADENA_MARKET_PRICE){
				return back("�߸��� �Ƶ� �����Դϴ�.");
			}
			quantity *= MJNCoinSettings.ADENA_GENERATE_UNIT;
			
		}
		price *= quantityUnit;
		if(item.getCount() < quantity || quantity <= 0 || quantity > 2000000000){			
			return back("�߸��� �����Դϴ�.");			
		}
		if(!item.isStackable() && quantity > 1){
			return back("�߸��� �����Դϴ�.");
		}
		ItemTradableStatus status = L1ItemInstance.tradableItem(pc, item);
		if(status != ItemTradableStatus.tradable){
			return back("�ŷ� �Ұ����� �������Դϴ�.");			
		}
		
		quantity = pc.getInventory().removeItem(item, quantity);
		if(quantity <= 0){
			System.out.println(String.format("%s(%s) �ŷ� ���� ���� �߻�. �����ǽ�", pc.getName(), pc.getAccountName()));
			return back("�߸��� �����Դϴ�.");
		}
		MJMyShopService.service().newTradeModel(pc, item, request().get_remote_address_string(), displayPart, subject, quantity, price, pricePerUnit);
		return redirect("/my/trade-me");
	}
}