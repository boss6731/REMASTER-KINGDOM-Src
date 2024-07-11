 package l1j.server.server.utils;

 import java.io.IOException;
 import java.io.InputStream;

 public class BinaryInputStream extends InputStream {
   InputStream _in;

   public BinaryInputStream(InputStream in) {
     this._in = in;
   }


   public long skip(long n) throws IOException {
     return this._in.skip(n);
   }


   public int available() throws IOException {
     return this._in.available();
   }


   public void close() throws IOException {
     this._in.close();
   }


   public int read() throws IOException {
     return this._in.read();
   }

   public int readByte() throws IOException {
     return this._in.read();
   }

   public int readShort() throws IOException {
     return this._in.read() | this._in.read() << 8 & 0xFF00;
   }

   public int readInt() throws IOException {
     return readShort() | readShort() << 16 & 0xFFFF0000;
   }
 }


