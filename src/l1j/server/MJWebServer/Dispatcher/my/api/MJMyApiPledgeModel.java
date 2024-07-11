package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.service.pledge.MJMyPledgeInfoModel;

class MJMyApiPledgeModel extends MJMyApiModel{
	String query;
	int category;
	MJMyPageNavigation navigation;
	List<MJMyPledgeInfoModel> pledgeList;
	MJMyApiPledgeModel() {
		super();
		navigation = new MJMyPageNavigation();
		query = MJString.EmptyString;
		category = 0;
	}
}
