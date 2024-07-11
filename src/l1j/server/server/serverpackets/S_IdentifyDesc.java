     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1ItemInstance;




     public class S_IdentifyDesc
       extends ServerBasePacket
     {
       public S_IdentifyDesc(L1ItemInstance item) {
         buildPacket(item);
       }

       private void buildPacket(L1ItemInstance item) {
         writeC(181);
         writeH(item.getItem().getItemDescId());
         StringBuilder name = new StringBuilder();

         if (item.getBless() == 0) {
           name.append("$227 ");
         } else if (item.getBless() == 2) {
           name.append("$228 ");
         }

         name.append(item.getItem().getNameId());
         if (item.getItem().getType2() == 1) {
           writeH(134);
           writeC(3);
           writeS(name.toString());
           writeS(item.getItem().getDmgSmall() + "+" + item.getEnchantLevel());
           writeS(item.getItem().getDmgLarge() + "+" + item.getEnchantLevel());
         }
         else if (item.getItem().getType2() == 2) {
           if (item.getItem().getItemId() == 20383) {
             writeH(137);
             writeC(3);
             writeS(name.toString());
             writeS(String.valueOf(item.getChargeCount()));
           } else {
             writeH(135);
             writeC(2);
             writeS(name.toString());
             writeS(Math.abs(item.getItem().get_ac()) + "+" + item.getEnchantLevel());
           }

         } else if (item.getItem().getType2() == 0) {
           if (item.getItem().getType() == 1) {
             writeH(137);
             writeC(3);
             writeS(name.toString());
             writeS(String.valueOf(item.getChargeCount()));
           } else if (item.getItem().getType() == 2) {
             writeH(138);
             writeC(2);
             name.append(": $231 ");
             name.append(String.valueOf(item.getRemainingTime()));
             writeS(name.toString());
           } else if (item.getItem().getType() == 7) {
             writeH(136);
             writeC(3);
             writeS(name.toString());
             writeS(String.valueOf(item.getItem().getFoodVolume()));
           } else {
             writeH(138);
             writeC(2);
             writeS(name.toString());
           }
           writeS(String.valueOf(item.getWeight()));
         }
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


