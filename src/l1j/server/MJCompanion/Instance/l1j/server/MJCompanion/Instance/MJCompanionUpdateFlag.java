package l1j.server.MJCompanion.Instance;

public class MJCompanionUpdateFlag {
	public static final int UPDATE_NONE = 0;
	public static final int UPDATE_NAME = 1;
	public static final int UPDATE_CLASS_ID = 2;
	public static final int UPDATE_EXP = 4;
	public static final int UPDATE_HP = 8;
	public static final int UPDATE_STATS = 16;
	public static final int UPDATE_FRIEND_SHIP = 32;
	public static final int UPDATE_AC = 64;
	public static final int UPDATE_MR = 128;
	public static final int UPDATE_DELAY_REDUCE = 256;
	public static final int UPDATE_PVP_DAMAGE = 512;
	public static final int UPDATE_COMBO_TIME = 1024; // update_all 中不包含連擊時間
	public static final int UPDATE_ALL = UPDATE_NAME |
			UPDATE_CLASS_ID |
			UPDATE_EXP |
			UPDATE_HP |
			UPDATE_STATS |
			UPDATE_FRIEND_SHIP |
			UPDATE_AC |
			UPDATE_MR |
			UPDATE_DELAY_REDUCE |
			UPDATE_PVP_DAMAGE;

}
