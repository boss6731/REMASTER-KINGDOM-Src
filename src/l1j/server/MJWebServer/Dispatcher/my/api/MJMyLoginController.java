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
import l1j.server.server.Account;

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
			return failModel("다시 로그인 해주세요.");
		}
		if(MJString.isNullOrEmpty(accounts)){
			return failModel("계정을 입력해주세요.");
		}
		if(MJString.isNullOrEmpty(password)){
			return failModel("비밀번호를 입력해주세요.");
		}
		
		Account account = Account.load(accounts);
		if(account == null){
			return failModel("알 수 없는 계정입니다.");
		}
		if(!account.validatePassword(accounts, password)){
			return failModel("비밀번호를 확인해주세요.");
		}
		
		String authToken = MJMyUserTokenFactory.createAuthToken(appToken, accounts, password);
		if(MJString.isNullOrEmpty(authToken)){
			return failModel("시스템에 문제가 생겼습니다. 잠시 후 재시도해주세요.");
		}
		MJMyUserInfo uInfo = MJMyUserGroup.group().put(authToken, accounts, account.getAccessLevel(), request().get_remote_address_string());
		final MJMyLoginModel model = new MJMyLoginModel();
		model.code = MJMyApiModel.REDIRECT;
		model.nextLocation = MJMyResource.construct().auth().nextUri();
		model.authToken = uInfo.authToken();
		model.message = "로그인에 성공했습니다. 잠시만 기다려주세요.";
		model.newAppToken = MJString.EmptyString;
		return new MJMyJsonModel(request(), model, MJMyUserTokenFactory.loginSetter(authToken));
	}
}
