package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class RecommendGroup implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static RecommendGroup newInstance(){
		return new RecommendGroup();
	}
	private int _MinLevel;
	private int _MaxLevel;
	private java.util.LinkedList<PSSConfig> _PSClassDatas;
	private String _Version;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private RecommendGroup(){
	}
	public int get_MinLevel(){
		return _MinLevel;
	}
	public void set_MinLevel(int val){
		_bit |= 0x1;
		_MinLevel = val;
	}
	public boolean has_MinLevel(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_MaxLevel(){
		return _MaxLevel;
	}
	public void set_MaxLevel(int val){
		_bit |= 0x2;
		_MaxLevel = val;
	}
	public boolean has_MaxLevel(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<PSSConfig> get_PSClassDatas(){
		return _PSClassDatas;
	}
	public void add_PSClassDatas(PSSConfig val){
		if(!has_PSClassDatas()){
			_PSClassDatas = new java.util.LinkedList<PSSConfig>();
			_bit |= 0x4;
		}
		_PSClassDatas.add(val);
	}
	public boolean has_PSClassDatas(){
		return (_bit & 0x4) == 0x4;
	}
	public String get_Version(){
		return _Version;
	}
	public void set_Version(String val){
		_bit |= 0x8;
		_Version = val;
	}
	public boolean has_Version(){
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
		if (has_MinLevel()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _MinLevel);
		}
		if (has_MaxLevel()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _MaxLevel);
		}
		if (has_PSClassDatas()){
			for(PSSConfig val : _PSClassDatas){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		if (has_Version()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _Version);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_MinLevel()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_MaxLevel()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_PSClassDatas()){
			for(PSSConfig val : _PSClassDatas){
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
		if (has_MinLevel()){
			output.writeUInt32(1, _MinLevel);
		}
		if (has_MaxLevel()){
			output.writeUInt32(2, _MaxLevel);
		}
		if (has_PSClassDatas()){
			for (PSSConfig val : _PSClassDatas){
				output.writeMessage(3, val);
			}
		}
		if (has_Version()){
			output.writeString(4, _Version);
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
					set_MinLevel(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_MaxLevel(input.readUInt32());
					break;
				}
				case 0x0000001A:{
					add_PSClassDatas((PSSConfig)input.readMessage(PSSConfig.newInstance()));
					break;
				}
				case 0x00000022:{
					set_Version(input.readString());
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
		return new RecommendGroup();
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
