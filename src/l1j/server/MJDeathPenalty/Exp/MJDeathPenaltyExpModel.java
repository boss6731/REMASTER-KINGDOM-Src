package l1j.server.MJDeathPenalty.Exp;

import java.sql.ResultSet;
import java.sql.SQLException;
import l1j.server.MJTemplate.MJTime;

public class MJDeathPenaltyExpModel {
	private int ownerId;
	private int exp_ratio;
	private int recovery_cost;
	private long Delete_time;
	private int Id;
	private int Death_level;
		
	public MJDeathPenaltyExpModel() {}
	
	public static MJDeathPenaltyExpModel newInstance(){
		return new MJDeathPenaltyExpModel();
	}
	
	public static MJDeathPenaltyExpModel readToDatabase(ResultSet rs) throws SQLException {
		MJDeathPenaltyExpModel info = newInstance();
		info.ownerId = rs.getInt("owner_id");
		info.exp_ratio = rs.getInt("exp_ratio");
		info.recovery_cost = rs.getInt("recovery_cost");
		info.Delete_time = MJTime.convertLong(rs.getTimestamp("Delete_time")) / 1000;
		info.Id = rs.getInt("id");
		info.Death_level = rs.getInt("death_level");
		return info;
	}
	
	public int getDeathLevel() {
		return Death_level;
	}
	public void setDeathLevel(int i) {
		Death_level = i;
	}
	
	public int getId() {
		return Id;
	}
	public void setId(int i) {
		this.Id = i;
	}
	
	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getExp_ratio() {
		return exp_ratio;
	}

	public void setExp_ratio(int exp_ratio) {
		this.exp_ratio = exp_ratio;
	}

	public int getRecovery_cost() {
		return recovery_cost;
	}

	public void setRecovery_cost(int recovery_cost) {
		this.recovery_cost = recovery_cost;
	}

	public long getDelete_time() {
		return Delete_time;
	}

	public void setDelete_time(long delete_time) {
		Delete_time = delete_time;
	}

}
