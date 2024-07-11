 package l1j.server.server.model.map;

 import MJFX.UIAdapter.MJUIAdapter;
 import java.util.Map;
 import l1j.server.MapReader;


 public class L1WorldMap
 {
   private static L1WorldMap _instance;
   private Map<Integer, L1Map> _maps;

   public static L1WorldMap getInstance() {
     if (_instance == null) {
       _instance = new L1WorldMap();
     }
     return _instance;
   }

     // L1WorldMap 的私有構造函數
     private L1WorldMap() {
         // 獲取默認的地圖讀取器
         MapReader in = MapReader.getDefaultReader();

         try {
             // 讀取地圖數據
             this._maps = in.read();
             // 如果讀取的地圖數據為 null，則拋出運行時異常
             if (this._maps == null) {
                 throw new RuntimeException("讀取地圖失敗");
             }
         } catch (Exception e) {
             // 捕獲並打印異常
             e.printStackTrace();
             // 調用 MJUIAdapter 的 on_exit 方法
             MJUIAdapter.on_exit();
         }
     }

   public L1Map getMap(short mapId) {
     L1Map map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       map = L1Map.newNull();
     }
     return map;
   }

   public boolean getMapCK(short mapId) {
     L1Map map = this._maps.get(Integer.valueOf(mapId));
     return (map != null);
   }


   public void cloneMap(int targetId, int newId) {
     L1Map copymap = null;
     copymap = ((L1Map)this._maps.get(Integer.valueOf(targetId))).copyMap(newId);
     this._maps.put(Integer.valueOf(newId), copymap);
   }

   public L1Map cloneMapAndGet(int targetId, int newId) {
     L1Map copymap = null;
     copymap = ((L1Map)this._maps.get(Integer.valueOf(targetId))).copyMap(newId);
     this._maps.put(Integer.valueOf(newId), copymap);
     return copymap;
   }


   public synchronized void addMap(L1Map map) {
     this._maps.put(Integer.valueOf(map.getId()), map);
   }

   public synchronized void removeMap(int mapId) {
     this._maps.remove(Integer.valueOf(mapId));
   }
 }


