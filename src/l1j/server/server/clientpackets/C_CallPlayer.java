 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Location;
 import l1j.server.server.model.L1World;

 public class C_CallPlayer
   extends ClientBasePacket {
   private static final String C_CALL = "[C] C_Call";

   public C_CallPlayer(byte[] decrypt, GameClient client) {
     super(decrypt);
     L1PcInstance pc = client.getActiveChar();
     if (pc == null) {
       return;
     }
     if (!pc.isGm()) {
       return;
     }

     String name = readS();
     if (name.isEmpty()) {
       return;
     }

     L1PcInstance target = L1World.getInstance().getPlayer(name);

     if (target == null) {
       return;
     }

     L1Location loc = L1Location.randomLocation(target.getLocation(), 1, 2, false);

     pc.start_teleport(loc.getX(), loc.getY(), target.getMapId(), pc.getHeading(), 18339, false, false);
   }


   public String getType() {
     return "[C] C_Call";
   }
 }


