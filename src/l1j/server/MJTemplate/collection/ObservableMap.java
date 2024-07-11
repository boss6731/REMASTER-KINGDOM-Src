package l1j.server.MJTemplate.collection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <b>map의 변경 여부를 통지할 수 있는 맵 </b>
 * @author mjsoft
 * @see MapEventListener
 * @see Map
 **/
public interface ObservableMap<K, V> extends Map<K, V>{



	
	
	/**
	 * HashMap기반의 ObservableMap을 생성한다.
	 * @param capacity 생성될 맵의 기본 크기
	 * @return {@code ObservableMap<K, V>}
	 * @see java.util.HashMap
	 **/
	public static <K, V> ObservableMap<K, V> newHashMap(int capacity){
		return new ObservableMapWrapper<>(new HashMap<>(capacity), new LinkedList<>());
	}


	
	
	/**
	 * Concurrent기반의 ObservableMap을 생성한다.
	 * @param capacity 생성될 맵의 기본 크기
	 * @return {@code ObservableMap<K, V>}
	 * @see java.util.concurrent.ConcurrentHashMap
	 **/
	public static <K, V> ObservableMap<K, V> newConcurrentMap(int capacity){
		return new ObservableMapWrapper<>(new ConcurrentHashMap<>(capacity), new CopyOnWriteArrayList<>());
	}


	
	
	/**
	 * 리스너를 등록한다.
	 * @param listener {@code MapEventListener<K, V>}
	 * @see MapEventListener
	 **/
	public void addListener(MapEventListener<K, V> listener);


	
	
	/**
	 * 리스너를 삭제한다.
	 * @param listener {@code MapEventListener<K, V>}
	 * @see MapEventListener
	 **/
	public void removeListener(MapEventListener<K, V> listener);
}
