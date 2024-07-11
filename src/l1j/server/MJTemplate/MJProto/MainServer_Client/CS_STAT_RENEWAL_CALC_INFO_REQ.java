package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_STAT_RENEWAL_CALC_INFO_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_STAT_RENEWAL_CALC_INFO_REQ newInstance() {
		return new CS_STAT_RENEWAL_CALC_INFO_REQ();
	}

	private int _lev;
	private int _class;
	private int _infotype;
	private int _option_value;
	private int _option_mask;
	private int _str;
	private int _int;
	private int _wis;
	private int _dex;
	private int _con;
	private int _cha;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_STAT_RENEWAL_CALC_INFO_REQ() {
	}

	public int get_lev() {
		return _lev;
	}

	public void set_lev(int val) {
		_bit |= 0x1;
		_lev = val;
	}

	public boolean has_lev() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_class() {
		return _class;
	}

	public void set_class(int val) {
		_bit |= 0x2;
		_class = val;
	}

	public boolean has_class() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_infotype() {
		return _infotype;
	}

	public void set_infotype(int val) {
		_bit |= 0x4;
		_infotype = val;
	}

	public boolean has_infotype() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_option_value() {
		return _option_value;
	}

	public void set_option_value(int val) {
		_bit |= 0x8;
		_option_value = val;
	}

	public boolean has_option_value() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_option_mask() {
		return _option_mask;
	}

	public void set_option_mask(int val) {
		_bit |= 0x10;
		_option_mask = val;
	}

	public boolean has_option_mask() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_str() {
		return _str;
	}

	public void set_str(int val) {
		_bit |= 0x20;
		_str = val;
	}

	public boolean has_str() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_int() {
		return _int;
	}

	public void set_int(int val) {
		_bit |= 0x40;
		_int = val;
	}

	public boolean has_int() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_wis() {
		return _wis;
	}

	public void set_wis(int val) {
		_bit |= 0x80;
		_wis = val;
	}

	public boolean has_wis() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_dex() {
		return _dex;
	}

	public void set_dex(int val) {
		_bit |= 0x100;
		_dex = val;
	}

	public boolean has_dex() {
		return (_bit & 0x100) == 0x100;
	}

	public int get_con() {
		return _con;
	}

	public void set_con(int val) {
		_bit |= 0x200;
		_con = val;
	}

	public boolean has_con() {
		return (_bit & 0x200) == 0x200;
	}

	public int get_cha() {
		return _cha;
	}

	public void set_cha(int val) {
		_bit |= 0x400;
		_cha = val;
	}

	public boolean has_cha() {
		return (_bit & 0x400) == 0x400;
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
		if (has_lev()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _lev);
		}
		if (has_class()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _class);
		}
		if (has_infotype()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _infotype);
		}
		if (has_option_value()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _option_value);
		}
		if (has_option_mask()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _option_mask);
		}
		if (has_str()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _str);
		}
		if (has_int()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _int);
		}
		if (has_wis()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _wis);
		}
		if (has_dex()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _dex);
		}
		if (has_con()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _con);
		}
		if (has_cha()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _cha);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_lev()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_class()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_infotype()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_lev()) {
			output.wirteInt32(1, _lev);
		}
		if (has_class()) {
			output.wirteInt32(2, _class);
		}
		if (has_infotype()) {
			output.wirteInt32(3, _infotype);
		}
		if (has_option_value()) {
			output.wirteInt32(4, _option_value);
		}
		if (has_option_mask()) {
			output.wirteInt32(5, _option_mask);
		}
		if (has_str()) {
			output.wirteInt32(6, _str);
		}
		if (has_int()) {
			output.wirteInt32(7, _int);
		}
		if (has_wis()) {
			output.wirteInt32(8, _wis);
		}
		if (has_dex()) {
			output.wirteInt32(9, _dex);
		}
		if (has_con()) {
			output.wirteInt32(10, _con);
		}
		if (has_cha()) {
			output.wirteInt32(11, _cha);
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
					set_lev(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_class(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_infotype(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_option_value(input.readInt32());
					break;
				}
				case 0x00000028: {
					set_option_mask(input.readInt32());
					break;
				}
				case 0x00000030: {
					set_str(input.readInt32());
					break;
				}
				case 0x00000038: {
					set_int(input.readInt32());
					break;
				}
				case 0x00000040: {
					set_wis(input.readInt32());
					break;
				}
				case 0x00000048: {
					set_dex(input.readInt32());
					break;
				}
				case 0x00000050: {
					set_con(input.readInt32());
					break;
				}
				case 0x00000058: {
					set_cha(input.readInt32());
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
				if (this.get_infotype() == 8) {
					SC_STAT_RENEWAL_INFO_NOTI.send_stat_renewal_info(clnt, this.get_option_value(), this.get_class(),
							this.get_infotype(), this.get_str(), this.get_int(), this.get_wis(), this.get_dex(),
							this.get_con(), this.get_cha());
				} else if (this.get_infotype() == 1) {
					SC_STAT_RENEWAL_INFO_NOTI.send_stat_renewal_info(clnt, this.get_option_value(), this.get_class(),
							this.get_infotype(), this.get_str(), this.get_int(), this.get_wis(), this.get_dex(),
							this.get_con(), this.get_cha());
				}
				// System.out.println(String.format("info %d option %d str %d",
				// this.get_infotype(), this.get_option_value(), this.get_str()));
				return this;
			}
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			if (pc.getReturnStat() != 0)
				SC_STAT_RENEWAL_INFO_NOTI.send_stat_renewal_info(clnt, get_class(), get_infotype(), get_str(),
						get_int(), get_wis(), get_dex(), get_con(), get_cha());
			else
				SC_STAT_RENEWAL_INFO_NOTI.send_stat_renewal_info(clnt, get_option_mask(), get_class(), get_infotype(),
						get_str(), get_int(), get_wis(), get_dex(), get_con(), get_cha());
			SC_STAT_RENEWAL_BASESTAT_INFO_RES.send_bonus_stat(pc, get_str(), get_int(), get_wis(), get_dex(), get_con(),
					get_cha());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_STAT_RENEWAL_CALC_INFO_REQ();
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

	public enum INFO_TYPE {
		STAT_NORMAL(1),
		STAT_CALC(2),
		STAT_LEVUP(4),
		STAT_CALC_START(8),
		STAT_CALC_INGAME(16),
		;

		private int value;

		INFO_TYPE(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(INFO_TYPE v) {
			return value == v.value;
		}

		public static INFO_TYPE fromInt(int i) {
			switch (i) {
				case 1:
					return STAT_NORMAL;
				case 2:
					return STAT_CALC;
				case 4:
					return STAT_LEVUP;
				case 8:
					return STAT_CALC_START;
				case 16:
					return STAT_CALC_INGAME;
				default:
					throw new IllegalArgumentException(String.format("無效參數 INFO_TYPE，%d", i));
			}
		}
	}

	public enum OPTION_VALUE {
		OPTION_NO_RESPONSE(1),
		;

		private int value;

		OPTION_VALUE(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(OPTION_VALUE v) {
			return value == v.value;
		}

		public static OPTION_VALUE fromInt(int i) {
			switch (i) {
				case 1:
					return OPTION_NO_RESPONSE;
				default:
					throw new IllegalArgumentException(String.format("無效參數 OPTION_VALUE，%d", i));
			}
		}
	}

	public enum OPTION_MASK {
		OPTION_STR_MASK(1),
		OPTION_INT_MASK(2),
		OPTION_WIS_MASK(4),
		OPTION_DEX_MASK(8),
		OPTION_CON_MASK(16),
		OPTION_CHA_MASK(32),
		;

		private int value;

		OPTION_MASK(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(OPTION_MASK v) {
			return value == v.value;
		}

		public static OPTION_MASK fromInt(int i) {
			switch (i) {
				case 1:
					return OPTION_STR_MASK;
				case 2:
					return OPTION_INT_MASK;
				case 4:
					return OPTION_WIS_MASK;
				case 8:
					return OPTION_DEX_MASK;
				case 16:
					return OPTION_CON_MASK;
				case 32:
					return OPTION_CHA_MASK;
				default:
					throw new IllegalArgumentException(String.format("無效參數 OPTION_MASK，%d", i));
			}
		}
	}
}
