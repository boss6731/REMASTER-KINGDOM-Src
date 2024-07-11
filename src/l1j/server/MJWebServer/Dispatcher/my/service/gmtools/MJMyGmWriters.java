package l1j.server.MJWebServer.Dispatcher.my.service.gmtools;

import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJWebServer.ws.MJWebSockRequest;
import l1j.server.MJWebServer.ws.protocol.MJWebSockSendModel;

class MJMyGmWriters {
	static MJMyGmWriter newResourceWriter(){
		return new MJMyResourceWriter();
	}
	
	static MJMyGmWriter newCharacterInWriter(){
		return new MJMyCharacterInWriter();
	}
	
	static MJMyGmWriter newCharacterOutWriter(){
		return new MJMyCharacterOutWriter();
	}
	
	private static MJWebSockSendModel<MJMyGmModel> makeSendModel(String callbackName, MJMyGmModel model){
		return new MJWebSockSendModel<MJMyGmModel>(callbackName, model);
	}
	
	private static void sendInternal(Matcher<MJWebSockRequest> matcher, String callbackName, MJMyGmModel model){
		MJMyGmService
		.service()
		.exchangeHandler()
		.group()
		.write(matcher, makeSendModel(callbackName, model));
	}
	
	private static class MJMyResourceWriter implements MJMyGmWriter{
		@Override
		public void write(Matcher<MJWebSockRequest> matcher, MJMyGmModel model) {
			sendInternal(matcher, "onresource", model);
		}
	}
	
	private static class MJMyCharacterInWriter implements MJMyGmWriter{
		@Override
		public void write(Matcher<MJWebSockRequest> matcher, MJMyGmModel model) {
			sendInternal(matcher, "oncharacter-in", model);			
		}
	}
	
	private static class MJMyCharacterOutWriter implements MJMyGmWriter{
		@Override
		public void write(Matcher<MJWebSockRequest> matcher, MJMyGmModel model) {
			sendInternal(matcher, "oncharacter-out", model);			
		}
	}
}
