package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum eToggleInfoType{
	TOGGLE_INFO_NONE_TYPE(0),
	TOGGLE_INFO_FAITH_OF_HALPAH_USABLE_TYPE(1),
	;
	private int value;
	eToggleInfoType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eToggleInfoType v){
		return value == v.value;
	}
	public static eToggleInfoType fromInt(int i){
		switch(i){
		case 0:
			return TOGGLE_INFO_NONE_TYPE;
		case 1:
			return TOGGLE_INFO_FAITH_OF_HALPAH_USABLE_TYPE;
		default:
			throw new IllegalArgumentException(String.format("無效參數 eToggleInfoType, %d", i));
		}
	}
}
