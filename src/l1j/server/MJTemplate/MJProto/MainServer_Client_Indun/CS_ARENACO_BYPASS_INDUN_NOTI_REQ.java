package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.IndunEx.RoomInfo.MJIndunRoomController;

// TODO : 自動生成的 PROTO 代碼。由 MJSoft 製作。
public class CS_ARENACO_BYPASS_INDUN_NOTI_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {

	public static CS_ARENACO_BYPASS_INDUN_NOTI_REQ newInstance() {
		return new CS_ARENACO_BYPASS_INDUN_NOTI_REQ();
	}

	// 用於記錄序列化大小，初始值為 -1
	private int _memorizedSerializedSize = -1;
	// 用於記錄是否初始化，初始值為 -1
	private byte _memorizedIsInitialized = -1;
	// 用於記錄位置信息
	private int _bit;

	// 私有構造函數，防止外部實例化
	private CS_ARENACO_BYPASS_INDUN_NOTI_REQ() {
	}

	@override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@override
	public int getSerializedSize() {
		int size = 0;
		// 更新記錄的序列化大小
		_memorizedSerializedSize = size;
		return size;
	}
}

	@override
	public boolean isInitialized() {
	// 檢查是否已初始化
		if (_memorizedIsInitialized == 1)
			return true;
		// 設置已初始化標記
		_memorizedIsInitialized = 1;
		return true;
	}

	@override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
	// 此方法用於將數據寫入輸出流，當前為空實現
	}

	@override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		// 創建新的輸出流，大小為序列化大小加上擴展寫入大小
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
						message.toInt());
		try {
// 將數據寫入流
			writeTo(stream);
		} catch (java.io.IOException e) {
// 捕獲並打印IO異常
			e.printStackTrace();
		}
		return stream;
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
// 從輸入流讀取數據
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
// 處理默認情況
				default: {
					return this;
				}
			}
		}
		return this;
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
// 創建新的輸入流，設置大小和擴展讀取大小
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
// 從輸入流讀取數據
			readFrom(is);

// 檢查是否已初始化
			if (!isInitialized())
				return this;

// 獲取當前活動的玩家角色
			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

// TODO : 在此處插入處理代碼。由 MJSoft 製作。
// 處理房間列表通知
			MJIndunRoomController.getInstance().onListRoomAlram(pc, this);
		} catch (Exception e) {
// 捕獲並打印異常
			e.printStackTrace();
		}
		return this;
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
// 返回一個新的 CS_ARENACO_BYPASS_INDUN_NOTI_REQ 實例
		return new CS_ARENACO_BYPASS_INDUN_NOTI_REQ();
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
// 返回一個新的 CS_ARENACO_BYPASS_INDUN_NOTI_REQ 實例
		return newInstance();
	}

	@override
	public void dispose() {
// 重置位置信息和初始化標記
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
