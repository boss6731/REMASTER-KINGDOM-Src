package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SlotInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SlotInfo newInstance(){
		return new SlotInfo();
	}
	private String _desc;
	private int _icon;
	private SlotInfo.SlotType _slotType;
	private int _count;
	private BlessCode _bless;
	private MagicDollPotential _potential;
	private String _cuid;
	private int _dataId;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SlotInfo(){
		set_icon(-1);
		set_slotType(SlotInfo.SlotType.UNKNOWN);
		set_bless(BlessCode.NORMAL);
	}
	public String get_desc(){
		return _desc;
	}
	public void set_desc(String val){
		_bit |= 0x1;
		_desc = val;
	}
	public boolean has_desc(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_icon(){
		return _icon;
	}
	public void set_icon(int val){
		_bit |= 0x2;
		_icon = val;
	}
	public boolean has_icon(){
		return (_bit & 0x2) == 0x2;
	}
	public SlotInfo.SlotType get_slotType(){
		return _slotType;
	}
	public void set_slotType(SlotInfo.SlotType val){
		_bit |= 0x4;
		_slotType = val;
	}
	public boolean has_slotType(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_count(){
		return _count;
	}
	public void set_count(int val){
		_bit |= 0x8;
		_count = val;
	}
	public boolean has_count(){
		return (_bit & 0x8) == 0x8;
	}
	public BlessCode get_bless(){
		return _bless;
	}
	public void set_bless(BlessCode val){
		_bit |= 0x10;
		_bless = val;
	}
	public boolean has_bless(){
		return (_bit & 0x10) == 0x10;
	}
	public MagicDollPotential get_potential(){
		return _potential;
	}
	public void set_potential(MagicDollPotential val){
		_bit |= 0x20;
		_potential = val;
	}
	public boolean has_potential(){
		return (_bit & 0x20) == 0x20;
	}
	public String get_cuid(){
		return _cuid;
	}
	public void set_cuid(String val){
		_bit |= 0x40;
		_cuid = val;
	}
	public boolean has_cuid(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_dataId(){
		return _dataId;
	}
	public void set_dataId(int val){
		_bit |= 0x80;
		_dataId = val;
	}
	public boolean has_dataId(){
		return (_bit & 0x80) == 0x80;
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
		if (has_desc()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _desc);
		}
		if (has_icon()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _icon);
		}
		if (has_slotType()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _slotType.toInt());
		}
		if (has_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _count);
		}
		if (has_bless()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(5, _bless.toInt());
		}
		if (has_potential()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, _potential);
		}
		if (has_cuid()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(7, _cuid);
		}
		if (has_dataId()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _dataId);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_desc()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_icon()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_slotType()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_desc()){
			output.writeString(1, _desc);
		}
		if (has_icon()){
			output.wirteInt32(2, _icon);
		}
		if (has_slotType()){
			output.writeEnum(3, _slotType.toInt());
		}
		if (has_count()){
			output.wirteInt32(4, _count);
		}
		if (has_bless()){
			output.writeEnum(5, _bless.toInt());
		}
		if (has_potential()){
			output.writeMessage(6, _potential);
		}
		if (has_cuid()){
			output.writeString(7, _cuid);
		}
		if (has_dataId()){
			output.wirteInt32(8, _dataId);
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
					set_desc(input.readString());
					break;
				}
				case 0x00000010:{
					set_icon(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_slotType(SlotInfo.SlotType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000020:{
					set_count(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_bless(BlessCode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000032:{
					set_potential((MagicDollPotential)input.readMessage(MagicDollPotential.newInstance()));
					break;
				}
				case 0x0000003A:{
					set_cuid(input.readString());
					break;
				}
				case 0x00000040:{
					set_dataId(input.readInt32());
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
		return new SlotInfo();
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
	public enum SlotType{
		UNKNOWN(0),
		ITEM(1),
		SKILL(2),
		;
		private int value;
		SlotType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(SlotType v){
			return value == v.value;
		}
		public static SlotType fromInt(int i){
			switch(i){
			case 0:
				return UNKNOWN;
			case 1:
				return ITEM;
			case 2:
				return SKILL;
			default:
				throw new IllegalArgumentException(String.format("無效參數 SlotType，%d", i));
			}
		}
	}
}
