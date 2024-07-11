package l1j.server.MJTemplate.Attribute;

/**
 * <b>儲存屬性映射的值.</b>
 * @author mjsoft
 * @see MJAttrMap
 * @see MJAttrKey
 **/
public interface MJAttrValue<V> {


	
	
	/**
	 * 儲存值.
	 * @param v 價值
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
