package l1j.server.MJTemplate.MJClassesType.MainStat;

import l1j.server.server.model.Instance.L1PcInstance;

public class MJMainIntStat implements MJClassMainStat{
	@Override
	public void addStat(L1PcInstance pc, int i) {
		pc.getAbility().addAddedInt(i);
	}
}
