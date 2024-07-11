package l1j.server.MJWebServer.Dispatcher.my.api;

class MJMyApiTradeConfirmModel extends MJMyApiModel{
	int tradeNo;
	int useCoin;
	int remainCoin;
	String confirmMessage;
	
	MJMyApiTradeConfirmModel(){
		super();
		tradeNo = 0;
		useCoin = 0;
		remainCoin = 0;
	}
}