      */ package l1j.server.server.serverpackets;
























     public class S_ChangeShape
       extends ServerBasePacket
     {
       public S_ChangeShape(int objId, int polyId) {
         buildPacket(objId, polyId, false);
       }

       public S_ChangeShape(int objId, int polyId, boolean weaponTakeoff) {
         buildPacket(objId, polyId, weaponTakeoff);
       }

       private void buildPacket(int objId, int polyId, boolean weaponTakeoff) {
         writeC(16);
         writeD(objId);
         writeH(polyId);

         writeC(weaponTakeoff ? 0 : 29);
         writeC(255);
         writeC(255);
         writeC(0);
         writeS("abcd");
       }
       public S_ChangeShape(int objId, int polyId, int currentWeapon) {
         writeC(16);
         writeD(objId);
         writeH(polyId);
         writeC(currentWeapon);
         writeC(255);
         writeC(255);
         writeC(0);
         writeS("abcd");
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


