     package l1j.server.server.serverpackets;

     public class S_CharSpeedUpdate
       extends ServerBasePacket
     {
       private static final String S_CharSpeedUpdate = "[S] S_CharSpeedUpdate";

       public S_CharSpeedUpdate(int objid, int speed, int level) {
         writeC(60);
         writeD(objid);
         writeC(speed);
         writeC(level);
         writeC(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_CharSpeedUpdate";
       }
     }


