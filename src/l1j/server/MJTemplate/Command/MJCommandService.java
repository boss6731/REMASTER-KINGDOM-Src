package l1j.server.MJTemplate.Command;

import l1j.server.MJTemplate.MJString;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJCommandService{
	private static final MJCommandService gmService = new MJCommandService();
	private static final MJCommandService userService = new MJCommandService();
	
	public static MJCommandService gmService(){
		return gmService;
	}
	
	public static MJCommandService userService(){
		return userService;
	}
	
	private MJCommandComposite composite;
	private MJCommandService(){
		composite = new MJCommandComposite(MJString.EmptyString, true);
	}
	
	public void append(MJCommandEx command){
		composite.append(command);
	}
	
	public boolean execute(L1PcInstance pc, String command, String param){
		return execute(new MJCommandArgs().setOwner(pc).setParam(MJString.concat(command, " ", param)));
	}
	
	private boolean execute(MJCommandArgs args) {
		return composite.execute(args);
	}
}
