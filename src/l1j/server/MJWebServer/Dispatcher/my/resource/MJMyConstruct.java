package l1j.server.MJWebServer.Dispatcher.my.resource;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJString;

public class MJMyConstruct {
	private boolean running;
	private MJMyWebSocket webSocket;
	private MJMyAuth auth;
	private MJMyApiMonitor apiMonitor;
	MJMyConstruct(){
		running = true;
		webSocket = new MJMyWebSocket();
		auth = new MJMyAuth();
		apiMonitor = null;
	}
	
	public boolean running() {
		return running;
	}
	
	public void running(boolean running) {
		this.running = running;
	}
	
	public MJMyWebSocket webSocket(){
		return webSocket;
	}
	
	public MJMyAuth auth(){
		return auth;
	}
	
	public MJMyApiMonitor apiMonitor(){
		return apiMonitor;
	}
	
	public static class MJMyWebSocket{
		private String path;
		private int port;
		private long sessionMonitorMillis;
		private MJMyWebSocketGmTools gm;
		MJMyWebSocket(){
			path = MJString.EmptyString;
			port = -1;
			sessionMonitorMillis = 10000L;
			gm = new MJMyWebSocketGmTools();
		}
		
		public String path(){
			return path;
		}
		
		public int port(){
			return port;
		}
		
		public long sessionMonitorMillis(){
			return sessionMonitorMillis;
		}
		
		public MJMyWebSocketGmTools gm(){
			return gm;
		}
		
		public static class MJMyWebSocketGmTools{
			private long resourcePerformUpdateMillis;
			MJMyWebSocketGmTools(){
				resourcePerformUpdateMillis = 3000L;
			}
			
			public long resourcePerformUpdateMillis(){
				return resourcePerformUpdateMillis;
			}
		}
	}
	
	public static class MJMyAuth{
		private int gmAccessLevel;
		private long authExpirationMillis;
		private String authTokenKey;
		private byte[] authTokenKeyBytes;
		private String authUri;
		private String authSecretUri;
		private String authTemplatePath;
		private String nextUri;
		MJMyAuth(){
			gmAccessLevel = 1;
			authExpirationMillis = 3600000L;
			authTokenKey = "#)(*%MJSoftTokenKey@#$";
			authTokenKeyBytes = authTokenKey.getBytes(MJEncoding.UTF8);
			authTemplatePath = "./appcenter/my/login.html";
			authUri = "/my/login";
			authSecretUri = "/my/secret-login_gm";
			nextUri = "/my/index";
		}
		
		public int gmAccessLevel(){
			return gmAccessLevel;
		}
		
		public long authExpirationMillis(){
			return authExpirationMillis;
		}
		
		public byte[] authTokenKeyBytes(){
			if(authTokenKeyBytes == null){
				authTokenKeyBytes = authTokenKey.getBytes(MJEncoding.UTF8);
			}
			return authTokenKeyBytes;
		}
		
		public String authTemplatePath(){
			return authTemplatePath;
		}
		
		public String authUri(){
			return authUri;
		}
		
		public String authSecretUri() {
			return authSecretUri;
		}
		
		public String nextUri(){
			return nextUri;
		}
	}
	
	public static class MJMyApiMonitor{
		private long monitorIntervalMillis;
		private int accessPerInterval;
		private int overBanned;
		MJMyApiMonitor(){
			monitorIntervalMillis = 3000L;
			accessPerInterval = 10;
			overBanned = 30;
		}
		
		public long monitorIntervalMillis(){
			return monitorIntervalMillis;
		}
		
		public int accessPerInterval(){
			return accessPerInterval;
		}
		
		public int overBanned(){
			return overBanned;
		}
	}
}
