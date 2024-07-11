 package l1j.server.server.utils;
 
 import java.io.IOException;
 import java.io.InputStream;
 
 public class MJBytesInputStream extends InputStream {
   private byte[] _buf;
   private int _idx;
   private int _limit;
   private int _mark;
   private boolean _isClosed;
   
   public MJBytesInputStream() {
     this._buf = null;
     this._idx = -1;
     this._limit = -1;
     this._mark = -1;
     this._isClosed = true;
   }
   
   public MJBytesInputStream(byte[] data) {
     this(data, 0, data.length);
   }
   
   public MJBytesInputStream(byte[] data, int offset, int length) {
     if (data == null) {
       throw new NullPointerException();
     }
     if (offset < 0 || offset + length > data.length || length < 0) {
       throw new IndexOutOfBoundsException();
     }
     this._buf = data;
     this._idx = offset;
     this._limit = offset + length;
     this._mark = offset;
     this._isClosed = false;
   }
   
   public void setBuff(byte[] data, int offset, int length) throws IOException {
     if (data == null) {
       throw new NullPointerException();
     }
     if (offset < 0 || offset + length > data.length || length < 0) {
       throw new IndexOutOfBoundsException();
     }
     this._buf = data;
     this._idx = offset;
     this._limit = offset + length;
     this._mark = offset;
     this._isClosed = false;
   }
   
   public int readD() throws IOException {
     if (this._isClosed) {
       throw new IOException("BytesInputStream Closed...");
     }
     if (this._idx + 3 >= this._limit) {
       return -1;
     }
     int result = this._buf[this._idx++] & 0xFF;
     result |= this._buf[this._idx++] << 8 & 0xFF00;
     result |= this._buf[this._idx++] << 16 & 0xFF0000;
     result |= this._buf[this._idx++] << 24 & 0xFF000000;
     return result;
   }
   
   public int readH() throws IOException {
     if (this._isClosed) {
       throw new IOException("BytesInputStream Closed...");
     }
     if (this._idx + 1 >= this._limit) {
       return -1;
     }
     int result = this._buf[this._idx++] & 0xFF;
     result |= this._buf[this._idx++] << 8 & 0xFF00;
     return result;
   }
 
   
   public int read() throws IOException {
     if (this._isClosed) {
       throw new IOException("BytesInputStream Closed...");
     }
     if (this._idx >= this._limit)
       return -1; 
     return this._buf[this._idx++] & 0xFF;
   }
   
   public int read(byte[] data, int offset, int length) throws IOException {
     if (data == null)
       throw new NullPointerException(); 
     if (offset < 0 || offset + length > data.length || length < 0)
       throw new IndexOutOfBoundsException(); 
     if (this._isClosed)
       throw new IOException("BytesInputStream Closed..."); 
     if (this._idx >= this._limit)
       return -1; 
     if (length > this._limit - this._idx)
       length = this._limit - this._idx; 
     System.arraycopy(this._buf, this._idx, data, offset, length);
     this._idx += length;
     return length;
   }
   
   public String readS() throws IOException {
     return readS("UTF-8");
   }
   
   public String readS(String encoding) throws IOException {
     if (this._isClosed) {
       throw new IOException("BytesInputStream Closed...");
     }
     if (this._idx >= this._limit) {
       return null;
     }
     String s = null;
     s = new String(this._buf, this._idx, this._buf.length - this._idx, encoding);
     s = s.substring(0, s.indexOf(false));
     this._idx += MJHexHelper.getTxtToBytesLength(s);
     return s;
   }
   
   public String readS(int length) throws IOException {
     return readS("UTF-8", length);
   }
   
   public String readS(String encoding, int length) throws IOException {
     if (this._isClosed) {
       throw new IOException("BytesInputStream Closed...");
     }
     if (this._idx >= this._limit) {
       return null;
     }
     String s = null;
     s = new String(this._buf, this._idx, length, encoding);
     this._idx += MJHexHelper.getTxtToBytesLength(s);
     return s;
   }
   
   public String readSForMultiBytes() throws IOException {
     byte d1 = 0;
     byte d2 = 0;
     byte[] tmpB = new byte[2];
     StringBuilder sb = new StringBuilder();
     
     if (this._isClosed) {
       throw new IOException("BytesInputStream Closed...");
     }
     
     while (this._idx + 2 < this._limit) {
 
       
       d1 = this._buf[this._idx++];
       d2 = this._buf[this._idx++];
       if (d1 == 0 && d2 == 0) {
         break;
       }
       if (d1 >= Byte.MAX_VALUE || d2 >= Byte.MAX_VALUE) {
         
         tmpB[0] = d2;
         tmpB[1] = d1;
         sb.append(new String(tmpB, 0, 2, "UTF-8"));
         continue;
       } 
       tmpB[0] = d1;
       sb.append(new String(tmpB, 0, 1, "UTF-8"));
     } 
     
     return sb.toString();
   }
   
   public long skip(long amount) throws IOException {
     if (this._isClosed)
       throw new IOException("BytesInputStream Closed..."); 
     if (amount <= 0L) {
       return 0L;
     }
     if (amount > (this._limit - this._idx))
       amount = (this._limit - this._idx); 
     this._idx += (int)amount;
     return amount;
   }
   
   public int available() throws IOException {
     if (this._isClosed)
       throw new IOException("BytesInputStream Closed..."); 
     return this._limit - this._idx;
   }
   
   public void close() {
     this._isClosed = true;
   }
   
   public void mark() {
     this._mark = this._idx;
   }
   
   public void resetIdx() throws IOException {
     if (this._isClosed) {
       throw new IOException("BytesInputStream Closed...");
     }
     this._idx = this._mark;
   }
   
   public void reset() {
     this._buf = null;
     this._idx = -1;
     this._limit = -1;
     this._mark = -1;
     this._isClosed = true;
   }
   
   public boolean markSupported() {
     return true;
   }
 }


