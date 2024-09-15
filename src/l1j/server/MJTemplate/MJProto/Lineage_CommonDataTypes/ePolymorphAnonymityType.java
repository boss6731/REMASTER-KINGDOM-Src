package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum ePolymorphAnonymityType{
	eNone(0),
	eNormal(1),
	eRandom(2),
	eSpecialChar(3),
	eRandomExceptOurTeam(4),
	;
	private int value;
	ePolymorphAnonymityType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(ePolymorphAnonymityType v){
		return value == v.value;
	}
	public static ePolymorphAnonymityType fromInt(int i){
		switch(i){
		case 0:
			return eNone;
		case 1:
			return eNormal;
		case 2:
			return eRandom;
		case 3:
			return eSpecialChar;
		case 4:
			return eRandomExceptOurTeam;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments ePolymorphAnonymityType, %d", i));
		}
	}
}
