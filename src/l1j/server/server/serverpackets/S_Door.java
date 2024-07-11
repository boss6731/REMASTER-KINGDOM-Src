     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1DoorInstance;























     public class S_Door
       extends ServerBasePacket
     {
       private static final String S_DOOR = "[S] S_Door";

       public S_Door(L1DoorInstance door) {
         buildPacket(door.getEntranceX(), door.getEntranceY(), door
             .getDirection(), door.getPassable());
       }

       public S_Door(int x, int y, int direction, int passable) {
         buildPacket(x, y, direction, passable);
       }

       private void buildPacket(int x, int y, int direction, int passable) {
         writeC(38);
         writeH(x);
         writeH(y);
         writeC(direction);
         writeC(passable);
       }



       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_Door";
       }
     }


