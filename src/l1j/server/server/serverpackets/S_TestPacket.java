 package l1j.server.server.serverpackets;

 import java.util.StringTokenizer;




 public class S_TestPacket
   extends ServerBasePacket
 {
   private static final String S_TestPack = "S_TestPack";
   public static final int a = 103;
   public static final int b = 103;
   public static final int 伺服器問候語 = 103;

   public S_TestPacket(int type, int gfxid, int messageCode, String color) {
     StringTokenizer st;
     writeC(19);
     writeC(type);
     switch (type) {
       case 103:
         writeC(0);
         writeC(8);
         writeBit((gfxid * 2));
         writeC(16);
         writeBit((messageCode * 2));
         writeC(26);
         writeC(3);
         st = new StringTokenizer(color.toString());
         while (st.hasMoreTokens()) {
           writeC(Integer.parseInt(st.nextToken(), 16));
         }
         writeC(32);
         writeC(20);
         writeH(0);
         break;
     }
   }


   public S_TestPacket(int type) {
     String 消息 = "9c 43 03 08 01 12 00";
     StringTokenizer st = new StringTokenizer(消息.toString());
     while (st.hasMoreTokens()) {
       writeC(Integer.parseInt(st.nextToken(), 16));
     }
   }

   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "S_TestPack";
   }
 }


