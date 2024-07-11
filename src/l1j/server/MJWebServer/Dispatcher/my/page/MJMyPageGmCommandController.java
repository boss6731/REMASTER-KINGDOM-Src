package l1j.server.MJWebServer.Dispatcher.my.page;

import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyHtmlModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageMapped.MJMyPageInfo;
import l1j.server.MJWebServer.Dispatcher.my.page.command.MJMyPageGmCommandInvoker;
import l1j.server.MJWebServer.Dispatcher.my.page.command.MJMyPageGmCommandModel;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMyPageGmCommandController extends MJMyPageController{
	private String command;
	private String targetAccount;
	private String targetCharacter;
	private String parameter0;
	MJMyPageGmCommandController(MJHttpRequest request, MJMyPageInfo pInfo) {
		super(request, pInfo);
		command = MJString.EmptyString;
		targetAccount = MJString.EmptyString;
		targetCharacter = MJString.EmptyString;
		parameter0 = MJString.EmptyString;
		parseParameters();
	}

	private void parseParameters(){
		MJHttpRequest request = request();
		command = request.read_parameters_at_once("command");
		targetAccount = request.read_parameters_at_once("targetAccount");
		targetCharacter = request.read_parameters_at_once("targetCharacter");
		parameter0 = request.read_parameters_at_once("parameter0");
	}
	
	@Override
	protected MJMyModel viewModelInternal() {
		if(MJString.isNullOrEmpty(command) || MJString.isNullOrEmpty(targetAccount) || MJString.isNullOrEmpty(targetCharacter)){
			return notFound();
		}
		
		MJMyPageGmCommandModel model = MJMyPageGmCommandInvoker.invoker().execute(command, targetAccount, targetCharacter, parameter0);
		if(model == null){
			return notFound();
		}
		
		String json = MJJsonUtil.toJson(model, false);
		String doc = MJString.replace(pInfo.pageText, "{POPUP_DATA}", json);
		return new MJMyHtmlModel(request(), doc, null);
	}
	
	

}
