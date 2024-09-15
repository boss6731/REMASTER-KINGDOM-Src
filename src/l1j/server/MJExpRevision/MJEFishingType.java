package l1j.server.MJExpRevision;

public enum MJEFishingType {
	GROWN_UP(0, "Grown-Up Fishing"),
	HIGH_GROWN_UP(1, "High Grown-Up Fishing"),
	ANCIENT_SILVER(2, "Ancient Silver Fishing"),
	ANCIENT_GOLD(3, "Ancient Gold Fishing");
	
	int m_val;
	String m_name;
	MJEFishingType(int val, String name){
		m_val = val;
		m_name = name;
	}
	
	public int to_val(){
		return m_val;
	}
	public String to_name(){
		return m_name;
	}
	public static MJEFishingType from_name(String name){
		for(MJEFishingType f_type : values()){
			if(f_type.to_name().equals(name))
				return f_type;
		}
		return null;
	}
	public static MJEFishingType from_int(int val){
		for(MJEFishingType f_type : values()){
			if(f_type.to_val() == val)
				return f_type;
		}
		return null;
	}
}
