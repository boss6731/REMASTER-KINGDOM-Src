package l1j.server.MJWebServer.Dispatcher.my.service.ncoin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import MJNCoinSystem.MJNCoinDepositInfo;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyServiceHelper;

public class MJMyNcoinService {
	private static final MJMyNcoinService service = new MJMyNcoinService();
	private static final int COUNT_PER_PAGE = 20;
	
	public static MJMyNcoinService service(){
		return service;
	}
	
	private MJMyNcoinService(){}
	
	public int countPerPage(){
		return COUNT_PER_PAGE;
	}
	
	public MJMyNcoinSelectResult selectItemsInfo(int page){
		return selectItemsInfo(MJMyNcoinCategory.categories[0], MJMyNcoinSearchColumn.all, MJString.EmptyString, page);
	}
	
	public MJMyNcoinSelectResult selectItemsInfo(final MJMyNcoinCategory category, final MJMyNcoinSearchColumn searchColumn, final String searchName, final int page){
		return selectItemsInfo(category, searchColumn, searchName, page, COUNT_PER_PAGE);
	}
	
	public MJMyNcoinSelectResult selectItemsInfo(final MJMyNcoinCategory category, final MJMyNcoinSearchColumn searchColumn, final String searchName, final int page, final int countPerPage){
		MJMyNcoinSelectResult result = new MJMyNcoinSelectResult();
		result.items = selectItemInternal(category, searchColumn, searchName, page, countPerPage);
		result.totalCount = page != 1 || result.items.size() >= countPerPage ? selectItemsCount(category, searchColumn, searchName) : countPerPage;
		result.totalPage = MJMyServiceHelper.calculateTotalPage(result.totalCount, countPerPage);
		return result;
	}
	
	public int selectItemsCount(final MJMyNcoinCategory category, final MJMyNcoinSearchColumn searchColumn, final String searchName){
		return MJMyNcoinDatabaseProvider.selectDepositCount(category, searchColumn, searchName);
	}
	
	private List<MJMyNcoinServiceItem> selectItemInternal(final MJMyNcoinCategory category, final MJMyNcoinSearchColumn searchColumn, final String searchName, final int page, final int countPerPage){
		List<MJNCoinDepositInfo> deposits = MJMyNcoinDatabaseProvider.selectDeposit(category, searchColumn, searchName, page, countPerPage);
		if(deposits == null || deposits.size() <= 0){
			return Collections.emptyList();
		}
		ArrayList<MJMyNcoinServiceItem> items = new ArrayList<>(deposits.size());
		for(MJNCoinDepositInfo nInfo : deposits){
			MJMyNcoinServiceItem item = MJMyNcoinServiceItem.newInstance(nInfo);
			items.add(item);
		}
		return items;
	}
}
