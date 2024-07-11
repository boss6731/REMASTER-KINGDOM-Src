 package l1j.server.server.model;

 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.types.Rectangle;

 public class L1MapArea extends Rectangle {
   private L1Map _map = L1Map.newNull();

   public L1Map getMap() {
     return this._map;
   }

   public void setMap(L1Map map) {
     this._map = map;
   }

   public int getMapId() {
     return this._map.getId();
   }

   public L1MapArea(int left, int top, int right, int bottom, int mapId) {
     super(left, top, right, bottom);

     this._map = L1WorldMap.getInstance().getMap((short)mapId);
   }

   public boolean contains(L1Location loc) {
     return (this._map.getId() == loc.getMap().getId() && contains(loc));
   }
 }


