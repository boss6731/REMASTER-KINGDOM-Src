package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 自動生成的 PROTO 代碼。由 MJSoft 製作。
public class SC_OBTAINED_ITEM_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {

	// 發送獲得的物品信息給玩家
	public static void send_obtained(L1PcInstance pc, L1ItemInstance item, int count) {
		SC_OBTAINED_ITEM_INFO noti = SC_OBTAINED_ITEM_INFO.newInstance();

// 檢查物品描述ID是否有效
		if (item.getItem().getItemDescId() < 1) {
//System.out.println(String.format("異常描述的物品名稱: %s 物品ID: %d 描述ID: %d", item.getName(), item.getItemId(), item.getItem().getItemDescId()));
			return;
		}

/*
如果物品描述ID為 25754，根據 count 來更新羽毛計數
if (item.getItem().getItemDescId() == 25754) {
if (count > 0) {
pc.getAccount().addTotalFeatherCount(count);
} else if (count < 0) {
pc.getAccount().useTotalFeatherCount(count);
}
}
*/

// 設置物品描述ID
		noti.set_namd_id(item.getItem().getItemDescId());
// 設置祝福碼
		noti.set_bless_code(item.getBless());
// 設置變更數量
		noti.set_changing_count(count);

// 目前處理為 null 值，本服也未正確顯示此信息
// 如果本服改善，則將其改為 item.getStatusBytes() 即可正常顯示
		noti.set_extra_desc(null);
//noti.set_extra_desc(item.getStatusBytes());

// 發送獲得物品信息給玩家
		pc.sendPackets(noti, MJEProtoMessages.SC_OBTAINED_ITEM_INFO, true);
	}
}
	
	public static SC_OBTAINED_ITEM_INFO newInstance(){
		return new SC_OBTAINED_ITEM_INFO();
	}
	private int _namd_id;
	private int _bless_code;
	private int _changing_count;
	private byte[] _extra_desc;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_OBTAINED_ITEM_INFO(){
	}
	public int get_namd_id(){
		return _namd_id;
	}
	public void set_namd_id(int val){
		_bit |= 0x1;
		_namd_id = val;
	}
	public boolean has_namd_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_bless_code(){
		return _bless_code;
	}
	public void set_bless_code(int val){
		_bit |= 0x2;
		_bless_code = val;
	}
	public boolean has_bless_code(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_changing_count(){
		return _changing_count;
	}
	public void set_changing_count(int val){
		_bit |= 0x4;
		_changing_count = val;
	}
	public boolean has_changing_count(){
		return (_bit & 0x4) == 0x4;
	}
	public byte[] get_extra_desc(){
		return _extra_desc;
	}
	public void set_extra_desc(byte[] val){
		_bit |= 0x8;
		_extra_desc = val;
	}
	public boolean has_extra_desc(){
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
		if (has_namd_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _namd_id);
		}
		if (has_bless_code()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _bless_code);
		}
		if (has_changing_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _changing_count);
		}
		if (has_extra_desc()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _extra_desc);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_namd_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bless_code()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_changing_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_extra_desc()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_namd_id()){
			output.wirteInt32(1, _namd_id);
		}
		if (has_bless_code()){
			output.wirteInt32(2, _bless_code);
		}
		if (has_changing_count()){
			output.wirteInt32(3, _changing_count);
		}
		if (has_extra_desc()){
			output.writeBytes(4, _extra_desc);
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
					set_namd_id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_bless_code(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_changing_count(input.readInt32());
					break;
				}
				case 0x00000022:{
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

// TODO : 在此處插入處理代碼。由 MJSoft 製作。

		} catch (Exception e) {
// 捕獲並打印異常
			e.printStackTrace();
		}
		return this;
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
// 返回一個新的 SC_OBTAINED_ITEM_INFO 實例
		return new SC_OBTAINED_ITEM_INFO();
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
// 返回一個新的 SC_OBTAINED_ITEM_INFO 實例
		return newInstance();
	}

	@override
	public void dispose() {
// 重置位置信息和初始化標記
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
