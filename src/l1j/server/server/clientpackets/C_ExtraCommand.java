 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_ExtraCommand extends ClientBasePacket {
   private static final String C_EXTRA_COMMAND = "[C] C_ExtraCommand";

   public C_ExtraCommand(byte[] abyte0, GameClient client) throws Exception {
     super(abyte0);
     int actionId = readC();



     L1PcInstance pc = client.getActiveChar();
     if (pc == null || pc.isGhost()) {
       return;
     }
     if (pc.isInvisble()) {
       return;
     }
     if (pc.get_teleport()) {
       return;
     }


     int gfxId_Sub = pc.getCurrentSpriteId();
     if (gfxId_Sub != 0 && gfxId_Sub != 1 && gfxId_Sub != 20553 && gfxId_Sub != 48 && gfxId_Sub != 138 && gfxId_Sub != 37 && gfxId_Sub != 20278 && gfxId_Sub != 20279 && gfxId_Sub != 2786 && gfxId_Sub != 2796 && gfxId_Sub != 6658 && gfxId_Sub != 6661 && gfxId_Sub != 6671 && gfxId_Sub != 6650 && gfxId_Sub != 20567 && gfxId_Sub != 20577 && gfxId_Sub != 18520 && gfxId_Sub != 18499) {





       Poly_Sub_Action(pc, actionId);
     } else {
       return;
     }

     if (pc.hasSkillEffect(67)) {
       int gfxId = pc.getCurrentSpriteId();
       if (gfxId != 6080 && gfxId != 6094) {
         return;
       }
     } else {
       S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), actionId);
       Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)gfx);
     }
   }




   public void Poly_Sub_Action(L1PcInstance pc, int actionId) {
     long curtime = System.currentTimeMillis() / 1000L;
     if (pc.getQuizTime2() + 5L > curtime) {
       return;
     }


     if (actionId == 68) {
       int gfxid = 3204;
       pc.send_effect(gfxid, true);
       pc.setQuizTime2(curtime);
     } else if (actionId == 69) {
       int gfxid = 3205;
       pc.send_effect(gfxid, true);
       pc.setQuizTime2(curtime);
     } else if (actionId == 67) {
       int gfxid = 3206;
       pc.send_effect(gfxid, true);
       pc.setQuizTime2(curtime);
     } else if (actionId == 66) {
       int gfxid = 3207;
       pc.send_effect(gfxid, true);
       pc.setQuizTime2(curtime);
     }
   }



   public String getType() {
     return "[C] C_ExtraCommand";
   }
 }


