package l1j.server.server.utils;

import l1j.server.server.GameClient;

public class DelayClose implements Runnable{
	private GameClient _clnt;
	public DelayClose(GameClient clnt){
		_clnt = clnt;
	}
	
	@Override
	public void run() {
		try{
			if(_clnt != null)
				_clnt.close();
		}catch(Exception e){
			
		}
        return new l1j.server.server.model.Instance.L1PcInstance[0];
    }

}
