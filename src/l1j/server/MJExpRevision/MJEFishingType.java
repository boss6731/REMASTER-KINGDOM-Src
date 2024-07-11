package l1j.server.MJExpRevision;

public enum MJEFishingType {
	GROWN_UP(0, "成長釣魚"),
	HIGH_GROWN_UP(1, "高級成長釣魚"),
	ANCIENT_SILVER(2, "古代銀色釣魚"),
	ANCIENT_GOLD(3, "古代金色釣魚");

	int m_val;
	String m_name;

	MJEFishingType(int val, String name) {
		m_val = val;
		m_name = name;
	}

	public int to_val() {
		return m_val;
	}

	public String to_name() {
		return m_name;
	}

	public static MJEFishingType from_name(String name) {
		for (MJEFishingType f_type : values()) {
			if (f_type.to_name().equals(name))
				return f_type;
		}
		return null;
	}

	public static MJEFishingType from_int(int val) {
		for (MJEFishingType f_type : values()) {
			if (f_type.to_val() == val)
				return f_type;
		}
		return null;
	}
}
