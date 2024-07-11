package l1j.server.MJTemplate.MJProto.resultCode;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.Optional;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.datatables.CharacterFreeShieldTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1FreeShield;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_PC_MASTER_FAVOR_UPDATE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public void send (L1PcInstance pc) {
		SC_PC_MASTER_FAVOR_UPDATE_NOTI noti = SC_PC_MASTER_FAVOR_UPDATE_NOTI.newInstance();
		
		L1FreeShield shield = CharacterFreeShieldTable.getInstance().getFreeShield(pc);
		
		noti.set_favor_state(pc.isPcBuff());
		noti.set_favor_remain_count(shield.get_Pc_Gaho() - shield.get_Pc_Gaho_use() > 3 ? 3: shield.get_Pc_Gaho());
		long time = pc.getAccount().get_Pk_Time();
		long currenttime = System.currentTimeMillis();
		long locktime = 0;
		if (time + (30*60) > currenttime / 1000) {
			locktime = time + (30*60) - currenttime / 1000; 
		} else {
			locktime = 0;
		}
		int locktime1 = Long.valueOf(locktime).intValue();
		noti.set_favor_locked_time(locktime1 );
//		noti.set_reward_item_count(pc.getAccount().getFeatherCount());
		noti.set_reward_item_count(pc.getInventory().checkItemCount(41921));
		pc.sendPackets(noti, MJEProtoMessages.SC_PC_MASTER_FAVOR_UPDATE_NOTI, true);
	}
	
	
	public static SC_PC_MASTER_FAVOR_UPDATE_NOTI newInstance(){
		return new SC_PC_MASTER_FAVOR_UPDATE_NOTI();
	}
	private boolean _favor_state;
	private int _favor_remain_count;
	private int _favor_locked_time;
	private int _reward_item_count;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_PC_MASTER_FAVOR_UPDATE_NOTI(){
	}
	public boolean get_favor_state(){
		return _favor_state;
	}
	public void set_favor_state(boolean val){
		_bit |= 0x1;
		_favor_state = val;
	}
	public boolean has_favor_state(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_favor_remain_count(){
		return _favor_remain_count;
	}
	public void set_favor_remain_count(int val){
		_bit |= 0x2;
		_favor_remain_count = val;
	}
	public boolean has_favor_remain_count(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_favor_locked_time(){
		return _favor_locked_time;
	}
	public void set_favor_locked_time(int val){
		_bit |= 0x4;
		_favor_locked_time = val;
	}
	public boolean has_favor_locked_time(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_reward_item_count(){
		return _reward_item_count;
	}
	public void set_reward_item_count(int val){
		_bit |= 0x8;
		_reward_item_count = val;
	}
	public boolean has_reward_item_count(){
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
		if (has_favor_state()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _favor_state);
		}
		if (has_favor_remain_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _favor_remain_count);
		}
		if (has_favor_locked_time()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _favor_locked_time);
		}
		if (has_reward_item_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _reward_item_count);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_favor_state()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_favor_remain_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_favor_state()){
			output.writeBool(1, _favor_state);
		}
		if (has_favor_remain_count()){
			output.wirteInt32(2, _favor_remain_count);
		}
		if (has_favor_locked_time()){
			output.wirteInt32(3, _favor_locked_time);
		}
		if (has_reward_item_count()){
			output.wirteInt32(4, _reward_item_count);
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
					set_favor_state(input.readBool());
					break;
				}
				case 0x00000010:{
					set_favor_remain_count(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_favor_locked_time(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_reward_item_count(input.readInt32());
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
		return new SC_PC_MASTER_FAVOR_UPDATE_NOTI();
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
