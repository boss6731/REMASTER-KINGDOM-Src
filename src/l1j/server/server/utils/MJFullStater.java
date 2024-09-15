package l1j.server.server.utils;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class MJFullStater implements Runnable{
	public static void running(L1PcInstance pc, String s, int count){
		int i = pc.remainBonusStats();
		if(i <= 0 || count <= 0 || i < count){
			pc.sendPackets(String.valueOf(new S_SystemMessage(String.format("屬性點數不足。[剩餘屬性點數: %d]", i))));
			return;
		}

		MJFullStater stater = new MJFullStater(pc, s, count);
		if(count <= 1){
			try{
				stater.work(0L);
				pc.sendPackets(String.valueOf(new S_SystemMessage(String.format("%s 已經增加了。[剩餘屬性點數: %d]", s, pc.remainBonusStats()))));
				pc.sendBonusStats();
			}catch(Exception e){
				pc.sendPackets(String.valueOf(new S_SystemMessage(String.format("屬性點數不足。[剩餘屬性點數: %d]", pc.remainBonusStats()))));
			}
		}else
			GeneralThreadPool.getInstance().execute(stater);
	}

	private L1PcInstance 	_pc;
	private String				_s;
	private int					_count;
	private MJFullStater(L1PcInstance pc, String s, int count){
		_pc 		= pc;
		_s			= s;
		_count	= count;
	}

	@Override
	public void run() {
		try{
			for(int i = _count - 1; i>= 0; --i){
				work(100L);
			}
			_pc.sendPackets(String.valueOf(new S_SystemMessage(String.format("%s 已經增加了。[剩餘屬性點數: %d]", _s, _pc.remainBonusStats()))));
		}catch(Exception e){
			if(_pc != null)
				_pc.sendPackets(String.valueOf(new S_SystemMessage(String.format("屬性點數不足。[剩餘屬性點數: %d]", _pc.remainBonusStats()))));
		}finally{
			_pc.sendBonusStats();
		}
        return null;
    }

	private void work(long sleeping) throws Exception{
		if(!_pc.onStat(_s))
			throw new Exception();
		if(sleeping > 0)
			Thread.sleep(sleeping);
	}
}
