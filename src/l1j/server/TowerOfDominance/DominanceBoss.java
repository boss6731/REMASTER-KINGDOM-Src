 package l1j.server.TowerOfDominance;

 import MJFX.UIAdapter.MJUIAdapter;
 import java.util.Random;
 import l1j.server.server.utils.SystemUtil;







 public class DominanceBoss
 {
   private int _bossnum;
   private String _bossname;
   private int _npcid;
   private int _mapx;
   private int _mapy;
   private int _mapid;
   private int[][] _bosstime;

   public int getBossNum() {
     return this._bossnum;
   } private boolean _isment; private String _ment; private boolean alleffect; private int _effid; private int _rnds; private String[] yoil; private int[][] real_time; private int rnd_time_minute;
   public void setBossNum(int num) {
     this._bossnum = num;
   }

   public String getBossName() {
     return this._bossname;
   }
   public void setBossName(String name) {
     this._bossname = name;
   }

   public int getNpcId() {
     return this._npcid;
   }
   public void setNpcId(int id) {
     this._npcid = id;
   }

   public int getMapX() {
     return this._mapx;
   }
   public void setMapX(int x) {
     this._mapx = x;
   }

   public int getMapY() {
     return this._mapy;
   }
   public void setMapY(int y) {
     this._mapy = y;
   }

   public int getMapId() {
     return this._mapid;
   }
   public void setMapId(int mapid) {
     this._mapid = mapid;
   }

   public int[][] getBossTime() {
     return this._bosstime;
   }
   public void setBossTime(int[][] time) {
     this._bosstime = time;
   }



   public int[][] getRealTime() {
     return this.real_time;
   }

   public void setRealTime(int[][] time) {
     this.real_time = time;
   }

   public boolean isMentuse() {
     return this._isment;
   }

   public DominanceBoss setMentuse(boolean flag) {
     this._isment = flag;
     return this;
   }

   public String getMent() {
     return this._ment;
   }

   public void setMent(String ment) {
     this._ment = ment;
   }

   public boolean isAllEffect() {
     return this.alleffect;
   }

   public DominanceBoss setAllEffect(boolean flag) {
     this.alleffect = flag;
     return this;
   }

   public int getEffectNum() {
     return this._effid;
   }

   public void setEffectNum(int num) {
     this._effid = num;
   }

   public int getRandomSpawn() {
     return this._rnds;
   }

   public void setRandomSpawn(int num) {
     this._rnds = num;
   }



   public int getRndMinuteTime() {
     return this.rnd_time_minute;
   }

   public void setRndMinuteTime(int i) {
     this.rnd_time_minute = i;
   }

   public String[] getYoil() {
     return this.yoil;
   }

   public void setYoil(String[] yoil) {
     this.yoil = yoil;
   }


   public boolean isSpawnTime(int h, int m, long current_time) {
     String now_y = SystemUtil.getYoil(System.currentTimeMillis());
     boolean isYoil = false;
     for (String y : this.yoil) {
       if (y.equalsIgnoreCase("全部") || y.equalsIgnoreCase(now_y))
         isYoil = true;
     }
     if (!isYoil) {
       return false;
     }
     for (int[] t : this._bosstime) {
       if (t[0] == h && t[1] == m) {
         return true;
       }
     }
     return false;
   }

   private static Random _random = new Random(System.nanoTime());

   public void resetSpawnTime() {
     if (this.real_time == null || this._bosstime == null) {
       return;
     }



     for (int i = 0; i < this._bosstime.length; i++) {
       int[] t = this._bosstime[i];
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

       MJUIAdapter.on_boss_append(this._npcid, this._bossname + " " + t[0] + ":" + t[1] + " ", this._mapx, this._mapy, this._mapid);
     }
   }
 }


