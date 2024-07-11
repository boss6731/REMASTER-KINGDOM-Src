 package l1j.server.server.model.Warehouse;

 public class WarehouseManager {
   private static WarehouseManager uniqueInstance = null;
   private PrivateWarehouseList plist = new PrivateWarehouseList();
   private ElfWarehouseList elist = new ElfWarehouseList();
   private ClanWarehouseList clist = new ClanWarehouseList();
   private SupplementaryServiceList supplist = new SupplementaryServiceList();
   private SpecialWarehouseList speciallist = new SpecialWarehouseList();

   public static WarehouseManager getInstance() {
     if (uniqueInstance == null)
       synchronized (ElfWarehouseList.class) {
         if (uniqueInstance == null) {
           uniqueInstance = new WarehouseManager();
         }
       }
     return uniqueInstance;
   }

   public PrivateWarehouse getPrivateWarehouse(String name) {
     return (PrivateWarehouse)this.plist.findWarehouse(name);
   }

   public ElfWarehouse getElfWarehouse(String name) {
     return (ElfWarehouse)this.elist.findWarehouse(name);
   }

   public ClanWarehouse getClanWarehouse(String name) {
     return (ClanWarehouse)this.clist.findWarehouse(name);
   }

   public SupplementaryService getSupplementaryService(String name) {
     return (SupplementaryService)this.supplist.findWarehouse(name);
   }

   public SpecialWarehouse getSpecialWarehouse(String name) {
     return (SpecialWarehouse)this.speciallist.findWarehouse(name);
   }



   public PrivateWarehouse getPrivateItems(String name) {
     return (PrivateWarehouse)this.plist.findWarehouse1(name);
   }

   public ElfWarehouse getElfItems(String name) {
     return (ElfWarehouse)this.elist.findWarehouse1(name);
   }

   public ClanWarehouse getClanItems(String name) {
     return (ClanWarehouse)this.clist.findWarehouse1(name);
   }

   public SupplementaryService getSupplementaryItems(String name) {
     return (SupplementaryService)this.supplist.findWarehouse1(name);
   }

   public void delPrivateWarehouse(String name) {
     this.plist.delWarehouse(name);
   }

   public void delElfWarehouse(String name) {
     this.elist.delWarehouse(name);
   }

   public void delClanWarehouse(String name) {
     this.clist.delWarehouse(name);
   }

   public void delSupplementaryService(String name) {
     this.supplist.delWarehouse(name);
   }

   public void delSpecialWarehouse(String name) {
     this.speciallist.delWarehouse(name);
   }
 }


