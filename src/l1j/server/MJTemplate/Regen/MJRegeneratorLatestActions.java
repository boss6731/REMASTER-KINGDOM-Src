package l1j.server.MJTemplate.Regen;

public enum MJRegeneratorLatestActions {
	// index, hp tick seconds, mp tick seconds
	LATEST_ACTION_STANDING(0, 3, 15),
	LATEST_ACTION_MOVE(1, 6, 32),
	LATEST_ACTION_FIGHT(2, 12, 64),
	;
	private int _latest_action_value;
	private long _hp_generate_millis;
	private long _mp_generate_millis;
	
	MJRegeneratorLatestActions(int latest_action_value, int hp_generate_seconds, int mp_generate_seconds){
		_latest_action_value = latest_action_value;
		_hp_generate_millis = hp_generate_seconds * 1000L;
		_mp_generate_millis = mp_generate_seconds * 1000L;
	}

	public int get_latest_action_value() {
		return _latest_action_value;
	}
	public long get_hp_generate_millis() {
		return _hp_generate_millis;
	}
	public long get_mp_generate_millis() {
		return _mp_generate_millis;
	}
	
	public static MJRegeneratorLatestActions from_latest_action_value(int latest_action_value) {
		if(latest_action_value == 1)
			return MJRegeneratorLatestActions.LATEST_ACTION_MOVE;
		else if(latest_action_value == 2)
			return MJRegeneratorLatestActions.LATEST_ACTION_FIGHT;
		return MJRegeneratorLatestActions.LATEST_ACTION_STANDING;
	}
}
