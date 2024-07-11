 package BITOPERATION;
 import java.math.BigInteger;

 public class BITOPERATION {
   public static void main4(String[] args) {
     byte[] temp = { -26, -103, -97, -60, 5 };



     StringBuffer sb = new StringBuffer();
     for (int i = temp.length - 1; i >= 0; i--) {
       if (i != temp.length - 1) {
         String a = Integer.toBinaryString(temp[i] & Byte.MAX_VALUE);
         if (a.length() < 7) {
           StringBuffer sbb = new StringBuffer();
           for (int j = a.length() % 7; j < 7; j++)
             sbb.append("0");
           sbb.append(a);
           a = sbb.toString();
         }
         sb.append(a);
       } else {
         sb.append(Integer.toBinaryString(temp[i] & 0xFF));
       }
     }  System.out.println("價值：" + Integer.parseInt(sb.toString(), 2));
   }

   public static void main2(String[] args) {
     int temp = 1701031002;
     int i = 0;
     BigInteger b = new BigInteger("18446744073709551615");
     while (BigInteger.valueOf(temp).and(b).shiftRight((i + 1) * 7).longValue() > 0L) {
       StringBuffer sbb = new StringBuffer();
       System.out.println("價值：" + BigInteger.valueOf(temp).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(128L)).or(BigInteger.valueOf(128L)).intValue());
     }
     System.out.println("價值：" + BigInteger.valueOf(temp).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(128L)).intValue());
   }

   public static void main3(String[] args) {
     byte[] temp = { 16, -21, -17, -57, 121 };


     StringBuffer sb = new StringBuffer();
     for (int i = temp.length - 1; i >= 0; i--) {
       if (temp[i] == 16) {
         System.out.println("價值：" + Integer.parseInt(sb.toString(), 2));
         sb = new StringBuffer();
       } else if (i != temp.length - 1) {
         String a = Integer.toBinaryString(temp[i] & Byte.MAX_VALUE);
         if (a.length() < 7) {
           StringBuffer sbb = new StringBuffer();
           for (int k = a.length() % 7; k < 7; k++)
             sbb.append("0");
           sbb.append(a);
           a = sbb.toString();
         }
         sb.append(a);
       } else {
         sb.append(Integer.toBinaryString(temp[i] & 0xFF));
       }
     }
     int temp2 = 1707131504;
     int j = 0;
     BigInteger b = new BigInteger("18446744073709551615");
     while (BigInteger.valueOf(temp2).and(b).shiftRight((j + 1) * 7).longValue() > 0L) {
       StringBuffer sbb = new StringBuffer();
       System.out.println("價值：" + BigInteger.valueOf(temp2).and(b).shiftRight(7 * j++).remainder(BigInteger.valueOf(128L)).or(BigInteger.valueOf(128L)).intValue());
     }
     System.out.println("價值：" + BigInteger.valueOf(temp2).and(b).shiftRight(7 * j++).remainder(BigInteger.valueOf(128L)).intValue());
   }

   public static void main(String[] args) {
     byte[] temp = { 50, 48 };


     String str = new String(temp);
     System.out.println("價值：" + str);
   }
 }


