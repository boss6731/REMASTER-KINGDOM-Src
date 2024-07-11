package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public enum eDungeonType{
	UNDEFINED(0),
	DEFENCE_TYPE(1),
	DUNGEON_TYPE(2);
	private int value;
	eDungeonType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eDungeonType v){
		return value == v.value;
	}
	public static eDungeonType fromInt(int i){
		switch(i){
		case 0:
			return UNDEFINED;
		case 1:
			return DEFENCE_TYPE;
		case 2:
			return DUNGEON_TYPE;
		default:
			return null;
		}
	}
}
