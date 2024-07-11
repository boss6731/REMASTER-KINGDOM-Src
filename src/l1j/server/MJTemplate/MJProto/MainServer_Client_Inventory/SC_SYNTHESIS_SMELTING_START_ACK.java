package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_SYNTHESIS_SMELTING_START_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc) {
		SC_SYNTHESIS_SMELTING_START_ACK noti = SC_SYNTHESIS_SMELTING_START_ACK.newInstance();
		noti.set_result(eResult.SUCCESS_START);
		pc.sendPackets(noti, MJEProtoMessages.SC_SYNTHESIS_SMELTING_START_ACK, true);
	}
	
	public static SC_SYNTHESIS_SMELTING_START_ACK newInstance(){
		return new SC_SYNTHESIS_SMELTING_START_ACK();
	}
	private SC_SYNTHESIS_SMELTING_START_ACK.eResult _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SYNTHESIS_SMELTING_START_ACK(){
	}
	public SC_SYNTHESIS_SMELTING_START_ACK.eResult get_result(){
		return _result;
	}
	public void set_result(SC_SYNTHESIS_SMELTING_START_ACK.eResult val){
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
					set_result(SC_SYNTHESIS_SMELTING_START_ACK.eResult.fromInt(input.readEnum()));
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

// TODO: 在此處插入處理代碼。由 Nature 製作。

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
// 返回一個新的 SC_SYNTHESIS_SMELTING_START_ACK 實例
		return new SC_SYNTHESIS_SMELTING_START_ACK();
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
// 調用 newInstance 方法，返回一個新的 SC_SYNTHESIS_SMELTING_START_ACK 實例
		return newInstance();
	}

	@override
	public void dispose() {
// 重置字段值
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	// 定義 eResult 枚舉，用於表示操作結果
	public enum eResult {
		SUCCESS_START(0),             // 成功開始
		FAIL_CANT_START_HERE(1),      // 無法在此處開始
		FAIL_CANT_LOAD_CONFIG(2),     // 無法加載配置
		FAIL_CANT_FIND_USER(3),       // 無法找到用戶
		INVALID_TELEPORT_WORLD(4);    // 無效的傳送世界

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
					return SUCCESS_START;
				case 1:
					return FAIL_CANT_START_HERE;
				case 2:
					return FAIL_CANT_LOAD_CONFIG;
				case 3:
					return FAIL_CANT_FIND_USER;
				case 4:
					return INVALID_TELEPORT_WORLD;
				default:
					throw new IllegalArgumentException(String.format("無效的 eResult，%d", i));
			}
		}
	}
