package l1j.server.MJTemplate.MJProto.MainServer_Client_GameGate;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class CS_GAMEGATE_INSTANT_TOKEN_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_GAMEGATE_INSTANT_TOKEN_REQ newInstance() {
		return new CS_GAMEGATE_INSTANT_TOKEN_REQ();
	}

	private String _app_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_GAMEGATE_INSTANT_TOKEN_REQ() {
	}

	public String get_app_id() {
		return _app_id;
	}

	public void set_app_id(String val) {
		_bit |= 0x1;
		_app_id = val;
	}

	public boolean has_app_id() {
		return (_bit & 0x1) == 0x1;
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
		if (has_app_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _app_id);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_app_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_app_id()) {
			output.writeString(1, _app_id);
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
				case 0x0000000A: {
					set_app_id(input.readString());
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
			// System.out.println(MJHexHelper.toString(bytes, bytes.length));
			if (!isInitialized())
				return this;
			/*
			 * System.out.println(_app_id);
			 * // TODO : 아래부터 처리 코드를 삽입하십시오. made by Lenz.
			 * SC_GAMEGATE_INSTANT_TOKEN_NOTI noti =
			 * SC_GAMEGATE_INSTANT_TOKEN_NOTI.newInstance();
			 * noti.set_instant_token_id("NG1lbl9zZXJ2ZXI=");
			 * clnt.sendPacket(noti,
			 * MJEProtoMessages.SC_GAMEGATE_INSTANT_TOKEN_NOTI.toInt());
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new CS_GAMEGATE_INSTANT_TOKEN_REQ();
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
