package l1j.server.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MJBytesOutputStream extends OutputStream{

	private byte[]     _buf;        // 緩衝區
	private int        _idx;        // 當前緩衝區指向的索引
	private int        _capacity;   // 緩衝區的大小/擴展大小
	private boolean    _isClosed;   // 流是否已關閉
	private boolean    _isShared;   // 流數據是否可共享
	
	public MJBytesOutputStream(){
		this(4096);
	}
	
	public MJBytesOutputStream(int capacity){
		_isShared	= false;
		_isClosed	= false;
		_capacity 	= capacity;
		_buf		= new byte[_capacity];
	}

	/** 重新調整流的大小 **/
	private void realloc(int capacity){
		_capacity 	= capacity;
		byte[] tmp 	= new byte[_capacity];
		System.arraycopy(_buf, 0, tmp, 0, _idx);
		_buf 		= tmp;
		_isShared 	= false;
	}

	/** 寫入數據 **/
	@Override
	public void write(int i) throws IOException {
		if(_isClosed)
			throw new IOException("BytesOutputStream Closed...");
		
		if(_idx >= _capacity)
			realloc(_capacity*2+1);
		
		_buf[_idx++] = (byte)(i & 0xff);
	}

	/** 寫入資料 **/
	public void write(byte[] data, int offset, int length) throws IOException{
		if(data == null)
			throw new NullPointerException();
		
		if(offset < 0 || offset + length > data.length || length < 0)
			throw new IndexOutOfBoundsException();
		
		if(_isClosed)
			throw new IOException("BytesOutputStream Closed...");
		
		int capacity = _capacity;
		while(_idx + length > capacity)
			capacity = capacity*2+1;
		if(capacity > _capacity)
			realloc(capacity);
		
		System.arraycopy(data, offset, _buf, _idx, length);
		_idx += length;
	}
	
	
	public void writeBytes(byte[] data) throws IOException{
		if(data == null || data.length <= 0)
			write(0);
		else{
			writeBit(data.length);
			write(data);
		}
	}

	/** 寫入 short 型 (2 字節) 數據 **/
	public void writeH(int i) throws IOException{
		write(i 		& 0xFF);
	    write(i >> 8 	& 0xFF);
	}

	/** 寫入 int 型 (4 字節) 數據 **/
	public void writeD(int i) throws IOException{
		write(i 		& 0xFF);
	    write(i >> 8 	& 0xFF);
	    write(i >> 16 	& 0xFF);
	    write(i >> 24 	& 0xFF);
	}
	
	public void writeBit(long value) throws IOException
	{
		if (value < 0L) {
			String str = Integer.toBinaryString((int)value);
			value = Long.valueOf(str, 2).longValue();
		}
		int i = 0;
		while (value >> 7 * (i + 1) > 0L)
			write((int)((value >> 7 * i++) % 128L | 0x80));
		write((int)((value >> 7 * i) % 128L));
	}
	
	public void writeS(String text) throws IOException{
		writeS(text, "UTF-8");
	}
	
	public void writeS(String text, String encoding) throws IOException{
		if(text != null){
			byte[] b = text.getBytes(encoding);
			write(b, 0, b.length);
		}
		write(0);
	}
	
	public void writeS2(String text) {
		try {
			if (text != null && !text.isEmpty()) {
				byte[] name = text.getBytes("UTF-8");
				write(name.length & 0xff);
				if (name.length > 0) {
					write(name);
				}
			} else {
				write(0 & 0xff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeSForMultiBytes(String text) throws IOException{
		writeSForMultiBytes(text, "UTF-8");
	}
	
	public void writeSForMultiBytes(String text, String encoding) throws IOException{
		if(text != null){
			byte[] b = text.getBytes(encoding);
			int i = 0;
			while(i < b.length){
				if((b[i] & 0xff) >= 0x7f){
					write(b[i + 1]);
					write(b[i]);
					i += 2;
				}else{
					write(b[i]);
					write(0);
					i += 1;
				}
			}
		}
		write(0);
		write(0);
	}

	/** 寫入新的 outputStream **/
	public void writeTo(OutputStream out) throws IOException{
		out.write(_buf, 0, _idx);
	}
	
	public void writeB(boolean b) throws IOException{
		write(b ? 0x01 : 0x00);
	}
	
	public void writeB(Object o) throws IOException{
		write(o != null ? 0x01 : 0x00);
	}
	
	public void writePoint(int x, int y) throws Exception{
		int pt 	= 	(y << 16) 	& 0xffff0000;
		pt 		|= 	(x 			& 0x0000ffff);
		writeBit(pt);
	}

	/** 生成 InputStream **/
	public InputStream toInputStream(){
		_isShared = true;
		return new l1j.server.server.utils.MJBytesInputStream(_buf, 0, _idx);
	}

	/** 初始化 **/
	public void reset() throws IOException{
		if(_isClosed)
			_isClosed = false;
		
		if(_isShared){
			_buf = new byte[_capacity];
			_isShared = false;
		}
		
		_idx = 0;
	}

	/** 關閉流 **/
	public void close(){
		_isClosed = true;
	}
	
	public void dispose(){
		_isClosed 	= true;
		_isShared	= false;
		_buf 		= null;
	}

	/** 將流的內容返回為數組 **/
	public byte[] toArray(){
		byte[] result = new byte[_idx];
		System.arraycopy(_buf, 0, result, 0, _idx);
		return result;
	}
	
	public boolean isClose(){
		return _isClosed;
	}
}
