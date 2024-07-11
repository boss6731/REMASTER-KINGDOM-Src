package l1j.server.MJTemplate.ObjectEvent;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @作者 mjsoft
 * <b>能夠管理多個事件監聽器的監聽器組合</b>
 * @參見 MJObjectEventListener
 * @參見 MJObjectEventArgs
 **/
public class MJObjectEventComposite<T extends MJObjectEventArgs> {
	private final ConcurrentHashMap<Integer, MJObjectEventListener<T>> composite; // 用於存儲監聽器的並發哈希圖

	// 構造函數
	MJObjectEventComposite() {
		this.composite = new ConcurrentHashMap<>();
	}

	/**
	 * <b>添加監聽器。</b>
	 * @param listener {@link MJObjectEventListener}
	 * @參見 MJObjectEventListener
	 **/
	void addListener(MJObjectEventListener<T> listener) {
		composite.put(listener.compositeId(), listener); // 將監聽器添加到組合中
	}

	/**
	 * <b>移除監聽器。</b>
	 * @param listener {@link MJObjectEventListener}
	 * @return int 返回集合中剩餘的監聽器數量。
	 * @參見 MJObjectEventListener
	 **/
	int removeListener(MJObjectEventListener<T> listener) {
		composite.remove(listener.compositeId()); // 從組合中移除監聽器
		return size(); // 返回剩餘的監聽器數量
	}

	/**
	 * <b>移除多個監聽器。</b>
	 * @param listeners {@code Collection<MJObjectEventListener<T>>}
	 * @return int 返回集合中剩餘的監聽器數量。
	 * @參見 MJObjectEventListener
	 **/
	int removeListener(Collection<MJObjectEventListener<T>> listeners) {
		if (listeners != null && listeners.size() > 0) {
			for (MJObjectEventListener<T> listener : listeners) {
				composite.remove(listener.compositeId()); // 從組合中移除監聽器
			}
		}
		return size(); // 返回剩餘的監聽器數量
	}

	/**
	 * <b>檢查監聽器是否存在</b>
	 * @param listener {@link MJObjectEventListener}
	 * @return boolean 如果存在則返回 true。
	 * @參見 MJObjectEventListener
	 **/
	boolean containsListener(MJObjectEventListener<T> listener) {
		return composite.contains(listener.compositeId()); // 檢查監聽器是否存在於組合中
	}

	/**
	 * <b>返回組合中剩餘監聽器的數量。</b>
	 * @return int
	 * @參見 MJObjectEventListener
	 **/
	int size() {
		return composite.size(); // 返回組合中剩餘監聽器的數量
	}

	/**
	 * <b>觸發事件。</b>
	 * @param args {@code <T extends MJObjectEventArgs>}
	 * @return int 返回組合中剩餘監聽器的數量。
	 * @參見 MJObjectEventArgs
	 **/
	int fire(T args) {
		for (Iterator<MJObjectEventListener<T>> itr = composite.values().iterator(); itr.hasNext();) {
			if (args.canceled()) {
				break; // 如果事件被取消，跳出循環
			}
			args.resetRemove(); // 重置刪除等待狀態
			MJObjectEventListener<T> listener = itr.next();
			listener.onEvent(args); // 觸發事件
			if (args.removed()) {
				itr.remove(); // 如果監聽器處於刪除等待狀態，從組合中移除
			}
		}
		return size(); // 返回組合中剩餘監聽器的數量
	}
}
