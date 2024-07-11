package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.Config;
import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class CS_CLIENT_VERSION_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_CLIENT_VERSION_INFO newInstance() {
		return new CS_CLIENT_VERSION_INFO();
	}

	private int _protocol_version;
	private int _active_code_page;
	private int _check_seed;
	private int _client_version;
	private int _client_instance_count;
	private byte[] _check_resource_seed;
	private boolean _is_remaster;
	private int _client_specific_value;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_CLIENT_VERSION_INFO() {
	}

	public int get_protocol_version() {
		return _protocol_version;
	}

	public void set_protocol_version(int val) {
		_bit |= 0x1;
		_protocol_version = val;
	}

	public boolean has_protocol_version() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_active_code_page() {
		return _active_code_page;
	}

	public void set_active_code_page(int val) {
		_bit |= 0x2;
		_active_code_page = val;
	}

	public boolean has_active_code_page() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_check_seed() {
		return _check_seed;
	}

	public void set_check_seed(int val) {
		_bit |= 0x4;
		_check_seed = val;
	}

	public boolean has_check_seed() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_client_version() {
		return _client_version;
	}

	public void set_client_version(int val) {
		_bit |= 0x8;
		_client_version = val;
	}

	public boolean has_client_version() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_client_instance_count() {
		return _client_instance_count;
	}

	public void set_client_instance_count(int val) {
		_bit |= 0x10;
		_client_instance_count = val;
	}

	public boolean has_client_instance_count() {
		return (_bit & 0x10) == 0x10;
	}

	public byte[] get_check_resource_seed() {
		return _check_resource_seed;
	}

	public void set_check_resource_seed(byte[] val) {
		_bit |= 0x20;
		_check_resource_seed = val;
	}

	public boolean has_check_resource_seed() {
		return (_bit & 0x20) == 0x20;
	}

	public boolean get_is_remaster() {
		return _is_remaster;
	}

	public void set_is_remaster(boolean val) {
		_bit |= 0x40;
		_is_remaster = val;
	}

	public boolean has_is_remaster() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_client_specific_value() {
		return _client_specific_value;
	}

	public void set_client_specific_value(int val) {
		_bit |= 0x80;
		_client_specific_value = val;
	}

	public boolean has_client_specific_value() {
		return (_bit & 0x80) == 0x80;
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
		if (has_protocol_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _protocol_version);
		if (has_active_code_page())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _active_code_page);
		if (has_check_seed())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _check_seed);
		if (has_client_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _client_version);
		if (has_client_instance_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _client_instance_count);
		if (has_check_resource_seed())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(6, _check_resource_seed);
		if (has_is_remaster())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(7, _is_remaster);
		if (has_client_specific_value())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _client_specific_value);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_protocol_version()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_active_code_page()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_check_seed()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_client_version()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_protocol_version()) {
			output.writeUInt32(1, _protocol_version);
		}
		if (has_active_code_page()) {
			output.wirteInt32(2, _active_code_page);
		}
		if (has_check_seed()) {
			output.writeUInt32(3, _check_seed);
		}
		if (has_client_version()) {
			output.wirteInt32(4, _client_version);
		}
		if (has_client_instance_count()) {
			output.writeUInt32(5, _client_instance_count);
		}
		if (has_check_resource_seed()) {
			output.writeBytes(6, _check_resource_seed);
		}
		if (has_is_remaster()) {
			output.writeBool(7, _is_remaster);
		}
		if (has_client_specific_value()) {
			output.wirteInt32(8, _client_specific_value);
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
					set_protocol_version(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_active_code_page(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_check_seed(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_client_version(input.readInt32());
					break;
				}
				case 0x00000028: {
					set_client_instance_count(input.readUInt32());
					break;
				}
				case 0x00000032: {
					set_check_resource_seed(input.readBytes());
					break;
				}
				case 0x00000038: {
					set_is_remaster(input.readBool());
					break;
				}
				case 0x00000040: {
					set_client_specific_value(input.readInt32());
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
			long clientversion = Long.parseLong(Integer.toUnsignedString(_client_version));
			// System.out.println(clientversion);
			// System.out.println(Long.parseLong(Integer.toUnsignedString(MJNetSafeLoadManager.NS_CLIENT_VERSION)));
			// clnt.set_version(_client_version);
			clnt.set_version(clientversion);
			SC_SERVER_VERSION_INFO.send(clnt);
			// SC_SERVER_VERSION_INFO.send(clnt);
			// System.out.println(_is_remaster);
			// System.out.println(get_client_specific_value());

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			/*
			 * if (_client_instance_count >
			 * MJNetServerLoadManager.DESKTOP_CLIENT_PERMISSION) {
			 * SC_SERVER_VERSION_INFO.denial_client_over(clnt);
			 * } else
			 */

			/*
			 * if (Config.version_check) {
			 * clnt.set_version(_client_version);
			 * SC_SERVER_VERSION_INFO.send(clnt);
			 * } else if (_client_version == MJNetSafeLoadManager.NS_CLIENT_VERSION &&
			 * _client_instance_count >= 1) {
			 * clnt.set_version(_client_version);
			 * SC_SERVER_VERSION_INFO.send(clnt);
			 * } else {
			 * SC_SERVER_VERSION_INFO.denial(clnt);
			 * }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new CS_CLIENT_VERSION_INFO();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
