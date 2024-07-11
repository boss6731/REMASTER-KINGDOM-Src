 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.FaceToFace;







 public class C_Fight
   extends ClientBasePacket
 {
   private static final String C_FIGHT = "[C] C_Fight";

   public C_Fight(byte[] abyte0, GameClient client) throws Exception {
     super(abyte0);

     L1PcInstance pc = client.getActiveChar();
     if (pc == null || pc.isGhost()) {
       return;
     }
     L1PcInstance target = FaceToFace.faceToFace(pc);
     if (target != null &&
       !target.isParalyzed()) {
       if (pc.getFightId() != 0) {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(633)); return;
       }
       if (target.getFightId() != 0) {
         target.sendPackets((ServerBasePacket)new S_ServerMessage(634));
         return;
       }
       pc.setFightId(target.getId());
       target.setFightId(pc.getId());
       target.sendPackets((ServerBasePacket)new S_Message_YN(630, pc.getName()));
     }
   }



   public String getType() {
     return "[C] C_Fight";
   }
 }


