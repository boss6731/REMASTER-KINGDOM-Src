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
		_timer.schedule(this, 0, 1000); //���λ��� �簻�� ��ų �ð� 1000=>1��
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
        return new l1j.server.server.model.Instance.L1PcInstance[0];
    }

}
