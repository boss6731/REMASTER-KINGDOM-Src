package l1j.server.MJWebServer.Dispatcher.my.service.refund;

import java.util.Collections;
import java.util.List;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyServiceHelper;

public class MJMyRefundService {
	private static final MJMyRefundService service = new MJMyRefundService();
	private static final int COUNT_PER_PAGE = 20;
	public static MJMyRefundService service(){
		return service;
	}
	
	private MJMyRefundService(){}
	
	public int countPerPage(){
		return COUNT_PER_PAGE;
	}
	
	public MJMyRefundSelectResult selectItemsInfo(int page){
		return selectItemsInfo(MJMyRefundCategory.categories[0], MJMyRefundSearchColumn.all, MJString.EmptyString, page);
	}
	
	public MJMyRefundSelectResult selectItemsInfo(final MJMyRefundCategory category, final MJMyRefundSearchColumn searchColumn, final String searchName, final int page){
		return selectItemsInfo(category, searchColumn, searchName, page, COUNT_PER_PAGE);
	}
	
	public MJMyRefundSelectResult selectItemsInfo(final MJMyRefundCategory category, final MJMyRefundSearchColumn searchColumn, final String searchName, final int page, final int countPerPage){
		MJMyRefundSelectResult result = new MJMyRefundSelectResult();
		result.items =  selectItemInternal(category, searchColumn, searchName, page, countPerPage);
		result.totalCount = page != 1 || result.items.size() >= countPerPage ? selectItemsCount(category, searchColumn, searchName) : result.items.size();
		result.totalPage = MJMyServiceHelper.calculateTotalPage(result.totalCount, countPerPage);
		return result;
	}
	
	public int selectItemsCount(final MJMyRefundCategory category, final MJMyRefundSearchColumn searchColumn, final String searchName){
		return MJMyRefundDatabaseProvider.selectRefundCount(category, searchColumn, searchName);
	}
	
	private List<MJMyRefundServiceItem> selectItemInternal(final MJMyRefundCategory category, final MJMyRefundSearchColumn searchColumn, final String searchName, final int page, final int countPerPage){
		List<MJMyRefundServiceItem> items = MJMyRefundDatabaseProvider.selectRefund(category, searchColumn, searchName, page, countPerPage);
		if(items == null || items.size() <= 0){
			return Collections.emptyList();
		}
		return items;
	}
}
