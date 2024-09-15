package l1j.server.server.templates;

import l1j.server.server.utils.CommonUtil;

public class EventTimeTemp {

	private int type;

	public int get_type() {
		return type;
	}

	public void set_type(int i) {
		this.type = i;
	}

	private int npcid;

	public int get_npcid() {
		return npcid;
	}

	public void set_npcid(int i) {
		this.npcid = i;
	}

	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String sub_npc;

	public String getSubNpc() {
		return sub_npc;
	}

	public void setSubNpc(String npc) {
		this.sub_npc = npc;
	}

	private String subtitle;

	public String getSubTitle() {
		return subtitle;
	}

	public void setSubTitle(String title) {
		this.subtitle = title;
	}

	private String subactid;

	public String getSubActid() {
		return subactid;
	}

	public void setSubActid(String Actid) {
		this.subactid = Actid;
	}

	private int loc_x;

	public int get_loc_x() {
		return loc_x;
	}

	public void set_loc_x(int i) {
		this.loc_x = i;
	}

	private int loc_y;

	public int get_loc_y() {
		return loc_y;
	}

	public void set_loc_y(int i) {
		this.loc_y = i;
	}

	private int loc_map;

	public int get_loc_map() {
		return loc_map;
	}

	public void set_loc_map(int i) {
		this.loc_map = i;
	}

	private int loc_rnd;

	public int get_loc_rnd() {
		return loc_rnd;
	}

	public void set_loc_rnd(int i) {
		this.loc_rnd = i;
	}

	private int hour;

	public int get_hour() {
		return hour;
	}

	public void set_hour(int i) {
		this.hour = CommonUtil.get_current(i, 0, 24);
	}

	private int minute;

	public int get_minute() {
		return minute;
	}

	public void set_minute(int i) {
		this.minute = CommonUtil.get_current(i, 0, 60);
	}

	private int delete_time;

	public int get_delete_time() {
		return delete_time;
	}

	public void set_delete_time(int i) {
		this.delete_time = i;
	}

	private boolean _is_tel;

	public boolean is_tel() {
		return _is_tel;
	}

	public void set_tel(boolean flag) {
		this._is_tel = flag;
	}

	private int tel_x;

	public int get_tel_x() {
		return tel_x;
	}

	public void set_tel_x(int i) {
		this.tel_x = i;
	}

	private int tel_y;

	public int get_tel_y() {
		return tel_y;
	}

	public void set_tel_y(int i) {
		this.tel_y = i;
	}

	private int tel_map;

	public int get_tel_map() {
		return tel_map;
	}

	public void set_tel_map(int i) {
		this.tel_map = i;
	}

	private int tel_rnd;

	public int get_tel_rnd() {
		return tel_rnd;
	}

	public void set_tel_rnd(int i) {
		this.tel_rnd = i;
	}

	private int _tel_count;

	public int get_tel_count() {
		return _tel_count;
	}

	public void set_tel_count(int i) {
		this._tel_count = i;
	}

	private boolean _is_msg;

	public boolean isMsg() {
		return _is_msg;
	}

	public void setMsg(boolean flag) {
		this._is_msg = flag;
	}

	private String boss_message;

	public String get_boss_message() {
		return boss_message;
	}

	public void set_boss_message(String s) {
		this.boss_message = s;
	}

	private boolean _yn;

	public boolean isYn() {
		return _yn;
	}

	public void setYn(boolean flag) {
		this._yn = flag;
	}

	private String _yn_ment;

	public String get_yn_ment() {
		return _yn_ment;
	}

	public void set_yn_ment(String s) {
		this._yn_ment = s;
	}

	private boolean _is_effect;

	public boolean is_Effect() {
		return _is_effect;
	}

	public void set_is_Effect(boolean flag) {
		this._is_effect = flag;
	}

	private int _effect;

	public int get_Effect() {
		return _effect;
	}

	public void set_Effect(int i) {
		this._effect = i;
	}

	private boolean _is_alarm_onoff;

	public boolean is_alarm_onoff() {
		return _is_alarm_onoff;
	}

	public void set_alarm_onoff(boolean flag) {
		this._is_alarm_onoff = flag;
	}

	private boolean _ain_effect;

	public void setAinEffect(boolean f) {
		_ain_effect = f;
	}

	public boolean isAinEffect() {
		return _ain_effect;
	}
	
	private boolean _New_effect;

	public void setNewEffect(boolean f) {
		_New_effect = f;
	}

	public boolean isNewEffect() {
		return _New_effect;
	}

	private String[] _yoil;
	
	public String[] getYoil() {
		return _yoil;
	}

	public void setYoil(String[] yoil) {
		_yoil = yoil;
	}
	
	private long _startdate;
	private long _enddate;
	
	public long get_startdate() {
		return _startdate;
	}

	public void set_startdate(long _startdate) {
		this._startdate = _startdate;
	}

	public long get_enddate() {
		return _enddate;
	}

	public void set_enddate(long _enddate) {
		this._enddate = _enddate;
	}
	
	private int _next_day_index;
	
	public int get_next_day_index() {
		return _next_day_index;
	}

	public void set_next_day_index(int _next_day_index) {
		this._next_day_index = _next_day_index;
	}
}