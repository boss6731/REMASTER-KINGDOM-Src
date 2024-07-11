package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.api.MJMyApiInventoryModel.ItemInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvItemInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvService;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharService;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharSimpleInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiInventoryController extends MJMyApiController{
	private String categoryName;
	MJMyApiInventoryController(MJHttpRequest request) {
		super(request);
		categoryName = MJString.EmptyString;
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		
		categoryName = postDatas.get("categoryName");
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(MJString.isNullOrEmpty(categoryName)){
			return failModel("알 수 없는 타입 입니다.");
		}
		switch(categoryName){
		case "inventory":
			return inventory();
		case "account":
			return account();
		case "character":
			return character();
		}
		return failModel("알 수 없는 타입 입니다.");
	}
	
	private void convertItems(MJMyApiInventoryModel model, List<MJMyCharInvItemInfo> items){
		if(items == null || items.size() <= 0){
			model.items = Collections.emptyList();
			return;
		}
		for(MJMyCharInvItemInfo item : items){
			ItemInfo info = new ItemInfo();
			info.name = item.name;
			info.display = item.display;
			info.iconId = item.iconId;
			info.count = item.count;
			info.category = item.category.name();
			model.items.add(info);
		}
		model.numOfItems = model.items.size();
	}
	
	private MJMyModel character(){
		MJMyApiInventoryModel model = new MJMyApiInventoryModel();
		model.code = MJMyApiModel.SUCCESS;
		convertItems(model, MJMyCharInvService.service().characterWarehouseItems(userInfo().account()));
		return new MJMyJsonModel(request(), model, null);
	}
	
	private MJMyModel account(){
		MJMyApiInventoryModel model = new MJMyApiInventoryModel();
		model.code = MJMyApiModel.SUCCESS;
		convertItems(model, MJMyCharInvService.service().accountWarehouseItems(userInfo().account()));
		return new MJMyJsonModel(request(), model, null);
	}
	
	private MJMyModel inventory(){
		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return failModel("설정된 대표 캐릭터가 없습니다.");
		}
		
		MJMyCharSimpleInfo cInfo = MJMyCharService.service().fromCharacterName(representative);
		if(cInfo == null){
			return failModel("캐릭터 설정을 불러오는데 실패했습니다.");
		}
		
		MJMyApiInventoryModel model = new MJMyApiInventoryModel();
		model.code = MJMyApiModel.SUCCESS;
		convertItems(model, MJMyCharInvService.service().allInventoryItems(cInfo.objectId()));
		return new MJMyJsonModel(request(), model, null);
	}

}
