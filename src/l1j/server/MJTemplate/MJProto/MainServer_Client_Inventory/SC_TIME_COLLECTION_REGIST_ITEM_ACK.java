package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_TIME_COLLECTION_REGIST_ITEM_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, L1TimeCollectionUser user, int slotIndex, L1ItemInstance item) {
		SC_TIME_COLLECTION_REGIST_ITEM_ACK ack = SC_TIME_COLLECTION_REGIST_ITEM_ACK.newInstance();
		ack.set_result(user.isRegistComplet() ? eResult.SUCCESS_SET_COMPLETE : eResult.SUCCESS_NON_COMPLETE);
		ack.set_buffType(user.getBuffType().getType());
//		ack.set_buffType(user.isRegistComplet() ? user.getBuffType().getType() : 0x00);
		ack.set_groupID(user.getType().getType());
		ack.set_setID(user.getCollectionIndex());
		ack.set_slotNo(slotIndex);
		ack.set_nameId(item.getItem().getItemDescId());
		ack.set_enchant(item.getEnchantLevel());
		ack.set_extra_desc(item.getStatusBytes(pc));
		ack.set_bless(1);
		boolean activeBuff = user.getBuffTimer() != null;
		int time = 0;
		if (activeBuff) {
			time = Long.valueOf(user.getBuffTime().getTime() /1000).intValue();
			ack.set_completeTime(time);
		}
//		ack.set_useRecycle(user.isRegistComplet() ? 0x01 : 0x00);
		ack.set_useRecycle(0);
		
		pc.sendPackets(ack, MJEProtoMessages.SC_TIME_COLLECTION_REGIST_ITEM_ACK, true);
	}
	public static SC_TIME_COLLECTION_REGIST_ITEM_ACK newInstance(){
		return new SC_TIME_COLLECTION_REGIST_ITEM_ACK();
	}
	private SC_TIME_COLLECTION_REGIST_ITEM_ACK.eResult _result;
	private int _buffType;
	private int _groupID;
	private int _setID;
	private int _slotNo;
	private int _nameId;
	private int _enchant;
	private byte[] _extra_desc;
	private int _bless;
	private int _completeTime;
	private int _useRecycle;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_TIME_COLLECTION_REGIST_ITEM_ACK(){
	}
	public SC_TIME_COLLECTION_REGIST_ITEM_ACK.eResult get_result(){
		return _result;
	}
	public void set_result(SC_TIME_COLLECTION_REGIST_ITEM_ACK.eResult val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_buffType(){
		return _buffType;
	}
	public void set_buffType(int val){
		_bit |= 0x2;
		_buffType = val;
	}
	public boolean has_buffType(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_groupID(){
		return _groupID;
	}
	public void set_groupID(int val){
		_bit |= 0x4;
		_groupID = val;
	}
	public boolean has_groupID(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_setID(){
		return _setID;
	}
	public void set_setID(int val){
		_bit |= 0x8;
		_setID = val;
	}
	public boolean has_setID(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_slotNo(){
		return _slotNo;
	}
	public void set_slotNo(int val){
		_bit |= 0x10;
		_slotNo = val;
	}
	public boolean has_slotNo(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_nameId(){
		return _nameId;
	}
	public void set_nameId(int val){
		_bit |= 0x20;
		_nameId = val;
	}
	public boolean has_nameId(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_enchant(){
		return _enchant;
	}
	public void set_enchant(int val){
		_bit |= 0x40;
		_enchant = val;
	}
	public boolean has_enchant(){
		return (_bit & 0x40) == 0x40;
	}
	public byte[] get_extra_desc(){
		return _extra_desc;
	}
	public void set_extra_desc(byte[] val){
		_bit |= 0x80;
		_extra_desc = val;
	}
	public boolean has_extra_desc(){
		return (_bit & 0x80) == 0x80;
	}
	public int get_bless(){
		return _bless;
	}
	public void set_bless(int val){
		_bit |= 0x100;
		_bless = val;
	}
	public boolean has_bless(){
		return (_bit & 0x100) == 0x100;
	}
	public int get_completeTime(){
		return _completeTime;
	}
	public void set_completeTime(int val){
		_bit |= 0x200;
		_completeTime = val;
	}
	public boolean has_completeTime(){
		return (_bit & 0x200) == 0x200;
	}
	public int get_useRecycle(){
		return _useRecycle;
	}
	public void set_useRecycle(int val){
		_bit |= 0x400;
		_useRecycle = val;
	}
	public boolean has_useRecycle(){
		return (_bit & 0x400) == 0x400;
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
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		}
		if (has_buffType()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _buffType);
		}
		if (has_groupID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _groupID);
		}
		if (has_setID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _setID);
		}
		if (has_slotNo()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _slotNo);
		}
		if (has_nameId()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _nameId);
		}
		if (has_enchant()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _enchant);
		}
		if (has_extra_desc()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(8, _extra_desc);
		}
		if (has_bless()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _bless);
		}
		if (has_completeTime()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _completeTime);
		}
		if (has_useRecycle()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _useRecycle);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_groupID()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_setID()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_slotNo()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_nameId()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enchant()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bless()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_result()){
			output.writeEnum(1, _result.toInt());
		}
		if (has_buffType()){
			output.wirteInt32(2, _buffType);
		}
		if (has_groupID()){
			output.wirteInt32(3, _groupID);
		}
		if (has_setID()){
			output.wirteInt32(4, _setID);
		}
		if (has_slotNo()){
			output.wirteInt32(5, _slotNo);
		}
		if (has_nameId()){
			output.wirteInt32(6, _nameId);
		}
		if (has_enchant()){
			output.wirteInt32(7, _enchant);
		}
		if (has_extra_desc()){
			output.writeBytes(8, _extra_desc);
		}
		if (has_bless()){
			output.wirteInt32(9, _bless);
		}
		if (has_completeTime()){
			output.wirteInt32(10, _completeTime);
		}
		if (has_useRecycle()){
			output.wirteInt32(11, _useRecycle);
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
					set_result(SC_TIME_COLLECTION_REGIST_ITEM_ACK.eResult.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_buffType(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_groupID(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_setID(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_slotNo(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_nameId(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_enchant(input.readInt32());
					break;
				}
				case 0x00000042:{
					set_extra_desc(input.readBytes());
					break;
				}
				case 0x00000048:{
					set_bless(input.readInt32());
					break;
				}
				case 0x00000050:{
					set_completeTime(input.readInt32());
					break;
				}
				case 0x00000058:{
					set_useRecycle(input.readInt32());
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
		return new SC_TIME_COLLECTION_REGIST_ITEM_ACK();
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
	public enum eResult {
		SUCCESS_NON_COMPLETE(0),         // 成功但未完成
		SUCCESS_SET_COMPLETE(1),         // 成功且設置完成
		WAIT_SET_DATA(10),               // 等待設置數據
		ERROR_INVALID_TARGET_ID(11),     // 無效的目標ID錯誤
		ERROR_INVALID_GROUP_ID(12),      // 無效的組ID錯誤
		ERROR_INVALID_SET_ID(13),        // 無效的設置ID錯誤
		ERROR_INVALID_SLOT_NO(14),       // 無效的插槽編號錯誤
		ERROR_INVALID_ITEM(15),          // 無效的項目錯誤
		ERROR_OVERLAP_SLOT(16),          // 插槽重疊錯誤
		ERROR_UNKNOWN(9999);             // 未知錯誤

		private int value;

		// 枚舉構造函數
		eResult(int val) {
			value = val;
		}

		// 返回枚舉的整數值
		public int toInt() {
			return value;
		}

		// 比較兩個枚舉是否相等
		public boolean equals(eResult v) {
			return value == v.value;
		}

		// 根據整數值返回對應的枚舉
		public static eResult fromInt(int i) {
			switch (i) {
				case 0:
					return SUCCESS_NON_COMPLETE;
				case 1:
					return SUCCESS_SET_COMPLETE;
				case 10:
					return WAIT_SET_DATA;
				case 11:
					return ERROR_INVALID_TARGET_ID;
				case 12:
					return ERROR_INVALID_GROUP_ID;
				case 13:
					return ERROR_INVALID_SET_ID;
				case 14:
					return ERROR_INVALID_SLOT_NO;
				case 15:
					return ERROR_INVALID_ITEM;
				case 16:
					return ERROR_OVERLAP_SLOT;
				case 9999:
					return ERROR_UNKNOWN;
				default:
					throw new IllegalArgumentException(String.format("無效的 eResult，%d", i));
			}
		}
	}
