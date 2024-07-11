 package l1j.server.server.serverpackets;




 public class S_PssConfig
   extends ServerBasePacket
 {
   public static final int Pss_Config = 2501;
   private static final byte[] config_byte = "LIVE_LIN_PSSCONFIG_100020859349".getBytes();

   public static final S_PssConfig CONFIG = new S_PssConfig();



   public S_PssConfig() {
     writeC(19);
     writeH(2501);
     writeC(10);
     writeBytesWithLength(config_byte);
   }



   public static void init() {}


   public byte[] getContent() {
     return getBytes();
   }
 }


