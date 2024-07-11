 package MJNCoinSystem;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.List;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1BoardInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.gametime.BaseTime;
 import l1j.server.server.model.gametime.RealTimeClock;
 import l1j.server.server.model.gametime.TimeListener;
 import l1j.server.server.serverpackets.S_NCoinInfo;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class MJNCoinAdenaManager implements Runnable, TimeListener {
   public static final MJNCoinAdenaManager DEFAULT = new MJNCoinAdenaManager();

   private static final int BOARD_PAGE_SIZE = 8;

   private NcoinAdenaReport m_previous_report;

   private NcoinAdenaReport m_report;
   private boolean m_is_on_update;

   public void on_ncoin_adena_show_list(L1PcInstance pc, L1BoardInstance board) {
     on_ncoin_adena_show_list(pc, board, 0);
   }

   public void on_ncoin_adena_show_list(L1PcInstance pc, L1BoardInstance board, int latest_trade_id) {
     List<MJNCoinAdenaInfo> adenas_info = null;
     if (latest_trade_id <= 0) {
       adenas_info = MJNCoinAdenaInfo.load_page(1, 7);
     } else {
       adenas_info = MJNCoinAdenaInfo.load_page(1, 7, latest_trade_id);
     }
     pc.sendPackets((ServerBasePacket)S_NCoinInfo.ncoin_adena_show_list((L1NpcInstance)board, this.m_report, adenas_info));
   }

   public void on_ncoin_adena_show_content(L1PcInstance pc, int trade_id) {
     S_NCoinInfo nInfo = (trade_id == 0) ? S_NCoinInfo.ncoin_adena_show_report(this.m_report) : S_NCoinInfo.ncoin_adena_show_content(trade_id);
     if (nInfo != null)
       pc.sendPackets((ServerBasePacket)nInfo);
   }

   public void do_load() {
     this.m_previous_report = null;
     this.m_report = new NcoinAdenaReport();
     this.m_report.do_select();
     RealTimeClock.getInstance().addListener(this, 5);
     RealTimeClock.getInstance().addListener(this, 12);
   }

   public void complete_adena_trade(MJNCoinAdenaInfo aInfo) {
     this.m_report.complete_adena_trade(aInfo);
     this.m_is_on_update = true;
   }



   public void onMonthChanged(BaseTime time) {}



   public void onDayChanged(BaseTime time) {
     this.m_report.m_date = MJString.get_current_date();
     this.m_is_on_update = true;
   }


   public void onHourChanged(BaseTime time) {}


   public void onMinuteChanged(BaseTime time) {
     if (this.m_is_on_update) {
       this.m_is_on_update = false;
       GeneralThreadPool.getInstance().execute(this);
     }
   }


   public void onSecondChanged(BaseTime time) {}


   public void run() {
     try {
       if (this.m_previous_report != null) {
         this.m_previous_report.do_update();
         this.m_previous_report = null;
       }
       this.m_report.do_update();
     } catch (Exception e) {
       e.printStackTrace();
     }
   }







   public static class NcoinAdenaReport
   {
     public String m_date = MJString.get_current_date();
     public long m_current_trade_count = this.m_maximum_price = this.m_average_price = this.m_total_price = 0L;



     public long m_minimum_price = 2147483647L; public long m_maximum_price;
     public long m_average_price;
     public long m_total_price;

     public String toString() {
       StringBuilder sb = new StringBuilder(256);
       sb.append("[").append(this.m_date).append("] 累計交易訊息").append("\n");
       sb.append("總交易量：").append(String.format("%,d", new Object[] { Long.valueOf(this.m_current_trade_count) })).append("\r\n");
       sb.append("總交易金額：").append(String.format("%,d", new Object[] { Long.valueOf(this.m_total_price) })).append("\r\n");
       sb.append("最低交易額：").append(String.format("%,d", new Object[] { Long.valueOf(this.m_minimum_price) })).append("\r\n");
       sb.append("最大交易量： ").append(String.format("%,d", new Object[] { Long.valueOf(this.m_maximum_price) })).append("\r\n");
       sb.append("平均價格：").append(String.format("%,d", new Object[] { Long.valueOf(this.m_average_price) })).append("\r\n\r\n");
       sb.append("引用").append(String.format("可以從每單位最低 %,d 韓元開始進行交易。", new Object[] { Integer.valueOf(MJNCoinSettings.ADENA_GENERATE_UNIT / 10000), Integer.valueOf(MJNCoinSettings.ADENA_MARKET_PRICE) })).append("\r\n\r\n");
       sb.append("已售出的物品會自動刪除。").append("\r\n");
       return sb.toString();
     }

     void complete_adena_trade(MJNCoinAdenaInfo aInfo) {
       this.m_current_trade_count++;
       this.m_total_price += aInfo.get_ncoin_value();
       this.m_maximum_price = Math.max(this.m_maximum_price, aInfo.get_ncoin_per_adena());
       this.m_minimum_price = Math.min(this.m_minimum_price, aInfo.get_ncoin_per_adena());

       this.m_average_price = (this.m_maximum_price + this.m_minimum_price) / 2L;
     }

     void do_select() {
       Selector.exec("select * from ncoin_trade_adena_report where temp_key=0 limit 1", (SelectorHandler)new FullSelectorHandler()
           {
             public void result(ResultSet rs) throws Exception {
               if (rs.next()) {
                 MJNCoinAdenaManager.NcoinAdenaReport.this.m_current_trade_count = rs.getLong("current_trade_count");
                 MJNCoinAdenaManager.NcoinAdenaReport.this.m_minimum_price = rs.getLong("minimum_price");
                 MJNCoinAdenaManager.NcoinAdenaReport.this.m_maximum_price = rs.getLong("maximum_price");
                 MJNCoinAdenaManager.NcoinAdenaReport.this.m_average_price = rs.getLong("average_price");
                 MJNCoinAdenaManager.NcoinAdenaReport.this.m_total_price = rs.getLong("total_price");
                 if (MJNCoinAdenaManager.NcoinAdenaReport.this.m_minimum_price <= 0L)
                   MJNCoinAdenaManager.NcoinAdenaReport.this.m_minimum_price = MJNCoinAdenaManager.NcoinAdenaReport.this.m_maximum_price;
               } else {
                 MJNCoinAdenaManager.NcoinAdenaReport.this.do_insert();
               }
             }
           });
     }

     void do_insert() {
       Updator.exec("insert into ncoin_trade_adena_report set temp_key=?, current_trade_count=?, minimum_price=?, maximum_price=?, average_price=?, total_price=?", new Handler()
           {
             public void handle(PreparedStatement pstm) throws Exception
             {
               int idx = 0;
               pstm.setInt(++idx, 0);
               pstm.setLong(++idx, MJNCoinAdenaManager.NcoinAdenaReport.this.m_current_trade_count);
               pstm.setLong(++idx, MJNCoinAdenaManager.NcoinAdenaReport.this.m_minimum_price);
               pstm.setLong(++idx, MJNCoinAdenaManager.NcoinAdenaReport.this.m_maximum_price);
               pstm.setLong(++idx, MJNCoinAdenaManager.NcoinAdenaReport.this.m_average_price);
               pstm.setLong(++idx, MJNCoinAdenaManager.NcoinAdenaReport.this.m_total_price);
             }
           });
     }

     void do_update() {
       Updator.exec("update ncoin_trade_adena_report set current_trade_count=?, minimum_price=?, maximum_price=?, average_price=?, total_price=? where temp_key=0", new Handler()
           {
             public void handle(PreparedStatement pstm) throws Exception
             {
               int idx = 0;
               pstm.setLong(++idx, MJNCoinAdenaManager.NcoinAdenaReport.this.m_current_trade_count);
               pstm.setLong(++idx, MJNCoinAdenaManager.NcoinAdenaReport.this.m_minimum_price);
               pstm.setLong(++idx, MJNCoinAdenaManager.NcoinAdenaReport.this.m_maximum_price);
               pstm.setLong(++idx, MJNCoinAdenaManager.NcoinAdenaReport.this.m_average_price);
               pstm.setLong(++idx, MJNCoinAdenaManager.NcoinAdenaReport.this.m_total_price);
             }
           });
     }
   }
 }


