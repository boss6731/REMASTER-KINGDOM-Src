package l1j.server.MJTemplate.MJProto.MainServer_Client_Spell;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CharacterClass;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.ePolymorphAnonymityType;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_POLYMORPH_ANONYMITY_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_POLYMORPH_ANONYMITY_NOTI newInstance(){
		return new SC_POLYMORPH_ANONYMITY_NOTI();
	}
	private int _object_id;
	private ePolymorphAnonymityType _anonymity_type;
	private String _real_name;
	private String _anonymity_name;
	private CharacterClass _char_class;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_POLYMORPH_ANONYMITY_NOTI(){
	}
	public int get_object_id(){
		return _object_id;
	}
	public void set_object_id(int val){
		_bit |= 0x1;
		_object_id = val;
	}
	public boolean has_object_id(){
		return (_bit & 0x1) == 0x1;
	}
	public ePolymorphAnonymityType get_anonymity_type(){
		return _anonymity_type;
	}
	public void set_anonymity_type(ePolymorphAnonymityType val){
		_bit |= 0x2;
		_anonymity_type = val;
	}
	public boolean has_anonymity_type(){
		return (_bit & 0x2) == 0x2;
	}
	public String get_real_name(){
		return _real_name;
	}
	public void set_real_name(String val){
		_bit |= 0x4;
		_real_name = val;
	}
	public boolean has_real_name(){
		return (_bit & 0x4) == 0x4;
	}
	public String get_anonymity_name(){
		return _anonymity_name;
	}
	public void set_anonymity_name(String val){
		_bit |= 0x8;
		_anonymity_name = val;
	}
	public boolean has_anonymity_name(){
		return (_bit & 0x8) == 0x8;
	}
	public CharacterClass get_char_class(){
		return _char_class;
	}
	public void set_char_class(CharacterClass val){
		_bit |= 0x10;
		_char_class = val;
	}
	public boolean has_char_class(){
		return (_bit & 0x10) == 0x10;
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
		if (has_object_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _object_id);
		}
		if (has_anonymity_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _anonymity_type.toInt());
		}
		if (has_real_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _real_name);
		}
		if (has_anonymity_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _anonymity_name);
		}
		if (has_char_class()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(5, _char_class.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_object_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_anonymity_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_object_id()){
			output.writeUInt32(1, _object_id);
		}
		if (has_anonymity_type()){
			output.writeEnum(2, _anonymity_type.toInt());
		}
		if (has_real_name()){
			output.writeString(3, _real_name);
		}
		if (has_anonymity_name()){
			output.writeString(4, _anonymity_name);
		}
		if (has_char_class()){
			output.writeEnum(5, _char_class.toInt());
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
					set_object_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_anonymity_type(ePolymorphAnonymityType.fromInt(input.readEnum()));
					break;
				}
				case 0x0000001A:{
					set_real_name(input.readString());
					break;
				}
				case 0x00000022:{
					set_anonymity_name(input.readString());
					break;
				}
				case 0x00000028:{
					set_char_class(CharacterClass.fromInt(input.readEnum()));
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
		return new SC_POLYMORPH_ANONYMITY_NOTI();
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
