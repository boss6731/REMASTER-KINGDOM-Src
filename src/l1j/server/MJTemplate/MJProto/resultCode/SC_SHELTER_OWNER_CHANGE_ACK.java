package l1j.server.MJTemplate.MJProto.resultCode;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_SHELTER_OWNER_CHANGE_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_SHELTER_OWNER_CHANGE_ACK newInstance(){
		return new SC_SHELTER_OWNER_CHANGE_ACK();
	}
//	private .LIN.SC_SHELTER_OWNER_CHANGE_ACK.eRES _result;
	private int _need_change_time;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SHELTER_OWNER_CHANGE_ACK(){
	}
//	public .LIN.SC_SHELTER_OWNER_CHANGE_ACK.eRES get_result(){
//		return _result;
//	}
//	public void set_result(.LIN.SC_SHELTER_OWNER_CHANGE_ACK.eRES val){
//		_bit |= 0x1;
//		_result = val;
//	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_need_change_time(){
		return _need_change_time;
	}
	public void set_need_change_time(int val){
		_bit |= 0x2;
		_need_change_time = val;
	}
	public boolean has_need_change_time(){
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
		if (has_result()){
//			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		}
		if (has_need_change_time()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _need_change_time);
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
//			output.writeEnum(1, _result.toInt());
		}
		if (has_need_change_time()){
			output.wirteInt32(2, _need_change_time);
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
//					set_result(.LIN.SC_SHELTER_OWNER_CHANGE_ACK.eRES.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_need_change_time(input.readInt32());
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
		return new SC_SHELTER_OWNER_CHANGE_ACK();
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
	public enum eRES{
		eOK(0),
		eNotOwner(1),
		eNotShelterMap(2),
		eNotChangeTime(3),
		eAlreadyChanging(4),
		eNetworkBusy(5),
		eNotTargetShelterMap(6),
		eTargetOwner(7),
		eTargetNeedLevel(8),
		eNotOwnerShelterKey(9),
		eNotTarget(10),
		eFailShelterKeyItem(11),
		;
		private int value;
		eRES(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eRES v){
			return value == v.value;
		}
		public static eRES fromInt(int i){
			switch(i){
			case 0:
				return eOK;
			case 1:
				return eNotOwner;
			case 2:
				return eNotShelterMap;
			case 3:
				return eNotChangeTime;
			case 4:
				return eAlreadyChanging;
			case 5:
				return eNetworkBusy;
			case 6:
				return eNotTargetShelterMap;
			case 7:
				return eTargetOwner;
			case 8:
				return eTargetNeedLevel;
			case 9:
				return eNotOwnerShelterKey;
			case 10:
				return eNotTarget;
			case 11:
				return eFailShelterKeyItem;
			default:
				throw new IllegalArgumentException(String.format("i無效參數 eRES，%d", i));
			}
		}
	}
}
