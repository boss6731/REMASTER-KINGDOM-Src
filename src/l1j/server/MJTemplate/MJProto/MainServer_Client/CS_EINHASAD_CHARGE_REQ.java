package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SystemMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_EINHASAD_CHARGE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_EINHASAD_CHARGE_REQ newInstance() {
		return new CS_EINHASAD_CHARGE_REQ();
	}

	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_EINHASAD_CHARGE_REQ() {
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

			einhasad_charge(pc, SC_REST_EXP_INFO_NOTI.RE_DRAGON_DIAMOND_CHARGE);

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public static void einhasad_charge(L1PcInstance pc, int charge_point) {
		Account account = pc.getAccount();
		if (account.getBlessOfAinCharge() == 0)
			return;

		if (pc.hasSkillEffect(L1SkillId.EINHASAD_GREAT_FLAT)) {
			pc.sendPackets(String.format("無法在使用亞伊瑪德的祝福（包月制）時使用此功能。"));
			return;
		}
		if (pc.getAccount().getBlessOfAin() > charge_point) {
			pc.sendPackets(String.format("僅當亞伊瑪德的祝福數值低於 %d%% 時可用。", charge_point / 10000));
			return;
		}

		// pc.getAccount().setBlessOfAin(charge_point, pc);
		account.setBlessOfAinCharge(0);
		pc.getAccount().addBlessOfAin(charge_point - account.getBlessOfAin(), pc);
		pc.sendPackets(new S_SystemMessage(String.format("亞伊瑪德的祝福已充電 %d%%。", charge_point / 10000)));
		pc.send_effect(15357);
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_EINHASAD_CHARGE_REQ();
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
