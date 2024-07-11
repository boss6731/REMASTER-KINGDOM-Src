     package l1j.server.server.serverpackets;

     import l1j.server.MJTemplate.MJSimpleRgb;
     import l1j.server.server.model.Instance.L1ItemInstance;
     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_IconMessage
       extends ServerBasePacket {
       private static final int SC_NOTIFICATION_MESSAGE_NOTI = 64;
       private static final int SC_NOTIFICATION_STRINGKINDEX_NOTI = 103;

       public static S_IconMessage getGmMessage(String msg) {
         return getMessage(msg, new MJSimpleRgb(0, 255, 0), 38, 60);
       }

       public static S_IconMessage getMessage(String msg, MJSimpleRgb rgb, int surfNum, int duration) {
         S_IconMessage s = new S_IconMessage(64);
         s.writeBit(8L);
         s.writeBit((surfNum * 2));
         s.writeBit(18L);
         s.writeS2(msg);
         s.writeBit(26L);
         try {
           s.writeBytes(rgb.get_bytes());
         } catch (Exception e) {

           e.printStackTrace();
         }
         s.writeBit(32L);
         s.writeBit(duration);
         s.writeH(0);
         return s;
       }

       public static S_IconMessage getMessage(int scode, MJSimpleRgb rgb, int surfNum, int duration) {
         S_IconMessage s = new S_IconMessage(103);
         s.writeC(8);
         s.writeBit((surfNum * 2));
         s.writeBit(16L);
         s.writeBit((scode * 2));
         s.writeC(26);
         try {
           s.writeBytes(rgb.get_bytes());
         } catch (Exception e) {

           e.printStackTrace();
         }
         s.writeC(32);
         s.writeBit(duration);
         s.writeH(0);
         return s;
       }

       private S_IconMessage(int i) {
         writeC(19);
         writeH(i);
       }

       public S_IconMessage(L1PcInstance gm, String p) {
         int SC_UPDATE_INVENTORY_NOTI = 589;
         writeC(19);
         writeH(SC_UPDATE_INVENTORY_NOTI);
         writeC(10);
         L1ItemInstance item = gm.getInventory().findItemObjId(269226741);
         item.setAttrEnchantLevel(3);
         byte[] data = item.serialize();
         writeBit(data.length);
         writeByte(data);
         writeH(0);
       }

       public S_IconMessage(boolean b) {
         int SC_BLOODPLEDGE_USER_INFO_NOTI = 537;
         writeC(19);
         writeH(SC_BLOODPLEDGE_USER_INFO_NOTI);
         try {
           byte[] bytes = "免費".getBytes("UTF-8");
           writeC(10);
           writeBit(bytes.length);
           writeBytes(bytes);
           writeC(16);
           writeC(10);
         } catch (Exception e) {
           e.printStackTrace();
         }
         writeH(0);
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


