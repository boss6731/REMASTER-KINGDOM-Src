package l1j.server.MJWebServer.Dispatcher.my.service.pledge;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.MJMyServiceHelper;

public class MJMyPledgeService {
	private static final int COUNT_PER_PAGE = 10;
	private static final MJMyPledgeService service = new MJMyPledgeService();
	public static MJMyPledgeService service(){
		return service;
	}
	
	
	private MJMyPledgeService(){
	}

	private int numOfPledge0(String query){
		return MJMyPledgeProvider.provider().numOfPledge(query);
	}
	
	private List<MJMyPledgeInfoModel> selectPledge0(String query, MJMyPledgeCategory category, int page, int countPerPage){
		return MJMyPledgeProvider.provider().selectPledge(query, category, page, countPerPage);
	}
	
	public MJMyPledgeResult selectPledge(String query, MJMyPledgeCategory category, int page){
		MJMyPledgeResult result = new MJMyPledgeResult();
		result.items = selectPledge0(query, category, page, COUNT_PER_PAGE);
		result.totalCount = page != 1 || result.items.size() >= COUNT_PER_PAGE ? numOfPledge0(query) : result.items.size();
		result.totalPage = MJMyServiceHelper.calculateTotalPage(result.totalCount, COUNT_PER_PAGE);
		result.countPerPage = COUNT_PER_PAGE;
		return result;
	}
}
