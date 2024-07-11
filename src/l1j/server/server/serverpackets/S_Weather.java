 package l1j.server.server.serverpackets;


 public class S_Weather
   extends ServerBasePacket
 {
   private static final String S_WEATHER = "[S] S_Weather";

   public S_Weather(int weather) {
     buildPacket(weather);
   }

   private void buildPacket(int weather) {
     writeC(176);
     writeC(weather);
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_Weather";
   }
 }


