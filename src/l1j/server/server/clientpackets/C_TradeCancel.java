 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Trade;





 public class C_TradeCancel
   extends ClientBasePacket
 {
   private static final String C_TRADE_CANCEL = "[C] C_TradeCancel";

   public C_TradeCancel(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     L1PcInstance player = clientthread.getActiveChar();
     if (player == null)
       return;  L1Trade trade = new L1Trade();
     trade.TradeCancel(player);
   }


   public String getType() {
     return "[C] C_TradeCancel";
   }
 }


