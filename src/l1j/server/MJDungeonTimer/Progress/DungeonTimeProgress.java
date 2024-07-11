package l1j.server.MJDungeonTimer.Progress;

public abstract class DungeonTimeProgress<T>{
	private T _owner_info;
	private int _timer_id;
	private int _remain_seconds;
	private int _charge_count;

	public DungeonTimeProgress<T> set_owner_info(T val){
		_owner_info = val;
		return this;
	}
	public T get_owner_info(){
		return _owner_info;
	}
	public DungeonTimeProgress<T> set_timer_id(int val){
		_timer_id = val;
		return this;
	}
	public int get_timer_id(){
		return _timer_id;
	}

	public DungeonTimeProgress<T> set_remain_seconds(int val){
		_remain_seconds = val;
		return this;
	}
	public int get_remain_seconds(){
		return _remain_seconds;
	}
	

	
	public int inc_remain_seconds(){
		return ++_remain_seconds;
	}
	
	public int dec_remain_seconds(){
		return --_remain_seconds;
	}
	
	

	public DungeonTimeProgress<T> set_charge_count(int val){
		_charge_count = val;
		return this;
	}
	public int get_charge_count(){
		return _charge_count;
	}
	
	public int inc_charge_count(){
		return ++_charge_count;
	}
	
	public int dec_charge_count(){
		return --_charge_count;
	}
}