package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJString;

class MJMyAccountModel extends MJMyApiModel{
	String accountName;
	int nCoinPoint;
	int logNotiCount;
	ArrayList<MJMyCharacterModel> characters;
	MJMyAccountModel(){
		super();
		accountName = MJString.EmptyString;
		logNotiCount = 0;
		logNotiCount = 1;
	}
	
	static class MJMyCharacterModel{
		String name;
		int level;
		int characterClass;
		String gender;
		boolean representative;
		MJMyCharacterModel(){
			name = MJString.EmptyString;
			level = 0;
			characterClass = 0;
			gender = "m";
			representative = false;
		}
	}
}
