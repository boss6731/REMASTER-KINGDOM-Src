 package l1j.server.server.model;

 import java.util.Random;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.types.Point;


















 public class L1Location
   extends Point
 {
   private static Random _random = new Random(System.nanoTime());

   protected L1Map _map = L1Map.newNull();


   public L1Location() {}


   public L1Location(L1Location loc) {
     this(loc._x, loc._y, loc._map);
   }

   public L1Location(int x, int y, int mapId) {
     super(x, y);
     setMap(mapId);
   }

   public L1Location(int x, int y, L1Map map) {
     super(x, y);
     this._map = map;
   }

   public L1Location(Point pt, int mapId) {
     super(pt);
     setMap(mapId);
   }

   public L1Location(Point pt, L1Map map) {
     super(pt);
     this._map = map;
   }

   public void set(L1Location loc) {
     this._map = loc._map;
     this._x = loc._x;
     this._y = loc._y;
   }

   public void set(int x, int y, int mapId) {
     set(x, y);
     setMap(mapId);
   }

   public void set(int x, int y, L1Map map) {
     set(x, y);
     this._map = map;
   }

   public void set(Point pt, int mapId) {
     set(pt);
     setMap(mapId);
   }

   public void set(Point pt, L1Map map) {
     set(pt);
     this._map = map;
   }

   public L1Map getMap() {
     return this._map;
   }

   public int getMapId() {
     return this._map.getId();
   }

   public void setMap(L1Map map) {
     this._map = map;
   }

   public void setMap(int mapId) {
     this._map = L1WorldMap.getInstance().getMap((short)mapId);
   }


   public boolean equals(Object obj) {
     if (!(obj instanceof L1Location)) {
       return false;
     }
     L1Location loc = (L1Location)obj;
     return (getMap() == loc.getMap() && getX() == loc.getX() &&
       getY() == loc.getY());
   }

   public boolean equals(L1Location loc) {
     return (getMap() == loc.getMap() && getX() == loc.getX() &&
       getY() == loc.getY());
   }


   public int hashCode() {
     return 7 * this._map.getId() + super.hashCode();
   }


   public String toString() {
     return String.format("(%d, %d) on %d", new Object[] { Integer.valueOf(this._x), Integer.valueOf(this._y), Integer.valueOf(this._map.getId()) });
   }




     // 返回一個範圍在 0 到 max 之間的隨機位置（適用於隨機傳送）
     public L1Location randomLocation(int max, boolean isRandomTeleport) {
         return randomLocation(0, max, isRandomTeleport);
     }

     // 返回一個範圍在 min 到 max 之間的隨機位置（適用於隨機傳送）
     public L1Location randomLocation(int min, int max, boolean isRandomTeleport) {
         return randomLocation(this, min, max, isRandomTeleport);
     }

     // 基於 baseLocation 產生一個範圍在 min 到 max 之間的隨機位置（適用於隨機傳送）
     public static L1Location randomLocation(L1Location baseLocation, int min, int max, boolean isRandomTeleport) {
         // 確認 min 不大於 max，否則拋出非法參數異常
         if (min > max) {
             throw new IllegalArgumentException("min > max 的參數是無效的");
         }
         // 如果 max 小於或等於 0，則返回 baseLocation 的副本
         if (max <= 0) {
             return new L1Location(baseLocation);
         }

         // 如果 min 值小於 5，則將 min 設為 5
         if (min < 5) {
             min = 5;
         }

         // 在這裡應該添加隨機生成位置的具體邏輯
         // ...
     }

     L1Location newLocation = new L1Location();
     int newX = 0;
     int newY = 0;
     int locX = baseLocation.getX();
     int locY = baseLocation.getY();
     short mapId = (short)baseLocation.getMapId();
     L1Map map = baseLocation.getMap();

     newLocation.setMap(map);

     int locX1 = locX - max;
     int locX2 = locX + max;
     int locY1 = locY - max;
     int locY2 = locY + max;

     int mapX1 = map.getX();
     int mapX2 = mapX1 + map.getWidth();
     int mapY1 = map.getY();
     int mapY2 = mapY1 + map.getHeight();

     if (locX1 < mapX1) {
       locX1 = mapX1;
     }
     if (locX2 > mapX2) {
       locX2 = mapX2;
     }
     if (locY1 < mapY1) {
       locY1 = mapY1;
     }
     if (locY2 > mapY2) {
       locY2 = mapY2;
     }

     int diffX = locX2 - locX1;
     int diffY = locY2 - locY1;

     int trial = 0;
     int amax = (int)Math.pow((1 + max * 2), 2.0D);
     int amin = (min == 0) ? 0 : (int)Math.pow((1 + (min - 1) * 2), 2.0D);
     int trialLimit = 40 * amax / (amax - amin);

     while (true) {
       if (trial >= trialLimit) {
         newLocation.set(locX, locY);
         break;
       }
       trial++;

       if (diffX < 0 || diffY < 0)
         continue;
       newX = locX1 + _random.nextInt(diffX + 1);
       newY = locY1 + _random.nextInt(diffY + 1);
       if (newX == locX && newY == locY) {
         continue;
       }
       newLocation.set(newX, newY);

       if (baseLocation.getTileLineDistance(newLocation) < min) {
         continue;
       }

       if (isRandomTeleport) {
         if (newX == locX && newY == locY) {
           continue;
         }
         if (L1CastleLocation.checkInAllWarArea(newX, newY, mapId)) {
           continue;
         }
         if (isNotEnableLoc(newX, newY, mapId)) {
           continue;
         }

         if (L1HouseLocation.isInHouse(newX, newY, mapId)) {
           continue;
         }
       }

       if (map.isInMap(newX, newY) && map.isPassable(newX, newY)) {
         break;
       }
     }
     return newLocation;
   }


     public static L1Location randomLocation2(int x, int y, L1Map maps, short mapid, int min, int max, boolean isRandomTeleport) {
// 確保 min 不大於 max，否則拋出非法參數異常
         if (min > max) {
             throw new IllegalArgumentException("min > max 的參數是無效的");
         }
// 如果 max 小於或等於 0，返回指定座標和地圖的 L1Location
         if (max <= 0) {
             return new L1Location(x, y, mapid);
         }
// 如果 min 小於 0，將 min 設為 0
         if (min < 0) {
             min = 0;
         }

// 隨機生成的具體邏輯應在此處添加
// ...
     }

     L1Location newLocation = new L1Location();
     int newX = 0;
     int newY = 0;
     int locX = x;
     int locY = y;
     short mapId = mapid;
     L1Map map = maps;

     newLocation.setMap(map);

     int locX1 = locX - max;
     int locX2 = locX + max;
     int locY1 = locY - max;
     int locY2 = locY + max;


     int mapX1 = map.getX();
     int mapX2 = mapX1 + map.getWidth();
     int mapY1 = map.getY();
     int mapY2 = mapY1 + map.getHeight();


     if (locX1 < mapX1) {
       locX1 = mapX1;
     }
     if (locX2 > mapX2) {
       locX2 = mapX2;
     }
     if (locY1 < mapY1) {
       locY1 = mapY1;
     }
     if (locY2 > mapY2) {
       locY2 = mapY2;
     }

     int diffX = locX2 - locX1;
     int diffY = locY2 - locY1;

     int trial = 0;

     int amax = (int)Math.pow((1 + max * 2), 2.0D);
     int amin = (min == 0) ? 0 : (int)Math.pow((1 + (min - 1) * 2), 2.0D);
     int trialLimit = 40 * amax / (amax - amin);

     while (true) {
       if (trial >= trialLimit) {
         newLocation.set(locX, locY);
         break;
       }
       trial++;

       newX = locX1 + _random.nextInt(diffX + 1);
       newY = locY1 + _random.nextInt(diffY + 1);

       newLocation.set(newX, newY);





       int a = Math.max(Math.abs(newLocation.getX() - x),
           Math.abs(newLocation.getY() - y));
       if (a < min) {
         continue;
       }
       if (isRandomTeleport) {
         if (L1CastleLocation.checkInAllWarArea(newX, newY, mapId)) {
           continue;
         }

         if (isNotEnableLoc(newX, newY, mapId)) {
           continue;
         }


         if (L1HouseLocation.isInHouse(newX, newY, mapId)) {
           continue;
         }
       }

       if (map.isInMap(newX, newY) && map.isPassable(newX, newY)) {
         break;
       }
     }
     return newLocation;
   }



   public static L1Location notOverlap(L1PcInstance pc, L1Location l1Location) {
     return null;
   }

   static boolean isNotEnableLoc(int x, int y, short mapId) {
     if (x >= 32704 && x <= 32835 && y >= 33110 && y <= 33234 && mapId == 4) {
       return true;
     }
     if (x >= 33472 && x <= 33536 && y >= 32838 && y <= 32876 && mapId == 4) {
       return true;
     }
     if (mapId == 30 && (x < 32718 || x > 32818 || y < 32718 || y > 32818)) {
       return true;
     }
     if (mapId == 492 && (x < 32738 || x > 32812 || y < 32916 || y > 32999)) {
       return true;
     }
     if (mapId == 285 && (x < 32657 || x > 32762 || y < 32778 || y > 32880)) {
       return true;
     }
     if (mapId == 15883 && (x < 32736 || x > 32924 || y < 32732 || y > 32928)) {
       return true;
     }
     return false;
   }
 }


