package l1j.server.server.templates;

public enum WareHouseLeaveType{
	NO_WAREHOUSE(0), // 無法使用倉庫
	SPECIAL_OK(2), // 可使用特殊倉庫
	SPECIAL_INDIVIDUAL_OK(3), // 可使用個人及特殊倉庫
	OK_WAREHOUSE(7), // 可使用所有倉庫
	OK_WAREHOUSE_LIMIT(2); // 可以寄存所有物品，但若有附加附魔等級限制時
	
		private int value;
		WareHouseLeaveType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(WareHouseLeaveType v){
			return value == v.value;
		}
		public static WareHouseLeaveType fromInt(int i){
			switch(i){
			case 0:
				return NO_WAREHOUSE;
			case 2:
				return SPECIAL_OK;
			case 3:
				return SPECIAL_INDIVIDUAL_OK;
			case 7:
				return OK_WAREHOUSE;
			case 8:
				return OK_WAREHOUSE_LIMIT;
			default:
				return null;
			}
		}
		
		public static WareHouseLeaveType fromString(String i){
			switch(i){
			case "NO_WAREHOUSE(0)":
				return NO_WAREHOUSE;
			case "SPECIAL_OK(2)":
				return SPECIAL_OK;
			case "SPECIAL_INDIVIDUAL_OK(3)":
				return SPECIAL_INDIVIDUAL_OK;
			case "OK_WAREHOUSE(7)":
				return OK_WAREHOUSE;
			case "OK_WAREHOUSE_LIMIT(8)":
				return OK_WAREHOUSE_LIMIT;
			default:
				return null;
			}
		}
	}