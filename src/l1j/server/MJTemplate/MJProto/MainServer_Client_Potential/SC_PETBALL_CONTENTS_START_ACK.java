package l1j.server.MJTemplate.MJProto.MainServer_Client_Potential;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_PETBALL_CONTENTS_START_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc) {
		SC_PETBALL_CONTENTS_START_ACK noti = SC_PETBALL_CONTENTS_START_ACK.newInstance();
		noti.set_start_result(eResult.SUCCESS_START);
		pc.sendPackets(noti, MJEProtoMessages.SC_PETBALL_CONTENTS_START_ACK, true);
	}
	
	public static SC_PETBALL_CONTENTS_START_ACK newInstance(){
		return new SC_PETBALL_CONTENTS_START_ACK();
	}
	private SC_PETBALL_CONTENTS_START_ACK.eResult _start_result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_PETBALL_CONTENTS_START_ACK(){
	}
	public SC_PETBALL_CONTENTS_START_ACK.eResult get_start_result(){
		return _start_result;
	}
	public void set_start_result(SC_PETBALL_CONTENTS_START_ACK.eResult val){
		_bit |= 0x1;
		_start_result = val;
	}
	public boolean has_start_result(){
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
		if (has_start_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _start_result.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_start_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_start_result()){
			output.writeEnum(1, _start_result.toInt());
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
					set_start_result(SC_PETBALL_CONTENTS_START_ACK.eResult.fromInt(input.readEnum()));
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
		return new SC_PETBALL_CONTENTS_START_ACK();
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
		// 成功開始
		SUCCESS_START(0),
		// 無法在此開始
		FAIL_CANT_START_HERE(1),
		// 無法加載配置
		FAIL_CANT_LOAD_CONFIG(2),
		// 無法找到用戶
		FAIL_CANT_FIND_USER(3),

		;

		private int value;

		// 枚舉類型的構造函數，用於初始化枚舉值
		eResult(int val) {
			value = val;
		}

		// 返回枚舉值的整數表示
		public int toInt() {
			return value;
		}

		// 比較當前枚舉值與其他枚舉值是否相等
		public boolean equals(eResult v) {
			return value == v.value;
		}

		// 通過整數值返回對應的枚舉類型。如果輸入的整數值無效，則拋出 IllegalArgumentException
		public static eResult fromInt(int i) {
			switch (i) {
				case 0:
					return SUCCESS_START; // 成功開始
				case 1:
					return FAIL_CANT_START_HERE; // 無法在此開始
				case 2:
					return FAIL_CANT_LOAD_CONFIG; // 無法加載配置
				case 3:
					return FAIL_CANT_FIND_USER; // 無法找到用戶
				default:
					throw new IllegalArgumentException(String.format("無效的 eResult 參數: %d", i));
			}
		}
	}
