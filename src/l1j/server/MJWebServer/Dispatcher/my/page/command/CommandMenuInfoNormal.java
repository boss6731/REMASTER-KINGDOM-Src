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
			return new MJMyPageGmCommandModel(String.format("%s ĳ���͸� ã�� �� �����ϴ�.", targetCharacter)); 
		}
		
		MJMyPageGmCommandModel model = new MJMyPageGmCommandModel();
		model.title = String.format("%s ����", targetCharacter);
		model.dataItems.add(new MyGmCommandDataModel("effect", "ĳ����", targetCharacter));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", targetAccount));
		model.dataItems.add(new MyGmCommandDataModel("desc", "������Ʈ", cInfo.objectId));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", cInfo.gm ? "���" : "�Ϲ�����"));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", String.format("Lv.%d %.2f%%", cInfo.level, ExpTable.getExpPercentagedouble(cInfo.level, (int)cInfo.exp))));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", cInfo.gender.equals("f") ? "��" : "��"));
		model.dataItems.add(new MyGmCommandDataModel("desc", "Ŭ����", MJEClassesType.fromInt(cInfo.characterClass).text()));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", cInfo.pledge));
		model.dataItems.add(new MyGmCommandDataModel("desc", "������", cInfo.birth));
		model.dataItems.add(new MyGmCommandDataModel("desc", "��ŷ", String.format("Ŭ����: %d, ��ü: %d", cInfo.classRank, cInfo.totalRank)));
		return model;
	}
}
