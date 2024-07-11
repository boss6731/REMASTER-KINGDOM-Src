 package l1j.server.server.model.item.function;

 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class OmanAmulet
 {
   public static void clickItem(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance) {
     if (pc.isParalyzed() || pc.isSleeped() || pc.isDead()) {
       return;
     }
     if (!pc.getMap().isEscapable()) {
       return;
     }
     if ((pc.getX() >= 33922 && pc.getX() <= 33933) || (pc.getY() >= 33339 && pc.getY() <= 33351) || (pc.getX() >= 33935 && pc.getX() <= 33920) || (pc.getY() >= 33337 && pc.getY() <= 33352)) {
       switch (itemId) {
         case 830012:
         case 830022:
           pc.start_teleport(32735, 32798, 101, pc.getHeading(), 18339, true, false);
           break;
         case 830013:
         case 830023:
           pc.start_teleport(32727, 32803, 102, pc.getHeading(), 18339, true, false);
           break;
         case 830014:
         case 830024:
           pc.start_teleport(32726, 32803, 103, pc.getHeading(), 18339, true, false);
           break;
         case 830015:
         case 830025:
           pc.start_teleport(32620, 32859, 104, pc.getHeading(), 18339, true, false);
           break;
         case 830016:
         case 830026:
           pc.start_teleport(32601, 32866, 105, pc.getHeading(), 18339, true, false);
           break;
         case 830017:
         case 830027:
           pc.start_teleport(32611, 32863, 106, pc.getHeading(), 18339, true, false);
           break;
         case 830018:
         case 830028:
           pc.start_teleport(32618, 32866, 107, pc.getHeading(), 18339, true, false);
           break;
         case 830019:
         case 830029:
           pc.start_teleport(32602, 32867, 108, pc.getHeading(), 18339, true, false);
           break;
         case 830020:
         case 830030:
           pc.start_teleport(32613, 32866, 109, pc.getHeading(), 18339, true, false);
           break;
         case 830021:
         case 830031:
           pc.start_teleport(32730, 32803, 110, pc.getHeading(), 18339, true, false);
           break;
       }
     } else {
       pc.sendPackets(3236);
     }
   }
 }


