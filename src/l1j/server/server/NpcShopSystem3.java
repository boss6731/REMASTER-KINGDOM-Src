 package l1j.server.server;

 import java.util.ArrayList;
 import java.util.List;
 import l1j.server.MJDShopSystem.MJDShopItem;
 import l1j.server.MJDShopSystem.MJDShopStorage;
 import l1j.server.server.datatables.NpcShopSpawnTable3;
 import l1j.server.server.datatables.NpcShopTable3;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1NpcShopInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.shop.L1Shop;
 import l1j.server.server.serverpackets.S_DoActionShop;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1NpcShop;
 import l1j.server.server.templates.L1ShopItem;

 public class NpcShopSystem3 implements Runnable {
   private static NpcShopSystem3 _instance;
   private boolean _power = false;

   public static NpcShopSystem3 getInstance() {
     if (_instance == null) {
       _instance = new NpcShopSystem3();
     }
     return _instance;
   }

   private static ArrayList<L1NpcInstance> _shops = new ArrayList<>(20);


   public void run() {
     try {
       if (isPower()) {
         NpcShopTable3.reloding();
         int size = _shops.size();
         for (int i = 0; i < size; i++) {
           shopRefill(_shops.get(i));
         }
         MJDShopStorage.updateProcess(_shops);
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   private void shopRefill(L1NpcInstance npc) {
     try {
       L1Shop shop = NpcShopTable3.getInstance().get(npc.getNpcId());
       if (shop == null) {
         return;
       }
       List<L1ShopItem> list = shop.getSellingItems();
       if (list == null) {
         return;
       }
       int size = list.size();
       MJDShopItem ditem = null; int i;
       for (i = 0; i < size; i++) {
         ditem = MJDShopItem.create(list.get(i), i, false);
         npc.addSellings(ditem);
       }

       list = shop.getBuyingItems();
       if (list == null) {
         return;
       }
       size = list.size();
       for (i = 0; i < size; i++) {
         ditem = MJDShopItem.create(list.get(i), i, true);
         npc.addPurchasings(ditem);
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }



   static class NpcShopTimer
     implements Runnable
   {
     public void run() {
       try {
         ArrayList<L1NpcShop> list = NpcShopSpawnTable3.getInstance().getList();
         for (int i = 0; i < list.size(); i++) {
           L1NpcShop shop = list.get(i);
           L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(shop.getNpcId());
           npc.setId(IdFactory.getInstance().nextId());
           npc.setMap(shop.getMapId());
           npc.getLocation().set(shop.getX(), shop.getY(), shop.getMapId());
           npc.getLocation().forward(5);
           npc.setHomeX(npc.getX());
           npc.setHomeY(npc.getY());
           npc.setHeading(shop.getHeading());
           npc.setName(shop.getName());
           npc.setTitle(shop.getTitle());
           L1NpcShopInstance obj = (L1NpcShopInstance)npc;
           obj.setShopName(shop.getShopName());
           L1World.getInstance().storeObject((L1Object)npc);
           L1World.getInstance().addVisibleObject((L1Object)npc);
           npc.getLight().turnOnOffLight();
           Thread.sleep(30L);
           obj.setState(1);
           Broadcaster.broadcastPacket((L1Character)npc, (ServerBasePacket)new S_DoActionShop(npc
                 .getId(), 70, shop.getShopName()));


           NpcShopSystem3._shops.add(npc);
           Thread.sleep(10L);
         }

       }
       catch (Exception exception) {
         return;
       } finally {
         GeneralThreadPool.getInstance().scheduleAtFixedRate(NpcShopSystem3.getInstance(), 0L, 3600000L);
       }
     }
   }


   public void npcShopStart() {
     NpcShopTimer ns = new NpcShopTimer();
     GeneralThreadPool.getInstance().execute(ns);
     this._power = true;
   }


   public void npcShopStop() {
     this._power = false;
     int size = _shops.size();
     for (int i = 0; i < size; i++) {
       L1NpcInstance npc = _shops.get(i);
       if (npc != null) {


         GeneralThreadPool.getInstance().execute((Runnable)new MJDShopStorage((L1Character)npc, true));
         npc.deleteMe();
       }
     }
   }
   public boolean isPower() {
     return this._power;
   }
 }


