package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class UsingCondition implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static UsingCondition newInstance(){
		return new UsingCondition();
	}
	private UsingCondition.StateType _state;
	private int _value;
	private boolean _activate;
	private UsingCondition.Operator _operator;
	private java.util.LinkedList<String> _strValue;
	private java.util.LinkedList<SlotInfo> _itemInfo;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private UsingCondition(){
		set_state(UsingCondition.StateType.NONE);
		set_operator(UsingCondition.Operator.GREATER_EQUAL);
	}
	public UsingCondition.StateType get_state(){
		return _state;
	}
	public void set_state(UsingCondition.StateType val){
		_bit |= 0x1;
		_state = val;
	}
	public boolean has_state(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_value(){
		return _value;
	}
	public void set_value(int val){
		_bit |= 0x2;
		_value = val;
	}
	public boolean has_value(){
		return (_bit & 0x2) == 0x2;
	}
	public boolean get_activate(){
		return _activate;
	}
	public void set_activate(boolean val){
		_bit |= 0x4;
		_activate = val;
	}
	public boolean has_activate(){
		return (_bit & 0x4) == 0x4;
	}
	public UsingCondition.Operator get_operator(){
		return _operator;
	}
	public void set_operator(UsingCondition.Operator val){
		_bit |= 0x8;
		_operator = val;
	}
	public boolean has_operator(){
		return (_bit & 0x8) == 0x8;
	}
	public java.util.LinkedList<String> get_strValue(){
		return _strValue;
	}
	public void add_strValue(String val){
		if(!has_strValue()){
			_strValue = new java.util.LinkedList<String>();
			_bit |= 0x10;
		}
		_strValue.add(val);
	}
	public boolean has_strValue(){
		return (_bit & 0x10) == 0x10;
	}
	public java.util.LinkedList<SlotInfo> get_itemInfo(){
		return _itemInfo;
	}
	public void add_itemInfo(SlotInfo val){
		if(!has_itemInfo()){
			_itemInfo = new java.util.LinkedList<SlotInfo>();
			_bit |= 0x20;
		}
		_itemInfo.add(val);
	}
	public boolean has_itemInfo(){
		return (_bit & 0x20) == 0x20;
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
		if (has_state()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _state.toInt());
		}
		if (has_value()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _value);
		}
		if (has_activate()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _activate);
		}
		if (has_operator()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _operator.toInt());
		}
		if (has_strValue()){
			for(String val : _strValue){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(5, val);
			}
		}
		if (has_itemInfo()){
			for(SlotInfo val : _itemInfo){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_state()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_value()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_activate()){
			_memorizedIsInitialized = -1;
			return false;
		}
//		if (has_strValue()){
//			for(String val : _strValue){
//				if (!val.isInitialized()){
//					_memorizedIsInitialized = -1;
//					return false;
//				}
//			}
//		}
		if (has_itemInfo()){
			for(SlotInfo val : _itemInfo){
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
		if (has_state()){
			output.writeEnum(1, _state.toInt());
		}
		if (has_value()){
			output.writeUInt32(2, _value);
		}
		if (has_activate()){
			output.writeBool(3, _activate);
		}
		if (has_operator()){
			output.writeEnum(4, _operator.toInt());
		}
		if (has_strValue()){
			for (String val : _strValue){
				output.writeString(5, val);
			}
		}
		if (has_itemInfo()){
			for (SlotInfo val : _itemInfo){
				output.writeMessage(6, val);
			}
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
					set_state(UsingCondition.StateType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_value(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_activate(input.readBool());
					break;
				}
				case 0x00000020:{
					set_operator(UsingCondition.Operator.fromInt(input.readEnum()));
					break;
				}
				case 0x0000002A:{
					add_strValue(input.readString());
					break;
				}
				case 0x00000032:{
					add_itemInfo((SlotInfo)input.readMessage(SlotInfo.newInstance()));
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
		return new UsingCondition();
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
	public enum StateType{
		HP(0),
		MP(1),
		DETOX(2),
		SINK(3),
		BUFF(4),
		NONCOMBAT(5),
		COMBATTIME(6),
		PVP(7),
		ATTACKNUM(8),
		ITEM(9),
		PSSPLAY(10),
		WEIGHT(11),
		INVENSIZE(12),
		DIE(13),
		TIME(14),
		RETURN(15),
		COUNT(16),
		PSSSTOP(17),
		HUNGRY(18),
		PATHREPEAT(19),
		TOTALPATHREPEAT(20),
		NONE(21),
		;
		private int value;
		StateType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(StateType v){
			return value == v.value;
		}
		public static StateType fromInt(int i){
			switch(i){
			case 0:
				return HP;
			case 1:
				return MP;
			case 2:
				return DETOX;
			case 3:
				return SINK;
			case 4:
				return BUFF;
			case 5:
				return NONCOMBAT;
			case 6:
				return COMBATTIME;
			case 7:
				return PVP;
			case 8:
				return ATTACKNUM;
			case 9:
				return ITEM;
			case 10:
				return PSSPLAY;
			case 11:
				return WEIGHT;
			case 12:
				return INVENSIZE;
			case 13:
				return DIE;
			case 14:
				return TIME;
			case 15:
				return RETURN;
			case 16:
				return COUNT;
			case 17:
				return PSSSTOP;
			case 18:
				return HUNGRY;
			case 19:
				return PATHREPEAT;
			case 20:
				return TOTALPATHREPEAT;
			case 21:
				return NONE;
			default:
				throw new IllegalArgumentException(String.format("參數 StateType 無效，%d", i));
			}
		}
	}
	public enum Operator{
		GREATER_EQUAL(0),
		EQUAL(1),
		LESS_EQUAL(2),
		;
		private int value;
		Operator(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(Operator v){
			return value == v.value;
		}
		public static Operator fromInt(int i){
			switch(i){
			case 0:
				return GREATER_EQUAL;
			case 1:
				return EQUAL;
			case 2:
				return LESS_EQUAL;
			default:
				throw new IllegalArgumentException(String.format("無效參數運算符，%d", i));
			}
		}
	}
}
