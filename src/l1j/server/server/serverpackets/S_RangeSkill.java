 package l1j.server.server.serverpackets;

 import java.util.concurrent.atomic.AtomicInteger;
 import l1j.server.server.model.L1Character;


 public class S_RangeSkill
   extends ServerBasePacket
 {
   private static final String S_RANGE_SKILL = "[S] S_RangeSkill";
   private static AtomicInteger _sequentialNumber = new AtomicInteger(0);

   public static final int TYPE_NODIR = 0;

   public static final int TYPE_DIR = 8;


   public S_RangeSkill(L1Character cha, L1Character[] target, int spellgfx, int actionId, int type) {
     buildPacket(cha, target, spellgfx, actionId, type);
   }

   private void buildPacket(L1Character cha, L1Character[] target, int spellgfx, int actionId, int type) {
     writeC(137);
     writeC(actionId);
     writeD(cha.getId());
     writeH(cha.getX());
     writeH(cha.getY());
     L1Character targeter = null;
     if (target != null && target.length > 0) {
       targeter = target[0];
     }

     int targetX = (targeter == null) ? 0 : target[0].getX();
     int targetY = (targeter == null) ? 0 : target[0].getY();

     if (type == 0) {
       writeC(cha.getHeading());
     } else if (type == 8) {
       int newHeading = calcheading(cha.getX(), cha.getY(), targetX, targetY);
       cha.setHeading(newHeading);
       writeC(cha.getHeading());
     }
     writeD(_sequentialNumber.incrementAndGet());
     writeH(spellgfx);
     writeC(type);
     writeH(0);


     writeH(targetX);
     writeH(targetY);

     writeH(target.length);
     for (int i = 0; i < target.length; i++) {
       writeD(target[i].getId());
       writeC(32);
       writeC(actionId);
     }
   }


   public byte[] getContent() {
     return getBytes();
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
     return "[S] S_RangeSkill";
   }
 }


