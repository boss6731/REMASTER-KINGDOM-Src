package l1j.server.server.templates;

import java.sql.Timestamp;



public class L1FreeShield {
	private int _account_id;
	private String _account_name;
	private Timestamp _pk_time;
	private int _pc_gaho;
	private int _pc_gaho_use;
	private int _free_gaho;
	private int _free_gaho_use;
	private int _event_gaho;
	private int _event_gaho_use;
	
	public void set_Account_Id(int accountid) {
		_account_id = accountid;
	}
	public int get_Account_Id() {
		return _account_id;
	}
	
	public void set_Account_name(String name) {
		_account_name = name;
	}
	public String get_Account_name() {
		return _account_name;
	}
	
	public void set_PK_Time(Timestamp t) {
		_pk_time = t;
	}
	public Timestamp get_Pk_time() {
		return _pk_time;
	}
	public void set_Pc_Gaho(int count) {
		_pc_gaho = count;
	}
	
	public int get_Pc_Gaho() {
		return _pc_gaho;
	}
	public void set_Pc_Gaho_use(int count) {
		_pc_gaho_use = count;
	}
	public int get_Pc_Gaho_use() {
		return _pc_gaho_use;
	}
	
	public void set_Free_Gaho(int count) {
		_free_gaho = count;
	}
	
	public int get_Free_Gaho() {
		return _free_gaho;
	}
	public void set_Free_Gaho_use(int count) {
		_free_gaho_use = count;
	}
	public int get_Free_Gaho_use() {
		return _free_gaho_use;
	}
	
	public void set_Event_Gaho(int count) {
		_event_gaho = count;
	}
	
	public int get_Event_Gaho() {
		return _event_gaho;
	}
	public void set_Event_Gaho_use(int count) {
		_event_gaho_use = count;
	}
	public int get_Event_Gaho_use() {
		return _event_gaho_use;
	}
}
