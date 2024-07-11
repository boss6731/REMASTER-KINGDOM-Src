package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class LogInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static LogInfo newInstance(){
		return new LogInfo();
	}
	private LogInfo.LogType _Type;
	private int _StrID;
	private java.util.LinkedList<String> _StrValue;
	private String _Time;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private LogInfo(){
		set_Type(LogInfo.LogType.NONE);
		set_StrID(0);
	}
	public LogInfo.LogType get_Type(){
		return _Type;
	}
	public void set_Type(LogInfo.LogType val){
		_bit |= 0x1;
		_Type = val;
	}
	public boolean has_Type(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_StrID(){
		return _StrID;
	}
	public void set_StrID(int val){
		_bit |= 0x2;
		_StrID = val;
	}
	public boolean has_StrID(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<String> get_StrValue(){
		return _StrValue;
	}
	public void add_StrValue(String val){
		if(!has_StrValue()){
			_StrValue = new java.util.LinkedList<String>();
			_bit |= 0x4;
		}
		_StrValue.add(val);
	}
	public boolean has_StrValue(){
		return (_bit & 0x4) == 0x4;
	}
	public String get_Time(){
		return _Time;
	}
	public void set_Time(String val){
		_bit |= 0x8;
		_Time = val;
	}
	public boolean has_Time(){
		return (_bit & 0x8) == 0x8;
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
		if (has_Type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _Type.toInt());
		}
		if (has_StrID()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _StrID);
		}
		if (has_StrValue()){
			for(String val : _StrValue){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, val);
			}
		}
		if (has_Time()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _Time);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_Type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_StrID()){
			_memorizedIsInitialized = -1;
			return false;
		}
//		if (has_StrValue()){
//			for(String val : _StrValue){
//				if (!val.isInitialized()){
//					_memorizedIsInitialized = -1;
//					return false;
//				}
//			}
//		}
		if (!has_Time()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_Type()){
			output.writeEnum(1, _Type.toInt());
		}
		if (has_StrID()){
			output.writeUInt32(2, _StrID);
		}
		if (has_StrValue()){
			for (String val : _StrValue){
				output.writeString(3, val);
			}
		}
		if (has_Time()){
			output.writeString(4, _Time);
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
					set_Type(LogInfo.LogType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_StrID(input.readUInt32());
					break;
				}
				case 0x0000001A:{
					add_StrValue(input.readString());
					break;
				}
				case 0x00000022:{
					set_Time(input.readString());
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new LogInfo();
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
	public enum LogType{
		NONE(1),
		OFF_ETC(2),
		OFF_DEAD(3),
		OFF_PEACE(4),
		OFF_COMEBACK(5),
		OFF_HALT(6),
		OFF_RESTART(7),
		OFF_BUY_FAIL(8),
		OFF_MOVE_FAIL(9),
		OFF_MAP_TIME_ERR(10),
		OFF_WEIGHT_OVER(11),
		START_ALL(12),
		YETI_GET_ITEM(13),
		SHOP_BUY(14),
		SHOP_BUFF(15),
		PVP_ATTACKED(16),
		HP_MP_RESTORE(17),
		RETURN(18),
		MAP_CHANGE(19),
		WAREHOUSE_USE(20),
		START_SEMI(21),
		START_SURROUND(22),
		;
		// 定義枚舉類型 LogType
		public enum LogType {
			NONE(1),
			OFF_ETC(2),
			OFF_DEAD(3),
			OFF_PEACE(4),
			OFF_COMEBACK(5),
			OFF_HALT(6),
			OFF_RESTART(7),
			OFF_BUY_FAIL(8),
			OFF_MOVE_FAIL(9),
			OFF_MAP_TIME_ERR(10),
			OFF_WEIGHT_OVER(11),
			START_ALL(12),
			YETI_GET_ITEM(13),
			SHOP_BUY(14),
			SHOP_BUFF(15),
			PVP_ATTACKED(16),
			HP_MP_RESTORE(17),
			RETURN(18),
			MAP_CHANGE(19),
			WAREHOUSE_USE(20),
			START_SEMI(21),
			START_SURROUND(22),

			;

			private int value;

			// 枚舉類型的構造函數，用於初始化枚舉值
			LogType(int val) {
				value = val;
			}

			// 返回枚舉值的整數表示
			public int toInt() {
				return value;
			}

			// 比較當前枚舉值與其他枚舉值是否相等
			public boolean equals(LogType v) {
				return value == v.value;
			}

			// 通過整數值返回對應的枚舉類型。如果輸入的整數值無效，則拋出 IllegalArgumentException
			public static LogType fromInt(int i) {
				switch (i) {
					case 1:
						return NONE; // 無
					case 2:
						return OFF_ETC; // 其他關閉
					case 3:
						return OFF_DEAD; // 關閉死亡
					case 4:
						return OFF_PEACE; // 關閉和平
					case 5:
						return OFF_COMEBACK; // 關閉回歸
					case 6:
						return OFF_HALT; // 關閉停止
					case 7:
						return OFF_RESTART; // 關閉重啟
					case 8:
						return OFF_BUY_FAIL; // 關閉購買失敗
					case 9:
						return OFF_MOVE_FAIL; // 關閉移動失敗
					case 10:
						return OFF_MAP_TIME_ERR; // 關閉地圖時間錯誤
					case 11:
						return OFF_WEIGHT_OVER; // 關閉超重
					case 12:
						return START_ALL; // 開始所有
					case 13:
						return YETI_GET_ITEM; // 雪人獲取物品
					case 14:
						return SHOP_BUY; // 商店購買
					case 15:
						return SHOP_BUFF; // 商店增益
					case 16:
						return PVP_ATTACKED; // PVP 攻擊
					case 17:
						return HP_MP_RESTORE; // 恢復 HP 和 MP
					case 18:
						return RETURN; // 返回
					case 19:
						return MAP_CHANGE; // 地圖變更
					case 20:
						return WAREHOUSE_USE; // 使用倉庫
					case 21:
						return START_SEMI; // 開始半
					case 22:
						return START_SURROUND; // 開始包圍
					default:
						throw new IllegalArgumentException(String.format("無效的 LogType 參數: %d", i));
				}
			}
		}
