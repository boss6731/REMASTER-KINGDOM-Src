 package l1j.server.server.model.map;
 import java.util.ArrayList;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.Config;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Location;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.types.Point;

 public class L1MapCluster {
   private static final int CLUSTER_SIZE = 20;
   private int _startX;
   private int _startY;
   private int _clusterX;
   private int _clusterY;
   private final ConcurrentHashMap<Integer, L1Object>[][] _objects;

   public L1MapCluster(int startX, int endX, int startY, int endY) {
     this._startX = startX / 20 * 20;
     int endPosX = (endX + 20 - 1) / 20 * 20;
     this._clusterX = (endPosX - this._startX) / 20;

     this._startY = startY / 20 * 20;
     int endPosY = (endY + 20 - 1) / 20 * 20;
     this._clusterY = (endPosY - this._startY) / 20;

     this._objects = (ConcurrentHashMap<Integer, L1Object>[][])new ConcurrentHashMap[this._clusterX][this._clusterY];
     for (int i = 0; i < this._clusterX; i++) {
       for (int j = 0; j < this._clusterY; j++)
         this._objects[i][j] = new ConcurrentHashMap<>();
     }
   }

   public int calculateClusterX(int x) {
     int clusterX = Math.max(x - this._startX, 0) / 20;
     if (clusterX >= this._clusterX) clusterX = this._clusterX - 1;
     return clusterX;
   }

   public int calculateClusterY(int y) {
     int clusterY = Math.max(y - this._startY, 0) / 20;
     if (clusterY >= this._clusterY) clusterY = this._clusterY - 1;
     return clusterY;
   }

   public void setObject(L1Object object) {
     int clusterX = calculateClusterX(object.getX());
     int clusterY = calculateClusterY(object.getY());
     object.setClusterPos(clusterX, clusterY);
     this._objects[clusterX][clusterY].put(Integer.valueOf(object.getId()), object);
   }

   public void setObject(L1Object object, int x, int y) {
     int clusterX = calculateClusterX(x);
     int clusterY = calculateClusterY(y);
     object.setClusterPos(clusterX, clusterY);
     this._objects[clusterX][clusterY].put(Integer.valueOf(object.getId()), object);
   }

   public void removeObject(L1Object object) {
     try {
       this._objects[object.getClusterX()][object.getClusterY()].remove(Integer.valueOf(object.getId()));
       object.setClusterPos(-1, -1);
     } catch (Exception exception) {}
   }



   public void onMove(L1Object object) {
     int clusterX = calculateClusterX(object.getX());
     int clusterY = calculateClusterY(object.getY());
     try {
       if (clusterX != object.getClusterX() || clusterY != object.getClusterY()) {
         this._objects[object.getClusterX()][object.getClusterY()].remove(Integer.valueOf(object.getId()));
         this._objects[clusterX][clusterY].put(Integer.valueOf(object.getId()), object);
         object.setClusterPos(clusterX, clusterY);
       }
     } catch (Exception exception) {}
   }

   public void onMove(L1Object object, int x, int y) {
     int clusterX = calculateClusterX(x);
     int clusterY = calculateClusterY(y);
     if (clusterX != object.getClusterX() || clusterY != object.getClusterY()) {
       this._objects[object.getClusterX()][object.getClusterY()].remove(Integer.valueOf(object.getId()));
       this._objects[clusterX][clusterY].put(Integer.valueOf(object.getId()), object);
       object.setClusterPos(clusterX, clusterY);
     }
   }

   public ArrayList<L1Object> getVisiblePoint(L1Location loc, int radius) {
     int mapId = loc.getMapId();
     ArrayList<L1Object> result = new ArrayList<>();
     int startX = calculateClusterX(loc.getX()) - 1;
     int startY = calculateClusterY(loc.getY()) - 1;
     for (int clusterX = startX; clusterX < startX + 3; clusterX++) {
       if (clusterX >= 0 && clusterX < this._clusterX)
         for (int clusterY = startY; clusterY < startY + 3; clusterY++) {
           if (clusterY >= 0 && clusterY < this._clusterY)
             for (L1Object target : this._objects[clusterX][clusterY].values()) {
               if (mapId == target.getMapId() &&
                 loc.getTileLineDistance((Point)target.getLocation()) <= radius) result.add(target);
             }
         }
     }
     return result;
   }

   public ArrayList<L1Object> getVisibleObjects(L1Object object, int radius) {
     int mapId = object.getMapId();
     L1Location l1Location = object.getLocation();
     ArrayList<L1Object> result = new ArrayList<>();
     int objectClusterX = calculateClusterX(object.getX());
     int objectClusterY = calculateClusterY(object.getY());
     int startX = objectClusterX - 1;
     int startY = objectClusterY - 1;
     for (int clusterX = startX; clusterX < startX + 3; clusterX++) {
       if (clusterX >= 0 && clusterX < this._clusterX)
         for (int clusterY = startY; clusterY < startY + 3; clusterY++) {
           if (clusterY >= 0 && clusterY < this._clusterY)
             for (L1Object target : this._objects[clusterX][clusterY].values()) {
               if (target.equals(object) ||
                 mapId != target.getMapId())
                 continue;  if (radius == -1) {
                 if (l1Location.isInScreen((Point)target.getLocation())) result.add(target);  continue;
               }  if (radius == 0) {
                 if (l1Location.isSamePoint((Point)target.getLocation())) result.add(target);  continue;
               }
               if (l1Location.getTileLineDistance((Point)target.getLocation()) <= radius) result.add(target);

             }
         }
     }
     return result;
   }

   public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object, int radius) {
     int mapId = object.getMapId();
     L1Location l1Location = object.getLocation();
     ArrayList<L1PcInstance> result = new ArrayList<>();
     int objectClusterX = calculateClusterX(object.getX());
     int objectClusterY = calculateClusterY(object.getY());
     int startX = objectClusterX - 1;
     int startY = objectClusterY - 1;
     for (int clusterX = startX; clusterX < startX + 3; clusterX++) {
       if (clusterX >= 0 && clusterX < this._clusterX)
         for (int clusterY = startY; clusterY < startY + 3; clusterY++) {
           if (clusterY >= 0 && clusterY < this._clusterY)
             for (L1Object target : this._objects[clusterX][clusterY].values()) {
               if (!(target instanceof L1PcInstance))
                 continue;  L1PcInstance targetPc = (L1PcInstance)target;
               if (targetPc.equals(object) ||
                 mapId != targetPc.getMapId())
                 continue;  if (radius == -1) {
                 if (l1Location.isInScreen((Point)target.getLocation())) result.add(targetPc);  continue;
               }  if (radius == 0) {
                 if (l1Location.isSamePoint((Point)target.getLocation())) result.add(targetPc);  continue;
               }
               if (l1Location.getTileLineDistance((Point)target.getLocation()) <= radius) result.add(targetPc);

             }
         }
     }
     return result;
   }

   public ArrayList<L1PcInstance> getVisiblePlayerExceptTargetSight(L1Object object, L1Object target) {
     L1Location l1Location1 = object.getLocation();
     L1Location l1Location2 = target.getLocation();
     ArrayList<L1PcInstance> result = new ArrayList<>();
     int objectClusterX = calculateClusterX(object.getX());
     int objectClusterY = calculateClusterY(object.getY());
     int startX = objectClusterX - 1;
     int startY = objectClusterY - 1;
     for (int clusterX = startX; clusterX < startX + 3; clusterX++) {
       if (clusterX >= 0 && clusterX < this._clusterX)
         for (int clusterY = startY; clusterY < startY + 3; clusterY++) {
           if (clusterY >= 0 && clusterY < this._clusterY) {
             for (L1Object targetObject : this._objects[clusterX][clusterY].values()) {
               if (!(targetObject instanceof L1PcInstance))
                 continue;  L1PcInstance element = (L1PcInstance)targetObject;
               if (element.equals(object))
                 continue;  if (Config.ServerAdSetting.PC_RECOGNIZE_RANGE == -1) {
                 if (l1Location1.isInScreen((Point)element.getLocation()) &&
                   !l1Location2.isInScreen((Point)element.getLocation())) result.add(element);
                 continue;
               }
               if (l1Location1.getTileLineDistance((Point)element.getLocation()) <= Config.ServerAdSetting.PC_RECOGNIZE_RANGE &&
                 l1Location2.getTileLineDistance((Point)element.getLocation()) > Config.ServerAdSetting.PC_RECOGNIZE_RANGE) result.add(element);

             }
           }
         }
     }
     return result;
   }
 }


