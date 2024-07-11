package l1j.server.CPMWBQSystem;

import l1j.server.CPMWBQSystem.Database.CPMWBQNpcInfoTable;
import l1j.server.CPMWBQSystem.Database.CPMWBQmapInfoTable;
import l1j.server.CPMWBQSystem.info.CPMWBQReward;

public class CPMWBQDataLoader {
	private static CPMWBQDataLoader CPMW_instance;
	public static CPMWBQDataLoader getInstance(){
		if(CPMW_instance == null)
			CPMW_instance = new CPMWBQDataLoader();
		return CPMW_instance;
	}

	private CPMWBQDataLoader(){
		CPMWBQmapInfoTable.do_load();
		CPMWBQNpcInfoTable.do_load();
		CPMWBQReward.service();
	}
}


