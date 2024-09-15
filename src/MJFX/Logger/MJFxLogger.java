package MJFX.Logger;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.Calendar;
import java.util.TimeZone;

public enum MJFxLogger {
	CHAT_WORLD(0),
	CHAT_NORMAL(1),
	CHAT_PLEDGE(2),
	CHAT_PARTY(3),
	CHAT_WHISPER(4),
	CHAT_TRADE(5),
	ACCOUNT_CREATE(6),
	LOGIN_CHARACTER(7),
	GM_COMMAND(8),
	TRADE(9),
	WAREHOUSE(10),
	BOSS_TIMER(11),
	ENCHANT_MONITOR(12),
	ITEM(13),
	MINIGAME(14);
	// TODO 管理員窗口日誌行數超過一千行時刪除
	public static final int MAX_LINES = 1000;

	private int m_val;
	private int m_append_count;
	private TextArea m_txt;
	MJFxLogger(int val){
		m_val = val;
		m_append_count = 0;
	}

	public int to_int(){
		return m_val;
	}

	public void set_text_area(TextArea txt){
		m_txt = txt;
	}

	public void append_log(String message){
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		String content = String.format("[%02d:%02d:%02d]%s\r\n", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), message);
		update_log(content);
	}

	public void update_log(final String message){
		if(m_txt == null)
			return;

		Platform.runLater(()->{
			if(++m_append_count >= MAX_LINES){
				m_append_count = 0;
				m_txt.clear();
			}
			m_txt.appendText(message);
		});
	}

}