 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1SignboardInstance;


 public class S_SignboardPack
   extends ServerBasePacket
 {
   private static final String S_SIGNBOARD_PACK = "[S] S_SignboardPack";
   private static final int STATUS_POISON = 1;

   public S_SignboardPack(L1SignboardInstance signboard) {
     writeC(186);
     writeH(signboard.getX());
     writeH(signboard.getY());
     writeD(signboard.getId());
     writeH(signboard.getCurrentSpriteId());
     writeC(0);
     writeC(getDirection(signboard.getHeading()));
     writeC(0);
     writeC(0);
     writeD(0);
     writeH(0);
     writeS(null);
     writeS(signboard.getName());
     int status = 0;
     if (signboard.getPoison() != null &&
       signboard.getPoison().getEffectId() == 1) {
       status |= 0x1;
     }

     writeC(status);
     writeD(0);
     writeS(null);
     writeS(null);
     writeC(0);
     writeC(255);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(255);
     writeC(255);
     writeC(0);
     writeC(0);
     writeC(255);
     writeH(0);
   }

   private int getDirection(int heading) {
     int dir = 0;
     switch (heading) { case 2:
         dir = 1; break;
       case 3: dir = 2; break;
       case 4: dir = 3; break;
       case 6: dir = 4; break;
       case 7: dir = 5; break; }

     return dir;
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_SignboardPack";
   }
 }


