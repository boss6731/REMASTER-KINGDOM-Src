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

	public static final int MAX_LINES = 1000; // 設定最多記錄行數
	private int m_val; // 每個枚舉常量對應的值
	private int m_append_count; // 追加行數計數
	private TextArea m_txt; // 對應的TextArea

	MJFxLogger(int val) {
		this.m_val = val;
		this.m_append_count = 0;
	}

	// 將枚舉常量轉換為整數值的方法
	public int to_int() {
		return this.m_val;
	}

	// 設定TextArea的方法
	public void set_text_area(TextArea txt) {
		this.m_txt = txt;
	}

	// 追加日誌信息的方法，並添加時間戳
	public void append_log(String message) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		String content = String.format("[%02d:%02d:%02d] %s\r\n",
				cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND),
				message);
		update_log(content);
	}

	// 更新日誌信息的方法
	public void update_log(String message) {
		if (this.m_txt == null) {
			return;
		}
		Platform.runLater(() -> {
			if (++this.m_append_count >= MAX_LINES) {
				this.m_append_count = 0;
				this.m_txt.clear(); // 超過最大行數時清空
			}
			this.m_txt.appendText(message);
		});
	}
}


