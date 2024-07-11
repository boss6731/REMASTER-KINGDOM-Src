package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SlotInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SlotInfoT newInstance(){
		return new SlotInfoT();
	}
	private int _category;
	private int _slot_id;
	private int _item_id;
	private int _craft_id;
	private int _awakening;
	private int _red_dot_notice;
	private ItemInfo _item_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SlotInfoT(){
		set_awakening(0);
		set_red_dot_notice(0);
	}
	public int get_category(){
		return _category;
	}
	public void set_category(int val){
		_bit |= 0x1;
		_category = val;
	}
	public boolean has_category(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_slot_id(){
		return _slot_id;
	}
	public void set_slot_id(int val){
		_bit |= 0x2;
		_slot_id = val;
	}
	public boolean has_slot_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_item_id(){
		return _item_id;
	}
	public void set_item_id(int val){
		_bit |= 0x4;
		_item_id = val;
	}
	public boolean has_item_id(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_craft_id(){
		return _craft_id;
	}
	public void set_craft_id(int val){
		_bit |= 0x8;
		_craft_id = val;
	}
	public boolean has_craft_id(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_awakening(){
		return _awakening;
	}
	public void set_awakening(int val){
		_bit |= 0x10;
		_awakening = val;
	}
	public boolean has_awakening(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_red_dot_notice(){
		return _red_dot_notice;
	}
	public void set_red_dot_notice(int val){
		_bit |= 0x20;
		_red_dot_notice = val;
	}
	public boolean has_red_dot_notice(){
		return (_bit & 0x20) == 0x20;
	}
	public ItemInfo get_item_info(){
		return _item_info;
	}
	public void set_item_info(ItemInfo val){
		_bit |= 0x40;
		_item_info = val;
	}
	public boolean has_item_info(){
		return (_bit & 0x40) == 0x40;
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
		if (has_category()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _category);
		}
		if (has_slot_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _slot_id);
		}
		if (has_item_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _item_id);
		}
		if (has_craft_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _craft_id);
		}
		if (has_awakening()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _awakening);
		}
		if (has_red_dot_notice()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _red_dot_notice);
		}
		if (has_item_info()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, _item_info);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_category()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_slot_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_category()){
			output.wirteInt32(1, _category);
		}
		if (has_slot_id()){
			output.wirteInt32(2, _slot_id);
		}
		if (has_item_id()){
			output.wirteInt32(3, _item_id);
		}
		if (has_craft_id()){
			output.wirteInt32(4, _craft_id);
		}
		if (has_awakening()){
			output.wirteInt32(5, _awakening);
		}
		if (has_red_dot_notice()){
			output.wirteInt32(6, _red_dot_notice);
		}
		if (has_item_info()){
			output.writeMessage(7, _item_info);
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
					set_category(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_slot_id(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_item_id(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_craft_id(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_awakening(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_red_dot_notice(input.readInt32());
					break;
				}
				case 0x0000003A:{
					set_item_info((ItemInfo)input.readMessage(ItemInfo.newInstance()));
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
		return new SlotInfoT();
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
