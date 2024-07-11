package l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util;

import java.util.Timer;
import java.util.TimerTask;

import l1j.server.MJWebServer.Dispatcher.Template.Market.API.MJMarketPriceLoader;

public class MJMarketReloadTask extends TimerTask{
	private static MJMarketReloadTask _instance;
	public static MJMarketReloadTask getInstance(){
		if(_instance == null){
			_instance = new MJMarketReloadTask();
		}
		return _instance;
	}
	
	private Timer _timer;
	public MJMarketReloadTask(){
		_timer = new Timer();
		_timer.schedule(this, 0, 1000); //개인상점 재갱신 시킬 시간 1000=>1초
	}
	
	public void release(){
		if(_timer != null){
			_timer.cancel();
			_timer = null;
		}
	}
	
	@Override
	public void run() {
		MJMarketPriceLoader.reload();
	}

}
