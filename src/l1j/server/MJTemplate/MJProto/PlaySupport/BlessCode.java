package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum BlessCode{
	BLESSED(0),
	NORMAL(1),
	CURSED(2),
	UNIDENTIFIED(3),
	;
	private int value;
	BlessCode(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(BlessCode v){
		return value == v.value;
	}
	public static BlessCode fromInt(int i){
		switch(i){
		case 0:
			return BLESSED;
		case 1:
			return NORMAL;
		case 2:
			return CURSED;
		case 3:
			return UNIDENTIFIED;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments BlessCode, %d", i));
		}
	}
}
