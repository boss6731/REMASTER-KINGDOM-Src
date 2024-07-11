package l1j.server.MJWebServer.Dispatcher.my.page.command;

import l1j.server.MJWebServer.Dispatcher.my.page.command.MJMyPageGmCommandModel.MyGmCommandDataModel;
import l1j.server.server.GMCommands;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

abstract class CommandActionExecutor implements MJMyPageGmCommand{
	private static final CommandActionExecutor chatPause = new CommandActionExecutor(){
		@Override
		protected String command(L1PcInstance pc) {
			return String.format("ä�� %s 10", pc.getName());
		}
	};
	
	private static final CommandActionExecutor banned = new CommandActionExecutor(){
		@Override
		protected String command(L1PcInstance pc) {
			return String.format("�����߹� %s 1", pc.getName());
		}		
	};
	
	private static final CommandActionExecutor wideBanned = new CommandActionExecutor(){
		@Override
		protected String command(L1PcInstance pc) {
			return String.format("�����߹� %s 1", pc.getName());
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
			return new MJMyPageGmCommandModel(String.format("%s ĳ���͸� ã�� �� �����ϴ�.", targetCharacter)); 
		}
		MJMyPageGmCommandModel model = new MJMyPageGmCommandModel();
		model.title = String.format("%s �κ��丮 ����", targetCharacter);
		model.dataItems.add(new MyGmCommandDataModel("effect", "ĳ����", targetCharacter));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", targetAccount));
		MJGmCommandDelegate delegate = new MJGmCommandDelegate(model);
		GMCommands.getInstance().handleCommands(delegate, command(pc));
		model.dataItems.add(new MyGmCommandDataModel("desc", "�˸�", "��ɾ� ������ �Ϸ��߽��ϴ�."));
		return model;
	}
	
	protected abstract String command(L1PcInstance pc);
}
