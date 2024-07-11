package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class PrepareOption implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static PrepareOption newInstance(){
		return new PrepareOption();
	}
	private SlotInfo _itemInfo;
	private int _retrieveCount;
	private boolean _needToBuy;
	private boolean _needToMelting;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PrepareOption(){
		set_retrieveCount(0);
		set_needToBuy(false);
		set_needToMelting(false);
	}
	public SlotInfo get_itemInfo(){
		return _itemInfo;
	}
	public void set_itemInfo(SlotInfo val){
		_bit |= 0x1;
		_itemInfo = val;
	}
	public boolean has_itemInfo(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_retrieveCount(){
		return _retrieveCount;
	}
	public void set_retrieveCount(int val){
		_bit |= 0x2;
		_retrieveCount = val;
	}
	public boolean has_retrieveCount(){
		return (_bit & 0x2) == 0x2;
	}
	public boolean get_needToBuy(){
		return _needToBuy;
	}
	public void set_needToBuy(boolean val){
		_bit |= 0x4;
		_needToBuy = val;
	}
	public boolean has_needToBuy(){
		return (_bit & 0x4) == 0x4;
	}
	public boolean get_needToMelting(){
		return _needToMelting;
	}
	public void set_needToMelting(boolean val){
		_bit |= 0x8;
		_needToMelting = val;
	}
	public boolean has_needToMelting(){
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
		if (has_itemInfo()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _itemInfo);
		}
		if (has_retrieveCount()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _retrieveCount);
		}
		if (has_needToBuy()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _needToBuy);
		}
		if (has_needToMelting()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _needToMelting);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_itemInfo()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_retrieveCount()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_needToBuy()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_needToMelting()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_itemInfo()){
			output.writeMessage(1, _itemInfo);
		}
		if (has_retrieveCount()){
			output.writeUInt32(2, _retrieveCount);
		}
		if (has_needToBuy()){
			output.writeBool(3, _needToBuy);
		}
		if (has_needToMelting()){
			output.writeBool(4, _needToMelting);
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
					set_itemInfo((SlotInfo)input.readMessage(SlotInfo.newInstance()));
					break;
				}
				case 0x00000010:{
					set_retrieveCount(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_needToBuy(input.readBool());
					break;
				}
				case 0x00000020:{
					set_needToMelting(input.readBool());
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
		return new PrepareOption();
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
