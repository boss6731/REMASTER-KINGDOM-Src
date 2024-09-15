package l1j.server.server.model.item.collection.time.construct;

/**
 * Selectis 展示會增益狀態
 * @author LinOffice
 */
public enum L1TimeCollectionStatus {
	START(1),    // 開始
	DELETE(2),   // 移除
	CLOSE(3),    // 結束
	CHANGE(4);   // 變更

	private int status;

	private L1TimeCollectionStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
