     package l1j.server.server.serverpackets;

     import l1j.server.server.model.gametime.GameTimeClock;


     public class S_GameTime
       extends ServerBasePacket
     {
       public S_GameTime(int time) {
         buildPacket(time);
       }

       public S_GameTime(long time) {
         buildPacket(time);
       }

       private void buildPacket(long time) {
         writeC(156);
         writeD(time);
       }

       public S_GameTime() {
         int time = GameTimeClock.getInstance().getGameTime().getSeconds();
         buildPacket(time);
       }

       private void buildPacket(int time) {
         writeC(156);
         writeD(time);
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


