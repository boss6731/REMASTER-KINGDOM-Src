package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserGroup;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserInfo;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserTokenFactory;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyLoginController extends MJMyApiController{
	private String appToken;
	private String accounts;
	private String password;
	MJMyLoginController(MJHttpRequest request){
		super(request);
		parseParameters();
	}

	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		appToken = postDatas.get("app_token");
		accounts = postDatas.get("accounts");
		password = postDatas.get("password");
	}
	
	@Override
	protected MJMyModel failModel(String message){
		MJMyLoginModel model = new MJMyLoginModel();
		model.code = MJMyApiModel.FAILURE;
		model.message = message;
		model.newAppToken = MJMyUserTokenFactory.createAppToken();
		return new MJMyJsonModel(request(), model, null);
	}
	
	@Override
	protected boolean isNeedLogin() {
		return false;
	}

	@Override
	protected MJMyModel responseModel() {
		if(MJString.isNullOrEmpty(appToken)){
			return failModel("�ٽ� �α��� ���ּ���.");
		}
		if(MJString.isNullOrEmpty(accounts)){
			return failModel("������ �Է����ּ���.");
		}
		if(MJString.isNullOrEmpty(password)){
			return failModel("��й�ȣ�� �Է����ּ���.");
		}
		
		Account account = Account.load(accounts);
		if(account == null){
			return failModel("�� �� ���� �����Դϴ�.");
		}
		if(!account.validatePassword(accounts, password)){
			return failModel("��й�ȣ�� Ȯ�����ּ���.");
		}
		
		String authToken = MJMyUserTokenFactory.createAuthToken(appToken, accounts, password);
		if(MJString.isNullOrEmpty(authToken)){
			return failModel("�ý��ۿ� ������ ������ϴ�. ��� �� ��õ����ּ���.");
		}
		MJMyUserInfo uInfo = MJMyUserGroup.group().put(authToken, accounts, account.getAccessLevel(), request().get_remote_address_string());
		final MJMyLoginModel model = new MJMyLoginModel();
		model.code = MJMyApiModel.REDIRECT;
		model.nextLocation = MJMyResource.construct().auth().nextUri();
		model.authToken = uInfo.authToken();
		model.message = "�α��ο� �����߽��ϴ�. ��ø� ��ٷ��ּ���.";
		model.newAppToken = MJString.EmptyString;
		return new MJMyJsonModel(request(), model, MJMyUserTokenFactory.loginSetter(authToken));
	}
}
