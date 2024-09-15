package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum eNotiAnimationType{
	NO_ANIMATION(0),
	ANT_QUEEN(1),
	OMAN_MORPH(2),
	;
	private int value;
	eNotiAnimationType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eNotiAnimationType v){
		return value == v.value;
	}
	public static eNotiAnimationType fromInt(int i){
		switch(i){
		case 0:
			return NO_ANIMATION;
		case 1:
			return ANT_QUEEN;
		case 2:
			return OMAN_MORPH;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eNotiAnimationType, %d", i));
		}
	}
}
