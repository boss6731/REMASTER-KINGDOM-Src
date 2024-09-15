package l1j.server.server.model.item.itemdelay;

import java.sql.Timestamp;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;

public class ItemDelayTimerController extends ItemDelayTimer{
	private L1Character character;
	private final long timeMillis;
	private final int ItemId;
	private final String ItemName;
	private int remainingSeconds;
	private Timestamp log_delay_time;
	private boolean stop;
	ItemDelayTimerController(L1Character character, int itemId, String ItemName, long timeMillis, Timestamp logtime) {
		this.character = character;
		this.ItemId = itemId;
		this.ItemName = ItemName;
		this.timeMillis = timeMillis;
		this.remainingSeconds = (int)(timeMillis / 1000);
		this.log_delay_time = logtime;
		this.stop = false;
	}

	@Override
	public L1Character owner(){
		return character;
	}

	@Override
	public int ItemId(){
		return ItemId;
	}
	
	@Override
	public String ItemName(){
		return ItemName;
	}

	@Override
	public long timeMillis(){
		return timeMillis; 
	}
	
	@Override
	public boolean stopped(){
		return stop;
	}

	@Override
	public int remainingSeconds() {
		return remainingSeconds;
	}

	@Override
	public void remainingSeconds(int remainingSeconds) {
		this.remainingSeconds = remainingSeconds;
	}
	
	@Override
	public Timestamp LogDelayTime() {
		return log_delay_time;
	}
	
	@Override
	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, 100L);
	}
	
	@Override
	public void end() {
		stop = true;
		character = null;
	}

	@Override
	public void kill() {
		stop = true;
		character = null;
	}

}
