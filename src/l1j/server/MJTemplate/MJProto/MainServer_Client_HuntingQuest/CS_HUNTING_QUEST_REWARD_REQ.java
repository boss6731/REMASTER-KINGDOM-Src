package l1j.server.MJTemplate.MJProto.MainServer_Client_HuntingQuest;

import l1j.server.CPMWBQSystem.CPMWBQSystemProvider;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eMonsterBookV2RewardGrade;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_HUNTING_QUEST_REWARD_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_HUNTING_QUEST_REWARD_REQ newInstance() {
		return new CS_HUNTING_QUEST_REWARD_REQ();
	}

	private int _map_number;
	private int _location_desc;
	private eMonsterBookV2RewardGrade _reward_grade;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_HUNTING_QUEST_REWARD_REQ() {
	}

	public int get_map_number() {
		return _map_number;
	}

	public void set_map_number(int val) {
		_bit |= 0x1;
		_map_number = val;
	}

	public boolean has_map_number() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_location_desc() {
		return _location_desc;
	}

	public void set_location_desc(int val) {
		_bit |= 0x2;
		_location_desc = val;
	}

	public boolean has_location_desc() {
		return (_bit & 0x2) == 0x2;
	}

	public eMonsterBookV2RewardGrade get_reward_grade() {
		return _reward_grade;
	}

	public void set_reward_grade(eMonsterBookV2RewardGrade val) {
		_bit |= 0x4;
		_reward_grade = val;
	}

	public boolean has_reward_grade() {
		return (_bit & 0x4) == 0x4;
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
		if (has_map_number()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _map_number);
		}
		if (has_location_desc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _location_desc);
		}
		if (has_reward_grade()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _reward_grade.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_map_number()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_reward_grade()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_map_number()) {
			output.wirteInt32(1, _map_number);
		}
		if (has_location_desc()) {
			output.wirteInt32(2, _location_desc);
		}
		if (has_reward_grade()) {
			output.writeEnum(3, _reward_grade.toInt());
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
					set_map_number(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_location_desc(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_reward_grade(eMonsterBookV2RewardGrade.fromInt(input.readEnum()));
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
			CPMWBQSystemProvider.provider().BQQuestComplete(pc, this);
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_HUNTING_QUEST_REWARD_REQ();
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
