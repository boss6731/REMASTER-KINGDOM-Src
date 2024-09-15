package l1j.server.MJTemplate.MJProto.PlaySupport;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum ClassID{
	PRINCE(0),
	KNIGHT(1),
	ELF(2),
	MAGICIAN(3),
	DARKELF(4),
	DRAGON_KNIGHT(5),
	ILLUSIONIST(6),
	WARRIOR(7),
	FENCER(8),
	LANCER(9),
	UNKNOWN(10),
	;
	private int value;
	ClassID(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(ClassID v){
		return value == v.value;
	}
	public static ClassID fromInt(int i){
		switch(i){
		case 0:
			return PRINCE;
		case 1:
			return KNIGHT;
		case 2:
			return ELF;
		case 3:
			return MAGICIAN;
		case 4:
			return DARKELF;
		case 5:
			return DRAGON_KNIGHT;
		case 6:
			return ILLUSIONIST;
		case 7:
			return WARRIOR;
		case 8:
			return FENCER;
		case 9:
			return LANCER;
		case 10:
			return UNKNOWN;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments ClassID, %d", i));
		}
	}
}
