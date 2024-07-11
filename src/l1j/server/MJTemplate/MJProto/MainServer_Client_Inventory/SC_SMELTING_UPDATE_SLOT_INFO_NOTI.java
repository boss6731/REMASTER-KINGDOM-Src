package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;




// TODO: 自動生成的 PROTO 代碼。由 Nature 製作。
public class SC_SMELTING_UPDATE_SLOT_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {

	public static SC_SMELTING_UPDATE_SLOT_INFO_NOTI newInstance() {
		return new SC_SMELTING_UPDATE_SLOT_INFO_NOTI();
	}

	// 發送更新插槽信息的通知
	public static void send(L1PcInstance pc, L1ItemInstance scrollobj, L1ItemInstance targetobj, int slotnum, SmeltingResult result) {
		SC_SMELTING_UPDATE_SLOT_INFO_NOTI ask = new SC_SMELTING_UPDATE_SLOT_INFO_NOTI();
		ask.set_target_object_id(targetobj.getId()); // 設置目標對象ID
		ask.set_scroll_name_id(scrollobj.getItem().getItemDescId()); // 設置捲軸名稱ID
		ask.set_result(result); // 設置熔煉結果
		ask.set_slot_no(slotnum); // 設置插槽號
		pc.sendPackets(ask, MJEProtoMessages.SC_SMELTING_UPDATE_SLOT_INFO_NOTI, true); // 發送通知
	}

	// 發送更新插槽信息的通知，使用捲軸ID
	public static void send(L1PcInstance pc, int scrollobj, L1ItemInstance targetobj, int slotnum, SmeltingResult result) {
		SC_SMELTING_UPDATE_SLOT_INFO_NOTI ask = new SC_SMELTING_UPDATE_SLOT_INFO_NOTI();
		ask.set_target_object_id(targetobj.getId()); // 設置目標對象ID
		ask.set_scroll_name_id(scrollobj); // 設置捲軸名稱ID
		ask.set_result(result); // 設置熔煉結果
		ask.set_slot_no(slotnum); // 設置插槽號
		pc.sendPackets(ask, MJEProtoMessages.SC_SMELTING_UPDATE_SLOT_INFO_NOTI, true); // 發送通知
	}

	// 發送彈出插槽信息的通知
	public static void eject(L1PcInstance pc, L1ItemInstance targetobj, int slotnum, SmeltingResult result) {
		SC_SMELTING_UPDATE_SLOT_INFO_NOTI ask = new SC_SMELTING_UPDATE_SLOT_INFO_NOTI();
		ask.set_target_object_id(targetobj.getId()); // 設置目標對象ID
		ask.set_scroll_name_id(0); // 設置捲軸名稱ID為0
		ask.set_result(result); // 設置熔煉結果
		ask.set_slot_no(slotnum); // 設置插槽號
		pc.sendPackets(ask, MJEProtoMessages.SC_SMELTING_UPDATE_SLOT_INFO_NOTI, true); // 發送通知
	}
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
	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
// 創建 ProtoInputStream 實例，並設置讀取大小
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(
				bytes,
				l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
				((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE
		);

		try {
// 從輸入流中讀取數據
			readFrom(is);

// 檢查是否初始化，如果未初始化，返回當前實例
			if (!isInitialized())
				return this;

// 獲取當前活動角色
			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

// TODO: 從此處開始插入處理代碼。由 Nature 製作。

		} catch (Exception e) {
// 捕獲並打印異常
			e.printStackTrace();
		}
		return this; // 返回當前實例
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
// 返回新實例的副本
		return new SC_SMELTING_UPDATE_SLOT_INFO_NOTI();
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
// 返回新實例
		return newInstance();
	}

	@override
	public void dispose() {
// 重置對象狀態
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
