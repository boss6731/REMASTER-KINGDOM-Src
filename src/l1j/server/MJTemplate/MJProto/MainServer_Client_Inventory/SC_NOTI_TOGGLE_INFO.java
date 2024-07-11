package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_NOTI_TOGGLE_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_NOTI_TOGGLE_INFO newInstance(){
		return new SC_NOTI_TOGGLE_INFO();
	}
	private eToggleInfoType _toggle_info_type;
	private boolean _is_enable;
	private boolean _by_user;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_NOTI_TOGGLE_INFO(){
	}
	public eToggleInfoType get_toggle_info_type(){
		return _toggle_info_type;
	}
	public void set_toggle_info_type(eToggleInfoType val){
		_bit |= 0x1;
		_toggle_info_type = val;
	}
	public boolean has_toggle_info_type(){
		return (_bit & 0x1) == 0x1;
	}
	public boolean get_is_enable(){
		return _is_enable;
	}
	public void set_is_enable(boolean val){
		_bit |= 0x2;
		_is_enable = val;
	}
	public boolean has_is_enable(){
		return (_bit & 0x2) == 0x2;
	}
	public boolean get_by_user(){
		return _by_user;
	}
	public void set_by_user(boolean val){
		_bit |= 0x4;
		_by_user = val;
	}
	public boolean has_by_user(){
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
		if (has_toggle_info_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _toggle_info_type.toInt());
		}
		if (has_is_enable()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_enable);
		}
		if (has_by_user()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _by_user);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_toggle_info_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_is_enable()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_by_user()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_toggle_info_type()){
			output.writeEnum(1, _toggle_info_type.toInt());
		}
		if (has_is_enable()){
			output.writeBool(2, _is_enable);
		}
		if (has_by_user()){
			output.writeBool(3, _by_user);
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
					set_toggle_info_type(eToggleInfoType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_is_enable(input.readBool());
					break;
				}
				case 0x00000018:{
					set_by_user(input.readBool());
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
		return new SC_NOTI_TOGGLE_INFO();
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
