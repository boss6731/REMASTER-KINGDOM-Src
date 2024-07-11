 package l1j.server.server.serverpackets;

 import java.util.List;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PetInstance;



 public class S_PetInventory
   extends ServerBasePacket
 {
   private static final String S_PET_INVENTORY = "[S] S_PetInventory";

   public S_PetInventory(L1PetInstance pet) {
     List<L1ItemInstance> itemList = pet.getInventory().getItems();

     writeC(127);
     writeD(pet.getId());
     writeH(itemList.size());
     writeC(11);
     L1ItemInstance item = null;
     for (L1ItemInstance itemObject : itemList) {
       item = itemObject;
       if (item != null) {
         writeD(item.getId());
         writeC(22);
         writeH(item.get_gfxid());
         writeC(item.getItem().getBless());
         writeD(item.getCount());
         switch (item.isEquipped() ? 1 : 0) {
           case false:
             writeC(item.isIdentified() ? 1 : 0);
             break;
           case true:
             writeC(3);
             break;
         }
         writeS(item.getViewName());
       }
     }
     writeC(pet.getAC().getAc());
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_PetInventory";
   }
 }


