package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

// TODO : 由 MJSoft 自動生成的 PROTO 代碼。
public enum eRevengeResult {
    SUCCESS(0), // 成功
    FAIL_USER(1), // 找尋失敗
    FAIL_TIME(2), // 時間失敗
    FAIL_COST(3), // 花費失敗
    FAIL_LIST(4), // 清單失敗
    FAIL_ACTION(5), // 動作失敗
    FAIL_COUNT(6), // 計數失敗
    FAIL_SERVER(7), // 伺服器失敗
    FAIL_UPDATE_PERIOD(8), // 更新失敗
    FAIL_ALREADY_PURSUITING(9), // 已在追蹤中（連線失敗？）
    FAIL_OTHER(10); // 其他失敗

    private final int value;

    private eRevengeResult(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
	private int value;
	eRevengeResult(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eRevengeResult v){
		return value == v.value;
	}
	public static eRevengeResult fromInt(int i){
		switch(i){
		case 0:
			return SUCCESS;
		case 1:
			return FAIL_USER;
		case 2:
			return FAIL_TIME;
		case 3:
			return FAIL_COST;
		case 4:
			return FAIL_LIST;
		case 5:
			return FAIL_ACTION;
		case 6:
			return FAIL_COUNT;
		case 7:
			return FAIL_SERVER;
		case 8:
			return FAIL_UPDATE_PERIOD;
		case 9:
			return FAIL_ALREADY_PURSUITING;
		case 10:
			return FAIL_OTHER;
		default:
			throw new IllegalArgumentException(String.format("無效的參數 eRevengeResult, %d", i));
		}
	}
}
