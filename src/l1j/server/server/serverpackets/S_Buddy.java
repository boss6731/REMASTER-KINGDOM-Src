     package l1j.server.server.serverpackets;

     import java.io.IOException;
     import java.util.ArrayList;
     import l1j.server.server.datatables.BuddyTable;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Buddy;
     import l1j.server.server.utils.BinaryOutputStream;

     public class S_Buddy
       extends ServerBasePacket
     {
       private static final String _S_Buddy = "[S] _S_Buddy";

       public S_Buddy(L1PcInstance pc) {
         writeC(19);
         writeH(337);
         writeC(8);
         writeBit(1L);


         ArrayList<L1Buddy> buddy_list = BuddyTable.getInstance().getBuddyList(pc.getId());
         L1Buddy buddy = null;
         if (buddy_list != null) {
           for (int i = 0; i < buddy_list.size(); i++) {
             buddy = buddy_list.get(i);
             if (buddy != null) {
               writeC(18);
               byte[] stats = getBuddyData(buddy);
               writeBit(stats.length);
               writeByte(stats);
             }
           }
         }

         writeH(0);
       }

       private byte[] getBuddyData(L1Buddy buddy) {
         BinaryOutputStream os = new BinaryOutputStream();
         try {
           L1PcInstance target = L1PcInstance.load(buddy.getCharName());

           os.writeC(10);
           os.writeBit((buddy.getCharName().getBytes()).length);
           os.writeByte(buddy.getCharName().getBytes());
           os.writeC(16);
           os.writeBit((target.getOnlineStatus() > 0) ? 1L : 0L);
           if (buddy.getMemo() != null && buddy.getMemo().length() > 1) {
             os.writeC(26);
             os.writeBit((buddy.getMemo().getBytes()).length);
             os.writeByte(buddy.getMemo().getBytes());
           } else {
             os.writeC(26);
             os.writeC(0);
           }
           os.writeC(32);
           os.writeBit(target.getType());

           return os.getBytes();
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           if (os != null) {
             try {
               os.close();
             } catch (IOException e) {
               e.printStackTrace();
             }
           }
         }
         return null;
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] _S_Buddy";
       }
     }


