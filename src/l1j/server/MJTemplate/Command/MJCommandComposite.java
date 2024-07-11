package l1j.server.MJTemplate.Command;

import java.util.LinkedList;
import java.util.List;

import l1j.server.MJTemplate.Exceptions.MJCommandArgsIndexException;

public class MJCommandComposite implements MJCommandEx{
	private static final int COMMAND_LINE_LENGTH = 24;
	
	private String commandKey;
	private boolean skippedHeader;
	private List<MJCommandEx> composite;
	public MJCommandComposite(String commandKey){
		this(commandKey, false);
	}
	
	MJCommandComposite(String commandKey, boolean skippedHeader){
		this.commandKey = commandKey;
		this.skippedHeader = skippedHeader;
		this.composite = new LinkedList<>();
	}
	
	public MJCommandComposite append(MJCommandEx command){
		composite.add(command);
		return this;
	}
	
	@Override
	public boolean execute(MJCommandArgs args) {
		try {
			String executorKey = args.nextString();
			for(MJCommandEx command : composite){
				if(command.commandKey().equalsIgnoreCase(executorKey)){
					command.execute(args);
					return true;
				}
			}
		} catch (MJCommandArgsIndexException e) {}
		if(!skippedHeader) {
			printHelpText(args);
		}
		return false;
	}
	
	@Override
	public String commandKey() {
		return commandKey;
	}

	private void printHelpText(MJCommandArgs args){
		StringBuilder sb = new StringBuilder();
		sb.append(".").append(commandKey()).append("\r\n");
		int length = 0;
		for(MJCommandEx command : composite){
			String commandKey = command.commandKey();
			if(length > 0 && length + commandKey.length() > COMMAND_LINE_LENGTH){
				sb.append("\r\n");
				length = 0;
			}
			sb.append(".").append(commandKey).append(" ");
			length += commandKey.length();
		}
		args.notify(sb.toString());
	}
}
