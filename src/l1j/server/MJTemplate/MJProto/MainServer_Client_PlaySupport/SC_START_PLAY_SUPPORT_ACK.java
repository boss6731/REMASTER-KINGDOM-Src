package l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_START_PLAY_SUPPORT_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_START_PLAY_SUPPORT_ACK newInstance(){
		return new SC_START_PLAY_SUPPORT_ACK();
	}
	private eResult _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_START_PLAY_SUPPORT_ACK(){
	}
	public eResult get_result(){
		return _result;
	}
	public void set_result(eResult val){
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
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_result())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
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
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_result()){
			output.writeEnum(1, _result.toInt());
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{ //default 와 자리바꿈
					set_result(eResult.fromInt(input.readEnum()));
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
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			
			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar(); //220801 proto에서 추가
			if (pc == null){
				return this;
			}
// TODO: 在此處插入處理代碼。由 MJSoft 製作。

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@override
	public MJIProtoMessage copyInstance() {
// 返回一個新的 SC_START_PLAY_SUPPORT_ACK 實例
		return new SC_START_PLAY_SUPPORT_ACK();
	}

	@override
	public MJIProtoMessage reloadInstance() {
// 調用 newInstance 方法，返回一個新的 SC_START_PLAY_SUPPORT_ACK 實例
		return newInstance();
	}

	@override
	public void dispose() {
// 重置字段值
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	// 定義枚舉 eResult
	public enum eResult {
		VALID(0),             // 有效
		INVALID_MAP(1),       // 無效地圖
		INVALID_LEVEL(2),     // 無效等級
		TIME_EXPIRE(3),       // 時間到期
		UNKNOWN_ERROR(99);    // 未知錯誤

		private int value;

		// 枚舉的構造函數
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
					return VALID;
				case 1:
					return INVALID_MAP;
				case 2:
					return INVALID_LEVEL;
				case 3:
					return TIME_EXPIRE;
				case 99:
					return UNKNOWN_ERROR;
				default:
					throw new IllegalArgumentException(String.format("無效的 eResult，%d", i));
			}
		}
	}