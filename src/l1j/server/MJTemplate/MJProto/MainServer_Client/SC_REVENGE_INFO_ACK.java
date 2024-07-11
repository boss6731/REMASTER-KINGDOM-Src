package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eRevengeResult;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_REVENGE_INFO_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_REVENGE_INFO_ACK newInstance() {
		return new SC_REVENGE_INFO_ACK();
	}

	private eRevengeResult _result;
	private int _list_duration;
	private int _pursuit_cost;
	private java.util.LinkedList<RevengeInfoT> _revenge_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_REVENGE_INFO_ACK() {
	}

	public eRevengeResult get_result() {
		return _result;
	}

	public void set_result(eRevengeResult val) {
		_bit |= 0x1;
		_result = val;
	}

	public boolean has_result() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_list_duration() {
		return _list_duration;
	}

	public void set_list_duration(int val) {
		_bit |= 0x2;
		_list_duration = val;
	}

	public boolean has_list_duration() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_pursuit_cost() {
		return _pursuit_cost;
	}

	public void set_pursuit_cost(int val) {
		_bit |= 0x4;
		_pursuit_cost = val;
	}

	public boolean has_pursuit_cost() {
		return (_bit & 0x4) == 0x4;
	}

	public java.util.LinkedList<RevengeInfoT> get_revenge_info() {
		return _revenge_info;
	}

	public void add_revenge_info(RevengeInfoT val) {
		if (!has_revenge_info()) {
			_revenge_info = new java.util.LinkedList<RevengeInfoT>();
			_bit |= 0x8;
		}
		_revenge_info.add(val);
	}

	public boolean has_revenge_info() {
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
		if (has_result()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		}
		if (has_list_duration()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _list_duration);
		}
		if (has_pursuit_cost()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _pursuit_cost);
		}
		if (has_revenge_info()) {
			for (RevengeInfoT val : _revenge_info) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_result()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_pursuit_cost()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_revenge_info()) {
			for (RevengeInfoT val : _revenge_info) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_result()) {
			output.writeEnum(1, _result.toInt());
		}
		if (has_list_duration()) {
			output.writeUInt32(2, _list_duration);
		}
		if (has_pursuit_cost()) {
			output.writeUInt32(3, _pursuit_cost);
		}
		if (has_revenge_info()) {
			for (RevengeInfoT val : _revenge_info) {
				output.writeMessage(4, val);
			}
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
					set_result(eRevengeResult.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_list_duration(input.readUInt32());
					break;
				}
				case 0x00000018: {
					set_pursuit_cost(input.readUInt32());
					break;
				}
				case 0x00000022: {
					add_revenge_info((RevengeInfoT) input.readMessage(RevengeInfoT.newInstance()));
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
		return new SC_REVENGE_INFO_ACK();
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
