     package l1j.server.server.serverpackets;

     import java.util.ArrayList;
     import java.util.List;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1PcInstance;


     public class S_FixWeaponList
       extends ServerBasePacket
     {
       private static final String S_FIX_WEAPON_LIST = "[S] S_FixWeaponList";

       public S_FixWeaponList(L1PcInstance pc) {
         buildPacket(pc);
       }

       private void buildPacket(L1PcInstance pc) {
         writeC(136);
         writeD(200);

         List<L1ItemInstance> weaponList = new ArrayList<>();
         List<L1ItemInstance> itemList = pc.getInventory().getItems();
         for (L1ItemInstance item : itemList) {


           switch (item.getItem().getType2()) {
             case 1:
               if (item.get_durability() > 0) {
                 weaponList.add(item);
               }
           }


         }
         writeH(weaponList.size());

         for (L1ItemInstance weapon : weaponList) {

           writeD(weapon.getId());
           writeC(weapon.get_durability());
         }
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_FixWeaponList";
       }
     }


