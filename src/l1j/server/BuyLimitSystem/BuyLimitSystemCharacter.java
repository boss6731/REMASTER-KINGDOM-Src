package l1j.server.BuyLimitSystem;

import java.sql.Timestamp;
import java.util.HashMap;

public class BuyLimitSystemCharacter {
	private final int _CharId;
	private int _Count;
	private Timestamp _Time;

	public final HashMap<String, Integer> _limit_items = new HashMap<String, Integer>();

	public BuyLimitSystemCharacter(int charId) {
		_CharId = charId;
	}

	public int getCharId() {
		return _CharId;
	}

	public int getCount() {
		return _Count;
	}
	
	public void setCount(int count) {
		_Count = count;
	}
	
	public Timestamp getBuyTime() {
		return _Time;
	}
	
	public void setBuyTime(Timestamp time) {
		_Time = time;
	}

	public boolean add(String key, int count, Timestamp time) {
		if (_limit_items.containsKey(key)) {
			return false;
		}
		_limit_items.put(key, count);
		setCount(count);
		setBuyTime(time);
		return true;
	}
}
