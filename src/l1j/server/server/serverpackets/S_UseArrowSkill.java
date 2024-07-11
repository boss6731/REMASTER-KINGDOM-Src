 package l1j.server.server.serverpackets;

 import java.util.concurrent.atomic.AtomicInteger;
 import l1j.server.server.model.L1Character;



 public class S_UseArrowSkill
   extends ServerBasePacket
 {
   private static final String S_USE_ARROW_SKILL = "[S] S_UseArrowSkill";
   private static AtomicInteger _sequentialNumber = new AtomicInteger(0);

   public S_UseArrowSkill(int attacker_id, int ox, int oy, int oh, int act_id, int target_id, int spellgfx, int tx, int ty, boolean is_hit) {
     writeC(5);
     writeC(act_id);
     writeD(attacker_id);
     writeD(target_id);
     writeC(is_hit ? 6 : 0);
     writeC(0);
     writeC(oh);
     writeD(_sequentialNumber.incrementAndGet());
     writeH(spellgfx);
     writeC(127);
     writeH(ox);
     writeH(oy);
     writeH(tx);
     writeH(ty);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
   }




   public S_UseArrowSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, boolean isHit) {
     int aid = 1;


     int spriteId = cha.getCurrentSpriteId();


     if (!(cha instanceof l1j.server.server.model.Instance.L1PcInstance) &&
       spriteId == 14170) {
       if (cha.MoBTripleArrow) {
         aid = 21;
       } else {
         aid = 63;
       }
     }


     if (!(cha instanceof l1j.server.server.model.Instance.L1PcInstance) &&
       spriteId == 15659) {
       if (cha.MoBTripleArrow_PRISON) {
         aid = 75;
       } else {
         aid = 1;
       }
     }


     if (spriteId == 3860 || spriteId == 11382) {
       aid = 21;
     }
     writeC(5);
     writeC(aid);
     writeD(cha.getId());
     writeD(targetobj);
     writeC(isHit ? 6 : 0);
     writeC(0);
     writeC(cha.getHeading());


     writeD(_sequentialNumber.incrementAndGet());
     writeH(spellgfx);
     writeC(127);
     writeH(cha.getX());
     writeH(cha.getY());
     writeH(x);
     writeH(y);





     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
   }


   public byte[] getContent() {
     byte[] b = getBytes();

     return b;
   }

   public String getType() {
     return "[S] S_UseArrowSkill";
   }
 }


