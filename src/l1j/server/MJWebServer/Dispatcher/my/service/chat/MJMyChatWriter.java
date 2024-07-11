package l1j.server.MJWebServer.Dispatcher.my.service.chat;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJMyChatWriter {
	public void write(L1PcInstance writer, String message, String to);
}
