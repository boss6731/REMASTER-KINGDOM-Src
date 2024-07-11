package l1j.server.lotto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class lotto_system_info {
	static lotto_system_info newInstance(ResultSet rs) throws SQLException{
		lotto_system_info Info = newInstance();
		Info._round = rs.getInt("round");
		Info._number1 = rs.getInt("number1");
		Info._number2 = rs.getInt("number2");
		Info._number3 = rs.getInt("number3");
		Info._pc_count = rs.getInt("pc_count");
		Info._prize = rs.getBoolean("prize");
		return Info;
	}
	
	private static lotto_system_info newInstance() {
		return new lotto_system_info();
	}
	
	private int _round, _number1, _number2, _number3, _pc_count;
	private boolean _prize;
	
	public int get_round() {
		return _round;
	}
	public int get_number1() {
		return _number1;
	}
	public int get_number2() {
		return _number2;
	}
	public int get_number3() {
		return _number3;
	}
	public int get_pc_count() {
		return _pc_count;
	}
	public boolean is_prize() {
		return _prize;
	}
	
}
