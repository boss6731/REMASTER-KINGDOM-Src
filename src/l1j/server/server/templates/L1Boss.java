 package l1j.server.server.templates;

 import MJFX.UIAdapter.MJUIAdapter;
 import java.util.Random;
 import l1j.server.server.utils.SystemUtil;









 public class L1Boss
 {
   private int npcid;
   private String mapname;
   private int groupid;
   private int x;
   private int y;
   private int map;
   private int rnd;
   private int movement;
   private int[][] time;
   private int del_time;

   public int getNpcId() {
     return this.npcid;
   }
   private String monname; private String npc_type; private String[] yoil; private boolean _ment; private boolean _yn; private String message; private String ynmessage; private int _display_effect; private int nonespawntimernd; private int rnd_time_minute;
   public void setNpcId(int id) {
     this.npcid = id;
   }

   public String getMapName() {
     return this.mapname;
   }

   public void setMapName(String name) {
     this.mapname = name;
   }

   public int getGroupId() {
     return this.groupid;
   }

   public void setGroupId(int id) {
     this.groupid = id;
   }

   public int getX() {
     return this.x;
   }

   public void setX(int x) {
     this.x = x;
   }

   public int getY() {
     return this.y;
   }

   public void setY(int y) {
     this.y = y;
   }

   public int getMap() {
     return this.map;
   }

   public void setMap(int mapid) {
     this.map = mapid;
   }

   public int getRndLoc() {
     return this.rnd;
   }

   public void setRndLoc(int i) {
     this.rnd = i;
   }

   public int getMovement_distance() {
     return this.movement;
   }

   public void setMovement_distance(int i) {
     this.movement = i;
   }

   public String getMonName() {
     return this.monname;
   }

   public void setMonName(String type) {
     this.monname = type;
   }

   public String getNpcType() {
     return this.npc_type;
   }

   public void setNpcType(String type) {
     this.npc_type = type;
   }

   public int[][] getTime() {
     return this.time;
   }

   public void setTime(int[][] time) {
     this.time = time;
   }

   public int getDeleteTime() {
     return this.del_time;
   }

   public void setDeleteTime(int i) {
     this.del_time = i;
   }

   public String[] getYoil() {
     return this.yoil;
   }

   public void setYoil(String[] yoil) {
     this.yoil = yoil;
   }

   public boolean isMent() {
     return this._ment;
   }

   public void setMent(boolean flag) {
     this._ment = flag;
   }

   public boolean isYn() {
     return this._yn;
   }

   public void setYn(boolean flag) {
     this._yn = flag;
   }

   public String getMentMessage() {
     return this.message;
   }

   public void setMentMessage(String ment) {
     this.message = ment;
   }

   public String getYnMessage() {
     return this.ynmessage;
   }

   public void setYnMessage(String ment) {
     this.ynmessage = ment;
   }



   public void set_display_effect(int display_effect) {
     this._display_effect = display_effect;
   }

   public int get_display_effect() {
     return this._display_effect;
   }



   public int getnonespawntime() {
     return this.nonespawntimernd;
   }

   public void setnonespawntime(int i) {
     this.nonespawntimernd = i;
   }



   public int getRndMinuteTime() {
     return this.rnd_time_minute;
   }

   public void setRndMinuteTime(int i) {
     this.rnd_time_minute = i;
   }








   public boolean isSpawnTime(int h, int m, long current_time) {
     String now_y = SystemUtil.getYoil(System.currentTimeMillis());
     boolean isYoil = false;
     for (String y : this.yoil) {
       if (y.equalsIgnoreCase("全部") || y.equalsIgnoreCase(now_y))
         isYoil = true;
     }
     if (this.time == null) {
       System.out.println(String.format("[警告]: spawnlist_boss_date->spawn_time = NULL 這個存在.", new Object[0]));
       return false;
     }
     if (!isYoil) {
       return false;
     }

     for (int[] t : this.time) {
       if (t[0] == h && t[1] == m) {
         return true;
       }
     }
     return false;
   }

   private static Random _random = new Random(System.nanoTime());
   private int[][] real_time;

   public int[][] getRealTime() {
     return this.real_time;
   }

   public void setRealTime(int[][] time) {
     this.real_time = time;
   }

   public void resetSpawnTime() {
     if (this.real_time == null || this.time == null) {
       return;
     }



     for (int i = 0; i < this.time.length; i++) {
       int[] t = this.time[i];
       int[] real_t = this.real_time[i];

       int rnd_min = (this.rnd_time_minute == 0) ? 0 : _random.nextInt(this.rnd_time_minute);

       t[0] = real_t[0];
       t[1] = real_t[1] + rnd_min;

       while (t[1] >= 60) {
         t[1] = t[1] - 60;
         t[0] = t[0] + 1;
       }

       if (t[0] >= 24) {
         t[0] = t[0] - 24;
       }

       MJUIAdapter.on_boss_append(this.npcid, this.monname + " " + t[0] + ":" + t[1] + " ", this.x, this.y, this.map);
     }
   }
 }


