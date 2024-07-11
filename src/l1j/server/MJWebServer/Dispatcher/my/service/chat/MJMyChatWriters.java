package l1j.server.MJWebServer.Dispatcher.my.service.chat;

import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJTemplate.matcher.Matchers;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharSimpleInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatModel.MJMyChatPledgeModel;
import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatModel.MJMyChatSystemModel;
import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatModel.MJMyChatWhisperFromModel;
import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatModel.MJMyChatWhisperToModel;
import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatModel.MJMyChatWorldModel;
import l1j.server.MJWebServer.ws.MJWebSockRequest;
import l1j.server.MJWebServer.ws.protocol.MJWebSockSendModel;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

class MJMyChatWriters {
	
	static MJMyChatWriter newWorldGmWriter() {
		return new MJMyChatWorldGmWriter();
	}
	
	static MJMyChatWriter newWorldWriter(){
		return new MJMyChatWorldWriter();
	}
	
	static MJMyChatWriter newPledgeWriter(){
		return new MJMyChatPledgeWriter();
	}

	static MJMyChatWriter newWhisperWriter(){
		return new MJMyWhisperChatWriter();
	}
	
	static MJMyChatWriter newSystemWriter(){
		return new MJMySystemChatWriter();
	}
	
	private MJMyChatWriters(){}
	
	private static MJMyChatModel updateModel(MJMyChatModel model, L1PcInstance writer){
		model.nick = writer.getName();
		model.characterClass = writer.getType();
		model.gender = writer.get_sex() == 0 ? "m" : "f";
		return model;
	}
	
	private static MJWebSockSendModel<MJMyChatModel> makeSendModel(MJMyChatModel model){
		return new MJWebSockSendModel<MJMyChatModel>("onchat", model);
	}
	
	private static void sendInternal(Matcher<MJWebSockRequest> matcher, MJMyChatModel model){
		MJMyChatService
		.service()
		.exchangeHandler()
		.group()
		.write(matcher, makeSendModel(model));
	}
	
	private static boolean isOnRegisteredUsers(){
		return MJMyChatService.service().numOfRegisteredUsers() > 0;
	}
	
	private static class MJMyChatWorldWriter implements MJMyChatWriter{
		
		@Override
		public void write(L1PcInstance writer, String message, String to) {
			if(!isOnRegisteredUsers()){
				return;
			}
			
			MJMyChatWorldModel model = new MJMyChatWorldModel();
			updateModel(model, writer);
			if(writer.isGm()){
				model.nick = "******";
				model.gm = true;
			}
			model.msg = message;
			sendInternal(Matchers.all(), model);
		}
	}
	
	private static class MJMyChatWorldGmWriter implements MJMyChatWriter{
		@Override
		public void write(L1PcInstance writer, String message, String to) {
			if(!isOnRegisteredUsers()){
				return;
			}
			
			MJMyChatWorldModel model = new MJMyChatWorldModel();
			model.nick = "******";
			model.characterClass = 0;
			model.gender = "m";
			model.gm = true;
			model.msg = message;
			sendInternal(Matchers.all(), model);
		}
	}
	
	private static class MJMyChatPledgeWriter implements MJMyChatWriter{
		@Override
		public void write(L1PcInstance writer, String message, String to) {
			if(!isOnRegisteredUsers()){
				return;
			}
			
			MJMyChatPledgeModel model = new MJMyChatPledgeModel();
			updateModel(model, writer);
			model.clanName = writer.getClanname();
			model.clanRank = displayPledgeRank(writer.getClanRank());
			model.msg = message;
			
			final int clanId = writer.getClanid();
			sendInternal(new Matcher<MJWebSockRequest>(){
				@Override
				public boolean matches(MJWebSockRequest t) {
					MJMyChatUserInfo uInfo = t.attr(MJMyChatExchangeHandler.chatUserKey).get();
					if(uInfo == null){
						return false;
					}
					MJMyCharSimpleInfo cInfo = uInfo.character();
					if(cInfo.gm()){
						return true;
					}
					L1PcInstance listener = L1World.getInstance().getPlayer(cInfo.nick());
					return listener != null && listener.getClanid() == clanId;
				}
			}, model);			
		}
		
		private String displayPledgeRank(int rank){
			switch(rank){
			case L1Clan.부군주:
				return "부군주";
/*			case L1Clan.수련:
				return "수련";*/
			case L1Clan.일반:
				return "일반";
			case L1Clan.수호:
				return "수호";
			case L1Clan.군주:
				return "군주";
			case L1Clan.정예:
				return "정예";
			}
			return "일반";
		}
	}
	private static class MJMySystemChatWriter implements MJMyChatWriter{
		@Override
		public void write(L1PcInstance writer, String message, String to) {
			MJMyChatSystemModel model = new MJMyChatSystemModel();
			model.nick = "시스템";
			model.msg = message;
			model.gm = true;
			sendInternal(Matchers.all(), model);
		}
	}
	
	private static class MJMyWhisperChatWriter implements MJMyChatWriter{
		@Override
		public void write(L1PcInstance writer, String message, String to) {
			MJMyChatUserInfo writerInfo = MJMyChatService.service().unmodifiedNamesUsers().get(writer.getName());
			if(writerInfo != null){
				MJMyChatWhisperToModel model = new MJMyChatWhisperToModel();
				updateModel(model, writer);
				model.to = to;
				model.msg = message;
				writerInfo.request().write(makeSendModel(model));
			}
			
			MJMyChatUserInfo receiveInfo = MJMyChatService.service().unmodifiedNamesUsers().get(to);
			if(receiveInfo != null){
				MJMyChatWhisperFromModel model = new MJMyChatWhisperFromModel();
				updateModel(model, writer);
				model.msg = message;
				receiveInfo.request().write(makeSendModel(model));
			}	
		}
		
	}
}
