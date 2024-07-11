package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.ArrayList;
import java.util.List;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.api.MJMyApiCharDetailModel.EquipmentInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharDetailInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvItemInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharInvService;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharService;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.datatables.ExpTable;

class MJMyApiCharDetailController extends MJMyApiController{

	MJMyApiCharDetailController(MJHttpRequest request) {
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
			return failModel("설정된 대표 캐릭터가 없습니다.");
		}
		
		MJMyCharDetailInfo cInfo = MJMyCharService.service().fromCharacterNameDetail(representative);
		if(cInfo == null){		
			return failModel(String.format("캐릭터 정보를 불러 올 수 없습니다. %s", representative));
		}
		
		MJMyApiCharDetailModel model = new MJMyApiCharDetailModel();
		model.code = MJMyApiModel.SUCCESS;
		model.pledge = cInfo.pledge;
		model.birth = cInfo.birth;
		model.login = cInfo.login;
		model.level = cInfo.level;
		model.exp = ExpTable.getExpPercentagedouble(model.level, (int)cInfo.exp);
		model.totalRank = cInfo.totalRank;
		model.classRank = cInfo.classRank;
		model.maxHp = cInfo.maxHp;
		model.curHp = Math.min(cInfo.curHp, cInfo.maxHp);
		model.maxMp = cInfo.maxMp;
		model.curMp = Math.min(cInfo.curMp, cInfo.maxMp);
		model.str = cInfo.str;
		model.intel = cInfo.intel;
		model.dex = cInfo.dex;
		model.wis = cInfo.wis;
		model.con = cInfo.con;
		model.cha = cInfo.cha;
		model.pk = cInfo.pk;
		model.lawful = cInfo.lawful;
		List<MJMyCharInvItemInfo> items = MJMyCharInvService.service().equippedItems(cInfo.objectId);
		if(items != null && items.size() > 0){
			model.equipmentList = new ArrayList<>(items.size());
			for(MJMyCharInvItemInfo info : items){
				EquipmentInfo eInfo = new EquipmentInfo();
				eInfo.name = info.name;
				eInfo.display = info.display;
				eInfo.iconId = info.iconId;
				model.equipmentList.add(eInfo);
			}
		}
		return new MJMyJsonModel(request(), model, null);
	}

}
