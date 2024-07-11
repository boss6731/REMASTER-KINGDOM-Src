package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : �ڵ����� ������ PROTO �ڵ��Դϴ�. made by MJSoft.
public enum eDistributionType{
	AUTO_DISTRIBUTION(1),
	GET_PRIORITY(2),
	;
	private int value;
	eDistributionType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eDistributionType v){
		return value == v.value;
	}
	public static eDistributionType fromInt(int i){
		switch(i){
		case 1:
			return AUTO_DISTRIBUTION;
		case 2:
			return GET_PRIORITY;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eDistributionType, %d", i));
		}
	}
}
