 package l1j.server.server.types;



 public class UByte8
 {
   public static byte[] fromArray(long[] buff) {
     byte[] byteBuff = new byte[buff.length * 4];

     for (int i = 0; i < buff.length; i++) {
       byteBuff[i * 4 + 0] = (byte)(int)(buff[i] & 0xFFL);
       byteBuff[i * 4 + 1] = (byte)(int)(buff[i] >> 8L & 0xFFL);
       byteBuff[i * 4 + 2] = (byte)(int)(buff[i] >> 16L & 0xFFL);
       byteBuff[i * 4 + 3] = (byte)(int)(buff[i] >> 24L & 0xFFL);
     }

     return byteBuff;
   }

   public static byte[] fromArray(char[] buff) {
     byte[] byteBuff = new byte[buff.length];

     for (int i = 0; i < buff.length; i++) {
       byteBuff[i] = (byte)(buff[i] & 0xFF);
     }

     return byteBuff;
   }

   public static byte fromUChar8(char c) {
     return (byte)(c & 0xFF);
   }


   public static byte[] fromULong32(long l) {
     byte[] byteBuff = new byte[4];

     byteBuff[0] = (byte)(int)(l & 0xFFL);
     byteBuff[1] = (byte)(int)(l >> 8L & 0xFFL);
     byteBuff[2] = (byte)(int)(l >> 16L & 0xFFL);
     byteBuff[3] = (byte)(int)(l >> 24L & 0xFFL);

     return byteBuff;
   }
 }


