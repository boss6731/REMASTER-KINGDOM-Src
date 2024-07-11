 package l1j.server.server.model.trap;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Location;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.storage.TrapStorage;

















 public class L1TeleportTrap
   extends L1Trap
 {
   private final L1Location _loc;

   public L1TeleportTrap(TrapStorage storage) {
     super(storage);

     int x = storage.getInt("teleportX");
     int y = storage.getInt("teleportY");
     int mapId = storage.getInt("teleportMapId");
     this._loc = new L1Location(x, y, mapId);
   }


   public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
     sendEffect(trapObj);


     trodFrom.start_teleport(this._loc.getX(), this._loc.getY(), this._loc.getMapId(), 5, 18339, true, false);
   }
 }


