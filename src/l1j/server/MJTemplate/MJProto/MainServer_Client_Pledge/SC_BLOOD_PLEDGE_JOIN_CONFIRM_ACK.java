package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static ProtoOutputStream sendJoinConfirm(int type){
		SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK noti = SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK.newInstance();
		noti.set_result(ePLEDGE_JOIN_CONFIRM_ACK_RESULT.fromInt(type));
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK);
		noti.dispose();
		return stream;
	}
	
	public static SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK newInstance(){
		return new SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK();
	}
	private SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK.ePLEDGE_JOIN_CONFIRM_ACK_RESULT _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK(){
	}
	public SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK.ePLEDGE_JOIN_CONFIRM_ACK_RESULT get_result(){
		return _result;
	}
	public void set_result(SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK.ePLEDGE_JOIN_CONFIRM_ACK_RESULT val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
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
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
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
					set_result(SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK.ePLEDGE_JOIN_CONFIRM_ACK_RESULT.fromInt(input.readEnum()));
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
		return new SC_BLOOD_PLEDGE_JOIN_CONFIRM_ACK();
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
	public enum ePLEDGE_JOIN_CONFIRM_ACK_RESULT{
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_OK(0),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_ERROR(1),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_USER_OFFLINE(2),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_NO_PRIVILEGE(3),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_NOT_EXIST(4),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_ALREADY_EXIST(5),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_JOIN_REQ_NOT_EXIST(6),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_IS_FULL(7),
		;
		private int value;

		ePLEDGE_JOIN_CONFIRM_ACK_RESULT(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(ePLEDGE_JOIN_CONFIRM_ACK_RESULT v) {
			return value == v.value;
		}

		public static ePLEDGE_JOIN_CONFIRM_ACK_RESULT fromInt(int i) {
			switch (i) {
				case 0:
					return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_OK;
				case 1:
					return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_ERROR;
				case 2:
					return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_USER_OFFLINE;
				case 3:
					return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_NO_PRIVILEGE;
				case 4:
					return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_NOT_EXIST;
				case 5:
					return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_ALREADY_EXIST;
				case 6:
					return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_JOIN_REQ_NOT_EXIST;
				case 7:
					return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_IS_FULL;
				default:
					throw new IllegalArgumentException(String.format("無效的 ePLEDGE_JOIN_CONFIRM_ACK_RESULT 參數: %d", i));
			}
		}
	}
