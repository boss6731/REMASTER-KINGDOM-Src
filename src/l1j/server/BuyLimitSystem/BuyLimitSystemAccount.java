package l1j.server.BuyLimitSystem;

import java.sql.Timestamp;
import java.util.HashMap;

public class BuyLimitSystemAccount {
	private String _Account;
	private int _Count;
	private Timestamp _Time;

	public final HashMap<String, Integer> _limit_items = new HashMap<String, Integer>();

	public BuyLimitSystemAccount(String Account) {
		_Account = Account;
	}

	public String getAccount() {
		return _Account;
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
