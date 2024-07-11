package l1j.server.MWautoBankerProvider;
import java.sql.Timestamp;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class MWautoBankerInfo {
	static MWautoBankerInfo newInstance(int pcid){
		MWautoBankerInfo model = new MWautoBankerInfo();
		int targetId = pcid;
		L1Object bankeruser = L1World.getInstance().findObject(targetId);
		if (bankeruser instanceof L1PcInstance) {
			model.user = (L1PcInstance) bankeruser;
		}
		return model;
	}
	
	public L1PcInstance user;
	int inmountcount;
	boolean DepositStat;
	boolean PaidNcoinStat;
	String Depositor;
	Timestamp Depositdate;
	private MWautoBankerInfo(){
		inmountcount = 0;
		DepositStat = false;
		PaidNcoinStat = false;
		Depositor = null;
		Depositdate = null;
	}
}
