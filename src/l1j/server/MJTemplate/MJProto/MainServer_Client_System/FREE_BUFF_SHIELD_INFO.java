package l1j.server.MJTemplate.MJProto.MainServer_Client_System;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class FREE_BUFF_SHIELD_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static FREE_BUFF_SHIELD_INFO newInstance(){
		return new FREE_BUFF_SHIELD_INFO();
	}
	private FREE_BUFF_SHIELD_TYPE _favor_type;
	private int _favor_remain_count;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private FREE_BUFF_SHIELD_INFO(){
	}
	public FREE_BUFF_SHIELD_TYPE get_favor_type(){
		return _favor_type;
	}
	public void set_favor_type(FREE_BUFF_SHIELD_TYPE val){
		_bit |= 0x1;
		_favor_type = val;
	}
	public boolean has_favor_type(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_favor_remain_count(){
		return _favor_remain_count;
	}
	public void set_favor_remain_count(int val){
		_bit |= 0x2;
		_favor_remain_count = val;
	}
	public boolean has_favor_remain_count(){
		return (_bit & 0x2) == 0x2;
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
		if (has_favor_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _favor_type.toInt());
		}
		if (has_favor_remain_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _favor_remain_count);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_favor_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_favor_remain_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_favor_type()){
			output.writeEnum(1, _favor_type.toInt());
		}
		if (has_favor_remain_count()){
			output.wirteInt32(2, _favor_remain_count);
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
					set_favor_type(FREE_BUFF_SHIELD_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_favor_remain_count(input.readInt32());
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
		return new FREE_BUFF_SHIELD_INFO();
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
