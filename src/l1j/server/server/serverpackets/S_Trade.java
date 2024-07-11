 package l1j.server.server.serverpackets;



 public class S_Trade
   extends ServerBasePacket
 {
   private static final String _S__77_TRADE = "[S] S_Trade";

   public S_Trade(String name) {
     writeC(88);
     writeS(name);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_Trade";
   }
 }


