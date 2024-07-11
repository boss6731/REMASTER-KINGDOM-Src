package l1j.server.MJPushitem.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MJChaPushModel {
	private int ownerId;
	private int pushId;
	private int pushstate;
	
	public static MJChaPushModel newInstance(){
		return new MJChaPushModel();
	}
	
	public static MJChaPushModel readToDatabase(ResultSet rs) throws SQLException {
		MJChaPushModel info = newInstance();
		info.ownerId = rs.getInt("owner_id");
		info.pushId = rs.getInt("push_id");
		info.pushstate = rs.getInt("state");
		return info;
	}
	
	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getPushId() {
		return pushId;
	}

	public void setPushId(int pushId) {
		this.pushId = pushId;
	}

	public int getPushstate() {
		return pushstate;
	}

	public void setPushstate(int pushstate) {
		this.pushstate = pushstate;
	}

}
