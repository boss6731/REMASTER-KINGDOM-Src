     package l1j.server.server.serverpackets;

     public class S_FishingTime
       extends ServerBasePacket
     {
       public static final int FISH_WINDOW = 63;
       public static final int CAUI = 76;

       public S_FishingTime(int type, int subType, boolean ck, int i) {
         writeC(19);
         writeH(type);
         switch (type) {
           case 63:
             writeC(8);
             writeC(subType);
             if (subType == 1) {
               writeC(16);
               if (ck) {
                 writeC(i);
               } else {
                 writeH(i);
               }
               writeC(24);
               writeC(ck ? 2 : 1);
               writeH(0); break;
             }  if (subType == 2) {
               writeH(0);
             }
             break;
         }
       }

       public S_FishingTime(int type, int test, int time, String CName, boolean swich) {
         writeC(19);
         writeH(76);
         writeH(264);
         writeC(16);
         if (swich) {
           writeH(test);
           writeH(time);
           writeS(CName);
         } else {
           writeC(0);
         }
         writeH(0);
       }

       public byte[] getContent() {
         return getBytes();
       }
     }


