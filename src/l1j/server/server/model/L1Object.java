 package l1j.server.server.model;

 import java.io.Serializable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;





 public class L1Object
         implements Serializable
 {
     private static final long serialVersionUID = 1L;
     private L1Location _loc = new L1Location();
     private int _id = 0;





















     public short getMapId() {
         return (short)this._loc.getMap().getId();
     }







     public void setMap(short mapId) {
         this._loc.setMap(L1WorldMap.getInstance().getMap(mapId));
     }


     private int _clusterX = -1; private int _clusterY = -1; private long effectdeletetime;

     public void setClusterPos(int x, int y) {
         this._clusterX = x;
         this._clusterY = y;
     } private L1Character _skilltarget; private int _skillId;
     public int getClusterX() {
         return this._clusterX; } public int getClusterY() {
         return this._clusterY;
     }







     public L1Map getMap() {
         return this._loc.getMap();
     }







     public void setMap(L1Map map) {
         if (map == null) {
             throw new NullPointerException();
         }
         this._loc.setMap(map);
     }







     public int getId() {
         return this._id;
     }







     public void setId(int id) {
         this._id = id;
     }






     public int getX() {
         return this._loc.getX();
     }







     public void setX(int x) {
         this._loc.setX(x);
     }







     public int getY() {
         return this._loc.getY();
     }







     public void setY(int y) {
         this._loc.setY(y);
     }







     public L1Location getLocation() {
         return this._loc;
     }

     public void setLocation(L1Location loc) {
         this._loc.setX(loc.getX());
         this._loc.setY(loc.getY());
         this._loc.setMap(loc.getMapId());
     }


     public void setLocation(int x, int y, int mapid) {
         this._loc.setX(x);
         this._loc.setY(y);
         this._loc.setMap(mapid);
     }





     public double getLineDistance(L1Object obj) {
         return getLocation().getLineDistance(obj.getLocation());
     }




     public int getTileLineDistance(L1Object obj) {
         return getLocation().getTileLineDistance(obj.getLocation());
     }




     public int getTileDistance(L1Object obj) {
         return getLocation().getTileDistance(obj.getLocation());
     }







     public void onPerceive(L1PcInstance perceivedFrom) {}







     public void onAction(L1PcInstance actionFrom) {}







     public void onAction(L1Character actionFrom) {}







     public void onAction(L1PcInstance actionFrom, int adddmg) {}







     public void onTalkAction(L1PcInstance talkFrom) {}






     public int getL1Type() {
         return 1;
     }

     public boolean instanceOf(int flg) {
         return ((getL1Type() & flg) > 0);
     }

     public void setEffectDeleteTime(long i) {
         this.effectdeletetime = i;
     }
     public long getEffectDeleteTime() {
         return this.effectdeletetime;
     }
     public void addEffectDeleteTime(long i) {
         this.effectdeletetime += i;
     }


     public void setTarget(L1Character cha) {
         this._skilltarget = cha;
     }
     public L1Character getTarget() {
         return this._skilltarget;
     }

     public void setSkillId(int skillid) {
         this._skillId = skillid;
     }
     public int getSkillId() {
         return this._skillId;
     }
 }


