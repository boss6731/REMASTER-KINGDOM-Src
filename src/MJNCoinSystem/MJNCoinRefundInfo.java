 package MJNCoinSystem;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.MJTemplate.MJObjectWrapper;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;

 public class MJNCoinRefundInfo {
   private int m_refund_id;
   private int m_character_object_id;
   private String m_account_name;
   private String m_character_name;
   private String m_refund_name;

   public static void update_is_refund(final int refund_id, final boolean is_refund, final String current_date) {
     Updator.exec("update ncoin_trade_refund set is_refund=?, complete_date=? where refund_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, is_refund ? 1 : 0);
             pstm.setString(2, current_date);
             pstm.setInt(3, refund_id);
           }
         });
   }
   private String m_bank_name; private String m_bank_account_number; private int m_ncoin_value; private boolean m_is_refund; private String m_generate_date;
   public static void do_store(final MJNCoinRefundInfo refund_info) {
     Updator.exec("insert into ncoin_trade_refund set refund_id=?, character_object_id=?, account_name=?, character_name=?, refund_name=?, bank_name=?, bank_account_number=?, ncoin_value=?, is_refund=?, generate_date=?", new Handler()
         {

           public void handle(PreparedStatement pstm) throws Exception
           {
             int idx = 0;
             pstm.setInt(++idx, refund_info.get_refund_id());
             pstm.setInt(++idx, refund_info.get_character_object_id());
             pstm.setString(++idx, refund_info.get_account_name());
             pstm.setString(++idx, refund_info.get_character_name());
             pstm.setString(++idx, refund_info.get_refund_name());
             pstm.setString(++idx, refund_info.get_bank_name());
             pstm.setString(++idx, refund_info.get_bank_account_number());
             pstm.setInt(++idx, refund_info.get_ncoin_value());
             pstm.setInt(++idx, refund_info.get_is_refund() ? 1 : 0);
             pstm.setString(++idx, refund_info.get_generate_date());
           }
         });
   }

   public static MJNCoinRefundInfo from_refund_id(final int refund_id) {
     final MJObjectWrapper<MJNCoinRefundInfo> wrapper = new MJObjectWrapper();
     Selector.exec("select * from ncoin_trade_refund where refund_id=?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, refund_id);
           }

           public void result(ResultSet rs) throws Exception {
             if (rs.next()) {
               wrapper.value = MJNCoinRefundInfo.newInstance(rs);
             }
           }
         });
     return (MJNCoinRefundInfo)wrapper.value;
   }

   public static MJNCoinRefundInfo newInstance(ResultSet rs) throws SQLException {
     return newInstance()
       .set_refund_id(rs.getInt("refund_id"))
       .set_character_object_id(rs.getInt("character_object_id"))
       .set_account_name(rs.getString("account_name"))
       .set_character_name(rs.getString("character_name"))
       .set_refund_name(rs.getString("refund_name"))
       .set_bank_name(rs.getString("bank_name"))
       .set_bank_account_number(rs.getString("bank_account_number"))
       .set_ncoin_value(rs.getInt("ncoin_value"))
       .set_is_refund((rs.getInt("is_refund") != 0))
       .set_generate_date(rs.getString("generate_date"));
   }

   public static MJNCoinRefundInfo newInstance() {
     return new MJNCoinRefundInfo();
   }














   public String toString() {
     StringBuilder sb = new StringBuilder();
     sb.append("提款ID：").append(this.m_refund_id).append("\n");
     sb.append("帳戶名稱：").append(this.m_account_name).append("\n");
     sb.append("角色ID：").append(this.m_character_name).append("\n");
     sb.append("提款人姓名：").append(this.m_refund_name).append("\n");
     sb.append("銀行名稱：").append(this.m_bank_name).append("\n");
     sb.append("帳號 ：").append(this.m_bank_account_number).append("\n");
     sb.append("N幣數量：").append(this.m_ncoin_value).append("\n");
     sb.append("註冊日期 ：").append(this.m_generate_date);
     return sb.toString();
   }

   public MJNCoinRefundInfo set_refund_id(int refund_id) {
     this.m_refund_id = refund_id;
     return this;
   }
   public MJNCoinRefundInfo set_character_object_id(int character_object_id) {
     this.m_character_object_id = character_object_id;
     return this;
   }
   public MJNCoinRefundInfo set_account_name(String account_name) {
     this.m_account_name = account_name;
     return this;
   }
   public MJNCoinRefundInfo set_character_name(String character_name) {
     this.m_character_name = character_name;
     return this;
   }
   public MJNCoinRefundInfo set_refund_name(String refund_name) {
     this.m_refund_name = refund_name;
     return this;
   }
   public MJNCoinRefundInfo set_bank_name(String bank_name) {
     this.m_bank_name = bank_name;
     return this;
   }
   public MJNCoinRefundInfo set_bank_account_number(String bank_account_number) {
     this.m_bank_account_number = bank_account_number;
     return this;
   }
   public MJNCoinRefundInfo set_ncoin_value(int ncoin_value) {
     this.m_ncoin_value = ncoin_value;
     return this;
   }
   public MJNCoinRefundInfo set_is_refund(boolean is_refund) {
     this.m_is_refund = is_refund;
     return this;
   }
   public MJNCoinRefundInfo set_generate_date(String generate_date) {
     this.m_generate_date = generate_date;
     return this;
   }
   public int get_refund_id() {
     return this.m_refund_id;
   }
   public int get_character_object_id() {
     return this.m_character_object_id;
   }
   public String get_account_name() {
     return this.m_account_name;
   }
   public String get_character_name() {
     return this.m_character_name;
   }
   public String get_refund_name() {
     return this.m_refund_name;
   }
   public String get_bank_name() {
     return this.m_bank_name;
   }
   public String get_bank_account_number() {
     return this.m_bank_account_number;
   }
   public int get_ncoin_value() {
     return this.m_ncoin_value;
   }
   public boolean get_is_refund() {
     return this.m_is_refund;
   }
   public String get_generate_date() {
     return this.m_generate_date;
   }
 }


