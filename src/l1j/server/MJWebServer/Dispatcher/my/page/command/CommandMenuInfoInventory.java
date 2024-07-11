package l1j.server.MJWebServer.Dispatcher.my.page.command;

import l1j.server.MJWebServer.Dispatcher.my.page.command.MJMyPageGmCommandModel.MyGmCommandDataInvModel;
import l1j.server.MJWebServer.Dispatcher.my.page.command.MJMyPageGmCommandModel.MyGmCommandDataModel;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

class CommandMenuInfoInventory implements MJMyPageGmCommand{

	@Override
	public MJMyPageGmCommandModel execute(String command, String targetAccount, String targetCharacter, String parameter0) {
		L1PcInstance pc = L1World.getInstance().getPlayer(targetCharacter);
		if(pc == null){
			return new MJMyPageGmCommandModel(String.format("%s ĳ���͸� ã�� �� �����ϴ�.", targetCharacter)); 
		}
		
		
		MJMyPageGmCommandModel model = new MJMyPageGmCommandModel();
		model.targetAccount = targetAccount;
		model.targetCharacterName = targetCharacter;
		model.title = String.format("%s �κ��丮 ����", targetCharacter);
		model.dataItems.add(new MyGmCommandDataModel("effect", "ĳ����", targetCharacter));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", targetAccount));
		for(L1ItemInstance item : pc.getInventory().getItems()){
			model.dataItems.add(new MyGmCommandDataInvModel(item.getId(), "desc", item.isEquipped() ? "������" : "�κ��丮", L1ItemInstance.to_simple_description(item), true));
		}
		return model;
	}
}