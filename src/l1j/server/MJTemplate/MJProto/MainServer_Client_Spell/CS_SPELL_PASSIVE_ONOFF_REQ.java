package l1j.server.MJTemplate.MJProto.MainServer_Client_Spell;

import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJPassiveSkill.MJPassiveInfo;
import l1j.server.MJPassiveSkill.MJPassiveLoader;

public class CS_SPELL_PASSIVE_ONOFF_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_SPELL_PASSIVE_ONOFF_REQ newInstance() {
		return new CS_SPELL_PASSIVE_ONOFF_REQ();
	}

	private int _passive_id;
	private boolean _onoff;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_SPELL_PASSIVE_ONOFF_REQ() {
	}

	public int get_passive_id() {
		return _passive_id;
	}

	public void set_passive_id(int val) {
		_bit |= 0x1;
		_passive_id = val;
	}

	public boolean has_passive_id() {
		return (_bit & 0x1) == 0x1;
	}

	public boolean get_onoff() {
		return _onoff;
	}

	public void set_onoff(boolean val) {
		_bit |= 0x2;
		_onoff = val;
	}

	public boolean has_onoff() {
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
		if (has_passive_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _passive_id);
		}
		if (has_onoff()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _onoff);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_passive_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_onoff()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_passive_id()) {
			output.wirteInt32(1, _passive_id);
		}
		if (has_onoff()) {
			output.writeBool(2, _onoff);
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
				set_passive_id(input.readInt32());
				break;
			}
			case 0x00000010: {
				set_onoff(input.readBool());
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
			
			if (pc.isElf() && pc.isDead()) {
				SC_SPELL_PASSIVE_ONOFF_ACK.send(pc, MJPassiveID.BLOODY_SOUL_NEW.toInt(), false);
				return this;
			}

			if (pc.getPassive(_passive_id) == null) {
				return this;
			}

			if (!pc.isElf()) {
				return this;
			}

			MJPassiveInfo passInfo = MJPassiveLoader.getInstance().fromPassiveId(_passive_id);
			if (passInfo == null)
				return this;

			if (_passive_id == MJPassiveID.BLOODY_SOUL_NEW.toInt()) {
				if (_onoff) {
					pc.startBloodToSoul();
				} else {
					pc.stopBloodToSoul();
				}
			}

			SC_SPELL_PASSIVE_ONOFF_ACK.send(pc, _passive_id, _onoff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_SPELL_PASSIVE_ONOFF_REQ();
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
