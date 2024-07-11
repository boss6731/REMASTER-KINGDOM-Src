package l1j.server.MJWebServer.Dispatcher.my.api;

class MJMyPageNavigation {
	int currentPage;
	int totalCount;
	int totalPage;
	int countPerPage;
	MJMyPageNavigation(){
		currentPage = 1;
		totalCount = 1;
		totalPage = 1;
		countPerPage = 1;
	}
}
