package MJNCoinSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class MJNCoinCreditLoader {
	public enum CATEGORY_TYPE{
		SELL,
		BUY,
	};
	
	private static MJNCoinCreditLoader m_instance;
	public static MJNCoinCreditLoader getInstance(){
		if(m_instance == null)
			m_instance = new MJNCoinCreditLoader();
		return m_instance;
	}

	public static void reload() {
		m_instance = new MJNCoinCreditLoader();
	}
	
	private ArrayList<MJNCoinCreditInfo> m_sellings_info;
	private ArrayList<MJNCoinCreditInfo> m_buy_info;
	
	private MJNCoinCreditLoader(){
		do_load();
	}
	
	public MJNCoinCreditInfo select_selling_info(long credit_exp) {
		return select_info(m_sellings_info, credit_exp);
	}
	
	public MJNCoinCreditInfo select_buy_info(long credit_exp) {
		return select_info(m_buy_info, credit_exp);
	}
	
	private MJNCoinCreditInfo select_info(ArrayList<MJNCoinCreditInfo> list, long credit_exp) {
		for(MJNCoinCreditInfo cInfo : list) {
			if(credit_exp >= cInfo.get_credit_exp())
				return cInfo;
		}
		return list.get(list.size() - 1);
	}
	
	private void do_load(){
		ArrayList<MJNCoinCreditInfo> sellings_info = new ArrayList<MJNCoinCreditInfo>();
		ArrayList<MJNCoinCreditInfo> buy_info = new ArrayList<MJNCoinCreditInfo>();
		Selector.exec("select * from ncoin_credit order by credit_exp desc", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJNCoinCreditInfo o = MJNCoinCreditInfo.newInstance(rs);
					if(o.get_category() == CATEGORY_TYPE.SELL) {
						sellings_info.add(o);
					}else {
						buy_info.add(o);
					}
				}
			}
		});
		m_sellings_info = sellings_info;
		m_buy_info = buy_info;
	}
	

	public static class MJNCoinCreditInfo {


		static MJNCoinCreditInfo newInstance(ResultSet rs) throws SQLException{
			return newInstance()
					.set_category(rs.getString("category").equalsIgnoreCase("銷售") ? CATEGORY_TYPE.SELL : CATEGORY_TYPE.BUY)
					.set_credit_description(rs.getString("credit_description"))
					.set_credit_exp(rs.getLong("credit_exp"))
					.set_commission(rs.getDouble("commission"));
		}

		static MJNCoinCreditInfo newInstance(){
			return new MJNCoinCreditInfo();
		}

		private CATEGORY_TYPE m_category;
		private String m_credit_description;
		private long m_credit_exp;
		private double m_commission;
		private MJNCoinCreditInfo(){}

		public MJNCoinCreditInfo set_category(CATEGORY_TYPE category){
			m_category = category;
			return this;
		}
		public MJNCoinCreditInfo set_credit_description(String credit_description){
			m_credit_description = credit_description;
			return this;
		}
		public MJNCoinCreditInfo set_credit_exp(long credit_exp){
			m_credit_exp = credit_exp;
			return this;
		}
		public MJNCoinCreditInfo set_commission(double commission){
			m_commission = commission;
			return this;
		}
		public CATEGORY_TYPE get_category(){
			return m_category;
		}
		public String get_credit_description(){
			return m_credit_description;
		}
		public long get_credit_exp(){
			return m_credit_exp;
		}
		public double get_commission(){
			return m_commission;
		}

	}

}

