package l1j.server.MJTemplate.MJProto.MainServer_Client_PolymorphBook;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_POLYMORPHBOOK_LIST_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_POLYMORPHBOOK_LIST_NOTI newInstance(){
		return new SC_POLYMORPHBOOK_LIST_NOTI();
	}
	private java.util.LinkedList<PolymorphBookInfo> _Info;
	private boolean _Finished;
	private int _BuffBookId;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_POLYMORPHBOOK_LIST_NOTI(){
	}
	public java.util.LinkedList<PolymorphBookInfo> get_Info(){
		return _Info;
	}
	public void add_Info(PolymorphBookInfo val){
		if(!has_Info()){
			_Info = new java.util.LinkedList<PolymorphBookInfo>();
			_bit |= 0x1;
		}
		_Info.add(val);
	}
	public boolean has_Info(){
		return (_bit & 0x1) == 0x1;
	}
	public boolean get_Finished(){
		return _Finished;
	}
	public void set_Finished(boolean val){
		_bit |= 0x2;
		_Finished = val;
	}
	public boolean has_Finished(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_BuffBookId(){
		return _BuffBookId;
	}
	public void set_BuffBookId(int val){
		_bit |= 0x4;
		_BuffBookId = val;
	}
	public boolean has_BuffBookId(){
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
		if (has_Info()){
			for(PolymorphBookInfo val : _Info){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_Finished()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _Finished);
		}
		if (has_BuffBookId()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _BuffBookId);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_Info()){
			for(PolymorphBookInfo val : _Info){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_Finished()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_Info()){
			for (PolymorphBookInfo val : _Info){
				output.writeMessage(1, val);
			}
		}
		if (has_Finished()){
			output.writeBool(2, _Finished);
		}
		if (has_BuffBookId()){
			output.wirteInt32(3, _BuffBookId);
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
					add_Info((PolymorphBookInfo)input.readMessage(PolymorphBookInfo.newInstance()));
					break;
				}
				case 0x00000010:{
					set_Finished(input.readBool());
					break;
				}
				case 0x00000018:{
					set_BuffBookId(input.readInt32());
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
		return new SC_POLYMORPHBOOK_LIST_NOTI();
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
