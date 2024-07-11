package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.ArrayList;
import java.util.List;

import l1j.server.MJTemplate.MJString;

class MJMySiegeModel extends MJMyApiModel {
	List<SiegeInfo> siegeCastleList;
	
	MJMySiegeModel(){
		super();
		siegeCastleList = new ArrayList<>();
	}
	
	static class SiegeInfo{
		int castleId;
		String pledge;
		String pledgeMaster;
		int tax;
		int siegePoint;
		String occupyDate;
		String engName;
		String korName;
		String powerbookUrl;
		SiegeInfo(){
			castleId = 0;
			pledge = MJString.EmptyString;
			pledgeMaster = MJString.EmptyString;
			tax = 0;
			siegePoint = 0;
			occupyDate = MJString.EmptyString;
			engName = MJString.EmptyString;
			korName = MJString.EmptyString;
			powerbookUrl = MJString.EmptyString;
		}
	}
}
