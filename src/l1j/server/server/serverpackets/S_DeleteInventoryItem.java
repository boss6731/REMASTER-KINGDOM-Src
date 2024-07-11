     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1ItemInstance;




     public class S_DeleteInventoryItem
       extends ServerBasePacket
     {
       private static final String S_DELETE_INVENTORY_ITEM = "[S] S_DeleteInventoryItem";

       public S_DeleteInventoryItem(L1ItemInstance item) {
         if (item != null) {
           writeC(98);
           writeD(item.getId());
         }
       }

       public S_DeleteInventoryItem(int id) {
         writeC(98);
         writeD(id);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_DeleteInventoryItem";
       }
     }


