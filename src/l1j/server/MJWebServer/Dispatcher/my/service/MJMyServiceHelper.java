package l1j.server.MJWebServer.Dispatcher.my.service;

public class MJMyServiceHelper {
	public static int calculateOffset(int page, int countPerPage){
		if(page <= 1){
			return 0;
		}
		return (page - 1) * countPerPage;
	}
	
	public static int calculatePage(int offset, int countPerPage){
		if(offset < countPerPage){
			return 1;
		}
		int additionOffset = offset + 1;
		return (additionOffset / countPerPage) + (additionOffset % countPerPage > 0 ? 1 : 0);
	}
	
	public static int calculateTotalPage(int totalCount, int countPerPage){
		if(totalCount <= 0){
			return 1;
		}
		return (totalCount / countPerPage) + (totalCount % countPerPage > 0 ? 1 : 0);
	}
	
}
