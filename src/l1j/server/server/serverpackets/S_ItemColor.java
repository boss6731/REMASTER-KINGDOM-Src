     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1ItemInstance;


     public class S_ItemColor
       extends ServerBasePacket
     {
       private static final String S_ITEM_COLOR = "[S] S_ItemColor";

       public S_ItemColor(L1ItemInstance item) {
         if (item == null) {
           return;
         }
         buildPacket(item);
       }

       public S_ItemColor(L1ItemInstance item, int color) {
         if (item == null) {
           return;
         }
         buildPacket(item, color);
       }

       private void buildPacket(L1ItemInstance item) {
         writeC(70);
         writeD(item.getId());
         writeC(item.getBless());
       }

       private void buildPacket(L1ItemInstance item, int color) {
         writeC(70);
         writeD(item.getId());

         writeC(color);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_ItemColor";
       }
     }


