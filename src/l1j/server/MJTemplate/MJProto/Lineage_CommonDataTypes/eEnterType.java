package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum eEnterType{
	INDUN_ENTER_NORMAL(1),
	INDUN_ENTER_AUTO_MATCHING(2),
	INDUN_ENTER_QUICK_START(3),
	;
	private int value;
	eEnterType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eEnterType v){
		return value == v.value;
	}
	public static eEnterType fromInt(int i){
		switch(i){
		case 1:
			return INDUN_ENTER_NORMAL;
		case 2:
			return INDUN_ENTER_AUTO_MATCHING;
		case 3:
			return INDUN_ENTER_QUICK_START;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eEnterType, %d", i));
		}
	}
}
