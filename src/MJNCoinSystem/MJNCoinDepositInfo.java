 package MJNCoinSystem;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.List;
 import l1j.server.MJTemplate.MJObjectWrapper;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.MJWebServer.Dispatcher.my.service.MJMyServiceHelper;

 public class MJNCoinDepositInfo {
   private int m_deposit_id;
   private int m_character_object_id;
   private String m_account_name;
   private String m_character_name;

   public static void do_store(final MJNCoinDepositInfo deposit) {
     Updator.exec("insert into ncoin_trade_deposit set deposit_id=?, character_object_id=?, account_name=?, character_name=?, deposit_name=?, ncoin_value=?, generate_date=?, is_deposit=?", new Handler()
         {

           public void handle(PreparedStatement pstm) throws Exception
           {
             int idx = 0;
             pstm.setInt(++idx, deposit.get_deposit_id());
             pstm.setInt(++idx, deposit.get_character_object_id());
             pstm.setString(++idx, deposit.get_account_name());
             pstm.setString(++idx, deposit.get_character_name());
             pstm.setString(++idx, deposit.get_deposit_name());
             pstm.setInt(++idx, deposit.get_ncoin_value());
             pstm.setString(++idx, deposit.get_generate_date());
             pstm.setInt(++idx, deposit.is_deposit());
           }
         });
   }
   private String m_deposit_name; private int m_ncoin_value; private String m_generate_date; private int is_deposit;

   public static MJNCoinDepositInfo newInstance(ResultSet rs) throws SQLException {
     return newInstance()
       .set_deposit_id(rs.getInt("deposit_id"))
       .set_character_object_id(rs.getInt("character_object_id"))
       .set_account_name(rs.getString("account_name"))
       .set_character_name(rs.getString("character_name"))
       .set_deposit_name(rs.getString("deposit_name"))
       .set_ncoin_value(rs.getInt("ncoin_value"))
       .set_generate_date(rs.getString("generate_date"))
       .set_is_deposit(rs.getInt("is_deposit"));
   }


   public static void update_is_deposit(final int deposit_id, final int is_deposit, final String current_date) {
     Updator.exec("update ncoin_trade_deposit set is_deposit=?, complete_date=? where deposit_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, is_deposit);
             pstm.setString(2, current_date);
             pstm.setInt(3, deposit_id);
           }
         });
   }

   public static MJNCoinDepositInfo from_deposit_id(final int refund_id) {
     final MJObjectWrapper<MJNCoinDepositInfo> wrapper = new MJObjectWrapper();
     Selector.exec("select * from ncoin_trade_deposit where deposit_id=?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setInt(1, refund_id);
           }

           public void result(ResultSet rs) throws Exception {
             if (rs.next()) {
               wrapper.value = MJNCoinDepositInfo.newInstance(rs);
             }
           }
         });
     return (MJNCoinDepositInfo)wrapper.value;
   }

   public static int numOfDeposit() {
     final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper();
     wrapper.value = Integer.valueOf(0);
     Selector.exec("select count(deposit_id) as cnt from ncoin_trade_deposit", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             if (rs.next()) {
               wrapper.value = Integer.valueOf(rs.getInt("cnt"));
             }
           }
         });
     return ((Integer)wrapper.value).intValue();
   }

   public static int numOfDeposit(final String deposit_name) {
     final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper();
     wrapper.value = Integer.valueOf(0);
     Selector.exec("select count(deposit_id) as cnt from ncoin_trade_deposit where deposit_name like ?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             pstm.setString(1, String.format("%%%s%%", new Object[] { this.val$deposit_name }));
           }


           public void result(ResultSet rs) throws Exception {
             if (rs.next()) {
               wrapper.value = Integer.valueOf(rs.getInt("cnt"));
             }
           }
         });
     return ((Integer)wrapper.value).intValue();
   }

   public static List<MJNCoinDepositInfo> page(final int page, final int countPerPage) {
     final MJObjectWrapper<ArrayList<MJNCoinDepositInfo>> wrapper = new MJObjectWrapper();
     wrapper.value = new ArrayList(countPerPage);
     Selector.exec("select * from ncoin_trade_deposit order by deposit_id desc limit ?,?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int offset = MJMyServiceHelper.calculateOffset(page, countPerPage);
             int idx = 0;
             pstm.setInt(++idx, offset);
             pstm.setInt(++idx, countPerPage);
           }


           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               ((ArrayList<MJNCoinDepositInfo>)wrapper.value).add(MJNCoinDepositInfo.newInstance(rs));
             }
           }
         });
     return (List<MJNCoinDepositInfo>)wrapper.value;
   }

   public static List<MJNCoinDepositInfo> page(final String deposit_name, final int page, final int countPerPage) {
     final MJObjectWrapper<ArrayList<MJNCoinDepositInfo>> wrapper = new MJObjectWrapper();
     wrapper.value = new ArrayList(countPerPage);
     Selector.exec("select * from ncoin_trade_deposit where deposit_name like ? order by deposit_id desc limit ?,?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception
           {
             int offset = MJMyServiceHelper.calculateOffset(page, countPerPage);
             int idx = 0;
             pstm.setString(++idx, String.format("%%%s%%", new Object[] { this.val$deposit_name }));
             pstm.setInt(++idx, offset);
             pstm.setInt(++idx, countPerPage);
           }


           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               ((ArrayList<MJNCoinDepositInfo>)wrapper.value).add(MJNCoinDepositInfo.newInstance(rs));
             }
           }
         });
     return (List<MJNCoinDepositInfo>)wrapper.value;
   }

   public static List<MJNCoinDepositInfo> page(final int page, final int countPerPage, final int deposit_state) {
     final MJObjectWrapper<ArrayList<MJNCoinDepositInfo>> wrapper = new MJObjectWrapper();
     wrapper.value = new ArrayList(countPerPage);
     Selector.exec("select * from ncoin_trade_deposit where is_deposit=? order by deposit_id desc limit ?,?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int offset = MJMyServiceHelper.calculateOffset(page, countPerPage);
             int idx = 0;
             pstm.setInt(++idx, deposit_state);
             pstm.setInt(++idx, offset);
             pstm.setInt(++idx, countPerPage);
           }


           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               ((ArrayList<MJNCoinDepositInfo>)wrapper.value).add(MJNCoinDepositInfo.newInstance(rs));
             }
           }
         });
     return (List<MJNCoinDepositInfo>)wrapper.value;
   }

   public static List<MJNCoinDepositInfo> page(final String deposit_name, final int page, final int countPerPage, final int deposit_state) {
     final MJObjectWrapper<ArrayList<MJNCoinDepositInfo>> wrapper = new MJObjectWrapper();
     wrapper.value = new ArrayList(countPerPage);
     Selector.exec("select * from ncoin_trade_deposit where deposit_name like ? and is_deposit=? order by deposit_id desc limit ?,?", new SelectorHandler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int offset = MJMyServiceHelper.calculateOffset(page, countPerPage);
             int idx = 0;
             pstm.setString(++idx, String.format("%%%s%%", new Object[] { this.val$deposit_name }));
             pstm.setInt(++idx, deposit_state);
             pstm.setInt(++idx, offset);
             pstm.setInt(++idx, countPerPage);
           }


           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               ((ArrayList<MJNCoinDepositInfo>)wrapper.value).add(MJNCoinDepositInfo.newInstance(rs));
             }
           }
         });
     return (List<MJNCoinDepositInfo>)wrapper.value;
   }

   public static MJNCoinDepositInfo newInstance() {
     return new MJNCoinDepositInfo();
   }












   public String toString() {
     StringBuilder sb = new StringBuilder();
     sb.append("存款編號：").append(this.m_deposit_id).append("\n");
     sb.append("帳戶名稱：").append(this.m_account_name).append("\n");
     sb.append("角色ID：").append(this.m_character_name).append("\n");
     sb.append("存款人姓名：").append(this.m_deposit_name).append("\n");
     sb.append("N幣數量：").append(this.m_ncoin_value).append("\n");
     sb.append("註冊日期 ：").append(this.m_generate_date);
     return sb.toString();
   }
   public MJNCoinDepositInfo set_deposit_id(int deposit_id) {
     this.m_deposit_id = deposit_id;
     return this;
   }
   public MJNCoinDepositInfo set_character_object_id(int character_object_id) {
     this.m_character_object_id = character_object_id;
     return this;
   }
   public MJNCoinDepositInfo set_account_name(String account_name) {
     this.m_account_name = account_name;
     return this;
   }
   public MJNCoinDepositInfo set_character_name(String character_name) {
     this.m_character_name = character_name;
     return this;
   }
   public MJNCoinDepositInfo set_deposit_name(String deposit_name) {
     this.m_deposit_name = deposit_name;
     return this;
   }
   public MJNCoinDepositInfo set_ncoin_value(int ncoin_value) {
     this.m_ncoin_value = ncoin_value;
     return this;
   }
   public MJNCoinDepositInfo set_generate_date(String generate_date) {
     this.m_generate_date = generate_date;
     return this;
   }
   public MJNCoinDepositInfo set_is_deposit(int i) {
     this.is_deposit = i;
     return this;
   }
   public int get_deposit_id() {
     return this.m_deposit_id;
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
   public String get_deposit_name() {
     return this.m_deposit_name;
   }
   public int get_ncoin_value() {
     return this.m_ncoin_value;
   }
   public String get_generate_date() {
     return this.m_generate_date;
   }
   public int is_deposit() {
     return this.is_deposit;
   }
 }


