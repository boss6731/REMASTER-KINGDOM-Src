     package l1j.server.server.serverpackets;

     import java.io.IOException;
     import java.util.List;
     import l1j.server.server.model.Instance.L1ItemInstance;

     public class S_FishingAddItem
       extends ServerBasePacket
     {
       private static final int SC_ADD_INVENTORY_NOTI = 588;

       public static S_FishingAddItem get(L1ItemInstance item) throws Exception {
         S_FishingAddItem s = new S_FishingAddItem();
         byte[] tmp = item.serializeFishingItem();

         s.writeC(10);
         s.writeBytes(tmp);
         s.writeH(0);
         return s;
       }

       public static S_FishingAddItem getList(List<L1ItemInstance> list) throws Exception {
         S_FishingAddItem s = new S_FishingAddItem();
         for (L1ItemInstance item : list) {
           byte[] b = item.serializeFishingItem();
           s.writeC(10);
           s.writeBytes(b);
         }
         s.writeC(16);
         s.writeB(true);
         return s;
       }

       private S_FishingAddItem() {
         writeC(19);
         writeH(588);
       }


       public byte[] getContent() throws IOException {
         return getBytes();
       }
     }


