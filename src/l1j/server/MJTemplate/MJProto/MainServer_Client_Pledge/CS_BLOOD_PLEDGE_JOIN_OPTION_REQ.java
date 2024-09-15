package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import MJShiftObject.Battle.MJShiftBattlePlayManager;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_BLOOD_PLEDGE_JOIN_OPTION_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_BLOOD_PLEDGE_JOIN_OPTION_REQ newInstance(){
		return new CS_BLOOD_PLEDGE_JOIN_OPTION_REQ();
	}
	private CS_BLOOD_PLEDGE_JOIN_OPTION_REQ.eOPTION_TYPE _type;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_BLOOD_PLEDGE_JOIN_OPTION_REQ(){
	}
	public CS_BLOOD_PLEDGE_JOIN_OPTION_REQ.eOPTION_TYPE get_type(){
		return _type;
	}
	public void set_type(CS_BLOOD_PLEDGE_JOIN_OPTION_REQ.eOPTION_TYPE val){
		_bit |= 0x1;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x1) == 0x1;
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
		if (has_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _type.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_type()){
			output.writeEnum(1, _type.toInt());
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
					set_type(CS_BLOOD_PLEDGE_JOIN_OPTION_REQ.eOPTION_TYPE.fromInt(input.readEnum()));
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

			if (pc.getClanid() == 0)
				return this;

			if (MJShiftBattlePlayManager.is_shift_battle(pc))
				return this;

			pc.sendPackets(SC_BLOOD_PLEDGE_JOIN_OPTION_ACK.sendJoinSetting(pc, pc.getClan().getJoinSetting(), pc.getClan().getJoinType()));
		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_BLOOD_PLEDGE_JOIN_OPTION_REQ();
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
	public enum eOPTION_TYPE{
		eOPTION_TYPE_ENABLE_JOIN(1),
		eOPTION_TYPE_JOIN_TYPE(2),
		eOPTION_TYPE_HASHED_PASSWORD(3),
		eOPTION_TYPE_ALL(4),
		;
		private int value;
		eOPTION_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eOPTION_TYPE v){
			return value == v.value;
		}
		public static eOPTION_TYPE fromInt(int i){
			switch(i){
			case 1:
				return eOPTION_TYPE_ENABLE_JOIN;
			case 2:
				return eOPTION_TYPE_JOIN_TYPE;
			case 3:
				return eOPTION_TYPE_HASHED_PASSWORD;
			case 4:
				return eOPTION_TYPE_ALL;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eOPTION_TYPE, %d", i));
			}
		}
	}
}
