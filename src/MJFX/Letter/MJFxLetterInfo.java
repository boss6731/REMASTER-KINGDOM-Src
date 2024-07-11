 package MJFX.Letter;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import javafx.beans.property.SimpleIntegerProperty;
 import javafx.beans.property.SimpleStringProperty;

 public class MJFxLetterInfo {
   private final SimpleIntegerProperty colNo;

   public static MJFxLetterInfo newInstance(int no, String sender, String title, String content, String date, String receiver) {
     return new MJFxLetterInfo(no, sender, title, content, date, false, receiver);
   }
   private final SimpleStringProperty colSender;
   private final SimpleStringProperty colTitle;
   private final SimpleStringProperty colContent;
   private final SimpleStringProperty colDate;
   private final SimpleStringProperty colChecked;
   private String m_gm_name;
   private boolean m_is_checked;

   public static MJFxLetterInfo newInstance(ResultSet rs, String gm_name) throws SQLException {
     return new MJFxLetterInfo(rs
         .getInt("item_object_id"), rs
         .getString("sender"), rs
         .getString("subject"), rs
         .getString("content"), rs
         .getString("date"),
         (rs.getInt("isCheck") != 0), gm_name);
   }











   private MJFxLetterInfo(int id, String sender, String title, String content, String date, boolean is_checked, String gm_name) {
     this.colNo = new SimpleIntegerProperty(id);
     this.colSender = new SimpleStringProperty(sender);
     this.colTitle = new SimpleStringProperty(title);
     this.colContent = new SimpleStringProperty(content);
     this.colDate = new SimpleStringProperty(date);
     this.colChecked = new SimpleStringProperty(is_checked ? "O" : "X");
     this.m_gm_name = gm_name;
     this.m_is_checked = is_checked;
   }

   public Integer getColNo() {
     return Integer.valueOf(this.colNo.get());
   }
   public String getColSender() {
     return this.colSender.get();
   }
   public String getColTitle() {
     return this.colTitle.get();
   }
   public String getColContent() {
     return this.colContent.get();
   }
   public String getColDate() {
     return this.colDate.get();
   }
   public String getColChecked() {
     return this.colChecked.get();
   }
   public String get_gm_name() {
     return this.m_gm_name;
   }
   public boolean is_checked() {
     return this.m_is_checked;
   }
   public void on_readed() {
     this.m_is_checked = true;
     this.colChecked.set("O");
   }

   public SimpleIntegerProperty colNoProperty() {
     return this.colNo;
   }
   public SimpleStringProperty colSenderProperty() {
     return this.colSender;
   }
   public SimpleStringProperty colTitleProperty() {
     return this.colTitle;
   }
   public SimpleStringProperty colContentProperty() {
     return this.colContent;
   }
   public SimpleStringProperty colDateProperty() {
     return this.colDate;
   }
   public SimpleStringProperty colCheckedProperty() {
     return this.colChecked;
   }
 }


