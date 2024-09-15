package l1j.server.MJTemplate.ObjectEvent;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author mjsoft
 * <b>여러 이벤트 리스너들을 관리할 수 있는 리스너들의 컴포지트</b>
 * @see MJObjectEventListener
 * @see MJObjectEventArgs
 **/
public class MJObjectEventComposite<T extends MJObjectEventArgs> {
	private final ConcurrentHashMap<Integer, MJObjectEventListener<T>> composite;
	MJObjectEventComposite(){
		this.composite = new ConcurrentHashMap<>();
	}


	
	
	/**
	 * <b>리스너를 추가한다.</b>
	 * @param listener {@link MJObjectEventListener}
	 * @see MJObjectEventListener
	 **/
	void addListener(MJObjectEventListener<T> listener){
		composite.put(listener.compositeId(), listener);
	}


	
	
	/**
	 * <b>리스너를 제거한다.</b>
	 * @param listener {@link MJObjectEventListener}
	 * @return int 컬렉션에 남은 리스너의  수를 반환한다.
	 * @see MJObjectEventListener
	 **/
	int removeListener(MJObjectEventListener<T> listener){
		composite.remove(listener.compositeId());
		return size();
	}


	
	
	/**
	 * <b>리스너를 제거한다.</b>
	 * @param listeners {@code Collection<MJObjectEventListener<T>>}
	 * @return int 컬렉션에 남은 리스너의  수를 반환한다.
	 * @see MJObjectEventListener
	 **/
	int removeListener(Collection<MJObjectEventListener<T>> listeners){
		if(listeners != null && listeners.size() > 0){
			for(MJObjectEventListener<T> listener : listeners){
				composite.remove(listener.compositeId());
			}
		}
		return size();
	}


	
	
	/**
	 * <b>리스너가 존재하는지</b>
	 * @param listener {@link MJObjectEventListener}
	 * @return boolean 존재한다면 true.
	 * @see MJObjectEventListener
	 **/
	boolean containsListener(MJObjectEventListener<T> listener){
		return composite.contains(listener.compositeId());
	}


	
	
	/**
	 * <b>컬렉션에 남은 리스너의  수를 반환한다.</b>
	 * @return int
	 * @see MJObjectEventListener
	 **/
	int size(){
		return composite.size();
	}


	
	
	/**
	 * <b>이벤트를 발생시킨다.</b>
	 * @param args {@code <T extends MJObjectEventArgs>}
	 * @return int 컬렉션에 남은 리스너의  수를 반환한다.
	 * @see MJObjectEventArgs
	 **/
	int fire(T args){
		for(Iterator<MJObjectEventListener<T>> itr = composite.values().iterator(); itr.hasNext();){
			if(args.canceled()){
				break;
			}
			args.resetRemove();
			MJObjectEventListener<T> listener = itr.next();
			listener.onEvent(args);
			if(args.removed()){
				itr.remove();
			}
		}
		return size();
	}
}
