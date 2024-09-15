package l1j.server.server.templates;

public enum eShopBuyLimitType{
	NONE(0),
	CHARACTER_DAY_LIMIT(1),
	ACCOUNT_DAY_LIMIT(2),
	CHARACTER_WEEK_LIMIT(3),
	ACCOUNT_WEEK_LIMIT(4)
	;
	
	private int value;
	eShopBuyLimitType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	
	public String toStr(int i) {
		switch(i){
		case 0:
			return "NONE(0)";
		case 1:
			return "CHARACTER_DAY_LIMIT(1)";
		case 2:
			return "ACCOUNT_DAY_LIMIT(2)";
		case 3:
			return "CHARACTER_WEEK_LIMIT(3)";
		case 4:
			return "ACCOUNT_WEEK_LIMIT(4)";

		default:
			throw new IllegalArgumentException(String.format("invalid arguments eShopBuyLimitType, %d", i));
		}
	}
	
	public boolean equals(eShopBuyLimitType v){
		return value == v.value;
	}
	public static eShopBuyLimitType fromInt(int i){
		switch(i){
		case 0:
			return NONE;
		case 1:
			return CHARACTER_DAY_LIMIT;
		case 2:
			return ACCOUNT_DAY_LIMIT;
		case 3:
			return CHARACTER_WEEK_LIMIT;
		case 4:
			return ACCOUNT_WEEK_LIMIT;

		default:
			throw new IllegalArgumentException(String.format("invalid arguments eShopBuyLimitType, %d", i));
		}
	}
	
	public static eShopBuyLimitType fromString(String i){
		switch(i){
		case "NONE(0)":
			return NONE;
		case "CHARACTER_DAY_LIMIT(1)":
			return CHARACTER_DAY_LIMIT;
		case "ACCOUNT_DAY_LIMIT(2)":
			return ACCOUNT_DAY_LIMIT;
		case "CHARACTER_WEEK_LIMIT(3)":
			return CHARACTER_WEEK_LIMIT;
		case "ACCOUNT_WEEK_LIMIT(4)":
			return ACCOUNT_WEEK_LIMIT;

		default:
			throw new IllegalArgumentException(String.format("invalid arguments eShopBuyLimitType, %s", i));
		}
	}
}
