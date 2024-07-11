 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import l1j.server.MJTemplate.MJSimpleRgb;


 public class S_NotificationMessage
   extends ServerBasePacket
 {
   private static final int SC_NOTIFICATION_MESSAGE = 316;
   public static final int DISPLAY_POSITION_TOP = 1;
   public static final int DISPLAY_POSITION_MIDDLE = 2;
   public static final int DISPLAY_POSITION_BOTTOM = 3;

   public static S_NotificationMessage getGmMessage(String message) {
     return get(1, message, new MJSimpleRgb(0, 255, 255), 60);
   }

   public static S_NotificationMessage get(String message, MJSimpleRgb rgb) {
     return get(2, message, rgb, 60);
   }

   public static S_NotificationMessage get(String message, MJSimpleRgb rgb, int duration_second) {
     return get(2, message, rgb, duration_second);
   }

   public static S_NotificationMessage get(int display_position, String message, MJSimpleRgb rgb, int duration_second) {
     S_NotificationMessage p = new S_NotificationMessage(message.length() + 32);
     p.writeC(8);
     p.writeC(display_position);
     p.writeC(18);
     p.writeS2(message);
     p.writeC(26);
     try {
       p.writeBytes(rgb.get_bytes());
     } catch (Exception e) {

       e.printStackTrace();
     }
     p.writeBit(32L);
     p.writeBit(duration_second);
     p.writeH(0);
     return p;
   }


   private S_NotificationMessage(int capacity) {
     super(capacity);
     writeC(19);
     writeH(316);
   }


   public byte[] getContent() throws IOException {
     return getBytes();
   }
 }


