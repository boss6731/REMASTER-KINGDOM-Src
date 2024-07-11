package l1j.server.MJWebServer.Dispatcher.my.page.command;

import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJWebServer.Dispatcher.my.page.command.MJMyPageGmCommandModel.MyGmCommandDataModel;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharDetailInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharService;
import l1j.server.server.datatables.ExpTable;

class CommandMenuInfoNormal implements MJMyPageGmCommand{

	@Override
	public MJMyPageGmCommandModel execute(String command, String targetAccount, String targetCharacter, String parameter0) {
		MJMyCharDetailInfo cInfo = MJMyCharService.service().fromCharacterNameDetail(targetCharacter);
		if(cInfo == null){
			return new MJMyPageGmCommandModel(String.format("%s 캐릭터를 찾을 수 없습니다.", targetCharacter)); 
		}
		
		MJMyPageGmCommandModel model = new MJMyPageGmCommandModel();
		model.title = String.format("%s 정보", targetCharacter);
		model.dataItems.add(new MyGmCommandDataModel("effect", "캐릭터", targetCharacter));
		model.dataItems.add(new MyGmCommandDataModel("desc", "계정", targetAccount));
		model.dataItems.add(new MyGmCommandDataModel("desc", "오브젝트", cInfo.objectId));
		model.dataItems.add(new MyGmCommandDataModel("desc", "구분", cInfo.gm ? "운영자" : "일반유저"));
		model.dataItems.add(new MyGmCommandDataModel("desc", "레벨", String.format("Lv.%d %.2f%%", cInfo.level, ExpTable.getExpPercentagedouble(cInfo.level, (int)cInfo.exp))));
		model.dataItems.add(new MyGmCommandDataModel("desc", "성별", cInfo.gender.equals("f") ? "여" : "남"));
		model.dataItems.add(new MyGmCommandDataModel("desc", "클래스", MJEClassesType.fromInt(cInfo.characterClass).text()));
		model.dataItems.add(new MyGmCommandDataModel("desc", "혈맹", cInfo.pledge));
		model.dataItems.add(new MyGmCommandDataModel("desc", "생성일", cInfo.birth));
		model.dataItems.add(new MyGmCommandDataModel("desc", "랭킹", String.format("클래스: %d, 전체: %d", cInfo.classRank, cInfo.totalRank)));
		return model;
	}
}
