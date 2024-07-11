package l1j.server.MJBookQuestSystem.Reward;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1PcInstance;

public class BQSExpReward extends BQSAbstractReward{
	public static BQSExpReward newInstance(ResultSet rs) throws SQLException{
		return newInstance(rs.getInt("reward_grade"), rs.getLong("reward_exp"));
	}
	
	public static BQSExpReward newInstance(int grade, long exp){
		BQSExpReward reward = new BQSExpReward();
		reward.setGrade(grade);
		reward.setExp(exp);
		return reward;
	}
	
	private long _exp;
	private BQSExpReward(){}
	
	public void setExp(long exp){
		_exp = exp;
	}
	
	public long getExp(){
		return _exp;
	}
	
	@Override
	public void doReward(L1PcInstance pc) {
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		pc.add_exp((long) (_exp * exppenalty));
	}

}
