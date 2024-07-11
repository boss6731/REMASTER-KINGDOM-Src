 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1ItemInstance;





 public class S_ItemStatus
   extends ServerBasePacket
 {
   private static final String S_ITEM_STATUS = "[S] S_ItemStatus";

   public S_ItemStatus(L1ItemInstance item) {
     writeC(164);
     writeD(item.getId());
     writeS(item.getViewName());
     writeD(item.getCount());
     if (!item.isIdentified()) {
       writeC(0);
     } else {
       byte[] status = item.getStatusBytes();
       writeC(status.length);
       for (byte b : status) {
         writeC(b);
       }
     }
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_ItemStatus";
   }
 }


