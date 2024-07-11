 package l1j.server.server.utils;

 import java.util.List;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;




 public class FaceToFace
 {
   public static L1PcInstance faceToFace(L1PcInstance pc) {
     int pcX = pc.getX();
     int pcY = pc.getY();
     int pcHeading = pc.getHeading();
     List<L1PcInstance> players = L1World.getInstance().getVisiblePlayer((L1Object)pc, 1);


     if (players.size() == 0) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(93));
       return null;
     }
     for (L1PcInstance target : players) {
       int targetX = target.getX();
       int targetY = target.getY();
       int targetHeading = target.getHeading();
       if (pcHeading == 0 && pcX == targetX && pcY == targetY + 1) {
         if (targetHeading == 4) {
           return target;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(91, target.getName()));
         return null;
       }
       if (pcHeading == 1 && pcX == targetX - 1 && pcY == targetY + 1) {

         if (targetHeading == 5) {
           return target;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(91, target.getName()));
         return null;
       }
       if (pcHeading == 2 && pcX == targetX - 1 && pcY == targetY) {
         if (targetHeading == 6) {
           return target;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(91, target.getName()));
         return null;
       }
       if (pcHeading == 3 && pcX == targetX - 1 && pcY == targetY - 1) {

         if (targetHeading == 7) {
           return target;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(91, target.getName()));
         return null;
       }
       if (pcHeading == 4 && pcX == targetX && pcY == targetY - 1) {
         if (targetHeading == 0) {
           return target;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(91, target.getName()));
         return null;
       }
       if (pcHeading == 5 && pcX == targetX + 1 && pcY == targetY - 1) {

         if (targetHeading == 1) {
           return target;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(91, target.getName()));
         return null;
       }
       if (pcHeading == 6 && pcX == targetX + 1 && pcY == targetY) {
         if (targetHeading == 2) {
           return target;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(91, target.getName()));
         return null;
       }
       if (pcHeading == 7 && pcX == targetX + 1 && pcY == targetY + 1) {

         if (targetHeading == 3) {
           return target;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(91, target.getName()));
         return null;
       }
     }

     pc.sendPackets((ServerBasePacket)new S_ServerMessage(93));
     return null;
   }


   public static L1Character faceToFace1(L1PcInstance pc) {
     int pcX = pc.getX();
     int pcY = pc.getY();
     int pcHeading = pc.getHeading();
     List<L1Object> list = L1World.getInstance().getVisibleObjects((L1Object)pc, 1);

     if (list.size() == 0) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(93));
       return null;
     }
     L1Character target = null;
     for (L1Object t : list) {
       if (t instanceof L1PcInstance || t instanceof l1j.server.server.model.Instance.L1NpcInstance) {
         target = (L1Character)t;
       }
       int targetX = target.getX();
       int targetY = target.getY();
       int targetHeading = target.getHeading();
       if (pcHeading == 0 && pcX == targetX && pcY == targetY + 1) {
         if (targetHeading == 4) {
           return target;
         }
         return null;
       }
       if (pcHeading == 1 && pcX == targetX - 1 && pcY == targetY + 1) {
         if (targetHeading == 5) {
           return target;
         }
         return null;
       }
       if (pcHeading == 2 && pcX == targetX - 1 && pcY == targetY) {
         if (targetHeading == 6) {
           return target;
         }
         return null;
       }
       if (pcHeading == 3 && pcX == targetX - 1 && pcY == targetY - 1) {

         if (targetHeading == 7) {
           return target;
         }
         return null;
       }
       if (pcHeading == 4 && pcX == targetX && pcY == targetY - 1) {
         if (targetHeading == 0) {
           return target;
         }
         return null;
       }
       if (pcHeading == 5 && pcX == targetX + 1 && pcY == targetY - 1) {

         if (targetHeading == 1) {
           return target;
         }
         return null;
       }
       if (pcHeading == 6 && pcX == targetX + 1 && pcY == targetY) {
         if (targetHeading == 2) {
           return target;
         }
         return null;
       }
       if (pcHeading == 7 && pcX == targetX + 1 && pcY == targetY + 1) {

         if (targetHeading == 3) {
           return target;
         }
         return null;
       }
     }

     return null;
   }
 }


