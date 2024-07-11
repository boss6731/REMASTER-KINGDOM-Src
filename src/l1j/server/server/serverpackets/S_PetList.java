 package l1j.server.server.serverpackets;

 import java.util.ArrayList;
 import java.util.List;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;



 public class S_PetList
   extends ServerBasePacket
 {
   private static final String S_PETLIST = "[S] S_PetList";

   public S_PetList(int npcObjId, L1PcInstance pc) {
     buildPacket(npcObjId, pc);
   }

   private void buildPacket(int npcObjId, L1PcInstance pc) {
     List<L1ItemInstance> amuletList = new ArrayList<>();
     L1ItemInstance item = null;
     for (Object itemObject : pc.getInventory().getItems()) {
       item = (L1ItemInstance)itemObject;
       if ((item.getItem().getItemId() == 40314 || item
         .getItem().getItemId() == 40316) &&
         !isWithdraw(pc, item)) {
         amuletList.add(item);
       }
     }

     if (amuletList.size() != 0) {
       writeC(136);
       writeD(70);
       writeH(amuletList.size());
       for (L1ItemInstance _item : amuletList) {
         writeD(_item.getId());
         writeC(_item.getCount());
       }
     }
   }

   private boolean isWithdraw(L1PcInstance pc, L1ItemInstance item) {
     Object[] petlist = pc.getPetList().values().toArray();
     L1PetInstance pet = null;
     for (Object petObject : petlist) {
       if (petObject instanceof L1PetInstance) {
         pet = (L1PetInstance)petObject;
         if (item.getId() == pet.getItemObjId()) {
           return true;
         }
       }
     }
     return false;
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_PetList";
   }
 }


