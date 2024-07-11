package l1j.server.MJTemplate.MJProto.MainServer_Client_Spell;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.ePolymorphAnonymityType;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_POLYMORPH_ANONYMITY_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_POLYMORPH_ANONYMITY_ACK newInstance(){
		return new SC_POLYMORPH_ANONYMITY_ACK();
	}
	private ePolymorphAnonymityType _anonymity_type;
	private boolean _result_type;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_POLYMORPH_ANONYMITY_ACK(){
	}
	public ePolymorphAnonymityType get_anonymity_type(){
		return _anonymity_type;
	}
	public void set_anonymity_type(ePolymorphAnonymityType val){
		_bit |= 0x1;
		_anonymity_type = val;
	}
	public boolean has_anonymity_type(){
		return (_bit & 0x1) == 0x1;
	}
	public boolean get_result_type(){
		return _result_type;
	}
	public void set_result_type(boolean val){
		_bit |= 0x2;
		_result_type = val;
	}
	public boolean has_result_type(){
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
		if (has_anonymity_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _anonymity_type.toInt());
		}
		if (has_result_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _result_type);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_anonymity_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_result_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_anonymity_type()){
			output.writeEnum(1, _anonymity_type.toInt());
		}
		if (has_result_type()){
			output.writeBool(2, _result_type);
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
					set_anonymity_type(ePolymorphAnonymityType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_result_type(input.readBool());
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
		return new SC_POLYMORPH_ANONYMITY_ACK();
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
