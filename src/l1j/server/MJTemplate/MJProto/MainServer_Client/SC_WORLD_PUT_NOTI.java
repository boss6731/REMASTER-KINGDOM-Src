package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RegionT;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_WORLD_PUT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_WORLD_PUT_NOTI newInstance(L1PcInstance pc, boolean isUnderwater, boolean isUnderground) {
		SC_WORLD_PUT_NOTI noti = newInstance();
		int mapId = pc.getMap().getBaseMapId();
		if (mapId > 6000 && mapId < 6499) {
			mapId = 1005;
		} // 안타
		else if (mapId > 6501 && mapId < 6999) {
			mapId = 1011;
		} // 파푸
		else if (mapId > 1017 && mapId < 1023) {
			mapId = 1017;
		} else if (mapId > 9000 && mapId < 9099) {
			mapId = 9000;
		} // 말섬
		else if (mapId > 2101 && mapId < 2200) {
			mapId = 2101;
		} // 얼녀
		else if (mapId > 2699 && mapId < 2798) {
			mapId = 2699;
		} else if (mapId > 2600 && mapId < 2698) {
			mapId = 2600;
		} else if (mapId >= 7783 && mapId <= 7793) {
			mapId = 7783;
		} // 클라우디아
		else if (mapId >= 12152 && mapId <= 12252) {
			mapId = 12152;
		} // 클라우디아 공성장
		else if (mapId >= 12257 && mapId <= 12357) {
			mapId = 12257;
		} // 클라우디아 데스나이트
		else {
			mapId = MJRaidSpace.getInstance().getIdenMap(mapId);
			mapId = MJInstanceSpace.getInstance().getIdenMap(mapId);
		}

		noti.set_worldnumber(mapId);
		noti.set_homeserverno(0);
		noti.set_isunderwather(isUnderwater);
		noti.set_isunderground(isUnderground);
		noti.set_eventflag1(0);
		noti.set_eventflag2(0);
		noti.set_ghost_region(null);
		return noti;
	}

	public static ProtoOutputStream make_stream(L1PcInstance pc, boolean isUnderwater, boolean isUnderground) {
		SC_WORLD_PUT_NOTI noti = newInstance(pc, isUnderwater, isUnderground);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_WORLD_PUT_NOTI);
		noti.dispose();
		return stream;
	}

	public static SC_WORLD_PUT_NOTI newInstance() {
		return new SC_WORLD_PUT_NOTI();
	}

	private int _worldnumber;
	private int _homeserverno;
	private boolean _isunderwather;
	private boolean _isunderground;
	private int _eventflag1;
	private int _eventflag2;
	private RegionT _ghost_region;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_WORLD_PUT_NOTI() {
	}

	public int get_worldnumber() {
		return _worldnumber;
	}

	public void set_worldnumber(int val) {
		_bit |= 0x1;
		_worldnumber = val;
	}

	public boolean has_worldnumber() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_homeserverno() {
		return _homeserverno;
	}

	public void set_homeserverno(int val) {
		_bit |= 0x2;
		_homeserverno = val;
	}

	public boolean has_homeserverno() {
		return (_bit & 0x2) == 0x2;
	}

	public boolean get_isunderwather() {
		return _isunderwather;
	}

	public void set_isunderwather(boolean val) {
		_bit |= 0x4;
		_isunderwather = val;
	}

	public boolean has_isunderwather() {
		return (_bit & 0x4) == 0x4;
	}

	public boolean get_isunderground() {
		return _isunderground;
	}

	public void set_isunderground(boolean val) {
		_bit |= 0x8;
		_isunderground = val;
	}

	public boolean has_isunderground() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_eventflag1() {
		return _eventflag1;
	}

	public void set_eventflag1(int val) {
		_bit |= 0x10;
		_eventflag1 = val;
	}

	public boolean has_eventflag1() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_eventflag2() {
		return _eventflag2;
	}

	public void set_eventflag2(int val) {
		_bit |= 0x20;
		_eventflag2 = val;
	}

	public boolean has_eventflag2() {
		return (_bit & 0x20) == 0x20;
	}

	public RegionT get_ghost_region() {
		return _ghost_region;
	}

	public void set_ghost_region(RegionT val) {
		_bit |= 0x40;
		_ghost_region = val;
	}

	public boolean has_ghost_region() {
		return (_bit & 0x40) == 0x40;
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
		if (has_worldnumber())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _worldnumber);
		if (has_homeserverno())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _homeserverno);
		if (has_isunderwather())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _isunderwather);
		if (has_isunderground())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _isunderground);
		if (has_eventflag1())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _eventflag1);
		if (has_eventflag2())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(6, _eventflag2);
		if (has_ghost_region())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, _ghost_region);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_worldnumber()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_homeserverno()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isunderwather()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_isunderground()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_worldnumber()) {
			output.wirteInt32(1, _worldnumber);
		}
		if (has_homeserverno()) {
			output.wirteInt32(2, _homeserverno);
		}
		if (has_isunderwather()) {
			output.writeBool(3, _isunderwather);
		}
		if (has_isunderground()) {
			output.writeBool(4, _isunderground);
		}
		if (has_eventflag1()) {
			output.writeUInt32(5, _eventflag1);
		}
		if (has_eventflag2()) {
			output.writeUInt32(6, _eventflag2);
		}
		if (has_ghost_region()) {
			output.writeMessage(7, _ghost_region);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE,
						message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				default: {
					return this;
				}
				case 0x00000008: {
					set_worldnumber(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_homeserverno(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_isunderwather(input.readBool());
					break;
				}
				case 0x00000020: {
					set_isunderground(input.readBool());
					break;
				}
				case 0x00000028: {
					set_eventflag1(input.readUInt32());
					break;
				}
				case 0x00000030: {
					set_eventflag2(input.readUInt32());
					break;
				}
				case 0x0000003A: {
					set_ghost_region((RegionT) input.readMessage(RegionT.newInstance()));
					break;
				}
			}
		}
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Lenz.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_WORLD_PUT_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_ghost_region() && _ghost_region != null) {
			_ghost_region.dispose();
			_ghost_region = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
