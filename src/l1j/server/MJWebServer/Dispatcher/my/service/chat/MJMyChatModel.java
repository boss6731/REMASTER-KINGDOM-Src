package l1j.server.MJWebServer.Dispatcher.my.service.chat;

import l1j.server.MJTemplate.MJString;

abstract class MJMyChatModel {
	static final int CHAT_TYPE_WORLD = 0;
	static final int CHAT_TYPE_PLEDGE = 1;
	static final int CHAT_TYPE_WHISPER_TO = 2;
	static final int CHAT_TYPE_WHISPER_FROM = 3;
	static final int CHAT_TYPE_SYSTEM = 4;
	
	int characterClass;
	int type;
	String gender;
	String nick;
	String msg;
	boolean gm;
	protected MJMyChatModel(int chatType){
		characterClass = 0;
		gender = MJString.EmptyString;
		nick = MJString.EmptyString;
		type = chatType;
		msg = MJString.EmptyString;
		gm = false;
	}
	
	static class MJMyChatWorldModel extends MJMyChatModel{
		MJMyChatWorldModel(){
			super(CHAT_TYPE_WORLD);
		}
	}
	
	static class MJMyChatPledgeModel extends MJMyChatModel{
		String clanName;
		String clanRank;
		MJMyChatPledgeModel(){
			super(CHAT_TYPE_PLEDGE);
		}
	}
	
	static class MJMyChatWhisperToModel extends MJMyChatModel{
		String to;
		
		MJMyChatWhisperToModel(){
			super(CHAT_TYPE_WHISPER_TO);
		}
	}
	
	static class MJMyChatWhisperFromModel extends MJMyChatModel{
		MJMyChatWhisperFromModel(){
			super(CHAT_TYPE_WHISPER_FROM);
		}
	}
	
	static class MJMyChatSystemModel extends MJMyChatModel{
		MJMyChatSystemModel() {
			super(CHAT_TYPE_SYSTEM);
		}	
	}
}
