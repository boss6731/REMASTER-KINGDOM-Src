 package l1j.server.server.model.item.collection.favor.bean;

 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Timestamp;
 import java.text.SimpleDateFormat;


 public class L1FavorBookTypeObject
 {
   private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   private int type;
   private String desc;
   private Timestamp startTime;
   private Timestamp endTime;
   private String startTimeToString;
   private String endTimeToString;

   public L1FavorBookTypeObject(ResultSet rs) throws SQLException {
     this(rs.getInt("type"), rs.getString("desc"), rs.getTimestamp("startTime"), rs.getTimestamp("endTime"));
   }

   public L1FavorBookTypeObject(int type, String desc, Timestamp startTime, Timestamp endTime) {
     this.type = type;
     this.desc = desc;
     this.startTime = startTime;
     this.endTime = endTime;
     if (startTime != null) {
       this.startTimeToString = DATE_FORMAT.format(startTime);
     }
     if (endTime != null) {
       this.endTimeToString = DATE_FORMAT.format(endTime);
     }
   }

   public int getType() {
     return this.type;
   }
   public String getDesc() {
     return this.desc;
   }
   public Timestamp getStartTime() {
     return this.startTime;
   }
   public Timestamp getEndTime() {
     return this.endTime;
   }
   public String getStartTimeToString() {
     return this.startTimeToString;
   }
   public String getEndTimeToString() {
     return this.endTimeToString;
   }


   public String toString() {
     StringBuilder sb = new StringBuilder();
     sb.append("type : ").append(this.type).append("\r\n");
     sb.append("desc : ").append(this.desc).append("\r\n");
     if (this.startTime != null) {
       sb.append("startTime : ").append(this.startTimeToString).append("\r\n");
     }
     if (this.endTime != null) {
       sb.append("endTime : ").append(this.endTimeToString).append("\r\n");
     }
     return sb.toString();
   }
 }


