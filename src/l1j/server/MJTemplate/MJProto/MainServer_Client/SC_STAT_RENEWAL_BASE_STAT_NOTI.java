package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_STAT_RENEWAL_BASE_STAT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send_base_stat(L1PcInstance pc) {
		SC_STAT_RENEWAL_BASE_STAT_NOTI noti = SC_STAT_RENEWAL_BASE_STAT_NOTI.newInstance();
		noti.set_str(pc.getAbility().getStr());
		noti.set_int(pc.getAbility().getInt());
		noti.set_wis(pc.getAbility().getWis());
		noti.set_dex(pc.getAbility().getDex());
		noti.set_con(pc.getAbility().getCon());
		noti.set_cha(pc.getAbility().getCha());
		pc.sendPackets(noti, MJEProtoMessages.SC_STAT_RENEWAL_BASE_STAT_NOTI);
	}

	public static SC_STAT_RENEWAL_BASE_STAT_NOTI newInstance() {
		return new SC_STAT_RENEWAL_BASE_STAT_NOTI();
	}

	private int _str;
	private int _int;
	private int _wis;
	private int _dex;
	private int _con;
	private int _cha;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_STAT_RENEWAL_BASE_STAT_NOTI() {
	}

	public int get_str() {
		return _str;
	}

	public void set_str(int val) {
		_bit |= 0x1;
		_str = val;
	}

	public boolean has_str() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_int() {
		return _int;
	}

	public void set_int(int val) {
		_bit |= 0x2;
		_int = val;
	}

	public boolean has_int() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_wis() {
		return _wis;
	}

	public void set_wis(int val) {
		_bit |= 0x4;
		_wis = val;
	}

	public boolean has_wis() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_dex() {
		return _dex;
	}

	public void set_dex(int val) {
		_bit |= 0x8;
		_dex = val;
	}

	public boolean has_dex() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_con() {
		return _con;
	}

	public void set_con(int val) {
		_bit |= 0x10;
		_con = val;
	}

	public boolean has_con() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_cha() {
		return _cha;
	}

	public void set_cha(int val) {
		_bit |= 0x20;
		_cha = val;
	}

	public boolean has_cha() {
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
		if (has_str()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _str);
		}
		if (has_int()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _int);
		}
		if (has_wis()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _wis);
		}
		if (has_dex()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _dex);
		}
		if (has_con()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _con);
		}
		if (has_cha()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _cha);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_str()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_int()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_wis()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_dex()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_con()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_cha()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_str()) {
			output.wirteInt32(1, _str);
		}
		if (has_int()) {
			output.wirteInt32(2, _int);
		}
		if (has_wis()) {
			output.wirteInt32(3, _wis);
		}
		if (has_dex()) {
			output.wirteInt32(4, _dex);
		}
		if (has_con()) {
			output.wirteInt32(5, _con);
		}
		if (has_cha()) {
			output.wirteInt32(6, _cha);
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
					set_str(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_int(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_wis(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_dex(input.readInt32());
					break;
				}
				case 0x00000028: {
					set_con(input.readInt32());
					break;
				}
				case 0x00000030: {
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
		return new SC_STAT_RENEWAL_BASE_STAT_NOTI();
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
