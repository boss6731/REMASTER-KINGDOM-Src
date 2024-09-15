package l1j.server.server.server.datatables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
public class ExpTable {
	private static ArrayList<ExpTable> m_experience_info;
	private static int m_max_level;
	private static long m_max_exp;
	public static void do_load(){
		ArrayList<ExpTable> experience_info = new ArrayList<ExpTable>(128);
		Selector.exec("select * from experience_info order by level asc", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					ExpTable o = newInstance(rs);
					experience_info.add(o);
				}
			}
		});
		m_experience_info = experience_info;
		ExpTable eInfo = m_experience_info.get(m_experience_info.size() - 1);
		m_max_level = eInfo.get_level();
//		m_max_exp = eInfo.get_exp();
		m_max_exp = 0xAAFFC248L;
	}

	public static int get_max_level() {
		return m_max_level;
	}
	
	public static long get_max_exp() {
		return m_max_exp;
	}

	/**
	 * 要求達到指定等級所需的累積經驗值。
	 * @param level 等級
	 * @return 所需的累積經驗值
	 **/
	public static long getExpByLevel(int level) {
		if(level > m_max_level) {
			return m_max_exp;
		}
		return m_experience_info.get(level - 1).get_exp();
	}

	/**
	 * 要求達到下一個等級所需的經驗值。
	 * @param level 當前等級
	 * @return 所需的經驗值
	 */
	public static long getNeedExpNextLevel(int level) {
		if(level >= m_max_level) {
			return m_max_exp - getExpByLevel(level);
		}
		return getExpByLevel(level + 1) - getExpByLevel(level);
	}


	/**
	 * 根據累積經驗值計算等級。
	 * @param exp 累積經驗值
	 * @return 計算出的等級
	 **/
	public static int getLevelByExp(long exp) {
		int size = m_experience_info.size();
		int lvl;
		for(lvl = 1; lvl < size; ++lvl) {
			ExpTable expInfo = m_experience_info.get(lvl);
			if(exp < expInfo.get_exp())
				break;
		}
		return Math.min(lvl, m_max_level);		
	}

	public static int getExpPercentage(int level, long exp) {
		return (int) (100.0 * ((double) (exp - getExpByLevel(level)) / (double) getNeedExpNextLevel(level)));
	}
	
	public static double getExpPercentagedouble(int level, int exp) {
		return 100.0D * ((double)(exp - getExpByLevel(level)) / (double)getNeedExpNextLevel(level));
	}

	/**
	 * 根據當前等級計算經驗值的懲罰率。
	 * @param level 當前等級
	 * @return 計算出的經驗值懲罰率
	 */
	public static double getPenaltyRate(int level) {
		return m_experience_info.get(level - 1).get_penalty();
	}
	

	private static ExpTable newInstance(ResultSet rs) throws SQLException{
		return newInstance()
				.set_level(rs.getInt("level"))
				.set_exp(Long.parseLong(rs.getString("exp").replace("0x", ""), 16))
				//.set_penalty(1.0D / (double)rs.getInt("penalty"));
				.set_penalty(rs.getDouble("penalty"));
	}

	private static ExpTable newInstance(){
		return new ExpTable();
	}

	private int m_level;
	private long m_exp;
	private double m_penalty;
	private ExpTable(){}

	public ExpTable set_level(int level){
		m_level = level;
		return this;
	}
	public ExpTable set_exp(long exp){
		m_exp = exp;
		return this;
	}
	public ExpTable set_penalty(double penalty){
		m_penalty = penalty;
		return this;
	}
	public int get_level(){
		return m_level;
	}
	public long get_exp(){
		return m_exp;
	}
	public double get_penalty(){
		return m_penalty;
	}

}

