 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.Random;
 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.types.Point;

 public class ElementalStoneGenerator
   implements Runnable
 {
   public static final int SLEEP_TIME = 300000;
   private static final int ELVEN_FOREST_MAPID = 4;
   private static final int MAX_COUNT = Config.ServerAdSetting.ELEMENTALSTONEAMOUNT;

   private static final int INTERVAL = 3;
   private static final int FIRST_X = 32911;
   private static final int FIRST_Y = 32210;
   private static final int LAST_X = 33141;
   private static final int LAST_Y = 32500;
   private static final int ELEMENTAL_STONE_ID = 40515;
   private ArrayList<L1GroundInventory> _itemList = new ArrayList<>(MAX_COUNT);

   private Random _random = new Random(System.nanoTime());

   private static ElementalStoneGenerator _instance = null;

   private final L1Object _dummy;


   public static ElementalStoneGenerator getInstance() {
     if (_instance == null) {
       _instance = new ElementalStoneGenerator();
     }
     return _instance;
   }
   private ElementalStoneGenerator() {
     this._dummy = new L1Object();
   }



   private boolean canPut(L1Location loc) {
     this._dummy.setMap(loc.getMap());
     this._dummy.setX(loc.getX());
     this._dummy.setY(loc.getY());


     if (L1World.getInstance().getVisiblePlayer(this._dummy).size() > 0) {
       return false;
     }
     return true;
   }





   private Point nextPoint() {
     int newX = this._random.nextInt(230) + 32911;
     int newY = this._random.nextInt(290) + 32210;

     return new Point(newX, newY);
   }





   private void removeItemsPickedUp() {
     L1GroundInventory gInventory = null;
     for (int i = 0; i < this._itemList.size(); i++) {
       gInventory = this._itemList.get(i);
       if (!gInventory.checkItem(40515)) {
         this._itemList.remove(i);
         i--;
       }
     }
   }




   private void putElementalStone(L1Location loc) {
     L1GroundInventory gInventory = L1World.getInstance().getInventory(loc);

     L1ItemInstance item = ItemTable.getInstance().createItem(40515);

     item.setEnchantLevel(0);
     item.setCount(1);
     gInventory.storeItem(item);
     this._itemList.add(gInventory);
   }


   public void run() {
     try {
       L1Map map = L1WorldMap.getInstance().getMap((short)4);

       L1Location loc = null;

       removeItemsPickedUp();

       if (this._itemList.size() < MAX_COUNT) {
         loc = new L1Location(nextPoint(), map);

         if (canPut(loc))
         {
           putElementalStone(loc);
         }


         GeneralThreadPool.getInstance().schedule(this, 3000L);
       }
     } catch (Throwable e) {
       e.printStackTrace();
     }
   }
 }


