package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class PathRecord implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static PathRecord newInstance(){
		return new PathRecord();
	}
	private String _name;
	private java.util.LinkedList<PathInfo> _pathlist;
	private java.util.LinkedList<PathInfo> _attackpathlist;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PathRecord(){
	}
	public String get_name(){
		return _name;
	}
	public void set_name(String val){
		_bit |= 0x1;
		_name = val;
	}
	public boolean has_name(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<PathInfo> get_pathlist(){
		return _pathlist;
	}
	public void add_pathlist(PathInfo val){
		if(!has_pathlist()){
			_pathlist = new java.util.LinkedList<PathInfo>();
			_bit |= 0x2;
		}
		_pathlist.add(val);
	}
	public boolean has_pathlist(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<PathInfo> get_attackpathlist(){
		return _attackpathlist;
	}
	public void add_attackpathlist(PathInfo val){
		if(!has_attackpathlist()){
			_attackpathlist = new java.util.LinkedList<PathInfo>();
			_bit |= 0x4;
		}
		_attackpathlist.add(val);
	}
	public boolean has_attackpathlist(){
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
		if (has_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _name);
		}
		if (has_pathlist()){
			for(PathInfo val : _pathlist){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		if (has_attackpathlist()){
			for(PathInfo val : _attackpathlist){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_pathlist()){
			for(PathInfo val : _pathlist){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_attackpathlist()){
			for(PathInfo val : _attackpathlist){
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
		if (has_name()){
			output.writeString(1, _name);
		}
		if (has_pathlist()){
			for (PathInfo val : _pathlist){
				output.writeMessage(2, val);
			}
		}
		if (has_attackpathlist()){
			for (PathInfo val : _attackpathlist){
				output.writeMessage(3, val);
			}
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
					set_name(input.readString());
					break;
				}
				case 0x00000012:{
					add_pathlist((PathInfo)input.readMessage(PathInfo.newInstance()));
					break;
				}
				case 0x0000001A:{
					add_attackpathlist((PathInfo)input.readMessage(PathInfo.newInstance()));
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
		return new PathRecord();
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
