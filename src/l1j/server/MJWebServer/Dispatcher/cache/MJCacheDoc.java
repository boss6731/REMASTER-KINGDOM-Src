package l1j.server.MJWebServer.Dispatcher.cache;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.MJWebServer.Dispatcher.cache.life.MJLifeCacheProvider;
/**
 * @author mjsoft
 * <p></p>
 * <table BORDER CELLPADDING=3 CELLSPACING=1>
 * <tr><td>Provider</td><td>description</td></tr>
 * <tr><td>MJMonitorCacheProvider</td><td>변경을 감지하고 데이터를 로드하는 캐시</td></tr>
 * <tr><td>MJLifeCacheProvider</td><td>라이프 사이클을 가지는 캐시</td></tr>
 * </table>
 * <p>MJMonitorCacheProvider는 파일 위주의 캐시 작업에 최적화 되어 있으며</p>
 * <p>텍스트, 바이너리, json, static 네 가지의 모델을 제공한다.</p>
 * <p></p>
 * <p>MJLifeCacheProvider는 데이터베이스나 외부 서버 등의 캐시 작업에 최적화 되어있다. 현재 제공되는건 database뿐</p>
 * <p>호출 횟수와 timeout 두 가지만 현재 제공되고 있으며, 사용처가 늘어남에 따라 추가할 예정</p>
 * <p></p>
 * @see MJMonitorCacheProvider#newTextFileCacheModel(String, String, java.nio.charset.Charset)
 * @see MJMonitorCacheProvider#newBinaryFileCacheModel(String, String)
 * @see MJMonitorCacheProvider#newJsonFileCacheModel(String, String, Class, java.nio.charset.Charset)
 * @see MJMonitorCacheProvider#newStaticCacheModel(String, Object)
 * @see MJLifeCacheProvider#newCallCountDatabaseCacheModel(String, int, l1j.server.MJWebServer.Dispatcher.cache.life.MJLifeDatabaseAdapter)
 * @see MJLifeCacheProvider#newTimeDatabaseCacheModel(String, long, l1j.server.MJWebServer.Dispatcher.cache.life.MJLifeDatabaseAdapter)
 **/
public class MJCacheDoc {
	
}
