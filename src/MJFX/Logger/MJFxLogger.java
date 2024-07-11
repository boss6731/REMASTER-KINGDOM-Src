 package MJFX.Logger;

 import java.util.Calendar;
 import java.util.TimeZone;
 import javafx.application.Platform;
 import javafx.scene.control.TextArea;

 public enum MJFxLogger
 {
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

   public static final int MAX_LINES = 1000;
   private int m_val;
   private int m_append_count;
   private TextArea m_txt;

   MJFxLogger(int val) {
     this.m_val = val;
     this.m_append_count = 0;
   }

   public int to_int() {
     return this.m_val;
   }

   public void set_text_area(TextArea txt) {
     this.m_txt = txt;
   }

   public void append_log(String message) {
     Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
     String content = String.format("[%02d:%02d:%02d]%s\r\n", new Object[] { Integer.valueOf(cal.get(11)), Integer.valueOf(cal.get(12)), Integer.valueOf(cal.get(13)), message });
     update_log(content);
   }

   public void update_log(String message) {
     if (this.m_txt == null) {
       return;
     }
     Platform.runLater(() -> {
           if (++this.m_append_count >= 1000) {
             this.m_append_count = 0;
             this.m_txt.clear();
           }
           this.m_txt.appendText(message);
         });
   }
 }


