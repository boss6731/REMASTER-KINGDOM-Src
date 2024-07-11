package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RevengeInfoT.eAction;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eRevengeResult;
import l1j.server.revenge.MJRevengeService;
import l1j.server.revenge.model.MJRevengeModel;
import l1j.server.revenge.model.MJRevengeProvider;
import l1j.server.revenge.model.MJRevengeProvider.MJRevengeFindResult;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_REVENGE_START_PURSUIT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_REVENGE_START_PURSUIT_REQ newInstance() {
		return new CS_REVENGE_START_PURSUIT_REQ();
	}

	private PursuitTargetT _target;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_REVENGE_START_PURSUIT_REQ() {
	}

	public PursuitTargetT get_target() {
		return _target;
	}

	public void set_target(PursuitTargetT val) {
		_bit |= 0x1;
		_target = val;
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
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _target);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_target()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_target()) {
			output.writeMessage(1, _target);
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
					set_target((PursuitTargetT) input.readMessage(PursuitTargetT.newInstance()));
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

			if (_target == null || MJString.isNullOrEmpty(_target.get_user_name())) {
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			MJRevengeModel model = null;
			SC_REVENGE_START_PURSUIT_ACK ack = SC_REVENGE_START_PURSUIT_ACK.newInstance();
			ack.set_target(_target);
			_target.set_server_no(0);
			MJRevengeFindResult result = MJRevengeProvider.provider().findRevengeModel(pc, _target.get_user_name(),
					eAction.PURSUIT);
			if (result.result != eRevengeResult.SUCCESS) {
				ack.set_result(result.result);
			} else {
				eRevengeResult revengeResult = result.model.onRevengeAction(pc);
				ack.set_result(revengeResult);
				if (revengeResult == eRevengeResult.SUCCESS) {
					_target.set_location(result.target.getLongLocationReverse());
					_target.set_world_number(result.target.getMapId());
					model = result.model;
				}
			}
			pc.sendPackets(ack, MJEProtoMessages.SC_REVENGE_START_PURSUIT_ACK);
			/*
			 * if(model != null) {
			 * model.noti(pc);
			 * }
			 */
			MJRevengeProvider.provider().ackRevengeModel(pc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_REVENGE_START_PURSUIT_REQ();
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
