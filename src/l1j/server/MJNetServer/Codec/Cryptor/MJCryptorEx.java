package l1j.server.MJNetServer.Codec.Cryptor;

import io.netty.buffer.ByteBuf;
import l1j.server.server.utils.MJCommons;

public class MJCryptorEx extends MJCryptor{

	private byte[] _encode_key;
	public MJCryptorEx(byte[] encode_key){
		super();
		_encode_key = encode_key;
	}
	@Override
	public byte[] decrypt(byte[] data, int size){
		MJCommons.encode_xor(data, _encode_key, 0, size);
		return super.decrypt(data, size);

		// 連接器附加編碼（加密）測試
		/*
		System.out.println(MJHexHelper.toString(data, data.length));
		MJCommons.encode_xor(data, _encode_key, 0, size);
		System.out.println(MJHexHelper.toString(data, data.length));
		data = super.decrypt(data, size);
		System.out.println(MJHexHelper.toString(data, data.length));
		return data;
		*/
	}
	
	@Override
	public void encrypt(byte[] data, ByteBuf buf){
		int size = data.length;
		int key_size = _encode_key.length;
		int k = 0, j, m, o;

		buf.writeShortLE(size + 2);
		for(int i=0; i<size; ++i){
			m = i + 1;
			o = m / 256;
			m -= o * 256;

			j = m & 0xFF;
			k += _clntHash[m];
			k &= 255;

			byte tk = _clntHash[j];
			_clntHash[j] = _clntHash[k];
			_clntHash[k] = tk;

			int b3 = _clntHash[j];
			int b4 = _clntHash[k];

			b3 += b4;
			b3 &= 255;
			
			byte b = (byte)((data[i] ^ _clntHash[b3]) & 0xff);
			b ^= _encode_key[i % key_size];
			buf.writeByte(b);
		}
	}
}
