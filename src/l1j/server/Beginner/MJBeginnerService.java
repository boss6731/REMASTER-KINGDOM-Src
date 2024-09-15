package l1j.server.Beginner;

import l1j.server.Beginner.Controller.MJBeginnerControllerProvider;
import l1j.server.Beginner.Model.MJBeginnerModelProvider;
import l1j.server.Beginner.View.MJBeginnerViewProvider;

public class MJBeginnerService {

	private static final MJBeginnerService service = new MJBeginnerService();
	public static MJBeginnerService service(){
		return service;
	}
	
	public static void onApplicationStartup(){
		service.initialize();
	}

	private MJBeginnerService(){
	}
	
	private void initialize(){
		MJBeginnerModelProvider.provider().initialize(this);
		MJBeginnerControllerProvider.provider().initialize(this);
		MJBeginnerViewProvider.provider().initialize(this);
	}
}
