package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum eEinhasadStatType{
	BLESS(0),
	LUCKY(1),
	VITAL(2),
	ITEM_SPELL_PROB(3),
	ABSOLUTE_REGEN(4),
	POTION(5),
	ATTACK_SPELL(6),
	PVP_REDUCTION(7),
	PVE_REDUCTION(8),
	_MAX_(9),
	;
	private int value;
	eEinhasadStatType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eEinhasadStatType v){
		return value == v.value;
	}
	public static eEinhasadStatType fromInt(int i){
		switch(i){
		case 0:
			return BLESS;
		case 1:
			return LUCKY;
		case 2:
			return VITAL;
		case 3:
			return ITEM_SPELL_PROB;
		case 4:
			return ABSOLUTE_REGEN;
		case 5:
			return POTION;
		case 6:
			return ATTACK_SPELL;
		case 7:
			return PVP_REDUCTION;
		case 8:
			return PVE_REDUCTION;
		case 9:
			return _MAX_;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eEinhasadStatType, %d", i));
		}
	}
}
