package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum eArenaMapKind{
	None(0),
	_SMALL_MAP_BEGIN(1),
	LastShowdown(1),
	_SMALL_MAP_END(1),
	_MIDDLE_MAP_BEGIN(11),
	ArenaMatch(11),
	WarSquare(12),
	WarSquare2(13),
	BattleHunter(14),
	ArenaMatch_LFC(15),
	WarSquare_LFC(16),
	WarSquare_LFC1(17),
	WarSquare_LFC2(18),
	WarSquare_LFC3(19),
	WarSquare_LFC4(20),
	WarSquare_LFC5(21),
	BreakingTower(22),
	BreakingTower_LFC(23),
	LastShowdown_LFC(24),
	_MIDDLE_MAP_END(24),
	_INSTANCE_DUNGEON_BEGIN(201),
	OrimLab_Minor(201),
	OrimLab_Normal(202),
	CROCODILE(203),
	FANTASY(204),
	SPACE(205),
	AURAKIA(206),
//	OrimLab_Major(203),
//	_INSTANCE_DUNGEON_END(203),
//	_INSTANCE_DUNGEON_END(206)
	;
	private int value;
	eArenaMapKind(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eArenaMapKind v){
		return value == v.value;
	}
	private static final java.util.HashMap<Integer, eArenaMapKind> _eArenaMapKinds;
	static{
		_eArenaMapKinds = new java.util.HashMap<Integer, eArenaMapKind>(24);
		for(eArenaMapKind v : eArenaMapKind.values())
			_eArenaMapKinds.put(v.toInt(), v);
	}
	public static eArenaMapKind fromInt(int i){
		return _eArenaMapKinds.get(i);
	}
}
