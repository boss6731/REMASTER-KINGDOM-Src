package l1j.server.MJWebServer.Dispatcher.my.page.command;

import java.util.HashMap;

public class MJMyPageGmCommandInvoker implements MJMyPageGmCommand {
	private static final HashMap<String, MJMyPageGmCommand> commands;
	static{
		commands = new HashMap<>();
		commands.put("menu-info-normal", new CommandMenuInfoNormal());
		commands.put("menu-info-detail", new CommandMenuInfoDetail());
		commands.put("menu-info-inventory", new CommandMenuInfoInventory());
		commands.put("menu-action-chat-pause", CommandActionExecutor.chatPause());
		commands.put("menu-action-chat-ban", CommandActionExecutor.banned());
		commands.put("menu-action-chat-wide-ban", CommandActionExecutor.wideBanned());
		commands.put("info-inventory-delete", new CommandInventoryDelete());
	};
	
	private static final MJMyPageGmCommandInvoker invoker = new MJMyPageGmCommandInvoker();
	public static final MJMyPageGmCommandInvoker invoker(){
		return invoker;
	}
	
	private MJMyPageGmCommandInvoker(){}

	@Override
	public MJMyPageGmCommandModel execute(String command, String targetAccount, String targetCharacter, String parameter0) {
		MJMyPageGmCommand gmCommand = commands.get(command);
		return gmCommand == null ? null : gmCommand.execute(command, targetAccount, targetCharacter, parameter0);
	}
}
