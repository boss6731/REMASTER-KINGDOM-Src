package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_EXTEND_CHAR_SLOT_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_EXTEND_CHAR_SLOT_ACK newInstance(){
		return new SC_EXTEND_CHAR_SLOT_ACK();
	}
	private int _result_code;
	private String _use_item_char;
	private int _additional_msg_code;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_EXTEND_CHAR_SLOT_ACK(){
	}
	public int get_result_code(){
		return _result_code;
	}
	public void set_result_code(int val){
		_bit |= 0x1;
		_result_code = val;
	}
	public boolean has_result_code(){
		return (_bit & 0x1) == 0x1;
	}
	public String get_use_item_char(){
		return _use_item_char;
	}
	public void set_use_item_char(String val){
		_bit |= 0x2;
		_use_item_char = val;
	}
	public boolean has_use_item_char(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_additional_msg_code(){
		return _additional_msg_code;
	}
	public void set_additional_msg_code(int val){
		_bit |= 0x4;
		_additional_msg_code = val;
	}
	public boolean has_additional_msg_code(){
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
		if (has_result_code()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _result_code);
		}
		if (has_use_item_char()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _use_item_char);
		}
		if (has_additional_msg_code()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _additional_msg_code);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result_code()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_result_code()){
			output.wirteInt32(1, _result_code);
		}
		if (has_use_item_char()){
			output.writeString(2, _use_item_char);
		}
		if (has_additional_msg_code()){
			output.wirteInt32(3, _additional_msg_code);
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
					set_result_code(input.readInt32());
					break;
				}
				case 0x00000012:{
					set_use_item_char(input.readString());
					break;
				}
				case 0x00000018:{
					set_additional_msg_code(input.readInt32());
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
		return new SC_EXTEND_CHAR_SLOT_ACK();
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
