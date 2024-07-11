 package MJNCoinSystem;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.List;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

 public class MJNCoinAdenaInfo {
   private int m_trade_id;
   private int m_character_object_id;
   private String m_account_name;
   private String m_character_name;
   private int m_adena_amount;
   private int m_ncoin_value;
   private double m_commission;

   public static List<MJNCoinAdenaInfo> load_page(final int page, final int page_size, final int id_offset) {
     final List<MJNCoinAdenaInfo> adenas_info = new ArrayList<>(page_size);
     Selector.exec("select * from ncoin_trade_adena where complete_date is null and trade_id<? order by trade_id desc limit ?,?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int offset = (page - 1) * page_size;
             pstm.setInt(1, id_offset);
             pstm.setInt(2, offset);
             pstm.setInt(3, page_size);
           }


           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               MJNCoinAdenaInfo aInfo = MJNCoinAdenaInfo.newInstance(rs);
               adenas_info.add(aInfo);
             }
           }
         });
     return adenas_info;
   }
   private int m_commission_ncoin; private int m_drain_ncoin; private int m_ncoin_per_adena; private int m_ncoin_per_adena_unit; private String m_generate_date; private String m_complete_date; private int m_customer_id; private String m_customer_name;
   public static List<MJNCoinAdenaInfo> load_page(final int page, final int page_size) {
     final List<MJNCoinAdenaInfo> adenas_info = new ArrayList<>(page_size);
     Selector.exec("select * from ncoin_trade_adena where complete_date is null order by trade_id desc limit ?,?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int offset = (page - 1) * page_size;
             pstm.setInt(1, offset);
             pstm.setInt(2, page_size);
           }


           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               MJNCoinAdenaInfo aInfo = MJNCoinAdenaInfo.newInstance(rs);
               adenas_info.add(aInfo);
             }
           }
         });
     return adenas_info;
   }

   public static void delete_trade_info(final MJNCoinAdenaInfo aInfo) {
     Updator.exec("delete from ncoin_trade_adena where trade_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, aInfo.get_trade_id());
           }
         });
   }

   public static void do_store(final MJNCoinAdenaInfo aInfo) {
     Updator.exec("insert into ncoin_trade_adena set trade_id=?, character_object_id=?, character_name=?, account_name=?, adena_amount=?, ncoin_value=?, commission=?, commission_ncoin=?, drain_ncoin=?, ncoin_per_adena=?, ncoin_per_adena_unit=?, generate_date=?", new Handler()
         {

           public void handle(PreparedStatement pstm) throws Exception
           {
             int idx = 0;
             pstm.setInt(++idx, aInfo.get_trade_id());
             pstm.setInt(++idx, aInfo.get_character_object_id());
             pstm.setString(++idx, aInfo.get_character_name());
             pstm.setString(++idx, aInfo.get_account_name());
             pstm.setInt(++idx, aInfo.get_adena_amount());
             pstm.setInt(++idx, aInfo.get_ncoin_value());
             pstm.setString(++idx, String.format("%.2f", new Object[] { Double.valueOf(this.val$aInfo.get_commission()) }));
             pstm.setInt(++idx, aInfo.get_commission_ncoin());
             pstm.setInt(++idx, aInfo.get_drain_ncoin());
             pstm.setInt(++idx, aInfo.get_ncoin_per_adena());
             pstm.setInt(++idx, aInfo.get_ncoin_per_adena_unit());
             pstm.setString(++idx, aInfo.get_generate_date());
           }
         });
   }

   public static void update_customer_for_gm(final MJNCoinAdenaInfo aInfo) {
     Updator.exec("update ncoin_trade_adena set commission=?, commission_ncoin=?, drain_ncoin=?, complete_date=?, customer_id=?, customer_name=? where trade_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setString(++idx, String.format("%.2f", new Object[] { Double.valueOf(this.val$aInfo.get_commission()) }));
             pstm.setInt(++idx, aInfo.get_commission_ncoin());
             pstm.setInt(++idx, aInfo.get_drain_ncoin());
             pstm.setString(++idx, aInfo.get_complete_date());
             pstm.setInt(++idx, aInfo.get_customer_id());
             pstm.setString(++idx, aInfo.get_customer_name());
             pstm.setInt(++idx, aInfo.get_trade_id());
           }
         });
   }

   public static void update_customer(MJNCoinAdenaInfo aInfo) {
     update_customer(aInfo.get_trade_id(), aInfo.get_complete_date(), aInfo.get_customer_id(), aInfo.get_customer_name());
   }

   public static void update_customer(final int trade_id, final String current_date, final int customer_id, final String customer_name) {
     Updator.exec("update ncoin_trade_adena set complete_date=?, customer_id=?, customer_name=? where trade_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setString(++idx, current_date);
             pstm.setInt(++idx, customer_id);
             pstm.setString(++idx, customer_name);
             pstm.setInt(++idx, trade_id);
           }
         });
   }

   public static MJNCoinAdenaInfo from_trade_id(final int trade_id) {
     final MJObjectWrapper<MJNCoinAdenaInfo> wrapper = new MJObjectWrapper();
     Selector.exec("select * from ncoin_trade_adena where trade_id=?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, trade_id);
           }

           public void result(ResultSet rs) throws Exception {
             if (rs.next()) {
               wrapper.value = MJNCoinAdenaInfo.newInstance(rs);
             }
           }
         });
     return (MJNCoinAdenaInfo)wrapper.value;
   }

   private static MJNCoinAdenaInfo newInstance(ResultSet rs) throws SQLException {
     return newInstance()
       .set_trade_id(rs.getInt("trade_id"))
       .set_character_object_id(rs.getInt("character_object_id"))
       .set_account_name(rs.getString("account_name"))
       .set_character_name(rs.getString("character_name"))
       .set_adena_amount(rs.getInt("adena_amount"))
       .set_ncoin_value(rs.getInt("ncoin_value"))
       .set_commission(Double.parseDouble(rs.getString("commission")))
       .set_commission_ncoin(rs.getInt("commission_ncoin"))
       .set_drain_ncoin(rs.getInt("drain_ncoin"))
       .set_ncoin_per_adena(rs.getInt("ncoin_per_adena"))
       .set_ncoin_per_adena_unit(rs.getInt("ncoin_per_adena_unit"))
       .set_generate_date(rs.getString("generate_date"))
       .set_complete_date(rs.getString("complete_date"))
       .set_customer_id(rs.getInt("customer_id"))
       .set_customer_name(rs.getString("customer_name"));
   }

   public static MJNCoinAdenaInfo newInstance() {
     return new MJNCoinAdenaInfo();
   }



















   public String toString() {
     StringBuilder sb = new StringBuilder();
     sb.append("交易號：: ").append(this.m_trade_id).append("\r\n");
     sb.append("賣方 ：").append(this.m_character_name).append("\r\n");
     sb.append("買家：").append(MJString.isNullOrEmpty(this.m_customer_name) ? "" : this.m_customer_name).append("\r\n");
     sb.append("貨幣數量：").append(String.format("%,d", new Object[] { Integer.valueOf(this.m_adena_amount) })).append("\r\n");
     sb.append("價格 ：").append(String.format("%,d", new Object[] { Integer.valueOf(this.m_ncoin_value) })).append("\r\n");
     sb.append("註冊日期 ：").append(this.m_generate_date).append("\r\n");
     sb.append("發售日期：").append(MJString.isNullOrEmpty(this.m_complete_date) ? "" : this.m_complete_date).append("\r\n");
     return sb.toString();
   }

   public String to_provider() {
     String content = toString();
     return (new StringBuilder(content.length() + 48))
       .append(content).append("\n")
       .append("N 存幣金額：").append(String.format("%,d", new Object[] { Integer.valueOf(this.m_drain_ncoin) })).append("\n")
       .append("適用費用：").append(String.format("%,d(", new Object[] { Integer.valueOf(this.m_commission_ncoin) })).append((int)(this.m_commission * 100.0D)).append("%)\n\n")
       .append("按下老虎機視窗中 F8 上方的“C”按鈕確認！")
       .toString();
   }

   public MJNCoinAdenaInfo set_trade_id(int trade_id) {
     this.m_trade_id = trade_id;
     return this;
   }
   public MJNCoinAdenaInfo set_character_object_id(int character_object_id) {
     this.m_character_object_id = character_object_id;
     return this;
   }
   public MJNCoinAdenaInfo set_account_name(String account_name) {
     this.m_account_name = account_name;
     return this;
   }
   public MJNCoinAdenaInfo set_character_name(String character_name) {
     this.m_character_name = character_name;
     return this;
   }
   public MJNCoinAdenaInfo set_adena_amount(int adena_amount) {
     this.m_adena_amount = adena_amount;
     return this;
   }
   public MJNCoinAdenaInfo set_ncoin_value(int ncoin_value) {
     this.m_ncoin_value = ncoin_value;
     return this;
   }
   public MJNCoinAdenaInfo set_commission(double commission) {
     this.m_commission = commission;
     return this;
   }
   public MJNCoinAdenaInfo set_commission_ncoin(int commission_ncoin) {
     this.m_commission_ncoin = commission_ncoin;
     return this;
   }
   public MJNCoinAdenaInfo set_drain_ncoin(int drain_ncoin) {
     this.m_drain_ncoin = drain_ncoin;
     return this;
   }
   public MJNCoinAdenaInfo set_ncoin_per_adena(int ncoin_per_adena) {
     this.m_ncoin_per_adena = ncoin_per_adena;
     return this;
   }
   public MJNCoinAdenaInfo set_ncoin_per_adena_unit(int ncoin_per_adena_unit) {
     this.m_ncoin_per_adena_unit = ncoin_per_adena_unit;
     return this;
   }
   public MJNCoinAdenaInfo set_generate_date(String generate_date) {
     this.m_generate_date = generate_date;
     return this;
   }
   public MJNCoinAdenaInfo set_complete_date(String complete_date) {
     this.m_complete_date = complete_date;
     return this;
   }
   public MJNCoinAdenaInfo set_customer_id(int customer_id) {
     this.m_customer_id = customer_id;
     return this;
   }
   public MJNCoinAdenaInfo set_customer_name(String customer_name) {
     this.m_customer_name = customer_name;
     return this;
   }

   public int get_trade_id() {
     return this.m_trade_id;
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
   public int get_adena_amount() {
     return this.m_adena_amount;
   }
   public int get_ncoin_value() {
     return this.m_ncoin_value;
   }
   public double get_commission() {
     return this.m_commission;
   }
   public int get_commission_ncoin() {
     return this.m_commission_ncoin;
   }
   public int get_drain_ncoin() {
     return this.m_drain_ncoin;
   }
   public int get_ncoin_per_adena() {
     return this.m_ncoin_per_adena;
   }
   public int get_ncoin_per_adena_unit() {
     return this.m_ncoin_per_adena_unit;
   }
   public String get_generate_date() {
     return this.m_generate_date;
   }
   public String get_complete_date() {
     return this.m_complete_date;
   }
   public int get_customer_id() {
     return this.m_customer_id;
   }
   public String get_customer_name() {
     return this.m_customer_name;
   }
 }


