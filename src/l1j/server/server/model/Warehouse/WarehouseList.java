 package l1j.server.server.model.Warehouse;

 import java.util.Vector;



 public abstract class WarehouseList
 {
   private Vector<Warehouse> warehouseList = new Vector<>();


   protected abstract Warehouse createWarehouse(String paramString);

   public synchronized Warehouse findWarehouse(String name) {
     Warehouse warehouse = null;
     for (Warehouse wh : this.warehouseList) {
       if (wh.getName().equalsIgnoreCase(name)) {
         return wh;
       }
     }
     warehouse = createWarehouse(name);
     this.warehouseList.add(warehouse);
     return warehouse;
   }
   public synchronized void delWarehouse(String accountName) {
     Warehouse iwilldie = null;
     for (Warehouse wh : this.warehouseList) {
       if (wh.getName().equalsIgnoreCase(accountName) && wh != null) {
         iwilldie = wh;
       }
     }
     if (iwilldie != null) {
       iwilldie.clearItems();
       this.warehouseList.remove(iwilldie);
       iwilldie = null;
     }
   }

   public synchronized Warehouse findWarehouse1(String name) {
     Warehouse warehouse = null;
     for (Warehouse wh : this.warehouseList) {
       if (wh.getName().equals(name)) {
         return wh;
       }
     }
     warehouse = createWarehouse(name);
     this.warehouseList.add(warehouse);
     return warehouse;
   }
   public synchronized void delWarehouse1(String accountName) {
     Warehouse iwilldie = null;
     for (Warehouse wh : this.warehouseList) {
       if (wh.getName().equals(accountName) && wh != null) {
         iwilldie = wh;
       }
     }
     if (iwilldie != null) {
       iwilldie.clearItems();
       this.warehouseList.remove(iwilldie);
       iwilldie = null;
     }
   }
 }


