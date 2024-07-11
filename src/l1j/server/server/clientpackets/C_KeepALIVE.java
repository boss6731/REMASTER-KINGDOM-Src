 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
 import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
 import l1j.server.server.serverpackets.S_GameTime;
 import l1j.server.server.serverpackets.ServerBasePacket;



















 public class C_KeepALIVE
   extends ClientBasePacket
 {
   private static final String C_KEEP_ALIVE = "[C] C_KeepALIVE";
   private GameClient client;

   public C_KeepALIVE(byte[] decrypt, GameClient client) {
     super(decrypt);

     L1PcInstance pc = client.getActiveChar();
     pc.sendPackets((ServerBasePacket)new S_GameTime());
   }


   public String getType() {
     return "[C] C_KeepALIVE";
   }
   private void favorBookInventoryTimeOut() {
     long currentTime = System.currentTimeMillis();

     L1PcInstance pc = this.client.getActiveChar();
     L1FavorBookInventory favorBook = pc.getFavorBook();
     if (favorBook == null) {
       return;
     }
     for (L1FavorBookUserObject user : favorBook.getList()) {
       if (user == null || user.getType().getEndTime() == null || user.getType().getEndTime().getTime() > currentTime) {
         continue;
       }
       favorBook.deleteFavor(user);
     }
   }
 }


