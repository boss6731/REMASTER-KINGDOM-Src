package l1j.server.server.model.item.collection.favor.construct;

/**
 * 聖物背包狀態
 * @author LinOffice
 */
public enum L1FavorBookInventoryStatus {
	STORE(0),    // 新增
	LIST(1);     // 列表
	private int status;
	private L1FavorBookInventoryStatus(int status) {
		this.status	= status;
	}
	public int getStatus(){
		return status;
	}
}
