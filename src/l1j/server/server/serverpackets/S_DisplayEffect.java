     package l1j.server.server.serverpackets;

     public class S_DisplayEffect
       extends ServerBasePacket
     {
       public static final int PUPLE_BORDER_DISPLAY = 1;
       public static final int QUAKE_DISPLAY = 2;
       public static final int FIREWORK_DISPLAY = 3;
       public static final int RINDVIOR_LIGHT_DISPLAY = 4;
       public static final int LIFECRY_DISPLAY = 5;
       public static final int BLUE_BORDER_DISPLAY = 6;
       public static final int VALAKAS_BORDER_DISPLAY = 7;
       public static final int RINDVIOR_BORDER_DISPLAY = 8;
       public static final int BLACK_DISPLAY = 10;
       public static final int BLOOD_DUNGEON_WHITE = 17;
       public static final int BLOOD_DUNGEON_GREEN = 18;
       public static final int BLOOD_DUNGEON_BLUE = 19;
       public static final int BLOOD_DUNGEON_RED = 20;
       public static final int BLOOD_DUNGEON_PUPLE = 21;
       public static final int BLOOD_DUNGEON_RAINBOW = 22;

       public static S_DisplayEffect newInstance(int value) {
         S_DisplayEffect eff = newInstance();
         eff.writeD(value);
         eff.writeH(0);
         return eff;
       }

       public static S_DisplayEffect newInstance() {
         return new S_DisplayEffect();
       }


       public S_DisplayEffect(int value) {
         writeC(108);
         writeC(83);
         writeD(value);
         writeH(0);
       }

       private S_DisplayEffect() {
         writeC(108);
         writeC(83);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "S_DisplayEffect";
       }
     }


