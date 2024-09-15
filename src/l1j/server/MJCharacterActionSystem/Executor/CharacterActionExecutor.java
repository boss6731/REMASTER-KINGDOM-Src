package l1j.server.MJCharacterActionSystem.Executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import l1j.server.Config;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJTemplate.Regen.MJRegeneratorLatestActions;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;

public class CharacterActionExecutor implements Runnable{
	public static final int ACTION_IDX_WALK 			= 0;
	public static final int ACTION_IDX_SPELL 			= 1;
	public static final int ACTION_IDX_PICKUP 			= 2;
	public static final int ACTION_IDX_WAND 			= 3;
	public static final int ACTION_IDX_ATTACK 			= 4;
	public static final int ACTION_IDX_ATTACKCONTINUE 	= 5;
	private static final int MAXIMUM_ACTION_AMOUNT 		= 6;
	public static long NETWORK_DELAY_MILLIS = Config.Synchronization.NETWORK_DELAY_MILLIS; // 若延遲發生，可增加此值。
	public static long NETWORK_ATTACK_DELAY_MILLIS = Config.Synchronization.NETWORK_ATTACK_DELAY_MILLIS; // 若延遲發生，可增加此值（非延遲，而是背刺類型）。

	public static CharacterActionExecutor execute(L1PcInstance pc){
		CharacterActionExecutor executor = new CharacterActionExecutor(pc);
		GeneralThreadPool.getInstance().execute(executor);
		return executor;
	}

	private AbstractActionHandler[] 	_handler;
	private L1PcInstance				_owner;
	private ArrayBlockingQueue<Object>	_signal;
	private Object						_signaled;
	private Object						_dummy;
	private CharacterActionExecutor(L1PcInstance owner){
		_handler 		= new AbstractActionHandler[MAXIMUM_ACTION_AMOUNT];
		_owner			= owner;
		_signal			= new ArrayBlockingQueue<Object>(1);
		_dummy			= new Object();
	}

	public void register(int idx, AbstractActionHandler handler){
		if(idx == ACTION_IDX_WALK){
			for(int i = _handler.length - 1; i>=ACTION_IDX_ATTACK; --i)
				unregister(idx);
		}
		//CMD窗口顯示43(46)行錯誤時，無需特別在意，這通常發生在某人採取行動中斷線或被強制終止時。
		//這個錯誤會顯示，其他錯誤才能被檢測：顯示5~8個網絡點是正常的。
		//PC在採取行動中斷線，導致NPC攻擊/魔法等觸發。
		if(_handler == null){
			StringBuilder sb = new StringBuilder(256);
			if(_owner != null){
				sb.append(_owner.getName()).append(" 動作處理器為空。");
				sb.append("(").append(idx).append(")");
				sb.append("Connected : ").append(_owner.getNetConnection());
				if(_owner.getNetConnection() != null){
					sb.append("isConnected : ").append(_owner.getNetConnection().isConnected());
				}
			}else{
				handler.dispose();
				return;
			}
			System.out.println("CharacterActionExecutor:" +sb.toString());
			return;
		}
		_handler[idx] = handler;
		_signal.offer(_dummy);
	}

	public void unregister(int idx){
		AbstractActionHandler handler = _handler[idx];
		if(handler != null){
			_handler[idx] = null;
			handler.dispose();
		}
	}

	private AbstractActionHandler pop(int idx){
		AbstractActionHandler handler = _handler[idx];
		if(idx != ACTION_IDX_ATTACKCONTINUE)
			_handler[idx] = null;

		return handler;
	}

	private int size(){
		int amount = 0;
		for(int i = _handler.length - 1; i>=0; --i){
			if(_handler[i] != null)
				++amount;
		}

		return amount;
	}

	public boolean hasSpell(){
		return _handler[ACTION_IDX_SPELL] != null;
	}

	public boolean hasAction(int idx){
		return _handler[idx] != null;
	}

	private void update_action(int idx) {
		switch(idx) {
			case ACTION_IDX_WALK:
				_owner.set_latest_action(MJRegeneratorLatestActions.LATEST_ACTION_MOVE);
				break;

			case ACTION_IDX_SPELL:
			case ACTION_IDX_WAND:
			case ACTION_IDX_ATTACK:
			case ACTION_IDX_ATTACKCONTINUE:
				_owner.set_latest_action(MJRegeneratorLatestActions.LATEST_ACTION_FIGHT);
				break;
		}
	}

	@Override
	public void run() {
		try{
			while(true){
				if(_signaled == null)
					_signaled = _signal.poll(3000L, TimeUnit.MILLISECONDS);

				if(_owner == null || _owner.getNetConnection() == null || !_owner.getNetConnection().isConnected()){
					dispose();
					return null;
				}

				if(_signaled == null)
					continue;

				long interval	= 0L;
				long error		= System.currentTimeMillis();
				for(int i=0; i<MAXIMUM_ACTION_AMOUNT; ++i){
					AbstractActionHandler handler = pop(i);
					if(handler == null)
						continue;

					if(interval > 0L)
						Thread.sleep(interval);

					update_action(i);
					handler.handle();
					interval = handler.getInterval();

					if (i != ACTION_IDX_ATTACKCONTINUE) {
						handler.dispose();
						if (i == ACTION_IDX_ATTACK) {
							// XXX 攻速
							interval -= NETWORK_ATTACK_DELAY_MILLIS;
							interval -= (System.currentTimeMillis() - error);
							if (interval < _owner.getAttackDelayChecker()) {
								// System.out.println("最終前 : " + interval);
								if (Config.Synchronization.NETWORK_ATTACK_DELAY_FALSE) {
									interval = (long) _owner.getAttackDelayChecker();
									_owner.addAttackDelayCount(1);
								}
								// System.out.println("最終 : " + interval);
								if (Config.LogStatus.ATTACKDELAYCOUNT_LOG <= 98) {
									if (_owner.getAttackDelayCount() > Config.LogStatus.ATTACKDELAYCOUNT_LOG) {
										System.out.println(String.format("[攻擊速度異常嫌疑者]: [帳號]: %s, [計數]: %d (需監視)", _owner.getName(), _owner.getAttackDelayCount()));
										if (Config.Synchronization.NETWORK_ATTACK_DELAY_FALSE) {
											_owner.setAttackDelayCount(0);
										}
									}
								}
							}
						} else {
							// XXX 移動/魔法/杖
							interval -= NETWORK_DELAY_MILLIS;
							interval -= (System.currentTimeMillis() - error);
						}
					} else {
						if (hasSpell()) {
							i = ACTION_IDX_SPELL - 1;
							continue;
						}
					}
				}

				if(size() <= 0)
					_signaled = null;
				if(interval > 0L)
					Thread.sleep(interval);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(_signal != null){
				GeneralThreadPool.getInstance().execute(this);
			}
		}
		return null;
	}

	private void dispose(){
		if(_handler != null){
			for(AbstractActionHandler h : _handler){
				if(h != null)
					h.dispose();
			}
			_handler = null;
		}

		if(_signal != null){
			_signal.clear();
			_signal = null;
		}

		_owner = null;
	}
}
