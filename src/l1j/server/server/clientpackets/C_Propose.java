 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.FaceToFace;




 public class C_Propose
   extends ClientBasePacket
 {
   private static final String C_PROPOSE = "[C] C_Propose";

   public C_Propose(byte[] abyte0, GameClient clientthread) {
     super(abyte0);
     int c = readC();

     L1PcInstance pc = clientthread.getActiveChar();
     if (c == 0) {
       if (pc == null || pc.isGhost()) {
         return;
       }
       L1PcInstance target = FaceToFace.faceToFace(pc);
       if (target != null) {
         if (pc.getPartnerId() != 0) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(657));
           return;
         }
         if (target.getPartnerId() != 0) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(658));
           return;
         }
         if (pc.get_sex() == target.get_sex()) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(661));
           return;
         }
         if (pc.getX() >= 33974 && pc.getX() <= 33976 && pc
           .getY() >= 33362 && pc.getY() <= 33365 && pc
           .getMapId() == 4 && target.getX() >= 33974 && target
           .getX() <= 33976 && target.getY() >= 33362 && target
           .getY() <= 33365 && target.getMapId() == 4) {
           target.setTempID(pc.getId());
           target.sendPackets((ServerBasePacket)new S_Message_YN(654, pc.getName()));
         }
       }
     } else if (c == 1) {
       if (pc.getPartnerId() == 0) {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(662));
         return;
       }
       pc.sendPackets((ServerBasePacket)new S_Message_YN(653, ""));
     }
   }


   public String getType() {
     return "[C] C_Propose";
   }
 }


