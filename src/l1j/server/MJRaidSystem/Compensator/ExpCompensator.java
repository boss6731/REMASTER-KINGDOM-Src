package l1j.server.MJRaidSystem.Compensator;

import l1j.server.Config;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1PcInstance;

public class ExpCompensator implements CompensatorElement{
	private long _exp;
	
	public ExpCompensator(long exp){
		_exp = exp;
	}

	@Override
	public void compensate(Object obj) {
		if(_exp <= 0)
			return;

		try{
			L1PcInstance pc = (L1PcInstance)obj;
			double penalty	= ExpTable.getPenaltyRate(pc.getLevel());
			pc.add_exp((long)(_exp * Config.ServerRates.RateXp * penalty));
			pc.save();
			pc.refresh();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
