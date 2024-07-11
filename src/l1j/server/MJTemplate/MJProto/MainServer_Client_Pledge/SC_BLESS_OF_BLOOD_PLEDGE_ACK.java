package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BLESS_OF_BLOOD_PLEDGE_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_BLESS_OF_BLOOD_PLEDGE_ACK newInstance(){
		return new SC_BLESS_OF_BLOOD_PLEDGE_ACK();
	}
	private SC_BLESS_OF_BLOOD_PLEDGE_ACK.eResult _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLESS_OF_BLOOD_PLEDGE_ACK(){
	}
	public SC_BLESS_OF_BLOOD_PLEDGE_ACK.eResult get_result(){
		return _result;
	}
	public void set_result(SC_BLESS_OF_BLOOD_PLEDGE_ACK.eResult val){
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
					set_result(SC_BLESS_OF_BLOOD_PLEDGE_ACK.eResult.fromInt(input.readEnum()));
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
		return new SC_BLESS_OF_BLOOD_PLEDGE_ACK();
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
		// 成功
		Success(0),
		// 世界存在失敗
		WorldExistingFail(1),
		// 公會檢查失敗
		PledgeCheckFail(2),
		// 公會等級失敗
		PledgeRankFail(3),
		// 阿登娜（遊戲貨幣）不足
		EnoughAdenaFail(4),
		// 條件混亂失敗
		ShuffleConditionFail(5),
		// 傳送條件失敗
		TeleportConditionFail(6),
		;

		private int value;

		eResult(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eResult v) {
			return value == v.value;
		}

		public static eResult fromInt(int i) {
			switch (i) {
				case 0:
					return Success;
				case 1:
					return WorldExistingFail;
				case 2:
					return PledgeCheckFail;
				case 3:
					return PledgeRankFail;
				case 4:
					return EnoughAdenaFail;
				case 5:
					return ShuffleConditionFail;
				case 6:
					return TeleportConditionFail;
				default:
					throw new IllegalArgumentException(String.format("無效的 eResult 參數: %d", i));
			}
		}
	}
