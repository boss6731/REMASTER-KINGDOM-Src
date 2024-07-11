package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_EXTEND_CHAR_SLOT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_EXTEND_CHAR_SLOT_REQ newInstance(){
		return new CS_EXTEND_CHAR_SLOT_REQ();
	}
	private int _dummy;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_EXTEND_CHAR_SLOT_REQ(){
	}
	public int get_dummy(){
		return _dummy;
	}
	public void set_dummy(int val){
		_bit |= 0x1;
		_dummy = val;
	}
	public boolean has_dummy(){
		return (_bit & 0x1) == 0x1;
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
		if (has_dummy()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _dummy);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_dummy()){
			output.wirteInt32(1, _dummy);
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
					set_dummy(input.readInt32());
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

			if(clnt == null) {
				return this;
			}
			
			if (clnt.getAccount().getCharSlot() >= 10){
				return this;
			}
//			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(clnt.getAccountName());
//			warehouse.getItems().clear();
//			warehouse.loadItems();
//			ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(clnt.getAccountName());
//			elfwarehouse.getItems().clear();
//			elfwarehouse.loadItems();
//			SupplementaryService supplementaryservice = WarehouseManager.getInstance().getSupplementaryService(clnt.getAccountName());
//			supplementaryservice.getItems().clear();
//			supplementaryservice.loadItems();
//			if(warehouse.findItemId(210083) != null) {
//				warehouse.deleteItem(warehouse.findItemId(210083));
//				clnt.getAccount().set_posess_slotkey_charname("dummy");
//			} else if(elfwarehouse.findItemId(210083) != null) {
//				elfwarehouse.deleteItem(elfwarehouse.findItemId(210083));
//				clnt.getAccount().set_posess_slotkey_charname("dummy");
//			} else if(supplementaryservice.findItemId(210083) != null) {
//				supplementaryservice.deleteItem(elfwarehouse.findItemId(210083));
//				clnt.getAccount().set_posess_slotkey_charname("dummy");
//			} else {
//				clnt.getAccount().get_slot_char(clnt.getAccountName());
//			}
			clnt.getAccount().get_slot_char(clnt.getAccountName());
			if(clnt.getAccount().is_posess_slotkey_charname() == null) {
				SC_EXTEND_CHAR_SLOT_ACK ack = SC_EXTEND_CHAR_SLOT_ACK.newInstance();
				ack.set_result_code(0);
				ack.set_additional_msg_code(8394);
				clnt.sendPacket(ack, MJEProtoMessages.SC_EXTEND_CHAR_SLOT_ACK.toInt(), true);
			} else {
				SC_EXTEND_CHAR_SLOT_ACK ack = SC_EXTEND_CHAR_SLOT_ACK.newInstance();
				ack.set_result_code(1);
				ack.set_use_item_char("");
				ack.set_additional_msg_code(8394);
				clnt.sendPacket(ack, MJEProtoMessages.SC_EXTEND_CHAR_SLOT_ACK.toInt(), true);	
			}
			clnt.getAccount().set_posess_slotkey_charname(null);
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_EXTEND_CHAR_SLOT_REQ();
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
