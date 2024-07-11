package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class PledgeInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static PledgeInfo newInstance(){
		return new PledgeInfo();
	}
	private int _pledge_id;
	private String _pledge_name;
	private String _master_name;
	private int _emblem_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PledgeInfo(){
	}
	public int get_pledge_id(){
		return _pledge_id;
	}
	public void set_pledge_id(int val){
		_bit |= 0x1;
		_pledge_id = val;
	}
	public boolean has_pledge_id(){
		return (_bit & 0x1) == 0x1;
	}
	public String get_pledge_name(){
		return _pledge_name;
	}
	public void set_pledge_name(String val){
		_bit |= 0x2;
		_pledge_name = val;
	}
	public boolean has_pledge_name(){
		return (_bit & 0x2) == 0x2;
	}
	public String get_master_name(){
		return _master_name;
	}
	public void set_master_name(String val){
		_bit |= 0x4;
		_master_name = val;
	}
	public boolean has_master_name(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_emblem_id(){
		return _emblem_id;
	}
	public void set_emblem_id(int val){
		_bit |= 0x8;
		_emblem_id = val;
	}
	public boolean has_emblem_id(){
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
		if (has_pledge_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _pledge_id);
		}
		if (has_pledge_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _pledge_name);
		}
		if (has_master_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _master_name);
		}
		if (has_emblem_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _emblem_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_pledge_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_pledge_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_master_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_emblem_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_pledge_id()){
			output.writeUInt32(1, _pledge_id);
		}
		if (has_pledge_name()){
			output.writeString(2, _pledge_name);
		}
		if (has_master_name()){
			output.writeString(3, _master_name);
		}
		if (has_emblem_id()){
			output.writeUInt32(4, _emblem_id);
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
					set_pledge_id(input.readUInt32());
					break;
				}
				case 0x00000012:{
					set_pledge_name(input.readString());
					break;
				}
				case 0x0000001A:{
					set_master_name(input.readString());
					break;
				}
				case 0x00000020:{
					set_emblem_id(input.readUInt32());
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new PledgeInfo();
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
