package MJNCoinSystem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.List;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.serverpackets.S_NCoinInfo;

public class MJNCoinAdenaManager implements Runnable, TimeListener{
	public static final MJNCoinAdenaManager DEFAULT = new MJNCoinAdenaManager();
	private static final int BOARD_PAGE_SIZE = 8;
	
	private NcoinAdenaReport m_previous_report;
	private NcoinAdenaReport m_report;
	private boolean m_is_on_update;
	private MJNCoinAdenaManager(){		
	}	
	
	public void on_ncoin_adena_show_list(L1PcInstance pc, L1BoardInstance board){
		on_ncoin_adena_show_list(pc, board, 0);
	}
	
	public void on_ncoin_adena_show_list(L1PcInstance pc, L1BoardInstance board, int latest_trade_id){
		List<MJNCoinAdenaInfo> adenas_info = null;
		if(latest_trade_id <= 0){
			adenas_info = MJNCoinAdenaInfo.load_page(1, BOARD_PAGE_SIZE - 1);
		}else{
			adenas_info = MJNCoinAdenaInfo.load_page(1, BOARD_PAGE_SIZE - 1, latest_trade_id);
		}
		pc.sendPackets(S_NCoinInfo.ncoin_adena_show_list(board, m_report, adenas_info));
	}
	
	public void on_ncoin_adena_show_content(L1PcInstance pc, int trade_id){
		S_NCoinInfo nInfo = trade_id == 0 ? S_NCoinInfo.ncoin_adena_show_report(m_report) : S_NCoinInfo.ncoin_adena_show_content(trade_id);
		if(nInfo != null)
			pc.sendPackets(nInfo);
	}
	
	public void do_load(){
		m_previous_report = null;
		m_report = new NcoinAdenaReport();
		m_report.do_select();
		RealTimeClock.getInstance().addListener(this, Calendar.DAY_OF_MONTH);
		RealTimeClock.getInstance().addListener(this, Calendar.MINUTE);
	}
	
	public void complete_adena_trade(MJNCoinAdenaInfo aInfo){
		m_report.complete_adena_trade(aInfo);
		m_is_on_update = true;
	}
	@Override
	public void onMonthChanged(BaseTime time) {
	}
	@Override
	public void onDayChanged(BaseTime time) {
		//m_previous_report = m_report;
		//m_report = new NcoinAdenaReport();
		//m_report.do_insert();
		m_report.m_date = MJString.get_current_date();
		m_is_on_update = true;
	}
	@Override
	public void onHourChanged(BaseTime time) {
		
	}
	@Override
	public void onMinuteChanged(BaseTime time) {
		if(m_is_on_update){
			m_is_on_update = false;
			GeneralThreadPool.getInstance().execute(this);
		}
	}
	@Override
	public void onSecondChanged(BaseTime time) {
	}

	@Override
	public void run() {
		try{
			if(m_previous_report != null){
				m_previous_report.do_update();
				m_previous_report = null;
			}
			m_report.do_update();
		}catch(Exception e){
			e.printStackTrace();
		}
        return new L1PcInstance[0];
    }
	
	public static class NcoinAdenaReport{
		public long m_current_trade_count;
		public long m_minimum_price;
		public long m_maximum_price;
		public long m_average_price;
		public long m_total_price;
		public String m_date;
		NcoinAdenaReport(){
			m_date = MJString.get_current_date();
			m_current_trade_count =
			m_maximum_price =
			m_average_price =
			m_total_price = 0;
			m_minimum_price = Integer.MAX_VALUE;
		}

		@override
		public String toString() {
			StringBuilder sb = new StringBuilder(256);
			sb.append("[").append(m_date).append("] 累計交易信息").append("\n");
			sb.append("總交易量: ").append(String.format("%,d", m_current_trade_count)).append("\r\n");
			sb.append("總交易額: ").append(String.format("%,d", m_total_price)).append("\r\n");
			sb.append("最低交易價: ").append(String.format("%,d", m_minimum_price)).append("\r\n");
			sb.append("最高交易價: ").append(String.format("%,d", m_maximum_price)).append("\r\n");
			sb.append("平均交易價: ").append(String.format("%,d", m_average_price)).append("\\r\\n\\r\\n\\");
			sb.append("市價 ").append(String.format("%,d萬 當 最低 %,d元 起開始交易。", (MJNCoinSettings.ADENA_GENERATE_UNIT / 10000), MJNCoinSettings.ADENA_MARKET_PRICE)).append("\r\n\r\n");
			sb.append("銷售完成的項目將自動刪除。").append("\r\n");
			return sb.toString();
		}
		
		void complete_adena_trade(MJNCoinAdenaInfo aInfo){
			++m_current_trade_count;
			m_total_price += aInfo.get_ncoin_value(); 
			m_maximum_price = Math.max(m_maximum_price, aInfo.get_ncoin_per_adena());
			m_minimum_price = Math.min(m_minimum_price, aInfo.get_ncoin_per_adena());
			//m_average_price = m_total_price / m_current_trade_count;
			m_average_price = (m_maximum_price + m_minimum_price) / 2;
		}
		
		void do_select(){
			Selector.exec("select * from ncoin_trade_adena_report where temp_key=0 limit 1", new FullSelectorHandler(){
				@Override
				public void result(ResultSet rs) throws Exception {
					if(rs.next()){
						m_current_trade_count = rs.getLong("current_trade_count");
						m_minimum_price = rs.getLong("minimum_price");
						m_maximum_price = rs.getLong("maximum_price");
						m_average_price = rs.getLong("average_price");
						m_total_price = rs.getLong("total_price");
						if(m_minimum_price <= 0)
							m_minimum_price = m_maximum_price;
					}else{
						do_insert();
					}
				}
			});
		}
		
		void do_insert(){
			Updator.exec("insert into ncoin_trade_adena_report set "
					+ "temp_key=?, current_trade_count=?, minimum_price=?, maximum_price=?, average_price=?, total_price=?", new Handler(){
						@Override
						public void handle(PreparedStatement pstm) throws Exception {
							int idx = 0;
							pstm.setInt(++idx, 0);
							pstm.setLong(++idx, m_current_trade_count);
							pstm.setLong(++idx, m_minimum_price);
							pstm.setLong(++idx, m_maximum_price);
							pstm.setLong(++idx, m_average_price);
							pstm.setLong(++idx, m_total_price);
						}
					});
		}
		
		void do_update(){
			Updator.exec("update ncoin_trade_adena_report set "
					+ "current_trade_count=?, minimum_price=?, maximum_price=?, average_price=?, total_price=? where temp_key=0", new Handler(){
						@Override
						public void handle(PreparedStatement pstm) throws Exception {
							int idx = 0;
							pstm.setLong(++idx, m_current_trade_count);
							pstm.setLong(++idx, m_minimum_price);
							pstm.setLong(++idx, m_maximum_price);
							pstm.setLong(++idx, m_average_price);
							pstm.setLong(++idx, m_total_price);
						}
						
					});
		}
	}
}
