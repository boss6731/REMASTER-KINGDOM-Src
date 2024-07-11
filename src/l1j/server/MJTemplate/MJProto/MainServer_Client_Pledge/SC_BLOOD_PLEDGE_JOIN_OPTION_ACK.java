package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BLOOD_PLEDGE_JOIN_OPTION_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static ProtoOutputStream sendJoinSetting(L1PcInstance pc, int join, int type){
		SC_BLOOD_PLEDGE_JOIN_OPTION_ACK noti = SC_BLOOD_PLEDGE_JOIN_OPTION_ACK.newInstance();
		L1Clan clan = pc.getClan();
		if (clan == null)
			noti.set_result(eRESULT.fromInt(3));
		else if (!pc.isCrown())
			noti.set_result(eRESULT.fromInt(2));
		else
			noti.set_result(eRESULT.fromInt(1));
		
		if (join == 0)
			noti.set_enable_join(false);
		else
			noti.set_enable_join(true);
		
		noti.set_join_type(ePLEDGE_JOIN_REQ_TYPE.fromInt(type));
		
		noti.set_hashed_password("".getBytes());
		noti.set_introduction_message("".getBytes());
		
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_BLOOD_PLEDGE_JOIN_OPTION_ACK);
		noti.dispose();
		return stream;
	}
	
	public static SC_BLOOD_PLEDGE_JOIN_OPTION_ACK newInstance(){
		return new SC_BLOOD_PLEDGE_JOIN_OPTION_ACK();
	}
	private SC_BLOOD_PLEDGE_JOIN_OPTION_ACK.eRESULT _result;
	private boolean _enable_join;
	private ePLEDGE_JOIN_REQ_TYPE _join_type;
	private byte[] _hashed_password;
	private byte[] _introduction_message;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_JOIN_OPTION_ACK(){
	}
	public SC_BLOOD_PLEDGE_JOIN_OPTION_ACK.eRESULT get_result(){
		return _result;
	}
	public void set_result(SC_BLOOD_PLEDGE_JOIN_OPTION_ACK.eRESULT val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public boolean get_enable_join(){
		return _enable_join;
	}
	public void set_enable_join(boolean val){
		_bit |= 0x2;
		_enable_join = val;
	}
	public boolean has_enable_join(){
		return (_bit & 0x2) == 0x2;
	}
	public ePLEDGE_JOIN_REQ_TYPE get_join_type(){
		return _join_type;
	}
	public void set_join_type(ePLEDGE_JOIN_REQ_TYPE val){
		_bit |= 0x4;
		_join_type = val;
	}
	public boolean has_join_type(){
		return (_bit & 0x4) == 0x4;
	}
	public byte[] get_hashed_password(){
		return _hashed_password;
	}
	public void set_hashed_password(byte[] val){
		_bit |= 0x8;
		_hashed_password = val;
	}
	public boolean has_hashed_password(){
		return (_bit & 0x8) == 0x8;
	}
	public byte[] get_introduction_message(){
		return _introduction_message;
	}
	public void set_introduction_message(byte[] val){
		_bit |= 0x10;
		_introduction_message = val;
	}
	public boolean has_introduction_message(){
		return (_bit & 0x10) == 0x10;
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
		if (has_enable_join()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _enable_join);
		}
		if (has_join_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _join_type.toInt());
		}
		if (has_hashed_password()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _hashed_password);
		}
		if (has_introduction_message()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(5, _introduction_message);
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
		if (has_enable_join()){
			output.writeBool(2, _enable_join);
		}
		if (has_join_type()){
			output.writeEnum(3, _join_type.toInt());
		}
		if (has_hashed_password()){
			output.writeBytes(4, _hashed_password);
		}
		if (has_introduction_message()){
			output.writeBytes(5, _introduction_message);
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
					set_result(SC_BLOOD_PLEDGE_JOIN_OPTION_ACK.eRESULT.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_enable_join(input.readBool());
					break;
				}
				case 0x00000018:{
					set_join_type(ePLEDGE_JOIN_REQ_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000022:{
					set_hashed_password(input.readBytes());
					break;
				}
				case 0x0000002A:{
					set_introduction_message(input.readBytes());
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
		return new SC_BLOOD_PLEDGE_JOIN_OPTION_ACK();
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
	public enum eRESULT {
		// 操作成功
		eRESULT_OK(1),
		// 錯誤：不是領主
		eRESULT_ERROR_NOT_LORD(2),
		// 錯誤：沒有公會
		eRESULT_ERROR_HAVE_NO_PLEDGE(3),

		;

		private int value;

		// 枚舉類型的構造函數，用於初始化枚舉值
		eRESULT(int val) {
			value = val;
		}

		// 返回枚舉值的整數表示
		public int toInt() {
			return value;
		}

		// 比較當前枚舉值與其他枚舉值是否相等
		public boolean equals(eRESULT v) {
			return value == v.value;
		}

		// 通過整數值返回對應的枚舉類型。如果輸入的整數值無效，則拋出 IllegalArgumentException
		public static eRESULT fromInt(int i) {
			switch (i) {
				case 1:
					return eRESULT_OK;
				case 2:
					return eRESULT_ERROR_NOT_LORD;
				case 3:
					return eRESULT_ERROR_HAVE_NO_PLEDGE;
				default:
					throw new IllegalArgumentException(String.format("無效的 eRESULT 參數: %d", i));
			}
		}
	}
