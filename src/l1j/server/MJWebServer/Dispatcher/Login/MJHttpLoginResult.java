package l1j.server.MJWebServer.Dispatcher.Login;

import l1j.server.MJTemplate.MJString;

public class MJHttpLoginResult {
	public String result_code;
	public String auth_token;
	public MJHttpLoginResult() {
		result_code = MJString.EmptyString;
		auth_token = MJString.EmptyString;
	}
}
