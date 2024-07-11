 package l1j.server.server.clientpackets;

 import java.util.List;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;






 public class C_UsePetItem
   extends ClientBasePacket
 {
   private static final String C_USE_PET_ITEM = "[C] C_UsePetItem";

   public C_UsePetItem(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     int data = readC();
     int petId = readD();
     int listNo = readC();
     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null)
       return;  L1PetInstance pet = (L1PetInstance)L1World.getInstance().findObject(petId);
     if (pet == null) {
       return;
     }
     List<L1ItemInstance> items = pet.getInventory().getItems();
     if (items == null) {
       return;
     }
     int size = items.size();
     if (size <= 0 || listNo >= size) {
       return;
     }
     L1ItemInstance item = items.get(listNo);
     if (item == null) {
       return;
     }

     if (item.getItem().getType2() == 0 && item
       .getItem().getType() == 11) {
       int itemId = item.getItem().getItemId();
       if ((itemId >= 40749 && itemId <= 40752) || (itemId >= 40756 && itemId <= 40758)) {

         pet.usePetWeapon(pc, pet, item);
         pc.sendPackets((ServerBasePacket)new S_PacketBox(37, data, pet.getId(), pet.getAC().getAc()));
       } else if (itemId >= 40761 && itemId <= 40766) {
         pet.usePetArmor(pc, pet, item);
         pc.sendPackets((ServerBasePacket)new S_PacketBox(37, data, pet.getId(), pet.getAC().getAc()));
       } else {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
       }
     } else {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
     }
   }


   public String getType() {
     return "[C] C_UsePetItem";
   }
 }


