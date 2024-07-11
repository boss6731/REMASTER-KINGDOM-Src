package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_SUMMON_PET_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void on_summoned(L1PcInstance pc, L1ItemInstance item, int duration, int class_id) {
		SC_SUMMON_PET_NOTI noti = newInstance();
		noti.set_bonus_duration(duration);
		noti.set_monclass(class_id);
		noti.set_petball_nameId(item.getId());
		noti.set_bonus_desc(item.getStatusBytes());
		pc.sendPackets(noti, MJEProtoMessages.SC_SUMMON_PET_NOTI);
	}

	public static void off_summoned(L1PcInstance pc, L1ItemInstance item, int class_id) {
		SC_SUMMON_PET_NOTI noti = newInstance();
		/*
		 * noti.set_bonus_duration(-1);
		 * noti.set_monclass(class_id);
		 * noti.set_petball_nameId(item.getId());
		 */ noti.set_bonus_duration(-1);
		noti.set_monclass(0);
		noti.set_petball_nameId(0);
		pc.sendPackets(noti, MJEProtoMessages.SC_SUMMON_PET_NOTI);
	}

	public static void off_summoned(L1PcInstance pc) {
		SC_SUMMON_PET_NOTI noti = newInstance();
		noti.set_bonus_duration(-1);
		noti.set_monclass(0);
		noti.set_petball_nameId(0);
		pc.sendPackets(noti, MJEProtoMessages.SC_SUMMON_PET_NOTI);
	}

	public static SC_SUMMON_PET_NOTI newInstance() {
		return new SC_SUMMON_PET_NOTI();
	}

	private int _bonus_duration;
	private int _monclass;
	private int _petball_nameId;
	private byte[] _bonus_desc;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_SUMMON_PET_NOTI() {
	}

	public int get_bonus_duration() {
		return _bonus_duration;
	}

	public void set_bonus_duration(int val) {
		_bit |= 0x1;
		_bonus_duration = val;
	}

	public boolean has_bonus_duration() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_monclass() {
		return _monclass;
	}

	public void set_monclass(int val) {
		_bit |= 0x2;
		_monclass = val;
	}

	public boolean has_monclass() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_petball_nameId() {
		return _petball_nameId;
	}

	public void set_petball_nameId(int val) {
		_bit |= 0x4;
		_petball_nameId = val;
	}

	public boolean has_petball_nameId() {
		return (_bit & 0x4) == 0x4;
	}

	public byte[] get_bonus_desc() {
		return _bonus_desc;
	}

	public void set_bonus_desc(byte[] val) {
		_bit |= 0x8;
		_bonus_desc = val;
	}

	public boolean has_bonus_desc() {
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
		if (has_bonus_duration())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _bonus_duration);
		if (has_monclass())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _monclass);
		if (has_petball_nameId())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _petball_nameId);
		if (has_bonus_desc())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _bonus_desc);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_bonus_duration()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_monclass()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_petball_nameId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_bonus_duration()) {
			output.wirteInt32(1, _bonus_duration);
		}
		if (has_monclass()) {
			output.wirteInt32(2, _monclass);
		}
		if (has_petball_nameId()) {
			output.wirteInt32(3, _petball_nameId);
		}
		if (has_bonus_desc()) {
			output.writeBytes(4, _bonus_desc);
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
					set_bonus_duration(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_monclass(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_petball_nameId(input.readInt32());
					break;
				}
				case 0x00000022: {
					set_bonus_desc(input.readBytes());
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_SUMMON_PET_NOTI();
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
