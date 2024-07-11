package l1j.server.Beginner.Controller;

import l1j.server.Beginner.MJBeginnerService;

public class MJBeginnerControllerProvider {
	private static final MJBeginnerControllerProvider provider = new MJBeginnerControllerProvider();
	public static MJBeginnerControllerProvider provider(){
		return provider;
	}
	
	private MJBeginnerClientController clientController;
	public MJBeginnerControllerProvider(){
	}
	
	public MJBeginnerClientController clientController(){
		return clientController;
	}
	
	public void initialize(MJBeginnerService service){
	}
	
	public void onDevelopModeChanged(boolean developMode){
		clientController = developMode ? new MJBeginnerClientController.MJBeginnerDeveloperController() : new MJBeginnerClientController();
	}
}
