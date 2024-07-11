package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_SMELTING_MAKE_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_SMELTING_MAKE_ACK newInstance(){
		return new SC_SMELTING_MAKE_ACK();
	}
	private SC_SMELTING_MAKE_ACK.ResultCode _result_code;
	private java.util.LinkedList<SC_SMELTING_MAKE_ACK.OutputItem> _output_items;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SMELTING_MAKE_ACK(){
	}
	public SC_SMELTING_MAKE_ACK.ResultCode get_result_code(){
		return _result_code;
	}
	public void set_result_code(SC_SMELTING_MAKE_ACK.ResultCode val){
		_bit |= 0x1;
		_result_code = val;
	}
	public boolean has_result_code(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<SC_SMELTING_MAKE_ACK.OutputItem> get_output_items(){
		return _output_items;
	}
	public void add_output_items(SC_SMELTING_MAKE_ACK.OutputItem val){
		if(!has_output_items()){
			_output_items = new java.util.LinkedList<SC_SMELTING_MAKE_ACK.OutputItem>();
			_bit |= 0x2;
		}
		_output_items.add(val);
	}
	
	public void add_output_items(int itemId, int iconId, int bless){
		OutputItem val = new OutputItem();
		val.set_item_id(itemId);
		val.set_icon_id(iconId);
		val.set_bless_code(bless);
		add_output_items(val);
	}
	
	
	
	public boolean has_output_items(){
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
		if (has_result_code()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result_code.toInt());
		}
		if (has_output_items()){
			for(SC_SMELTING_MAKE_ACK.OutputItem val : _output_items){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result_code()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_output_items()){
			for(SC_SMELTING_MAKE_ACK.OutputItem val : _output_items){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_result_code()){
			output.writeEnum(1, _result_code.toInt());
		}
		if (has_output_items()){
			for (SC_SMELTING_MAKE_ACK.OutputItem val : _output_items){
				output.writeMessage(2, val);
			}
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
					set_result_code(SC_SMELTING_MAKE_ACK.ResultCode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					add_output_items((SC_SMELTING_MAKE_ACK.OutputItem)input.readMessage(SC_SMELTING_MAKE_ACK.OutputItem.newInstance()));
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
		return new SC_SMELTING_MAKE_ACK();
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
	public static class OutputItem implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static OutputItem newInstance(){
			return new OutputItem();
		}
		private int _item_id;
		private int _icon_id;
		private int _bless_code;
		private int _item_grade;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private OutputItem(){
		}
		public int get_item_id(){
			return _item_id;
		}
		public void set_item_id(int val){
			_bit |= 0x1;
			_item_id = val;
		}
		public boolean has_item_id(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_icon_id(){
			return _icon_id;
		}
		public void set_icon_id(int val){
			_bit |= 0x2;
			_icon_id = val;
		}
		public boolean has_icon_id(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_bless_code(){
			return _bless_code;
		}
		public void set_bless_code(int val){
			_bit |= 0x4;
			_bless_code = val;
		}
		public boolean has_bless_code(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_item_grade(){
			return _item_grade;
		}
		public void set_item_grade(int val){
			_bit |= 0x8;
			_item_grade = val;
		}
		public boolean has_item_grade(){
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
			if (has_item_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _item_id);
			}
			if (has_icon_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _icon_id);
			}
			if (has_bless_code()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _bless_code);
			}
			if (has_item_grade()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _item_grade);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_item_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_icon_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_item_id()){
				output.wirteInt32(1, _item_id);
			}
			if (has_icon_id()){
				output.wirteInt32(2, _icon_id);
			}
			if (has_bless_code()){
				output.wirteInt32(3, _bless_code);
			}
			if (has_item_grade()){
				output.wirteInt32(4, _item_grade);
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
						set_item_id(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_icon_id(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_bless_code(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_item_grade(input.readInt32());
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
			return new OutputItem();
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
	}
	public enum ResultCode{
		RC_SUCCESS(0),
		RC_FAIL(1),
		RC_ERROR_INVALID_INPUT(2),
		RC_ERROR_INVEN_OVER(3),
		RC_ERROR_WEIGHT_OVER(4),
		RC_ERROR_ALCHEMY_DOES_NOT_EXIST(5),
		RC_ERROR_NO_REQUIRED_ITEM(6),
		RC_ERROR_BLOCKED_ALCHEMY_ID(7),
		RC_ERROR_CURRENTLY_CLOSED(8),
		RC_ERROR_PETBALL_SUMMONING(9),
		RC_HYPER_SUCCESS(10),
		RC_ERROR_NOT_ENOUGH_SUBINPUT(11),
		RC_ERROR_UNKNOWN(9999),
		;
		private int value;
		private ProtoOutputStream cachedMessage;
		
		ResultCode(int val){
			value = val;
		}
		
		public void sendCachedMessage(L1PcInstance pc){
			if(cachedMessage == null){
				SC_SMELTING_MAKE_ACK ack = new SC_SMELTING_MAKE_ACK();
				ack.set_result_code(this);
				cachedMessage = ack.writeTo(MJEProtoMessages.SC_SMELTING_MAKE_ACK);
				ack.dispose();
			}
			pc.sendPackets(cachedMessage, false);
		}


		public enum ResultCode {
			RC_SUCCESS(0), // 成功
			RC_FAIL(1), // 失敗
			RC_ERROR_INVALID_INPUT(2), // 無效輸入錯誤
			RC_ERROR_INVEN_OVER(3), // 超過庫存錯誤
			RC_ERROR_WEIGHT_OVER(4), // 超過重量錯誤
			RC_ERROR_ALCHEMY_DOES_NOT_EXIST(5), // 煉金術不存在錯誤
			RC_ERROR_NO_REQUIRED_ITEM(6), // 缺少必需物品錯誤
			RC_ERROR_BLOCKED_ALCHEMY_ID(7), // 被封鎖的煉金術ID錯誤
			RC_ERROR_CURRENTLY_CLOSED(8), // 當前關閉錯誤
			RC_ERROR_PETBALL_SUMMONING(9), // 召喚寵物球錯誤
			RC_HYPER_SUCCESS(10), // 超成功
			RC_ERROR_NOT_ENOUGH_SUBINPUT(11), // 輸入不足錯誤
			RC_ERROR_UNKNOWN(9999); // 未知錯誤

			private int value;

			// 構造函數，設置枚舉值
			ResultCode(int val) {
				value = val;
			}

			// 返回枚舉值的整數表示
			public int toInt() {
				return value;
			}

			// 比較枚舉值是否相等
			public boolean equals(ResultCode v) {
				return value == v.value;
			}

			// 從整數轉換為對應的枚舉值
			public static ResultCode fromInt(int i) {
				switch (i) {
					case 0:
						return RC_SUCCESS;
					case 1:
						return RC_FAIL;
					case 2:
						return RC_ERROR_INVALID_INPUT;
					case 3:
						return RC_ERROR_INVEN_OVER;
					case 4:
						return RC_ERROR_WEIGHT_OVER;
					case 5:
						return RC_ERROR_ALCHEMY_DOES_NOT_EXIST;
					case 6:
						return RC_ERROR_NO_REQUIRED_ITEM;
					case 7:
						return RC_ERROR_BLOCKED_ALCHEMY_ID;
					case 8:
						return RC_ERROR_CURRENTLY_CLOSED;
					case 9:
						return RC_ERROR_PETBALL_SUMMONING;
					case 10:
						return RC_HYPER_SUCCESS;
					case 11:
						return RC_ERROR_NOT_ENOUGH_SUBINPUT;
					case 9999:
						return RC_ERROR_UNKNOWN;
					default:
						throw new IllegalArgumentException(String.format("無效的參數 ResultCode, %d", i));
				}
			}
		}
