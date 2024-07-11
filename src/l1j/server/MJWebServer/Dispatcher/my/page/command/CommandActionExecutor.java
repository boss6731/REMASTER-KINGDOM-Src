package l1j.server.MJWebServer.Dispatcher.my.page.command;

import l1j.server.MJWebServer.Dispatcher.my.page.command.MJMyPageGmCommandModel.MyGmCommandDataModel;
import l1j.server.server.GMCommands;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

abstract class CommandActionExecutor implements MJMyPageGmCommand{
	private static final CommandActionExecutor chatPause = new CommandActionExecutor(){
		@Override
		protected String command(L1PcInstance pc) {
			return String.format("채금 %s 10", pc.getName());
		}
	};
	
	private static final CommandActionExecutor banned = new CommandActionExecutor(){
		@Override
		protected String command(L1PcInstance pc) {
			return String.format("영구추방 %s 1", pc.getName());
		}		
	};
	
	private static final CommandActionExecutor wideBanned = new CommandActionExecutor(){
		@Override
		protected String command(L1PcInstance pc) {
			return String.format("광역추방 %s 1", pc.getName());
		}
	};
	
	
	
	static CommandActionExecutor chatPause(){
		return chatPause;
	}
	
	static CommandActionExecutor banned(){
		return banned;
	}
	
	static CommandActionExecutor wideBanned(){
		return wideBanned;
	}
	
	@Override
	public MJMyPageGmCommandModel execute(String command, String targetAccount, String targetCharacter, String parameter0) {
		L1PcInstance pc = L1World.getInstance().getPlayer(targetCharacter);
		if(pc == null){
			return new MJMyPageGmCommandModel(String.format("%s 캐릭터를 찾을 수 없습니다.", targetCharacter)); 
		}
		MJMyPageGmCommandModel model = new MJMyPageGmCommandModel();
		model.title = String.format("%s 인벤토리 정보", targetCharacter);
		model.dataItems.add(new MyGmCommandDataModel("effect", "캐릭터", targetCharacter));
		model.dataItems.add(new MyGmCommandDataModel("desc", "계정", targetAccount));
		MJGmCommandDelegate delegate = new MJGmCommandDelegate(model);
		GMCommands.getInstance().handleCommands(delegate, command(pc));
		model.dataItems.add(new MyGmCommandDataModel("desc", "알림", "명령어 실행을 완료했습니다."));
		return model;
	}
	
	protected abstract String command(L1PcInstance pc);
}
