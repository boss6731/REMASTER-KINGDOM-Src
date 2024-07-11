package l1j.server.MJTemplate.MJProto.MainServer_Client_Companion;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CompanionT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CompanionT.eCommand;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_SUMMON_TM_COMMAND_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_SUMMON_TM_COMMAND_REQ newInstance() {
		return new CS_SUMMON_TM_COMMAND_REQ();
	}

	private CompanionT.eCommand _command;
	private int _npc_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_SUMMON_TM_COMMAND_REQ() {
	}

	public CompanionT.eCommand get_command() {
		return _command;
	}

	public void set_command(CompanionT.eCommand val) {
		_bit |= 0x1;
		_command = val;
	}

	public boolean has_command() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_npc_id() {
		return _npc_id;
	}

	public void set_npc_id(int val) {
		_bit |= 0x2;
		_npc_id = val;
	}

	public boolean has_npc_id() {
		return (_bit & 0x2) == 0x2;
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
		if (has_command()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _command.toInt());
		}
		if (has_npc_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _npc_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_command()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_npc_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_command()) {
			output.writeEnum(1, _command.toInt());
		}
		if (has_npc_id()) {
			output.writeUInt32(2, _npc_id);
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
					set_command(CompanionT.eCommand.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_npc_id(input.readUInt32());
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
			L1Object obj = L1World.getInstance().findObject(_npc_id);
			if (obj == null) {
				return this;
			}

			if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (npc.getMaster() == null) {
					return this;
				} else if (_command == eCommand.TM_Attack) {
					npc.onFinalAction(pc, "aggressive");
				} else if (_command == eCommand.TM_Defensive) {
					npc.onFinalAction(pc, "stay");
					npc.onFinalAction(pc, "defensive");
				} else {
					return this;
				}
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_SUMMON_TM_COMMAND_REQ();
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
