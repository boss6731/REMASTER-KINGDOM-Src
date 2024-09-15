package l1j.server.server.templates;

public class L1NewNpcChat {
	public L1NewNpcChat() {
	}

	private int _npcId;

	public int getNpcId() {
		return _npcId;
	}

	public void setNpcId(int i) {
		_npcId = i;
	}
	
	private int _chat_position;

	public int getChatPosition() {
		return _chat_position;
	}

	public void setChatPosition(int s) {
		_chat_position = s;
	}
	
	private String _chat_type;

	public String getChatType() {
		return _chat_type;
	}

	public void setChatType(String s) {
		_chat_type = s;
	}
	
	private boolean _isRepeat;

	public boolean isRepeat() {
		return _isRepeat;
	}

	public void setRepeat(boolean flag) {
		_isRepeat = flag;
	}
	
	private int _chatInterval;

	public int getChatInterval() {
		return _chatInterval;
	}

	public void setChatInterval(int i) {
		_chatInterval = i;
	}

	private String _ment;

	public String getMent() {
		return _ment;
	}

	public void setMent(String s) {
		_ment = s;
	}
	
	private int _ment_chance;

	public int getMentChance() {
		return _ment_chance;
	}

	public void setMentChance(int i) {
		_ment_chance = i;
	}
	
	private boolean _isShout;

	public boolean isShout() {
		return _isShout;
	}

	public void setShout(boolean flag) {
		_isShout = flag;
	}

	private boolean _isWorldChat;

	public boolean isWorldChat() {
		return _isWorldChat;
	}

	public void setWorldChat(boolean flag) {
		_isWorldChat = flag;
	}
	
	private boolean _isNormalChat;

	public boolean isNormalChat() {
		return _isNormalChat;
	}

	public void setNormalChat(boolean flag) {
		_isNormalChat = flag;
	}

}
