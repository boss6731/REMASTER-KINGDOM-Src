package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum SmeltingStoneGrade{
	SG_NORMAL(1),
	SG_RARE(2),
	SG_HERO(3),
	SG_LEGENDARY(4),
	;
	private int value;
	SmeltingStoneGrade(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(SmeltingStoneGrade v){
		return value == v.value;
	}
	public static SmeltingStoneGrade fromInt(int i){
		switch(i){
		case 1:
			return SG_NORMAL;
		case 2:
			return SG_RARE;
		case 3:
			return SG_HERO;
		case 4:
			return SG_LEGENDARY;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments SmeltingStoneGrade, %d", i));
		}
	}
}
