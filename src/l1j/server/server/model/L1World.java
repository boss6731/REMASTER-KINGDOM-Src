 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import java.util.function.Supplier;
 import java.util.logging.Logger;
 import java.util.stream.Collectors;
 import java.util.stream.Stream;
 import l1j.server.Config;
 import l1j.server.MJ3SEx.MJNpcSpeedData;
 import l1j.server.MJCompanion.Instance.MJCompanionInstance;
 import l1j.server.MJINNSystem.MJINNRoom;
 import l1j.server.MJTemplate.MJFindObjectFilter;
 import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.MJWarSystem.MJWar;
 import l1j.server.MJWebServer.Dispatcher.my.service.mapview.MJMyMapViewService;
 import l1j.server.server.model.Instance.L1CastleGuardInstance;
 import l1j.server.server.model.Instance.L1GuardInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1MerchantInstance;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1NpcShopInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.Instance.L1SummonInstance;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.types.Point;



 public class L1World
 {
     private static Logger _log = Logger.getLogger(L1World.class.getName());

     private final ConcurrentHashMap<String, L1PcInstance> _allPlayers;

     private final ConcurrentHashMap<Integer, L1PetInstance> _allPets;

     private final ConcurrentHashMap<Integer, MJCompanionInstance> _allCompanions;
     private final ConcurrentHashMap<Integer, L1SummonInstance> _allSummons;
     private final ConcurrentHashMap<Integer, L1Object> _allObjects;
     private final ConcurrentHashMap<Integer, L1Object>[] _visibleObjects;
     private final ConcurrentHashMap<Integer, L1PcInstance>[] _visiblePlayers;
     private final ConcurrentHashMap<Integer, MJWar> _allClanWars;
     private final ConcurrentHashMap<Integer, L1ItemInstance> _allitem;
     private final ConcurrentHashMap<Integer, L1Clan> _allClans;
     private final ConcurrentHashMap<Integer, L1NpcInstance> _allNpc;
     private final ConcurrentHashMap<Integer, L1NpcInstance> _allNpcObjects;
     private final ConcurrentHashMap<Integer, L1NpcShopInstance> _allShopNpc;
     private final ConcurrentHashMap<Integer, L1GuardInstance> _allGuard;
     private final ConcurrentHashMap<Integer, L1CastleGuardInstance> _allCastleGuard;
     private final ConcurrentHashMap<Integer, L1MerchantInstance> _allMerchant;
     private final ConcurrentHashMap<Integer, L1MerchantInstance> _allHastActionobjet;
     private int _weather = 4;

     private boolean _worldChatEnabled = true;

     private boolean _processingContributionTotal = false;

     private static final int MAX_MAP_ID = 32768;
     private static L1World _instance;
     private Collection<L1Object> _allValues;
     private Collection<L1PcInstance> _allPlayerValues;
     private Collection<L1NpcShopInstance> _allShopNpcValues;

     private L1World() {
         this._allPlayers = new ConcurrentHashMap<>(Config.Login.MaximumOnlineUsers);
         this._allPets = new ConcurrentHashMap<>(Config.Login.MaximumOnlineUsers / 16);
         this._allCompanions = new ConcurrentHashMap<>(Config.Login.MaximumOnlineUsers / 16);
         this._allSummons = new ConcurrentHashMap<>(Config.Login.MaximumOnlineUsers / 16);
         this._allMerchant = new ConcurrentHashMap<>(Config.Synchronization._allMerchant_HashMap);
         this._visibleObjects = (ConcurrentHashMap<Integer, L1Object>[])new ConcurrentHashMap[32769];

         this._visiblePlayers = (ConcurrentHashMap<Integer, L1PcInstance>[])new ConcurrentHashMap[32769];
         this._allClanWars = new ConcurrentHashMap<>(Config.Synchronization._allClanWars_HashMap);
         this._allClans = new ConcurrentHashMap<>(Config.Synchronization._allClans_HashMap);
         this._allNpc = new ConcurrentHashMap<>(Config.Synchronization._allNpc_HashMap + Config.Login.MaximumOnlineUsers / 2);
         this._allNpcObjects = new ConcurrentHashMap<>(Config.Synchronization._allNpcObjects_HashMap + Config.Login.MaximumOnlineUsers / 2);
         this._allShopNpc = new ConcurrentHashMap<>(Config.Synchronization._allShopNpc_HashMap);
         this._allitem = new ConcurrentHashMap<>(Config.Synchronization._allitem_HashMap * Config.Login.MaximumOnlineUsers);
         this._allGuard = new ConcurrentHashMap<>(Config.Synchronization._allGuard_HashMap);
         this._allCastleGuard = new ConcurrentHashMap<>(Config.Synchronization._allCastleGuard_HashMap);
         this._allHastActionobjet = new ConcurrentHashMap<>(9);
         this._allObjects = new ConcurrentHashMap<>(Config.Login.MaximumOnlineUsers * 4 + Config.Synchronization._allObjects_HashMap + 256 + 64 + Config.Login.MaximumOnlineUsers / 16);

         for (int i = 0; i <= 32768; i++) {
             if (i == 4) {
                 this._visibleObjects[i] = new ConcurrentHashMap<>(Config.Synchronization.Map4in_visibleObjects);
                 this._visiblePlayers[i] = new ConcurrentHashMap<>(Config.Login.MaximumOnlineUsers);
             } else {
                 this._visibleObjects[i] = new ConcurrentHashMap<>(Config.Synchronization.Map4out_visibleObjects);
                 this._visiblePlayers[i] = new ConcurrentHashMap<>(Config.Synchronization.Map4out_visiblePlayers);
             }
         }
     }
     private Collection<L1PetInstance> _allPetValues; private Collection<MJCompanionInstance> _allCompanionValues; private Collection<L1SummonInstance> _allSummonValues; private Collection<L1Clan> _allClanValues; private Collection<L1ItemInstance> _allItemValues; private Collection<L1MerchantInstance> _allMerchantValues; private Collection<L1MerchantInstance> _allHastActionobjetValues;
     public static L1World getInstance() {
         if (_instance == null) {
             _instance = new L1World();
         }
         return _instance;
     }





     public void clear() {
         _instance = new L1World();
     }

     public void storeObject(L1Object object) {
         if (object == null) {
             throw new NullPointerException();
         }

         this._allObjects.put(Integer.valueOf(object.getId()), object);
         if (object instanceof L1PcInstance) {
             L1PcInstance player = (L1PcInstance)object;
             this._allPlayers.put(((L1PcInstance)object).getName().toUpperCase(), player);
         } else if (object instanceof L1NpcInstance) {
             L1NpcInstance npc = (L1NpcInstance)object;
             this._allNpc.put(Integer.valueOf(npc.getNpcTemplate().get_npcId()), npc);
             this._allNpcObjects.put(Integer.valueOf(npc.getId()), npc);














             MJNpcSpeedData.install_npc(npc);
             if (object instanceof L1PetInstance) {
                 this._allPets.put(Integer.valueOf(object.getId()), (L1PetInstance)object);
             } else if (object instanceof L1MerchantInstance) {
                 L1MerchantInstance merchant = (L1MerchantInstance)object;
                 this._allMerchant.put(Integer.valueOf(object.getId()), (L1MerchantInstance)object);
                 if (merchant.getNpcTemplate().get_npcId() >= 120839 && merchant.getNpcTemplate().get_npcId() <= 120847) {
                     this._allHastActionobjet.put(Integer.valueOf(object.getId()), (L1MerchantInstance)object);
                 }
             } else if (object instanceof L1SummonInstance) {
                 this._allSummons.put(Integer.valueOf(object.getId()), (L1SummonInstance)object);
             } else if (object instanceof L1NpcShopInstance) {
                 this._allShopNpc.put(Integer.valueOf(object.getId()), (L1NpcShopInstance)object);
             } else if (object instanceof MJCompanionInstance) {
                 this._allCompanions.put(Integer.valueOf(object.getId()), (MJCompanionInstance)object);
             }
         }
     }

     public void removeObject(L1Object object) {
         if (object == null) {
             throw new NullPointerException();
         }

         if (object.instanceOf(512)) {
             L1ItemInstance item = (L1ItemInstance)object;
             if (item.getItemId() == 40312) {
                 MJINNRoom.remove(item);
             }
         }
         this._allObjects.remove(Integer.valueOf(object.getId()));
         if (object instanceof L1PcInstance) {
             L1PcInstance player = (L1PcInstance)object;
             this._allPlayers.remove(player.getName().toUpperCase());
         } else if (object instanceof L1NpcInstance) {
             this._allNpc.remove(Integer.valueOf(((L1NpcInstance)object).getNpcTemplate().get_npcId()));
             this._allNpcObjects.remove(Integer.valueOf(object.getId()));
             if (object instanceof L1PetInstance) {
                 this._allPets.remove(Integer.valueOf(object.getId()));
             } else if (object instanceof L1MerchantInstance) {
                 this._allMerchant.remove(Integer.valueOf(object.getId()));
             } else if (object instanceof L1SummonInstance) {
                 this._allSummons.remove(Integer.valueOf(object.getId()));
             } else if (object instanceof L1NpcShopInstance) {
                 this._allShopNpc.remove(Integer.valueOf(object.getId()));
             } else if (object instanceof MJCompanionInstance) {
                 this._allCompanions.remove(Integer.valueOf(object.getId()));
             }
         }
     }

     public L1Object findObject(int oID) {
         return this._allObjects.get(Integer.valueOf(oID));
     }




     public L1Object findObject(String name) {
         int nl = name.length();
         for (L1PcInstance each : getAllPlayers()) {
             String s = each.getName();
             if (s.length() != nl) {
                 continue;
             }
             if (s.equalsIgnoreCase(name)) {
                 return (L1Object)each;
             }
         }
         return null;
     }

     public L1Object findObjectfromNpcID(int id) {
         for (L1NpcInstance each : getAllNpcShop()) {
             int npcid = each.getNpcId();
             if (npcid != id) {
                 continue;
             }
             if (npcid == id) {
                 return (L1Object)each;
             }
         }

         return null;
     }



     public Collection<L1Object> getObject() {
         Collection<L1Object> vs = this._allValues;
         return (vs != null) ? vs : (this._allValues = Collections.unmodifiableCollection(this._allObjects.values()));
     }

     public L1GroundInventory getInventory(int x, int y, short map) {
         int inventoryKey = ((x - 30000) * 10000 + y - 30000) * -1;
         Object object = this._visibleObjects[map].get(Integer.valueOf(inventoryKey));
         if (object == null) {
             return new L1GroundInventory(inventoryKey, x, y, map);
         }
         return (L1GroundInventory)object;
     }


     public L1GroundInventory getInventory(L1Location loc) {
         return getInventory(loc.getX(), loc.getY(), (short)loc.getMap().getId());
     }

     public void addVisibleObject(L1Object object) {
         if (object == null) {
             return;
         }
         int map_id = object.getMapId();
         int id = object.getId();
         if (map_id <= 32768) {
             MJMyMapViewService.service().onAppendObject(map_id, object);
             this._visibleObjects[map_id].put(Integer.valueOf(id), object);
             if (object.instanceOf(4)) {
                 this._visiblePlayers[map_id].put(Integer.valueOf(id), (L1PcInstance)object);
             }
         }
     }

     public void removeVisibleObject(L1Object object) {
         if (object == null) {
             return;
         }
         int map_id = object.getMapId();
         int id = object.getId();
         if (map_id <= 32768) {
             MJMyMapViewService.service().onRemoveObject(map_id, id);
             this._visibleObjects[map_id].remove(Integer.valueOf(id));
             this._visiblePlayers[map_id].remove(Integer.valueOf(id));
         }
     }

     public void moveVisibleObject(L1Object object, int newMap) {
         if (object == null) {
             return;
         }
         int map_id = object.getMapId();
         int id = object.getId();
         if (map_id != newMap) {
             if (map_id <= 32768) {
                 MJMyMapViewService.service().onRemoveObject(map_id, id);
                 this._visibleObjects[map_id].remove(Integer.valueOf(id));
                 this._visiblePlayers[map_id].remove(Integer.valueOf(id));
             }
             if (newMap <= 32768) {
                 MJMyMapViewService.service().onAppendObject(newMap, object);
                 this._visibleObjects[newMap].put(Integer.valueOf(id), object);
                 if (object.instanceOf(4)) {
                     this._visiblePlayers[newMap].put(Integer.valueOf(id), (L1PcInstance)object);
                 }
             }
         }
     }

     private ConcurrentHashMap<Integer, Integer> createLineMap(Point src, Point target) {
         ConcurrentHashMap<Integer, Integer> lineMap = new ConcurrentHashMap<>(src.getTileDistance(target));









         int x0 = src.getX();
         int y0 = src.getY();
         int x1 = target.getX();
         int y1 = target.getY();
         int sx = (x1 > x0) ? 1 : -1;
         int dx = (x1 > x0) ? (x1 - x0) : (x0 - x1);
         int sy = (y1 > y0) ? 1 : -1;
         int dy = (y1 > y0) ? (y1 - y0) : (y0 - y1);

         int x = x0;
         int y = y0;

         if (dx >= dy) {
             int E = -dx;
             for (int i = 0; i <= dx; i++) {
                 int key = (x << 16) + y;
                 lineMap.put(Integer.valueOf(key), Integer.valueOf(key));
                 x += sx;
                 E += 2 * dy;
                 if (E >= 0) {
                     y += sy;
                     E -= 2 * dx;
                 }
             }
         } else {

             int E = -dy;
             for (int i = 0; i <= dy; i++) {
                 int key = (x << 16) + y;
                 lineMap.put(Integer.valueOf(key), Integer.valueOf(key));
                 y += sy;
                 E += 2 * dx;
                 if (E >= 0) {
                     x += sx;
                     E -= 2 * dy;
                 }
             }
         }

         return lineMap;
     }

     public ArrayList<L1Object> getVisibleLineObjects(L1Object src, L1Object target) {
         ConcurrentHashMap<Integer, Integer> lineMap = createLineMap(src.getLocation(), target.getLocation());

         int map = target.getMapId();
         Collection<L1Object> col = this._visibleObjects[map].values();
         if (col.size() <= 0 || map > 32768) {
             return null;
         }
         ArrayList<L1Object> result = new ArrayList<>(lineMap.size());
         for (L1Object element : col) {
             if (element == null || element.equals(src)) {
                 continue;
             }
             int key = (element.getX() << 16) + element.getY();
             if (lineMap.containsKey(Integer.valueOf(key))) {
                 result.add(element);
             }
         }
         return result;
     }

     public ArrayList<L1Object> getVisibleBoxObjects(L1Object object, int heading, int width, int height) {
         int x = object.getX();
         int y = object.getY();
         int map = object.getMapId();
         Collection<L1Object> col = this._visibleObjects[map].values();
         if (col.size() <= 0 || map > 32768) {
             return null;
         }
         L1Location location = object.getLocation();
         ArrayList<L1Object> result = new ArrayList<>(width * height);
         int[] headingRotate = { 6, 7, 0, 1, 2, 3, 4, 5 };
         double cosSita = Math.cos(headingRotate[heading] * Math.PI / 4.0D);
         double sinSita = Math.sin(headingRotate[heading] * Math.PI / 4.0D);

         if (map <= 32768) {
             for (L1Object element : col) {
                 if (element == null || element.equals(object)) {
                     continue;
                 }
                 if (map != element.getMapId()) {
                     continue;
                 }
                 if (location.isSamePoint(element.getLocation())) {
                     result.add(element);
                     continue;
                 }
                 int distance = location.getTileLineDistance(element.getLocation());

                 if (distance > height && distance > width) {
                     continue;
                 }


                 int x1 = element.getX() - x;
                 int y1 = element.getY() - y;


                 int rotX = (int)Math.round(x1 * cosSita + y1 * sinSita);
                 int rotY = (int)Math.round(-x1 * sinSita + y1 * cosSita);

                 int xmin = 0;
                 int xmax = height;
                 int ymin = -width;
                 int ymax = width;




                 if (rotX > xmin && distance <= xmax && rotY >= ymin && rotY <= ymax) {
                     result.add(element);
                 }
             }
         }
         return result;
     }

     public final Collection<L1PcInstance> getVisiblePlayers(int map_id) {
         return this._visiblePlayers[map_id].values();
     }

     public void broadcast_map(int map_id, String message) {
         broadcast_map(map_id, (ServerBasePacket)new S_SystemMessage(message));
     }

     public void broadcast_map(int map_id, ServerBasePacket pck) {
         broadcast_map(map_id, pck, true);
     }

     public void broadcast_map(int map_id, ServerBasePacket pck, boolean is_clear) {
         for (L1PcInstance pc : getVisiblePlayers(map_id)) {
             if (pc != null)
                 pc.sendPackets(pck, false);
         }
         if (is_clear)
             pck.clear();
     }

     public void broadcast_map(int map_id, ServerBasePacket[] pcks) {
         broadcast_map(map_id, pcks, true);
     }

     public void broadcast_map(int map_id, ServerBasePacket[] pcks, boolean is_clear) {
         for (L1PcInstance pc : getVisiblePlayers(map_id)) {
             if (pc != null)
                 pc.sendPackets(pcks, false);
         }
         if (is_clear) {
             int len = pcks.length;
             for (int i = len - 1; i >= 0; i--) {
                 pcks[i].clear();
                 pcks[i] = null;
             }
         }
     }

     public ArrayList<L1Object> getVisibleObjects(L1Object object) {
         return getVisibleObjects(object, -1);
     }

     public ArrayList<L1Object> getVisibleObjects(L1Object object, int radius) {
         L1Map map = object.getMap();
         Point pt = object.getLocation();
         ArrayList<L1Object> result = new ArrayList<>();









         if (map.getId() <= 32768) {
             Collection<L1Object> col = this._visibleObjects[map.getId()].values();
             for (L1Object element : col) {
                 if (element == null || element.equals(object)) {
                     continue;
                 }
                 if (map != element.getMap()) {
                     continue;
                 }

                 if (radius == -1) {
                     if (pt.isInScreen(element.getLocation()))
                         result.add(element);  continue;
                 }
                 if (radius == 0) {
                     if (pt.isSamePoint(element.getLocation()))
                         result.add(element);
                     continue;
                 }
                 if (pt.getTileLineDistance(element.getLocation()) <= radius) {
                     result.add(element);
                 }
             }
         }


         return result;
     }

     public boolean isVisibleNpc(int x, int y, int mid) {
         Collection<L1Object> col = this._visibleObjects[mid].values();
         for (L1Object obj : col) {
             if (obj == null || obj.getMapId() != mid) {
                 continue;
             }
             if (obj.getX() == x && obj.getY() == y &&
                     obj instanceof L1NpcInstance) {
                 return true;
             }
         }

         return false;
     }

     public boolean isVisibleObject(int x, int y, int mid) {
         Collection<L1Object> col = this._visibleObjects[mid].values();
         for (L1Object obj : col) {
             if (obj == null || obj.getMapId() != mid) {
                 continue;
             }
             if (obj.getX() == x && obj.getY() == y) {
                 if (obj instanceof L1PcInstance) {
                     if (!((L1PcInstance)obj).isInvisble())
                         return true;  continue;
                 }  if (obj instanceof L1MonsterInstance) {
                     if (((L1MonsterInstance)obj).getHiddenStatus() < 1)
                         return true;  continue;
                 }
                 return true;
             }
         }

         return false;
     }

     public ArrayList<L1Object> getVisiblePoint(L1Location loc, int radius) {
         int mapId = loc.getMapId();
         Collection<L1Object> col = this._visibleObjects[mapId].values();
         if (col.size() <= 0 || mapId > 32768)
             return new ArrayList<>();
         ArrayList<L1Object> result = new ArrayList<>(radius * 8);
         for (L1Object element : this._visibleObjects[mapId].values()) {
             if (element == null || mapId != element.getMapId()) {
                 continue;
             }
             if (loc.getTileLineDistance(element.getLocation()) <= radius)
                 result.add(element);
         }
         return result;
     }

     public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object) {
         return getVisiblePlayer(object, -1);
     }

     public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object, int radius) {
         int map = object.getMapId();
         Point pt = object.getLocation();
         Collection<L1PcInstance> pc = this._allPlayers.values();
         ArrayList<L1PcInstance> result = new ArrayList<>();










         for (L1PcInstance element : pc) {
             if (element == null || element.equals(object) || map != element.getMapId())
                 continue;
             if (radius == -1) {
                 if (pt.isInScreen(element.getLocation()))
                     result.add(element);  continue;
             }  if (radius == 0) {
                 if (pt.isSamePoint(element.getLocation()))
                     result.add(element);  continue;
             }
             if (pt.getTileLineDistance(element.getLocation()) <= radius) {
                 result.add(element);
             }
         }

         return result;
     }

     public boolean getVisibleNpc(L1Location loc, L1NpcInstance object) {
         return getVisibleNpc(loc, object, -1);
     }

     public boolean getVisibleNpc(L1Location loc, L1NpcInstance object, int radius) {
         int map = loc.getMapId();
         Point pt = loc;
         Collection<L1NpcInstance> npc = this._allNpc.values();
         boolean result = false;

         for (L1NpcInstance element : npc) {
             if (element == null || element.equals(object) || map != element.getMapId())
                 continue;
             if (radius == -1) {
                 if (pt.isInScreen(element.getLocation()))
                     result = true;  continue;
             }  if (radius == 0) {
                 if (pt.isSamePoint(element.getLocation()))
                     result = true;  continue;
             }
             if (pt.getTileLineDistance(element.getLocation()) <= radius) {
                 result = true;
             }
         }

         return result;
     }



     public ArrayList<L1PcInstance> getVisiblePlayerExceptTargetSight(L1Object object, L1Object target) {
         int map = object.getMapId();
         Point objectPt = object.getLocation();
         Point targetPt = target.getLocation();
         Collection<L1Object> col = this._visibleObjects[map].values();
         ArrayList<L1PcInstance> result = new ArrayList<>(64);
         for (L1Object targetObject : col) {
             if (!(targetObject instanceof L1PcInstance)) {
                 continue;
             }

             L1PcInstance element = (L1PcInstance)targetObject;

             if (element == null || element.equals(object)) {
                 continue;
             }

             if (Config.Connection.PcRecognizeRange == -1) {
                 if (objectPt.isInScreen(element.getLocation()) && !targetPt.isInScreen(element.getLocation()))
                     result.add(element);  continue;
             }
             if (objectPt.getTileLineDistance(element.getLocation()) <= Config.Connection.PcRecognizeRange && targetPt
                     .getTileLineDistance(element.getLocation()) > Config.Connection.PcRecognizeRange) {
                 result.add(element);
             }
         }
         return result;
     }







     public ArrayList<L1PcInstance> getRecognizePlayer(L1Object object) {
         return getVisiblePlayer(object, Config.Connection.PcRecognizeRange);
     }

     public L1PcInstance[] getAllPlayers3() {
         return (L1PcInstance[])this._allPlayers.values().toArray((Object[])new L1PcInstance[this._allPlayers.size()]);
     }



     public Collection<L1PcInstance> getAllPlayers() {
         Collection<L1PcInstance> vs = this._allPlayerValues;
         return (vs != null) ? vs : (this._allPlayerValues = Collections.unmodifiableCollection(this._allPlayers.values()));
     }

     public int getAllPlayersSize() {
         return this._allPlayers.size();
     }

     public Stream<L1PcInstance> getAllPlayerStream() {
         Collection<L1PcInstance> col = getAllPlayers();
         return (col.size() > 1024) ? col.parallelStream() : col.stream();
     }

     public ArrayList<L1PcInstance> createAllPlayerArrayList() {
         return new ArrayList<>(this._allPlayers.values());
     }

     public Collection<L1NpcShopInstance> getAllNpcShop() {
         return Collections.unmodifiableCollection(this._allShopNpc.values());
     }

     public Collection<L1NpcInstance> getAllNpc() {
         return Collections.unmodifiableCollection(this._allNpc.values());
     }

     public Collection<L1Object> getAllObj() {
         return Collections.unmodifiableCollection(this._allObjects.values());
     }

     public Collection<L1GuardInstance> getAllGuard() {
         return Collections.unmodifiableCollection(this._allGuard.values());
     }

     public Collection<L1CastleGuardInstance> getAllCastleGuard() {
         return Collections.unmodifiableCollection(this._allCastleGuard.values());
     }








     public L1PcInstance getPlayer(String name) {
         if (null == name)
             return null;
         return this._allPlayers.get(name.toUpperCase());
     }

     public int get_npc_size() {
         return this._allNpcObjects.size();
     }
     public int get_object_size() {
         return this._allObjects.size();
     }
     public int get_player_size() {
         return this._allPlayers.size();
     }
     public int get_item_size() {
         return this._allitem.size();
     }
     public L1ItemInstance get_item(int id) {
         return this._allitem.get(Integer.valueOf(id));
     }








     public L1NpcShopInstance getShopNpc(String name) {
         Collection<L1NpcShopInstance> npc = null;
         npc = getAllShopNpc();
         int nl = name.length();
         for (L1NpcShopInstance each : npc) {
             if (each == null) {
                 continue;
             }
             String s = each.getName();
             if (s.length() != nl) {
                 continue;
             }
             if (s.equalsIgnoreCase(name)) {
                 return each;
             }
         }
         return null;
     }




     public Collection<L1NpcShopInstance> getAllShopNpc() {
         Collection<L1NpcShopInstance> vs = this._allShopNpcValues;
         return (vs != null) ? vs : (this._allShopNpcValues = Collections.unmodifiableCollection(this._allShopNpc.values()));
     }




     public Collection<L1PetInstance> getAllPets() {
         Collection<L1PetInstance> vs = this._allPetValues;
         return (vs != null) ? vs : (this._allPetValues = Collections.unmodifiableCollection(this._allPets.values()));
     }


     public Collection<MJCompanionInstance> getAllCompanions() {
         Collection<MJCompanionInstance> vs = this._allCompanionValues;
         return (vs != null) ? vs : (this._allCompanionValues = Collections.unmodifiableCollection(this._allCompanions.values()));
     }

     public boolean contains_companion(int companion_id) {
         return this._allCompanions.contains(Integer.valueOf(companion_id));
     }




     public Collection<L1SummonInstance> getAllSummons() {
         Collection<L1SummonInstance> vs = this._allSummonValues;
         return (vs != null) ? vs : (this._allSummonValues = Collections.unmodifiableCollection(this._allSummons.values()));
     }

     public final Map<Integer, L1Object> getVisibleObjects(int mapId) {
         return this._visibleObjects[mapId];
     }

     public void addWar(MJWar war) {
         this._allClanWars.put(Integer.valueOf(war.getId()), war);
     }

     public MJWar removeWar(MJWar war) {
         return this._allClanWars.remove(Integer.valueOf(war.getId()));
     }

     public Stream<MJWar> createWarStream() {
         return (this._allClanWars.size() <= 0) ? null : this._allClanWars.values().stream();
     }

     public void storeClan(L1Clan clan) {
         this._allClans.put(Integer.valueOf(clan.getClanId()), clan);
     }

     public void removeClan(L1Clan clan) {
         this._allClans.remove(Integer.valueOf(clan.getClanId()));
     }

     public void clearClan() {
         this._allClans.clear();
     }

     public L1Clan findClan(String clanName) {
         if (clanName == null)
             return null;
         int len = clanName.length();
         for (L1Clan clan : getAllClans()) {
             String name = clan.getClanName();
             if (name.length() != len) {
                 continue;
             }
             if (name.equalsIgnoreCase(clanName))
                 return clan;
         }
         return null;
     }

     public L1Clan getClan(int clanId) {
         return this._allClans.get(Integer.valueOf(clanId));
     }




     public Collection<L1Clan> getAllClans() {
         Collection<L1Clan> vs = this._allClanValues;
         return (vs != null) ? vs : (this._allClanValues = Collections.unmodifiableCollection(this._allClans.values()));
     }

     public void setWeather(int weather) {
         this._weather = weather;
     }

     public int getWeather() {
         return this._weather;
     }

     public void set_worldChatElabled(boolean flag) {
         this._worldChatEnabled = flag;
     }

     public boolean isWorldChatElabled() {
         return this._worldChatEnabled;
     }

     public void setProcessingContributionTotal(boolean flag) {
         this._processingContributionTotal = flag;
     }

     public boolean isProcessingContributionTotal() {
         return this._processingContributionTotal;
     }

     public L1Object[] getObject2() {
         return (L1Object[])this._allObjects.values().toArray((Object[])new L1Object[this._allObjects.size()]);
     }







     public void broadcastPacketToAll(ServerBasePacket[] pcks) {
         for (L1PcInstance pc : getAllPlayers()) {
             if (pc == null) {
                 continue;
             }
             for (ServerBasePacket pck : pcks) {
                 pc.sendPackets(pck, false);
             }
         }
         for (ServerBasePacket pck : pcks)
             pck.clear();
     }

     public void broadcastPacketToAll(ServerBasePacket[] pcks, boolean isClear) {
         for (L1PcInstance pc : getAllPlayers()) {
             if (pc == null) {
                 continue;
             }
             for (ServerBasePacket pck : pcks) {
                 pc.sendPackets(pck, false);
             }
         }
         if (isClear)
             for (ServerBasePacket pck : pcks) {
                 pck.clear();
             }
     }

     public void broadcastPacketToAll(String message) {
         broadcastPacketToAll((ServerBasePacket)new S_SystemMessage(message));
     }

     public void broadcastPacketToAll(ProtoOutputStream stream, boolean clear) {
         for (L1PcInstance pc : getAllPlayers()) {
             if (pc == null) {
                 continue;
             }
             pc.sendPackets(stream, false);
         }
         if (clear) {
             stream.dispose();
         }
     }

     public void broadcastPacketToAll(ProtoOutputStream stream) {
         broadcastPacketToAll(stream, true);
     }

     public void broadcastPacketToAll(ServerBasePacket packet) {
         for (L1PcInstance pc : getAllPlayers()) {
             if (pc != null) {
                 pc.sendPackets(packet, false);
             }
         }
         packet.clear();
         packet = null;
     }

     public void broadcastPacketToAll(ServerBasePacket packet, boolean clear) {
         Collection<L1PcInstance> pclist = null;
         pclist = getAllPlayers();
         _log.finest("玩家須知： " + pclist.size());
         for (L1PcInstance pc : pclist) {
             if (pc != null)
                 pc.sendPackets(packet, false);
         }
         if (clear) {
             packet.clear();
             packet = null;
         }
     }

     public void getMapObject(int mapid) {
         Collection<L1Object> result = new ArrayList<>();
         result = this._visibleObjects[mapid].values();
         for (L1Object obj : result) {
             if (obj instanceof L1MonsterInstance) {
                 L1MonsterInstance mon = (L1MonsterInstance)obj;
                 if (mon.getHiddenStatus() != 0 || mon.isDead()) {
                     continue;
                 }
                 if (mon.getMapId() == mapid) {
                     mon.re();
                 }
             }
         }
     }






     public void broadcastServerMessage(String message) {
         broadcastPacketToAll((ServerBasePacket)new S_SystemMessage(message));
     }

     public L1NpcInstance findNpc(int id) {
         return this._allNpc.get(Integer.valueOf(id));
     }

     public L1NpcInstance findNpc(String name) {
         int nl = name.length();
         for (L1NpcInstance each : getAllNpc()) {
             String s = each.getName();
             if (s.length() != nl) {
                 continue;
             }
             if (s.equalsIgnoreCase(name)) {
                 return each;
             }
         }
         return null;
     }

     public L1PcInstance findPlayer(String name) {
         int nl = name.length();
         for (L1PcInstance each : getAllPlayers()) {
             String s = each.getName();
             if (s.length() != nl) {
                 continue;
             }
             if (s.equalsIgnoreCase(name)) {
                 return each;
             }
         }
         return null;
     }



     public Collection<L1ItemInstance> getAllItem() {
         Collection<L1ItemInstance> vs = this._allItemValues;
         return (vs != null) ? vs : (this._allItemValues = Collections.unmodifiableCollection(this._allitem.values()));
     }

     public L1PcInstance findpc(String name) {
         int nl = name.length();
         for (L1PcInstance each : getAllPlayers()) {
             String s = each.getName();
             if (nl != s.length()) {
                 continue;
             }
             if (s.equalsIgnoreCase(name)) {
                 return each;
             }
         }
         return null;
     }

     public Stream<L1Object> createVisibleObjectsStream(int mapId) {
         Collection<L1Object> col = this._visibleObjects[mapId].values();
         return (col.size() > 1024) ? col.parallelStream() : col.stream();
     }

     public Stream<L1Object> createVisibleObjectsStream(int mapId, MJFindObjectFilter filter) {
         Stream<L1Object> stream = createVisibleObjectsStream(mapId);
         return stream.filter(vobj -> !filter.isFilter(vobj));
     }



     public Stream<L1Object> createVisibleObjectsStream(L1Object obj) {
         return createVisibleObjectsStream(obj, -1);
     }

     public Stream<L1Object> createVisibleObjectsStream(L1Object obj, int radius) {
         L1Map m = obj.getMap();
         int mid = m.getId();
         Point pt = obj.getLocation();
         Stream<L1Object> stream = null;
         if (mid <= 32768) {
             stream = createVisibleObjectsStream(mid);
             return stream.filter(vobj ->
                     (vobj != null && vobj.getId() != obj.getId() && vobj.getMapId() == mid && ((radius > 0 && pt.getTileLineDistance(vobj.getLocation()) <= radius) || (radius == -1 && pt.isInScreen(vobj.getLocation())) || (radius == 0 && pt.isSamePoint(vobj.getLocation())))));
         }








         return stream;
     }

     public L1Object findVisibleObjectFromPosition(int x, int y, int mapId) {
         for (L1Object obj : this._visibleObjects[mapId].values()) {
             if (obj.getX() == x && obj.getY() == y) {
                 return obj;
             }
         }
         return null;
     }

     public L1Object findVisibleCharacterFromPosition(int x, int y, int mapId) {
         for (L1Object obj : this._visibleObjects[mapId].values()) {
             if ((obj instanceof L1PcInstance || obj instanceof L1NpcInstance || obj instanceof L1MonsterInstance || obj instanceof L1SummonInstance || obj instanceof L1PetInstance || obj instanceof MJCompanionInstance) &&
                     obj.getX() == x && obj.getY() == y) {
                 return obj;
             }
         }

         return null;
     }

     public L1PcInstance findVisiblePlayerFromPosition(int x, int y, int mapId) {
         for (L1PcInstance obj : this._visiblePlayers[mapId].values()) {
             if (obj.getX() == x && obj.getY() == y) {
                 return obj;
             }
         }
         return null;
     }

     public ArrayList<L1Object> findVisibleObjectFromPosition(L1Object obj) {
         int id = obj.getId();
         int mid = obj.getMapId();
         int x = obj.getX();
         int y = obj.getY();
         if (mid > 32768)
             return new ArrayList<>();
         Stream<L1Object> stream = createVisibleObjectsStream(mid);
         return (ArrayList<L1Object>)stream.filter(vobj ->
                         (vobj != null && vobj.getId() != id && vobj.getMapId() == mid && vobj.getX() == x && vobj.getY() == y))




                 .collect(Collectors.toCollection(ArrayList::new));
     }

     public ArrayList<L1Object> findVisibleObjectFromPosition(L1Object obj, MJFindObjectFilter filter) {
         int id = obj.getId();
         int mid = obj.getMapId();
         int x = obj.getX();
         int y = obj.getY();
         if (mid > 32768)
             return new ArrayList<>();
         Stream<L1Object> stream = createVisibleObjectsStream(mid);
         return (ArrayList<L1Object>)stream.filter(vobj ->
                         (vobj != null && vobj.getId() != id && vobj.getMapId() == mid && vobj.getX() == x && vobj.getY() == y && !filter.isFilter(vobj)))





                 .collect(Collectors.toCollection(ArrayList::new));
     }

     public long findVisibleObjectFromPositionCount(int id, int x, int y, int mid) {
         if (mid > 32768)
             return 0L;
         Stream<L1Object> stream = createVisibleObjectsStream(mid);
         return stream.filter(vobj ->
                         (vobj != null && vobj.getId() != id && vobj.getMapId() == mid && vobj.getX() == x && vobj.getY() == y))




                 .count();
     }

     public long findVisibleObjectFromPositionCount(int id, int x, int y, int mid, MJFindObjectFilter filter) {
         if (mid > 32768)
             return 0L;
         Stream<L1Object> stream = createVisibleObjectsStream(mid);
         return stream.filter(vobj ->
                         (vobj != null && vobj.getId() != id && vobj.getMapId() == mid && vobj.getX() == x && vobj.getY() == y && !filter.isFilter(vobj)))





                 .count();
     }

     public Collection<L1Clan> getRealClans() {
         ArrayList<L1Clan> clans = new ArrayList<>();
         for (L1Clan clan : getAllClans()) {
             if (clan.isRedKnight())
                 continue;
             if (MJString.isNullOrEmpty(clan.getLeaderName())) {
                 continue;
             }
             clans.add(clan);
         }
         return clans;
     }




     public Collection<L1MerchantInstance> getAllMerchant() {
         Collection<L1MerchantInstance> vs = this._allMerchantValues;
         return (vs != null) ? vs : (this._allMerchantValues = Collections.unmodifiableCollection(this._allMerchant.values()));
     }

     public L1Object isNpcShop(int id) {
         Collection<L1MerchantInstance> npc = getAllMerchant();
         for (L1MerchantInstance each : npc) {
             if (each != null &&
                     each.getNpcId() == id) {
                 return this._allObjects.get(Integer.valueOf(each.getId()));
             }
         }
         return null;
     }

     public void castPacketGm(ServerBasePacket packet) {
         for (L1PcInstance pc : getAllPlayers()) {
             if (pc != null && pc.isGm())
                 pc.sendPackets(packet, false);
         }
         packet.clear();
     }



     public Collection<L1MerchantInstance> getAllHastActionobjet() {
         Collection<L1MerchantInstance> vs = this._allHastActionobjetValues;
         return (vs != null) ? vs : (this
                 ._allHastActionobjetValues = Collections.unmodifiableCollection(this._allHastActionobjet.values()));
     }
 }


