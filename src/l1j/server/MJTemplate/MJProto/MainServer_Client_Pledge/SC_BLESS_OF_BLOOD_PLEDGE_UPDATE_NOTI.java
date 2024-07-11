package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BLESS_OF_BLOOD_PLEDGE_UPDATE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_BLESS_OF_BLOOD_PLEDGE_UPDATE_NOTI newInstance(){
		return new SC_BLESS_OF_BLOOD_PLEDGE_UPDATE_NOTI();
	}
	private java.util.LinkedList<WorldInfo> _world_candidate;
	private boolean _can_shuffle;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLESS_OF_BLOOD_PLEDGE_UPDATE_NOTI(){
	}
	public java.util.LinkedList<WorldInfo> get_world_candidate(){
		return _world_candidate;
	}
	public void add_world_candidate(WorldInfo val){
		if(!has_world_candidate()){
			_world_candidate = new java.util.LinkedList<WorldInfo>();
			_bit |= 0x1;
		}
		_world_candidate.add(val);
	}
	public boolean has_world_candidate(){
		return (_bit & 0x1) == 0x1;
	}
	public boolean get_can_shuffle(){
		return _can_shuffle;
	}
	public void set_can_shuffle(boolean val){
		_bit |= 0x2;
		_can_shuffle = val;
	}
	public boolean has_can_shuffle(){
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
		if (has_world_candidate()){
			for(WorldInfo val : _world_candidate){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_can_shuffle()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _can_shuffle);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_world_candidate()){
			for(WorldInfo val : _world_candidate){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_world_candidate()){
			for (WorldInfo val : _world_candidate){
				output.writeMessage(1, val);
			}
		}
		if (has_can_shuffle()){
			output.writeBool(2, _can_shuffle);
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
					add_world_candidate((WorldInfo)input.readMessage(WorldInfo.newInstance()));
					break;
				}
				case 0x00000010:{
					set_can_shuffle(input.readBool());
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
		return new SC_BLESS_OF_BLOOD_PLEDGE_UPDATE_NOTI();
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
