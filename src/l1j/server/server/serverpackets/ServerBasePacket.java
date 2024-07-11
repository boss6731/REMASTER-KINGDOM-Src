 package l1j.server.server.serverpackets;
 
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.math.BigInteger;
 
 
 
 
 public abstract class ServerBasePacket
 {
   private int OpKey;
   private boolean isKey = true;
   protected ByteArrayOutputStream _bao;
   private byte[] _byte;
   
   protected ServerBasePacket() {
     this._bao = new ByteArrayOutputStream(128);
   }
   
   protected ServerBasePacket(int capacity) {
     this._bao = new ByteArrayOutputStream(capacity);
   }
   
   public void clear() {
     if (this._bao == null) {
       return;
     }
     try {
       this._bao.reset();
       this._bao.close();
     } catch (IOException e) {
       
       e.printStackTrace();
     } 
     this._bao = null;
   }
 
   
   private void setKey(int i) {
     this.OpKey = i;
   }
 
   
   private int getKey() {
     return this.OpKey;
   }
   
   protected void writeD(int value) {
     this._bao.write(value & 0xFF);
     this._bao.write(value >> 8 & 0xFF);
     this._bao.write(value >> 16 & 0xFF);
     this._bao.write(value >> 24 & 0xFF);
   }
   
   protected void writeD(long value) {
     this._bao.write((int)(value & 0xFFL));
     this._bao.write((int)(value >> 8L & 0xFFL));
     this._bao.write((int)(value >> 16L & 0xFFL));
     this._bao.write((int)(value >> 24L & 0xFFL));
   }
   
   protected void writeL(long value) {
     this._bao.write((byte)(int)(value & 0xFFL));
     this._bao.write((byte)(int)(value >> 8L & 0xFFL));
     this._bao.write((byte)(int)(value >> 16L & 0xFFL));
     this._bao.write((byte)(int)(value >> 24L & 0xFFL));
     this._bao.write((byte)(int)(value >> 32L & 0xFFL));
     this._bao.write((byte)(int)(value >> 40L & 0xFFL));
     this._bao.write((byte)(int)(value >> 48L & 0xFFL));
     this._bao.write((byte)(int)(value >> 56L & 0xFFL));
   }
   
   protected void writeH(int value) {
     this._bao.write(value & 0xFF);
     this._bao.write(value >> 8 & 0xFF);
   }
   
   protected void writeC(int value) {
     this._bao.write(value & 0xFF);
     
     if (this.isKey) {
       setKey(value);
       this.isKey = false;
     } 
   }
   
   protected void writeSU16(String text) {
     try {
       if (text != null) {
         this._bao.write(text.getBytes("UTF-16LE"));
       }
     } catch (Exception e) {
       e.printStackTrace();
     } 
     
     this._bao.write(0);
     this._bao.write(0);
   }
 
   
   protected void writeK(int value) {
     int valueK = value / 128;
     if (valueK == 0) {
       this._bao.write(value);
     } else if (valueK <= 127) {
       this._bao.write((value & 0x7F) + 128);
       this._bao.write(valueK);
     } else if (valueK <= 16383) {
       this._bao.write((value & 0x7F) + 128);
       this._bao.write((valueK & 0x7F) + 128);
       this._bao.write(valueK / 128);
     } else if (valueK <= 2097151) {
       this._bao.write((value & 0x7F) + 128);
       this._bao.write((valueK & 0x7F) + 128);
       this._bao.write((valueK / 128 & 0x7F) + 128);
       this._bao.write(valueK / 16384);
     } else {
       this._bao.write((value & 0x7F) + 128);
       this._bao.write((valueK & 0x7F) + 128);
       this._bao.write((valueK / 128 & 0x7F) + 128);
       this._bao.write((valueK / 16384 & 0x7F) + 128);
       this._bao.write(valueK / 2097152);
     } 
   }
   
   public int bitlengh(int obj) {
     int length = 0;
     if (obj < 0) {
       BigInteger b = new BigInteger("18446744073709551615");
       while (BigInteger.valueOf(obj).and(b).shiftRight((length + 1) * 7).longValue() > 0L) {
         length++;
       }
       length++;
     }
     else if (obj <= 127) {
       length = 1;
     } else if (obj <= 16383) {
       length = 2;
     } else if (obj <= 2097151) {
       length = 3;
     } else if (obj <= 268435455) {
       length = 4;
     } else if (obj <= 34359738367L) {
       length = 5;
     } 
     
     return length;
   }
 
 
   
   protected void write4bit(int value) {
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
   
   protected void writeBit(long value) {
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
 
 
 
 
 
 
   
   protected void writeBit(int value1, int value2) {
     String str1 = Integer.toBinaryString(value1);
     String str2 = Integer.toBinaryString(value2);
     if (value1 <= 32767) str1 = "0" + str1; 
     String str3 = str2 + str1;
     writeBit(Long.valueOf(str3, 2).longValue());
   }
 
   
   protected void writePoint(int x, int y) {
     int pt = y << 16 & 0xFFFF0000;
     pt |= x & 0xFFFF;
     writeBit(pt);
   }
   
   protected void writeB(boolean b) {
     writeC(b ? 1 : 0);
   }
   
   protected void writeB(Object o) {
     writeC((o != null) ? 1 : 0);
   }
   
   protected int getBitSize(long value) {
     if (value < 0L) {
       String stringValue = Integer.toBinaryString((int)value);
       value = Long.valueOf(stringValue, 2).longValue();
     } 
     int size = 0;
     while (value >> (size + 1) * 7 > 0L) {
       size++;
     }
     size++;
     
     return size;
   }
   
   protected void write7B(long value) {
     int i = 0;
     BigInteger b = new BigInteger("18446744073709551615");
     
     while (BigInteger.valueOf(value).and(b).shiftRight((i + 1) * 7).longValue() > 0L) {
       this._bao.write(BigInteger.valueOf(value).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(128L)).or(BigInteger.valueOf(128L)).intValue());
     }
     this._bao.write(BigInteger.valueOf(value).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(128L)).intValue());
   }
   
   public int size7B(int obj) {
     int length = 0;
     if (obj < 0) {
       BigInteger b = new BigInteger("18446744073709551615");
       while (BigInteger.valueOf(obj).and(b).shiftRight((length + 1) * 7).longValue() > 0L) {
         length++;
       }
       length++;
     }
     else if (obj <= 127) {
       length = 1;
     } else if (obj <= 16383) {
       length = 2;
     } else if (obj <= 2097151) {
       length = 3;
     } else if (obj <= 268435455) {
       length = 4;
     } else if (obj <= 34359738367L) {
       length = 5;
     } 
     
     return length;
   }
   
   protected void writeP(int value) {
     this._bao.write(value);
   }
   
   protected void writeF(double org) {
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
   
   protected void writeS(String text) {
     try {
       if (text != null) {
         this._bao.write(text.getBytes("UTF-8"));
       }
     } catch (Exception e) {
       e.printStackTrace();
     } 
     
     this._bao.write(0);
   }
   
   protected void writeSS(String text) {
     try {
       if (text != null) {
         byte[] test = text.getBytes("UTF-8");
         for (int i = 0; i < test.length; ) {
           if ((test[i] & 0xFF) >= 127) {
             
             this._bao.write(test[i + 1]);
             this._bao.write(test[i]);
             i += 2;
             continue;
           } 
           this._bao.write(test[i]);
           this._bao.write(0);
           i++;
         }
       
       } 
     } catch (Exception e) {
       e.printStackTrace();
     } 
     this._bao.write(0);
     this._bao.write(0);
   }
   
   protected void writeBytesWithLength(byte[] bytes) {
     if (bytes == null || bytes.length <= 0) { writeC(0); }
     else
     { if (bytes.length > 127) { writeBit(bytes.length); }
       else { writeC(bytes.length); }
        writeByte(bytes); }
   
   }
   
   protected void writeByte(byte[] text) {
     try {
       if (text != null) {
         this._bao.write(text);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } 
   }
   
   public int getLength() {
     return this._bao.size() + 2;
   }
   
   public byte[] getBytes() {
     if (this._byte == null)
       this._byte = this._bao.toByteArray(); 
     return this._byte;
   }
   
   protected void writeS2(String text) {
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
 
 
   
   public abstract byte[] getContent() throws IOException;
 
 
   
   public String getType() {
     return "";
   }
 
 
   
   public String toString() {
     String sTemp = getType().equals("") ? "" : ("[" + getKey() + "] " + getType());
     return sTemp;
   }
   
   public void writeBytes(byte[] data) throws Exception {
     if (data == null || data.length <= 0) {
       writeC(0);
     } else {
       writeBit(data.length);
       this._bao.write(data);
     } 
   }
   
   protected void write8x(long value, int size) {
     for (int i = 0; i < size; i++) {
       this._bao.write((byte)(int)(value & 0xFFL));
       this._bao.write((byte)(int)(value >> 8L & 0xFFL));
       this._bao.write((byte)(int)(value >> 16L & 0xFFL));
       this._bao.write((byte)(int)(value >> 24L & 0xFFL));
       this._bao.write((byte)(int)(value >> 32L & 0xFFL));
       this._bao.write((byte)(int)(value >> 40L & 0xFFL));
       this._bao.write((byte)(int)(value >> 48L & 0xFFL));
       this._bao.write((byte)(int)(value >> 56L & 0xFFL));
       this._bao.write((byte)(int)(value >> 64L & 0xFFL));
     } 
   }
   
   protected void write16(long value) {
     this._bao.write((byte)(int)(value & 0xFFL));
     this._bao.write((byte)(int)(value >> 8L & 0xFFL));
     this._bao.write((byte)(int)(value >> 16L & 0xFFL));
     this._bao.write((byte)(int)(value >> 24L & 0xFFL));
     this._bao.write((byte)(int)(value >> 32L & 0xFFL));
     this._bao.write((byte)(int)(value >> 40L & 0xFFL));
     this._bao.write((byte)(int)(value >> 48L & 0xFFL));
     this._bao.write((byte)(int)(value >> 56L & 0xFFL));
     this._bao.write((byte)(int)(value >> 64L & 0xFFL));
     this._bao.write((byte)(int)(value >> 72L & 0xFFL));
     this._bao.write((byte)(int)(value >> 80L & 0xFFL));
     this._bao.write((byte)(int)(value >> 88L & 0xFFL));
     this._bao.write((byte)(int)(value >> 96L & 0xFFL));
     this._bao.write((byte)(int)(value >> 104L & 0xFFL));
     this._bao.write((byte)(int)(value >> 112L & 0xFFL));
     this._bao.write((byte)(int)(value >> 120L & 0xFFL));
     this._bao.write((byte)(int)(value >> 128L & 0xFFL));
     this._bao.write((byte)(int)(value >> 136L & 0xFFL));
   }
   
   public void close() {
     if (this._byte != null)
       this._byte = null; 
     try {
       this._bao.close();
     } catch (Exception exception) {}
   }
 }


