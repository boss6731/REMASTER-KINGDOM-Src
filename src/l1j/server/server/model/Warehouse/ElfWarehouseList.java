 package l1j.server.server.model.Warehouse;

 public class ElfWarehouseList
   extends WarehouseList {
   protected ElfWarehouse createWarehouse(String name) {
     return new ElfWarehouse(name);
   }
 }


