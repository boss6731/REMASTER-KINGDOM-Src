package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BLOODPLEDGE_MEMBER_CHANGED_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_BLOODPLEDGE_MEMBER_CHANGED_NOTI newInstance(){
		return new SC_BLOODPLEDGE_MEMBER_CHANGED_NOTI();
	}
	private SC_BLOODPLEDGE_MEMBER_CHANGED_NOTI.Reason _reason;
	private int _user_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOODPLEDGE_MEMBER_CHANGED_NOTI(){
	}
	public SC_BLOODPLEDGE_MEMBER_CHANGED_NOTI.Reason get_reason(){
		return _reason;
	}
	public void set_reason(SC_BLOODPLEDGE_MEMBER_CHANGED_NOTI.Reason val){
		_bit |= 0x1;
		_reason = val;
	}
	public boolean has_reason(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_user_id(){
		return _user_id;
	}
	public void set_user_id(int val){
		_bit |= 0x2;
		_user_id = val;
	}
	public boolean has_user_id(){
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
		if (has_reason()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _reason.toInt());
		}
		if (has_user_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _user_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_reason()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_user_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_reason()){
			output.writeEnum(1, _reason.toInt());
		}
		if (has_user_id()){
			output.writeUInt32(2, _user_id);
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
					set_reason(SC_BLOODPLEDGE_MEMBER_CHANGED_NOTI.Reason.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_user_id(input.readUInt32());
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
		return new SC_BLOODPLEDGE_MEMBER_CHANGED_NOTI();
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
	public enum Reason {
		// 添加
		ADD(0),
		// 刪除
		DEL(1),
		// 添加自己
		ADD_ME(2),
		// 刪除自己
		DEL_ME(3),

		;

		private int value;

		// 枚舉類型的構造函數，用於初始化枚舉值
		Reason(int val) {
			value = val;
		}

		// 返回枚舉值的整數表示
		public int toInt() {
			return value;
		}

		// 比較當前枚舉值與其他枚舉值是否相等
		public boolean equals(Reason v) {
			return value == v.value;
		}

		// 通過整數值返回對應的枚舉類型。如果輸入的整數值無效，則拋出 IllegalArgumentException
		public static Reason fromInt(int i) {
			switch (i) {
				case 0:
					return ADD; // 添加
				case 1:
					return DEL; // 刪除
				case 2:
					return ADD_ME; // 添加自己
				case 3:
					return DEL_ME; // 刪除自己
				default:
					throw new IllegalArgumentException(String.format("無效的 Reason 參數: %d", i));
			}
		}
	}
