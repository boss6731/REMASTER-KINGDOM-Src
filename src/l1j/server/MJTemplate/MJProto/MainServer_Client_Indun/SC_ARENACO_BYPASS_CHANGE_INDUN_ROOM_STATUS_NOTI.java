package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.ArenaUserInfo;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.IndunRoomInfo;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI newInstance(){
		return new SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI();
	}
	private int _room_id;
	private int _observer_count;
	private java.util.LinkedList<ArenaUserInfo> _player_info;
	private IndunRoomInfo _room_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI(){
	}
	public int get_room_id(){
		return _room_id;
	}
	public void set_room_id(int val){
		_bit |= 0x1;
		_room_id = val;
	}
	public boolean has_room_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_observer_count(){
		return _observer_count;
	}
	public void set_observer_count(int val){
		_bit |= 0x2;
		_observer_count = val;
	}
	public boolean has_observer_count(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<ArenaUserInfo> get_player_info(){
		return _player_info;
	}
	public void add_player_info(ArenaUserInfo val){
		if(!has_player_info()){
			_player_info = new java.util.LinkedList<ArenaUserInfo>();
			_bit |= 0x4;
		}
		_player_info.add(val);
	}
	public boolean has_player_info(){
		return (_bit & 0x4) == 0x4;
	}
	public IndunRoomInfo get_room_info(){
		return _room_info;
	}
	public void set_room_info(IndunRoomInfo val){
		_bit |= 0x8;
		_room_info = val;
	}
	public boolean has_room_info(){
		return (_bit & 0x8) == 0x8;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_room_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _room_id);
		}
		if (has_observer_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _observer_count);
		}
		if (has_player_info()){
			for(ArenaUserInfo val : _player_info){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		if (has_room_info()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _room_info);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_room_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_observer_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_player_info()){
			for(ArenaUserInfo val : _player_info){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_room_id()){
			output.writeUInt32(1, _room_id);
		}
		if (has_observer_count()){
			output.writeUInt32(2, _observer_count);
		}
		if (has_player_info()){
			for (ArenaUserInfo val : _player_info){
				output.writeMessage(3, val);
			}
		}
		if (has_room_info()){
			output.writeMessage(4, _room_info);
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = 
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_room_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_observer_count(input.readUInt32());
					break;
				}
				case 0x0000001A:{
					add_player_info((ArenaUserInfo)input.readMessage(ArenaUserInfo.newInstance()));
					break;
				}
				case 0x00000022:{
					set_room_info((IndunRoomInfo)input.readMessage(IndunRoomInfo.newInstance()));
					break;
				}
				default:{
					return this;
				}
			}
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null){
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_ARENACO_BYPASS_CHANGE_INDUN_ROOM_STATUS_NOTI();
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
