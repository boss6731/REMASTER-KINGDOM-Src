package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ newInstance(){
		return new CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ();
	}
	private CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ.ePLEDGE_JOIN_CONFIRM_REQ_TYPE _req_type;
	private String _user_name;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ(){
	}
	public CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ.ePLEDGE_JOIN_CONFIRM_REQ_TYPE get_req_type(){
		return _req_type;
	}
	public void set_req_type(CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ.ePLEDGE_JOIN_CONFIRM_REQ_TYPE val){
		_bit |= 0x1;
		_req_type = val;
	}
	public boolean has_req_type(){
		return (_bit & 0x1) == 0x1;
	}
	public String get_user_name(){
		return _user_name;
	}
	public void set_user_name(String val){
		_bit |= 0x2;
		_user_name = val;
	}
	public boolean has_user_name(){
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
		if (has_req_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _req_type.toInt());
		}
		if (has_user_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _user_name);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_req_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_user_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_req_type()){
			output.writeEnum(1, _req_type.toInt());
		}
		if (has_user_name()){
			output.writeString(2, _user_name);
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
					set_req_type(CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ.ePLEDGE_JOIN_CONFIRM_REQ_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					set_user_name(input.readString());
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

// TODO: 在此處插入處理代碼。由 MJSoft 製作。

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
// 返回一個新的 CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ 實例
		return new CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ();
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
// 調用 newInstance 方法，返回一個新的 CS_BLOOD_PLEDGE_JOIN_CONFIRM_REQ 實例
		return newInstance();
	}

	@override
	public void dispose() {
// 重置字段值
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	// 定義枚舉 ePLEDGE_JOIN_CONFIRM_REQ_TYPE
	public enum ePLEDGE_JOIN_CONFIRM_REQ_TYPE {
		ePLEDGE_JOIN_CONFIRM_REQ_TYPE_ACCEPT(1),  // 接受
		ePLEDGE_JOIN_CONFIRM_REQ_TYPE_REJECT(2);  // 拒絕

		private int value;

		// 枚舉的構造函數
		ePLEDGE_JOIN_CONFIRM_REQ_TYPE(int val) {
			value = val;
		}

		// 返回枚舉的整數值
		public int toInt() {
			return value;
		}

		// 比較兩個枚舉是否相等
		public boolean equals(ePLEDGE_JOIN_CONFIRM_REQ_TYPE v) {
			return value == v.value;
		}

		// 根據整數值返回對應的枚舉
		public static ePLEDGE_JOIN_CONFIRM_REQ_TYPE fromInt(int i) {
			switch (i) {
				case 1:
					return ePLEDGE_JOIN_CONFIRM_REQ_TYPE_ACCEPT;
				case 2:
					return ePLEDGE_JOIN_CONFIRM_REQ_TYPE_REJECT;
				default:
					throw new IllegalArgumentException(String.format("無效的 ePLEDGE_JOIN_CONFIRM_REQ_TYPE，%d", i));
			}
		}
	}
