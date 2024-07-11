 package l1j.server.server.utils;
 
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.OutputStream;
 
 public class MJBytesOutputStream
   extends OutputStream {
   private byte[] _buf;
   private int _idx;
   private int _capacity;
   private boolean _isClosed;
   private boolean _isShared;
   
   public MJBytesOutputStream() {
     this(4096);
   }
   
   public MJBytesOutputStream(int capacity) {
     this._isShared = false;
     this._isClosed = false;
     this._capacity = capacity;
     this._buf = new byte[this._capacity];
   }
 
   
   private void realloc(int capacity) {
     this._capacity = capacity;
     byte[] tmp = new byte[this._capacity];
     System.arraycopy(this._buf, 0, tmp, 0, this._idx);
     this._buf = tmp;
     this._isShared = false;
   }
 
 
   
   public void write(int i) throws IOException {
     if (this._isClosed) {
       throw new IOException("BytesOutputStream Closed...");
     }
     if (this._idx >= this._capacity) {
       realloc(this._capacity * 2 + 1);
     }
     this._buf[this._idx++] = (byte)(i & 0xFF);
   }
 
   
   public void write(byte[] data, int offset, int length) throws IOException {
     if (data == null) {
       throw new NullPointerException();
     }
     if (offset < 0 || offset + length > data.length || length < 0) {
       throw new IndexOutOfBoundsException();
     }
     if (this._isClosed) {
       throw new IOException("BytesOutputStream Closed...");
     }
     int capacity = this._capacity;
     while (this._idx + length > capacity)
       capacity = capacity * 2 + 1; 
     if (capacity > this._capacity) {
       realloc(capacity);
     }
     System.arraycopy(data, offset, this._buf, this._idx, length);
     this._idx += length;
   }
 
   
   public void writeBytes(byte[] data) throws IOException {
     if (data == null || data.length <= 0) {
       write(0);
     } else {
       writeBit(data.length);
       write(data);
     } 
   }
 
   
   public void writeH(int i) throws IOException {
     write(i & 0xFF);
     write(i >> 8 & 0xFF);
   }
 
   
   public void writeD(int i) throws IOException {
     write(i & 0xFF);
     write(i >> 8 & 0xFF);
     write(i >> 16 & 0xFF);
     write(i >> 24 & 0xFF);
   }
 
   
   public void writeBit(long value) throws IOException {
     if (value < 0L) {
       String str = Integer.toBinaryString((int)value);
       value = Long.valueOf(str, 2).longValue();
     } 
     int i = 0;
     while (value >> 7 * (i + 1) > 0L)
       write((int)((value >> 7 * i++) % 128L | 0x80L)); 
     write((int)((value >> 7 * i) % 128L));
   }
   
   public void writeS(String text) throws IOException {
     writeS(text, "UTF-8");
   }
   
   public void writeS(String text, String encoding) throws IOException {
     if (text != null) {
       byte[] b = text.getBytes(encoding);
       write(b, 0, b.length);
     } 
     write(0);
   }
   
   public void writeS2(String text) {
     try {
       if (text != null && !text.isEmpty()) {
         byte[] name = text.getBytes("UTF-8");
         write(name.length & 0xFF);
         if (name.length > 0) {
           write(name);
         }
       } else {
         write(0);
       } 
     } catch (Exception e) {
       e.printStackTrace();
     } 
   }
   
   public void writeSForMultiBytes(String text) throws IOException {
     writeSForMultiBytes(text, "UTF-8");
   }
   
   public void writeSForMultiBytes(String text, String encoding) throws IOException {
     if (text != null) {
       byte[] b = text.getBytes(encoding);
       int i = 0;
       while (i < b.length) {
         if ((b[i] & 0xFF) >= 127) {
           write(b[i + 1]);
           write(b[i]);
           i += 2; continue;
         } 
         write(b[i]);
         write(0);
         i++;
       } 
     } 
     
     write(0);
     write(0);
   }
 
   
   public void writeTo(OutputStream out) throws IOException {
     out.write(this._buf, 0, this._idx);
   }
   
   public void writeB(boolean b) throws IOException {
     write(b ? 1 : 0);
   }
   
   public void writeB(Object o) throws IOException {
     write((o != null) ? 1 : 0);
   }
   
   public void writePoint(int x, int y) throws Exception {
     int pt = y << 16 & 0xFFFF0000;
     pt |= x & 0xFFFF;
     writeBit(pt);
   }
 
   
   public InputStream toInputStream() {
     this._isShared = true;
     return new MJBytesInputStream(this._buf, 0, this._idx);
   }
 
   
   public void reset() throws IOException {
     if (this._isClosed) {
       this._isClosed = false;
     }
     if (this._isShared) {
       this._buf = new byte[this._capacity];
       this._isShared = false;
     } 
     
     this._idx = 0;
   }
 
   
   public void close() {
     this._isClosed = true;
   }
   
   public void dispose() {
     this._isClosed = true;
     this._isShared = false;
     this._buf = null;
   }
 
   
   public byte[] toArray() {
     byte[] result = new byte[this._idx];
     System.arraycopy(this._buf, 0, result, 0, this._idx);
     return result;
   }
   
   public boolean isClose() {
     return this._isClosed;
   }
 }


