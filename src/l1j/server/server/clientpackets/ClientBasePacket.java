 package l1j.server.server.clientpackets;

 import java.nio.ByteBuffer;
 import java.nio.charset.Charset;
 import java.util.logging.Logger;
 import l1j.server.server.GameClient;


 public abstract class ClientBasePacket
 {
   private static Logger _log = Logger.getLogger(ClientBasePacket.class.getName());
   private byte[] _decrypt;
   private int _off;

   public ClientBasePacket(byte[] abyte0) {
     _log.finest("type=" + getType() + ", len=" + abyte0.length);
     this._decrypt = abyte0;
     this._off = 1;
   }


   public ClientBasePacket(ByteBuffer bytebuffer, GameClient clientthread) {}

   public void clear() {
     this._decrypt = null;
     this._off = 0;
   }

   public int readD() {
     int i = 0;
     try {
       i = this._decrypt[this._off++] & 0xFF;
       i |= this._decrypt[this._off++] << 8 & 0xFF00;
       i |= this._decrypt[this._off++] << 16 & 0xFF0000;
       i |= this._decrypt[this._off++] << 24 & 0xFF000000;
     } catch (Exception exception) {}
     return i;
   }

   public void readP(int index) {
     this._off += index;
   }
   public boolean isRead(int size) {
     return (this._off + size <= this._decrypt.length);
   }
   public int readC() {
     int i = this._decrypt[this._off++] & 0xFF;
     return i;
   }

   public int readKH() {
     int i = (this._decrypt[this._off++] & 0xFF) - 128;
     i |= (this._decrypt[this._off++] & 0xFF) << 7;
     return i;
   }

   public int readKCH() {
     int i = (this._decrypt[this._off++] & 0xFF) - 128;
     i |= (this._decrypt[this._off++] & 0xFF) - 128 << 7;
     i |= (this._decrypt[this._off++] & 0xFF) << 14;
     return i;
   }

   public int readK() {
     int i = (this._decrypt[this._off++] & 0xFF) - 128;
     i |= (this._decrypt[this._off++] & 0xFF) - 128 << 7;
     i |= (this._decrypt[this._off++] & 0xFF) - 128 << 14;
     i |= (this._decrypt[this._off++] & 0xFF) << 21;
     return i;
   }

   public int read4(int size) {
     if (size == 0) return 0;
     int i = this._decrypt[this._off++] & Byte.MAX_VALUE;
     if (size == 1) return i;
     if (size >= 2) i |= (this._decrypt[this._off++] << 8 & 0x7F00) >> 1;
     if (size >= 3) i |= (this._decrypt[this._off++] << 16 & 0x7F0000) >> 2;
     if (size >= 4) i |= (this._decrypt[this._off++] << 24 & 0x7F000000) >> 3;
     if (size >= 5) i = (int)(i | (this._decrypt[this._off++] << 32L & 0x7F00000000L) >> 4L);
     return i;
   }

   public int read_size() {
     int i = 0;

     while ((this._decrypt[this._off + i] & 0xFF) >= 128)
     {

       i++;
     }

     return i + 1;
   }


   public byte[] readByteL(int length) {
     byte[] result = new byte[length];

     try {
       System.arraycopy(this._decrypt, this._off, result, 0, length);
       this._off += length;
     } catch (Exception exception) {}


     return result;
   }

   public int readH() {
     int i = this._decrypt[this._off++] & 0xFF;
     i |= this._decrypt[this._off++] << 8 & 0xFF00;
     return i;
   }

   public int readCH() {
     int i = this._decrypt[this._off++] & 0xFF;
     i |= this._decrypt[this._off++] << 8 & 0xFF00;
     i |= this._decrypt[this._off++] << 16 & 0xFF0000;
     return i;
   }

   public double readF() {
     long l = (this._decrypt[this._off++] & 0xFF);
     l |= (this._decrypt[this._off++] << 8 & 0xFF00);
     l |= (this._decrypt[this._off++] << 16 & 0xFF0000);
     l |= (this._decrypt[this._off++] << 24 & 0xFF000000);
     l |= this._decrypt[this._off++] << 32L & 0xFF00000000L;
     l |= this._decrypt[this._off++] << 40L & 0xFF0000000000L;
     l |= this._decrypt[this._off++] << 48L & 0xFF000000000000L;
     l |= this._decrypt[this._off++] << 56L & 0xFF00000000000000L;
     return Double.longBitsToDouble(l);
   }

   public String readS(Charset charset) {
     String s = new String(this._decrypt, this._off, this._decrypt.length - this._off, charset);
     s = s.substring(0, s.indexOf(false));
     this._off += (s.getBytes(charset)).length + 1;
     return s;
   }

   public String readS() {
     String s = null;

     try { s = new String(this._decrypt, this._off, this._decrypt.length - this._off, "EUC-KR");
       s = s.substring(0, s.indexOf(false));
       this._off += (s.getBytes("EUC-KR")).length + 1; }
     catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {  }
     catch (Exception exception) {}


     return s;
   }


   public String readS(int length) {
     String s = null;

     try {
       s = new String(this._decrypt, this._off, length, "MS949");
       this._off += length + 1;
     } catch (Exception exception) {}



     return s;
   }

   public String readS2(int length) {
     String s = null;

     try { s = new String(this._decrypt, this._off, length, "MS949");
       s = s.substring(0, s.indexOf(false));
       this._off += (s.getBytes("MS949")).length + 1; }
     catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {  }
     catch (Exception exception) {}


     return s;
   }


   public String readSS() {
     String text = null;
     int loc = 0;
     int start = 0;

     try { start = this._off;
       while (readH() != 0) {
         loc += 2;
       }
       StringBuffer test = new StringBuffer();
       do {
         if ((this._decrypt[start] & 0xFF) >= 127 || (this._decrypt[start + 1] & 0xFF) >= 127) {

           byte[] t = new byte[2];
           t[0] = this._decrypt[start + 1];
           t[1] = this._decrypt[start];
           test.append(new String(t, 0, 2, "MS949"));
         } else {

           test.append(new String(this._decrypt, start, 1, "MS949"));
         }
         start += 2;
         loc -= 2;
       } while (0 < loc);





       return text; } catch (Exception e) { return text; } finally { Exception exception = null; }

   }

   public String readS2() {
     String s = null;
     try {
       int size = this._decrypt[this._off++] & 0xFF;
       s = new String(this._decrypt, this._off, size, "MS949");
       this._off += size;
     } catch (Exception exception) {}

     return s;
   }


   public byte[] readByte() {
     byte[] result = new byte[this._decrypt.length - this._off];
     try {
       System.arraycopy(this._decrypt, this._off, result, 0, this._decrypt.length - this._off);
       this._off = this._decrypt.length;
     } catch (Exception exception) {}


     return result;
   }

   public byte[] readByte(int length) {
     byte[] result = new byte[length];
     try {
       System.arraycopy(this._decrypt, this._off, result, 0, length);
       this._off += length;
     } catch (Exception exception) {}


     return result;
   }

   public int readBit() {
     int i = 0;
     int j = 0;

     while ((this._decrypt[this._off] & 0xFF) >= 128) {
       i |= (this._decrypt[this._off++] & 0xFF ^ 0x80) << 7 * j++;
     }
     return i |= (this._decrypt[this._off++] & 0xFF) << 7 * j;
   }





   public int readM() {
     // Byte code:
     //   0: iconst_0
     //   1: istore_1
     //   2: aload_0
     //   3: getfield _decrypt : [B
     //   6: arraylength
     //   7: aload_0
     //   8: getfield _off : I
     //   11: isub
     //   12: istore_2
     //   13: iconst_0
     //   14: istore_3
     //   15: iload_3
     //   16: iload_2
     //   17: if_icmpge -> 75
     //   20: aload_0
     //   21: getfield _decrypt : [B
     //   24: aload_0
     //   25: dup
     //   26: getfield _off : I
     //   29: dup_x1
     //   30: iconst_1
     //   31: iadd
     //   32: putfield _off : I
     //   35: baload
     //   36: sipush #255
     //   39: iand
     //   40: istore #4
     //   42: iload_1
     //   43: iload #4
     //   45: sipush #128
     //   48: irem
     //   49: iload_3
     //   50: bipush #7
     //   52: imul
     //   53: ishl
     //   54: ior
     //   55: dup
     //   56: istore_1
     //   57: istore_1
     //   58: iload #4
     //   60: sipush #128
     //   63: if_icmpge -> 69
     //   66: goto -> 75
     //   69: iinc #3, 1
     //   72: goto -> 15
     //   75: iload_1
     //   76: ireturn
     // Line number table:
     //   Java source line number -> byte code offset
     //   #259	-> 0
     //   #260	-> 2
     //   #261	-> 13
     //   #262	-> 20
     //   #263	-> 42
     //   #264	-> 58
     //   #265	-> 66
     //   #261	-> 69
     //   #267	-> 75
     // Local variable table:
     //   start	length	slot	name	descriptor
     //   42	27	4	j	I
     //   15	60	3	a	I
     //   0	77	0	this	Ll1j/server/server/clientpackets/ClientBasePacket;
     //   2	75	1	i	I
     //   13	64	2	size	I
   }





   public String readS_Chat(int length) {
     String s = null;
     try {
       s = new String(this._decrypt, this._off, length, "MS949");
       this._off += length;
     } catch (Exception exception) {}

     return s;
   }




   public String getType() {
     return "[C] " + getClass().getSimpleName();
   }
 }


