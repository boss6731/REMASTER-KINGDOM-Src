package l1j.server.MJTemplate.Stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.MJBytesInputStream;

public class MJPresentationOutputStream extends OutputStream {

	// 創建新的 MJPresentationOutputStream 實例，使用默認容量
	public static MJPresentationOutputStream newInstance() {
		return new MJPresentationOutputStream(4096);
	}

	// 創建新的 MJPresentationOutputStream 實例，使用指定容量
	public static MJPresentationOutputStream newInstance(int capacity) {
		return new MJPresentationOutputStream(capacity);
	}

	// 私有成員變量
	private byte[] _buf; // 緩衝區
	private int _idx; // 當前緩衝區索引
	private int _capacity; // 緩衝區的容量
	private boolean _isClosed; // 流是否已關閉
	private boolean _isShared; // 流的數據是否可共享

	// 保護構造函數，使用指定容量初始化
	protected MJPresentationOutputStream(int capacity) {
		_isShared = false;
		_isClosed = false;
		_capacity = capacity;
		_buf = new byte[_capacity];
	}

	/** 重新分配流的容量 **/
	private void realloc(int capacity) {
		_capacity = capacity;
		byte[] tmp = new byte[_capacity];
		System.arraycopy(_buf, 0, tmp, 0, _idx);
		_buf = tmp;
		_isShared = false;
	}

	/** 寫入數據到流 **/
	@override
	public void write(int i) throws IOException {
		if (_isClosed)
			throw new IOException("流已關閉...");

		if (_idx >= _capacity)
			realloc(_capacity * 2 + 1);

		_buf[_idx++] = (byte) (i & 0xff);
	}


	public class MJPresentationOutputStream extends OutputStream{

		// 創建新的 MJPresentationOutputStream 實例，使用默認容量
		public static MJPresentationOutputStream newInstance() {
			return new MJPresentationOutputStream(4096);
		}

		// 創建新的 MJPresentationOutputStream 實例，使用指定容量
		public static MJPresentationOutputStream newInstance(int capacity) {
			return new MJPresentationOutputStream(capacity);
		}

		// 私有成員變量
		private byte[] _buf; // 緩衝區
		private int _idx; // 當前緩衝區索引
		private int _capacity; // 緩衝區的容量
		private boolean _isClosed; // 流是否已關閉
		private boolean _isShared; // 流的數據是否可共享

		// 保護構造函數，使用指定容量初始化
		protected MJPresentationOutputStream(int capacity) {
			_isShared = false;
			_isClosed = false;
			_capacity = capacity;
			_buf = new byte[_capacity];
		}

		/** 重新分配流的容量 **/
		private void realloc(int capacity) {
			_capacity = capacity;
			byte[] tmp = new byte[_capacity];
			System.arraycopy(_buf, 0, tmp, 0, _idx);
			_buf = tmp;
			_isShared = false;
		}

		/** 寫入一個字節到流 **/
		@override
		public void write(int i) throws IOException {
			if (_isClosed) {
				throw new IOException("流已關閉...");
			}

			if (_idx >= _capacity) {
				realloc(_capacity * 2 + 1);
			}

			_buf[_idx++] = (byte) (i & 0xff);
		}

		/** 寫入數據到流 **/
		public void write(byte[] data, int offset, int length) throws IOException {
			if (data == null) {
				throw new NullPointerException();
			}

			if (offset < 0 || offset + length > data.length || length < 0) {
				throw new IndexOutOfBoundsException();
			}

			if (_isClosed) {
				throw new IOException("流已關閉...");
			}

			int capacity = _capacity;
			while (_idx + length > capacity) {
				capacity = capacity * 2 + 1;
			}
			if (capacity > _capacity) {
				realloc(capacity);
			}

			System.arraycopy(data, offset, _buf, _idx, length);
			_idx += length;
		}

		/** 寫入完整字節數組到流 **/
		public void writeBytes(byte[] data) throws IOException {
			if (data == null || data.length <= 0) {
				write(0);
			} else {
				writeBit(data.length);
				write(data);
			}
		}

		/** 寫入短整型(2字節)數據到流 **/
		public void writeH(int i) throws IOException {
			write(i & 0xFF);
			write(i >> 8 & 0xFF);
		}

		/** 寫入整型(4字節)數據到流 **/
		public void writeD(int i) throws IOException {
			write(i & 0xFF);
			write(i >> 8 & 0xFF);
			write(i >> 16 & 0xFF);
			write(i >> 24 & 0xFF);
		}

// 省略可能的補充方法、關閉流等


	
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
		writeS(text, "MS949");
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
				byte[] name = text.getBytes("MS949");
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
		writeSForMultiBytes(text, "MS949");
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

		public class MJPresentationOutputStream extends OutputStream{

			// 創建新的 MJPresentationOutputStream 實例，使用默認容量
			public static MJPresentationOutputStream newInstance() {
				return new MJPresentationOutputStream(4096);
			}

			// 創建新的 MJPresentationOutputStream 實例，使用指定容量
			public static MJPresentationOutputStream newInstance(int capacity) {
				return new MJPresentationOutputStream(capacity);
			}

			// 私有成員變量
			private byte[] _buf; // 緩衝區
			private int _idx; // 當前緩衝區索引
			private int _capacity; // 緩衝區的容量
			private boolean _isClosed; // 流是否已關閉
			private boolean _isShared; // 流的數據是否可共享

			// 保護構造函數，使用指定容量初始化
			protected MJPresentationOutputStream(int capacity) {
				_isShared = false;
				_isClosed = false;
				_capacity = capacity;
				_buf = new byte[_capacity];
			}

			/** 重新分配流的容量 **/
			private void realloc(int capacity) {
				_capacity = capacity;
				byte[] tmp = new byte[_capacity];
				System.arraycopy(_buf, 0, tmp, 0, _idx);
				_buf = tmp;
				_isShared = false;
			}

			/** 寫入一個字節到流 **/
			@override
			public void write(int i) throws IOException {
				if (_isClosed) {
					throw new IOException("流已關閉...");
				}

				if (_idx >= _capacity) {
					realloc(_capacity * 2 + 1);
				}

				_buf[_idx++] = (byte) (i & 0xff);
			}

			/** 寫入數據到流 **/
			public void write(byte[] data, int offset, int length) throws IOException {
				if (data == null) {
					throw new NullPointerException();
				}

				if (offset < 0 || offset + length > data.length || length < 0) {
					throw new IndexOutOfBoundsException();
				}

				if (_isClosed) {
					throw new IOException("流已關閉...");
				}

				int capacity = _capacity;
				while (_idx + length > capacity) {
					capacity = capacity * 2 + 1;
				}
				if (capacity > _capacity) {
					realloc(capacity);
				}

				System.arraycopy(data, offset, _buf, _idx, length);
				_idx += length;
			}
			/** 寫入完整字節數組到流 **/
			public void writeBytes(byte[] data)
					throws IOException
			{if (data == null || data.length <= 0) {
				write(0);
			} else {
				writeBit(data.length);
				write(data);}}
			/** 寫入短整型(2字節)數據到流 **/
			public void writeH(int i)
					throws IOException
			{write(i & 0xFF);write(i >> 8 & 0xFF);}
			/** 寫入整型(4字節)數據到流 **/
			public void writeD(int i)
					throws IOException
			{write(i & 0xFF);write(i >> 8 & 0xFF);write(i >> 16 & 0xFF);write(i >> 24 & 0xFF);}
			/** 寫入當前流到另一個 OutputStream **/
			public void writeTo(OutputStream out)
					throws IOException
			{out.write(_buf, 0, _idx);}
			/** 寫入一個布爾值到流 **/
			public void writeB(boolean b)
					throws IOException
			{write(b ? 0x01 : 0x00);}
			/** 根據對象是否為 null 寫入布爾值到流**/
				public void writeB(Object o)
						throws IOException
				{write(o != null ? 0x01 : 0x00);}
			/** 寫入一個點（x, y）到流 **/
				public void writePoint(int x, int y)
						throws Exception {
					int pt = (y << 16) & 0xffff0000;
					pt |= (x & 0x0000ffff);
					writeBit(pt);
				}/**將流轉換為 InputStream **/
				public InputStream toInputStream() {
					_isShared = true;
					return new MJBytesInputStream(_buf, 0, _idx);
				}
				/** 重置流 **/
			 public void reset() throws IOException {
			 if (_isClosed) {
			 _isClosed = false;
			 }
			 if (_isShared) {
			 _buf = new byte[_capacity];
			 _isShared = false;
			 }
			 _idx = 0;


	/** 關閉流 **/
				@override
				public void close() {
					_isClosed = true;
				}

	// 假設 writeBit 方法和 MJBytesInputStream 類在其他地方定義
			}
	public void dispose(){
		_isClosed 	= true;
		_isShared	= false;
		_buf 		= null;
	}
	
	/** 以數組形式傳回流的內容 **/
	public byte[] toArray(){
		byte[] result = new byte[_idx];
		System.arraycopy(_buf, 0, result, 0, _idx);
		return result;
	}
	
	public boolean isClose(){
		return _isClosed;
	}
	
	public MJPresentationOutputStream write_weapon_info(L1Item item, int weight) throws IOException{
		write(0x01);
		write(item.getDmgSmall());
		write(item.getDmgLarge());
		write(item.getMaterial());
		writeD(weight);
		return this;
	}
	
	public MJPresentationOutputStream write_weapon_etc_info(L1Item item, int durability) throws IOException{
		if(item.isTwohandedWeapon())
			write(0x04);
		if(durability != 0){
			write(0x03);
			write(durability);
		}
		
		write(0x07);
		write((item.isUseRoyal() ? 1 : 0)
				| (item.isUseKnight() ? 2 : 0)
				| (item.isUseElf() ? 4 : 0)
				| (item.isUseMage() ? 8 : 0)
				| (item.isUseDarkelf() ? 16 : 0)
				| (item.isUseDragonKnight() ? 32 : 0)
				| (item.isUseBlackwizard() ? 64 : 0)
				| (item.isUse전사() ? 128 : 0));
		
		write(130);
		if (!item.isTradable()) {
			writeD(6);
		} else {
			writeD(7);
		}
		
		write(132);
		writeD(3);
		
		if(item.get_canbedmg() == 0) {
			write(131);
			writeD(1);	// 비손상
		}
		
		if (isUndeadDmg(item)) {
			write(114);
			writeD(1);	// 언데드
		}
		
		return this;
	}
	
	public MJPresentationOutputStream write_weapon_add_damage(int enchant_level) throws IOException{
		write(0x02);
		write(enchant_level);
		return this;
	}
	
	public MJPresentationOutputStream write_weapon_add_damage(int small_damage, int large_damage) throws IOException{
		write(0x6B);
		write(small_damage);
		write(large_damage);
		return this;
	}
	
	public MJPresentationOutputStream writeAbilityPierce(int pierce) throws IOException{
		write(122);
		write(pierce);
		return this;
	}
	
	public MJPresentationOutputStream writeSpiritPierce(int pierce) throws IOException{
		write(123);
		write(pierce);
		return this;
	}
	
	public MJPresentationOutputStream writeDragonSpellPierce(int pierce) throws IOException{
		write(124);
		write(pierce);
		return this;
	}
	
	public MJPresentationOutputStream writeFearPierce(int pierce) throws IOException{
		write(120);
		write(pierce);
		return this;
	}
	
	public MJPresentationOutputStream writeAllPierce(int pierce) throws IOException{
		write(121);
		write(pierce);
		return this;
	}
	
	public MJPresentationOutputStream write_safeenchant(int Safeen) throws IOException{
		return write_text(String.format("\\aG[安全附魔：+%d]", Safeen));
	}

	public MJPresentationOutputStream write_short_damage(int dmg) throws IOException{
		write(0x2f);
		write(dmg);
		return this;
	}
	
	public MJPresentationOutputStream write_short_hit(int hit) throws IOException{
		write(0x30);
		write(hit);
		return this;
	}
	
	public MJPresentationOutputStream write_short_critical(int critical) throws IOException{
		write(0x64);
		write(critical);
		return this;
	}
	
	public MJPresentationOutputStream write_long_damage(int dmg) throws IOException{
		write(0x23);
		write(dmg);
		return this;
	}
	
	public MJPresentationOutputStream write_long_hit(int hit) throws IOException{
		write(0x18);
		write(hit);
		return this;
	}
	
	public MJPresentationOutputStream write_long_critical(int critical) throws IOException{
		write(0x63);
		write(critical);
		return this;
	}
	
	public MJPresentationOutputStream write_magic_hit(int hit) throws IOException{
		write(0x28);
		write(hit);
		return this;
	}
	
	public MJPresentationOutputStream write_magic_critical(int critical) throws IOException{
		write(0x32);
		writeH(critical);
		return this;
	}
	
	public MJPresentationOutputStream write_text(String s) throws IOException{
		write(0x27);
		writeS(s);
		return this;
	}
	
	public MJPresentationOutputStream write_max_hp(int max_hp) throws IOException{
		write(0x0E);
		writeH(max_hp);
		return this;
	}
	
	public MJPresentationOutputStream write_drain_hp() throws IOException{
		write(0x22);
		return this;
	}
	
	public MJPresentationOutputStream write_str(int str) throws IOException{
		write(0x08);
		write(str);
		return this;
	}
	
	public MJPresentationOutputStream write_dex(int dex) throws IOException{
		write(0x09);
		write(dex);
		return this;
	}
	
	public MJPresentationOutputStream write_con(int con) throws IOException{
		write(0x0A);
		write(con);
		return this;
	}
	
	public MJPresentationOutputStream write_wis(int wis) throws IOException{
		write(0x0B);
		write(wis);
		return this;
	}
	
	public MJPresentationOutputStream write_int(int intel) throws IOException{
		write(0x0C);
		write(intel);
		return this;
	}
	
	public MJPresentationOutputStream write_cha(int cha) throws IOException{
		write(0x0D);
		write(cha);
		return this;
	}
	
	public MJPresentationOutputStream write_sp(int sp) throws IOException{
		write(0x11);
		write(sp);
		return this;
	}
	
	public MJPresentationOutputStream write_magic_name(L1ItemInstance item, String magic_name) throws IOException{
		int weaponlevel = item.get_item_level();
		if (weaponlevel == 0) {
		write(0x4A);
		writeS(magic_name);
		}
		return this;
	}
	
	public MJPresentationOutputStream write_damage_reduction(int reduction) throws IOException{
		write(0x3F);
		write(reduction);
		return this;
	}
	
	public MJPresentationOutputStream write_ignore_reduction(int ignore) throws IOException{
		return write_text(String.format("忽略傷害減免+%d", ignore));
	}
	
	public MJPresentationOutputStream write_addsub_hit(int hit) throws IOException{
		write(0x05);
		write(hit);
		return this;
	}
	
	public MJPresentationOutputStream write_addsub_damage(int dmg) throws IOException{
		write(0x06);
		write(dmg);
		return this;
	}
	
	public MJPresentationOutputStream write_addsub_short_damage(int dmg) throws IOException{
		write(47);
		write(dmg);
		return this;
	}
	
	public MJPresentationOutputStream write_addsub_long_damage(int dmg) throws IOException{
		write(35);
		write(dmg);
		return this;
	}
	
	public MJPresentationOutputStream write_elemental_enchant_info(int elemental_enchant_level) throws IOException {
		if(elemental_enchant_level > 0) {
			int val = ((L1ItemInstance.pureAttrEnchantLevel(elemental_enchant_level) << 4) & 0xf0) |
			(L1ItemInstance.attrEnchantToElementalType(elemental_enchant_level) & 0x0f);
			write(0x6E);
			write(val);
		}
		return this;
	}

			public MJPresentationOutputStream write_blessed_options(L1ItemInstance item) throws IOException{
				int bless = item.get_bless_level();
				if(bless > 0){
					int type = item.getItem().getType();
					if (type == 7 || type == 16 || type == 17) {
						write_text("\fI祝福的 SP:\aA +" + bless); // 翻譯「축복받은 SP」為「祝福的 SP」
					} else {
						write_text("\fI祝福的追加傷害:\aA +" + bless); // 翻譯「축복받은 추타」為「祝福的追加傷害」
					}
				}
				return this;


				public MJPresentationOutputStream write_weapon_level_options(L1ItemInstance item) throws IOException{
					int weaponlevel = item.get_item_level();
					if(weaponlevel > 0) {
						int type = item.getItem().getType2();
						if (type == 1) {
							switch (weaponlevel) {
								case 1:
									write_text("\fI發動:\aA1階段魔法"); // 翻譯「발동」為「發動」，「1단계 마법」為「1階段魔法」
									break;
								case 2:
									write_text("\fI發動:\aA2階段魔法"); // 翻譯「2단계 마법」為「2階段魔法」
									break;
								case 3:
									write_text("\fI發動:\aA3階段魔法"); // 翻譯「3단계 마법」為「3階段魔法」
									break;
								case 4:
									write_text("\fI發動:\aA4階段魔法"); // 翻譯「4단계 마법」為「4階段魔法」
									break;
								default:
									break;
							}
						}
					}
					return this;
				}
	
	
	public boolean isUndeadDmg(L1Item item) {
		boolean result = false;
		
		if(item.getMaterial() == 14 || item.getMaterial() == 17 || item.getMaterial() == 22)
			result = true;
		
		return result;
	}
}
