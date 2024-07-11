 package l1j.server.server.templates;public class EventTimeTemp { private int type; private int npcid; private String title; private String sub_npc; private String subtitle; private String subactid; private int loc_x; private int loc_y; private int loc_map; private int loc_rnd;
   private int hour;
   private int minute;
   private int delete_time;
   private boolean _is_tel;
   private int tel_x;
   private int tel_y;

   public int get_type() {
     return this.type;
   }
   private int tel_map; private int tel_rnd; private int _tel_count; private boolean _is_msg; private String boss_message; private boolean _yn; private String _yn_ment; private boolean _is_effect; private int _effect; private boolean _is_alarm_onoff; private boolean _ain_effect; private boolean _New_effect; private String[] _yoil; private long _startdate; private long _enddate; private int _next_day_index;
   public void set_type(int i) {
     this.type = i;
   }



   public int get_npcid() {
     return this.npcid;
   }

   public void set_npcid(int i) {
     this.npcid = i;
   }



   public String getTitle() {
     return this.title;
   }

   public void setTitle(String title) {
     this.title = title;
   }



   public String getSubNpc() {
     return this.sub_npc;
   }

   public void setSubNpc(String npc) {
     this.sub_npc = npc;
   }



   public String getSubTitle() {
     return this.subtitle;
   }

   public void setSubTitle(String title) {
     this.subtitle = title;
   }



   public String getSubActid() {
     return this.subactid;
   }

   public void setSubActid(String Actid) {
     this.subactid = Actid;
   }



   public int get_loc_x() {
     return this.loc_x;
   }

   public void set_loc_x(int i) {
     this.loc_x = i;
   }



   public int get_loc_y() {
     return this.loc_y;
   }

   public void set_loc_y(int i) {
     this.loc_y = i;
   }



   public int get_loc_map() {
     return this.loc_map;
   }

   public void set_loc_map(int i) {
     this.loc_map = i;
   }



   public int get_loc_rnd() {
     return this.loc_rnd;
   }

   public void set_loc_rnd(int i) {
     this.loc_rnd = i;
   }



   public int get_hour() {
     return this.hour;
   }

   public void set_hour(int i) {
     this.hour = CommonUtil.get_current(i, 0, 24);
   }



   public int get_minute() {
     return this.minute;
   }

   public void set_minute(int i) {
     this.minute = CommonUtil.get_current(i, 0, 60);
   }



   public int get_delete_time() {
     return this.delete_time;
   }

   public void set_delete_time(int i) {
     this.delete_time = i;
   }



   public boolean is_tel() {
     return this._is_tel;
   }

   public void set_tel(boolean flag) {
     this._is_tel = flag;
   }



   public int get_tel_x() {
     return this.tel_x;
   }

   public void set_tel_x(int i) {
     this.tel_x = i;
   }



   public int get_tel_y() {
     return this.tel_y;
   }

   public void set_tel_y(int i) {
     this.tel_y = i;
   }



   public int get_tel_map() {
     return this.tel_map;
   }

   public void set_tel_map(int i) {
     this.tel_map = i;
   }



   public int get_tel_rnd() {
     return this.tel_rnd;
   }

   public void set_tel_rnd(int i) {
     this.tel_rnd = i;
   }



   public int get_tel_count() {
     return this._tel_count;
   }

   public void set_tel_count(int i) {
     this._tel_count = i;
   }



   public boolean isMsg() {
     return this._is_msg;
   }

   public void setMsg(boolean flag) {
     this._is_msg = flag;
   }



   public String get_boss_message() {
     return this.boss_message;
   }

   public void set_boss_message(String s) {
     this.boss_message = s;
   }



   public boolean isYn() {
     return this._yn;
   }

   public void setYn(boolean flag) {
     this._yn = flag;
   }



   public String get_yn_ment() {
     return this._yn_ment;
   }

   public void set_yn_ment(String s) {
     this._yn_ment = s;
   }



   public boolean is_Effect() {
     return this._is_effect;
   }

   public void set_is_Effect(boolean flag) {
     this._is_effect = flag;
   }



   public int get_Effect() {
     return this._effect;
   }

   public void set_Effect(int i) {
     this._effect = i;
   }



   public boolean is_alarm_onoff() {
     return this._is_alarm_onoff;
   }

   public void set_alarm_onoff(boolean flag) {
     this._is_alarm_onoff = flag;
   }



   public void setAinEffect(boolean f) {
     this._ain_effect = f;
   }

   public boolean isAinEffect() {
     return this._ain_effect;
   }



   public void setNewEffect(boolean f) {
     this._New_effect = f;
   }

   public boolean isNewEffect() {
     return this._New_effect;
   }



   public String[] getYoil() {
     return this._yoil;
   }

   public void setYoil(String[] yoil) {
     this._yoil = yoil;
   }




   public long get_startdate() {
     return this._startdate;
   }

   public void set_startdate(long _startdate) {
     this._startdate = _startdate;
   }

   public long get_enddate() {
     return this._enddate;
   }

   public void set_enddate(long _enddate) {
     this._enddate = _enddate;
   }



   public int get_next_day_index() {
     return this._next_day_index;
   }

   public void set_next_day_index(int _next_day_index) {
     this._next_day_index = _next_day_index;
   } }


