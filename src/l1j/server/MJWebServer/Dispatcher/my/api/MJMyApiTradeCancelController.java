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
			return failModel("�� �� ���� �ŷ��Դϴ�.");
		}

		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return cancel("��ǥ�� ������ ĳ���Ͱ� ���� ��Ұ� �Ұ����մϴ�.");
		}
		L1PcInstance pc = L1World.getInstance().getPlayer(representative);
		if(pc == null){
			return cancel(String.format("���忡 �������� ���� ��ǥ ĳ���ͷδ�\r\n��Ұ� �Ұ����մϴ�.\r\n(ĳ���͸�: %s)", representative));
		}
		
		MJMyTradeShopModel shop = MJMyShopService.service().selectTradeShopModel(tradeNo);
		if(shop == null){
			return cancel("�ش� �ŷ��� ���� �̿��� �� �����ϴ�. ��� �� �õ����ּ���.");
		}
		try{
			return cancelInternal(pc, shop);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			MJMyShopService.service().releaseTradeShopModel(tradeNo);
		}
		return cancel("��ְ� �߻��߽��ϴ�. ��� �� �̿����ּ���.");
	}
	
	private MJMyJsonModel cancelInternal(L1PcInstance pc, MJMyTradeShopModel shop){
		switch(shop.completed()){
		case CANCELED:
			return cancel("�̹� ��ҵ� �ŷ��Դϴ�.");
		case COMPLETE:
			return cancel("�̹� �Ϸ�� �ŷ��Դϴ�.");
		case ALIVE:
			if(!shop.onCancel(userInfo().account())){
				System.out.println(String.format("%s(%s) ��� ���� .���� Ȯ��(�ŷ���ȣ:%d)", pc.getName(), userInfo().account(), tradeNo));
				return cancel("��ҿ� �����߽��ϴ�.");
			}
			
			shop.toItem(pc);
			MJNCoinExecutor.do_write_letter(
				pc.getName(), 
				MJTimesFormatter.BASIC.toString(System.currentTimeMillis()), 
//				String.format("[��ҿϷ�] %s(%d)", shop.itemModel().itemName(), shop.itemModel().quantity()), 
				String.format("[�ǸſϷ�] �ŷ��� ��ǰ"),
				String.format("%s\n�հ�:%,d", shop.displayPart(), shop.itemModel().price())
				);
			return cancel("���������� ��ҵǾ����ϴ�.");
		}
		return cancel("�� �� ���� �ŷ��Դϴ�.");
	}
	
	private MJMyJsonModel cancel(String message){
		MJMyApiTradeCancelModel model = new MJMyApiTradeCancelModel();
		model.code = MJMyApiModel.SUCCESS;
		model.tradeNo = tradeNo;
		model.cancelMessage = message;
		return new MJMyJsonModel(request(), model, null);
	}
}