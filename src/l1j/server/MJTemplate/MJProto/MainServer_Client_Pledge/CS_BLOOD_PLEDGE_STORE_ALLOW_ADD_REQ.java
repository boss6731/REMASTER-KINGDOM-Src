package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;


// TODO : �ڵ����� ������ PROTO �ڵ��Դϴ�. made by Nature.
public class CS_BLOOD_PLEDGE_STORE_ALLOW_ADD_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_BLOOD_PLEDGE_STORE_ALLOW_ADD_REQ newInstance(){
		return new CS_BLOOD_PLEDGE_STORE_ALLOW_ADD_REQ();
	}
	private String _add_user_name;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_BLOOD_PLEDGE_STORE_ALLOW_ADD_REQ(){
	}
	public String get_add_user_name(){
		return _add_user_name;
	}
	public void set_add_user_name(String val){
		_bit |= 0x1;
		_add_user_name = val;
	}
	public boolean has_add_user_name(){
		return (_bit & 0x1) == 0x1;
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
		if (has_add_user_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _add_user_name);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_add_user_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_add_user_name()){
			output.writeString(1, _add_user_name);
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
				case 0x0000000A:{
					set_add_user_name(input.readString());
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

			// TODO : �Ʒ����� ó�� �ڵ带 �����Ͻʽÿ�. made by Nature.
			
			System.out.print("�߰�");
			SC_BLOOD_PLEDGE_STORE_ALLOW_ADD_ACK.send(pc, get_add_user_name());
		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_BLOOD_PLEDGE_STORE_ALLOW_ADD_REQ();
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
