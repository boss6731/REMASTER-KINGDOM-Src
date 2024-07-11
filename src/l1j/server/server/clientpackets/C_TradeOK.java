 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Trade;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;






 public class C_TradeOK
   extends ClientBasePacket
 {
   private static final String C_TRADE_CANCEL = "[C] C_TradeOK";

   public C_TradeOK(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0);

     L1PcInstance player = clientthread.getActiveChar();
     if (player == null)
       return;  L1PcInstance trading_partner = (L1PcInstance)L1World.getInstance().findObject(player.getTradeID());
     if (trading_partner != null) {
       player.setTradeOk(true);
       if (player.getTradeOk() && trading_partner.getTradeOk())
       {


         if (player.getInventory().getSize() < 184 && trading_partner.getInventory().getSize() < 184) {
           L1Trade trade = new L1Trade();
           trade.TradeOK(player);
         } else {
           player.sendPackets((ServerBasePacket)new S_ServerMessage(263));
           trading_partner.sendPackets((ServerBasePacket)new S_ServerMessage(263));
           L1Trade trade = new L1Trade();
           trade.TradeCancel(player);
         }
       }
     } else {

       L1Trade trade = new L1Trade();
       trade.TradeOK(player);
       System.out.println("c opcode : ");
     }
   }


   public String getType() {
     return "[C] C_TradeOK";
   }
 }


