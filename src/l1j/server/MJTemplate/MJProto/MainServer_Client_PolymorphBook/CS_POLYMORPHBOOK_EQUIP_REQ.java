package l1j.server.MJTemplate.MJProto.MainServer_Client_PolymorphBook;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_POLYMORPHBOOK_EQUIP_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_POLYMORPHBOOK_EQUIP_REQ newInstance(){
		return new CS_POLYMORPHBOOK_EQUIP_REQ();
	}
	private int _Id;
	private int _Slot;
	private int _EquipItemId;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_POLYMORPHBOOK_EQUIP_REQ(){
	}
	public int get_Id(){
		return _Id;
	}
	public void set_Id(int val){
		_bit |= 0x1;
		_Id = val;
	}
	public boolean has_Id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_Slot(){
		return _Slot;
	}
	public void set_Slot(int val){
		_bit |= 0x2;
		_Slot = val;
	}
	public boolean has_Slot(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_EquipItemId(){
		return _EquipItemId;
	}
	public void set_EquipItemId(int val){
		_bit |= 0x4;
		_EquipItemId = val;
	}
	public boolean has_EquipItemId(){
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
		if (has_Id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _Id);
		}
		if (has_Slot()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _Slot);
		}
		if (has_EquipItemId()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _EquipItemId);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_Id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_Slot()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_EquipItemId()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_Id()){
			output.wirteInt32(1, _Id);
		}
		if (has_Slot()){
			output.wirteInt32(2, _Slot);
		}
		if (has_EquipItemId()){
			output.writeUInt32(3, _EquipItemId);
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
					set_Id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_Slot(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_EquipItemId(input.readUInt32());
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
		return new CS_POLYMORPHBOOK_EQUIP_REQ();
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
