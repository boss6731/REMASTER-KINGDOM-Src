 package l1j.server.server.utils;

 public class MJHexHelper {
   public static int parseRawByte(byte[] buff, int off) {
     return (off >= buff.length) ? 0 : (buff[off] & 0xFF);
   }

   public static int parseInt32(byte[] buff, int off) {
     return parseRawByte(buff, off) |
       parseRawByte(buff, off + 1) << 8 |
       parseRawByte(buff, off + 2) << 16 |
       parseRawByte(buff, off + 3) << 24;
   }

   public static int[] parseInt32Array(byte[] buff, int len) {
     int size = len / 4 + ((len % 4 == 0) ? 0 : 1);
     int[] arr = new int[size];
     int position = size * 4;
     for (int i = size - 1; i >= 0; i--) {
       position -= 4;
       arr[i] = parseInt32(buff, position);
     }
     return arr;
   }

   public static byte[] parseHexStringToByteArray(String s, String tok) {
     String[] spl = s.split(tok);
     byte[] buff = new byte[spl.length];
     for (int i = spl.length - 1; i >= 0; i--)
       buff[i] = (byte)(Integer.parseInt(spl[i], 16) & 0xFF);
     return buff;
   }

   public static int[] parseHexStringToInt32Array(String s, String tok) {
     byte[] b = parseHexStringToByteArray(s, tok);
     return parseInt32Array(b, b.length);
   }

   public static boolean compareArrays(int[] buff1, int[] buff2, int len) {
     for (int i = len - 1; i >= 0; i--) {
       if (buff1[i] != buff2[i])
         return false;
     }
     return true;
   }

   public static boolean compareArrays(int[] buff1, byte[] buff2, int len) {
     return compareArrays(buff1, parseInt32Array(buff2, buff2.length), len);
   }

   public static boolean compareArrays(byte[] buff1, byte[] buff2, int len) {
     return compareArrays(parseInt32Array(buff1, len), parseInt32Array(buff2, len), len);
   }

   public static String toSourceString(byte[] data, int len) {
     StringBuilder sb = new StringBuilder(len * 6);
     for (int i = 0; i < len; i++) {
       sb.append(String.format("%02X ", new Object[] { Integer.valueOf(data[i] & 0xFF) }));
     }  return sb.toString();
   }
   public static String toStringEx(byte[] data, int len) {
     StringBuilder result = new StringBuilder(len * 4 + 16);
     int counter = 0;
     for (int i = 0; i < len; i++) {
       if (counter % 10 == 0 && counter != 20) {
         result.append("    ");
       }
       result.append(String.format("%02X ", new Object[] { Integer.valueOf(data[i] & 0xFF) }));
       counter++;
       if (counter == 20) {
         result.append("\n");
         counter = 0;
       }
     }
     return result.toString();
   }
   public static String toString(byte[] data, int len) {
     StringBuilder sb = new StringBuilder(len * 4 + 16);
     int cnt = 0;
     for (int i = 0; i < len; i++) {
       if (cnt % 16 == 0)
         sb.append(String.format("%04X : ", new Object[] { Integer.valueOf(i) }));
       sb.append(String.format("%02X ", new Object[] { Integer.valueOf(data[i] & 0xFF) }));
       cnt++;

       if (cnt == 16) {
         sb.append("\t");
         int p = i - 15;
         for (int j = 0; j < 16; j++)
           sb.append(toHexChar(data[p++]));
         sb.append("\n");
         cnt = 0;
       }
     }

     int rest = len % 16;
     if (rest > 0) {
       for (int k = 0; k < 17 - rest; k++) {
         sb.append("   ");
       }
       int p = len - rest;
       for (int j = 0; j < rest; j++)
         sb.append(toHexChar(data[p++]));
     }
     sb.append("\n");
     return sb.toString();
   }

   public static char toHexChar(int i) {
     if (i > 31 && i < 128) {
       return (char)i;
     }
     return '.';
   }

   public static int getTxtToBytesLength(String s) {
     int result = 1;
     int size = s.length();
     for (int i = 0; i < size; i++) {
       if (s.charAt(i) >= '') {
         result += 2;
       } else {
         result++;
       }
     }  return result;
   }
 }


