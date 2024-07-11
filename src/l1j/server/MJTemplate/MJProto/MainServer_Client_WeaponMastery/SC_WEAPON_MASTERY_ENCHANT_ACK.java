package l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_WEAPON_MASTERY_ENCHANT_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_WEAPON_MASTERY_ENCHANT_ACK newInstance(){
		return new SC_WEAPON_MASTERY_ENCHANT_ACK();
	}
	private int _weapon_type;
	private int _mastery_slot_id;
	private int _enchant_value;
	private SC_WEAPON_MASTERY_ENCHANT_ACK.eResult _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_WEAPON_MASTERY_ENCHANT_ACK(){
	}
	public int get_weapon_type(){
		return _weapon_type;
	}
	public void set_weapon_type(int val){
		_bit |= 0x1;
		_weapon_type = val;
	}
	public boolean has_weapon_type(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_mastery_slot_id(){
		return _mastery_slot_id;
	}
	public void set_mastery_slot_id(int val){
		_bit |= 0x2;
		_mastery_slot_id = val;
	}
	public boolean has_mastery_slot_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_enchant_value(){
		return _enchant_value;
	}
	public void set_enchant_value(int val){
		_bit |= 0x4;
		_enchant_value = val;
	}
	public boolean has_enchant_value(){
		return (_bit & 0x4) == 0x4;
	}
	public SC_WEAPON_MASTERY_ENCHANT_ACK.eResult get_result(){
		return _result;
	}
	public void set_result(SC_WEAPON_MASTERY_ENCHANT_ACK.eResult val){
		_bit |= 0x8;
		_result = val;
	}
	public boolean has_result(){
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
		if (has_weapon_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _weapon_type);
		}
		if (has_mastery_slot_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _mastery_slot_id);
		}
		if (has_enchant_value()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _enchant_value);
		}
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _result.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_weapon_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_mastery_slot_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enchant_value()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_weapon_type()){
			output.wirteInt32(1, _weapon_type);
		}
		if (has_mastery_slot_id()){
			output.wirteInt32(2, _mastery_slot_id);
		}
		if (has_enchant_value()){
			output.wirteInt32(3, _enchant_value);
		}
		if (has_result()){
			output.writeEnum(4, _result.toInt());
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
					set_weapon_type(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_mastery_slot_id(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_enchant_value(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_result(SC_WEAPON_MASTERY_ENCHANT_ACK.eResult.fromInt(input.readEnum()));
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
		return new SC_WEAPON_MASTERY_ENCHANT_ACK();
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
	public enum eResult {
		// 附魔成功
		ENCHANT_SUCCESS(0),
		// 附魔失敗
		ENCHANT_FAIL(1),
		// 附魔錯誤
		ENCHANT_ERROR(2),

		;

		private int value;

		// 枚舉類型的構造函數，用於初始化枚舉值
		eResult(int val) {
			value = val;
		}

		// 返回枚舉值的整數表示
		public int toInt() {
			return value;
		}

		// 比較當前枚舉值與其他枚舉值是否相等
		public boolean equals(eResult v) {
			return value == v.value;
		}

		// 通過整數值返回對應的枚舉類型。如果輸入的整數值無效，則拋出 IllegalArgumentException
		public static eResult fromInt(int i) {
			switch (i) {
				case 0:
					return ENCHANT_SUCCESS; // 附魔成功
				case 1:
					return ENCHANT_FAIL; // 附魔失敗
				case 2:
					return ENCHANT_ERROR; // 附魔錯誤
				default:
					throw new IllegalArgumentException(String.format("無效的 eResult 參數: %d", i));
			}
		}
	}
