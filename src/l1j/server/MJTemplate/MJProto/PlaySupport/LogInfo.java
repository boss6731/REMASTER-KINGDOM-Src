package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class LogInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static LogInfo newInstance(){
		return new LogInfo();
	}
	private LogInfo.LogType _Type;
	private int _StrID;
	private java.util.LinkedList<String> _StrValue;
	private String _Time;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private LogInfo(){
		set_Type(LogInfo.LogType.NONE);
		set_StrID(0);
	}
	public LogInfo.LogType get_Type(){
		return _Type;
	}
	public void set_Type(LogInfo.LogType val){
		_bit |= 0x1;
		_Type = val;
	}
	public boolean has_Type(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_StrID(){
		return _StrID;
	}
	public void set_StrID(int val){
		_bit |= 0x2;
		_StrID = val;
	}
	public boolean has_StrID(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<String> get_StrValue(){
		return _StrValue;
	}
	public void add_StrValue(String val){
		if(!has_StrValue()){
			_StrValue = new java.util.LinkedList<String>();
			_bit |= 0x4;
		}
		_StrValue.add(val);
	}
	public boolean has_StrValue(){
		return (_bit & 0x4) == 0x4;
	}
	public String get_Time(){
		return _Time;
	}
	public void set_Time(String val){
		_bit |= 0x8;
		_Time = val;
	}
	public boolean has_Time(){
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
		if (has_Type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _Type.toInt());
		}
		if (has_StrID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _StrID);
		}
		if (has_StrValue()){
			for(String val : _StrValue){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, val);
			}
		}
		if (has_Time()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _Time);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_Type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_StrID()){
			_memorizedIsInitialized = -1;
			return false;
		}
//		if (has_StrValue()){
//			for(String val : _StrValue){
//				if (!val.isInitialized()){
//					_memorizedIsInitialized = -1;
//					return false;
//				}
//			}
//		}
		if (!has_Time()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_Type()){
			output.writeEnum(1, _Type.toInt());
		}
		if (has_StrID()){
			output.writeUInt32(2, _StrID);
		}
		if (has_StrValue()){
			for (String val : _StrValue){
				output.writeString(3, val);
			}
		}
		if (has_Time()){
			output.writeString(4, _Time);
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
					set_Type(LogInfo.LogType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_StrID(input.readUInt32());
					break;
				}
				case 0x0000001A:{
					add_StrValue(input.readString());
					break;
				}
				case 0x00000022:{
					set_Time(input.readString());
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
		return new LogInfo();
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
	public enum LogType{
		NONE(1),
		OFF_ETC(2),
		OFF_DEAD(3),
		OFF_PEACE(4),
		OFF_COMEBACK(5),
		OFF_HALT(6),
		OFF_RESTART(7),
		OFF_BUY_FAIL(8),
		OFF_MOVE_FAIL(9),
		OFF_MAP_TIME_ERR(10),
		OFF_WEIGHT_OVER(11),
		START_ALL(12),
		YETI_GET_ITEM(13),
		SHOP_BUY(14),
		SHOP_BUFF(15),
		PVP_ATTACKED(16),
		HP_MP_RESTORE(17),
		RETURN(18),
		MAP_CHANGE(19),
		WAREHOUSE_USE(20),
		START_SEMI(21),
		START_SURROUND(22),
		;
		private int value;
		LogType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(LogType v){
			return value == v.value;
		}
		public static LogType fromInt(int i){
			switch(i){
			case 1:
				return NONE;
			case 2:
				return OFF_ETC;
			case 3:
				return OFF_DEAD;
			case 4:
				return OFF_PEACE;
			case 5:
				return OFF_COMEBACK;
			case 6:
				return OFF_HALT;
			case 7:
				return OFF_RESTART;
			case 8:
				return OFF_BUY_FAIL;
			case 9:
				return OFF_MOVE_FAIL;
			case 10:
				return OFF_MAP_TIME_ERR;
			case 11:
				return OFF_WEIGHT_OVER;
			case 12:
				return START_ALL;
			case 13:
				return YETI_GET_ITEM;
			case 14:
				return SHOP_BUY;
			case 15:
				return SHOP_BUFF;
			case 16:
				return PVP_ATTACKED;
			case 17:
				return HP_MP_RESTORE;
			case 18:
				return RETURN;
			case 19:
				return MAP_CHANGE;
			case 20:
				return WAREHOUSE_USE;
			case 21:
				return START_SEMI;
			case 22:
				return START_SURROUND;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments LogType, %d", i));
			}
		}
	}
}
