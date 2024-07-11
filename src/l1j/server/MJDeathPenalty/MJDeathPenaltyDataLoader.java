package l1j.server.MJDeathPenalty;

public class MJDeathPenaltyDataLoader {
	private static MJDeathPenaltyDataLoader CPMW_instance;
	public static MJDeathPenaltyDataLoader getInstance(){
		if(CPMW_instance == null)
			CPMW_instance = new MJDeathPenaltyDataLoader();
		return CPMW_instance;
	}

	private MJDeathPenaltyDataLoader(){
		MJDeathPenaltyService.service();
	}
}


