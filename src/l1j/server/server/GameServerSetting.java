package l1j.server.server;

import l1j.server.Config;

public class GameServerSetting
{
	private static GameServerSetting _instance;
	
	public static GameServerSetting getInstance(){
		if (_instance == null){
			_instance = new GameServerSetting();
		}
		return _instance;
	}

	/** 伺服器管理員相關部分 1 **/
	public static boolean general = false;
	public static boolean whisper = false;
	public static boolean global = false;
	public static boolean clan = false;
	public static boolean party = false;
	public static boolean trade = false;
	public static boolean Att = false;
	public static boolean NYEvent = false;

	public static boolean ServerDown = false;

	private int maxLevel = Config.CharSettings.LimitLevel;

	public int get_maxLevel() {
		return maxLevel;
	}

	public void set_maxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	private GameServerSetting() {
	}
}
