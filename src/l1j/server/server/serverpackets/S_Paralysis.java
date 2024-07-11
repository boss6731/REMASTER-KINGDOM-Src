 package l1j.server.server.serverpackets;public class S_Paralysis extends ServerBasePacket { public static final int TYPE_PARALYSIS = 1; public static final int TYPE_PARALYSIS2 = 2; public static final int TYPE_SLEEP = 3; public static final int TYPE_FREEZE = 4;
   public static final int TYPE_STUN = 5;
   public static final int TYPE_BIND = 6;
   public static final int TYPE_TELEPORT_UNLOCK = 7;

   public S_Paralysis(int type) {
     writeC(33);
     writeC(type);
   } public static final int TYPE_TELEPORT_UNLOCK2 = 10; public static final int TYPE_RIP = 8; public static final int TYPE_PERADO = 9; public static final int TYPE_FORCE_STUN = 10; public static final int TYPE_PANTHERA = 11; public static final int TYPE_PHANTOM = 12; public static final int TYPE_OSIRIS = 13; private static final String _S__2F_PARALYSIS = "[S] S_Paralysis";
   public S_Paralysis(byte[] buff) {
     writeC(33);
     writeByte(buff);
   }

   public S_Paralysis(int type, boolean flag) {
     writeC(33);
     switch (type) {
       case 1:
         writeC(flag ? 2 : 3);
         return;

       case 2:
         writeC(flag ? 4 : 5);
         return;

       case 7:
         writeC(7);
         return;

       case 3:
         writeC(flag ? 10 : 11);
         return;

       case 4:
         writeC(flag ? 12 : 13);
         return;

       case 5:
         writeC(flag ? 22 : 23);
         return;

       case 6:
         writeC(flag ? 24 : 25);
         return;

       case 8:
         writeC(flag ? 26 : 27);
         return;

       case 9:
         writeC(flag ? 30 : 31);
         return;

       case 10:
         writeC(flag ? 34 : 35);
         return;

       case 11:
         writeC(flag ? 36 : 37);
         return;

       case 12:
         writeC(flag ? 38 : 39);
         return;

       case 13:
         writeC(flag ? 40 : 41);
         return;
     }

     writeH(0);
   }

   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_Paralysis";
   } }


