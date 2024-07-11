package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum ChatType {
	CHAT_NORMAL(0),
	CHAT_WHISPER(1),
	CHAT_SHOUT(2),
	CHAT_WORLD(3),
	CHAT_PLEDGE(4),
	CHAT_HUNT_PARTY(11),
	CHAT_TRADE(12),
	CHAT_PLEDGE_PRINCE(13),
	CHAT_CHAT_PARTY(14),
	CHAT_PLEDGE_ALLIANCE(15),
	CHAT_PLEDGE_NOTICE(17),
	CHAT_CLASS(22),
	CHAT_TEAM(29),
	CHAT_ARENA_TEAM(30),
	CHAT_ARENA_OBSERVER(31),
	CHAT_ROOM_ARENA_ALL(32);

	private int value;

	ChatType(int val) {
		value = val;
	}

	public int toInt() {
		return value;
	}

	public boolean equals(ChatType v) {
		return value == v.value;
	}

	private static final java.util.HashMap<Integer, ChatType> _ChatTypes;
	static {
		_ChatTypes = new java.util.HashMap<Integer, ChatType>(16);
		for (ChatType v : ChatType.values())
			_ChatTypes.put(v.toInt(), v);
	}

	public static ChatType fromInt(int i) {
		return _ChatTypes.get(i);
	}
}
