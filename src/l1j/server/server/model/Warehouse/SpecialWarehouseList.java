 package l1j.server.server.model.Warehouse;

 public class SpecialWarehouseList
   extends WarehouseList {
   protected SpecialWarehouse createWarehouse(String name) {
     return new SpecialWarehouse(name);
   }
 }


