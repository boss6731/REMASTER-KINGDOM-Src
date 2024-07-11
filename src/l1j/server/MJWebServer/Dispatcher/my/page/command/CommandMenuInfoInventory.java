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
			return new MJMyPageGmCommandModel(String.format("%s 캐릭터를 찾을 수 없습니다.", targetCharacter)); 
		}
		
		
		MJMyPageGmCommandModel model = new MJMyPageGmCommandModel();
		model.targetAccount = targetAccount;
		model.targetCharacterName = targetCharacter;
		model.title = String.format("%s 인벤토리 정보", targetCharacter);
		model.dataItems.add(new MyGmCommandDataModel("effect", "캐릭터", targetCharacter));
		model.dataItems.add(new MyGmCommandDataModel("desc", "계정", targetAccount));
		for(L1ItemInstance item : pc.getInventory().getItems()){
			model.dataItems.add(new MyGmCommandDataInvModel(item.getId(), "desc", item.isEquipped() ? "착용중" : "인벤토리", L1ItemInstance.to_simple_description(item), true));
		}
		return model;
	}
}