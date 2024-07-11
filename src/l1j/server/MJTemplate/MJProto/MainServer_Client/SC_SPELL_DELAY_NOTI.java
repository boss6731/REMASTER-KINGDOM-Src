package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_SPELL_DELAY_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void UseSkillDelay(L1PcInstance pc, int delay, int global_delay, int group_id) {
		SC_SPELL_DELAY_NOTI noti = SC_SPELL_DELAY_NOTI.newInstance();
		noti.set_next_spell_delay(delay); // 메인
		noti.set_next_spell_global_delay(global_delay); // 이건 그룹으로 갈릴경우 사용 아닌경우 메인과 같은값을 보낸다.
		noti.set_spell_group_id(group_id);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_DELAY_NOTI, true);
	}

	// b4 0f 04 //윈드커터
	// 08 e8 03 //488
	// 10 e8 03
	// 18 00 af db

	// if (delay <= 0) {
	// delay = 5000;
	// }
	// SC_SPELL_DELAY_NOTI noti = newInstance();
	// noti.set_next_spell_delay(delay);
	//
	// int groupId = getSkillGroupId(pc, spellId);
	// if (pc.isFencer()) {
	// noti.set_next_spell_global_delay(IntRange.getTotalValueToPercent(delay, 14,
	// true));
	// //System.out.println("delay:" + delay + " div:" +
	// IntRange.getTotalValueToPercent(delay, 14, true));
	// noti.set_spell_group_id(groupId);
	// } else {
	// if (groupId > 0) {
	// noti.set_next_spell_global_delay(IntRange.getTotalValueToPercent(delay, 14,
	// true));
	// noti.set_spell_group_id(groupId);
	// } else {
	// noti.set_next_spell_global_delay(delay);
	// noti.set_spell_group_id(0);
	// }
	// }
	//
	// pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_DELAY_NOTI, true);
	//
	public static SC_SPELL_DELAY_NOTI newInstance() {
		return new SC_SPELL_DELAY_NOTI();
	}

	private int _next_spell_delay; // 0x00000008
	private int _next_spell_global_delay; // 0x00000010
	private int _spell_group_id; // 0x00000018
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_SPELL_DELAY_NOTI() {
	}

	public int get_next_spell_delay() {
		return _next_spell_delay;
	}

	public void set_next_spell_delay(int val) {
		_bit |= 0x1;
		_next_spell_delay = val;
	}

	public boolean has_next_spell_delay() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_next_spell_global_delay() {
		return _next_spell_global_delay;
	}

	public void set_next_spell_global_delay(int val) {
		_bit |= 0x2;
		_next_spell_global_delay = val;
	}

	public boolean has_next_spell_global_delay() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_spell_group_id() {
		return _spell_group_id;
	}

	public void set_spell_group_id(int val) {
		_bit |= 0x4;
		_spell_group_id = val;
	}

	public boolean has_spell_group_id() {
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
		if (has_next_spell_delay()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _next_spell_delay);
		}
		if (has_next_spell_global_delay()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _next_spell_global_delay);
		}
		if (has_spell_group_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _spell_group_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_next_spell_delay()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_next_spell_delay()) {
			output.wirteInt32(1, _next_spell_delay);
		}
		if (has_next_spell_global_delay()) {
			output.wirteInt32(2, _next_spell_global_delay);
		}
		if (has_spell_group_id()) {
			output.wirteInt32(3, _spell_group_id);
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
					set_next_spell_delay(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_next_spell_global_delay(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_spell_group_id(input.readInt32());
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
		return new SC_SPELL_DELAY_NOTI();
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
