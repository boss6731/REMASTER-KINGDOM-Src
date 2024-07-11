package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class EinhasadStatDetailsT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static EinhasadStatDetailsT newInstance(){
		return new EinhasadStatDetailsT();
	}
	private eEinhasadStatType _index;
	private int _value;
	private int _abilValue1;
	private int _abilValue2;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private EinhasadStatDetailsT(){
	}
	public eEinhasadStatType get_index(){
		return _index;
	}
	public void set_index(eEinhasadStatType val){
		_bit |= 0x1;
		_index = val;
	}
	public boolean has_index(){
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
	public int get_abilValue1(){
		return _abilValue1;
	}
	public void set_abilValue1(int val){
		_bit |= 0x4;
		_abilValue1 = val;
	}
	public boolean has_abilValue1(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_abilValue2(){
		return _abilValue2;
	}
	public void set_abilValue2(int val){
		_bit |= 0x8;
		_abilValue2 = val;
	}
	public boolean has_abilValue2(){
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
		if (has_index()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _index.toInt());
		}
		if (has_value()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _value);
		}
		if (has_abilValue1()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _abilValue1);
		}
		if (has_abilValue2()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _abilValue2);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_index()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_value()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_abilValue1()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_abilValue2()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_index()){
			output.writeEnum(1, _index.toInt());
		}
		if (has_value()){
			output.wirteInt32(2, _value);
		}
		if (has_abilValue1()){
			output.wirteInt32(3, _abilValue1);
		}
		if (has_abilValue2()){
			output.wirteInt32(4, _abilValue2);
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
					set_index(eEinhasadStatType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_value(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_abilValue1(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_abilValue2(input.readInt32());
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
		return new EinhasadStatDetailsT();
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
