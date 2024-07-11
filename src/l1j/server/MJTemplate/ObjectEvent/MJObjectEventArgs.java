package l1j.server.MJTemplate.ObjectEvent;

/**
 * @author mjsoft
 * <b>當物件事件監聽器接收事件時使用的參數的頂層類別</b>
 * @see MJObjectEventListener
 * @see MJObjectEventComposite
 **/
public abstract class MJObjectEventArgs {
	private boolean canceled; // 用來記錄事件是否被取消
	private boolean removed; // 用來記錄監聽器是否處於刪除等待狀態

	// 類的建構函式
	protected MJObjectEventArgs() {
		canceled = false; // 初始化取消狀態
		removed = false; // 初始化刪除狀態
	}

	/**
	 * <b>當需要停止事件時使用此方法。</b>
	 * <b>從下一個監聽器開始，事件將不會被接收。</b>
	 **/
	public void cancel() {
		canceled = true; // 設置事件取消狀態為 true
	}

	/**
	 * <b>檢查事件是否處於中止狀態</b>
	 * @return boolean 如果事件已中止，則返回 true。
	 **/
	boolean canceled() {
		return canceled; // 返回事件是否被取消
	}

	/**
	 * <b>將當前監聽器轉換為刪除等待狀態</b>
	 * <b>必須在事件處理器內使用此方法</b>
	 **/
	public void remove() {
		removed = true; // 設置監聽器的刪除等待狀態為 true
	}

	/**
	 * 重置監聽器的刪除等待狀態
	 **/
	void resetRemove() {
		removed = false; // 重置刪除等待狀態為 false
	}

	/**
	 * <b>檢查當前監聽器是否處於刪除等待狀態</b>
	 * @return boolean 如果處於刪除等待狀態，則返回 true。
	 **/
	boolean removed() {
		return removed; // 返回監聽器是否處於刪除等待狀態
	}
}
