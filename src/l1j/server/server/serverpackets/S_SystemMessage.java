 package l1j.server.server.serverpackets;

 public class S_SystemMessage
   extends ServerBasePacket
 {
   private static final String S_SYSTEM_MESSAGE = "[S] S_SystemMessage";

   public S_SystemMessage(int type, int time) {
     super(8);
     writeC(153);
     writeH(type);
     writeC(1);
     writeH(time);
   }

   public S_SystemMessage(String msg) {
     super(4 + msg.length() * 2);
     writeC(153);
     writeC(9);
     writeS(msg);
   }

   public S_SystemMessage(String msg, boolean nameid) {
     super(8 + msg.length() * 2);
     writeC(61);
     writeC(2);
     writeD(0);
     writeS(msg);
   }

   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_SystemMessage";
   }
 }


