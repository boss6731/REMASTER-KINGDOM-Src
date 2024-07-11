package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eRevengeResult;
import l1j.server.revenge.MJRevengeService;
import l1j.server.revenge.model.MJRevengeModel;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_REVENGE_PURSUIT_INFO_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_REVENGE_PURSUIT_INFO_REQ newInstance() {
		return new CS_REVENGE_PURSUIT_INFO_REQ();
	}

	private java.util.LinkedList<PursuitTargetT> _target;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_REVENGE_PURSUIT_INFO_REQ() {
	}

	public java.util.LinkedList<PursuitTargetT> get_target() {
		return _target;
	}

	public void add_target(PursuitTargetT val) {
		if (!has_target()) {
			_target = new java.util.LinkedList<PursuitTargetT>();
			_bit |= 0x1;
		}
		_target.add(val);
	}

	public boolean has_target() {
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
		if (has_target()) {
			for (PursuitTargetT val : _target) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_target()) {
			for (PursuitTargetT val : _target) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_target()) {
			for (PursuitTargetT val : _target) {
				output.writeMessage(1, val);
			}
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
				case 0x0000000A: {
					add_target((PursuitTargetT) input.readMessage(PursuitTargetT.newInstance()));
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
			if (!MJRevengeService.service().use()) {
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			SC_REVENGE_PURSUIT_INFO_ACK ack = SC_REVENGE_PURSUIT_INFO_ACK.newInstance();
			if (!has_target()) {
				ack.set_result(eRevengeResult.FAIL_OTHER);
			} else {
				MJRevengeModel model = pc.attribute().getNotExistsNew(L1PcInstance.revengePursuitModelKey).get();
				if (model == null) {
					ack.set_result(eRevengeResult.FAIL_LIST);
				} else if (!model.alivePursuit()) {
					ack.set_result(eRevengeResult.FAIL_TIME);
				} else {
					L1PcInstance target = model.target();
					if (target == null) {
						ack.set_result(eRevengeResult.SUCCESS);
						PursuitTargetT targetT = PursuitTargetT.newInstance();
						targetT.set_user_name(get_target().get(0).get_user_name());
						targetT.set_server_no(0);
						ack.add_target(targetT);
					} else {
						ack.set_result(eRevengeResult.SUCCESS);
						PursuitTargetT targetT = PursuitTargetT.newInstance();
						targetT.set_world_number(target.getMapId());
						targetT.set_location(target.getLongLocationReverse());
						targetT.set_server_no(0);
						targetT.set_user_name(target.getName());
						ack.add_target(targetT);
					}
				}
			}
			pc.sendPackets(ack, MJEProtoMessages.SC_REVENGE_PURSUIT_INFO_ACK);
			// System.out.println(MJHexHelper.toString(bytes, bytes.length));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_REVENGE_PURSUIT_INFO_REQ();
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
