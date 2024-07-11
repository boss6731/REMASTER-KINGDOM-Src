     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1DoorInstance;



     public class S_DoorPack
       extends ServerBasePacket
     {
       private static final String S_DOOR_PACK = "[S] S_DoorPack";
       private static final int STATUS_POISON = 1;

       public S_DoorPack(L1DoorInstance door) {
         buildPacket(door);
       }

       private void buildPacket(L1DoorInstance door) {
         writeC(186);
         writeH(door.getX());
         writeH(door.getY());
         writeD(door.getId());

         int spriteId = door.getCurrentSpriteId();
         writeH(spriteId);
         int doorStatus = door.getStatus();
         int openStatus = door.getOpenStatus();
         if (door.isDead()) {
           writeC(doorStatus);
         } else if (openStatus == 28) {
           writeC(openStatus);
         } else if (door.getMaxHp() > 1 && doorStatus != 0) {
           writeC(doorStatus);
         } else {
           writeC(openStatus);
         }
         writeC(0);
         writeC(0);
         writeC(0);
         writeD(1);
         writeH(0);
         if (spriteId == 12164 || spriteId == 12167 || spriteId == 12170 || spriteId == 12987 || spriteId == 12989 || spriteId == 12991 || spriteId == 12127 || spriteId == 12129 || spriteId == 12131 || spriteId == 12133) {



           writeS("$440");
         } else if (spriteId == 339 || spriteId == 1336 || spriteId == 12163) {



           writeS("$441");
         } else {
           writeS(null);
         }
         writeS(null);
         int status = 0;
         if (door.getPoison() != null &&
           door.getPoison().getEffectId() == 1) {
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


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_DoorPack";
       }
     }


