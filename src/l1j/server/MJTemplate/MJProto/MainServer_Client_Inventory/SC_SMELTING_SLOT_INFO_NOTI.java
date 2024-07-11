package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO: 自動生成的 PROTO 代碼。由 Nature 製作。
public class SC_SMELTING_SLOT_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {

	public static SC_SMELTING_SLOT_INFO_NOTI newInstance() {
		return new SC_SMELTING_SLOT_INFO_NOTI();
	}

	private int _target_object_id; // 目標對象ID
	private int _slot_total_count; // 槽總數
	private java.util.LinkedList<SC_SMELTING_SLOT_INFO_NOTI.SlotInfo> _slot_infos; // 槽信息列表
	private int _memorizedSerializedSize = -1; // 記憶的序列化大小
	private byte _memorizedIsInitialized = -1; // 記憶的初始化標記
	private int _bit; // 位元標記

	private SC_SMELTING_SLOT_INFO_NOTI() {
	}

	// 獲取目標對象ID
	public int get_target_object_id() {
		return _target_object_id;
	}

	// 設置目標對象ID
	public void set_target_object_id(int val) {
		_bit |= 0x1;
		_target_object_id = val;
	}

	// 判斷是否有目標對象ID
	public boolean has_target_object_id() {
		return (_bit & 0x1) == 0x1;
	}

	// 獲取槽總數
	public int get_slot_total_count() {
		return _slot_total_count;
	}

	// 設置槽總數
	public void set_slot_total_count(int val) {
		_bit |= 0x2;
		_slot_total_count = val;
	}

	// 判斷是否有槽總數
	public boolean has_slot_total_count() {
		return (_bit & 0x2) == 0x2;
	}

	// 獲取槽信息列表
	public java.util.LinkedList<SC_SMELTING_SLOT_INFO_NOTI.SlotInfo> get_slot_infos() {
		return _slot_infos;
	}

	// 添加槽信息到列表
	public void add_slot_infos(SC_SMELTING_SLOT_INFO_NOTI.SlotInfo val) {
		if (!has_slot_infos()) {
			_slot_infos = new java.util.LinkedList<SC_SMELTING_SLOT_INFO_NOTI.SlotInfo>();
			_bit |= 0x4;
		}
		_slot_infos.add(val);
	}

	// 判斷是否有槽信息
	public boolean has_slot_infos() {
		return (_bit & 0x4) == 0x4;
	}

	@override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}
}
	@override
	public int getSerializedSize() {
		int size = 0;

// 計算 target_object_id 的序列化大小
		if (has_target_object_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _target_object_id);
		}

// 計算 slot_total_count 的序列化大小
		if (has_slot_total_count()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _slot_total_count);
		}

// 計算 slot_infos 的序列化大小
		if (has_slot_infos()) {
			for (SC_SMELTING_SLOT_INFO_NOTI.SlotInfo val : _slot_infos) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}

// 記憶已計算的序列化大小
		_memorizedSerializedSize = size;
		return size;
	}

	@override
	public boolean isInitialized() {
// 如果已初始化，直接返回 true
		if (_memorizedIsInitialized == 1) {
			return true;
		}

// 檢查 target_object_id 是否已設置
		if (!has_target_object_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}

// 檢查 slot_total_count 是否已設置
		if (!has_slot_total_count()) {
			_memorizedIsInitialized = -1;
			return false;
		}

// 檢查 slot_infos 中的每個 SlotInfo 是否已初始化
		if (has_slot_infos()) {
			for (SC_SMELTING_SLOT_INFO_NOTI.SlotInfo val : _slot_infos) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}

// 記憶已初始化的狀態
		_memorizedIsInitialized = 1;
		return true;
	}
	@override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
// 如果 target_object_id 存在，寫入到輸出流
		if (has_target_object_id()) {
			output.writeUInt32(1, _target_object_id);
		}

// 如果 slot_total_count 存在，寫入到輸出流
		if (has_slot_total_count()) {
			output.writeUInt32(2, _slot_total_count);
		}

// 如果 slot_infos 存在，寫入每個 SlotInfo 到輸出流
		if (has_slot_infos()) {
			for (SC_SMELTING_SLOT_INFO_NOTI.SlotInfo val : _slot_infos) {
				output.writeMessage(3, val);
			}
		}
	}

	@override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
// 創建新的 ProtoOutputStream 實例，並設置所需的緩衝大小和消息類型
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(
				getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
				message.toInt()
		);

		try {
// 將當前對象的數據寫入到創建的輸出流中
			writeTo(stream);
		} catch (java.io.IOException e) {
// 捕獲並打印異常信息
			e.printStackTrace();
		}

// 返回已寫入數據的輸出流
		return stream;
	}

		try {
// 將當前對象寫入輸出流
			writeTo(stream);
		} catch (java.io.IOException e) {
// 捕獲並打印異常
			e.printStackTrace();
		}

		return stream;
	}
@override
public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
		// 迴圈讀取輸入流，直到結束
		while (!input.isAtEnd()) {
		int tag = input.readTag(); // 讀取標籤
		switch (tag) {
		case 0x00000008: {
		// 設置 target_object_id
		set_target_object_id(input.readUInt32());
		break;
		}
		case 0x00000010: {
		// 設置 slot_total_count
		set_slot_total_count(input.readUInt32());
		break;
		}
		case 0x0000001A: {
		// 添加 slot_info
		add_slot_infos((SC_SMELTING_SLOT_INFO_NOTI.SlotInfo) input.readMessage(SC_SMELTING_SLOT_INFO_NOTI.SlotInfo.newInstance()));
		break;
		}
default: {
		// 遇到未知標籤，返回當前實例
		return this;
		}
		}
		}
		return this; // 返回當前實例
		}

@override
public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		// 創建 ProtoInputStream 實例，並設置讀取大小
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(
		bytes,
		l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
		((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE
		);

		try {
		// 從輸入流中讀取數據
		readFrom(is);

		// 檢查是否初始化，如果未初始化，返回當前實例
		if (!isInitialized())
		return this;

		// 獲取當前活動角色
		l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
		if (pc == null) {
		return this;
		}

		// TODO: 在此處插入處理代碼。由 Nature 製作。

		} catch (Exception e) {
		// 捕獲並打印異常
		e.printStackTrace();
		}
		return this; // 返回當前實例
		}

		try {
			// 從輸入流中讀取數據
			readFrom(is);

			// 如果未初始化，則返回當前實例
			if (!isInitialized()) {
				return this;
			}

			// 獲取當前活動角色
			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			// TODO: 在此處插入處理代碼。由 Nature 製作。

		} catch (Exception e) {
			// 捕獲並打印異常
			e.printStackTrace();
		}
		return this;
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		// 返回一個新的 SC_SMELTING_SLOT_INFO_NOTI 實例
		return new SC_SMELTING_SLOT_INFO_NOTI();
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
		// 返回一個新的 SC_SMELTING_SLOT_INFO_NOTI 實例
		return newInstance();
	}

	@override
	public void dispose() {
		// 重置位置信息和初始化標記
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class SlotInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static SlotInfo newInstance(){
			return new SlotInfo();
		}
		private int _slot_no;
		private int _slot_scroll_nameId;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private SlotInfo(){
		}
		public int get_slot_no(){
			return _slot_no;
		}
		public void set_slot_no(int val){
			_bit |= 0x1;
			_slot_no = val;
		}
		public boolean has_slot_no(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_slot_scroll_nameId(){
			return _slot_scroll_nameId;
		}
		public void set_slot_scroll_nameId(int val){
			_bit |= 0x2;
			_slot_scroll_nameId = val;
		}
		public boolean has_slot_scroll_nameId(){
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
			if (has_slot_no()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _slot_no);
			}
			if (has_slot_scroll_nameId()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _slot_scroll_nameId);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_slot_no()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_slot_scroll_nameId()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_slot_no()){
				output.writeUInt32(1, _slot_no);
			}
			if (has_slot_scroll_nameId()){
				output.writeUInt32(2, _slot_scroll_nameId);
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
						set_slot_no(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_slot_scroll_nameId(input.readUInt32());
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}
		@override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(
					bytes,
					l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
					((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE
			);

			try {
				// 從輸入流中讀取數據
				readFrom(is);

				// 如果未初始化，則返回當前實例
				if (!isInitialized()) {
					return this;
				}

				// 獲取當前活動角色
				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null) {
					return this;
				}

				// TODO : 在此處插入處理代碼。由 Nature 製作。

			} catch (Exception e) {
				// 捕獲並打印異常
				e.printStackTrace();
			}
			return this;
		}

		@override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			// 返回一個新的 SlotInfo 實例
			return new SlotInfo();
		}

		@override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
			// 返回一個新的 SlotInfo 實例
			return newInstance();
		}

		@override
		public void dispose() {
			// 重置位置信息和初始化標記
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
