package l1j.server.server.templates;

import l1j.server.server.templates.eCustomQuestType;

public enum eCustomQuestType {
	REPEAT(1),
	ONEDAY(2),
	WEEK(3);
	
	private int value;
	eCustomQuestType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eCustomQuestType v){
		return value == v.value;
	}
	public static eCustomQuestType fromInt(int i){
		switch(i){
		case 1:
			return REPEAT;
		case 2:
			return ONEDAY;
		case 3:
			return WEEK;
		default:
			return null;
		}
	}
	public static eCustomQuestType fromString(String name) {
		switch(name){
			case "重複任務":
				return REPEAT;
			case "每日任務":
				return ONEDAY;
			case "每週任務":
			return WEEK;
		default:
			return null;
		}
	}
}
