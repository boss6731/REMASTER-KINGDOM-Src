package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.api.MJMyApiTradeRegModel.MJMyApiTradeRegItemModel;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvItemInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvService;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopService;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1ItemInstance.ItemTradableStatus;
import l1j.server.server.model.Instance.L1PcInstance;

class MJMyApiTradeRegController extends MJMyApiController {
	private static Matcher<L1ItemInstance> tradableMatcher(final L1PcInstance pc){
		return new Matcher<L1ItemInstance>(){
			@Override
			public boolean matches(L1ItemInstance t) {
				return L1ItemInstance.tradableItem(pc, t) == ItemTradableStatus.tradable && !MJMyShopService.service().excludeItemsContains(t);
			}
		};
	}
	
	MJMyApiTradeRegController(MJHttpRequest request) {
		super(request);
	}
	
	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return failModel("��ǥ�� ������ ĳ���Ͱ� ���� ��ǰ ����� �Ұ����մϴ�.");
		}
		
		L1PcInstance pc = L1World.getInstance().getPlayer(representative);
		if(pc == null){
			return failModel(String.format("���忡 �������� ���� ��ǥ ĳ���ͷδ�\r\n��ǰ ����� �Ұ����մϴ�.\r\n(ĳ���͸�: %s)", representative));
		}
		
		List<MJMyCharInvItemInfo> items = MJMyCharInvService.service().matchesInventoryItems(pc, tradableMatcher(pc));		
		MJMyApiTradeRegModel model = new MJMyApiTradeRegModel();
		model.code = MJMyApiModel.SUCCESS;
		model.characterName = pc.getName();
		model.totalInventoryItemsCount = pc.getInventory().getItems().size();
		model.tradableItemsCount = items.size();
		for(MJMyCharInvItemInfo item : items){
			MJMyApiTradeRegItemModel itemModel = MJMyApiTradeRegItemModel.newModel(item);
			model.tradableItems.add(itemModel);
		}
		return new MJMyJsonModel(request(), model, null);
	}
}