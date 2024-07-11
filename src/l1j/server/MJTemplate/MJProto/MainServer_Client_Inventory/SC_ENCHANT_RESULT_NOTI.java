package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.(해당 프로토자바 삭제됨 10-28)
public class SC_ENCHANT_RESULT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ENCHANT_RESULT_NOTI newInstance(){
		return new SC_ENCHANT_RESULT_NOTI();
	}
	private SC_ENCHANT_RESULT_NOTI.ENCHANT_TYPE _enchant_type;
	private SC_ENCHANT_RESULT_NOTI.ENCHANT_RESULT_TYPE _enchant_result;
	private SC_ENCHANT_RESULT_NOTI.BLESS_CODE _bless_code;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ENCHANT_RESULT_NOTI(){
	}
	public SC_ENCHANT_RESULT_NOTI.ENCHANT_TYPE get_enchant_type(){
		return _enchant_type;
	}
	public void set_enchant_type(SC_ENCHANT_RESULT_NOTI.ENCHANT_TYPE val){
		_bit |= 0x1;
		_enchant_type = val;
	}
	public boolean has_enchant_type(){
		return (_bit & 0x1) == 0x1;
	}
	public SC_ENCHANT_RESULT_NOTI.ENCHANT_RESULT_TYPE get_enchant_result(){
		return _enchant_result;
	}
	public void set_enchant_result(SC_ENCHANT_RESULT_NOTI.ENCHANT_RESULT_TYPE val){
		_bit |= 0x2;
		_enchant_result = val;
	}
	public boolean has_enchant_result(){
		return (_bit & 0x2) == 0x2;
	}
	public SC_ENCHANT_RESULT_NOTI.BLESS_CODE get_bless_code(){
		return _bless_code;
	}
	public void set_bless_code(SC_ENCHANT_RESULT_NOTI.BLESS_CODE val){
		_bit |= 0x4;
		_bless_code = val;
	}
	public boolean has_bless_code(){
		return (_bit & 0x4) == 0x4;
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
		if (has_enchant_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _enchant_type.toInt());
		}
		if (has_enchant_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _enchant_result.toInt());
		}
		if (has_bless_code()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _bless_code.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_enchant_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enchant_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bless_code()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_enchant_type()){
			output.writeEnum(1, _enchant_type.toInt());
		}
		if (has_enchant_result()){
			output.writeEnum(2, _enchant_result.toInt());
		}
		if (has_bless_code()){
			output.writeEnum(3, _bless_code.toInt());
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
					set_enchant_type(SC_ENCHANT_RESULT_NOTI.ENCHANT_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_enchant_result(SC_ENCHANT_RESULT_NOTI.ENCHANT_RESULT_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000018:{
					set_bless_code(SC_ENCHANT_RESULT_NOTI.BLESS_CODE.fromInt(input.readEnum()));
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
		return new SC_ENCHANT_RESULT_NOTI();
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
	public enum ENCHANT_TYPE{
		ENCHANT_TYPE_WEAPON(1),
		ENCHANT_TYPE_ARMOR(2),
		ENCHANT_TYPE_ACCESSORY(3),
		ENCHANT_TYPE_UNIDENTIFIED(4),
		;
		private int value;
		ENCHANT_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ENCHANT_TYPE v){
			return value == v.value;
		}
		public static ENCHANT_TYPE fromInt(int i){
			switch(i){
			case 1:
				return ENCHANT_TYPE_WEAPON;
			case 2:
				return ENCHANT_TYPE_ARMOR;
			case 3:
				return ENCHANT_TYPE_ACCESSORY;
			case 4:
				return ENCHANT_TYPE_UNIDENTIFIED;
			default:
				throw new IllegalArgumentException(String.format("無效的參數 arguments ENCHANT_TYPE, %d", i));
			}
		}
	}
	public enum ENCHANT_RESULT_TYPE{
		ENCHANT_FAIL(1),
		ENCHANT_SUCCESS(2),
		ENCHANT_NOTHING_HAPPEN(3),
		ENCHANT_DIFFERENT_ELEMENT_TYPE(4),
		ENCHANT_CANT_ENCHANT_ANYMORE(5),
		ENCHANT_TARGET_DOESE_NOT_ELEMENTALLY_ENCHANTED(6),
		ENCHANT_SAME_ELEMENT_ENCHANT_TYPE(7),
		ENCHANT_CANT_CHANGE_ELEMENT_TYPE(8),
		;
		private int value;
		ENCHANT_RESULT_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ENCHANT_RESULT_TYPE v){
			return value == v.value;
		}
		public static ENCHANT_RESULT_TYPE fromInt(int i){
			switch(i){
			case 1:
				return ENCHANT_FAIL;
			case 2:
				return ENCHANT_SUCCESS;
			case 3:
				return ENCHANT_NOTHING_HAPPEN;
			case 4:
				return ENCHANT_DIFFERENT_ELEMENT_TYPE;
			case 5:
				return ENCHANT_CANT_ENCHANT_ANYMORE;
			case 6:
				return ENCHANT_TARGET_DOESE_NOT_ELEMENTALLY_ENCHANTED;
			case 7:
				return ENCHANT_SAME_ELEMENT_ENCHANT_TYPE;
			case 8:
				return ENCHANT_CANT_CHANGE_ELEMENT_TYPE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ENCHANT_RESULT_TYPE, %d", i));
			}
		}
	}
	public enum BLESS_CODE{
		BC_BLESSED(0),
		BC_NORMAL(1),
		BC_CURSED(2),
		BC_UNIDENTIFIED(3),
		;
		private int value;
		BLESS_CODE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(BLESS_CODE v){
			return value == v.value;
		}
		public static BLESS_CODE fromInt(int i){
			switch(i){
			case 0:
				return BC_BLESSED;
			case 1:
				return BC_NORMAL;
			case 2:
				return BC_CURSED;
			case 3:
				return BC_UNIDENTIFIED;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments BLESS_CODE, %d", i));
			}
		}
	}
}
