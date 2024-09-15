package l1j.server.MJTemplate.Attribute;

/**
 * <b>속성 맵</b>
 * <p>키의 제네릭 형태로 밸류를 저장할 수 있는 맵 자료구조</p>
 * @author mjsoft
 * @see MJAttrKey
 * @see MJAttrValue
 **/
public interface MJAttrMap {


	
	
	/**
	 * <p>해시 기반의 어트리뷰트를 생성한다.</p>
	 * 스레드 safe하지 않다.
	 * @return {@link MJAttrMap}
	 * @see MJAttrMap
	 * @see java.util.HashMap
	 **/
	public static MJAttrMap newHash(){
		return new MJAttrHashMap();
	}


	
	
	/**
	 * concurrent해시 기반의 동기화를 보장받는 어트리뷰트를 생성한다.
	 * thread safe하다
	 * @return {@link MJAttrMap}
	 * @see MJAttrMap
	 * @see java.util.concurrent.ConcurrentHashMap
	 **/
	public static MJAttrMap newConcurrentHash(){
		return new MJAttrConcurrentMap();
	}


	
	
	/**
	 * key에 대한 동일한 제네릭을 가진 value를 반환한다.
	 * <p>value가 없으면 null반환</p>
	 * @param key {@link MJAttrKey}
	 * @return {@link MJAttrValue}
	 * @see MJAttrKey
	 * @see MJAttrValue
	 **/
	public <T> MJAttrValue<T> get(MJAttrKey<T> key);


	
	
	/**
	 * key에 대한 동일한 제네릭을 가진 value를 반환한다.
	 * <p>value가 없으면 생성 후 반환한다.</p>
	 * @param key {@link MJAttrKey}
	 * @return {@link MJAttrValue}
	 * @see MJAttrKey
	 * @see MJAttrValue
	 **/
	public <T> MJAttrValue<T> getNotExistsNew(MJAttrKey<T> key);


	
	
	/**
	 * key에 대한 동일한 제네릭을 가진 value를 삭제한다.
	 * @param key {@link MJAttrKey}
	 * @return {@link MJAttrValue}
	 * @see MJAttrKey
	 * @see MJAttrValue
	 **/
	public <T> MJAttrValue<T> remove(MJAttrKey<T> key);

	
	
	/**
	 * key에 대한 동일한 제네릭을 가진 value가 존재하는지
	 * @param key {@link MJAttrKey}
	 * @return boolean 존재한다면 true
	 * @see MJAttrKey
	 * @see MJAttrValue
	 **/
	public boolean has(MJAttrKey<?> key);

	
	
	/**
	 * 어트리뷰트 전체 크기를 구한다.
	 * @return int
	 **/
	public int numOfAttributes();
}
