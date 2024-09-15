package l1j.server.MJTemplate.ObjectEvent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mjsoft
 * <b>이벤트를 받을 리스너의 최상위 인터페이스</b>
 * @see MJObjectEventComposite
 * @see MJObjectEventArgs
 **/
public abstract class MJObjectEventListener<T extends MJObjectEventArgs> {
	private static final AtomicInteger hashFactory = new AtomicInteger(0);
	
	private final int compositeId = hashFactory.getAndIncrement();

	int compositeId(){
		return compositeId;
	}
	
	@Override
	public int hashCode(){
		return compositeId;
	}
	
	@Override
	public String toString(){
		return new StringBuilder(32)
				.append("[")
				.append(MJObjectEventListener.class)
				.append("] compositeId : ")
				.append(compositeId())
				.toString();
	}
	
	/**
	 * <b>이벤트가 발생하면 호출된다.</b>
	 * @see MJObjectEventArgs
	 **/
	public abstract void onEvent(T args);
}
