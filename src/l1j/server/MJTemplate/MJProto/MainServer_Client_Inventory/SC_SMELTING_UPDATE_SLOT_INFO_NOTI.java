package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;




// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_SMELTING_UPDATE_SLOT_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_SMELTING_UPDATE_SLOT_INFO_NOTI newInstance(){
		return new SC_SMELTING_UPDATE_SLOT_INFO_NOTI();
	}

	public static void send(L1PcInstance pc, L1ItemInstance scrollobj, L1ItemInstance targetobj, int slotnum, SmeltingResult result){
		SC_SMELTING_UPDATE_SLOT_INFO_NOTI ask = new SC_SMELTING_UPDATE_SLOT_INFO_NOTI();
		ask.set_target_object_id(targetobj.getId());
		ask.set_scroll_name_id(scrollobj.getItem().getItemDescId());
		ask.set_result(result);
		ask.set_slot_no(slotnum);
		pc.sendPackets(ask, MJEProtoMessages.SC_SMELTING_UPDATE_SLOT_INFO_NOTI, true);
	}
	
	public static void send(L1PcInstance pc, int scrollobj, L1ItemInstance targetobj, int slotnum, SmeltingResult result){
		SC_SMELTING_UPDATE_SLOT_INFO_NOTI ask = new SC_SMELTING_UPDATE_SLOT_INFO_NOTI();
		ask.set_target_object_id(targetobj.getId());
		ask.set_scroll_name_id(scrollobj);
		ask.set_result(result);
		ask.set_slot_no(slotnum);
		pc.sendPackets(ask, MJEProtoMessages.SC_SMELTING_UPDATE_SLOT_INFO_NOTI, true);
	}
	public static void eject(L1PcInstance pc,  L1ItemInstance targetobj, int slotnum, SmeltingResult result){
		SC_SMELTING_UPDATE_SLOT_INFO_NOTI ask = new SC_SMELTING_UPDATE_SLOT_INFO_NOTI();
		ask.set_target_object_id(targetobj.getId());
		ask.set_scroll_name_id(0);
		ask.set_result(result);
		ask.set_slot_no(slotnum);
		pc.sendPackets(ask, MJEProtoMessages.SC_SMELTING_UPDATE_SLOT_INFO_NOTI, true);
	}
	
	
	private int _target_object_id;
	private int _slot_no;
	private int _scroll_name_id;
	private SmeltingResult _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SMELTING_UPDATE_SLOT_INFO_NOTI(){
	}
	public int get_target_object_id(){
		return _target_object_id;
	}
	public void set_target_object_id(int val){
		_bit |= 0x1;
		_target_object_id = val;
	}
	public boolean has_target_object_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_slot_no(){
		return _slot_no;
	}
	public void set_slot_no(int val){
		_bit |= 0x2;
		_slot_no = val;
	}
	public boolean has_slot_no(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_scroll_name_id(){
		return _scroll_name_id;
	}
	public void set_scroll_name_id(int val){
		_bit |= 0x4;
		_scroll_name_id = val;
	}
	public boolean has_scroll_name_id(){
		return (_bit & 0x4) == 0x4;
	}
	public SmeltingResult get_result(){
		return _result;
	}
	public void set_result(SmeltingResult val){
		_bit |= 0x8;
		_result = val;
	}
	public boolean has_result(){
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
		if (has_target_object_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _target_object_id);
		}
		if (has_slot_no()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _slot_no);
		}
		if (has_scroll_name_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _scroll_name_id);
		}
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _result.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_target_object_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_slot_no()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_scroll_name_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_target_object_id()){
			output.writeUInt32(1, _target_object_id);
		}
		if (has_slot_no()){
			output.writeUInt32(2, _slot_no);
		}
		if (has_scroll_name_id()){
			output.writeUInt32(3, _scroll_name_id);
		}
		if (has_result()){
			output.writeEnum(4, _result.toInt());
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
					set_target_object_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_slot_no(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_scroll_name_id(input.readUInt32());
					break;
				}
				case 0x00000020:{
					set_result(SmeltingResult.fromInt(input.readEnum()));
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
		return new SC_SMELTING_UPDATE_SLOT_INFO_NOTI();
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
