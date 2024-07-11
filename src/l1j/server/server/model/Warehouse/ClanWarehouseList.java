package l1j.server.server.model.Warehouse;

public class ClanWarehouseList extends WarehouseList {
    protected ClanWarehouse createWarehouse(String name) {
        return new ClanWarehouse(name);
    }
}
