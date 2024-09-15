package l1j.server.MJWebServer.ws;

/**
 * 當頻道結束時接收事件的監聽器
 * @作者 mjsoft
 **/
public interface MJWebSockInactiveListener {

	/**
	 * 在頻道結束時被調用。
	 * @param request {@link MJWebSockRequest}
	 **/
	public void onInactive(MJWebSockRequest request);
}
