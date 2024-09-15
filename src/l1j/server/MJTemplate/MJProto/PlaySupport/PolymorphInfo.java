package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum PolymorphInfo{
	POLY_INDEX(0),
	POLY_DESC(1),
	POLY_PNG(2),
	POLY_ADVANCED(3),
	POLY_POSTFIX(4),
	POLYMORPH_INFO_SIZE(5),
	;
	private int value;
	PolymorphInfo(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(PolymorphInfo v){
		return value == v.value;
	}
	public static PolymorphInfo fromInt(int i){
		switch(i){
		case 0:
			return POLY_INDEX;
		case 1:
			return POLY_DESC;
		case 2:
			return POLY_PNG;
		case 3:
			return POLY_ADVANCED;
		case 4:
			return POLY_POSTFIX;
		case 5:
			return POLYMORPH_INFO_SIZE;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments PolymorphInfo, %d", i));
		}
	}
}
