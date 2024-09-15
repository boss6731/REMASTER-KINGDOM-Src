package l1j.server.MJTemplate.Attribute;

/**
 * <b>속성 맵에 대한 값을 저장한다.</b>
 * @author mjsoft
 * @see MJAttrMap
 * @see MJAttrKey
 **/
public interface MJAttrValue<V> {


	
	
	/**
	 * 값을 저장한다.
	 * @param v 값
	 * @see MJAttrMap#attr(MJAttrKey)
	 **/
	public void set(V v);


	
	
	/**
	 * 값을 불러온다.
	 * @return v 값
	 * @see MJAttrMap#attr(MJAttrKey)
	 **/
	public V get();
}
