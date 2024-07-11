package l1j.server.revenge.model;

import java.util.Comparator;


class MJRevengeComparator implements Comparator<MJRevengeModel> {
	private boolean asc;
	MJRevengeComparator(boolean asc){
		this.asc = asc;
	}
	@Override
	public int compare(MJRevengeModel o1, MJRevengeModel o2) {		
		return compare0(o1, o2) * (asc ? 1 : -1);
	}
	
	private int compare0(MJRevengeModel o1, MJRevengeModel o2) {
		long l2 = o2.registerTimestamp();
		long l1 = o1.registerTimestamp();
		if(l1 < l2) {
			return -1;
		}else if(l2 < l1) {
			return 1;
		}
		return 0;
	}
}
