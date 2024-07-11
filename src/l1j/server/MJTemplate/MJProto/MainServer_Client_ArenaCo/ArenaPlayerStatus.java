package l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class ArenaPlayerStatus implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ArenaPlayerStatus newInstance() {
		return new ArenaPlayerStatus();
	}

	private long _arena_char_id;
	private int _kill_count;
	private int _death_count;
	private int _hp_ratio;
	private int _mp_ratio;
	private int _loc_x;
	private int _loc_y;
	private int _attack_amount;
	private int _damaged_amount;
	private int _heal_amount;
	private boolean _poisoned;
	private boolean _paralysed;
	private boolean _is_live;
	private int _obj_id;
	private boolean _is_ready;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private ArenaPlayerStatus() {
	}

	public long get_arena_char_id() {
		return _arena_char_id;
	}

	public void set_arena_char_id(long val) {
		_bit |= 0x1;
		_arena_char_id = val;
	}

	public boolean has_arena_char_id() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_kill_count() {
		return _kill_count;
	}

	public void set_kill_count(int val) {
		_bit |= 0x2;
		_kill_count = val;
	}

	public boolean has_kill_count() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_death_count() {
		return _death_count;
	}

	public void set_death_count(int val) {
		_bit |= 0x4;
		_death_count = val;
	}

	public boolean has_death_count() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_hp_ratio() {
		return _hp_ratio;
	}

	public void set_hp_ratio(int val) {
		_bit |= 0x8;
		_hp_ratio = val;
	}

	public boolean has_hp_ratio() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_mp_ratio() {
		return _mp_ratio;
	}

	public void set_mp_ratio(int val) {
		_bit |= 0x10;
		_mp_ratio = val;
	}

	public boolean has_mp_ratio() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_loc_x() {
		return _loc_x;
	}

	public void set_loc_x(int val) {
		_bit |= 0x20;
		_loc_x = val;
	}

	public boolean has_loc_x() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_loc_y() {
		return _loc_y;
	}

	public void set_loc_y(int val) {
		_bit |= 0x40;
		_loc_y = val;
	}

	public boolean has_loc_y() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_attack_amount() {
		return _attack_amount;
	}

	public void set_attack_amount(int val) {
		_bit |= 0x80;
		_attack_amount = val;
	}

	public boolean has_attack_amount() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_damaged_amount() {
		return _damaged_amount;
	}

	public void set_damaged_amount(int val) {
		_bit |= 0x100;
		_damaged_amount = val;
	}

	public boolean has_damaged_amount() {
		return (_bit & 0x100) == 0x100;
	}

	public int get_heal_amount() {
		return _heal_amount;
	}

	public void set_heal_amount(int val) {
		_bit |= 0x200;
		_heal_amount = val;
	}

	public boolean has_heal_amount() {
		return (_bit & 0x200) == 0x200;
	}

	public boolean get_poisoned() {
		return _poisoned;
	}

	public void set_poisoned(boolean val) {
		_bit |= 0x400;
		_poisoned = val;
	}

	public boolean has_poisoned() {
		return (_bit & 0x400) == 0x400;
	}

	public boolean get_paralysed() {
		return _paralysed;
	}

	public void set_paralysed(boolean val) {
		_bit |= 0x800;
		_paralysed = val;
	}

	public boolean has_paralysed() {
		return (_bit & 0x800) == 0x800;
	}

	public boolean get_is_live() {
		return _is_live;
	}

	public void set_is_live(boolean val) {
		_bit |= 0x1000;
		_is_live = val;
	}

	public boolean has_is_live() {
		return (_bit & 0x1000) == 0x1000;
	}

	public int get_obj_id() {
		return _obj_id;
	}

	public void set_obj_id(int val) {
		_bit |= 0x2000;
		_obj_id = val;
	}

	public boolean has_obj_id() {
		return (_bit & 0x2000) == 0x2000;
	}

	public boolean get_is_ready() {
		return _is_ready;
	}

	public void set_is_ready(boolean val) {
		_bit |= 0x4000;
		_is_ready = val;
	}

	public boolean has_is_ready() {
		return (_bit & 0x4000) == 0x4000;
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
		if (has_arena_char_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(1, _arena_char_id);
		if (has_kill_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _kill_count);
		if (has_death_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _death_count);
		if (has_hp_ratio())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _hp_ratio);
		if (has_mp_ratio())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _mp_ratio);
		if (has_loc_x())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _loc_x);
		if (has_loc_y())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _loc_y);
		if (has_attack_amount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(8, _attack_amount);
		if (has_damaged_amount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(9, _damaged_amount);
		if (has_heal_amount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(10, _heal_amount);
		if (has_poisoned())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(11, _poisoned);
		if (has_paralysed())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(12, _paralysed);
		if (has_is_live())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(13, _is_live);
		if (has_obj_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(14, _obj_id);
		if (has_is_ready())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(15, _is_ready);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_arena_char_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_arena_char_id()) {
			output.writeInt64(1, _arena_char_id);
		}
		if (has_kill_count()) {
			output.writeUInt32(2, _kill_count);
		}
		if (has_death_count()) {
			output.writeUInt32(3, _death_count);
		}
		if (has_hp_ratio()) {
			output.writeUInt32(4, _hp_ratio);
		}
		if (has_mp_ratio()) {
			output.writeUInt32(5, _mp_ratio);
		}
		if (has_loc_x()) {
			output.wirteInt32(6, _loc_x);
		}
		if (has_loc_y()) {
			output.wirteInt32(7, _loc_y);
		}
		if (has_attack_amount()) {
			output.writeUInt32(8, _attack_amount);
		}
		if (has_damaged_amount()) {
			output.writeUInt32(9, _damaged_amount);
		}
		if (has_heal_amount()) {
			output.writeUInt32(10, _heal_amount);
		}
		if (has_poisoned()) {
			output.writeBool(11, _poisoned);
		}
		if (has_paralysed()) {
			output.writeBool(12, _paralysed);
		}
		if (has_is_live()) {
			output.writeBool(13, _is_live);
		}
		if (has_obj_id()) {
			output.writeUInt32(14, _obj_id);
		}
		if (has_is_ready()) {
			output.writeBool(15, _is_ready);
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
					set_arena_char_id(input.readInt64());
					break;
				}
				case 0x00000010: {
					set_kill_count(input.readUInt32());
					break;
				}
				case 0x00000018: {
					set_death_count(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_hp_ratio(input.readUInt32());
					break;
				}
				case 0x00000028: {
					set_mp_ratio(input.readUInt32());
					break;
				}
				case 0x00000030: {
					set_loc_x(input.readInt32());
					break;
				}
				case 0x00000038: {
					set_loc_y(input.readInt32());
					break;
				}
				case 0x00000040: {
					set_attack_amount(input.readUInt32());
					break;
				}
				case 0x00000048: {
					set_damaged_amount(input.readUInt32());
					break;
				}
				case 0x00000050: {
					set_heal_amount(input.readUInt32());
					break;
				}
				case 0x00000058: {
					set_poisoned(input.readBool());
					break;
				}
				case 0x00000060: {
					set_paralysed(input.readBool());
					break;
				}
				case 0x00000068: {
					set_is_live(input.readBool());
					break;
				}
				case 0x00000070: {
					set_obj_id(input.readUInt32());
					break;
				}
				case 0x00000078: {
					set_is_ready(input.readBool());
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
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new ArenaPlayerStatus();
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
