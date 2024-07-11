 package l1j.server.server.serverpackets;

 public class S_SocialAction
   extends ServerBasePacket
 {
   private static final int SC_SOCIAL_ACTION_NOTI = 320;

   public static S_SocialAction get(int id, int type, int code) {
     S_SocialAction s = new S_SocialAction();
     s.writeBit(8L);
     s.writeBit(id);
     s.writeBit(16L);
     s.writeBit(type);
     s.writeBit(24L);
     s.writeBit(code);
     s.writeH(0);
     return s;
   }

   private S_SocialAction() {
     writeC(19);
     writeH(320);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "S_SocialAction";
   }
 }


