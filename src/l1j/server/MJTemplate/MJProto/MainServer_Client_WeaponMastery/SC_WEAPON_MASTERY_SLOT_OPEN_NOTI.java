package l1j.server.MJTemplate.MJProto.MainServer_Client_WeaponMastery;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_WEAPON_MASTERY_SLOT_OPEN_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_WEAPON_MASTERY_SLOT_OPEN_NOTI newInstance(){
		return new SC_WEAPON_MASTERY_SLOT_OPEN_NOTI();
	}
	private int _weapon_type;
	private int _mastery_slot_id;
	private int _enchant_value;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_WEAPON_MASTERY_SLOT_OPEN_NOTI(){
		set_enchant_value(0);
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
		return new SC_WEAPON_MASTERY_SLOT_OPEN_NOTI();
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
