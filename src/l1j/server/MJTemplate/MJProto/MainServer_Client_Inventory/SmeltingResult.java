package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public enum SmeltingResult{
	SMELTING_FAIL(0),
	SMELTING_SUCCESS(1),
	TARGET_ITEM_NOT_FOUND(2),
	SCROLL_ITEM_NOT_FOUND(3),
	SLOT_INDEX_OVERFLOW(4),
	IT_WAS_EMPTY(5),
	INSERT_SMELTING_FAIL_ALREADY_EXIST(6),
	SAME_GROUP_ALREADY_EXIST(7),
	SMELTING_OBSOLETE_TIME_REMOVE(8),
	SMELTING_EJECT_PREPARE(9),
	SMELTING_EJECT_START(10),
	SMELTING_INSERT_SUCCESS(11),
	SMELTING_EJECT_SUCCESS(12),
	SMELTING_EJECT_FAIL(13),
	SLOT_COUNT_OVER(14),
	REQUEST_OPEN_SLOT_COUNT_OVER(15),
	REQUEST_OPEN_SLOT_COUNT_IS_ZERO(16),
	CANNOT_BE_SMELTED_BODYPART(17),
	NO_ENCHANT_ITEM(18),
	PET_ITEM_CANNOT_BE_SMELTED(19),
	TIME_OUT_OBJECT_CANNOT_BE_SMELTED(20),
	TEMPORARY_ITEM_CANNOT_BE_SMELTED(21),
	ENERY_LOST_ITEM_CANNOT_BE_SMELTED(22),
	SEAL_ITEM_CANNOT_BE_SMELTED(23),
	NO_SMELTING_ITEM(24),
	CURRENT_ITEM_HAS_NOT_SLOT(25),
	REQUEST_SLOT_INDEX_IS_INCORRECT(26),
	THERE_ARE_ALREADY_EXIST_SMELTING_SCROLL(27),
	SEAL_ITEM_CANNOT_BE_EJECTED(28),
	THERE_ARE_NO_SLOTS(29),
	THERE_ARE_NOT_SMELTING_SCROLL(30),
	;
	private int value;
	SmeltingResult(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(SmeltingResult v){
		return value == v.value;
	}
	public static SmeltingResult fromInt(int i){
		switch(i){
		case 0:
			return SMELTING_FAIL;
		case 1:
			return SMELTING_SUCCESS;
		case 2:
			return TARGET_ITEM_NOT_FOUND;
		case 3:
			return SCROLL_ITEM_NOT_FOUND;
		case 4:
			return SLOT_INDEX_OVERFLOW;
		case 5:
			return IT_WAS_EMPTY;
		case 6:
			return INSERT_SMELTING_FAIL_ALREADY_EXIST;
		case 7:
			return SAME_GROUP_ALREADY_EXIST;
		case 8:
			return SMELTING_OBSOLETE_TIME_REMOVE;
		case 9:
			return SMELTING_EJECT_PREPARE;
		case 10:
			return SMELTING_EJECT_START;
		case 11:
			return SMELTING_INSERT_SUCCESS;
		case 12:
			return SMELTING_EJECT_SUCCESS;
		case 13:
			return SMELTING_EJECT_FAIL;
		case 14:
			return SLOT_COUNT_OVER;
		case 15:
			return REQUEST_OPEN_SLOT_COUNT_OVER;
		case 16:
			return REQUEST_OPEN_SLOT_COUNT_IS_ZERO;
		case 17:
			return CANNOT_BE_SMELTED_BODYPART;
		case 18:
			return NO_ENCHANT_ITEM;
		case 19:
			return PET_ITEM_CANNOT_BE_SMELTED;
		case 20:
			return TIME_OUT_OBJECT_CANNOT_BE_SMELTED;
		case 21:
			return TEMPORARY_ITEM_CANNOT_BE_SMELTED;
		case 22:
			return ENERY_LOST_ITEM_CANNOT_BE_SMELTED;
		case 23:
			return SEAL_ITEM_CANNOT_BE_SMELTED;
		case 24:
			return NO_SMELTING_ITEM;
		case 25:
			return CURRENT_ITEM_HAS_NOT_SLOT;
		case 26:
			return REQUEST_SLOT_INDEX_IS_INCORRECT;
		case 27:
			return THERE_ARE_ALREADY_EXIST_SMELTING_SCROLL;
		case 28:
			return SEAL_ITEM_CANNOT_BE_EJECTED;
		case 29:
			return THERE_ARE_NO_SLOTS;
		case 30:
			return THERE_ARE_NOT_SMELTING_SCROLL;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments SmeltingResult, %d", i));
		}
	}
}
