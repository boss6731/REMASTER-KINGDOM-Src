package l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport;

import l1j.server.MJTemplate.MJProto.WireFormat;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_UPDATE_PLAY_SUPPORT_TIME_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_UPDATE_PLAY_SUPPORT_TIME_NOTI newInstance(){
		return new SC_UPDATE_PLAY_SUPPORT_TIME_NOTI();
	}
	private eType _type;
	private int _remain_time;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_UPDATE_PLAY_SUPPORT_TIME_NOTI(){
	}
	public eType get_type(){
		return _type;
	}
	public void set_type(eType val){
		_bit |= 0x1;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_remain_time(){
		return _remain_time;
	}
	public void set_remain_time(int val){
		_bit |= 0x2;
		_remain_time = val;
	}
	public boolean has_remain_time(){
		return (_bit & 0x2) == 0x2;
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
		if (has_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _type.toInt());
		if (has_remain_time())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _remain_time);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_remain_time()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_type()){
			output.writeEnum(1, _type.toInt());
		}
		if (has_remain_time()){
			output.writeUInt32(2, _remain_time);
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

				case 0x00000008:{
					set_type(eType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_remain_time(input.readUInt32());
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
			L1PcInstance pc = clnt.getActiveChar();
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
// 返回一個新的 SC_UPDATE_PLAY_SUPPORT_TIME_NOTI 實例
		return new SC_UPDATE_PLAY_SUPPORT_TIME_NOTI();
	}

	@override
	public MJIProtoMessage reloadInstance() {
// 調用 newInstance 方法，返回一個新的 SC_UPDATE_PLAY_SUPPORT_TIME_NOTI 實例
		return newInstance();
	}

	@override
	public void dispose() {
// 重置字段值
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	// 定義枚舉 eType
	public enum eType {
		CHARGE_BY_EVENT(1),  // 通過事件充值
		CHARGE_BY_ITEM(2);   // 通過道具充值

		private int value;

		// 枚舉的構造函數
		eType(int val) {
			value = val;
		}

		// 返回枚舉的整數值
		public int toInt() {
			return value;
		}

		// 比較兩個枚舉是否相等
		public boolean equals(eType v) {
			return value == v.value;
		}

		// 根據整數值返回對應的枚舉
		public static eType fromInt(int i) {
			switch (i) {
				case 1:
					return CHARGE_BY_EVENT;
				case 2:
					return CHARGE_BY_ITEM;
				default:
					throw new IllegalArgumentException(String.format("無效的 eType，%d", i));
			}
		}
	}
