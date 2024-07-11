 package l1j.server.server.model.item.function;

 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Location;

 public class omanTel
 {
   public static void clickItem(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance) {
     L1Location newLocation = pc.getLocation().randomLocation(200, true);
     int newX = newLocation.getX();
     int newY = newLocation.getY();
     short mapId = (short)newLocation.getMapId();
     if (pc.isParalyzed() || pc.isSleeped() || pc.isDead()) {
       return;
     }
     if (!pc.getMap().isEscapable()) {
       return;
     }
     switch (itemId) {
       case 830001:
         if (pc.getMapId() == 101) {
           pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
         } else {
           pc.start_teleport(32735, 32798, 101, pc.getHeading(), 18339, true, false);
         }
         pc.getInventory().removeItem(l1iteminstance, 1);
         break;
       case 830002:
         if (pc.getMapId() == 102) {
           pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
         } else {
           pc.start_teleport(32726, 32803, 102, pc.getHeading(), 18339, true, false);
         }
         pc.getInventory().removeItem(l1iteminstance, 1);
         break;
       case 830003:
         if (pc.getMapId() == 103) {
           pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
         } else {
           pc.start_teleport(32726, 32803, 103, pc.getHeading(), 18339, true, false);
         }
         pc.getInventory().removeItem(l1iteminstance, 1);
         break;
       case 830004:
         if (pc.getMapId() == 104) {
           pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
         } else {
           pc.start_teleport(32613, 32863, 104, pc.getHeading(), 18339, true, false);
         }
         pc.getInventory().removeItem(l1iteminstance, 1);
         break;
       case 830005:
         if (pc.getMapId() == 105) {
           pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
         } else {
           pc.start_teleport(32597, 32867, 105, pc.getHeading(), 18339, true, false);
         }
         pc.getInventory().removeItem(l1iteminstance, 1);
         break;
       case 830006:
         if (pc.getMapId() == 106) {
           pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
         } else {

           pc.start_teleport(32607, 32865, 106, pc.getHeading(), 18339, true, false);
         }
         pc.getInventory().removeItem(l1iteminstance, 1);
         break;
       case 830007:
         if (pc.getMapId() == 107) {
           pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
         } else {
           pc.start_teleport(32618, 32866, 107, pc.getHeading(), 18339, true, false);
         }
         pc.getInventory().removeItem(l1iteminstance, 1);
         break;
       case 830008:
         if (pc.getMapId() == 108) {
           pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
         } else {
           pc.start_teleport(32598, 32867, 108, pc.getHeading(), 18339, true, false);
         }
         pc.getInventory().removeItem(l1iteminstance, 1);
         break;
       case 830009:
         if (pc.getMapId() == 109) {
           pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
         } else {
           pc.start_teleport(32609, 32866, 109, pc.getHeading(), 18339, true, false);
         }
         pc.getInventory().removeItem(l1iteminstance, 1);
         break;
       case 830010:
         if (pc.getMapId() == 110) {
           pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
         } else {
           pc.start_teleport(32726, 32803, 110, pc.getHeading(), 18339, true, false);
         }
         pc.getInventory().removeItem(l1iteminstance, 1);
         break;



       case 830011:
         pc.start_teleport(32619, 32807, 111, pc.getHeading(), 18339, true, false);

         pc.getInventory().removeItem(l1iteminstance, 1);
         break;
     }
   }
 }


