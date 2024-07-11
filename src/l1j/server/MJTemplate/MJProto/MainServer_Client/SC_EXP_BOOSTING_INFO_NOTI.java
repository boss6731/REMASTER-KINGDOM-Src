package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_EXP_BOOSTING_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_EXP_BOOSTING_INFO_NOTI newInstance() {
		return new SC_EXP_BOOSTING_INFO_NOTI();
	}

	public static void send(L1PcInstance pc) {
		if (pc == null || pc.getAccount() == null)
			return;

		SC_EXP_BOOSTING_INFO_NOTI bonus_noti = SC_EXP_BOOSTING_INFO_NOTI.newInstance();

		int hasad = pc.getAccount().getBlessOfAin();
		if (hasad > 0)
			hasad /= 10000;

		int ein_level = SC_REST_EXP_INFO_NOTI.get_ein_level(hasad);
		double ration = SC_REST_EXP_INFO_NOTI.expRation(ein_level) * 100;
		double extra = SC_REST_EXP_INFO_NOTI.expExtra(pc, ein_level) * 100;

		int item = (int) pc.get_item_exp_bonus();
		if (pc.getLevel() <= Config.ServerAdSetting.LineageBuff) {
			item += Config.ServerAdSetting.LineageBuffExpRation;
		}
		if (pc.hasSkillEffect(L1SkillId.PC_EXP_UP)) {
			item += 10;
		}

		int total = (int) (ration + extra + item);
		// System.out.println("ein_level : " + ein_level + " / ration : " + ration + " /
		// extra : " + extra + " / item : " + item);
		bonus_noti.set_exp_boosting_ratio(total);
		pc.sendPackets(bonus_noti, MJEProtoMessages.SC_EXP_BOOSTING_INFO_NOTI, true);
	}

	private int _exp_boosting_ratio;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EXP_BOOSTING_INFO_NOTI() {
	}

	public int get_exp_boosting_ratio() {
		return _exp_boosting_ratio;
	}

	public void set_exp_boosting_ratio(int val) {
		_bit |= 0x1;
		_exp_boosting_ratio = val;
	}

	public boolean has_exp_boosting_ratio() {
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
		if (has_exp_boosting_ratio()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _exp_boosting_ratio);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_exp_boosting_ratio()) {
			output.writeUInt32(1, _exp_boosting_ratio);
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
					set_exp_boosting_ratio(input.readUInt32());
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
		return new SC_EXP_BOOSTING_INFO_NOTI();
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
