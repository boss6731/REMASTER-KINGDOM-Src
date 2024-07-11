 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1ItemInstance;



 public class S_TradeAddItem
   extends ServerBasePacket
 {
   private static final String S_TRADE_ADD_ITEM = "[S] S_TradeAddItem";

   public S_TradeAddItem(L1ItemInstance item, int count, int type) {
     writeC(145);
     writeC(type);
     writeH(item.getItem().getGfxId());
     writeS(item.getNumberedViewName(count));


     if (!item.isIdentified()) {

       writeH(3);
     } else {
       byte[] status = null;
       int bless = item.getBless();
       writeC(bless);
       status = item.getStatusBytes();
       writeC(status.length);
       for (byte b : status) {
         writeC(b);
       }
     }

     writeH(7);
   }

   public S_TradeAddItem(L1ItemInstance item, String name, int count, int type) {
     writeC(145);
     writeC(type);
     writeH(item.getItem().getGfxId());
     writeS(name);


     if (!item.isIdentified()) {

       writeH(3);
     } else {
       byte[] status = null;
       int bless = item.getBless();
       writeC(bless);
       status = item.getStatusBytes();
       writeC(status.length);
       for (byte b : status) {
         writeC(b);
       }
     }

     writeH(7);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_TradeAddItem";
   }
 }


