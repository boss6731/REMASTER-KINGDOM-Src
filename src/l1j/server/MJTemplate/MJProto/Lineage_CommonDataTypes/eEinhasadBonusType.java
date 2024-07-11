package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum eEinhasadBonusType{
	BonusNone(0),
	SectionBonus(1),
	PCCafe(2),
	EinhasadFavor(3),
	;
	private int value;
	eEinhasadBonusType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eEinhasadBonusType v){
		return value == v.value;
	}
	public static eEinhasadBonusType fromInt(int i){
		switch(i){
		case 0:
			return BonusNone;
		case 1:
			return SectionBonus;
		case 2:
			return PCCafe;
		case 3:
			return EinhasadFavor;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eEinhasadBonusType, %d", i));
		}
	}
}
