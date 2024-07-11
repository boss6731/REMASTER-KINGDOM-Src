package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import l1j.server.Config;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;
import l1j.server.server.datatables.CraftListNewAllow;
import l1j.server.server.model.item.collection.favor.loader.FavorBookCraftLoader;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_LIMITED_CRAFT_INFO_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_LIMITED_CRAFT_INFO_REQ newInstance() {
		return new CS_LIMITED_CRAFT_INFO_REQ();
	}

	private int _craft_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_LIMITED_CRAFT_INFO_REQ() {
	}

	public int get_craft_id() {
		return _craft_id;
	}

	public void set_craft_id(int val) {
		_bit |= 0x1;
		_craft_id = val;
	}

	public boolean has_craft_id() {
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
		if (has_craft_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _craft_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_craft_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_craft_id()) {
			output.wirteInt32(1, _craft_id);
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
					set_craft_id(input.readInt32());
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

			// TODO 특정 제작은 못하도록 !를 한이유는 서버측에서 등록한거 외에는 사용불가
			MJObjectEventProvider.provider().pcEventFactory().fireCraftOpen(pc);
			if (_craft_id != 0) {
				if (!CraftListNewAllow.getInstance().isCraft(_craft_id)) {
					pc.sendPackets("該製作服務暫時中斷，請重新啟動客戶端。");
					if (pc.isGm())
						System.out.println("[CS_LIMITED_CRAFT_INFO_REQ] : (不可)craft_id:" + _craft_id);
					return this;
				}
			}

			if (!FavorBookCraftLoader.getInstance().isFavorCraft(_craft_id)) {
				if (!Config.ServerAdSetting.TotalCraftList) {
					pc.sendPackets("目前【統合製作】系統暫時中斷。");
					return this;
				}
			}

			// 01-22주석
			// CraftListLoader.getInstance().reloadSend(pc);

			SC_LIMITED_CRAFT_INFO_ACK.send(pc, _craft_id);
			// System.out.println("CS_LIMITED_CRAFT_INFO_REQ : (허용)craft_id:" + _craft_id);
			pc.setCraftUseType(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_LIMITED_CRAFT_INFO_REQ();
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
