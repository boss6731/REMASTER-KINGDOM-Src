     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1ItemInstance;

     public class S_ItemAmount
       extends ServerBasePacket
     {
       private static final String S_ITEM_AMOUNT = "[S] S_ItemAmount";

       public S_ItemAmount(L1ItemInstance item) {
         if (item == null) {
           return;
         }

         buildPacket(item);
       }

       private void buildPacket(L1ItemInstance item) {
         writeC(164);
         writeD(item.getId());
         writeS(item.getViewName());
         writeD(item.getCount());
         writeC(0);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_ItemAmount";
       }
     }


