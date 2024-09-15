package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1BonusExp;
import l1j.server.server.utils.SQLUtil;

public class BonusExpTable {
	
	public static BonusExpTable _instance;
	
	public Map<Integer, L1BonusExp> _bonus_exp_list = new HashMap<Integer, L1BonusExp>();
	
	public static BonusExpTable getInstance() {
		if (_instance == null) {
			_instance = new BonusExpTable();
		}
		return _instance;
	}
	
	public static void reload() {
		BonusExpTable oldInstance = _instance;
		_instance = new BonusExpTable();
		oldInstance._bonus_exp_list.clear();
	}
	
	private BonusExpTable(){
		loadBonusExp();
	}
	
	private void loadBonusExp(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bonus_exp");
			rs = pstm.executeQuery();
			while(rs.next()) {
				L1BonusExp EXP = new L1BonusExp();
				EXP.setLevel(rs.getInt("level"));
				EXP.setExpBonus(Double.parseDouble(rs.getString("exp_probablity")));
				_bonus_exp_list.put(EXP.getLevel(), EXP);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public L1BonusExp getExpBonusLv(int lv){
		return _bonus_exp_list.get(lv);
	}
	
	public boolean isExpBonusLv(int lv){
		Set<Integer> keys = _bonus_exp_list.keySet();
		int level;
		boolean OK = false;
		for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
			level = iterator.next();
			if(level == lv){
				OK = true;
				break;
			}
		}
		return OK;
	}

}