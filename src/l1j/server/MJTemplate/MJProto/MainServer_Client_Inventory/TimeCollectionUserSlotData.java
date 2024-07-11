package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class TimeCollectionUserSlotData implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static TimeCollectionUserSlotData send(L1PcInstance pc, int slotIndex, L1ItemInstance item) {
		TimeCollectionUserSlotData user_data = TimeCollectionUserSlotData.newInstance();
		user_data.set_slotNo(slotIndex);
		user_data.set_nameId(item.getItem().getItemDescId());
		user_data.set_enchant(item.getEnchantLevel());
		user_data.set_bless(1);
		user_data.set_extra_desc(item.getStatusBytes());
		return user_data;
	}
	public static TimeCollectionUserSlotData newInstance(){
		return new TimeCollectionUserSlotData();
	}
	private int _slotNo;
	private int _nameId;
	private int _enchant;
	private int _bless;
	private byte[] _extra_desc;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private TimeCollectionUserSlotData(){
	}
	public int get_slotNo(){
		return _slotNo;
	}
	public void set_slotNo(int val){
		_bit |= 0x1;
		_slotNo = val;
	}
	public boolean has_slotNo(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_nameId(){
		return _nameId;
	}
	public void set_nameId(int val){
		_bit |= 0x2;
		_nameId = val;
	}
	public boolean has_nameId(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_enchant(){
		return _enchant;
	}
	public void set_enchant(int val){
		_bit |= 0x4;
		_enchant = val;
	}
	public boolean has_enchant(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_bless(){
		return _bless;
	}
	public void set_bless(int val){
		_bit |= 0x8;
		_bless = val;
	}
	public boolean has_bless(){
		return (_bit & 0x8) == 0x8;
	}
	public byte[] get_extra_desc(){
		return _extra_desc;
	}
	public void set_extra_desc(byte[] val){
		_bit |= 0x10;
		_extra_desc = val;
	}
	public boolean has_extra_desc(){
		return (_bit & 0x10) == 0x10;
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
		if (has_slotNo()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _slotNo);
		}
		if (has_nameId()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _nameId);
		}
		if (has_enchant()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _enchant);
		}
		if (has_bless()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _bless);
		}
		if (has_extra_desc()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(5, _extra_desc);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_slotNo()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_slotNo()){
			output.writeUInt32(1, _slotNo);
		}
		if (has_nameId()){
			output.writeUInt32(2, _nameId);
		}
		if (has_enchant()){
			output.writeUInt32(3, _enchant);
		}
		if (has_bless()){
			output.writeUInt32(4, _bless);
		}
		if (has_extra_desc()){
			output.writeBytes(5, _extra_desc);
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
					set_slotNo(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_nameId(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_enchant(input.readUInt32());
					break;
				}
				case 0x00000020:{
					set_bless(input.readUInt32());
					break;
				}
				case 0x0000002A:{
					set_extra_desc(input.readBytes());
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
		return new TimeCollectionUserSlotData();
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
