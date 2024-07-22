package MJShiftObject;

public enum MJEShiftObjectType {
	NONE(-1, "NONE"),
	BATTLE(0, "BATTLE"),
	TRANSFER(1, "TRANSFER");


	// 枚舉類型的成員變量
	private int m_val; // 存儲枚舉類型的整數值
	private String m_name; // 存儲枚舉類型的名稱

	// 构造函數，初始化枚舉類型的成員變量
	MJEShiftObjectType(int val, String name) {
		this.m_val = val;
		this.m_name = name;
	}

	// 返回枚舉類型的整數值
	public int to_int() {
		return this.m_val;
	}

	// 返回枚舉類型的名稱
	public String to_name() {
		return this.m_name;
	}

	// 判斷當前枚舉類型是否與另一枚舉類型相等
	public boolean equals(MJEShiftObjectType type) {
		return (to_int() == type.to_int());
	}

	// 通過名稱獲取對應的枚舉類型
	public static MJEShiftObjectType from_name(String name) {
		for (MJEShiftObjectType type : values()) {
			if (type.m_name.equals(name)) {
				return type;
			}
		}
		return null;
	}

	// 通過整數值獲取對應的枚舉類型
	public static MJEShiftObjectType from_val(int val) {
		for (MJEShiftObjectType type : values()) {
			if (type.to_int() == val) {
				return type;
			}
		}
		return null;
	}
}


