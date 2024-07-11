package l1j.server.MJTemplate.collection;

import java.util.Map;

/**
 * <b>ObservableMap으로 부터 이벤트를 받을 리스너</b>
 * @author mjsoft
 * @see ObservableMap
 * @see Map
 **/
public interface MapEventListener<K, V>{
	
	/**
	 * <b>MapEventListener에서 사용될 변경 아이템의 노드</b>
	 * @author mjsoft
	 * @see ObservableMap
	 * @see Map
	 * @see MapEventListener
	 **/
	public static class ChangeNode<K, V>{
		public K key;
		public V value;
		public ChangeNode(K key, V value){
			this.key = key;
			this.value = value;
		}
	}


	
	
	/**
	 * 맵에 put 이벤트가 발생
	 * @param map 이벤트가 발생한 맵 {@code Map<K, V>}
	 * @param newNode 삽입된 노드 정보  {@code ChangeNode<K, V>}
	 * @param oldNode 이전에 삽입되어 있던 노드 정보 {@code ChangeNode<K, V>}
	 * @see Map#put(Object, Object)
	 * @see ChangeNode
	 **/
	public void onPut(Map<K, V> map, ChangeNode<K, V> newNode, ChangeNode<K, V> oldNode);


	
	
	/**
	 * 맵에 putAll 이벤트가 발생
	 * @param map 이벤트가 발생한 맵 {@code Map<K, V>}
	 * @param putsMap 삽입된 맵  {@code Map<? extends K, ? extends V>}
	 * @see Map#putAll(Map)
	 **/
	public void onPutAll(Map<K, V> map, Map<? extends K, ? extends V> putsMap);


	
	
	/**
	 * 맵에 remove 이벤트가 발생
	 * @param map 이벤트가 발생한 맵 {@code Map<K, V>}
	 * @param removeNode 삭제된 노드 정보  {@code ChangeNode<K, V>}
	 * @see Map#remove(Object)
	 * @see ChangeNode
	 **/
	public void onRemove(Map<K, V> map, ChangeNode<K, V> removeNode);


	
	
	/**
	 * 맵에 clear 이벤트가 발생
	 * @param map 이벤트가 발생한 맵 {@code Map<K, V>}
	 * @see Map#clear()
	 **/
	public void onClear(Map<K, V> map);
}

