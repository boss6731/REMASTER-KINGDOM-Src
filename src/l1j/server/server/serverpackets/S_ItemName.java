 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1ItemInstance;





 public class S_ItemName
   extends ServerBasePacket
 {
   private static final String S_ITEM_NAME = "[S] S_ItemName";

   public S_ItemName(int object_id, String name) {
     writeC(194);
     writeD(object_id);
     writeS(name);
   }

   public S_ItemName(L1ItemInstance item) {
     if (item == null) {
       return;
     }


     writeC(194);
     writeD(item.getId());
     writeS(item.getViewName());
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_ItemName";
   }
 }


