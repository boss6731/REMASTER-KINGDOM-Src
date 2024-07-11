 package l1j.server.server.utils;




 public class ByteArrayUtil
 {
   private final byte[] _byteArray;

   public ByteArrayUtil(byte[] byteArray) {
     this._byteArray = byteArray;
   }

   public String getTerminatedString(int i) {
     StringBuffer stringbuffer = new StringBuffer();
     for (int j = i; j < this._byteArray.length && this._byteArray[j] != 0; j++) {
       stringbuffer.append((char)this._byteArray[j]);
     }

     return stringbuffer.toString();
   }

   public String dumpToString() {
     StringBuffer stringbuffer = new StringBuffer();
     int j = 0;
     for (int k = 0; k < this._byteArray.length; k++) {
       if (j % 16 == 0) {
         stringbuffer.append(fillHex(k, 4) + ": ");
       }

       stringbuffer.append(
           fillHex(this._byteArray[k] & 0xFF, 2) + " ");
       if (++j == 16) {


         stringbuffer.append("   ");
         int i1 = k - 15;
         for (int l1 = 0; l1 < 16; l1++) {
           byte byte0 = this._byteArray[i1++];
           if (byte0 > 31 && byte0 < 128) {
             stringbuffer.append((char)byte0);
           } else {
             stringbuffer.append('.');
           }
         }

         stringbuffer.append("\n");
         j = 0;
       }
     }
     int l = this._byteArray.length % 16;
     if (l > 0) {
       for (int j1 = 0; j1 < 17 - l; j1++) {
         stringbuffer.append("   ");
       }

       int k1 = this._byteArray.length - l;
       for (int i2 = 0; i2 < l; i2++) {
         byte byte1 = this._byteArray[k1++];
         if (byte1 > 31 && byte1 < 128) {
           stringbuffer.append((char)byte1);
         } else {
           stringbuffer.append('.');
         }
       }

       stringbuffer.append("\n");
     }
     return stringbuffer.toString();
   }

   private String fillHex(int i, int j) {
     String s = Integer.toHexString(i);
     for (int k = s.length(); k < j; k++) {
       s = "0" + s;
     }

     return s;
   }
 }


