package l1j.server.AinhasadSpecialStat;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AinhasadSpecialStatInfo {
	static AinhasadSpecialStatInfo newInstance(ResultSet rs) throws SQLException {
		AinhasadSpecialStatInfo Info = newInstance();
		Info._obj_id = rs.getInt("obj_id");
		Info._char_name = rs.getString("char_name");
		Info._bless = rs.getInt("bless");
		Info._lucky = rs.getInt("lucky");
		Info._vital = rs.getInt("vital");
		Info._invoke = rs.getInt("invoke");
		Info._invoke_val_1 = rs.getInt("invoke_val_1");
		Info._invoke_val_2 = rs.getInt("invoke_val_2");
		Info._restore = rs.getInt("restore");
		Info._restore_val_1 = rs.getInt("restore_val_1");
		Info._restore_val_2 = rs.getInt("restore_val_2");
		Info._potion = rs.getInt("potion");	
		Info._potion_val_1 = rs.getInt("potion_val_1");
		Info._potion_val_2 = rs.getInt("potion_val_2");
		Info._current_stat = rs.getInt("current_stat");	
		Info._total_stat = rs.getInt("total_stat");
		Info._point = rs.getInt("point");
		Info._cur_enchant_level = rs.getInt("cur_enchant_level");
		return Info;
	}
	
	private static AinhasadSpecialStatInfo newInstance() {
		return new AinhasadSpecialStatInfo();
	}
	
	private int _obj_id;
	private String _char_name;
	private int _bless;
	private int _lucky;
	private int _vital;
	private int _invoke;
	private int _invoke_val_1;
	private int _invoke_val_2;
	private int _restore;
	private int _restore_val_1;
	private int _restore_val_2;
	private int _potion;
	private int _potion_val_1;
	private int _potion_val_2;
	private int _current_stat;
	private int _total_stat;
	private int _point;
	private int _cur_enchant_level;
	
	public int get_obj_id() {
		return _obj_id;
	}
	
	public String get_char_name() {
		return _char_name;
	}
	
	public int get_bless() {
		if (_bless > 25)
			_bless = 25;
		return _bless;
	}
	
	public int add_bless(int bless) {
		if (_bless > 25)
			bless = 0;
		return _bless += bless;
	}
	
	public int set_bless(int bless) {
		return _bless = bless;
	}
	
	public int get_lucky() {
		if (_lucky > 25)
			_lucky = 25;
		return _lucky;
	}
	
	public int add_lucky(int lucky) {
		if (_lucky > 25)
			lucky = 0;
		return _lucky += lucky;
	}
	
	public int set_lucky(int lucky) {
		return _lucky = lucky;
	}
	
	public int get_vital() {
		if (_vital > 25)
			_vital = 25;
		return _vital;
	}
	
	public int add_vital(int vital) {
		if (_vital > 25)
			vital = 0;
		return _vital += vital;
	}
	
	public int set_vital(int vital) {
		return _vital = vital;
	}
	
	public int get_invoke() {
		if (_invoke > 35)
			_invoke = 35;
		return _invoke;
	}
	
	public int add_invoke(int invoke) {
		if (_invoke > 35)
			_invoke = 0;
		return _invoke += invoke;
	}
	
	public int set_invoke(int invoke) {
		return _invoke = invoke;
	}
	
	public int get_invoke_val_1() {
		return _invoke_val_1;
	}
	
	public int add_invoke_val_1(int invoke) {
		return _invoke_val_1 += invoke;
	}
	
	public int set_invoke_val_1(int invoke) {
		return _invoke_val_1 = invoke;
	}
	
	public int get_invoke_val_2() {
		return _invoke_val_2;
	}
	
	public int add_invoke_val_2(int invoke) {
		return _invoke_val_2 += invoke;
	}
	
	public int set_invoke_val_2(int invoke) {
		return _invoke_val_2 = invoke;
	}
	
	public int get_restore() {
		if (_restore > 35)
			_restore = 35;
		return _restore;
	}
	
	public int add_restore(int restore) {
		if (_restore > 35)
			_restore = 0;
		return _restore += restore;
	}
	
	public int set_restore(int restore) {
		return _restore = restore;
	}
	
	public int get_restore_val_1() {
		return _restore_val_1;
	}
	
	public int add_restore_val_1(int restore) {
		return _restore_val_1 += restore;
	}
	
	public int set_restore_val_1(int restore) {
		return _restore_val_1 = restore;
	}
	
	public int get_restore_val_2() {
		return _restore_val_2;
	}
	
	public int add_restore_val_2(int restore) {
		return _restore_val_2 += restore;
	}
	
	public int set_restore_val_2(int restore) {
		return _restore_val_2 = restore;
	}
	
	public int get_potion() {
		if (_potion > 35)
			_potion = 35;
		return _potion;
	}
	
	public int add_potion(int potion) {
		if (_potion > 35)
			_potion = 0;
		return _potion += potion;
	}
	
	public int set_potion(int potion) {
		return _potion = potion;
	}
	
	public int get_potion_val_1() {
		return _potion_val_1;
	}
	
	public int add_potion_val_1(int potion) {
		return _potion_val_1 += potion;
	}
	
	public int set_potion_val_1(int potion) {
		return _potion_val_1 = potion;
	}
	
	public int get_potion_val_2() {
		return _potion_val_2;
	}
	
	public int add_potion_val_2(int potion) {
		return _potion_val_2 += potion;
	}
	
	public int set_potion_val_2(int potion) {
		return _potion_val_2 = potion;
	}
	
	public int get_current_stat() {
		return _current_stat;
	}
	
	public int add_current_stat(int current_stat) {
		return _current_stat += current_stat;
	}
	
	public int set_current_stat(int current_stat) {
		return _current_stat = current_stat;
	}
	
	public int get_total_stat() {
		if (_total_stat > 180)
			_total_stat = 180;
		return _total_stat;
	}
	
	public int add_total_stat(int total_stat) {
		if (_total_stat > 180)
			total_stat = 0;
		return _total_stat += total_stat;
	}
	
	public int set_total_stat(int total_stat) {
		return _total_stat = total_stat;
	}
	
	public int get_point() {
		return _point;
	}
	
	public int add_point(int point) {
		return _point += point;
	}
	
	public int set_point(int point) {
		return _point = point;
	}
	
	public int get_cur_enchant_level() {
		return _cur_enchant_level;
	}
	
	public int add_cur_enchant_level(int level) {
		return _cur_enchant_level += level;
	}
	
	public int set_cur_enchant_level(int level) {
		return _cur_enchant_level = level;
	}
}
