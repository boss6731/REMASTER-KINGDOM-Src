package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_EINHASAD_POINT_STAT_INVEST_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_EINHASAD_POINT_STAT_INVEST_REQ newInstance() {
		return new CS_EINHASAD_POINT_STAT_INVEST_REQ();
	}

	private int _bless;
	private int _lucky;
	private int _vital;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_EINHASAD_POINT_STAT_INVEST_REQ() {
	}

	public int get_bless() {
		return _bless;
	}

	public void set_bless(int val) {
		_bit |= 0x1;
		_bless = val;
	}

	public boolean has_bless() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_lucky() {
		return _lucky;
	}

	public void set_lucky(int val) {
		_bit |= 0x2;
		_lucky = val;
	}

	public boolean has_lucky() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_vital() {
		return _vital;
	}

	public void set_vital(int val) {
		_bit |= 0x4;
		_vital = val;
	}

	public boolean has_vital() {
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
		if (has_bless()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _bless);
		}
		if (has_lucky()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _lucky);
		}
		if (has_vital()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _vital);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_bless()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_lucky()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_vital()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_bless()) {
			output.wirteInt32(1, _bless);
		}
		if (has_lucky()) {
			output.wirteInt32(2, _lucky);
		}
		if (has_vital()) {
			output.wirteInt32(3, _vital);
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
					set_bless(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_lucky(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_vital(input.readInt32());
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
			// AinhasadSpecialStatInfo Info =
			// AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
			// if (Info != null) {
			// if (get_bless() + get_lucky() + get_vital() > Info.get_total_stat()) {
			// System.out.println(String.format("아인하사드 스탯 초과 : 중계기 사용 의심됨 캐릭명(%s) -클라오류
			// 가능성-", pc.getName()));
			// return this;
			// }
			//
			// if (get_bless() < 0 || get_lucky() < 0 || get_vital() < 0) {
			// System.out.println(String.format("아인하사드 스탯 초과 : 중계기 사용 의심됨 캐릭명(%s) -클라오류
			// 가능성-", pc.getName()));
			// return this;
			// }
			//
			// if (get_bless() > 25 || get_lucky() > 25 || get_vital() > 25) {
			// System.out.println(String.format("아인하사드 스탯 초과 : 중계기 사용 의심됨 캐릭명(%s) -클라오류
			// 가능성-", pc.getName()));
			// return this;
			// }
			//
			// Info.set_bless(get_bless());
			// Info.set_lucky(get_lucky());
			// Info.set_vital(get_vital());
			// }
			//
			// SC_EINHASAD_POINT_STAT_INVEST_ACK.send_point_stat_invest(pc, get_bless(),
			// get_lucky(), get_vital());
			// SC_REST_EXP_INFO_NOTI.send(pc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_EINHASAD_POINT_STAT_INVEST_REQ();
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
