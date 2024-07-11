package l1j.server.MJWebServer.ws;

/**
 * 當通道關閉時接收事件的監聽器
 * 作者: mjsoft
 **/
public interface MJWebSockInactiveListener {

	/**
	 * 當通道關閉時調用此方法。
	 * @param request {@link MJWebSockRequest}
	 **/
	public void onInactive(MJWebSockRequest request);
}
