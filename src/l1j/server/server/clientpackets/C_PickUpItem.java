 package l1j.server.server.clientpackets;

 import l1j.server.MJCharacterActionSystem.Pickup.PickupActionHandler;
 import l1j.server.MJCharacterActionSystem.PickupActionHandlerFactory;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Disconnect;
 import l1j.server.server.serverpackets.ServerBasePacket;




 public class C_PickUpItem
   extends ClientBasePacket
 {
   private static final String C_PICK_UP_ITEM = "[C] C_PickUpItem";

   public C_PickUpItem(byte[] decrypt, GameClient client) throws Exception {
     super(decrypt);
     L1PcInstance pc = client.getActiveChar();
     if (pc == null || pc.isGhost() || pc.isDead() || pc.isInvisble() || pc.isInvisDelay() || pc.isFishing()) {
       return;
     }
     if (pc.getOnlineStatus() != 1) {
       pc.sendPackets((ServerBasePacket)new S_Disconnect());

       return;
     }
     PickupActionHandler pickupActionHandler = PickupActionHandlerFactory.create();
     if (pickupActionHandler == null) {
       return;
     }
     pickupActionHandler.parse(pc, this);
     pickupActionHandler.doWork();
   }


   public String getType() {
     return "[C] C_PickUpItem";
   }
 }


