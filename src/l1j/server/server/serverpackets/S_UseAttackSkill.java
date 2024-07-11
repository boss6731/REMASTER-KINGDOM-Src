 package l1j.server.server.serverpackets;

 import java.util.concurrent.atomic.AtomicInteger;
 import l1j.server.server.model.L1Character;



 public class S_UseAttackSkill
   extends ServerBasePacket
 {
   private static final String S_USE_ATTACK_SKILL = "[S] S_UseAttackSkill";
   private static AtomicInteger _sequentialNumber = new AtomicInteger(0);

   public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId) {
     buildPacket(cha, targetobj, spellgfx, x, y, actionId, 6, true);
   }

   public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, boolean motion) {
     buildPacket(cha, targetobj, spellgfx, x, y, actionId, 0, motion);
   }

   public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, int isHit) {
     buildPacket(cha, targetobj, spellgfx, x, y, actionId, isHit, true);
   }

   private void buildPacket(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, int isHit, boolean withCastMotion) {
     if (cha instanceof l1j.server.server.model.Instance.L1PcInstance)
     {
       if (cha.hasSkillEffect(67) && actionId == 18) {
         int tempchargfx = cha.getCurrentSpriteId();
         if (tempchargfx == 5727 || tempchargfx == 5730) {
           actionId = 19;
         } else if (tempchargfx == 5733 || tempchargfx == 5736) {
           actionId = 1;
         }
       }
     }
     if (cha.equalsCurrentSprite(4013)) {
       actionId = 1;
     }
     int newheading = calcheading(cha.getX(), cha.getY(), x, y);
     cha.setHeading(newheading);
     writeC(5);
     writeC(actionId);
     writeD(withCastMotion ? cha.getId() : 0);
     writeD(targetobj);
     writeC(isHit);
     writeC(0);
     writeC(newheading);
     writeD(_sequentialNumber.incrementAndGet());
     writeH(spellgfx);
     writeC(6);
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

   private static int calcheading(int myx, int myy, int tx, int ty) {
     int newheading = 0;
     if (tx > myx && ty > myy) {
       newheading = 3;
     }
     if (tx < myx && ty < myy) {
       newheading = 7;
     }
     if (tx > myx && ty == myy) {
       newheading = 2;
     }
     if (tx < myx && ty == myy) {
       newheading = 6;
     }
     if (tx == myx && ty < myy) {
       newheading = 0;
     }
     if (tx == myx && ty > myy) {
       newheading = 4;
     }
     if (tx < myx && ty > myy) {
       newheading = 5;
     }
     if (tx > myx && ty < myy) {
       newheading = 1;
     }
     return newheading;
   }


   public String getType() {
     return "[S] S_UseAttackSkill";
   }
 }


