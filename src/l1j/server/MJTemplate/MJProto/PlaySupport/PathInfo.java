package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class PathInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static PathInfo newInstance(){
		return new PathInfo();
	}
	private int _map;
	private Point _pos;
	private PathInfo.PathType _type;
	private String _strValue;
	private int _id;
	private BlessCode _bless;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PathInfo(){
	}
	public int get_map(){
		return _map;
	}
	public void set_map(int val){
		_bit |= 0x1;
		_map = val;
	}
	public boolean has_map(){
		return (_bit & 0x1) == 0x1;
	}
	public Point get_pos(){
		return _pos;
	}
	public void set_pos(Point val){
		_bit |= 0x2;
		_pos = val;
	}
	public boolean has_pos(){
		return (_bit & 0x2) == 0x2;
	}
	public PathInfo.PathType get_type(){
		return _type;
	}
	public void set_type(PathInfo.PathType val){
		_bit |= 0x4;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x4) == 0x4;
	}
	public String get_strValue(){
		return _strValue;
	}
	public void set_strValue(String val){
		_bit |= 0x8;
		_strValue = val;
	}
	public boolean has_strValue(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_id(){
		return _id;
	}
	public void set_id(int val){
		_bit |= 0x10;
		_id = val;
	}
	public boolean has_id(){
		return (_bit & 0x10) == 0x10;
	}
	public BlessCode get_bless(){
		return _bless;
	}
	public void set_bless(BlessCode val){
		_bit |= 0x20;
		_bless = val;
	}
	public boolean has_bless(){
		return (_bit & 0x20) == 0x20;
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
		if (has_map()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _map);
		}
		if (has_pos()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _pos);
		}
		if (has_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _type.toInt());
		}
		if (has_strValue()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _strValue);
		}
		if (has_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _id);
		}
		if (has_bless()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(6, _bless.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_map()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_pos()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_map()){
			output.wirteInt32(1, _map);
		}
		if (has_pos()){
			output.writeMessage(2, _pos);
		}
		if (has_type()){
			output.writeEnum(3, _type.toInt());
		}
		if (has_strValue()){
			output.writeString(4, _strValue);
		}
		if (has_id()){
			output.wirteInt32(5, _id);
		}
		if (has_bless()){
			output.writeEnum(6, _bless.toInt());
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
					set_map(input.readInt32());
					break;
				}
				case 0x00000012:{
					set_pos((Point)input.readMessage(Point.newInstance()));
					break;
				}
				case 0x00000018:{
					set_type(PathInfo.PathType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000022:{
					set_strValue(input.readString());
					break;
				}
				case 0x00000028:{
					set_id(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_bless(BlessCode.fromInt(input.readEnum()));
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
		return new PathInfo();
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
	public enum PathType{
		PATH(0),
		TELEPORT(1),
		DIALOG(2),
		HACTION(3),
		ITEM(4),
		MAPTELEPORT(5),
		OMANTELEPORT(6),
		MAX(7),
		;
		private int value;
		PathType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(PathType v){
			return value == v.value;
		}
		public static PathType fromInt(int i){
			switch(i){
			case 0:
				return PATH;
			case 1:
				return TELEPORT;
			case 2:
				return DIALOG;
			case 3:
				return HACTION;
			case 4:
				return ITEM;
			case 5:
				return MAPTELEPORT;
			case 6:
				return OMANTELEPORT;
			case 7:
				return MAX;
			default:
				throw new IllegalArgumentException(String.format("參數 PathType 無效，%d", i));
			}
		}
	}
}
