     package l1j.server.server.utils;

     public class HexHelper {
       public static final int[] HEX_TABLE = new int[] { 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255 };


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

       public static String DataToPacket(byte[] data, int len) {
         StringBuffer result = new StringBuffer();
         int counter = 0;
         for (int i = 0; i < len; i++) {
           if (counter % 16 == 0) result.append(HexToDex(i, 4) + ": ");
           result.append(HexToDex(data[i] & 0xFF, 2) + " ");
           counter++;
           if (counter == 16) {
             result.append("   ");
             int charpoint = i - 15;
             for (int a = 0; a < 16; a++) {
               int t1 = data[charpoint++];
               if (t1 > 31 && t1 < 128) { result.append((char)t1); }
               else { result.append('.'); }

             }  result.append("\n"); counter = 0;
           }
         }
         int rest = data.length % 16;
         if (rest > 0) {
           for (int j = 0; j < 17 - rest; ) { result.append("   "); j++; }
            int charpoint = data.length - rest;
           for (int a = 0; a < rest; a++) {
             int t1 = data[charpoint++];
             if (t1 > 31 && t1 < 128) { result.append((char)t1); }
             else { result.append('.'); }

           }  result.append("\n");
         }
         return result.toString();
       }

       private static String HexToDex(int data, int digits) {
         String number = Integer.toHexString(data);
         for (int i = number.length(); i < digits; ) { number = "0" + number; i++; }
          return number;
       }
     }


