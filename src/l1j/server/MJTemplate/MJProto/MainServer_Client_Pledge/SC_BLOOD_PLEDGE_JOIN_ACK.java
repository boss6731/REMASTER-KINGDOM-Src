package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import java.sql.Timestamp;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.ePLEDGE_JOIN_REQ_TYPE;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.storage.mysql.MySqlCharacterStorage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BLOOD_PLEDGE_JOIN_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static ProtoOutputStream sendClan(L1PcInstance pc, String clanname, int type, int msgcode){
		SC_BLOOD_PLEDGE_JOIN_ACK noti = SC_BLOOD_PLEDGE_JOIN_ACK.newInstance();
		noti.set_result(ePLEDGE_JOIN_ACK_RESULT.fromInt(msgcode));
		//ePLEDGE_JOIN_ACK_RESULT_JOIN_OK(0),                             加入完成。
		//ePLEDGE_JOIN_ACK_RESULT_CONFIRMING(1),                        已申請加入 [公會名稱]。
		//ePLEDGE_JOIN_ACK_RESULT_INVALID_JOIN_TYPE(2),                    申請訊息已提交，加入申請完成。
		//ePLEDGE_JOIN_ACK_RESULT_INVALID_PASSWORD(3),                    輸入的密碼不正確。
		//ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_EXIST(4),                    當前無法加入公會，公會成員未上線或公會不存在。
		//ePLEDGE_JOIN_ACK_RESULT_USER_JOINING_COUNT_IS_OVER(5),        加入申請最多可提交給三個公會。
		//ePLEDGE_JOIN_ACK_RESULT_PLEDGE_JOINING_COUNT_IS_OVER(6),        [公會名稱] 公會目前無法加入。
		//ePLEDGE_JOIN_ACK_RESULT_PLEDGE_IS_FULL(7),                    [公會名稱] 公會目前無法加入。
		//ePLEDGE_JOIN_ACK_RESULT_NEED_LEVEL(8),                        [公會名稱] 公會目前無法加入。
		//ePLEDGE_JOIN_ACK_RESULT_ALREADY_JOINED(9),                    已經加入了公會。
		//ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_OPENED(10),                [公會名稱] 公會目前無法加入。
		//ePLEDGE_JOIN_ACK_RESULT_NO_PLEDGE_MEMBER_IN_WORLD(11),        當前無法加入公會，公會成員未上線或公會不存在。
		//ePLEDGE_JOIN_ACK_RESULT_DIED(12),                                [公會名稱] 公會目前無法加入。
		//ePLEDGE_JOIN_ACK_RESULT_PRINCE_NEED_OTHER_METHOD(13),            請與 [公會名稱] 公會的君主會面以加入公會。
		//ePLEDGE_JOIN_ACK_RESULT_JOIN_MSG_NOT_EXIST(14),                無內容。
		noti.set_pledge_name(clanname);
		noti.set_need_join_type(ePLEDGE_JOIN_REQ_TYPE.fromInt(type));
		noti.set_msg(clanname);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		pc.setClanJoinDate(time);
		MySqlCharacterStorage.storeClanJoinTime(pc);
		
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_BLOOD_PLEDGE_JOIN_ACK);
		noti.dispose();
		return stream;
	}
	
	public static void ack(L1PcInstance pc, SC_BLOOD_PLEDGE_JOIN_ACK.ePLEDGE_JOIN_ACK_RESULT result) {
		SC_BLOOD_PLEDGE_JOIN_ACK ack = SC_BLOOD_PLEDGE_JOIN_ACK.newInstance();
		ack.set_result(result);
		pc.sendPackets(ack, MJEProtoMessages.SC_BLOOD_PLEDGE_JOIN_ACK);
	}
	
	public static SC_BLOOD_PLEDGE_JOIN_ACK newInstance(){
		return new SC_BLOOD_PLEDGE_JOIN_ACK();
	}
	private SC_BLOOD_PLEDGE_JOIN_ACK.ePLEDGE_JOIN_ACK_RESULT _result;
	private String _pledge_name;
	private ePLEDGE_JOIN_REQ_TYPE _need_join_type;
	private String _msg;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_JOIN_ACK(){
	}
	public SC_BLOOD_PLEDGE_JOIN_ACK.ePLEDGE_JOIN_ACK_RESULT get_result(){
		return _result;
	}
	public void set_result(SC_BLOOD_PLEDGE_JOIN_ACK.ePLEDGE_JOIN_ACK_RESULT val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public String get_pledge_name(){
		return _pledge_name;
	}
	public void set_pledge_name(String val){
		_bit |= 0x2;
		_pledge_name = val;
	}
	public boolean has_pledge_name(){
		return (_bit & 0x2) == 0x2;
	}
	public ePLEDGE_JOIN_REQ_TYPE get_need_join_type(){
		return _need_join_type;
	}
	public void set_need_join_type(ePLEDGE_JOIN_REQ_TYPE val){
		_bit |= 0x4;
		_need_join_type = val;
	}
	public boolean has_need_join_type(){
		return (_bit & 0x4) == 0x4;
	}
	public String get_msg(){
		return _msg;
	}
	public void set_msg(String val){
		_bit |= 0x8;
		_msg = val;
	}
	public boolean has_msg(){
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
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		}
		if (has_pledge_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _pledge_name);
		}
		if (has_need_join_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _need_join_type.toInt());
		}
		if (has_msg()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _msg);
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
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_result()){
			output.writeEnum(1, _result.toInt());
		}
		if (has_pledge_name()){
			output.writeString(2, _pledge_name);
		}
		if (has_need_join_type()){
			output.writeEnum(3, _need_join_type.toInt());
		}
		if (has_msg()){
			output.writeString(4, _msg);
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
					set_result(SC_BLOOD_PLEDGE_JOIN_ACK.ePLEDGE_JOIN_ACK_RESULT.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					set_pledge_name(input.readString());
					break;
				}
				case 0x00000018:{
					set_need_join_type(ePLEDGE_JOIN_REQ_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000022:{
					set_msg(input.readString());
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_BLOOD_PLEDGE_JOIN_ACK();
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
	public enum ePLEDGE_JOIN_ACK_RESULT{
		ePLEDGE_JOIN_ACK_RESULT_JOIN_OK(0),
		ePLEDGE_JOIN_ACK_RESULT_CONFIRMING(1),
		ePLEDGE_JOIN_ACK_RESULT_INVALID_JOIN_TYPE(2),
		ePLEDGE_JOIN_ACK_RESULT_INVALID_PASSWORD(3),
		ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_EXIST(4),
		ePLEDGE_JOIN_ACK_RESULT_USER_JOINING_COUNT_IS_OVER(5),
		ePLEDGE_JOIN_ACK_RESULT_PLEDGE_JOINING_COUNT_IS_OVER(6),
		ePLEDGE_JOIN_ACK_RESULT_PLEDGE_IS_FULL(7),
		ePLEDGE_JOIN_ACK_RESULT_NEED_LEVEL(8),
		ePLEDGE_JOIN_ACK_RESULT_ALREADY_JOINED(9),
		ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_OPENED(10),
		ePLEDGE_JOIN_ACK_RESULT_NO_PLEDGE_MEMBER_IN_WORLD(11),
		ePLEDGE_JOIN_ACK_RESULT_DIED(12),
		ePLEDGE_JOIN_ACK_RESULT_PRINCE_NEED_OTHER_METHOD(13),
		ePLEDGE_JOIN_ACK_RESULT_JOIN_MSG_NOT_EXIST(14),
		ePLEDGE_JOIN_ACK_RESULT_UNKNOWN_ERROR(9999),
		;
		private int value;
		ePLEDGE_JOIN_ACK_RESULT(int val){
			value = val;
		}
		public int toInt() {
			return value;
		}

		public boolean equals(ePLEDGE_JOIN_ACK_RESULT v) {
			return value == v.value;
		}

		public static ePLEDGE_JOIN_ACK_RESULT fromInt(int i) {
			switch (i) {
				case 0:
					return ePLEDGE_JOIN_ACK_RESULT_JOIN_OK;
				case 1:
					return ePLEDGE_JOIN_ACK_RESULT_CONFIRMING;
				case 2:
					return ePLEDGE_JOIN_ACK_RESULT_INVALID_JOIN_TYPE;
				case 3:
					return ePLEDGE_JOIN_ACK_RESULT_INVALID_PASSWORD;
				case 4:
					return ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_EXIST;
				case 5:
					return ePLEDGE_JOIN_ACK_RESULT_USER_JOINING_COUNT_IS_OVER;
				case 6:
					return ePLEDGE_JOIN_ACK_RESULT_PLEDGE_JOINING_COUNT_IS_OVER;
				case 7:
					return ePLEDGE_JOIN_ACK_RESULT_PLEDGE_IS_FULL;
				case 8:
					return ePLEDGE_JOIN_ACK_RESULT_NEED_LEVEL;
				case 9:
					return ePLEDGE_JOIN_ACK_RESULT_ALREADY_JOINED;
				case 10:
					return ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_OPENED;
				case 11:
					return ePLEDGE_JOIN_ACK_RESULT_NO_PLEDGE_MEMBER_IN_WORLD;
				case 12:
					return ePLEDGE_JOIN_ACK_RESULT_DIED;
				case 13:
					return ePLEDGE_JOIN_ACK_RESULT_PRINCE_NEED_OTHER_METHOD;
				case 14:
					return ePLEDGE_JOIN_ACK_RESULT_JOIN_MSG_NOT_EXIST;
				case 9999:
					return ePLEDGE_JOIN_ACK_RESULT_UNKNOWN_ERROR;
				default:
					throw new IllegalArgumentException(String.format("無效的 ePLEDGE_JOIN_ACK_RESULT 參數: %d", i));
			}
		}
	}
}