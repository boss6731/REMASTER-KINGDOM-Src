package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class CS_CLIENT_INFO_LOG_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_CLIENT_INFO_LOG_REQ newInstance() {
		return new CS_CLIENT_INFO_LOG_REQ();
	}

	private int _transaction_id;
	private LogDFormat _log_data;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_CLIENT_INFO_LOG_REQ() {
	}

	public int get_transaction_id() {
		return _transaction_id;
	}

	public void set_transaction_id(int val) {
		_bit |= 0x1;
		_transaction_id = val;
	}

	public boolean has_transaction_id() {
		return (_bit & 0x1) == 0x1;
	}

	public LogDFormat get_log_data() {
		return _log_data;
	}

	public void set_log_data(LogDFormat val) {
		_bit |= 0x2;
		_log_data = val;
	}

	public boolean has_log_data() {
		return (_bit & 0x2) == 0x2;
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
		if (has_transaction_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _transaction_id);
		if (has_log_data())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _log_data);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_transaction_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_log_data()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_transaction_id()) {
			output.writeUInt32(1, _transaction_id);
		}
		if (has_log_data()) {
			output.writeMessage(2, _log_data);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				default: {
					return this;
				}
				case 0x00000008: {
					set_transaction_id(input.readUInt32());
					break;
				}
				case 0x00000012: {
					set_log_data((LogDFormat) input.readMessage(LogDFormat.newInstance()));
					break;
				}
			}
		}
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);
			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Lenz.
			if (_log_data != null && !MJString.isNullOrEmpty(_log_data.get_data1_str())) {
				L1PcInstance pc = clnt.getActiveChar();
				if (pc == null) {
					return this;
				}
				// SC_SPELL_LATE_HANDLING_NOTI.send(pc, true, eLevel.NOT_CORRECTION);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new CS_CLIENT_INFO_LOG_REQ();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_log_data() && _log_data != null) {
			_log_data.dispose();
			_log_data = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
