package l1j.server.MJTemplate.Attribute;

/**
 * <b>屬性圖</b>
 * <p>可以以鍵的通用形式儲存值的映射資料結構</p>
 * @author mjsoft
 * @see MJAttrKey
 * @see MJAttrValue
 **/
public interface MJAttrMap {


	
	
	/**
	 * <p>創建基於哈希的屬性.</p>
	 * 不是線程安全的.
	 * @return {@link MJAttrMap}
	 * @see MJAttrMap
	 * @see java.util.HashMap
	 **/
	public static MJAttrMap newHash(){
		return new MJAttrHashMap();
	}


	
	
	/**
	 * 建立一個保證並發基於哈希的同步的屬性.
	 * thread 安全的
	 * @return {@link MJAttrMap}
	 * @see MJAttrMap
	 * @see java.util.concurrent.ConcurrentHashMap
	 **/
	public static MJAttrMap newConcurrentHash(){
		return new MJAttrConcurrentMap();
	}


	
	
	/**
	 * 傳回與鍵相同的通用值.
	 * <p>如果沒有值，則傳回 null</p>
	 * @param key {@link MJAttrKey}
	 * @return {@link MJAttrValue}
	 * @see MJAttrKey
	 * @see MJAttrValue
	 **/
	public <T> MJAttrValue<T> get(MJAttrKey<T> key);


	
	
	/**
	 * 傳回與鍵相同的通用值.
	 * <p>如果值不存在，則建立它並傳回它.</p>
	 * @param key {@link MJAttrKey}
	 * @return {@link MJAttrValue}
	 * @see MJAttrKey
	 * @see MJAttrValue
	 **/
	public <T> MJAttrValue<T> getNotExistsNew(MJAttrKey<T> key);


	
	
	/**
	 * 刪除具有相同通用鍵的值.
	 * @param key {@link MJAttrKey}
	 * @return {@link MJAttrValue}
	 * @see MJAttrKey
	 * @see MJAttrValue
	 **/
	public <T> MJAttrValue<T> remove(MJAttrKey<T> key);

	
	
	/**
	 * 是否存在具有相同通用鍵的值？
	 * @param key {@link MJAttrKey}
	 * @return boolean 尋找屬性的總大小 true
	 * @see MJAttrKey
	 * @see MJAttrValue
	 **/
	public boolean has(MJAttrKey<?> key);

	
	
	/**
	 * 尋找屬性的總大小.
	 * @return int
	 **/
	public int numOfAttributes();
}
