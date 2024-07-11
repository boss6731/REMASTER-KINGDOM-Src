     package l1j.server.server.serverpackets;

     public class S_CharPass
       extends ServerBasePacket {
       public static final int _케릭선택창진입 = 64;
       public static final int _케릭선택창진입2 = 10;
       public static final int _케릭선택창진입3 = 22;
       public static final int _비번생성창 = 23;
       public static final int _비번생성완료창 = 17;
       public static final int _비번입력창 = 20;
       public static final int _비번입력비번틀림 = 21;
       public static final int _비번변경답변 = 19;
       public static final int _비번인증완료 = 63;
       private static final String _S__19_Test = "[S] S_Test";

       public static S_CharPass do_fail_password(int failure_count, int maximum_count) {
         S_CharPass packet = new S_CharPass();
         packet.writeC(21);
         packet.writeD(165);
         packet.writeH(failure_count);
         packet.writeH(maximum_count);
         packet.writeD(0);
         return packet;
       }

       public S_CharPass() {
         writeC(43);
       }

       public S_CharPass(int val) {
         writeC(43);
         writeC(val);
         switch (val) {
           case 21:
             writeD(165);
             writeH(2);
             writeH(5);
             writeD(0);
             break;
           case 17:
             writeD(0);
             break;
           case 10:
             writeD(2);
             break;
           case 22:
             writeD(170);
             writeD(0);
             writeD(0);
             writeH(0);
             writeC(1);
             writeC(0);
             break;
         }
       }



       public S_CharPass(int val, boolean ck) {
         writeC(43);
         writeC(val);
         switch (val) {

           case 19:
             if (ck) {
               writeD(0);
               writeH(0);
               writeH(5);
               writeD(0); break;
             }
             writeD(165);
             writeH(1);
             writeH(5);
             writeD(0);
             break;

           case 22:
             if (ck) {
               writeD(0);
               writeH(0);
               writeD(5);
               writeD(0);
               writeH(1); break;
             }
             writeD(170);
             writeD(0);
             writeD(0);
             writeH(0);
             writeH(1);
             break;
         }
       }





       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_Test";
       }
     }


