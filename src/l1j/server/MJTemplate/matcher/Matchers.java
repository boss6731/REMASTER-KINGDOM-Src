package l1j.server.MJTemplate.matcher;

import java.util.Collection;

/**
 * <b>공용으로 자주 사용되는 Matcher 팩토리</b>
 * @author mjsoft
 * @see Matcher
 * @see #all()
 * @see #invert(Matcher)
 * @see #is(Object)
 * @see #equal(Object)
 **/
public class Matchers {
	private Matchers(){}
	
	
	
	
	/**
	 * <p>무조건 true인 matcher를 반환한다.</p>
	 * @return {@code Matcher<T>}
	 * @see Matcher
	 **/
	public static <T> Matcher<T> all(){
		return new Matcher<T>(){
			@Override
			public boolean matches(T t) {
				return true;
			}
		};
	}
	
	
	
	
	/**
	 * <p>파라미터로 전해진 matcher와 반대의 경우만 true를 반환한다.</p>
	 * @param matcher {@code Matcher<T>}
	 * @return {@code Matcher<T>}
	 * @see Matcher
	 **/
	public static <T> Matcher<T> invert(final Matcher<T> matcher){
		return new InvertMatcher<>(matcher);
	}
	
	
	
	
	/**
	 * <p>파라미터로 전해진 t와 똑같은 경우만 true를 반환한다. == 연산</p>
	 * @param t 비교 대상
	 * @return {@code Matcher<T>}
	 * @see Matcher
	 **/
	public static <T> Matcher<T> is(final T t){
		return new IsMatcher<>(t);
	}
	
	
	
	
	/**
	 * <p>파라미터로 전해진 t와 똑같은 경우만 true를 반환한다. equals 연산</p>
	 * @param t 비교 대상
	 * @return {@code Matcher<T>}
	 * @see Matcher
	 **/
	public static <T> Matcher<T> equal(final T t){
		return new EqualsMatcher<>(t);
	}
	
	
	
	
	/**
	 * <p>두 matcher와의 비교로 matcher 여부를 결정짓는 matcher</p>
	 * <table BORDER CELLPADDING=3 CELLSPACING=1>
	 * <tr><td>and</td><td>두 matcher들이 true일 경우만 true를 반환</td></tr>
	 * <tr><td>or</td><td>두 matcher들 중 하나만이라도 true면 true반환</td></tr>
	 * </table><br>
	 * @param matcher1 {@link Matcher<T>}
	 * @param matcher2 {@link Matcher<T>}
	 * @param and true일 경우 and식 false일 경우 or식
	 * @return {@code Matcher<T>}
	 * @see Matcher
	 **/
	public static <T> Matcher<T> pairs(final Matcher<T> matcher1, final Matcher<T> matcher2, boolean and){
		return new PairsMatcher<T>(matcher1, matcher2, and);
	}
	
	
	
	
	/**
	 * <p>컬렉션으로 전해진 모든 matcher와의 비교로 matcher 여부를 결정짓는 matcher</p>
	 * <table BORDER CELLPADDING=3 CELLSPACING=1>
	 * <tr><td>and</td><td>모든 matcher들이 true일 경우만 true를 반환</td></tr>
	 * <tr><td>or</td><td>matcher들 중 하나만이라도 true면 true반환</td></tr>
	 * </table><br>
	 * @param matchers matcher컬렉션 {@code Collection<Matcher<T>>}
	 * @param and true일 경우 and식 false일 경우 or식
	 * @return {@code Matcher<T>}
	 * @see Matcher
	 **/
	public static <T> Matcher<T> composite(final Collection<Matcher<T>> matchers, boolean and){
		return new CompositeMatcher<>(matchers, and);
	}
	
	
	
	
	/**
	 * <p>배열로 전해진 모든 matcher와의 비교로 matcher 여부를 결정짓는 matcher</p>
	 * <table BORDER CELLPADDING=3 CELLSPACING=1>
	 * <tr><td>and</td><td>모든 matcher들이 true일 경우만 true를 반환</td></tr>
	 * <tr><td>or</td><td>matcher들 중 하나만이라도 true면 true반환</td></tr>
	 * </table><br>
	 * @param matchers matcher배열
	 * @param and true일 경우 and식 false일 경우 or식
	 * @return {@code Matcher<T>}
	 * @see Matcher
	 **/
	public static <T> Matcher<T> composite(Matcher<T>[] matchers, boolean and){
		return new CompositeArrayMatcher<>(matchers, and);
	}
	
	
	
	private static class InvertMatcher<T> implements Matcher<T>{
		private final Matcher<T> matcher;
		private InvertMatcher(Matcher<T> matcher){
			this.matcher = matcher;
		}
		
		@Override
		public boolean matches(T t) {
			return !matcher.matches(t);
		}
	}
	
	private static class IsMatcher<T> implements Matcher<T>{
		private final T t;
		private IsMatcher(T t){
			this.t = t;
		}
		
		@Override
		public boolean matches(T t) {
			return this.t == t;
		}
	}
	
	private static class EqualsMatcher<T> implements Matcher<T>{
		private final T t;
		private EqualsMatcher(T t){
			this.t = t;
		}
		
		@Override
		public boolean matches(T t) {
			return this.t.equals(t);
		}
	}
	
	private static class PairsMatcher<T> implements Matcher<T>{
		private Matcher<T> matcher1;
		private Matcher<T> matcher2;
		private boolean and;
		private PairsMatcher(Matcher<T> matcher1, Matcher<T> matcher2, boolean and){
			this.matcher1 = matcher1;
			this.matcher2 = matcher2;
			this.and = and;
		}
		@Override
		public boolean matches(T t) {
			return and ? 
					matcher1.matches(t) && matcher2.matches(t) :
					matcher1.matches(t) || matcher2.matches(t);
		}
	}
	
	private static class CompositeMatcher<T> implements Matcher<T>{
		private Collection<Matcher<T>> matchers;
		private boolean and;
		private CompositeMatcher(Collection<Matcher<T>> matchers, boolean and){
			this.matchers = matchers;
			this.and = and;
		}
		
		@Override
		public boolean matches(T t) {
			if(and){
				for(Matcher<T> matcher : matchers){
					if(!matcher.matches(t)){
						return false;
					}
				}
				return true;
			}else{
				for(Matcher<T> matcher : matchers){
					if(matcher.matches(t)){
						return true;
					}
				}
				return false;
			}
		}
	}
	
	private static class CompositeArrayMatcher<T> implements Matcher<T>{
		private Matcher<T>[] matchers;
		private boolean and;
		private CompositeArrayMatcher(Matcher<T>[] matchers, boolean and){
			this.matchers = matchers;
			this.and = and;
		}
		
		@Override
		public boolean matches(T t) {
			if(and){
				for(Matcher<T> matcher : matchers){
					if(!matcher.matches(t)){
						return false;
					}
				}
				return true;
			}else{
				for(Matcher<T> matcher : matchers){
					if(matcher.matches(t)){
						return true;
					}
				}
				return false;
			}
		}
	}
}
