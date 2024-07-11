package l1j.server.MJWebServer.ws;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJWebServer.ws.protocol.MJWebSockRecvModel;

public class MJWebSockCallbackService{
	private final ConcurrentHashMap<String, Class<? extends MJWebSockRecvModel>> models;
	public MJWebSockCallbackService(){
		models = new ConcurrentHashMap<>();
	}
	
	public void put(String callbackName, Class<? extends MJWebSockRecvModel> classOf){
		models.put(callbackName, classOf);
	}
	
	public Class<? extends MJWebSockRecvModel> get(String callbackName){
		return models.get(callbackName);
	}
	
	public Class<? extends MJWebSockRecvModel> remove(String callbackName){
		return models.remove(callbackName);
	}
	
	public void callback(MJWebSockRequest request, String callbackName, String body) throws CallModelNotFoundException, CallModelJsonConverterException{
		Class<? extends MJWebSockRecvModel> classOf = get(callbackName);
		if(classOf == null){
			throw new CallModelNotFoundException();
		}
		
		MJWebSockRecvModel model = MJJsonUtil.fromJson(body, classOf);
		if(model == null){
			throw new CallModelJsonConverterException();
		}
		try {
			model.callback(request);
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	static class CallModelNotFoundException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}
	
	static class CallModelJsonConverterException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}
	
}
