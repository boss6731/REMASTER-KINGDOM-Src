package l1j.server.MJTemplate.MJProto.resultCode;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, eUpdateReason i) {
		SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI noti = SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.newInstance();
		
		GoldenBuffInfo info0 = GoldenBuffInfo.newInstance();
		info0.set_index(0);
		info0.set_type(pc.getAccount().get_Index0_type(pc));
		info0.add_grade(pc.getAccount().get_Index0_1());
		info0.add_grade(pc.getAccount().get_Index0_2());
		info0.add_grade(pc.getAccount().get_Index0_3());
		info0.set_remain_time(pc.getAccount().get_Index0_Remain_Time());
		noti.add_golden_buff_info(info0);
		
		GoldenBuffInfo info1 = GoldenBuffInfo.newInstance();
		info1.set_index(1);
		info1.set_type(pc.getAccount().get_Index1_type(pc));
		info1.add_grade(pc.getAccount().get_Index1_1());
		info1.add_grade(pc.getAccount().get_Index1_2());
		info1.add_grade(pc.getAccount().get_Index1_3());
		info1.set_remain_time(pc.getAccount().get_Index1_Remain_Time());
		noti.add_golden_buff_info(info1);
		
		noti.set_reason(i);
		
		pc.sendPackets(noti, MJEProtoMessages.SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI, true);
		
	}
	public static SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI newInstance(){
		return new SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI();
	}
	private java.util.LinkedList<GoldenBuffInfo> _golden_buff_info;
	private SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.eUpdateReason _reason;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI(){
	}
	public java.util.LinkedList<GoldenBuffInfo> get_golden_buff_info(){
		return _golden_buff_info;
	}
	public void add_golden_buff_info(GoldenBuffInfo val){
		if(!has_golden_buff_info()){
			_golden_buff_info = new java.util.LinkedList<GoldenBuffInfo>();
			_bit |= 0x1;
		}
		_golden_buff_info.add(val);
	}
	public boolean has_golden_buff_info(){
		return (_bit & 0x1) == 0x1;
	}
	public SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.eUpdateReason get_reason(){
		return _reason;
	}
	public void set_reason(SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.eUpdateReason val){
		_bit |= 0x2;
		_reason = val;
	}
	public boolean has_reason(){
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
		if (has_golden_buff_info()){
			for(GoldenBuffInfo val : _golden_buff_info){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_reason()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _reason.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_golden_buff_info()){
			for(GoldenBuffInfo val : _golden_buff_info){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_reason()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_golden_buff_info()){
			for (GoldenBuffInfo val : _golden_buff_info){
				output.writeMessage(1, val);
			}
		}
		if (has_reason()){
			output.writeEnum(2, _reason.toInt());
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
					add_golden_buff_info((GoldenBuffInfo)input.readMessage(GoldenBuffInfo.newInstance()));
					break;
				}
				case 0x00000010:{
					set_reason(SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.eUpdateReason.fromInt(input.readEnum()));
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
		return new SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI();
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
	public enum eUpdateReason{
		UPDATE(0),
		ENFORCE_ACK(1),
		SWITCH_TYPE_ACK(2),
		;
		private int value;
		eUpdateReason(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eUpdateReason v){
			return value == v.value;
		}
		public static eUpdateReason fromInt(int i){
			switch(i){
			case 0:
				return UPDATE;
			case 1:
				return ENFORCE_ACK;
			case 2:
				return SWITCH_TYPE_ACK;
			default:
				throw new IllegalArgumentException(String.format("無效參數 eUpdateReason，%d", i));
			}
		}
	}
}
