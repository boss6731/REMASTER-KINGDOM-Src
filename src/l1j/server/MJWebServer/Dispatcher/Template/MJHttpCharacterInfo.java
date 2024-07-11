package l1j.server.MJWebServer.Dispatcher.Template;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.MJTemplate.MJFormatter;
import l1j.server.MJTemplate.MJKeyValuePair;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.Character.MJUser;

public class MJHttpCharacterInfo {
	public static ArrayList<MJKeyValuePair<String, String>> getCharacterInfo(MJUser _user) {
		ArrayList<MJKeyValuePair<String, String>> params = new ArrayList<MJKeyValuePair<String, String>>();
		params.add(new MJKeyValuePair<String, String>("{CHARACTER_NAME}", _user.getCharName()));
		params.add(new MJKeyValuePair<String, String>("{LEVEL}", String.valueOf(_user.getLevel())));
		params.add(new MJKeyValuePair<String, String>("{NOW}", MJFormatter.get_tdouble_formatter_time()));
		params.add(new MJKeyValuePair<String, String>("{SERVER_NAME}", Config.Message.GameServerName));
		
		params.add(new MJKeyValuePair<String, String>("{PROFILE_URL}","https://wstatic-cdn.plaync.com/resource/lineage/profile/char" + _user.getClassId() + "_" + (_user.getClassId() == 0 ? "f" : "m") + ".png"));
		params.add(new MJKeyValuePair<String, String>("{NCOIN}", MJString.parse_money_string(_user.getNcoin())));
		
		return params;
	}
}
