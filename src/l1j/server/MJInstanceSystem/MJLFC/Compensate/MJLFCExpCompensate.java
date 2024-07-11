package l1j.server.MJInstanceSystem.MJLFC.Compensate;

import l1j.server.Config;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJLFCExpCompensate implements MJLFCCompensate{
	private int _partition;
	private int _level;
	private int _quantity;
	
	@Override
	public void setPartition(int i){
		_partition = i;
	}
	
	@Override
	public int getPartition(){
		return _partition;
	}
	
	@Override
	public void setIdentity(int i){
		
	}
	
	@Override
	public void setQuantity(int i){
		_quantity = i;
	}
	
	@Override
	public void setLevel(int i){
		_level = i;
	}
	
	@Override
	public void compensate(L1PcInstance pc){
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel()) + _level;
		pc.add_exp((long)((long)_quantity * Config.ServerRates.RateXp * exppenalty));
		pc.refresh();
	}
}
