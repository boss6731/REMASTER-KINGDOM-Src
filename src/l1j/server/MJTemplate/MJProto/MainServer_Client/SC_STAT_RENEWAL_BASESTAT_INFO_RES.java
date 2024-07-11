package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.IntRange;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_STAT_RENEWAL_BASESTAT_INFO_RES implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send_bonus_stat(L1PcInstance pc, int str, int intel, int wis, int dex, int con, int cha) {
		SC_STAT_RENEWAL_BASESTAT_INFO_RES noti = SC_STAT_RENEWAL_BASESTAT_INFO_RES.newInstance();
		noti.set_strinfo(str_info(str));
		noti.set_intinfo(int_info(intel));
		noti.set_wisinfo(wis_info(wis));
		noti.set_dexinfo(dex_info(dex));
		noti.set_coninfo(con_info(con));
		noti.set_chainfo(cha_info(cha));
		pc.sendPackets(noti, MJEProtoMessages.SC_STAT_RENEWAL_BASESTAT_INFO_RES);
	}

	public static void send_bonus_stat(L1PcInstance pc, int value) {
		SC_STAT_RENEWAL_BASESTAT_INFO_RES noti = SC_STAT_RENEWAL_BASESTAT_INFO_RES.newInstance();
		noti.set_strinfo(str_info(value));
		noti.set_intinfo(int_info(value));
		noti.set_wisinfo(wis_info(value));
		noti.set_dexinfo(dex_info(value));
		noti.set_coninfo(con_info(value));
		noti.set_chainfo(cha_info(value));
		pc.sendPackets(noti, MJEProtoMessages.SC_STAT_RENEWAL_BASESTAT_INFO_RES);
	}

	public static STR_INFO str_info(int str) {
		int dmg = 0;
		int hit = 0;
		int critical = 0;
		if (IntRange.includes(str, 1, 24)) {
			str = 12;
			dmg = 0;
			hit = 0;
		}
		if (IntRange.includes(str, 25, 34)) {
			str = 25;
			dmg = 1;
			hit = 1;
		}
		if (IntRange.includes(str, 35, 44)) {
			str = 35;
			dmg = 1;
			hit = 1;
		}
		if (IntRange.includes(str, 45, 54)) {
			str = 45;
			dmg = 3;
			hit = 3;
			critical = 1;
		}
		if (IntRange.includes(str, 55, 59)) {
			str = 55;
			dmg = 5;
			hit = 5;
			critical = 2;
		}
		if (str >= 60) {
			str = 60;
			dmg = 5;
			hit = 5;
			critical = 2;
		}
		STR_INFO str_info = STR_INFO.newInstance();
		str_info.set_basestr(str);
		str_info.set_damagebonus(dmg);
		str_info.set_hitbonus(hit);
		str_info.set_cribonus(critical);
		return str_info;
	}

	public static INT_INFO int_info(int intel) {
		int dmg = 0;
		int hit = 0;
		int critical = 0;
		if (IntRange.includes(intel, 1, 24)) {
			intel = 12;
			dmg = 0;
			hit = 0;
		}
		if (IntRange.includes(intel, 25, 34)) {
			intel = 25;
			dmg = 1;
			hit = 1;
		}
		if (IntRange.includes(intel, 35, 44)) {
			intel = 35;
			dmg = 1;
			hit = 1;
		}
		if (IntRange.includes(intel, 45, 54)) {
			intel = 45;
			dmg = 3;
			hit = 3;
			critical = 1;
		}
		if (IntRange.includes(intel, 55, 59)) {
			intel = 55;
			dmg = 5;
			hit = 5;
			critical = 2;
		}
		if (intel >= 60) {
			intel = 60;
			dmg = 5;
			hit = 5;
			critical = 2;
		}
		INT_INFO int_info = INT_INFO.newInstance();
		int_info.set_baseint(intel);
		int_info.set_damagebonus(dmg);
		int_info.set_hitbonus(hit);
		int_info.set_cribonus(critical);
		return int_info;
	}

	public static WIS_INFO wis_info(int wis) {
		int mpregen = 0;
		int mp_potion_bonus = 0;
		int add_mp = 0;
		if (IntRange.includes(wis, 1, 24)) {
			wis = 12;
			mpregen = 0;
			mp_potion_bonus = 0;
			add_mp = 0;
		}
		if (IntRange.includes(wis, 25, 34)) {
			wis = 25;
			mpregen = 1;
			mp_potion_bonus = 1;
			add_mp = 50;
		}
		if (IntRange.includes(wis, 35, 44)) {
			wis = 35;
			mpregen = 1;
			mp_potion_bonus = 1;
			add_mp = 100;
		}
		if (IntRange.includes(wis, 45, 54)) {
			wis = 45;
			mpregen = 3;
			mp_potion_bonus = 3;
			add_mp = 150;
		}
		if (IntRange.includes(wis, 55, 59)) {
			wis = 55;
			mpregen = 5;
			mp_potion_bonus = 4;
			add_mp = 200;
		}
		if (wis >= 60) {
			wis = 60;
			mpregen = 5;
			mp_potion_bonus = 4;
			add_mp = 200;
		}
		WIS_INFO wis_info = WIS_INFO.newInstance();
		wis_info.set_basewis(wis);
		wis_info.set_mpregen(mpregen);
		wis_info.set_mpincpotion(mp_potion_bonus);
		wis_info.set_addmpinc(add_mp);
		return wis_info;
	}

	public static DEX_INFO dex_info(int dex) {
		int dmg = 0;
		int hit = 0;
		int critical = 0;
		if (IntRange.includes(dex, 1, 24)) {
			dex = 12;
			dmg = 0;
			hit = 0;
		}
		if (IntRange.includes(dex, 25, 34)) {
			dex = 25;
			dmg = 1;
			hit = 1;
		}
		if (IntRange.includes(dex, 35, 44)) {
			dex = 35;
			dmg = 1;
			hit = 1;
		}
		if (IntRange.includes(dex, 45, 54)) {
			dex = 45;
			dmg = 3;
			hit = 3;
			critical = 1;
		}
		if (IntRange.includes(dex, 54, 59)) {
			dex = 55;
			dmg = 5;
			hit = 5;
			critical = 2;
		}
		if (dex >= 60) {
			dex = 60;
			dmg = 5;
			hit = 5;
			critical = 2;
		}
		DEX_INFO dex_info = DEX_INFO.newInstance();
		dex_info.set_basedex(dex);
		dex_info.set_damagebonus(dmg);
		dex_info.set_hitbonus(hit);
		dex_info.set_cribonus(critical);
		return dex_info;
	}

	public static CON_INFO con_info(int con) {
		int hpregen = 0;
		int hp_potion_bonus = 0;
		int add_hp = 0;
		if (IntRange.includes(con, 1, 24)) {
			con = 12;
			hpregen = 0;
			add_hp = 0;
		}
		if (IntRange.includes(con, 1, 34)) {
			con = 25;
			hpregen = 1;
			add_hp = 200;
			// add_hp = 50;
		}
		if (IntRange.includes(con, 35, 44)) {
			con = 35;
			hpregen = 1;
			hp_potion_bonus = 1;
			// add_hp = 100;
			add_hp = 400;
		}
		if (IntRange.includes(con, 45, 54)) {
			con = 45;
			hpregen = 3;
			hp_potion_bonus = 2;
			// add_hp = 150;
			add_hp = 600;
		}
		if (IntRange.includes(con, 55, 59)) {
			con = 55;
			hpregen = 5;
			hp_potion_bonus = 5;
			// add_hp = 150;
			add_hp = 800;
		}
		if (con >= 60) {
			con = 60;
			hpregen = 5;
			hp_potion_bonus = 5;
			// add_hp = 200;
			add_hp = 1000;
		}
		CON_INFO con_info = CON_INFO.newInstance();
		con_info.set_basecon(con);
		con_info.set_hpregen(hpregen);
		con_info.set_hpincpotion(hp_potion_bonus);
		con_info.set_addhpinc(add_hp);
		return con_info;
	}

	public static CHA_INFO cha_info(int cha) {
		int pierceall = 0;
		int decreasecooltime = 0;
		int decreaseCCduration = 0;
		if (IntRange.includes(cha, 1, 24)) {
			cha = 12;
			pierceall = 1;
			decreasecooltime = 1;
			decreaseCCduration = 1;
		}
		if (IntRange.includes(cha, 25, 34)) {
			cha = 25;
			decreasecooltime = 1;
			decreaseCCduration = 1;
		}
		if (IntRange.includes(cha, 35, 44)) {
			cha = 35;
			pierceall = 1;
		}
		if (IntRange.includes(cha, 45, 54)) {
			cha = 45;
			pierceall = 1;
		}
		if (IntRange.includes(cha, 55, 59)) {
			cha = 55;
			pierceall = 1;
		}
		if (cha == 60) {
			pierceall = 1;
			decreasecooltime = 1;
			decreaseCCduration = 1;
		}
		CHA_INFO cha_info = CHA_INFO.newInstance();
		cha_info.set_basecha(cha);
		cha_info.set_pierceall(pierceall);
		cha_info.set_decreasecooltime(decreasecooltime);
		cha_info.set_decreaseCCduration(decreaseCCduration);
		return cha_info;

	}

	public static SC_STAT_RENEWAL_BASESTAT_INFO_RES newInstance() {
		return new SC_STAT_RENEWAL_BASESTAT_INFO_RES();
	}

	private SC_STAT_RENEWAL_BASESTAT_INFO_RES.STR_INFO _strinfo;
	private SC_STAT_RENEWAL_BASESTAT_INFO_RES.INT_INFO _intinfo;
	private SC_STAT_RENEWAL_BASESTAT_INFO_RES.WIS_INFO _wisinfo;
	private SC_STAT_RENEWAL_BASESTAT_INFO_RES.DEX_INFO _dexinfo;
	private SC_STAT_RENEWAL_BASESTAT_INFO_RES.CON_INFO _coninfo;
	private SC_STAT_RENEWAL_BASESTAT_INFO_RES.CHA_INFO _chainfo;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_STAT_RENEWAL_BASESTAT_INFO_RES() {
	}

	public SC_STAT_RENEWAL_BASESTAT_INFO_RES.STR_INFO get_strinfo() {
		return _strinfo;
	}

	public void set_strinfo(SC_STAT_RENEWAL_BASESTAT_INFO_RES.STR_INFO val) {
		_bit |= 0x1;
		_strinfo = val;
	}

	public boolean has_strinfo() {
		return (_bit & 0x1) == 0x1;
	}

	public SC_STAT_RENEWAL_BASESTAT_INFO_RES.INT_INFO get_intinfo() {
		return _intinfo;
	}

	public void set_intinfo(SC_STAT_RENEWAL_BASESTAT_INFO_RES.INT_INFO val) {
		_bit |= 0x2;
		_intinfo = val;
	}

	public boolean has_intinfo() {
		return (_bit & 0x2) == 0x2;
	}

	public SC_STAT_RENEWAL_BASESTAT_INFO_RES.WIS_INFO get_wisinfo() {
		return _wisinfo;
	}

	public void set_wisinfo(SC_STAT_RENEWAL_BASESTAT_INFO_RES.WIS_INFO val) {
		_bit |= 0x4;
		_wisinfo = val;
	}

	public boolean has_wisinfo() {
		return (_bit & 0x4) == 0x4;
	}

	public SC_STAT_RENEWAL_BASESTAT_INFO_RES.DEX_INFO get_dexinfo() {
		return _dexinfo;
	}

	public void set_dexinfo(SC_STAT_RENEWAL_BASESTAT_INFO_RES.DEX_INFO val) {
		_bit |= 0x8;
		_dexinfo = val;
	}

	public boolean has_dexinfo() {
		return (_bit & 0x8) == 0x8;
	}

	public SC_STAT_RENEWAL_BASESTAT_INFO_RES.CON_INFO get_coninfo() {
		return _coninfo;
	}

	public void set_coninfo(SC_STAT_RENEWAL_BASESTAT_INFO_RES.CON_INFO val) {
		_bit |= 0x10;
		_coninfo = val;
	}

	public boolean has_coninfo() {
		return (_bit & 0x10) == 0x10;
	}

	public SC_STAT_RENEWAL_BASESTAT_INFO_RES.CHA_INFO get_chainfo() {
		return _chainfo;
	}

	public void set_chainfo(SC_STAT_RENEWAL_BASESTAT_INFO_RES.CHA_INFO val) {
		_bit |= 0x20;
		_chainfo = val;
	}

	public boolean has_chainfo() {
		return (_bit & 0x20) == 0x20;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		if (has_strinfo()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _strinfo);
		}
		if (has_intinfo()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _intinfo);
		}
		if (has_wisinfo()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _wisinfo);
		}
		if (has_dexinfo()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _dexinfo);
		}
		if (has_coninfo()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, _coninfo);
		}
		if (has_chainfo()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, _chainfo);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_strinfo()) {
			output.writeMessage(1, _strinfo);
		}
		if (has_intinfo()) {
			output.writeMessage(2, _intinfo);
		}
		if (has_wisinfo()) {
			output.writeMessage(3, _wisinfo);
		}
		if (has_dexinfo()) {
			output.writeMessage(4, _dexinfo);
		}
		if (has_coninfo()) {
			output.writeMessage(5, _coninfo);
		}
		if (has_chainfo()) {
			output.writeMessage(6, _chainfo);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
						message.toInt());
		try {
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				case 0x0000000A: {
					set_strinfo((SC_STAT_RENEWAL_BASESTAT_INFO_RES.STR_INFO) input
							.readMessage(SC_STAT_RENEWAL_BASESTAT_INFO_RES.STR_INFO.newInstance()));
					break;
				}
				case 0x00000012: {
					set_intinfo((SC_STAT_RENEWAL_BASESTAT_INFO_RES.INT_INFO) input
							.readMessage(SC_STAT_RENEWAL_BASESTAT_INFO_RES.INT_INFO.newInstance()));
					break;
				}
				case 0x0000001A: {
					set_wisinfo((SC_STAT_RENEWAL_BASESTAT_INFO_RES.WIS_INFO) input
							.readMessage(SC_STAT_RENEWAL_BASESTAT_INFO_RES.WIS_INFO.newInstance()));
					break;
				}
				case 0x00000022: {
					set_dexinfo((SC_STAT_RENEWAL_BASESTAT_INFO_RES.DEX_INFO) input
							.readMessage(SC_STAT_RENEWAL_BASESTAT_INFO_RES.DEX_INFO.newInstance()));
					break;
				}
				case 0x0000002A: {
					set_coninfo((SC_STAT_RENEWAL_BASESTAT_INFO_RES.CON_INFO) input
							.readMessage(SC_STAT_RENEWAL_BASESTAT_INFO_RES.CON_INFO.newInstance()));
					break;
				}
				case 0x00000032: {
					set_chainfo((SC_STAT_RENEWAL_BASESTAT_INFO_RES.CHA_INFO) input
							.readMessage(SC_STAT_RENEWAL_BASESTAT_INFO_RES.CHA_INFO.newInstance()));
					break;
				}
				default: {
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_STAT_RENEWAL_BASESTAT_INFO_RES();
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public static class STR_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static STR_INFO newInstance() {
			return new STR_INFO();
		}

		private int _basestr;
		private int _damagebonus;
		private int _hitbonus;
		private int _cribonus;
		private int _carrybonus;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private STR_INFO() {
		}

		public int get_basestr() {
			return _basestr;
		}

		public void set_basestr(int val) {
			_bit |= 0x1;
			_basestr = val;
		}

		public boolean has_basestr() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_damagebonus() {
			return _damagebonus;
		}

		public void set_damagebonus(int val) {
			_bit |= 0x2;
			_damagebonus = val;
		}

		public boolean has_damagebonus() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_hitbonus() {
			return _hitbonus;
		}

		public void set_hitbonus(int val) {
			_bit |= 0x4;
			_hitbonus = val;
		}

		public boolean has_hitbonus() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_cribonus() {
			return _cribonus;
		}

		public void set_cribonus(int val) {
			_bit |= 0x8;
			_cribonus = val;
		}

		public boolean has_cribonus() {
			return (_bit & 0x8) == 0x8;
		}

		public int get_carrybonus() {
			return _carrybonus;
		}

		public void set_carrybonus(int val) {
			_bit |= 0x10;
			_carrybonus = val;
		}

		public boolean has_carrybonus() {
			return (_bit & 0x10) == 0x10;
		}

		@Override
		public long getInitializeBit() {
			return (long) _bit;
		}

		@Override
		public int getMemorizedSerializeSizedSize() {
			return _memorizedSerializedSize;
		}

		@Override
		public int getSerializedSize() {
			int size = 0;
			if (has_basestr()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _basestr);
			}
			if (has_damagebonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _damagebonus);
			}
			if (has_hitbonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _hitbonus);
			}
			if (has_cribonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _cribonus);
			}
			if (has_carrybonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _carrybonus);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_basestr()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_basestr()) {
				output.wirteInt32(1, _basestr);
			}
			if (has_damagebonus()) {
				output.wirteInt32(2, _damagebonus);
			}
			if (has_hitbonus()) {
				output.wirteInt32(3, _hitbonus);
			}
			if (has_cribonus()) {
				output.wirteInt32(4, _cribonus);
			}
			if (has_carrybonus()) {
				output.wirteInt32(5, _carrybonus);
			}
		}

		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
				l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
							message.toInt());
			try {
				writeTo(stream);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return stream;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
				l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
					case 0x00000008: {
						set_basestr(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_damagebonus(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_hitbonus(input.readInt32());
						break;
					}
					case 0x00000020: {
						set_cribonus(input.readInt32());
						break;
					}
					case 0x00000028: {
						set_carrybonus(input.readInt32());
						break;
					}
					default: {
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
					.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
							((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
									+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try {
				readFrom(is);

				if (!isInitialized())
					return this;

				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null) {
					return this;
				}

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new STR_INFO();
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
			return newInstance();
		}

		@Override
		public void dispose() {
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}

	public static class INT_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static INT_INFO newInstance() {
			return new INT_INFO();
		}

		private int _baseint;
		private int _damagebonus;
		private int _hitbonus;
		private int _cribonus;
		private int _magicbonus;
		private int _reducemp;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private INT_INFO() {
		}

		public int get_baseint() {
			return _baseint;
		}

		public void set_baseint(int val) {
			_bit |= 0x1;
			_baseint = val;
		}

		public boolean has_baseint() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_damagebonus() {
			return _damagebonus;
		}

		public void set_damagebonus(int val) {
			_bit |= 0x2;
			_damagebonus = val;
		}

		public boolean has_damagebonus() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_hitbonus() {
			return _hitbonus;
		}

		public void set_hitbonus(int val) {
			_bit |= 0x4;
			_hitbonus = val;
		}

		public boolean has_hitbonus() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_cribonus() {
			return _cribonus;
		}

		public void set_cribonus(int val) {
			_bit |= 0x8;
			_cribonus = val;
		}

		public boolean has_cribonus() {
			return (_bit & 0x8) == 0x8;
		}

		public int get_magicbonus() {
			return _magicbonus;
		}

		public void set_magicbonus(int val) {
			_bit |= 0x10;
			_magicbonus = val;
		}

		public boolean has_magicbonus() {
			return (_bit & 0x10) == 0x10;
		}

		public int get_reducemp() {
			return _reducemp;
		}

		public void set_reducemp(int val) {
			_bit |= 0x20;
			_reducemp = val;
		}

		public boolean has_reducemp() {
			return (_bit & 0x20) == 0x20;
		}

		@Override
		public long getInitializeBit() {
			return (long) _bit;
		}

		@Override
		public int getMemorizedSerializeSizedSize() {
			return _memorizedSerializedSize;
		}

		@Override
		public int getSerializedSize() {
			int size = 0;
			if (has_baseint()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _baseint);
			}
			if (has_damagebonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _damagebonus);
			}
			if (has_hitbonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _hitbonus);
			}
			if (has_cribonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _cribonus);
			}
			if (has_magicbonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _magicbonus);
			}
			if (has_reducemp()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _reducemp);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_baseint()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_baseint()) {
				output.wirteInt32(1, _baseint);
			}
			if (has_damagebonus()) {
				output.wirteInt32(2, _damagebonus);
			}
			if (has_hitbonus()) {
				output.wirteInt32(3, _hitbonus);
			}
			if (has_cribonus()) {
				output.wirteInt32(4, _cribonus);
			}
			if (has_magicbonus()) {
				output.wirteInt32(5, _magicbonus);
			}
			if (has_reducemp()) {
				output.wirteInt32(6, _reducemp);
			}
		}

		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
				l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
							message.toInt());
			try {
				writeTo(stream);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return stream;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
				l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
					case 0x00000008: {
						set_baseint(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_damagebonus(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_hitbonus(input.readInt32());
						break;
					}
					case 0x00000020: {
						set_cribonus(input.readInt32());
						break;
					}
					case 0x00000028: {
						set_magicbonus(input.readInt32());
						break;
					}
					case 0x00000030: {
						set_reducemp(input.readInt32());
						break;
					}
					default: {
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
					.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
							((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
									+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try {
				readFrom(is);

				if (!isInitialized())
					return this;

				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null) {
					return this;
				}

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new INT_INFO();
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
			return newInstance();
		}

		@Override
		public void dispose() {
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}

	public static class WIS_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static WIS_INFO newInstance() {
			return new WIS_INFO();
		}

		private int _basewis;
		private int _mpregen;
		private int _mpincpotion;
		private int _mrbonus;
		private int _maxmplow;
		private int _maxmphigh;
		private int _addmpinc;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private WIS_INFO() {
		}

		public int get_basewis() {
			return _basewis;
		}

		public void set_basewis(int val) {
			_bit |= 0x1;
			_basewis = val;
		}

		public boolean has_basewis() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_mpregen() {
			return _mpregen;
		}

		public void set_mpregen(int val) {
			_bit |= 0x2;
			_mpregen = val;
		}

		public boolean has_mpregen() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_mpincpotion() {
			return _mpincpotion;
		}

		public void set_mpincpotion(int val) {
			_bit |= 0x4;
			_mpincpotion = val;
		}

		public boolean has_mpincpotion() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_mrbonus() {
			return _mrbonus;
		}

		public void set_mrbonus(int val) {
			_bit |= 0x8;
			_mrbonus = val;
		}

		public boolean has_mrbonus() {
			return (_bit & 0x8) == 0x8;
		}

		public int get_maxmplow() {
			return _maxmplow;
		}

		public void set_maxmplow(int val) {
			_bit |= 0x10;
			_maxmplow = val;
		}

		public boolean has_maxmplow() {
			return (_bit & 0x10) == 0x10;
		}

		public int get_maxmphigh() {
			return _maxmphigh;
		}

		public void set_maxmphigh(int val) {
			_bit |= 0x20;
			_maxmphigh = val;
		}

		public boolean has_maxmphigh() {
			return (_bit & 0x20) == 0x20;
		}

		public int get_addmpinc() {
			return _addmpinc;
		}

		public void set_addmpinc(int val) {
			_bit |= 0x40;
			_addmpinc = val;
		}

		public boolean has_addmpinc() {
			return (_bit & 0x40) == 0x40;
		}

		@Override
		public long getInitializeBit() {
			return (long) _bit;
		}

		@Override
		public int getMemorizedSerializeSizedSize() {
			return _memorizedSerializedSize;
		}

		@Override
		public int getSerializedSize() {
			int size = 0;
			if (has_basewis()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _basewis);
			}
			if (has_mpregen()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _mpregen);
			}
			if (has_mpincpotion()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _mpincpotion);
			}
			if (has_mrbonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _mrbonus);
			}
			if (has_maxmplow()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _maxmplow);
			}
			if (has_maxmphigh()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _maxmphigh);
			}
			if (has_addmpinc()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _addmpinc);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_basewis()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_basewis()) {
				output.wirteInt32(1, _basewis);
			}
			if (has_mpregen()) {
				output.wirteInt32(2, _mpregen);
			}
			if (has_mpincpotion()) {
				output.wirteInt32(3, _mpincpotion);
			}
			if (has_mrbonus()) {
				output.wirteInt32(4, _mrbonus);
			}
			if (has_maxmplow()) {
				output.wirteInt32(5, _maxmplow);
			}
			if (has_maxmphigh()) {
				output.wirteInt32(6, _maxmphigh);
			}
			if (has_addmpinc()) {
				output.wirteInt32(7, _addmpinc);
			}
		}

		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
				l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
							message.toInt());
			try {
				writeTo(stream);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return stream;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
				l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
					case 0x00000008: {
						set_basewis(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_mpregen(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_mpincpotion(input.readInt32());
						break;
					}
					case 0x00000020: {
						set_mrbonus(input.readInt32());
						break;
					}
					case 0x00000028: {
						set_maxmplow(input.readInt32());
						break;
					}
					case 0x00000030: {
						set_maxmphigh(input.readInt32());
						break;
					}
					case 0x00000038: {
						set_addmpinc(input.readInt32());
						break;
					}
					default: {
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
					.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
							((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
									+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try {
				readFrom(is);

				if (!isInitialized())
					return this;

				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null) {
					return this;
				}

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new WIS_INFO();
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
			return newInstance();
		}

		@Override
		public void dispose() {
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}

	public static class DEX_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static DEX_INFO newInstance() {
			return new DEX_INFO();
		}

		private int _basedex;
		private int _damagebonus;
		private int _hitbonus;
		private int _cribonus;
		private int _acbonus;
		private int _evasionbonus;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private DEX_INFO() {
		}

		public int get_basedex() {
			return _basedex;
		}

		public void set_basedex(int val) {
			_bit |= 0x1;
			_basedex = val;
		}

		public boolean has_basedex() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_damagebonus() {
			return _damagebonus;
		}

		public void set_damagebonus(int val) {
			_bit |= 0x2;
			_damagebonus = val;
		}

		public boolean has_damagebonus() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_hitbonus() {
			return _hitbonus;
		}

		public void set_hitbonus(int val) {
			_bit |= 0x4;
			_hitbonus = val;
		}

		public boolean has_hitbonus() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_cribonus() {
			return _cribonus;
		}

		public void set_cribonus(int val) {
			_bit |= 0x8;
			_cribonus = val;
		}

		public boolean has_cribonus() {
			return (_bit & 0x8) == 0x8;
		}

		public int get_acbonus() {
			return _acbonus;
		}

		public void set_acbonus(int val) {
			_bit |= 0x10;
			_acbonus = val;
		}

		public boolean has_acbonus() {
			return (_bit & 0x10) == 0x10;
		}

		public int get_evasionbonus() {
			return _evasionbonus;
		}

		public void set_evasionbonus(int val) {
			_bit |= 0x20;
			_evasionbonus = val;
		}

		public boolean has_evasionbonus() {
			return (_bit & 0x20) == 0x20;
		}

		@Override
		public long getInitializeBit() {
			return (long) _bit;
		}

		@Override
		public int getMemorizedSerializeSizedSize() {
			return _memorizedSerializedSize;
		}

		@Override
		public int getSerializedSize() {
			int size = 0;
			if (has_basedex()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _basedex);
			}
			if (has_damagebonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _damagebonus);
			}
			if (has_hitbonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _hitbonus);
			}
			if (has_cribonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _cribonus);
			}
			if (has_acbonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _acbonus);
			}
			if (has_evasionbonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _evasionbonus);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_basedex()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_basedex()) {
				output.wirteInt32(1, _basedex);
			}
			if (has_damagebonus()) {
				output.wirteInt32(2, _damagebonus);
			}
			if (has_hitbonus()) {
				output.wirteInt32(3, _hitbonus);
			}
			if (has_cribonus()) {
				output.wirteInt32(4, _cribonus);
			}
			if (has_acbonus()) {
				output.wirteInt32(5, _acbonus);
			}
			if (has_evasionbonus()) {
				output.wirteInt32(6, _evasionbonus);
			}
		}

		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
				l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
							message.toInt());
			try {
				writeTo(stream);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return stream;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
				l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
					case 0x00000008: {
						set_basedex(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_damagebonus(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_hitbonus(input.readInt32());
						break;
					}
					case 0x00000020: {
						set_cribonus(input.readInt32());
						break;
					}
					case 0x00000028: {
						set_acbonus(input.readInt32());
						break;
					}
					case 0x00000030: {
						set_evasionbonus(input.readInt32());
						break;
					}
					default: {
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
					.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
							((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
									+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try {
				readFrom(is);

				if (!isInitialized())
					return this;

				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null) {
					return this;
				}

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new DEX_INFO();
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
			return newInstance();
		}

		@Override
		public void dispose() {
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}

	public static class CON_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static CON_INFO newInstance() {
			return new CON_INFO();
		}

		private int _basecon;
		private int _hpregen;
		private int _hpincpotion;
		private int _carrybonus;
		private int _maxhpinc;
		private int _addhpinc;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private CON_INFO() {
		}

		public int get_basecon() {
			return _basecon;
		}

		public void set_basecon(int val) {
			_bit |= 0x1;
			_basecon = val;
		}

		public boolean has_basecon() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_hpregen() {
			return _hpregen;
		}

		public void set_hpregen(int val) {
			_bit |= 0x2;
			_hpregen = val;
		}

		public boolean has_hpregen() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_hpincpotion() {
			return _hpincpotion;
		}

		public void set_hpincpotion(int val) {
			_bit |= 0x4;
			_hpincpotion = val;
		}

		public boolean has_hpincpotion() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_carrybonus() {
			return _carrybonus;
		}

		public void set_carrybonus(int val) {
			_bit |= 0x8;
			_carrybonus = val;
		}

		public boolean has_carrybonus() {
			return (_bit & 0x8) == 0x8;
		}

		public int get_maxhpinc() {
			return _maxhpinc;
		}

		public void set_maxhpinc(int val) {
			_bit |= 0x10;
			_maxhpinc = val;
		}

		public boolean has_maxhpinc() {
			return (_bit & 0x10) == 0x10;
		}

		public int get_addhpinc() {
			return _addhpinc;
		}

		public void set_addhpinc(int val) {
			_bit |= 0x20;
			_addhpinc = val;
		}

		public boolean has_addhpinc() {
			return (_bit & 0x20) == 0x20;
		}

		@Override
		public long getInitializeBit() {
			return (long) _bit;
		}

		@Override
		public int getMemorizedSerializeSizedSize() {
			return _memorizedSerializedSize;
		}

		@Override
		public int getSerializedSize() {
			int size = 0;
			if (has_basecon()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _basecon);
			}
			if (has_hpregen()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _hpregen);
			}
			if (has_hpincpotion()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _hpincpotion);
			}
			if (has_carrybonus()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _carrybonus);
			}
			if (has_maxhpinc()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _maxhpinc);
			}
			if (has_addhpinc()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _addhpinc);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_basecon()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_basecon()) {
				output.wirteInt32(1, _basecon);
			}
			if (has_hpregen()) {
				output.wirteInt32(2, _hpregen);
			}
			if (has_hpincpotion()) {
				output.wirteInt32(3, _hpincpotion);
			}
			if (has_carrybonus()) {
				output.wirteInt32(4, _carrybonus);
			}
			if (has_maxhpinc()) {
				output.wirteInt32(5, _maxhpinc);
			}
			if (has_addhpinc()) {
				output.wirteInt32(6, _addhpinc);
			}
		}

		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
				l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
							message.toInt());
			try {
				writeTo(stream);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return stream;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
				l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
					case 0x00000008: {
						set_basecon(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_hpregen(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_hpincpotion(input.readInt32());
						break;
					}
					case 0x00000020: {
						set_carrybonus(input.readInt32());
						break;
					}
					case 0x00000028: {
						set_maxhpinc(input.readInt32());
						break;
					}
					case 0x00000030: {
						set_addhpinc(input.readInt32());
						break;
					}
					default: {
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
					.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
							((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
									+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try {
				readFrom(is);

				if (!isInitialized())
					return this;

				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null) {
					return this;
				}

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new CON_INFO();
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
			return newInstance();
		}

		@Override
		public void dispose() {
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}

	public static class CHA_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static CHA_INFO newInstance() {
			return new CHA_INFO();
		}

		private int _basecha;
		private int _pierceall;
		private int _decreasecooltime;
		private int _decreaseCCduration;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private CHA_INFO() {
		}

		public int get_basecha() {
			return _basecha;
		}

		public void set_basecha(int val) {
			_bit |= 0x1;
			_basecha = val;
		}

		public boolean has_basecha() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_pierceall() {
			return _pierceall;
		}

		public void set_pierceall(int val) {
			_bit |= 0x2;
			_pierceall = val;
		}

		public boolean has_pierceall() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_decreasecooltime() {
			return _decreasecooltime;
		}

		public void set_decreasecooltime(int val) {
			_bit |= 0x4;
			_decreasecooltime = val;
		}

		public boolean has_decreasecooltime() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_decreaseCCduration() {
			return _decreaseCCduration;
		}

		public void set_decreaseCCduration(int val) {
			_bit |= 0x8;
			_decreaseCCduration = val;
		}

		public boolean has_decreaseCCduration() {
			return (_bit & 0x8) == 0x8;
		}

		@Override
		public long getInitializeBit() {
			return (long) _bit;
		}

		@Override
		public int getMemorizedSerializeSizedSize() {
			return _memorizedSerializedSize;
		}

		@Override
		public int getSerializedSize() {
			int size = 0;
			if (has_basecha()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _basecha);
			}
			if (has_pierceall()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _pierceall);
			}
			if (has_decreasecooltime()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _decreasecooltime);
			}
			if (has_decreaseCCduration()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _decreaseCCduration);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_basecha()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_basecha()) {
				output.wirteInt32(1, _basecha);
			}
			if (has_pierceall()) {
				output.wirteInt32(2, _pierceall);
			}
			if (has_decreasecooltime()) {
				output.wirteInt32(3, _decreasecooltime);
			}
			if (has_decreaseCCduration()) {
				output.wirteInt32(4, _decreaseCCduration);
			}
		}

		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
				l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
							message.toInt());
			try {
				writeTo(stream);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return stream;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
				l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
					case 0x00000008: {
						set_basecha(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_pierceall(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_decreasecooltime(input.readInt32());
						break;
					}
					case 0x00000020: {
						set_decreaseCCduration(input.readInt32());
						break;
					}
					default: {
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
					.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
							((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
									+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try {
				readFrom(is);

				if (!isInitialized())
					return this;

				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null) {
					return this;
				}

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new CHA_INFO();
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
			return newInstance();
		}

		@Override
		public void dispose() {
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}
