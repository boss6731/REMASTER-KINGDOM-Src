package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserExpirationListener;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_TIME_COLLECTION_ADENA_REFILL_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send (L1PcInstance pc, int result, L1TimeCollectionUser user, int refill_count) {
		SC_TIME_COLLECTION_ADENA_REFILL_ACK ack = SC_TIME_COLLECTION_ADENA_REFILL_ACK.newInstance();
		ack.set_result(ADENA_REFILL_ACK_RESULT.fromInt(result));
		ack.set_refillCount(refill_count);
		
		pc.sendPackets(ack, MJEProtoMessages.SC_TIME_COLLECTION_ADENA_REFILL_ACK, true);
		
	}
	public static SC_TIME_COLLECTION_ADENA_REFILL_ACK newInstance(){
		return new SC_TIME_COLLECTION_ADENA_REFILL_ACK();
	}
	private SC_TIME_COLLECTION_ADENA_REFILL_ACK.ADENA_REFILL_ACK_RESULT _result;
	private int _refillCount;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_TIME_COLLECTION_ADENA_REFILL_ACK(){
	}
	public SC_TIME_COLLECTION_ADENA_REFILL_ACK.ADENA_REFILL_ACK_RESULT get_result(){
		return _result;
	}
	public void set_result(SC_TIME_COLLECTION_ADENA_REFILL_ACK.ADENA_REFILL_ACK_RESULT val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_refillCount(){
		return _refillCount;
	}
	public void set_refillCount(int val){
		_bit |= 0x2;
		_refillCount = val;
	}
	public boolean has_refillCount(){
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
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		}
		if (has_refillCount()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _refillCount);
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
		if (has_refillCount()){
			output.wirteInt32(2, _refillCount);
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
					set_result(SC_TIME_COLLECTION_ADENA_REFILL_ACK.ADENA_REFILL_ACK_RESULT.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_refillCount(input.readInt32());
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
// 返回一個新的 SC_TIME_COLLECTION_ADENA_REFILL_ACK 實例
		return new SC_TIME_COLLECTION_ADENA_REFILL_ACK();
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
// 調用 newInstance 方法，返回一個新的 SC_TIME_COLLECTION_ADENA_REFILL_ACK 實例
		return newInstance();
	}

	@override
	public void dispose() {
// 重置字段值
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	// 定義 ADENA_REFILL_ACK_RESULT 枚舉，用於表示 Adena 補充操作的結果
	public enum ADENA_REFILL_ACK_RESULT {
		TCAR_SUCCESS(1),                  // 成功
		TCAR_FAIL_RECYCLE_COUNT_CHECK(2), // 回收計數檢查失敗
		TCAR_FAIL_CAN_NOT_REFILL_SET(3),  // 無法設置補充
		TCAR_FAIL_NOT_BUFF_STATE(4),      // 不在增益狀態
		TCAR_FAIL_NOT_ENOUGH_ADENA(5),    // Adena 不足
		TCAR_FAIL_DECREASE_ADENA_ERROR(6),// 減少 Adena 錯誤
		TCAR_FAIL_NOT_ENOUGH_REFILL_COUNT(7), // 補充次數不足
		TCAR_DATA_ERROR(100);             // 數據錯誤

		private int value;

		// 枚舉構造函數
		ADENA_REFILL_ACK_RESULT(int val) {
			value = val;
		}

		// 返回枚舉的整數值
		public int toInt() {
			return value;
		}

		// 比較兩個枚舉是否相等
		public boolean equals(ADENA_REFILL_ACK_RESULT v) {
			return value == v.value;
		}

		// 根據整數值返回對應的枚舉
		public static ADENA_REFILL_ACK_RESULT fromInt(int i) {
			switch (i) {
				case 1:
					return TCAR_SUCCESS;
				case 2:
					return TCAR_FAIL_RECYCLE_COUNT_CHECK;
				case 3:
					return TCAR_FAIL_CAN_NOT_REFILL_SET;
				case 4:
					return TCAR_FAIL_NOT_BUFF_STATE;
				case 5:
					return TCAR_FAIL_NOT_ENOUGH_ADENA;
				case 6:
					return TCAR_FAIL_DECREASE_ADENA_ERROR;
				case 7:
					return TCAR_FAIL_NOT_ENOUGH_REFILL_COUNT;
				case 100:
					return TCAR_DATA_ERROR;
				default:
					throw new IllegalArgumentException(String.format("無效的 ADENA_REFILL_ACK_RESULT，%d", i));
			}
		}
	}
