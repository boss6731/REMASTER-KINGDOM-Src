 package l1j.server.server.model.Warehouse;

 public class PrivateWarehouseList
   extends WarehouseList {
   protected PrivateWarehouse createWarehouse(String name) {
     return new PrivateWarehouse(name);
   }
 }


