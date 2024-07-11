package l1j.server.MJTemplate.ObjectEvent;

import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.MJTemplate.Attribute.MJAttrMap;

/**
 * @作者 mjsoft
 * <p>由於不是擁有單獨事件隊列的處理器，在添加和移除等寫操作時，可能會發生納秒級別的同步不均衡。</p>
 * <p>目前服務器模式的混亂也是原因之一。</p>
 * <p>將來如果需要納秒級別的調整，或者處理器使用頻繁，應該實現一個單獨的事件隊列來使用。</p>
 * <p>如果想消除納秒級別的同步不均衡，應該在 removeListener 中移除 attribute.remove 操作。</p>
 **/
public class MJObjectEventHandler {
	private MJAttrMap attribute; // 用於存儲屬性的映射

	// 構造函數
	MJObjectEventHandler() {
		attribute = MJAttrMap.newConcurrentHash(); // 初始化並發哈希映射
	}

	// 觸發事件的方法
	public <A extends MJObjectEventArgs> void fire(MJAttrKey<MJObjectEventComposite<A>> key, A args) {
		if (!attribute.has(key)) {
			return; // 如果屬性中沒有這個鍵，直接返回
		}
		MJObjectEventComposite<A> composite = attribute.get(key).get(); // 獲取事件組合
		if (composite.fire(args) <= 0) {
			attribute.remove(key); // 如果組合中沒有剩餘的監聽器，從屬性中移除這個鍵
		}
	}

	public class MJObjectEventHandler {

		private MJAttrMap attribute; // 用於存儲屬性的映射

		// 構造函數
		MJObjectEventHandler() {
			attribute = MJAttrMap.newConcurrentHash(); // 初始化並發哈希映射
		}

		/**
		 * 添加監聽器。
		 * @param key {@link MJAttrKey<MJObjectEventComposite<A>>}
		 * @param listener {@link MJObjectEventListener<A>}
		 */
		public <A extends MJObjectEventArgs> void addListener(MJAttrKey<MJObjectEventComposite<A>> key, MJObjectEventListener<A> listener) {
			if (!attribute.has(key)) {
				newComposite(key); // 創建新的事件組合
			}
			attribute.get(key).get().addListener(listener); // 添加監聽器到組合中
		}

		/**
		 * 移除監聽器。
		 * @param key {@link MJAttrKey<MJObjectEventComposite<A>>}
		 * @param listener {@link MJObjectEventListener<A>}
		 */
		public <A extends MJObjectEventArgs> void removeListener(MJAttrKey<MJObjectEventComposite<A>> key, MJObjectEventListener<A> listener) {
			if (!attribute.has(key)) {
				return; // 如果屬性中沒有這個鍵，直接返回
			}
			MJObjectEventComposite<A> composite = attribute.get(key).get(); // 獲取事件組合
			if (composite.removeListener(listener) <= 0) {
				attribute.remove(key); // 如果組合中沒有剩餘的監聽器，從屬性中移除這個鍵
			}
		}

		// 創建新的事件組合
		private <A extends MJObjectEventArgs> void newComposite(MJAttrKey<MJObjectEventComposite<A>> key) {
			attribute.getNotExistsNew(key).set(new MJObjectEventComposite<A>());
		}

		/**
		 * 返回事件的數量。
		 * @return int
		 */
		public int numOfEvents() {
			return attribute.numOfAttributes(); // 返回屬性中事件的數量
		}
	}
