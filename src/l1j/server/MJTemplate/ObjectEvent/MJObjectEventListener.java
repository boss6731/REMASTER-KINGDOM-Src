package l1j.server.MJTemplate.ObjectEvent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @作者 mjsoft
 * <b>接收事件的監聽器的頂層接口</b>
 * @參見 MJObjectEventComposite
 * @參見 MJObjectEventArgs
 **/
public abstract class MJObjectEventListener<T extends MJObjectEventArgs> {
	private static final AtomicInteger hashFactory = new AtomicInteger(0); // 用於生成唯一ID的原子整數

	private final int compositeId = hashFactory.getAndIncrement(); // 每個監聽器的唯一ID

	// 返回監聽器的唯一ID
	int compositeId() {
		return compositeId;
	}

	@override
	public int hashCode() {
		return compositeId; // 重寫hashCode方法，返回唯一ID
	}

	@override
	public String toString() {
		return new StringBuilder(32)
				.append("[")
				.append(MJObjectEventListener.class)
				.append("] compositeId : ")
				.append(compositeId())
				.toString(); // 重寫toString方法，返回監聽器的字符串表示
	}

	/**
	 * <b>當事件發生時調用此方法。</b>
	 * @參見 MJObjectEventArgs
	 **/
	public abstract void onEvent(T args); // 抽象方法，當事件發生時調用
}
