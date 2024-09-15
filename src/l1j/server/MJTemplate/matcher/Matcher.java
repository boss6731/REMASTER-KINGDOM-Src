package l1j.server.MJTemplate.matcher;

/**
 * <b>match 대리자</b>
 * <p>match될 경우 true를 반환한다.</p>
 * @author mjsoft
 * @see #matches(Object)
 **/
public interface Matcher<T> {
	
	
	
	
	/**
	 * <p>match될 경우 true를 반환한다.</p>
	 * @param t 비교 대상
	 * @return boolean
	 **/
	public boolean matches(T t);
}
