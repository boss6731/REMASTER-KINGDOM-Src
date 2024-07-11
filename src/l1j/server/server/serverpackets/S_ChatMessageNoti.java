     package l1j.server.server.serverpackets;

     import l1j.server.MJTemplate.MJSimpleRgb;
     import l1j.server.server.model.L1Object;

     public class S_ChatMessageNoti
       extends ServerBasePacket
     {
       private static final int SC_CHAT_MESSAGE_NOTI_PACKET = 516;
       public static final int CHAT_NORMAL = 0;
       public static final int CHAT_WHISPER = 1;
       public static final int CHAT_SHOUT = 2;
       public static final int CHAT_WORLD = 3;
       public static final int CHAT_PLEDGE = 4;
       public static final int CHAT_HUNT_PARTY = 11;
       public static final int CHAT_TRADE = 12;
       public static final int CHAT_PLEDGE_PRINCE = 13;
       public static final int CHAT_CHAT_PARTY = 14;
       public static final int CHAT_PLEDGE_ALLIANCE = 15;
       public static final int CHAT_PLEDGE_NOTICE = 17;
       public static final int CHAT_CLASS = 22;
       public static final int CHAT_TEAM = 29;
       public static final int CHAT_ARENA_TEAM = 30;
       public static final int CHAT_ARENA_OBSERVER = 31;
       public static final int CHAT_ROOM_ARENA_ALL = 32;

       public static S_ChatMessageNoti getWhisper(String msg, String sender) {
         S_ChatMessageNoti s = new S_ChatMessageNoti();
         s.writeC(8);
         s.writeBit(0L);
         s.writeC(16);
         s.writeC(1);
         s.writeC(26);
         s.writeS2(msg);
         s.writeC(42);
         s.writeS2(sender);
         s.writeC(48);
         s.writeC(0);
         s.writeH(0);
         return s;
       }

       public static S_ChatMessageNoti getNotice(String msg, String sender) {
         S_ChatMessageNoti s = new S_ChatMessageNoti();
         s.writeC(8);
         s.writeBit(0L);
         s.writeC(16);
         s.writeC(17);
         s.writeC(26);
         s.writeS2(msg);
         s.writeC(42);
         s.writeS2(sender);
         s.writeC(48);
         s.writeC(0);
         s.writeH(0);
         return s;
       }

       public static S_ChatMessageNoti get(int type, String msg, MJSimpleRgb rgb, String sender, L1Object obj, int rank) {
         S_ChatMessageNoti s = new S_ChatMessageNoti();
         s.writeC(8);
         s.writeBit(0L);
         s.writeC(16);
         s.writeC(type);
         s.writeC(26);
         s.writeS2(msg);
         if (rgb != null) {
           s.writeC(34);
           try {
             s.writeBytes(rgb.get_bytes());
           } catch (Exception e) {

             e.printStackTrace();
           }
         }

         if (sender != null) {
           s.writeC(42);
           s.writeS2(sender);
         }
         s.writeC(48);
         s.writeC(1);
         if (obj != null) {
           s.writeC(56);
           s.writeBit(obj.getId());
           s.writeC(64);
           s.writeBit(obj.getX());
           s.writeC(72);
           s.writeBit(obj.getY());
         }

         if (rank > 0) {
           s.writeC(80);
           s.writeC(rank);
         }
         s.writeH(0);
         return s;
       }

       private S_ChatMessageNoti() {
         writeC(19);
         writeH(516);
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


