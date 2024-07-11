package l1j.server.MJTemplate.MJProto.MainServer_Client_System;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.datatables.CharacterFreeShieldTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1FreeShield;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_FREE_BUFF_SHIELD_INFO_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc) {
		SC_FREE_BUFF_SHIELD_INFO_ACK info = SC_FREE_BUFF_SHIELD_INFO_ACK.newInstance();

		DISABLE_FREE_BUFF_SHIELD disable = DISABLE_FREE_BUFF_SHIELD.newInstance();
		long time = pc.getAccount().get_Pk_Time();
		long currenttime = System.currentTimeMillis();
		long locktime = 0;
		int count = 0;
		if (time + (30*60) > currenttime / 1000) {
			locktime = time + (30*60) - currenttime / 1000; 
		} else {
			locktime = 0;
		}
		int locktime1 = Long.valueOf(locktime).intValue();
		disable.set_favor_locked_time(locktime1);

		info.set_disable_state(disable);
		/*PC_CAFE_SHIELD(0),
		EVENT_BUFF_SHIELD(1),
		FREE_BUFF_SHIELD(2),*/
		L1FreeShield shield = CharacterFreeShieldTable.getInstance().getFreeShield(pc);
		for (int i = 0; i < 3; i++) {
			if (i == 0) {
				count = shield.get_Pc_Gaho() - shield.get_Pc_Gaho_use() > 3 ? 3: shield.get_Pc_Gaho();
			}else if (i == 1){
				count = shield.get_Event_Gaho();	
			} else if (i == 2) {
				count = shield.get_Free_Gaho();
			}
			
			FREE_BUFF_SHIELD_INFO shield_info = FREE_BUFF_SHIELD_INFO.newInstance();
			shield_info.set_favor_type(FREE_BUFF_SHIELD_TYPE.fromInt(i));
			shield_info.set_favor_remain_count(count);
			info.add_free_buff_shield_info(shield_info);
		}
		pc.sendPackets(info, MJEProtoMessages.SC_FREE_BUFF_SHIELD_INFO_ACK, true);
		
	}
	public static SC_FREE_BUFF_SHIELD_INFO_ACK newInstance(){
		return new SC_FREE_BUFF_SHIELD_INFO_ACK();
	}
	private DISABLE_FREE_BUFF_SHIELD _disable_state;
	private java.util.LinkedList<FREE_BUFF_SHIELD_INFO> _free_buff_shield_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_FREE_BUFF_SHIELD_INFO_ACK(){
	}
	public DISABLE_FREE_BUFF_SHIELD get_disable_state(){
		return _disable_state;
	}
	public void set_disable_state(DISABLE_FREE_BUFF_SHIELD val){
		_bit |= 0x1;
		_disable_state = val;
	}
	public boolean has_disable_state(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<FREE_BUFF_SHIELD_INFO> get_free_buff_shield_info(){
		return _free_buff_shield_info;
	}
	public void add_free_buff_shield_info(FREE_BUFF_SHIELD_INFO val){
		if(!has_free_buff_shield_info()){
			_free_buff_shield_info = new java.util.LinkedList<FREE_BUFF_SHIELD_INFO>();
			_bit |= 0x2;
		}
		_free_buff_shield_info.add(val);
	}
	public boolean has_free_buff_shield_info(){
		return (_bit & 0x2) == 0x2;
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
		if (has_disable_state()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _disable_state);
		}
		if (has_free_buff_shield_info()){
			for(FREE_BUFF_SHIELD_INFO val : _free_buff_shield_info){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_free_buff_shield_info()){
			for(FREE_BUFF_SHIELD_INFO val : _free_buff_shield_info){
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
		if (has_disable_state()){
			output.writeMessage(1, _disable_state);
		}
		if (has_free_buff_shield_info()){
			for (FREE_BUFF_SHIELD_INFO val : _free_buff_shield_info){
				output.writeMessage(2, val);
			}
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
				case 0x0000000A:{
					set_disable_state((DISABLE_FREE_BUFF_SHIELD)input.readMessage(DISABLE_FREE_BUFF_SHIELD.newInstance()));
					break;
				}
				case 0x00000012:{
					add_free_buff_shield_info((FREE_BUFF_SHIELD_INFO)input.readMessage(FREE_BUFF_SHIELD_INFO.newInstance()));
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
		return new SC_FREE_BUFF_SHIELD_INFO_ACK();
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
