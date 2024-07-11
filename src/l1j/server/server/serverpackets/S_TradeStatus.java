 package l1j.server.server.serverpackets;



 public class S_TradeStatus
   extends ServerBasePacket
 {
   private static final String _S__2A_TRADESTATUS = "[S] S_TradeStatus";

   public S_TradeStatus(int type) {
     writeC(167);
     writeC(type);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_TradeStatus";
   }
 }


