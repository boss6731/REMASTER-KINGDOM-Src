 package l1j.server.server.utils;
 
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.math.BigInteger;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class BinaryOutputStream
   extends OutputStream
 {
   protected final ByteArrayOutputStream _bao;
   
   public BinaryOutputStream(int capacity) {
     this._bao = new ByteArrayOutputStream(capacity);
   }
   
   public BinaryOutputStream() {
     this._bao = new ByteArrayOutputStream();
   }
 
   
   public void write(int b) throws IOException {
     this._bao.write(b);
   }
   
   public void writeS2(String text) {
     try {
       if (text != null && !text.isEmpty()) {
         byte[] name = text.getBytes("UTF-8");
         this._bao.write(name.length & 0xFF);
         if (name.length > 0) {
           this._bao.write(name);
         }
       } else {
         this._bao.write(0);
       } 
     } catch (Exception e) {
       e.printStackTrace();
     } 
   }
   
   public void writeD(int value) {
     this._bao.write(value & 0xFF);
     this._bao.write(value >> 8 & 0xFF);
     this._bao.write(value >> 16 & 0xFF);
     this._bao.write(value >> 24 & 0xFF);
   }
   
   public void writeD(long value) {
     this._bao.write((int)(value & 0xFFL));
     this._bao.write((int)(value >> 8L & 0xFFL));
     this._bao.write((int)(value >> 16L & 0xFFL));
     this._bao.write((int)(value >> 24L & 0xFFL));
   }
   
   public void writeH(int value) {
     this._bao.write(value & 0xFF);
     this._bao.write(value >> 8 & 0xFF);
   }
   
   public void writeC(int value) {
     this._bao.write(value & 0xFF);
   }
   
   public void writeP(int value) {
     this._bao.write(value);
   }
   
   public void writeL(long value) {
     this._bao.write((int)(value & 0xFFL));
   }
   
   public void writeF(double org) {
     long value = Double.doubleToRawLongBits(org);
     this._bao.write((int)(value & 0xFFL));
     this._bao.write((int)(value >> 8L & 0xFFL));
     this._bao.write((int)(value >> 16L & 0xFFL));
     this._bao.write((int)(value >> 24L & 0xFFL));
     this._bao.write((int)(value >> 32L & 0xFFL));
     this._bao.write((int)(value >> 40L & 0xFFL));
     this._bao.write((int)(value >> 48L & 0xFFL));
     this._bao.write((int)(value >> 56L & 0xFFL));
   }
 
 
   
   public void writeS(String text) {
     try {
       if (text != null) {
         this._bao.write(text.getBytes("UTF-8"));
       }
     } catch (Exception exception) {}
 
     
     this._bao.write(0);
   }
   
   public void writeBit(long value) {
     if (value < 0L) {
       String stringValue = Integer.toBinaryString((int)value);
       value = Long.valueOf(stringValue, 2).longValue();
     } 
     int i = 0;
     while (value >> 7 * (i + 1) > 0L) {
       this._bao.write((int)((value >> 7 * i++) % 128L | 0x80L));
     }
     this._bao.write((int)((value >> 7 * i) % 128L));
   }
   
   public void writeBit(int value1, int value2) {
     String str1 = Integer.toBinaryString(value1);
     String str2 = Integer.toBinaryString(value2);
     if (value1 <= 32767) str1 = "0" + str1; 
     String str3 = str2 + str1;
     writeBit(Long.valueOf(str3, 2).longValue());
   }
   
   public void write4bit(int value) {
     if (value <= 127) {
       this._bao.write(value & 0x7F);
     } else if (value <= 16383) {
       this._bao.write(value & 0x7F | 0x80);
       this._bao.write(value >> 7 & 0x7F);
     } else if (value <= 2097151) {
       this._bao.write(value & 0x7F | 0x80);
       this._bao.write(value >> 7 & 0x7F | 0x80);
       this._bao.write(value >> 14 & 0x7F);
     } else if (value <= 268435455) {
       this._bao.write(value & 0x7F | 0x80);
       this._bao.write(value >> 7 & 0x7F | 0x80);
       this._bao.write(value >> 14 & 0x7F | 0x80);
       this._bao.write(value >> 21 & 0x7F);
     } else if (value <= 34359738367L) {
       this._bao.write(value & 0x7F | 0x80);
       this._bao.write(value >> 7 & 0x7F | 0x80);
       this._bao.write(value >> 14 & 0x7F | 0x80);
       this._bao.write(value >> 21 & 0x7F | 0x80);
       this._bao.write(value >> 28 & 0x7F);
     } 
   }
   public void write7B(long value) {
     int i = 0;
     BigInteger b = new BigInteger("18446744073709551615");
     while (BigInteger.valueOf(value).and(b).shiftRight((i + 1) * 7).longValue() > 0L)
       this._bao.write(BigInteger.valueOf(value).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(128L)).or(BigInteger.valueOf(128L)).intValue()); 
     this._bao.write(BigInteger.valueOf(value).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(128L)).intValue());
   }
 
   
   public void writeK(int value) {
     int valueK = value / 128;
     if (valueK == 0) {
       this._bao.write(value);
     } else if (valueK <= 127) {
       this._bao.write((value & 0x7F) + 128);
       this._bao.write(valueK);
     } else if (valueK <= 255) {
       this._bao.write((value & 0x7F) + 128);
       this._bao.write((valueK & 0x7F) + 128);
       this._bao.write(valueK / 128);
     } else {
       this._bao.write((value & 0x7F) + 128);
       this._bao.write((valueK & 0x7F) + 128);
       this._bao.write((valueK / 128 & 0x7F) + 128);
       this._bao.write(valueK / 16384);
     } 
   }
   
   public int writeLenght(int obj) {
     int length = 0;
     if (obj < 0)
     { BigInteger b = new BigInteger("18446744073709551615");
       for (; BigInteger.valueOf(obj).and(b).shiftRight((length + 1) * 7).longValue() > 0L; length++);
       length++; }
     
     else if (obj <= 127) { length = 1; }
     else if (obj <= 16383) { length = 2; }
     else if (obj <= 2097151) { length = 3; }
     else if (obj <= 268435455) { length = 4; }
     else if (obj <= 34359738367L) { length = 5; }
     
     return length;
   }
   
   public void writeInt32NoTag(int value) throws IOException {
     if (value >= 0) { writeRawVarInt32(value); }
     else { writeRawVarInt64(value); }
   
   } private void writeRawVarInt32(int value) throws IOException {
     while (true) {
       if ((value & 0xFFFFFF80) == 0) {
         writeRawByte(value);
         return;
       } 
       writeRawByte(value & 0x7F | 0x80);
       value >>>= 7;
     } 
   }
   private void writeRawVarInt64(long value) throws IOException {
     while (true) {
       if ((value & 0xFFFFFFFFFFFFFF80L) == 0L) {
         writeRawByte((int)value);
         return;
       } 
       writeRawByte((int)value & 0x7F | 0x80);
       value >>>= 7L;
     } 
   }
   
   private void writeRawByte(int value) throws IOException {
     writeRawByte((byte)value);
   }
   private void writeRawByte(byte value) throws IOException {
     this._bao.write(value);
   }
   
   public void writeByte(byte[] text) {
     try {
       if (text != null) {
         this._bao.write(text);
       }
     } catch (Exception exception) {}
   }
 
   
   public void clear() {
     try {
       this._bao.reset();
       this._bao.close();
     } catch (IOException e) {
       e.printStackTrace();
     } 
   }
   public int getLength() {
     return this._bao.size() + 2;
   }
   
   public int getSize() {
     return this._bao.size();
   }
   
   public byte[] getBytes() {
     return this._bao.toByteArray();
   }
   
   public void byteWrite(long value) {
     long temp = value / 128L;
     if (temp > 0L)
     { writeC(HexHelper.HEX_TABLE[(int)value % 128]);
       while (temp >= 128L) {
         writeC(HexHelper.HEX_TABLE[(int)temp % 128]);
         temp /= 128L;
       } 
       if (temp > 0L) writeC((int)temp);
        }
     else if (value == 0L) { writeC(0); }
     else
     { writeC(HexHelper.HEX_TABLE[(int)value]);
       writeC(0); }
   
   }
 
   
   public void writeStringWithLength(String s) {
     try {
       if (StringUtil.isNullOrEmpty(s)) { writeC(0); }
       else
       { byte[] data = s.getBytes("ZH-TW");
         writeBytesWithLength(data); }
     
     } catch (Exception e) {
       e.printStackTrace();
     } 
   }
   
   public void writeBytesWithLength(byte[] bytes) {
     if (bytes == null || bytes.length <= 0) { writeC(0); }
     else
     { if (bytes.length > 127) { writeBit(bytes.length); }
       else { writeC(bytes.length); }
        writeByte(bytes); }
   
   }
   
   public int getBitSize(long value) {
     if (value < 0L) {
       String stringValue = Integer.toBinaryString((int)value);
       value = Long.valueOf(stringValue, 2).longValue();
     } 
     int size = 0;
     for (; value >> (size + 1) * 7 > 0L; size++);
     size++;
     return size;
   }
   
   public void writeB(boolean b) {
     writeC(b ? 1 : 0);
   }
   
   public void writeB(Object o) {
     writeC((o != null) ? 1 : 0);
   }
 }


