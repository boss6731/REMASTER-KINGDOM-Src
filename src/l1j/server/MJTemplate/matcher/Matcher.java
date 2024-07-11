package l1j.server.MJTemplate.matcher;

/**
 * <b>match 代理</b>
 * <p>如果匹配則返回 true。</p>
 * @author mjsoft
 * @see #matches(Object)
 **/
public interface Matcher<T> {

	/**
	 * <p>如果匹配則返回 true。</p>
	 * @param t 比較對象
	 * @return boolean
	 **/
	public boolean matches(T t);
}
