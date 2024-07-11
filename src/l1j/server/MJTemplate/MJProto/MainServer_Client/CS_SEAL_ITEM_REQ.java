package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.S_PacketBox;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_SEAL_ITEM_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_SEAL_ITEM_REQ newInstance() {
		return new CS_SEAL_ITEM_REQ();
	}

	private int _object_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_SEAL_ITEM_REQ() {
	}

	public int get_object_id() {
		return _object_id;
	}

	public void set_object_id(int val) {
		_bit |= 0x1;
		_object_id = val;
	}

	public boolean has_object_id() {
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
		if (has_object_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _object_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_object_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_object_id()) {
			output.writeUInt32(1, _object_id);
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
					set_object_id(input.readUInt32());
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

			L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(this.get_object_id());

			/*
			 * if (l1iteminstance1.getBless() == 0 || l1iteminstance1.getBless() == 1 ||
			 * l1iteminstance1.getBless() == 2 || l1iteminstance1.getBless() == 3) {
			 * pc.sendPackets("\\f3봉인 시스템이 중단 되었습니다.");
			 * return this;
			 * } else {
			 * pc.sendPackets("\\f3봉인 시스템이 중단 되었습니다.");
			 * return this;
			 * }
			 */

			if (l1iteminstance1.getItem().getType2() == 0) { // etc 아이템이라면
				if (l1iteminstance1.getItem().getUseType() != 73) {
					pc.sendPackets(79); // 아무일도 일어나지 않는다 (멘트)
					return this;
				}
			}

			if (l1iteminstance1.getItem().isEndedTimeMessage()) {
				pc.sendPackets(79);
				return this;
			}

			if (!pc.getInventory().checkItem(40308, 10000)) {
				pc.sendPackets("缺少 10,000 金幣。");
				return this;
			}

			if (l1iteminstance1.getBless() == 0 || l1iteminstance1.getBless() == 1 || l1iteminstance1.getBless() == 2
					|| l1iteminstance1.getBless() == 3) {
				int Bless = 0;
				switch (l1iteminstance1.getBless()) {
					case 0:
						Bless = 128;
						break; // 축
					case 1:
						Bless = 129;
						break; // 보통 case 2: Bless = 130; break; // 저주
					case 3:
						Bless = 131;
						break; // 미확인
				}
				l1iteminstance1.setBless(Bless);
				int st = 0;
				if (l1iteminstance1.isIdentified())
					st += 1;
				if (!l1iteminstance1.getItem().isTradable())
					st += 2;
				if (l1iteminstance1.getItem().isCantDelete())
					st += 4;
				if (l1iteminstance1.getItem().get_safeenchant() < 0)
					st += 8;
				if (l1iteminstance1.getBless() >= 128) {
					st = 32;
					if (l1iteminstance1.isIdentified()) {
						st += 15;
					} else {
						st += 14;
					}
				}
				pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, l1iteminstance1, st));
				pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
				pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
				pc.getInventory().consumeItem(40308, 100000);
				// pc.sendPackets("봉인해제시 .봉인해제신청 명령어를 사용하세요.");
			} else
				pc.sendPackets(79); // \f1 아무것도 일어나지 않았습니다.

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_SEAL_ITEM_REQ();
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
