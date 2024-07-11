 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;

 public class S_SkillSound
   extends ServerBasePacket {
   private static final String S_SKILL_SOUND = "[S] S_SkillSound";

   public static void broadcast(L1Character cha, int id) {
     S_SkillSound sound = new S_SkillSound(cha.getId(), id);
     if (cha instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)cha;
       pc.sendPackets(sound, false);
     }
     cha.broadcastPacket(sound);
   }

   public S_SkillSound(int objid, int gfxid, int aid) {
     buildPacket(objid, gfxid, aid);
   }

   public S_SkillSound(int objid, int gfxid, int aid, int x, int y) {
     buildPacket(objid, gfxid, aid, x, y);
   }

   public S_SkillSound(int objid, int gfxid) {
     buildPacket(objid, gfxid, 0);
   }


   private void buildPacket(int objid, int gfxid, int aid) {
     if (gfxid == 1100) {
       (new Throwable()).printStackTrace();
     }

     writeC(86);
     writeD(objid);
     writeH(gfxid);
     writeH(0);
     writeD(0);
   }


   private void buildPacket(int objid, int gfxid, int aid, int x, int y) {
     if (gfxid == 1100) {
       (new Throwable()).printStackTrace();
     }


     writeC(86);
     writeD(objid);
     writeH(gfxid);
     writeH(0);
     writeD(0);
     writeH(x);
     writeH(y);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_SkillSound";
   }
 }


