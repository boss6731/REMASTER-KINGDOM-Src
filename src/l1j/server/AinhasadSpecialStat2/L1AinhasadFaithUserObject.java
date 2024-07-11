package l1j.server.AinhasadSpecialStat2;

import java.sql.Timestamp;

public class L1AinhasadFaithUserObject {
	private int group;
	private int index;
	private int type;
	private Timestamp endTime;
	private int charObjid;
	private AinhasadSpecialStat2Info statInfo;
	
	public L1AinhasadFaithUserObject(int charObjid, int group, int index, int type, Timestamp t) {
		this.charObjid = charObjid;
		this.group = group;
		this.index = index;
		this.type = type;
		this.endTime = t;
		
	}

	public int getCharObjId() {
		return charObjid;
	}
	public int getType() {
		return type;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int i) {
		group = i;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int i) {
		index = i;
	}
	
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp t) {
		endTime = t;
	}
	
	public AinhasadSpecialStat2Info getStatInfo() {
		return statInfo;
	}
	public void setStatInfo(AinhasadSpecialStat2Info i) {
		this.statInfo = i;
	}
	
}
