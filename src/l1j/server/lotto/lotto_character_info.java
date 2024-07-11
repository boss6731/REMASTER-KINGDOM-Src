package l1j.server.lotto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class lotto_character_info {
	static lotto_character_info newInstance(ResultSet rs) throws SQLException{
		lotto_character_info Info = newInstance();
		Info._char_obj_id = rs.getInt("char_obj_id");
		Info._char_name = rs.getString("char_name");
		Info._round = rs.getInt("round");
		Info._number1 = rs.getInt("number1");
		Info._number2 = rs.getInt("number2");
//		Info._number3 = rs.getInt("number3");
		return Info;
	}
	
	private static lotto_character_info newInstance() {
		return new lotto_character_info();
	}
	private int _char_obj_id;
	
	private String _char_name;
	private int _round;
	private int _number1, _number2, _number3, _number4;
	
	public String get_char_name() {
		return _char_name;
	}
	public int get_char_id() {
		return _char_obj_id;
	}
	
	public int get_round() {
		return _round;
	}
	public int get_number1() {
		return _number1;
	}
	public int get_number2() {
		return _number2;
	}
/*	public int get_number3() {
		return _number3;
	}
*/
	
	
	
}
