package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class TimeCollectionSetData implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static TimeCollectionSetData send(L1PcInstance pc, L1TimeCollection obj, L1TimeCollectionUser user) {
		TimeCollectionSetData set_data = TimeCollectionSetData.newInstance();
		set_data.set_setId(obj.getCollectionIndex());
		if (user != null) {
//			set_data.set_useRecycle(user.isRegistComplet() ? 0x01 : 0x00);
			set_data.set_useRecycle(0);
			
			ConcurrentHashMap<Integer, L1ItemInstance> registItem = user.getRegistItem();
			if (registItem != null && !registItem.isEmpty()) {
				for (Map.Entry<Integer, L1ItemInstance> entry :  registItem.entrySet()) {
					set_data.add_slotData(TimeCollectionUserSlotData.send(pc, entry.getKey(), entry.getValue()));
//					set_data.add_slotData(TimeCollectionUserSlotData.send(pc, entry.getKey(), entry.getValue()), true);
				}
			}
			set_data.set_buffType(user.getBuffType().getType());
			
			boolean isBuff = user.isBuffActive();
			int time = 0;
			if (isBuff) {
				time = Long.valueOf(user.getBuffTime().getTime() / 1000).intValue();	
			}
			set_data.set_completeTime(time);
			set_data.set_enchantSum(user.getSumEnchant());
			set_data.set_state(isBuff ? TIME_COLLECTION_STATE.TIME_COLLECTION_COMPLETE: TIME_COLLECTION_STATE.TIME_COLLECTION_NONE);
			set_data.set_useRefill(user.getRefill_count());
//			System.out.println("리픽횟수"+user.getRefill_count());
			
		}
		return set_data;
	}
	public static TimeCollectionSetData newInstance(){
		return new TimeCollectionSetData();
	}
	private int _setId;
	private int _useRecycle;
	private java.util.LinkedList<TimeCollectionUserSlotData> _slotData;
	private int _buffType;
	private int _completeTime;
	private int _enchantSum;
	private TIME_COLLECTION_STATE _state;
	private int _useRefill;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private TimeCollectionSetData(){
	}
	public int get_setId(){
		return _setId;
	}
	public void set_setId(int val){
		_bit |= 0x1;
		_setId = val;
	}
	public boolean has_setId(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_useRecycle(){
		return _useRecycle;
	}
	public void set_useRecycle(int val){
		_bit |= 0x2;
		_useRecycle = val;
	}
	public boolean has_useRecycle(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<TimeCollectionUserSlotData> get_slotData(){
		return _slotData;
	}
	public void add_slotData(TimeCollectionUserSlotData val){
		if(!has_slotData()){
			_slotData = new java.util.LinkedList<TimeCollectionUserSlotData>();
			_bit |= 0x4;
		}
		_slotData.add(val);
	}
	public boolean has_slotData(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_buffType(){
		return _buffType;
	}
	public void set_buffType(int val){
		_bit |= 0x8;
		_buffType = val;
	}
	public boolean has_buffType(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_completeTime(){
		return _completeTime;
	}
	public void set_completeTime(int val){
		_bit |= 0x10;
		_completeTime = val;
	}
	public boolean has_completeTime(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_enchantSum(){
		return _enchantSum;
	}
	public void set_enchantSum(int val){
		_bit |= 0x20;
		_enchantSum = val;
	}
	public boolean has_enchantSum(){
		return (_bit & 0x20) == 0x20;
	}
	public TIME_COLLECTION_STATE get_state(){
		return _state;
	}
	public void set_state(TIME_COLLECTION_STATE val){
		_bit |= 0x40;
		_state = val;
	}
	public boolean has_state(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_useRefill(){
		return _useRefill;
	}
	public void set_useRefill(int val){
		_bit |= 0x80;
		_useRefill = val;
	}
	public boolean has_useRefill(){
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
		if (has_setId()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _setId);
		}
		if (has_useRecycle()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _useRecycle);
		}
		if (has_slotData()){
			for(TimeCollectionUserSlotData val : _slotData){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		if (has_buffType()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _buffType);
		}
		if (has_completeTime()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _completeTime);
		}
		if (has_enchantSum()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _enchantSum);
		}
		if (has_state()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(7, _state.toInt());
		}
		if (has_useRefill()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _useRefill);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_setId()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_slotData()){
			for(TimeCollectionUserSlotData val : _slotData){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_setId()){
			output.wirteInt32(1, _setId);
		}
		if (has_useRecycle()){
			output.wirteInt32(2, _useRecycle);
		}
		if (has_slotData()){
			for (TimeCollectionUserSlotData val : _slotData){
				output.writeMessage(3, val);
			}
		}
		if (has_buffType()){
			output.wirteInt32(4, _buffType);
		}
		if (has_completeTime()){
			output.wirteInt32(5, _completeTime);
		}
		if (has_enchantSum()){
			output.wirteInt32(6, _enchantSum);
		}
		if (has_state()){
			output.writeEnum(7, _state.toInt());
		}
		if (has_useRefill()){
			output.wirteInt32(8, _useRefill);
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
					set_setId(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_useRecycle(input.readInt32());
					break;
				}
				case 0x0000001A:{
					add_slotData((TimeCollectionUserSlotData)input.readMessage(TimeCollectionUserSlotData.newInstance()));
					break;
				}
				case 0x00000020:{
					set_buffType(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_completeTime(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_enchantSum(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_state(TIME_COLLECTION_STATE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000040:{
					set_useRefill(input.readInt32());
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
		return new TimeCollectionSetData();
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
