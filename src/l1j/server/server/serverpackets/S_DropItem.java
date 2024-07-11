     package l1j.server.server.serverpackets;

     import java.text.SimpleDateFormat;
     import java.util.Locale;
     import l1j.server.MJCTSystem.Loader.MJCTSystemLoader;
     import l1j.server.MJCTSystem.MJCTObject;
     import l1j.server.MJTemplate.MJString;
     import l1j.server.server.model.Instance.L1ItemInstance;




     public class S_DropItem
       extends ServerBasePacket
     {
       private static final String _S__OB_DropItem = "[S] S_DropItem";
       private static final SimpleDateFormat itemRemainTimeFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.KOREA);
       public S_DropItem(L1ItemInstance item) {
         buildPacket(item);
       }

       private String parse_item_name(L1ItemInstance item) {
         StringBuffer sb = null;
         sb = new StringBuffer();
         if (item.isIdentified() && (
           item.getItem().getType2() == 1 || item.getItem().getType2() == 2)) {
           switch (item.getAttrEnchantLevel()) { case 1:
               sb.append("$6115"); break;
             case 2: sb.append("$6116"); break;
             case 3: sb.append("$6117"); break;
             case 4: sb.append("$14361"); break;
             case 5: sb.append("$14365"); break;
             case 6: sb.append("$6118"); break;
             case 7: sb.append("$6119"); break;
             case 8: sb.append("$6120"); break;
             case 9: sb.append("$14362"); break;
             case 10: sb.append("$14366"); break;
             case 11: sb.append("$6121"); break;
             case 12: sb.append("$6122"); break;
             case 13: sb.append("$6123"); break;
             case 14: sb.append("$14363"); break;
             case 15: sb.append("$14367"); break;
             case 16: sb.append("$6124"); break;
             case 17: sb.append("$6125"); break;
             case 18: sb.append("$6126"); break;
             case 19: sb.append("$14364"); break;
             case 20: sb.append("$14368"); break;
             default:
               sb.append(" ");
               break; }


           if (item.getEnchantLevel() > 0) {
             sb.append("+" + item.getEnchantLevel() + " ");
           } else if (item.getEnchantLevel() < 0) {
             sb.append(String.valueOf(item.getEnchantLevel()) + " ");
           }
         }


         String real_name = item.getItem().getNameView();
         if (item.getItem().getItemId() == 3000469 && MJCTSystemLoader.getInstance().get(item.getId()) != null) {
           MJCTObject obj = MJCTSystemLoader.getInstance().get(item.getId());
           sb.append("[").append(obj.name).append("]封印寶珠");
         } else {
           if (MJString.isNullOrEmpty(real_name) || item.isIdentified()) {
             real_name = item.getItem().getNameId();
           }
           sb.append(real_name);
         }


         if (item.getCount() > 1) {
           sb.append(" (" + item.getCount() + ")");
         } else {
           int itemId = item.getItem().getItemId();
           int isId = item.isIdentified() ? 1 : 0;
           if (itemId == 20383 && isId == 1) {
             sb.append(" [" + item.getChargeCount() + "]");
           } else if ((itemId == 40006 || itemId == 40007 || itemId == 40008 || itemId == 46091 || itemId == 40009 || itemId == 45464 || itemId == 140006 || itemId == 140008) && isId == 1) {


             sb.append(" (" + item.getChargeCount() + ")");
           } else if (itemId == 40312) {
             sb.append(" [").append(itemRemainTimeFormat.format(Long.valueOf(item.getEndTime().getTime()))).append("]");
           } else if (itemId == 80500) {
             sb.append(" [" + item.getEndTime().getTime() + "]");
           } else if (item.getItem().getLightRange() != 0 && item.isNowLighting()) {
             sb.append(" ($10)");
           }
         }
         return sb.toString();
       }





       private void buildPacket(L1ItemInstance item) {
         writeC(186);
         writeH(item.getX());
         writeH(item.getY());
         writeD(item.getId());
         writeH(item.getItem().getGroundGfxId());
         writeC(0);
         writeC(0);
         if (item.isNowLighting()) {
           writeC(item.getItem().getLightRange());
         } else {
           writeC(0);
         }
         writeC(0);
         writeD(item.getCount());
         writeC(0);
         writeC(0);

         writeS(parse_item_name(item));
         writeC(0);
         writeD(0);
         writeD(0);
         writeC(255);
         writeC(0);
         writeC(0);
         writeC(0);
         writeH(65535);
         writeD(0);
         writeC(8);
         writeC(0);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_DropItem";
       }
     }


