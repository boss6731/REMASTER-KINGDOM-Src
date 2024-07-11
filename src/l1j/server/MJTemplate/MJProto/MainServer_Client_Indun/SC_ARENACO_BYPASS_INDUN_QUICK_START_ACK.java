package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaMapKind;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_ARENACO_BYPASS_INDUN_QUICK_START_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ARENACO_BYPASS_INDUN_QUICK_START_ACK newInstance(){
		return new SC_ARENACO_BYPASS_INDUN_QUICK_START_ACK();
	}
	private boolean _result;
	private boolean _is_owner;
	private int _room_id;
	private eArenaMapKind _mapkind;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ARENACO_BYPASS_INDUN_QUICK_START_ACK(){
		set_mapkind(eArenaMapKind.None);
	}
	public boolean get_result(){
		return _result;
	}
	public void set_result(boolean val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public boolean get_is_owner(){
		return _is_owner;
	}
	public void set_is_owner(boolean val){
		_bit |= 0x2;
		_is_owner = val;
	}
	public boolean has_is_owner(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_room_id(){
		return _room_id;
	}
	public void set_room_id(int val){
		_bit |= 0x4;
		_room_id = val;
	}
	public boolean has_room_id(){
		return (_bit & 0x4) == 0x4;
	}
	public eArenaMapKind get_mapkind(){
		return _mapkind;
	}
	public void set_mapkind(eArenaMapKind val){
		_bit |= 0x8;
		_mapkind = val;
	}
	public boolean has_mapkind(){
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
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _result);
		}
		if (has_is_owner()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_owner);
		}
		if (has_room_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _room_id);
		}
		if (has_mapkind()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _mapkind.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_result()){
			output.writeBool(1, _result);
		}
		if (has_is_owner()){
			output.writeBool(2, _is_owner);
		}
		if (has_room_id()){
			output.writeUInt32(3, _room_id);
		}
		if (has_mapkind()){
			output.writeEnum(4, _mapkind.toInt());
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
					set_result(input.readBool());
					break;
				}
				case 0x00000010:{
					set_is_owner(input.readBool());
					break;
				}
				case 0x00000018:{
					set_room_id(input.readUInt32());
					break;
				}
				case 0x00000020:{
					set_mapkind(eArenaMapKind.fromInt(input.readEnum()));
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_ARENACO_BYPASS_INDUN_QUICK_START_ACK();
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
