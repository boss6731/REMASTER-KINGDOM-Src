package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.ArrayList;
import java.util.List;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.api.MJMyAccountModel.MJMyCharacterModel;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharSimpleInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharService;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyAccountController extends MJMyApiController{
	MJMyAccountController(MJHttpRequest request) {
		super(request);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		MJMyAccountModel model = new MJMyAccountModel();
		model.code = MJMyApiModel.SUCCESS;
		model.nextLocation = MJString.EmptyString;
		model.accountName = userInfo().account();
		model.nCoinPoint = MJMyRepresentativeService.service().selectNcoinAmount(model.accountName);
		final String representativeCharacterName = MJMyRepresentativeService.service().selectRepresentativeCharacter(model.accountName);
		List<MJMyCharSimpleInfo> characters = MJMyCharService.service().fromAccount(model.accountName);
		model.characters = new ArrayList<>(characters.size());
		boolean findRepresentative = false;
		
		for(MJMyCharSimpleInfo cInfo : characters){
			MJMyCharacterModel cmodel = new MJMyCharacterModel();
			cmodel.name = cInfo.nick();
			cmodel.level = cInfo.level();
			cmodel.characterClass = cInfo.characterClass();
			cmodel.gender = cInfo.gender();
			if(!MJString.isNullOrEmpty(representativeCharacterName) && representativeCharacterName.equalsIgnoreCase(cmodel.name)){
					cmodel.representative = true;
					findRepresentative = true;
			}else{
				cmodel.representative = false;
			}
			model.characters.add(cmodel);
		}
		if(!findRepresentative && model.characters.size() > 0){
			MJMyCharacterModel cmodel = model.characters.get(0);
			MJMyRepresentativeService.service().updateRepresentativeCharacter(model.accountName, cmodel.name);
			cmodel.representative = true;
		}		
		return new MJMyJsonModel(request(), model, null);
	}

}
